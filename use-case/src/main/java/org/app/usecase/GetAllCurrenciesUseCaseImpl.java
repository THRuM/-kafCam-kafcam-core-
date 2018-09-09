package org.app.usecase;

import org.app.domain.Producer;
import org.app.port.secondary.ProducerRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Named
public class GetAllCurrenciesUseCaseImpl implements GetAllCurrenciesUseCase {

    private ProducerRepository producerRepository;

    @Inject
    public GetAllCurrenciesUseCaseImpl(ProducerRepository producerRepository) {
        this.producerRepository = producerRepository;
    }

    @Override
    public List<String> execute() {

        List<Producer> allProducers = producerRepository.findAllProducers();

        return allProducers
                .stream()
                .map(Producer::getCurrenciesList)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }
}
