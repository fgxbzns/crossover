package cii;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.w3c.dom.Document;

import coreServlets.Reactant;

public class Simulator {
	
	private static String firePath = "d:/Dropbox/hm_share/green card/work/Columbia University/test/";
//	private static String fileName = "bioinformatics_test";	 
	private static String fileName = "bioinformatics_test_70000";	
//	private static String fileName = "bioinformatics_test_ori";	 
	private static String fnaFile = firePath + fileName + ".fna";
	private static String qualFile = firePath + fileName + ".qual";

	private static int number_of_total_reads;
	private static int number_of_reads_greater_than_100bp;
	private static int number_of_reads_aqs_greater_than_20;
	private static int number_of_reads_contain_primer;
	private static int number_of_reads_contain_adaptor;
	private static int number_of_reads_contain_both;
	
	
	public static void main(String argv[]) throws Exception {
		
		ArrayList<Reads> readsList = new ArrayList<Reads>();
		
		long start = System.currentTimeMillis();
		
//		Scanner reader = new Scanner(System.in);
//		Scanner fnaFileInput = null;
//		Scanner qualFileInput = null;
//		Reads current_read = new Reads();;
//		
//		ArrayList<String> a = new ArrayList<String>();;
//
//		
//		int total = 0;
//		// get info from fasta file
//		try {
//			File file = new File(qualFile);
//			fnaFileInput = new Scanner(file);
//			
//			if (fnaFileInput.hasNext()) {
//				
//				while (fnaFileInput.hasNext()) {
//					String currentFnaElement = fnaFileInput.next();
//					a.add(currentFnaElement);
//					total ++;	
//				}		
//			}		
//		} catch (Exception e) {
//			System.out.println(e);
//			System.exit(0);
//		}		
//		fnaFileInput.close();
//	
//		System.out.println("total line is " + total);
//		System.out.println("total arraylist size is " + a.size());
		
		
		
		
		
//		readsList = Function.getReadsList(fnaFile, qualFile);
//		
//		
//		number_of_total_reads = readsList.size();
//		for (int i = 0; i < readsList.size(); i++) {
//			Reads current_read = readsList.get(i);
//
//			if (current_read.getLength() > 100) {
//				
//				number_of_reads_greater_than_100bp ++;
//			}			
//			if (current_read.getAverage_quality_score() > 20) {
//				number_of_reads_aqs_greater_than_20 ++;
//			}
//			if (current_read.getContain_primer()) {
//				number_of_reads_contain_primer ++;
//			}
//			if (current_read.getContain_adaptor()) {
//				number_of_reads_contain_adaptor ++;
//			}
//			if (current_read.getContain_both()) {
//				number_of_reads_contain_both ++;
//			}
//			
//			
//		}
		
//		Function.printReadsList(readsList);
		
		Results result = Function.processReadsList(fnaFile, qualFile);
		
		
		number_of_total_reads = result.getNumber_of_total_reads();
		number_of_reads_greater_than_100bp = result.getNumber_of_reads_greater_than_100bp();
		number_of_reads_aqs_greater_than_20 = result.getNumber_of_reads_aqs_greater_than_20();
		number_of_reads_contain_primer = result.getNumber_of_reads_contain_primer();
		number_of_reads_contain_adaptor = result.getNumber_of_reads_contain_adaptor();
		number_of_reads_contain_both = result.getNumber_of_reads_contain_both();
		
		System.out.println("number_of_total_reads is " + number_of_total_reads);
		System.out.println("number_of_reads_greater_than_100bp is " + number_of_reads_greater_than_100bp);
		System.out.println("number_of_reads_aqs_greater_than_20 is " + number_of_reads_aqs_greater_than_20);
		System.out.println("number_of_reads_contain_primer is " + number_of_reads_contain_primer);
		System.out.println("number_of_reads_contain_adaptor is " + number_of_reads_contain_adaptor);
		System.out.println("number_of_reads_contain_both is " + number_of_reads_contain_both);
		
		long end = System.currentTimeMillis();
		
		long time = end - start;
		System.out.println("time  is " + time);
		
		
		
//		2) Fasta file containing reads greater than 100bp, average read quality scores greater than 20, primers and adaptors trimmed.
		
		
		
		
//		try {
//			String reactionFile = firePath + fileName + "fasta.txt";
//			BufferedWriter fasta = new BufferedWriter(new FileWriter(reactionFile));
//			
//			for (int i = 0; i < readsList.size(); i++) {
//				Reads current_read = readsList.get(i);
//				
//				if (current_read.getLength() > 100 && current_read.getAverage_quality_score() > 20) {				
//				}
//						
//			}
//
//			fasta.close();
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
		
		
		
		
		
		
		
		

	
	}

}
