package com.freshdirect.dataloader.payment.reconciliation.paymentech.parsers;

import java.util.Map;

import com.freshdirect.dataloader.BadDataException;
import com.freshdirect.dataloader.PipeDelimitedFileParser;
import com.freshdirect.dataloader.SynchronousParser;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.EnumPaymentechCategory;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.EnumPaymentechRecordType;
import com.freshdirect.dataloader.payment.reconciliation.paymentech.EnumPaymentechSubCategory;
import com.freshdirect.framework.util.TimeOfDay;

public abstract class PaymentechSettlementParser extends PipeDelimitedFileParser implements SynchronousParser {
	
	public static String COMPANY_ID = "COMPANY_ID";
	public static String RECORD_TYPE = "RECORD_TYPE";
	public static String FROM_DATE = "FROM_DATE";
	public static String TO_DATE = "TO_DATE";
	public static String GENERATION_DATE = "GENERATION_DATE";
	public static String GENERATION_TIME = "GENERATION_TIME";
	
	public static String ENTITY_TYPE = "ENTITY_TYPE";
	public static String ENTITY_NUMBER = "ENTITY_NUMBER";
	//Funds Transfer Instruction number if it is null -> funds are not transfered yet
	public static String FUNDS_TRANSFER_INS_NUM = "FUNDS_TRANSFER_INS_NUM";
	//Paymentech assigned secure bank account number null -> funds are not tranfered yet
	public static String SECURE_BA_NUM = "SECURE_BA_NUM";
	public static String CURRENCY = "CURRENCY";
	// 2 letter code for payment method will be null for any category other than SALE or REF
	public static String MOP = "MOP";
	// Type of activity
	public static String CATEGORY = "CATEGORY";
	// Settled or Conveyed valid values S or C
	public static String SETTLED_OR_CONVEYED = "SETTLED_OR_CONVEYED";
	// Number of items reported, will only have value for SALE and REF
	public static String COUNT = "COUNT";
	// Value of the number of items included.
	public static String AMOUNT = "AMOUNT";
	
	public TimeOfDay getTime(Map<String, String> tokens, String fieldName, String format) throws BadDataException{
		String s = this.getString(tokens, fieldName);
		return new TimeOfDay(s, format);
	}
	
	public EnumPaymentechRecordType getRecordType(Map<String, String> tokens, String fieldName) throws BadDataException {
		String s = this.getString(tokens, fieldName);
		EnumPaymentechRecordType type = EnumPaymentechRecordType.getEnum(s);
		if(type == null) {
			throw new BadDataException("BAD RECORD TYPE: " +s);
		}
		return type;
	}
	
	public EnumPaymentechCategory getCategory(Map<String, String> tokens, String fieldName) throws BadDataException {
		String s = this.getString(tokens, fieldName);
		EnumPaymentechCategory type = EnumPaymentechCategory.getEnum(s);
		if(type == null) {
			throw new BadDataException("BAD CATEGORY CODE: " +s);
		}
		return type;
	}
	
	public EnumPaymentechSubCategory getSubCategory(Map<String, String> tokens, String fieldName) throws BadDataException {
		String s = this.getString(tokens, fieldName);
		EnumPaymentechSubCategory type = EnumPaymentechSubCategory.getEnum(s);
		if(type == null) {
			throw new BadDataException("BAD SUB CATEGORY CODE: " +s);
		}
		return type;
	}
	
	public String getMerchantReferenceNumber(Map<String, String> tokens, String fieldName) throws BadDataException {
		String saleId = unQuoteString(getString(tokens, fieldName));
		if(saleId.indexOf('X') > 0) {
			saleId = saleId.substring(0, saleId.indexOf('X'));
		}
		return saleId;
	}
	
	public String unQuoteString(String s){
		if(s == null || s.length() < 2){
			return s;
		}
		s = s.substring(1);
		return s.substring(0, s.length()-1);
	}
}
