package com.freshdirect.fdstore;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.common.ERPServiceLocator;
import com.freshdirect.erp.ejb.ErpInfoSB;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.fdstore.cache.FDAbstractCache;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDInventoryCache extends FDAbstractCache<String, Date, ErpInventoryModel> {
	private final static Logger LOGGER = LoggerFactory.getInstance(FDInventoryCache.class);

	private static FDInventoryCache instance;

	private FDInventoryCache() {
		super();
	}
	
	@Override
	public long getRefreshDelay() {
	    return DateUtil.MINUTE * FDStoreProperties.getInventoryRefreshPeriod();
	}

        public synchronized static FDInventoryCache getInstance() {
            if (instance == null) {
                instance = new FDInventoryCache();
                instance.startRefresher();
            }
            return instance;
        }

	@Override
	protected Map<String, ErpInventoryModel> loadData(Date since) {
		try {
			LOGGER.info("REFRESHING");
			ErpInfoSB sb = ERPServiceLocator.getInstance().getErpInfoSessionBean();
			Map<String, ErpInventoryModel> data = sb.loadInventoryInfo(since == null ? new Date(0) : since);
			LOGGER.info("REFRESHED: " + data.size());
			return data;
		} catch (RemoteException e) {
			throw new FDRuntimeException(e);
		}
	}

	public ErpInventoryModel getInventory(String materialId) {
		return (ErpInventoryModel) this.getCachedItem(materialId);
	}
	
	@Override
	protected Logger getLog() {
	    return LOGGER;
	}
}
