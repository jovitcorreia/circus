<div align="center">
  <a href="https://github.com/castanhocorreia/circus">
    <img src="icon.svg" alt="Logo" width="80" height="80">
  </a>
  <h2 align="center">CIRCUS</h2>
  <h6 align="center">Video rooms application built using Apache Kafka, Java, Spring Boot, Spring Security and PostgreSQL</h6>
</div>

### About

CIRCUS is a Java and Spring Boot application for creating rooms with the purpose of watching YouTube videos together, similar to [Watch2Gether](https://w2g.tv)

It supports user management and authentication via JWT, using Spring Security and a PostgreSQL relational database

Video rooms maintain socket connection and provide real-time chat and the ability for users to control the video player, through an Apache Kafka broker

**This project is a case study and should not be used for any real purpose**


### Technologies

- [Apache Kafka](https://kafka.apache.org/)
- [Apache Log4j 2](https://logging.apache.org/log4j/2.x/)
- [Docker](https://www.docker.com/)
- [Flyway](https://flywaydb.org/)
- [Java](https://www.oracle.com/java/)
- [JSON Web Tokens](https://jwt.io/)
- [PostgreSQL](https://www.postgresql.org/)
- [Project Lombok](https://projectlombok.org/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Swagger](https://swagger.io/)

### Requisites

- Docker version 20 or higher and docker-compose
- Ports 5432, 8080, 22181 and 29092

### Installation

Clone the project, and run the following command in the root directory:

```
docker-compose up -d --build
```

The API will be running on localhost, port 8080

### License

All the code on this repository is licensed under the [GNU General Public License v3.0](https://www.gnu.org/licenses/gpl-3.0.en.html)