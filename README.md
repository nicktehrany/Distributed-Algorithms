# Schiper-Eggli-Sandoz Algorithm

Exercise 1 and 2 for Distributed Algorithms (IN4150), implementing the Schiper-Eggli-Sandoz algorithm for causal ordering
of point-to-point messages.

### Deadline: Friday 27 November

## Exercise 1B description:

Implement the Schiper-Eggli-Sandoz algorithm for causal ordering of point-to-point messages with Java/RMI. The implemented program should be truly distributed in that the it can be demonstrated to run across multiple physical machines.

First day
- [ ] Implement the algorithm itself. Incorporate random delays before processes send a me

Second day
- [ ] Design a few test cases for the algorithm. Implement a program that records for any two messages whether the send event of one happens before the send event of the other, that records all receive events in all processes, and that checks the causal ordering.

## Exercise 2B description:
Implement Suzuki’s and Kasami’s algorithm for mutual exclusion in a distributed system with Java/RMI. The implemented program should be truly distributed in that the it can be demonstrated to run across multiple physical machines. The assignment can be split up into the following three part.


Part 1:
- [ ] Write the remote interface and the global framework of the Component class implementing the components of the distributed algorithm.
- [ ] In addition, create the framework for the Main class that will create the Component objects and their threads on a single host. It must be possible to specify the number of these components. Include into Main and Component the functionality of registering and looking up components.

Part 2:
- [ ] Include into Component the functionality for broadcasting and receiving messages for requesting the critical section. Include random delays in the critical sections and between finishing one execution of the critical section and requesting access again.

Part 3:
- [ ] Include into Component the functionality for sending and receiving the token. It can be assumed that a single designated process initially contains the token. Make sure that the output of the algorithm makes it possible to check its correct operation.

# Build

Build instructions for each exercise are in the respective directory.
