package com.freshdirect.dataloader.sap.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.erp.ErpProductPromotion;
import com.freshdirect.erp.ErpProductPromotionInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * 
 * @author ksriram
 *
 */
public class SAPProductPromotionLoaderSessionBean extends SessionBeanSupport {
	
	private static Category LOGGER = LoggerFactory.getInstance( SAPProductPromotionLoaderSessionBean.class );

	public void loadProductPromotions(List<ErpProductPromotion> ppList,List<ErpProductPromotionInfo> ppInfoList,int batchVersion) throws LoaderException{
		Connection conn = null;
		try{
			conn = getConnection();			
			SAPProductPromotionLoaderDAO.loadProductPromotions(conn,ppList,ppInfoList,batchVersion);
		}catch(SQLException sqle){
			LOGGER.error("Unable to save product promotions batch", sqle);
			throw new LoaderException(sqle);
		}finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.error("Unable to close db resources", sqle);
				throw new LoaderException(sqle);
			}
		}
	}
	public void createHistoryData(Timestamp timestamp,int batchNumber) throws LoaderException{
		Connection conn = null;
		try{
			conn = getConnection();			
			SAPProductPromotionLoaderDAO.createHistoryData(conn,timestamp,batchNumber);
		}catch(SQLException sqle){
			LOGGER.error("Unable to create product promotions history record:", sqle);
			throw new LoaderException(sqle);
		}finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.error("Unable to close db resources", sqle);
				throw new LoaderException(sqle);
			}
		}
	}
	
	public void updateHistoryData(int batchNumber,String status) throws LoaderException{
		Connection conn = null;
		try{
			conn = getConnection();			
			SAPProductPromotionLoaderDAO.updateHistoryData(conn,batchNumber,status);
		}catch(SQLException sqle){
			LOGGER.error("Unable to update product promotions history record:", sqle);
			throw new LoaderException(sqle);
		}finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.error("Unable to close db resources", sqle);
				throw new LoaderException(sqle);
			}
		}
	}
	public int getNextBatchNumber() throws LoaderException{
		Connection conn = null;
		int batchNumber=-1;
		try{
			conn = getConnection();			
			batchNumber = SAPProductPromotionLoaderDAO.getNextBatchNumber(conn);
		}catch(SQLException sqle){
			LOGGER.error("Unable to save product promotions batch", sqle);
			throw new LoaderException(sqle);
		}finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.error("Unable to close db resources", sqle);
				throw new LoaderException(sqle);
			}
		}
		return batchNumber;
	}
}
	

