# Developer's Manual

### Pre-requisite
* Java Version: 21
* Maven: 3.9.5
* MongoDB 

### Setup and build

> ./mvnw clean install


### Run Springboot application locally

In order to deploy this application run the commands below:

> docker pull mongo

> docker run -d -p 27017-27019:27017-27019 --name mongodb -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=password mongo

>java -jar target/springboot-kitchensink-0.0.1-SNAPSHOT.jar


### Run Springboot Application in docker container (locally)

> In application properties replace "localhost" with "host.docker.internal".

> ./mvnw spring-boot:build-image

> docker run -d -p 27017-27019:27017-27019 --name mongodb -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=password mongo

> docker run -p 8080:8080 docker.io/library/springboot-kitchensink:0.0.1-SNAPSHOT


### Endpoints

Please open this endpoint in a browser 'http://localhost:8080/kitchensink/'