/*
 * XMLMutableTreeNode.java
 *
 * Created on July 18, 2006, 9:25 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.rummage.slide.tree;

import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

/**
 *
 * @author nelsnelson
 */
public class XMLMutableTreeNode
    extends DefaultMutableTreeNode
    implements Node
{
    /** Creates a new instance of XMLMutableTreeNode */
    public XMLMutableTreeNode() {
        super();
    }
    
    /** Creates a new instance of XMLMutableTreeNode */
    public XMLMutableTreeNode(Object userObject) {
        super(userObject);
    }
    
    public Node getNode() {
        if (userObject instanceof Node) {
            return (Node) userObject;
        }
        
        return null;
    }
    
    public Object getUserObject() {
        Node node = getNode();
        Node child = node.getFirstChild();
        
        if (child != null && child.getNodeType() == Node.TEXT_NODE) {
            return child.getNodeValue();
        }
        
        return node;
    }
    
    public short getNodeType() {
        Node node = getNode();
        
        return node == null ? Node.ELEMENT_NODE : node.getNodeType();
    }
    
    public String getNodeName() {
        Node node = getNode();
        
        return node == null ? "" : node.getNodeName();
    }
    
    public String getNodeValue() throws DOMException {
        Node node = getNode();
        
        return node == null ? "" : node.getNodeValue();
    }
    
    public void setNodeValue(String nodeValue) throws DOMException {
        Node node = getNode();
        
        if (node == null) {
            return;
        }
        
        node.setNodeValue(nodeValue);
    }
    
    public Node getParentNode() {
        Node node = getNode();
        
        return node == null ? null : node.getParentNode();
    }
    
    public NodeList getChildNodes() {
        Node node = getNode();
        
        return node == null ? null : node.getChildNodes();
    }
    
    public XMLMutableTreeNode getFirstChild() {
        return
            super.getChildCount() > 0 ?
                (XMLMutableTreeNode) super.getFirstChild() : null;
    }
    
    public XMLMutableTreeNode getLastChild() {
        return
            super.getChildCount() > 0 ?
                (XMLMutableTreeNode) super.getLastChild() : null;
    }
    
    public XMLMutableTreeNode getNextSibling() {
        return (XMLMutableTreeNode) super.getNextSibling();
    }
    
    public XMLMutableTreeNode getPreviousSibling() {
        return (XMLMutableTreeNode) super.getPreviousSibling();
    }
    
    public NamedNodeMap getAttributes() {
        Node node = getNode();
        
        return node == null ? null : node.getAttributes();
    }
    
    public Document getOwnerDocument() {
        Node node = getNode();
        
        return node == null ? null : node.getOwnerDocument();
    }
    
    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        Node node = getNode();
        
        return node == null ? null : node.insertBefore(newChild, refChild);
    }
    
    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        Node node = getNode();
        
        return node == null ? null : node.replaceChild(newChild, oldChild);
    }
    
    public Node removeChild(Node oldChild) throws DOMException {
        Node node = getNode();
        
        return node == null ? null : node.removeChild(oldChild);
    }
    
    public Node appendChild(Node newChild) throws DOMException {
        Node node = getNode();
        
        return node == null ? null : node.appendChild(newChild);
    }
    
    public boolean hasChildNodes() {
        Node node = getNode();
        
        return node == null ? false : node.hasChildNodes();
    }
    
    public Node cloneNode(boolean deep) {
        Node node = getNode();
        
        return node == null ? null : node.cloneNode(deep);
    }
    
    public void normalize() {
        Node node = getNode();
        
        if (node == null) {
            return;
        }
        
        node.normalize();
    }
    
    public boolean isSupported(String feature, String version) {
        Node node = getNode();
        
        return node == null ? false : node.isSupported(feature, version);
    }
    
    public String getNamespaceURI() {
        Node node = getNode();
        
        return node == null ? "" : node.getNamespaceURI();
    }
    
    public String getPrefix() {
        Node node = getNode();
        
        return node == null ? "" : node.getPrefix();
    }
    
    public void setPrefix(String prefix) throws DOMException {
        Node node = getNode();
        
        if (node == null) {
            return;
        }
        
        node.setPrefix(prefix);
    }
    
    public String getLocalName() {
        Node node = getNode();
        
        return node == null ? "" : node.getLocalName();
    }
    
    public boolean hasAttributes() {
        Node node = getNode();
        
        return node == null ? false : node.hasAttributes();
    }
    
    public String getBaseURI() {
        Node node = getNode();
        
        return node == null ? "" : node.getBaseURI();
    }
    
    public short compareDocumentPosition(Node other) throws DOMException {
        Node node = getNode();
        
        return node == null ? 0 : node.compareDocumentPosition(other);
    }
    
    public String getTextContent() throws DOMException {
        Node node = getNode();
        
        return node == null ? null : node.getTextContent();
    }
    
    public void setTextContent(String textContent) throws DOMException {
        Node node = getNode();
        
        if (node == null) {
            return;
        }
        
        node.setTextContent(textContent);
    }
    
    public boolean isSameNode(Node other) {
        Node node = getNode();
        
        return node == null ? false : node.isSameNode(other);
    }
    
    public String lookupPrefix(String namespaceURI) {
        Node node = getNode();
        
        return node == null ? null : node.lookupPrefix(namespaceURI);
    }
    
    public boolean isDefaultNamespace(String namespaceURI) {
        Node node = getNode();
        
        return node == null ? false : node.isDefaultNamespace(namespaceURI);
    }
    
    public String lookupNamespaceURI(String prefix) {
        Node node = getNode();
        
        return node == null ? null : node.lookupNamespaceURI(prefix);
    }
    
    public boolean isEqualNode(Node arg) {
        Node node = getNode();
        
        return node == null ? false : node.isEqualNode(arg);
    }
    
    public Object getFeature(String feature, String version) {
        Node node = getNode();
        
        return node == null ? null : node.getFeature(feature, version);
    }
    
    public Object setUserData(String key, Object data, UserDataHandler handler) {
        Node node = getNode();
        
        return node == null ? null : node.setUserData(key, data, handler);
    }
    
    public Object getUserData(String key) {
        Node node = getNode();
        
        return node == null ? null : node.getUserData(key);
    }
    
    public Vector xmlchildren() {
        Vector xmlchildren = new Vector();
        
        NodeList childNodes = getChildNodes();
        
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            
            XMLMutableTreeNode treeNode = new XMLMutableTreeNode(childNode);
            
            xmlchildren.add(treeNode);
        }
        
        return xmlchildren;
    }
    
    public Enumeration preorderEnumeration() {
        return new PreorderEnumeration(this);
    }

    final class PreorderEnumeration implements Enumeration<TreeNode> {
	protected Stack stack;

	public PreorderEnumeration(TreeNode rootNode) {
	    super();
	    Vector v = new Vector(1);
	    v.addElement(rootNode);	// PENDING: don't really need a vector
	    stack = new Stack();
	    stack.push(v.elements());
	}

	public boolean hasMoreElements() {
	    return (!stack.empty() &&
		    ((Enumeration)stack.peek()).hasMoreElements());
	}

	public TreeNode nextElement() {
	    Enumeration	enumer = (Enumeration)stack.peek();
	    TreeNode	node = (TreeNode)enumer.nextElement();
	    Enumeration	children = node.children();
	    Enumeration xmlchildren = ((XMLMutableTreeNode) node).xmlchildren().elements();
            
            
            
	    if (!enumer.hasMoreElements()) {
		stack.pop();
	    }
	    if (children.hasMoreElements()) {
		stack.push(children);
	    }
	    if (xmlchildren.hasMoreElements()) {
		stack.push(xmlchildren);
	    }
	    return node;
	}
    }  // End of class PreorderEnumeration
}
