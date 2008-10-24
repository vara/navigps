/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.gui;

import app.gui.*;
import app.gui.svgComponents.UpdateComponentsWhenChangedDoc;
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
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 *
 * @author vara
 */
public class MemoryGui extends JComponent implements Runnable,
						     MouseListener{
    
    public static final int KB = 1024;
    public static final int MB = 1024*1024;
    public static final int GB = 1024*1024*1024;
    
    protected static final int STARTED=0;
    protected static final int STOPED=1;
    protected static final int PAUSED=2;
    protected static final int RESUMED=3;    
    
    private double mul = MemoryGui.MB;
    private String unitName = "MB";    
    private Runtime runtime = Utils.getRuntime();    
    private int wPaint = 100;
    private int hpaint = 34;
    private float tPaint = 1.0f;
    private long freeMem = 0;
    private long totalMem = 0;
    private long maxMem = 0;
    private int refresh = 1; //unit sec
    
    private GridClass grid = new GridClass();    
    private boolean loopThread = false;    
    private Chart chart = null;
    
    private Border mainBorder = BorderFactory.createRaisedBevelBorder();    
    private Border mouseOnBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED,new Color(210,210,210,255), Color.WHITE);   
    
    private UpdateComponentsWhenChangedDoc letOfChanged = null;
    
    private boolean showText = true;
    private boolean showShadow = true;
    
    private boolean monitorStoped;
    private boolean monitorStared;
    private boolean monitorPaused;
    
    public MemoryGui() {
	init();
    }
    public MemoryGui(UpdateComponentsWhenChangedDoc l){
	letOfChanged = l;
	init();	
    }
    
    private void init(){
	
	
	MyLogger.log.log(Level.FINE, "Init Memory Gui");
	chart = new Chart();		
	setBounds(0,1,getWPaint()+(int)tPaint,getHPaint()+(int)tPaint+1);
	setPreferredSize(new Dimension(getWPaint()+(int)tPaint,getHPaint()+(int)tPaint+1));
	setBorder(mainBorder);	
	addMouseListener(this);	
	setToolTipText("Click to force garbage colector !");
	start();
    }
    
    @Override
    public void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
	g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	BasicStroke OldStroke = (BasicStroke)g2.getStroke();
	
	double transx =tPaint+getWPaint();
	double transy =tPaint+getHPaint();
	AffineTransform at = g2.getTransform();	
	at.translate(transx,transy);
	at.rotate(Math.PI);
	g2.setTransform(at);
	
	//draw background		
	g2.setColor(new Color(210,206,190));
	Rectangle2D rec = new Rectangle2D.Double(0,0,getWPaint(),getHPaint());
	g2.fill(rec);
	
	//draw chart		
	Paint oldPaint = g2.getPaint();
	Shape shape = chart.transformToShape();	
	GradientPaint gradient1 = new GradientPaint(0.0f, 0.0f,new Color(107,104,31,250), 
							    getWPaint(), getHPaint(),new Color(197,211,170,250));	
	g2.setPaint(gradient1);	
	g2.fill(shape);
	g2.setPaint(oldPaint);
	
	//grid
	if (grid.isShowGrid()) {
	    g2.setStroke(new BasicStroke(0.3f));
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
	at.rotate(Math.PI);
	at.translate(-transx,-transy);	
	g2.setTransform(at);
	
	//draw memory status
	if(isShowText()){
	    FontMetrics fm = g2.getFontMetrics();
	    String info = roundsValue((getTotalMem()-getFreeMem()),1)+"/"+roundsValue(getTotalMem(),1)+getUnitName();
	    int x = ((int)tPaint+getWPaint() - fm.stringWidth(info)) / 3;
	    int y = (fm.getAscent() + ((int)tPaint+getHPaint() - (fm.getAscent() + fm.getDescent())) / 2);	
	    FontRenderContext frc = g2.getFontRenderContext();
	    Font font = new Font("Serif", Font.BOLD, 14);
	    GlyphVector gv = font.createGlyphVector(frc, info);
	    //text shadow
	    if(isShowShadow()){
		g2.setColor(new Color(30,30,30,150));
		g2.drawGlyphVector(gv,x+1,y+1);
	    }
	    //orginal text
	    g2.setColor(Color.WHITE);
	    g2.drawGlyphVector(gv,x,y);
	}	
    }
    
    public String roundsValue(double val,int n){
	NumberFormat nf = NumberFormat.getInstance();
	nf.setMaximumFractionDigits(n);
	return nf.format(val/getMul());	
    }
    
    public void run() {
	try {
	//int xx = 0;
	    while(isLoopThread()){
		
		freeMem = runtime.freeMemory();
		totalMem = runtime.totalMemory();
		maxMem = runtime.maxMemory();		
		double convert = getTotalMem()/getMul();
		double scale = getHPaint()/convert;				
		chart.updatePoint((int)( ((getTotalMem()-getFreeMem())/getMul())*scale));
		//chart.updatePoint(xx%getHPaint());
		//xx++;
		repaint();
		Thread.sleep(getRefresh()*1000);
	    }
	} catch (InterruptedException ex) {
	    MyLogger.log.log(Level.WARNING, getClass().getName() + "\n" + ex);
	}
    }
    
    public void stop(){
	
	if(isLoopThread()){
	    setLoopThread(false);
	    chart.resetBitmap();
	    if(letOfChanged!=null)
		letOfChanged.currentStatusChanged("Memory Monitor stoped");
	    setStatusMonitor(MemoryGui.STOPED);
	    repaint();
	}
    }
    
    public void start(){
	
	if(!isLoopThread()){
	    
	    if(letOfChanged!=null)
	    letOfChanged.currentStatusChanged("Memory Monitor started");
	    
	    setLoopThread(true);
	    Thread thread = new Thread(this);
	    thread.setPriority(Thread.MIN_PRIORITY);
	    thread.start();
	    setStatusMonitor(MemoryGui.STARTED);    
	}
    }
        
    public void pause(){
	
	setLoopThread(false);
	
	if(letOfChanged!=null)
	    letOfChanged.currentStatusChanged("Memory Monitor paused");
	setStatusMonitor(MemoryGui.PAUSED);
    }
    
    protected void setStatusMonitor(int mode){
	
	switch(mode){
	    case 0://monitor run
		setMonitorStared(true);
		setMonitorStoped(false);
		setMonitorPaused(false);
		break;
	    case 1://stoped monitor
		setMonitorStared(false);
		setMonitorStoped(true);
		setMonitorPaused(false);
		break;
	    case 2://paused monitor
		setMonitorStared(false);
		setMonitorStoped(false);
		setMonitorPaused(true);
		break;	    
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

    protected void setFreeMem(long freeMem) {
	this.freeMem = freeMem;
    }

    public long getTotalMem() {
	return totalMem;
    }

    protected void setTotalMem(long totalMem) {
	this.totalMem = totalMem;
    }

    public long getMaxMem() {
	return maxMem;
    }

    protected void setMaxMem(long maxMem) {
	this.maxMem = maxMem;
    }

    public int getRefresh() {
	return refresh;
    }

    public void setRefresh(int refresh) {
	this.refresh = refresh;
    }

    public boolean isLoopThread() {
	return loopThread;
    }

    protected void setLoopThread(boolean repaintThread) {
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

    protected void setUnitName(String unitName) {
	this.unitName = unitName;
    }

    public boolean isShowText() {
	return showText;
    }

    public void setShowText(boolean showText) {
	this.showText = showText;
    }

    public boolean isShowShadow() {
	return showShadow;
    }

    public void setShowShadow(boolean showShadow) {
	this.showShadow = showShadow;
    }

    public boolean isMonitorStoped() {
	return monitorStoped;
    }

    protected void setMonitorStoped(boolean monitorStoped) {
	this.monitorStoped = monitorStoped;
    }

    public boolean isMonitorStared() {
	return monitorStared;
    }

    protected void setMonitorStared(boolean monitorStared) {
	this.monitorStared = monitorStared;
    }

    public boolean isMonitorPaused() {
	return monitorPaused;
    }

    protected void setMonitorPaused(boolean monitorpaused) {
	this.monitorPaused = monitorpaused;
    }

    protected class GridClass {

	private int width = 15;
	private int height = 5;	
	private Color color = new Color(240,240,240,150);
	private boolean showGrid = true;
	
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

	public boolean isShowGrid() {
	    return showGrid;
	}

	public void setShowGrid(boolean showGrid) {
	    this.showGrid = showGrid;
	    if(letOfChanged!=null)
		letOfChanged.currentStatusChanged("Monitor.grid show ="+showGrid);
	}
    }
    
    private class Chart {
	
	private int [] bitMap = new int [getWPaint()];	
	
	public Chart(){	    	    
	    resetBitmap();	    
	}
	
	public void resetBitmap(){
	    for (int i = 0; i < bitMap.length; i++)
		bitMap[i] = 0;    
	}
	
	public void updatePoint(int y){
	    bitMap[0]=y;
	    rollValuesInTable();
	    //System.out.println("update point "+y);
	}
	
	private void rollValuesInTable(){
	    for (int i = bitMap.length-2; i>=0; i--)		    
		    bitMap[i+1]=bitMap[i];		
	}
	
	public Shape transformToShape(){
	    GeneralPath genPath = new GeneralPath();
	    genPath.moveTo(0,0);//start point
	    for (int i = 0; i < bitMap.length; i++)
		genPath.lineTo(i,bitMap[i]);	    
	    genPath.lineTo(getWPaint(),0);//end point
	    genPath.closePath();
	    return genPath;
	}	
    }

    public void mouseClicked(MouseEvent e) {
	
	int button = e.getButton();
	if(button == MouseEvent.BUTTON1)
	    runtime.gc();
	else if(button == MouseEvent.BUTTON3){
	    MyPopupMenu popup = new MyPopupMenu();
	    	    
	    JCheckBoxMenuItem [] jcbmi = new JCheckBoxMenuItem[4];	    
	    jcbmi[0] = new JCheckBoxMenuItem("Show Grid",grid.isShowGrid());
	    jcbmi[0].addItemListener(new ItemListener() {
		public void itemStateChanged(ItemEvent e){		    
		    grid.setShowGrid(e.getStateChange() == ItemEvent.SELECTED);
		    repaint();
		}
	    });
	    jcbmi[1] = new JCheckBoxMenuItem("Show Text",isShowText());
	    jcbmi[1].addItemListener(new ItemListener() {
		public void itemStateChanged(ItemEvent e){		    
		    setShowText(e.getStateChange() == ItemEvent.SELECTED);
		    repaint();
		}
	    });
	    jcbmi[2] = new JCheckBoxMenuItem("Show Shadow",isShowShadow());
	    jcbmi[2].addItemListener(new ItemListener() {
		public void itemStateChanged(ItemEvent e){		    
		    setShowShadow(e.getStateChange() == ItemEvent.SELECTED);
		    repaint();
		}
	    });
	    JMenuItem item1 = new JMenuItem(new ActionMonitorProcess());
	    JMenuItem item2 = new JMenuItem(roundsValue(getMaxMem(),3));
	    
	    popup.add(item2);
	    popup.add(jcbmi[0]);
	    popup.add(jcbmi[1]);
	    popup.add(jcbmi[2]);
	    popup.addSeparator();
	    popup.add(item1);
	    popup.show(e.getComponent(),e.getX(),e.getY());
	}
	
    }
    @Override
    public void setVisible(boolean val){
	super.setVisible(val);
	if(!val)
	    stop();
	else
	    start();
	//if(letOfChanged!=null)
	//    letOfChanged.currentStatusChanged("MemoryMonitor.setvisible "+val);
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
  
    private class ActionMonitorProcess extends AbstractAction{
	
	public ActionMonitorProcess(){
	    
	    putValue(Action.NAME,check(false));
	}
	private String check(boolean action){
	    if(isMonitorStared())
	    {
		if(action)stop();
		return "Stop";
	    }else if(isMonitorStoped())
	    {
		if(action)start();
		return "Start";
	    }else if(isMonitorPaused())
	    {
		if(action)start();
		return "Start";
	    }
	    return "Unknown Action";
	}
	public void actionPerformed(ActionEvent e) {
	    putValue(Action.NAME,check(true));
	}
	
    }
}