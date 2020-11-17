package tudelft.in4150.da;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private Map<Integer, VectorClock> Buffer;

    public DASchiperEggliSandoz(int pid, int port) throws RemoteException {

        this.ID = pid;
        this.port = port;
        Buffer = new HashMap<Integer, VectorClock>();
        MessageBuffer = new ArrayList<Message>();

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

            VectorClock.incClock(ID);
            message.setTimestamp(VectorClock);
            message.setBuffer(Buffer);
            stub.receive(ID, message);

            // Construct pair of id and timestamp to add to own local buffer.
            // Using a copy of VectorClock as it is passed by reference into hashmap.
            VectorClock bufferTimestamp = new VectorClock(VectorClock);
            addBuffer(receiver, bufferTimestamp);
        } catch (NotBoundException e) {
            // TODO Auto-generated catch block
            LOGGER.error("Unable to locate process-" + receiver);
            e.printStackTrace();
        }

    }

    private void addBuffer(int id, VectorClock bufferTimestamp) {

        if (Buffer.containsKey(id)) {
            VectorClock element = Buffer.get(id);
            element.setMax(bufferTimestamp); // element is used by ref, hence no need to place it into hasmap again.
        }
        else
            Buffer.put(id, bufferTimestamp);
    }

    public synchronized void receive(int sender, Message message) throws RemoteException {
        LOGGER.info(this.ID + " received message " + message.toString() + " from " + sender);

        if (deliveryCondition(message)) {
            deliver(message);
            checkMessageBuffer();
        }
        else {
            LOGGER.info("Delivery condition not met, adding message to buffer.");
            MessageBuffer.add(message);
        }
    }

    private void checkMessageBuffer() {
        for (Message message : MessageBuffer) {
            if (deliveryCondition(message))
                deliver(message);
        }
    }

    /**
     * Delivery condition met if there does not exist a vector clock of the
     * receiving process in the message buffer, or there exists a vector clock of
     * the receiving process in the message buffer and its local buffer >= the clock
     * in the message buffer.
     * 
     * @param message
     * @return
     */
    private boolean deliveryCondition(Message message) {
        if (!message.getBuffer().containsKey(ID) || VectorClock.greaterEqual(message.getBuffer().get(ID)))
            return true;
        return false;
    }

    private void deliver(Message message) {
        LOGGER.info("Delivery condition met, delivering message to " + ID);
        
        for (Map.Entry<Integer, VectorClock> buffer : message.getBuffer().entrySet()) {
            if (buffer.getKey() != ID)
                addBuffer(buffer.getKey(), buffer.getValue());
        }
        
        VectorClock = new VectorClock(message.getTimestamp());
        VectorClock.incClock(ID); // Increase own clock since message was delivered.
    }

    public int getId() {
        return this.ID;
    }
}
