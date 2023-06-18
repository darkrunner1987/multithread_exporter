# Configuration

To configure an application use `src/main/resources/application.properties`.

Data source configuration is controlled by Spring configuration properties in `spring.datasource.*.`:
```
spring.datasource.driver-class-name = org.postgresql.Driver
spring.datasource.url = jdbc:postgresql://localhost:32768/postgres
spring.datasource.username = postgres
spring.datasource.password =
```
# Run the Application

You can run the application from your IDE as a simple Java application.

## Running as a packaged application

Using java -jar:
```
$ java -jar target/multithread_exporter-1.0-SNAPSHOT.jar
```

## Using the Maven plugin

```
mvn spring-boot:run
```