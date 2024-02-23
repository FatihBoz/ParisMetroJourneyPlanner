

public class Edge<T> {
    private Vertex<T> source;
    private Vertex<T> destination;
    private int weight;
    private String lineID;

    public Edge(Vertex<T> source, Vertex<T> destination, int weight, String lineID) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
        this.lineID = lineID;
    }

    public Vertex<T> getSource() {
        return source;
    }

    public void setSource(Vertex<T> source) {
        this.source = source;
    }

    public Vertex<T> getDestination() {
        return destination;
    }

    public void setDestination(Vertex<T> destination) {
        this.destination = destination;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getLineID() {
        return lineID;
    }

    public void setLineID(String lineID) {
        this.lineID = lineID;
    }
}