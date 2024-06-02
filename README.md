# Developer's Manual

### Pre-requisite
* Java Version: 21
* Maven: 3.9.5

### Step 1: Setup and build

> ./mvnw clean install


### Step 2: Start MongoDB docker container 

> docker pull mongo

> docker run -d -p 27017-27019:27017-27019 --name mongodb -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=password mongo


### Step 3: Seed data in MongoDb

> brew tap mongodb/brew

> brew install mongodb-database-tools

> mongoimport --username admin --password password mongodb://localhost:27017/kitchensink --collection member --mode insert --file src/main/resources/seed.json --authenticationDatabase admin


### Step 4: Run Springboot application locally

> java -jar target/springboot-kitchensink-0.0.1-SNAPSHOT.jar


### Step 4 (Alternate): Run Springboot Application in docker container (locally)

> In application properties replace "localhost" with "host.docker.internal".

> ./mvnw spring-boot:build-image

> docker run -p 8080:8080 docker.io/library/springboot-kitchensink:0.0.1-SNAPSHOT


### Endpoints

Please open this endpoint in a browser 'http://localhost:8080/kitchensink/'