package tudelft.in4150.da;

/**
 * Class for constructing edges to an adjacent node with a weight and a certain state.
 */
public class Edge {
    private String node;
    private Integer weight;
    private Edgestate state;

    /**
     * Edge constructor.
     * @param name
     * @param weight
     */
    public Edge(String name, Integer weight) {
        node = name;
        this.weight = weight;
        state = Edgestate.Q_in_MST;
    }

    /**
     * Get the name of the adjacent node.
     * @return node
     */
    public String getNode() {
        return node;
    }

    /**
     * Get the weight of the edge.
     * @return weight
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * Get the state of the adjacent node.
     * @return state
     */
    public Edgestate getState() {
        return state;
    }

    /**
     * Set the state of an adjacent node.
     * @param state
     */
    public void setState(Edgestate state) {
        this.state = state;
    }
}
