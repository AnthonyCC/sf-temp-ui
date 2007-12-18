package com.freshdirect.crm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.crm.ejb.CrmAgentRoleDAO;
import com.freshdirect.enum.EnumModel;

public class CrmAgentRole extends EnumModel {

	private static Map enums = null;
	public static final String SYS_CODE = "SYS";
	public static final String GUE_CODE = "GUE";
	public static final String CSR_CODE = "CSR";
	public static final String TRN_CODE = "TRN";
	public static final String ASV_CODE = "ASV";
	public static final String SUP_CODE = "SUP";
	public static final String ADM_CODE = "ADM";
	public static final String EXC_CODE = "EXC";
	public static final String CSRH_CODE = "CSRH";

	public CrmAgentRole(String code, String name, String description) {
		super(code, name, description);
	}
    
	public static CrmAgentRole getEnum(String code) {
		loadEnums();
		return (CrmAgentRole) enums.get(code);
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
			List lst = loadEnums(CrmAgentRoleDAO.class);
			for (Iterator i = lst.iterator(); i.hasNext();) {
				CrmAgentRole e = (CrmAgentRole) i.next();
				enums.put(e.getCode(), e);
			}
		}
	}

}
