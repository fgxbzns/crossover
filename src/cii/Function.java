package cii;


import java.io.*;
import javax.xml.parsers.*;

import java.lang.Object;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import org.w3c.dom.Node;

public class Function {



	
	// get GlobalSystem information.
//	public static void getGlobalSystem(Document doc) {
//		NodeList list = doc.getElementsByTagName("system");
//		Element systemElement = (Element) list.item(0);
//
//		float time = Float.valueOf(systemElement.getAttribute("time"));
//		float timestep = Float.valueOf(systemElement.getAttribute("timestep"));
//		int total = Integer.valueOf(systemElement.getAttribute("total"));
//		int reportInterval = Integer.valueOf(systemElement
//				.getAttribute("reportInterval"));
//
//
//		GlobalSystem.getInstance().setTime(time);
//		GlobalSystem.getInstance().setDt(timestep);
//		GlobalSystem.getInstance().setTotal(total);
//		GlobalSystem.getInstance().setReportInterval(reportInterval);
//	}

	// get reads information. 
	public static ArrayList<Reads> getReadsList(String fna_file, String qual_file) throws Exception {
		
		Scanner reader = new Scanner(System.in);
		Scanner fnaFileInput = null;
		Scanner qualFileInput = null;
		ArrayList<Reads> readsList = new ArrayList<Reads>();
		Reads current_read = new Reads();;
		
		
		// get info from fasta file
		try {
			File file = new File(fna_file);
			fnaFileInput = new Scanner(file);
		} catch (Exception e) {
			System.out.println("fna file not found. Please try again.");
			System.exit(0);
		}
		
		// get info from qual file
		try {
			File file = new File(qual_file);
			qualFileInput = new Scanner(file);
		} catch (Exception e) {
			System.out.println("qual file not found. Please try again.");
			System.exit(0);
		}
		
		ArrayList<String> quality_score = new ArrayList<String>();
		double sum_quality_score = 0;
		double number_of_quality_score = 0;
		double average_quality_score;
		
		//String sequence = "";
		StringBuffer sequence = new StringBuffer ("");
	    
		
		if (fnaFileInput.hasNext()) {
			String currentFnaElement = fnaFileInput.next();
			String currentQualElement = null;
			qualFileInput.next();
			
			while (fnaFileInput.hasNextLine()) {
//				Reads current_read = new Reads();
				
				
				if (currentFnaElement.startsWith(">")) {
					current_read = new Reads();
					
					String identifier;
					int length;
					String xy;
					String region;
					String run;	
					sequence = new StringBuffer (""); // start a new read, empty the sequence.
					sum_quality_score = 0;
					number_of_quality_score = 0;
									
					
					identifier = currentFnaElement.substring(currentFnaElement.indexOf('>')+1);
					
					currentFnaElement = fnaFileInput.next();		
					length = Integer.parseInt(currentFnaElement.substring(currentFnaElement.indexOf('=')+1));
					qualFileInput.next();
					
					currentFnaElement = fnaFileInput.next();
					xy = currentFnaElement.substring(currentFnaElement.indexOf('=')+1);
					qualFileInput.next();
					
					currentFnaElement = fnaFileInput.next();
					region = currentFnaElement.substring(currentFnaElement.indexOf('=')+1);
					qualFileInput.next();
					
					currentFnaElement = fnaFileInput.next();
					run = currentFnaElement.substring(currentFnaElement.indexOf('=')+1);
					qualFileInput.next();
					
					currentFnaElement = fnaFileInput.next();
					currentQualElement = qualFileInput.next();
//					System.out.println("currentQualElement is " + currentQualElement);	
					
					current_read.setIdentifier(identifier);
					current_read.setLength(length);
					current_read.setXy(xy);
					current_read.setRegion(region);
					current_read.setRun(run);
				
					
//					System.out.println("identifier is " + identifier);	
//					System.out.println("length is " + length);
					
				}	
				else {
					while (!currentFnaElement.startsWith(">") && fnaFileInput.hasNext()){
//						sequence += currentFnaElement;
						sequence.append(currentFnaElement);
						currentFnaElement = fnaFileInput.next();	
					}
					if (!fnaFileInput.hasNext()) {						
//						sequence += currentFnaElement;
						sequence.append(currentFnaElement);
					}
					String seq = sequence.toString();
					current_read.setSequence(seq);
					current_read.check_if_contains_primer();
					current_read.check_if_contains_adatpor();
					current_read.check_if_contains_both();
					
//					System.out.println("contains primer " + current_read.getContain_primer());
//					System.out.println("contains adatpor " + current_read.getContain_adaptor());
//					System.out.println("contains both " + current_read.getContain_both());
					
					//System.out.println("sequence is " + sequence);
										
					while (!currentQualElement.startsWith(">") && qualFileInput.hasNext()){
						quality_score.add(currentQualElement);
						sum_quality_score += Integer.valueOf(currentQualElement);
						number_of_quality_score++;
						currentQualElement = qualFileInput.next();						
					}
					if (!qualFileInput.hasNext()) {						
						quality_score.add(currentQualElement);
						sum_quality_score += Integer.valueOf(currentQualElement);
						number_of_quality_score++;
					}
					
//					System.out.println("quality_score size is " + quality_score.size());
//					System.out.println("quality_score is " + quality_score);
					
					current_read.setQuality_score(quality_score);
//					current_read.setAverage_quality_score(sum_quality_score/number_of_quality_score);
					current_read.set_quality_score_avage();
					
					readsList.add(current_read);
//					System.out.println("quality_score ave is " + current_read.getAverage_quality_score());
										
				}

			}
		} else {
			System.out.println("The file is empty, please try again.");
			System.exit(1);
		}
		
		fnaFileInput.close();
		qualFileInput.close();
		
		return readsList;
	
	}
	
	
	
	public static void printReadsList(ArrayList<Reads> readsList) {

		for (int i = 0; i < readsList.size(); i++) {
			Reads current_read = readsList.get(i);
			
				System.out.println("Identifier is : "+current_read.getIdentifier() + " ");
				System.out.println("Length is : "+current_read.getLength() + " ");
				System.out.println("quality_score is : "+current_read.getQuality_score() + " ");
				System.out.println("average_quality_score is: "+current_read.getAverage_quality_score() + " ");
				System.out.println("primer : "+current_read.getContain_primer());
				System.out.println("adaptor : "+current_read.getContain_adaptor());
				System.out.println("both : "+current_read.getContain_both());
				System.out.println("sequence: "+current_read.getSequence());
			
		}		
	}
	



}
