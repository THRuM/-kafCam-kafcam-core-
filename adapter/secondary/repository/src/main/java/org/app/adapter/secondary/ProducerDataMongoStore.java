package org.app.adapter.secondary;

import org.app.adapter.secondary.data.ProducerData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProducerDataMongoStore extends MongoRepository<ProducerData, String> {

}
