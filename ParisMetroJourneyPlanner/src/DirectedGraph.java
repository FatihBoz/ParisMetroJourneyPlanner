import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

public class DirectedGraph<T> implements GraphInterface<T> {
    private HashMap<T, Vertex<T>> vertices;

    public DirectedGraph() {
        this.vertices = new HashMap<>();
    }

    public void addEdge(T source, T destination, int weight, String lineID) {
        Vertex<T> sourceVertex = vertices.get(source);
        //Get source vertex
        Vertex<T> destinationVertex = vertices.get(destination);
        //Get destination vertex

        if (sourceVertex != null && destinationVertex != null && sourceVertex.hasEdge(destination)) {
        	//if there is already an edge between the source and the destination
            return;
        } else {
            if (vertices.get(source) == null) {
            	//if the source vertex is null
                sourceVertex = new Vertex<>(source);
              //Create a vertex that given source as a parameter
                vertices.put(source, sourceVertex);
                //add new vertex to hash map
            }

            if (vertices.get(destination) == null) {
            	//if the destination vertex is null
                destinationVertex = new Vertex<>(destination);
                //Create a vertex that given destination as a parameter
                vertices.put(destination, destinationVertex);
                //Add the new vertex to hashmap
            }

            Edge<T> edge = new Edge<>(sourceVertex, destinationVertex, weight, lineID);
            //Create an edge between source and destination
            sourceVertex.addEdge(edge);
            //Add the edge to source's edge list
        }
    }

    public void print() {
        for (Vertex<T> v : vertices.values()) {
            System.out.print(v.getName() + " -> ");

            Iterator<Vertex<T>> neighbors = v.getNeighborIterator();
            while (neighbors.hasNext()) {
                Vertex<T> n = neighbors.next();
                //Get next neighbor
                System.out.print(n.getName() + " ");
                //print neighbor's name
            }
            System.out.println();
        }
    }

    public Iterable<Vertex<T>> vertices() {
        return vertices.values();
    }

    public int size() {
        return vertices.size();
    }

    private void resetVertices() {
        for (Vertex<T> v : vertices.values()) {
            v.unvisit();
            v.setCost(0);
            v.setParent(null);
        }
    }

    public int getShortestPaths(T start, T end, String bannedLine) {
        resetVertices();
        //reset all the informations
        PriorityQueue<Vertex<T>> q = new PriorityQueue <>();
        //use priority queue to sort the costs
        Vertex<T> originVertex = vertices.get(start);
        //create an source vertex with given parameter
        q.add(originVertex);
        //Add source vertex to priority queue
        
        while (!q.isEmpty()) {
            Vertex<T> cv = q.remove();
            //remove the first element of the priority queue and assign to another vertex
            cv.visit();
            //sign it as visited
            Vertex<T> vertex = null;

            for (Edge<T> edge : cv.getEdges()) {
                if (bannedLine != null && edge.getLineID().equals(bannedLine)) {
                	//if banned line is the same as current line, it is over.
                    continue;
                }

                vertex = edge.getDestination();
                //get an edge of current vertex and get its destination
                if (!vertex.isVisited()) {
                	vertex.visit();
                	//sign as visited
                    vertex.setCost(cv.getCost() + 1);
                    //set its cost
                    vertex.setParent(cv);
                    //set current vertex as its parent
                    q.add(vertex);
                    //add to priority queue

                    if (vertex.getName().equals(end)) {
                    	//if the end vertex is found
                        return (int) vertex.getCost();
                        //return the cost
                    }
                }
            }
        }
        return -1;
    }

 // Method to find the cheapest paths from a start vertex to an end vertex, avoiding a specified line
    public double getCheapestPaths(T start, T end, String bannedLine) {
        // Reset vertices to unvisited and initialize total cost
        resetVertices();
        double totalCost = 0;
        // Priority queue to store vertices based on their cost
        PriorityQueue<Vertex<T>> pq = new PriorityQueue<>();
        // Get the origin vertex from the vertices map
        Vertex<T> originVertex = vertices.get(start);
        // Add the origin vertex to the priority queue
        pq.add(originVertex);

        // Continue processing until priority queue is empty
        while (!pq.isEmpty()) {
            // Remove and mark the current vertex as visited
            Vertex<T> cv = pq.remove();
            cv.visit();
            Vertex<T> vertex = null;

            // Iterate through the edges of the current vertex
            for (Edge<T> edge : cv.getEdges()) {
                // Check if the current edge belongs to the banned line
                if (bannedLine != null && edge.getLineID().equals(bannedLine)) {
                    continue; // Skip this edge if it belongs to the banned line
                }

                // Get the destination vertex of the edge
                vertex = edge.getDestination();
                // Check if the destination vertex has not been visited
                if (!vertex.isVisited()) {
                	// Mark the destination vertex as visited
                	vertex.visit();
                    // Calculate the total cost to reach the destination vertex
                    totalCost = edge.getWeight() + cv.getCost();

                    // Update the cost and parent of the destination vertex if it's cheaper or unvisited
                    if (totalCost <= vertex.getCost() || vertex.getCost() == 0) {
                        vertex.setCost(totalCost);
                        vertex.setParent(cv);
                        pq.add(vertex); // Add the destination vertex to the priority queue
                    }

                    // Check if the destination vertex is the end vertex
                    if (vertex.getName().equals(end)) {
                        return totalCost; // Return the total cost if the end vertex is reached
                    }
                }
            }
        }

        return -1; // Return -1 if no path is found
    }

