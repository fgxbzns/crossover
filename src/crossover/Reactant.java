package crossover;

import java.io.Serializable;
import java.util.ArrayList;

public class Reactant {

	private double NA = (double) 6.0221415e23; // Avogadro constant
	private double volume = (double) 1.0e-20;
	private double number;
	private double my_concentration;
	private double number_update;
	private String my_chemical_name;
	private Boolean is_enzyme;
	private String outputTo;
	
	private Boolean chemostat; // A chemostat is a bioreactor to which fresh
								// medium is continuously added, while culture
								// liquid is continuously removed to keep the
								// culture volume constant
	
	// store the number at each report point
	private ArrayList<Double> dataList = new ArrayList<Double>(); 
	
	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public double getNumber() {
		return number;
	}

	public void setNumber(double number) {
		this.number = number;
	}

	public double getMy_concentration() {
		return my_concentration;
	}

	public void setMy_concentration(double myConcentration) {
		my_concentration = myConcentration;
	}

	public double getNumber_update() {
		return number_update;
	}

	public void setNumber_update(double numberUpdate) {
		number_update = numberUpdate;
	}

	public String getMy_chemical_name() {
		return my_chemical_name;
	}

	public void setMy_chemical_name(String myChemicalName) {
		my_chemical_name = myChemicalName;
	}
	
	public Boolean getIs_enzyme() {
		return is_enzyme;
	}

	public void setIs_enzyme(Boolean is_enzyme) {
		this.is_enzyme = is_enzyme;
	}

	public Boolean getChemostat() {
		return chemostat;
	}

	public void setChemostat(Boolean chemostat) {
		this.chemostat = chemostat;
	}
	
	public String getOutputTo() {
		return outputTo;
	}

	public void setOutputTo(String outputTo) {
		this.outputTo = outputTo;
	}	

	// keep the reactant at a certain concentration
	public void setChemostatWithConcentration(double myConcentration) {
		this.my_concentration = myConcentration;
		this.chemostat = true;
	}

	public ArrayList<Double> getDataList() {
		return dataList;
	}

	public void setDataList(ArrayList<Double> dataList) {
		this.dataList = dataList;
	}

	public void setNumberByConcentration() {
		int int_number = (int) (my_concentration * volume * NA);
		number = (double) int_number;
//		System.out.println("vN is "+(volume * NA));
//		System.out.println(my_chemical_name+ " cons is "+my_concentration);
//		System.out.println(my_chemical_name+" number is "+number);
	}

	public void setConcentrationByNumber() {
		my_concentration = number / volume / NA;
	}

	public Boolean commit() {
		if (chemostat)
			return true;
		number += number_update;
		number_update = 0;
		
		if (number < 0) {
			 number = 0;
//			 System.out.println(my_chemical_name +" is not enough "+number);
		}
//		 if (Double.isNaN(number))
//		    {
//			 	System.out.println(my_chemical_name +" is NaN "+number);
//			 	System.exit(0);
//		    }
		
		my_concentration = number / volume / NA;	
		return false;
	}

	public Boolean isReactantEnough(double requested) {
		if (chemostat)
			return true;
		if (requested > (number + number_update)) {
			return false;
		}
		return true;
	}

	public Boolean subtract(double substrate) {
		if (chemostat)
			return true;
		number_update -= substrate;
		return true;
	}

	public Boolean add(double product) {// products can always be added.
		if (chemostat)
			return true;
		number_update += product;
		return true;
	}

}
