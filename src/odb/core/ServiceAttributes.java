/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package odb.core;

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

/**
 *
 * @author ACME
 */
public class ServiceAttributes implements Element {

    private String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;

    public ServiceAttributes(Float x, Float y) {
        
        this.setAttribute("x", x.toString());
        this.setAttribute("y", y.toString());
    }

    public String getTagName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getAttribute(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAttribute(String name, String value) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeAttribute(String name) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Attr getAttributeNode(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Attr setAttributeNode(Attr newAttr) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NodeList getElementsByTagName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getAttributeNS(String namespaceURI, String localName) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Attr getAttributeNodeNS(String namespaceURI, String localName) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasAttribute(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasAttributeNS(String namespaceURI, String localName) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TypeInfo getSchemaTypeInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setIdAttribute(String name, boolean isId) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getNodeName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getNodeValue() throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setNodeValue(String nodeValue) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public short getNodeType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node getParentNode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NodeList getChildNodes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node getFirstChild() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node getLastChild() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node getPreviousSibling() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node getNextSibling() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NamedNodeMap getAttributes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Document getOwnerDocument() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node removeChild(Node oldChild) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node appendChild(Node newChild) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasChildNodes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Node cloneNode(boolean deep) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void normalize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSupported(String feature, String version) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getNamespaceURI() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getPrefix() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setPrefix(String prefix) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getLocalName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean hasAttributes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getBaseURI() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public short compareDocumentPosition(Node other) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getTextContent() throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setTextContent(String textContent) throws DOMException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isSameNode(Node other) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String lookupPrefix(String namespaceURI) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isDefaultNamespace(String namespaceURI) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String lookupNamespaceURI(String prefix) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isEqualNode(Node arg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getFeature(String feature, String version) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object setUserData(String key, Object data, UserDataHandler handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getUserData(String key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
