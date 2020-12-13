package tudelft.in4150.da;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    public String sender;
    Type mType;

    enum Type {
        Connect,
        Initiate,
        Test
    }
}

class Connect extends Message {
    private static final long serialVersionUID = 1L;
    private int level;

    Connect(int level, String sender) {
        this.level = level;
        super.sender = sender;
        super.mType = Type.Connect;
    }

    public int getLevel() {
        return level;
    }

}

class Initiate extends Message {
    private static final long serialVersionUID = 1L;
    private int level;
    private int fragmentName;
    private State state;

    Initiate(int level, int name, State state, String sender) {
        this.level = level;
        this.fragmentName = name;
        this.state = state;
        super.mType = Type.Initiate;
        super.sender = sender;
    }

    public int getLevel() {
        return level;
    }

    public int getFragmentName() {
        return fragmentName;
    }

    public State getState() {
        return state;
    }
}

class Test extends Message {
    private static final long serialVersionUID = 1L;
    private int level;
    private int fragmentName;

    Test(int level, int name, String sender) {
        this.level = level;
        this.fragmentName = name;
        super.mType = Type.Test;
        super.sender = sender;
    }

    public int getLevel() {
        return level;
    }

    public int getFragmentName() {
        return fragmentName;
    }
}