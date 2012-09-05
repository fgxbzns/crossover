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

		GlobalSystem system = GlobalSystem.getInstance();
		int totalTime = system.getTotal(); // convert to second
		int numOfPoints = 100;
		if (system.getReportInterval() != 0) {
			numOfPoints = totalTime / system.getReportInterval();
		}

		// System.out.println("totalTime: " + totalTime);
		// System.out.println("getReportInterval: " +
		// system.getReportInterval());

		ArrayList<Partition> partitionList = new ArrayList<Partition>();
		ArrayList<Reactant> reactantList = new ArrayList<Reactant>();

		partitionList = system.getPartitions();
		reactantList = partitionList.get(0).getReactantList();

		String reactantToDisplay = "";
		Reactant thisReactant = reactantList.get(0);

		if (request.getParameter("reactantToDisplay") != null) {
			// System.out.println("ok here 10");
			reactantToDisplay = request.getParameter("reactantToDisplay");
			for (int m = 0; m < partitionList.size(); m++) {
				reactantList = partitionList.get(m).getReactantList();
				for (int i = 0; i < reactantList.size(); i++) {
					if (reactantToDisplay.equals(reactantList.get(i)
							.getMy_chemical_name())) {
						thisReactant = reactantList.get(i);
					}
				}
			}
		}
		// else {
		// thisReactant = reactantList.get(0);
		// }
		try {
			// From AxisChartServlet.java:init()
			LegendProperties legendProperties = new LegendProperties();
			ChartProperties chartProperties = new ChartProperties();
			AxisProperties axisProperties = new AxisProperties(false);

			ChartFont axisScaleFont = new ChartFont(new Font(
					"Georgia Negreta cursiva", Font.PLAIN, 13), Color.black);
			axisProperties.getXAxisProperties()
					.setScaleChartFont(axisScaleFont);
			axisProperties.getYAxisProperties()
					.setScaleChartFont(axisScaleFont);

			ChartFont axisTitleFont = new ChartFont(new Font("Arial Narrow",
					Font.PLAIN, 14), Color.black);
			axisProperties.getXAxisProperties()
					.setTitleChartFont(axisTitleFont);
			axisProperties.getYAxisProperties()
					.setTitleChartFont(axisTitleFont);

			Stroke[] strokes = { LineChartProperties.DEFAULT_LINE_STROKE, };
			// LineChartProperties.DEFAULT_LINE_STROKE,
			// LineChartProperties.DEFAULT_LINE_STROKE };
			Shape[] shapes = { PointChartProperties.SHAPE_TRIANGLE, };
			// PointChartProperties.SHAPE_DIAMOND,};
			// PointChartProperties.SHAPE_CIRCLE };
			LineChartProperties lineChartProperties = new LineChartProperties(
					strokes, shapes);

			String[] xAxisLabels = new String[numOfPoints];

			// System.out.println("num of points: " + numOfPoints);

			// String[] xAxisLabels = { "1998", "1999", "2000", "2001",
			// "2002", "2003", "2004" };
			for (int i = 0; i < numOfPoints; i++) {
				xAxisLabels[i] = Integer.toString(i);
			}

			// System.out.println("ok here 1");
			// for (int j=0;j<xAxisLabels.length; j++) {
			// System.out.println(xAxisLabels[j]+ " ");
			// }
			//			

			String xAxisTitle = "Time";
			String yAxisTitle = "Reactant Number";
			String title = "Simulation result for "
					+ thisReactant.getMy_chemical_name();
			// String title = "Simulation result for ";
			DataSeries dataSeries = new DataSeries(xAxisLabels, xAxisTitle,
					yAxisTitle, title);

			// From AxisChartServlet.java:createAxisChartDataSet
			// double[][] data = TestDataGenerator.getRandomNumbers(3, 7, 200,
			// 500);

			// getRandomNumbers( int numberOfDataSets, int numToGenerate, double
			// minValue, double maxValue )
			//			
			// double[][] data=new double[ numberOfDataSets ][ numToGenerate ];
			// for( int j=0; j < numberOfDataSets; j++ )
			// {
			// for( int i=0; i < numToGenerate; i++ )
			// {
			// data[ j ][ i ]=getRandomNumber( minValue, maxValue );
			// }
			// }

			double[][] data = new double[1][numOfPoints];
			for (int j = 0; j < 1; j++) {
				for (int i = 0; i < numOfPoints; i++) {
					data[j][i] = thisReactant.getDataList().get(i);
				}
			}

			// double[] result = new double[thisReactant.getDataList().size()];
			// double[][] data = new
			// double[0][thisReactant.getDataList().size()];
			// System.out.println(thisReactant.getDataList().size());
			//			
			// for (int i=0; i<thisReactant.getDataList().size(); i++){
			// data[0][i] = thisReactant.getDataList().get(i);
			// }

			// String[] legendLabels = { "Bugs", "Security Holes", "Backdoors"
			// };
			String[] legendLabels = { thisReactant.getMy_chemical_name() };
			Paint[] paints = TestDataGenerator.getRandomPaints(1);
			AxisChartDataSet acds = new AxisChartDataSet(data, legendLabels,
					paints, ChartType.LINE, lineChartProperties);
			dataSeries.addIAxisPlotDataSet(acds);
			AxisChart axisChart = new AxisChart(dataSeries, chartProperties,
					axisProperties, legendProperties, 550, 360);

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