
public class Station {

	private String name;
	private int arrivalTime;
	private int stopSequence;
	private String lineID;
	
	public Station(String name , int arrivalTime , int stopID , String lineID) {
		this.name = name;
		this.arrivalTime = arrivalTime;
		this.stopSequence = stopID;
		this.lineID = lineID;
	}

	public String getName() {
		return name;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public int getStopSequence() {
		return stopSequence;
	}
	
	public String getLineID() {
		return lineID;
	}

}

