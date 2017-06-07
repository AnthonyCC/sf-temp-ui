package com.freshdirect.payment.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.ecommerce.data.delivery.AbstractRestrictionData;
import com.freshdirect.ecommerce.data.delivery.AddressRestrictionData;
import com.freshdirect.ecommerce.data.delivery.AlcoholRestrictionData;
import com.freshdirect.ecommerce.data.delivery.DateRangeData;
import com.freshdirect.ecommerce.data.delivery.OneTimeRestrictionData;
import com.freshdirect.ecommerce.data.delivery.OneTimeReverseRestrictionData;
import com.freshdirect.ecommerce.data.delivery.RecurringRestrictionData;
import com.freshdirect.ecommerce.data.delivery.RestrictionData;
import com.freshdirect.ecommerce.data.delivery.TimeOfDayData;
import com.freshdirect.ecommerce.data.delivery.TimeOfDayRangeData;
import com.freshdirect.framework.util.TimeOfDayRange;

public class DlvRestrictionModelConverter {

	public static AddressRestrictionData buildAddressRestrictionData(String address1, String apartment, String zipCode) {
		AddressRestrictionData addressRestrictionData = new AddressRestrictionData();
		addressRestrictionData.setAddress(address1);
		addressRestrictionData.setApartment(apartment);
		addressRestrictionData.setZipcode(zipCode);
		return addressRestrictionData;
	}

	public static List  buildDlvRestrictionListResponse(List<RestrictionData> restrictionDataList) {
		List restrictions = new ArrayList();
		for (RestrictionData restrictionData : restrictionDataList) {
			if(restrictionData.getClassType().equals(OneTimeRestrictionData.class.getName())){
				restrictions.add(restrictionData.getOneTimeRestrictionData());
			}
			else if(restrictionData.getClassType().equals(OneTimeReverseRestrictionData.class.getName())){
				restrictions.add(restrictionData.getOneTimeReverseRestrictionData());
			}
			else if(restrictionData.getClassType().equals(RecurringRestrictionData.class.getName())){
				restrictions.add(restrictionData.getRecurringRestrictionData());
			}
			else{
				restrictions.add(restrictionData.getAlcoholRestrictionData());
			}
		}
		return restrictions;
	}

	public static Object  buildDlvRestrictionResponse(RestrictionData restrictionData) {
		if(restrictionData.getClassType().equals(OneTimeRestrictionData.class.getName())){
			return restrictionData.getOneTimeRestrictionData();
		}
		else if(restrictionData.getClassType().equals(OneTimeReverseRestrictionData.class.getName())){
			return restrictionData.getOneTimeReverseRestrictionData();
		}
		else if(restrictionData.getClassType().equals(RecurringRestrictionData.class.getName())){
			return restrictionData.getRecurringRestrictionData();
		}
		else{
			return restrictionData.getAlcoholRestrictionData();
		}
		
	}

	



}
