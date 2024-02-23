import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;


public class Test {
	
	static DirectedGraph<String> graph = new DirectedGraph<>();
	
	static boolean firstPath = true;
	
	static ArrayList<String> bannedLines = new ArrayList<String>();
	
	static int suggestionCount = 0;
	
	public static void main(String[] args) { 
		 
		
		 ArrayList<Station> sl = new ArrayList<>();  //station List
		 
		 
		
		try {
			Scanner sc = new Scanner(new File("Paris_RER_Metro.csv"));
			
			sc.nextLine();
			while (sc.hasNextLine())  
			{  
				
				String[] str = sc.nextLine().split(",");
 				
				Station station = new Station(str[1],Integer.parseInt(str[2]),Integer.parseInt(str[3]),str[5]);
				
				sl.add(station);

			}
			
			sc.close();
			
		}
		catch(Exception e) {
			
			System.out.println(e);
			
			}
				
		for(int i = 0; i < sl.size() -1;i++) {
					
			//name -> 1 , time -> 2 , stop sequence -> 3, line id  -> 5;
			
			if(Math.abs(sl.get(i).getStopSequence() - (sl.get(i+1).getStopSequence())) == 1) { 
				 //check if the stop sequences are consecutive.
				int weight = Math.abs(sl.get(i).getArrivalTime() - sl.get(i+1).getArrivalTime()); //calculate the weight
				//order does not matter because of the absolute value
				
				graph.addEdge(sl.get(i).getName(), sl.get(i+1).getName(), weight , sl.get(i).getLineID()); 
			}
		}						

		Scanner myObj = new Scanner(System.in);
		
		//Take inputs
		System.out.print("Origin station : ");
		String station1 = myObj.nextLine();
		
		System.out.print("Destination station : ");
		String station2 = myObj.nextLine();
		
		try {
			//Choose the preferences
			System.out.println("\n1 - Shortest Path\n2 - Cheapest Path");
			System.out.print("Preferetion(1 or 2) : ");
			int preferetion = myObj.nextInt();
			System.out.println();	
			
			
			String start = station1;
			String end = station2;
			
			
			selectPathType(preferetion , start , end , null);
			
			//Give more suggestions up to 3.
			while(suggestionCount < 3) {
				if(bannedLines.isEmpty())break;
				//if there is no banned lines , it means there is no path has founded
				selectPathType(preferetion , start , end , bannedLines.get(0));
				
				bannedLines.remove(0);
				//this line is banned.Remove it from the banned lines.
			}
		}
		
		catch(Exception e) {
			System.out.println("There is a problem. Check the Stations' name or your preferetion.");
		}
		
		
		myObj.close();
		
		}
	
	
	
	
	public static void selectPathType(int preferetion , String start , String end , String bannedLine) {
		//
		switch(preferetion) {
		
		case 1:
			int stopCount = graph.getShortestPaths(start, end , bannedLine);
			if(stopCount != -1) {
				//if there is a path
				suggestionCount++;
				//increase suggestion count by 1.
				display(start,end);
				//Display the path.
				System.out.println("\n"+stopCount+" station\n\n");
				//Print how many stops has found.
			}
			
			break;
			
		case 2:
			double time = graph.getCheapestPaths(start, end , bannedLine);
			if(time != -1) {
				//if there is a path
				suggestionCount++;
				//increase suggestion count by 1.
				display(start,end);
				//Display the path.
				System.out.println("\n"+convertSecondToMinute(time)+" min\n\n");
				//Print how much time this path needs.
			}
			
			break;
			
		default:
			System.out.println("Invalid Entry! Shortest Path has been selected.");
			int stopCount2 = graph.getShortestPaths(start, end , bannedLine);
			if(stopCount2 != -1) {
				//if there is a path
				suggestionCount++;
				//increase suggestion count by 1.
				display(start,end);
				//Display the path.
				System.out.println("\n"+stopCount2+" station\n\n");
				//Print how many stops has found.
			}
			
		}
	}
	
	
	
	public static double convertSecondToMinute(double second) {
		
		double realTime = (double) (second / 60);
		
		int roundedTime = (int) Math.round(realTime);
		
		return roundedTime;
	}
	
	
	
	public static void print(ArrayList<String> stationNames , String lineID , int stationCount , Vertex<String> currentVertex) {

			if(firstPath) {
				//Determine the first path's lines not to select again.
				bannedLines.add(lineID);
			}
			
			//Print necessary informations.
			stationNames.add((String) currentVertex.getName());
			System.out.println("Line "+lineID+":");
			System.out.print(stationNames.get(0)+" <-> "+stationNames.get(1)+"  ---> "+stationCount+" Station\n");
			stationNames.remove(0);
	}
	
	
	
	public static void display(String start, String end) { // Separate stations according to their line ID.
	    System.out.println();

	    int stationCount = -1; // Initialize the station count.
	    Stack<Vertex<String>> s = graph.buildPath(start, end); // Build the path using the graph.

	    ArrayList<String> stationNames = new ArrayList<String>(); // Initialize a list to store station names.

	    stationNames.add(start); // Add the starting station to the list.
	    String lineID = ""; // Initialize the line ID.

	    // Iterate through the path stack until it's empty.
	    while (!s.isEmpty()) {
	        Vertex<String> currentVertex = s.pop(); // Get the current vertex.
	        stationCount++; // Increment the station count.
	        Edge<String> edge = null; // Initialize the edge.

	        // Iterate through the edges of the current vertex to find the edge leading to the next station.
	        for (Edge<String> edgeToFind : currentVertex.getEdges()) {
	            // Check if the edge destination matches the next station in the stack.
	            if (!s.isEmpty() && edgeToFind.getDestination().equals(s.peek())) {
	                edge = edgeToFind; // Set the edge to the found edge.
	                break; 
	            }
	        }

	        // If an edge is found, check if the line ID changes.
	        if (edge != null) {
	            if ((!lineID.equalsIgnoreCase(edge.getLineID()) && !lineID.equals(""))) {
	                print(stationNames, lineID, stationCount, currentVertex); // Print the stations with the previous line ID.
	                stationCount = 0; // Reset the station count.
	            }

	            lineID = edge.getLineID(); // Update the line ID.
	        }

	        // If the stack is empty, print the remaining stations.
	        if (s.isEmpty()) {
	            print(stationNames, lineID, stationCount, currentVertex); // Print the stations with the last line ID.
	            stationCount = 0; // Reset the station count.
	        }
	    }

	    firstPath = false; 
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
