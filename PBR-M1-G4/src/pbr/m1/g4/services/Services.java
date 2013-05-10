package pbr.m1.g4.services;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.soap.SOAPException;

/**
 * Wrapper for pbr.m1.g4.SOAPClient, providing several simple use methods for 
 * different Web Services found at: http://nlptools.info.uaic.ro/
 * @author Chirila Alexandru
 */
public class Services {
    static final String NAMESPACE = "mns1";
    static final String URL = "http://nlptools.info.uaic.ro:80/WebNpChunkerRo/NpChunkerRoWS";
    static final String XLMNS = "http://webNpChunkerRo.uaic/";
    static final String XmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
    
    static final String ChunkText = "chunkText";
    static final String ChunkTaggedText = "chunkTaggedText";
    
    public static String chunkText(String inputText){
        try {
            return new SOAPClient(URL)
                    .addCallName(ChunkText,NAMESPACE,XLMNS)
                    .addParameter("inputText", inputText).call();
        } catch (UnsupportedOperationException | SOAPException | IOException ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static String chunkTaggedText(String taggedXML){
        try {
            return new SOAPClient(URL)
                    .addCallName(ChunkTaggedText,NAMESPACE,XLMNS)
                    .addParameter("taggedXml", taggedXML).call();
        } catch (UnsupportedOperationException | SOAPException | IOException ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
