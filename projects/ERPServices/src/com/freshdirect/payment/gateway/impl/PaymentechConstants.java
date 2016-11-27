package com.freshdirect.payment.gateway.impl;

import java.util.HashMap;
import java.util.Map;

import com.freshdirect.payment.gateway.CreditCardType;
import com.freshdirect.payment.gateway.Currency;
import com.freshdirect.payment.gateway.Merchant;
import com.freshdirect.payment.gateway.PaymentMethodType;
import com.freshdirect.payment.gateway.TransactionType;

final class PaymentechConstants implements java.io.Serializable {
	
	public static final String SUCCESS="0";
	public static final String TERMINAL_ID = "001";
	public static final String CURRENCY_EXPONENT = "2";
	public static final String[] VALID_ZIP_CHECK_RESPONSES={"9","A","B","C","H","JA","JD","M2","M5","N5","N7","N8","N9","X","Z"};
	public static final String CVV_MATCH="M";
	public static final String VOID_INDICATOR="N";
	public static final String REVERSE_AUTH_INDICATOR="Y";
	public static final String[] COUNTRY_CODES={"US","UK","GB","CA"};
	public static final String MASTERPASS = "MasterPass";
	public static final String MPWALLETID ="1";
	public static final String DWSLI="4";
	public static final String ECPAUTHMETHOD = "I";
	public static final String BANKPMTDELV = "B";
	
	private PaymentechConstants() {
	}

	static enum CurrencyCode implements java.io.Serializable {
		USD(Currency.USD, "840");
		
		private Currency id;
	    private String value;
	    private static Map<Currency, CurrencyCode> currencyCodes;
	    static {
	    	init();
	    }
	    private static void init() {
	    	currencyCodes = new HashMap<Currency, CurrencyCode>();
	        for (CurrencyCode s : values()) {
	        	currencyCodes.put(s.id, s);
	        }
	    }
	    
	    private CurrencyCode(Currency  id, String value) {
	        this.id = id;
	        this.value = value;
	    }
	     public String toString() {
	        final StringBuilder sb = new StringBuilder();
	        sb.append(this.getClass().getName());
	        sb.append("{id=").append(id.name());
	        sb.append(", value='").append(value).append('\'');
	        sb.append('}');
	        return sb.toString();
	    }
	     Currency getID() {
	        return id;
	    }
	 
	     String getValue() {
	        return value;
	    }
	     static CurrencyCode get(Currency  id) {
	        
	     	return currencyCodes.get(id);
	     }
	} 
	
	  static enum AccountType implements java.io.Serializable {
		CREDIT_CARD("CC",PaymentMethodType.CREDIT_CARD.name()),
		ECHECK("EC",PaymentMethodType.ECHECK.name());
		private String code;
	    private String name;
	    private AccountType(String  code, String name) {
	        this.code = code;
	        this.name = name;
	    }
	     public String toString() {
	        final StringBuilder sb = new StringBuilder();
	        sb.append(this.getClass().getName());
	        sb.append("{code=").append(code);
	        sb.append(", name='").append(name).append('\'');
	        sb.append('}');
	        return sb.toString();
	    }
	     String getCode() {
	        return code;
	    }
	 
	     String getName() {
	        return name;
	    }
	}
	 
	 static enum MerchantID implements java.io.Serializable {
			FRESHDIRECT(Merchant.FRESHDIRECT,"087991"),
			USQ(Merchant.USQ,"105944"),
			FDW(Merchant.FDW,"238313"),
			FDX(Merchant.FDX,"259597");
			private Merchant id;
		    private String value;
		    private static Map<Merchant, MerchantID> merchantIDs;
		    static {
		    	init();
		    }
		    private static void init() {
		    	merchantIDs = new HashMap<Merchant, MerchantID>();
		        for (MerchantID s : values()) {
		        	merchantIDs.put(s.id, s);
		        }
		    }
		    
		    private MerchantID(Merchant  id, String value) {
		        this.id = id;
		        this.value = value;
		    }
		     public String toString() {
		        final StringBuilder sb = new StringBuilder();
		        sb.append(this.getClass().getName());
		        sb.append("{id=").append(id.name());
		        sb.append(", value='").append(value).append('\'');
		        sb.append('}');
		        return sb.toString();
		    }
		     Merchant getID() {
		        return id;
		    }
		 
		     String getValue() {
		        return value;
		    }
		     public static MerchantID get(Merchant  id) {
		        
		     	return merchantIDs.get(id);
		     }
		} 
	
	 static enum BIN implements java.io.Serializable {
		  SALEM("000001","Salem Platform"),
		;
		private String code;
	    private String name;
	    private BIN(String  code, String name) {
	        this.code = code;
	        this.name = name;
	    }
	     public String toString() {
	        final StringBuilder sb = new StringBuilder();
	        sb.append(this.getClass().getName());
	        sb.append("{code=").append(code);
	        sb.append(", name='").append(name).append('\'');
	        sb.append('}');
	        return sb.toString();
	    }
	     String getCode() {
	        return code;
	    }
	 
	     String getName() {
	        return name;
	    }
	}
	 
	static enum MessageType implements java.io.Serializable {
		AUTH("A","Authorization"),
		AUTH_AND_CAPTURE("AC","Authorization and Mark for Capture"),
		FORCE_CAPTURE("FC","Force Capture"),
		REFUND("R","Refund");
		private String code;
	    private String description;
		
		private MessageType(String  code, String description) {
	        this.code = code;
	        this.description = description;
	    }
		 String getCode() {
		     return code;
		 }
		 
