package org.rummage.toolbox.util;

public class Pair<T>
    extends java.util.ArrayList {

    public Pair(T item1, T item2) {
        super(java.util.Arrays.asList(new Object[] {item1, item2}));
    }
    
    public Pair(T... items) {
        this(items[0], items[1]);
    }
    
    public T first() {
        return (T) super.get(0);
    }
    
    public T second() {
        return (T) super.get(1);
    }
}
