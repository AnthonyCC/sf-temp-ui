package com.freshdirect.webapp.taglib.crm;

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
			customer.getCustomerInfo().isReceiveOptinNewsletter());
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

		public CrmCustomerInfo(String userId, String passwordHint, boolean recieveNews, boolean textOnlyEmail, boolean receiveOptinNewsletter) {
			this.userId = userId;
			this.passwordHint = passwordHint;
			this.recieveNews = recieveNews;
			this.textOnlyEmail = textOnlyEmail;
			this.receiveOptinNewsletter = receiveOptinNewsletter;
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
		
	}

}
