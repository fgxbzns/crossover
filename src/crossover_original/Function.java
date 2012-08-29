package crossover_original;

import java.io.*;
import javax.xml.parsers.*;

//import metabolismSimulator.reactant;
//import metabolismSimulator.reaction_wobbly;

import org.w3c.dom.*;
import org.xml.sax.*;
import java.lang.Object;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.w3c.dom.Node;

public class Function {

	// for the simulation

	static Document doc = null;

	public static Document getDocument(String fileName) {
		Document doc = null;
		try {
			File file = new File(fileName);
			if (file.exists()) {
				// Create a factory
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				// Use the factory to create a builder
				DocumentBuilder builder = factory.newDocumentBuilder();
				doc = builder.parse(fileName);
			} else {
				System.out.print("File not found!");
			}
		} catch (Exception e) {
			System.exit(1);
		}
		return doc;
	}

	// public static String getCharacterDataFromElement(Element e) {
	// Node child = e.getFirstChild();
	// if (child instanceof CharacterData) {
	// CharacterData cd = (CharacterData) child;
	// return cd.getData();
	// }
	// return "no data";
	// }

	// Check if the reactant is already in the list. Remove duplicate
	public static boolean notContain(ArrayList<Reactant> list, String name) {
		boolean contains = true;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getMy_chemical_name().equals(name)) {
				contains = false;
			}
		}
		return contains;
	}
	
	// get GlobalSystem information.
	public static void getGlobalSystem(Document doc) {
		NodeList list = doc.getElementsByTagName("system");
		Element systemElement = (Element) list.item(0);

		float time = Float.valueOf(systemElement.getAttribute("time"));
		float timestep = Float.valueOf(systemElement.getAttribute("timestep"));
		int total = Integer.valueOf(systemElement.getAttribute("total"));
		int reportInterval = Integer.valueOf(systemElement.getAttribute("reportInterval"));

		GlobalSystem.getInstance().setTime(time);
		GlobalSystem.getInstance().setDt(timestep);
		GlobalSystem.getInstance().setTotal(total);
		GlobalSystem.getInstance().setReportInterval(reportInterval);
	}

	// get partition information. 
	public static void getPartitionList(Document doc) throws Exception {
		
		NodeList partationNode = doc.getElementsByTagName("partition");
				
		ArrayList<Partition> partitionList = new ArrayList<Partition>();
		
		for (int i = 0; i < partationNode.getLength(); i++) {
			Element partitionElement = (Element) partationNode.item(i);
			String partitionName = partitionElement.getAttribute("name");
			float volume = Float.valueOf(partitionElement.getAttribute("volume"));
			
			Partition aPartition = new Partition();
			aPartition.setPartitionName(partitionName);
			aPartition.setVolume(volume);
					
//			System.out.println(partitionName);
//			System.out.println(volume);
	
			// get reactants in current partition
			NodeList reactantNode = partitionElement.getElementsByTagName("reactant");
			ArrayList<Reactant> reactantList = new ArrayList<Reactant>();
			for (int j = 0; j < reactantNode.getLength(); j++) {
				Element reactantElement = (Element) reactantNode.item(j);
				
				String reactantName = reactantElement.getAttribute("name");
				String number = reactantElement.getAttribute("number");
				String concentration = reactantElement.getAttribute("concentration");
				String chemostat = reactantElement.getAttribute("chemostat");
				String is_enzyme;
				is_enzyme = reactantElement.getAttribute("is_enzyme");
				
				if (is_enzyme != ""){
					is_enzyme = reactantElement.getAttribute("is_enzyme");
				} else {
					is_enzyme = "false"; 
				}

				String outputTo = reactantElement.getAttribute("outputTo");
				
//				System.out.println(reactantName);
				
				if (notContain(reactantList, reactantName)) {
					Reactant aReactant = new Reactant();
					aReactant.setMy_chemical_name(reactantName);
					
	// temporary volume, need to remove
					double tVolume = 1.0e-20; 
					
					aReactant.setVolume(tVolume); //get volume from partition
									
					//check whether number of concentration is available
					if (number != ""){
						aReactant.setNumber(Integer.valueOf(number));
						aReactant.setConcentrationByNumber();					
					} else if (concentration != "") {
						aReactant.setMy_concentration(Double.valueOf(concentration));
						aReactant.setNumberByConcentration();
//						System.out.println("cons is "+Double.valueOf(concentration));
					} else {
						System.out.println("Number or concentration must be defined for ractant "
								+ reactantName);
						System.exit(0);		
					}
					
					if (outputTo != ""){
						outputTo = reactantElement.getAttribute("outputTo");
					} else {
						outputTo = partitionName; // default ouput is its current partation
					}
					
					aReactant.setOutputTo(outputTo);
					
					aReactant.setChemostat(Boolean.valueOf(chemostat));
					aReactant.setIs_enzyme(Boolean.valueOf(is_enzyme));
					reactantList.add(aReactant);
				}
			}	
			
			// add reactant list to current partation
			aPartition.setReactantList(reactantList);  
			
			// get reactions in current partition
			NodeList reactionNode = partitionElement.getElementsByTagName("reaction");
			ArrayList<Reaction> reactionList = new ArrayList<Reaction>();
			
//			System.out.println("there are "+reactionNode.getLength()+" reactions");

			for (int j = 0; j < reactionNode.getLength(); j++) {
				Reaction aReaction = new Reaction();

				ArrayList<Reactant> input_species = new ArrayList<Reactant>();
				ArrayList<Reactant> output_species = new ArrayList<Reactant>();
				ArrayList<Float> affinities = new ArrayList<Float>();
				
				String reactionID;
				String reactionName;
				
				Reactant enzyme = new Reactant();;
				String enzymeName;
				
				float rate_constant;
				double enzyme_concentration = 0;
				
				// Reactant enzyme;
				boolean use_enzyme = false;  // default value is false
				
				Element reactionElement = (Element) reactionNode.item(j);
				reactionID = reactionElement.getAttribute("id");
				reactionName = reactionElement.getAttribute("name");
				rate_constant = Float.valueOf(reactionElement
						.getAttribute("constant"));
				
				use_enzyme = Boolean.valueOf(reactionElement
						.getAttribute("use_enzyme"));
//				System.out.println(reactionID+" rate is "+ rate_constant);
				
				if (use_enzyme){				
					enzymeName = reactionElement.getAttribute("enzyme");
//					System.out.println(reactionID+" enzyme is "+ enzymeName);
					enzyme = getEnzyme(reactantList, enzymeName);
//					System.out.println(reactionID+" enzyme is "+ enzyme.getMy_chemical_name());
					enzyme_concentration = enzyme.getMy_concentration();	
				}
	
//				if (!reactionElement.getAttribute("concentration").equals("") ) {
//					System.out.println("null");
//					enzyme_concentration = Float.valueOf(reactionElement
//						.getAttribute("concentration"));
//				} else 
//					enzyme_concentration = (float) 1.0;


				// get input_species, check if it is in the defined species.
				NodeList inputNode = reactionElement.getElementsByTagName("input");
				for (int k = 0; k < inputNode.getLength(); k++) {
					Element inputElement = (Element) inputNode.item(k);
					String inputName = inputElement.getAttribute("name");
					// System.out.println(inputName);
					Reactant thisReactant = validateReaction(reactantList,
							inputName);
					if (use_enzyme) {
						affinities.add(Float.valueOf(inputElement
								.getAttribute("constant")));
					}
					input_species.add(thisReactant);
					
//					System.out.println(inputName);
//					System.out.println(Float.valueOf(inputElement
//							.getAttribute("constant")));
					
				}

				// get output_species
				NodeList outputNode = reactionElement
						.getElementsByTagName("output");
				for (int k = 0; k < outputNode.getLength(); k++) {
					Element outputElement = (Element) outputNode.item(k);
					String outputName = outputElement.getAttribute("name");
					Reactant thisReactant = validateReaction(reactantList,
							outputName);
					output_species.add(thisReactant);
				}

				aReaction.setId(reactionID);
				aReaction.setName(reactionName);
				aReaction.setInput_species(input_species);
				aReaction.setOutput_species(output_species);
				
				aReaction.setRate_constant(rate_constant);
				aReaction.setUse_enzyme(use_enzyme);
				if (use_enzyme) {
					aReaction.setEnzyme(enzyme);
					aReaction.setEnzyme_concentration(enzyme_concentration);
					aReaction.setAffinities(affinities);
				}

//				 System.out.println(input_species.size());
//				 System.out.println(output_species.get(0).getMy_chemical_name());
//				 System.out.println(use_enzyme);
				reactionList.add(aReaction);
			}
			
			// add reaction list to current partation
			aPartition.setReactionList(reactionList);  	
			
			// get event list
			NodeList eventNode = partitionElement.getElementsByTagName("event");
			ArrayList<Event> eventList = new ArrayList<Event>();
			

			for (int j = 0; j < eventNode.getLength(); j++) {
				
				String eventID;
				float timePoint;
				String targetName;
				String targetProperty;
				String action;
				double value;
				
				Event anEvent = new Event();
	
				Element eventElement = (Element) eventNode.item(j);
				eventID = eventElement.getAttribute("id");
				timePoint = Float.valueOf(eventElement
						.getAttribute("timePoint"));
				targetName = eventElement.getAttribute("targetName");
				targetProperty = eventElement.getAttribute("targetProperty");
				action = eventElement.getAttribute("action");				
				value = Double.valueOf(eventElement
						.getAttribute("value"));

				anEvent.setEventID(eventID);
				anEvent.setTimePoint(timePoint);
				anEvent.setTargetName(targetName);
				anEvent.setTargetProperty(targetProperty);
				anEvent.setAction(action);
				anEvent.setValue(value);	
				
				eventList.add(anEvent);
			}
			
			// add event list to current partation
			aPartition.setEventList(eventList); 
			
			partitionList.add(aPartition);
		}
		GlobalSystem.getInstance().setPartitions(partitionList);
	}
	
	public static Reactant getEnzyme(ArrayList<Reactant> reactantList,
			String inputName) throws Exception {
		Reactant aReactant = new Reactant();
		Boolean found = false;
		for (int i = 0; i < reactantList.size(); i++) {
			if (inputName.equals(reactantList.get(i).getMy_chemical_name())) {
				aReactant = reactantList.get(i);
				found = true;
			}
		}
		if (!found) {
			System.out
					.println("Enzyme "
							+ inputName
							+ " is not defined. \nMust define all species before defining reaction ");
			System.exit(0);
		}
		return aReactant;
	}

	//validate if the reactants in the reaction are already defined. 
	public static Reactant validateReaction(ArrayList<Reactant> reactantList,
			String inputName) throws Exception {
		Reactant aReactant = new Reactant();
		Boolean found = false;
		for (int i = 0; i < reactantList.size(); i++) {
			if (inputName.equals(reactantList.get(i).getMy_chemical_name())) {
				aReactant = reactantList.get(i);
				found = true;
			}
		}
		if (!found) {
			System.out
					.println("Reactant "
							+ inputName
							+ " is not defined. \nMust define all species before defining reaction ");
			System.exit(0);
		}
		return aReactant;
	}
	
	public static void printReactantList(ArrayList<Partition> partitionList) {
		for (int m = 0; m < partitionList.size(); m++) {
		String partitionName = partitionList.get(m).getPartitionName();
		ArrayList<Reactant> reactantList = partitionList.get(m)
				.getReactantList();
		System.out.println("partition is: "+partitionName);
		
		for (int i = 0; i < reactantList.size(); i++) {
				Reactant thisReactant = reactantList.get(i);
			if(thisReactant.getOutputTo().equals(partitionName)){
				System.out.print("reactant "+ i + " ");
				System.out.print("name: "+thisReactant.getMy_chemical_name() + " ");
				System.out.print("number: "+thisReactant.getNumber() + " ");
				System.out.print("concentration: "+thisReactant.getMy_concentration() + " ");
				System.out.print("output to: "+thisReactant.getOutputTo() + " ");
				System.out.println("chemostat: "+thisReactant.getChemostat());
			}
		}
		}
	}
	
	public static void printThisReactantList(ArrayList<Reactant> reactantList) {
		for (int i = 0; i < reactantList.size(); i++) {
			System.out.print("reactant "+ i + " ");
			System.out.print("name: "+reactantList.get(i).getMy_chemical_name() + " ");
			System.out.print("number: "+reactantList.get(i).getNumber() + " ");
			System.out.print("concentration: "+reactantList.get(i).getMy_concentration() + " ");
			System.out.print("output to: "+reactantList.get(i).getOutputTo() + " ");
			System.out.println("chemostat: "+reactantList.get(i).getChemostat());
		}
	}
	
	public static void printReactionList(ArrayList<Partition> partitionList) {
		for (int m = 0; m < partitionList.size(); m++) {
			String partitionName = partitionList.get(m).getPartitionName();
			ArrayList<Reaction> reactionList = partitionList.get(m).getReactionList();
			System.out.println("partition is: "+partitionName);
			
			for (int i = 0; i < reactionList.size(); i++) {
					Reaction thisReaction = reactionList.get(i);
					System.out.print("reaction id " + reactionList.get(i).getId() + " " );
					System.out.print("use enzyme: "+reactionList.get(i).getUse_enzyme() + " ");
					if (reactionList.get(i).getUse_enzyme()){
						System.out.print("enzyme is: "+reactionList.get(i).getEnzyme().getMy_chemical_name() + " ");				
					}	
					System.out.println("");
			}
			}
	}
	
	public static void printThisReactionList(ArrayList<Reaction> reactionList) {
		for (int i = 0; i < reactionList.size(); i++) {
			System.out.print("reaction id " + reactionList.get(i).getId() + " " );
			System.out.print("use enzyme: "+reactionList.get(i).getUse_enzyme() + " ");
			if (reactionList.get(i).getUse_enzyme()){
				System.out.print("enzyme is: "+reactionList.get(i).getEnzyme().getMy_chemical_name() + " ");				
			}	
			System.out.println("");
		}
	}
	
	

		


}
