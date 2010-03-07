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

import com.freshdirect.erp.*;
import com.freshdirect.erp.model.*;

import com.freshdirect.fdstore.FDResourceException;

import com.freshdirect.content.attributes.*;

import com.freshdirect.webapp.util.FormElementNameHelper;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.Category;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class MaterialTag extends com.freshdirect.framework.webapp.BodyTagSupport {
    
    private static Category LOGGER = LoggerFactory.getInstance(MaterialTag.class);
    
    private String id = null;
    
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    private String sapId = null;
    
    public String getSapId() {
        return this.sapId;
    }
    
    public void setSapId(String sid) {
        this.sapId = sid;
    }
    
    ErpMaterialModel material = null;
    private String sessionName = "freshdirect.material";
    
    public int doStartTag() throws JspException {
        //
        // get the user's session and request
        //
        HttpSession session = pageContext.getSession();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        
        if ((sapId != null) && !"".equals(sapId.trim())) {
            //
            // find the material and get its model with attributes set
            // given its SAP material number
            //
            try {
                //
                // get the material model
                //
                material = ErpFactory.getInstance().getMaterial(sapId);
                //
                // cache the material model with its attributes in the
                // user's session
                session.setAttribute(sessionName, material);
                
            } catch (FDResourceException fdre) {
                LOGGER.debug(fdre);
                throw new JspException(fdre.getMessage());
            }
        } else {
            //
            // no SAP id?
            // retrieve the material model cached in the user's session
            //
            material = (ErpMaterialModel) session.getAttribute(sessionName);
        }
        if (("POST".equalsIgnoreCase(request.getMethod())) && (material != null)) {
            //
            // if a form was posted through this tag, try and pick up all the
            // relevant fields to set
            //
            setAttributes(material, request);
        }
        if ((material != null) && (id != null) && !"".equals(id.trim())) {
            //
            // if there is a material and we have a name for it
            // place it in the page context as a scripting variable
            // and evaluate the body of the tag
            //
            pageContext.setAttribute(id, material);
            return EVAL_BODY_BUFFERED;
        } else {
            //
            // otherwise skip the body since we either don't have a material
            // or don't know what to name it in the page
            //
            return SKIP_BODY;
        }
        
        
    }
    
    private void setAttributes(ErpMaterialModel matl, HttpServletRequest request) {
        //
        // collect attributes from things we care about
        //
        // sales units
        //
        Iterator iter = matl.getSalesUnits().iterator();
        while (iter.hasNext()) {
            ErpSalesUnitModel slu = (ErpSalesUnitModel) iter.next();
            String description = request.getParameter(FormElementNameHelper.getFormElementName(slu,EnumAttributeName.DESCRIPTION.getName()));
            if (description != null)
                slu.getAttributes().setAttribute(EnumAttributeName.DESCRIPTION.getName(), description);
            
            String selected = request.getParameter(EnumAttributeName.SELECTED.getName());
            if (selected != null) {
                if (selected.equalsIgnoreCase(FormElementNameHelper.getFormElementName(slu,EnumAttributeName.SELECTED.getName()))) {
                    slu.getAttributes().setAttribute(EnumAttributeName.SELECTED.getName(), true);
                } else {
                    slu.getAttributes().setAttribute(EnumAttributeName.SELECTED.getName(), false);
                }
            }
        }
        //
        // taxable
        //
        String taxable = request.getParameter(FormElementNameHelper.getFormElementName(matl, EnumAttributeName.TAXABLE.getName()));
        if (taxable != null) {
            matl.getAttributes().setAttribute(EnumAttributeName.TAXABLE.getName(), true);
        } else {
            matl.getAttributes().setAttribute(EnumAttributeName.TAXABLE.getName(), false);
        }
        //
        // eligible for customer promo
        //
        String promo = request.getParameter(FormElementNameHelper.getFormElementName(matl, EnumAttributeName.CUST_PROMO.getName()));
        if (promo != null) {
            matl.getAttributes().setAttribute(EnumAttributeName.CUST_PROMO.getName(), true);
        } else {
            matl.getAttributes().setAttribute(EnumAttributeName.CUST_PROMO.getName(), false);
        }
        //
        // label name
        //
        String label_name = request.getParameter(FormElementNameHelper.getFormElementName(matl, EnumAttributeName.LABEL_NAME.getName()));
        if (label_name != null) {
            matl.getAttributes().setAttribute(EnumAttributeName.LABEL_NAME.getName(), label_name);
        }
        //
        // deposit amount
        //
        String deposit = request.getParameter(FormElementNameHelper.getFormElementName(matl, EnumAttributeName.DEPOSIT_AMOUNT.getName()));
        if ((deposit != null) && !"".equals(deposit.trim())) {
            matl.getAttributes().setAttribute(EnumAttributeName.DEPOSIT_AMOUNT.getName(), Integer.parseInt(deposit));
        } else {
            matl.getAttributes().setAttribute(EnumAttributeName.DEPOSIT_AMOUNT.getName(), 0);
        }
        //
        // restrictions
        //
        String restrictions = request.getParameter(FormElementNameHelper.getFormElementName(matl, EnumAttributeName.RESTRICTIONS.getName()));
        matl.getAttributes().setAttribute(EnumAttributeName.RESTRICTIONS.getName(), restrictions);
        //add ability to set advance order flag via erpsy-daisy gui
        String advance_order_flag = request.getParameter(FormElementNameHelper.getFormElementName(matl, EnumAttributeName.ADVANCE_ORDER_FLAG.getName()));
        if (advance_order_flag != null) {
            matl.getAttributes().setAttribute(EnumAttributeName.ADVANCE_ORDER_FLAG.getName(), true);
        } else {
            matl.getAttributes().setAttribute(EnumAttributeName.ADVANCE_ORDER_FLAG.getName(), false);
        }
        
        //
        // kosher production item
        //
        String kosher_prod = request.getParameter(FormElementNameHelper.getFormElementName(matl, EnumAttributeName.KOSHER_PRODUCTION.getName()));
        if (kosher_prod != null) {
            matl.getAttributes().setAttribute(EnumAttributeName.KOSHER_PRODUCTION.getName(), true);
        } else {
            matl.getAttributes().setAttribute(EnumAttributeName.KOSHER_PRODUCTION.getName(), false);
        }
    }
    
    
}
