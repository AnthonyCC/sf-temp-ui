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
public class AmexJCBUnbundledParser extends SettlementParser {
	
	private SynchronousParserClient client = null;
    
    public final static String UNBUNDLED_OPTION         = "UNBUNDLED_OPTION";
    public final static String AMEX_INT_FEE_AMOUNT      = "AMEX_INT_FEE_AMOUNT";
    public final static String AMEX_ASS_FEE_AMOUNT      = "AMEX_ASS_FEE_AMOUNT";
    public final static String AMEX_TRAN_FEE_AMOUNT     = "AMEX_TRAN_FEE_AMOUNT";
    public final static String JCB_INT_FEE_AMOUNT       = "JCB_INT_FEE_AMOUNT";
    public final static String JCB_ASS_FEE_AMOUNT       = "JCB_ASS_FEE_AMOUNT";
    public final static String JCB_TRAN_FEE_AMOUNT      = "JCB_TRAN_FEE_AMOUNT";
    
    private AmexJCBUnbundled record = null;
    
    /** Creates new ReconciliationParser */
    public AmexJCBUnbundledParser() {
        super();
        
        fields.add(new Field(UNBUNDLED_OPTION,          1, true));
        fields.add(new Field(AMEX_INT_FEE_AMOUNT,       7, true));
        fields.add(new Field(AMEX_ASS_FEE_AMOUNT,       7, true));
        fields.add(new Field(AMEX_TRAN_FEE_AMOUNT,      7, true));
        fields.add(new Field(JCB_INT_FEE_AMOUNT,        7, true));
        fields.add(new Field(JCB_ASS_FEE_AMOUNT,        7, true));
        fields.add(new Field(JCB_TRAN_FEE_AMOUNT,       7, true));
        
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
        record = new AmexJCBUnbundled();
        record.setUnbundledOption(getString(tokens, UNBUNDLED_OPTION));
        record.setAmexInterchangeFee(getSignedDouble(tokens, AMEX_INT_FEE_AMOUNT, 2));
        record.setAmexAssessmentFee(getSignedDouble(tokens, AMEX_ASS_FEE_AMOUNT, 2));
        record.setAmexTransactionFee(getSignedDouble(tokens, AMEX_TRAN_FEE_AMOUNT, 2));
        record.setJCBInterchangeFee(getSignedDouble(tokens, JCB_INT_FEE_AMOUNT, 2));
        record.setJCBAssessmentFee(getSignedDouble(tokens, JCB_ASS_FEE_AMOUNT, 2));
        record.setJCBTransactionFee(getSignedDouble(tokens, JCB_TRAN_FEE_AMOUNT, 2));
        
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