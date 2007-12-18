package com.freshdirect.crm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.crm.ejb.CrmCaseOriginDAO;
import com.freshdirect.enum.EnumModel;

public class CrmCaseOrigin extends EnumModel implements TerminableI {

	private static Map enums = null;

	public final static String CODE_PHONE = "PHONE";
	public final static String CODE_SYS = "SYS";
	public final static String CODE_EMAIL = "EMAIL";
	public final static String CODE_WEB_FORM = "WEB_FORM";

	private final boolean obsolete;

	public CrmCaseOrigin(String code, String name, String description, boolean obsolete) {
		super(code, name, description);
		this.obsolete = obsolete;
	}

	public boolean isObsolete() {
		return obsolete;
	}

	public static CrmCaseOrigin getEnum(String code) {
		loadEnums();
		return (CrmCaseOrigin) enums.get(code);
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
			List lst = loadEnums(CrmCaseOriginDAO.class);
			for (Iterator i = lst.iterator(); i.hasNext();) {
				CrmCaseOrigin e = (CrmCaseOrigin) i.next();
				enums.put(e.getCode(), e);
			}
		}
	}
	
}
