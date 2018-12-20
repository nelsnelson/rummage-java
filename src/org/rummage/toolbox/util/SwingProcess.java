/*
 * SwingProcess.java
 *
 * Created on January 20, 2006, 3:01 PM
 */
package org.rummage.toolbox.util;


/**
 *
 * @author not attributable
 */
public abstract class SwingProcess
    extends SwingWorker
{
    private Object[] args = null;
    
    /**
     * Creates a new instance of SwingProcess
     */
    public SwingProcess(Object... args) {
        this.args = args;
        
        start();
    }
    
    protected abstract void process() throws Exception;

    public Object construct() throws Exception {
        process();
        
        return null;
    }
}
