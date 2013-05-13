package pbr.m1.g4.services;
 
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
 
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
 
/**
 * Class used for parsing/generating XML file
 * using response from http://nlptools.info.uaic.ro services
 * 
 * Use:
 * 
 * #1 Input from XML file
 * XMLFactory.init("input.xml", "output_xml_file_name", true);
 * 
 * #2 Input from XML string/obj XML
 * XMLFactory.init("<input><xml_string>value</xml_string></input>", "output_xml_file_name", false);
 * 
 * @authors Munteanu Catalin, Popa Alexandru
 * 
 */
public class XMLFactory {
        
    /**
     * Method used to create XML obj from input string
     * 
     */
    public static Document load_XML_from_string(String xml) throws Exception {
        DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder         = factory.newDocumentBuilder();
        InputSource is                  = new InputSource(new StringReader(xml));

        return builder.parse(is);
    }

    /**
     * Method used to parse input XML from string/file and create output XML
     * 
     */    
    public static void init(String input_xml, String xml_filename, boolean is_file) throws Exception {

        try {
            // Init XML Builder/Parse
            DocumentBuilderFactory dbFactory    = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder          = dbFactory.newDocumentBuilder();
            Document input_xml_obj;

            // Read input XML obj
            if(is_file) {
                // Read input XML file
                File xml_file = new File(input_xml);                
                input_xml_obj = docBuilder.parse(xml_file);

            } else {
                // Read input XML from string
                input_xml_obj = load_XML_from_string(input_xml);
            }

            // Create output XML obj
            // root element
            Document output_xml_obj = docBuilder.newDocument();
            Element output_root_el = output_xml_obj.createElement("ROOT");
            output_xml_obj.appendChild(output_root_el);
           
            // optional - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            input_xml_obj.getDocumentElement().normalize();

            // System.out.println("Root: " + input_xml_obj.getDocumentElement().getNodeName());
            
            // Get all sentences in response
            NodeList sentences_nodes = input_xml_obj.getElementsByTagName("S");
            
            // Loop in root children nodes (sentences)  
            for(int i = 0; i < sentences_nodes.getLength(); ++i) {

                Node sentence_node = sentences_nodes.item(i);

                // System.out.println("Current Sentence: " + sentence_node.getNodeName());
                // System.out.println(sentence_node.getNodeType());
                
                // Get all words in sentence
                Element sentence_element = (Element) sentence_node;
                NodeList words_nodes     = sentence_element.getElementsByTagName("W");
                
                // Create sentence nodes in output XML
                Element SEG = output_xml_obj.createElement("SEG");
                SEG.setAttribute("ID", sentence_element.getAttribute("id"));
                output_root_el.appendChild(SEG);
                
                // Loop in words for current sentence
                for(int y=0; y<words_nodes.getLength(); ++y) {
                    Node word_node          = words_nodes.item(y);
                    Element word_element    = (Element) word_node;

                    // System.out.println("Current Word: " + word_node.getNodeName());
                    // System.out.println(word_node.getNodeType());
                    // System.out.println("Value:  " + word_element.getTextContent());
                    // System.out.println("Id:  " + word_element.getAttribute("id"));
                    
                    // Create sentence nodes in output XML
                    Element W = output_xml_obj.createElement("W");
                    
                    W.setTextContent(word_element.getTextContent());
                    W.setAttribute("ID",    word_element.getAttribute("id"));
                    W.setAttribute("LEMMA", word_element.getAttribute("LEMMA"));
                    W.setAttribute("POS",   word_element.getAttribute("POS"));
                    
                    SEG.appendChild(W);
                }
            }
            
            // Write output_xml to XML FILE
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
           
            transformerFactory.setAttribute("indent-number", 4);
            
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(output_xml_obj);
            
            // Save result to XML file
            StreamResult result = new StreamResult(new File("INPUTXML/" + xml_filename + ".xml"));
            
            // Show result in console
            // StreamResult result = new StreamResult(System.out);
            
            transformer.transform(source, result);
            System.out.println("Output XML successfully saved!");
            
        } catch (ParserConfigurationException | DOMException | SAXException | IOException | TransformerFactoryConfigurationError | IllegalArgumentException | TransformerException e) {
        }
    }
}