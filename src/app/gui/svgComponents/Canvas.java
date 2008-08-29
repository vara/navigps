/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app.gui.svgComponents;

import config.Configuration;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JLabel;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.GVTTreeBuilderAdapter;
import org.apache.batik.swing.svg.GVTTreeBuilderEvent;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;

/**
 *
 * @author vara
 */

/* important "Rendering Process"
 * 
 *  The rendering process can be broken down into five phases. 
 *  Not all of those steps are required - depending on the method used to specify 
 *  the SVG document to display, but basically the steps in the rendering process are:
 *
 *  --Building a DOM tree 
 *	If the loadSVGDocument(String) method is used, the SVG file is parsed 
 *	and an SVG DOM Tree is built.
 * 
 *  --Building a GVT tree 
 *	Once an SVGDocument is created (using the step 1 or if the setSVGDocument(SVGDocument) 
 *	method has been used) - a GVT tree is constructed. 
 *	The GVT tree is the data structure used internally to render an SVG document. 
 *	see the org.apache.batik.gvt package.
 * 
 *  --Executing the SVGLoad event handlers 
 *	If the document is dynamic, the scripts are initialized and the SVGLoad 
 *	event is dispatched before the initial rendering. 
 * 
 *  --Rendering the GVT tree 
 *	Then the GVT tree is rendered. see the org.apache.batik.gvt.renderer package.
 * 
 *  --Running the document 
 *	If the document is dynamic, the update threads are started.
 */

public class Canvas extends JSVGCanvas{

    private StatusDocument labStatusDocument= new StatusDocument("Chose Document");
    
    public Canvas(){	
	
	setDocumentState(JSVGCanvas.ALWAYS_DYNAMIC);
	addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter() {
		@Override
		public void documentLoadingStarted(SVGDocumentLoaderEvent e) {
		    String currentStatus = "Document Loading...";
		    labStatusDocument.setText(currentStatus);
		    if(Configuration.getMode()){			
			System.out.println(currentStatus);
		    }
		}
		@Override
		public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {

		    if(Configuration.getMode())
			System.out.println("Document Loaded.");
		}
	    });
	    
	addGVTTreeBuilderListener(new GVTTreeBuilderAdapter() {
	    @Override
	    public void gvtBuildStarted(GVTTreeBuilderEvent e) {

		if(Configuration.getMode())
		    System.out.println("Build Started...");

	    }
	    @Override
	    public void gvtBuildCompleted(GVTTreeBuilderEvent e) {
		
		if(Configuration.getMode()){
		    System.out.println("Build Done.");		    
		}
	    }
	});
	
    }
    
    protected void setTextforLabStatusDocument(String str){
	labStatusDocument.setText(str);
    }
    
    public String getTextFromLabStatusDocument(){
	return labStatusDocument.getText();
    }
    
    private class StatusDocument extends JLabel implements MouseMotionListener{

	public StatusDocument(String str){
	    super(str);
	}
	public void mouseDragged(MouseEvent e) {
	    
	}

	public void mouseMoved(MouseEvent e) {
	    setToolTipText(getText());
	}    
    }
}
