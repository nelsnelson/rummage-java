/*
 * DefaultModel.java
 *
 * Created on October 19, 2005, 10:36 AM
 */

package org.rummage.slide;

import java.util.Observable;
import java.util.Observer;

/**
 * A generic MVC model.
 *
 * @author not attributable
 */
public class DefaultModel
    extends Observable
    implements Model
{
    private boolean modified;
    
    public DefaultModel() {
        super();
    }
    
    public DefaultModel(Observer view) {
        super();
        
        addObserver(view);
    }
    
    public DefaultModel(Observer... views) {
        super();
        
        addObservers(views);
    }
    
    public void addObservers(final Observer... observers) {
        for (Observer o : observers) {
            addObserver(o);
        }
        
        notifyObservers();
    }
    
    public void setModified(boolean aFlag) {
        modified = aFlag;
    }
    
    public boolean isModified() {
        return modified;
    }
}
