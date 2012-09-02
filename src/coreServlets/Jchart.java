package coreServlets;

import java.io.File;
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


import org.jCharts.*;
import java.awt.*;
import org.jCharts.chartData.*;
import org.jCharts.properties.*;
import org.jCharts.types.ChartType;
import org.jCharts.axisChart.*;
import org.jCharts.test.TestDataGenerator;
import org.jCharts.encoders.JPEGEncoder13;
import org.jCharts.properties.util.ChartFont;
import org.jCharts.encoders.ServletEncoderHelper;

public class Jchart extends javax.servlet.http.HttpServlet implements
		javax.servlet.Servlet {
	static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String reactantName = "E";
		GlobalSystem system = GlobalSystem.getInstance();
		int totalTime = system.getTotal()/1000;   //convert to second
		int numOfPoints = totalTime/system.getReportInterval();
		
		ArrayList<Partition> partitionList = new ArrayList<Partition>();
		ArrayList<Reactant> reactantList = new ArrayList<Reactant>();
		
		partitionList = system.getPartitions();
		reactantList = partitionList.get(0).getReactantList();
		
		Reactant thisReactant = reactantList.get(0);
		
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
			
			String[] xAxisLabels = new String[numOfPoints];
			
			

//			String[] xAxisLabels = { "1998", "1999", "2000", "2001",
//					"2002", "2003", "2004" };
			for (int i=1; i<=numOfPoints; i++) {
				xAxisLabels[i-1] = Integer.toString(i);			
			}
			
			//System.out.println("ok here 1");
			for (int j=0;j<xAxisLabels.length; j++) {
				System.out.println(xAxisLabels[j]+ " ");
			}
			System.out.println("ok here 1");
			
			String xAxisTitle = "Time";
			String yAxisTitle = "Numbers";
			String title = "Simulation result for " + thisReactant;
			DataSeries dataSeries = new DataSeries(xAxisLabels, xAxisTitle,
					yAxisTitle, title);

			//From AxisChartServlet.java:createAxisChartDataSet
//			double[][] data = TestDataGenerator.getRandomNumbers(3, 7, 200,
//					500);
			//double[] result = new double[thisReactant.getDataList().size()];
			double[][] data = new double[0][thisReactant.getDataList().size()];
			System.out.println(thisReactant.getDataList().size());
			
			for (int i=0; i<thisReactant.getDataList().size(); i++){
				data[0][i] = thisReactant.getDataList().get(i);				
			}
						
			//String[] legendLabels = { "Bugs", "Security Holes", "Backdoors" };
			String[] legendLabels = { reactantName };
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
		
	
	}
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}