package pbr.m1.g4.services;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * Class used for parsing/generating XML file using response from
 * http://nlptools.info.uaic.ro services
 *
 * Use:
 * Input from XML string XML
 * XMLFactory.init(xml_filename, [string_xml_input, string_xml_input]);
 * 
 * @authors Munteanu Catalin, Popa Alexandru
 *
 */
public class XMLFactory {

    /**
     * Method used to create XML obj from input string
     *
     */
    private static Document load_XML_from_string(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));

        return builder.parse(is);
    }
    
    /**
     * Method used to append sentences to root element and words to sentences nodes
     * 
     */
    public static Element parse_input_xml(Document output_xml_obj, Element output_root_el, Document input_xml_obj) {
        int sentence_index,
            word_index;
        
        // Get all sentences in response
        NodeList sentences_nodes = input_xml_obj.getElementsByTagName("S");
        
        // Loop in sentences
        for(int i = 0; i < sentences_nodes.getLength(); ++i) {
            sentence_index           = output_root_el.getElementsByTagName("SEG").getLength() + 1;
            Node sentence_node       = sentences_nodes.item(i);
            Element sentence_element = (Element) sentence_node;
            NodeList words_nodes     = sentence_element.getElementsByTagName("W");

            // Create sentence nodes in output XML
            Element SEG = output_xml_obj.createElement("SEG");
            SEG.setAttribute("ID", Integer.toString(sentence_index));
            output_root_el.appendChild(SEG);

            // Loop in words for current sentence
            for(int y=0; y<words_nodes.getLength(); ++y) {
                word_index              = SEG.getElementsByTagName("W").getLength() + 1;
                Node word_node          = words_nodes.item(y);
                Element word_element    = (Element) word_node;

                // System.out.println("Current Word: " + word_node.getNodeName());
                // System.out.println(word_node.getNodeType());
                // System.out.println("Value:  " + word_element.getTextContent());
                // System.out.println("Id:  " + word_element.getAttribute("id"));

                // Create sentence nodes in output XML
                Element W = output_xml_obj.createElement("W");

                W.setTextContent(word_element.getTextContent());
                W.setAttribute("ID",    Integer.toString(sentence_index) + '.' + Integer.toString(word_index));
                W.setAttribute("LEMMA", word_element.getAttribute("LEMMA"));
                W.setAttribute("POS",   word_element.getAttribute("POS"));

                SEG.appendChild(W);
            }
        }
        
        return output_root_el;
    }

    /**
     * Method used to parse input XML from string/file and create output XML
     * 
     */    
    public static void init(String xml_filename, String[] xml_input) throws Exception {

        try {
            // Init XML Builder/Parse
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
            Document input_xml_obj;
            
            // Create output XML obj
            // root element
            Document output_xml_obj = docBuilder.newDocument();
            Element output_root_el = output_xml_obj.createElement("ROOT");
            output_xml_obj.appendChild(output_root_el);
            
            for(int i=0; i<xml_input.length; ++i) {
                // Read input XML from string
                input_xml_obj = load_XML_from_string(xml_input[i]);
                // optional - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
                input_xml_obj.getDocumentElement().normalize();
                output_root_el = parse_input_xml(output_xml_obj, output_root_el, input_xml_obj);
            }
          
            // Write output_xml to XML FILE
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            transformerFactory.setAttribute("indent-number", 4);

            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(output_xml_obj);

            // Save result to XML file
            StreamResult result = new StreamResult(new File(xml_filename));

            // Show result in console
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);
            System.out.println("Output XML successfully saved!");

        } catch (ParserConfigurationException | DOMException | SAXException | IOException | TransformerFactoryConfigurationError | IllegalArgumentException | TransformerException ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
