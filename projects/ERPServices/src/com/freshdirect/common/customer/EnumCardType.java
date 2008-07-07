/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.common.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.ValuedEnum;

import com.freshdirect.payment.EnumPaymentMethodType;

/**
 * Type-safe enumeration for credit card types.
 *
 * @version $Revision$
 * @author $Author$
 */
public class EnumCardType extends ValuedEnum {
	
	private static final Map CARDS_BY_SETTLEMENT_CODE = new HashMap();
	private static final Map CARDS_BY_PAYMENTECH_CODE = new HashMap();

	public final static EnumCardType AMEX = new EnumCardType(0, "AMEX", "AMEX", "003", "A","Amex", "AX", EnumPaymentMethodType.CREDITCARD);
	public final static EnumCardType MC = new EnumCardType(1, "MC", "MC", "002", "M", "MasterCard", "MC", EnumPaymentMethodType.CREDITCARD);
	public final static EnumCardType VISA = new EnumCardType(2, "VISA", "VISA", "001", "V", "Visa", "VI", EnumPaymentMethodType.CREDITCARD);
	public final static EnumCardType DISC = new EnumCardType(3, "DISC", "DISC", "004", "S", "Discover", "DI", EnumPaymentMethodType.CREDITCARD);
	public final static EnumCardType ECP = new EnumCardType(4, "ECP", "ECP", "005", "C", "ECheck", "EC", EnumPaymentMethodType.ECHECK);
	
	private EnumCardType(int id, String fdName, String sapName, String paylinxId, String settlementCode, String displayName, String paymentechCode, EnumPaymentMethodType type) {
		super(fdName, id);
		this.sapName=sapName;
		this.paylinxId = paylinxId;
		this.settlementCode = settlementCode;
		this.displayName = displayName;
		this.paymentechCode = paymentechCode;
		this.type = type;
		
		CARDS_BY_SETTLEMENT_CODE.put(settlementCode, this);
		CARDS_BY_PAYMENTECH_CODE.put(paymentechCode, this);
	}
	
	
	private final String sapName;
	private final String paylinxId;
	private final String settlementCode;
	private final String displayName;
	private final String paymentechCode;
	private final EnumPaymentMethodType type;
	
	//this method is only there for backward compatibility
	
	public static EnumCardType getCardType(String code) {
		return getEnum(code);
	}
	
	public static EnumCardType getEnum(String code) {
		return (EnumCardType) getEnum(EnumCardType.class, code);
	}

	public static EnumCardType getEnum(int id) {
		return (EnumCardType) getEnum(EnumCardType.class, id);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumCardType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumCardType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumCardType.class);
	}
	
	public static EnumCardType getBySettlementCode(String settlementCode){
		return (EnumCardType) CARDS_BY_SETTLEMENT_CODE.get(settlementCode);
	}
	
	public static EnumCardType getByPaymentechCode(String paymentechCode) {
		return (EnumCardType) CARDS_BY_PAYMENTECH_CODE.get(paymentechCode);
	}
	
	public static List getCardTypes() {
		List cards = new ArrayList();
		for(Iterator i = iterator(); i.hasNext(); ) {
			EnumCardType cType = (EnumCardType) i.next();
			if(EnumPaymentMethodType.CREDITCARD.equals(cType.getPaymentMethodType())){
				cards.add(cType);
			}
		}
		return cards;
	}

	public String getFdName() {
		return this.getName();
	}

	public String getSapName() {
		return this.sapName;
	}
	
	public String getPaylinxId(){
		return this.paylinxId;
	}
	
	public String getSettlementCode(){
		return this.settlementCode;
	}

	public String getDisplayName(){
		return this.displayName;
	}
	
	public String toString() {
		return this.getFdName();
	}
	
	public String getPaymentechCode() {
		return this.paymentechCode;
	}
	
	public EnumPaymentMethodType getPaymentMethodType(){
		return this.type;
	}
	
}