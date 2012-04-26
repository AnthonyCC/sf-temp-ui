package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.erp.ErpProductPromotionPreviewInfo;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.ZonePriceListing;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.sap.command.SapProductPromotionPreviewCommand;
import com.freshdirect.sap.ejb.SapException;

public class ErpProductPromotionInfoSessionBean extends SessionBeanSupport {
	
	private static Category LOGGER = LoggerFactory.getInstance( ErpProductPromotionInfoSessionBean.class );

	public Map<String,List<FDProductPromotionInfo>> getAllProductsByType(String ppType) throws FDResourceException, RemoteException{
		Connection conn = null;
		Map<String, List<FDProductPromotionInfo>> productPromoInfoMap=null;
		try{
			conn = getConnection();			
			productPromoInfoMap=ErpProductPromotionInfoDAO.getAllProductsByType(conn,ppType,null);
		}catch(SQLException sqle){
			LOGGER.error("Unable to get all product promotions of type: "+ppType , sqle);
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
		
		return productPromoInfoMap;
	}
	
	public Map<String,List<FDProductPromotionInfo>> getAllProductsByType(String ppType, Date lastPublishDate) throws FDResourceException, RemoteException{
		Connection conn = null;
		Map<String,List<FDProductPromotionInfo>> productPromoInfoMap=null;
		try{
			conn = getConnection();			
			productPromoInfoMap=ErpProductPromotionInfoDAO.getAllProductsByType(conn,ppType,lastPublishDate);
		}catch(SQLException sqle){
			LOGGER.error("Unable to get all product promotions of type: "+ppType , sqle);
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
		
		return productPromoInfoMap;
	}
	public List<FDProductPromotionInfo> getProductsByZoneAndType(String ppType, String zoneId) throws FDResourceException, RemoteException{
		Connection conn = null;
		List<FDProductPromotionInfo> promotionProducts=null;
		try{
			conn = getConnection();			
			promotionProducts=ErpProductPromotionInfoDAO.getProductsByZoneAndType(conn,ppType,zoneId);
			if((null == promotionProducts || promotionProducts.isEmpty())&& !ZonePriceListing.MASTER_DEFAULT_ZONE.equals(zoneId)){
				zoneId= ZonePriceListing.MASTER_DEFAULT_ZONE;
				promotionProducts=ErpProductPromotionInfoDAO.getProductsByZoneAndType(conn,ppType,zoneId);
			}
		}catch(SQLException sqle){
			LOGGER.error("Unable to get product promotions for zone:"+ zoneId+" ,of type: "+ppType , sqle);
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
		return promotionProducts;
	}
	
	public ErpProductPromotionPreviewInfo getProductPromotionPreviewInfo(String ppPreviewId)throws FDResourceException, RemoteException{
		try {
			SapProductPromotionPreviewCommand command = new SapProductPromotionPreviewCommand(ppPreviewId);
			command.execute();
			ErpProductPromotionPreviewInfo erpProductPromotionPreviewInfo = command.getErpProductPromotionPreviewInfo();
			return erpProductPromotionPreviewInfo;
		} catch (SapException e) {
			throw new EJBException(e);
		}
	}
}
