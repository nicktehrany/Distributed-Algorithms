package tudelft.in4150.da;

/**
 * Core class representign the core of a fragment with the weight of the edge, and the fragment level.
 * @return
 */
public class Core {
    private int weight;
    private int level;

    Core(int weight, int level) {

        this.weight = weight;
        this.level = level;
    }

    public int getWeight() {
        return weight;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "Edge: " + weight + " Level: " + level;
    }
}
