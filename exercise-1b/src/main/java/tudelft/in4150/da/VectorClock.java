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

    @Override
    public String toString() {
        String clock = Arrays.toString(clocks);
        String formattedClock = '(' + clock.substring(1, clock.length() - 1) + ')';
        return formattedClock;
    }

	public void incClock(int id) {
        clocks[id - 1]++;
	}
}
