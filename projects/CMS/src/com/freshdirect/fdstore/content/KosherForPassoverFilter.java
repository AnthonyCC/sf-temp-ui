/*
 * Created on Feb 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.freshdirect.fdstore.content;

import java.util.Iterator;

import org.apache.log4j.Category;

import com.freshdirect.content.nutrition.EnumClaimValue;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.framework.util.log.LoggerFactory;

public class KosherForPassoverFilter extends AbstractProductFilter{
	private static final Category LOGGER = LoggerFactory.getInstance( KosherForPassoverFilter.class );
	public boolean applyTest(ProductModel product) throws FDResourceException {
		
		SkuModel sku=null;
			boolean result = false;
			LOGGER.debug("IN Kosher for Passover Filter with product: "+product.getContentName());
			 for (Iterator iSku = product.getSkus().iterator(); iSku.hasNext() && result==false;) {
			 	sku = (SkuModel) iSku.next();
				try {
					if (sku.isUnavailable()) continue;
					result = sku.getProduct().hasClaim(EnumClaimValue.KOSHER_FOR_PASSOVER);
				} catch (FDSkuNotFoundException snfe) {
					LOGGER.warn("Caught FDSkuNotFoundException with sku"+sku.getSkuCode(),snfe);
					continue;
				} catch(FDResourceException fre) {
					LOGGER.warn("Caught FDResourceException with sku "+sku.getSkuCode(),fre);
					throw fre;
				}
			 }
			LOGGER.debug("product: "+product.getContentName()+"  returning: "+result);
			return result;
	}

}


