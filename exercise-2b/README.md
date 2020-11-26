# Suzuki-Kasami Algorithm

Exercise 2B for Distributed Algorithms (IN4150), implementing the Suzuki-Kasami algorithm for mutual exclusion in 
distributed systems.

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

## Program

Running the program with multiple instances (executing the .jar file) creates multiple JVM instances which 
all communicate to each other over RMI to exchange a token to enter their critical section. With the above provided
command line arguments, all instances will attempt to create the RMI registry, but only one will succeed and then
each process binds to the port. Each process has a seperate thread for executing the actual work of handling requests,
accessing the CS, and sending the token. When binding, each process checks if they are the first to bind and if so, 
they hold the token. Then each instance waits for 10 seconds for other processes to bind, before starting to send 
request to access the critical section at random rime intervals for 2 times. Output will log which request was received
by whom and to whom tokens are send, as well as when a process is entering and leaving its CS. After completing 
ones own CS requests, the process will wait 10 seconds for other process, in case they request the CS and it is holding
the token, before exiting.