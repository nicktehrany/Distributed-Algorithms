package tudelft.in4150.da;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
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
    private Integer level = 0;
    private Integer findCount = 0;

    enum State {
        sleeping,
        find,
        found
    }

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

    public void createEdge(String node, Integer weight) {
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
     * Wake up a process to start looking for its MOE.
     */
    public void wakeup() {
        LOGGER.info("Waking up " + rmiBind);
        Edge minEdge = getMinEdge();
        minEdge.state = Edge.adjState.in_MST;
        state = State.found;

        try {
            Registry registry = LocateRegistry.getRegistry(ip, port);
            DAGallagerHumbleSpiraRMI stub = (DAGallagerHumbleSpiraRMI) registry.lookup(minEdge.getNode());
            LOGGER.info("Sending connect to " + minEdge.getNode());
            stub.connect(0, rmiBind);
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
    public void connect(int level, String sender) throws RemoteException {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                handleConnect(level, sender);
            }
        });
    }

    public void handleConnect(int level, String sender) {
        LOGGER.info(rmiBind + " received connect");
        if (state == State.sleeping) {
            wakeup();
        }
        
        Edge j = findEdge(sender);
        if (level < this.level) {
            // TODO send initiate

            if (state == State.find) {
                findCount++;
            }
        } else {
            if (j.state == Edge.adjState.Q_in_MST) {
                // TODO append message to queue
            } else {
                // TODO send initiate
                LOGGER.info("Initiate from " + rmiBind);
            }
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
