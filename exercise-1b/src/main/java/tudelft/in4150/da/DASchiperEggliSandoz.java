package tudelft.in4150.da;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Server class that creates a server instance with the available RMI stub.
 */
public class DASchiperEggliSandoz extends UnicastRemoteObject implements DASchiperEggliSandozRMI {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger(DASchiperEggliSandoz.class);
    public int ID;
    private int port;

    public DASchiperEggliSandoz(int pid, int port) throws RemoteException {

        this.ID = pid;
        this.port = port;
        try {
            Registry registry = LocateRegistry.getRegistry(port);
            LOGGER.debug("Binding process " + ID);
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
     * Function that says hello.
     * 
     * @return String
     */
    public String sayHello() {
        return "Hello, world!";
    }

    /**
     * Main Function ...
     */
    public static void main() {
    }

    public void send(int receiver, Message message) throws RemoteException {
        Registry registry = LocateRegistry.getRegistry(port);
        try {
            DASchiperEggliSandozRMI stub = (DASchiperEggliSandozRMI) registry.lookup("process-" + receiver);
            stub.receive(receiver, message);
        } catch (NotBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void receive(int receiver, Message message) throws RemoteException {
        LOGGER.debug("Call received on " + receiver + " " + message);

    }

    public int getId() {
        return this.ID;
    }
}
