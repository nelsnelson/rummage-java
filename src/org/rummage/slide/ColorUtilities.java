package org.rummage.slide;
/*
 * ColorUtilities.java
 *
 * Created on November 29, 2005, 2:18 PM
 */

import java.awt.Color;

// 	07/08/97	LAB	Removed checkValidPercent() function, and replaced
//					calls to it with calls to symantec.itools.util.GeneralUtils.checkValidPercent().
// 	08/05/97	LAB	Added lightness, calculateHilightColor, and calculateShadowColor.
//					Updated version to 1.1.  Removed GetColor, and corresponding
//					Hash table for copyright reasons.

// Written by Michael Hopkins, and Levi Brown, 1.0, June 27, 1997.

/**
 * Many useful color related utility functions for color manipulation.
 * <p>
 * @version 1.1, August 5, 1997
 * @author  Symantec
 */
public class ColorUtilities {
    /**
     * Do not use, this is an all-static class.
     */
    public ColorUtilities()
    {
    }

	/**
	 * Darkens a given color by the specified percentage.
	 * @param r The red component of the color to darken.
	 * @param g The green component of the color to darken.
	 * @param b The blue component of the color to darken.
	 * @param percent percentage to darken.  Needs to be <= 1 && >= 0.
	 * @return a new Color with the desired characteristics.
     * @exception IllegalArgumentException
     * if the specified percentage value is unacceptable
	 */
	public static Color darken( int r, int g, int b, double percent ) throws IllegalArgumentException
	{
		GeneralUtilities.checkValidPercent(percent);

		return new Color( Math.max((int)(r * (1-percent)), 0),
							Math.max((int)(g * (1-percent)),0),
							Math.max((int)(b * (1-percent)),0));
	}

	/**
	 * Darkens a given color by the specified percentage.
	 * @param to the Color to darken.
	 * @param percent percentage to darken.  Needs to be <= 1 && >= 0.
	 * @return a new Color with the desired characteristics.
     * @exception IllegalArgumentException
     * if the specified percentage value is unacceptable
	 */
	public static Color darken( Color c, double percent ) throws IllegalArgumentException
	{
		GeneralUtilities.checkValidPercent(percent);

		int r, g, b;
		r = c.getRed();
		g = c.getGreen();
		b = c.getBlue();
		return darken( r, g, b, percent );
	}

	/**
	 * Lightens a given color by the specified percentage.
	 * @param r The red component of the color to lighten.
	 * @param g The green component of the color to lighten.
	 * @param b The blue component of the color to lighten.
	 * @param percent percentage to lighten.  Needs to be <= 1 && >= 0.
	 * @return a new Color with the desired characteristics.
     * @exception IllegalArgumentException
     * if the specified percentage value is unacceptable
	 */
	public static Color lighten( int r, int g, int b, double percent ) throws IllegalArgumentException
	{
		GeneralUtilities.checkValidPercent(percent);

		int r2, g2, b2;
		r2 = r + (int)((255 - r) * percent );
		g2 = g + (int)((255 - g) * percent );
		b2 = b + (int)((255 - b) * percent );
		return new Color( r2, g2, b2 );
	}

	/**
	 * Lightens a given color by the specified percentage.
	 * @param to the Color to lighten.
	 * @param percent percentage to lighten.  Needs to be <= 1 && >= 0.
	 * @return a new Color with the desired characteristics.
     * @exception IllegalArgumentException
     * if the specified percentage value is unacceptable
	 */
	public static Color lighten( Color c, double percent ) throws IllegalArgumentException
	{
		GeneralUtilities.checkValidPercent(percent);

		int r, g, b;
		r = c.getRed();
		g = c.getGreen();
		b = c.getBlue();
		return lighten( r, g, b, percent );
	}

	/**
	 * Fades from one color to another by the given percentage.
	 * @param from the Color to fade from.
	 * @param to the Color to fade to.
	 * @param percent percentage to fade.  Needs to be <= 1 && >= 0.
	 * @return a new Color with the desired characteristics.
     * @exception IllegalArgumentException
     * if the specified percentage value is unacceptable
	 */
	public static Color fade( Color from, Color to, double percent ) throws IllegalArgumentException
	{
		GeneralUtilities.checkValidPercent(percent);

		int from_r, from_g, from_b;
		int to_r, to_g, to_b;
		int r, g, b;

		from_r = from.getRed();
		from_g = from.getGreen();
		from_b = from.getBlue();

		to_r = to.getRed();
		to_g = to.getGreen();
		to_b = to.getBlue();

		if (from_r > to_r)
			r = to_r + (int)((from_r - to_r)* (1 - percent));
		else
			r = to_r - (int)((to_r - from_r)* (1 - percent));
		if (from_g > to_r)
			g = to_g + (int)((from_g - to_g)* (1 - percent));
		else
			g = to_g - (int)((to_g - from_g)* (1 - percent));
		if (from_b > to_b)
			b = to_b + (int)((from_b - to_b)* (1 - percent));
		else
			b = to_b - (int)((to_b - from_b)* (1 - percent));

		return new Color(r, g, b);
	}

