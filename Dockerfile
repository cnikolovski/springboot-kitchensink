FROM openjdk:21-jdk
EXPOSE 8080
ARG JAR_FILE=target/springboot-kitchensink-1.0.0.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]