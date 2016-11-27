package com.freshdirect.giftcard;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;
/**
*
* @author  skrishnasamy
* @version 1.0
* @created 05-Jun-2006
* 
*/
public class EnumGiftCardFailureType  extends Enum {
	
	public static final EnumGiftCardFailureType INVALID_GIFT_CERTIFICATE = new EnumGiftCardFailureType("INVALID", "Invalid Card number");
	public static final EnumGiftCardFailureType CARD_IN_USE = new EnumGiftCardFailureType("INUSE", "Card In Use");

	private final String displayName;
	private EnumGiftCardFailureType(String name, String displayName) {
		super(name);
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public static EnumGiftCardFailureType getEnum(String name) {
		return (EnumGiftCardFailureType) getEnum(EnumGiftCardFailureType.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumGiftCardFailureType.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumGiftCardFailureType.class);
	}

	public static Iterator iterator() {
		return iterator(EnumGiftCardFailureType.class);
	}
}
