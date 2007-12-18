package com.freshdirect.cms.ui.tapestry;

import java.security.Principal;
import java.util.Set;

import javax.security.auth.Subject;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.BaseEngine;

import weblogic.security.Security;
import weblogic.security.principal.WLSUserImpl;

import com.freshdirect.cms.application.CmsUser;

public class CmsEngine extends BaseEngine {

	public Object getVisit(IRequestCycle cycle) {
		CmsVisit visit = (CmsVisit) super.getVisit(cycle);
		Principal principal = getCurrentPrincipal();
		String name = principal == null ? System.getProperty("user.name") : principal.getName();
		CmsUser user = new CmsUser(name);
		visit.setUser(user);
		return visit;
	}

	// FIXME how to get Prinicpal without Weblogic specific code?  
	private Principal getCurrentPrincipal() {
		Subject subject = Security.getCurrentSubject();
		if (subject == null) {
			return null;
		}
		Set principals = subject.getPrincipals(WLSUserImpl.class);
		if (principals.isEmpty()) {
			return null;
		}
		return (Principal) principals.iterator().next();
	}

}