/*
 * VirtualProduct.java
 *
 *
 */

package com.freshdirect.dataloader.payment.reconciliation.detail;

import java.util.*;

import com.freshdirect.dataloader.*;

import com.freshdirect.payment.reconciliation.detail.*;
import com.freshdirect.dataloader.payment.reconciliation.SettlementParser;

/**
 *
 * @author  mrose
 * @version
 */
public class FileTrailerParser extends SettlementParser {
    
    private SynchronousParserClient client = null;
    public final static String REC_COUNT       = "REC_COUNT";
    
    private FileTrailer record = null;
    
    /** Creates new ReconciliationParser */
    public FileTrailerParser() {
        super();
        
        fields.add(new Field(REC_COUNT,    11, true));

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
    protected void makeObjects(HashMap tokens) throws BadDataException {
        record = new FileTrailer();
        record.setRecordCount(getInt(tokens, REC_COUNT));
        
        this.client.accept(record);
    }
    
    public SynchronousParserClient getClient(){
    	return this.client;
    }
    
    public void setClient(SynchronousParserClient client){
    	this.client = client;
    }
    
    public String debug() {
        return this.record.toString();
    }
    
    
}