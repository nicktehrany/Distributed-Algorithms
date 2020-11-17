package tudelft.in4150.da;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit test for Schiper-Eggli-Sandoz algorithm implementation.
 */
class MainTest {
    private static final ByteArrayOutputStream NEWOUT = new ByteArrayOutputStream();

    @BeforeAll
    private static void setup() {
        System.setProperty("java.security.policy", "java.policy");

        // Setup new out to capture std out from tests.
        System.setOut(new PrintStream(NEWOUT));

        // TODO START TEST SETUP (REGISTERY)
    }

    @Test
    public void test1() {
        DASchiperEggliSandozMain.main(null);
        final String standardOut = NEWOUT.toString();
        System.out.print(standardOut);
    }
}
