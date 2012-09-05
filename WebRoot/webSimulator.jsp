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
		<link href="<%out.println(basePath);%>common.css" rel="stylesheet"
			type="text/css" />

	</head>

	<body>

		<div class="mainboard">

			<div id="introduction">
				<h1 align="center">
					Deterministic-Stochastic Crossover Algorithm
				</h1>
				<div id="introduction_text">
					<h2>
						What?
					</h2>
					<p>
						The deterministic-stochastic crossover algorithm is a novel method
						for simulating biological networks. It retains the high efficiency
						of deterministic method while still reflects the stochastic
						effects of random fluctuations of the system at lower
						concentration. The ability of revealing the stochastic property
						with high efficiency makes this algorithm very useful for
						researches and applications based on systems biology.
					</p>
					<h2>
						Why?
					</h2>
					<p>
						The deterministic method works well for systems with large
						molecular population, but fails to capture the inherent
						fluctuations and correlations of the species at lower
						concentration. The stochastic methods are able to capture the
						fluctuations at lower concentration, but are very computationally
						inefficient. Therefore, novel algorithms are needed to improve the
						accuracy of deterministic methods and efficiency of stochastic
						methods.
					</p>
					<h2>
						Features:
					</h2>
					<p style="margin-left: 200px; margin-top: -10px">
						<li>
							Support XML (eXtensible Markup Language) formatted model
							representation
						</li>
						<li>
							Support both elementatory and Michaelis-Menten type reactions
						</li>
						<li>
							Support Mutiple-compartment system
						</li>
						<li>
							Support system event during simulation
						</li>
					</p>
					<br />
				</div>

			</div>

			<div id="model_example">
				<div id="model_text">
					<h2>
						Model examples:
					</h2>
					<h4 align='left'>
						1. Michaelis-Menten enzyme kinetics &nbsp
						<a href="" target='_blank'>Reactions</a>&nbsp
						<a href="<%out.println(basePath);%>models/enzyme.xml"
							target='_blank'>XML</a>&nbsp
						<a
							href="
			<%out.println(basePath + "webSimulator.jsp?fileName=enzyme");%>">Run
							simulation</a>
					</h4>
					<h4 align='left'>
						2. Auto-regulatory genetic network &nbsp
						<a href="" target='_blank'>Reactions</a>&nbsp
						<a href="<%out.println(basePath);%>models/autoReg.xml"
							target='_blank'>XML</a>&nbsp
						<a
							href="
			<%out.println(basePath + "webSimulator.jsp?fileName=autoReg");%>">Run
							simulation</a>
					</h4>
					<h4 align='left'>
						3. Dimerisation kinetics &nbsp
						<a href="" target='_blank'>Reactions</a>&nbsp
						<a href="<%out.println(basePath);%>models/dim.xml" target='_blank'>XML</a>
						&nbsp
						<a
							href="
			<%out.println(basePath + "webSimulator.jsp?fileName=dim");%>">Run
							simulation</a>
					</h4>
					<h4 align='left'>
						4. lac operon &nbsp
						<a href="" target='_blank'>Reactions</a> &nbsp
						<a href="<%out.println(basePath);%>models/lac.xml" target='_blank'>XML</a>
						&nbsp
						<a
							href="
			<%out.println(basePath + "webSimulator.jsp?fileName=lac");%>">Run
							simulation</a>
					</h4>
