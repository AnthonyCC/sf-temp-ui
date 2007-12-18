package com.freshdirect.crm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.crm.ejb.CrmCaseStateDAO;
import com.freshdirect.enum.EnumModel;

public class CrmCaseState extends EnumModel {

	private static Map enums = null;

	public final static String CODE_NEW = "NEW";
	public final static String CODE_OPEN = "OPEN";
	public final static String CODE_CLOSED = "CLSD";
	public final static String CODE_REVIEW = "REVW";
	public final static String CODE_APPROVED = "APVD";

	public CrmCaseState(String code, String name, String description) {
		super(code, name, description);
	}

	public static CrmCaseState getEnum(String code) {
		loadEnums();
		return (CrmCaseState) enums.get(code);
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
			List lst = loadEnums(CrmCaseStateDAO.class);
			for (Iterator i = lst.iterator(); i.hasNext();) {
				CrmCaseState e = (CrmCaseState) i.next();
				enums.put(e.getCode(), e);
			}
		}
	}

}
