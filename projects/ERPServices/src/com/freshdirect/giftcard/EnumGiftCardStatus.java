package com.freshdirect.giftcard;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EnumGiftCardStatus extends Enum {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4199200116057882779L;
	public static final EnumGiftCardStatus UNKNOWN = new EnumGiftCardStatus("U", "Unknown");
	public static final EnumGiftCardStatus ACTIVE = new EnumGiftCardStatus("A", "Active");
	public static final EnumGiftCardStatus INACTIVE = new EnumGiftCardStatus("I", "InActive");
	public static final EnumGiftCardStatus ZERO_BALANCE = new EnumGiftCardStatus("Z", "Zero");
	
	private final String description;

	public EnumGiftCardStatus(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	@JsonCreator
	public static EnumGiftCardStatus getEnum(@JsonProperty("name") String name) {
		return (EnumGiftCardStatus) getEnum(EnumGiftCardStatus.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumGiftCardStatus.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumGiftCardStatus.class);
	}

	public static Iterator iterator() {
		return iterator(EnumGiftCardStatus.class);
	}
}