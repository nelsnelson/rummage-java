/*
 * State.java
 *
 * Created on January 23, 2006, 12:18 PM
 */
package org.rummage.slide;


/**
 *
 * @author nelsnelson
 */
public class State<L> {
    public static final int ERROR = -1;
    public static final int IDLE = 0;
    public static final int MESSAGE = 1;
    public static final int CHANGE = 2;
    public static final int REQUEST = 4;
    public static final int UPDATE = 8;
    public static final int FOCUS = 16;
    public static final int VISIBLE = 32;
    
    public static final int UPDATE_EDIT_POPUP = 128;
    public static final int SHOW_EDIT_POPUP = 256;
    
    public static final int BUSY = 1024;
    public static final int QUIT = 2048;
    
    public static final long NEVER = -1;
    public static final long NOW = 0;
    public static final long WHENEVER = 1;
    
    private int type = IDLE;
    private long when = NOW;
    private String description = null;
    private Object source = null;
    private boolean consumed = false;
    private int mouseX = -1;
    private int mouseY = -1;
    
    /**
     * Creates a new instance of State
     */
    public State(int type) {
        this.type = type;
    }
    
    /**
     * Creates a new instance of State
     */
    public State(int type, long when) {
        this.type = type;
        this.when = when;
    }
    
    /**
     * Creates a new instance of State
     */
    public State(Command command) {
        this.description = command.toString();
        this.type = CHANGE;
    }
    
    /**
     * Creates a new instance of State
     */
    public State(String description) {
        this.description = description;
        this.type = MESSAGE;
    }
    
    /**
     * Creates a new instance of State
     */
    public State(String description, int type) {
        this.description = description;
        this.type = type;
    }
    
    /**
     * Creates a new instance of State
     */
    public State(String description, long when) {
        this.description = description;
        this.when = when;
        this.type = MESSAGE;
    }
    
    /**
     * Creates a new instance of State
     */
    public State(int type, Object source) {
        this.type = type;
        this.source = source;
    }
    
    /**
     * Creates a new instance of State
     */
    public State(int type, Object source, int mouseX, int mouseY) {
        this.type = type;
        this.source = source;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }
    
    public boolean isImmediate() {
        return when == NOW;
    }
    
    public void consume() {
        consumed = true;
    }
    
    public boolean isConsumed() {
        return consumed;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public int getType() {
        return type;
    }
    
    public boolean ofType(int type) {
        return this.type == type;
    }
    
    public Object getSource() {
        return source;
    }
    
    public int getX() {
        return mouseX;
    }
    
    public int getY() {
        return mouseY;
    }
    
    public boolean equals(String s) {
        return toString().equals(s);
    }
    
    public String toString() {
        return description;
    }
}
