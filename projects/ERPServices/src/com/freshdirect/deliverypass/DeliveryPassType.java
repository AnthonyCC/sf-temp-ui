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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.Enum;

import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.crm.ejb.CrmAgentRoleDAO;
import com.freshdirect.deliverypass.ejb.DlvPassTypeDAO;
import com.freshdirect.enum.EnumModel;

/**
 * @author skrishnasamy
 *
 */
public class DeliveryPassType extends EnumModel {

	private static Map enums = null;	    
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
		return (DeliveryPassType) enums.get(code);
	}

	public static Map getEnumMap() {
		loadEnums();
		return Collections.unmodifiableMap(enums);
	}

	public static List getEnumList() {
		loadEnums();
		return Collections.unmodifiableList(new ArrayList(enums.values()));
	}

	public static DeliveryPassType getUnlimitedType(){
		DeliveryPassType type  = null;
		Iterator iter = getEnumList().iterator();
		while(iter.hasNext()){
			type = (DeliveryPassType)iter.next();
			if(type.isUnlimited()){
				break;
			}
		}
		return type;
	}

	public static List getUnlimitedTypes(){
		List types  = new ArrayList();
		Iterator iter = getEnumList().iterator();
		while(iter.hasNext()){
			DeliveryPassType type = (DeliveryPassType)iter.next();
			if(type.isUnlimited()){
				types.add(type);
			}
		}
		return types;
    }
	public static List getBSGSTypes(){
		List types  = new ArrayList();
		Iterator iter = getEnumList().iterator();
		while(iter.hasNext()){
			DeliveryPassType type = (DeliveryPassType)iter.next();
			if(!type.isUnlimited()){
				types.add(type);
			}
		}
		return types;
	}
	
	
	private static void loadEnums() {
		if (enums == null) {
			enums = new HashMap();
			List lst = loadEnums(DlvPassTypeDAO.class);
			for (Iterator i = lst.iterator(); i.hasNext();) {
				DeliveryPassType e = (DeliveryPassType) i.next();
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
	//restrictFreeTrial
	
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
