<%@ page language="java" import="java.util.*"
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

		<title>My JSP 'jchart.jsp' starting page</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

	</head>

	<body>
		This is my JSP page.
		<br>

		<%
			try {
				//From AxisChartServlet.java:init()
				LegendProperties legendProperties = new LegendProperties();
				ChartProperties chartProperties = new ChartProperties();
				AxisProperties axisProperties = new AxisProperties(false);

				ChartFont axisScaleFont = new ChartFont(new Font(
						"Georgia Negreta cursiva", Font.PLAIN, 13), Color.black);
				axisProperties.getXAxisProperties().setScaleChartFont(
						axisScaleFont);
				axisProperties.getYAxisProperties().setScaleChartFont(
						axisScaleFont);

				ChartFont axisTitleFont = new ChartFont(new Font(
						"Arial Narrow", Font.PLAIN, 14), Color.black);
				axisProperties.getXAxisProperties().setTitleChartFont(
						axisTitleFont);
				axisProperties.getYAxisProperties().setTitleChartFont(
						axisTitleFont);

				Stroke[] strokes = { LineChartProperties.DEFAULT_LINE_STROKE,
						LineChartProperties.DEFAULT_LINE_STROKE,
						LineChartProperties.DEFAULT_LINE_STROKE };
				Shape[] shapes = { PointChartProperties.SHAPE_TRIANGLE,
						PointChartProperties.SHAPE_DIAMOND,
						PointChartProperties.SHAPE_CIRCLE };
				LineChartProperties lineChartProperties = new LineChartProperties(
						strokes, shapes);

				String[] xAxisLabels = { "1998", "1999", "2000", "2001",
						"2002", "2003", "2004" };
				String xAxisTitle = "Years";
				String yAxisTitle = "Problems";
				String title = "Micro$oft At Work";
				DataSeries dataSeries = new DataSeries(xAxisLabels, xAxisTitle,
						yAxisTitle, title);

				//From AxisChartServlet.java:createAxisChartDataSet
				double[][] data = TestDataGenerator.getRandomNumbers(3, 7, 200,
						500);
				String[] legendLabels = { "Bugs", "Security Holes", "Backdoors" };
				Paint[] paints = TestDataGenerator.getRandomPaints(3);
				AxisChartDataSet acds = new AxisChartDataSet(data,
						legendLabels, paints, ChartType.LINE,
						lineChartProperties);
				dataSeries.addIAxisPlotDataSet(acds);
				AxisChart axisChart = new AxisChart(dataSeries,
						chartProperties, axisProperties, legendProperties, 550,
						360);

				ServletEncoderHelper.encodeJPEG13(axisChart, 1.0f, response);

			} catch (Exception e) {
				System.out.println(e);
			}
			
						
		%>


	</body>
</html>
