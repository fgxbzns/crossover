//Exercise program of Center for Infection and Immunity
//Stores functions to be used in other program.
//Author: Guoxing Fu 
//Oct. 4 2011

package cii;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Function {

	final private static String primer = "CGCCGTTTCCCAGTAGGTCTC";
	final private static String adaptor = "ACTGAGTGGGAGGCAAGGCACACAGGGGATAGG";

	/**
	 * To calculate the avrage of quality score
	 * 
	 * @param quality_score
	 * @return
	 */
	public static double get_quality_score_avrage(
			ArrayList<String> quality_score) {
		double sum = 0;
		double average_quality_score = 0;

		for (int i = 0; i < quality_score.size(); i++) {
			sum += Integer.valueOf(quality_score.get(i));
		}
		average_quality_score = sum / quality_score.size();
		return average_quality_score;
	}

	/**
	 * To check if the sequence contains primer
	 * 
	 * @param sequence
	 * @param primer
	 * @return
	 */
	public static boolean check_if_contains_primer(String sequence,
			String primer) {
		int index = sequence.indexOf(primer);
		if (index >= 0) {
			return true;
		}
		return false;
	}

	/**
	 * To check if the sequence contains adatpor
	 * 
	 * @param sequence
	 * @param adaptor
	 * @return
	 */
	public static boolean check_if_contains_adatpor(String sequence,
			String adaptor) {
		int index = sequence.indexOf(adaptor);
		if (index >= 0) {
			return true;
		}
		return false;
	}

	/**
	 * To check if the sequence contains both primer and adaptor
	 * 
	 * @param sequence
	 * @param primer
	 * @param adaptor
	 * @return
	 */
	public static boolean check_if_contains_both(String sequence,
			String primer, String adaptor) {
		if (check_if_contains_primer(sequence, primer)
				&& check_if_contains_adatpor(sequence, adaptor)) {
			return true;
		}
		return false;
	}

	/**
	 * Store information from each read into a Reads object
	 * 
	 * @param fna_file
	 * @param qual_file
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<Reads> getReadsList(String fna_file,
			String qual_file) throws Exception {

		Scanner reader = new Scanner(System.in);
		Scanner fnaFileInput = null;
		Scanner qualFileInput = null;
		ArrayList<Reads> readsList = new ArrayList<Reads>();
		Reads current_read = new Reads();
		;

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

		StringBuffer sequence = new StringBuffer("");

		if (fnaFileInput.hasNext()) {
			String currentFnaElement = fnaFileInput.next();
			String currentQualElement = null;
			qualFileInput.next();

			while (fnaFileInput.hasNextLine()) {

				if (currentFnaElement.startsWith(">")) {
					current_read = new Reads();

					String identifier;
					int length;
					String xy;
					String region;
					String run;
					sequence = new StringBuffer(""); // start a new read, empty
														// the sequence.
					sum_quality_score = 0;
					number_of_quality_score = 0;

					identifier = currentFnaElement.substring(currentFnaElement
							.indexOf('>') + 1);

					currentFnaElement = fnaFileInput.next();
					length = Integer.parseInt(currentFnaElement
							.substring(currentFnaElement.indexOf('=') + 1));
					qualFileInput.next();

					currentFnaElement = fnaFileInput.next();
					xy = currentFnaElement.substring(currentFnaElement
							.indexOf('=') + 1);
					qualFileInput.next();

					currentFnaElement = fnaFileInput.next();
					region = currentFnaElement.substring(currentFnaElement
							.indexOf('=') + 1);
					qualFileInput.next();

					currentFnaElement = fnaFileInput.next();
					run = currentFnaElement.substring(currentFnaElement
							.indexOf('=') + 1);
					qualFileInput.next();

					currentFnaElement = fnaFileInput.next();
					currentQualElement = qualFileInput.next();

					current_read.setIdentifier(identifier);
					current_read.setLength(length);
					current_read.setXy(xy);
					current_read.setRegion(region);
					current_read.setRun(run);

				} else {
					while (!currentFnaElement.startsWith(">")
							&& fnaFileInput.hasNext()) {
						sequence.append(currentFnaElement);
						currentFnaElement = fnaFileInput.next();
					}
					if (!fnaFileInput.hasNext()) {
						sequence.append(currentFnaElement);
					}
					String seq = sequence.toString();
					current_read.setSequence(seq);
					current_read.check_if_contains_primer();
					current_read.check_if_contains_adatpor();
					current_read.check_if_contains_both();

					while (!currentQualElement.startsWith(">")
							&& qualFileInput.hasNext()) {
						quality_score.add(currentQualElement);
						currentQualElement = qualFileInput.next();
					}
					if (!qualFileInput.hasNext()) {
						quality_score.add(currentQualElement);

					}

					// System.out.println("quality_score size is " +
					// quality_score.size());
					// System.out.println("quality_score is " + quality_score);

					current_read.setQuality_score(quality_score);
					current_read.set_quality_score_avage();

					readsList.add(current_read);
					// System.out.println("quality_score ave is " +
					// current_read.getAverage_quality_score());

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

	/**
	 * This method store the output in a results object. It is faster than the
	 * method "getReadsList".
	 * 
	 * @param fna_file
	 * @param qual_file
	 * @return
	 * @throws Exception
	 */
	public static Results processReadsList(String fna_file, String qual_file)
			throws Exception {

		Scanner fnaFileInput = null;
		Scanner qualFileInput = null;
		Results result = new Results();

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

		StringBuffer sequence = new StringBuffer("");

		if (fnaFileInput.hasNext()) {
			String currentFnaElement = fnaFileInput.next();
			String currentQualElement = null;
			qualFileInput.next();

			while (fnaFileInput.hasNextLine()) {

				if (currentFnaElement.startsWith(">")) {

					String identifier;
					int length;
					String xy;
					String region;
					String run;
					sequence = new StringBuffer(""); // start a new read, empty
														// the sequence.
					sum_quality_score = 0;
					number_of_quality_score = 0;

					identifier = currentFnaElement.substring(currentFnaElement
							.indexOf('>') + 1);

					currentFnaElement = fnaFileInput.next();
					length = Integer.parseInt(currentFnaElement
							.substring(currentFnaElement.indexOf('=') + 1));
					qualFileInput.next();

					currentFnaElement = fnaFileInput.next();
					xy = currentFnaElement.substring(currentFnaElement
							.indexOf('=') + 1);
					qualFileInput.next();

					currentFnaElement = fnaFileInput.next();
					region = currentFnaElement.substring(currentFnaElement
							.indexOf('=') + 1);
					qualFileInput.next();

					currentFnaElement = fnaFileInput.next();
					run = currentFnaElement.substring(currentFnaElement
							.indexOf('=') + 1);
					qualFileInput.next();

					currentFnaElement = fnaFileInput.next();
					currentQualElement = qualFileInput.next();
					// System.out.println("currentQualElement is " +
					// currentQualElement);

					result.increase_number_of_total_reads();
					if (length > 100) {
						result.increase_number_of_reads_greater_than_100bp();
					}

				} else {
					while (!currentFnaElement.startsWith(">")
							&& fnaFileInput.hasNext()) {
						sequence.append(currentFnaElement);
						currentFnaElement = fnaFileInput.next();
					}
					if (!fnaFileInput.hasNext()) {
						sequence.append(currentFnaElement);
					}
					String seq = sequence.toString();

					if (check_if_contains_primer(seq, primer)) {
						result.increase_number_of_reads_contain_primer();
					}
					if (check_if_contains_adatpor(seq, adaptor)) {
						result.increase_number_of_reads_contain_adaptor();
					}
					if (check_if_contains_both(seq, primer, adaptor)) {
						result.increase_number_of_reads_contain_both();
					}

					while (!currentQualElement.startsWith(">")
							&& qualFileInput.hasNext()) {
						quality_score.add(currentQualElement);
						currentQualElement = qualFileInput.next();
					}
					if (!qualFileInput.hasNext()) {
						quality_score.add(currentQualElement);
					}

					if (get_quality_score_avrage(quality_score) > 20) {
						result.increase_number_of_reads_aqs_greater_than_20();
					}
				}
			}
		} else {
			System.out.println("The file is empty, please try again.");
			System.exit(1);
		}

		fnaFileInput.close();
		qualFileInput.close();

		return result;

	}

	/**
	 * To print the reads list
	 * 
	 * @param readsList
	 */
	public static void printReadsList(ArrayList<Reads> readsList) {

		for (int i = 0; i < readsList.size(); i++) {
			Reads current_read = readsList.get(i);

			System.out.println("Identifier is : "
					+ current_read.getIdentifier() + " ");
			System.out.println("Length is : " + current_read.getLength() + " ");
			System.out.println("quality_score is : "
					+ current_read.getQuality_score() + " ");
			System.out.println("average_quality_score is: "
					+ current_read.getAverage_quality_score() + " ");
			System.out.println("primer : " + current_read.getContain_primer());
			System.out
					.println("adaptor : " + current_read.getContain_adaptor());
			System.out.println("both : " + current_read.getContain_both());
			System.out.println("sequence: " + current_read.getSequence());

		}
	}

}
