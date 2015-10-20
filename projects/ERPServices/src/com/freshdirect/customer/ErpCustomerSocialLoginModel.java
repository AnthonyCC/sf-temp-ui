package com.freshdirect.customer;

import java.io.Serializable;

public class ErpCustomerSocialLoginModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1257396834016069308L;
	private String user_id;
	private String user_token;
	private String identity_token;
	private String provider;
	private String display_name;
	private String preferred_user_name;
	private String email;
	private boolean email_verified;
	
	public ErpCustomerSocialLoginModel(){
		super();
	}
	
	public ErpCustomerSocialLoginModel(String user_id, String user_token,
			String identity_token, String provider, String display_name,
			String preferred_user_name, String email, boolean email_verified) {
		
		super();
		this.user_id = user_id;
		this.user_token = user_token;
		this.identity_token = identity_token;
		this.provider = provider;
		this.display_name = display_name;
		this.preferred_user_name = preferred_user_name;
		this.email = email;
		this.email_verified = email_verified;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_token() {
		return user_token;
	}

	public void setUser_token(String user_token) {
		this.user_token = user_token;
	}

	public String getIdentity_token() {
		return identity_token;
	}

	public void setIdentity_token(String identity_token) {
		this.identity_token = identity_token;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public String getPreferred_user_name() {
		return preferred_user_name;
	}

	public void setPreferred_user_name(String preferred_user_name) {
		this.preferred_user_name = preferred_user_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEmail_verified() {
		return email_verified;
	}

	public void setEmail_verified(boolean email_verified) {
		this.email_verified = email_verified;
	}

	@Override
	public String toString() {
		return "ErpCustomerSocialLoginModel [user_id=" + user_id
				+ ", user_token=" + user_token + ", identity_token="
				+ identity_token + ", provider=" + provider + ", display_name="
				+ display_name + ", preferred_user_name=" + preferred_user_name
				+ ", email=" + email + ", email_verified=" + email_verified
				+ "]";
	}
	
	
	
	

}
