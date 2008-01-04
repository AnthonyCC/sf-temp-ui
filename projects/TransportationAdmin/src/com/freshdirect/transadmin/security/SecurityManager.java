package com.freshdirect.transadmin.security;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class SecurityManager {
	
	public final static String GUEST = "TrnGuest";
	public final static String ADMIN = "TrnAdmin";
	public final static String DISPATCH = "TrnDispatch";
	
	public static String getUserRole(ServletRequest request){
		if (isUserAdmin(request)) {
			return ADMIN;
		}
		if (isUserDispatch(request)) {
			return DISPATCH;
		}
		return GUEST;
    }
	
	private static boolean isUserInRole(String role, ServletRequest request) {
		return ((HttpServletRequest)request).isUserInRole(role);
	}

	public static boolean isUserGuest(ServletRequest request) {
		return isUserInRole(GUEST, request);
	}
	public static boolean isUserDispatch(ServletRequest request) {
		return isUserInRole(DISPATCH, request);
	}
	
	public static boolean isUserAdmin(ServletRequest request) {
		return isUserInRole(ADMIN, request);
	}
	
	public static String getUserName(ServletRequest request) {
		return ((HttpServletRequest)request).getRemoteUser();
	}

}
