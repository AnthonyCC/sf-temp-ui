package com.freshdirect.fdstore;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.erp.ejb.ErpInfoHome;
import com.freshdirect.erp.ejb.ErpInfoSB;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDInventoryCache extends FDAbstractCache {
	private static Category LOGGER = LoggerFactory.getInstance(FDInventoryCache.class);
	
	private static FDInventoryCache instance;
	
	private static final Map EMPTY=new HashMap();
	
	private FDInventoryCache(){
		super(DateUtil.MINUTE * FDStoreProperties.getInventoryRefreshPeriod());
	}
	
	public synchronized static FDInventoryCache getInstance(){
		if(instance == null){
			instance = new FDInventoryCache();
		}
		return instance;
	}
	
	protected Map loadData(Date since) {
		try {
			LOGGER.info("REFRESHING entries newer than "+ new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.S").format(since));
			ErpInfoSB sb = this.lookupInfoHome().create();
			Map data = sb.loadInventoryInfo(since);
			LOGGER.info("REFRESHED: " + data.size());
			//LOGGER.debug("REFRESHED ENTRIES: " + data);
			return data;
		} catch (RemoteException e) {
			LOGGER.error("Failed to refresh due to " + e);
			//throw new FDRuntimeException(e);
		} catch (CreateException e) {
			LOGGER.error("Failed to refresh due to " + e);
			//throw new FDRuntimeException(e);
		}
		return EMPTY;
	}
	
	protected Date getModifiedDate(Object item) {
		if(!(item instanceof ErpInventoryModel)){
			return null;
		}
		return ((ErpInventoryModel)item).getLastUpdated();
	}
	
	public ErpInventoryModel getInventory(String materialId) {
		return (ErpInventoryModel) this.getCachedItem(materialId);
	}
	
	private ErpInfoHome lookupInfoHome() {
		try {
			return (ErpInfoHome) serviceLocator.getRemoteHome("freshdirect.erp.Info");
		} catch (NamingException ne) {
			throw new EJBException(ne);
		}
	}

}
