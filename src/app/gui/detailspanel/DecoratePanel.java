package app.gui.detailspanel;

import app.gui.MainWindowIWD;
import app.gui.label.ui.TitleLabelUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

/**
 *
 * @author wara
 */
public class DecoratePanel extends AbstractDecoratePanel{

    private JButton closeButton = new JButton("X");
    private JLabel content = new JLabel();

    public DecoratePanel(){
        setOpaque(false);
        setPreferredSize(new Dimension(10,30));
        init();
    }

    public void addActionListenerToCloseButton(ActionListener al){
        closeButton.addActionListener(al);
    }

    private void init(){
        setLayout(new BorderLayout());
        //add(closeButton);        
        getContent().setFont(new Font("Ariel", Font.BOLD, 16));

        getContent().setUI(new TitleLabelUI());
        //getContent().setIconTextGap(50);
        getContent().setForeground(Color.WHITE);
        //getContent().setBorder(LineBorder.createGrayLineBorder());
        //content.setIcon(MainWindowIWD.createNavigationIcon("open32"));
        add(getContent(),BorderLayout.CENTER);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);        
    }   

    
    @Override
    public String getTitle() {
        return getContent().getText();
    }

    @Override
    public void setTitle(String text) {
         getContent().setText(text);
    }

    @Override
    public JLabel getContent() {
        return content;
    }

    @Override
    public void setContent(JLabel content) {
        this.content = content;
    }
}
