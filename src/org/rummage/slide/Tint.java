/*
 * Tint.java
 *
 * Created on November 29, 2005, 2:18 PM
 */

package org.rummage.slide;

import java.awt.Color;

/**
 *
 * @author nelsnelson
 */
public class Tint
{
    private static final double FACTOR = 0.95d;
    
    public static Color shade(Color c) {
        return shade(c, FACTOR);
    }
    
    public static Color shade(Color c, double shade) {
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();
        
        shade = (shade % 1); // always get darker [ 0%, 100% ]
        
	return new Color(Math.min(Math.max((int)(r * shade), 0), 255), 
			 Math.min(Math.max((int)(g * shade), 0), 255),
			 Math.min(Math.max((int)(b * shade), 0), 255));
    }
    
    public static Color light(Color c) {
        return light(c, FACTOR);
    }
    
    public static Color light(Color c, double light) {
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();
        
        /* Collelary to Color.brighter():
         * 1. light(black) should return grey
         * 2. applying light to blue will always return blue, brighter
         * 3. non pure color (non zero rgb) will eventually return white
         */
        int i = (int)(1.0/(1.0-(light % 1)));
        if ( r == 0 && g == 0 && b == 0) {
           return new Color(i, i, i);
        }
        if ( r > 0 && r < i ) r = i;
        if ( g > 0 && g < i ) g = i;
        if ( b > 0 && b < i ) b = i;
        
        light = (light % 1) + 1; // always get lighter [ 100%, 200% ]

        return new Color(Math.max(Math.min((int)(r * light), 255), 0),
                         Math.max(Math.min((int)(g * light), 255), 0),
                         Math.max(Math.min((int)(b * light), 255), 0));
    }
}
