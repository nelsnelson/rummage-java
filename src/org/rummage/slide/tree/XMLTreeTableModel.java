/*
 * XMLTreeTableImpl.java
 *
 * Created on October 27, 2005, 4:15 PM
 */

package org.rummage.slide.tree;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author nelsnelson
 */
public class XMLTreeTableModel
    extends DefaultTreeTableModel
{
    private TreeNode root;
    
    public XMLTreeTableModel(Document document) {
        this(document.getDocumentElement());
    }
    
    public XMLTreeTableModel(Node root) {
        super();
        this.root = createTreeNode(root);
    }
    
    public Object getRoot() {
        return root;
    }
    
    /**
     * This takes a DOM Node and recurses through the children until each one is added
     * to a DefaultMutableTreeNode. The JTree then uses this object as a tree model.
     *
     * @param root org.w3c.Node.Node
     *
     * @return Returns a DefaultMutableTreeNode object based on the root Node passed in
     */
    private DefaultMutableTreeNode createTreeNode(Node root) {
        if (root == null) {
            return null;
        }
        
        XMLMutableTreeNode  treeNode = new XMLMutableTreeNode(root);
        
        // Get data from root node
        String type = getNodeType(root);
        String name = root.getNodeName();
        String value = root.getNodeValue();
        
        // Recurse children nodes if any exist
        if (root.hasChildNodes()) {
            NodeList children = root.getChildNodes();
            // Only recurse if Child Nodes are non-null
            if (children != null) {
                int numChildren = children.getLength();
                
                for (int i=0; i < numChildren; i++) {
                    Node node = children.item(i);
                    
                    if (node != null) {
                        // A special case could be made for each Node type.
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            treeNode.add(createTreeNode(node));
                        }
                    }
                }
            }
        }
        
        return treeNode;
    }
    
    /**
     * This method returns a string representing the type of node passed in.
     *
     * @param node org.w3c.Node.Node
     *
     * @return Returns a String representing the node type
     */
    private String getNodeType(Node node) {
        String type;
        
        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:
            {
                type = "Element";
                break;
            }
            case Node.ATTRIBUTE_NODE:
            {
                type = "Attribute";
                break;
            }
            case Node.TEXT_NODE:
            {
                type = "Text";
                break;
            }
            case Node.CDATA_SECTION_NODE:
            {
                
                type = "CData section";
                break;
            }
            case Node.ENTITY_REFERENCE_NODE:
            {
                type = "Entity reference";
                break;
            }
            case Node.ENTITY_NODE:
            {
                type = "Entity";
                break;
            }
            case Node.PROCESSING_INSTRUCTION_NODE:
            {
                type = "Processing instruction";
                break;
            }
            case Node.COMMENT_NODE:
            {
                type = "Comment";
                break;
            }
            case Node.DOCUMENT_NODE:
            {
                type = "Document";
                break;
            }
            case Node.DOCUMENT_TYPE_NODE:
            {
                type = "Document type";
                break;
            }
            case Node.DOCUMENT_FRAGMENT_NODE:
            {
                type = "Document fragment";
                break;
            }
            case Node.NOTATION_NODE:
            {
                type = "Notation";
                break;
            }
            default:
            {
                type = "???";
                break;
            }
        }
        
        return type;
    }
    
    public Object getChild(Object parent, int index) {
        Node node = getNode(parent);
        NamedNodeMap attrs = node.getAttributes();
        int attrCount = attrs != null ? attrs.getLength() : 0;
        
        if (index < attrCount) {
            return attrs.item(index);
        }
        
        NodeList children = node.getChildNodes();
        
        return children.item(index - attrCount);
    }
    
    public int getChildCount(Object parent) {
        if (parent == null) {
            return 0;
        }
        
        Node node = getNode(parent);
        
        NamedNodeMap attrs = node.getAttributes();
        int attrCount = attrs != null ? attrs.getLength() : 0;
        NodeList children = node.getChildNodes();
        int childCount = children != null ? children.getLength() : 0;
        
        if (childCount == 1 &&
            children.item(0).getNodeType() == Node.TEXT_NODE)
        {
            return attrCount;
        }
        else {
            return attrCount + childCount;
        }
    }
    
    public boolean isLeaf(Object node) {
        return getChildCount(node) == 0;
    }
    
    public int getIndexOfChild(Object parent, Object child) {
        Node node = getNode(parent);
        Node childNode = getNode(child);
        
        NamedNodeMap attrs = node.getAttributes();
        int attrCount = attrs != null ? attrs.getLength() : 0;
        
        if (childNode.getNodeType() == Node.ATTRIBUTE_NODE) {
            for (int i = 0; i < attrCount; i++) {
                if (attrs.item(i) == child) {
                    return i;
                }
            }
        }
        else {
            NodeList children = node.getChildNodes();
            int childCount = children != null ? children.getLength() : 0;
            
            for (int i = 0; i < childCount; i++){
                if (children.item(i) == child) {
                    return attrCount + i;
                }
            }
        }
        
        throw new RuntimeException("This should never happen!");
    }
    
    public void addTreeModelListener(TreeModelListener listener) {
        // not editable
    }
    
    public void removeTreeModelListener(TreeModelListener listener) {
        // not editable
    }
    
    public void valueForPathChanged(TreePath path, Object newValue) {
        // not editable
    }
    
    public Object getValueAt(Object node, int column) {
        if (column == 0) {
            return node;
        }
        
        return ((XMLMutableTreeNode) node).getUserObject();
        
        /*
        Node n = getNode(node);
        
        if (n.getNodeType() == Node.ELEMENT_NODE) {
            NodeList children = n.getChildNodes();
            int childCount = children != null ? children.getLength() : 0;
            
            if (childCount == 1 &&
                children.item(0).getNodeType() == Node.TEXT_NODE)
            {
                return children.item(0).getNodeValue();
            }
        }
        
        return n.getNodeValue();*/
    }
    
    public void clear() {
        root = null;
    }
    
    public boolean isCellEditable(Object node, int column) {
        return false;
    }
    
    public void setValueAt(Object aValue, Object node, int column) {
        Node n = getNode(node);
        String val = (aValue == null ? "" : aValue.toString());
        if (column == 0) {
            n.setNodeValue(val);
        }
        else {
            String columnName = getColumnName(column);
            NamedNodeMap attributes = n.getAttributes();
            Node attribute = attributes.getNamedItem(columnName);
            attribute.setTextContent(val);
        }
    }
    
    public void insertNode(Object node, Object newNode) {
        Node parent = getNode(node);
        
        if (parent == null) {
            return;
        }
        
        Node n = (Node) newNode;
        
        if (n == null) {
            return;
        }
        
        if (n.getNodeType() == Node.ATTRIBUTE_NODE) {
            String attributeName = n.getNodeName();
            NamedNodeMap attributes = parent.getAttributes();
            Node attribute = attributes.getNamedItem(attributeName);
            attribute.setNodeValue(n.getNodeValue());
        }
        else if (n.getNodeType() == Node.ELEMENT_NODE) {
            parent.appendChild(n);
        }
    }
    
    public void removeNode(Object node) {
        Node n = getNode(node);
        
        if (n == null) {
            return;
        }
        
        Node parent = n.getParentNode();
        
        if (parent == null) {
            return;
        }
        
        parent.removeChild(n);
    }

    private Node getNode(Object o) {
        if (o == null) {
            return null;
        }
        
        if (o instanceof DefaultMutableTreeNode) {
            DefaultMutableTreeNode n = (DefaultMutableTreeNode) o;
            return (Node) n.getUserObject();
        }
        else {
            return (Node) o;
        }
    }
}
