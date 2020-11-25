package tudelft.in4150.da;

import java.rmi.RemoteException;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Process {
    private DASuzukiKasami instance;
    private static final Logger LOGGER = LogManager.getLogger(Process.class);

    public Process(String ip, int port) throws RemoteException {
        Random rand = new Random(System.currentTimeMillis());
        instance = new DASuzukiKasami(ip, Math.abs(rand.nextInt()), port);
    }

    // PROVIDE INTERFACE FOR MAIN LIKE THIS
    public void requestToken() {
        // instance.requestToken(receiver, message, delay);
        LOGGER.info(instance.toString());
    }    
}
