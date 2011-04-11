/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.content;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import com.freshdirect.content.attributes.EnumAttributeName;
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
	
	private static DateFormat DATE_FORMAT1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");

	private static DateFormat DATE_FORMAT2 = new SimpleDateFormat("MM/dd/yyyy");

	private static DateFormat DATE_FORMAT3 = new SimpleDateFormat("MM/dd/yy");
	
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
        String user = request.getRemoteUser();
        String sapId = request.getParameter("sapId");
        
        if ("save".equalsIgnoreCase(action)) {
            //
            // save the atibutes for the erpObject and its children
            //
            doSaveAttributes(erpObject, user, sapId);
        } else if("copy".equalsIgnoreCase(action)){
        	doCopyAttributes(erpObject, request.getParameter("sourceId"), request.getParameter("skuCode"), user);
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
	private void doCopyAttributes(ErpModelSupport targetModel, String sourceSku, String targetSku, String user) throws JspException {
		try {
			ErpFactory factory = ErpFactory.getInstance();
			ErpProductModel source = factory.getProduct(sourceSku.trim());
			if(source.getSkuCode() != null && targetSku != null){			
				ErpNutritionModel nutrition =factory.getNutrition(sourceSku);
				nutrition.setSkuCode(targetSku);
				factory.saveNutrition(nutrition, user);
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

	private void doSaveAttributes(ErpModelSupport erpModel, String user, String sapId) throws JspException {
        if (erpModel == null) {
            feedback = "No ERP object to edit";
            return;
        }
        try {
        	feedback = validateAttributes(erpModel);
        	if (feedback == null) {
        		ErpFactory.getInstance().saveAttributes(erpModel, user, sapId);
        		feedback = "Attributes saved";
        	}
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

	private String validateAttributes(ErpModelSupport erpModel) {
		if (erpModel instanceof ErpProductModel) {
			ErpProductModel prod = (ErpProductModel) erpModel;
			String new_prod_date = prod.getAttributes().getAttribute(EnumAttributeName.NEW_PRODUCT_DATE);
			String back_prod_date = prod.getAttributes().getAttribute(EnumAttributeName.BACK_IN_STOCK_DATE);
			String date = new_prod_date != null && new_prod_date.trim().length() != 0 ? new_prod_date.trim() : null;
			if (back_prod_date != null && back_prod_date.trim().length() != 0) {
				if (date != null)
					return "Cannot set both manual override dates at the same time!";
				else
					date = back_prod_date.trim();
			}
			if (date != null) {
				try {
					DATE_FORMAT1.parse(date);
				} catch (ParseException e) {
					try {
						DATE_FORMAT3.parse(date);
					} catch (ParseException e1) {
						try {
							DATE_FORMAT2.parse(date);
						} catch (ParseException e2) {
							return "Unparseable date: " + date;
						}
					}
				}
				
			}
		}
		return null;
	}
}
