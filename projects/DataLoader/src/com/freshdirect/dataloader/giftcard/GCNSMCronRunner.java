package com.freshdirect.dataloader.giftcard;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.framework.util.log.LoggerFactory;

public class GCNSMCronRunner {
	private final static Category LOGGER = LoggerFactory.getInstance(GCNSMCronRunner.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			LOGGER.info("Inside GCNSMCronRunner..");
			FDCustomerManager.resubmitGCOrders();
			LOGGER.info("Completed GCNSMCronRunner..");
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}

}
