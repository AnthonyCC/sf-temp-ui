package com.freshdirect.fdstore;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.common.ERPServiceLocator;
import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.ejb.ErpNutritionSB;
import com.freshdirect.fdstore.cache.FDAbstractCache;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDNutritionCache extends FDAbstractCache<String, Date, ErpNutritionModel> {
	
	private final static Logger LOGGER = LoggerFactory.getInstance( FDNutritionCache.class );
	private static FDNutritionCache instance;
	
	private FDNutritionCache() {
		super();
	}
	
	@Override
	public long getRefreshDelay() {
	    return DateUtil.MINUTE * FDStoreProperties.getNutritionRefreshPeriod();
	}
	
	public synchronized static FDNutritionCache getInstance(){
		if(instance == null){
		    FDCachedFactory.setupMemcached();
		    instance = new FDNutritionCache();
		    instance.startRefresher();
		}
		return instance;
	}
	
        @Override
	protected Map<String, ErpNutritionModel> loadData(Date since){
		try{
			LOGGER.info("REFRESHING");
			ErpNutritionSB sb = ERPServiceLocator.getInstance().getErpNutritionSessionBean();
			Map<String, ErpNutritionModel> data = sb.loadNutrition(since == null ? new Date(0) : since);
			LOGGER.info("REFRESHED: " + data.size());
			return data;
		} catch (RemoteException e) {
			LOGGER.error("error while loading nutrition data", e);
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
	
	@Override
	protected Logger getLog() {
	    return LOGGER;
	}
}
