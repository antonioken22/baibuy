# Bai Buy

By Team BBLets

## Technologies Used

#### Backend

- Java 17 – Programming language
- Spring Boot 3.4.4 – Main Java framework
- Spring Web – REST API and controller layer
- Spring Data JPA – ORM layer for MySQL
- Spring Validation – Form and DTO validation
- Thymeleaf – Server-side templating engine
- MySQL 8.0.41 – Relational database
- JDBC (MySQL Connector/J) – Database driver for MySQL

#### Frontend

- Bootstrap 5.3.3 – CSS and UI layout framework (used in Thymeleaf templates)

#### Developer Tools 

- Spring Boot DevTools – Hot reload and dev experience
- Lombok – Cleaner, boilerplate-free Java code (auto-generates getters, setters, etc.)
- Spring Boot Test Starter – Unit and integration testing tools

## Prerequisites Before Running Locally

### Install the following into your own computer:

1. [git](https://git-scm.com/downloads)

2. [Visual Studio Code](https://code.visualstudio.com/download)

3. [Java](https://www.oracle.com/java/technologies/downloads) (Install the LTS Version)

> Run this code into your cmd/cli to verify the installation.
>
> ```shell
> java -version
> ```

## Running Locally

1. Clone this repository into your computer.

2. Open and run a MySQL Instance in your MySQL Workbench locally, then add a database and name it `baibuy`.

3. Fill in all the necessary variables in the `application.properties`

## application.properties

```properties
spring.application.name=baibuy

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/baibuy
spring.datasource.username=root
spring.datasource.password=

spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
```

4. Run your web app at `http://localhost:8080` by using the Spring Boot Dashboard extension in your VSCode.

## Updating

1. In your Visual Studio Code, navigate to `Source Control (Ctrl+Shift+G)`.

2. Click the `...` (More Actions...) icon.

3. And in the dropdown options, click `Pull`.

## Reference

- [CRUD Operations using Spring Boot + Spring MVC + MySQL + Thymeleaf | Create, Read, Update and Delete](https://www.youtube.com/watch?v=6zfIxgaVkQI&t=2171s)
