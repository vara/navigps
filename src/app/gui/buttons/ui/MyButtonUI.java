package app.gui.buttons.ui;

import app.utils.MetalUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicButtonListener;
import javax.swing.plaf.metal.MetalButtonUI;
import sun.swing.SwingUtilities2;

/**
 * Created on 2009-01-13, 00:00:31
 * @author vara
 * Status (Testing)
 */
public class MyButtonUI extends MetalButtonUI{


    /**
     *
     * @param b
     */
    @Override
    public void installDefaults(AbstractButton b) {
        super.installDefaults(b);
    }

    /**
     *
     * @param b
     */
    @Override
    public void uninstallDefaults(AbstractButton b) {
	super.uninstallDefaults(b);
    }

    // ********************************
    //         Create Listeners
    // ********************************
    /**
     *
     * @param b
     * @return
     */
    @Override
    protected BasicButtonListener createButtonListener(AbstractButton b) {
        return new MyBasicButtonListener(b);
    }


    // ********************************
    //         Default Accessors
    // ********************************
    /**
     *
     * @return
     */
    @Override
    protected Color getSelectColor() {
        selectColor = UIManager.getColor(getPropertyPrefix() + "select");
	return selectColor;
    }

    /**
     *
     * @return
     */
    @Override
    protected Color getDisabledTextColor() {
        disabledTextColor = UIManager.getColor(getPropertyPrefix() +
                                               "disabledText");
	return disabledTextColor;
    }

    /**
     *
     * @return
     */
    @Override
    protected Color getFocusColor() {
        focusColor = UIManager.getColor(getPropertyPrefix() + "focus");
	return focusColor;
    }

    @Override
    public void update(Graphics g, JComponent c) {
        System.out.println("Update");
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        AbstractButton button = (AbstractButton)c;
        if ((c.getBackground() instanceof UIResource) &&
                  button.isContentAreaFilled() && c.isEnabled()) {

            ButtonModel model = button.getModel();
            if (!MetalUtils.isToolBarButton(c)) {
                if (!model.isArmed() && !model.isPressed() &&
                        MetalUtils.drawGradient(c, g2, "Button.gradient", 0, 0, c.getWidth(),
                                c.getHeight(), true)) {
                    paint(g2, c);
                    return;
                }
            }
            else if (model.isRollover() && MetalUtils.drawGradient(
                        c, g2, "Button.gradient", 0, 0, c.getWidth(),
                        c.getHeight(), true)) {
                System.out.println("Tool bar button");
                paint(g2, c);
                return;
            }
        }
        super.update(g, c);
    }

    /**
     *
     * @param g
     * @param b
     */
    @Override
    protected void paintButtonPressed(Graphics g, AbstractButton b) {
        System.out.println("paintButtonPressed");
        if ( b.isContentAreaFilled() ) {
            Dimension size = b.getSize();
	    g.setColor(getSelectColor());
	    g.fillRect(0, 0, size.width, size.height);
	}
    }

    /**
     *
     * @param g
     * @param b
     * @param viewRect
     * @param textRect
     * @param iconRect
     */
    @Override
    protected void paintFocus(Graphics g, AbstractButton b,
			      Rectangle viewRect, Rectangle textRect, Rectangle iconRect){
         System.out.println("paintFocus");
        Rectangle focusRect = new Rectangle();
	String text = b.getText();
	boolean isIcon = b.getIcon() != null;

        // If there is text
        if ( text != null && !text.equals( "" ) ) {
  	    if ( !isIcon ) {
	        focusRect.setBounds( textRect );
	    }
	    else {
	        focusRect.setBounds( iconRect.union( textRect ) );
	    }
        }
        // If there is an icon and no text
        else if ( isIcon ) {
  	    focusRect.setBounds( iconRect );
        }

        g.setColor(getFocusColor());
	g.drawRect((focusRect.x-1), (focusRect.y-1),
		  focusRect.width+1, focusRect.height+1);

    }


    @Override
    protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
        AbstractButton b = (AbstractButton) c;
        System.out.println("Paint Text");
        Graphics2D g2 = (Graphics2D)g;
        ButtonModel model = b.getModel();
        FontMetrics fm = SwingUtilities2.getFontMetrics(c, g);
            int mnemIndex = b.getDisplayedMnemonicIndex();

        /* Draw the Text */
        if(model.isEnabled()) {
            /*** paint the text normally */
            g2.setColor(b.getForeground());
        }
        else {
            /*** paint the text disabled ***/
            g2.setColor(getDisabledTextColor());
            }
            SwingUtilities2.drawStringUnderlineCharAt(c, g2,text,mnemIndex,
                                      textRect.x, textRect.y + fm.getAscent());
        }

    class MyBasicButtonListener extends BasicButtonListener{
        public MyBasicButtonListener(AbstractButton b) {
            super(b);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            System.out.println("Mouse Entered");
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            System.out.println("Mouse Relased");
        }

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            System.out.println("Mouse Exited");
        }
    }
}
