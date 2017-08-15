package com.freshdirect.fdstore.ecoupon;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.fdstore.ecoupon.model.ErpCouponTransactionDetailModel;
import com.freshdirect.fdstore.ecoupon.model.ErpCouponTransactionModel;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.DaoUtil;

public class FDCouponTransactionDAO implements Serializable {
    
	

	private static final long serialVersionUID = -6810003366617873611L;
	
	private static final String INSERT_COUPON_TRANS = "INSERT INTO CUST.COUPON_TRANS (ID, SALESACTION_ID,TRANS_STATUS,TRANS_TYPE,ERROR_MSG, ERROR_DETAILS,CREATE_TIME,TRANS_TIME) VALUES (?,?,?,?,?,?,?,?)";
	private static final String UPDATE_COUPON_TRANS = "UPDATE CUST.COUPON_TRANS SET TRANS_STATUS=?,ERROR_MSG=?, ERROR_DETAILS=?, TRANS_TIME=? WHERE ID=?";
	private static final String INSERT_COUPON_TRANS_DETAILS = "INSERT INTO CUST.COUPON_TRANS_DETAILS  (ID, COUPON_TRANS_ID,COUPON_ID, COUPONLINE_ID, DISCOUNT_AMT, TRANS_TIME) VALUES (?,?,?,?,?,?)";


	public static void storeCouponTransaction(Connection conn, ErpCouponTransactionModel transModel) throws SQLException{
		PreparedStatement ps =null;					
		ps =conn.prepareStatement(INSERT_COUPON_TRANS);
		try {
			
			int i=1;
			ps.setString(i++, SequenceGenerator.getNextId(conn,"CUST"));
			ps.setString(i++, transModel.getSaleActionId());
			ps.setString(i++, transModel.getTranStatus().getName());
			ps.setString(i++, transModel.getTranType().getName());
			if(null !=transModel.getErrorMessage()){
				ps.setString(i++, transModel.getErrorMessage());
			}else{
				ps.setNull(i++, Types.VARCHAR);
			}
			if(null !=transModel.getErrorDetails()){
				ps.setString(i++, transModel.getErrorDetails());
			}else{
				ps.setNull(i++, Types.VARCHAR);
			}
			ps.setDate(i++, new java.sql.Date(transModel.getCreateTime().getTime()));			
			ps.setDate(i++, new java.sql.Date(transModel.getTranTime().getTime()));			
			ps.executeQuery();
			ps.close();
			
		} finally {
			if (ps != null)
				ps.close();
		}		
	}
    
    public static void updateCouponTransaction(Connection conn, ErpCouponTransactionModel transModel) throws SQLException{
		PreparedStatement ps =null;					
		ps =conn.prepareStatement(UPDATE_COUPON_TRANS);
		try {
			
			int i=1;
			ps.setString(i++, transModel.getTranStatus().getName());
			if(null !=transModel.getErrorMessage()){
				ps.setString(i++, transModel.getErrorMessage());
			}else{
				ps.setNull(i++, Types.VARCHAR);
			}
			if(null !=transModel.getErrorDetails()){
				ps.setString(i++, transModel.getErrorDetails());
			}else{
				ps.setNull(i++, Types.VARCHAR);
			}
			ps.setTimestamp(i++, new java.sql.Timestamp(transModel.getTranTime().getTime()));
			ps.setString(i++, transModel.getId());			
			ps.executeQuery();
			ps.close();
			
			if(null !=transModel.getTranDetails() && !transModel.getTranDetails().isEmpty()){
				ps = conn.prepareStatement(INSERT_COUPON_TRANS_DETAILS);
				for (Iterator<ErpCouponTransactionDetailModel> iterator = transModel.getTranDetails().iterator(); iterator.hasNext();) {
					ErpCouponTransactionDetailModel transDetailModel =iterator.next();
					i=1;
					ps.setString(i++, SequenceGenerator.getNextId(conn, "CUST"));
					ps.setString(i++, transModel.getId());
					ps.setString(i++, transDetailModel.getCouponId());
					ps.setString(i++, transDetailModel.getCouponLineId());
					ps.setString(i++, transDetailModel.getDiscountAmt());
					ps.setTimestamp(i++, new java.sql.Timestamp(transDetailModel.getTransTime().getTime()));
					ps.addBatch();					
				}
				ps.executeBatch();
				ps.close();
			}
			
		} finally {
			if (ps != null)
				ps.close();
		}		
	}
    
