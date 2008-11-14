package com.freshdirect.cms.ui.tapestry;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.BaseEngine;

import com.freshdirect.cms.application.CmsUser;

public class CmsEngine extends BaseEngine {

	public Object getVisit(IRequestCycle cycle) {
		CmsVisit visit = (CmsVisit) super.getVisit(cycle);
		HttpServletRequest request = cycle.getRequestContext().getRequest();
		Principal principal = request.getUserPrincipal();
		String name = principal == null ? System.getProperty("user.name") : principal.getName();
		CmsUser user = new CmsUser(name, request.isUserInRole("cms_editor"),request.isUserInRole("cms_admin"));
		visit.setUser(user);
		return visit;
	}

}