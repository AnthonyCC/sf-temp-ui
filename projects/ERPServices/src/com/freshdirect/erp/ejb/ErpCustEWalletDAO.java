package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpCustEWalletModel;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author Aniwesh Vatsal
 *
 */
public class ErpCustEWalletDAO {
	
	/** logger for messages
     */
    private static Category LOGGER = LoggerFactory.getInstance( ErpCustEWalletDAO.class );
	
	private static final String EWALLET_GET_LONG_ACCESS_TOKEN="SELECT ce.ID,ce.ewallet_id,ce.long_access_token,ce.customer_id " +
			" FROM CUST.CUST_EWALLET ce, CUST.ewallet e where ce.ewallet_id=e.id and customer_id =? and e.ewallet_type = ? ";
	
	private static final String UPDATE_LONG_ACCESS_TOKEN ="UPDATE CUST.CUST_EWALLET SET long_access_token = ?  " +
			" where customer_id=? and ewallet_id = (SELECT ID from CUST.ewallet where ewallet_type =?)";
    
	private static final String INSERT_INTO_CUST_EWALLET ="INSERT INTO CUST.CUST_EWALLET (ID,EWALLET_ID,LONG_ACCESS_TOKEN,CUSTOMER_ID) " +
			"  values (?,?,?,?)";
	
	private static final String DELETE_LONG_ACCESS_TOKEN ="DELETE FROM CUST.CUST_EWALLET where customer_id =? and EWALLET_ID = ?";
	/**
	 * @param conn
	 * @param custID
	 * @param eWalletType
	 * @return
	 * @throws SQLException
	 */
	public static ErpCustEWalletModel getLongAccessTokenByCustID(Connection conn,String custID, String eWalletType)
			throws SQLException {
		
		ErpCustEWalletModel erpCustEWalletModel = new ErpCustEWalletModel();
		   PreparedStatement ps = null;
		   ResultSet rs = null;
			try {	    	   	    	   	    	   
				ps = conn.prepareStatement(EWALLET_GET_LONG_ACCESS_TOKEN);
				ps.setString(1,custID);
				ps.setString(2,eWalletType);
				
				rs = ps.executeQuery();	 
				while (rs.next()) {
					erpCustEWalletModel.setId(rs.getString("ID"));
					erpCustEWalletModel.seteWalletId(rs.getString("ewallet_id"));
					erpCustEWalletModel.setLongAccessToken(rs.getString("long_access_token"));
					erpCustEWalletModel.setCustomerId("customer_id");
				}
			}catch(SQLException e){
				LOGGER.error(e.getMessage());
				throw e;
			}finally{
		    	   if(rs != null) rs.close();
		    	   if(ps != null) ps.close();
		    }
	 	    	
			return erpCustEWalletModel;
	    }
	
	/**
	 * This Method will delete the LongAccessToken based on Customer Id and Ewallet ID
	 * @param conn
	 * @param custID
	 * @param eWalletID
	 * @return
	 * @throws SQLException
	 */
	public static int deleteLongAccessTokenByCustIDAndEWalletID(Connection conn,String custID, String eWalletID)
			throws SQLException {
		
		   PreparedStatement ps = null;
		   int rows = 0;
			try {	    	   	    	   	    	   
				ps = conn.prepareStatement(DELETE_LONG_ACCESS_TOKEN);
				ps.setString(1,custID);
				ps.setString(2,eWalletID);
				rows = ps.executeUpdate();	 
			}catch(SQLException e){
				LOGGER.error(e.getMessage());
				throw e;
			}finally{
		    	   if(ps != null) ps.close();
		    }
	 	    	
			return rows;
	    }
	
	public static int updateLongAccessToken(Connection conn,String custId, String longAccessToken,
			String eWalletType) throws RemoteException, SQLException {
		
		PreparedStatement ps = null;
		int rows=0;
			try {	    	   	    	   	    	   
				ps = conn.prepareStatement(UPDATE_LONG_ACCESS_TOKEN);
				ps.setString(1,longAccessToken);
				ps.setString(2,custId);
				ps.setString(3,eWalletType);
				
				rows= ps.executeUpdate();	 
				
			}catch(SQLException e){
				LOGGER.error(e.getMessage());
				throw e;
			}finally{
		    	   if(ps != null) ps.close();
		    }
			return rows;
	}

	public static int insertCustomerLongAccessToken(Connection conn,ErpCustEWalletModel custEWallet) throws RemoteException, SQLException {
		String id = SequenceGenerator.getNextId(conn, "CUST");
		PreparedStatement ps = null;
		int rows=0;
			try {	  
					ps = conn.prepareStatement(INSERT_INTO_CUST_EWALLET);
					ps.setString(1,id);
					ps.setString(2,custEWallet.geteWalletId());
					ps.setString(3,custEWallet.getLongAccessToken());
					ps.setString(4,custEWallet.getCustomerId());
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

	
    
															
    
