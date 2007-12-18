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

import com.freshdirect.content.attributes.AttributeException;
import com.freshdirect.content.attributes.FlatAttribute;
import com.freshdirect.content.attributes.FlatAttributeCollection;
import com.freshdirect.content.attributes.ejb.AttributeFacadeHome;
import com.freshdirect.content.attributes.ejb.AttributeFacadeSB;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDAttributeCache extends FDAbstractCache {
	
	private static Category LOGGER = LoggerFactory.getInstance( FDAttributeCache.class );
	private static FDAttributeCache instance;
	
	private FDAttributeCache() {
		super(DateUtil.MINUTE * FDStoreProperties.getAttributesRefreshPeriod());
	}
	
	public synchronized static FDAttributeCache getInstance(){
		if(instance == null){
			instance = new FDAttributeCache();
		}
		return instance;
	}
	
	protected Map loadData(Date since) {
		try {
			LOGGER.info("REFRESHING");
			AttributeFacadeSB sb = this.lookupAttributeHome().create();
			Map data = sb.loadAttributes(since);
			LOGGER.info("REFRESHED: " + data.size());
			return data;
		} catch (RemoteException e) {
			throw new FDRuntimeException(e);
		} catch (CreateException e) {
			throw new FDRuntimeException(e);
		} catch (AttributeException e) {
			throw new FDRuntimeException(e);
		}
	}
	
	protected Date getModifiedDate(Object item) {
		if(!(item instanceof List)){
			return null;
		}
		Date d = new Date(0);
		for(Iterator i = ((List)item).iterator(); i.hasNext(); ){
			FlatAttribute ra = (FlatAttribute) i.next();
			if(d.before(ra.getLastModifiedDate())){
				d = ra.getLastModifiedDate();
			}
		}
		return d;
	}
	
	public FlatAttributeCollection getAttributes(String[] rootIds) {
		List attribs = new ArrayList();
		for(int i = 0; i < rootIds.length; i++) {
			List lst = (List) this.getCachedItem(rootIds[i]);
			if(lst != null) {
				attribs.addAll(lst);
			}
		}
		
		return new FlatAttributeCollection(attribs);
	}
	
	private AttributeFacadeHome lookupAttributeHome() {
		try {
			return (AttributeFacadeHome) serviceLocator.getRemoteHome("freshdirect.content.AttributeFacade", AttributeFacadeHome.class);
		} catch (NamingException ne) {
			throw new EJBException(ne);
		}
	}

}
