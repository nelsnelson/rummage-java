/*
 * XMLTreeTableCellRenderer.java
 *
 * Created on October 27, 2005, 4:37 PM
 */

package org.rummage.slide.tree;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;

/**
 *
 * @author not attributable
 */
public class XMLTreeTableCellRenderer
    extends DefaultTreeCellRenderer
{
    Color elementColor = new Color(0, 0, 128);
    Color attributeColor = new Color(0, 128, 0);

    public XMLTreeTableCellRenderer() {
        setOpenIcon(null);
        setClosedIcon(null);
        setLeafIcon(null);
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean sel, boolean expanded, boolean leaf, int row,
        boolean hasFocus)
    {
        Node node = null;
        
        if (value instanceof DefaultMutableTreeNode) {
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
        
            Object o = treeNode.getUserObject();
            
            if (o instanceof String) {
                value = (String) o;
            }
            else if (o instanceof Node) {
                node = (Node) o;
            }
        }
        else {
            node = (Node) value;
        }
        
        if (node != null) {
            switch(node.getNodeType()){
                case Node.ELEMENT_NODE:
                    value = '<'+node.getNodeName()+'>';
                    break;
                case Node.ATTRIBUTE_NODE:
                    value = '@'+node.getNodeName();
                    break;
                case Node.TEXT_NODE:
                    value = "# text";
                    break;
                case Node.COMMENT_NODE:
                    value = "# comment";
                    break;
                case Node.DOCUMENT_TYPE_NODE:
                    DocumentType dtype = (DocumentType)node;
                    value = "# doctype";
                    break;
                default:
                    value = node.getNodeName();
            }
        }
        
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
            row, hasFocus);
        
        if(!selected && node != null) {
            switch(node.getNodeType()) {
                case Node.ELEMENT_NODE:
                    setForeground(elementColor);
                    break;
                case Node.ATTRIBUTE_NODE:
                    setForeground(attributeColor);
                    break;
            }
        }
        
        return this;
    }
}
