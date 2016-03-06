/**
 * @author Ivan Tovar
 * @date February 2016
 * Yii Architecture Analyzer Plugin
 * 
 * This uses the JDOM library to read XML files.
 */

package visualyzer.resultsreader;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


public class XMLParser {
    
    private Document document;
    
    public XMLParser(String filename) {
        this.setFile(filename);
    }
    
    public List<Element> getChildrenObjects(String name) {
        Element root = document.getRootElement();
        return root.getChildren(name);
    }
    
    public final void setFile(String filename) {
        try {
            SAXBuilder builder = new SAXBuilder();
            File file = new File(filename);
            document = (Document)builder.build(file);
        } 
        catch (JDOMException | IOException ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
    
    public Document getDocument() {
        return document;
    }
    
    public Element getRootNode() {
        return document.getRootElement();
    }
    
}
