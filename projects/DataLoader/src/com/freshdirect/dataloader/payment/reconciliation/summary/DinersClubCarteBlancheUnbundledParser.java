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
import com.freshdirect.payment.reconciliation.summary.DinersClubCarteBlancheUnbundled;

/**
 *
 * @author  mrose
 * @version
 */
public class DinersClubCarteBlancheUnbundledParser extends SettlementParser {
	
	private SynchronousParserClient client = null;
    
    public final static String UNBUNDLED_OPTION       = "UNBUNDLED_OPTION";
    public final static String DC_INT_FEE_AMOUNT      = "DC_INT_FEE_AMOUNT";
    public final static String DC_ASS_FEE_AMOUNT      = "DC_ASS_FEE_AMOUNT";
    public final static String DC_TRAN_FEE_AMOUNT     = "DC_TRAN_FEE_AMOUNT";
    public final static String CB_INT_FEE_AMOUNT      = "CB_INT_FEE_AMOUNT";
    public final static String CB_ASS_FEE_AMOUNT      = "CB_ASS_FEE_AMOUNT";
    public final static String CB_TRAN_FEE_AMOUNT     = "CB_TRAN_FEE_AMOUNT";
    
    private DinersClubCarteBlancheUnbundled record = null;
    
    /** Creates new ReconciliationParser */
    public DinersClubCarteBlancheUnbundledParser() {
        super();
        
        fields.add(new Field(UNBUNDLED_OPTION,            1, true));
        fields.add(new Field(DC_INT_FEE_AMOUNT,           7, true));
        fields.add(new Field(DC_ASS_FEE_AMOUNT,           7, true));
        fields.add(new Field(DC_TRAN_FEE_AMOUNT,          7, true));
        fields.add(new Field(CB_INT_FEE_AMOUNT,           7, true));
        fields.add(new Field(CB_ASS_FEE_AMOUNT,           7, true));
        fields.add(new Field(CB_TRAN_FEE_AMOUNT,          7, true));
        
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
        record = new DinersClubCarteBlancheUnbundled();
        record.setUnbundledOption(getString(tokens, UNBUNDLED_OPTION));
        record.setDinersClubInterchangeFee(getSignedDouble(tokens, DC_INT_FEE_AMOUNT, 2));
        record.setDinersClubAssessmentFee(getSignedDouble(tokens, DC_ASS_FEE_AMOUNT, 2));
        record.setDinersClubTransactionFee(getSignedDouble(tokens, DC_TRAN_FEE_AMOUNT, 2));
        record.setCarteBlancheInterchangeFee(getSignedDouble(tokens, CB_INT_FEE_AMOUNT, 2));
        record.setCarteBlancheAssessmentFee(getSignedDouble(tokens, CB_ASS_FEE_AMOUNT, 2));
        record.setCarteBlancheTransactionFee(getSignedDouble(tokens, CB_TRAN_FEE_AMOUNT, 2));
        
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