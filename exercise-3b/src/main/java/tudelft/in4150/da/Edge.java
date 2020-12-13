package tudelft.in4150.da;

public class Edge {
    private String node;
    private Integer weight;
    private adjState state;

    public Edge(String name, Integer weight) {
        node = name;
        this.weight = weight;
        state = adjState.Q_in_MST;
    }

    public String getNode() {
        return node;
    }

    public Integer getWeight() {
        return weight;
    }

    public adjState getState() {
        return state;
    }

    public void setState(adjState state) {
        this.state = state;
    }
}