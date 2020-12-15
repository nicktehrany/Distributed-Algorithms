package tudelft.in4150.da;

import java.io.Serializable;

/**
 * Message class from which all different message types inherit.
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    public String sender;
    public Type mType;

    enum Type {
        Connect,
        Initiate,
        Test,
        Report,
        Accept,
        Reject,
        ChangeRoot,
        Finished
    }
}

/**
 * Connect message for issuing connects to processes.
 */
class Connect extends Message {
    private static final long serialVersionUID = 1L;
    private int level;

    /**
     * Connect message constructor.
     * @param level
     * @param sender
     */
    Connect(int level, String sender) {
        this.level = level;
        super.sender = sender;
        super.mType = Type.Connect;
    }

    /**
     * Get the level of the connect message.
     * @return level
     */
    public int getLevel() {
        return level;
    }

}

/**
 * Initiate message for initiating merge or absorb with fragments.
 */
class Initiate extends Message {
    private static final long serialVersionUID = 1L;
    private int level;
    private int fragmentName;
    private State state;

    /**
     * Initiate message constructor.
     * @param level
     * @param name
     * @param state
     * @param sender
     */
    Initiate(int level, int name, State state, String sender) {
        this.level = level;
        this.fragmentName = name;
        this.state = state;
        super.mType = Type.Initiate;
        super.sender = sender;
    }

    /**
     * Get level of the message.
     * @return level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Get fragment name sent in the message.
     * @return fragmentName
     */
    public int getFragmentName() {
        return fragmentName;
    }

    /**
     * Get state of the initate message.
     * @return state
     */
    public State getState() {
        return state;
    }
}

/**
 * Test message for testing if edges are in the same fragment.
 */
class Test extends Message {
    private static final long serialVersionUID = 1L;
    private int level;
    private int fragmentName;

    /**
     * Test message constructor.
     * @param level
     * @param name
     * @param sender
     */
    Test(int level, int name, String sender) {
        this.level = level;
        this.fragmentName = name;
        super.mType = Type.Test;
        super.sender = sender;
    }

    /**
     * Get level of the message.
     * @return level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Get fragment name sent in the message.
     * @return fragmentName
     */
    public int getFragmentName() {
        return fragmentName;
    }
}

/**
 * Report message for reporting potential MOE to the core of the fragment.
 */
class Report extends Message {
    private static final long serialVersionUID = 1L;
    private int weight;

    /**
     * Report message constructor.
     * @param weight
     * @param sender
     */
    Report(int weight, String sender) {
        this.weight = weight;
        super.mType = Type.Report;
        super.sender = sender;
    }

    /**
     * Get weight of edge sent in message.
     * @return weight
     */
    public int getWeight() {
        return weight;
    }
}

/**
 * Accept message for replying to test message.
 */
class Accept extends Message {
    private static final long serialVersionUID = 1L;

    /**
     * Accept message constructor.
     * @param sender
     */
    Accept(String sender) {
        super.mType = Type.Accept;
        super.sender = sender;
    }
}

/**
 * Reject message for replying to test message.
 */
class Reject extends Message {
    private static final long serialVersionUID = 1L;

    /**
     * Reject message constructor.
     * @param sender
     */
    Reject(String sender) {
        super.mType = Type.Reject;
        super.sender = sender;
    }
}

/**
 * ChangeRoot mesage to send from core to MOE to change the core.
 */
class ChangeRoot extends Message {
    private static final long serialVersionUID = 1L;

    /**
     * ChangeRoot message constructor.
     * @param sender
     */
    ChangeRoot(String sender) {
        super.mType = Type.ChangeRoot;
        super.sender = sender;
    }
}

/**
 * Finished message is not part of the algorithm but only to broadcast a message once done to cleanup and terminate.
 */
class Finished extends Message {
    private static final long serialVersionUID = 1L;

    /**
     * Finished message constructor.
     */
    Finished() {
        super.mType = Type.Finished;
    }
}
