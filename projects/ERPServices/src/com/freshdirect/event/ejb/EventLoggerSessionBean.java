package com.freshdirect.event.ejb;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Category;

import com.freshdirect.event.DBEventSink;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.core.DataSourceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.event.EventSinkI;
import com.freshdirect.framework.event.FDWebEvent;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author knadeem Date May 3, 2005
 */

public class EventLoggerSessionBean extends SessionBeanSupport {
	
	private static Category LOGGER = LoggerFactory.getInstance(EventLoggerSessionBean.class);
	private static EventSinkI dbSink;
	
	public void log(FDWebEvent event) {
		if(dbSink == null) {
			dbSink = new DBEventSink(this.getDataSource());
		}
		dbSink.log(event); 
	}
	
	private DataSource getDataSource () {
		try {
			return DataSourceLocator.getDataSource(this.getResourceCacheKey());
		} catch (NamingException e) {
			LOGGER.warn("Cannot obtain a DataSource", e);
			throw new FDRuntimeException(e, "Cannot obtain a DataSource");
		}
	}
	
	protected String getResourceCacheKey() {
		return "com.freshdirect.event.ejb.EventLoggerSessionBean" ;
	}
}
