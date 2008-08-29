package app.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.w3c.dom.svg.SVGDocument;

/**
 *
 * @author wara
 */
public class SVGLoader {

	public SVGLoader()
	{}

	public static SVGDocument getSVGDocumentFromPath(String path)
	{
		File file = new File(path);
		return getSVGDocumentFromPath(file);
	}

	public static SVGDocument getSVGDocumentFromInputStream(InputStream in)
	{	if(in!=null)
			{
			try {
				String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
				SAXSVGDocumentFactory fac = new SAXSVGDocumentFactory(null);
				SVGDocument doc = fac.createSVGDocument(svgNS,in);
				return doc;

			} catch(IOException ex) {
				System.err.println(SVGLoader.class.getName()+" "+ ex);
			}
		}else
			System.err.println(SVGLoader.class.getName()+"  InputStream == null");
		return null;
	}
	
	public static SVGDocument getSVGDocumentFromPath(File file)
	{
		if(file.isFile()){		    
		    try {
			//String parser = XMLResourceDescriptor.getXMLParserClassName();//always ret. null
			SAXSVGDocumentFactory fac = new SAXSVGDocumentFactory(null);
			return fac.createSVGDocument(file.toURI().toString());

		    } catch (IOException ex) {
			Logger.getLogger(SVGLoader.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		    }
		}
		return null;
		
	}
}
