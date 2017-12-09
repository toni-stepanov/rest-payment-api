transfer-service
===============================

Simple rest api.

## Technology stack:

* Spring Boot
* Spring Data JPA
* H2 DB
* Swagger
* Spring Test/JUnit/Mockito/Rider
* Lombok

## Requirements

* JDK 8

  Oracle Java 8 is required, go to [Oracle Java website](http://java.oracle.com) to download it and install into your 
  system. 
 
  Optionally, you can set **JAVA\_HOME** environment variable and add *&lt;JDK installation dir>/bin* in your **PATH** 
  environment variable.
 
## Get the source codes

Get a copy of the source codes into your local system:
    
    git clone https://github.com/toni-stepanov/rest-payment-api

## Get the source codes

To build & run via command line shell from code directory:

    ./gradlew clean build && java -jar build/libs/rest-payment-api-0.0.1-SNAPSHOT.jar

Or simply run `TransferApplication.main()` via IDEA (14.1+ with latest Lombok plugin recommended) 
or your favorite IDE.

## Checkout from Swagger

You can check api using this link:
http://localhost:8080/swagger-ui.html#!/transfer-controller/createAccountUsingPOST