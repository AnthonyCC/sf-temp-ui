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
import com.rsa.jsafe.ba;

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
    	ErpsAttributes attributes = prod.getAttributes().clone();
        String pricing_unit_descr = request.getParameter(FormElementNameHelper.getFormElementName(prod, EnumAttributeName.PRICING_UNIT_DESCRIPTION.getName()));
        if (pricing_unit_descr != null)
        	pricing_unit_descr = pricing_unit_descr.trim();
        
        attributes.setPricingUnitDescription(pricing_unit_descr);

        String new_product_date = request.getParameter(FormElementNameHelper.getFormElementName(prod, EnumAttributeName.NEW_PRODUCT_DATE.getName()));
        if (new_product_date != null)
        	new_product_date = new_product_date.trim();
        
        attributes.setNewProdDate(new_product_date);

        String back_in_stock_date = request.getParameter(FormElementNameHelper.getFormElementName(prod, EnumAttributeName.BACK_IN_STOCK_DATE.getName()));
        if (back_in_stock_date != null)
        	back_in_stock_date = back_in_stock_date.trim();
        
        attributes.setBackInStock(back_in_stock_date);
        
        prod.setChangedAttributes(attributes);
    }
    
    
    
}
