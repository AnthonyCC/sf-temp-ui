package com.freshdirect.fdstore.ecoupon;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import com.freshdirect.fdstore.ecoupon.model.FDCouponActivityLogModel;
import com.freshdirect.framework.core.SequenceGenerator;

public class FDCouponActivityLogDAO implements Serializable {

	
	public static void logCouponActivity(FDCouponActivityLogModel log, Connection conn) throws SQLException {
			PreparedStatement ps = null;		
			try {
				ps =	conn.prepareStatement(
						"INSERT INTO MIS.COUPON_ACTIVITY_LOG(ID, FDUSER_ID, CUSTOMER_ID, TRANS_TYPE, SALE_ID, COUPON_ID, " +
										              "DETAILS,SOURCE,INITIATOR , START_TIME, END_TIME) values(?,?,?,?,?,?,?,?,?,?,?)");
				int i=1;
				ps.setInt(i++, Integer.parseInt(SequenceGenerator.getNextIdFromSequence(conn, "MIS.COUPON_LOG_SEQUENCE")));
				if(null !=log.getFdUserId()){
					ps.setString(i++, log.getFdUserId());
				}else{
					ps.setNull(i++, Types.NUMERIC);
				}
				
				if(null !=log.getCustomerId()){
					ps.setString(i++, log.getCustomerId());
				}else{
					ps.setNull(i++, Types.VARCHAR);
				}
				
				ps.setString(i++, log.getTransType().getName());	
				
				if(null !=log.getSaleId()){
					ps.setString(i++, log.getSaleId());
				}else{
					ps.setNull(i++, Types.VARCHAR);
				}
				if(null !=log.getCouponId()){
					ps.setString(i++, log.getCouponId());
				}else{
					ps.setNull(i++, Types.VARCHAR);
				}
				if(null !=log.getDetails()){
					ps.setString(i++, log.getDetails());
				}else{
					ps.setNull(i++, Types.VARCHAR);
				}
					
				if(null !=log.getSource()){
					ps.setString(i++, log.getSource().getCode());
				}else{
					ps.setNull(i++, Types.VARCHAR);
				}
				if(null !=log.getInitiator()){
					ps.setString(i++, log.getInitiator());
				}else{
					ps.setNull(i++, Types.VARCHAR);
				}
				
				ps.setTimestamp(i++, new java.sql.Timestamp(log.getStartTime().getTime()));
				ps.setTimestamp(i++, new java.sql.Timestamp(log.getEndTime().getTime()));
				
				ps.execute();
				
			} finally{
				if(null != ps){
					ps.close();
				}
			}
		
		
	}
}
