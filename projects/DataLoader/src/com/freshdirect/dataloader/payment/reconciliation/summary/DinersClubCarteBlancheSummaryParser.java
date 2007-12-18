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
public class DinersClubCarteBlancheSummaryParser extends SettlementParser {
	
	private SynchronousParserClient client = null;
    
    public final static String NUMBER_DC_ITEMS             = "NUMBER_DC_ITEMS";
    public final static String DC_NET_SALES_AMOUNT         = "DC_NET_SALES_AMOUNT";
    public final static String DC_FDMS_DISCOUNT_AMOUNT     = "DC_FDMS_DISCOUNT_AMOUNT";
    public final static String DC_ISSUER_DISCOUNT_AMOUNT   = "DC_ISSUER_DISCOUNT_AMOUNT";
    public final static String NUMBER_CB_ITEMS             = "NUMBER_CB_ITEMS";
    public final static String CB_NET_SALES_AMOUNT         = "CB_NET_SALES_AMOUNT";
    public final static String CB_FDMS_DISCOUNT_AMOUNT     = "CB_FDMS_DISCOUNT_AMOUNT";
    public final static String CB_ISSUER_DISCOUNT_AMOUNT   = "CB_ISSUER_DISCOUNT_AMOUNT";
    
    private DinersClubCarteBlancheSummary summary = null;
    
    /** Creates new ReconciliationParser */
    public DinersClubCarteBlancheSummaryParser() {
        super();
        
        fields.add(new Field(NUMBER_DC_ITEMS,              3, true));
        fields.add(new Field(DC_NET_SALES_AMOUNT,          7, true));
        fields.add(new Field(DC_FDMS_DISCOUNT_AMOUNT,      7, true));
        fields.add(new Field(DC_ISSUER_DISCOUNT_AMOUNT,    7, true));
        fields.add(new Field(NUMBER_CB_ITEMS,              3, true));
        fields.add(new Field(CB_NET_SALES_AMOUNT,          7, true));
        fields.add(new Field(CB_FDMS_DISCOUNT_AMOUNT,      7, true));
        fields.add(new Field(CB_ISSUER_DISCOUNT_AMOUNT,    7, true));
        
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
        summary = new DinersClubCarteBlancheSummary();
        summary.setNumberOfDinersClubItems(getInt(tokens, NUMBER_DC_ITEMS));
        summary.setDinersClubNetSalesAmount(getSignedDouble(tokens, DC_NET_SALES_AMOUNT, 2));
        summary.setDinersClubFDMSDiscountAmount(getSignedDouble(tokens, DC_FDMS_DISCOUNT_AMOUNT, 2));
        summary.setDinersClubIssuerDiscountAmount(getSignedDouble(tokens, DC_ISSUER_DISCOUNT_AMOUNT, 2));
        summary.setNumberOfCarteBlancheItems(getInt(tokens, NUMBER_CB_ITEMS));
        summary.setCarteBlancheNetSalesAmount(getSignedDouble(tokens, CB_NET_SALES_AMOUNT, 2));
        summary.setCarteBlancheFDMSDiscountAmount(getSignedDouble(tokens, CB_FDMS_DISCOUNT_AMOUNT, 2));
        summary.setCarteBlancheIssuerDiscountAmount(getSignedDouble(tokens, CB_ISSUER_DISCOUNT_AMOUNT, 2));
        
        client.accept(summary);
    }
    
    public String debug() {
        return this.summary.toString();
    }
    
    public SynchronousParserClient getClient(){
    	return this.client;
    }
    
    public void setClient(SynchronousParserClient client){
    	this.client = client;
    }
}