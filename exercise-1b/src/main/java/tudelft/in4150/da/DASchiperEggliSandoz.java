package tudelft.in4150.da;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tudelft.in4150.VectorClock;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Server class that creates a server instance with the available RMI stub.
 */
public class DASchiperEggliSandoz extends UnicastRemoteObject implements DASchiperEggliSandozRMI {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger(DASchiperEggliSandoz.class);
    private int ID;
    private int port;
    private VectorClock VectorClock;
    private List<Message> MessageBuffer;
    private List<Map<Integer, VectorClock>> LocalBuffer;

    public DASchiperEggliSandoz(int pid, int port) throws RemoteException {

        this.ID = pid;
        this.port = port;

        try {
            Registry registry = LocateRegistry.getRegistry(port);
            LOGGER.debug("Binding process " + ID + " to port " + port);
            registry.bind("process-" + ID, this);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Initialize the RMI registry.
     * @param port Port on which RMI registry is created.
     */
    public static void initRegistry(int port) {
        // Setup RMI regisrty
        try {
            java.rmi.registry.LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // Setup security manager.
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
    }

    public static DASchiperEggliSandoz[] createProcesses(int numProcesses, int port) {
        DASchiperEggliSandoz[] processes = new DASchiperEggliSandoz[numProcesses];

        for (int i = 0; i < numProcesses; i++) {
            try {
                Thread thread = new Thread();

                LOGGER.debug("Starting thread for process " + (i + 1));
                thread.start();
                processes[i] = new DASchiperEggliSandoz(i + 1, port);
                processes[i].VectorClock = new VectorClock(numProcesses);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return processes;
    }

    public synchronized void send(int receiver, Message message) throws RemoteException {
        Registry registry = LocateRegistry.getRegistry(port);
        try {
            DASchiperEggliSandozRMI stub = (DASchiperEggliSandozRMI) registry.lookup("process-" + receiver);
            LOGGER.info(this.ID + " sending message to " + receiver);
            message.setTimestamp(VectorClock);
            stub.receive(receiver, message);
        } catch (NotBoundException e) {
            // TODO Auto-generated catch block
            LOGGER.error("Unable to locate process-" + receiver);
            e.printStackTrace();
        }

    }

    public synchronized void receive(int receiver, Message message) throws RemoteException {
        LOGGER.info(this.ID + " received message " + message.toString() + " from " + receiver);

    }

    public int getId() {
        return this.ID;
    }
}
