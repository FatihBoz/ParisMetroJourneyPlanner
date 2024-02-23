import java.util.Queue;

public interface GraphAlgorithmsInterface<T> {

	public int getShortestPath(T begin , T end);
	
	public int getCheapestPath(T begin , T end);
	
	public Queue<T> getBreadthFirstTraversal(T origin);
	
	public Queue<T> getDepthFirstTraversal(T origin);
	
}
