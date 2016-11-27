package com.freshdirect.fdstore.ewallet;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.ValuedEnum;

/**
 * @author Aniwesh Vatsal
 *
 */
public class EnumEwalletType extends ValuedEnum {

	private static final long serialVersionUID = -8962535387638057138L;

	public final static EnumEwalletType MP = new EnumEwalletType("MP", "MasterPass", 1);
	
	public final static EnumEwalletType PP = new EnumEwalletType("PP", "PayPal", 2);
	
	private String description;
	
	private EnumEwalletType(String code, String description, int id) {
		super(code, id);
		this.description = description;
	}
	
	public static EnumEwalletType getEnum(String code) {
		return (EnumEwalletType) getEnum(EnumEwalletType.class, code);
	}

	public static EnumEwalletType getEnum(int id) {
		return (EnumEwalletType) getEnum(EnumEwalletType.class, id);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumEwalletType.class);
	}

	public static List<EnumEwalletType> getEnumList() {
		return getEnumList(EnumEwalletType.class);
	}

	public static Iterator<EnumEwalletType> iterator() {
		return iterator(EnumEwalletType.class);
	}
	
	public String getDescription(){
		return this.description;
	}

	public String toString() {
		return this.description;		
	}
}
