# IMDB
* Backend application for movies and actors management via REST API

##Getting Started
* IMBD is stand-alone Spring based application created via Spring boot 2.2.2

###Prerequisites
* Programming language: Java - 1.8 (tested with jdk1.8.0_111)
* Database: H2, version 1.4.200
* Build automation: Maven
* Server: Apache Tomcat 9.0.29
* Database VCS: Flyway Community Edition 6.0.8 by Redgate
* Frameworks: Hibernate 5.4.9.Final, Spring boot 2.2.2

###Installing
* [HELP.md]
* Project is mavenized, so all dependencies are in pom.xml.
* Database is H2. So fer, it is used in embedded mode, as either in memory or persistent database.
* To configure database connection, check application.properties. 
<br/>To use in memory database, add/uncomment:
<br/>spring.datasource.url=jdbc:h2:mem:/data/imdbDB
<br/>To use persisted database, add/uncomment:
<br/>spring.datasource.url=jdbc:h2:file:~/imdbDB;
<br/>spring.jpa.hibernate.ddl-auto=update
* For database initialization is used Flyway, so database initialization will be executed automatically. 
<br/>Initialization sql file is V1__init.sql. 
* To check which is the last migration executed, check table flyway_schema_history in database.
data.sql is used to fill database with some test data. If database is persisted, after first
run, rename this file to avoid unnecessary constraint exceptions, since data.sql is run on every run/debug.
* H2 console available at '/h2-console'.
* Entry point to application is ImdbApplication, which can be run in Run or Debug mode.
* Tomcat starts on port: 8080 (http).

###Running the tests
Tests should be added to src/test/java/com/demo/imdb/

