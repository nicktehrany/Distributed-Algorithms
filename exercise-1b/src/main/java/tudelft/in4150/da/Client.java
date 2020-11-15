package tudelft.in4150.da;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    Client() {
    }

    public static void main() {
        try {
            Registry registry = LocateRegistry.getRegistry(1098);
            publicInterface stub = (publicInterface) registry.lookup("Hello");
            String response = stub.sayHello();
            System.out.println("response: " + response);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
