package pbr.m1.g4;

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pbr.m1.g4.services.Services;
import pbr.m1.g4.services.XMLFactory;

/**
 * Collects a list of SOAP responses
 * @author Chirila Alexandru
 */
public class Collector {

    private volatile LinkedList<String> xmls;

    public LinkedList<String> getXmls() {
        return xmls;
    }

    private Collector() {
        this.xmls = new LinkedList<>();
    }

    public static Collector getInstance() {
        return CollecterHolder.INSTANCE;
    }

    /**
     * Send a SOAP request with the given input and saves the response
     * @param input 
     */
    public void parse(String input) {
        synchronized (this) {
            xmls.add(Services.chunkText(input));
        }
    }

    /**
     * Clear the collector
     */
    public void clear() {
        synchronized (this) {
            xmls.clear();
        }
    }

    /**
     * Save the collected XMLS to the file
     * @param fileName the name of the file
     * @return true is successful
     */
    public boolean saveToFile(String fileName) {
        synchronized (this) {
            try {                
                String[] arr = new String[xmls.size()];
                xmls.toArray(arr);
                XMLFactory.init(fileName, arr);                
                return true;
            } catch (Exception ex) {
                Logger.getLogger(Collector.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
    }

    private static class CollecterHolder {

        private static final Collector INSTANCE = new Collector();
    }
}
