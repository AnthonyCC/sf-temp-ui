package com.freshdirect.crm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.crm.ejb.CrmCasePriorityDAO;
import com.freshdirect.enum.EnumModel;

public class CrmCasePriority extends EnumModel {
	private static Map enums = null;

	public static final String CODE_HIGH = "HI";
	public static final String CODE_MEDIUM = "MD";
	public static final String CODE_LOW = "LO";

	private final int priority;

	public CrmCasePriority(String code, String name, String description, int priority) {
		super(code, name, description);
		this.priority = priority;
	}

	public int getPriority() {
		return this.priority;
	}

	public static CrmCasePriority getEnum(String code) {
		loadEnums();
		return (CrmCasePriority) enums.get(code);
	}

	public static Map getEnumMap() {
		loadEnums();
		return Collections.unmodifiableMap(enums);
	}

	public static List getEnumList() {
		loadEnums();
		return Collections.unmodifiableList(new ArrayList(enums.values()));
	}

	private static void loadEnums() {
		if (enums == null) {
			enums = new HashMap();
			List lst = loadEnums(CrmCasePriorityDAO.class);
			for (Iterator i = lst.iterator(); i.hasNext();) {
				CrmCasePriority e = (CrmCasePriority) i.next();
				enums.put(e.getCode(), e);
			}
		}
	}

}
