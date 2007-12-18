/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2003 FreshDirect, Inc.
 *
 */
 
package com.freshdirect.webapp.taglib.fdstore;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.TagSupport;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class GetServiceEmailTag extends TagSupport {
  	
  	private String serviceEmail = "service@freshdirect.com";
  	
  	private static Category LOGGER = LoggerFactory.getInstance( GetServiceEmailTag.class );
  	
  	public int doStartTag() throws JspException {
    	try {
    		HttpSession session = pageContext.getSession();    		
			FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
			
			if(user != null) {
				if(user.isDepotUser()){
					this.serviceEmail = FDDepotManager.getInstance().getCustomerServiceEmail(user.getDepotCode());
				} else if(user.isCorporateUser()){
					this.serviceEmail = "corporateservice@freshdirect.com";
				}
				if(user.isChefsTable()){
				    this.serviceEmail = FDStoreProperties.getChefsTableEmail();
				}
			}

			
			pageContext.getOut().print(this.serviceEmail);
			
		} catch (Exception ex) {
			LOGGER.warn("Exception occured in GetServiceEmail:", ex);
			throw new JspException(ex);
	
		}
    	return SKIP_BODY;
  	}
  	
  	public int doEndTag() {
    return SKIP_BODY;
  	}
}


 