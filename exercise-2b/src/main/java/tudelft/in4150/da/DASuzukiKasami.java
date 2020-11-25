package tudelft.in4150.da;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Algorithms main class that implements the RMI interface and provides
 * additonal functionality for bootstraping processes and servers.
 */
public class DASuzukiKasami extends UnicastRemoteObject implements DASuzukiKasamiRMI {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger(DASuzukiKasami.class);
    private int pid;
    private int port;
    private String ip;
    private ExecutorService executor;
    private boolean holdsToken = false;
    private String rmiBind = "rmi:://";
    private int[] requestNumbers;

    public DASuzukiKasami(String ip, int port, ExecutorService executor) throws RemoteException {
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

        // Initialize local array to invalid value to check later and initialize once
        // all processes connected to rmi.
        requestNumbers = new int[1];
        requestNumbers[0] = -1;

        this.ip = ip;
        this.port = port;
        this.executor = executor;
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
     * Request the token to access the CS by broadcasting the request.
     */
    public void requestCS() {
        LOGGER.info(this.pid + " requesting CS");

        // Checking if RN has been initialized.
        if (requestNumbers[0] == -1) {
            initLocalCounters();
        }

        requestNumbers[this.pid]++;

        // if (holdsToken) { // IS THIS CORRECT? NOT SENDING A REQUEST?
        //     enterCS();
        // } else {
        try {
            Registry registry = LocateRegistry.getRegistry(ip, port);
            String[] registeredProcesses = registry.list();
            for (String process : registeredProcesses) {
                DASuzukiKasamiRMI stub = (DASuzukiKasamiRMI) registry.lookup(process);
                stub.receiveRequest(this.pid, requestNumbers[this.pid]);
            }
        } catch (RemoteException e) {
            LOGGER.error("Remote Exception");
            e.printStackTrace();
        } catch (NotBoundException e) {
            LOGGER.error("Unbound process exception");
        }
	}

    private void enterCS() {
        LOGGER.info("Entering CS");
    }

    /**
     * Each process checks if it is first in the rmi registry list and if so gets assigned the token.
     */
	public void assignToken() {
        try {
            String[] registeredProcesses = LocateRegistry.getRegistry(port).list();
            if (rmiBind.equals(registeredProcesses[0])) {
                this.holdsToken = true;
                LOGGER.info("Process " + pid + " holds the token first");
            }
        } catch (RemoteException e) {
            LOGGER.error("Remote Exception");
            e.printStackTrace();
        }
	}

    public void receiveRequest(int sender, int counter) throws RemoteException {
        if (requestNumbers[0] == -1) {
            initLocalCounters();
        }
        LOGGER.info("Received from " + sender + " " + counter);
    }

    /**
     * If the requestNumbers counter has not been initialized yet count the number of processes in the rmi registry and
     * initialize requestNumbers.
     */
    private void initLocalCounters() {
        int size = 1;
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry(ip, port);
            size = registry.list().length;
        } catch (RemoteException e) {
            LOGGER.error("Remote Exception when connecting to RMI");
        }
        requestNumbers = new int[size];
        Arrays.fill(requestNumbers, 0);
    }

    public void receiveToken(int receiver, Message message) throws RemoteException {
        // TODO Auto-generated method stub

    }
   
    
}
