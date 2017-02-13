package com.freshdirect.fdstore.content;

import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;

public class PopulatorUtil {
	private static final Logger LOGGER = Logger.getLogger(PopulatorUtil.class.getSimpleName());
	
	public static final ProductModel getProduct( String productId, String categoryId ) {
		if ( categoryId == null ) {
			// get product in its primary home
			return (ProductModel)ContentFactory.getInstance().getContentNodeByKey(ContentKey.getContentKey(ContentType.get( "Product" ), productId) );
		} else {
			// get product in specified category context
			return ContentFactory.getInstance().getProductByName( categoryId, productId );
		}		
	}

	public static final ProductModel getProduct( String skuCode ) throws FDSkuNotFoundException {
		if ( skuCode != null ) {
			return ContentFactory.getInstance().getProduct(skuCode);
		}		
		return null;
	}
	
	public static final SkuModel getDefSku( ProductModel product ) {
		if (product == null) {
			LOGGER.error("getDefSku(): No input product!");
			return null;
		}
		SkuModel sku = product.getDefaultSku();
		if ( sku == null ) {
			//LOGGER.debug("getDefSku(): ... fall back to default temporary unavailable sku");

			// temporary unav item?
			sku = product.getDefaultTemporaryUnavSku();
			
			if (sku == null) {
				//LOGGER.error("getDefSku(): No default SKU found for product with key " + (ck != null ? ck.getId() : "<null>") + " at all");
			}
		}
		return sku;
	}

	/**
	 * Check if a product is newly created in CMS
	 * and corresponding ERPS data is not assigned yet.
	 * 
	 * @return
	 * 
	 * @throws FDSkuNotFoundException 
	 * @throws FDResourceException 
	 */
	public static final boolean isProductIncomplete(ProductModel prd) throws FDResourceException, FDSkuNotFoundException {
		if (!FDStoreProperties.getPreviewMode()) {
			// usual business
			return false;
		}
		
		if (null == prd.getSkus() || prd.getSkus().isEmpty()) {
			// No SKUs found. This is really bad.
			// Let the execution go and break somewhere else
			throw new FDSkuNotFoundException("Product " + prd.getContentName() + " contains NO SKUs!");
		}
		
		// now pick the first SKU
		// Theoretically there should be only one by now 
		
		SkuModel sku = PopulatorUtil.getDefSku(prd);
		if (sku == null) {
			return true;
		}

		FDProductInfo pInfo = null;
		try {
			pInfo = sku.getProductInfo();
		} catch (FDSkuNotFoundException ex) {
			return true;
		}

		return pInfo == null || pInfo.getVersion() == 0;
	}
	
	/** originally in GetPeakProduceTag.isProduce() **/
	public static boolean isShowRatings(String skuCode) {
		boolean matchFound = false;
		
		String _skuPrefixes=FDStoreProperties.getRatingsSkuPrefixes(); // grab sku prefixes that should show ratings
		if (_skuPrefixes!=null && !"".equals(_skuPrefixes)) { //if we have prefixes then check them

			StringTokenizer st=new StringTokenizer(_skuPrefixes, ","); //setup for splitting property
			String curPrefix = ""; //holds prefix to check against
			
			while(st.hasMoreElements()) { //loop and check each prefix
				curPrefix=st.nextToken();
				if(skuCode.startsWith(curPrefix)) { //if prefix matches get product info
					matchFound=true;
                }
				
				if (matchFound) { //exit on matched sku prefix
					break;
				}
            }
        }
		return matchFound;
	}
}
