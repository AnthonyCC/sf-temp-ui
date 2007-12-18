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
		this.extension = extension;
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
		char[] chars = string.toCharArray();
		StringBuffer clean = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			if (Character.isDigit(chars[i])) {
				clean.append(chars[i]);
			}
		}
		return clean.toString();
	}

}