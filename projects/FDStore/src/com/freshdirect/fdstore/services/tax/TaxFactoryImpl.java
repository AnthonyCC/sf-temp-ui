package com.freshdirect.fdstore.services.tax;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.customer.EnumNotificationType;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartI;

/*
 * @author Nakkeeran Annamalai
 */
public class TaxFactoryImpl implements TaxFactory {
	
	private Map<String,TaxStrategy> taxStrategies;
	
	private static final Logger LOGGER = Logger.getLogger(TaxFactoryImpl.class);
	
	public AvalaraContext getTax(AvalaraContext avalaraContext){
		TaxStrategy taxStrategy = getTaxStrategies().get("FD_TRADITIONAL_TAX");
		
		if(FDStoreProperties.getAvalaraTaxEnabled()){ 
			taxStrategy = getTaxStrategies().get("AVALARA_TAX");
			LOGGER.info("Avalara Tax Strategy is selected");
		}

		try{
			AvalaraContext response = taxStrategy.getTaxResponse(avalaraContext);
			if(response.isAvalaraTaxed()){
			avalaraContext.getCart().setTaxationType(EnumNotificationType.AVALARA);
			}
			else{
				LOGGER.info("Avalara call attempted but there are missing fields: Orderlines are: "+avalaraContext.getCart().getOrderLines() 
						+"  and delivery address is: "+avalaraContext.getCart().getDeliveryAddress() + 
						" for order: "+((null!=avalaraContext.getCart().getOrderLines() && !avalaraContext.getCart().getOrderLines().isEmpty() && null!=avalaraContext.getCart().getOrderLines().get(0))?avalaraContext.getCart().getOrderLines().get(0).getOrderId():null));
			}
			return response;
		} catch(FDException e){
			LOGGER.error("Error in AValara Tax Calculation - Defaults to traditional tax");
			try{
				taxStrategy = getTaxStrategies().get("FD_TRADITIONAL_TAX");
				AvalaraContext response = taxStrategy.getTaxResponse(avalaraContext);
				response.setAvalaraTaxed(false);
				return response;
			} catch(FDException e1){
				LOGGER.error("Error in Traditional tax calculation");
			}
		}
		return null;
	}
	
	@Override
	public boolean cancelTax(AvalaraContext avalaraContext){
		if(FDStoreProperties.getAvalaraTaxEnabled()){
			TaxStrategy taxStrategy = getTaxStrategies().get("AVALARA_TAX");
			return taxStrategy.cancelTax(avalaraContext);
		}
		return false;
	}

	public Map<String,TaxStrategy> getTaxStrategies() {
		taxStrategies = new HashMap<String,TaxStrategy>();
		taxStrategies.put("AVALARA_TAX", new AvalaraTaxStrategyImpl());
		taxStrategies.put("FD_TRADITIONAL_TAX", new FDTaxStrategyImpl());
		return taxStrategies;
	}

	public void setTaxStrategies(Map<String,TaxStrategy> taxStrategies) {
		this.taxStrategies = taxStrategies;
	}
}