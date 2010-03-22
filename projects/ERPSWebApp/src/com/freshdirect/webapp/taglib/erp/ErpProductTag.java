/*
 * ErpProductTag.java
 *
 * Created on September 12, 2001, 7:26 PM
 */

package com.freshdirect.webapp.taglib.erp;

/**
 *
 * @author  knadeem
 * @version
 */

import javax.servlet.http.*;
import javax.servlet.jsp.*;

import com.freshdirect.webapp.util.FormElementNameHelper;

import com.freshdirect.erp.model.*;
import com.freshdirect.erp.ErpFactory;
import com.freshdirect.fdstore.FDResourceException;

import com.freshdirect.content.attributes.*;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.Category;

public class ErpProductTag extends com.freshdirect.framework.webapp.BodyTagSupport {
    
    private static Category LOGGER = LoggerFactory.getInstance(ErpProductTag.class);
    
    private String skuCode = null;
    public String getSkuCode(){
        return (this.skuCode);
    }
    public void setSkuCode(String skuCode){
        this.skuCode = skuCode;
    }
    
    private String id = null;
    public String getId(){
        return (this.id);
    }
    public void setId(String id){
        this.id = id;
    }
    
    private String sessionName = "freshdirect.erp.product";
    
    private ErpProductModel product = null;
    
    public void doInitBody() throws JspException {
        //
        // clear the page context for this tag
        //
        pageContext.removeAttribute(id);
        HttpSession session = pageContext.getSession();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        if ((skuCode != null) && !"".equals(skuCode)) {
            try {
                product = ErpFactory.getInstance().getProduct(skuCode);
                session.setAttribute(sessionName, product);
            } catch (FDResourceException fdre) {
                LOGGER.debug(fdre);
                throw new JspException(fdre.getMessage());
            }
        } else {
            product = (ErpProductModel) session.getAttribute(sessionName);
        }
        if (("POST".equalsIgnoreCase(request.getMethod())) && (product != null)) {
            //
            // if a form was posted through this tag, try and pick up all the
            // relevant fields to set
            //
            setAttributes(product, request);
        }
        if (product == null) {
            session.removeAttribute(sessionName);
        } else {
            pageContext.setAttribute(id, product);
        }
    }
    
    
    private void setAttributes(ErpProductModel prod, HttpServletRequest request) {
        //
        // collect attributes from things we care about
        //
        //
        // label name
        //
        String pricing_unit_descr = request.getParameter(FormElementNameHelper.getFormElementName(prod, EnumAttributeName.PRICING_UNIT_DESCRIPTION.getName()));
        if (pricing_unit_descr != null) {
            prod.getAttributes().setAttribute(EnumAttributeName.PRICING_UNIT_DESCRIPTION.getName(), pricing_unit_descr);
        }

        String new_product_date = request.getParameter(FormElementNameHelper.getFormElementName(prod, EnumAttributeName.NEW_PRODUCT_DATE.getName()));
        if (new_product_date != null) {
            prod.getAttributes().setAttribute(EnumAttributeName.NEW_PRODUCT_DATE.getName(), new_product_date);
        }

        String back_in_stock_date = request.getParameter(FormElementNameHelper.getFormElementName(prod, EnumAttributeName.BACK_IN_STOCK_DATE.getName()));
        if (back_in_stock_date != null) {
            prod.getAttributes().setAttribute(EnumAttributeName.BACK_IN_STOCK_DATE.getName(), back_in_stock_date);
        }
        
    }
    
    
    
}
