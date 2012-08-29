package crossover;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 
 * @author Robert W. Harrison
 *         <p>
 *         class reaction implements the reactions (D'oh!) It attempts to
 *         perform a reaction (estimates amounts) and then proceeds if enough of
 *         the reactions are present
 *         </p>
 *         <p>
 *         Its constructor uses the list of reactants that are already defined.
 *         If not already defined it will throw a wobbly.
 *         </p>
 * 
 */

public class Reaction {

	private String id;
	private String name;
	private ArrayList<Reactant> input_species;

	private ArrayList<Reactant> output_species;
	private ArrayList<Float> affinities;
	private float rate_constant;
	private double enzyme_concentration;
	private Reactant enzyme;
	private Boolean use_enzyme;
	private String outputTo;

	private double NA = (double) 6.0221415e23; // need to set the standard units
	private double volume = (double) 0.00000000000001;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Reactant> getInput_species() {
		return input_species;
	}

	public void setInput_species(ArrayList<Reactant> inputSpecies) {
		input_species = inputSpecies;
	}

	public ArrayList<Reactant> getOutput_species() {
		return output_species;
	}

	public void setOutput_species(ArrayList<Reactant> outputSpecies) {
		output_species = outputSpecies;
	}

	public ArrayList<Float> getAffinities() {
		return affinities;
	}

	public void setAffinities(ArrayList<Float> affinities) {
		this.affinities = affinities;
	}

	public float getRate_constant() {
		return rate_constant;
	}

	public void setRate_constant(float rateConstant) {
		rate_constant = rateConstant;
	}

	public double getEnzyme_concentration() {
		return enzyme_concentration;
	}

	public void setEnzyme_concentration(double enzymeConcentration) {
		enzyme_concentration = enzymeConcentration;
	}

	public Reactant getEnzyme() {
		return enzyme;
	}

	public void setEnzyme(Reactant enzyme) {
		this.enzyme = enzyme;
	}

	public Boolean getUse_enzyme() {
		return use_enzyme;
	}

	public void setUse_enzyme(Boolean useEnzyme) {
		use_enzyme = useEnzyme;
	}
	
	public String getOutputTo() {
		return outputTo;
	}

	public void setOutputTo(String outputTo) {
		this.outputTo = outputTo;
	}	

	public Boolean deterministic(float dt, rrandom rng) {

		double saturation;
		saturation = (double) 1.;
		for (int i = 0; i < input_species.size(); i++) {
			double cons;
			cons = input_species.get(i).getNumber();
//			cons = input_species.get(i).getMy_concentration();
			saturation = saturation * cons;
		}

		double dpdt;
		dpdt = rate_constant * saturation * dt;
		double idpdt = dpdt;
							
		Boolean reactantIsEnough;
		reactantIsEnough = false;
		
		int number_done = 0;
		for (int i = 0; i < input_species.size(); i++) {
			Reactant x;
			x = input_species.get(i);
			if (x.isReactantEnough(idpdt)) {
				number_done++;
				x.subtract(idpdt);
				reactantIsEnough = true;
			} else {
				//if one reactant is less than the required, than the 
				//remaining reactant will proceed for the reaction
				int available_reactant = (int) input_species.get(i).getNumber();
				if (available_reactant > 0) {
					double difference = idpdt - available_reactant;
					idpdt = available_reactant;
					x.subtract(idpdt);
					reactantIsEnough = true;
					for (int j = 0; j < number_done; j++) {
						Reactant y;
						y = input_species.get(j);
						y.add(difference);
					}
				} else {
					for (int j = 0; j < number_done; j++) {
						Reactant y;
						y = input_species.get(j);
						y.add(idpdt);
					}
					break;
				}
			}
		}
		
		if (reactantIsEnough) {
			for (int i = 0; i < output_species.size(); i++) {
				output_species.get(i).add(idpdt);
			}
		}
		return true;
	}
	
	public Boolean crossover(float dt, rrandom rng) {
	
		double keff;
		double saturation;
		saturation = 1.0;
		if (use_enzyme) {
			keff = rate_constant * enzyme_concentration;
			for (int i = 0; i < input_species.size(); i++) {
				double s;
//				s = input_species.get(i).getMy_concentration();
				s = input_species.get(i).getNumber();
				s = s / (affinities.get(i) + s);
//				System.out.println(affinities.get(i));
				saturation *= s;
//				System.out.println(input_species.get(i).getMy_chemical_name()+" saturation is "+ saturation);
			}
			
		} else {
			keff = rate_constant;
			for (int i = 0; i < input_species.size(); i++) {
				double cons;
				cons = input_species.get(i).getNumber();
				saturation *= cons;
			}
		}
		
		double dpdt;
		dpdt = keff * saturation * dt;
		
		int idpdt;
		idpdt = (int) dpdt;
		dpdt = dpdt - (float) idpdt; // fractional part
		
		if (rng.nextFloat() < dpdt)
			idpdt += 1;

		// need to check each reactant and update its number
		Boolean reactantIsEnough;
		reactantIsEnough = false;
		int number_done = 0;
		for (int i = 0; i < input_species.size(); i++) {
			Reactant x;
			x = input_species.get(i);
			if (x.isReactantEnough(idpdt)) {
				number_done++;
				x.subtract(idpdt);
				reactantIsEnough = true;
			} else {
				//if one reactant is less than the required, than the 
				//remaining reactant will proceed for the reaction
				int available_reactant = (int) input_species.get(i).getNumber();
				if (available_reactant > 0) {
					int difference = idpdt - available_reactant;
					idpdt = available_reactant;
					x.subtract(idpdt);
					reactantIsEnough = true;
					for (int j = 0; j < number_done; j++) {
						Reactant y;
						y = input_species.get(j);
						y.add(difference);
					}
				} else {
					for (int j = 0; j < number_done; j++) {
						Reactant y;
						y = input_species.get(j);
						y.add(idpdt);
					}
					break;
				}
			}
		}
		
		if (reactantIsEnough) {
			for (int i = 0; i < output_species.size(); i++) {
				output_species.get(i).add(idpdt);
			}
		}			
		return true;
	}
}
