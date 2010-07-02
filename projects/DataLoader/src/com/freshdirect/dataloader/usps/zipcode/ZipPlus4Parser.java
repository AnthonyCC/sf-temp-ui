/*
 * VirtualProduct.java
 *
 *
 */

package com.freshdirect.dataloader.usps.zipcode;

import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParser;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.usps.USPSFileParser;

/**
 *
 * @author  mrose
 * @version
 */
public class ZipPlus4Parser extends USPSFileParser implements SynchronousParserClient, SynchronousParser {
    
    public final static String RECORD_TYPE    = "RECORD_TYPE";
    public final static String RECORD_BODY    = "RECORD_BODY";
    
    DetailParser parser;
    
    /** Creates new ReconciliationParser */
    public ZipPlus4Parser() {
        super(182);
        parser = new DetailParser();
        parser.setClient(this);
        fields.add(new Field(RECORD_TYPE,    1, true));
        fields.add(new Field(RECORD_BODY,   -1, true));
        
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
        String recType = getString(tokens, RECORD_TYPE);
        if ("D".equals(recType)) {
            parser.parseLine(getString(tokens, RECORD_BODY));
        }
        
    }
    
    SynchronousParserClient client;
    
    public void setClient(SynchronousParserClient client) {
        this.client = client;
    }
	
	public SynchronousParserClient getClient() {
        return this.client;
    }

    public void accept(Object o) {
        getClient().accept(o);
    }

    
}