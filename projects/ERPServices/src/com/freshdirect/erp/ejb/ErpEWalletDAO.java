package com.freshdirect.erp.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpEWalletModel;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author Aniwesh Vatsal
 *
 */
public class ErpEWalletDAO {
	
	/** logger for messages
     */
    private static Category LOGGER = LoggerFactory.getInstance( ErpEWalletDAO.class );
	
	private static final String EWALLET_SELECT_BYID="SELECT ID,EWALLET_TYPE,EWALLET_STATUS,ewallet_verify,EWALLETM_STATUS FROM CUST.EWALLET where ID=? ";
	private static final String EWALLET_SELECT_BYTYPE="SELECT ID,EWALLET_TYPE,EWALLET_STATUS,ewallet_verify,EWALLETM_STATUS FROM CUST.EWALLET where EWALLET_TYPE=? ";
    
	/**
	 * @param conn
	 * @param eWalletID
	 * @return
	 * @throws SQLException
	 */
	public static ErpEWalletModel getEWalletById(Connection conn,String eWalletID) throws SQLException{

    	ErpEWalletModel erpEWalletModel = new ErpEWalletModel();
	   PreparedStatement ps = null;
	   ResultSet rs = null;
		try {	    	   	    	   	    	   
			ps = conn.prepareStatement(EWALLET_SELECT_BYID);
			ps.setString(1,eWalletID);
			//ps.setString(2,matId);
			rs = ps.executeQuery();	 
			while (rs.next()) {
				erpEWalletModel.setId(rs.getString("ID"));
				erpEWalletModel.seteWalletType(rs.getString("EWALLET_TYPE"));
				erpEWalletModel.seteWalletStatus(rs.getString("EWALLET_STATUS"));
				erpEWalletModel.seteWalletVerify(rs.getString("ewallet_verify"));
				erpEWalletModel.setEwalletmStatus(rs.getString("EWALLETM_STATUS"));
			}
		}catch(SQLException e){
			LOGGER.error(e.getMessage());
			throw e;
		}finally{
	    	   if(rs != null) rs.close();
	    	   if(ps != null) ps.close();
	    }
 	    	
		return erpEWalletModel;
    }
	
	/**
	 * @param conn
	 * @param eWalletID
	 * @return
	 * @throws SQLException
	 */
	public static ErpEWalletModel getEWalletByType(Connection conn,String eWalletType) throws SQLException{

    	ErpEWalletModel erpEWalletModel = new ErpEWalletModel();
	   PreparedStatement ps = null;
	   ResultSet rs = null;
		try {	    	   	    	   	    	   
			ps = conn.prepareStatement(EWALLET_SELECT_BYTYPE);
			ps.setString(1,eWalletType);
			
			rs = ps.executeQuery();	 
			while (rs.next()) {
				erpEWalletModel.setId(rs.getString("ID"));
				erpEWalletModel.seteWalletType(rs.getString("EWALLET_TYPE"));
				erpEWalletModel.seteWalletStatus(rs.getString("EWALLET_STATUS"));
				erpEWalletModel.seteWalletVerify(rs.getString("ewallet_verify"));
				erpEWalletModel.setEwalletmStatus(rs.getString("EWALLETM_STATUS"));
				
			}
		}catch(SQLException e){
			LOGGER.error(e.getMessage());
			throw e;
		}finally{
	    	   if(rs != null) rs.close();
	    	   if(ps != null) ps.close();
	    }
 	    	
		return erpEWalletModel;
    }
}

	
    
															
    
