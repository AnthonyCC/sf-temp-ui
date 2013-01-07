package com.freshdirect.fdstore.content.customerrating;

import java.util.List;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.customerrating.CustomerRatingsDAO;
import com.freshdirect.fdstore.content.customerrating.CustomerRatingsDTO;
import com.freshdirect.framework.core.SessionBeanSupport;

public class BazaarvoiceUfServiceSessionBean extends SessionBeanSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4735004684491213606L;
	
	private static CustomerRatingsDAO customerRatingsDAO = new CustomerRatingsDAO();
	
	public BazaarvoiceFeedProcessResult processFile() {
		 return new UploadFeedProcessTask().process();
	}

	public BazaarvoiceFeedProcessResult processRatings() {
		BazaarvoiceFeedProcessResult result = new DownloadFeedProcessTask().process();
		if (result.isSuccess()) {
			result = new StoreFeedTask().process();
		}
		return result;
	}
	
	public long getLastRefresh() throws FDResourceException{
		return customerRatingsDAO.getTimestamp();
	}
	
	public List<CustomerRatingsDTO> getCustomerRatings() throws FDResourceException{
		return customerRatingsDAO.getCustomerRatings();
	}
}
