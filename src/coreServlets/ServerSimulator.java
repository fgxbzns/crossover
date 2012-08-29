package coreServlets;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.w3c.dom.Document;

public class ServerSimulator extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {
	static final long serialVersionUID = 1L;

	private static final String DATA_DIRECTORY = "";
	private static final int MAX_MEMORY_SIZE = 1024 * 1024 * 2;
	private static final int MAX_REQUEST_SIZE = 1024 * 1024;
	static String filePath = "";
	static String fileName = "";

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\"> \n"
						+ "<HTML>\n"
						+ "<HEAD>"
						+ "<TITLE>Survey Report</TITLE>"
					    +"<link href='http://i5-pc:8080/crossover/common.css' rel='stylesheet' type='text/css' />"
						+ "</HEAD>\n"
						+ "<font><strong><h1 align='center'>Crossover Algorithm</h1>"
						+ "</strong></font><br><br>");

		// filePath = "D:/webServer/enzyme.xml";

		// String uploadFolder = getServletContext().getRealPath("")
		// + File.separator + DATA_DIRECTORY;
		String uploadFolder = "D:/webServer/";
		String fileName = "enzyme";
		// String fileName = "autoReg";
		filePath = uploadFolder + fileName;
		String xmlFile = uploadFolder + fileName + ".xml";

		// out.println("<html>");
		// out.println("<head>");
		// out.println("<title>Servlet upload</title>");
		
		out.println("</head>");
		out.println("<body>");

		out.println("<p> uploadFolder  is  " + uploadFolder + "</p>");
		out.println(" file path is  " + xmlFile + "");

		Document doc = null;
		doc = Function.getDocument(xmlFile);

		ArrayList<Partition> partitionList = new ArrayList<Partition>();
		ArrayList<Partition> initialPartitionList = new ArrayList<Partition>();

		ArrayList<Reactant> reactantList = new ArrayList<Reactant>();
		ArrayList<Reaction> reactionList = new ArrayList<Reaction>();

		Function.getGlobalSystem(doc);

		// get partition with its reactants and reactions
		try {
			Function.getPartitionList(doc);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		GlobalSystem system = GlobalSystem.getInstance();

		partitionList = system.getPartitions();
		initialPartitionList = partitionList;

		reactantList = partitionList.get(0).getReactantList();

		try {
			reactionList = partitionList.get(0).getReactionList();
			out.println("<p>there are " + reactionList.size()
					+ " reactions </p>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println(Function.printReactantListWeb(partitionList));

		float time = 0, dt = (float) 0.001; // dt =0.001s
		int total = 100000; // 100s
		int reportInterval = 500; // 1000 points

		rrandom random;
		random = new rrandom(1010101);

		try {
			String resultFile = uploadFolder + fileName + ".xls";
			BufferedWriter outputFile = new BufferedWriter(new FileWriter(
					resultFile));

			outputFile.write("Time \t");

			// write name for species
			for (int m = 0; m < partitionList.size(); m++) {
				String partitionName = partitionList.get(m).getPartitionName();
				outputFile.write(partitionName + " \t");
				ArrayList<Reactant> currentReactantList = partitionList.get(m)
						.getReactantList();
				for (int j = 0; j < currentReactantList.size(); j++) {
					Reactant currentReactant = new Reactant();
					currentReactant = currentReactantList.get(j);
					if (!currentReactant.getIs_enzyme()
							&& (currentReactant.getOutputTo()
									.equals(partitionName))) {
						outputFile.write(currentReactant.getMy_chemical_name()
								+ "\t");
					}
				}
			}

			outputFile.write("\n");
			outputFile.write(time + " \t");
			// write initial number
			for (int m = 0; m < partitionList.size(); m++) {
				String partitionName = partitionList.get(m).getPartitionName();
				outputFile.write(" \t");
				ArrayList<Reactant> currentReactantList = partitionList.get(m)
						.getReactantList();
				for (int j = 0; j < currentReactantList.size(); j++) {
					Reactant currentReactant = new Reactant();
					currentReactant = currentReactantList.get(j);
					if (!currentReactant.getIs_enzyme()) {
						outputFile.write(currentReactant.getNumber() + "\t");
					}
					currentReactant.getDataList().add(
							currentReactant.getNumber());
				}
			}

			outputFile.write("\n");

			for (int i = 0; i < total; i += reportInterval) {
				for (int j = 0; j < reportInterval; j++) {

					for (int m = 0; m < system.getPartitions().size(); m++) {
						Partition currentPartion = system.getPartitions()
								.get(m);
						currentPartion.reactionProceedCrossover(dt, random);
					}

					for (int m = 0; m < system.getPartitions().size(); m++) {
						Partition currentPartion = system.getPartitions()
								.get(m);
						currentPartion.update();
					}

					time += dt;
				}
				outputFile.write(time + "\t");

				for (int m = 0; m < partitionList.size(); m++) {
					String partitionName = partitionList.get(m)
							.getPartitionName();
					outputFile.write(" \t");
					ArrayList<Reactant> currentReactantList = partitionList
							.get(m).getReactantList();
					for (int j = 0; j < currentReactantList.size(); j++) {
						Reactant currentReactant = new Reactant();
						currentReactant = currentReactantList.get(j);
						if (!currentReactant.getIs_enzyme()
								&& (currentReactant.getOutputTo()
										.equals(partitionName))) {
							outputFile
									.write(currentReactant.getNumber() + "\t");
						}
						currentReactant.getDataList().add(
								currentReactant.getNumber());
					}
				}
				outputFile.write("\n");
			}
			outputFile.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		out.println("after simulation");

		out.println(Function.printReactantListWeb(partitionList));

		out.println("</body>");
		out.println("</html>");

		// displays done.jsp page after upload finished
		// getServletContext().getRequestDispatcher("/done.jsp").forward(
		// request, response);

		// } catch (FileUploadException ex) {
		// throw new ServletException(ex);
		// } catch (Exception ex) {
		// throw new ServletException(ex);
		// }
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}