/*
 * Text.java
 *
 * Created on October 19, 2005, 10:08 AM
 */

package org.rummage.toolbox.util;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author nelsnelson
 */
public abstract class Text {
    public static final int TITLE_CASE = 1;
    
    public static final String DEFAULT_EXPLODER_DELIMITER = " ";
    public static final String DEFAULT_JOINER_DELIMITER = "\n";
    
    protected static String exploderDelimiter = DEFAULT_EXPLODER_DELIMITER;
    protected static String joinerDelimiter = DEFAULT_JOINER_DELIMITER;
    
    public enum Forgiveness { FORGIVING, UNFORGIVING };
    
    public static <T> java.util.Vector<T> vectorize(T[] a) {
        Vector<T> v = new Vector();
        v.addAll(a);

        return v;
    }
    
    public static java.util.Vector<?> vectorize(Enumeration e) {
        java.util.Vector v = new java.util.Vector(listify(e));
        
        return v;
    }

    public static <T> Vector<T> vectorize(Vector<T> v, T... a) {
        return vectorize(false, v, a);
    }

    public static <T> Vector<T> vectorize(boolean reverse, Vector<T> v, T... a) {
        Vector<T> v0 = new Vector();
        
        if (reverse) {
            v0.addAll(a);
            v0.addAll(v);
        }
        else {
            v0.addAll(v);
            v0.addAll(a);
        }
        
        return v0;
    }
    
    public static <T> List<T> listify(T... a) {
        List<T> l = new ArrayList(Arrays.asList(a));
        
        return l;
    }
    
    public static <T> List<T> listify(Enumeration<T> e) {
        List<T> l = new ArrayList(Collections.list(e));
        
        return l;
    }
    
    public static int[] intarrayify(String[] a) {
        int[] prims = new int[a == null ? 0 : a.length];
        
        if (a != null) {
        int i = 0;
        
        for (String s : a) {
            try {
                prims[i++] = Integer.parseInt(s);
            }
            catch (NumberFormatException ex) {
                prims[i++] = 0;
            }
        }
        }
        
        return prims;
    }
    
    public static <S, T> Map<S, T> mapify(List<S> keys, List<T> values) {
        return mapify(keys, values, Forgiveness.UNFORGIVING);
    }
    
    public static <S, T> Map<S, T> mapify(List<S> keys, List<T> values,
        Forgiveness forgiveness)
    {
        return
            mapify(keys, values, forgiveness.equals(Forgiveness.FORGIVING));
    }
    
    public static <S, T> Map<S, T> mapify(List<S> keys, List<T> values,
        boolean forgiving)
    {
        Map<S, T> m = new java.util.LinkedHashMap();
        
        if (keys != null && values != null) {
        if (!forgiving && keys.size() != values.size()) {
            return m;
        }
        
        java.util.Iterator j = values.iterator();
        
        for (S key : keys) {
            T value = j.hasNext() ? (T) j.next() : null;
            
            if (value == null) {
                if (forgiving) continue;
                else break;
            }
            
            m.put(key, value);
        }
        }
        
        return m;
    }
    
    public static <S, T> Map<S, T> mapify(Set<Map.Entry<S, T>> set) {
        Map m = new TreeMap();
        
        if (set != null) {
        for (Map.Entry<S, T> e : set) {
            m.put(e.getKey(), e.getValue());
        }
        }
        
        return m;
    }
    
    public static String[] append(String[] head, String[] tail) {
        List l = Arrays.asList(head);
        l.addAll(Arrays.asList(tail));
        return (String[]) l.toArray(new String[l.size()]);
    }
    
    public static void setExploderDelimiter(String delimiter) {
        exploderDelimiter = delimiter;
    }
    
    public static String[] explode(String str) {
        return explode(str, exploderDelimiter);
    }
    
    public static String[] explode(String str, char delimiter) {
        return explode(str, String.valueOf(delimiter));
    }
    
    public static String[] explode(String str, String delimiter) {
        StringTokenizer tokens =
            new StringTokenizer(str == null ? "" : str, delimiter);
        
        String[] exploded = new String[tokens.countTokens()];
        
        for (int i = 0; i < exploded.length && tokens.hasMoreTokens(); i++) {
            String token = tokens.nextToken();
            
            exploded[i] = token;
        }
        
        return exploded;
    }
    
    public static String[] explode2(String str, char delimiter) {
        return explode2(str, String.valueOf(delimiter));
    }
    
    public static String[] explode2(String str, String delimiter) {
        String[] exploded = new String[frequency(str, delimiter)];
        
        int i = 0;
        
        while (i < exploded.length && str.contains(delimiter)) {
            int index = str.indexOf(delimiter);
            exploded[i++] = str.substring(0, index);
            str = str.substring(index + delimiter.length());
        }

        return exploded;
    }
    
