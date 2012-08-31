<%@ page language="java"
	import="java.util.*,java.sql.*,javax.servlet.*,java.io.*,org.w3c.dom.Document,coreServlets.*"
	import="java.awt.*,org.jCharts.*,org.jCharts.chartData.*,org.jCharts.properties.*,org.jCharts.types.ChartType,org.jCharts.axisChart.*,org.jCharts.test.TestDataGenerator,org.jCharts.encoders.JPEGEncoder13,org.jCharts.properties.util.ChartFont,
					  org.jCharts.encoders.ServletEncoderHelper,org.jCharts.*"
	pageEncoding="ISO-8859-1"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>Crossover Algorithm</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">

		<%-- css --%>
		<!--		<link href="http://i5-pc:8080/crossover/common.css" rel="stylesheet"-->
		<!--			type="text/css" />-->
		<link href="http://i3-540:8081/crossover/common.css" rel="stylesheet"
			type="text/css" />

	</head>

	<body>

		<h1 align="center">
			Crossover Algorithm
		</h1>
		<br>
		<br>

		<div class="mainboard">

		<h4 align='left'>
			1. Enzyme
			<a href="" target='_blank'>Model description</a>
			<a href="" target='_blank'>XML</a>
			<a href="
			<% out.println(basePath+"webSimulator.jsp?fileName=enzyme"); %>" >Run simulation</a>
		</h4>
		<h4 align='left'>
			2. Auto gene regulatory
			<a href="" target='_blank'>Model description</a>
			<a href="" target='_blank'>XML</a>
			<a href="
			<% out.println(basePath+"webSimulator.jsp?fileName=autoReg"); %>" >Run simulation</a>
		</h4>
		<h4 align='left'>
			3. Lac
			<a href="" target='_blank'>Model description</a>
			<a href="" target='_blank'>XML</a>
			<a href="
			<% out.println(basePath+"webSimulator.jsp?fileName=lac"); %>" >Run simulation</a>
		</h4>
		<h4 align='left'>
			4. Dim
			<a href="" target='_blank'>Model description</a>
			<a href="" target='_blank'>XML</a>
			<a href="
			<% out.println(basePath+"webSimulator.jsp?fileName=dim"); %>" >Run simulation</a>
		</h4>

		
		<%
			String filePath = "";
			String fileName = "";
	//		String uploadFolder = "D:/webServer/";
			String uploadFolder = "d:/Dropbox/major/cs_project/webServer/models/";
			fileName = "autoReg";
			
			if(request.getParameter("fileName")!= null){
				fileName = request.getParameter("fileName");
			}
			//fileName = "enzyme";
			//fileName = "autoReg";
			//fileName = "glycolysis";
			
			float time = 0, dt = (float) 0.001; // dt =0.001s
			int total = 100000; // 100s
			int reportInterval = 500; // 1000 points

			filePath = uploadFolder + fileName;
			String xmlFile = uploadFolder + fileName + ".xml";

		//	out.println("<p> uploadFolder  is  " + uploadFolder + "</p>");
		//	out.println(" file path is  " + xmlFile + "");

			Document doc = null;
			doc = Function.getDocument(xmlFile);

			ArrayList<Partition> partitionList = new ArrayList<Partition>();

			ArrayList<Reactant> reactantList = new ArrayList<Reactant>();
			ArrayList<Reaction> reactionList = new ArrayList<Reaction>();
			
			ArrayList<String> timeList = new ArrayList<String>(); // list for time steps

			Function.getGlobalSystem(doc);

			// get partition with its reactants and reactions
			try {
				Function.getPartitionList(doc);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			GlobalSystem system = GlobalSystem.getInstance();

			partitionList = system.getPartitions();
	//		total = system.getTotal();						// 100s
	//		reportInterval = system.getReportInterval(); 	// 1000 points
			timeList = system.getTimeList();

			reactantList = partitionList.get(0).getReactantList();

/*			try {
				reactionList = partitionList.get(0).getReactionList();
				out.println("<p>there are " + reactionList.size()
						+ " reactions </p>");
			} catch (Exception e) {
				e.printStackTrace();
			}
	*/		//out.println(Function.printReactantListWeb(partitionList));

			rrandom random;
			random = new rrandom(1010101);

			try {
				String resultFile = uploadFolder + fileName + ".xls";
				BufferedWriter outputFile = new BufferedWriter(new FileWriter(
						resultFile));

				outputFile.write("Time \t");

				// write name for species
				for (int m = 0; m < partitionList.size(); m++) {
					String partitionName = partitionList.get(m)
							.getPartitionName();
					outputFile.write(partitionName + " \t");
					ArrayList<Reactant> currentReactantList = partitionList
							.get(m).getReactantList();
					for (int j = 0; j < currentReactantList.size(); j++) {
						Reactant currentReactant = new Reactant();
						currentReactant = currentReactantList.get(j);
						if (!currentReactant.getIs_enzyme()
								&& (currentReactant.getOutputTo()
										.equals(partitionName))) {
							outputFile.write(currentReactant
									.getMy_chemical_name()
									+ "\t");
						}
					}
				}

				outputFile.write("\n");
				outputFile.write(time + " \t");
				//system.getTimeList().add(""+time);
				
				// write initial number
				for (int m = 0; m < partitionList.size(); m++) {
					String partitionName = partitionList.get(m)
							.getPartitionName();
					outputFile.write(" \t");
					ArrayList<Reactant> currentReactantList = partitionList
							.get(m).getReactantList();
					for (int j = 0; j < currentReactantList.size(); j++) {
						Reactant currentReactant = new Reactant();
						currentReactant = currentReactantList.get(j);
						if (!currentReactant.getIs_enzyme()) {
							outputFile
									.write(currentReactant.getNumber() + "\t");
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
								outputFile.write(currentReactant.getNumber()
										+ "\t");
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

			//out.println(Function.printReactantListWeb(partitionList));
		%>


			<div id="parameters_div">




			</div>
			<div id="result_div">



				<%
		String resultFile = basePath + fileName + ".xls";
		
		
		//out.println(system.getTimeList().get(50));
		out.println(""+time);
		
		
		out.println("<h4 align='left'>You can download the simulation result<a href="+resultFile+" target='_blank'>here</a>.</h4>");
		
		//out.println(resultFile);
		
		for (int m = 0; m < partitionList.size(); m++) {
		
		String partitionName = partitionList.get(m).getPartitionName();
		
		out.println("<h3> Compartment: " + partitionName + "</h3>");
		
		out.println("<table class='result_table' border='1'> ");
	
		out.println("<tr>");
		out.println("<td> Reactant ID </td>");
		out.println("<td> Name </td>");
		out.println("<td> Chemstat </td>");
		out.println("<td> Initial value </td>");
		out.println("<td> Final value </td>");
		out.println("</tr>");
		
				
		for (int i = 0; i < reactantList.size(); i++) {
				Reactant thisReactant = reactantList.get(i);
			if(!thisReactant.getMy_chemical_name().equals("empty")  && (!thisReactant.getIs_enzyme()) && thisReactant.getOutputTo().equals(partitionName)){
				out.println("<tr>");
				out.println("<td>" + i + "</td>");
				out.println("<td>" + thisReactant.getMy_chemical_name() + "</td>");
				out.println("<td>" + thisReactant.getChemostat() + "</td>");
				out.println("<td>" + thisReactant.getInitial_number() + "</td>");
				out.println("<td>" + thisReactant.getNumber() + "</td>");
				out.println("</tr>");

			}
		}
		
		out.println("</table><br><br>");
		}
		
		
		 %>



			</div>


			<div id="graph_div"></div>
			
			
			 <img src="servlet/coreServlets.Jchart" alt="Smiley face" height="360" width="550" /> 
			
			
		</div>



	</body>
</html>
