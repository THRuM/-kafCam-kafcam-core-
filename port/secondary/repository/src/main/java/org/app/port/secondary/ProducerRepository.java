package org.app.port.secondary;

import org.app.domain.Producer;

import java.util.List;

public interface ProducerRepository {
    List<Producer> findAllProducers();

    void save(Producer producer);
}
