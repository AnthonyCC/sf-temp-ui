package com.freshdirect.erp.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumZoneServiceType;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.customer.ErpZoneRegionInfo;
import com.freshdirect.framework.util.DaoUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
/**
 *@deprecated Please use the ErpZoneInfoDao  in Storefront2.0 project.
 * SVN location :: https://appdevsvn.nj01/appdev/ecommerce
 *
 *
 */
public class ErpZoneInfoDAO {

    /** logger for messages
     */
    private static Category LOGGER = LoggerFactory.getInstance( ErpZoneInfoDAO.class );

	
	private static final String ZONE_PRICING_SELECT_SQL="select ID, SAP_ID, REGION_ID, SERVICE_TYPE, PARENT_ZONE_ID, DEFAULT_ZONE, VERSION, DESCRIPTION, WEB_DESCRIPTION "+   
														" from  erps.pricing_zone where sap_id=? and version=(select max(version) version from erps.pricing_zone where sap_id=?) ";
	
	@Deprecated
	public static ErpZoneMasterInfo getZoneInfoDetails(Connection con,String zoneId) throws SQLException{
		   Connection conn = con;
		   ErpZoneMasterInfo zoneInfo=null;
		   PreparedStatement ps = null;
		   ResultSet rs = null;
		   try {	    	   	    	   	    	   
	    	   ps = conn.prepareStatement(ZONE_PRICING_SELECT_SQL);
	    	   ps.setString(1,zoneId);
	    	   ps.setString(2,zoneId);
	    	   rs = ps.executeQuery();	    	   
	             if(rs.next()) {
	            	 String regionId=rs.getString("REGION_ID");
	            	 ErpZoneRegionInfo regionInfo=getZoneRegionInfoDetails(conn,regionId);
	            	 //List zoneZipList=getZoneInfoZipDetails(conn,regionInfo);
	            	 String sapId=rs.getString("SAP_ID");
	            	 int version=rs.getInt("VERSION");
	            	 String desc=rs.getString("DESCRIPTION");
	            	 String webDesc=rs.getString("WEB_DESCRIPTION");
	            	 EnumZoneServiceType servType=EnumZoneServiceType.getEnum(rs.getString("SERVICE_TYPE"));
	            	 zoneInfo=new ErpZoneMasterInfo(sapId,regionInfo,servType,desc);
	            	 zoneInfo.setId(rs.getString("ID"));
	            	 zoneInfo.setWebDescription(webDesc);
	            	 zoneInfo.setVersion(version);
	            	 String parentZoneId=rs.getString("PARENT_ZONE_ID");
	            	 if(parentZoneId!=null){
	            		 zoneInfo.setParentZone(getZoneInfoDetails(conn,parentZoneId));
	            	 }
	            	 	            	 
	             }
	       }catch(SQLException e){
	      	 throw e;
	       } finally{
	    	   DaoUtil.close(rs);
	    	   DaoUtil.close(ps);
	       }
 	       //LOGGER.info("getZoneInfoDetails ErpZoneMasterInfo :"+zoneInfo);
		   return zoneInfo; 			
	}
	
	private static final String ZONE_PRICING_REGION_SELECT_SQL="select ID, VERSION, DESCRIPTION, SAP_ID  from erps.pricing_region where id=?";
	@Deprecated
	private static ErpZoneRegionInfo getZoneRegionInfoDetails(Connection con,String id) throws SQLException{
		   Connection conn = con;
		   ErpZoneRegionInfo zoneRegionInfo=null;
	       try {
	    	   PreparedStatement ps = conn.prepareStatement(ZONE_PRICING_REGION_SELECT_SQL);
	    	   ps.setString(1,id);
	    	   ResultSet rs = ps.executeQuery();
	             if (rs.next()) {
	            	 String sapId=rs.getString("SAP_ID");
	            	 String desc=rs.getString("DESCRIPTION");	            	 
	            	 zoneRegionInfo=new ErpZoneRegionInfo(sapId,desc);
	            	 zoneRegionInfo.setId(id);
	             }
	       }catch(SQLException e){
	      	 throw e;
	       }
	       //LOGGER.info("ErpZoneRegionInfo :"+zoneRegionInfo);
		   return zoneRegionInfo; 			
	}


	
}
