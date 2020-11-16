# Schiper-Eggli-Sandoz Algorithm

Exercise 1B for Distributed Algorithms (IN4150), implementing the Schiper-Eggli-Sandoz algorithm for causal ordering
of point-to-point messages.

To build the .jar file, execute from the current directory,

```bash
mvn package
```

To execute the generated .jar file,

```bash
java -Djava.security.policy=java.policy -jar target/DA-Schiper-Eggli-Sandoz.jar
```

Part 1:
- [ ] Write the remote interface and the global framework of the Component class implementing the components of the distributed algorithm.
- [ ] In addition, create the framework for the Main class that will create the Component objects and their threads on a single host. It must be possible to specify the number of these components. Include into Main and Component the functionality of registering and looking up components.

Part 2:
- [ ] Include into Component the functionality for broadcasting and receiving messages for requesting the critical section. Include random delays in the critical sections and between finishing one execution of the critical section and requesting access again.

Part 3:
- [ ] Include into Component the functionality for sending and receiving the token. It can be assumed that a single designated process initially contains the token. Make sure that the output of the algorithm makes it possible to check its correct operation.
