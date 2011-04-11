package com.freshdirect.erp.security;

import javax.servlet.http.HttpServletRequest;

public class SecurityManager {
	
    public static boolean isUserAdmin(HttpServletRequest request) {
		return request.isUserInRole("ErpsyAdminGrp");
	}

}
