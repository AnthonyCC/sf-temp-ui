package com.freshdirect.webapp.taglib.crm;

import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class CrmGetCustomerInfoTag extends AbstractGetterTag {

	private FDUserI user;

	public void setUser(FDUserI user) {
		this.user = user;
	}

	protected Object getResult() throws Exception {
		if (this.user == null) {
			throw new IllegalArgumentException("No id provided customer");
		}
		ErpCustomerModel customer = FDCustomerFactory.getErpCustomer(user.getIdentity());
		FDCustomerModel fdCustomer = FDCustomerFactory.getFDCustomer(user.getIdentity());
		return new CrmCustomerInfo(
			customer.getUserId(),
			fdCustomer.getPasswordHint(),
			customer.getCustomerInfo().isReceiveNewsletter(),
			customer.getCustomerInfo().isEmailPlaintext(),
			customer.getCustomerInfo().isReceiveOptinNewsletter(),
			customer.getCustomerInfo().getMobileNumber(),
			customer.getCustomerInfo().isDeliveryNotification(),
			customer.getCustomerInfo().isOffersNotification(),
			customer.getCustomerInfo().isGoGreen(),
			customer.getCustomerInfo().getMobilePreference());
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
		private boolean recieveNews;
		private boolean textOnlyEmail;
		private boolean receiveOptinNewsletter;
		private PhoneNumber mobileNumber;
		private boolean delNotification;
		private boolean offNotification;
		private boolean goGreen;
		private String mobilePrefs;
		
		

		

		public CrmCustomerInfo(String userId, String passwordHint, boolean recieveNews, boolean textOnlyEmail, boolean receiveOptinNewsletter,
					PhoneNumber mobileNumber, boolean delNotification, boolean offNotification, boolean goGreen, String mobilePrefs ) {
			this.userId = userId;
			this.passwordHint = passwordHint;
			this.recieveNews = recieveNews;
			this.textOnlyEmail = textOnlyEmail;
			this.receiveOptinNewsletter = receiveOptinNewsletter;
			this.mobileNumber = mobileNumber;
			this.delNotification = delNotification;
			this.offNotification = offNotification;
			this.goGreen = goGreen;
			this.mobilePrefs = mobilePrefs;
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
		
	}

}
