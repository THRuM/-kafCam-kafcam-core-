package org.app.adapter.secondary;

import org.app.adapter.secondary.data.RequestDataWrapper;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RequestDataWrapperMongoStore extends MongoRepository<RequestDataWrapper, String> {
    RequestDataWrapper findByRequestId(String requestId);
}
