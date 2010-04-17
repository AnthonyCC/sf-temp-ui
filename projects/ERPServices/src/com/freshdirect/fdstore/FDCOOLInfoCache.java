package com.freshdirect.fdstore;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.erp.ejb.ErpCOOLManagerHome;
import com.freshdirect.erp.ejb.ErpCOOLManagerSB;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

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
	
	protected Map loadData(Date since) {
		try {
			LOGGER.info("REFRESHING");
			ErpCOOLManagerSB sb = this.lookupCOOLInfoHome().create();
			Map data = sb.load(since);
			LOGGER.info("REFRESHED: " + data.size());
			return data;
		} catch (RemoteException e) {
			throw new FDRuntimeException(e);
		} catch (EJBException e) {
			throw new FDRuntimeException(e);
		} catch (CreateException e) {
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
	
	public List getCOOLInfo(String sapMatID) {
		 
		if(sapMatID==null||"".equals(sapMatID)) return null;
		ErpCOOLInfo info=(ErpCOOLInfo)this.getCachedItem(sapMatID);
		return info!=null? info.getCountryInfo():null;
	}
	
	private ErpCOOLManagerHome lookupCOOLInfoHome() {
		try {
			return (ErpCOOLManagerHome) serviceLocator.getRemoteHome(ErpServicesProperties.getCOOLManagerHome());
		} catch (NamingException ne) {
			throw new EJBException(ne);
		}
	}

}
