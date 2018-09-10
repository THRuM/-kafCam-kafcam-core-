## kafCam

Application created for fun and to play with Apache Kafka, Camunda and Hexagonal Architecture. At the beggining created as a monolith designed using hexagonal architecture, in time separate functionalities were moved to specific projects and comunicate via Kafka. 

We were looking for a way to play with a large number of data, as a result we find an api that exposes the currency rate in large numbers. Use cases are totaly 'invented' by us and were adjusted as the app was created to be more suitable. 

### Use cases
* User can get a currency rates from specified period of time
* User can get a currency expertise for specified currency from specified period of time
* Currency expertise is issued based on internal business logic and requires opinion from 'expert'

### Technical assumptions
* Currency ratings are persisted in Kafka topic
* Application is Event Driven, Kafka is used for communication between microservices
* Camunda BPMN is used for issuing the currency opinion

### Setup
* Application requires Apache Kafka
* MongoDb is used as a persistence for result of user request and also as a Kafka snapshot
* cam, kafprod, kafwork - multiple instances of this can be run simultaneously for scalability
* Kafka need 5 topics for setup use script:
[Linux](https://gist.github.com/THRuM/a0a34e7f5a5d458d81c3c909139481c0)
[Windows](https://gist.github.com/THRuM/5b170625b80d7c7494634a5ca8c88594)

kafcam - is single point of entry, it is responsible for handling the user requests via REST api, orchestration of the use cases, making the snaphot of the data from Kafka topic

cam - Camunda microservice, executes the BPMN

kafprod - Responsible for communication with the external API, act as a producer for Kafka currency topic

kafwork - Handles the request for data that is not available in the snapshot, searches through the Kafka topic to find specific data for specific currency

shared-domain - Through the process of separating the functionalities from monolith to separate microservices we encountered duplication of domain classes. This project contains domain classes for use by other projects, includes as a dependency.

## Authors

* **Maciej Cyrka** - [THRuM](https://github.com/THRuM)
* **Roman Kubis** - [THRuM](https://github.com/drx92)