    public static List<String> explode3(String str) {
        return explode3(str, DEFAULT_EXPLODER_DELIMITER);
    }
    
    public static List<String> explode3(String str, char delimiter) {
        return explode3(str, String.valueOf(delimiter));
    }
    
    public static List<String> explode3(String str, String delimiter) {
        List<String> exploded = new ArrayList<String>();
        
        do {
            exploded.add(before(str, delimiter));
            str = after(str, delimiter);
        }
        while (str.contains(delimiter));
        
        return exploded;
    }
    
    public static List<String> explode3(String str, int index) {
        List<String> exploded = new ArrayList<String>();
        
        while (index < str.length()) {
            exploded.add(str.substring(0, index));
            str = str.substring(index + 1);
        }
        
        exploded.add(str); // add the remaining String
        
        return exploded;
    }
    
    // TODO Remove
    public static Vector<String> explode4(String str, String delimiter) {
        List<String> exploded = new ArrayList<String>();
        
        while (str.contains(delimiter)) {
            int index = str.indexOf(delimiter);
            exploded.add(str.substring(0, index));
            str = str.substring(index + delimiter.length());
        }
        
        return new Vector<String>(exploded);
    }
    
    public static Vector<String> explode5(String str, String delimiter) {
        return new Vector<String>(str.split(delimiter));
    }
    
    public static String[] explode(String str, int index) {
        String[] exploded = new String[Math.round(str.length() / index)];
        
        int i = 0;
        
        while (i < exploded.length && index < str.length()) {
            exploded[i++] = str.substring(0, index);
            str = str.substring(index);
        }
        
        if (str.length() > 0 && i < exploded.length) {
            exploded[i] = str;
        }
        
        return exploded;
    }
    
    public static String join(List<String> l) {
        return join(l, joinerDelimiter);
    }
    
    public static String join(List<String> l, String delimiter) {
        String joined = "";
        
        if (l != null) {
        for (String s : l) {
            joined = joined + s + delimiter;
        }
        }
        
        return joined;
    }
    
    public static String join2(List<String> l) {
        return join(l, joinerDelimiter);
    }
    
    public static String join2(List<String> l, String delimiter) {
        String joined = "";
        
        Enumeration<String> enumerator = Collections.enumeration(l);
        
        if (enumerator != null) {
            while(enumerator.hasMoreElements()) {
                joined = joined + enumerator.nextElement() + delimiter;
            }
        }
        
        return joined;
    }
    
    public static String strip(String str, String e) {
        return str == null ? "" : str.replace(e, "");
    }
    
    public static String truncate(String s, int i) {
        return shear(s, i);
    }
    
    public static String shear(String s, int i) {
        return s.substring(0, s.length() - i);
    }
    
    public static String before(String s, String delimiter) {
        return before(s, s.contains(delimiter) ? s.indexOf(delimiter) : s.length());
    }
    
    public static String before(String s, int m) {
        return s.substring(0, m);
    }
    
    public static String after(String s, String delimiter) {
        return after(s, s.contains(delimiter) ? s.indexOf(delimiter) + delimiter.length() : 0);
    }
    
    public static String after(String s, int m) {
        return s.substring(m);
    }
    
    //redo
    public static int find(String str, String pattern) {
        String[] nonMatchingChunks = str.split(pattern);
        
        String s =
            nonMatchingChunks == null || nonMatchingChunks.length == 0 ? 
                "" : nonMatchingChunks[0];
        
        return str.indexOf(s) + s.length();
    }
    
    public static String extractFirstMatch(String pattern, String str) {
        Matcher m = Pattern.compile(pattern).matcher(str);
        
        return m == null || !m.matches() ? "" : m.group();
    }
    
    public static int find2(List<String> l, String pattern) {
        for (int i = 0; i < l.size(); i++) {
            String s = l.get(i);
            
            if (!s.matches(pattern)) {
                return i;
            }
        }
        
        return -1;
    }
    
    public static List<String> stripAll(List<String> l, String e) {
        for (String s : l) {
            l.set(l.indexOf(s), strip(s, e));
        }
        
        return l;
    }
    
    public static String padLeft(int n, String s) {
        for (int i = s.length(); i < n; i++) {
            s = " " + s;
        }
        
        return s;
    }
    
    public static String padRight(String s, int n) {
        for (int i = s.length(); i < n; i++) {
            s = s + " ";
        }
        
        return s;
    }
    
