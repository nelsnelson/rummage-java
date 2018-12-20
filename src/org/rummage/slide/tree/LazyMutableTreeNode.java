/*
 * LazyMutableTreeNode.java
 *
 * Created on September 23, 2006, 3:38 PM
 */

package org.rummage.slide.tree;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author nnelson
 */
public abstract class LazyMutableTreeNode extends DefaultMutableTreeNode {
    
    private boolean loaded = false;
    
    public LazyMutableTreeNode() {
        super();
    }
    
    public LazyMutableTreeNode(Object userObject) {
        super(userObject);
    }
    
    public LazyMutableTreeNode(Object userObject, boolean allowsChildren) {
        super(userObject, allowsChildren);
    }
    
    public int getChildCount() {
        synchronized (this) {
            if (!loaded) {
                loaded = true;
                loadChildren();
            }
        }
        return super.getChildCount();
    }
    
    public void clear() {
        removeAllChildren();
        loaded = false;
    }
    
    public boolean isLoaded() {
        return loaded;
    }
    
    protected abstract void loadChildren();    
}
