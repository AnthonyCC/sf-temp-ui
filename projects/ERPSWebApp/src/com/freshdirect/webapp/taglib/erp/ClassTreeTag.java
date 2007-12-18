/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.erp;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.erp.ErpFactory;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;


/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class ClassTreeTag extends com.freshdirect.framework.webapp.BodyTagSupport {
    
    private static Category LOGGER = LoggerFactory.getInstance(ClassTreeTag.class);
    
    private String classTreeSessionName = "freshdirect.classTree";
    
    private String id = null;
    
    public String getId() {
        return (this.id);
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public int doStartTag() throws JspException {
        //
        //
        //
        pageContext.removeAttribute(id);
        //
        // get the user's session, current request, and intended action
        //
        HttpSession session = pageContext.getSession();
        //
        // try to grab a cached class tree first
        //
        List allClasses = (List) session.getAttribute(classTreeSessionName);
        if (allClasses == null) {
            //
            // nothing cached, find all the classes
            //
            try {
                allClasses = new LinkedList(ErpFactory.getInstance().getClasses());
            } catch (FDResourceException fdre) {
                LOGGER.debug(fdre);
                throw new JspException(fdre.getMessage());
            }
            //
            // cache the results for next time
            //
            session.setAttribute(classTreeSessionName, allClasses);
            
        }
        
        if (allClasses == null) {
            return SKIP_BODY;
        } else {
            pageContext.setAttribute(id, allClasses);
            return EVAL_BODY_BUFFERED;
        }
        
    }
    
    
}
