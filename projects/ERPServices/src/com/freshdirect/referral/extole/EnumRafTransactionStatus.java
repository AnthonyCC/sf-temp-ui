package com.freshdirect.referral.extole;

import org.apache.commons.lang.StringUtils;

public enum EnumRafTransactionStatus {

	PENDING("P"), 
	FAILURE("F"), 
	SUCCESS("S");

	private String value;

	private EnumRafTransactionStatus(String value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public static EnumRafTransactionStatus getEnumFromValue(String value) {
		EnumRafTransactionStatus type = null;
		if (StringUtils.isNotBlank(value)) {
			for (EnumRafTransactionStatus t : EnumRafTransactionStatus.values()) {
				if (value.equalsIgnoreCase(t.getValue())) {
					type = t;
					break;
				}
			}
		}

		return type;
	}

}
