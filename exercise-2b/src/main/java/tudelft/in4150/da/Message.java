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
    private Map<Integer, Token> buffer;
    private Token timestamp;

    /**
     * Construct a message.
     *
     * @param numProcesses // Number of total processes needed for VectorClock creation
     */
    public Message(int numProcesses) {
        buffer = new HashMap<Integer, Token>();
        timestamp = new Token(numProcesses);
    }

    /**
     * Copy constructor.
     *
     * @param message
     */
    public Message(Message message) {
        buffer = new HashMap<Integer, Token>();
        buffer.putAll(message.buffer);
        timestamp = new Token(message.timestamp);
    }

    /**
     * Set the timestamp of the message from the provided VectorClock.
     *
     * @param timestamp
     */
    public void setTimestamp(Token timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Copy the provided buffer from a process into the message buffer.
     *
     * @param buffer
     */
    public void setBuffer(Map<Integer, Token> buffer) {
        this.buffer = buffer;
    }

    /**
     * Overriding parent String format for better readibility.
     *
     * @return String
     */
    @Override
    public String toString() {
        if (buffer == null) {
            return "{" + "- , " +  timestamp + "}";
        }

        return "{" + buffer + ", " + timestamp + "}";
    }

    /**
     * Provide the timestamp of the message.
     *
     * @return VectorClock
     */
    public Token getTimestamp() {
        return timestamp;
    }

    /**
     * Helper function for unit testing to retrieve messageBuffer and check if messages are indeed buffered.
     *
     * @return Map<Integer, VectorClock>
     */
    public Map<Integer, Token> getBuffer() {
        return buffer;
    }
}
