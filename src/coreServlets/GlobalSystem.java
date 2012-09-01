package coreServlets;


import java.util.ArrayList;


public class GlobalSystem {

	protected static GlobalSystem _instance = null;

	ArrayList<Partition> partitions;
	ArrayList<String> timeList;
	float time, dt;
	int total=0, reportInterval=0;
	rrandom random;
	String currentModel = "";
	String simulationMethod = "";
	

	private GlobalSystem() {
	} // construction function
	
	// there will be one global system containing all the partitions.
	public static GlobalSystem getInstance() {
		if (_instance != null)
			return _instance;
		else {
			_instance = new GlobalSystem();
			return _instance;
		}
	}
	

	public ArrayList<Partition> getPartitions() {
		return partitions;
	}

	public void setPartitions(ArrayList<Partition> partitions) {
		this.partitions = partitions;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public float getDt() {
		return dt;
	}

	public void setDt(float dt) {
		this.dt = dt;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getReportInterval() {
		return reportInterval;
	}

	public void setReportInterval(int reportInterval) {
		this.reportInterval = reportInterval;
	}
	
	public ArrayList<String> getTimeList() {
		return timeList;
	}

	public void setTimeList(ArrayList<String> timeList) {
		this.timeList = timeList;
	}
	
	public String getCurrentModel() {
		return currentModel;
	}

	public void setCurrentModel(String currentModel) {
		this.currentModel = currentModel;
	}


}
