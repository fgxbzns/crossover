package cii;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.w3c.dom.Document;

public class Simulator {
	
	private static String firePath = "e:/Dropbox/hm_share/green card/work/Columbia University/test/";
//	private static String fileName = "bioinformatics_test";	 
	private static String fileName = "bioinformatics_test_ori";	 
	private static String fnaFile = firePath + fileName + ".fna";
	private static String qualFile = firePath + fileName + ".qual";

	private static int reads_total_number;
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
		
		
		
		
		
		readsList = Function.getReadsList(fnaFile, qualFile);
		
		
		reads_total_number = readsList.size();
		for (int i = 0; i < readsList.size(); i++) {
			Reads current_read = readsList.get(i);
//			System.out.println("length is " + current_read.getLength());
//			System.out.println("length is " + current_read.getIdentifier());
			if (current_read.getLength() > 100) {
				
				number_of_reads_greater_than_100bp ++;
			}			
			if (current_read.getAverage_quality_score() > 20) {
				number_of_reads_aqs_greater_than_20 ++;
			}
			if (current_read.getContain_primer()) {
				number_of_reads_contain_primer ++;
			}
			if (current_read.getContain_adaptor()) {
				number_of_reads_contain_adaptor ++;
			}
			if (current_read.getContain_both()) {
				number_of_reads_contain_both ++;
			}
			
			
		}
		
//		Function.printReadsList(readsList);
		
		System.out.println("reads_total_number is " + reads_total_number);
		System.out.println("number_of_reads_greater_than_100bp is " + number_of_reads_greater_than_100bp);
		System.out.println("number_of_reads_aqs_greater_than_20 is " + number_of_reads_aqs_greater_than_20);
		System.out.println("number_of_reads_contain_primer is " + number_of_reads_contain_primer);
		System.out.println("number_of_reads_contain_adaptor is " + number_of_reads_contain_adaptor);
		System.out.println("number_of_reads_contain_both is " + number_of_reads_contain_both);
		
		long end = System.currentTimeMillis();
		
		long time = end - start;
		System.out.println("time  is " + time);

	
	}

}
