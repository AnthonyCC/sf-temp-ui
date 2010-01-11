package com.freshdirect.event.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Category;

import com.freshdirect.event.RecommendationEventsAggregate;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.event.FDRecommendationEvent;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * Impression logger implementation.
 * 
 * @author istvan
 *
 */
public class RecommendationEventLoggerSessionBean extends SessionBeanSupport {

	private static final long serialVersionUID = 501869530989444843L;
	private static Category LOGGER = LoggerFactory.getInstance(RecommendationEventLoggerSessionBean.class);
	
	private static final String IMPRESSION_INSERT = 
		"INSERT INTO CUST.LOG_IMPRESSIONS (CONTENT_ID,VARIANT_ID,TIMESTAMP,FREQUENCY) VALUES(?,?,?,?)";
	
	private static final String CLICKTHROUGH_INSERT =
		"INSERT INTO CUST.LOG_CLICKTHROUGHS (CONTENT_ID,VARIANT_ID,TIMESTAMP,FREQUENCY) VALUES(?,?,?,?)";
	
	private void logRecommendationEvent(Connection conn, FDRecommendationEvent event, int frequency) {
		PreparedStatement ps = null;
		String statement = null;
		try {
			statement = 
				event instanceof FDRecommendationEvent.Impression ?
						IMPRESSION_INSERT:
						CLICKTHROUGH_INSERT;
			
			ps = conn.prepareStatement(statement);
			ps.setString(1, event.getContentId());
			ps.setString(2, event.getVariantId());
			ps.setDate(3, new java.sql.Date(event.getTimeStamp().getTime()));
			ps.setInt(4, frequency);
			
			if (ps.executeUpdate() != 1) {
				throw new FDRuntimeException("Could not execute " + statement);
			}
		} catch(SQLException e) {
			throw new FDRuntimeException("Could not execute " + statement);
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					LOGGER.warn("Could not close connection",e);
				}
		}
	}
	
	private void logRecommendationEvents(Connection conn, Class eventClazz, Collection events) {
	
		if (events.size() == 0) return;
		
		PreparedStatement ps = null;
		String statement = null;
		try {
			statement = 
				 eventClazz == FDRecommendationEvent.Impression.class ?
						IMPRESSION_INSERT:
						CLICKTHROUGH_INSERT;
			
			ps = conn.prepareStatement(statement);
			
			int totalToWrite = 0;
			for(Iterator i = events.iterator(); i.hasNext();) {
				RecommendationEventsAggregate eventAggregate = (RecommendationEventsAggregate)i.next();
				if (eventAggregate.getFrequency() == 0) continue;
				ps.setString(1, eventAggregate.getContentId());
				ps.setString(2, eventAggregate.getVariantId());
				ps.setDate(3, new java.sql.Date(eventAggregate.getDate().getTime()));
				ps.setInt(4, eventAggregate.getFrequency());
				ps.addBatch();
				++totalToWrite;
			}
			
			int [] results = ps.executeBatch();
			
			int totalWritten = totalToWrite;
			
			for(int i = 0; i< results.length; ++i) if (results[i] == Statement.EXECUTE_FAILED) --totalWritten;
			if (totalWritten < totalToWrite) {
				LOGGER.warn("Only " + totalWritten + " events were logged out of " + totalToWrite);
			}
			
		} catch(SQLException e) {
			throw new FDRuntimeException("Could not execute " + statement);
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					LOGGER.warn("Could not close connection",e);
				}
		}
	}
	
	public void log(Class eventClazz, Collection events) throws RemoteException {
		Connection conn = null;
		try {
			conn = getConnection();
			logRecommendationEvents(conn,eventClazz,events);
		} catch (SQLException e) {
			LOGGER.warn("Could not log " + events.size() + " impressions",e);
			throw new RemoteException("Could not log " + events.size() + " impressions",e);
		} finally {
                    close(conn);
		}
	}
	
	public void log(FDRecommendationEvent event, int frequency) throws RemoteException {
		Connection conn = null;
		try {
			conn = getConnection();
			logRecommendationEvent(conn,event,frequency);
		} catch (SQLException e) {
			LOGGER.warn("Could not log event: " + event,e);
			throw new RemoteException("Could not log event: " + event,e);
		} finally {
                    close(conn);
		}
	}
	
}
