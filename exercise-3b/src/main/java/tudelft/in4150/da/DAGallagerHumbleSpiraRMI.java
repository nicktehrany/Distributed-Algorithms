package tudelft.in4150.da;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * RMI interface for the different processes to send messages to find the MST.
 */
public interface DAGallagerHumbleSpiraRMI extends Remote {

    /**
     * Send a message to a different process over Java RMI by calling the this method on the remote process.
     * @param message
     * @throws RemoteException
     */
    void receive(Message message) throws RemoteException;
}
