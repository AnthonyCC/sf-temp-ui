/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.erp;

import java.util.*;

import javax.servlet.http.*;
import javax.servlet.jsp.*;

import com.freshdirect.erp.model.*;

import com.freshdirect.webapp.util.FormElementNameHelper;
import com.freshdirect.content.attributes.*;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class CharacteristicTag extends com.freshdirect.framework.webapp.BodyTagSupport {
    
    private String sessionName = "freshdirect.characteristic";
    
    private ErpMaterialModel material = null;
    
    public void setMaterial(ErpMaterialModel matl) {
        this.material = matl;
    }
    
    public ErpMaterialModel getMaterial() {
        return this.material;
    }
    
    private ErpClassModel erpClass = null;
    
    public void setErpClass(ErpClassModel ec) {
        this.erpClass = ec;
    }
    
    public ErpClassModel getErpClass() {
        return this.erpClass;
    }
    
    private String sapId = null;
    
    public void setSapId(String sapid) {
        this.sapId = sapid;
    }
    
    public String getSapId() {
        return this.sapId;
    }
    
    private String id = null;
    
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    ErpCharacteristicModel charac = null;
    
    public int doStartTag() throws JspException {
        //
        // get the user's session
        //
        HttpSession session = pageContext.getSession();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        
        if ((material != null) && (sapId != null)) {
            //
            // get the characteristic from the material by its sapId
            //
            Iterator iter = material.getCharacteristics().iterator();
            while (iter.hasNext()) {
                ErpCharacteristicModel m = (ErpCharacteristicModel) iter.next();
                if (m.getName().equals(sapId)) {
                    charac = m;
                    break;
                }
            }
            //
            // if one was found, cache it in the session
            //
            if (charac != null) {
                session.setAttribute(sessionName, charac);
            } else {
                session.removeAttribute(sessionName);
            }
        } else if ((erpClass != null) && (sapId != null)) {
            //
            // get the characteristic from the class by its sapId
            //
            charac = erpClass.getCharacteristic(sapId);
            //
            // if one was found, cache it in the session
            //
            if (charac != null) {
                session.setAttribute(sessionName, charac);
            } else {
                session.removeAttribute(sessionName);
            }
        } else {
            //
            // grab the last one cached in the users's session
            //
           charac = (ErpCharacteristicModel) session.getAttribute(sessionName);
        }
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            //
            // if a form was posted through this tag, try and pick up all the
            // relevant fields to set
            //
            setAttributes(request);
        }
        
        return EVAL_BODY_BUFFERED;
    }
    
    private void setAttributes(HttpServletRequest request) {
        //
        // collect attributes from things we care about
        //
        // characteristic
        //
        String description = request.getParameter(FormElementNameHelper.getFormElementName(charac, EnumAttributeName.DESCRIPTION.getName()));
        if (description != null)
            charac.getAttributes().setAttribute(EnumAttributeName.DESCRIPTION.getName(), description);
        
        String underLabel = request.getParameter(FormElementNameHelper.getFormElementName(charac, EnumAttributeName.UNDER_LABEL.getName()));
        if (underLabel != null)
            charac.getAttributes().setAttribute(EnumAttributeName.UNDER_LABEL.getName(), underLabel);
        
        String optional = request.getParameter(FormElementNameHelper.getFormElementName(charac, EnumAttributeName.OPTIONAL.getName()));
        if (optional != null) {
            charac.getAttributes().setAttribute(EnumAttributeName.OPTIONAL.getName(), new Boolean(optional).booleanValue());
        }
        String dispFormat = request.getParameter(FormElementNameHelper.getFormElementName(charac, EnumAttributeName.DISPLAY_FORMAT.getName()));
        if (dispFormat != null) {
            charac.getAttributes().setAttribute(EnumAttributeName.DISPLAY_FORMAT.getName(), dispFormat);
        }
        //
        // characteristic values
        //
        Iterator iter = charac.getCharacteristicValues().iterator();
        while (iter.hasNext()) {
            ErpCharacteristicValueModel cv = (ErpCharacteristicValueModel) iter.next();
            // description
            description = request.getParameter(FormElementNameHelper.getFormElementName(cv, EnumAttributeName.DESCRIPTION.getName()));
            if (description != null)
                cv.getAttributes().setAttribute(EnumAttributeName.DESCRIPTION.getName(), description);
            // selected
            String selected = request.getParameter(EnumAttributeName.SELECTED.getName());
            if (selected != null) {
                if (selected.equalsIgnoreCase(FormElementNameHelper.getFormElementName(cv, EnumAttributeName.SELECTED.getName()))) {
                    cv.getAttributes().setAttribute(EnumAttributeName.SELECTED.getName(), true);
                } else {
                    cv.getAttributes().setAttribute(EnumAttributeName.SELECTED.getName(), false);
                }
            }
            // label value
            String labelVal = request.getParameter(EnumAttributeName.LABEL_VALUE.getName());
            if (labelVal != null) {
                if (labelVal.equalsIgnoreCase(FormElementNameHelper.getFormElementName(cv, EnumAttributeName.LABEL_VALUE.getName()))) {
                    cv.getAttributes().setAttribute(EnumAttributeName.LABEL_VALUE.getName(), true);
                } else {
                    cv.getAttributes().setAttribute(EnumAttributeName.LABEL_VALUE.getName(), false);
                }
            }
            // priority
            String priority = request.getParameter(FormElementNameHelper.getFormElementName(cv, EnumAttributeName.PRIORITY.getName()));
            if ((priority != null) && !"".equals(priority.trim()))
                cv.getAttributes().setAttribute(EnumAttributeName.PRIORITY.getName(), Integer.parseInt(priority));
            // skucode
            String skucode = request.getParameter(FormElementNameHelper.getFormElementName(cv, EnumAttributeName.SKUCODE.getName()));
            if (skucode != null)
                cv.getAttributes().setAttribute(EnumAttributeName.SKUCODE.getName(), skucode);
        }
    }
    
    public void doInitBody() throws JspException {
        if (charac != null)
            pageContext.setAttribute(id, charac);
        else
            pageContext.removeAttribute(id);
    }
      
    
}
