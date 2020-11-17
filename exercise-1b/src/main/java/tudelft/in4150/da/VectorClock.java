package tudelft.in4150.da;

import java.io.Serializable;
import java.util.Arrays;

public class VectorClock implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public int[] clocks;

    public VectorClock(int processes) {
        clocks = new int[processes];
        Arrays.fill(clocks, 0);
    }

    // Copy Constructor
    public VectorClock(VectorClock clock) {
        clocks = new int[clock.clocks.length];
        for (int i = 0; i < clock.clocks.length; i++) {
            this.clocks[i] = clock.clocks[i];
        }
    }

    @Override
    public String toString() {
        String clock = Arrays.toString(clocks);
        String formattedClock = '(' + clock.substring(1, clock.length() - 1) + ')';
        return formattedClock;
    }

	public void incClock(int id) {
        clocks[id - 1]++;
	}

	public boolean greaterEqual(VectorClock timestamp) {
        for (int i = 0; i < clocks.length; i++) {
            if (clocks[i] < timestamp.clocks[i])
                return false;
        }
		return true;
	}

	public void setMax(VectorClock bufferTimestamp) {
        for (int i = 0; i < clocks.length; i++) {
            if (clocks[i] < bufferTimestamp.clocks[i])
                clocks[i] = bufferTimestamp.clocks[i];
        }
	}
}
