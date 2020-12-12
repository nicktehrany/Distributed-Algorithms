package tudelft.in4150.da;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DAGallagerHumbleSpiraRMI extends Remote {
    void connect(int level, String sender) throws RemoteException;
}
