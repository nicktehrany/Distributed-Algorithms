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
import java.util.concurrent.ExecutorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DAGallagerHumbleSpira extends UnicastRemoteObject implements DAGallagerHumbleSpiraRMI {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger(DAGallagerHumbleSpira.class);
    private int pid;
    private int port;
    private String ip;
    private ExecutorService executor;
    private String rmiBind = "rmi:://";
    private ArrayList<Edge> adjNodes;
    private State state;
    private int level = 0;
    private int findCount = 0;
    private Queue<Message> messageQueue;
    private int fragmentName = 0;
    private ArrayList<Edge> inBranch;
    private Edge bestEdge;

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
        inBranch = new ArrayList<Edge>();
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

    public void createEdge(String node, int weight) {
        adjNodes.add(new Edge(node, weight));
        LOGGER.debug("created edge for " + rmiBind + " to " + node + " " + weight);
    }

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
        LOGGER.info("Waking up " + rmiBind);
        Edge minEdge = getMinEdge();
        minEdge.setState(adjState.in_MST);
        state = State.found;
        send(new Connect(0, rmiBind), minEdge.getNode());
    }

    private void send(Message message, String receiver) {
        try {
            Registry registry = LocateRegistry.getRegistry(ip, port);
            DAGallagerHumbleSpiraRMI stub = (DAGallagerHumbleSpiraRMI) registry.lookup(receiver);
            LOGGER.info(rmiBind + " sending " + message.mType + " message to " + receiver);
            stub.receive(message);
        } catch (RemoteException e) {
            LOGGER.error("Remote Exception");
            e.printStackTrace();
        } catch (NotBoundException e) {
            LOGGER.error("Unbound process exception");
        }
    }
    
    /**
     * Wrapper method to create runnable of connect message and submit to worker thread.
     * @param level
     * @param sender
     */
    public void receive(Message message) throws RemoteException {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                if (message.mType == Message.Type.Connect) {
                    handleConnect((Connect) message);
                } else if (message.mType == Message.Type.Initiate) {
                    handleInitiate((Initiate) message);
                }
            }
        });
    }

    public void handleConnect(Connect message) {
        LOGGER.debug(rmiBind + " received connect");
        if (state == State.sleeping) {
            wakeup();
        }
        
        Edge j = findEdge(message.sender);
        if (level < this.level) {
            findEdge(message.sender).setState(adjState.in_MST); // ? Did sender also do this?
            send(new Initiate(this.level, fragmentName, state), message.sender);
            if (state == State.find) {
                findCount++;
            }
        } else {
            if (j.getState() == adjState.Q_in_MST) {
                messageQueue.add(message);
            } else {
                send(new Initiate(this.level + 1, findEdge(message.sender).getWeight(), State.find), message.sender);
            }
        }
    }

    public void handleInitiate(Initiate message) {
        LOGGER.debug(rmiBind + " received initiate");
        level = message.getLevel();
        fragmentName = message.getFragmentName();
        state = message.getState();

        Edge j = findEdge(message.sender);
        inBranch.add(new Edge(message.sender, j.getWeight()));
        bestEdge = new Edge(null, -1);

        for (Edge e : adjNodes) {
            if (e.getWeight() != j.getWeight() && e.getState() == adjState.in_MST) {
                send(new Initiate(message.getLevel(), findEdge(message.sender).getWeight(), message.getState()),
                    e.getNode());
            }
            if (state == State.find) {
                findCount++;
            }
        }
        if (state == State.find) {
            // test();
        }
    }

    /**
     * Finds the edge that is connected to the node with the specified name.
     * @param node
     * @return Edge to node
     */
    private Edge findEdge(String node){
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
     * Finds the adjacent edge with the minimum weight.
     * @return Node with min weight.
     */
    private Edge getMinEdge() {
        Edge minEdge = null;
        if (!adjNodes.isEmpty()) {
            minEdge = adjNodes.get(0);
            for (Edge node : adjNodes) {
                if (node.getWeight() < minEdge.getWeight()) {
                    minEdge = node;
                }

            }
        }

        return minEdge;
    }

	public int getPid() {
		return pid;
	}
}
