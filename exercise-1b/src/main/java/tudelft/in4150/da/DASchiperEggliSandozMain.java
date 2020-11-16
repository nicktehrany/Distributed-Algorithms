package tudelft.in4150.da;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main class that creates servers and builds rmi registry.
 */
public final class DASchiperEggliSandozMain {
    private static final Logger LOGGER = LogManager.getLogger(DASchiperEggliSandozMain.class);

    private DASchiperEggliSandozMain() {
    }

    /**
     * Says hello to the world.
     * @return
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        LOGGER.debug("Create a server");
        Server.main();

        LOGGER.info("Call a function as client");
        Client.main();
    }

}
