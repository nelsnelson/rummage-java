/*
 * Mirror.java
 *
 * Created on November 2, 2005, 9:43 AM
 */

package org.rummage.toolbox.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.rummage.toolbox.util.Text;

/**
 * These are some reflection utility methods.  These streamline the
 * process of getting handles on methods which cannot be reliably known to
 * exist by any given particular name before compilation, let alone before
 * runtime.
 *
 * @author nelsnelson
 */
public class Mirror {
    public static int DO_NOT_GUESS = 0;
    public static int BEST_GUESS = 1;
    
    /** Creates a new instance of Mirror */
    public Mirror() {
        
    }
    
    /**
     * This sucker returns the main class from the stack, if it exists.
     *
     * This method is not very reliable, since it assumes that the invoker is
     * not executing from within a dispatchEvent(AWTEvent event) invocation
     * stack.  To be certain, this method should be invoked at the beginning
     * of a program, and stored somewhere.  That way, if the main class is
     * ever needed, it can be retrieved, instead of divining it on the fly.
     */
    public static Class divineMainClass() {
        return divineMainClass(BEST_GUESS);
    }
    
    /**
     * This sucker returns the main class from the stack, if it exists.
     */
    public static Class divineMainClass(int mainClassDiscoveryMode) {
        boolean failurePredicted =
            javax.swing.SwingUtilities.isEventDispatchThread();
        
        Vector<StackTraceElement> v =
            Text.vectorize(Thread.currentThread().getStackTrace());
        
        String s = v.lastElement().toString();
        
        Class clazz = Object.class;
        
        Pattern pattern =
            Pattern.compile("(.*)\\.main\\(.*\\.java\\:\\d*\\)");
        
        Matcher matcher = pattern.matcher(s);
        
        if (matcher.find()) {
            try {
                clazz = Class.forName(matcher.group(1));
            }
            catch (ClassNotFoundException ex) {
                //ex.printStackTrace();
            }
        }
        
        if (clazz == null && failurePredicted) {
            // To do: do something useful here.
            //
            // JWarning here for testing only.  Remove it soon.  -- 2006/08/17
            new org.rummage.slide.JWarning("Mirror Testing...\n\nFailure prediction was correct.");
        }
        
        if (clazz == null && mainClassDiscoveryMode == BEST_GUESS) {
            // Okay, the first attempt at guessing the main class was
            // unsuccessful.  This means that this method is probably being
            // executed from within a dispatchEvent(AWTEvent event) invocation
            // stack.
            //
            // First, get the class of the invoker of this method.
            Class mostRecentInvokerClass = getInvoker();
            
            // assume that this is the main class
            return mostRecentInvokerClass;
        }
        else {
            return clazz;
        }
    }
    
    /**
     * This sucker returns true if and only if the stack contains some kinda
     * main method.
     *
     * This method is reliable, and less of a fortune-telling than the
     * divineMainClass() method.
     */
    public static boolean isExecutingWithinMainStack() {
        return divineMainClass() != null;
    }
    
    /**
     * This sucker returns true if and only if given object is actually the
     * main class itself.
     *
     * This method is also reliable, and less of a fortune-telling than the
     * divineMainClass() method.
     */
    public static boolean isMainClass(Object o) {
        Class mainClass = Mirror.divineMainClass();
        
        return mainClass == null ? false : o.getClass().equals(mainClass);
    }
    
    /**
     * This method returns the Class of the actor or object that invoked the
     * method from which this method was called.
     */
    public static Method getInvokee() {
        Vector<StackTraceElement> v =
            Text.vectorize(new Exception().getStackTrace());
        
        String s = "";
        
        // Get rid of any stack trace element entry that has anything at all
        // to do with this here contraption -- the Mirror class.
        do {
            s = v.remove(0).toString();
        }
        while (s.contains(Mirror.class.getName()));
        
        // ...and one more time for good measure.  Seriously though, one more
        // time to get the actual invoking method of the method than called
        // this one, getInvokee.
        s = v.remove(0).toString();
        
        Class clazz = Object.class;
        
        Pattern pattern =
            Pattern.compile("(.*)\\.[^\\(]*\\(.*\\.java\\:\\d*\\)");
        
        Matcher matcher = pattern.matcher(s);
        
        if (matcher.find()) {
            try {
                clazz = Class.forName(matcher.group(1));
            }
            catch (ClassNotFoundException ex) {
                //ex.printStackTrace();
            }
        }
        
        Method method = null;
        
        pattern = Pattern.compile(".*\\.([^\\.\\(]*)\\(.*\\.java\\:\\d*\\)");
        
        matcher = pattern.matcher(s);
        
        if (matcher.find()) {
            method = getMethodByName(clazz, matcher.group(1));
        }
        
        return method;
    }
    
