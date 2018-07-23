package com.freshdirect.deliverypass;

/**
*
* @author  skrishnasamy
* @version 1.0
* @created 05-Jun-2006
* 
*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.StringUtils;

import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.deliverypass.ejb.DlvPassTypeDAO;
import com.freshdirect.enums.EnumModel;
import com.freshdirect.fdstore.EnumEStoreId;

/**
 * @author skrishnasamy
 *
 */
public class DeliveryPassType extends EnumModel {

	private static final long	serialVersionUID	= -3499001811399219072L;
	
	private static Map<String, DeliveryPassType> enums = null;	    
	private int noOfDeliveries;
	private int duration; //no:of days for unlimited pass to expire.
	private boolean unlimited;
	private String profileValue;
	private boolean isAutoRenewDP;
	private boolean isFreeTrialDP;
	private boolean isFreeTrialRestricted;
	private String autoRenewalSKU;
	private List<Integer> eligibleDlvDays;
	private List<EnumDeliveryType> deliveryTypes;
	private List<EnumEStoreId> eStoreIds;
	private String shortName;
	
	
	public int getDuration() {
		return duration;
	}
	
	public boolean isUnlimited() {
		return unlimited;
	}

	public int getNoOfDeliveries() {
		return noOfDeliveries;
	}
	
	public String getProfileValue() {
		return profileValue;
	}

	public DeliveryPassType(@JsonProperty("code") String code, @JsonProperty("name") String name,
			@JsonProperty("noOfDeliveries") int noOfDlvs, @JsonProperty("duration") int duration,
			@JsonProperty("unlimited") boolean unlimited, @JsonProperty("profileValue") String profileValue,
			@JsonProperty("autoRenewDP") boolean isAutoRenewDP, @JsonProperty("freeTrialDP") boolean isFreeTrialDP,
			@JsonProperty("freeTrialRestricted") boolean isFreeTrialRestricted,
			@JsonProperty("autoRenewalSKU") String autoRenewalSKU,
			@JsonProperty("eligibleDlvDays") List<Integer> eligibleDlvDays,
			@JsonProperty("deliveryTypes") List<EnumDeliveryType> deliveryTypes,
			@JsonProperty("eStoreIds") List<EnumEStoreId> eStoreIds,
			@JsonProperty("shortName") String shortName) {
		super(code, name, null);
		this.noOfDeliveries = noOfDlvs;
		this.duration = duration;
		this.unlimited = unlimited;
		this.profileValue = profileValue;
		this.isAutoRenewDP = isAutoRenewDP;
		this.isFreeTrialDP = isFreeTrialDP;
		this.isFreeTrialRestricted = isFreeTrialRestricted;
		this.autoRenewalSKU = autoRenewalSKU;
		this.setEligibleDlvDays(eligibleDlvDays);
		this.deliveryTypes = deliveryTypes;
		this.eStoreIds = eStoreIds;
		this.shortName = shortName;
	}

	public static DeliveryPassType getEnum(String code) {
		loadEnums();
		return enums.get(code);
	}

	public static Map<String, DeliveryPassType> getEnumMap() {
		loadEnums();
		return Collections.unmodifiableMap(enums);
	}

	public static List<DeliveryPassType> getEnumList() {
		loadEnums();
		return Collections.unmodifiableList(new ArrayList<DeliveryPassType>(enums.values()));
	}

	public static DeliveryPassType getUnlimitedType() {
		for ( DeliveryPassType type : getEnumList() ) {
			if ( type.isUnlimited() ) {
				return type;
			}
		}
		return null;
	}

	public static List<DeliveryPassType> getUnlimitedTypes(){
		List<DeliveryPassType> types  = new ArrayList<DeliveryPassType>();
		for ( DeliveryPassType type : getEnumList() ) {
			if(type.isUnlimited()){
				types.add(type);
			}
		}
		return types;
    }

	public static List<DeliveryPassType> getBSGSTypes() {
		List<DeliveryPassType> types = new ArrayList<DeliveryPassType>();
		for ( DeliveryPassType type : getEnumList() ) {
			if ( !type.isUnlimited() ) {
				types.add( type );
			}
		}
		return types;
	}	
	
	private static void loadEnums() {
		if (enums == null) {
			enums = new HashMap<String, DeliveryPassType>();
			List<DeliveryPassType> lst = loadEnums(DlvPassTypeDAO.class);
			for ( DeliveryPassType e : lst ) {
				enums.put(e.getCode(), e);
			}
		}
	}

	public boolean isAutoRenewDP() {
		return isAutoRenewDP;
	}

	public void setAutoRenewDP(boolean isAutoRenewDP) {
		this.isAutoRenewDP = isAutoRenewDP;
	}

	public boolean isFreeTrialDP() {
		return isFreeTrialDP;
	}

	public void setIsFreeTrialDP(boolean isFreeTrialDP) {
		this.isFreeTrialDP = isFreeTrialDP;
	}
	
	public boolean isFreeTrialRestricted() {
		return isFreeTrialRestricted;
	}

	public void setIsFreeTrialRestricted(boolean isFreeTrialRestricted) {
		this.isFreeTrialRestricted = isFreeTrialRestricted;
	}

	public String getAutoRenewalSKU() {
		return autoRenewalSKU;
	}

	public List<Integer> getEligibleDlvDays() {
		return eligibleDlvDays;
	}

	public void setEligibleDlvDays(List<Integer> eligibleDlvDays) {
		this.eligibleDlvDays = eligibleDlvDays;
	}


	public List<EnumDeliveryType> getDeliveryTypes() {
		return deliveryTypes;
	}

	public void setDeliveryTypes(List<EnumDeliveryType> deliveryTypes) {
		this.deliveryTypes = deliveryTypes;
	}

	public List<EnumEStoreId> geteStoreIds() {
		return eStoreIds;
	}

	public void seteStoreIds(List<EnumEStoreId> eStoreIds) {
		this.eStoreIds = eStoreIds;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
}
