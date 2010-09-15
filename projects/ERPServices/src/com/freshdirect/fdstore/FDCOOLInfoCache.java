package com.freshdirect.fdstore;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.common.ERPServiceLocator;
import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.erp.ejb.ErpCOOLManagerSB;
import com.freshdirect.fdstore.cache.FDAbstractCache;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDCOOLInfoCache extends FDAbstractCache<String, Date, ErpCOOLInfo> {

	private static Category LOGGER = LoggerFactory.getInstance( FDCOOLInfoCache.class );
	private static FDCOOLInfoCache instance;

	private FDCOOLInfoCache() {
		super(DateUtil.MINUTE * FDStoreProperties.getCOOLInfoRefreshPeriod());
	}

	public synchronized static FDCOOLInfoCache getInstance(){
		if(instance == null){
			instance = new FDCOOLInfoCache();
			instance.startRefresher();
		}
		return instance;
	}

        @Override
	protected Map<String, ErpCOOLInfo> loadData(Date since) {
		try {
			LOGGER.info("REFRESHING");
			ErpCOOLManagerSB sb = ERPServiceLocator.getInstance().getErpCOOLManagerSessionBean();
			Map<String, ErpCOOLInfo> data = sb.load(since == null ? new Date(0) : since);
			LOGGER.info("REFRESHED: " + data.size());
			return data;
		} catch (RemoteException e) {
			throw new FDRuntimeException(e);
		} catch (EJBException e) {
			throw new FDRuntimeException(e);
		}
	}

    public List getCOOLInfo(String sapMatID) {

        if (sapMatID == null || "".equals(sapMatID)) {
            return null;
        }
        ErpCOOLInfo info = this.getCachedItem(sapMatID);
        return info != null ? info.getCountryInfo() : null;
    }

}
