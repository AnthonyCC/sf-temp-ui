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
import com.freshdirect.payment.reconciliation.summary.VisaMCUnbundled;

/**
 *
 * @author  mrose
 * @version
 */
public class VisaMCUnbundledParser extends SettlementParser{
    
    private SynchronousParserClient client = null;
    public final static String UNBUNDLED_OPTION         = "UNBUNDLED_OPTION";
    public final static String VISA_INT_FEE_AMOUNT      = "VISA_INT_FEE_AMOUNT";
    public final static String VISA_ASS_FEE_AMOUNT      = "VISA_ASS_FEE_AMOUNT";
    public final static String VISA_TRAN_FEE_AMOUNT     = "VISA_TRAN_FEE_AMOUNT";
    public final static String MC_INT_FEE_AMOUNT        = "MC_INT_FEE_AMOUNT";
    public final static String MC_ASS_FEE_AMOUNT        = "MC_ASS_FEE_AMOUNT";
    public final static String MC_TRAN_FEE_AMOUNT       = "MC_TRAN_FEE_AMOUNT";
    
    private VisaMCUnbundled record = null;
    
    /** Creates new ReconciliationParser */
    public VisaMCUnbundledParser() {
        super();
        
        fields.add(new Field(UNBUNDLED_OPTION,      1, true));
        fields.add(new Field(VISA_INT_FEE_AMOUNT,   7, true));
        fields.add(new Field(VISA_ASS_FEE_AMOUNT,   7, true));
        fields.add(new Field(VISA_TRAN_FEE_AMOUNT,  7, true));
        fields.add(new Field(MC_INT_FEE_AMOUNT,     7, true));
        fields.add(new Field(MC_ASS_FEE_AMOUNT,     7, true));
        fields.add(new Field(MC_TRAN_FEE_AMOUNT,    7, true));
        
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
        record = new VisaMCUnbundled();
        record.setUnbundledOption(getString(tokens, UNBUNDLED_OPTION));
        record.setVisaInterchangeFee(getSignedDouble(tokens, VISA_INT_FEE_AMOUNT, 2));
        record.setVisaAssessmentFee(getSignedDouble(tokens, VISA_ASS_FEE_AMOUNT, 2));
        record.setVisaTransactionFee(getSignedDouble(tokens, VISA_TRAN_FEE_AMOUNT, 2));
        record.setMCInterchangeFee(getSignedDouble(tokens, MC_INT_FEE_AMOUNT, 2));
        record.setMCAssessmentFee(getSignedDouble(tokens, MC_ASS_FEE_AMOUNT, 2));
        record.setMCTransactionFee(getSignedDouble(tokens, MC_TRAN_FEE_AMOUNT, 2));
        
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