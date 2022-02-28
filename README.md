# Note API Java

Java, Spring Boot implementation for the Note API, a web API to store, retrieve
text-based notes.

## Technologies

This project uses the following technologies:

* [Java 17](https://jdk.java.net/17/)
* [Spring Boot 2.6](https://spring.io/projects/spring-boot)
* [Gradle 7.4](https://docs.gradle.org/7.4/userguide/userguide.html)
* [MongoDB 5](https://docs.mongodb.com/manual/release-notes/5.0/)
* [Project Lombok](https://projectlombok.org/)
* [Jackson](https://github.com/FasterXML/jackson)
* [Swagger UI](https://github.com/swagger-api/swagger-ui)

Testing tools:

* [JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
* [AssertJ](https://assertj.github.io/doc/)
* [Testcontainers](https://www.testcontainers.org/)
* Spring integration test configs ([see here](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing.spring-boot-applications.autoconfigured-tests))
  * @SpringBootTest
  * @WebMvcTest
  * @JsonTest
  * @DataMongoTest

## Building the Application

Build dependencies:

* Java 17 JDK
* Docker - used for Testcontainers integration tests

To build the application, run `./gradlew build`.

## Running the Application

Runtime dependencies:

* Java 17
* MongoDB

To run the application, use `java -jar build/libs/note-api.jar`

### MongoDB

The application is automatically configured to communicate with a local MongoDB
instance running on localhost:27017.

You can start up a local MongoDB instance using the provided docker-compose.yaml
file, via `docker-compose up -d`