    /**
     * This method returns the Class of the actor or object that invoked the
     * method from which this method was called.
     */
    public static Class getInvoker() {
        Vector<StackTraceElement> v =
            Text.vectorize(new Exception().getStackTrace());
        
        String s = "";
        
        do {
            s = v.remove(0).toString();
        }
        while (s.contains(Mirror.class.getName()));
        
        Class clazz = Object.class;
        
        Pattern pattern =
            Pattern.compile("(.*)\\.[^\\(]*\\(.*\\.java\\:\\d*\\)");
        
        Matcher matcher = pattern.matcher(s);
        
        if (matcher.find()) {
            try {
                clazz = Class.forName(matcher.group(1));
            }
            catch (ClassNotFoundException ex) {
                //ex.printStackTrace();
            }
        }
        
        return clazz;
    }
    
    public static boolean hasMethodByName(Object target, String methodName) {
        return getMethodsNames(target).contains(methodName);
    }
    
    public static ArrayList getMethodsNames(Object target) {
        Method[] methods = target.getClass().getMethods();
        
        ArrayList methodsNames = new ArrayList();
        
        for (Method method : methods) {
            methodsNames.add(method.getName());
        }
        
        return methodsNames;
    }
    
    public static ArrayList getMethodsInformation(Object target) {
        Method[] methods = target.getClass().getMethods();
        
        ArrayList methodsInformation = new ArrayList();
        
        for (Method method : methods) {
            methodsInformation.add(method.toString());
        }
        
        return methodsInformation;
    }
    
    public static Method getMethodByName(Object target, String methodName) {
        Method method = null;
        
        Method[] methods = target.getClass().getDeclaredMethods();
        
        for (int i = 0, n = methods.length; i < n; i++) {
            method = methods[i];
            
            if (method.getName().equalsIgnoreCase(methodName)) {
                return method;
            }
        }
        
        return method;
    }
    
    public static Method getMethodByName(Class clazz, String methodName) {
        Method method = null;
        
        Method[] methods = clazz.getDeclaredMethods();
        
        for (int i = 0, n = methods.length; i < n; i++) {
            method = methods[i];
            
            if (method.getName().equalsIgnoreCase(methodName)) {
                return method;
            }
        }
        
        return method;
    }
    
    public static Class getPrimaryExpectedParameterType(Object target,
        Method method)
    {
        return getExpectedParameterType(target, method, 0);
    }
    
    public static Class getPrimaryExpectedParameterType(Object target,
        String methodName)
    {
        Method method = getMethodByName(target, methodName);
        
        return getPrimaryExpectedParameterType(target, method);
    }
    
    public static Class getExpectedParameterType(Object target, Method method,
        int i)
    {
        Class primaryExpectedParameterType = null;
        
        Class[] parameterTypes = method.getParameterTypes();
        
        if (parameterTypes != null && parameterTypes.length > 0) {
            primaryExpectedParameterType = parameterTypes[i];
        }
        
        return primaryExpectedParameterType;
    }
    
    public static Class getPrimaryInterface(Object target) {
        Class primaryInterface = null;
        
        Class targetClass = target.getClass();
        
        Class[] interfaces = targetClass.getInterfaces();
        
        if (interfaces != null && interfaces.length > 0) {
            primaryInterface = interfaces[0];
        }
        
        return primaryInterface;
    }
    
    public static Class[] getTypes(Object... objects) {
        Class[] classes = null;
        
        if (objects != null) {
            classes = new Class[objects.length];
        
            for (int i = 0, n = objects.length; i < n; i++) {
                classes[i] = objects[i].getClass();
            }
        }
        
        return classes;
    }
    
    public static Object doMethod(Object target, String methodName)
    {
        return doMethod(target, methodName, (Object[]) null);
    }
    
    public static Object doMethod(Object target, String methodName,
        Object... parameters)
    {
        Object object = null;
        
        try {
            Method method =
                target.getClass().getMethod(methodName,
                getTypes(parameters));

            object = method.invoke(target, parameters);
        }
        catch (InvocationTargetException ex) {
            //ex.printStackTrace();
        }
        catch (IllegalAccessException ex) {
            //ex.printStackTrace();
        }
        catch (NoSuchMethodException ex) {
            //ex.printStackTrace();
        }
        
        return object;
    }
    
    public static Object doMethod(Object target, String methodName,
        Object parameter)
    {
        return doMethod(target, methodName, parameter, Mirror.DO_NOT_GUESS);
    }
    
