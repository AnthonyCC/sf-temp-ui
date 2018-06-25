package com.freshdirect.enums;

public enum CaptchaType {
	DEFAULT(0),
	SIGN_IN(0),
	PAYMENT(1),
	SIGN_UP(2);
	private final int value;
    private CaptchaType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
