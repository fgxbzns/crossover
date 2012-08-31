
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
		
	
	}
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}