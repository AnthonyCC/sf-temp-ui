package com.freshdirect.webapp.ajax.oauth2.data;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TokenInfoResponse extends OAuth2Status implements Serializable {
	private static final long serialVersionUID = 3547435605034064313L;

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("data")
	private OAuth2CodeAndTokenData data;

	public TokenInfoResponse(Code code, OAuth2CodeAndTokenData data) {
		super(code);
		this.data = data;
	}

	public TokenInfoResponse(Code code, String message) {
		super(code, message);
		this.data = null;
	}

	@JsonProperty("data")
	public OAuth2CodeAndTokenData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(OAuth2CodeAndTokenData data) {
		this.data = data;
	}
}
