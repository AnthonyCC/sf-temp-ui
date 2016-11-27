package com.freshdirect.crm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.crm.ejb.CrmDepartmentDAO;
import com.freshdirect.enums.EnumModel;

public class CrmDepartment extends EnumModel implements TerminableI {

	private static Map enums = null;

	private final boolean obsolete;

	public CrmDepartment(String code, String name, String description, boolean obsolete) {
		super(code, name, description);
		this.obsolete = obsolete;
	}

	public static CrmDepartment getEnum(String code) {
		loadEnums();
		return (CrmDepartment) enums.get(code);
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
			List lst = loadEnums(CrmDepartmentDAO.class);
			for (Iterator i = lst.iterator(); i.hasNext();) {
				CrmDepartment e = (CrmDepartment) i.next();
				enums.put(e.getCode(), e);
			}
		}
	}

	public boolean isObsolete() {
		return this.obsolete;
	}

}
