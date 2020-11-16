package tudelft.in4150.da;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Server class that creates a server instance with the available RMI stub.
 * @return
 */
public class Server implements DASchiperEggliSandozRMI {
    private static final Logger LOGGER = LogManager.getLogger(Server.class);
    private static final int PORT = 1098;

    public Server() {
    }

    /**
     * Function that says hello.
     * @return String
     */
    public String sayHello() {
        return "Hello, world!";
    }

    /**
     * Main Function ...
     */
    public static void main() {
        try {
            Server obj = new Server();
            // was 1099 at port
            DASchiperEggliSandozRMI stub = (DASchiperEggliSandozRMI) UnicastRemoteObject.exportObject(obj, PORT);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.createRegistry(PORT);
            registry.bind("Hello", stub);
            LOGGER.info("Server ready");
        } catch (RemoteException e) {
            LOGGER.error("Server exception: " + e.toString());
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            LOGGER.error("Client unbound exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
