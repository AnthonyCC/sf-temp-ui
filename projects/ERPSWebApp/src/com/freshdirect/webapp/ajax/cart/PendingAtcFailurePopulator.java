package com.freshdirect.webapp.ajax.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.cart.data.AddToCartItem;
import com.freshdirect.webapp.ajax.cart.data.PendingAtcFailureData;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;

public class PendingAtcFailurePopulator {

	private static final Logger LOG = LoggerFactory.getInstance(PendingAtcFailurePopulator.class);
	
	public static Map<String, List<PendingAtcFailureData>> createPendingAtcFailureData(FDSessionUser user) {

		Map<String, List<AddToCartItem>> pendingAtcFailureMap = user.getPendingAtcFailures();
		Map<String, List<PendingAtcFailureData>> pendingAtcFailureDataMap = new HashMap<String, List<PendingAtcFailureData>>();
		
		if (pendingAtcFailureMap != null){
			for (String groupName : pendingAtcFailureMap.keySet()){
				List<PendingAtcFailureData> pendingAtcFailureDatas = new ArrayList<PendingAtcFailureData>();
				
				for (AddToCartItem addToCartItem : pendingAtcFailureMap.get(groupName)) {
					PendingAtcFailureData pendingAtcFailureData = new PendingAtcFailureData();
					pendingAtcFailureData.setAddToCartItem(addToCartItem);
					
					try {
						ProductModel prodNode = null;		
						String productId = addToCartItem.getProductId();
						String categoryId = addToCartItem.getCategoryId();
						String skuCode = addToCartItem.getSkuCode();
						if ( productId != null && categoryId != null ) {
							prodNode = ContentFactory.getInstance().getProductByName( categoryId, productId );
						}			
						if ( prodNode == null ) {
							prodNode = ContentFactory.getInstance().getProduct(skuCode);
						}			

						ProductData productData = ProductDetailPopulator.createProductData(user, prodNode);
						pendingAtcFailureData.setProductData(productData);
						pendingAtcFailureDatas.add(pendingAtcFailureData);
					} catch (FDResourceException e) {
						LOG.error( "Failed to get product info.", e );
					} catch (FDSkuNotFoundException e) {
						LOG.error( "Failed to get product info.", e );
					} catch (HttpErrorResponse e) {
						LOG.error( "Failed to get product info.", e );
					}
				}
				pendingAtcFailureDataMap.put(groupName, pendingAtcFailureDatas);
			}
		}
		return pendingAtcFailureDataMap;
	}

}
