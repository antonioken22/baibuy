# Bai Buy

By Team BBLets

## Technologies Used

#### Backend

- Spring Boot (v3.4.4) - Java framework

- MySQL (v8.0.41) - Database

- Spring Data JPA – ORM layer for MySQL

- Spring Validation – Input validation

- Thymeleaf – Server-side templating engine

#### Frontend

- Bootstrap (v5.3.3)

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
