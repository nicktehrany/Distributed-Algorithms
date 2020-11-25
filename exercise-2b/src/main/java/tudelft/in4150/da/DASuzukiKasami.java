package tudelft.in4150.da;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Algorithms main class that implements the RMI interface and provides
 * additonal functionality for bootstraping processes and servers.
 */
public class DASuzukiKasami extends UnicastRemoteObject implements DASuzukiKasamiRMI, Runnable {  
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger(DASuzukiKasami.class);
    private static final String RMIBIND = "rmi:://";
    private String ip;

    public DASuzukiKasami(String ip, int pid, int port) throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(ip, port);
            LOGGER.debug("Binding process " + pid + " to port " + port);
            registry.bind(RMIBIND + ip + "/process-" + pid, this);
        } catch (RemoteException e) {
            LOGGER.error("Remote exception when binding process " + pid);
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            LOGGER.error(pid + "already bound to registry on port " + port);
            e.printStackTrace();
        }

        this.ip = ip;
    }
    
    /**
     * Initialize the RMI registry.
     *
     * @param port Port on which RMI registry is created.
     */
    public static int initRegistry(int port) {
        int initialized = 0;
        // Setup RMI regisrty
        try {
            java.rmi.registry.LocateRegistry.createRegistry(port);
        } catch (ExportException e) {
            LOGGER.debug("Registry already intialized");
            initialized = 1;
        } catch (RemoteException e) {
            LOGGER.debug("Remote Exception initializing registry");
            e.getStackTrace();
        }

        // Setup security manager.
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        return initialized;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub

    }

    @Override
    public void receive(int receiver, Message message) throws RemoteException {
        // TODO Auto-generated method stub

    }
   
    
}
