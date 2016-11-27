/*
 * SummaryHeaderParser.java
 *
 *
 */

package com.freshdirect.dataloader.payment.reconciliation.summary;

import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.SynchronousParserClient;
import com.freshdirect.dataloader.payment.reconciliation.SettlementParser;
import com.freshdirect.payment.reconciliation.summary.NovusUnbundled;

/**
 *
 * @author  mrose
 * @version
 */
public class NovusUnbundledParser extends SettlementParser {
    
    private SynchronousParserClient client = null;
    
    public final static String UNBUNDLED_OPTION         = "UNBUNDLED_OPTION";
    public final static String NOVUS_INT_FEE_AMOUNT     = "NOVUS_INT_FEE_AMOUNT";
    public final static String NOVUS_ASS_FEE_AMOUNT     = "NOVUS_ASS_FEE_AMOUNT";
    public final static String NOVUS_TRAN_FEE_AMOUNT    = "NOVUS_TRAN_FEE_AMOUNT";
    
    private NovusUnbundled record = null;
    
    /** Creates new ReconciliationParser */
    public NovusUnbundledParser() {
        super();
        
        fields.add(new Field(UNBUNDLED_OPTION,              1, true));
        fields.add(new Field(NOVUS_INT_FEE_AMOUNT,          7, true));
        fields.add(new Field(NOVUS_ASS_FEE_AMOUNT,          7, true));
        fields.add(new Field(NOVUS_TRAN_FEE_AMOUNT,         7, true));
        
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
        record = new NovusUnbundled();
        record.setUnbundledOption(getString(tokens, UNBUNDLED_OPTION));
        record.setNovusInterchangeFee(getSignedDouble(tokens, NOVUS_INT_FEE_AMOUNT, 2));
        record.setNovusAssessmentFee(getSignedDouble(tokens, NOVUS_ASS_FEE_AMOUNT, 2));
        record.setNovusTransactionFee(getSignedDouble(tokens, NOVUS_TRAN_FEE_AMOUNT, 2));
        
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