package pbr.m1.g4.services;

/**
 * Application main class
 * @author Echipa 4 PBR 2013
 */
public class App {
    
    public static void main(String[] args) throws Exception {
        
        // Test parse and generate xml from XML file
        // XMLFactory.init("input.xml", "output_file", true);
        
        // Test parse and generate xml using SOAP services (from XML string)
        String soap_response = Services.chunkText("Maria are mere. Ionut merge la mare.");
        XMLFactory.init(soap_response, "output_filename", false);
    }    
}
