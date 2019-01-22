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
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.content.attributes.EnumAttributeName;
import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.erp.ErpFactory;
import com.freshdirect.erp.ErpModelSupport;
import com.freshdirect.erp.model.ErpCharacteristicModel;
import com.freshdirect.erp.model.ErpMaterialModel;
import com.freshdirect.erp.model.ErpProductModel;
import com.freshdirect.erp.model.ErpSalesUnitModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.erp.MaterialTag;
import com.freshdirect.webapp.util.FormElementNameHelper;


/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class BatchUploadTag extends com.freshdirect.framework.webapp.BodyTagSupport {
	
	private static Category LOGGER = LoggerFactory.getInstance(BatchUploadTag.class);
	
	private static DateFormat DATE_FORMAT1 = new SimpleDateFormat("MM/dd/yyyy HH:mm");

	private static DateFormat DATE_FORMAT2 = new SimpleDateFormat("MM/dd/yyyy");

	private static DateFormat DATE_FORMAT3 = new SimpleDateFormat("MM/dd/yy");
	
	private String id = null;
    
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
	
    public int doStartTag() throws JspException {        
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String action = request.getParameter("action");
        String user = request.getRemoteUser();
        
        if("rest".equals(action)) {
        	//Save the restrictions
        	String sap_id = request.getParameter("sap_id");
       		if(sap_id != null && sap_id.length() > 0) {
       			//time to process the material
       			try {
					ErpMaterialModel material = ErpFactory.getInstance().getMaterial(sap_id);
						
					//set updated attributes
					Iterator iter = material.getSalesUnits().iterator();
			        while (iter.hasNext()) {
			            ErpSalesUnitModel slu = (ErpSalesUnitModel) iter.next();
			            String description = request.getParameter(FormElementNameHelper.getFormElementName(slu,EnumAttributeName.DESCRIPTION.getName()));
			            if(description != null && description.length() > 0) {
			                slu.getAttributes().setAttribute(EnumAttributeName.DESCRIPTION.getName(), description);
			            }
			        }			            
			            
		            String label_name = request.getParameter(FormElementNameHelper.getFormElementName(material, EnumAttributeName.LABEL_NAME.getName()));
		            if(label_name != null && label_name.length() > 0) {
		            	material.getAttributes().setAttribute(EnumAttributeName.LABEL_NAME.getName(), label_name);
		            }
			            
		            String deposit = request.getParameter(FormElementNameHelper.getFormElementName(material, EnumAttributeName.DEPOSIT_AMOUNT.getName()));
		            if ((deposit != null) && !"".equals(deposit.trim())) {
		            	material.getAttributes().setAttribute(EnumAttributeName.DEPOSIT_AMOUNT.getName(), Integer.parseInt(deposit));
			        } else {
			        	material.getAttributes().setAttribute(EnumAttributeName.DEPOSIT_AMOUNT.getName(), 0);
			        }
		            
		            String advance_order_flag = request.getParameter(FormElementNameHelper.getFormElementName(material, EnumAttributeName.ADVANCE_ORDER_FLAG.getName()));
		            if (advance_order_flag != null && advance_order_flag.length() > 0) {
		            	material.getAttributes().setAttribute(EnumAttributeName.ADVANCE_ORDER_FLAG.getName(), true);
			        } else {
			        	material.getAttributes().setAttribute(EnumAttributeName.ADVANCE_ORDER_FLAG.getName(), false);
			        }
		            
		            String restrictions = request.getParameter(FormElementNameHelper.getFormElementName(material, EnumAttributeName.RESTRICTIONS.getName()));
		            material.getAttributes().setAttribute(EnumAttributeName.RESTRICTIONS.getName(), restrictions);
		            	
					ErpFactory.getInstance().saveAttributes(material, user, sap_id);
				} catch (FDResourceException e) {
					LOGGER.debug(e);
	                throw new JspException(e.getMessage());
				}
       		}
      	}        
        
        return EVAL_BODY_BUFFERED;
    }    
    
}
