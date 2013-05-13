package pbr.m1.g4;

import java.util.LinkedList;
import pbr.m1.g4.services.Services;

/**
 *
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
    
    public synchronized void parse(String input){     
        xmls.add(Services.chunkText(input));
    }
    
    public synchronized void clear(){
        xmls.clear();
    }
    
    private static class CollecterHolder {

        private static final Collector INSTANCE = new Collector();
    }
}
