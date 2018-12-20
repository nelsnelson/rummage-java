/*
 * DuplicateCommentsRemover.java
 *
 * Created on January 26, 2006, 9:09 AM
 */

package org.rummage.toolbox.util;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.w3c.dom.traversal.TreeWalker;

/**
 *
 * @author nelsnelson
 */
public abstract class XMLDocumentUtilities {
    private Document systemDocument = null;
    private Node root = null;
    
    public void setDocument(Document document) {
        systemDocument = document;
        
        setRoot(systemDocument.getDocumentElement());
    }
    
    public Document getDocument() {
        return systemDocument;
    }
    
    public void setRoot(Node rootNode) {
        root = rootNode;
    }
    
    public Node getRoot() {
        return root;
    }

    protected Node createNode(String elementName, String data) {
        Node n = systemDocument.createElement(elementName);
        
        if (n == null) {
            return null;
        }
        
        n.setTextContent(data);
        
        return n;
    }
    
    public Node find(String text, Node currentNode) {
        Node root = getRoot();

        TreeWalker w1 = walker(root, currentNode);
        
        Node n = null;
        Node found = null;

        while ((n = w1.nextNode()) != null) {
            de.bug.ging(n);
            
            if (n.getNodeName().contains(text)) {
                return n;
            }
            else if (n.hasChildNodes()) {
                continue;
            }
            else if (n.getTextContent().contains(text)) {
                return n;
            }
        }

        return null;
    }
    
    public static Node find(String text, Node root, Node currentNode) {
        if (root == null) {
            return null;
        }

        TreeWalker w1 = walker(root, currentNode);
        
        Node n = null;
        Node found = null;

        while ((n = w1.nextNode()) != null) {
            de.bug.ging(n);
            
            if (n.getNodeName().contains(text)) {
                return n;
            }
            else if (n.hasChildNodes()) {
                continue;
            }
            else if (n.getTextContent().contains(text)) {
                return n.getParentNode();
            }
        }
        
        return null;
    }
    
    protected Node getNodeByName(TreeWalker walk, String name) {
        Node node = null;
        
        while (walk != null && (node = walk.nextNode()) != null) {
            if (node.getNodeName().equals(name)) {
                return node;
            }
        }
        
        return null;
    }
    
    protected Node getChildNodeByName(Node parentNode, String name) {
        Node childNode = null;
        
        if (parentNode == null) {
            return null;
        }
        
        NodeList childNodesList = parentNode.getChildNodes();
        
        for (int i = 0, n = childNodesList.getLength(); i < n; i++) {
            childNode = childNodesList.item(i);
            
            if (childNode == null) {
                continue;
            }
            
            String childNodeName = childNode.getNodeName();
            
            if (childNodeName != null && childNodeName.equals(name)) {
                return childNode;
            }
        }
        
        return null;
    }
    
    protected List getAllChildNodesByName(Node parentNode, String name) {
        List childNodes = new ArrayList();
        
        if (parentNode == null) {
            return null;
        }
        
        NodeList childNodesList = parentNode.getChildNodes();
        
        for (int i = 0, n = childNodesList.getLength(); i < n; i++) {
            Node childNode = childNodesList.item(i);
            
            if (childNode == null) {
                continue;
            }
            
            String childNodeName = childNode.getNodeName();
            
            if (childNodeName == null) {
                continue;
            }
            
            if (childNodeName.equalsIgnoreCase(name)) {
                childNodes.add(childNode);
            }
        }
        
        return childNodes;
    }
    
    protected int getChildCount(Node parentNode) {
        return parentNode.getChildNodes().getLength();
    }
    
    protected Node getNodeById(TreeWalker walk, String id) {
        Node node = null;
        
        while ((node = walk.nextNode()) != null) {
            String nodeId = getAttribute(node, "id");
            
            if (nodeId != null && nodeId.equals(id)) {
                return node;
            }
        }
        
        return null;
    }
    
    protected Node getChildNodeById(Node parentNode, String id) {
        Node childNode = null;
        
        if (parentNode == null) {
            return null;
        }
        
        NodeList childNodesList = parentNode.getChildNodes();
        
        for (int i = 0, n = childNodesList.getLength(); i < n; i++) {
            childNode = childNodesList.item(i);
            
            if (childNode == null) {
                continue;
            }
            
            String childNodeId = getAttribute(childNode, "id");
            
            if (childNodeId != null && childNodeId.equals(id)) {
                return childNode;
            }
        }
        
        return null;
    }
    
