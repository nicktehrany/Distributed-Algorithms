package tudelft.in4150.da;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server implements publicInterface {
    public Server() {
    }

    public String sayHello() {
        return "Hello, world!";
    }

    public static void main() {
        try {
            Server obj = new Server();
            publicInterface stub = (publicInterface) UnicastRemoteObject.exportObject(obj, 1099);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.createRegistry(1098);
            registry.bind("Hello", stub);

            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
