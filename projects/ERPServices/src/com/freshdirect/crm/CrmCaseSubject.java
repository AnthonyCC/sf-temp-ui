package com.freshdirect.crm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.crm.ejb.CrmCaseSubjectDAO;
import com.freshdirect.enums.EnumModel;

public class CrmCaseSubject extends EnumModel implements TerminableI {

	private static final long	serialVersionUID	= 3627198720754854407L;

	private static Map<String, CrmCaseSubject> enums = null;

	public static final String CODE_PAYMENT_ERROR = "ASQ-001";
	public static final String CODE_AUTHORIZATION_FAILED = "ASQ-003";
	public static final String CODE_AVS_FAILED = "ASQ-004";
	public static final String CODE_ORDER_OVER_MAX = "ASQ-006";
	public static final String CODE_RETURN_REFUSAL = "ASQ-007";
	public static final String CODE_BAD_ACCOUNT_REVIEW = "ASQ-008";
	public static final String CODE_OVER_3_PLUS_DAYS = "ASQ-100";
	public static final String CODE_ASQ_MISC = "ASQ-100";
	public static final String CODE_NOT_SUBMITTED = "ASQ-100";

	public static final String CODE_STATUS = "OIQ-002";
	public static final String CODE_CANCEL = "OIQ-003";
	public static final String CODE_MODIFY = "OIQ-004";
	public static final String CODE_PRODUCT_ISSUE = "OIQ-005";
	public static final String CODE_PRODUCT = "OIQ-006";
	public static final String CODE_PROBLEM = "OIQ-100";
	
	public static final String CODE_CORPORATE_INFO = "GIQ-001";
	public static final String CODE_SERVICE_AVAILABILITY = "GIQ-002";
	public static final String CODE_GENERAL_INFO = "GIQ-100";
	public static final String CODE_NEW_PRODUCT_REQUEST = "GIQ-100";
	public static final String CODE_GIQ_MISC = "GIQ-100";
	
	public static final String CODE_PROMOTION = "PRQ-004";

	public static final String CODE_COMPLAINT = "TRQ-100";

	public static final String CODE_WEBSITE_PROBLEM = "WWQ-001";
	
	public static final String CODE_15RETENTION = "OUT-006";
	
	public static final String CODE_SHORTOUTITEM = "OUT-007";

	public static final String CODE_AUTO_BILL_PAYMENT_MISSING="DPQ-009";
	
	public static final String CODE_FIRST_ORDER_FOR_PICK_UP_USED_GC="ASQ-104";
	
	public static final String CODE_FIRST_ORDER_OVER_MAX_USED_GC="ASQ-105";
	
	public static final String CODE_GC_ORDER_OVER_MAX = "ASQ-104";
	
	public static final String CODE_GIFT_CARD_INFO = "GCQ-010";
	
	public static final String CODE_IPHONE_INFO = "IPQ-010";
	
	public static final String CODE_SECURITY_INTERNAL="SEQ-003";

	private final boolean obsolete;
	private final String queueCode;
	private final String priorityCode;
	private final boolean isCartonsRequired;

	public CrmCaseSubject(String queueCode, String code, String name, String descr, boolean obsolete, String priorityCode, boolean isCartonsRequired) {
		super(code, name, descr);
		this.obsolete = obsolete;
		this.queueCode = queueCode;
		this.priorityCode = priorityCode;
		this.isCartonsRequired = isCartonsRequired;
	}

	public boolean isObsolete() {
		return obsolete;
	}

	public CrmCaseQueue getQueue() {
		return CrmCaseQueue.getEnum(this.queueCode);
	}

	public CrmCasePriority getPriority() {
		return CrmCasePriority.getEnum(priorityCode);
	}

	public String getPriorityCode() {
		return priorityCode;
	}

	public static List<CrmCaseSubject> getSubjectsForQueue(String queueCode) {
		List<CrmCaseSubject> lst = new ArrayList<CrmCaseSubject>();
		for ( CrmCaseSubject sub : getEnumList() ) {
			if (sub.queueCode.equals(queueCode)) {
				lst.add(sub);
			}
		}
		Collections.sort(lst, COMP_NAME);
		return lst;
	}

	public static CrmCaseSubject getEnum(String code) {
		loadEnums();
		return enums.get(code);
	}

	public static Map<String, CrmCaseSubject> getEnumMap() {
		loadEnums();
		return Collections.unmodifiableMap(enums);
	}

	public static List<CrmCaseSubject> getEnumList() {
		loadEnums();
		return Collections.unmodifiableList(new ArrayList<CrmCaseSubject>(enums.values()));
	}

	private static void loadEnums() {
		if (enums == null) {
			enums = new HashMap<String, CrmCaseSubject>();
			List<CrmCaseSubject> lst = loadEnums(CrmCaseSubjectDAO.class);
			for ( CrmCaseSubject e : lst ) {
				enums.put(e.getCode(), e);
			}
		}
	}


	// [APPREQ-478]
	public boolean isCartonsRequired() {
		return this.isCartonsRequired;
	}
}
