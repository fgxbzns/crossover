package crossover;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Document;

public class Simulator {

	public static void main(String argv[]) throws Exception {

		Document doc = null;
		String firePath = "D:/project/cs_project/";
//		 String fileName = "dim"; //dimerization
//		 String fileName = "enzyme";
//		 String fileName = "glycogen_1";
//		 String fileName = "glycogen_2";
//		 String fileName = "glycogen";
//		 String fileName = "autoReg";
//		 String fileName = "gene";
//		 String fileName = "lac";
//		 String fileName = "gal";
//		 String fileName = "food_pray";
//		 String fileName = "partitionTest";
		 String fileName = "glycolysis";
		 
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
		reactantList = system.getPartitions().get(0).getReactantList();
//		reactionList = system.getPartitions().get(0).getReactionList();

		Function.printReactantList(partitionList);

		try {
			reactionList = system.getPartitions().get(0).getReactionList();
			System.out.println("there are " + reactionList.size()
					+ " reactions");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		Function.printReactionList(reactionList);

		// generate the reaction list file
		try {
			String reactionFile = firePath + fileName + "_reactions.cmdl";
			BufferedWriter a = new BufferedWriter(new FileWriter(reactionFile));

			for (int i = 0; i < reactantList.size(); i++) {
				a.write(reactantList.get(i).getMy_chemical_name() + " = "
						+ reactantList.get(i).getNumber() + ";\n");
			}
			a.write("\n");
			for (int i = 0; i < reactionList.size(); i++) {
				a.write(reactionList.get(i).getId() + ", \t");                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
				ArrayList<Reactant> input_species = reactionList.get(i)
						.getInput_species();
				int inputSize = input_species.size();
				for (int j = 0; j < input_species.size(); j++) {
					a.write(input_species.get(j).getMy_chemical_name());
					if (inputSize > 1) {
						a.write(" + ");
					}
					inputSize--;
				}
				a.write(" -> ");

				ArrayList<Reactant> output_species = reactionList.get(i)
						.getOutput_species();
				int outputSize = output_species.size();
				for (int j = 0; j < output_species.size(); j++) {
					a.write(output_species.get(j).getMy_chemical_name());
					if (outputSize > 1) {
						a.write(" + ");
					}
					outputSize--;
				}
				a.write(", \t"
						+ String.valueOf(reactionList.get(i).getRate_constant()));
				a.write("; \n");
			}
			a.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	
		// generate the ode file
		try {
			String reactionFile = firePath + fileName + "_xpp.ode";
			BufferedWriter a = new BufferedWriter(new FileWriter(reactionFile));
			a.write("# "+fileName+" (XPPAUT file) \n");
			
			a.write("# ODE \n");

			for (int i = 0; i < reactantList.size(); i++) {
				Reactant thisReactant = reactantList.get(i);
				String reactantName = thisReactant.getMy_chemical_name();
				if (!thisReactant.getIs_enzyme()){
//					if (!reactantName.equals("empty") && !thisReactant.getIs_enzyme()){
				boolean firstElement = true;  //plus sign cannot be the first element
				
				a.write("d" + reactantName + "/dt = ");

				for (int m = 0; m < reactionList.size(); m++) {
					float rate_constant = reactionList.get(m)
							.getRate_constant();
					

					ArrayList<Reactant> input_species = reactionList.get(m)
							.getInput_species();
					for (int j = 0; j < input_species.size(); j++) {
						if (reactantName.equals(input_species.get(j)
								.getMy_chemical_name())) {
							firstElement = false;
							a.write(" - " + rate_constant);
							for (int n = 0; n < input_species.size(); n++) {
								a.write("*"
										+ input_species.get(n)
												.getMy_chemical_name());
							}
						}
					}

					ArrayList<Reactant> output_species = reactionList.get(m)
							.getOutput_species();
					for (int j = 0; j < output_species.size(); j++) {
						if (reactantName.equals(output_species.get(j)
								.getMy_chemical_name())) {
							if (firstElement){
								a.write(""+rate_constant);
								firstElement = false;
							} else
								a.write(" + " + rate_constant);
							for (int n = 0; n < input_species.size(); n++) {
								a.write("*"
										+ input_species.get(n)
												.getMy_chemical_name());
							}
						}
					}					
				}
				a.write("\n");			
			}
			}

			a.write("# INITIAL CONDITIONS \n");  //no space between name and value
			for (int i = 0; i < reactantList.size(); i++) {
				Reactant thisReactant = reactantList.get(i);
				String reactantName = thisReactant.getMy_chemical_name();
				if (!thisReactant.getIs_enzyme()){
//				if (!reactantName.equals("empty")&& !thisReactant.getIs_enzyme()){
				a.write("init " + reactantName + "=" + thisReactant.getNumber()
						+ "\n");
				}
			}

			a.write("# CHANGES FROM XPP'S DEFAULT VALUES \n");
			a.write("@ TOTAL=100.0,DT=0.001,XLO=0.0,XHI=100.0,YLO=-10,YHI=200 \n");
			a.write("@ MAXSTOR=1000000 # to increase the maximum storage capacity \n");
			a.write("@ NOUT=500        # to save one data point every nout calculated points \n");
			a.write("done \n");
			a.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		//auto gene
//		float time = 0, dt = (float) 0.001; // dt =0.001s
//		int total = 100000; 				// 100s
//		int reportInterval = 200; 			// 1000 points
		
		float time = 0, dt = (float) 0.001; // dt =0.001s
//		int total = 100000; 					// 100s
		int total = 10000000; 					// 100s
		int reportInterval = 500; 			// 200 points
		
		rrandom random;
		random = new rrandom(1010101);
		
		long startTime = 0;

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
				if (!currentReactant.getIs_enzyme()&& (currentReactant.getOutputTo().equals(partitionName))) {
					a.write(currentReactant.getMy_chemical_name() + "\t");
				}
			}
			}
			
			a.write("\n");
			a.write(time + " \t");
			// write initial number
			for (int m = 0; m < partitionList.size(); m++) {
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
			
			// starting time
			startTime = System.currentTimeMillis();

			for (int i = 0; i < total; i += reportInterval) {
				for (int j = 0; j < reportInterval; j++) {
//					for (int k = 0; k < reactionList.size(); k++) {
////						reactionList.get(k).deterministic(dt, random);
//						reactionList.get(k).crossover(dt, random);
//					}
//					for (int k = 0; k < reactantList.size(); k++) {
//						reactantList.get(k).commit();
//					}				
					
					for (int m=0; m<system.getPartitions().size(); m++){
						Partition currentPartion = system.getPartitions().get(m);
						currentPartion.reactionProceed(dt, random);						
					}
					
					for (int m=0; m<system.getPartitions().size(); m++){
						Partition currentPartion = system.getPartitions().get(m);
						currentPartion.update();						
					}
					
//					for (int m=0; m<system.getPartitions().size(); m++){
//						Partition currentPartion = system.getPartitions().get(m);
//						currentPartion.checkEvent(time, dt);						
//					}

					time += dt;
				}
				a.write(time + "\t");
				
				
//				for (int j = 0; j < reactantList.size(); j++) {
//					Reactant currentReactant = new Reactant();
//					currentReactant = reactantList.get(j);
//					if (!currentReactant.getIs_enzyme()) {
//						a.write(currentReactant.getNumber() + "\t");
//					}
//					currentReactant.getDataList()
//							.add(currentReactant.getNumber());
//					// a.write(reactantList.get(j).getMy_concentration()+"\t");
//				}
				
				
				for (int m = 0; m < partitionList.size(); m++) {
					String partitionName = partitionList.get(m)
							.getPartitionName();
					a.write(" \t");
					ArrayList<Reactant> currentReactantList = partitionList
							.get(m).getReactantList();
					for (int j = 0; j < currentReactantList.size(); j++) {
						Reactant currentReactant = new Reactant();
						currentReactant = currentReactantList.get(j);
						if (!currentReactant.getIs_enzyme()
								&& (currentReactant.getOutputTo()
										.equals(partitionName))) {
							a.write(currentReactant.getNumber() + "\t");
						}
						currentReactant.getDataList().add(
								currentReactant.getNumber());
					}

				}
				
				a.write("\n");
			}
			a.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	
		long elapsedTimeMillis = System.currentTimeMillis()-startTime;
		// Get elapsed time in seconds
		float elapsedTimeSec = elapsedTimeMillis/1000F;
		System.out.println("Elapsed time is: " + elapsedTimeSec + " sec");
//		Function.printReactantList(partitionList);
		

		// plot the graph
//		for (int j = 0; j < reactantList.size(); j++) {
//			String name = reactantList.get(j).getMy_chemical_name();
//			boolean is_enzyme = reactantList.get(j).getIs_enzyme();
//			if ((!name.equals("empty")) && (!is_enzyme)) {
//				graphingData.drawGraph(reactantList.get(j).getDataList(), name);
//			}
//		}
		for (int m = 0; m < partitionList.size(); m++) {
			String partitionName = partitionList.get(m).getPartitionName();
			ArrayList<Reactant> currentReactantList = partitionList.get(m)
					.getReactantList();

//			for (int j = 0; j < currentReactantList.size(); j++) {
//
//				String name = currentReactantList.get(j).getMy_chemical_name();
//				String reactantOutputTo = currentReactantList.get(j)
//						.getOutputTo();
//				boolean is_enzyme = currentReactantList.get(j).getIs_enzyme();
//				if ((!name.equals("empty")) && (!is_enzyme)
//						&& (reactantOutputTo.equals(partitionName))) {
//					
//					graphingData.drawGraph(currentReactantList.get(j)
//							.getDataList(), name);
//					
//				}
//			}
		}
	
	}

}
