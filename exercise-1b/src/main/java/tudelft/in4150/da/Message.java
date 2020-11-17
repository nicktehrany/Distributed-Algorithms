package tudelft.in4150.da;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Message class to construct messages to be passed between processes.
 */
public class Message implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Map<Integer, VectorClock> buffer;
    private VectorClock timestamp;

    /**
     * Construct a message with a the sender's ID, and the receiver's ID.
     * @param receiverID
     */
    public Message() {
    }

    public void setTimestamp(VectorClock timestamp) {
        this.timestamp = timestamp;
    }

    public void setBuffer(Map<Integer, VectorClock> buffer) {
        this.buffer = buffer;
    }

    @Override
    public String toString() {
        if (buffer == null)
            return "{" + "- , " +  timestamp + "}";

        return "{" + buffer + ", " + timestamp + "}";
    }

	public VectorClock getTimestamp() {
        return timestamp;
	}

	public Map<Integer, VectorClock> getBuffer() {
        return buffer;
	}
}
