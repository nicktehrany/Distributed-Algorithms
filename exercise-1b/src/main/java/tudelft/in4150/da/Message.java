package tudelft.in4150.da;

import java.io.Serializable;

/**
 * Message class to construct messages to be passed between processes.
 */
public class Message implements Serializable {

    /**
     * Default serializable UID.
     */
    private static final long serialVersionUID = 1L;

    private int messageID;

    // Initialize to an unused process ID.
    private int senderID = -1;
    private int receiverID = -1;

    /**
     * Construct a message with a message ID, the sender's ID, and the receiver's ID.
     * @param messageID
     * @param senderID
     * @param receiverID
     */
    public Message(int messageID, int senderID, int receiverID) {
        this.messageID = messageID;
        this.senderID = senderID;
        this.receiverID = receiverID;
    }

    @Override
    public String toString() {
        return "{" + messageID + ", " + senderID + ", " + receiverID + "}";
    }
}
