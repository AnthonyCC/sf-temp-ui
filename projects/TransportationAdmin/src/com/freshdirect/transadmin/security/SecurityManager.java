package com.freshdirect.transadmin.security;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class SecurityManager {
			
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
		
		return null;
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
		if("accessdenied.do".equalsIgnoreCase(uri)) {
			return true;
		}
		return MenuManager.getInstance().hasAccess(request, uri);
	}

}
