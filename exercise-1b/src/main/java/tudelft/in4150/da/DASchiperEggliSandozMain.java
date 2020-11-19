package tudelft.in4150.da;

import java.rmi.RemoteException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main class that creates servers and builds rmi registry.
 */
public final class DASchiperEggliSandozMain {
    private static final Logger LOGGER = LogManager.getLogger(DASchiperEggliSandoz.class);
    private static final int PORT = 1098;
    private static final int NUMPROCESSES = 3;

    private DASchiperEggliSandozMain() {
    }

    /**
     * Says hello to the world.
     * 
     * @return
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {

        // Init the RMI registery and create processes.
        DASchiperEggliSandoz.initRegistry(PORT);
        DASchiperEggliSandoz[] processes = DASchiperEggliSandoz.createProcesses(NUMPROCESSES, PORT);

        // Send some messages.
        try {
            processes[0].send(processes[1].getId(), new Message(NUMPROCESSES), 2000);
            processes[0].send(processes[2].getId(), new Message(NUMPROCESSES), 0);
            processes[2].send(processes[1].getId(), new Message(NUMPROCESSES), 0);
        } catch (RemoteException e) {
            LOGGER.error("Remote exception sending messages.");
            e.printStackTrace();
        }

        // Sleep until all delays are finished to quit program.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            LOGGER.error("Interrupt exception.");
            e.printStackTrace();
        }
        System.exit(0);
    }
}
