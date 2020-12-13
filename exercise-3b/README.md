# Minimum Weighted Spanning Tree of asynchronous weighted networks of Gallager, Humblet, and Spira

Exercise 3B for Distributed Algorithms (IN4150), implementing the algorithm for creating minimum weight spanning trees
in an asynchronous network.

**TODO UPDATE FOR NEW SETUP AND CONF FILES**

To build the .jar file, execute from the current directory,

```bash
mvn clean package -Dmaven.test.skip=true 
```

This will skip unit tests, which we implemented using JUnit. To run tests, remove the added flag.
Note, to run tests in IDEs the tests still need to be compiled with the command above and the flag removed.

The build will generate two .jar files in the target/ directory. One for the logging tool log4j2, which we are using
to log information on message exchanges between processes, and a second which is the .jar of the actual program.
To execute the generated .jar file run this for one JVM instance,

```bash
java -Djava.security.policy=java.policy -jar target/DA-Suzuki-Kasami.jar -proc=1 -reqs=2
```

There are additional parameters for specifying the port to bind to and the IP address on which to do rmi.
All possible parameters are passed last in the command line, after specifying the .jar to execute. The parameters 
are `-proc=` for specyfing the number of local processes (default 1) to create in this JVM instance (Note each process 
still has its own thread runnning), `-port=` to specify the port to bind processes to and, if specified, initialize the 
rmi on (default 1098), `-ip=` to specify the ip to bind processes to the rmi (default localhost), and lastly `-reqs=` 
to specify the number of critical section requests to do (default 1). Requests will be isssues at random time 
intervals.