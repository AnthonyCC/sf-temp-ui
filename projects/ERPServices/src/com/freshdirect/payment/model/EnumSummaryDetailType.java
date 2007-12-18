package com.freshdirect.payment.model;

import java.io.Serializable;

/**
 *
 * @author  knadeem
 * @version
 */

public class EnumSummaryDetailType implements Serializable {
	
	public final static EnumSummaryDetailType VISA          		= new EnumSummaryDetailType(1, "V", "Visa", "VI");
	public final static EnumSummaryDetailType MASTERCARD      	= new EnumSummaryDetailType(2, "M", "MasterCard", "MC");
	public final static EnumSummaryDetailType AMERICAN_EXPRESS    = new EnumSummaryDetailType(3, "A", "American Express", "AX");
	public final static EnumSummaryDetailType DINERS_CLUB         = new EnumSummaryDetailType(4, "D", "Diners Club", "DC");
	public final static EnumSummaryDetailType CARTE_BLANCHE       = new EnumSummaryDetailType(5, "C", "Carte Blanche", "CB");
	public final static EnumSummaryDetailType NOVUS          	= new EnumSummaryDetailType(6, "S", "Novus", "DI");
	public final static EnumSummaryDetailType JCB		        = new EnumSummaryDetailType(7, "J", "JCB", "JC");
	
	private EnumSummaryDetailType(int id, String code, String name, String paymentechCode){
		this.id = id;
		this.code = code;
		this.name = name;
		this.paymentechCode = paymentechCode;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public String getName(){
		return this.name;
	}
	
	public boolean equals(Object o) {
		if (o instanceof EnumSummaryDetailType) {
			return this.id == ((EnumSummaryDetailType)o).id;
		}
		return false;
	}
	
	public static EnumSummaryDetailType getSummaryDetail(String code){
		
		if(VISA.getCode().equalsIgnoreCase(code)){
			return VISA;
		}else if(MASTERCARD.getCode().equalsIgnoreCase(code)){
			return MASTERCARD;	
		}else if(AMERICAN_EXPRESS.getCode().equalsIgnoreCase(code)){
			return AMERICAN_EXPRESS;	
		}else if(DINERS_CLUB.getCode().equalsIgnoreCase(code)){
			return DINERS_CLUB;	
		}else if(CARTE_BLANCHE.getCode().equalsIgnoreCase(code)){
			return CARTE_BLANCHE;	
		}else if(NOVUS.getCode().equalsIgnoreCase(code)){
			return NOVUS;	
		}else if(JCB.getCode().equalsIgnoreCase(code)){
			return JCB;	
		}else{
			return null;
		}
	}
	
	public static EnumSummaryDetailType getByPaymentechCode(String code) {
		if(VISA.paymentechCode.equals(code)){
			return VISA;
		}else if(MASTERCARD.paymentechCode.equals(code)){
			return MASTERCARD;
		}else if(AMERICAN_EXPRESS.paymentechCode.equals(code)){
			return AMERICAN_EXPRESS;
		}else if(DINERS_CLUB.paymentechCode.equals(code)){
			return DINERS_CLUB;
		}else if(CARTE_BLANCHE.paymentechCode.equals(code)){
			return CARTE_BLANCHE;
		}else if(NOVUS.paymentechCode.equals(code)){
			return NOVUS;
		}else if(JCB.paymentechCode.equals(code)){
			return JCB;
		}else{
			return null;
		}
	}
	
	private final int id;
	private final String code;
	private final String name;
	private final String paymentechCode;

}
