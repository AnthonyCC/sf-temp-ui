package com.freshdirect.fdstore.content.customerrating;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.BalkingExpiringReference;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CustomerRatingsContext extends BalkingExpiringReference<List<CustomerRatingsDTO>> {

	private static CustomerRatingsContext instance = null;
	
	public static CustomerRatingsContext getInstance() {
		return getInstance(12 * 60 * 60 * 1000);
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
	
	private static CustomerRatingsDAO customerRatingsDAO = new CustomerRatingsDAO();
	
	public List<CustomerRatingsDTO> getCustomerRatings() {
		
		return get();
	}
	
	public CustomerRatingsDTO getCustomerRatingByProductId(String productId) {
		
		
		List<CustomerRatingsDTO> ratedProducts = getCustomerRatings();
		for (CustomerRatingsDTO ratedProduct : ratedProducts) {
			if (ratedProduct.getProductId().equals(productId)) {
				return ratedProduct;
			}
		}
		return null;
	}
	
	protected List<CustomerRatingsDTO> load() {
		try {
			LAST_REFRESH = customerRatingsDAO.getTimestamp();
			return customerRatingsDAO.getCustomerRatings();
		} catch (FDResourceException e) {
			LOGGER.error("Refreshing customer ratings failed!",e);
			return referent;
		}
	}
	
	protected boolean isExpired() {
		try {
			return customerRatingsDAO.getTimestamp() - LAST_REFRESH > 0;
		} catch (FDResourceException e) {
			LOGGER.error("Refreshing customer ratings failed!",e);
			return true;
		}
	}

	
}
