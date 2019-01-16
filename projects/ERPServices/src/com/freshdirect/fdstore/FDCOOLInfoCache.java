package com.freshdirect.fdstore;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.erp.ErpCOOLKey;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;

public class FDCOOLInfoCache extends FDAbstractCache {
	
	private static Category LOGGER = LoggerFactory.getInstance( FDCOOLInfoCache.class );
	private static FDCOOLInfoCache instance;
	
	private FDCOOLInfoCache() {
		super(DateUtil.MINUTE * FDStoreProperties.getCOOLInfoRefreshPeriod());
	}
	
	public synchronized static FDCOOLInfoCache getInstance(){
		if(instance == null){
			instance = new FDCOOLInfoCache();
		}
		return instance;
	}
	
	protected Map<ErpCOOLKey, ErpCOOLInfo> loadData(Date since) {
		Map<ErpCOOLKey, ErpCOOLInfo> data = new HashMap<ErpCOOLKey, ErpCOOLInfo>();
		try {

			data = FDECommerceService.getInstance().getCountryOfOriginData(since);

			LOGGER.info("FDCOOLInfoCache REFRESHED: " + data.size());
			return data;
		} catch (RemoteException e) {
			throw new FDRuntimeException(e);
		} catch (EJBException e) {
			throw new FDRuntimeException(e);
		}
	}
	
	protected Date getModifiedDate(Object item) {
		if(!(item instanceof ErpCOOLInfo)){
			return null;
		}
		Date d = new Date(0);
		ErpCOOLInfo info=(ErpCOOLInfo)item;
		if(d.before(info.getLastModifiedDate())){
			d = info.getLastModifiedDate();
		}
		
		return d;
	}
	
	public List<String> getCOOLInfo(String sapMatID, String plantID) {		 
		if(sapMatID==null||"".equals(sapMatID)) return null;
		ErpCOOLKey key= new ErpCOOLKey(sapMatID,plantID);
		ErpCOOLInfo info=(ErpCOOLInfo)this.getCachedItem(key);
		return info!=null? info.getCountryInfo():null;
	}

}
