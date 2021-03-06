package com.freshdirect.webapp.taglib.crm;

import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.ejb.FDCustomerEStoreModel;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class CrmGetCustomerInfoTag extends AbstractGetterTag<CrmCustomerInfoI> {
	private static final long serialVersionUID = 5664099748140954763L;

	private FDUserI user;

	public void setUser(FDUserI user) {
		this.user = user;
	}

	@Override
	protected CrmCustomerInfoI getResult() throws Exception {
		if (this.user == null) {
			throw new IllegalArgumentException("No id provided customer");
		}
		ErpCustomerModel customer = FDCustomerFactory.getErpCustomer(user.getIdentity());
		FDCustomerModel fdCustomer = FDCustomerFactory.getFDCustomer(user.getIdentity());
		return new CrmCustomerInfo(
			customer.getUserId(),
			fdCustomer.getPasswordHint(),
			fdCustomer.getCustomerEStoreModel().getEmailOptIn(),//customer.getCustomerInfo().isReceiveNewsletter(),
			customer.getCustomerInfo().isEmailPlaintext(),
			customer.getCustomerInfo().isReceiveOptinNewsletter(),
			fdCustomer.getCustomerSmsPreferenceModel().getMobileNumber(),
			fdCustomer.getCustomerSmsPreferenceModel().getDeliveryNotification(),
			fdCustomer.getCustomerSmsPreferenceModel().getOffersNotification(),
			fdCustomer.getCustomerSmsPreferenceModel().getOrderNotices(),
			fdCustomer.getCustomerSmsPreferenceModel().getOrderExceptions(),
			fdCustomer.getCustomerSmsPreferenceModel().getOffers(),
			fdCustomer.getCustomerSmsPreferenceModel().getPartnerMessages(),
			customer.getCustomerInfo().isGoGreen(),
			customer.getCustomerInfo().getMobilePreference(),
			fdCustomer.getCustomerSmsPreferenceModel().getFdxMobileNumber(),
			fdCustomer.getCustomerSmsPreferenceModel().getFdxOrderNotices(),
			fdCustomer.getCustomerSmsPreferenceModel().getFdxOrderExceptions(),
			fdCustomer.getCustomerSmsPreferenceModel().getFdxOffers(),
			fdCustomer.getCustomerSmsPreferenceModel().getFdxdeliveryNotification(),
			fdCustomer.getCustomerSmsPreferenceModel().getFdxOffersNotification(),
			fdCustomer.getCustomerSmsPreferenceModel().getFdxPartnerMessages(),
			fdCustomer.getCustomerSmsPreferenceModel(),
			fdCustomer.getCustomerEStoreModel().getFdxEmailOptIn());
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "com.freshdirect.webapp.taglib.crm.CrmCustomerInfoI";
		}
	}

	public static class CrmCustomerInfo implements CrmCustomerInfoI {
		private String userId;
		private String password;
		private String passwordHint;
		private boolean recieveNews;//email optin for FD
		private boolean receiveFdxNews;//email optin for FDX
		private boolean textOnlyEmail;
		private boolean receiveOptinNewsletter;
		private PhoneNumber mobileNumber;
		private boolean delNotification;
		private boolean offNotification;
		//SMS Alerts
		private String orderNotices;
		private String orderExceptions;
		private String offers;
		private String partnerMessages;
		private PhoneNumber fdxMobileNumber;
		private String fdxOrderExceptions;
		private String fdxOffers;
		private String fdxPartnerMessages;
		private boolean fdxDeliveryNotification;
		private boolean fdxOffersNotification;
		
		
		public boolean isFdxDeliveryNotification() {
			return fdxDeliveryNotification;
		}

		public void setFdxDeliveryNotification(boolean fdxDeliveryNotification) {
			this.fdxDeliveryNotification = fdxDeliveryNotification;
		}

		public boolean isFdxOffersNotification() {
			return fdxOffersNotification;
		}

		public void setFdxOffersNotification(boolean fdxOffersNotification) {
			this.fdxOffersNotification = fdxOffersNotification;
		}

		public String getFdxPartnerMessages() {
			return fdxPartnerMessages;
		}

		public void setFdxPartnerMessages(String fdxPartnerMessages) {
			fdxPartnerMessages = fdxPartnerMessages;
		}

		private boolean goGreen;
		private String mobilePrefs;
		private FDCustomerEStoreModel customerSmsPreferenceModel;

		//FDX SMS Alerts
		private String fdxOrderNotices;
		public String getFdxOrderNotices() {
			return fdxOrderNotices;
		}

		public void setFdxOrderNotices(String fdxOrderNotices) {
			this.fdxOrderNotices = fdxOrderNotices;
		}

		public String getFdxOrderExceptions() {
			return this.fdxOrderExceptions = fdxOrderExceptions;
		}

		public void setFdxOrderExceptions(String fdxOrderExceptions) {
			this.fdxOrderExceptions = fdxOrderExceptions;;
		}

		public String getFdxOffers() {
			return this.fdxOffers=fdxOffers;
		}

		public void setFdxOffers(String fdxOffers) {
			this.fdxOffers = fdxOffers;
		}

		public PhoneNumber getFdxMobileNumber() {
			return this.fdxMobileNumber;
		}

		public void setFdxMobileNumber(PhoneNumber fdxMobileNumber) {
			this.fdxMobileNumber = fdxMobileNumber;
		}
		
		public FDCustomerEStoreModel getCustomerSmsPreferenceModel() {
			return customerSmsPreferenceModel;
		}

		public void setFdCustomerEStoreModel(FDCustomerEStoreModel customerSmsPreferenceModel) {
			this.customerSmsPreferenceModel = customerSmsPreferenceModel;
		}

		public CrmCustomerInfo(String userId, String passwordHint, boolean recieveNews, boolean textOnlyEmail, boolean receiveOptinNewsletter,
					PhoneNumber mobileNumber, boolean delNotification, boolean offNotification, String orderNotices, String orderExceptions,
					String offers, String partnerMessages, boolean goGreen, String mobilePrefs,PhoneNumber fdxMobileNumber,
					String fdxOrderNotices, String fdxOrderExceptions, String fdxOffers, boolean fdxDeliveryNotification,
					boolean fdxOffersNotification, String fdxPartnerMessages, FDCustomerEStoreModel customerSmsPreferenceModel,boolean receiveFdxNews) {
			this.userId = userId;
			this.passwordHint = passwordHint;
			this.recieveNews = recieveNews;
			this.textOnlyEmail = textOnlyEmail;
			this.receiveOptinNewsletter = receiveOptinNewsletter;
			this.mobileNumber = mobileNumber;
			this.delNotification = delNotification;
			this.offNotification = offNotification;
			this.fdxDeliveryNotification=fdxDeliveryNotification;
			this.fdxOffersNotification=fdxOffersNotification;
 			//sms Alerts
			this.orderNotices = orderNotices;
			this.orderExceptions = orderExceptions;
			this.offers = offers;
			this.partnerMessages = partnerMessages;
			this.fdxOrderNotices = fdxOrderNotices;
			this.fdxOrderExceptions = fdxOrderExceptions;
			this.fdxMobileNumber = fdxMobileNumber;
			this.fdxOffers = fdxOffers;
			this.goGreen = goGreen;
			this.mobilePrefs = mobilePrefs;
			this.fdxPartnerMessages=fdxPartnerMessages;
			this.customerSmsPreferenceModel=customerSmsPreferenceModel;
			this.receiveFdxNews =receiveFdxNews;
		}

		public String getOrderNotices() {
		
			return orderNotices;
		}
		public void setOrderNotices(String orderNotices) {
			this.orderNotices = orderNotices;
		}
		public String getOrderExceptions() {
			return orderExceptions;
		}
		public void setOrderExceptions(String orderExceptions) {
			this.orderExceptions = orderExceptions;
		}
		public String getOffers() {
			return offers;
		}
		public void setOffers(String offers) {
			this.offers = offers;
		}
		public String getPartnerMessages() {
			return partnerMessages;
		}
		public void setPartnerMessages(String partnerMessages) {
			this.partnerMessages = partnerMessages;
		}
		public String getUserId() {
			return this.userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getPassword() {
			return this.password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getPasswordHint() {
			return this.passwordHint;
		}

		public void setPasswordHint(String passwordHint) {
			this.passwordHint = passwordHint;
		}

		public boolean isRecieveFdNews() {
			return this.recieveNews;
		}

		public void setRecieveFdNews(boolean recieveNews) {
			this.recieveNews = recieveNews;
		}

		public boolean isTextOnlyEmail() {
			return this.textOnlyEmail;
		}

		public void setTextOnlyEmail(boolean textOnlyEmail) {
			this.textOnlyEmail = textOnlyEmail;
		}
		
		public boolean isReceiveOptinNewsletter() {
			return receiveOptinNewsletter;
		}
		
		public void setReceiveOptinNewsletter(boolean receiveOptinNewsletter) {
			this.receiveOptinNewsletter = receiveOptinNewsletter;
		}
		
		public PhoneNumber getMobileNumber() {
			return mobileNumber;
		}

		public void setMobileNumber(PhoneNumber mobileNumber) {
			this.mobileNumber = mobileNumber;
		}

		public boolean isDelNotification() {
			return delNotification;
		}

		public void setDelNotification(boolean delNotification) {
			this.delNotification = delNotification;
		}

		public boolean isOffNotification() {
			return offNotification;
		}

		public void setOffNotification(boolean offNotification) {
			this.offNotification = offNotification;
		}

		public boolean isGoGreen() {
			return goGreen;
		}

		public void setGoGreen(boolean goGreen) {
			this.goGreen = goGreen;
		}

		public String getMobilePreference() {
			return mobilePrefs;
		}

		public void setMobilePreference(String mobilePrefs) {
			this.mobilePrefs = mobilePrefs;
		}

		public boolean isReceiveFdxNews() {
			return receiveFdxNews;
		}

		public void setReceiveFdxNews(boolean receiveFdxNews) {
			this.receiveFdxNews = receiveFdxNews;
		}
		
	}

}
