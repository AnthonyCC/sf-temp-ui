package com.freshdirect.webapp.ajax.expresscheckout.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.erp.EnumStateCodes;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.payment.BillingCountryInfo;
import com.freshdirect.payment.BillingRegionInfo;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormMetaData;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormMetaDataItem;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormMetaDataItemCountry;

public class FormMetaDataService {


	private static final FormMetaDataService INSTANCE = new FormMetaDataService();

	private static final String XC_PAYMENT_PROMO_OAS_SITEPAGE = "www.freshdirect.com/XCpaymentpromo";
	private static final String DEFAULT_COUNTRY_CODE = "US";

	private FormMetaDataService() {
	}

	public static FormMetaDataService defaultService() {
		return INSTANCE;
	}

	public FormMetaData populateFormMetaData(FDUserI user) throws FDResourceException {
		// TODO: not null countryCode if the user has no payment method yet and populate billing with existing delivery address.
		FormMetaData formMetaData = new FormMetaData();
		formMetaData.setCardType(populateCardType(null));
		formMetaData.setExpireMonth(populateCardExpireMonth(null));
		formMetaData.setExpireYear(populateCardExpireYear(null));
		formMetaData.setState(populateState(null, null));
		formMetaData.setCountry(populateCountry(null, formMetaData));
        if (user != null) {
            formMetaData.setEnableECheck(populateECheck(user));
            formMetaData.setEnableEbtCheck(populateEbtCheck(user));
        }
		formMetaData.setPaymentPromoOAS(XC_PAYMENT_PROMO_OAS_SITEPAGE);
		return formMetaData;
	}

	public List<FormMetaDataItem> populateState(String countryCode, String state) {
		List<FormMetaDataItem> result = new ArrayList<FormMetaDataItem>();
		if (countryCode == null) {
			countryCode = DEFAULT_COUNTRY_CODE;
		}
		if (state == null) {
			state = EnumStateCodes.ENUM_STATE_NY.getId();
		}
		BillingCountryInfo bc = BillingCountryInfo.getEnum(countryCode);
		if (bc != null) {
			for (BillingRegionInfo regionInfo : bc.getRegions()) {
				FormMetaDataItem data = new FormMetaDataItem();
				data.setKey(regionInfo.getCode());
				data.setValue(regionInfo.getName());
				data.setSelected(state.equals(regionInfo.getCode()));
				result.add(data);
			}
		}
		return result;
	}

	public List<FormMetaDataItemCountry> populateCountry(String ccCountry) {
		return populateCountry(ccCountry, null);
	}
	
	public List<FormMetaDataItemCountry> populateCountry(String ccCountry, FormMetaData formMetaData) {
		List<FormMetaDataItemCountry> result = new ArrayList<FormMetaDataItemCountry>();
		if (ccCountry == null) {
			ccCountry = DEFAULT_COUNTRY_CODE;
		}
		for (BillingCountryInfo countryInfo : BillingCountryInfo.getEnumList()) {
			FormMetaDataItemCountry data = new FormMetaDataItemCountry();
			data.setKey(countryInfo.getCode());
			data.setValue(countryInfo.getName());
			data.setSelected(countryInfo.getCode().equalsIgnoreCase(ccCountry));
			data.setStates(populateState(countryInfo.getCode(), null));
			if (formMetaData != null) {
				formMetaData.getCountryCodeIndexMap().put(countryInfo.getCode(), result.size());
			}
			result.add(data);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<FormMetaDataItem> populateCardType(String selectedCardType) {
		List<FormMetaDataItem> result = new ArrayList<FormMetaDataItem>();
		List<EnumCardType> cardTypes = EnumCardType.getCardTypes();
		for (EnumCardType cardType : cardTypes) {
			FormMetaDataItem data = new FormMetaDataItem();
			data.setKey(cardType.getFdName());
			data.setValue(cardType.getDisplayName());
			data.setSelected(cardType.getFdName().equals(selectedCardType));
			result.add(data);
		}
		return result;
	}

	public List<FormMetaDataItem> populateCardExpireMonth(String ccExpireMonth) {
		List<FormMetaDataItem> result = new ArrayList<FormMetaDataItem>();
		String tCcExpireMonth = "";
		if (ccExpireMonth == null) {
			ccExpireMonth = "";
		}
		if (ccExpireMonth.length() == 2) {
			tCcExpireMonth = ccExpireMonth;
        } else if (ccExpireMonth.indexOf('0') < 1) {
			tCcExpireMonth = "0" + ccExpireMonth;
		}
		for (int mnth = 1; mnth < 13; mnth++) {
			String strMonth = mnth < 10 ? "0" + mnth : "" + mnth;
			FormMetaDataItem data = new FormMetaDataItem();
			data.setKey(strMonth);
			data.setValue(strMonth);
			data.setSelected((strMonth + "").equals(tCcExpireMonth));
			result.add(data);
		}
		return result;
	}

	public List<FormMetaDataItem> populateCardExpireYear(String ccExpireYear) {
		List<FormMetaDataItem> result = new ArrayList<FormMetaDataItem>();
		int startYear = Calendar.getInstance().get(Calendar.YEAR);
		int endYear = startYear + 15;
		for (int yr = startYear; yr < endYear; yr++) {
			FormMetaDataItem data = new FormMetaDataItem();
			String year = Integer.toString(yr);
			data.setKey(year);
			data.setValue(year);
			data.setSelected(year.equals(ccExpireYear));
			result.add(data);
		}
		return result;
	}

	public boolean populateECheck(FDUserI user) throws FDResourceException {
		return user.isCheckEligible() && !user.isECheckRestricted();//!FDCustomerManager.isECheckRestricted(user.getIdentity());
	}

	public boolean populateEbtCheck(FDUserI user) {
		return user.isEbtAccepted();
	}
	
}
