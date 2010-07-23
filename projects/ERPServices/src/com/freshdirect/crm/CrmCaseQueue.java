package com.freshdirect.crm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.crm.ejb.CrmCaseQueueDAO;
import com.freshdirect.enums.EnumModel;

public class CrmCaseQueue extends EnumModel implements TerminableI {

	private static final long	serialVersionUID	= 4599127865108443201L;

	private static Map<String, CrmCaseQueue> enums = null;
	
	public static final String CODE_ASQ = "ASQ";
	public static final String CODE_TRQ = "TRQ";
	
	public static final String CODE_DSQ = "DSQ";

	private final boolean obsolete;

	public CrmCaseQueue(String code, String name, String description, boolean obsolete) {
		super(code, name, description);
		this.obsolete = obsolete;
	}

	public boolean isObsolete() {
		return obsolete;
	}

	public List<CrmCaseSubject> getSubjects() {
		return CrmCaseSubject.getSubjectsForQueue(this.getCode());
	}
	
	public static boolean isTrnQueue(String queue) {		
		return "DSQ".equalsIgnoreCase(queue) || "LDQ".equalsIgnoreCase(queue) 
					|| "TCQ".equalsIgnoreCase(queue) || "ORQ".equalsIgnoreCase(queue);
	}

	public static CrmCaseQueue getEnum(String code) {
		loadEnums();
		return enums.get(code);
	}

	public static Map<String, CrmCaseQueue> getEnumMap() {
		loadEnums();
		return Collections.unmodifiableMap(enums);
	}

	public static List<CrmCaseQueue> getEnumList() {
		loadEnums();
		return Collections.unmodifiableList(new ArrayList<CrmCaseQueue>(enums.values()));
	}

	private static void loadEnums() {
		if (enums == null) {
			enums = new HashMap<String, CrmCaseQueue>();
			List<CrmCaseQueue> lst = loadEnums(CrmCaseQueueDAO.class);
			for ( CrmCaseQueue e : lst ) {
				enums.put(e.getCode(), e);
			}
		}
	}

}
