package tudelft.in4150.da;

import java.rmi.RemoteException;

public class Process {
    private DASuzukiKasami instance;

    public Process(int port, boolean token, int allocation[]) throws RemoteException {
        instance = new DASuzukiKasami(1, port, allocation, token);
        Thread thread = new Thread(instance);
        thread.start();
    }

    // PROVIDE INTERFACE FOR MAIN LIKE THIS
    // Sends all other process a token request
    public void requestToken() {
        instance.requestToken(this.instance.getId(), message, delay);
    }

    // PROVIDE INTERFACE FOR MAIN LIKE THIS
    // If a process has the token, send it to the process
    public void sendToken() {
        instance.sendToken(message, delay);
    }
}
