package com.freshdirect.crm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.crm.ejb.CrmAgentRoleDAO;
import com.freshdirect.enums.EnumModel;

public class CrmAgentRole extends EnumModel {

	private static final long	serialVersionUID	= 9198673972727902109L;
	
	private static Map<String, CrmAgentRole> enums = null;
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
		return enums.get(code);
	}

	public static Map<String, CrmAgentRole> getEnumMap() {
		loadEnums();
		return Collections.unmodifiableMap(enums);
	}

	public static List<CrmAgentRole> getEnumList() {
		loadEnums();
		return Collections.unmodifiableList(new ArrayList<CrmAgentRole>(enums.values()));
	}

	private static void loadEnums() {
		if (enums == null) {
			enums = new HashMap<String, CrmAgentRole>();
			List<CrmAgentRole> lst = loadEnums(CrmAgentRoleDAO.class);
			for ( CrmAgentRole e : lst ) {
				enums.put(e.getCode(), e);
			}
		}
	}

}