		 String getDescription() {
		     return description;
		 }
	}
	static enum ProfileAction implements java.io.Serializable {
		CREATE("C",TransactionType.ADD_PROFILE.name()),
		UPDATE("U",TransactionType.UPDATE_PROFILE.name()),
		DELETE("D",TransactionType.DELETE_PROFILE.name()),
		RETRIEVE("R",TransactionType.GET_PROFILE.name());
		
		 interface Fields {
			String MERCHANT_ID="CustomerMerchantID";
		}
		private String code;
	    private String description;
	    private ProfileAction(String  code, String description) {
	        this.code = code;
	        this.description = description;
	    }
	     public String toString() {
	        final StringBuilder sb = new StringBuilder();
	        sb.append(this.getClass().getName());
	        sb.append("{code=").append(code);
	        sb.append(", description='").append(description).append('\'');
	        sb.append('}');
	        return sb.toString();
	    }
	     String getCode() {
	        return code;
	    }
	 
	     String getDescription() {
	        return description;
	    }
	}
	static enum IndustryType implements java.io.Serializable {
		MAIL_ORDER("MO", "Mail Order transaction"),
	    RECURRING_PAYMENT("RC", "Recurring Payment"),
		ECOMMERCE("EC","eCommerce transaction"),
		PINLESS_DEBIT("IV","IVR[Pinless Debit Only");
	    private String code;
	    private String description;
	    private IndustryType(String  code, String description) {
	        this.code = code;
	        this.description = description;
	    }
	     public String toString() {
	        final StringBuilder sb = new StringBuilder();
	        sb.append(this.getClass().getName());
	        sb.append("{code=").append(code);
	        sb.append(", description='").append(description).append('\'');
	        sb.append('}');
	        return sb.toString();
	    }
	     String getCode() {
		        return code;
		 }
		 
		 String getDescription() {
		        return description;
		   }
	}
	
	static enum BankAccountType implements java.io.Serializable {
		CONSUMER_CHECKING( com.freshdirect.payment.gateway.BankAccountType.CHECKING,"C"),
		CONSUMER_SAVINGS( com.freshdirect.payment.gateway.BankAccountType.SAVINGS,"S");
		//COMMERCIAL_CHECKING("C", "Commercial Checking Account"),
	    private String code;
	    private com.freshdirect.payment.gateway.BankAccountType type;
	    private static Map<com.freshdirect.payment.gateway.BankAccountType, BankAccountType> accountTypes;
	    static {
	    	init();
	    }
	    private static void init() {
	    	accountTypes = new HashMap<com.freshdirect.payment.gateway.BankAccountType, BankAccountType>();
	        for (BankAccountType s : values()) {
	        	accountTypes.put(s.type, s);
	        }
	    }
	    private BankAccountType(com.freshdirect.payment.gateway.BankAccountType type,String  code) {
	        this.type = type;
	        this.code = code;
	    }
	     public String toString() {
	        final StringBuilder sb = new StringBuilder();
	        sb.append(this.getClass().getName());
	        sb.append("{code=").append(code);
	        sb.append(", type='").append(type).append('\'');
	        sb.append('}');
	        return sb.toString();
	    }
	     String getCode() {
		        return code;
		 }
		 
	     com.freshdirect.payment.gateway.BankAccountType getType() {
		        return type;
		 }
	      static BankAccountType get(com.freshdirect.payment.gateway.BankAccountType type) {
		        
		     	return accountTypes.get(type);
		  }
	}
	
	static enum CardType implements java.io.Serializable {
		VISA( CreditCardType.VISA,"VI"),
		MASTERCARD( CreditCardType.MASTERCARD,"MC"),
		AMEX( CreditCardType.AMEX,"AX"),
		DISCOVER( CreditCardType.DISCOVER,"DI");
		
	    private String code;
	    private CreditCardType type;
	    private static Map<CreditCardType, CardType> accountTypes;
	    static {
	    	init();
	    }
	    private static void init() {
	    	accountTypes = new HashMap<CreditCardType, CardType>();
	        for (CardType s : values()) {
	        	accountTypes.put(s.type, s);
	        }
	    }
	    private CardType(CreditCardType type,String  code) {
	        this.type = type;
	        this.code = code;
	    }
	     public String toString() {
	        final StringBuilder sb = new StringBuilder();
	        sb.append(this.getClass().getName());
	        sb.append("{code=").append(code);
	        sb.append(", type='").append(type).append('\'');
	        sb.append('}');
	        return sb.toString();
	    }
	     String getCode() {
		        return code;
		 }
		 
	     CreditCardType getType() {
		        return type;
		 }
	      static CardType get(CreditCardType type) {
		        
		     	return accountTypes.get(type);
		  }
	}
	
	static enum ECheckPymtDelivery implements java.io.Serializable {
		DEFAULT("B", "Best Possible Method/ Default"),
	    ACH("A", "ACH");
		
	    private String code;
	    private String description;
	    private ECheckPymtDelivery(String  code, String description) {
	        this.code = code;
	        this.description = description;
	    }
	     public String toString() {
	        final StringBuilder sb = new StringBuilder();
	        sb.append(this.getClass().getName());
	        sb.append("{code=").append(code);
	        sb.append(", description='").append(description).append('\'');
	        sb.append('}');
	        return sb.toString();
	    }
	    String getCode() {
		        return code;
		}
		String getDescription() {
		        return description;
		 }	     
	}
	
}
