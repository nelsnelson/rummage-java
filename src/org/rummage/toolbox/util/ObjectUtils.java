package org.rummage.toolbox.util;

import org.rummage.toolbox.reflect.Mirror;

public class ObjectUtils {
    private ObjectUtils() {
        
    }
    
    public static Object maybeInvokeMethod(Object target, String methodName,
    		Object... parameters)
    {
    	return Mirror.doMethod(target, methodName, parameters);
    }
}
