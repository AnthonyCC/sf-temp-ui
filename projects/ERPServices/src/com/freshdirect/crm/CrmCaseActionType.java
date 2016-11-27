package com.freshdirect.crm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.crm.ejb.CrmCaseActionTypeDAO;
import com.freshdirect.enums.EnumModel;

public class CrmCaseActionType extends EnumModel {

	private static Map enums = null;

	public final static String CODE_EDIT = "CAS_EDT";
	public final static String CODE_NOTE = "NOTE";
	public final static String CODE_CLOSE = "CLOSE";
	public final static String CODE_ESCRVW = "ESCRVW";
	public final static String CODE_RETURN = "RETURN";
	public final static String CODE_APPROV = "APPROV";
	public final static String CODE_EML_OUT = "EML_OUT";
	public final static String CODE_CAL = "CAL";
	public final static String CODE_CAL_MSG = "CAL_MSG";
	public final static String CODE_CAL_SPK = "CAL_SPK";
	public final static String CODE_CUST_CAL = "CUST_CAL";
	public final static String CODE_ACT_DM_R = "ACT_DM_R";
	public final static String CODE_ACT_DM_A = "ACT_DM_A";

	public CrmCaseActionType(String code, String name, String description) {
		super(code, name, description);
	}

	public static CrmCaseActionType getEnum(String code) {
		loadEnums();
		return (CrmCaseActionType) enums.get(code);
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
			List lst = loadEnums(CrmCaseActionTypeDAO.class);
			for (Iterator i = lst.iterator(); i.hasNext();) {
				CrmCaseActionType e = (CrmCaseActionType) i.next();
				enums.put(e.getCode(), e);
			}
		}
	}
	
}
