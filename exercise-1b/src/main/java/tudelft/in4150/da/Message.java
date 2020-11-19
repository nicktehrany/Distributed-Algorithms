package tudelft.in4150.da;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Message class to construct messages to be passed between processes.
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<Integer, VectorClock> buffer;
    private VectorClock timestamp;

    /**
     * Construct a message
     */
    public Message(int numProcesses) {
        buffer = new HashMap<Integer, VectorClock>();
        timestamp = new VectorClock(numProcesses);
    }

    public Message(Message message) {
        buffer = new HashMap<Integer, VectorClock>();
        buffer.putAll(message.buffer);
        timestamp = new VectorClock(message.timestamp);
    }

    /**
     * @param timestamp
     */
    public void setTimestamp(VectorClock timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @param buffer
     */
    public void setBuffer(Map<Integer, VectorClock> buffer) {
        this.buffer = buffer;
    }

    /**
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
     * @return VectorClock
     */
    public VectorClock getTimestamp() {
        return timestamp;
    }

    /**
     * @return Map<Integer, VectorClock>
     */
    public Map<Integer, VectorClock> getBuffer() {
        return buffer;
    }
}