    // Method to build and return the path from start vertex to end vertex
    public Stack<Vertex<T>> buildPath(T start, T end) {
        // Initialize a stack to store the path
        Stack<Vertex<T>> s = new Stack<>();
        // Get the current vertex as the end vertex
        Vertex<T> currentVertex = vertices.get(end);

        // Iterate backward from end vertex to start vertex
        while (currentVertex != null) {
            s.push(currentVertex); // Push the current vertex onto the stack
            currentVertex = currentVertex.getParent(); // Move to the parent vertex
        }

        return s; // Return the stack containing the path
    }

    // Method to perform breadth-first traversal starting from a given origin vertex
    public Queue<T> getBreadthFirstTraversal(T origin) {
        // Reset vertices to unvisited and initialize traversal order and vertex queue
        resetVertices();
        Queue<T> traversalOrder = new LinkedList<>();
        Queue<T> vertexQueue = new LinkedList<>();

        // Get the origin vertex from the vertices map and mark it as visited
        Vertex<T> originVertex = vertices.get(origin);
        originVertex.visit();

        // Add the origin vertex to the traversal order and vertex queue
        traversalOrder.add(origin);
        vertexQueue.add(origin);

        // Continue processing until the vertex queue is empty
        while (!vertexQueue.isEmpty()) {
            // Remove the front vertex from the vertex queue
            T frontVertex = vertexQueue.remove();
            // Iterate through the neighbors of the front vertex
            Iterator<Vertex<T>> neighbors = vertices.get(frontVertex).getNeighborIterator();

            while (neighbors.hasNext()) {
                // Get the next neighbor vertex
                Vertex<T> nextNeighbor = neighbors.next();
                // Check if the neighbor vertex has not been visited
                if (!nextNeighbor.isVisited()) {
                    // Mark the neighbor vertex as visited
                    nextNeighbor.visit();
                    // Add the neighbor vertex to the traversal order and vertex queue
                    traversalOrder.add(nextNeighbor.getName());
                    vertexQueue.add(nextNeighbor.getName());
                }
            }
        }

        return traversalOrder; // Return the traversal order
    }

 // Method to perform depth-first traversal starting from a given origin vertex
    public Queue<T> getDepthFirstTraversal(T origin) {
        // Reset vertices to unvisited and initialize traversal order and vertex stack
        resetVertices();
        Queue<T> traversalOrder = new LinkedList<>();
        Stack<T> vertexStack = new Stack<>();

        // Get the origin vertex from the vertices map and mark it as visited
        Vertex<T> originVertex = vertices.get(origin);
        originVertex.visit();

        // Add the origin vertex to the traversal order and vertex stack
        traversalOrder.add(origin);
        vertexStack.add(origin);

        // Continue processing until the vertex stack is empty
        while (!vertexStack.isEmpty()) {
            // Get the top vertex from the vertex stack
            Vertex<T> topVertex = vertices.get(vertexStack.peek());

            // Iterate through the neighbors of the top vertex
            Iterator<Vertex<T>> neighbors = topVertex.getNeighborIterator();

            // Check if there are neighbors
            if (neighbors.hasNext()) {
                // Get the next neighbor vertex
                Vertex<T> nextNeighbor = neighbors.next();

                // Check if the neighbor vertex has not been visited
                if (!nextNeighbor.isVisited()) {
                    // Mark the neighbor vertex as visited
                    nextNeighbor.visit();
                    // Add the neighbor vertex to the traversal order and vertex stack
                    traversalOrder.add(nextNeighbor.getName());
                    vertexStack.add(nextNeighbor.getName());
                }
            } else {
                // If no unvisited neighbor, pop the vertex from the stack
                vertexStack.pop();
            }
        }

        return traversalOrder; // Return the traversal order
    }

    public int getShortestPath(T begin, T end) {
        return getShortestPaths(begin , end , null);
    }

    public int getCheapestPath(T begin, T end) {
        return (int) getCheapestPaths(begin , end ,null);
    }

    public void addEdge(T source, T destination, int weight) {
    	addEdge(source,destination,weight,null);
    }
}