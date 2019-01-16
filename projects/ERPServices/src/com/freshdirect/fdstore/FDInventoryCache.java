package com.freshdirect.fdstore;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.ecomm.gateway.ErpInfoService;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDInventoryCache extends FDAbstractCache<String, ErpInventoryModel> {
	private static Category LOGGER = LoggerFactory.getInstance(FDInventoryCache.class);

    private static class InstanceHolder {
        private static final FDInventoryCache INSTANCE = new FDInventoryCache();
    }

	private FDInventoryCache(){
		super(DateUtil.MINUTE * FDStoreProperties.getInventoryRefreshPeriod());
	}

    public static FDInventoryCache getInstance() {
        return InstanceHolder.INSTANCE;
    }

	@Override
	protected Map<String, ErpInventoryModel> loadData(Date since) {
		try {
			LOGGER.info("REFRESHING entries newer than "+ new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.S").format(since));
			Map<String, ErpInventoryModel> data;
			
				data = ErpInfoService.getInstance().loadInventoryInfo(since);
			
			LOGGER.info("REFRESHED: " + data.size());
			//LOGGER.debug("REFRESHED ENTRIES: " + data);
			return data;
		} catch (RemoteException e) {
			throw new FDRuntimeException(e);
		} 
	}

	@Override
    protected Date getModifiedDate(ErpInventoryModel item) {
		if(!(item instanceof ErpInventoryModel)){
			return null;
		}
		return item.getLastUpdated();
	}

	public ErpInventoryModel getInventory(String materialId) {
		return this.getCachedItem(materialId);
	}



}
