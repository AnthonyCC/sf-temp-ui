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

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.content.attributes.EnumAttributeName;
import com.freshdirect.erp.ErpFactory;
import com.freshdirect.erp.model.ErpMaterialSalesAreaModel;
import com.freshdirect.erp.model.ErpProductModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.util.FormElementNameHelper;
//import com.rsa.jsafe.ba;

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
        Map<String,String> newnessOverride=new HashMap<String,String>();
        Map<String,String> backInStockOverride=new HashMap<String,String>();
        if ((skuCode != null) && !"".equals(skuCode)) {
            try {
                product = ErpFactory.getInstance().getProduct(skuCode);
                
                newnessOverride=ErpFactory.getInstance().getOverriddenNewness(skuCode);
                product.setNewnessOverride(newnessOverride);
                backInStockOverride=ErpFactory.getInstance().getOverriddenBackInStock(skuCode);
                product.setBackInStockOverride(backInStockOverride);
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
            try {
				setAttributes(product, request,newnessOverride,backInStockOverride);
				session.setAttribute(sessionName, product);
			} catch (FDResourceException fdre) {
				fdre.printStackTrace();
				LOGGER.debug(fdre);
                throw new JspException(fdre.getMessage());
			}
        }
        if (product == null) {
            session.removeAttribute(sessionName);
        } else {
            pageContext.setAttribute(id, product);
        }
    }
    
    
    private void setAttributes(ErpProductModel prod, HttpServletRequest request,Map<String,String> newnessOverride,Map<String,String> backInStockOverride) throws FDResourceException {
        //
        // collect attributes from things we care about
        //
        //
        // label name
        //
        String pricing_unit_descr = request.getParameter(FormElementNameHelper.getFormElementName(prod, EnumAttributeName.PRICING_UNIT_DESCRIPTION.getName()));
        if (pricing_unit_descr != null) {
        	try {
				pricing_unit_descr = java.net.URLDecoder.decode(pricing_unit_descr, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				//do nothing
			}
            prod.getAttributes().setAttribute(EnumAttributeName.PRICING_UNIT_DESCRIPTION.getName(), pricing_unit_descr);
        }
        Map<Object,Object> m=request.getParameterMap();
        boolean saveNewness=false;
        for (Map.Entry<Object, Object> entry : m.entrySet())
        {
            if(entry.getKey().toString().indexOf(EnumAttributeName.NEW_PRODUCT_DATE.getName())!=-1) {
            	saveNewness=true;
            }
        }
        
        if(!saveNewness)
        	return;
        java.util.List<ErpMaterialSalesAreaModel> salesAreas=product.getProxiedMaterial().getMaterialSalesAreas();
        Map<String,String> overriddenNewnessValues=new HashMap<String,String>();
        Map<String,String> overriddenBackInStockValues=new HashMap<String,String>();
        String salesOrg="";
        String distrChanel="";
        String _key="";
        for(ErpMaterialSalesAreaModel salesArea: salesAreas) {
        	salesOrg=salesArea.getSalesOrg();
        	distrChanel=salesArea.getDistChannel();
        	_key=new StringBuilder(8).append(salesOrg).append("-").append(distrChanel).toString();
        
        String new_product_date = request.getParameter(FormElementNameHelper.getFormElementName(prod, _key+EnumAttributeName.NEW_PRODUCT_DATE.getName()));
        /* APPDEV-2395 */
        if(new_product_date == null || new_product_date.trim().length() ==0)
        	new_product_date = "";
        //if (new_product_date != null && new_product_date.trim().length() != 0) {
        //prod.getAttributes().setAttribute(EnumAttributeName.NEW_PRODUCT_DATE.getName(), new_product_date);
        //}

        String back_in_stock_date = request.getParameter(FormElementNameHelper.getFormElementName(prod, _key+EnumAttributeName.BACK_IN_STOCK_DATE.getName()));
        if(back_in_stock_date == null || back_in_stock_date.length() == 0)
        	back_in_stock_date = "";
        //if (back_in_stock_date != null && back_in_stock_date.trim().length() != 0) {
            //prod.getAttributes().setAttribute(EnumAttributeName.BACK_IN_STOCK_DATE.getName(), back_in_stock_date);
        //}
            if(!StringUtil.isEmpty(new_product_date))
            	overriddenNewnessValues.put(_key, new_product_date);
            if(!StringUtil.isEmpty(back_in_stock_date))
            	overriddenBackInStockValues.put(_key, back_in_stock_date);
        }
        ErpFactory.getInstance().setOverriddenBackInStock(prod.getSkuCode(),overriddenBackInStockValues);
        ErpFactory.getInstance().setOverriddenNewness(prod.getSkuCode(), overriddenNewnessValues);
        product.setNewnessOverride(overriddenNewnessValues);
        product.setBackInStockOverride(overriddenBackInStockValues);
    }
    
    
    
}
