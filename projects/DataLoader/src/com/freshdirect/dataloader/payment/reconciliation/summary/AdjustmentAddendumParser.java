/*
 * SummaryHeaderParser.java
 *
 *
 */

package com.freshdirect.dataloader.payment.reconciliation.summary;

import java.util.*;

import com.freshdirect.dataloader.*;

import com.freshdirect.payment.reconciliation.summary.*;
import com.freshdirect.dataloader.payment.reconciliation.SettlementParser;

/**
 *
 * @author  mrose
 * @version
 */
public class AdjustmentAddendumParser extends SettlementParser {
	
	private SynchronousParserClient client = null;
    
    public final static String REFERENCE_NUMBER     = "REFERENCE_NUMBER";
    public final static String SEQUENCE_NUMBER      = "SEQUENCE_NUMBER";
    public final static String NET_SALES_AMOUNT     = "NET_SALES_AMOUNT";
    
    private AdjustmentAddendum record = null;
    
    /** Creates new ReconciliationParser */
    public AdjustmentAddendumParser() {
        super();
        
        fields.add(new Field(REFERENCE_NUMBER,     15, true));
        fields.add(new Field(SEQUENCE_NUMBER,       5, true));
        fields.add(new Field(NET_SALES_AMOUNT,      8, true));
        
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
        record = new AdjustmentAddendum();
        record.setReferenceNumber(getString(tokens, REFERENCE_NUMBER));
        record.setSequenceNumber(getString(tokens, SEQUENCE_NUMBER));
        record.setNetSalesAmount(getSignedDouble(tokens, NET_SALES_AMOUNT, 2));
        
        client.accept(record);
    }
    
    public String debug() {
        return this.record.toString();
    }
    
    public SynchronousParserClient getClient(){
    	return this.client;
    }
    
    public void setClient(SynchronousParserClient client){
    	this.client = client;
    }
    
}