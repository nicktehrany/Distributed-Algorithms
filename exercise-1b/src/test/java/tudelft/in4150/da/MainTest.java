package tudelft.in4150.da;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.rmi.RemoteException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit test for Schiper-Eggli-Sandoz algorithm implementation.
 */
class MainTest {
    private static final ByteArrayOutputStream NEWOUT = new ByteArrayOutputStream();
    private static final int PORT = 1098;

    @BeforeAll
    private static void setup() {
        System.setProperty("java.security.policy", "java.policy");

        // Setup new out to capture stdout from tests.
        System.setOut(new PrintStream(NEWOUT));

        // TODO START TEST SETUP (REGISTERY)
    }

    @Test
    public void test1() {
        DASchiperEggliSandoz.initRegistry(PORT);

        // TODO MAKE ACTUAL TEST CASES
        try {
            DASchiperEggliSandoz x = new DASchiperEggliSandoz(1, PORT);
            DASchiperEggliSandoz y = new DASchiperEggliSandoz(2, PORT);
            x.send(y.getId(), new Message(), 0);
            y.send(x.getId(), new Message(), 0);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final String standardOut = NEWOUT.toString();
        System.out.print(standardOut);
    }
}
