package com.freshdirect.fdstore.content;

public enum EnumBurstType {
    NEW("new"),
    DEAL("deal"),
    YOUR_FAVE("fave"),
    BACK_IN_STOCK("back"),
    PRODUCT_SAMPLE("free"),
    GOING_OUT_OF_STOCK("going");
	
	/**
	 * Tracking Code
	 */
    private String code;

	private EnumBurstType(String code) {
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
