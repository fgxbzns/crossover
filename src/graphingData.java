

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;

public class graphingData extends JPanel {

    ArrayList<Double> dataList = new ArrayList<Double>();
    final int PAD = 20;
    String color = "red";  //default color
    static int xOrigin = 50;
    static int yOrigin = 50;

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        // Draw ordinate.
        g2.draw(new Line2D.Double(PAD, PAD, PAD, h-PAD));
        // Draw abscissa.
        g2.draw(new Line2D.Double(PAD, h-PAD, w-PAD, h-PAD));
        double xInc = (double)(w - 2*PAD)/(dataList.size()-1);
        double max = Collections.max(dataList);
        double scale = (double)(h - 2*PAD)/max;
        // Mark data points.
        g2.setPaint(Color.red);
        for(int i = 0; i < dataList.size(); i++) {
            double x = PAD + i*xInc;
            double y = h - PAD - scale*dataList.get(i);
            g2.fill(new Ellipse2D.Double(x-2, y-2, 4, 4));
        }
    }

//    private long getMax() {
//        long max = -Integer.MAX_VALUE;
//        for(int i = 0; i < dataList.size(); i++) {
//            if(dataList.get(i) > max)
//                max = dataList.get(i);
//        }
//        return max;
//    }
    
    public static void setColor () {
    	
    }
    
    public void setDataSet (ArrayList<Double> dataList) {
    	this.dataList = dataList;
    }
    
    public static void drawGraph (ArrayList<Double> dataList, String title) {
    	 JFrame f = new JFrame(title);
         f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         f.setBackground(Color.white);
         graphingData graph = new graphingData();
         graph.dataList = dataList;
         f.add(graph);
         f.setSize(400,400);
         f.setLocation(xOrigin,yOrigin);
         if (xOrigin < 800) {
        	 xOrigin += 400;        
         } else if (yOrigin < 400) {
        	 xOrigin = 50;
        	 yOrigin += 400;
         } else {
        	 xOrigin = 100;
        	 yOrigin = 100;
         }
         f.setVisible(true);
    }
}