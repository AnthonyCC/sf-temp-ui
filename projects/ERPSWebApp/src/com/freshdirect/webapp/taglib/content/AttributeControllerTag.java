/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.content;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.erp.ErpFactory;
import com.freshdirect.erp.ErpModelSupport;
import com.freshdirect.erp.model.ErpProductModel;
import com.freshdirect.fdstore.FDResourceException;


/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class AttributeControllerTag extends com.freshdirect.framework.webapp.TagSupport {
	
    private String feedback = null;
    
    private ErpModelSupport erpObject = null;
    private String userMessage = null;
    
    public void setErpObject(ErpModelSupport erpObject) {
        this.erpObject = erpObject;
    }
    
    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }
    
    public int doStartTag() throws JspException {
        //
        // get the user's session, current request, and intended action
        //
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String action = request.getParameter("action");
        //
        // anything that finds stuff goes first
        //
        if ("save".equalsIgnoreCase(action)) {
            //
            // save the atibutes for the erpObject and its children
            //
            doSaveAttributes(erpObject);
        } else if("copy".equalsIgnoreCase(action)){
        	doCopyAttributes(erpObject, request.getParameter("sourceId"), request.getParameter("skuCode"));
        }
        
        if ((userMessage != null) && !"".equals(userMessage.trim())) {
            if (feedback != null)
                pageContext.setAttribute(userMessage, feedback);
            else
                pageContext.setAttribute(userMessage, "");
        }
        
        return SKIP_BODY;
    }
    
    
    /**
	 * @param erpObject2
     * @throws JspException
	 */
	private void doCopyAttributes(ErpModelSupport targetModel, String sourceSku, String targetSku) throws JspException {
		try {
			ErpFactory factory = ErpFactory.getInstance();
			ErpProductModel source = factory.getProduct(sourceSku.trim());
			if(source.getSkuCode() != null && targetSku != null){			
				ErpNutritionModel nutrition =factory.getNutrition(sourceSku);
				nutrition.setSkuCode(targetSku);
				factory.saveNutrition(nutrition);
			} else {
				if(source.getSkuCode() == null){
					feedback = "Invalid sku entered, nothing saved";
				} else if(targetSku == null){
					feedback = "Target Sku is null, must be a problem";
				}
			}
			
		} catch (FDResourceException e) {
			 feedback = "Unable to save attributes: ";
	            if (e.getNestedException() != null) {
	                feedback += e.getNestedException().getMessage();
	            } else {
	                feedback += e.getMessage();
	            }
		}
		
	}

	private void doSaveAttributes(ErpModelSupport erpModel) throws JspException {
        if (erpModel == null) {
            feedback = "No ERP object to edit";
            return;
        }
        try {
           ErpFactory.getInstance().saveAttributes(erpModel);
           feedback = "Attributes saved";
        } catch (FDResourceException fdre) {
            fdre.printStackTrace();
            feedback = "Unable to save attributes: ";
            if (fdre.getNestedException() != null) {
                feedback += fdre.getNestedException().getMessage();
            } else {
                feedback += fdre.getMessage();
            }
        }
        
    }
      
    
}
