package app.gui.verboseTextPane;

/**
 * Created on 2009-01-13, 21:45:14
 * @author vara
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

public class LineNumber extends JComponent{

    public final static String CHANGE_VISIBLE_LINE_NUMBER = "linenumber.visibleline";

    private float limitColorFontAlpha = 0.8f;
    private float colorFontAlpha = getLimitColorFontAlpha();

	private final static Color DEFAULT_BACKGROUND = new Color(204, 204, 255);
	private final static Color DEFAULT_FOREGROUND = new Color(0,0,0);
	private final static Font  DEFAULT_FONT = new Font("monospaced", Font.PLAIN, 12);

	//  LineNumber height (abends when I use MAX_VALUE)
	private final static int DEFAULT_HEIGHT = Integer.MAX_VALUE - 1000000;
    private final static int DEFAULT_WIDTH = 99;

	//  Set right/left margin
	private final static int DEFAULT_MARGIN = 5;

	//  Variables for this LineNumber component
	private FontMetrics fontMetrics;
	private int lineHeight;
	private int currentDigits;

	//  Metrics of the component used in the constructor
	private JComponent component;
	private int componentFontHeight;
	private int componentFontAscent;

    private boolean showNumber = true;
    private ShowLineNumberAction showNum = new ShowLineNumberAction();

	public LineNumber(JComponent component){

		if (component == null){
			setFont(DEFAULT_FONT);
			this.component = this;
		}
		else{
			setFont(component.getFont());
			this.component = component;
		}
		setBackground(DEFAULT_BACKGROUND);
		setForeground(DEFAULT_FOREGROUND);
		setPreferredWidth(DEFAULT_WIDTH);

        addMouseListener(new MyMouseListener());
	}

	/**
	 *  Calculate the width needed to display the maximum line number
	 */
	public void setPreferredWidth(int lines)
	{
		int digits = String.valueOf(lines).length();        
		//  Update sizes when number of digits in the line number changes
		if (digits != currentDigits && digits > 1){
			currentDigits = digits;
			int width = fontMetrics.charWidth('0') * digits;
			Dimension d = getPreferredSize();
			d.setSize(2 * DEFAULT_MARGIN + width, DEFAULT_HEIGHT);
			setPreferredSize(d);
			setSize(d);
		}
	}

	/**
	 *  Reset variables that are dependent on the font.
	 */
    @Override
	public void setFont(Font font){
		super.setFont(font);
		fontMetrics = getFontMetrics(getFont());
		componentFontHeight = fontMetrics.getHeight();
		componentFontAscent = fontMetrics.getAscent();
	}

	/**
	 *  The line height defaults to the line height of the font for this
	 *  component.
	 */
	public int getLineHeight(){
		if (lineHeight == 0)
			return componentFontHeight;
		else
			return lineHeight;
	}

	/**
	 *  Override the default line height with a positive value.
	 *  For example, when you want line numbers for a JTable you could
	 *  use the JTable row height.
	 */
	public void setLineHeight(int lineHeight){
		if (lineHeight > 0)
			this.lineHeight = lineHeight;
	}

	public int getStartOffset(){
		return component.getInsets().top + componentFontAscent;
	}

    @Override
	public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;

		int localLineHeight = getLineHeight();
		int startOffset = getStartOffset();
		Rectangle drawHere = g.getClipBounds();

		// Paint the background

		g2.setColor( getBackground() );
		g2.fillRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);

		//  Determine the number of lines to draw in the foreground.
        System.out.println("paintComponent isShow number "+isShowNumber());
        if(isShowNumber()){

            g2.setColor(getForeground());
            AlphaComposite newComposite =
                  AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,getColorFontAlpha());

            g2.setComposite(newComposite);

            int startLineNumber = (drawHere.y / localLineHeight) + 1;
            int endLineNumber = startLineNumber + (drawHere.height / localLineHeight);

            int start = (drawHere.y / localLineHeight) * localLineHeight + startOffset;

            for (int i = startLineNumber; i <= endLineNumber; i++){
                String lineNumber = String.valueOf(i);
                int stringWidth = fontMetrics.stringWidth(lineNumber);
                int rowWidth = getSize().width;
                g2.drawString(lineNumber, rowWidth - stringWidth - DEFAULT_MARGIN, start);
                start += localLineHeight;
            }
            int rows = component.getSize().height / componentFontHeight;
            setPreferredWidth(rows);
            System.out.println(""+rows);
        }
		setPreferredWidth(1);
	}

    public void setColorFontAlpha(float alpha){
        colorFontAlpha = alpha<0.0f ? 0.0f : (alpha>1.0f ? 1.0f:getLimitColorFontAlpha());
        System.out.println("ColorAlpha "+colorFontAlpha);
    }

    /**
     * @return the showNumber
     */
    public boolean isShowNumber() {
        return showNumber;
    }

    /**
     * @param showNumber the showNumber to set
     */
    public void setShowNumber(boolean showNumber) {
        boolean oldV = this.showNumber;
        this.showNumber = showNumber;
        firePropertyChange(LineNumber.CHANGE_VISIBLE_LINE_NUMBER, oldV, showNumber);
    }

    /**
     * @return the limitColorFontAlpha
     */
    public float getLimitColorFontAlpha() {
        return limitColorFontAlpha;
    }

    /**
     * @param limitColorFontAlpha the limitColorFontAlpha to set
     */
    public void setLimitColorFontAlpha(float limitColorFontAlpha) {
        this.limitColorFontAlpha = limitColorFontAlpha;
    }

    /**
     * @return the colorFontAlpha
     */
    public float getColorFontAlpha() {
        return colorFontAlpha;
    }

    class ShowLineNumberAction extends AbstractAction{

        public void actionPerformed(ActionEvent e) {
            
            final float startAlpha = isShowNumber() ? 1.0f : 0.0f;
            final float andAlpha = !isShowNumber() ? 1.0f : 0.0f;
            final float incUnit = isShowNumber() ? -0.01f : 0.01f;

            new Thread(new Runnable() {
                public void run() {

                    boolean disableShowNumber = false;
                    if(!isShowNumber())
                        setShowNumber(true);
                    else disableShowNumber = true;

                    for (float i = startAlpha; i < andAlpha+incUnit; i+=incUnit) {
                        setColorFontAlpha(i);
                        LineNumber.this.repaint();
                        try {Thread.sleep(50);} catch (InterruptedException e) {}
                    }
                    setShowNumber(!disableShowNumber);
                }
            });

            setShowNumber(!isShowNumber());
            repaint();
        }

    }

    class MyMouseListener implements MouseListener{

        public void mouseClicked(MouseEvent e) {

        }

        public void mousePressed(MouseEvent e) {
            showNum.actionPerformed(null);
        }

        public void mouseReleased(MouseEvent e) {

        }

        public void mouseEntered(MouseEvent e) {

        }

        public void mouseExited(MouseEvent e) {

        }

    }

    public static void main(String[] args){
		JFrame frame = new JFrame("LineNumberDemo");
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		JPanel panel = new JPanel();
		frame.setContentPane( panel );
		panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		panel.setLayout(new BorderLayout());

		JTextPane textPane = new JTextPane();
		textPane.setFont( new Font("monospaced", Font.PLAIN, 12) );
		textPane.setText("abc");

		JScrollPane scrollPane = new JScrollPane(textPane);
		panel.add(scrollPane);
		scrollPane.setPreferredSize(new Dimension(300, 250));

		LineNumber lineNumber = new LineNumber( textPane );
		scrollPane.setRowHeaderView( lineNumber );

        frame.pack();
		frame.setVisible(true);
      
	}
}