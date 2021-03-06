package app.navigps.gui.detailspanel;

import app.navigps.gui.label.ui.TitleLabelUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author wara
 */
public class DecoratePanel extends AbstractDecoratePanel{

    private JButton closeButton = new JButton("");
    private JLabel content = new JLabel();

    /**
     *
     */
    public DecoratePanel(){
        setOpaque(false);
        setPreferredSize(new Dimension(10,30));
        init();
        setOuterCorners(0,0);
        setInnerCorners(0, 0);
        setInsets(new Insets(0,0,0,0));
    }

    /**
     *
     * @param al
     */
    public void addActionListenerToCloseButton(ActionListener al){
        closeButton.addActionListener(al);
    }

    private void init(){
        setLayout(new BorderLayout());
        closeButton.setMargin(new Insets(1,1,1,1));
        closeButton.setFocusPainted(false);
        closeButton.setIconTextGap(0);
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        //closeButton.setRolloverIcon(RoundWindowUtils.getIcons("cancelOver.png", 30));
        closeButton.setPressedIcon(RoundWindowUtils.getIcons("cancelPressed.png", 24));
        closeButton.setIcon(RoundWindowUtils.getIcons("cancel.png", 24));
        add(closeButton,BorderLayout.EAST);
        getContent().setFont(new Font("Ariel", Font.BOLD, 16));

        getContent().setUI(new TitleLabelUI());
        //getContent().setIconTextGap(50);
        getContent().setForeground(Color.WHITE);
        //getContent().setBorder(LineBorder.createGrayLineBorder());
        //content.setIcon(MainWindowIWD.createNavigationIcon("open32"));
        add(getContent(),BorderLayout.CENTER);
    }

    /**
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);        
    }
    
    /**
     *
     * @return
     */
    @Override
    public String getTitle() {
        return getContent().getText();
    }

    /**
     *
     * @param text
     */
    @Override
    public void setTitle(String text) {
         getContent().setText(text);
    }

    /**
     *
     * @return
     */
    @Override
    public JLabel getContent() {
        return content;
    }

    /**
     *
     * @param content
     */
    @Override
    public void setContent(JLabel content) {
        this.content = content;
    }

    @Override
    public void setIcon(Icon ico) {
        getContent().setIcon(ico);
    }

    @Override
    public void setVisibleCloseButton(boolean val) {
        closeButton.setVisible(val);
    }

    @Override
    public boolean isVisibleCloseButton(boolean val) {
        return closeButton.isVisible();
    }
}
