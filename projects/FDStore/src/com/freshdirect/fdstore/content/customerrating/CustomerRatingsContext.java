package com.freshdirect.fdstore.content.customerrating;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.BalkingExpiringReference;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CustomerRatingsContext extends BalkingExpiringReference<List<CustomerRatingsDTO>> {

	private static CustomerRatingsContext instance = null;
	
	public static CustomerRatingsContext getInstance() {
		return getInstance(FDStoreProperties.getProductRatingRefreshInterval() * 60 * 60 * 1000);
	}

	public static CustomerRatingsContext getInstance(long refreshPeriod) {
		if (instance == null) {
			instance = new CustomerRatingsContext(refreshPeriod);
		}
		return instance;
	}
	
	private CustomerRatingsContext(long refreshPeriod) {
		super(refreshPeriod);
		this.referent = new ArrayList<CustomerRatingsDTO>();
	}

	private static final Logger LOGGER = LoggerFactory.getInstance( CustomerRatingsContext.class );

	public static long LAST_REFRESH = 0;
	
	public List<CustomerRatingsDTO> getCustomerRatings() {
		
		return get();
	}
	
	public CustomerRatingsDTO getCustomerRatingByProductId(String productId) {
		
		
		List<CustomerRatingsDTO> ratedProducts = getCustomerRatings();
		if(ratedProducts!=null){
			for (CustomerRatingsDTO ratedProduct : ratedProducts) {
				if (ratedProduct.getProductId().equals(productId)) {
					return ratedProduct;
				}
			}
		}
		return null;
	}
	
	protected List<CustomerRatingsDTO> load() {
		try {
			if(!FDStoreProperties.isProductRatingReload()){
				return referent;
			}
			BazaarvoiceUfServiceManager man = BazaarvoiceUfServiceManager.getInstance();
			//LAST_REFRESH = man.getLastRefresh();
			return man.getCustomerRatings();
		} catch (FDResourceException e) {
			LOGGER.error("Refreshing customer ratings failed!",e);
			return referent;
		}
	}
	
	/*protected boolean isExpired() {
		try {
			if(!FDStoreProperties.isProductRatingReload()){
				return false;
			}
			BazaarvoiceUfServiceManager man = BazaarvoiceUfServiceManager.getInstance();
			if (LAST_REFRESH == 0) return true;
			return man.getLastRefresh() - LAST_REFRESH > 0;
		} catch (FDResourceException e) {
			LOGGER.error("Refreshing customer ratings failed!",e);
			return true;
		}
	}*/

	
}
