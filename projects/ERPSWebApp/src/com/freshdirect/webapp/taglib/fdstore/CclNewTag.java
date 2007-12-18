/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.log.LoggerFactory;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class CclNewTag extends com.freshdirect.framework.webapp.TagSupport {
    
    private static final String INEXPERIENCED_USER_FRAGMENT = "/common/template/includes/ccl_inexperienced.jspf";

	private static Category LOGGER = LoggerFactory.getInstance( CclNewTag.class );

	private String template = null;
	
	public void setTemplate(String template) {
		this.template = template;
	}
	
    public int doStartTag() throws JspException {
        HttpSession session = pageContext.getSession();
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);	
		
    	if (user != null && user.isCCLEnabled() && user.isCCLInExperienced()) {
    		try {
    			if (template != null) {
    				pageContext.include(template);
    			} else {
    				pageContext.include(INEXPERIENCED_USER_FRAGMENT);
    			}
			} catch (ServletException e) {
				LOGGER.info(e);
				throw new JspException(e);
			} catch (IOException e) {
				LOGGER.info(e);
				throw new JspException(e);
			}
    	}
        
        return SKIP_BODY;
    }
    
    
    public int doEndTag() throws JspException {
    	return EVAL_PAGE;
    }
    
    
}
