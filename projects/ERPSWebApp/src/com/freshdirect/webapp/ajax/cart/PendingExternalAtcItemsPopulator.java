package com.freshdirect.webapp.ajax.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.cart.data.AddToCartItem;
import com.freshdirect.webapp.ajax.cart.data.PendingExternalAtcItemsData;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.StandingOrderHelper;

public class PendingExternalAtcItemsPopulator {

	private static final Logger LOG = LoggerFactory.getInstance(PendingExternalAtcItemsPopulator.class);
	
	public static Map<String, List<PendingExternalAtcItemsData>> createPendingExternalAtcItemsData(FDSessionUser user, HttpServletRequest request) {

		Map<String, List<AddToCartItem>> pendingExternalAtcItemsMap = user.getPendingExternalAtcItems();
		Map<String, List<PendingExternalAtcItemsData>> pendingExternalAtcItemDataMap = new HashMap<String, List<PendingExternalAtcItemsData>>();
				
		if (pendingExternalAtcItemsMap != null){
			for (String groupName : pendingExternalAtcItemsMap.keySet()){
				List<PendingExternalAtcItemsData> pendingAtcFailureDatas = new ArrayList<PendingExternalAtcItemsData>();
				
				for (AddToCartItem addToCartItem : pendingExternalAtcItemsMap.get(groupName)) {
					PendingExternalAtcItemsData pendingExternalAtcItemData = new PendingExternalAtcItemsData();
					pendingExternalAtcItemData.setAddToCartItem(addToCartItem);
					
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
						pendingExternalAtcItemData.setProductData(productData);
						pendingAtcFailureDatas.add(pendingExternalAtcItemData);
					} catch (FDResourceException e) {
						LOG.error( "Failed to get product info.", e );
					} catch (FDSkuNotFoundException e) {
						LOG.error( "Failed to get product info.", e );
					} catch (HttpErrorResponse e) {
						LOG.error( "Failed to get product info.", e );
					}
				}
				pendingExternalAtcItemDataMap.put(groupName, pendingAtcFailureDatas);
			}
		}
		return pendingExternalAtcItemDataMap;
	}

}