	/**
	 * Given a Color this function determines the lightness percent.
	 * @param c The Color to calculate from.  If null, it will return 0.
	 * @return the percent light of the specified color.  This value will be
	 * >= 0 && <= 1.
	 */
	public static double lightness(Color c)
	{
		if(c == null)
			return 0;

		double r, g, b, max, min;
		r = c.getRed();
		g = c.getGreen();
		b = c.getBlue();
		max = (Math.max(r, Math.max(g, b)) / 255) / 2;
		min = (Math.min(r, Math.min(g, b)) / 255) / 2;
		return (max + min);
	}

	/**
	 * Used to calculate a hilight color from a given color.
	 * @param c The color to use in the calculation.  If null, then
	 * it will return null.
	 * @return the newly calculated hilight color.
	 */
	public static Color calculateHilightColor(Color c)
	{
		if(c == null)
			return null;

		double lightness = lightness(c);

		if (lightness >= 0.90)
		{
			return(ColorUtilities.darken(c, 0.100));
		}
		else if (lightness <= 0.20)
		{
			return(ColorUtilities.lighten(c, 0.600));
		}
		else
		{
			return(ColorUtilities.lighten(c, 0.600));
		}
	}

	/**
	 * Used to calculate a shadow color from a given color.
	 * @param c The color to use in the calculation  If null, then
	 * it will return null.
	 * @return the newly calculated shadow color.
	 */
	public static Color calculateShadowColor(Color c)
	{
		if(c == null)
			return null;

		double lightness = lightness(c);

		if (lightness >= 0.90)
		{
			return(ColorUtilities.darken(c, 0.250));
		}
		else if (lightness <= 0.20)
		{
			return(ColorUtilities.lighten(c, 0.200));
		}
		else
		{
			return(ColorUtilities.darken(c, 0.250));
		}
	}

    public static Color getTranslucent(Color c, int alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }

    public static Color getTranslucent(Color c, double alphaChange) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), (int) (c.getAlpha() * alphaChange));
    }
    
    // Returns a Color based on 'colorName' which must be one of the predefined colors in
    // java.awt.Color. Returns null if colorName is not valid.
    public static Color getColor(String colorName) {
        try {
            // Find the field and value of colorName
            java.lang.reflect.Field field =
                Class.forName("java.awt.Color").getField(colorName.toUpperCase());
            
            return (Color)field.get(null);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static Color seeThrough() {
        return getTranslucent(Color.WHITE, 0);
    }
}

//???RKM??? I would have called this Util.java, but for the bug in the project manager

// 	06/11/97	LAB	Added frameTarget_* varaibles for convenience in classes
//					which need reference to these strings.
// 	07/08/97	LAB	Added checkValidPercent() function
//					Changed GeneralUtils to a "final" class so JIT can inline it.


// Written by Levi Brown and Rod Magnuson 1.1, July 8, 1997.

/**
 * Useful utility functions and constants.
 * @version 1.1, July 8, 1997
 * @author  Symantec
 */
class GeneralUtilities
{
    /**
     * Do not use, all-static class.
     */
    public GeneralUtilities() {
    }

    /**
     * A constant indicating a document should be shown in the current frame.
     * It is the second parameter for the method:
     * java.applet.AppletContext.showDocument(URL, String).
     * It is provided here for general use.
     * @see java.applet.AppletContext#showDocument(java.net.URL, java.lang.String)
     */
     public static String frameTarget_self = "_self";
    /**
     * A constant indicating a document should be shown in the parent frame.
     * It is the second parameter for the method:
     * java.applet.AppletContext.showDocument(URL, String).
     * It is provided here for general use.
     * @see java.applet.AppletContext#showDocument(java.net.URL, java.lang.String)
     */
     public static String frameTarget_parent = "_parent";
    /**
     * A constant indicating a document should be shown in the topmost frame.
     * It is the second parameter for the method:
     * java.applet.AppletContext.showDocument(URL, String).
     * It is provided here for general use.
     * @see java.applet.AppletContext#showDocument(java.net.URL, java.lang.String)
     */
     public static String frameTarget_top = "_top";
    /**
     * A constant indicating a document should be shown in a new unnamed
     * top-level window.
     * It is the second parameter for the method:
     * java.applet.AppletContext.showDocument(URL, String).
     * It is provided here for general use.
     * @see java.applet.AppletContext#showDocument(java.net.URL, java.lang.String)
     */
     public static String frameTarget_blank = "_blank";

	/**
	 * Compares two objects passed in for equality.
	 * Handle null objects.
	 * @param objectA one of the objects to be compared
	 * @param objectB one of the objects to be compared
	 */
	public static boolean objectsEqual(Object objectA,Object objectB)
	{
		if (objectA == null)
			return (objectB == null);

		return objectA.equals(objectB);
	}

	/**
	 * Checks to make sure the percent parameter is in range.
     * @exception IllegalArgumentException
     * if the specified percentage value is unacceptable
	 */
	public static void checkValidPercent(double percent) throws IllegalArgumentException
	{
		if(percent > 1 || percent < 0)
			throw new IllegalArgumentException(percent + " is not a valid percentage value. It should be <= 1 && >= 0");
	}

}