    public static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        else {
            return s.substring(0,1).toUpperCase() + s.substring(1);
        }
    }
    
    public static boolean startsWithIgnoreCase(String s, String beginning) {
        return s.toLowerCase().startsWith(beginning.toLowerCase());
    }
    
    public static boolean equalsAnyOfThese(String s, List<String> l) {
        return equalsAnyOfThese(s, (String[]) l.toArray());
    }
    
    public static boolean equalsAnyOfThese(String s, String... a) {
        for (String t : a) {
            if (s.equals(t)) {
                return true;
            }
        }
        
        return false;
    }
    
    public static boolean endsWithAnyOfThese(String s, List<String> l) {
        return endsWithAnyOfThese(s, (String[]) l.toArray());
    }
    
    public static boolean endsWithAnyOfThese(String s, String... a) {
        for (String t : a) {
            if (s.endsWith(t)) {
                return true;
            }
        }
        
        return false;
    }

    public static String getBestMatch(String[] pool, List<String> terms) {
        return getBestMatch(Arrays.asList(pool), terms);
    }
    
    public static String getBestMatch(List<String> pool, List<String> terms) {
        List<String> matches = new ArrayList<String>();
        
        for (String term : terms) {
            if (subContains(pool, term)) {
                matches.add(term);
            }
        }
        
        return getPrevalent(matches);
    }
    
    public static boolean subContains(List<String> l, String key) {
        for (String element : l) {
            if (element.contains(key)) {
                return true;
            }
        }
        
        return false;
    }
    
    public static String getPrevalent(List<String> l) {
        String prevalent = null;
        int max = 0;
        
        for (String s : uniquify(l)) {
            int n = frequency(l, s);
            
            if (n > max) {
                max = n;
                prevalent = s;
            }
        }
        
        return prevalent;
    }
    
    public static List<String> uniquify(List<String> l) {
        ArrayList<String> uniquification = new ArrayList<String>();
        
        for (String s : l) {
            if (!uniquification.contains(s)) {
                uniquification.add(s);
            }
        }
        
        return uniquification;
    }
    
    public static int frequency(String str, String delimiter) {
        int i = 0;
        
        while (str != null) {
            str = str.substring(str.indexOf(delimiter) + delimiter.length());
            i++;
        }
        
        return i;
    }
    
    public static int frequency(Collection c, Object o) {
        return Collections.frequency(c, o);
    }
    
    public static void printAll(Collection c) {
        printAll(c.toArray());
    }
    
    public static void printAll(Enumeration<?> e) {
        printAll(listify(e).toArray());
    }
    
    public static void printAll(Map m) {
        java.util.Iterator i = m.entrySet().iterator();
        while (i.hasNext()) {
            Map.Entry entry = (Map.Entry) i.next();
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
    
    public static <T> void printAll(T... items) {
        printAll(System.out, items);
    }
    
    public static <T> void printAll(PrintStream out, T... items) {
        if (items == null) {
            return;
        }
        
        out.print("[ ");
        
        for (Object item : items) {
            out.print(item + ", ");
        }
        
        out.println(" ]");
    }
    
    public static boolean evaluationEquals(Object o1, Object o2) {
        return String.valueOf(o1).equals(String.valueOf(o2));
    }

    public static Set filterSet(Set set, String s) {
        Set filteredSet = new TreeSet();
        
        for (Object o : set) {
            if (o != null && !o.toString().equals(s)) {
                filteredSet.add(o);
            }
        }
        
        return filteredSet;
    }

    public static Vector<String> series(int min, int max) {
        Vector series = new Vector();
        
        for (int i = min; i <= max; i++) {
            series.add(String.valueOf(i));
        }
        
        return series;
    }

    public static Map getStats(String s) {
        Map m = new LinkedHashMap();
        
        char[] a = s.toCharArray();
        
        for (int i = 0, n = a.length; i < n; i++) {
            
            Character c = new Character(a[i]);
            
            Integer j = (Integer) m.get(c);
            
            if (j == null) {
                j = new Integer(1);
            }
            
            m.put(c, new Integer(j.intValue() + 1));
        }
        
        return m;
    }
    
    public static class Vector<T extends Object>
        extends java.util.Vector
    {
        public Vector(Object... items) {
            super();
            super.addAll(Arrays.asList(items));
        }
        
        public Vector(String... items) {
            super();
            super.addAll(Arrays.asList(items));
        }
        
        public void addAll(Object... items) {
            super.addAll(Arrays.asList(items));
        }
        
        public void addAll(String... items) {
            super.addAll(Arrays.asList(items));
        }
    }
    
    public static class DataRecord
        extends java.util.LinkedHashMap
    {
        public Object put(Object key, Object... values) {
            return super.put(key, listify(values));
        }
        
        public Object getKey(int i) {
            return keySet().toArray()[i];
        }
        
        public Object get(Object key, int i) {
            return ((List) super.get(key)).get(i);
        }
        
        public Object get(int i, int j) {
            return ((List) super.get(getKey(i))).get(j);
        }
        
        public Object put(Object key, int j, Object value) {
            return ((List) super.get(key)).set(j, value);
        }
        
        public Object put(int i, int j, Object value) {
            return ((List) super.get(getKey(i))).set(j, value);
        }
    }
}
