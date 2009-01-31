package odb.core;

import java.util.Vector;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

public class RootElementStreet implements Node {
    
    private String name;
    private String additionalAttributeA;
    private String additionalAttributeB;
    private Vector services;
    private SVGOMPoint svgPoint;
    private double localx;
    private double localy;
    

    public RootElementStreet(String s,String attribA,String attribB,SVGOMPoint svgPoint,double localx,double localy) {
        this.name = s;
        this.additionalAttributeA = attribA;
        this.additionalAttributeB = attribB;
        this.services = null;
        this.svgPoint = svgPoint;
        this.localx = localx;
        this.localy = localy;
    }
    
    public RootElementStreet() {
        
    }

    public String getName() {
        return name;
    }

    public String getAdditionalAttributeA() { 
        return additionalAttributeA;
    }

    public String getAdditionalAttributeB() {
        return additionalAttributeB;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdditionalAttributeA(String additionalAttributeA) {
        this.additionalAttributeA = additionalAttributeA;
    }

    public void setAdditionalAttributeB(String additionalAttributeB) {
        this.additionalAttributeB = additionalAttributeB;
    }

    public Vector getServices() {
        return services;
    }

    public void setServices(Vector services) {
        this.services = services;
    }
    
    public void addService(SubElementService service) {
        services.add(service);
    }

    public double getLocalx() {
        return localx;
    }

    public void setLocalx(double localx) {
        this.localx = localx;
    }

    public double getLocaly() {
        return localy;
    }

    public void setLocaly(double localy) {
        this.localy = localy;
    }

    public SVGOMPoint getSvgPoint() {
        return svgPoint;
    }

    public void setSvgPoint(SVGOMPoint svgPoint) {
        this.svgPoint = svgPoint;
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