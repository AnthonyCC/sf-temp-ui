package com.freshdirect.payment;

import java.io.Serializable;

public class GCException implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1250672149372423982L;
	public GCException(String type, int code, String message) {
		super();
		this.type = type;
		this.code = code;
		this.message = message;
	}
	private String type;
	private int code;
	private String message;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
