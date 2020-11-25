package tudelft.in4150.da;

import java.rmi.RemoteException;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Process {
    private DASuzukiKasami instance;
    private static final Logger LOGGER = LogManager.getLogger(Process.class);
    private ExecutorService executor;
    private int pid;

    public Process(String ip, int port) throws RemoteException {
        Random rand = new Random(System.currentTimeMillis());
        this.pid = Math.abs(rand.nextInt());
        LOGGER.debug("Starting thread " + pid);
        executor = Executors.newSingleThreadExecutor();
        instance = new DASuzukiKasami(ip, pid, port);
        instance.assignToken();
    }

    // PROVIDE INTERFACE FOR MAIN LIKE THIS
    public void requestCS() {
        instance.requestCS(this.pid);
        // try {
        //     String[] x= LocateRegistry.getRegistry(port).list();
        //     for (String b: x) {
        //         LOGGER.info(b);
        //     }
        // } catch (RemoteException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        // instance.requestToken(receiver, message, delay);
        LOGGER.info(instance.toString());
    }

    /**
     * Terminate the running thread for the process after it has completed all its pending jobs or a 10 second delay.
     */
    public void terminate() {
        LOGGER.debug("Terminating thread " + pid + " after pending jobs finished");
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOGGER.debug("Interrupted Exception thread " + pid);
            e.printStackTrace();
        }
        LOGGER.debug("Thread " + pid + " terminated");
        // TODO UNBIND process from registry 
    }
}
