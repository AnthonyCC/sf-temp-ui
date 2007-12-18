/*
 * EnumTransactionType.java
 *
 * Created on November 15, 2001, 10:38 PM
 */

package com.freshdirect.customer;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author  mrose
 * @version
 */
public class EnumTransactionType implements java.io.Serializable {
	
	private final static Map CODE_MAP = new HashMap();

    public final static EnumTransactionType CREATE_ORDER			= new EnumTransactionType(0, "CRO", "Create Order");
    public final static EnumTransactionType MODIFY_ORDER			= new EnumTransactionType(1, "MOD", "Modify Order");
    public final static EnumTransactionType CANCEL_ORDER			= new EnumTransactionType(2, "CAO", "Cancel Order");
    public final static EnumTransactionType SUBMIT_FAILED		= new EnumTransactionType(3, "SMF", "Submit Failed");
    public final static EnumTransactionType INVOICE				= new EnumTransactionType(4, "INV", "Invoice");
    public final static EnumTransactionType PAYMENT				= new EnumTransactionType(5, "PAY", "Payment");
    public final static EnumTransactionType RETURN_ORDER			= new EnumTransactionType(6, "RET", "Return");
    public final static EnumTransactionType CREDIT				= new EnumTransactionType(7, "CRE", "Credit");
    public final static EnumTransactionType CHARGE				= new EnumTransactionType(8, "CHG", "Charge");
    public final static EnumTransactionType ADD_COMPLAINT		= new EnumTransactionType(9, "COM", "Add Complaint");
	public final static EnumTransactionType AUTHORIZATION		= new EnumTransactionType(10, "AUT", "Authorization");
	public final static EnumTransactionType CAPTURE				= new EnumTransactionType(11, "CAP", "CAPTURE");
	public final static EnumTransactionType FAILED_AUTHORIZATION	= new EnumTransactionType(12, "AUF", "Failed Authorization");
	public final static EnumTransactionType REVERSAL				= new EnumTransactionType(13, "REV", "Reversal");
	public final static EnumTransactionType CASHBACK				= new EnumTransactionType(14, "CAK", "Cash Back");
	public final static EnumTransactionType SETTLEMENT			= new EnumTransactionType(15, "STL", "Settlement");
	public final static EnumTransactionType CHARGEBACK 			= new EnumTransactionType(16, "CBK", "Chargeback");
	public final static EnumTransactionType ADJUSTMENT 			= new EnumTransactionType(17, "ADJ", "Adjustment");
	public final static EnumTransactionType RESUBMIT_PAYMENT 	= new EnumTransactionType(18, "RSP", "Resubmit Payment");
	public final static EnumTransactionType RETURN_INVOICE 		= new EnumTransactionType(19, "RIN", "Return Invoice");
	public final static EnumTransactionType REDELIVERY 			= new EnumTransactionType(20, "RED", "Redelivery");
	public final static EnumTransactionType VOID_CAPTURE			= new EnumTransactionType(21, "VDC", "Void Capture");
	public final static EnumTransactionType CHARGEBACK_REVERSAL 	= new EnumTransactionType(22, "CBR", "Chargeback Reversal");
	public final static EnumTransactionType SETTLEMENT_FAILED		= new EnumTransactionType(23, "STF", "Settlement Failed");
	public final static EnumTransactionType INVOICE_CHARGE		= new EnumTransactionType(24, "INC", "Invoice Charge");
	public final static EnumTransactionType SETTLEMENT_CHARGE		= new EnumTransactionType(25, "STC", "Settlement Charge");
	public final static EnumTransactionType FUNDS_REDEPOSIT			= new EnumTransactionType(26, "FRD", "Funds Redeposit");
	public final static EnumTransactionType SETTLEMENT_CHARGE_FAILED	= new EnumTransactionType(27, "SCF", "Settlement Charge Failed");
	public final static EnumTransactionType DELIVERY_CONFIRM	= new EnumTransactionType(28, "DLC", "delivery confirmed");

    private EnumTransactionType(int id, String code, String name) {
		this.id = id;
		this.code = code;
        this.name = name;
        
        CODE_MAP.put( this.code, this );
    }

	public String getCode(){
		return this.code;
	}

    public String getName() {
        return this.name;
    }
    
    public static EnumTransactionType getTransactionType(String code){
		return (EnumTransactionType) CODE_MAP.get( code.toUpperCase() );
	}

   	public boolean equals(Object o) {
		if (o instanceof EnumTransactionType) {
			return this.id == ((EnumTransactionType)o).id;
		}
		return false;
	}

	private final int id;
	private String code;
    private final String name;

}
