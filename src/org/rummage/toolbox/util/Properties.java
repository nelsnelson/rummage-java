/*
 * Properties.java
 *
 * Created on October 19, 2005, 3:11 PM
 */

package org.rummage.toolbox.util;

import java.awt.AWTException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import org.rummage.slide.SlideUtilities;
import org.rummage.toolbox.net.InetAddressParsingException;
import org.rummage.toolbox.net.NetUtils;

/**
 * Just a wrapper to look like the real thing so that extension is
 * transparent and all methods can still be called in a static context.
 *
 * @author nelsnelson
 */
public class Properties {
    private static final String DEFAULT_LIST_DELIMITER = ",";
    
    protected static java.util.Properties properties0 = null;
    protected static Properties properties = null;
    
    public static final String OS_NAME = "os.name";
    public static final String WINDOWS_XP = "Windows XP";
    public static final String WINDOWS_2000 = "Windows 2000";
    public static final String MACOS_X = "MacOS X";
    
    public static final int CUSTOM_ONLY = 0;
    public static final int INCLUDE_SYSTEM = 1;

    public static final String MAIN_CLASS = "Application.mainClass";
    public static final String PROPERTIES_FILE = "Properties.filePath";
    public static final String WINDOW_EXTENDED_STATE = "window.extendedState";
    public static final String WINDOW_X = "Window.X";
    public static final String WINDOW_Y = "Window.Y";
    public static final String WINDOW_WIDTH = "Window.width";
    public static final String WINDOW_HEIGHT = "Window.height";
    public static final String WINDOW_TITLE = "Window.title";
    public static final String TOOL_BAR_VISIBILITY = "ToolBar.isVisible";
    public static final String STATUS_BAR_VISIBILITY = "StatusBar.isVisible";
    public static final String SCROLL_INCREMENT = "ScrollBar.scrollIncrement";
    public static final String SUPPRESS_ERRORS = "Dialog.suppressErrors";
    public static final String SUPPRESS_WARNINGS = "Dialog.suppressWarnings";
    
    public static final String RECENT_DIRECTORY =
        "FileChooser.recentDirectory";
    
    public static String ABOUT_MESSAGE = "About.message";
    
    // constants specific to the actual properties file
    protected String applicationName = null;
    protected static final String defaultApplicationName = "Application";
    protected static final String propertiesDirectory =
        System.getProperty("user.home");

    protected int includeMode = CUSTOM_ONLY;
    
    protected static final String propertiesFileExtension = ".properties";
    
    /**
     * hashtable, filesystem dir prefix, filename, and properties for custom cursors support
     */
    
    private static final Hashtable  systemCustomCursors         = new Hashtable(1);
    private static final String systemCustomCursorDirPrefix = initCursorDir();
    
    private File propertiesFile = null;

    private static String initCursorDir() {
        String jhome =	(String) java.security.AccessController.doPrivileged(
                new org.rummage.gnu.java.security.action.GetPropertyAction("java.home"));
        return jhome +
                File.separator + "lib" + File.separator + "images" +
                File.separator + "cursors" + File.separator;
    }

    public static final String systemCustomCursorPropertiesFile = (
            systemCustomCursorDirPrefix + "cursors.properties");
    
    private static Properties systemCustomCursorProperties = null;
    
    /** Creates a new instance of Properties */
    public Properties() {
        this((Properties) null, null);
    }
    
    /** Creates a new instance of Properties */
    public Properties(String applicationName) {
        this((Properties) null, CUSTOM_ONLY, applicationName);
    }
    
    /** Creates a new instance of Properties */
    public Properties(Properties props) {
        this(props.getProperties0(), CUSTOM_ONLY, null);
    }
    
    /** Creates a new instance of Properties */
    public Properties(Properties props, String applicationName) {
        this(props.getProperties0(), CUSTOM_ONLY, applicationName);
    }
    
    /** Creates a new instance of Properties */
    public Properties(Properties props, int includeMode) {
        this(props.getProperties0(), includeMode, null);
    }
    
    /** Creates a new instance of Properties */
    public Properties(Properties props, int includeMode,
        String applicationName)
    {
        this(props.getProperties0(), includeMode, applicationName);
    }
    
    /** Creates a new instance of Properties */
    public Properties(java.util.Properties props, int includeMode,
        String applicationName)
    {
        if (props == null) {
            properties0 = new java.util.Properties(properties0);
        }
        else {
            properties0 = new java.util.Properties(props);
        }
        
        this.includeMode = includeMode;
        this.applicationName = applicationName;
        
        importProperties();
        
        this.properties = this;
        
        setProperty(MAIN_CLASS, ApplicationManager.getMainClass());
    }
    
