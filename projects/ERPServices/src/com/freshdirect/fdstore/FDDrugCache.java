package com.freshdirect.fdstore;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.content.nutrition.NutritionDrugPanel;
import com.freshdirect.content.nutrition.ejb.ErpNutritionHome;
import com.freshdirect.content.nutrition.ejb.ErpNutritionSB;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDDrugCache extends FDAbstractCache {
	
	private static Category LOGGER = LoggerFactory.getInstance( FDDrugCache.class );
	private static FDDrugCache instance;

	public FDDrugCache(long refreshDelay) {
		super(refreshDelay);
		// TODO Auto-generated constructor stub
	}

	private FDDrugCache() {
		super(DateUtil.MINUTE * FDStoreProperties.getNutritionRefreshPeriod());
	}
	
	public synchronized static FDDrugCache getInstance(){
		if(instance == null){
			instance = new FDDrugCache();
		}
		return instance;
	}
	
	protected Map loadData(Date since){
		try{
			LOGGER.info("REFRESHING");
			ErpNutritionSB sb = this.lookupNutritionHome().create();
			Map data = sb.loadDrugPanels(since);
			
			LOGGER.info("REFRESHED: " + data.size());
			return data;
		} catch (RemoteException e) {
			e.printStackTrace();
			throw new FDRuntimeException(e);
		} catch (CreateException e) {
			throw new FDRuntimeException(e);
		}
	}
	
	public NutritionDrugPanel getDrugPanel(String skuCode) {
		NutritionDrugPanel model = (NutritionDrugPanel) this.getCachedItem(skuCode);
		return model;
	}
	
	protected Date getModifiedDate(Object item) {
		if(!(item instanceof NutritionDrugPanel)){
			return null;
		}
		return ((NutritionDrugPanel)item).getLastModifiedDate();
	}
	
	private ErpNutritionHome lookupNutritionHome() {
		try {
			return (ErpNutritionHome) serviceLocator.getRemoteHome("freshdirect.content.Nutrition");
		} catch (NamingException ne) {
			throw new EJBException(ne);
		}
	}

}
