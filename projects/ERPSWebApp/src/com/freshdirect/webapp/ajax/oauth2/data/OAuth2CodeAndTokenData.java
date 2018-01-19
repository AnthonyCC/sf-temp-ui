package com.freshdirect.webapp.ajax.oauth2.data;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.webapp.ajax.oauth2.util.ClientDataValidator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OAuth2CodeAndTokenData implements Serializable {
	private static final long serialVersionUID = 8433828844436452870L;

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("salt")
	private Double salt;

	@JsonProperty("expiresOn")
	private long expiresOn; // UTC

	@JsonProperty("type")
	private OAuth2Type type;

	@JsonProperty("userLoginId")
	private String userLoginId;

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("erpCustomerPK")
	private String erpCustomerPK;

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("fdCustomerPK")
	private String fdCustomerPK;

	@JsonProperty("clientId")
	private String clientId;

	@JsonProperty("scope")
	private Set<String> scope;

	public OAuth2CodeAndTokenData() {
		// Do not remove this constructor
	}

	public OAuth2CodeAndTokenData(String userLoginId, String erpCustomerPK, String fdCustomerPK, Set<String> scope,
			String clientId, long expInSec, OAuth2Type type) {

		this.userLoginId = userLoginId;
		this.erpCustomerPK = erpCustomerPK;
		this.fdCustomerPK = fdCustomerPK;
		this.clientId = clientId;
		this.type = type;
		this.scope = scope;

		this.addSalt();
		this.refreshExpiration(expInSec);
	}

	public OAuth2CodeAndTokenData(OAuth2CodeAndTokenData other) {
		
		this.userLoginId = other.userLoginId;
		this.erpCustomerPK = other.erpCustomerPK;
		this.fdCustomerPK = other.fdCustomerPK;
		this.clientId = other.clientId;
		this.expiresOn = other.expiresOn;
		this.type = other.type;
		this.scope = other.scope;
		
		this.addSalt();
	}

	/*
	 * POJO setters and getters
	 */

	@JsonProperty("salt")
	public Double getSalt() {
		return salt;
	}

	public void setSalt(Double salt) {
		this.salt = salt;
	}
	
	public void addSalt() {
		this.salt = Math.random();
	}

	@JsonProperty("expiresOn")
	public Long getExpiresOn() {
		return expiresOn;
	}

	@JsonProperty("expiresOn")
	public void setExpiresOn(long expiresOn) {
		this.expiresOn = expiresOn;
	}

	@JsonProperty("userLoginId")
	public String getUserLoginId() {
		return userLoginId;
	}

	@JsonProperty("userLoginId")
	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
	}

	@JsonProperty("erpCustomerPK")
	public String getErpCustomerPK() {
		return erpCustomerPK;
	}

	@JsonProperty("erpCustomerPK")
	public void setErpCustomerPK(String erpCustomerPK) {
		this.erpCustomerPK = erpCustomerPK;
	}

	@JsonProperty("fdCustomerPK")
	public String getFdCustomerPK() {
		return fdCustomerPK;
	}

	@JsonProperty("fdCustomerPK")
	public void setFdCustomerPK(String fdCustomerPK) {
		this.fdCustomerPK = fdCustomerPK;
	}

	@JsonProperty("clientId")
	public String getClientId() {
		return clientId;
	}

	@JsonProperty("clientId")
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	@JsonProperty("type")
	public OAuth2Type getType() {
		return this.type;
	}

	@JsonProperty("type")
	public void setType(OAuth2Type type) {
		this.type = type;
	}

	@JsonProperty("scope")
	public Set<String> getScope() {
		return scope;
	}

	@JsonProperty("scope")
	public void setScope(Set<String> scope) {
		this.scope = scope;
	}

	public void refreshExpiration(long expInSec) {
		this.expiresOn = (expInSec <= 0) ? 0 : System.currentTimeMillis() / 1000 + expInSec;
	}

	/*
	 * static methods for data/json conversion
	 */

	public static OAuth2CodeAndTokenData fromJson(String json)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		OAuth2CodeAndTokenData data = mapper.readValue(json, OAuth2CodeAndTokenData.class);
		return data;
	}

	public static String toJson(OAuth2CodeAndTokenData data) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(data);
		return json;
	}

	/**
	 * Validate access token
	 */
	@JsonIgnore
	public OAuth2Status isTokenValid() {
		if (this.type != OAuth2Type.TOKEN) {
			return new OAuth2Status(OAuth2Status.Code.ERROR, "This is not an access token");
		}
		if (!ClientDataValidator.validateClientId(this.clientId)) {
			return new OAuth2Status(OAuth2Status.Code.ERROR, "Invalid client Id in the token");
		}
		if ((this.expiresOn != 0) && (System.currentTimeMillis() / 1000) > this.expiresOn) {
			return new OAuth2Status(OAuth2Status.Code.EXPIRED, "This is an expired access token");
		}

		return new OAuth2Status(OAuth2Status.Code.SUCCESS);
	}

	/**
	 * Validate refresh token
	 */
	@JsonIgnore
	public OAuth2Status isRefreshTokenValid() {
		if (this.type != OAuth2Type.REFRESHTOKEN) {
			return new OAuth2Status(OAuth2Status.Code.ERROR, "This is not an refresh-token");
		}
		if (!ClientDataValidator.validateClientId(this.clientId)) {
			return new OAuth2Status(OAuth2Status.Code.ERROR, "Invalid client Id in the refresh-token");
		}
		if ((this.expiresOn != 0) && (System.currentTimeMillis() / 1000) > this.expiresOn) {
			return new OAuth2Status(OAuth2Status.Code.EXPIRED, "This is an expired refresh-token");
		}

		return new OAuth2Status(OAuth2Status.Code.SUCCESS);
	}

	/**
	 * Validate auth code
	 */
	@JsonIgnore
	public OAuth2Status isCodeValid() {
		if (this.type != OAuth2Type.CODE) {
			return new OAuth2Status(OAuth2Status.Code.ERROR, "This is not an auth code");
		}
		if (!ClientDataValidator.validateClientId(this.clientId)) {
			return new OAuth2Status(OAuth2Status.Code.ERROR, "Invalid client Id in the code");
		}
		if ((this.expiresOn != 0) && (System.currentTimeMillis() / 1000) > this.expiresOn) {
			return new OAuth2Status(OAuth2Status.Code.EXPIRED, "This is an expired auth code");
		}

		return new OAuth2Status(OAuth2Status.Code.SUCCESS);
	}

}
