# Bai Buy

By Team BBLets

## Technologies Used

#### Backend

- Java 17 – Programming language
- Spring Boot 3.4.4 – Main Java framework
- Spring Web – REST API and controller layer
- Spring Security – Authentication and authorization
- Spring Data JPA – ORM layer for MySQL
- Hibernate – JPA implementation (via Spring Data JPA)
- Spring Validation – Form and DTO validation
- Thymeleaf – Server-side templating engine
- Thymeleaf Extras (Spring Security 6) – Thymeleaf integration with Spring Security
- MySQL 8.0.41 – Relational database
- JDBC (MySQL Connector/J) – Database driver for MySQL

#### Frontend

- Bootstrap 5.3.3 – CSS and UI layout framework (used in Thymeleaf templates)

#### Developer Tools

- Spring Boot DevTools – Hot reload and dev experience
- Lombok – Cleaner, boilerplate-free Java code
- Spring Boot Test Starter – Base for testing

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

3. Create a copy of `application.properties.template` in the same directory and rename the copy to `application.properties`. Then fill all the required fields.

4. Run your web app at `http://localhost:8080` by using the Spring Boot Dashboard extension in your VSCode.

## Updating

1. In your Visual Studio Code, navigate to `Source Control (Ctrl+Shift+G)`.

2. Click the `...` (More Actions...) icon.

3. And in the dropdown options, click `Pull`.

## Reference

- [CRUD Operations using Spring Boot + Spring MVC + MySQL + Thymeleaf | Create, Read, Update and Delete](https://www.youtube.com/watch?v=6zfIxgaVkQI&t=2171s)
