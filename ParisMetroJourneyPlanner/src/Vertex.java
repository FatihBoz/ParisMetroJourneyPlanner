import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Vertex<T> implements VertexInterface<T> {
    private T name;
    private ArrayList<Edge<T>> edgeList;
    private Vertex<T> parent;
    private boolean visited;
    private double cost;

    public Vertex(T name) {
        this.name = name;
        edgeList = new ArrayList<>();
        parent = null;
        visited = false;
    }

    public void addEdge(Edge<T> e) {
        edgeList.add(e);
    }

    public ArrayList<Edge<T>> getEdges() {
        return this.edgeList;
    }

    public T getName() {
        return name;
    }

    public void setName(T name) {
        this.name = name;
    }

    public Vertex<T> getParent() {
        return parent;
    }

    public void setParent(Vertex<T> parent) {
        this.parent = parent;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void visit() {
        this.visited = true;
    }

    public void unvisit() {
        this.visited = false;
    }

    public boolean isVisited() {
        return this.visited;
    }

    public Vertex<T> getUnvisitedNeighbor() {
        Vertex<T> result = null;

        Iterator<Vertex<T>> neighbors = getNeighborIterator();
        while (neighbors.hasNext() && (result == null)) {
            Vertex<T> nextNeighbor = neighbors.next();
            if (!nextNeighbor.isVisited())
                result = nextNeighbor;
        }
        return result;
    }

    public boolean hasEdge(T neighbor) {
        boolean found = false;
        Iterator<Vertex<T>> neighbors = getNeighborIterator();
        while (neighbors.hasNext()) {
            Vertex<T> nextNeighbor = neighbors.next();
            if (nextNeighbor.getName().equals(neighbor)) {
                found = true;
                break;
            }
        }
        return found;
    }

    public Iterator<Vertex<T>> getNeighborIterator() {
        return new NeighborIterator();
    }

    private class NeighborIterator implements Iterator<Vertex<T>> {
        int edgeIndex = 0;

        private NeighborIterator() {
            edgeIndex = 0;
        }

        public boolean hasNext() {
            return edgeIndex < edgeList.size();
        }

        public Vertex<T> next() {
            Vertex<T> nextNeighbor = null;

            if (hasNext()) {
                nextNeighbor = edgeList.get(edgeIndex).getDestination();
                edgeIndex++;
            } else
                throw new NoSuchElementException();

            return nextNeighbor;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public int compareTo(Vertex<T> o) {
        int COST = (int) (cost * 1000);
        int oCost = (int) (o.getCost() * 1000);

        return Integer.compare(COST, oCost);
    }

}