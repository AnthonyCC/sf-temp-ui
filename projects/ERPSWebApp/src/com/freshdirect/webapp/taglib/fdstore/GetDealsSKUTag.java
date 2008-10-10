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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;

import com.freshdirect.customer.ejb.ErpSaleEB;
import com.freshdirect.fdstore.DealsHelper;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.webapp.taglib.AbstractGetterTag;


public class GetDealsSKUTag extends AbstractGetterTag {
	
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
						products.add(sku);
					}
				} catch (FDSkuNotFoundException e) {
					throw new FDResourceException(e);
				}
			}
		}
		System.out.println("Deals size:"+products.size());
		System.out.println("Deals :"+products);
		
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

}

