package tudelft.in4150.da;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Message class to construct messages to be passed between processes.
 *
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private Token token;

    /**
     * Construct a message.
     *
     * @param numProcesses // Number of total processes needed for Token creation
     */
    public Message(int numProcesses) {
        token = new Token(numProcesses);
    }

    /**
     * Copy constructor.
     *
     * @param message
     */
    public Message(Message message) {
        token = new Token(message.token);
    }
}
