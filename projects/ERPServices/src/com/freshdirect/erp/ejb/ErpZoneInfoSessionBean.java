package com.freshdirect.erp.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ErpZoneInfoSessionBean extends SessionBeanSupport{
	
    /** logger for messages
     */
    private static Category LOGGER = LoggerFactory.getInstance( ErpZoneInfoSessionBean.class );

	
	public ErpZoneMasterInfo findZoneInfoMaster(String zoneId) throws RemoteException {
	     
		Connection conn = null;
        ErpZoneMasterInfo zoneInfo=null;
		try{
			conn = getConnection();			
   		    zoneInfo=ErpZoneInfoDAO.getZoneInfoDetails(conn,zoneId);			
		}catch(SQLException sqle){
			LOGGER.error("Unable to load all loadAllZoneInfoMaster " , sqle);
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
		return zoneInfo;
				
	}
	
	
	
	public Collection findZoneInfoMaster(String zoneIds[]) throws RemoteException{
		
		Connection conn = null;
        ErpZoneMasterInfo zoneInfo=null;
        Collection zoneInfoList=new ArrayList();
		try{
			
			conn = getConnection();
			
			for(int i=0;i<zoneIds.length;i++){
				zoneInfo=ErpZoneInfoDAO.getZoneInfoDetails(conn,zoneIds[i]);
				zoneInfoList.add(zoneInfo);
			}
		}catch(SQLException sqle){
			LOGGER.error("Unable to load all loadAllZoneInfoMaster "+zoneInfo , sqle);
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
		return zoneInfoList;
		
	}
    
    public Collection loadAllZoneInfoMaster() throws RemoteException{
		Connection conn = null;
        Collection zoneIds=null;
		try{
			conn = getConnection();
			zoneIds=ErpZoneInfoDAO.getAllZoneIds(conn);
			if(zoneIds==null || zoneIds.isEmpty()) throw new FDRuntimeException("No ZoneIds to display");			
		}catch(SQLException sqle){
			LOGGER.error("Unable to load all loadAllZoneInfoMaster " , sqle);
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
		return zoneIds;
    }
                
    public  String findZoneId(String serviceType,String zipCode) throws RemoteException{
		Connection conn = null;
        String zoneId=null;
		try{
			conn = getConnection();
			zoneId=ErpZoneInfoDAO.findZoneId(conn, serviceType, zipCode);					
		}catch(SQLException sqle){
			LOGGER.error("Unable to load all loadAllZoneInfoMaster " , sqle);
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
		return zoneId;

    }
    
    public String findZoneId(String servType) throws RemoteException{
    	Connection conn = null;
        String zoneId=null;
		try{
			conn = getConnection();
			zoneId=ErpZoneInfoDAO.findZoneId(conn, servType );					
		}catch(SQLException sqle){
			LOGGER.error("Unable to load all loadAllZoneInfoMaster " , sqle);
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
		return zoneId;
    }

	public String getResourceCacheKey() {
		return "com.freshdirect.erp.ejb.ErpZoneInfoHome" ;
	}
	
}
