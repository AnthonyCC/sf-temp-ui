package com.freshdirect.common.address;

import com.freshdirect.framework.util.NVL;

public class PhoneNumber implements java.io.Serializable {
	private final String phone;
	private final String extension;

	public PhoneNumber(String phone) {
		this(phone, "");
	}

	public PhoneNumber(String phone, String extension) {
		this.phone = retainDigits(NVL.apply(phone, ""));
		this.extension = this.phone.length() == 0 ? null : extension;
	}

	public boolean equals(Object o) {
		if (!(o instanceof PhoneNumber))
			return false;
		PhoneNumber li = (PhoneNumber) o;
		return NVL.nullEquals(phone, li.phone)
				&& NVL.nullEquals(extension, li.extension);
	}

	public String getPhone() {
		return this.isValid() ? formatPhone(this.phone) : this.phone;
	}

	public String getExtension() {
		return this.extension;
	}

	public String toString() {
		return (this.extension == null || "".equals(this.extension)) ? this.phone : (this.phone + " x" + this.extension);
	}

	public boolean isValid() {
		return this.phone.length() == 10;
	}

	private static String formatPhone(String string) {
		StringBuffer sb = new StringBuffer(string);
		sb.insert(0, '(');
		sb.insert(4, ") ");
		sb.insert(9, '-');
		return sb.toString();
	}

	private static String retainDigits(String string) {
		StringBuffer clean = new StringBuffer();
		if (string == null)
			return "";
		for (int i = 0; i < string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				clean.append(string.charAt(i));
			}
		}
		return clean.toString();
	}

	public static String format(String string) {
		String digits = retainDigits(string);
		if (digits.length() == 10)
			return formatPhone(digits);
		else
			return digits;
	}
	
	public static String normalize(String string) {
		return retainDigits(string);
	}
}