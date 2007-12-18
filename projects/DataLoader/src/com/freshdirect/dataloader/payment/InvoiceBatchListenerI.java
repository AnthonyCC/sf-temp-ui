package com.freshdirect.dataloader.payment;

/*
 * InvoiceBatchListenerI.java
 * Date: Jun 25, 2002 : 11:00:51 AM
 */
import com.freshdirect.dataloader.LoaderException;

/**
 * @author knadeem
 *
 */

public interface InvoiceBatchListenerI {
	
	public void processInvoiceBatch(String folder, String fileName) throws LoaderException;

}

