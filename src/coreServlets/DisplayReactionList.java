package coreServlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;

public class DisplayReactionList extends javax.servlet.http.HttpServlet
		implements javax.servlet.Servlet {
	static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		out
				.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\"> \n"
						+ "<HTML>\n"
						+ "<HEAD>"
						+ "<TITLE>Survey Report</TITLE>"
						+ "<link href='http://i5-pc:8080/crossover/common.css' rel='stylesheet' type='text/css' />"
						+ "</HEAD>\n"
						+ "<font><strong><h1 align='center'>Crossover Algorithm</h1>"
						+ "</strong></font><br><br>");

		String fileName = "";

		out.println("</head>");
		out.println("<body>");

		GlobalSystem system = GlobalSystem.getInstance();
		
		if (request.getParameter("fileName") != null) {
			fileName = request.getParameter("fileName");
			system.setCurrentModel(fileName);
		} else if (!system.getCurrentModel().equals("")) {
			fileName = system.getCurrentModel();
		} 

		// System.out.println("totalTime: " + totalTime);
		// System.out.println("getReportInterval: " +
		// system.getReportInterval());

		ArrayList<Partition> partitionList = new ArrayList<Partition>();
		ArrayList<Reactant> reactantList = new ArrayList<Reactant>();
		ArrayList<Reaction> reactionList = new ArrayList<Reaction>();
		
		String modelDiskPath = getServletContext().getRealPath("")
		+ File.separator + "models\\";
		
		String xmlFile = modelDiskPath + fileName + ".xml";

		//String xmlFile = basePath + fileName + ".xml";
		//out.println(" basePath  is  " + basePath + "");
		//out.println(" modelDiskPath  is  " + modelDiskPath + "");
		//out.println(" xmlFile is  " + xmlFile + "");

		Document doc = null;
		doc = Function.getDocument(xmlFile);
		Function.getGlobalSystem(doc);
		// get partition with its reactants and reactions
		try {
			Function.getPartitionList(doc);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		partitionList = system.getPartitions();

		for (int m = 0; m < partitionList.size(); m++) {
			
			String partitionName = partitionList.get(m).getPartitionName();

			out.println("<h3> Compartment: " + partitionName
					+ "</h3>");

			reactantList = partitionList.get(m).getReactantList();
			reactionList = partitionList.get(m).getReactionList();
			out.println("<h3 > List of reactant: </h3>");
			for (int i = 0; i < reactantList.size(); i++) {		
				out.println("<h4>" + reactantList.get(i).getMy_chemical_name()
						+ " = " + reactantList.get(i).getNumber() + "</h4>");
			}
			out.println("");
			out.println("<h3> List of reaction: </h3>");
			for (int i = 0; i < reactionList.size(); i++) {			
				out.println("<h4>" + reactionList.get(i).getId() + ", \t");
				ArrayList<Reactant> input_species = reactionList.get(i)
						.getInput_species();
				int inputSize = input_species.size();
				for (int j = 0; j < input_species.size(); j++) {
					out.println(input_species.get(j).getMy_chemical_name());
					if (inputSize > 1) {
						out.println(" + ");
					}
					inputSize--;
				}
				out.println(" -> ");

				ArrayList<Reactant> output_species = reactionList.get(i)
						.getOutput_species();
				int outputSize = output_species.size();
				for (int j = 0; j < output_species.size(); j++) {
					out
							.println(output_species.get(j)
									.getMy_chemical_name());
					if (outputSize > 1) {
						out.println(" + ");
					}
					outputSize--;
				}
				out.println(", \t"
						+ String.valueOf(reactionList.get(i)
								.getRate_constant()));
				out.println("</h4>");
			}
		}
		out.println("</body>");
		out.println("</html>");

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}