package fzi.mottem.runtime.reportgenerator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class DBVisualization extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5393215617082386382L;
	
	Point2D.Double[] points;
    int sampleRate;
    Path2D.Double path;
    Line2D.Double[] connectors;
    boolean showConnections = false;
    boolean removePoint = false;
    boolean firstTime = true;

    public DBVisualization(double[] results,int sampleRate) {
        points = new Point2D.Double[results.length];
        this.sampleRate = sampleRate;
        for(int j = 0; j < results.length; j++) {
            points[j] = new Point2D.Double(10 + j*sampleRate, 200-results[j]*100);
        } 

        path = new Path2D.Double();
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                            RenderingHints.VALUE_STROKE_PURE);
        if(firstTime) {
            firstTime = false;
       //     setPath();
        }
        
        g2.drawString("SampleRate: " + sampleRate , 10, 50); ;
        
        g2.drawString("1V", 10, 100);
        g2.drawString("0.5V", 10, 150);
        g2.drawString("0V", 10, 200);
        g2.drawString("-0.5V", 10, 250);
        g2.drawString("-1V", 10, 300);
        
        for (int x = 0; x < 800; x +=100) {
        	g2.setPaint(Color.pink);
        	for (int i = 0; i < 800; i++) {
            	Rectangle2D.Double rect = new Rectangle2D.Double(10 + x - 0.5, i - 0.5, 1, 1);
            	g2.draw(rect);
            }
        }
        
        for (int y = 0; y < 800; y+=25) {
        	g2.setPaint(Color.red);
        	if (y %100 == 0) {
        		g2.setPaint(Color.red.darker());
        		for (int i = 0; i < 800; i++) {
                	Rectangle2D.Double rect = new Rectangle2D.Double(10 + i - 0.5, y - 0.5, 1, 1);
                	g2.draw(rect);
                }
        	}
            for (int i = 0; i < 200; i++) {
            	Rectangle2D.Double rect = new Rectangle2D.Double(10 + 6*i - 0.5, y - 0.5, 1, 1);
            	g2.draw(rect);
            } 
        }
 
        g2.setPaint(Color.green.darker());
        for (int i =0; i < points.length; i++) {
        	if (points[i] == null) {
        		continue;
        	}
        	Rectangle2D.Double rect = new Rectangle2D.Double(points[i].x - 0.5, points[i].y - 0.5, 1, 1);
        	g2.draw(rect);
        }
        g2.draw(path);
        g2.setPaint(Color.red);
      
    }



}
