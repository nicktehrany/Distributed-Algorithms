package tudelft.in4150.da;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class DAGallagerHumbleSpiraMain {
    private static final Logger LOGGER = LogManager.getLogger(DAGallagerHumbleSpira.class);
    private static int port = 1098; // Default Port
    private static int numProcesses = 1; // Default 1 Process
    private static String ip = "localhost"; // Default ip of localhost
    private static String conffile = "/default.cfg";
    private static Process[] localProcesses;
    private static final int WAIT = 10000;

    private DAGallagerHumbleSpiraMain() {
    }

    /**
     * Says hello to the world.
     *
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {

        for (String arg : args) {
            if (arg.startsWith("-proc=")) {
                numProcesses = Integer.parseInt(arg.replaceAll("-proc=", ""));
            } else if (arg.startsWith("-port=")) {
                port = Integer.parseInt(arg.replaceAll("-port=", ""));
            } else if (arg.startsWith("-ip=")) {
                ip = arg.replaceAll("-ip=", "");
            } else if (arg.startsWith("-conf=/")) {
                conffile = arg.replaceAll("-conf=", "");
            } else if (arg.startsWith("-conf=")) {
                conffile = arg.replaceAll("-conf=", "/");
            }
        }

        // Attempt to initialize the RMI registry.
        DAGallagerHumbleSpira.initRegistry(port);

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
        parseConf();

        // Sleep 10s, waiting for other to bind to the registry before starting to initate algorithm.
        try {
            LOGGER.info("Waiting 10s for other processes to bind to rmi");
            Thread.sleep(WAIT);
        } catch (InterruptedException e1) {
            LOGGER.error("Interrupted Exception");
            e1.printStackTrace();
        }


        // A random local process will initiate the algorithm.
        Random rand = new Random(System.currentTimeMillis());
        int index = Math.abs(rand.nextInt()) % numProcesses;
        localProcesses[index].initiate();

        // for (Process p : localProcesses) {
        //     p.terminate();
        // }
        // System.exit(0);
    }

    private static void parseConf() {
        InputStream in = DAGallagerHumbleSpira.class.getResourceAsStream(conffile);
        if (in == null) {
            LOGGER.error("File " + conffile.replaceFirst("/", "") + " not found");
            System.exit(1);
        }

        InputStreamReader streamReader = new InputStreamReader(in);
        BufferedReader reader = new BufferedReader(streamReader);
        try {
            for (String line; (line = reader.readLine()) != null;) {
                assignEdge(line);
            }
        } catch (IOException e) {
            LOGGER.error("I/O Exception");
            e.printStackTrace();
        }
    }

    private static void assignEdge(String assignment) {
        String[] assign = assignment.split("\\s+");

        // Ensuring edge weight is a positive integer
        if (assign[1].matches("\\d+")) {
            Integer weight = Integer.parseInt(assign[1]);
            for (Process p : localProcesses) {
                String name = "rmi:://" + ip + "/" + p.getName();

                // Self edges are ignored and checks if other node exists
                if (name.equals(assign[0]) && !assign[0].equals(assign[2])) {
                    p.assignEdge(assign[2], weight);
                } else if (name.equals(assign[2]) && !assign[0].equals(assign[2])) {
                    p.assignEdge(assign[0], weight);
                }
            }
        }
    }
}
