/*
 * $Workfile:GetOrderTag.java$
 *
 * $Date:8/13/2003 6:40:45 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.util.GroupScaleUtil;
import com.freshdirect.erp.ErpFactory;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.fdstore.GrpZonePriceModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

/**
 *
 * @version $Revision:1$
 * @author $Author:Sairam Krishnasamy$
 */
public class GetGSProductsTag extends BodyTagSupport {

	
    private String   skuModelList;                                           
    private String   productList;
    private String grpId;
    private String version;
    
	private final static java.text.DecimalFormat qtyFormatter = new java.text.DecimalFormat("0");
	private final static java.text.DecimalFormat totalFormatter = new java.text.DecimalFormat("0.00");


    public int doStartTag() throws JspException {
		//get group info
		FDUserI user = (FDUserI)pageContext.getSession().getAttribute(SessionName.USER);
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		try{
			List<String> skuList = null;
			if (grpId != null && !"".equals(grpId)) {
				FDGroup group = new FDGroup(grpId, Integer.parseInt(version));
				MaterialPrice matPrice = GroupScaleUtil.getGroupScalePrice(group, user.getPricingZoneId());
				if (matPrice != null) {
					String grpQty = "0";
					String grpTotalPrice = "0";
					boolean isSaleUnitDiff = false;
					double displayPrice = 0.0;
					if(matPrice.getPricingUnit().equals(matPrice.getScaleUnit()))
						displayPrice = matPrice.getPrice() * matPrice.getScaleLowerBound();
					else {
						displayPrice = matPrice.getPrice();
						isSaleUnitDiff = true;
					}
					grpQty = qtyFormatter.format(matPrice.getScaleLowerBound());
					if(matPrice.getScaleUnit().equals("LB"))//Other than eaches append the /pricing unit for clarity.
						grpQty = grpQty + (matPrice.getScaleUnit().toLowerCase());

					grpTotalPrice = "$"+totalFormatter.format(displayPrice);
					if(isSaleUnitDiff)
						grpTotalPrice = grpTotalPrice + "/" + (matPrice.getPricingUnit().toLowerCase());
					GroupScalePricing gsp = GroupScaleUtil.lookupGroupPricing(group);
					request.setAttribute("grpQty", grpQty);
					request.setAttribute("grpTotalPrice", grpTotalPrice);
					request.setAttribute("grpShortDesc", gsp.getShortDesc());
					request.setAttribute("grpLongDesc", gsp.getLongDesc());
					skuList = gsp.getSkuList();
				}
			}

			if(skuList == null || skuList.isEmpty()) return SKIP_BODY;
			List <SkuModel> skuModels = new ArrayList<SkuModel>();
			if(skuModelList != null) {
				//matIds used for filtering virtual skus
				Set<String> matIds = new HashSet<String>();
				String prioritySku = request.getParameter("skuCode");
				if(prioritySku != null && prioritySku.length() > 0) {
					try{
						ProductModel priorityProd = ContentFactory.getInstance().getProduct(prioritySku);
						if (priorityProd != null) {
							SkuModel sku = priorityProd.getSku(prioritySku);
							if(sku != null && sku.getProductInfo().isGroupExists()){
								skuModels.add(sku);
								matIds.add(sku.getProduct().getMaterial().getMaterialNumber());
							}
							
						}
					}catch(FDSkuNotFoundException se){
						//Ignore this sku. move to next
					}
				}
				Iterator<String> it = skuList.iterator();
				while(it.hasNext()){
					String curSku = it.next();
					if(prioritySku != null && prioritySku.equals(curSku))
						//as it is already processed
						continue;
					try{
						ProductModel pm = ContentFactory.getInstance().getProduct(curSku);
						if (pm != null) {
							SkuModel sku = pm.getSku(curSku);
							String matId = sku.getProduct().getMaterial().getMaterialNumber();
							//if matIds.contains(matId) then its a virtual sku. do not include it.
							if(sku != null && sku.getProductInfo().isGroupExists() && !matIds.contains(matId)) {
								skuModels.add(sku);
								matIds.add(matId);
							}
						}
					}catch(FDSkuNotFoundException se){
						//Ignore this sku. move to next
					}
						
				}
		        pageContext.setAttribute(skuModelList, skuModels);
			}

			if(productList != null) {
				List <ProductModel> productModelList = new ArrayList<ProductModel>();
				Iterator<SkuModel> iter = skuModels.iterator();
				while(iter.hasNext()){
					SkuModel curSku = iter.next();
					ProductModel pm = curSku.getProductModel();
					if (pm != null) {
						productModelList.add(pm);
					}
				}
		        pageContext.setAttribute(productList, productModelList);
			}
			
		}catch(FDResourceException fe){
			throw new JspException(fe);
		}
		return EVAL_BODY_BUFFERED;
	}


	public void setSkuModelList(String skuModelList) {
		this.skuModelList = skuModelList;
	}

	public void setProductList(String productList) {
		this.productList = productList;
	}

	public static class TagEI extends TagExtraInfo {
	    /**
	     * Return information about the scripting variables to be created.
	     */
	    public VariableInfo[] getVariableInfo(TagData data) {
	        List variables = new ArrayList();
	        addToList(variables, new VariableInfo(data.getAttributeString("productList"), "java.util.List", true, VariableInfo.NESTED));
	        addToList(variables, new VariableInfo(data.getAttributeString("skuModelList"), "java.util.List", true, VariableInfo.NESTED));
	        return (VariableInfo[]) variables.toArray(new VariableInfo[variables.size()]);

	    }
	    void addToList(List variables, VariableInfo i) {
	        if (i.getVarName() != null) {
	            variables.add(i);
	        }
	    }


	}

	public void setGroupId(String grpId) {
		this.grpId = grpId;
	}


	public void setVersion(String version) {
		this.version = version;
	}

}
