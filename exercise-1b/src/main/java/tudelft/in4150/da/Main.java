package tudelft.in4150.da;

/**
 * Hello world!
 */
public final class Main {
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
