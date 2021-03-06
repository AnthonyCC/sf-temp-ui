package com.freshdirect.fdstore;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.freshdirect.ecomm.gateway.AttributeFacadeService;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;

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
	
	@Override
    protected Map loadData(Date since) {
		try {
			Map data=null;
			LOGGER.info("REFRESHING");
			if(FDStoreProperties.isSF2_0_AndServiceEnabled("attributes.ejb.AttributeFacadeSB")){
				data=AttributeFacadeService.getInstance().loadAttributes(since);
			}else{
			AttributeFacadeSB sb = this.lookupAttributeHome().create();
			data = sb.loadAttributes(since);
			}
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
	
	@Override
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
	
    public Map<String, Object> getAttributeByPathId(String[] pathIds) {
        return getAttributes(Arrays.copyOf(pathIds, 1)).getAttributeMap(pathIds);
    }

	private AttributeFacadeHome lookupAttributeHome() {
		try {
			return (AttributeFacadeHome) serviceLocator.getRemoteHome("freshdirect.content.AttributeFacade");
		} catch (NamingException ne) {
			throw new EJBException(ne);
		}
	}

}
