package tudelft.in4150.da;

import java.rmi.RemoteException;

/**
 * Main class that creates servers and builds rmi registry.
 */
public final class DASchiperEggliSandozMain {
    private static final int PORT = 1098;
    private static final int NUMPROCESSES = 3;

    private DASchiperEggliSandozMain() {
    }

    /**
     * Says hello to the world.
     * @return
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {

        // Init the RMI registery and create processes.
        DASchiperEggliSandoz.initRegistry(PORT);
        DASchiperEggliSandoz[] processes = DASchiperEggliSandoz.createProcesses(NUMPROCESSES, PORT);

        // Send some messages.
        try {
            processes[0].send(processes[1].getId(), new Message(), 0);
            processes[0].send(processes[2].getId(), new Message(), 0);
            processes[2].send(processes[1].getId(), new Message(), 0);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // TODO TEMP
        System.exit(0);
    }
}
