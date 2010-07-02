/*
 * VirtualProduct.java
 *
 *
 */

package com.freshdirect.dataloader.payment.reconciliation.detail;

import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.payment.reconciliation.SettlementParser;
import com.freshdirect.payment.reconciliation.detail.FileHeader;

/**
 *
 * @author  mrose
 * @version
 */
public class FileHeaderParser extends SettlementParser {
	
	public SynchronousParserClient client = null;
    
    public final static String FILE_CREATION_DATE       = "FILE_CREATION_DATE";
    public final static String FILLER_1                 = "FILLER_1";
    public final static String FILE_FORMAT_CODE         = "FILE_FORMAT_CODE";
    public final static String PROC_MESSAGE             = "PROC_MESSAGE";
    public final static String FILLER_2                 = "FILLER_2";
    public final static String FILE_ID                  = "FILE_ID";
    
    private FileHeader record = null;
    
    /** Creates new ReconciliationParser */
    public FileHeaderParser() {
        super();
        
        fields.add(new Field(FILE_CREATION_DATE,    8, true));
        fields.add(new Field(FILLER_1,              8, true));
        fields.add(new Field(FILE_FORMAT_CODE,      1, true));
        fields.add(new Field(PROC_MESSAGE,         14, true));
        fields.add(new Field(FILLER_2,             44, true));
        fields.add(new Field(FILE_ID,               4, true));
        
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
        record = new FileHeader();
        record.setFileCreationDate(getDate(tokens, FILE_CREATION_DATE, "MM/dd/yy"));
        record.setFileFormatCode(getString(tokens, FILE_FORMAT_CODE));
        record.setProcessingMessage(getString(tokens, PROC_MESSAGE));
        record.setFileId(getString(tokens, FILE_ID));
        
        this.client.accept(record);
    }
    
    public SynchronousParserClient getClient(){
    	return this.client;
    }
    
    public void setClient(SynchronousParserClient client){
    	this.client = client;
    }
    
    @Override
    public String debug() {
        return this.record.toString();
    }
    
    
}