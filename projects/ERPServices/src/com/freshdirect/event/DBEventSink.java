package com.freshdirect.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.sql.DataSource;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.event.EventSinkI;
import com.freshdirect.framework.event.FDEvent;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author knadeem Date May 3, 2005
 */
public class DBEventSink implements EventSinkI {
	
	private static Category LOGGER = LoggerFactory.getInstance(DBEventSink.class);

	private final DataSource dataSource;

	private static final String EVENT_INSERT = "INSERT INTO CUST.EVENTS (CUSTOMER_ID, COOKIE, URL, QUERY_STRING, EVENT_TYPE, TIMESTAMP, APPLICATION, "
		+ "SERVER, TRACKING_CODE, SOURCE, PARAM_1, PARAM_2, PARAM_3, PARAM_4, PARAM_5, PARAM_6, PARAM_7, PARAM_8, PARAM_9, PARAM_10, PARAM_11, PARAM_12, PARAM_13, PARAM_14, PARAM_15) "
		+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public DBEventSink(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public boolean log(FDEvent event) {
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = this.dataSource.getConnection();
			ps = conn.prepareStatement(EVENT_INSERT);
			int idx = 1;

			ps.setString(idx++, event.getCustomerId());
			ps.setString(idx++, event.getCookie());
			ps.setString(idx++, event.getUrl());
			ps.setString(idx++, event.getQueryString());
			ps.setString(idx++, event.getEventType());
			ps.setTimestamp(idx++, new Timestamp(event.getTimestamp().getTime()));
			ps.setString(idx++, event.getApplication());
			ps.setString(idx++, event.getServer());
			ps.setString(idx++, event.getTrackingCode());
			ps.setString(idx++, event.getSource() == null ? null : event
					.getSource().getName());

			String[] values = event.getEventValues();
			for (int i = 0; i < values.length; i++) {
				ps.setString(idx++, values[i]);
			}

			if (ps.executeUpdate() != 1) {
				throw new FDRuntimeException("Could not log the event: " + event);
			}

			return true;

		} catch (SQLException e) {
			throw new FDRuntimeException(e, "SQLException while trying to LOG event: " + event);
		}finally {
			try{
				if(ps != null){
					ps.close();
				}
			}catch(SQLException e){
				LOGGER.warn("Could not close Statement due to: ", e);
			}
			try{
				if(conn != null){
					conn.close();
				}
			}catch(SQLException e){
				LOGGER.warn("Could not close connection due to: ", e);
			}
		}
	}
}
