/*
 * SkuUpcParser.java
 *
 * Created on August 21, 2001, 8:49 PM
 */

package com.freshdirect.dataloader.nutrition;

/**
 *
 * @author  knadeem
 * @version
 */
import java.util.ArrayList;
import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParser;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.TabDelimitedFileParser;

/**
 *
 * @author  knadeem
 * @version
 */
public class SkuUpcParser extends TabDelimitedFileParser implements SynchronousParser {
    
    private ArrayList mapping = null;
    private SynchronousParserClient client = null;
    
    protected static String MATLNUM = "MATLNUM";
    protected static String SKUCODE = "SKUCODE";
    protected static String UPC = "UPC";
    
    public SkuUpcParser() {
        super();
        mapping = new ArrayList();
        
        fields.add(new Field(MATLNUM,   0, true));
        fields.add(new Field(SKUCODE,   0, true));
        fields.add(new Field(UPC,       0, true));
        
    }
    
    /**
     * a template method that must be defined by implementors
     * subclasses will know how to assemble model objects
     * from a a hash of tokens
     * @param tokens a HashMap containing parsed tokens from a single line
     * of a text file, keyed by their field names
     * @throws BadDataException an problems while trying to assemble objects from the
     * supplied tokens
     */
    @Override
    protected void makeObjects(Map<String, String> tokens) throws BadDataException {
        try {
            SkuUpcMap map = new SkuUpcMap();
            map.setSkuCode(getString(tokens, "SKUCODE"));
            map.setUpc(getString(tokens, "UPC"));
            if ((!map.getSkuCode().equals("")) && ((!map.getUpc().equals("")))) {
                System.out.println(map.getSkuCode()+ "  "+map.getUpc());
                client.accept(map);
            }
        } catch (BadDataException bde) {
            throw new BadDataException(bde, "Couldn't make an object");
        }
        
    }
    
    public SynchronousParserClient getClient() {
        return client;
    }
    
    public void setClient(SynchronousParserClient client) {
        this.client = client;
    }
    
    public class SkuUpcMap {
        
        private String skuCode;
        private String upc;
        
        public String getSkuCode() {
            return skuCode;
        }
        
        public void setSkuCode(String skuCode) {
            this.skuCode = skuCode;
        }
        
        public String getUpc() {
            return upc;
        }
        
        public void setUpc(String upc) {
            this.upc = upc;
        }
    }
    
}
