/*
 * BallonDispalyManager.java
 *
 * Created on 2009-04-07, 22:53:26
 */

package app.navigps.gui.svgComponents.DisplayObjects;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.LinkedList;
import javax.swing.Timer;
import javax.xml.crypto.Data;
import net.java.balloontip.BalloonTip;

/**
 *
 * @author Grzegorz (wara) Warywoda
 */
public final class BallonDispalyManager {

    public static final String ONLY_ONE_BALLON = "only.one";
    public static final String MANY_BALLONS = "many.one";

    private String mode = ONLY_ONE_BALLON;

    private LinkedList <ToolTipInvoker> queue = new LinkedList <ToolTipInvoker>();

    private static BallonDispalyManager instance = new BallonDispalyManager();

    public synchronized static void showBallon(BalloonTip ballon,int initialDelay, int showDelay){
    
        instance.addToQueue(new ToolTipInvoker(ballon, initialDelay, showDelay));
    }

    private void addToQueue(ToolTipInvoker ballon){
        
        if(!queue.contains(ballon)){
            queue.push(ballon);
            addedBallonToQueue();
        }       
    }

    private void addedBallonToQueue(){
        if(mode.equals(ONLY_ONE_BALLON)){
            queue.getFirst().start();
            if(queue.size()>1)
                queue.getLast().dipatchToolTip();
            
        }else if(mode.equals(MANY_BALLONS)){
            queue.pop().start();
        }
       // System.out.println("**ADDED ** QUEUE SIZE: "+queue.size()+" in mode: "+getMode());
    }

    private void removeBallonFromQueue(ToolTipInvoker invoker){
        boolean isRemoved = queue.remove(invoker);
        //System.out.println("is removed "+invoker.hashCode()+" : "+isRemoved);
        //System.out.println("**REMOVED ** QUEUE SIZE: "+queue.size());
    }

    public int getQueueSize(){
        return queue.size();
    }

    public static String getDisplayMode(){
        return instance.getMode();
    }

    public static void setDisplayMode(String aMode){
        instance.setMode(aMode);
    }

    /**
     * @return the mode
     */    
    public String getMode() {
        return mode;
    }

    /**
     * @param aMode the mode to set
     */    
    public void setMode(String aMode) {
        mode = aMode;
    }

    private static class ToolTipInvoker{

        private final BalloonTip balloonTip;
		private Timer initialTimer;
		private Timer showTimer;

        private final int hash;
		/**
		 * Constructor
		 * @param balloonTip
		 * @param initialDelay	in milliseconds, how long should you hover over the attached component before showing the tooltip
		 * @param showDelay		in milliseconds, how long should the tooltip stay visible
		 */
		public ToolTipInvoker(final BalloonTip balloonTip, int initialDelay, int showDelay) {
			super();
			this.balloonTip = balloonTip;
			initialTimer = new Timer(initialDelay, new ActionListener() {
                @Override
				public void actionPerformed(ActionEvent e) {
                    //System.out.println("\n\t"+new Date()+" [start] Is visible "+balloonTip.isVisible());
					balloonTip.setVisible(true);
					showTimer.start();
				}
			});
			initialTimer.setRepeats(false);

			showTimer = new Timer(showDelay, new ActionListener() {
                @Override
				public void actionPerformed(ActionEvent e) {
                    //System.out.println("\n\t"+new Date()+" [stop] Is visible "+balloonTip.isVisible());
					balloonTip.setVisible(false);
                    removeFromQueue();
				}
			});
			showTimer.setRepeats(false);

            hash = createHashCode();
		}

        public void dipatchToolTip(){
            if(initialTimer.isRunning()){
                initialTimer.stop();                
            }
            if(showTimer.isRunning()){
                showTimer.stop();
            }
            if(balloonTip.isShowing()){
                boolean animator = balloonTip.isAnimatorEnabled();
                if(animator){
                    balloonTip.setAnimatorEnabled(!animator);
                    balloonTip.setVisible(false);
                    balloonTip.setAnimatorEnabled(animator);
                }else{
                    balloonTip.setVisible(false);
                }
            }
            removeFromQueue();
            
        }
        private void removeFromQueue(){
            instance.removeBallonFromQueue(ToolTipInvoker.this);
        }
        public void start(){           
            //System.out.println("Start showing tooltip ;}");
            initialTimer.start();
            
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof ToolTipInvoker){
                return this.hash == obj.hashCode();
            }
            return false;
        }

        private int createHashCode(){
            return (this.balloonTip != null ? this.balloonTip.hashCode() : 0);
        }

        @Override
        public int hashCode() {            
            return hash;
        }
    }
}
