package tudelft.in4150.da;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    public String sender;
    Type mType;

    enum Type {
        Connect,
        Initiate,
        Test,
        Report,
        Accept,
        Reject,
        ChangeRoot
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

class Report extends Message {
    private static final long serialVersionUID = 1L;
    private int weight;

    Report(int weight) {
        this.weight = weight;
        super.mType = Type.Report;
    }

    public int getWeight() {
        return weight;
    }
}

class Accept extends Message {
    private static final long serialVersionUID = 1L;

    Accept() {
        super.mType = Type.Report;
    }
}

class Reject extends Message {
    private static final long serialVersionUID = 1L;

    Reject() {
        super.mType = Type.Report;
    }
}

class ChangeRoot extends Message {
    private static final long serialVersionUID = 1L;

    ChangeRoot() {
        super.mType = Type.ChangeRoot;
    }
}