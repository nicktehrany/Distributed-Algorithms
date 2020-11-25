package tudelft.in4150.da;

import java.lang.management.ManagementFactory;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main class that creates servers and builds rmi registry using the
 * DASchiperEggliSandoz class.
 */
public final class DASuzukiKasamiMain {
    private static final Logger LOGGER = LogManager.getLogger(DASuzukiKasamiMain.class);
    private static int port = 1098; // Default Port
    private static int numProcesses = 1; // Default 1 Process
    private static String ip = "localhost"; // Default ip of localhost

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
            else if (arg.startsWith("-port"))
                port = Integer.parseInt(arg.replaceAll("-port=", ""));
            else if (arg.startsWith("-ip"))
                ip = arg.replaceAll("-ip=", "");
        }

        // Init the RMI registry and create processes.
        int initialized = DASuzukiKasami.initRegistry(port);
        localProcesses = new Process[numProcesses];

        for (int i = 0; i < localProcesses.length; i++) {
            try {
                localProcesses[i] = new Process(ip, port);
            } catch (RemoteException e) {
                LOGGER.error("Remote exception creating process");
                e.printStackTrace();
            }
        }

        // TODO SLEEP 10s, wait for other servers
        try {
            LOGGER.info("Sleeping for 10 seconds to wait for others to bind processes to rmi");
            Thread.sleep(10000);
        } catch (InterruptedException e1) {
            LOGGER.error("Interrupted Exception");
            e1.printStackTrace();
        }

        // TODO if initialized clean up registry

        // System.exit(0);
    }
}
