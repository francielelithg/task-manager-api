## task manager backend
This application is the backend part from the Task Manager application developed as assignment to Bunny Studio. It was developed using Spring Boot framework (and its features like JPA), Maven, Java 8 and MySQL database. To run application, type on terminal:

    mvn spring-boot:run
Or right click on file *BackendApplication.java >> Run As >> Java Application*. The server is going to be running on **http://localhost:8080/**. The API documentation is available on this link through Swagger UI.

There are also some JUnit 5 tests implemented. They run in a particular environment poiting to an in-memory database (H2).  To run each of them, right click on the test class and *Run As >> JUnit Test*.
