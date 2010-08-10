package com.freshdirect.transadmin.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class SecurityManager {
	
	private static List<String> UNSECURED_URL = new ArrayList<String>();
	
	static {
		UNSECURED_URL.add("accessdenied.do");
		UNSECURED_URL.add("download.do");
		UNSECURED_URL.add("routenumber.do");
		UNSECURED_URL.add("viewfile.do");
		UNSECURED_URL.add("unassignedroute.do");
		UNSECURED_URL.add("showroute.do");
		UNSECURED_URL.add("drivingdirection.do");
		UNSECURED_URL.add("unassignedactiveemployees.do");
		UNSECURED_URL.add("dispatchDashboardScreen.do");
		UNSECURED_URL.add("gmapexport.do");
		UNSECURED_URL.add("gpsadmin.do");
		UNSECURED_URL.add("cutoffreport.do");
		UNSECURED_URL.add("sapupload.do"); 
		UNSECURED_URL.add("communityreport.do");    
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
	
    public static boolean isUserAdmin(ServletRequest request) {
		return "TrnAdmin".equalsIgnoreCase(getUserRole(request));
	}
    
	private static boolean isUserInRole(String role, ServletRequest request) {
		return ((HttpServletRequest)request).isUserInRole(role);
	}

		
	public static String getUserName(ServletRequest request) {
		return ((HttpServletRequest)request).getRemoteUser();
	}
	
	public static boolean hasAccessToPage(ServletRequest request, String uri) {
		if(UNSECURED_URL.contains(uri) || (uri != null && uri.endsWith(".ax"))) {
			return true;
		}
		return MenuManager.getInstance().hasAccess(request, uri);
	}

}
