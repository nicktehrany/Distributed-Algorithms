package tudelft.in4150.da;

import java.io.Serializable;

import tudelft.in4150.VectorClock;

/**
 * Message class to construct messages to be passed between processes.
 */
public class Message implements Serializable {

    /**
     * Default serializable UID.
     */
    private static final long serialVersionUID = 1L;
    private int receiverID;
    private int senderID;
    private VectorClock timestamp;

    /**
     * Construct a message with a message ID, the sender's ID, and the receiver's ID.
     * @param receiverID
     */
    public Message(int senderID, int receiverID) {
        this.senderID = senderID;
        this.receiverID = receiverID;
    }

    public void setTimestamp(VectorClock timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "{" + senderID + ", " + receiverID + ", " +  timestamp + "}";
    }
}
