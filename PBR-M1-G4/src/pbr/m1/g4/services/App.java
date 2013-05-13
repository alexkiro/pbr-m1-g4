package pbr.m1.g4.services;

/**
 * Application main class
 * @author Echipa 4 PBR 2013
 */
public class App {
    
    public static void main(String[] args) throws Exception {
        
        // Test parse and generate xml using SOAP services (from XML string)
        String[] response = new String[2];
        response[0] = Services.chunkText("Maria are mere. Ionut merge la mare.");
        response[1] = Services.chunkText("Andrei citeste o carte. Dan se plimba in parc. Maria nu mai are mere.");

        XMLFactory.init("output_filename", response);
    }    
}
