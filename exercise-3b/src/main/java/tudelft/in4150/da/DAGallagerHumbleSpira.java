package tudelft.in4150.da;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;

public class DAGallagerHumbleSpira extends UnicastRemoteObject implements DAGallagerHumbleSpiraRMI {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger(DAGallagerHumbleSpira.class);
    private static final int DELAY = 300;
    private int pid;
    private int port;
    private String ip;
    private ExecutorService executor;
    private String rmiBind = "rmi:://";
    private ArrayList<Edge> adjNodes;
    private State state;
    private int level = 0;
    private int findCount = Integer.MAX_VALUE;
    private Queue<Message> messageQueue;
    private int fragmentName;
    private Edge inBranch;
    private Edge bestEdge;
    private Edge testEdge;
    private int bestWeight;

    /**
     * Constructor method to bind process to the rmi registry on provided port and
     * ip.
     *
     * @param ip
     * @param port
     * @param executor
     * @throws RemoteException
     */
    public DAGallagerHumbleSpira(String ip, int port, ExecutorService executor) throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(ip, port);
            this.pid = registry.list().length;
            LOGGER.debug("Binding process " + pid + " to port " + port);
            rmiBind += ip + "/process-" + pid;
            registry.bind(rmiBind, this);
        } catch (RemoteException e) {
            LOGGER.error("Remote exception when binding process " + pid);
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            LOGGER.error(pid + "already bound to registry on port " + port);
            e.printStackTrace();
        }

        this.ip = ip;
        this.port = port;
        this.executor = executor;
        adjNodes = new ArrayList<Edge>();
        state = State.sleeping;
        messageQueue = new LinkedList<Message>();
    }

    /**
     * Initialize the RMI registry.
     *
     * @param port Port on which RMI registry is created.
     */
    public static void initRegistry(int port) {

        // Setup RMI regisrty
        try {
            java.rmi.registry.LocateRegistry.createRegistry(port);
        } catch (ExportException e) {
            LOGGER.debug("Registry already intialized");
        } catch (RemoteException e) {
            LOGGER.debug("Remote Exception initializing registry");
            e.getStackTrace();
        }

        // Setup security manager.
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
    }

    /**
     * Create an adjacent edge for the process.
     * @param node
     * @param weight
     */
    public void createEdge(String node, int weight) {
        adjNodes.add(new Edge(node, weight));
        LOGGER.debug("created edge for " + rmiBind + " to " + node + " " + weight);
    }

    /**
     * Helper function when creating edges, to check if a node exists.
     * @param name
     * @param p
     * @return if node exists
     */
    public static boolean nodeExists(String name, int p) {
        boolean found = false;

        try {
            String[] registeredProcesses = LocateRegistry.getRegistry(p).list();
            for (String node : registeredProcesses) {
                if (name.equals(node)) {
                    found = true;
                    break;
                }
            }
        } catch (RemoteException e) {
            LOGGER.error("Remote Exception");
        }

        return found;
    }

    /**
     * Wake up a process to start looking for its MOE by sending connect message to min edge.
     */
    public void wakeup() {
        LOGGER.debug("Waking up " + rmiBind);
        Edge minEdge = getMinEdge(adjNodes);
        minEdge.setState(Edgestate.in_MST);
        state = State.found;
        findCount = 0;
        send(new Connect(0, rmiBind), minEdge.getNode());
    }

    /**
     * Helper function to retireve a process' stub and send a message.
     * @param message
     * @param receiver
     */
    private void send(Message message, String receiver) {
        try {
            Registry registry = LocateRegistry.getRegistry(ip, port);
            DAGallagerHumbleSpiraRMI stub = (DAGallagerHumbleSpiraRMI) registry.lookup(receiver);
            LOGGER.log(Level.forName("SEND", 340), MarkerManager.getMarker(message.mType + " sent"), "from " + rmiBind
                + " to " + receiver);
            stub.receive(message);
        } catch (RemoteException e) {
            LOGGER.error("Remote Exception");
            e.printStackTrace();
        } catch (NotBoundException e) {
            LOGGER.error("Unbound process exception");
        }
    }

    /**
     * Wrapper method to create runnable of a message and submit to worker thread.
     * @param message
     */
    public void receive(Message message) throws RemoteException {
        LOGGER.log(Level.forName("RECEIVE", 350), MarkerManager.getMarker(message.mType + " received"), "by "
            + rmiBind);
        executor.submit(new Runnable() {
            @Override
            public void run() {
                addDelay();
                handleMessage(message);

                addDelay();
                // Check the queue if older messages can be accepted, if not they'll be queued again.
                int size = messageQueue.size();
                for (int i = 0; i < size; i++) {
                    Message m = messageQueue.remove();
                    handleMessage(m);
                }
            }
        });
    }

    /**
     * Method for adding a random delay, simulated by the thread sleeping.
     */
    private void addDelay() {
        Random rand = new Random(System.currentTimeMillis());
        int delay = Math.abs(rand.nextInt()) % DELAY;

        try {
            Thread.sleep(delay);
        } catch (InterruptedException e1) {
            LOGGER.error("Interrupted Exception");
            e1.printStackTrace();
        }
    }

    /**
     * Wrapper method for handling the different types of messages.
     * @param message
     */
    private void handleMessage(Message message) {
        if (message.mType == Message.Type.Connect) {
            handleConnect((Connect) message);
        } else if (message.mType == Message.Type.Initiate) {
            handleInitiate((Initiate) message);
        } else if (message.mType == Message.Type.Test) {
            handleTest((Test) message);
        } else if (message.mType == Message.Type.Accept) {
            handleAccept((Accept) message);
        } else if (message.mType == Message.Type.Reject) {
            handleReject((Reject) message);
        } else if (message.mType == Message.Type.Report) {
            handleReport((Report) message);
        } else if (message.mType == Message.Type.ChangeRoot) {
            handleChangeRoot((ChangeRoot) message);
        }
    }

    /**
     * Handling the connect message, when a process receives a connect message, upon which it checks if it can
     * merge or absorbe the sender.
     * @param message
     */
    private void handleConnect(Connect message) {
        if (state == State.sleeping) {
            wakeup();
        }

        Edge j = findEdge(message.sender);
        if (message.getLevel() < level) {
            findEdge(message.sender).setState(Edgestate.in_MST);
            LOGGER.log(Level.forName("OPERATION", 360), MarkerManager.getMarker("Absorb " + message.sender), "from "
                + rmiBind + " Fragment name: " + fragmentName + " Level: " + level);

                send(new Initiate(level, fragmentName, state, rmiBind), message.sender);
            if (state == State.find) {
                findCount++;
            }
        } else {
            if (j.getState() == Edgestate.Q_in_MST) {
                messageQueue.add(message);
            } else {
                // *Note: LOGGER output shows what the fragment name and level will be after merge completed*
                LOGGER.log(Level.forName("OPERATION", 360), MarkerManager.getMarker("Merge " + message.sender),
                    "with " + rmiBind + " Fragment name: " + j.getWeight() + " Level: " + (message.getLevel() + 1));

                send(new Initiate(level + 1, j.getWeight(), State.find, rmiBind),
                    message.sender);
            }
        }
    }

    /**
     * Handling the initiate message, when a process receives an initiate message, upon which it tries to find a
     * MOE in its adjacent edges.
     * @param message
     */
    private void handleInitiate(Initiate message) {
        level = message.getLevel();
        fragmentName = message.getFragmentName();
        state = message.getState();
        bestWeight = Integer.MAX_VALUE;

        Edge edge = findEdge(message.sender);
        inBranch = edge;
        bestEdge = new Edge(null, Integer.MAX_VALUE); // Max int value used as infinity
        for (Edge e : adjNodes) {
            if (e.getWeight() != edge.getWeight() && e.getState() == Edgestate.in_MST) {
                send(new Initiate(message.getLevel(), message.getFragmentName(), message.getState(), rmiBind),
                    e.getNode());

                if (message.getState() == State.find) {
                    findCount++;
                }
            }
        }
        if (state == State.find) {
            test();
        }
    }

    /**
     * Handling the test message, when a process receives a test message, upon which it either accepts or rejects
     * depending on its fragment level.
     * @param message
     */
    private void handleTest(Test message) {
        if (state == State.sleeping) {
            wakeup();
        }
        if (message.getLevel() > level) {
            messageQueue.add(message);
        } else {
            if (message.getFragmentName() != fragmentName) {
                send(new Accept(rmiBind), message.sender);
            } else {
                Edge edge = findEdge(message.sender);
                if (edge.getState() == Edgestate.Q_in_MST) {
                    LOGGER.log(Level.forName("MISC", 380), MarkerManager.getMarker("Edge " + edge.getWeight()
                        + " not in MST"), "");

                    edge.setState(Edgestate.not_in_MST);
                }
                if (testEdge.getWeight() != edge.getWeight()) {
                    send(new Reject(rmiBind), message.sender);
                } else {
                    test();
                }
            }
        }
    }

    /**
     * Handling the accept message, when a process receives an accept message, upon which it records the sender
     * as a potential MOE.
     * @param message
     */
    private void handleAccept(Accept message) {
        testEdge = null;
        Edge edge = findEdge(message.sender);
        if (edge.getWeight() < bestWeight) {
            bestEdge = edge;
            bestWeight = edge.getWeight();
        }
        report();
    }

    /**
     * Handling the reject message, when a process receives a reject message, upon which it records the sender
     * not being in the MST.
     * @param message
     */
    private void handleReject(Reject message) {
        Edge edge = findEdge(message.sender);
        if (edge.getState() == Edgestate.Q_in_MST) {
            edge.setState(Edgestate.not_in_MST);
        }
        test();
    }

    /**
     * Handling the report message, when a process receives a report message it will report its knowledge about the
     * best MOE to the core.
     * @param message
     */
    private void handleReport(Report message) {
        Edge edge = findEdge(message.sender);
        if (edge.getWeight() != inBranch.getWeight()) {
            findCount--;
            if (message.getWeight() < bestWeight) {
                bestWeight = message.getWeight();
                bestEdge = edge;
            }
            report();
        } else {
            if (state == State.find) {
                messageQueue.add(message);
            } else {
                if (message.getWeight() > bestWeight) {
                    changeRoot();
                } else if (message.getWeight() == bestWeight && bestWeight == Integer.MAX_VALUE) {
                    LOGGER.log(Level.forName("RESULT", 370), MarkerManager.getMarker("Final MST Core: " + fragmentName
                        + " Level: " + level), "");
                }
            }
        }
    }

    /**
     * Handling a ChangeRoot message by calling the changeRoot() method.
     * @param message
     */
    private void handleChangeRoot(ChangeRoot message) {
        changeRoot();
    }

    /**
     * Test method to find a potential MOE and report to the core.
     */
    private void test() {
        ArrayList<Edge> edges = getEdgesInQMST();
        if (!edges.isEmpty()) {
            testEdge = getMinEdge(edges);
            send(new Test(level, fragmentName, rmiBind), testEdge.getNode());
        } else {
            testEdge = null;
            report();
        }
    }

    /**
     * Report method to forward the best MOE to the core.
     */
    private void report() {
        if (findCount == 0 && testEdge == null) {
            state = State.found;
            send(new Report(bestWeight, rmiBind), inBranch.getNode());
        }
    }

    /**
     * Changroot method to send a ChangeRoot message to the core and if core, connect to the MOE.
     */
    private void changeRoot() {
        if (bestEdge.getState() == Edgestate.in_MST) {
            send(new ChangeRoot(rmiBind), bestEdge.getNode());
        } else {
            send(new Connect(level, rmiBind), bestEdge.getNode());
            bestEdge.setState(Edgestate.in_MST);
        }
    }

    /**
     * Helper function that finds all edges in adjNodes that are in Q_in_MST state.
     * @return ArrayList of Edge.
     */
    private ArrayList<Edge> getEdgesInQMST() {
        ArrayList<Edge> edges = new ArrayList<Edge>();
        for (Edge e : adjNodes) {
            if (e.getState() == Edgestate.Q_in_MST) {
                edges.add(e);
            }
        }
        return edges;
    }

    /**
     * Helper method that finds the edge that is connected to the node with the specified name.
     * @param node
     * @return Edge to node
     */
    private Edge findEdge(String node) {
        Edge edge = null;
        for (Edge e : adjNodes) {
            if (e.getNode().equals(node)) {
                edge = e;
                break;
            }
        }

        return edge;
    }


    /**
     * Helper method that finds the adjacent edge with the minimum weight.
     * @param edges ArrayList of edges to find the minimum from.
     * @return Node with min weight.
     */
    private Edge getMinEdge(ArrayList<Edge> edges) {
        Edge minEdge = null;
        if (!edges.isEmpty()) {
            minEdge = edges.get(0);
            for (Edge node : edges) {
                if (node.getWeight() < minEdge.getWeight()) {
                    minEdge = node;
                }

            }
        }

        return minEdge;
    }

    /**
     * Helper method to get a process' id.
     * @return pid
     */
    public int getPid() {
        return pid;
    }
}
