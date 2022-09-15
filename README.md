# Graphql  Microservice Application With Choreography-based Saga Pattern

Graphql Microservice Application With Choreography-based Saga Pattern


<img src="https://github.com/susimsek/GraphqlMicroserviceSagaPattern/blob/main/images/introduction.png" alt="Graphql  Microservice Application With Choreography-based Saga Pattern" width="100%" height="100%"/> 

# Graphql

GraphQL is a query language and server-side runtime for application programming interfaces (APIs) that prioritizes
giving clients exactly the data they request and no more.
GraphQL is designed to make APIs fast, flexible, and developer-friendly.
It can even be deployed within an integrated development environment (IDE) known as GraphiQL.
As an alternative to REST, GraphQL lets developers construct requests that pull data from multiple data sources in a
single API call.

# Data Flow

* The Order Service application takes in an Order as a request, which creates and sends an OrderPurchaseEvent to the Kafka topic orders which is processed by OrderPurchaseEventHandler in the payment service.  
* OrderPurchaseEventHandler processes the event and calculates if user has enough credit. If so, it sets the generated PaymentEvent status to APPROVED, otherwise DECLINED.  
* A PaymentEvent is emitted to the Kafka topic payments, which the PaymentEventHandler in the Payment Service application listens for.  
* If the PaymentEvent status is APPROVED, it saves the transaction in the TransactionRepository. A TransactionEvent is emitted to the transactions topic.  
* The TransactionEventConsumer reads this in the order service, if successful, the OrderRepository saves this as ORDER_COMPLETED, else ORDER_FAILED  

# Development

Before you can build this project, you must install and configure the following dependencies on your machine.

## Prerequisites for Backend 

* Java 17
* Kotlin
* Maven 3.x
* Mongodb
* Nodejs 14+ (required for Apollo Gateway)

### Run the microservice

You can install the dependencies by typing the following command

```sh
mvn clean install
```

You can run the spring boot microservice by typing the following command

```sh
mvn spring-boot:run
```

### Run the apollo gateway

You can install the dependencies by typing the following command

```sh
npm install
```

You can run the apollo gateway(accessible on http://127.0.0.1:4000) by typing the following command

```sh
npm run start:dev
```

# Supergraph Composition

## Prerequisites

* Rover

You can federate multiple subgraphs into a supergraph by typing the following command

```sh
rover supergraph compose --config ./deploy/supergraph.yaml > ./deploy/supergraph.graphql
```

# Sonar

## Code Quality For Backend

You can test code quality locally via sonarqube by typing the following command

```sh
mvn -Psonar compile initialize sonar:sonar
```

## Code Quality For Apollo Gateway

You can test code quality locally via sonarqube by typing the following command

```sh
npm run sonar
```

# Detekt

Detekt a static code analysis tool for the Kotlin programming language  

You can run detekt by typing the following command

```sh
mvn antrun:run@detekt
```

# Docker

You can also fully dockerize your application and all the services that it depends on. To achieve this, first build a docker image of your app.

## Build Docker Image for Backend

The docker image of microservice can be built as follows:

```sh
mvn -Pjib verify jib:dockerBuild
```

## Deployment with Docker Compose

### Prerequisites for Docker Compose Deployment

* Jq
* Docker
* Docker Compose

You can deploy app by running the following bash command


```sh
 sudo chmod +x deploy.sh
```

```sh
 ./deploy.sh -d
```

You can uninstall app the following bash command

```sh
 ./deploy.sh -d -r
```

The Fullstack GraphQL App be accessed with nginx from the link below.  
http://127.0.0.1


## Deployment Kubernetes with Helm

### Prerequisites for Kubernetes Deployment

* Kubernetes
* Helm

You can deploy app by running the following bash command

```sh
 sudo chmod +x deploy.sh
```

```sh
 ./deploy.sh -k
```

You can uninstall app the following bash command

```sh
 ./deploy.sh -k -r
```

You can upgrade the App (if you have made any changes to the generated manifests) by running the
following bash command

```sh
 ./deploy.sh -u
```

The app be accessed with ingress from the link below.(default nginx ingress)  
http://gqlmsweb.susimsek.github.io

# Used Technologies
## Backend Side
* Java 17
* Kotlin
* Docker
* Docker Compose
* Kubernetes
* Helm
* Sonarqube
* Detekt
* Circleci
* Snyk
* Kafka
* Vault
* Consul
* Nginx
* Mongodb
* Elasticsearch
* Kibana
* Logstash
* Apollo Gateway
* Spring Boot
* Spring Cloud
* Spring Boot Web Flux
* Spring Boot Graphql
* Spring Boot Validation
* Spring Cloud Stream
* Spring Boot Actuator
* Spring Boot Configuration Processor
* Kotlinx Coroutines Reactor
* Federation Graphql Java Support
* Logstash Logback Encoder
* Querydsl