    public void importProperties() {
        if (includeMode == INCLUDE_SYSTEM) {
            setProperties(System.getProperties());
        }
        
        propertiesFile =
            new File(propertiesDirectory, getApplicationName() +
            propertiesFileExtension);
        
        try {
            FileInputStream in = new FileInputStream(propertiesFile);
            
            getProperties0().loadFromXML(in);
            
            in.close();
        }
        catch (FileNotFoundException ex) {
            initDefaults0();
        
            exportProperties();
        }
        catch (InvalidPropertiesFormatException ex) {
            org.rummage.slide.Controller.getController().error(ex);
        }
        catch (IOException ex) {
            org.rummage.slide.Controller.getController().error(ex);
        }
    }
    
    public static void exportProperties() {
        try {
            FileOutputStream out =
                new FileOutputStream(Properties.getFile(PROPERTIES_FILE));
            
            getProperties0().storeToXML(out, null);
            
            out.flush();
            out.close();
        }
        catch (FileNotFoundException ex) {
            org.rummage.slide.Controller.getController().error(ex);
        }
        catch (IOException ex) {
            org.rummage.slide.Controller.getController().error(ex);
        }
    }
    
    public void setIncludeMode(int includeMode) {
        this.includeMode = includeMode;
    }
    
    private final void initDefaults0() {
        setProperty(PROPERTIES_FILE, propertiesFile);
        setProperty(WINDOW_EXTENDED_STATE, "0");
        setProperty(WINDOW_X, "50");
        setProperty(WINDOW_Y, "50");
        setProperty(WINDOW_WIDTH, "650");
        setProperty(WINDOW_HEIGHT, "450");
        setProperty(WINDOW_TITLE, getApplicationName());
        setProperty(TOOL_BAR_VISIBILITY, true);
        setProperty(STATUS_BAR_VISIBILITY, true);
        setProperty(SCROLL_INCREMENT, "10");
        setProperty(SUPPRESS_ERRORS, false);
        setProperty(SUPPRESS_WARNINGS, false);
        setProperty(RECENT_DIRECTORY, System.getProperty("user.home"));
        
        initDefaults();
    }
    
    protected void initDefaults() {
        // stub
    }
    
    public static void putAll(List keys, List values) {
        putAll(Text.mapify(keys, values, Text.Forgiveness.FORGIVING));
    }
    
    public static void putAll(Object[] keys, Object[] values) {
        putAll(Text.listify(keys), Text.listify(values));
    }
    
    public static void putAll(List keys, Object[] values) {
        putAll(keys, Text.listify(values));
    }
    
    public static void putAll(Object[] keys, List values) {
        putAll(Text.listify(keys), values);
    }
    
    public static void putAll(Map m) {
        getProperties0().putAll(m);
    }
    
    public static void setProperties(java.util.Properties p) {
        getProperties0().putAll(Text.mapify(p.entrySet()));
    }
    
    /** Wrapper extension method. */
    public static void setProperty(String key, String value) {
        getProperties0().setProperty(key, value);
    }
    
    private static void setProperty(String key, Class value) {
        setProperty(key, value.getName());
    }
    
    public static void setProperty(String key, InetAddress value) {
        setProperty(key, value.getHostAddress());
    }
    
    public static void setProperty(String key, List value) {
        setProperty(key, Text.join(value, ","));
    }

    public static void setProperty(String key, URL value) {
        setProperty(key, value.toString());
    }
    
    public static void setProperty(String key, File value) {
        setProperty(key, value.getAbsolutePath());
    }

    public static void setProperty(String key, Calendar value) {
        setProperty(key, value.getTime().toString());
    }
    
    public static void setProperty(String key, Date value) {
        setProperty(key, value.getTime());
    }
    
    public static void setProperty(String key, float value) {
        setProperty(key, String.valueOf(value));
    }
    
    public static void setProperty(String key, double value) {
        setProperty(key, String.valueOf(value));
    }
    
    public static void setProperty(String key, long value) {
        setProperty(key, String.valueOf(value));
    }
    
    public static void setProperty(String key, int value) {
        setProperty(key, String.valueOf(value));
    }

    public static void setProperty(String key, byte value) {
        setProperty(key, String.valueOf(value));
    }

    public static void setProperty(String key, boolean value) {
        setProperty(key, String.valueOf(value));
    }
    
    public static int getInt(String key) {
        try {
            return Integer.parseInt(getProperty(key));
        }
        catch (NumberFormatException ex) {
            return 0;
        }
    }
    
