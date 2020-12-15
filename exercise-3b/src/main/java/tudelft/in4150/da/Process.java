package tudelft.in4150.da;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;

/**
 * Process class to create processes, running the DASuzukiKasami instance.
 */
public class Process {
    private static final Logger LOGGER = LogManager.getLogger(Process.class);
    private DAGallagerHumbleSpira instance;
    private ExecutorService executor;
    private int pid;
    private String ip;

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
        this.ip = ip;
    }

    /**
     * Initate the search for the MST from a process.
     */
    public void initiate() {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                if (instance.isSleeping()) {
                    LOGGER.log(Level.forName("MISC", 380), MarkerManager.getMarker("Initiate rmi://" + ip
                        + "/process-" + pid), "");

                        instance.wakeup();
                }
            }
        });
    }

    /**
     * Terminate the running thread for the process after it has completed all its pending jobs or a 10 second delay.
     */
    public void terminate() {
        final int wait = 10;
        LOGGER.debug("Terminating thread " + pid + " after pending jobs finished");
        executor.shutdown();
        try {
            executor.awaitTermination(wait, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.debug("Interrupted Exception thread " + pid);
            e.printStackTrace();
        }
        LOGGER.debug("Thread " + pid + " terminated");
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

    /**
     * Check if the process has finished the algorithm.
     * @return boolean if finished
     */
    public boolean finished() {
        return instance.isFinished();
    }

    /**
     * Get the final MST formatted as a string.
     * @return String of final MST core and level
     */
    public String getFinalMST() {
        return instance.getFinalCore() + " Level: " + instance.getFinalLevel();
    }
}
