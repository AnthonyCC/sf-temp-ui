package com.freshdirect.affiliate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.affiliate.dao.ErpAffiliateDAO;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.enum.EnumModel;

/**
 * EnumModel representing an affiliated company. 
 */
public class ErpAffiliate extends EnumModel {

	private static Map enums = null;

	public static final String CODE_FD = "FD";
	public static final String CODE_WBL = "WBL";
	public static final String CODE_BC = "BC";
	public static final String CODE_USQ = "USQ";

	private final String depositConditionType;
	private final String taxConditionType;
	private final Map merchants; //PaymentType (VISA, MC, ECHECK, AMEX and DISC)-> marchant
	private final Set paymentechTxDivisions;

	public ErpAffiliate(String code, String name, String description, String taxConditionType, String depositConditionType, Map merchants, Set paymentechTxDivisions) {
		super(code, name, description);
		this.depositConditionType = depositConditionType;
		this.taxConditionType = taxConditionType;
		this.merchants = Collections.unmodifiableMap(merchants);
		this.paymentechTxDivisions = Collections.unmodifiableSet(paymentechTxDivisions);
	}

	public String getDepositConditionType() {
		return depositConditionType;
	}

	public String getTaxConditionType() {
		return taxConditionType;
	}

	public boolean isPrimary() {
		return this.getCode().equals(CODE_FD);
	}
	
	public String getMerchant(EnumCardType cardType) {
		return (String) this.merchants.get(cardType);
	}
	
	public String getPayementechTxDivision() {
		return this.paymentechTxDivisions.isEmpty() ? "" : (String)this.paymentechTxDivisions.iterator().next();
	}
	
	public static ErpAffiliate getAffiliateByTxDivision(String division){
		loadEnums();
		for(Iterator i = enums.values().iterator(); i.hasNext(); ) {
			ErpAffiliate aff = (ErpAffiliate) i.next();
			if(aff.paymentechTxDivisions.contains(division)){
				return aff;
			}
		}
		
		return getPrimaryAffiliate();
	}
	
	public static ErpAffiliate getAffiliateByMerchant(EnumCardType paymentType, String merchant) {
		loadEnums();
		for(Iterator i = enums.values().iterator(); i.hasNext(); ) {
			ErpAffiliate aff = (ErpAffiliate) i.next();
			if(aff.merchants.containsKey(paymentType) && aff.merchants.containsValue(merchant)) {
				return aff;
			}
		}
		
		return getPrimaryAffiliate();
	}
	
	public Map getMerchants() {
		return this.merchants;
	}
	
	public static ErpAffiliate getPrimaryAffiliate() {
		loadEnums();
		return (ErpAffiliate) enums.get(CODE_FD);
	}

	public static ErpAffiliate getEnum(String code) {
		loadEnums();
		return (ErpAffiliate) enums.get(code);
	}

	public static Map getEnumMap() {
		loadEnums();
		return Collections.unmodifiableMap(enums);
	}

	public static List getEnumList() {
		loadEnums();
		return Collections.unmodifiableList(new ArrayList(enums.values()));
	}

	private static void loadEnums() {
		if (enums == null) {
			enums = new HashMap();
			List lst = loadEnums(ErpAffiliateDAO.class);
			for (Iterator i = lst.iterator(); i.hasNext();) {
				ErpAffiliate e = (ErpAffiliate) i.next();
				enums.put(e.getCode(), e);
			}
		}
	}

}
