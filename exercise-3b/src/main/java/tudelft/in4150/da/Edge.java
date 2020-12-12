package tudelft.in4150.da;

public class Edge {
    private String node;
    private Integer weight;
    public adjState state;

    enum adjState {
        Q_in_MST,
        in_MST,
        not_in_MST
    }

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
}