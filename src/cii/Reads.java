package cii;

import java.io.Serializable;
import java.util.ArrayList;

public class Reads {
	
	private String identifier;
	private int length;
	private String xy;
	private String region;
	private String run;

	private ArrayList<String> quality_score;
	private double average_quality_score;
	private Boolean with_primer;
	private Boolean with_adapter;
	

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getXy() {
		return xy;
	}

	public void setXy(String xy) {
		this.xy = xy;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getRun() {
		return run;
	}

	public void setRun(String run) {
		this.run = run;
	}

	public ArrayList<String> getQuality_score() {
		return quality_score;
	}

	public void setQuality_score(ArrayList<String> qualityScore) {
		quality_score = qualityScore;
	}

	public double getAverage_quality_score() {
		return average_quality_score;
	}

	public void setAverage_quality_score(double averageQualityScore) {
		average_quality_score = averageQualityScore;
	}

	public Boolean getWith_primer() {
		return with_primer;
	}

	public void setWith_primer(Boolean withPrimer) {
		with_primer = withPrimer;
	}

	public Boolean getWith_adapter() {
		return with_adapter;
	}

	public void setWith_adapter(Boolean withAdapter) {
		with_adapter = withAdapter;
	}

	

	public void get_quality_score_avage() {
		int sum = 0;

		for (int i = 0; i < quality_score.size(); i++) {
			sum += Integer.valueOf(quality_score.get(i));
		}
		average_quality_score = sum / quality_score.size();
	}

}
