package org.app.adapter.secondary.kaffka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.app.domain.Currency;
import org.app.port.secondary.CurrencySnapRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

@Named
public class MongoSnapManagerImpl implements SnapManager {

    private static final Logger log = LoggerFactory.getLogger(MongoSnapManagerImpl.class);

    private KafkaConsumer<String, Currency> consumer;

    private CurrencySnapRepository currencySnapRepository;

    private final static long POOL_TIMEOUT = 1000;

    @Value("${currency.topic}")
    private String currencyTopic;

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String kafkaServers;

//    @Value("${spring.kafka.properties.security.protocol}")
//    private String kafkaSecurityProtocol;
//
//    @Value("${spring.kafka.properties.sasl.mechanism}")
//    private String kafkaSaslMechanizm;
//
//    @Value("${spring.kafka.properties.sasl.jaas.config}")
//    private String kafkaJaasConfig;

    @Inject
    public MongoSnapManagerImpl(CurrencySnapRepository currencySnapRepository) {
        this.currencySnapRepository = currencySnapRepository;
    }

    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafkaSnap" + UUID.randomUUID().toString());
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "kafkaSnapClient" + UUID.randomUUID().toString());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "org.app.domain");
//        props.put(AdminClientConfig.SECURITY_PROTOCOL_CONFIG, kafkaSecurityProtocol);
//        props.put(SaslConfigs.SASL_MECHANISM, kafkaSaslMechanizm);
//        props.put(SaslConfigs.SASL_JAAS_CONFIG, kafkaJaasConfig);
        this.consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList(currencyTopic));
    }

    @Override
    @Scheduled(fixedRateString = "${mongo.snapshot.time}")
    public void makeSnapShot() {
        log.info("Creating the snap for topic: {}", currencyTopic);

        List<Currency> snapData = new ArrayList<>();

        Set<TopicPartition> partitions = consumer.assignment();

        Map<TopicPartition, Long> currentPositionsForPartitions = getLastPositionsForPartition(consumer, partitions, currencyTopic);

        //Go to end for that moment
        consumer.seekToEnd(partitions);

        Map<TopicPartition, Long> maxPositionsForPartitions = getLastPositionsForPartition(consumer, partitions, currencyTopic);

        //Revert to previous offset
        partitions.forEach(singlePartition -> consumer.seek(singlePartition, currentPositionsForPartitions.get(singlePartition)));

        try {
            while (true) {
                ConsumerRecords<String, Currency> records = consumer.poll(POOL_TIMEOUT);

                Map<TopicPartition, Long> actualPositionsForPartitions = getLastPositionsForPartition(consumer, partitions, currencyTopic);

                if (records.isEmpty())
                    break;

                records.forEach(rec -> snapData.add(rec.value()));

                //Check if all messages from all partitions were read for moment of the method call (maxPositionsForPartitions)
                //to prevent from infinite loop in case when constantly there are new messages on topic
                boolean isAllRead = true;

                for (Map.Entry<TopicPartition, Long> partitionEntry : actualPositionsForPartitions.entrySet()) {
                    TopicPartition partition = partitionEntry.getKey();
                    Long offset = partitionEntry.getValue();

                    if (offset < maxPositionsForPartitions.get(partition)) {
                        isAllRead = false;
                    }
                }

                if (isAllRead)
                    break;

            }
        } catch (WakeupException e) {
            // ignore for shutdown
        }

        if (!snapData.isEmpty())
            currencySnapRepository.saveWithTTL(snapData);
    }

    private Map<TopicPartition, Long> getLastPositionsForPartition(KafkaConsumer consumer, Set<TopicPartition> partitions, String partition) {

        List<TopicPartition> collect = partitions.stream()
                .filter(t -> t.topic().equalsIgnoreCase(partition)).collect(Collectors.toList());

        return collect.stream()
                .collect(Collectors.toMap(t -> t, consumer::position));
    }

    @PreDestroy
    public void shutDown() {
        consumer.close();
    }
}
