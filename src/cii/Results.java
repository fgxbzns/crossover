package cii;

public class Results {

	private static int number_of_total_reads;
	private static int number_of_reads_greater_than_100bp;
	private static int number_of_reads_aqs_greater_than_20;
	private static int number_of_reads_contain_primer;
	private static int number_of_reads_contain_adaptor;
	private static int number_of_reads_contain_both;
	
	public static int getNumber_of_total_reads() {
		return number_of_total_reads;
	}
	public static void setNumber_of_total_reads(int number_of_total_reads) {
		Results.number_of_total_reads = number_of_total_reads;
	}
	public static int getNumber_of_reads_greater_than_100bp() {
		return number_of_reads_greater_than_100bp;
	}
	public static void setNumber_of_reads_greater_than_100bp(
			int number_of_reads_greater_than_100bp) {
		Results.number_of_reads_greater_than_100bp = number_of_reads_greater_than_100bp;
	}
	public static int getNumber_of_reads_aqs_greater_than_20() {
		return number_of_reads_aqs_greater_than_20;
	}
	public static void setNumber_of_reads_aqs_greater_than_20(
			int number_of_reads_aqs_greater_than_20) {
		Results.number_of_reads_aqs_greater_than_20 = number_of_reads_aqs_greater_than_20;
	}
	public static int getNumber_of_reads_contain_primer() {
		return number_of_reads_contain_primer;
	}
	public static void setNumber_of_reads_contain_primer(
			int number_of_reads_contain_primer) {
		Results.number_of_reads_contain_primer = number_of_reads_contain_primer;
	}
	public static int getNumber_of_reads_contain_adaptor() {
		return number_of_reads_contain_adaptor;
	}
	public static void setNumber_of_reads_contain_adaptor(
			int number_of_reads_contain_adaptor) {
		Results.number_of_reads_contain_adaptor = number_of_reads_contain_adaptor;
	}
	public static int getNumber_of_reads_contain_both() {
		return number_of_reads_contain_both;
	}
	public static void setNumber_of_reads_contain_both(
			int number_of_reads_contain_both) {
		Results.number_of_reads_contain_both = number_of_reads_contain_both;
	}
	
	public void increase_number_of_total_reads() {
		number_of_total_reads++;
	}
	
	public void increase_number_of_reads_greater_than_100bp() {
		number_of_reads_greater_than_100bp++;
	}
	public void increase_number_of_reads_aqs_greater_than_20() {
		number_of_reads_aqs_greater_than_20++;
	}
	public void increase_number_of_reads_contain_primer() {
		number_of_reads_contain_primer++;
	}
	public void increase_number_of_reads_contain_adaptor() {
		number_of_reads_contain_adaptor++;
	}
	public void increase_number_of_reads_contain_both() {
		number_of_reads_contain_both++;
	}
	
}
