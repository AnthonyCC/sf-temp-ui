/*
 * 
 * GetOrderByStatusTag.java
 * Date: Nov 13, 2002 Time: 12:42:09 PM
 */
package com.freshdirect.webapp.taglib.fdstore;

/**
 * 
 * @author knadeem
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderHistory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;


public class GetPeakProduceTag extends AbstractGetterTag {
	
	String deptId = null;
	
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	
	protected Object getResult() throws FDResourceException {
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
		FDIdentity identity = user.getIdentity();
		Collection peakProduce=null;
		ContentNodeModel node=ContentFactory.getInstance().getContentNode(deptId);
		if(node instanceof DepartmentModel) {
				peakProduce=getPeakProduce((DepartmentModel)node);

		}
		
		return peakProduce;
	}

	private Collection getPeakProduce(DepartmentModel dept) throws FDResourceException {
		
		Collection products=new HashSet(10);
		List categories=dept.getCategories();
		Iterator it=categories.iterator();
		CategoryModel catModel=null;
		List _products=null;
		while (it.hasNext()) {
			_products=new ArrayList();
			catModel=(CategoryModel)it.next();
			setPeakProduce(catModel,_products);
			if(_products!=null) {
				products.addAll(_products);
			}
		}
		products=getPeakProduce(products);
		System.out.println("Peak produce :"+products);
		if(products.size()<3) {
			return new HashSet();
		} 
		else {
			return products;
		}
		
		
	}
	
	private void setPeakProduce(CategoryModel category, List products) {
		
		
		products.addAll(category.getProducts());
		List subCats=category.getSubcategories();
		for (int i=0;i<subCats.size();i++) {
			setPeakProduce((CategoryModel)subCats.get(i),products);
		}
		
	}
	
	private Collection getPeakProduce(Collection products) throws FDResourceException {
		
		List peakProduce=new ArrayList(10);
		if(products==null || products.size()==0) {
			return null;
		}
		Iterator it=products.iterator();
		SkuModel sku=null;
		while(it.hasNext()) {
			ProductModel prod=(ProductModel)it.next();
			List skus=prod.getSkus();
			FDProductInfo prodInfo=null;
			for(int i=0;i<skus.size();i++) {
				sku=(SkuModel)skus.get(i);
				if(sku.isDiscontinued() || sku.isOutOfSeason() || sku.isTempUnavailable() ||sku.isUnavailable()) {
					continue;
				}
				try {
					prodInfo=sku.getProductInfo();
				} catch (FDSkuNotFoundException e) {
					throw new FDResourceException(e);
				}
				if(isPeakProduce(prodInfo.getRating())) {
					peakProduce.add(sku);
					break;
				}
			}
		}
		
		if(peakProduce.size()>5) {
			Random random=new Random();
			Set randomProducts=new HashSet(5);
			int index=0;
			while(randomProducts.size()<5) {
				index=random.nextInt(peakProduce.size());
				randomProducts.add(peakProduce.get(index));
			}
			return randomProducts;
		}
		else 
			return peakProduce;
	}
	
	private boolean isPeakProduce(String rating) {
		
		
		if ( EnumOrderLineRating.PEAK_PRODUCE_10.getStatusCode().equals(rating)||
			 EnumOrderLineRating.PEAK_PRODUCE_9.getStatusCode().equals(rating)||
			 EnumOrderLineRating.PEAK_PRODUCE_8.getStatusCode().equals(rating) ) {
			return true;
		}
		return false;
				
		
	}
	
	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.Collection";
		}

	}

}
