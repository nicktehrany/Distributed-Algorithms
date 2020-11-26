package tudelft.in4150.da;

import java.rmi.RemoteException;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main class that creates servers and builds rmi registry using the
 * DASuzukiKasamiMain class.
 */
public final class DASuzukiKasamiMain {
    private static final Logger LOGGER = LogManager.getLogger(DASuzukiKasamiMain.class);
    private static int port = 1098; // Default Port
    private static int numProcesses = 1; // Default 1 Process
    private static String ip = "localhost"; // Default ip of localhost
    private static int numRequests = 1;
    private static final int DELAY = 4000;
    private static final int WAIT = 10000;

    private DASuzukiKasamiMain() {
    }

    /**
     * @return
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        Process[] localProcesses;

        for (String arg : args) {
            if (arg.startsWith("-proc=")) {
                numProcesses = Integer.parseInt(arg.replaceAll("-proc=", ""));
            } else if (arg.startsWith("-port=")) {
                port = Integer.parseInt(arg.replaceAll("-port=", ""));
            } else if (arg.startsWith("-ip=")) {
                ip = arg.replaceAll("-ip=", "");
            } else if (arg.startsWith("-reqs=")) {
                numRequests = Integer.parseInt(arg.replaceAll("-reqs=", ""));
            }
        }

        // Attempt to initialize the RMI registry.
        DASuzukiKasami.initRegistry(port);

        // Create all local processes.
        localProcesses = new Process[numProcesses];
        for (int i = 0; i < localProcesses.length; i++) {
            try {
                localProcesses[i] = new Process(ip, port);
            } catch (RemoteException e) {
                LOGGER.error("Remote exception creating process");
                e.printStackTrace();
            }
        }

        // Sleep 10s, waiting for other to bind to the registry before starting to send messages.
        try {
            LOGGER.info("Waiting 10s for other processes to bind to rmi");
            Thread.sleep(WAIT);
        } catch (InterruptedException e1) {
            LOGGER.error("Interrupted Exception");
            e1.printStackTrace();
        }

        for (int i = 0; i < numRequests; i++) {
            Random rand = new Random(System.currentTimeMillis());
            int index = Math.abs(rand.nextInt()) % numProcesses;
            int delay = Math.abs(rand.nextInt()) % DELAY;

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e1) {
                LOGGER.error("Interrupted Exception");
                e1.printStackTrace();
            }
            localProcesses[index].requestCS();

        }

        // Sleep 10s in canse other processes still send requests.
        try {
            LOGGER.info("Sleeping 10s for other processes to finish before exiting");
            Thread.sleep(WAIT);
        } catch (InterruptedException e1) {
            LOGGER.error("Interrupted Exception");
            e1.printStackTrace();
        }

        System.exit(0);
    }
}
