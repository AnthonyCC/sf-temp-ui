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
import java.util.Collections;
import java.util.Comparator;

import java.util.Iterator;
import java.util.List;

import com.freshdirect.fdstore.DealsHelper;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.webapp.taglib.AbstractGetterTag;



public class GetDealsSKUTag extends AbstractGetterTag {
	
	public static final int COMPARE_NAME = 0;
	private final static Comparator PRODUCT_NAME_COMPARATOR = new ProductSorter(COMPARE_NAME);

	protected Object getResult() throws FDResourceException {
		return getDealsSKU();
	}

	private Collection getDealsSKU() throws FDResourceException {
		
		Collection products=null;
		String upperLimit=DealsHelper.getDealsUpperLimit()+".99";
		Collection _products=FDCachedFactory.findSKUsByDeal((double)DealsHelper.getDealsLowerLimit(), Double.parseDouble(upperLimit), new ArrayList());
		if(_products!=null && _products.size()!=0) {
			products=new ArrayList(_products.size());
			FDProductInfo productInfo=null;
			String sku="";
			for (Iterator i = _products.iterator(); i.hasNext();) {
				sku=i.next().toString();
				try {
					productInfo=FDCachedFactory.getProductInfo(sku);
					if(productInfo.isAvailable() && productInfo.isDeal()) {
						
						try {
							   SkuModel sm=ContentFactory.getInstance().getProduct(sku).getSku(sku);
							   if(!sm.isUnavailable())
								   products.add(sm);
					     } catch(Exception e) {
					     }
					}
				} catch (FDSkuNotFoundException e) {
					throw new FDResourceException(e);
				}
			}
		}
		
		if(products!=null)
			Collections.sort((List)products,PRODUCT_NAME_COMPARATOR);
	
        return products;
	}
	/*
	private List getSKUPrefixes() {
		
		  List skuPrefixes=new ArrayList(5);
		  String _skuPrefixes=DealsHelper.getDealsSkuPrefixes();
		  if(_skuPrefixes!=null && !"".equals(_skuPrefixes) && !DealsHelper.ALL_SKUS.equals(_skuPrefixes)) {
			  StringTokenizer st=new StringTokenizer(_skuPrefixes,DealsHelper.SKU_PREFIX_SEPERATOR);
			  while(st.hasMoreElements()) {
				  skuPrefixes.add(st.nextToken()+"%");
			  }
		  }

		return skuPrefixes;
	}*/
	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.Collection";
		}

	}
	
	private static class ProductSorter implements Comparator {
		private int compareBy;

    	public ProductSorter(int compareBy) {
    		this.compareBy = compareBy;
    	}

    	public int compare(Object o1, Object o2) {
    		SkuModel n1 = (SkuModel) o1;
    		SkuModel n2 = (SkuModel) o2;


			switch (compareBy) {
				case COMPARE_NAME:
					int comp = n1.getProductModel().getFullName().compareTo(n2.getProductModel().getFullName());
					return comp;
				default:
					return 0;
			}
    	}
    }

}

