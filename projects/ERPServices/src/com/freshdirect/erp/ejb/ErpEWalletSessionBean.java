package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpCustEWalletModel;
import com.freshdirect.customer.ErpEWalletModel;
import com.freshdirect.customer.ErpEWalletTxNotifyModel;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author Aniwesh Vatsal
 *
 */
public class ErpEWalletSessionBean extends SessionBeanSupport {

	private static final long serialVersionUID = -2328457228177148829L;
	/**
	 * logger for messages
	 */
	private static Category LOGGER = LoggerFactory
			.getInstance(ErpEWalletSessionBean.class);

	public List<ErpEWalletModel> getAllEWallets() throws RemoteException{
		return null;
	}

	/**
	 * @param eWalletId
	 * @return
	 * @throws RemoteException
	 */
	public ErpEWalletModel findEWalletById(String eWalletId)
			throws RemoteException{
		Connection conn = null;

		ErpEWalletModel erpEWalletModel = null;
		try{
			conn = getConnection();			
			erpEWalletModel=ErpEWalletDAO.getEWalletById(conn,eWalletId);			
		}catch(SQLException sqle){
			LOGGER.error("Unable to load EWallet " , sqle);
			throw new EJBException(sqle);
		}finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.error("Unable to close db resources", sqle);
				throw new EJBException(sqle);
			}
		}
		return erpEWalletModel;
	}
	
	/**
	 * @param eWalletId
	 * @return
	 * @throws RemoteException
	 */
	public ErpEWalletModel findEWalletByType(String eWalletType)
			throws RemoteException{
		Connection conn = null;

		ErpEWalletModel erpEWalletModel = null;
		try{
			conn = getConnection();			
			erpEWalletModel=ErpEWalletDAO.getEWalletByType(conn, eWalletType);			
		}catch(SQLException sqle){
			LOGGER.error("Unable to load EWallet " , sqle);
			throw new EJBException(sqle);
		}finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.error("Unable to close db resources", sqle);
				throw new EJBException(sqle);
			}
		}
		return erpEWalletModel;
	}
	
	
	/**
	 * @param custID
	 * @param eWalletType
	 * @return
	 */
	public ErpCustEWalletModel getLongAccessTokenByCustID(String custID, String eWalletType){
		Connection conn = null;
		ErpCustEWalletModel erpCustEWalletModel = null;
		try{
			conn = getConnection();
			erpCustEWalletModel = ErpCustEWalletDAO.getLongAccessTokenByCustID(conn, custID,eWalletType);
		}catch(SQLException sqle)
		{
			LOGGER.error("Unable to load EWallet " , sqle);
			throw new EJBException(sqle);
		}finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.error("Unable to close db resources", sqle);
				throw new EJBException(sqle);
			}
		}
		return erpCustEWalletModel;
	}
	
	/**
	 * @param custEWallet
	 * @return
	 * @throws RemoteException
	 */
	public int insertCustomerLongAccessToken(ErpCustEWalletModel custEWallet) throws RemoteException
	{
		
		Connection conn = null;
		int row=0;
		try{
			conn = getConnection();
			row=ErpCustEWalletDAO.insertCustomerLongAccessToken(conn, custEWallet);
		}catch(SQLException sqle)
		{
			LOGGER.error("Unable to load EWallet " , sqle);
			throw new EJBException(sqle);
		}finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.error("Unable to close db resources", sqle);
				throw new EJBException(sqle);
			}
		}
		return row;
		
	}
	
	public int updateLongAccessToken(String custId, String longAccessToken,String eWalletType) throws RemoteException{
		Connection conn = null;
		int row=0;
		try{
			conn = getConnection();
			row=ErpCustEWalletDAO.updateLongAccessToken(conn, custId, longAccessToken, eWalletType);
		}catch(SQLException sqle)
		{
			LOGGER.error("Unable to load EWallet " , sqle);
			throw new EJBException(sqle);
		}finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.error("Unable to close db resources", sqle);
				throw new EJBException(sqle);
			}
		}
		return row;
	}
	
	/**
	 * @param custId
	 * @param eWalletID
	 * @return
	 * @throws RemoteException
	 */
	public int deleteLongAccessToken(String custId, String eWalletID) throws RemoteException{
		Connection conn = null;
		int row=0;
		try{
			conn = getConnection();
			row=ErpCustEWalletDAO.deleteLongAccessTokenByCustIDAndEWalletID(conn, custId, eWalletID);
		}catch(SQLException sqle)
		{
			LOGGER.error("Unable to load EWallet " , sqle);
			throw new EJBException(sqle);
		}finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.error("Unable to close db resources", sqle);
				throw new EJBException(sqle);
			}
		}
		return row;
	}

}
