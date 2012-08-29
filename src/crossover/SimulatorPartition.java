package crossover;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Document;

public class SimulatorPartition {

	public static void main(String argv[]) throws Exception {

		Document doc = null;
		String firePath = "D:/project/cs_project/partition/";
//		 String fileName = "dim"; //dimerization
		 String fileName = "enzyme";
//		 String fileName = "glycogen_1";
//		 String fileName = "glycogen_2";
//		 String fileName = "glycogen";
//		 String fileName = "autoReg";
//		 String fileName = "gene";
//		 String fileName = "lac";
//		 String fileName = "gal";
//		 String fileName = "food_pray";
//		 String fileName = "partitionTest";
//		 String fileName = "glycolysis";
		 
		String xmlFile = firePath + fileName + ".xml";
		doc = Function.getDocument(xmlFile);
		
		ArrayList<Partition> partitionList = new ArrayList<Partition>();
		ArrayList<Reactant> reactantList = new ArrayList<Reactant>();
		ArrayList<Reaction> reactionList = new ArrayList<Reaction>();

		Function.getGlobalSystem(doc);

		// get partition with its reactants and reactions
		Function.getPartitionList(doc);

		GlobalSystem system = GlobalSystem.getInstance();
		// System.out.println("report interval is "+system.getReportInterval());
		// System.out.println("partition size is "+system.getPartitions().size());
		// System.out.println("partition name is "+system.getPartitions().get(0).getName());
		
		partitionList = system.getPartitions();
		
		reactantList = partitionList.get(0).getReactantList();

		Function.printReactantList(partitionList);
//		Function.printReactionList(partitionList);

		try {
			reactionList = partitionList.get(0).getReactionList();
			System.out.println("there are " + reactionList.size()
					+ " reactions");
		} catch (Exception e) {
			e.printStackTrace();
		}
		


		// generate the reaction list file
//		try {
//			String reactionFile = firePath + fileName + "_reactions.cmdl";
//			BufferedWriter a = new BufferedWriter(new FileWriter(reactionFile));
//
//			for (int i = 0; i < reactantList.size(); i++) {
//				a.write(reactantList.get(i).getMy_chemical_name() + " = "
//						+ reactantList.get(i).getNumber() + ";\n");
//			}
//			a.write("\n");
//			for (int i = 0; i < reactionList.size(); i++) {
//				a.write(reactionList.get(i).getId() + ", \t");                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
//				ArrayList<Reactant> input_species = reactionList.get(i)
//						.getInput_species();
//				int inputSize = input_species.size();
//				for (int j = 0; j < input_species.size(); j++) {
//					a.write(input_species.get(j).getMy_chemical_name());
//					if (inputSize > 1) {
//						a.write(" + ");
//					}
//					inputSize--;
//				}
//				a.write(" -> ");
//
//				ArrayList<Reactant> output_species = reactionList.get(i)
//						.getOutput_species();
//				int outputSize = output_species.size();
//				for (int j = 0; j < output_species.size(); j++) {
//					a.write(output_species.get(j).getMy_chemical_name());
//					if (outputSize > 1) {
//						a.write(" + ");
//					}
//					outputSize--;
//				}
//				a.write(", \t"
//						+ String.valueOf(reactionList.get(i).getRate_constant()));
//				a.write("; \n");
//			}
//			a.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		//auto gene
//		float time = 0, dt = (float) 0.001; // dt =0.001s
//		int total = 100000; 				// 100s
//		int reportInterval = 200; 			// 1000 points
		
		float time = 0, dt = (float) 0.001; // dt =0.001s
		int total = 100000; 					// 100s
		int reportInterval = 500; 			// 1000 points
		
		rrandom random;
		random = new rrandom(1010101);


		try {
			String resultFile = firePath + fileName + ".xls";
			BufferedWriter a = new BufferedWriter(new FileWriter(resultFile));

			a.write("Time \t");
//			for (int j = 0; j < reactantList.size(); j++) {
//				Reactant currentReactant = new Reactant();
//				currentReactant = reactantList.get(j);
//				if (!currentReactant.getIs_enzyme()) {
//					a.write(currentReactant.getMy_chemical_name() + "\t");
//				}	
//			}
			//write name for species
			for (int m = 0; m < partitionList.size(); m++) {
				String partitionName = partitionList.get(m).getPartitionName();
				a.write(partitionName+" \t");
				ArrayList<Reactant> currentReactantList = partitionList.get(m)
						.getReactantList();
			for (int j = 0; j < currentReactantList.size(); j++) {
				Reactant currentReactant = new Reactant();
				currentReactant = currentReactantList.get(j);
				if (!currentReactant.getIs_enzyme() && (currentReactant.getOutputTo().equals(partitionName))) {
					a.write(currentReactant.getMy_chemical_name() + "\t");
				}
			}
			}
			
			a.write("\n");
			a.write(time + " \t");
			// write initial number
			for (int m = 0; m < partitionList.size(); m++) {
				String partitionName = partitionList.get(m).getPartitionName();
				a.write(" \t");
				ArrayList<Reactant> currentReactantList = partitionList.get(m)
						.getReactantList();
			for (int j = 0; j < currentReactantList.size(); j++) {
				Reactant currentReactant = new Reactant();
				currentReactant = currentReactantList.get(j);
				if (!currentReactant.getIs_enzyme()) {
					a.write(currentReactant.getNumber() + "\t");
				}
				currentReactant.getDataList()
						.add(currentReactant.getNumber());
			}
			}
				
			a.write("\n");

			for (int i = 0; i < total; i += reportInterval) {
				for (int j = 0; j < reportInterval; j++) {
					
					for (int m=0; m<system.getPartitions().size(); m++){
						Partition currentPartion = system.getPartitions().get(m);
						currentPartion.reactionProceed(dt, random);						
					}
					
					for (int m=0; m<system.getPartitions().size(); m++){
						Partition currentPartion = system.getPartitions().get(m);
						currentPartion.update();						
					}
					
					
//					for (int k = 0; k < reactionList.size(); k++) {
////						reactionList.get(k).deterministic(dt, random);
//						reactionList.get(k).crossover(dt, random);
//					}
//					
//					for (int k = 0; k < reactantList.size(); k++) {
//						Reactant currentReactant = reactantList.get(k);
//						reactantList.get(k).commit();
//
//					}

					time += dt;
				}
				a.write(time + "\t");	
				
				
				for (int m = 0; m < partitionList.size(); m++) {
					String partitionName = partitionList.get(m).getPartitionName();
					a.write(" \t");
					ArrayList<Reactant> currentReactantList = partitionList.get(m)
							.getReactantList();
				for (int j = 0; j < currentReactantList.size(); j++) {
					Reactant currentReactant = new Reactant();
					currentReactant = currentReactantList.get(j);
					if (!currentReactant.getIs_enzyme()&& (currentReactant.getOutputTo().equals(partitionName))) {
						a.write(currentReactant.getNumber() + "\t");
					}
					currentReactant.getDataList()
							.add(currentReactant.getNumber());
				}
				
				
				}
	
				a.write("\n");
			}
			a.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Function.printReactantList(partitionList);
		

		// plot the graph
		for (int m = 0; m < partitionList.size(); m++) {
			String partitionName = partitionList.get(m).getPartitionName();
			ArrayList<Reactant> currentReactantList = partitionList.get(m)
					.getReactantList();

			for (int j = 0; j < currentReactantList.size(); j++) {

				String name = currentReactantList.get(j).getMy_chemical_name();
				String reactantOutputTo = currentReactantList.get(j)
						.getOutputTo();
				boolean is_enzyme = currentReactantList.get(j).getIs_enzyme();
				if ((!name.equals("empty")) && (!is_enzyme)
						&& (reactantOutputTo.equals(partitionName))) {
					
					graphingData.drawGraph(currentReactantList.get(j)
							.getDataList(), name);
					
				}
			}
		}
	}
	

}
