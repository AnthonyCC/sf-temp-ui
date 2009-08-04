package com.freshdirect.customer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumSendCreditEmail extends Enum implements java.io.Serializable {

	private final String description;

	public final static EnumSendCreditEmail DONT_SEND = new EnumSendCreditEmail("N", "Do not send");
	public final static EnumSendCreditEmail SEND_ON_COMPLAINT_CREATION = new EnumSendCreditEmail("C", "Send on creation");
	public final static EnumSendCreditEmail SEND_ON_APPROVAL = new EnumSendCreditEmail("A", "Send on approval");

	private EnumSendCreditEmail(String code, String desc) {
		super(code);
		this.description = desc;
	}

	public static EnumSendCreditEmail getEnum(String name) {
		return (EnumSendCreditEmail) getEnum(EnumSendCreditEmail.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumSendCreditEmail.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumSendCreditEmail.class);
	}

	public static Iterator iterator() {
		return iterator(EnumSendCreditEmail.class);
	}

	public String getDescription() {
		return this.description;
	}

	public String toString() {
		return "[EnumSendEmail  code: " + getName() + " Description: " + description + "]";
	}

}