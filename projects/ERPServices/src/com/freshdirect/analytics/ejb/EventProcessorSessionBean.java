package com.freshdirect.analytics.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.analytics.CEPService;
import com.freshdirect.analytics.SessionDAO;
import com.freshdirect.analytics.TimeslotEventDAO;
import com.freshdirect.analytics.TimeslotEventModel;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class EventProcessorSessionBean extends SessionBeanSupport{
	
	private static final Category LOGGER = LoggerFactory.getInstance(EventProcessorSessionBean.class);
	public List getEvents() throws RemoteException
	{
		final String METHODNAME = "getEvents()";
		Connection conn = null;
		List events = null;
		LOGGER.info("start: "+METHODNAME );
		try 
		{
			conn  = this.getConnection();
			events =  TimeslotEventDAO.getTimeslotEvents(conn);
			//events.addAll(TimeslotEventDAO.getCancelledReservationEvents(conn));
			events.addAll(TimeslotEventDAO.getOrders(conn));
			CEPService.insert(events);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}
		LOGGER.info("end: "+METHODNAME);
		
		return events;
	}	
	
	public void logEvent(String customerId, Date loginTime)
	{
		Connection conn = null;
		try 
		{
			conn  = this.getConnection();
			SessionDAO.insert(customerId, loginTime, new Date());
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}	
	}
	  
}