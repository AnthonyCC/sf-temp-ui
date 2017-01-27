package com.freshdirect.fdstore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.ejb.ErpNutritionHome;
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
	
	protected Map<String,ErpNutritionModel> loadData(Date since){
		/*try{
			LOGGER.info("REFRESHING");
			ErpNutritionSB sb = this.lookupNutritionHome().create();
			Map<String,ErpNutritionModel> data = sb.loadNutrition(since);
			
			LOGGER.info("REFRESHED: " + data.size());
			return data;
		} catch (RemoteException e) {
			e.printStackTrace();
			throw new FDRuntimeException(e);
		} catch (CreateException e) {
			throw new FDRuntimeException(e);
		}*/
		return new HashMap<String,ErpNutritionModel>();
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
	
	private ErpNutritionHome lookupNutritionHome() {
		try {
			return (ErpNutritionHome) serviceLocator.getRemoteHome("freshdirect.content.Nutrition");
		} catch (NamingException ne) {
			throw new EJBException(ne);
		}
	}

}