    protected List<Node> getAllNodesByName(TreeWalker walk, String name) {
        Node node = null;
        List<Node> nodes = new ArrayList<Node>();
        
        while ((node = walk.nextNode()) != null) {
            if (node.getNodeName().equals(name)) {
                nodes.add(node);
            }
        }
        
        return nodes;
    }
    
    protected List<Node> getAllNodesById(TreeWalker walk, String id) {
        Node node = null;
        List<Node> nodes = new ArrayList<Node>();
        
        while ((node = walk.nextNode()) != null) {
            if (getAttribute(node, "id").equals(id)) {
                nodes.add(node);
            }
        }
        
        return nodes;
    }
    
    protected List<String> getAllNodeValuesByName(TreeWalker walk,
        String name)
    {
        Node node = null;
        List<String> nodeValues = new ArrayList<String>();
        
        while ((node = walk.nextNode()) != null) {
            if (node.getNodeName().equals(name)) {
                nodeValues.add(node.getTextContent());
            }
        }
        
        return nodeValues;
    }
    
    protected String getAttribute(Node n, String name) {
        if (n == null) {
            return null;
        }
        
        NamedNodeMap nodeMap = n.getAttributes();
        
        if (nodeMap != null) {
            Node attribute = nodeMap.getNamedItem(name);
            
            if (attribute != null) {
                return attribute.getTextContent();
            }
        }
        
        return null;
    }
    
    protected boolean replaceNode(Node oldNode, Node newNode) {
        if (oldNode == null) {
            return false;
        }
        
        Node parentNode = oldNode.getParentNode();
        
        if (parentNode == null) {
            return false;
        }
        
        // Clone (the whole subtree of) the new node so that it doesn't get
        // transplanted (moved) from wherever it was by the replaceChild
        // method.
        Node replacementNode = newNode.cloneNode(true);
        
        parentNode.replaceChild(replacementNode, oldNode);
        
        return true;
    }
    
    public void appendNodesAsChildren(Node node, NodeList nodes) {
        int nodeCount = nodes.getLength();
        
        for (int i = 0; i < nodeCount; i++) {
            node.appendChild(nodes.item(i));
        }
    }
    
    public void removeChildren(Node node) {
        NodeList children = node.getChildNodes();
        int nodeCount = children.getLength();
        
        for (int i = 0; i < nodeCount; i++) {
            node.removeChild(children.item(0));
        }
    }
    
    public void nullify(Node node) {
        if (node == null) {
            return;
        }
        
        removeChildren(node);
        node.setNodeValue(null);
        node.setPrefix(null);
        node.setTextContent(null);
    }
    
    protected TreeWalker walker(Node currentNode) {
        if (currentNode == null) {
            return null;
        }
        
        DocumentTraversal traversal =
            (DocumentTraversal) getRoot().getOwnerDocument();
        
        TreeWalker walker =
            traversal.createTreeWalker(getRoot(), NodeFilter.SHOW_ALL,
            null, true);
        
        walker.setCurrentNode(currentNode);
            
        return walker;
    }
    
    public static TreeWalker walker(Node root, Node currentNode) {
        if (root == null) {
            return null;
        }
        
        DocumentTraversal traversal =
            (DocumentTraversal) root.getOwnerDocument();
        
        TreeWalker walker =
            traversal.createTreeWalker(root, NodeFilter.SHOW_ALL,
            null, true);
        
        if (currentNode == null) {
            return walker;
        }
        
        walker.setCurrentNode(currentNode);
            
        return walker;
    }
    
    protected NodeIterator iterator(Node currentNode) {
        DocumentTraversal traversal =
            (DocumentTraversal) getRoot().getOwnerDocument();
        
        NodeIterator iterator =
            traversal.createNodeIterator(currentNode, NodeFilter.SHOW_ALL,
            null, true);
            
        return iterator;
    }

    // filters elements in the XML document; returns all the Element nodes
    protected static class AllElements 
        implements NodeFilter
    {
        public short acceptNode (Node n) {
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                return FILTER_ACCEPT;
            }
            
            return FILTER_SKIP;
        }
    }
}
