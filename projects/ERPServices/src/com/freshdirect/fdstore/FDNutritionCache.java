package com.freshdirect.fdstore;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.ejb.ErpNutritionHome;
import com.freshdirect.content.nutrition.ejb.ErpNutritionSB;
import com.freshdirect.ecomm.gateway.ErpNutritionService;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;

public class FDNutritionCache extends FDAbstractCache<String,ErpNutritionModel> {
	
	private static Category LOGGER = LoggerFactory.getInstance( FDNutritionCache.class );
	private static FDNutritionCache instance;
	
	private FDNutritionCache() {
		super(DateUtil.MINUTE * FDStoreProperties.getNutritionRefreshPeriod());
	}
	
	public synchronized static FDNutritionCache getInstance(){
		if(instance == null){
			instance = new FDNutritionCache();
		}
		return instance;
	}
	
	protected Map<String,ErpNutritionModel> loadData(Date since){
		try{
			LOGGER.info("REFRESHING");
			ErpNutritionSB sb = this.lookupNutritionHome().create();
			Map<String,ErpNutritionModel> data ;
			if(FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.ErpNutritionSB)){
				data=ErpNutritionService.getInstance().loadNutrition(since);
			}else{
			data= sb.loadNutrition(since);
			}
			LOGGER.info("REFRESHED: " + data.size());
			return data;
		} catch (Exception e) {
			throw new FDRuntimeException(e);
		}
	}
	
	public ErpNutritionModel getNutrition(String skuCode) {
		ErpNutritionModel model = (ErpNutritionModel) this.getCachedItem(skuCode);
		if(model == null){
			model = new ErpNutritionModel();
			model.setSkuCode(skuCode);
		}
		return model;
	}
	
	protected Date getModifiedDate(ErpNutritionModel item) {
		if(!(item instanceof ErpNutritionModel)){
			return null;
		}
		return ((ErpNutritionModel)item).getLastModifiedDate();
	}
	
	private ErpNutritionHome lookupNutritionHome() throws Exception {
		
		return (ErpNutritionHome) serviceLocator.getRemoteHome("freshdirect.content.Nutrition");
		
	}

}
