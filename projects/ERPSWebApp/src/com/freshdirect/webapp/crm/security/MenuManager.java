package com.freshdirect.webapp.crm.security;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.ExpiringReference;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.crm.security.MenuBuilder.MenuBuilderException;

public class MenuManager {
	
	private static final Category LOGGER = LoggerFactory.getInstance(MenuManager.class);
//	private MenuAccess menuAccess = null;
		
	private static MenuManager instance = null;
	private Date lastRefreshed;
	
	private ExpiringReference<MenuAccess> menuAccess = new ExpiringReference<MenuAccess>(FDStoreProperties.getCrmMenuRolesRefreshPeriod()*1000){
		protected MenuAccess load() {
			try {
				LOGGER.info("REFRESHING CRM MENU ACCESS ROLES AND LINKS: "+lastRefreshed);
//				MenuAccess menuAccess = MenuBuilder.buildRolesMenu("C:\\CrmSecurity\\projects\\ERPSWebApp\\bin\\com\\freshdirect\\webapp\\crm\\security\\crm_menu_new.xml");
				MenuAccess menuAccess = MenuBuilder.buildRolesMenu("crm_roles_menu.xml");
				lastRefreshed = new Date();
				LOGGER.info("REFRESHED CRM MENU ACCESS ROLES AND LINKS");
				return menuAccess;
			} catch (MenuBuilderException ex) {
				throw new FDRuntimeException(ex);
			}
		}
	};
	private MenuManager() {
		
		/*try {
			menuAccess = MenuBuilder.buildRolesMenu("C:\\CrmSecurity\\projects\\ERPSWebApp\\bin\\com\\freshdirect\\webapp\\crm\\security\\crm_menu_new.xml");
		} catch(Exception exp) {
			exp.printStackTrace();
		}*/
	}
	
	public static MenuManager getInstance() {
	      if(instance == null) {
	         instance = new MenuManager();
	      }
	      return instance;
	}
	
	public MenuAccess getUpdatedMenuAccess(){
		return menuAccess.get();
	}
	
	
	public Set getUserRoles() {
		if(menuAccess != null) {
			return getUpdatedMenuAccess().getMenuRoleMapping().keySet();
		} else {
			return new HashSet();
		}
	}
	
	public boolean hasAccess(ServletRequest request, String uri) {
		if(menuAccess != null) {
			String userRole =CrmSecurityManager.getUserRole(request, getUpdatedMenuAccess().getMenuRoleMapping().keySet());
			//String userRole = "TrnAdmin";
			Object tmpMapping = getUpdatedMenuAccess().getLinkRoleMapping().get(userRole);
			if(tmpMapping != null) {
				return ((List)tmpMapping).contains(uri);
			}
		}
		return true;
	}
	
	public boolean hasAccess(String userRole, String uri) {
		if(menuAccess != null) {
			Object tmpMapping = getUpdatedMenuAccess().getLinkRoleMapping().get(userRole);
			if(tmpMapping != null) {
				return ((List)tmpMapping).contains(uri);
			}
		}
		return true;
	}	
	
	
	public List getLinksForRole(ServletRequest request){
		if(menuAccess != null) {
			String userRole =CrmSecurityManager.getUserRole(request, getUpdatedMenuAccess().getMenuRoleMapping().keySet());
			return (List)getUpdatedMenuAccess().getLinkRoleMapping().get(userRole);
		}
		return null;		
	}
	
	public String getAutoApprovalLimit(String role){
		if(menuAccess != null) {			
			return (String)getUpdatedMenuAccess().getAutoApproveLimitRoleMapping().get(role);
		}
		return null;	
	}
	
	public String getApprovalLimit(String role){
		if(menuAccess != null) {			
			return (String)getUpdatedMenuAccess().getApproveLimitRoleMapping().get(role);
		}
		return null;	
	}
}
