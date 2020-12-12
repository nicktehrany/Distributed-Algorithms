package tudelft.in4150.da;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    Type mType;

    enum Type {
        Connect,
        Initiate
    }

}

class Connect extends Message {
    private static final long serialVersionUID = 1L;
    private int level;
    private String sender;

    Connect(int level, String sender) {
        this.level = level;
        this.sender = sender;
        super.mType = Type.Connect;
    }

    public int getLevel() {
        return level;
    }

    public String getSender() {
        return sender;
    }
}