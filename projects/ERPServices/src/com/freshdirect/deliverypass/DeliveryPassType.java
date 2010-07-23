package com.freshdirect.deliverypass;

/**
*
* @author  skrishnasamy
* @version 1.0
* @created 05-Jun-2006
* 
*/

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.deliverypass.ejb.DlvPassTypeDAO;
import com.freshdirect.enums.EnumModel;

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

	public DeliveryPassType(String code, String name, int noOfDlvs, int duration, boolean unlimited, String profileValue, boolean isAutoRenewDP, boolean isFreeTrialDP,boolean isFreeTrialRestricted, String autoRenewalSKU ) {
		super(code, name, null);
		this.noOfDeliveries = noOfDlvs;
		this.duration = duration;
		this.unlimited = unlimited;
		this.profileValue=profileValue;
		this.isAutoRenewDP=isAutoRenewDP;
		this.isFreeTrialDP=isFreeTrialDP;
		this.isFreeTrialRestricted=isFreeTrialRestricted;
		this.autoRenewalSKU=autoRenewalSKU;
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
	
}
