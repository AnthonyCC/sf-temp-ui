package com.freshdirect.crm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.crm.ejb.CrmAgentRoleDAO;
import com.freshdirect.enums.EnumModel;

public class CrmAgentRole extends EnumModel {

	private static final long	serialVersionUID	= 9198673972727902109L;
	private String ldapRoleName="";
	
	private static Map<String, CrmAgentRole> enums = null;
	public static final String SYS_CODE = "SYS";
	public static final String GUE_CODE = "GUE";//obsolete
	public static final String CSR_CODE = "CSR";
	public static final String TRN_CODE = "TRN";
	public static final String ASV_CODE = "ASV";//obsolete
	public static final String SUP_CODE = "SUP";
	public static final String ADM_CODE = "ADM";
	public static final String EXC_CODE = "EXC";//obsolete
	public static final String CSRH_CODE = "CSRH";//obsolete
	
	//New CRM roles
	public static final String DEV_CODE = "DEV";
	public static final String QA_CODE = "QA";
	public static final String SEC_CODE = "SEC";
	public static final String FIN_CODE = "FIN";
	public static final String BUS_CODE = "BUS";
	public static final String ETS_CODE = "ETS";
	public static final String OPS_CODE = "OPS";
	public static final String SOP_CODE = "SOP";
	public static final String SCS_CODE = "SCS";
	public static final String COS_CODE = "COS";
	public static final String MOP_CODE = "MOP";
	public static final String ERP_CODE = "ERP";
	public static final String NCS_CODE = "NCS";
	public static final String TRNSP_CODE="TRNSP";
	
	

	public CrmAgentRole(String code, String name, String description) {
		super(code, name, description);
	}
	
	public CrmAgentRole(String code, String name, String description, String ldapRoleName) {
		super(code, name, description);
		this.ldapRoleName = ldapRoleName;
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
	
	public static CrmAgentRole getEnumByLDAPRole(String ldapRoleName) {
		CrmAgentRole crmAgentRole = null;
		List list = getEnumList();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			CrmAgentRole role = (CrmAgentRole) iterator.next();
			if(null !=role.ldapRoleName && role.ldapRoleName.equalsIgnoreCase(ldapRoleName)){
				crmAgentRole = role;
				break;
			}			
		}
		return crmAgentRole;
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
	
	public static boolean isSupervisorOrUp(CrmAgentRole role){
		boolean isSupervisorOrUp = false;
		if(null != role){
			if(role.equals(CrmAgentRole.getEnum(CrmAgentRole.SUP_CODE))||role.equals(CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE))||role.equals(CrmAgentRole.getEnum(CrmAgentRole.DEV_CODE))||role.equals(CrmAgentRole.getEnum(CrmAgentRole.QA_CODE))){
				isSupervisorOrUp = true;
			}
		}
		return isSupervisorOrUp;
	}

	public static boolean isSecurityOrAdmRole(CrmAgentRole role){
		boolean isSecurityOrAdmUser = false;
		if(null != role){
			if(role.equals(CrmAgentRole.getEnum(CrmAgentRole.SEC_CODE))||role.equals(CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE))){
				isSecurityOrAdmUser = true;
			}
		}
		return isSecurityOrAdmUser;
	}
	
	public static boolean isPrivateCaseAllowedRole(CrmAgentRole role){
		boolean isPrivateCaseAllowedRole = false;
		if(null != role){
			if(role.equals(CrmAgentRole.getEnum(CrmAgentRole.SEC_CODE))||role.equals(CrmAgentRole.getEnum(CrmAgentRole.ADM_CODE))||role.equals(CrmAgentRole.getEnum(CrmAgentRole.DEV_CODE))||role.equals(CrmAgentRole.getEnum(CrmAgentRole.QA_CODE))||role.equals(CrmAgentRole.getEnum(CrmAgentRole.SOP_CODE))){
				isPrivateCaseAllowedRole = true;
			}
		}
		return isPrivateCaseAllowedRole;
	}
	
	
	public String getLdapRoleName() {
		return ldapRoleName;
	}

	public void setLdapRoleName(String ldapRoleName) {
		this.ldapRoleName = ldapRoleName;
	}

}
