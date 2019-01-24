package com.freshdirect.affiliate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.freshdirect.affiliate.dao.ErpAffiliateDAO;
import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.enums.EnumModel;
import com.freshdirect.fdstore.EnumEStoreId;

/**
 * EnumModel representing an affiliated company. 
 */
public class ErpAffiliate extends EnumModel {

	private static final long serialVersionUID = -8959977127099286821L;

	private static Map<String, ErpAffiliate> enums = null;

	public static final String CODE_FD = "FD";
	public static final String CODE_WBL = "WBL";
	public static final String CODE_BC = "BC";
	public static final String CODE_USQ = "USQ";
	public static final String CODE_FDW = "FDW";
	public static final String CODE_FDX = "FDX";

	private final String depositConditionType;
	private final String taxConditionType;
	private final Map<EnumCardType, String> merchants; //PaymentType (VISA, MC, ECHECK, AMEX and DISC)-> marchant
	@JsonProperty("paymentechTxDivisions")
	private final Set<String> paymentechTxDivisions;

	public ErpAffiliate(@JsonProperty("code") String code, @JsonProperty("name") String name,
			@JsonProperty("description") String description, @JsonProperty("taxConditionType") String taxConditionType,
			@JsonProperty("depositConditionType") String depositConditionType,
			@JsonProperty("merchants") Map<EnumCardType, String> merchants, 
			@JsonProperty("paymentechTxDivisions") Set<String> paymentechTxDivisions ) {
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
		return CODE_FD.equals(this.getCode())||CODE_FDX.equals(this.getCode());
	}
	
	public String getMerchant(EnumCardType cardType) {
		return (String) this.merchants.get(cardType);
	}
	
	public String getPaymentechTxDivision() {
		return this.paymentechTxDivisions.isEmpty() ? "" : this.paymentechTxDivisions.iterator().next();
	}
	
	public static ErpAffiliate getAffiliateByTxDivision(String division){
		loadEnums();
		for(Iterator<ErpAffiliate> i = enums.values().iterator(); i.hasNext(); ) {
			ErpAffiliate aff = i.next();
			if(aff.paymentechTxDivisions.contains(division)){
				return aff;
			}
		}
		
		return getPrimaryAffiliate();
	}
	
	public static ErpAffiliate getAffiliateByMerchant(EnumCardType paymentType, String merchant) {
		loadEnums();
		for(Iterator<ErpAffiliate> i = enums.values().iterator(); i.hasNext(); ) {
			ErpAffiliate aff = i.next();
			if(aff.merchants.containsKey(paymentType) && aff.merchants.containsValue(merchant)) {
				return aff;
			}
		}
		
		return getPrimaryAffiliate();
	}
	
	public Map<EnumCardType, String> getMerchants() {
		return this.merchants;
	}
	
	public static ErpAffiliate getPrimaryAffiliate() {
		loadEnums();
		return enums.get(CODE_FD);
	}
	public static ErpAffiliate getPrimaryAffiliate(EnumEStoreId eStore) {
		loadEnums();
		return EnumEStoreId.FDX.equals(eStore)?(ErpAffiliate) enums.get(CODE_FDX):(ErpAffiliate) enums.get(CODE_FD);
	}

	public static ErpAffiliate getEnum(String code) {
		loadEnums();
		return enums.get(code);
	}

	public static Map<String, ErpAffiliate> getEnumMap() {
		loadEnums();
		return Collections.unmodifiableMap(enums);
	}

	public static List<ErpAffiliate> getEnumList() {
		loadEnums();
		return Collections.unmodifiableList(new ArrayList<ErpAffiliate>(enums.values()));
	}

	private static void loadEnums() {
		if (enums == null) {
			enums = new HashMap<String, ErpAffiliate>();
			List<ErpAffiliate> lst = loadEnums(ErpAffiliateDAO.class);
			for (Iterator<ErpAffiliate> i = lst.iterator(); i.hasNext();) {
				ErpAffiliate e = i.next();
				enums.put(e.getCode(), e);
			}
		}
	}

}
