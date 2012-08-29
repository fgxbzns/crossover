package crossover;

public class Event {

	
	
	private String eventID;
	private float timePoint;
	private String targetName;
	private String targetProperty; //number, my_concentration, chemostat
	private String action;  //changeTo, add, subtract
	private double value;
	
	
	public String getEventID() {
		return eventID;
	}
	public void setEventID(String eventID) {
		this.eventID = eventID;
	}
	public float getTimePoint() {
		return timePoint;
	}
	public void setTimePoint(float timePoint) {
		this.timePoint = timePoint;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public String getTargetProperty() {
		return targetProperty;
	}
	public void setTargetProperty(String targetProperty) {
		this.targetProperty = targetProperty;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
}
