# Suzuki-Kasami Algorithm

Exercise 2B for Distributed Algorithms (IN4150), implementing the Suzuki-Kasami algorithm for ....

To build the .jar file, execute from the current directory,

```bash
mvn clean package -Dmaven.test.skip=true 
```

This will skip unit tests, which we implemented using JUnit. To run tests, remove the added flag.
Note, to run tests in IDEs the tests still need to be compiled with the command above and the flag removed.

The build will generate two .jar files in the target/ directory. One for the logging tool log4j2, which we are using
to log information on message exchanges between processes, and a second which is the .jar of the actual program.
To execute the generated .jar file,

```bash
java -Djava.security.policy=java.policy -jar target/DA-Suzuki-Kasami.jar
```

## Program

