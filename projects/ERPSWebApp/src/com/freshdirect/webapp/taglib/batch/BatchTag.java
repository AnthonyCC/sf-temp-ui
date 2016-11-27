/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.batch;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.erp.ErpFactory;
import com.freshdirect.erp.model.BatchModel;
import com.freshdirect.fdstore.FDResourceException;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class BatchTag extends com.freshdirect.framework.webapp.BodyTagSupport {
  
    private BatchModel batch = null;
    private String sessionName = "freshdirect.batch";
    
    private String id = null;
    
    public String getId() {
        return this.id;
    }
    
    public void setId(String i) {
        this.id = i;
    }
    
    private String batchNumber = null;
    
    public String getBatchNumber() {
        return this.batchNumber;
    }
    
    public void setBatchNumber(String bn) {
        this.batchNumber = bn;
    }
    
    public int doStartTag() throws JspException {

        HttpSession session = pageContext.getSession();
        
        if ((batchNumber != null) && !"".equals(batchNumber.trim())) {
            //
            // find the batch and get its model given a batch number
            //
            try {
                int bn = Integer.parseInt(batchNumber);
                batch = ErpFactory.getInstance().getBatch(bn);
                
            } catch (FDResourceException fdre) {
                throw new JspException(fdre.getMessage());
            }
            	
        } else {
            //
            // or grab the last one cached in the users's session
            //
            batch = (BatchModel) session.getAttribute(sessionName);
        }
        //
        // put useful stuff in the session context
        //
        session.setAttribute(sessionName, batch);
        //
        // put the batch in the page as a scripting variable
        //
        pageContext.setAttribute(id, batch);
        
        return EVAL_BODY_BUFFERED;
    }
    
    
}
