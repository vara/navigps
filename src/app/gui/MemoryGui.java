/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.gui;

import app.utils.MyLogger;
import app.utils.Utils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 *
 * @author vara
 */
public class MemoryGui extends JComponent implements Runnable,MouseListener{
    
    public static int KB = 1024;
    public static int MB = 1024*1024;
    public static int GB = 1024*1024*1024;
    
    private double mul = MemoryGui.MB;
    private String unitName = "MB";    
    private Runtime runtime = Utils.getRuntime();    
    private int wPaint = 120;
    private int hpaint = 40;
    private float tPaint = 2.0f;
    private long freeMem = 0;
    private long totalMem = 0;
    private long maxMem = 0;
    private int refresh = 1; //unit sec
    private boolean ShowGrid = true;
    private GridClass grid = new GridClass();    
    private boolean loopThread = true;    
    private Chart chart = null;
    
    private Border mainBorder = BorderFactory.createRaisedBevelBorder();    
    private Border mouseOnBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED,new Color(210,210,210,255), Color.WHITE);   
    
    public MemoryGui() {

	MyLogger.log.log(Level.FINE, "Init Memory Gui");
	chart = new Chart();	
	Thread thread = new Thread(this);
	thread.setPriority(Thread.MIN_PRIORITY);
	thread.start();
	setBounds(0,0,getWPaint()+(int)tPaint,getHPaint()+(int)tPaint);
	setPreferredSize(new Dimension(getWPaint()+(int)tPaint,getHPaint()+(int)tPaint));
	setBorder(mainBorder);	
	addMouseListener(this);	
	setToolTipText("Click to force garbage colector !");
    }

    @Override
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	BasicStroke OldStroke = (BasicStroke)g2.getStroke();
	
	AffineTransform at = g2.getTransform();	
	at.translate(tPaint,tPaint);
	g2.setTransform(at);
	
	//draw background		
	g2.setColor(new Color(207,206,189,255));
	Rectangle2D rec = new Rectangle2D.Double(0,0,getWPaint(),getHPaint());
	g2.fill(rec);
	
	//draw chart		
	Paint oldPaint = g2.getPaint();
	Shape shape = chart.transformToShape();	
	GradientPaint gradient1 = new GradientPaint(0.0f, 0.0f,new Color(159,166,173,250), 
							    getWPaint(), getHPaint(),new Color(205,214,223,250));
	
	g2.setPaint(gradient1);	
	g2.fill(shape);
	g2.setPaint(oldPaint);
	
	//grid
	if (isGrid()) {
	    g2.setStroke(new BasicStroke(0.2f));
	    g2.setColor(grid.getGridColor());
	    int countx = (int) getWPaint() / grid.getWidthGridCell();	    
	    
	    for(int i=0;i<=getWPaint();i+=countx){
		g2.drawLine(i,0, i, getHPaint());
	    }
	    for(int i=0;i<=getHPaint();i+=grid.getHeightGridCell()){
		g2.drawLine(0,i,getWPaint(),i);
	    }
	    g2.setStroke(OldStroke);
	}
	at.translate(-tPaint,-tPaint);
	g2.setTransform(at);
	
	//draw memory status
	FontMetrics fm = g2.getFontMetrics();
	String info = roundsValue((getTotalMem()-getFreeMem()),1)+"/"+roundsValue(getTotalMem(),1)+getUnitName();
	int x = (getWPaint() - fm.stringWidth(info)) / 3;
	int y = (fm.getAscent() + (getHPaint() - (fm.getAscent() + fm.getDescent())) / 2);	
	FontRenderContext frc = g2.getFontRenderContext();
	Font font = new Font("Serif", Font.BOLD, 14);
	GlyphVector gv = font.createGlyphVector(frc, info);
	//text shadow
	g2.setColor(new Color(30,30,30,150));
	g2.drawGlyphVector(gv,x+1,y+1);
	//orginal text
	g2.setColor(Color.WHITE);
	g2.drawGlyphVector(gv,x,y);
	
		
    }
    
    public String roundsValue(double val,int n){
	NumberFormat nf = NumberFormat.getInstance();
	nf.setMaximumFractionDigits(n);
	return nf.format(val/getMul());	
    }
    
    public void run() {
	try {

	    while(isLoopThread()){
		
		freeMem = runtime.freeMemory();
		totalMem = runtime.totalMemory();
		maxMem = runtime.maxMemory();		
		double convert = getTotalMem()/getMul();
		double scale = getHPaint()/convert;				
		chart.updatePoint((int)( ((getTotalMem()-getFreeMem())/getMul())*scale));
		repaint();
		Thread.sleep(getRefresh()*1000);
	    }
	} catch (InterruptedException ex) {
	    MyLogger.log.log(Level.WARNING, getClass().getName() + "\n" + ex);
	}
    }

    public int getWPaint() {
	return wPaint;
    }

    public void setWPaint(int wPaint) {
	this.wPaint = wPaint;
    }

    public int getHPaint() {
	return hpaint;
    }

    public void setHpaint(int hpaint) {
	this.hpaint = hpaint;
    }

    public long getFreeMem() {
	return freeMem;
    }

    public void setFreeMem(long freeMem) {
	this.freeMem = freeMem;
    }

    public long getTotalMem() {
	return totalMem;
    }

    public void setTotalMem(long totalMem) {
	this.totalMem = totalMem;
    }

    public long getMaxMem() {
	return maxMem;
    }

    public void setMaxMem(long maxMem) {
	this.maxMem = maxMem;
    }

    public int getRefresh() {
	return refresh;
    }

    public void setRefresh(int refresh) {
	this.refresh = refresh;
    }

    public boolean isGrid() {
	return ShowGrid;
    }

    public void gridEnable(boolean grid) {
	this.ShowGrid = grid;
    }

    public boolean isLoopThread() {
	return loopThread;
    }

    public void setLoopThread(boolean repaintThread) {
	this.loopThread = repaintThread;
    }

    public double getMul() {
	return mul;
    }

    public void setMul(int mul) {
	if(mul<MemoryGui.KB)
	    setUnitName("B");
	else if(mul<MemoryGui.MB)
	    setUnitName("KB");
	else if(mul<MemoryGui.GB)
	    setUnitName("MB");	
	else if(mul>MemoryGui.GB)
	    setUnitName("GB");
	this.mul = mul;
    }

    public String getUnitName() {
	return unitName;
    }

    public void setUnitName(String unitName) {
	this.unitName = unitName;
    }

    public class GridClass {

	private int width = 10;
	private int height = 10;
	
	private Color color = new Color(240,240,240,110);
	
	public int getWidthGridCell() {
	    return width;
	}

	public void setWidthGridCell(int width) {
	    this.width = width;
	}

	public int getHeightGridCell() {
	    return height;
	}

	public void setHeightGridCell(int height) {
	    this.height = height;	    
	}
	public void setGridColor(int r,int g,int b){
	    color = new Color(r,g,b,color.getAlpha());
	}
	public void setGridAlpha(int a){
	    color = new Color(color.getRed(),color.getGreen(),color.getBlue(),a);
	}
	public Color getGridColor(){
	    return color;
	}
    }
    
    private class Chart {
	
	private int [] bitMap = new int [getWPaint()];
	private int count=0;
	
	public Chart(){	    	    
	    
	    for (int i = 0; i < bitMap.length; i++)
		bitMap[i] = 0;			    
	}
	public void updatePoint(int y){
	    bitMap[getCount()]=y;
	}
	
	protected int getCount(){
	    
	    if(count<bitMap.length-1)
		count++;
	    else 
		rollValuesInTable(true);
	    return count;
	}
	protected void rollValuesInTable(boolean side){
	    //if true roll left
	    if(side)
		for (int i = 1; i < bitMap.length; i++) {
		    int j = bitMap[i];
		    bitMap[i-1]=j;
		
	    }else
		for (int i = bitMap.length-2; i==0; i--) {
		    int j = bitMap[i];
		    bitMap[i+1]=j;
		}	    
	}
	public Shape transformToShape(){
	    GeneralPath genPath = new GeneralPath();
	    genPath.moveTo(0,0);//start point
	    for (int i = 0; i < bitMap.length; i++) {
		int j = bitMap[i];
		genPath.lineTo(i,j);
	    }
	    genPath.lineTo(getWPaint(),0);//end point
	    genPath.closePath();
	    return genPath;
	}	
    }

    public void mouseClicked(MouseEvent e) {
	runtime.gc();
    }

    public void mousePressed(MouseEvent e) {
	
    }

    public void mouseReleased(MouseEvent e) {
	
    }

    public void mouseEntered(MouseEvent e) {
	setBorder(mouseOnBorder);
    }

    public void mouseExited(MouseEvent e) {
	setBorder(mainBorder);
    }
}
