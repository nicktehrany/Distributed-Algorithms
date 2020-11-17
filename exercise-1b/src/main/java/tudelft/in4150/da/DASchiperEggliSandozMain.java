package tudelft.in4150.da;

import java.rmi.RemoteException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main class that creates servers and builds rmi registry.
 */
public final class DASchiperEggliSandozMain {
    private static final Logger LOGGER = LogManager.getLogger(DASchiperEggliSandozMain.class);
    private static final int PORT = 1098;
    private static final int REG = 1099;

    private DASchiperEggliSandozMain() {
    }

    /**
     * Says hello to the world.
     * @return
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {

        // Setup RMI regisrty
        try {
            java.rmi.registry.LocateRegistry.createRegistry(PORT);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // Setup security manager.
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        // TODO FOR NOW HARDCODED
        try {
            DASchiperEggliSandoz x = new DASchiperEggliSandoz(1, PORT);
            DASchiperEggliSandoz y = new DASchiperEggliSandoz(2, PORT);
            x.send(y.getId(), new Message(1, 1, 2));
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // TODO TEMP
        System.exit(0);
    }
}
