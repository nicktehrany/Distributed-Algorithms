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
    private int receiverID;
    private int senderID;
    private Map<Integer, VectorClock> buffer;
    private VectorClock timestamp;

    /**
     * Construct a message with a the sender's ID, and the receiver's ID.
     * @param receiverID
     */
    public Message(int senderID, int receiverID) {
        this.senderID = senderID;
        this.receiverID = receiverID;
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
            return "{" + senderID + ", " + receiverID + ", " + "- , "+  timestamp + "}";

        return "{" + senderID + ", " + receiverID + ", " + buffer + ", " + timestamp + "}";
    }
}
