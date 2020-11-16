package tudelft.in4150.da;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface publicInterface extends Remote {
    String sayHello() throws RemoteException;
}
