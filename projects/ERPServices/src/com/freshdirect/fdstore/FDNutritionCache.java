package com.freshdirect.fdstore;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.ecomm.gateway.ErpNutritionService;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

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
	
	protected Map<String, ErpNutritionModel> loadData(Date since) {
		try {
			LOGGER.info("REFRESHING: " + (since == null? "0" : since.getTime()));

			Map<String, ErpNutritionModel> data;
			data = ErpNutritionService.getInstance().loadNutrition(since);
			
			LOGGER.info("REFRESHED: " + (since == null? "0" : since.getTime()) + " , size:" + data.size());
			return data;
		} catch (Exception e) {
			LOGGER.error("REFRESH FAILED: " + (since == null? "0" : since.getTime()), e);
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
	
	

}
