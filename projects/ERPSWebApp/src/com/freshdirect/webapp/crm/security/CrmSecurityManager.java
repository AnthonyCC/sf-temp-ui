package com.freshdirect.webapp.crm.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class CrmSecurityManager {
	
	private static List<String> UNSECURED_URL = new ArrayList<String>();
	
	static {
		UNSECURED_URL.add("are-you-alive-ping-from-workshop");
		UNSECURED_URL.add("no_auth.jsp");
		UNSECURED_URL.add("login.jsp");
		UNSECURED_URL.add("main_index.jsp");
		UNSECURED_URL.add("logout.jsp");
		UNSECURED_URL.add("clear_session.jsp");    
		UNSECURED_URL.add("close_window.jsp");
		UNSECURED_URL.add("error_cust_case_mismatch.jsp");
		UNSECURED_URL.add("error.jsp");
	}
	
	public static String getUserRole(ServletRequest request, Set roles) {		
		if(roles != null) {
			Iterator _itr = roles.iterator();
			
			while(_itr.hasNext()) {
				String _tmpRole = (String)_itr.next();
				if(isUserInRole(_tmpRole, request)) {
					return _tmpRole; 
				}
			}
		}		
		return  null;
    }
	
	public static String getUserRole(ServletRequest request) {
		Set roles = MenuManager.getInstance().getUserRoles();
		if(roles != null) {
			Iterator _itr = roles.iterator();
			
			while(_itr.hasNext()) {
				String _tmpRole = (String)_itr.next();
				if(isUserInRole(_tmpRole, request)) {
					return _tmpRole; 
				}
			}
		}		
		return null;
    }	
   
    
	private static boolean isUserInRole(String role, ServletRequest request) {
		return ((HttpServletRequest)request).isUserInRole(role);
	}

		
	public static String getUserName(ServletRequest request) {
		return ((HttpServletRequest)request).getRemoteUser();
	}
	
	public static boolean hasAccessToPage(ServletRequest request, String uri) {
		if(UNSECURED_URL.contains(uri) || (uri != null && (uri.endsWith(".css")||uri.endsWith(".js")||uri.endsWith(".jspf")||uri.endsWith(".gif")||uri.endsWith(".ico")||uri.endsWith(".xls")))) {
			return true;
		}
		return MenuManager.getInstance().hasAccess(request, uri);
	}
	
	public static boolean hasAccessToPage(HttpServletRequest request) {
		String uri = request.getRequestURI();
		if((uri != null && (uri.indexOf("/includes")>=0 || uri.indexOf("/postbacks")>=0|| uri.indexOf("/template")>=0 ))) {
			return true;
		}
		//Do other checks if required after this check.
		return false;
	}
	
	public static boolean hasAccessToPage(String userRole, String uri) {		
		return MenuManager.getInstance().hasAccess(userRole, uri);
	}
	
	public static boolean isPromoAllAccessUser(String userRole){
		return MenuManager.getInstance().hasAccess(userRole, "promo_view.jsp") && MenuManager.getInstance().hasAccess(userRole, "promo_create.jsp") && MenuManager.getInstance().hasAccess(userRole, "promo_edit.jsp") && MenuManager.getInstance().hasAccess(userRole, "promo_publish.jsp");
	}

	public static boolean isAutoApproveAuthorized(String userRole){
		return MenuManager.getInstance().hasAccess(userRole, "autoApprove");
	}
	
	public static boolean isApproveAuthorized(String userRole){
		return MenuManager.getInstance().hasAccess(userRole, "approveCredit");
	}
	
	public static boolean isRejectAuthorized(String userRole){
		return MenuManager.getInstance().hasAccess(userRole, "rejectCredit");
	}
	
	public static Double getAutoApprovalLimit(String userRole){
		Double limit = null;
		String limitStr = MenuManager.getInstance().getAutoApprovalLimit(userRole);
		if(null != limitStr){
			limit = Double.parseDouble(limitStr);
		}
		return limit;
	}
	
	public static Double getApprovalLimit(String role){
		Double limit = null;
		String limitStr = MenuManager.getInstance().getApprovalLimit(role);
		if(null != limitStr){
			limit = Double.parseDouble(limitStr);
		}
		return limit;
	}
}
