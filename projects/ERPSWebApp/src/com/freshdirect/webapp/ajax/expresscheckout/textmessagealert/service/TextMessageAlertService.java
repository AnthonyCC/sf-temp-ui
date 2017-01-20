package com.freshdirect.webapp.ajax.expresscheckout.textmessagealert.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.delivery.sms.SMSAlertManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.ejb.FDCustomerEStoreModel;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sms.EnumSMSAlertStatus;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;
import com.freshdirect.webapp.ajax.expresscheckout.validation.service.AlertValidationConstraints;
import com.freshdirect.webapp.util.MediaUtils;

public class TextMessageAlertService {

	private static Category LOGGER = LoggerFactory.getInstance(TextMessageAlertService.class);

	private static final TextMessageAlertService INSTANCE = new TextMessageAlertService();
	
	private static final String SMS_TERMS_AND_CONDITIONS_MEDIA_PATH = "/media/editorial/site_pages/sms/terms_long.html";

	private TextMessageAlertService() {
	}

	public static TextMessageAlertService defaultService() {
		return INSTANCE;
	}

	public boolean storeSmsAlertPreferences(final FormDataRequest alertRequest, final FDUserI user) {
		boolean isSent = false;

		try {
			FDIdentity identity = user.getIdentity();
			Map<String, String> formData = FormDataService.defaultService().getSimpleMap(alertRequest);
			String mobileNumber = formData.get(AlertValidationConstraints.MOBILE);
			
			FDCustomerModel fdCustomer = FDCustomerFactory.getFDCustomer(user.getIdentity());
			isSent = SMSAlertManager.getInstance().smsOptIn(identity.getErpCustomerPK(), mobileNumber, user.getUserContext().getStoreContext().getEStoreId().getContentId());

			if (isSent) {
				FDCustomerManager.storeMobilePreferences(identity.getErpCustomerPK(), identity.getFDCustomerPK(), mobileNumber, convertBooleanValueToString(fdCustomer.getCustomerSmsPreferenceModel().getOffersNotification()),
						convertBooleanValueToString(fdCustomer.getCustomerSmsPreferenceModel().getDeliveryNotification()), isOptionEnabled(formData, AlertValidationConstraints.NOTICES),
						isOptionEnabled(formData, AlertValidationConstraints.ALERTS), isOptionEnabled(formData, AlertValidationConstraints.PERKS),
						isAlertStatusEnabled(EnumSMSAlertStatus.getEnum(fdCustomer.getCustomerSmsPreferenceModel().getPartnerMessages())), user.getUserContext().getStoreContext().getEStoreId());

				FDCustomerManager.storeSmsPreferenceFlag(identity.getFDCustomerPK(), "Y", user.getUserContext().getStoreContext().getEStoreId());
			}
		} catch (FDResourceException e) {
			LOGGER.error("Error while store text message alert preferences.", e);
		} 
		return isSent;
	}

	public boolean cancelSmsAlertPreferences(final FormDataRequest alertRequest, final FDUserI user) {
		boolean isCancel = false;

		try {
			FDIdentity identity = user.getIdentity();
			FDCustomerManager.storeSmsPreferenceFlag(identity.getFDCustomerPK(), "N", user.getUserContext().getStoreContext().getEStoreId());
			isCancel = true;
		} catch (FDResourceException e) {
			LOGGER.error("Error while cancel text message alert preferences.", e);
		}

		return isCancel;
	}

	public boolean showTextMessageAlertPopup(final FDUserI user) throws FDResourceException {
		FDCustomerModel fdCustomer = FDCustomerFactory.getFDCustomer(user.getIdentity());
		FDCustomerEStoreModel fdCustomerSmsPreferenceModel=fdCustomer.getCustomerSmsPreferenceModel();
		return FDStoreProperties.getSMSOverlayFlag() && !isModeModifyOrder(user) && isAlertFlagNotSet(fdCustomerSmsPreferenceModel);
	}
	
	public String getTermsAndConditionsMedia() throws IOException, TemplateException{
		StringWriter out = new StringWriter();
		String result = null;
		try {
			MediaUtils.render(SMS_TERMS_AND_CONDITIONS_MEDIA_PATH, out, null, null);
			result = out.toString();
		} finally {
			out.close();
		}
		return result;
	}

	private boolean isModeModifyOrder(final FDUserI user) {
		FDCartModel shoppingCart = user.getShoppingCart();
		return (shoppingCart instanceof FDModifyCartModel);
	}

	private boolean isAlertFlagNotSet(final FDCustomerEStoreModel fdCustomerEStoreModel) {
		return fdCustomerEStoreModel.getSmsPreferenceflag() == null;
	}

	private String isOptionEnabled(final Map<String, String> formdata, final String key) {
		return isOptionEnabled(formdata.get(key));
	}

	private String isOptionEnabled(final String option) {
		return convertBooleanValueToString("on".equals(option));
	}

	private String convertBooleanValueToString(final boolean value) {
		return (value) ? "Y" : "N";
	}

	private String isAlertStatusEnabled(final EnumSMSAlertStatus value) {
		return convertBooleanValueToString(EnumSMSAlertStatus.SUBSCRIBED.equals(value) || EnumSMSAlertStatus.PENDING.equals(value));
	}

}
