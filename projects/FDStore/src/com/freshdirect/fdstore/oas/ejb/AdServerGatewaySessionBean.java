package com.freshdirect.fdstore.oas.ejb;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.ejb.EJBException;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.framework.core.GatewaySessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

/**@author ekracoff on May 25, 2004*/
public class AdServerGatewaySessionBean extends GatewaySessionBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(AdServerGatewaySessionBean.class);
	
	private HashSet results;
	
	public void run() throws RemoteException{
		results = new HashSet();

		Set productKeys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.PRODUCT);
		for (Iterator i = productKeys.iterator(); i.hasNext(); ){
			ContentKey k = (ContentKey)i.next();
			ProductModel pm = (ProductModel) ContentFactory.getInstance().getContentNode(k.getId());
			this.visit(pm);
		}
		
		LOGGER.info("Enqueueing results");
		enqueue(results);
		LOGGER.info("Enqueueing finished");
	}

	/*
	 * For each product get all the pricing tiers
	 * off the default sku and send to OAS
	 */
	public void visit(ProductModel prod) {
		SkuModel sku = prod.getDefaultSku();
		//if default sku is null we dont care about pricing
		if(sku == null){
			results.add(new AdServerRow(prod.getContentName(), false, null));
		} else {
			evaluatePrices(prod, sku);
		}

		
	}

	private void evaluatePrices(ProductModel prod, SkuModel sku) {
		try {
			MaterialPrice[] prices = sku.getProduct().getPricing().getMaterialPrices();
			
			for (int i=0; i<prices.length; i++) {
				MaterialPrice mp = prices[i];
				String price = mp.getScaleLowerBound() + "@" + mp.getPrice();
				results.add(new AdServerRow(prod.getContentName(), !prod.isUnavailable(), price));
			}
		} catch (FDResourceException e) {
			throw new RuntimeException(e);
		} catch (FDSkuNotFoundException e) {
			//keep going if this happens
			e.printStackTrace();
		}
	}

	private void enqueue(HashSet results) {

		try {
			ObjectMessage rowMsg = qsession.createObjectMessage();
			rowMsg.setObject(results);

			this.qsender.send(rowMsg);
			LOGGER.info("Queued row with set of " + results.size() + " products");

		} catch (JMSException ex) {
			LOGGER.warn("Error enqueueing Capture Message", ex);
			throw new EJBException(ex);
		} catch (Throwable t) {
			LOGGER.warn("Unexpected exception queueing row", t);
			throw new EJBException(t.getMessage());
		}
	}

}
