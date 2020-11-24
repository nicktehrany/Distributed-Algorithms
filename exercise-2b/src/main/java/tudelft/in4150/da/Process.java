package tudelft.in4150.da;

import java.rmi.RemoteException;

public class Process {
    private DASuzukiKasami instance;

    public Process() throws RemoteException {
        instance = new DASuzukiKasami(1, 1098, new int[2], true);
        Thread thread = new Thread(instance);
        thread.start();
    }

    // PROVIDE INTERFACE FOR MAIN LIKE THIS
    public void requestToken() {
        // instance.requestToken(receiver, message, delay);
    }    
}
