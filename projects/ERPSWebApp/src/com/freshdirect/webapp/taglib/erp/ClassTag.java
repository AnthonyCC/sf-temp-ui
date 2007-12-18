/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.erp;

import java.util.Iterator;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

import com.freshdirect.erp.*;
import com.freshdirect.fdstore.FDResourceException;

import com.freshdirect.erp.model.*;

import com.freshdirect.content.attributes.*;

import com.freshdirect.webapp.util.FormElementNameHelper;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.Category;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class ClassTag extends com.freshdirect.framework.webapp.BodyTagSupport {
    
	private static Category LOGGER = LoggerFactory.getInstance(ClassTag.class);
    
    private String id = null;
    
    public String getId() {
        return (this.id);
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    private String sapId = null;
    
    public String getSapId() {
        return this.sapId;
    }
    
    public void setSapId(String sapId) {
        this.sapId = sapId;
    }
    
    private ErpClassModel erpClass = null;
    private String sessionName = "freshdirect.class";
    
    public int doStartTag() throws JspException {
        //
        // get the user's session and request
        //
        HttpSession session = pageContext.getSession();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        
        if ((sapId != null) && !"".equals(sapId.trim())) {
            //
            // find the erpClass and get its model with attributes set
            // given its SAP erpClass number
            //
            try {
                erpClass = ErpFactory.getInstance().getClass(sapId);
                session.setAttribute(sessionName, erpClass);
            } catch (FDResourceException fdre) {
                LOGGER.debug(fdre);
                throw new JspException(fdre.getMessage());
            }
        } else {
            //
            // no SAP id?
            // retrieve the erpClass model cached in the user's session
            //
            erpClass = (ErpClassModel) session.getAttribute(sessionName);
        }
        if (("POST".equalsIgnoreCase(request.getMethod())) && (erpClass != null)) {
            //
            // if a form was posted through this tag, try and pick up all the
            // relevant fields to set
            //
            setAttributes(erpClass, request);
        }
        if ((erpClass != null) && (id != null) && !"".equals(id.trim())) {
            //
            // if there is a erpClass and we have a name for it
            // place it in the page context as a scripting variable
            // and evaluate the body of the tag
            //
            pageContext.setAttribute(id, erpClass);
            return EVAL_BODY_BUFFERED;
        } else {
            //
            // otherwise skip the body since we either don't have a erpClass
            // or don't know what to name it in the page
            //
            return SKIP_BODY;
        }
        
        
    }
    
    private void setAttributes(ErpClassModel erpCls, HttpServletRequest request) {
        //
        // collect attributes from things we care about
        //
        //
        // characteristics
        //
        Iterator iter = erpCls.getCharacteristics().iterator();
        while (iter.hasNext()) {
            ErpCharacteristicModel ch = (ErpCharacteristicModel) iter.next();
            // priority
            String priority = request.getParameter(FormElementNameHelper.getFormElementName(ch, EnumAttributeName.PRIORITY.getName()));
            if ((priority != null) && !"".equals(priority.trim()))
                ch.getAttributes().setAttribute(EnumAttributeName.PRIORITY.getName(), Integer.parseInt(priority));
        }
    }
    
    
}
