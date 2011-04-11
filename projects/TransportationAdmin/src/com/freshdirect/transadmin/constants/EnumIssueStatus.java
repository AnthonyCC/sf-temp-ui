package com.freshdirect.transadmin.constants;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enums.Enum;

public class EnumIssueStatus  extends Enum {
	
	public static final EnumIssueStatus OPEN = new EnumIssueStatus("Open","OPEN");

    public static final EnumIssueStatus RESOLVED = new EnumIssueStatus("Resolved","RESOLVED");
    
    public static final EnumIssueStatus VERIFIED = new EnumIssueStatus("Verified","VERIFIED");
    
    public static final EnumIssueStatus REVERIFIED = new EnumIssueStatus("Re-Verified","RE-VERIFIED");
    
    private final String description;

	public EnumIssueStatus(String name, String description) {
		super(name);
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public static EnumIssueStatus getEnum(String name) {
		return (EnumIssueStatus) getEnum(EnumIssueStatus.class, name);
	}

	public static Map getEnumMap() {
		return getEnumMap(EnumIssueStatus.class);
	}

	public static List getEnumList() {
		return getEnumList(EnumIssueStatus.class);
	}

	public static Iterator iterator() {
		return iterator(EnumIssueStatus.class);
	}

	public String toString() {
		return this.getName();
	}
}
