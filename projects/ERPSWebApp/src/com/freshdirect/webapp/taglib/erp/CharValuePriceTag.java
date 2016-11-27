/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.erp;

import javax.servlet.jsp.*;

import com.freshdirect.erp.model.*;
import com.freshdirect.erp.ErpFactory;
import com.freshdirect.fdstore.FDResourceException;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.Category;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class CharValuePriceTag extends com.freshdirect.framework.webapp.BodyTagSupport {

	private static Category LOGGER = LoggerFactory.getInstance(CharValuePriceTag.class);
    
    private ErpMaterialModel material = null;
    
    public void setMaterial(ErpMaterialModel matl) {
        this.material = matl;
    }
    
    public ErpMaterialModel getMaterial() {
        return this.material;
    }
    
    private ErpCharacteristicValueModel charValue = null;
    
    public void setCharValue(ErpCharacteristicValueModel charValue) {
        this.charValue = charValue;
    }
    
    public ErpCharacteristicValueModel getCharValue() {
        return this.charValue;
    }
    
    private String id = null;
    
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    ErpCharacteristicValuePriceModel charValuePrice = null;
    
    public int doStartTag() throws JspException {
        try {
            
        	charValuePrice = ErpFactory.getInstance().getCharacteristicValuePrice(material, charValue);
            
        } catch (FDResourceException fdre) {
            LOGGER.debug(fdre);
            throw new JspException(fdre.getMessage());
        }
        
        return EVAL_BODY_BUFFERED;
    }
    
    public void doInitBody() throws JspException {
        if (charValuePrice != null)
            pageContext.setAttribute(id, charValuePrice);
        else
            pageContext.removeAttribute(id);
    }
    
}