    public static double getDouble(String key) {
        try {
            return Double.parseDouble(getProperty(key));
        }
        catch (NumberFormatException ex) {
            return 0.0d;
        }
    }
    
    public static long getLong(String key) {
        try {
            return Long.parseLong(getProperty(key));
        }
        catch (NumberFormatException ex) {
            return 0l; // 0 long
        }
    }
    
    public static boolean getBoolean(String key) {
        return Boolean.valueOf(getProperty(key));
    }
    
    public static boolean getFlag(String key) {
        return getBoolean(key);
    }
    
    public static String getParameter(String key) {
        return getProperty(key);
    }
    
    public static ImageIcon getImage(String key) {
        java.net.URL resource =
            SlideUtilities.getResource(getApplicationName0(), getProperty(key));
        
        return resource == null ? null : new ImageIcon(resource);
    }
    
    public static Date getDate(String key) {
        return new Date(getLong(key));
    }
    
    public static long getTime(String key) {
        return new Date(getLong(key)).getTime();
    }

    public static URL getURL(String key) {
        URL url = null;
        
        try {
            url = new URL(getProperty(key));
        }
        catch (NullPointerException ex) {
            //ex.printStackTrace();
        }
        catch (MalformedURLException ex) {
            //ex.printStackTrace();
        }
        
        return url;
    }
    
    public static File getFile(String key) {
        String property = getProperty(key);
        
        return property == null ? null : new java.io.File(property);
    }
    
    public static String[] getArray(String key) {
        return Text.explode(getProperty(key), DEFAULT_LIST_DELIMITER);
    }
    
    public static List<String> getList(String key) {
        String property = getProperty(key);
        
        return Text.listify(Text.explode(property, DEFAULT_LIST_DELIMITER));
    }
    
    public static InetAddress getInetAddress(String key) {
        InetAddress address = null;
        
        try {
            address = NetUtils.parseInternetProtocolAddress(getProperty(key));
        }
        catch (NullPointerException ex) {
            //ex.printStackTrace();
        }
        catch (InetAddressParsingException ex) {
            //ex.printStackTrace();
        }
        
        return address;
    }
    
    public static Class<?> getClass(String key) {
        Class<?> clazz = null;
        
        try {
            clazz = Class.forName(getProperty(key));
        }
        catch (ClassNotFoundException ex) {
            //ex.printStackTrace();
        }
        
        return clazz;
    }
    
    public static Properties getProperties() {
        return properties;
    }
    
    public static java.util.Properties getProperties0() {
        if (properties0 == null) {
            properties0 = new java.util.Properties();
        }
        
        return properties0;
    }
    
    /** Wrapper extension method. */
    public static String getProperty(String key) {
        return getProperties0().getProperty(key);
    }
    
    public static String stringify() {
        return
            properties == null ?
                "No properties have been initialized." :
                properties.toString();
    }

    /*
     * print the properties
     */
    public static void printProperties() {
        getProperties0().list(System.out);
    }

    /*
     * print the system properties
     */
    public static void printSystemProperties() {
        System.getProperties().list(System.out);
    }

    /*
     * print the cursor.properties file
     */
    public static void printSystemCustomCursorProperties() throws AWTException {
        try {
            AccessController.doPrivileged(new SpecialExceptionAction<Object>());
        } catch (Exception e) {
            throw new AWTException("Exception: " + e.getClass() + " " +
                    e.getMessage() + " occurred while loading: " +
                    systemCustomCursorPropertiesFile);
        } finally {
            Text.listify(System.out);
        }
    }
    
    protected static String getApplicationName0() {
        return getProperties().getApplicationName();
    }
    
    public String getApplicationName() {
        return
            applicationName == null ? defaultApplicationName : applicationName;
    }
    
    public static Class<?> getMainClass() {
        return getClass(MAIN_CLASS);
    }
    
    public static boolean isWindowsXP() {
        return System.getProperty(OS_NAME).equals(WINDOWS_XP);
    }
    
    public static boolean isWindows2000() {
        return System.getProperty(OS_NAME).equals(WINDOWS_2000);
    }

    public static boolean isMacOSX() {
        return System.getProperty(OS_NAME).equals(MACOS_X);
    }
}

class SpecialExceptionAction<T>
    implements PrivilegedExceptionAction<T>
{
    public T run() throws Exception {
        java.util.Properties systemCustomCursorProperties = new java.util.Properties();

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(
                    Properties.systemCustomCursorPropertiesFile);
            systemCustomCursorProperties.load(fis);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return null;
    }
}
