package tudelft.in4150.da;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Hello world!
 */
public final class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    private Main() {
    }

    /**
     * Says hello to the world.
     *
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        System.out.println("Create a server");
        Server.main();

        System.out.println("Call a function as client");
        Client.main();
    }

}
