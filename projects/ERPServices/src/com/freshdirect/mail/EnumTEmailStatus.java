package com.freshdirect.mail;

/*
 * Created on Jun 2, 2005
 */

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

/**
 * @author jng
 *
 */
public class EnumTEmailStatus extends Enum {
	public static final EnumTEmailStatus FAILED = new EnumTEmailStatus("FLD", "Failed to send Email");
	public static final EnumTEmailStatus SUCESS = new EnumTEmailStatus("COMP", "Success in sending email");
	public static final EnumTEmailStatus PROCESSING = new EnumTEmailStatus("PRC", "processing the email response");
	public static final EnumTEmailStatus NEW = new EnumTEmailStatus("NEW", "new email");
	public static final EnumTEmailStatus INFO = new EnumTEmailStatus("INFO", "Success in sending email, Recieved warning");
	
    private String description;

    protected EnumTEmailStatus(String name, String description) {
		super(name);
	    this.description = description;
	}
	
	public static EnumTEmailStatus getEnum(String type) {
		return (EnumTEmailStatus) getEnum(EnumTEmailStatus.class, type);
	}
		
	public static Map getEnumMap() {
		return getEnumMap(EnumTEmailStatus.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumTEmailStatus.class);
	}

	public static Iterator iterator() {
		return iterator(EnumTEmailStatus.class);
	}

	public String getDescription(){
		return this.description;
	}

	public String toString() {
		return this.description;		
	}	
}
