package org.app.adapter.secondary;

import org.app.adapter.secondary.data.ProducerData;
import org.app.domain.Producer;
import org.app.port.secondary.ProducerRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.stream.Collectors;

@Named
public class ProducerRepositoryImpl implements ProducerRepository {

    private ProducerDataMongoStore producerDataMongoStore;

    @Inject
    public ProducerRepositoryImpl(ProducerDataMongoStore producerDataMongoStore) {
        this.producerDataMongoStore = producerDataMongoStore;
    }

    @Override
    public List<Producer> findAllProducers() {
        return producerDataMongoStore
                .findAll()
                .stream()
                .map(p -> new Producer(p.getProducerId(), p.getCurrenciesList(), p.getInterval()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(Producer producer) {
        ProducerData producerData = new ProducerData(producer.getProducerId(), producer.getCurrenciesList(), producer.getInterval());
        producerDataMongoStore.save(producerData);
    }
}
