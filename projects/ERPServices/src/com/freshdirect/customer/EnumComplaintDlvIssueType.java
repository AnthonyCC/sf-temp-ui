package com.freshdirect.customer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.crm.ejb.CrmComplaintDlvTypeDAO;
import com.freshdirect.enums.EnumModel;

public class EnumComplaintDlvIssueType extends EnumModel {
	public static final String CODE_DAMG = "Damaged";
	public static final String CODE_LATE = "Late";
	public static final String CODE_MISS = "Missing";
	public static final String CODE_QTY = "Quality";

	
	private static Map<String,EnumComplaintDlvIssueType> enums = null;
	
	public EnumComplaintDlvIssueType(String code, String name,
			String description) {
		super(code, name, description);
	}

	private static final long serialVersionUID = 5817878443322520830L;

	public static EnumComplaintDlvIssueType getEnum(String code) {
		loadEnums();
		return (EnumComplaintDlvIssueType) enums.get(code);
	}

	public static Map<String,EnumComplaintDlvIssueType> getEnumMap() {
		loadEnums();
		return Collections.unmodifiableMap(enums);
	}

	public static List<EnumComplaintDlvIssueType> getEnumList() {
		loadEnums();
		return Collections.unmodifiableList(new ArrayList<EnumComplaintDlvIssueType>(enums.values()));
	}

	private static void loadEnums() {
		if (enums == null) {
			enums = new HashMap<String,EnumComplaintDlvIssueType>();
			List<EnumComplaintDlvIssueType> lst = loadEnums(CrmComplaintDlvTypeDAO.class);
			for (Iterator<EnumComplaintDlvIssueType> i = lst.iterator(); i.hasNext();) {
				EnumComplaintDlvIssueType e = (EnumComplaintDlvIssueType) i.next();
				enums.put(e.getCode(), e);
			}
		}
	}
}

