package com.freshdirect.fdstore.content;

public enum EnumBurstType {
	NEW("new"), DEAL("deal"), YOUR_FAVE("fave");
	
	/**
	 * Tracking Code
	 */
	String code;

	private EnumBurstType(String code) {
		// TODO Auto-generated constructor stub
		this.code = code;
	}


	/**
	 * Retrieve tracking code
	 * @return
	 */
	public String getCode() {
		return code;
	}
}
