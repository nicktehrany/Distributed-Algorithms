package tudelft.in4150.da;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DAGallagerHumbleSpiraRMI extends Remote {
    void receive(Message message) throws RemoteException;
}
