package tudelft.in4150.da;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
        // instance.assignToken();
    }

    /**
     * Initiate the ...
     */
    public void initiate() {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                // instance.requestCS();
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
}
