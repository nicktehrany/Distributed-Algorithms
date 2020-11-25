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
    private static boolean initialize = false;

    private DASuzukiKasamiMain() {
    }

    /**
     * @return
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        Process[] localProcesses;

        for (String arg : args) {
            if (arg.startsWith("-proc="))
                numProcesses = Integer.parseInt(arg.replaceAll("-proc=", ""));
            else if (arg.startsWith("-port="))
                port = Integer.parseInt(arg.replaceAll("-port=", ""));
            else if (arg.startsWith("-ip="))
                ip = arg.replaceAll("-ip=", "");
            else if (arg.equals("-initrmi")) {
                initialize = true;
            }
        }

        // Init the RMI registry and create processes.
        if (initialize)
            DASuzukiKasami.initRegistry(port);
        
        localProcesses = new Process[numProcesses];

        for (int i = 0; i < localProcesses.length; i++) {
            try {
                localProcesses[i] = new Process(ip, port);
            } catch (RemoteException e) {
                LOGGER.error("Remote exception creating process");
                e.printStackTrace();
            }
        }

        // Sleep 5s, waiting for other to bind to the registry before starting to send messages.
        try {
            LOGGER.info("Waiting 5s for other processes to bind to rmi");
            Thread.sleep(5000);
        } catch (InterruptedException e1) {
            LOGGER.error("Interrupted Exception");
            e1.printStackTrace();
        }


        Random rand = new Random(System.currentTimeMillis());
        int index = Math.abs(rand.nextInt()) % numProcesses;

        // TODO TEMP add some random time delay between requests
        if (!initialize)
            localProcesses[index].requestCS();

        // TODO if (initialized == 1) clean up registry

        // System.exit(0);
    }
}
