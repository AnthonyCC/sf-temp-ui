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
	
	private static final int MAX_PEAK_PRODUCE_COUNT=5;
	private static final int MIN_PEAK_PRODUCE_COUNT=3;
	
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
		
		List products=new ArrayList(10);
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
		System.out.println("Peak produce :"+products);
		if(products.size()<MIN_PEAK_PRODUCE_COUNT) {
			return new ArrayList();
		} 
		else if(products.size()>MAX_PEAK_PRODUCE_COUNT) {
			Random random=new Random();
			Set randomProducts=new HashSet(MAX_PEAK_PRODUCE_COUNT);
			int index=0;
			while(randomProducts.size()<MAX_PEAK_PRODUCE_COUNT) {
				index=random.nextInt(products.size());
				randomProducts.add(products.get(index));
			}
			System.out.println("Chose Peak produce :"+randomProducts);
			return randomProducts;
		}
		else {
			return products;
		}
		

	}
	
	private void setPeakProduce(CategoryModel category, List products) throws FDResourceException {

		
		List peakProduce=getPeakProduce(category.getProducts());
		if(peakProduce!=null && peakProduce.size()>0)
			products.addAll(peakProduce);
		List subCats=category.getSubcategories();
		for (int i=0;i<subCats.size();i++) {
			setPeakProduce((CategoryModel)subCats.get(i),products);
		}
		
	}
	
	private List getPeakProduce(Collection products) throws FDResourceException {
		
		
		if(products==null || products.size()==0) {
			return null;
		}
		List peakProduce=new ArrayList(10);
		Iterator it=products.iterator();
		SkuModel sku=null;
		while(it.hasNext()) {
			ProductModel prod=(ProductModel)it.next();
			List skus=prod.getSkus();
			String rating=null;
			for(int i=0;i<skus.size();i++) {
				sku=(SkuModel)skus.get(i);
				if(sku.isDiscontinued() || sku.isOutOfSeason() || sku.isTempUnavailable() ||sku.isUnavailable()) {
					continue;
				}
				try {
					rating=sku.getProductInfo().getRating();
				} catch (FDSkuNotFoundException e) {
					throw new FDResourceException(e);
				}
				if(isProduce(sku.getSkuCode())&&isPeakProduce(rating)) {
					peakProduce.add(sku);
					break;
				}
			}
		}
		return peakProduce;
	}
	
	private boolean isProduce(String skuCode) {
		if(skuCode.startsWith("FRU") || skuCode.startsWith("VEG") || skuCode.startsWith("YEL")) {
			return true;
		}
		return false;
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

