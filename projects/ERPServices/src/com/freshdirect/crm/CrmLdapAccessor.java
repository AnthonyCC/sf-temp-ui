package com.freshdirect.crm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import weblogic.jndi.Environment;
import weblogic.management.MBeanHome;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.SecurityConfigurationMBean;
import weblogic.management.security.RealmMBean;
import weblogic.management.security.authentication.AuthenticationProviderMBean;
import weblogic.management.utils.InvalidCursorException;
import weblogic.management.utils.InvalidParameterException;
import weblogic.management.utils.NotFoundException;
import weblogic.security.providers.authentication.ActiveDirectoryAuthenticatorMBean;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.ExpiringReference;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CrmLdapAccessor {
	
	private static final Category LOGGER = LoggerFactory.getInstance(CrmLdapAccessor.class);
	private static CrmLdapAccessor crmLdapAccessor = null;
	private static RealmMBean realmMbean = null;
	private static Date lastRefreshed;
	private static Map<CrmAgentRole, List<String>> ldapMap = new HashMap<CrmAgentRole, List<String>>();
//	private static Map<CrmAgentRole, List<String>> ldapGroupMembersMap = new HashMap<CrmAgentRole, List<String>>();
	private static ExpiringReference<Map<CrmAgentRole, List<String>>> ldapGroupMembersMap = new ExpiringReference<Map<CrmAgentRole, List<String>>>(FDStoreProperties.getCrmLDAPUsersRefreshPeriod()*1000){
		protected Map<CrmAgentRole, List<String>> load() {			
				LOGGER.info("REFRESHING CRM MENU ACCESS ROLES AND LINKS: ");
				Map<CrmAgentRole, List<String>> map = populateMap();
				
				lastRefreshed = new Date();
				LOGGER.info("REFRESHED CRM MENU ACCESS ROLES AND LINKS: "+lastRefreshed);
				return map;			
		}
	};
	private CrmLdapAccessor(){
	}
	
	
	public static void init(){
		populateMap();
	}
	
	public static Map<CrmAgentRole, List<String>> getUpdatedLdapGroupMembersMap(){
		 Map<CrmAgentRole, List<String>> lLdapMap = ldapGroupMembersMap.get();
		 if(null != lLdapMap && !lLdapMap.isEmpty()){
			ldapMap = lLdapMap; 
		 }
		 return ldapMap;
	}
	private static Map<CrmAgentRole, List<String>> populateMap(){
		Context ctx = null;
		try {
			ctx = getContext();
			MBeanHome mbeanHome = (MBeanHome) ctx.lookup(MBeanHome.LOCAL_JNDI_NAME);    
			DomainMBean domain = mbeanHome.getActiveDomain();
			SecurityConfigurationMBean secConf = domain.getSecurityConfiguration();
			RealmMBean realmMbean =secConf.findDefaultRealm();
			Map<CrmAgentRole, List<String>> lLdapGroupMembersMap = new HashMap<CrmAgentRole, List<String>>(); 
			AuthenticationProviderMBean providerMbeans[] = realmMbean.getAuthenticationProviders();
			for (int i = 0; i < providerMbeans.length; i++) {
				AuthenticationProviderMBean providerMBean = providerMbeans[i];
				if(providerMBean instanceof ActiveDirectoryAuthenticatorMBean){
					ActiveDirectoryAuthenticatorMBean activeDirectoryMBean =(ActiveDirectoryAuthenticatorMBean)providerMBean;
					//All the CRM access group's names should start with 'CRM'.
					String groups =activeDirectoryMBean.listGroups("CRM_*", 0);
					while(activeDirectoryMBean.haveCurrent(groups)){
						String groupName =activeDirectoryMBean.getCurrentName(groups);
						CrmAgentRole crmAgentRole = CrmAgentRole.getEnumByLDAPRole(groupName);
						System.out.println(groupName);
						List<String> members = null;
						if(null != crmAgentRole){
							if(lLdapGroupMembersMap.containsKey(crmAgentRole)){
								 members= lLdapGroupMembersMap.get(crmAgentRole);
							}
							if(null == members){
								members = new ArrayList<String>();
							}
							String groupMembers = getMembers(activeDirectoryMBean,
									groupName, members);
							Collections.sort(members);
							lLdapGroupMembersMap.put(crmAgentRole, members);
						}
						activeDirectoryMBean.advance(groups);
					}
					break;
				}
			}
			return lLdapGroupMembersMap;
		} catch (InvalidParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidCursorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try{
			if(null != ctx){
				ctx.close();
			}
			}catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}


	private static String getMembers(
			ActiveDirectoryAuthenticatorMBean activeDirectoryMBean,
			String groupName, List<String> members) throws NotFoundException,
			InvalidParameterException, InvalidCursorException {
		String groupMembers ="";
		if(activeDirectoryMBean.groupExists(groupName)){
			groupMembers = activeDirectoryMBean.listGroupMembers(groupName,"*",0);    			
			while(activeDirectoryMBean.haveCurrent(groupMembers)){    			
				String memberName = activeDirectoryMBean.getCurrentName(groupMembers);
				getMembers(activeDirectoryMBean,memberName, members);
//				System.out.println("      "+memberName);
				if(!activeDirectoryMBean.groupExists(memberName) && !members.contains(memberName)){
					members.add(memberName);
				}
				activeDirectoryMBean.advance(groupMembers);
			}
		}
		return groupMembers;
	}


	private static Context getContext() throws NamingException {
//		
			Environment env = new Environment();
//			String url ="iiop://"+"crm01.stdev04.nyc1.freshdirect.com"+":7001/";
//			env.setProviderUrl(url);
			env.setProviderUrl(FDStoreProperties.getCrmLDAPPrimaryHostName());
			env.setSecurityPrincipal("weblogic");
			env.setSecurityCredentials("weblogic");
			Context ctx = env.getInitialContext();
			
//		}
		return ctx;
	}
	
	public static void main(String[] args){
		CrmLdapAccessor.init();
		System.out.println(getUpdatedLdapGroupMembersMap().size());
	}


	public static Map<CrmAgentRole, List<String>> getLdapGroupMembersMap() {
		if(getUpdatedLdapGroupMembersMap()== null || getUpdatedLdapGroupMembersMap().isEmpty()){CrmLdapAccessor.init();}
		return getUpdatedLdapGroupMembersMap();
	}


	/*public static void setLdapGroupMembersMap(
			Map<CrmAgentRole, List<String>> ldapGroupMembersMap) {
		CrmLdapAccessor.ldapGroupMembersMap = ldapGroupMembersMap;
	}*/

	
}
