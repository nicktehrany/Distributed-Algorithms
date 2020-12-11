package tudelft.in4150.da;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class DAGallagerHumbleSpiraMain {
    private static final Logger LOGGER = LogManager.getLogger(DAGallagerHumbleSpira.class);
    private static int port = 1098; // Default Port
    private static int numProcesses = 1; // Default 1 Process
    private static String ip = "localhost"; // Default ip of localhost
    private static String conffile = "/default.cfg";
    private static Process[] localProcesses;

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

        // TODO cleanup processes
        localProcesses[0].terminate();
        System.exit(0);
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

        // TODO Error check before assign
        Integer weight = Integer.parseInt(assign[1]);
        for (Process p : localProcesses) {
            String name = "rmi:://" + ip + "/" + p.getName();

            // Self edges are ignored
            if ((name.equals(assign[0]) || name.equals(assign[2])) && 
                !assign[0].equals(assign[2])) {

                // TODO assign weight to edge
                LOGGER.info("here");
            }
        }
    }
}
