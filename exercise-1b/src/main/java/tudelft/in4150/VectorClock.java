package tudelft.in4150;

public class VectorClock {
    public int[] clocks;

    public VectorClock(int processes) {
        this.clocks = new int[processes];
    }
}
