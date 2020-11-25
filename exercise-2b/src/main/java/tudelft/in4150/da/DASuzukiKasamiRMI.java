package tudelft.in4150.da;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface for Remote Method Invocation (RMI) using the Schiper-Eggli-Sandoz algorithm
 * for point to point causal message ordering.
 *
 * @throws RemoteException
 */
public interface DASuzukiKasamiRMI extends Remote {

    void receiveRequest(int sender, int counter) throws RemoteException;
    void receiveToken(int sender, Token token) throws RemoteException;
}
