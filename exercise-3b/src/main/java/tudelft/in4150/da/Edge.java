package tudelft.in4150.da;

public class Edge {
    private String node;
    private Integer weight;
    private Edgestate state;

    public Edge(String name, Integer weight) {
        node = name;
        this.weight = weight;
        state = Edgestate.Q_in_MST;
    }

    public String getNode() {
        return node;
    }

    public Integer getWeight() {
        return weight;
    }

    public Edgestate getState() {
        return state;
    }

    public void setState(Edgestate state) {
        this.state = state;
    }
}