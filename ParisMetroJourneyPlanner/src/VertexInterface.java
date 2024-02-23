
public interface VertexInterface<T> extends Comparable<Vertex<T>>{

	public void visit();
	
	public void unvisit();
	
	public boolean isVisited();
	
	public void addEdge(Edge<T> e);
	
	public boolean hasEdge(T neighbor);
	
	public Vertex<T> getUnvisitedNeighbor();
	
	public void setCost(double cost);
	
	public double getCost();
	
	public Vertex<T> getParent();
}
