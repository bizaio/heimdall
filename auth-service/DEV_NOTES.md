# Development Notes

Notes for the developers

## Initialise certificate for spring application

`mvn clean spring-boot:run -Dspring-boot.run.arguments=--initcert=localhost`

## For dev integration testing

`mvn clean verify -Dmaven.test.failure.ignore=true`

