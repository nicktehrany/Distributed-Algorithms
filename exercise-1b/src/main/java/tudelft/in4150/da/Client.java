package tudelft.in4150.da;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Client class that connects to the server via the RMI registry and sends a message.
 */
public final class Client {
    private static final Logger LOGGER = LogManager.getLogger(Client.class);
    private static final int PORT = 1098;

    private Client() {
    }

    /**
     * Main function to setup connection over RMI registry.
     * @return
     * @param
     */
    public static void main() {
        try {
            Registry registry = LocateRegistry.getRegistry(PORT);
            DASchiperEggliSandozRMI stub = (DASchiperEggliSandozRMI) registry.lookup("Hello");
            String response = stub.sayHello();
            LOGGER.info("response: " + response);
        } catch (RemoteException e) {
            LOGGER.error("Client RMI exception: " + e.toString());
            e.printStackTrace();
        } catch (NotBoundException e) {
            LOGGER.error("Client unbound exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