<!--					<h4 align='left'>-->
<!--						5. Glycolysis &nbsp-->
<!--						<a href="" target='_blank'>Reactions</a> &nbsp-->
<!--						<a href="<%out.println(basePath);%>models/glycolysis.xml" target='_blank'>XML</a>-->
<!--						&nbsp-->
<!--						<a-->
<!--							href="-->
<!--			<%out.println(basePath + "webSimulator.jsp?fileName=glycolysis");%>">Run-->
<!--							simulation</a>-->
<!--					</h4>-->

				</div>
			</div>


			<%
				String fileName = "";
				String resultFile = "";

				//model xml file location
				String modelDiskPath = getServletContext().getRealPath("")
						+ File.separator + "models/";
				String simulationMethod = "crossover"; //default value			

				boolean isDeterministic = false;
				boolean isCrossover = true;

				//		String uploadFolder = "D:/webServer/";
				//String uploadFolder = "e:/Dropbox/major/cs_project/webServer/models/";

				GlobalSystem system = GlobalSystem.getInstance();

				if (request.getParameter("fileName") != null) {
					fileName = request.getParameter("fileName");
					system.setCurrentModel(fileName);
				} else if (!system.getCurrentModel().equals("")) {
					fileName = system.getCurrentModel();
				} else {
					fileName = "enzyme"; //default model
				}

				//fileName = "enzyme"; //default model

				if (request.getParameter("simulationMethod") != null
						&& request.getParameter("simulationMethod").equals(
								"deterministic")) {
					simulationMethod = "deterministic";
					system.setSimulationMethod("deterministic");
					isDeterministic = true;
					isCrossover = false;
				} else if (request.getParameter("simulationMethod") != null
						&& request.getParameter("simulationMethod").equals(
								"crossover")) {
					simulationMethod = "crossover";
					system.setSimulationMethod("crossover");
				} else if (!system.getSimulationMethod().equals("")) {
					simulationMethod = system.getSimulationMethod();
				} else {
					system.setSimulationMethod("crossover"); //default method
					isDeterministic = false;
					isCrossover = true;
				}

				//filePath = modelDiskPath + fileName;
				String xmlFile = modelDiskPath + fileName + ".xml";

				//String xmlFile = basePath + fileName + ".xml";
				//out.println(" basePath  is  " + basePath + "");
				//out.println(" modelDiskPath  is  " + modelDiskPath + "");
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

				float time = 0, dt = (float) 0.001; // dt =0.001s
				int totalTime = system.getTotal(); // 100s
				int reportInterval = system.getReportInterval(); // 100 points

				//int totalTime = 100000; // 100s
				//int reportInterval = 1000; // 100 points

				// pass user defined totalTime and numOfPoints for graph display
				synchronized (session) {

					if (session.getAttribute("totalTime") != null
							&& session.getAttribute("reportInterval") != null) {
						totalTime = (Integer) session.getAttribute("totalTime");
						system.setTotal(totalTime);
						reportInterval = (Integer) session
								.getAttribute("reportInterval");
						system.setReportInterval(reportInterval);

						//out.println(totalTime);
						//out.println (reportInterval);
					}

					session.removeAttribute("totalTime");
					session.removeAttribute("reportInterval");
				}

				// totalTime and numOfPoints submitted by form are checked here.
				if (request.getParameter("totalTime") != null) {
					totalTime = Integer.parseInt(request.getParameter("totalTime"));
					system.setTotal(totalTime);
					synchronized (session) {
						session.setAttribute("totalTime", totalTime);
					}
				}

				if (request.getParameter("numOfPoints") != null) {
					reportInterval = totalTime
							/ Integer.parseInt(request.getParameter("numOfPoints"));
					system.setReportInterval(reportInterval);
					synchronized (session) {
						session.setAttribute("reportInterval", reportInterval);
					}
				}

				partitionList = system.getPartitions();

				timeList = system.getTimeList();

				reactantList = partitionList.get(0).getReactantList();

				/*			try {
				 reactionList = partitionList.get(0).getReactionList();
				 out.println("<p>there are " + reactionList.size()
				 + " reactions </p>");
				 } catch (Exception e) {
				 e.printStackTrace();
				 }
				 *///out.println(Function.printReactantListWeb(partitionList));

				rrandom random;
				random = new rrandom(1010101);

				try {
					resultFile = modelDiskPath + fileName + ".xls";
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

					for (int i = 0; i < totalTime; i += reportInterval) {
						for (int j = 0; j < reportInterval; j++) {
							if (simulationMethod.equals("crossover")) {
								for (int m = 0; m < system.getPartitions().size(); m++) {
									Partition currentPartion = system
											.getPartitions().get(m);
									currentPartion.reactionProceedCrossover(dt,
											random);
								}
							}

							if (simulationMethod.equals("deterministic")) {
								for (int m = 0; m < system.getPartitions().size(); m++) {
									Partition currentPartion = system
											.getPartitions().get(m);
									currentPartion.reactionProceedDeterministic(dt,
											random);
								}
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
			%>


			<div id="parameters_div">
				<h2 align="left">
					Parameters setting
				</h2>

				<form name="myform" id="myForm" method="post"
					action="webSimulator.jsp" onsubmit="return validate_form(this)">
					<!--					<fieldset id="parameters_fieldset">-->
					<!--						<legend>-->
					<!--							Parameters setting-->
					<!--						</legend>-->




					<table class='parameters_table' cellpadding="3" cellspacing="3">
						<tr>
							<td>
								<%
									String message = "Please choose a model to test.";
									if (system.getCurrentModel() != null) {
										message = system.getCurrentModel();
									}
									out.print("Current Model is: &nbsp &nbsp " + message);
								%>
							</td>
						</tr>
						<tr>
							<td>
								<%
									out.print("Current simulation method is: &nbsp &nbsp "
											+ system.getSimulationMethod());
								%>
							</td>
						</tr>
						<tr>
							<td>
								Total time (s):&nbsp &nbsp &nbsp
								<input type="text" id="totalTime" name="totalTime"
									value="<%out.print(system.getTotal());%>" size="30"
									maxlength="10" tabindex="1" />
							</td>
						</tr>

						<tr>
							<td>
								Number of points:
								<input type="text" id="numOfPoints" name="numOfPoints"
									value="<%out.print(system.getTotal() / system.getReportInterval());%>"
									size="30" maxlength="10" tabindex="1" />
							</td>
						</tr>

						<tr>
							<td>
								<input type="radio" name='simulationMethod'
									value="deterministic""<%if (isDeterministic) {
				out.print("checked");
			}%>">
								Deterministic
								<input type="radio" name='simulationMethod' value="crossover""<%if (isCrossover) {
				out.print("checked");
			}%>">
								Crossover
							</td>
						</tr>

						<tr>
							<td align="right">
								<input type="submit" name="submit" value="Submit Change"
									tabindex="4" />
							</td>
						</tr>

					</table>
					<!--					</fieldset>-->
				</form>
			</div>
			<div id="result_div">



				<%
					resultFile = basePath + "models/" + fileName + ".xls";

					out
							.println("<h4 align='left'>The simulation result can be downloaded <a href="
									+ resultFile + " target='_blank'>here</a>.</h4>");

					//out.println(resultFile);

					for (int m = 0; m < partitionList.size(); m++) {

						String partitionName = partitionList.get(m).getPartitionName();

						out.println("<h4 align='left'> Compartment: " + partitionName
								+ "</h4>");

						out.println("<table class='result_table' border='1'> ");

						out.println("<tr>");
						//out.println("<td> Reactant ID </td>");
						out.println("<td> Name </td>");
						out.println("<td> Chemstat </td>");
						out.println("<td> Initial value </td>");
						out.println("<td> Final value </td>");
						out.println("<td> Display Graph </td>");
						out.println("</tr>");
						
						reactantList = partitionList.get(m).getReactantList();
						
						for (int i = 0; i < reactantList.size(); i++) {
							Reactant thisReactant = reactantList.get(i);
							if (!thisReactant.getMy_chemical_name().equals("empty")
									&& (!thisReactant.getIs_enzyme())
									&& thisReactant.getOutputTo().equals(partitionName)) {
								out.println("<tr>");
								//out.println("<td>" + i + "</td>");
								out.println("<td>" + thisReactant.getMy_chemical_name()
										+ "</td>");
								out.println("<td>" + thisReactant.getChemostat()
										+ "</td>");
								out.println("<td>" + thisReactant.getInitial_number()
										+ "</td>");

								double finalNumber = Math.round(thisReactant
										.getNumber() * 100.0) / 100.0; // round the number to two decimal point
								out.println("<td>" + finalNumber + "</td>");
								//out.println("<td>" + thisReactant.getMy_chemical_name()
								//		+ "</td>");
								out.println("<td>"
										+ "<a href='"
										+ basePath
										+ "webSimulator.jsp?reactantToDisplay="
										+ thisReactant.getMy_chemical_name()
										//+ "?totalTime=" + totalTime
										//+ "?numOfPoints=" + totalTime/reportInterval
										+ "'> Display "
										+ thisReactant.getMy_chemical_name()
										+ "</a></td>");
								out.println("</tr>");

							}
						}
						

						out.println("</table><br><br>");
					}
				%>

			</div>

			<div id="graph_div">


				<!--				<img src="servlet/coreServlets.Jchart" alt="Simulation graph"-->
				<!--					height="360" width="550" />-->

				<%
					String reactantToDisplay = "";

					if (request.getParameter("reactantToDisplay") != null) {
						reactantToDisplay = request.getParameter("reactantToDisplay");
						out
								.println("<img src='servlet/coreServlets.Jchart?reactantToDisplay="
										+ reactantToDisplay
										+ "' alt='Simulation graph' height='360' width='550' />");

						// To pass the current totalTime and reportInterval in case user has made changes.	
						synchronized (session) {
							session.setAttribute("totalTime", totalTime);
							session.setAttribute("reportInterval", reportInterval);
						}

					} else {
						out.println("Please choose a reactant to display.");
					}
				%>




			</div>


		</div>



	</body>
</html>
