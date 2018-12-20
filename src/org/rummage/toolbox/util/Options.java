package org.rummage.toolbox.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Options {
    private static final Map<String, String> options = 
        new LinkedHashMap<String, String>();
    
    public Options(String[] args) {
        Iterator<String> i = Arrays.asList(args).iterator();
        
        while (i.hasNext()) {
            String s = i.next();
            
            if (s.startsWith("--")) {
                s = s.substring(2); // get rid of "--"

                String key = s;
                String value = s;
                
                if (s.contains("=")) {
                    String[] pair = s.split("=");
                    
                    key = pair[0];
                    value = pair[1];
                }
                
                options.put(key, value);
            }
            else if (s.contains("-")) {
                if (s.length() == 2) {
                    String value = i.next();
                    
                    if (value == null) {
                        throw new IllegalArgumentException();
                    }
                    
                    options.put(s, value);
                }
                else {
                    throw new IllegalArgumentException();
                }
            }
            else {
                options.put(s, s);
            }
        }
    }
    
    public static boolean contains(String name) {
        return getOptions().containsKey(name);
    }
    
    private static String get(String name) {
        return options.get(name);
    }
    
    private static Map getOptions() {
        return options;
    }
}
