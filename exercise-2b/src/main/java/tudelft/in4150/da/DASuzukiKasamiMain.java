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

        for (Process proc: localProcesses) {
            try {
                proc = new Process(ip, port);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // TODO SLEEP 10s, wait for other servers

        try {
            String[] x= LocateRegistry.getRegistry(port).list();
            for (String b: x) {
                LOGGER.info(b);
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // System.exit(0);
    }
}
