package com.freshdirect.erp.ejb;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.erp.model.ATPFailureInfo;

public class ATPFailureDAO {

	/**
	 * @param atpFailureInfos List of ATPFailureInfo
	 */
	public void create(Connection conn, List atpFailureInfos) throws SQLException {
		PreparedStatement ps =
			conn.prepareStatement(
				"INSERT INTO ERPS.ATP_FAILURE(REQUESTED_DATE, MATERIAL_SAP_ID, QUANTITY_REQ, SALES_UNIT, QUANTITY_AVAIL, TIMESTAMP, CUSTOMER_ID) VALUES (?,?,?,?,?,SYSDATE, ?)");

		for (Iterator i = atpFailureInfos.iterator(); i.hasNext();) {
			ATPFailureInfo info = (ATPFailureInfo) i.next();

			ps.setDate(1, new Date(info.getRequestedDate().getTime()));
			ps.setString(2, info.getMaterialNumber());
			ps.setDouble(3, info.getRequestedQuantity());
			ps.setString(4, info.getSalesUnit());
			ps.setDouble(5, info.getAvailableQuantity());
			ps.setString(6, info.getErpCustomerId());

			ps.addBatch();
		}

		ps.executeBatch();
		ps.close();
	}

}
