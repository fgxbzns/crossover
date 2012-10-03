package cii;

import java.io.Serializable;
import java.util.ArrayList;

public class Reads {
	
	private String identifier;
	private int length;
	private String xy;
	private String region;
	private String run;
	private String sequence;
	
	final private String primer = "CGCCGTTTCCCAGTAGGTCTC";
	final private String adaptor = "ACTGAGTGGGAGGCAAGGCACACAGGGGATAGG";	

	private ArrayList<String> quality_score;
	private double average_quality_score;
	private Boolean contain_primer = false;
	private Boolean contain_adaptor = false;
	private Boolean contain_both = false;
	

	public Boolean getContain_primer() {
		return contain_primer;
	}

	public void setContain_primer(Boolean containPrimer) {
		contain_primer = containPrimer;
	}

	public Boolean getContain_adaptor() {
		return contain_adaptor;
	}

	public void setContain_adaptor(Boolean containAdaptor) {
		contain_adaptor = containAdaptor;
	}

	public Boolean getContain_both() {
		return contain_both;
	}

	public void setContain_both(Boolean containBoth) {
		contain_both = containBoth;
	}


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

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
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

	public void set_quality_score_avage() {
		double sum = 0;

		for (int i = 0; i < quality_score.size(); i++) {
			sum += Integer.valueOf(quality_score.get(i));
		}
		average_quality_score = sum / quality_score.size();
	}
	
	public void check_if_contains_primer() {
		int index = sequence.indexOf(primer);
		if (index >= 0) {
			contain_primer = true;
		}
	}
	
	public void check_if_contains_adatpor() {
		int index = sequence.indexOf(adaptor);
		if (index >= 0) {
			contain_adaptor = true;
		}
	}
	
	public void check_if_contains_both() {
		if(contain_primer && contain_adaptor) {
			contain_both = true;
		}		
	}
	
	
	


}
