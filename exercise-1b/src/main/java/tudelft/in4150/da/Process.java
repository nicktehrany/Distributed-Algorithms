package tudelft.in4150.da;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Process {
    private static final Logger LOGGER = LogManager.getLogger(Process.class);
    public int ID;

    void startProcess(int port, int pid) {
        // this.ID = pid;
        // try {
        //     DASchiperEggliSandoz obj = new DASchiperEggliSandoz();
        //     Registry registry = LocateRegistry.getRegistry(port);
        //     LOGGER.debug("Binding process " + ID);
        //     registry.bind("process-" + ID, obj);
        // } catch (RemoteException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // } catch (AlreadyBoundException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
    }
}