    private static final String SELECT_SUBMIT_PENDING_COUPON_TRANS_="SELECT FCT.ID AS TRANS_ID,FCT.SALESACTION_ID AS SALESACTION_ID,S.ID AS SALE_ID, FCT.TRANS_TYPE AS TRANS_TYPE, FCT.TRANS_STATUS AS TRANS_STATUS " +
    		"FROM CUST.SALE S, CUST.SALESACTION SA, CUST.COUPON_TRANS FCT " 
    		+ "WHERE S.TYPE='REG' AND S.STATUS NOT IN ('CAN','MOC') AND SA.CUSTOMER_ID = S.CUSTOMER_ID AND " 
    		+ "SA.SALE_ID=S.ID AND SA.ID = FCT.SALESACTION_ID AND FCT.TRANS_STATUS IN('P','F') AND FCT.TRANS_TYPE IN ('CREATE_ORDER', 'MODIFY_ORDER') " 
    		+ "AND SA.ACTION_TYPE IN ('CRO','MOD') AND SA.ACTION_DATE=S.CROMOD_DATE AND FCT.CREATE_TIME > SYSDATE-30";
    public static List<ErpCouponTransactionModel> getSubmitPendingCouponTransactions(Connection conn) throws SQLException{
    	List<ErpCouponTransactionModel> list = new ArrayList<ErpCouponTransactionModel>();
		PreparedStatement ps =conn.prepareStatement(SELECT_SUBMIT_PENDING_COUPON_TRANS_);
		try {
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				ErpCouponTransactionModel transModel = new ErpCouponTransactionModel();
				transModel.setId(rs.getString("TRANS_ID"));
				transModel.setSaleActionId(rs.getString("SALESACTION_ID"));
				transModel.setTranType(EnumCouponTransactionType.getEnum(rs.getString("TRANS_TYPE")));
				transModel.setTranStatus(EnumCouponTransactionStatus.getEnum(rs.getString("TRANS_STATUS")));
				transModel.setSaleId(rs.getString("SALE_ID"));
				list.add(transModel);			
			}
			ps.close();
			
		} finally {
			if (ps != null)
				ps.close();
		}		
		return list;
	}
    
    private static final String SELECT_SUBMIT_PENDING_COUPON_TRANS_SALE="SELECT FCT.ID AS TRANS_ID,FCT.SALESACTION_ID AS SALESACTION_ID,S.ID AS SALE_ID, FCT.TRANS_TYPE AS TRANS_TYPE, FCT.TRANS_STATUS AS TRANS_STATUS " +
    		"FROM CUST.SALE S, CUST.SALESACTION SA, CUST.COUPON_TRANS FCT " 
    		+ "WHERE S.TYPE='REG' AND S.STATUS NOT IN ('CAN','MOC') AND SA.CUSTOMER_ID = S.CUSTOMER_ID AND " 
    		+ "SA.SALE_ID=S.ID AND SA.ID = FCT.SALESACTION_ID AND FCT.TRANS_STATUS IN('P','F') AND FCT.TRANS_TYPE IN ('CREATE_ORDER', 'MODIFY_ORDER') " 
    		+ "AND SA.ACTION_TYPE IN ('CRO','MOD') AND SA.ACTION_DATE=S.CROMOD_DATE AND FCT.CREATE_TIME > SYSDATE-30 AND S.ID=? ORDER BY FCT.CREATE_TIME DESC";
    public static ErpCouponTransactionModel getSubmitPendingCouponTransaction(Connection conn,String saleId) throws SQLException{
    	ErpCouponTransactionModel transModel =null;
		PreparedStatement ps =conn.prepareStatement(SELECT_SUBMIT_PENDING_COUPON_TRANS_SALE);
		if(null !=saleId){
			ps.setString(1, saleId);
			try {
				ResultSet rs =ps.executeQuery();
				if(rs.next()){
					transModel = new ErpCouponTransactionModel();
					transModel.setId(rs.getString("TRANS_ID"));
					transModel.setSaleActionId(rs.getString("SALESACTION_ID"));
					transModel.setTranType(EnumCouponTransactionType.getEnum(rs.getString("TRANS_TYPE")));
					transModel.setTranStatus(EnumCouponTransactionStatus.getEnum(rs.getString("TRANS_STATUS")));
					transModel.setSaleId(rs.getString("SALE_ID"));								
				}
				ps.close();
				
			} finally {
				if (ps != null)
					ps.close();
			}		
		}
		return transModel;
	}
    
    private static final String SELECT_SUBMIT_PENDING_COUPON_SALES="SELECT DISTINCT S.ID AS SALE_ID " +
    		"FROM CUST.SALE S, CUST.SALESACTION SA, CUST.COUPON_TRANS FCT " 
    		+ "WHERE S.TYPE='REG' AND S.STATUS NOT IN ('CAN','MOC') AND SA.CUSTOMER_ID = S.CUSTOMER_ID AND " 
    		+ "SA.SALE_ID=S.ID AND SA.ID = FCT.SALESACTION_ID AND FCT.TRANS_STATUS IN('P','F') AND FCT.TRANS_TYPE IN ('CREATE_ORDER', 'MODIFY_ORDER') " 
    		+ "AND SA.ACTION_TYPE IN ('CRO','MOD') AND SA.ACTION_DATE=S.CROMOD_DATE AND FCT.CREATE_TIME > SYSDATE-30";
    public static List<String> getSubmitPendingCouponSales(Connection conn) throws SQLException{
    	List<String> list = new ArrayList<String>();
		PreparedStatement ps =conn.prepareStatement(SELECT_SUBMIT_PENDING_COUPON_SALES);
		try {
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				list.add(rs.getString("SALE_ID"));			
			}
			ps.close();
			
		} finally {
			if (ps != null)
				ps.close();
		}		
		return list;
	}
    
    private static final String SELECT_CANCEL_PENDING_COUPON_TRANS_="SELECT FCT.ID AS TRANS_ID,FCT.SALESACTION_ID AS SALESACTION_ID,S.ID AS SALE_ID, FCT.TRANS_TYPE AS TRANS_TYPE, FCT.TRANS_STATUS AS TRANS_STATUS " +
    		"FROM CUST.SALE S, CUST.SALESACTION SA, CUST.COUPON_TRANS FCT " 
    		+ "WHERE S.TYPE='REG' AND SA.CUSTOMER_ID = S.CUSTOMER_ID AND " 
    		+ "SA.SALE_ID=S.ID AND SA.ID = FCT.SALESACTION_ID AND FCT.TRANS_STATUS IN('P','F') AND FCT.TRANS_TYPE IN ('CANCEL_ORDER') " 
    		+ " AND FCT.CREATE_TIME > SYSDATE-30";
    public static List<ErpCouponTransactionModel> getCancelPendingCouponTransactions(Connection conn) throws SQLException{
    	List<ErpCouponTransactionModel> list = new ArrayList<ErpCouponTransactionModel>();
    	ResultSet rs = null;
    	PreparedStatement ps =conn.prepareStatement(SELECT_CANCEL_PENDING_COUPON_TRANS_);
		try {
			rs =ps.executeQuery();
			while(rs.next()){
				ErpCouponTransactionModel transModel = new ErpCouponTransactionModel();
				transModel.setId(rs.getString("TRANS_ID"));
				transModel.setSaleActionId(rs.getString("SALESACTION_ID"));
				transModel.setTranType(EnumCouponTransactionType.getEnum(rs.getString("TRANS_TYPE")));
				transModel.setTranStatus(EnumCouponTransactionStatus.getEnum(rs.getString("TRANS_STATUS")));
				transModel.setSaleId(rs.getString("SALE_ID"));
				list.add(transModel);			
			}
		} finally {
			DaoUtil.close(rs);
			DaoUtil.close(ps);
		}		
		return list;
	}
    
    private static final String SELECT_CONFIRM_PENDING_COUPON_TRANS_="SELECT FCT.ID AS TRANS_ID,FCT.SALESACTION_ID AS SALESACTION_ID,S.ID AS SALE_ID, FCT.TRANS_TYPE AS TRANS_TYPE, FCT.TRANS_STATUS AS TRANS_STATUS" 
    		+" FROM CUST.SALE S, CUST.SALESACTION SA, CUST.COUPON_TRANS FCT " 
    		+" WHERE S.TYPE='REG' AND " 
    		+" SA.SALE_ID=S.ID AND ((SYSDATE- FCT.CREATE_TIME)*24 >=5) AND SA.ID = FCT.SALESACTION_ID AND FCT.TRANS_STATUS IN('P','F') AND FCT.TRANS_TYPE IN ('CONFIRM_ORDER') " 
    		+" AND FCT.CREATE_TIME > SYSDATE-30";
    public static List<ErpCouponTransactionModel> getConfirmPendingCouponTransactions(Connection conn) throws SQLException{
    	List<ErpCouponTransactionModel> list = new ArrayList<ErpCouponTransactionModel>();
    	ResultSet rs = null;
    	PreparedStatement ps =conn.prepareStatement(SELECT_CONFIRM_PENDING_COUPON_TRANS_);
		try {
			rs =ps.executeQuery();
			while(rs.next()){
				ErpCouponTransactionModel transModel = new ErpCouponTransactionModel();
				transModel.setId(rs.getString("TRANS_ID"));
				transModel.setSaleActionId(rs.getString("SALESACTION_ID"));
				transModel.setTranType(EnumCouponTransactionType.getEnum(rs.getString("TRANS_TYPE")));
				transModel.setTranStatus(EnumCouponTransactionStatus.getEnum(rs.getString("TRANS_STATUS")));
				transModel.setSaleId(rs.getString("SALE_ID"));
				list.add(transModel);			
			}
		} finally {
			DaoUtil.close(rs);
			DaoUtil.close(ps);
		}		
		return list;
	}
    
    private static final String SELECT_CONFIRM_PENDING_COUPON_SALES="SELECT DISTINCT S.ID AS SALE_ID" 
    		+" FROM CUST.SALE S, CUST.SALESACTION SA, CUST.COUPON_TRANS FCT " 
    		+" WHERE S.TYPE='REG' AND " 
    		+" SA.SALE_ID=S.ID AND ((SYSDATE- FCT.CREATE_TIME)*24 >=5) AND SA.ID = FCT.SALESACTION_ID AND FCT.TRANS_STATUS IN('P','F') AND FCT.TRANS_TYPE IN ('CONFIRM_ORDER') " 
    		+" AND FCT.CREATE_TIME > SYSDATE-30";
    public static List<String> getConfirmPendingCouponSales(Connection conn) throws SQLException{
    	List<String> list = new ArrayList<String>();
		PreparedStatement ps =conn.prepareStatement(SELECT_CONFIRM_PENDING_COUPON_SALES);
		try {
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				list.add(rs.getString("SALE_ID"));			
			}
			ps.close();
			
		} finally {
			if (ps != null)
				ps.close();
		}		
		return list;
	} 
    
    public static void cancelSubmitPendingCouponTransactions(Connection conn,String saleId) throws SQLException{
    	PreparedStatement ps =conn.prepareStatement("UPDATE CUST.COUPON_TRANS FCT SET FCT.TRANS_STATUS='C' WHERE FCT.TRANS_STATUS IN('P','F') AND FCT.TRANS_TYPE IN ('CREATE_ORDER','MODIFY_ORDER') " +
    			"AND EXISTS(SELECT ID FROM CUST.SALESACTION SA WHERE SA.SALE_ID=? AND SA.ID=FCT.SALESACTION_ID)");
    	try {
			ps.setString(1, saleId);
			ps.executeUpdate();
		} finally {
			if (ps != null)
				ps.close();
		}	
    }
    
    public static void cancelConfirmPendingCouponTransactions(Connection conn,String saleId) throws SQLException{
    	PreparedStatement ps =conn.prepareStatement("UPDATE CUST.COUPON_TRANS FCT SET FCT.TRANS_STATUS='C' WHERE FCT.TRANS_STATUS IN('P','F') AND FCT.TRANS_TYPE IN ('CONFIRM_ORDER') " +
    			"AND EXISTS(SELECT ID FROM CUST.SALESACTION SA WHERE SA.SALE_ID=? AND SA.ID=FCT.SALESACTION_ID)");
    	try {
			ps.setString(1, saleId);
			ps.executeUpdate();
		} finally {
			if (ps != null)
				ps.close();
		}	
    }
    
    private static final String SELECT_CONFIRM_PENDING_COUPON_TRANS_SALE="SELECT FCT.ID AS TRANS_ID,FCT.SALESACTION_ID AS SALESACTION_ID,S.ID AS SALE_ID, FCT.TRANS_TYPE AS TRANS_TYPE, FCT.TRANS_STATUS AS TRANS_STATUS" 
    		+" FROM CUST.SALE S, CUST.SALESACTION SA, CUST.COUPON_TRANS FCT " 
    		+" WHERE S.TYPE='REG' AND SA.CUSTOMER_ID = S.CUSTOMER_ID AND " 
    		+" SA.SALE_ID=S.ID AND SA.ID = FCT.SALESACTION_ID AND FCT.TRANS_STATUS IN('P','F') AND FCT.TRANS_TYPE IN ('CONFIRM_ORDER') " 
    		+" AND FCT.CREATE_TIME > SYSDATE-30 AND S.ID=? ORDER BY FCT.CREATE_TIME DESC";
    public static ErpCouponTransactionModel getConfirmPendingCouponTransaction(Connection conn,String saleId) throws SQLException{
    	ErpCouponTransactionModel transModel =null;
    	ResultSet rs = null;
    	PreparedStatement ps =conn.prepareStatement(SELECT_CONFIRM_PENDING_COUPON_TRANS_SALE);
		if(null !=saleId){
			ps.setString(1, saleId);
			try {
				rs =ps.executeQuery();
				if(rs.next()){
					transModel = new ErpCouponTransactionModel();
					transModel.setId(rs.getString("TRANS_ID"));
					transModel.setSaleActionId(rs.getString("SALESACTION_ID"));
					transModel.setTranType(EnumCouponTransactionType.getEnum(rs.getString("TRANS_TYPE")));
					transModel.setTranStatus(EnumCouponTransactionStatus.getEnum(rs.getString("TRANS_STATUS")));
					transModel.setSaleId(rs.getString("SALE_ID"));
								
				}
			} finally {
				DaoUtil.close(rs);
				DaoUtil.close(ps);
			}		
		}
		return transModel;
	}
}
