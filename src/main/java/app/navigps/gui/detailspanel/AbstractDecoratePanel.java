package app.navigps.gui.detailspanel;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 *
 * @author Grzegorz (vara) Warywoda
 */
public abstract class AbstractDecoratePanel extends RoundJPanel{

    /**
     *
     * @param text
     */
    abstract public void setTitle(String text);
    /**
     *
     * @return
     */
    abstract public String getTitle();
    /**
     *
     * @return
     */
    abstract public JLabel getContent();
    /**
     *
     * @param lab
     */
    abstract public void setContent(JLabel lab);

    abstract public void setIcon(Icon ico);

    abstract public void setVisibleCloseButton(boolean val);
    abstract public boolean isVisibleCloseButton(boolean val);
}
