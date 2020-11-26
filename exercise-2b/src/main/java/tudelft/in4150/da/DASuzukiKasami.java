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
    private Token token = null;
    private int numprocesses = 0;

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

        requestNumbers = new int[1];
        requestNumbers[0] = 0;

        this.ip = ip;
        this.port = port;
        this.executor = executor;
        this.token = new Token(1);
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

        checkValidCounters(pid);

        requestNumbers[this.pid]++;

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

    /**
     * Submiting simulation of CS to thread by sleeping random time.
     */
    private void enterCS() {
        LOGGER.info("Entering CS");
        
        try {
            Thread.sleep(1000);
            LOGGER.info("Leaving CS");
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted exception during CS");
        }
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

    /**
     * Wrapper method to create runnable of request and submit to worker thread.
     * @param sender
     * @param counter
     */
    public void receiveRequest(int sender, int counter) throws RemoteException {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                handleRequest(sender, counter);
            }
        });
    }

    private void handleRequest(int sender, int counter) {
        
        LOGGER.info("Received request from " + sender + " counter " + counter);
        checkValidCounters(sender);
        requestNumbers[sender] = counter;

        if (holdsToken && requestNumbers[sender] > token.getValue(sender)) {
            holdsToken = false;

            try {
                Registry registry = LocateRegistry.getRegistry(ip, port);
                DASuzukiKasamiRMI stub;
                stub = (DASuzukiKasamiRMI) registry.lookup("rmi:://" + ip + "/process-" + sender);
                LOGGER.info("Sending token to rmi:://" + ip + "/process-" + sender);
                stub.receiveToken(this.pid, token);
            } catch (RemoteException | NotBoundException e) {
                LOGGER.error("Exception sending token to process"); // TODO WHAT HAPPENS WITH TOKEN IF EXCEPTION?
            }
        }
    }

    /**
     * If the requestNumbers counter has not been initialized yet count the number of processes in the rmi registry and
     * initialize requestNumbers.
     */
    private void initLocalCounters() {
        Registry registry;
        try {
            registry = LocateRegistry.getRegistry(ip, port);

            // Remeber the number of processes in the rmi for later.
            numprocesses = registry.list().length;
        } catch (RemoteException e) {
            LOGGER.error("Remote Exception when connecting to RMI");
        }
        requestNumbers = new int[numprocesses];
        Arrays.fill(requestNumbers, 0);
    }

    public void receiveToken(int sender, Token token) throws RemoteException {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                handleToken(sender, token);
            }
        });
    }
    
    /**
     * Handle the received token, enter own CS, and send token to next requesting process.
     * @param sender
     * @param token
     */
    private void handleToken(int sender, Token token) {
        LOGGER.info(pid + " received token from " + sender + " " + token.toString());
        
        holdsToken = true;
        enterCS();
        
        this.token = token;
        this.token.setValue(pid, requestNumbers[pid]);

        int counter = pid + 1;
        while (counter != pid) {

            if (requestNumbers[counter] > token.getValue(counter)) {
                holdsToken = false;
                Registry registry;
                try {
                    registry = LocateRegistry.getRegistry(ip, port);
                    DASuzukiKasamiRMI stub = (DASuzukiKasamiRMI) registry.lookup("rmi:://" + ip + "/process-" + sender);
                    LOGGER.info("Sending token to rmi:://" + ip + "/process-" + sender);
                    stub.receiveToken(this.pid, token);
                } catch (RemoteException e) {
                    LOGGER.error("Remote exception sending token.");
                } catch (NotBoundException e) {
                    LOGGER.error("Not bound exception sending token.");
                }
                break;
            }
            
            // subtract 1 as pids start at 0 but numprocesses at 1.
            if (counter == numprocesses - 1) {
                counter = 0;
            } else {
                counter++;
            }     
        }
    }
    
    /**
     * If a new process has been added to the registry, but is not present in the local array yet and token, update 
     * both by copying existing values and creating new indeices for new processes.
     * @param id
     */
    private void checkValidCounters(int id) {
        if (requestNumbers.length < id + 1) {
            int[] temp = new int[id + 1];
            Arrays.fill(temp, 0);
            for (int i = 0; i < requestNumbers.length; i++) {
                temp[i] = requestNumbers[i];
            }
            requestNumbers = temp;
            numprocesses = requestNumbers.length;
        }

        if (token.getLength() < id + 1) {
            token = new Token(token, id + 1);
        }
    }
}
