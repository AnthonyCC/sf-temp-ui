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
import com.freshdirect.payment.reconciliation.summary.FileTrailer;

/**
 *
 * @author  mrose
 * @version
 */
public class FileTrailerParser extends SettlementParser {
	
	private SynchronousParserClient client = null;
    
    public final static String FILE_RECORD_COUNT      = "FILE_RECORD_COUNT";
    public final static String FILE_NET_AMOUNT        = "FILE_NET_AMOUNT";
    public final static String FILE_NET_DEPOSIT       = "FILE_NET_DEPOSIT";
    public final static String FILE_EFT_DEPOSITS 	= "FILE_EFT_DEPOSITS";
    public final static String FILE_EFT_RETURNS 		= "FILE_EFT_RETURNS";
    public final static String FILE_EFT_FEES 		= "FILE_EFT_FEES";
    public final static String FILE_POSTED_AMOUNT 	= "FILE_POSTED_AMOUNT";
    
    private FileTrailer record = null;
    
    /** Creates new ReconciliationParser */
    public FileTrailerParser() {
        super();
        
        fields.add(new Field(FILE_RECORD_COUNT,	9, true));
        fields.add(new Field(FILE_NET_AMOUNT,      11, true));
        fields.add(new Field(FILE_NET_DEPOSIT,     11, true));
        fields.add(new Field(FILE_EFT_DEPOSITS,    11, false));
        fields.add(new Field(FILE_EFT_RETURNS,     11, false));
        fields.add(new Field(FILE_EFT_FEES,         9, false));
        fields.add(new Field(FILE_POSTED_AMOUNT,   -1, true));
        
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
        record = new FileTrailer();
        record.setFileRecordCount(getInt(tokens, FILE_RECORD_COUNT));
        record.setFileNetAmount(getSignedDouble(tokens, FILE_NET_DEPOSIT, 2));
        record.setFileNetDeposit(getSignedDouble(tokens, FILE_NET_DEPOSIT, 2));
        record.setFilePostedAmount(getSignedDouble(tokens, FILE_POSTED_AMOUNT, 2));
        
        client.accept(record);
    }
    
    @Override
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