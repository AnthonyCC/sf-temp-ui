package com.freshdirect.payment.ewallet.gateway.ejb;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import com.freshdirect.framework.core.SequenceGenerator;

/**
 * @author Aniwesh Vatsal
 *
 */
public class EwalletActivityLogDAO {

	public static void log(EwalletActivityLogModel log, Connection conn) {
		PreparedStatement ps = null;
		try {
			// Needed for Request and Response Object received from Masterpass
			 Clob myClobRequest = conn.createClob();
			 Clob myClobResponse = conn.createClob();
			 
			ps = conn
					.prepareStatement("INSERT INTO MIS.EWALLETAUDITLOG(ID,EWALLET_ID,CUSTOMER_ID,REQUEST,RESPONSE,TRANSACTION_TYPE,CREATION_TIMESTAMP,STATUS,ORDER_ID,TRANSACTION_ID) values(?,?,?,?,?,?,?,?,?,?)");
			int i = 1;
			ps.setInt(i++, Integer.parseInt(SequenceGenerator
					.getNextIdFromSequence(conn, "MIS.EWALLETAUDITLOG_SEQUENCE")));
			if (null != log.geteWalletID()) {
				 ps.setString(i++, log.geteWalletID());
			}
			
			if (null != log.getCustomerId()) {
				 ps.setString(i++, log.getCustomerId());
			} else {
				ps.setNull(i++, Types.VARCHAR);
			}
			
			if (null != log.getRequest()) {
				myClobRequest.setString(1, log.getRequest());
				 ps.setClob(i++, myClobRequest);
			} else {
				ps.setNull(i++, Types.VARCHAR);
			}
			
			if (null != log.getResponse()) {
				myClobResponse.setString(1, log.getResponse());
				 ps.setClob(i++, myClobResponse);
			} else {
				ps.setNull(i++, Types.VARCHAR);
			}
			if (null != log.getTransactionType()) {
				 ps.setString(i++, log.getTransactionType());
			}
			
			if (null != log.getCreationTimeStamp()) {
				 ps.setTimestamp(i++, log.getCreationTimeStamp());
			}
			
			if (null != log.getStatus()) {
				 ps.setString(i++, log.getStatus());
			} else {
				ps.setNull(i++, Types.VARCHAR);
			}
			
			if (null != log.getOrderId()) {
				 ps.setString(i++, log.getOrderId());
			} else {
				ps.setNull(i++, Types.VARCHAR);
			}
			
			ps.setString(i++, log.getTransactionId());
			
			ps.execute();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
