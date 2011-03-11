package com.freshdirect.erp.ejb;


import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpGrpPriceModel;
import com.freshdirect.erp.PricingFactory;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDGroupNotFoundException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ErpGrpInfoSessionBean extends SessionBeanSupport{
	
    /** logger for messages
     */
    private static Category LOGGER = LoggerFactory.getInstance( ErpGrpInfoSessionBean.class );

	
	public GroupScalePricing findGrpInfoMaster(FDGroup group) throws FDGroupNotFoundException {
	     
		Connection conn = null;
		ErpGrpPriceModel grpInfo=null;
		try{
			conn = getConnection();			
			grpInfo=ErpGrpInfoDAO.getGrpInfo(conn,group.getGroupId(), group.getVersion());
		}catch(SQLException sqle){
			LOGGER.error("Unable to load all findGrpInfoMaster " , sqle);
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
		if(grpInfo == null)
			throw new FDGroupNotFoundException("Group Id "+group.getGroupId()+" Version "+group.getVersion()+" Not found.");
		
		return PricingFactory.convertToGroupScalePricing(grpInfo);
				
	}
	
	
	public FDGroup getGroupIdentityForMaterial(String matId) throws RemoteException {
	     
		Connection conn = null;
		FDGroup group=null;
		try{
			conn = getConnection();			
   		    group=ErpGrpInfoDAO.getGroupIdentityForMaterial(conn,matId);			
		}catch(SQLException sqle){
			LOGGER.error("Unable to load all findGrpInfoMaster " , sqle);
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
		return group;
				
	}
	
	public Collection getFilteredSkus(List skuList) throws RemoteException{
		
		Connection conn = null;
		Collection zoneInfo=null;
		try{
			conn = getConnection();			
   		    zoneInfo=ErpGrpInfoDAO.getFilteredSkus(conn,skuList);			
		}catch(SQLException sqle){
			LOGGER.error("Unable to load all findGrpInfoMaster " , sqle);
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
	
	
	public Collection<GroupScalePricing> findGrpInfoMaster(FDGroup grpIds[]) throws RemoteException{
		
		Connection conn = null;
        Collection<GroupScalePricing> grpInfoList=new ArrayList<GroupScalePricing>();
		try{
			
			conn = getConnection();
			
			for(int i=0;i<grpIds.length;i++){
				FDGroup group = grpIds[i];
				ErpGrpPriceModel grpInfo=ErpGrpInfoDAO.getGrpInfo(conn,group.getGroupId(), group.getVersion());
				grpInfoList.add(PricingFactory.convertToGroupScalePricing(grpInfo));
			}
		}catch(SQLException sqle){
			LOGGER.error("Unable to load all findGrpInfoMaster "+grpIds, sqle);
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
		return grpInfoList;
		
	}
    
    public Collection<FDGroup> loadAllGrpInfoMaster() throws RemoteException{
		Connection conn = null;
        Collection<FDGroup> groups=null;
		try{
			conn = getConnection();
			groups=ErpGrpInfoDAO.getAllGroupIds(conn);
			if(groups==null || groups.isEmpty()) throw new FDRuntimeException("No Groups to display");			
		}catch(SQLException sqle){
			LOGGER.error("Unable to load all loadAllGrpInfoMaster " , sqle);
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
		return groups;
    }
    
    public int getLatestVersionNumber(String grpId) {
		Connection conn = null;
		try{
			conn = getConnection();
			return ErpGrpInfoDAO.getLatestVersionNumber(conn, grpId);
		}catch(SQLException sqle){
			LOGGER.error("Unable to load all loadAllGrpInfoMaster " , sqle);
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
    }
  

	public String getResourceCacheKey() {
		return "com.freshdirect.erp.ejb.ErpGrpInfoHome" ;
	}
	
	public Collection<FDGroup> findGrpsForMaterial(String matId) throws RemoteException {
		Connection conn = null;
		Collection<FDGroup> groups = null;
		try {
			conn = getConnection();
			groups = ErpGrpInfoDAO.getAllGroupsForMaterial(conn, matId);
			if (groups == null || groups.isEmpty())
				throw new FDRuntimeException("No Groups to display");
		} catch (SQLException sqle) {
			LOGGER.error("Unable to load all loadAllGrpInfoMaster ", sqle);
			throw new EJBException(sqle);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle) {
				LOGGER.error("Unable to close db resources", sqle);
				throw new EJBException(sqle);
			}
		}
		return groups;
	}
    
	
}
