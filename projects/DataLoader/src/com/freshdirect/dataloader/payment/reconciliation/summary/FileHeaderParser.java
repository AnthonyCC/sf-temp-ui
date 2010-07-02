/*
 * VirtualProduct.java
 *
 *
 */

package com.freshdirect.dataloader.payment.reconciliation.summary;

import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.payment.reconciliation.SettlementParser;
import com.freshdirect.payment.reconciliation.summary.FileHeader;

/**
 *
 * @author  mrose
 * @version
 */
public class FileHeaderParser extends SettlementParser {
	
	private SynchronousParserClient client = null;
    
    public final static String PROC_PERIOD_START      = "PROC_PERIOD_START";
    public final static String PROC_PERIOD_END        = "PROC_PERIOD_END";
    public final static String FILLER                 = "FILLER";
    public final static String FILE_ID                = "FILE_ID";
    
    private FileHeader header = null;
    
    /** Creates new ReconciliationParser */
    public FileHeaderParser() {
        super();
        
        fields.add(new Field(PROC_PERIOD_START, 6, true));
        fields.add(new Field(FILLER,            2, true));
        fields.add(new Field(PROC_PERIOD_END,   6, true));
        fields.add(new Field(FILLER,           61, true));
        fields.add(new Field(FILE_ID,           4, true));
        
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
        header = new FileHeader();
        header.setProcessingPeriodStartDate(getDate(tokens, PROC_PERIOD_START, "MMddyy"));
        header.setProcessingPeriodEndDate(getDate(tokens, PROC_PERIOD_END, "MMddyy"));
        header.setFileID(getString(tokens, FILE_ID));
        
        client.accept(header);
    }
    
    @Override
    public String debug() {
        return this.header.toString();
    }
    
    public SynchronousParserClient getClient(){
    	return this.client;
    }
    
    public void setClient(SynchronousParserClient client){
    	this.client = client;
    }   
    
}