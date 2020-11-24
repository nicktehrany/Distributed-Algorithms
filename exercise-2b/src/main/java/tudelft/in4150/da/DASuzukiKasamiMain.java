package tudelft.in4150.da;

import java.rmi.RemoteException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main class that creates servers and builds rmi registry using the DASchiperEggliSandoz class.
 */
public final class DASuzukiKasamiMain {
    private static final Logger LOGGER = LogManager.getLogger(DASuzukiKasamiMain.class);
    private static final int PORT = 1098;
    private static final int NUMPROCESSES = 3;

    private DASuzukiKasamiMain() {
    }

    /**
     * Example execution with 3 processes (P1, P2, and P3).
     * P1 sends m1 -> P2 with 2000ms delay.
     * P1 sends m2 -> P3 with no delay.
     * P3 sends m3 -> P2 with no delay.
     *
     * @return
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {

        // Init the RMI registry and create processes.
        // SuzukiKasami.initRegistry(PORT);
        // SuzukiKasami[] processes = SuzukiKasami.createProcesses(NUMPROCESSES, PORT);
        final int delay = 2000;
        int[] x = new int [2];
        try {
            Process p = new Process();
            p.requestToken();
            Process p2 = new Process();
            p2.requestToken();
        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        LOGGER.debug(java.lang.Thread.activeCount());
        // Send some messages.
        // try {
        //     // Process 0 will get the token (by default)
        //     processes[0].send(processes[1].getId(), new Message(NUMPROCESSES), delay);

        //     // Process 2 requests the token
        //     //processes[2].requestToken(NUMPROCESSES, new Message(NUMPROCESSES), 0);
        //     //processes[2].wait(2000); //Simulates Mutual Exclusion process

        //     // Process 3 request the token, but still in use by 2
        //     //processes[3].requestToken(NUMPROCESSES, new Message(NUMPROCESSES), 0);
        //     //processes[3].wait(2000); //Simulates Mutual Exclusion process

        // } catch (RemoteException e) {
        //     LOGGER.error("Remote exception sending messages.");
        //     e.printStackTrace();
        // }

        // Sleep until all delays are finished to quit program.
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            LOGGER.error("Interrupt exception.");
            e.printStackTrace();
        }
        System.exit(0);
    }
}
