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
    	
    	ErpsAttributes attributes = charac.getAttributes().clone();

        String description = request.getParameter(FormElementNameHelper.getFormElementName(charac, EnumAttributeName.DESCRIPTION.getName()));
        if (description != null)
        	description = description.trim();
        
        attributes.setDescription(description);
        
        String underLabel = request.getParameter(FormElementNameHelper.getFormElementName(charac, EnumAttributeName.UNDER_LABEL.getName()));
        if (underLabel != null)
        	underLabel = underLabel.trim();
        
        attributes.setUnderLabel(underLabel);
        
        String optional = request.getParameter(FormElementNameHelper.getFormElementName(charac, EnumAttributeName.OPTIONAL.getName()));
        if (optional != null)
            attributes.setOptional(Boolean.valueOf(optional.trim()));
        else 
        	attributes.setOptional(false);

        String dispFormat = request.getParameter(FormElementNameHelper.getFormElementName(charac, EnumAttributeName.DISPLAY_FORMAT.getName()));
        if (dispFormat != null)
        	dispFormat = dispFormat.trim();
        attributes.setDisplayFormat(dispFormat);
        
        charac.setChangedAttributes(attributes);

        //
        // characteristic values
        //
        Iterator iter = charac.getCharacteristicValues().iterator();
        while (iter.hasNext()) {
            ErpCharacteristicValueModel cv = (ErpCharacteristicValueModel) iter.next();
            ErpsAttributes attribs = cv.getAttributes().clone();
            // description
            description = request.getParameter(FormElementNameHelper.getFormElementName(cv, EnumAttributeName.DESCRIPTION.getName()));
            if (description != null)
            	description = description.trim();
            
            attribs.setDescription(description);
            // selected
            String selected = request.getParameter(EnumAttributeName.SELECTED.getName());
            attribs.setSelected(selected != null &&
            		selected.equalsIgnoreCase(FormElementNameHelper.getFormElementName(cv, EnumAttributeName.SELECTED.getName())));

            // label value
            String labelVal = request.getParameter(EnumAttributeName.LABEL_VALUE.getName());
            attribs.setLabelValue(labelVal != null &&
            		labelVal.equalsIgnoreCase(FormElementNameHelper.getFormElementName(cv, EnumAttributeName.LABEL_VALUE.getName())));

            // priority
            String priority = request.getParameter(FormElementNameHelper.getFormElementName(cv, EnumAttributeName.PRIORITY.getName()));
            int priorityValue = priority != null ? Integer.valueOf(priority.trim()) : 0;
            attribs.setPriority(priorityValue);

            // skucode
            String skucode = request.getParameter(FormElementNameHelper.getFormElementName(cv, EnumAttributeName.SKUCODE.getName()));
            if (skucode != null)
            	skucode = skucode.trim();
            attribs.setSkucode(skucode);
            
            cv.setChangedAttributes(attribs);
        }
    }
    
    public void doInitBody() throws JspException {
        if (charac != null)
            pageContext.setAttribute(id, charac);
        else
            pageContext.removeAttribute(id);
    }
      
    
}
