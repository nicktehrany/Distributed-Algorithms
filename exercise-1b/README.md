# Schiper-Eggli-Sandoz Algorithm

Exercise 1B for Distributed Algorithms (IN4150), implementing the Schiper-Eggli-Sandoz algorithm for causal ordering
of point-to-point messages.

To build the .jar file, execute from the current directory,

```bash
mvn clean package -Dmaven.test.skip=true 
```

This will skip unit tests, which we implemented using JUnit. To run tests, remove the added parameter.
Note, to run tests in IDEs the tests still need to be compiled with the command above and the parameter removed.

To execute the generated .jar file,

```bash
java -Djava.security.policy=java.policy -jar target/DA-Schiper-Eggli-Sandoz.jar
```
