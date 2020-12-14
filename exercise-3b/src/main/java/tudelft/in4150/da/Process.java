package tudelft.in4150.da;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Process class to create processes, running the DASuzukiKasami instance.
 */
public class Process {
    private static final Logger LOGGER = LogManager.getLogger(Process.class);
    private DAGallagerHumbleSpira instance;
    private ExecutorService executor;
    private int pid;

    /**
     * Process constructre that creates a single threaded executor and a DASuzukiKasami instance.
     * @param ip
     * @param port
     * @throws RemoteException
     */
    public Process(String ip, int port) throws RemoteException {
        executor = Executors.newSingleThreadExecutor();
        instance = new DAGallagerHumbleSpira(ip, port, executor);
        pid = instance.getPid();
    }

    /**
     * Initate the search for the MST from a process.
     */
    public void initiate() {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                instance.wakeup();
            }
        });
    }

    /**
     * Assign an edge with a weight and a node to a process.
     * @param node
     * @param weight
     */
    public void assignEdge(String node, Integer weight) {
        instance.createEdge(node, weight);
    }

    /**
     * Helper method to get the name of a process.
     * @return process-id
     */
    public String getName() {
        return "process-" + pid;
    }
}
