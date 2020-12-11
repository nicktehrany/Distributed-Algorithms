package tudelft.in4150.da;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DAGallagerHumbleSpiraRMI {
    void receive(int sender) throws RemoteException;
}
