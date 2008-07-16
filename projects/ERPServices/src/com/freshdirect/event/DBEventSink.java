package com.freshdirect.event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.sql.DataSource;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.event.EventSinkI;
import com.freshdirect.framework.event.FDWebEvent;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author knadeem Date May 3, 2005
 */
public class DBEventSink implements EventSinkI {
	
	private static Category LOGGER = LoggerFactory.getInstance(DBEventSink.class);

	private final DataSource dataSource;

	private static final String EVENT_INSERT = "INSERT INTO CUST.LOG_CART_EVENTS"
		// base FDEvent parameters (10)
		+ "(ID, CUSTOMER_ID, COOKIE, URL, QUERY_STRING, EVENT_TYPE, TIMESTAMP, APPLICATION, "
		+ "SERVER, TRACKING_CODE, SOURCE, "
		// FDCartLineEvent parameters (12)
		+ "PRODUCT_ID, SKU_CODE, CATEGORY_ID, DEPARTMENT_ID, "
		+ "CARTLINE_ID, QUANTITY, SALES_UNIT, CONFIGURATION, "
		+ "YMAL_CATEGORY, YMAL_PRODUCT, YMAL_SET_ID, CCL_ID, VARIANT_ID) "
		+ "VALUES(CUST.LOG_CART_EVENTS_SEQ.NEXTVAL, ?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public DBEventSink(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public boolean log(FDWebEvent event) {
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = this.dataSource.getConnection();
			ps = conn.prepareStatement(EVENT_INSERT);
			int idx = 1;

			// Set general attributes
			//
			ps.setString(idx++, event.getCustomerId());
			ps.setString(idx++, event.getCookie());
			ps.setString(idx++, event.getUrl());
			ps.setString(idx++, event.getQueryString());
			ps.setString(idx++, event.getEventType());
			ps.setTimestamp(idx++, new Timestamp(event.getTimestamp().getTime()));
			ps.setString(idx++, event.getApplication());
			ps.setString(idx++, event.getServer());
			ps.setString(idx++, event.getTrackingCode());
			ps.setString(idx++, event.getSource() == null ? null : event.getSource().getName());

			// Set specific attributes
			//
			if (event instanceof FDCartLineEvent) {
				FDCartLineEvent cle = (FDCartLineEvent) event;
				
				ps.setString(idx++, cle.getProductId());		// 0
				ps.setString(idx++, cle.getSkuCode());			// 1
				ps.setString(idx++, cle.getCategoryId());		// 2
				ps.setString(idx++, cle.getDepartment());		// 3
				ps.setString(idx++, cle.getCartlineId());		// 4

				ps.setString(idx++, cle.getQuantity());			// 5
				ps.setString(idx++, cle.getSalesUnit());		// 6
				ps.setString(idx++, cle.getConfiguration());	// 7

				ps.setString(idx++, cle.getYmalCategory());		// 8
				ps.setString(idx++, cle.getOriginatingProduct());	// 9
				ps.setString(idx++, cle.getYmalSet());			// 10

				ps.setString(idx++, cle.getCclId());			// 11
				
				ps.setString(idx++, cle.getVariantId());		// 12
			} else {
				LOGGER.error("Skipped event with unknown class " + event.getClass().getName() );
				return false;
			}

			if (ps.executeUpdate() != 1) {
				throw new FDRuntimeException("Could not log the event: " + event);
			}

			return true;

		} catch (SQLException e) {
			throw new FDRuntimeException(e, "SQLException while trying to LOG event: " + event);
		} finally {
			try {
				if(ps != null){
					ps.close();
				}
			} catch(SQLException e){
				LOGGER.warn("Could not close Statement due to: ", e);
			} try {
				if(conn != null){
					conn.close();
				}
			} catch(SQLException e){
				LOGGER.warn("Could not close connection due to: ", e);
			}
		}
	}
}
