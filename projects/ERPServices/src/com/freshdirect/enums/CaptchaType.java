package com.freshdirect.enums;

public enum CaptchaType {
	DEFAULT(0, "default"),
	SIGN_IN(0, "signIn"),
	PAYMENT(1, "payment"),
	SIGN_UP(2, "signUp");
	private final int value;
	private final String name;
    private CaptchaType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }
    
    @Override
    public String toString() {
    	return this.name;
    }
    
}
