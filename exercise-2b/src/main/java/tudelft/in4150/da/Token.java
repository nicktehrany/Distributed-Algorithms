package tudelft.in4150.da;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Token class which is used as the Vector token for each of the processes and the timestamps in messages.
 */
public class Token implements Serializable {
    private static final long serialVersionUID = 1L;
    private int[] LN;

    /**
     * Token constructor, initialize token with 0s on Vectrotoken construction.
     *
     * @param processes
     */
    public Token(int processes) {
        LN = new int[processes];
        Arrays.fill(LN, 0);
    }

    /**
     * Token copy constructor with new LN length.
     *
     * @param token
     */
    public Token(Token token, int length) {
        int[] temp = new int[length];
        Arrays.fill(temp, 0);
        for (int i = 0; i < token.LN.length; i++) {
            temp[i] = token.LN[i];
        }
        LN = temp;
    }

    /**
     * Get the counter of the token satisfied requests for a process.
     *
     * @param index
     */
    public int getValue(int index) {
        return LN[index];
    }

    /**
     * Set the token satisfied request counter.
     * @param index
     * @param value
     */
    public void setValue(int index, int value) {
        LN[index] = value;
    }

    /**
     * 
     */
    @Override
    public String toString() {
        return Arrays.toString(LN);
    }

    public int getLength() {
        return LN.length;
    }

}
