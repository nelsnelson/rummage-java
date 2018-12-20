/*
 * Command.java
 *
 * Created on January 31, 2006, 2:32 PM
 */
package org.rummage.slide;


/**
 *
 * @author not attributable
 */
public class Command {
    private String command = null;
    private Object source = null;
    
    /** Creates a new instance of Command */
    public <T> Command(String command, T source) {
        if (command == null) {
            throw new IllegalArgumentException("Argument \"command\" cannot" +
                "be null.");
        }
        
        this.command = command;
        this.source = source;
    }
    
    public <T> T getSource() {
        return (T) source;
    }
    
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        
        return command.equals(o.toString());
    }
    
    public String toString() {
        return command;
    }
}