    public static Object doMethod(Object target, String methodName,
        Object parameter, int parameterClassDiscoveryMode)
    {
        Object object = null;
        
        try {
            if (parameterClassDiscoveryMode == Mirror.BEST_GUESS) {
                Class type =
                    getPrimaryExpectedParameterType(target,
                    getMethodByName(target, methodName));
                
                if (parameter != null && !parameter.getClass().equals(type)) {
                    parameter = getNewInstance(type, parameter);
                }
            }
            
            if (parameter != null) {
	            Method method =
	                target.getClass().getMethod(methodName,
	                parameter.getClass());
	            
	            object = method.invoke(target, parameter);
            }
        }
        catch (InvocationTargetException ex) {
            //ex.printStackTrace();
        }
        catch (IllegalAccessException ex) {
            //ex.printStackTrace();
        }
        catch (NoSuchMethodException ex) {
            //ex.printStackTrace();
        }
        
        return object;
    }
    
    public static Object doMethod(Object target, String methodName,
        Object parameter, Class parameterClass)
    {
        Object object = null;
        
        try {
            Method method =
                target.getClass().getMethod(methodName, parameterClass);

            object = method.invoke(target, parameter);
        }
        catch (InvocationTargetException ex) {
            //ex.printStackTrace();
        }
        catch (IllegalAccessException ex) {
            //ex.printStackTrace();
        }
        catch (NoSuchMethodException ex) {
            //ex.printStackTrace();
        }
        
        return object;
    }
    
    public static Object maybeInvokeMethod(String target, String methodName,
            Object... parameters)
    {
        return doStaticMethod(target, methodName, parameters);
    }
    
    public static Object doStaticMethod(Class target, String methodName, 
        Object parameter)
    {
        Object object = null;
        
        try {
            Method method =
                target.getMethod(methodName, parameter.getClass());

            object = method.invoke(null, parameter);
        }
        catch (InvocationTargetException ex) {
            //ex.printStackTrace();
        }
        catch (IllegalAccessException ex) {
            //ex.printStackTrace();
        }
        catch (NoSuchMethodException ex) {
            //ex.printStackTrace();
        }
        
        return object;
    }
    
    public static Object doStaticMethod(String target, String methodName, 
        Object... parameters)
    {
		Object object = null;
		
        try {
	    	object = 
	    	    doStaticMethod(Class.forName(target), methodName, parameters);
        }
        catch (ClassNotFoundException ex) {
            //ex.printStackTrace();
        }
        
        return object;
    }
    
    public static Object doStaticMethod(Class target, String methodName, 
        Object... parameters)
    {
		Object object = null;
		
		try {
			Method method = 
				target.getMethod(methodName, getTypes(parameters));
			
			method.invoke(null, parameters);
	    }
	    catch (InvocationTargetException ex) {
            //ex.printStackTrace();
	    }
	    catch (IllegalAccessException ex) {
            //ex.printStackTrace();
	    }
	    catch (NoSuchMethodException ex) {
            //ex.printStackTrace();
	    }
	    catch (NullPointerException ex) {
            //ex.printStackTrace();
	    }
		
		return object;
    }
    
    public static Object getNewInstance(Class type) {
        Object instance = null;
        
        try {
            instance = type.newInstance();
        }
        catch (InstantiationException ex) {
            System.out.println(ex);
        }
        catch (IllegalAccessException ex) {
            System.out.println(ex);
        }
        
        return instance;
    }
    
    public static Object getNewInstance(Class type, Object... arguments) {
        Object instance = null;
        
        try {
            Constructor constructor = type.getConstructor(getTypes(arguments));
            instance = constructor.newInstance(arguments);
        }
        catch (NoSuchMethodException ex) {
            //System.out.println(ex);
        }
        catch (InvocationTargetException ex) {
            //System.out.println(ex);
        }
        catch (InstantiationException ex) {
            //System.out.println(ex);
        }
        catch (IllegalAccessException ex) {
            //System.out.println(ex);
        }
        
        return instance;
    }
    
    public static ArrayList doToAll(ArrayList targets, String methodName) {
        ArrayList resultsList = new ArrayList();
        
        Iterator i = targets.iterator();
        
        while (i.hasNext()) {
            resultsList.add(doMethod(i.next(), methodName));
        }
        
        return resultsList;
    }
    
    public static ArrayList doToAll(Class staticTarget,
        String staticMethodName, ArrayList parametersList)
    {
        ArrayList resultsList = new ArrayList();
        
        Iterator i = parametersList.iterator();
        
        while (i.hasNext()) {
            Object parameter = i.next();
            
            resultsList.add(doStaticMethod(staticTarget, staticMethodName,
                parameter));
        }
        
        return resultsList;
    }

    public static StackTraceElement glance() {
        return Thread.currentThread().getStackTrace()[1];
    }

    public static Object glance(Object o) {
        return Thread.currentThread().getStackTrace()[1];
    }
}

