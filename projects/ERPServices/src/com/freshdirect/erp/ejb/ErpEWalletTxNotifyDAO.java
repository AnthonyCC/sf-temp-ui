package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpEWalletTxNotifyModel;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author Aniwesh Vatsal
 *
 */
public class ErpEWalletTxNotifyDAO {
	
	/** logger for messages
     */
    private static Category LOGGER = LoggerFactory.getInstance( ErpEWalletTxNotifyDAO.class );
	
	private static final String INSERT_INTO_EWALLET_TXN_NOTIFY ="INSERT INTO CUST.EWALLET_TXNOTIFY " +
			"(ID,TRANSACTION_ID,STATUS,EWALLET_ID,VENDOR_EWALLET_ID,CUSTOMER_ID,ORDER_ID,NOTIFY_STATUS) " +
			"  values (?,?,?,?,?,?,?,?)";
	
	private static final String UPDATE_INTO_EWALLET_TXN_NOTIFY ="UPDATE CUST.ewallet_txnotify SET transaction_id = ?," +
			" vendor_ewallet_id = ? , Notify_status=?, customer_id=?  where order_id=? and ewallet_id=?";
	
	private static final String UPDATE_STATUS_INTO_EWALLET_TXN_NOTIFY ="UPDATE CUST.ewallet_txnotify SET STATUS = ? where order_id=? ";
	
	
	public static void updateEWalletTxnNotifyStatus(Connection conn,String orderId, String status) throws RemoteException, SQLException {
		PreparedStatement ps = null;
		try {	  
				ps = conn.prepareStatement(UPDATE_STATUS_INTO_EWALLET_TXN_NOTIFY);
				
				ps.setString(1,status);
				ps.setString(2,orderId);
				ps.executeUpdate();	 
				
		}catch(SQLException e){
			LOGGER.error(e.getMessage());
			throw e;
		}finally{
	    	   if(ps != null) ps.close();
	    }
	}
	/**
	 * @param conn
	 * @param eWallettxtNotify
	 * @return
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public static int updateEWalletTxnNotify(Connection conn,ErpEWalletTxNotifyModel eWallettxtNotify) throws RemoteException, SQLException {
		PreparedStatement ps = null;
		int rows=0;
			try {	  
					ps = conn.prepareStatement(UPDATE_INTO_EWALLET_TXN_NOTIFY);
					
					ps.setString(1,eWallettxtNotify.getTransactionId());
					ps.setString(2,eWallettxtNotify.geteWalletVendorId());
					ps.setString(3,eWallettxtNotify.getNotifyStatus());
					ps.setString(4,eWallettxtNotify.getCustomerId());
					ps.setString(5,""+eWallettxtNotify.getOrderId());
					ps.setString(6,eWallettxtNotify.geteWalletId());
					rows= ps.executeUpdate();	 
					
			}catch(SQLException e){
				LOGGER.error(e.getMessage());
				throw e;
			}finally{
		    	   if(ps != null) ps.close();
		    }
			return rows;
	}
	
	/**
	 * @param conn
	 * @param eWallettxtNotify
	 * @return
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public static int insertEWalletTxnNotify(Connection conn,ErpEWalletTxNotifyModel eWallettxtNotify) throws RemoteException, SQLException {
		PreparedStatement ps = null;
		String id = SequenceGenerator.getNextId(conn, "CUST");
		int rows=0;
			try {	  
					ps = conn.prepareStatement(INSERT_INTO_EWALLET_TXN_NOTIFY);
					System.out.println("Update_INTO_EWALLET_TXN_NOTIFY = "+INSERT_INTO_EWALLET_TXN_NOTIFY);
					ps.setString(1,id);
					ps.setString(2,eWallettxtNotify.getTransactionId());
					ps.setString(3,eWallettxtNotify.getStatus());
					ps.setString(4,eWallettxtNotify.geteWalletId());
					ps.setString(5,eWallettxtNotify.geteWalletVendorId());
					ps.setString(6,eWallettxtNotify.getCustomerId());
					ps.setString(7,eWallettxtNotify.getOrderId());
					ps.setString(8,eWallettxtNotify.getNotifyStatus());
					
					rows= ps.executeUpdate();	 
					
			}catch(SQLException e){
				LOGGER.error(e.getMessage());
				throw e;
			}finally{
		    	   if(ps != null) ps.close();
		    }
			return rows;
	}
}

	
    
															
    
