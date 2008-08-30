/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.utils;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 *
 * @author vara
 */
public class Utils {

    public static Border createSimpleBorder(int top,int left,int bottom,int right,Color colFrame)
    {     
        return BorderFactory.createCompoundBorder(
			    BorderFactory.createEtchedBorder(BevelBorder.LOWERED,colFrame,Color.LIGHT_GRAY),
			    BorderFactory.createEmptyBorder(
				top,   // number of points from the top
				left,   // number of points along the left side
				bottom,    // number of points from the bottom
				right)  // number of points on the right side
				);
    }
}
