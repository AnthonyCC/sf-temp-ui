package com.freshdirect.erp.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Category;

import weblogic.auddi.util.Logger;

import com.freshdirect.customer.EnumZoneServiceType;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.customer.ErpZoneRegionInfo;
import com.freshdirect.customer.ErpZoneRegionZipInfo;
import com.freshdirect.erp.EnumApprovalStatus;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ErpZoneInfoDAO {

    /** logger for messages
     */
    private static Category LOGGER = LoggerFactory.getInstance( ErpZoneInfoDAO.class );

	
/*	public static int getNextBatchNumber(Connection con) throws SQLException{
         int batchNumber=-1;	
		 Connection conn = con;
         try {
            
             //
             // get a new batch number
             //
             PreparedStatement ps = conn.prepareStatement("select erps.zonebatch_seq.nextval from dual");
             ResultSet rs = ps.executeQuery();
             if (rs.next()) {
                 batchNumber = rs.getInt(1);
             } else {
                 LOGGER.error("Unable to begin new zone batch.  Didn't get a new zone batch number.");
                 throw new SQLException("Unable to begin new batch.  Didn't get a new batch number.");
             }
             rs.close();
             ps.close();
             return batchNumber;
         }catch(SQLException e){
        	 throw e;
         }         
	}
	

	public static void createHistoryData(Connection con, Timestamp batchTimestamp,int batchNumber) throws SQLException
	{
	   Connection conn = con;
       try {
    	
    	    PreparedStatement ps = conn.prepareStatement("insert into erps.history (version, date_created, created_by, approval_status) values (?,?,?,?)");
		    ps.setInt(1, batchNumber);
		    ps.setTimestamp(2, batchTimestamp);
		    ps.setString(3, "Loader");
		    ps.setString(4, EnumApprovalStatus.LOADING.getStatusCode());
		    int rowsaffected = ps.executeUpdate();
		    if (rowsaffected != 1) {
		        throw new SQLException("Unable to begin new batch.  Couldn't update loader history table.");
		    }
		    		    
		    ps.close();
		    
       }catch(SQLException e){
      	 throw e;
       }	    
	}
	
	
	public static void updateHistoryData(Connection con, int batchNumber,EnumApprovalStatus approvalStatus,String description) throws SQLException
	{
        Connection conn = con;
        try {
            //
            // get connection
            //
            

            PreparedStatement ps = conn.prepareStatement("update erps.history set approval_status=?, description=? where version=?");
            ps.setString(1, approvalStatus.getStatusCode());
            ps.setString(2, description);
            ps.setInt(3, batchNumber);
            int rowsaffected = ps.executeUpdate();
            if (rowsaffected != 1) {
                throw new SQLException("Unable to complete a new batch.  Couldn't update loader history table to mark a batch as sucessfully loaded.");
            }
            ps.close();
            ps = null;
            
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            LOGGER.error("Unable to complete a new batch.  Couldn't update loader history table to mark a batch as sucessfully loaded.", sqle);
            throw new SQLException("Unable to complete a new batch.  Couldn't update loader history table to mark a batch as sucessfully loaded.  " + sqle);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle2) {
                    sqle2.printStackTrace();
                    LOGGER.error("Unable to complete a new batch.  Couldn't update loader history table to mark a batch as sucessfully loaded.", sqle2);
                    throw new SQLException("Unable to complete a new batch.  Couldn't update loader history table to mark a batch as sucessfully loaded.  " + sqle2);
                }
            }
        }
	}
	
	
	*/
    
    
    private static final String FIND_ZONE_PRICING_SELECT=" select z.ID, z.SAP_ID sap_id, z.REGION_ID, z.SERVICE_TYPE,zip.ZIP_CODE "+
    													" from  erps.pricing_zone z, erps.pricing_region_zips zip " +  
    													" where z.version=(select max(version) version from erps.pricing_zone where sap_id=z.sap_id) "+
    													" and z.region_id=zip.region_id "+ 
    													" and zip.zip_code=? "+
    													" and z.service_type=?";
    
    
                    
    public static String findZoneId(Connection con,String serviceType,String zipCode) throws SQLException{		
		   Connection conn = con;
		   List zoneIdList=new ArrayList();	
		   String zoneId=null;
	       try {
	    	   PreparedStatement ps = conn.prepareStatement(FIND_ZONE_PRICING_SELECT);
	    	   ps.setString(1,zipCode);
	    	   ps.setString(2,serviceType);
	    	   ResultSet rs = ps.executeQuery();
	           if (rs.next()) {
	           	 zoneId=rs.getString("sap_id");	            	 
	           }
	       }catch(SQLException e){
	      	 throw e;
	       }
	       Logger.info("zoneId is :"+zoneId);
		   return zoneId; 
	}
    

    private static final String FIND_ZONE_PRICING_DEFALUT_SELECT="select z.ID, z.SAP_ID, z.REGION_ID, z.SERVICE_TYPE "+
    													  "	from  erps.pricing_zone z where z.version=(select max(version) version from erps.pricing_zone where sap_id=z.sap_id) "+
    													  " and z.DEFAULT_ZONE='X' and z.service_type=?";
    
    
    
    public static String findZoneId(Connection con,String serviceType) throws SQLException{		
		   Connection conn = con;
		   List zoneIdList=new ArrayList();	
		   String zoneId=null;
	       try {
	    	   PreparedStatement ps = conn.prepareStatement(FIND_ZONE_PRICING_DEFALUT_SELECT);	    	   
	    	   ps.setString(1,serviceType);
	    	   ResultSet rs = ps.executeQuery();
	           if (rs.next()) {
	           	 zoneId=rs.getString("sap_id");	            	 
	           }
	       }catch(SQLException e){
	      	 throw e;
	       }
	       Logger.info("zoneId is :"+zoneId);
		   return zoneId; 
	}
    
    
	private static final String ALL_ZONE_PRICING_SELECT_SQL="select max(id), sap_id, max(version) from erps.pricing_zone group by (sap_id)";
   	
	public static Collection getAllZoneIds(Connection con) throws SQLException{
		
		   Connection conn = con;
		   List zoneIdList=new ArrayList();	
	       try {
	    	   PreparedStatement ps = conn.prepareStatement(ALL_ZONE_PRICING_SELECT_SQL);
	    	   ResultSet rs = ps.executeQuery();
	             while (rs.next()) {
	            	 zoneIdList.add(rs.getString("sap_id"));	            	 
	             }
	       }catch(SQLException e){
	      	 throw e;
	       }
 	       Logger.info("All Zone Id List :"+zoneIdList.size());
		   return zoneIdList; 
	}
	
	
	private static final String ZONE_PRICING_SELECT_SQL="select ID, SAP_ID, REGION_ID, SERVICE_TYPE, PARENT_ZONE_ID, DEFAULT_ZONE, VERSION, DESCRIPTION, WEB_DESCRIPTION "+   
														" from  erps.pricing_zone where sap_id=? and version=(select max(version) version from erps.pricing_zone where sap_id=?) ";
	
	
	public static ErpZoneMasterInfo getZoneInfoDetails(Connection con,String zoneId) throws SQLException{
		   Connection conn = con;
		   ErpZoneMasterInfo zoneInfo=null;
	       try {	    	   	    	   	    	   
	    	   PreparedStatement ps = conn.prepareStatement(ZONE_PRICING_SELECT_SQL);
	    	   ps.setString(1,zoneId);
	    	   ps.setString(2,zoneId);
	    	   ResultSet rs = ps.executeQuery();	    	   
	             if(rs.next()) {
	            	 String regionId=rs.getString("REGION_ID");
	            	 ErpZoneRegionInfo regionInfo=getZoneRegionInfoDetails(conn,regionId);
	            	 List zoneZipList=getZoneInfoZipDetails(conn,regionInfo);
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
	       }
 	       Logger.info("getZoneInfoDetails ErpZoneMasterInfo :"+zoneInfo);
		   return zoneInfo; 			
	}
	
	
	public static final String ZONE_PRICING_REGION_SELECT_SQL="select ID, VERSION, DESCRIPTION, SAP_ID  from erps.pricing_region where id=?";
	
	public static ErpZoneRegionInfo getZoneRegionInfoDetails(Connection con,String id) throws SQLException{
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
	       Logger.info("ErpZoneRegionInfo :"+zoneRegionInfo);
		   return zoneRegionInfo; 			
	}


    private static final String ZONE_PRICING_ZIP_SELECT="select ID, REGION_ID,VERSION,ZIP_CODE from erps.pricing_region_zips where region_id=?";	
	
	public static List getZoneInfoZipDetails(Connection con,ErpZoneRegionInfo regionInfo) throws SQLException{
		   Connection conn = con;
		   
		   List zoneZipInfoList=new ArrayList();
	       try {
	    	   PreparedStatement ps = conn.prepareStatement(ZONE_PRICING_ZIP_SELECT);
	    	   ps.setString(1,regionInfo.getId());
	    	   ResultSet rs = ps.executeQuery();
	             while (rs.next()) {
	            	 String id=rs.getString("ID");	            	 
	            	 int version=rs.getInt("VERSION");
	            	 String zipCode=rs.getString("ZIP_CODE");	            	 
	            	 ErpZoneRegionZipInfo zoneZipInfo=new ErpZoneRegionZipInfo(regionInfo,zipCode);
	            	 zoneZipInfo.setId(id);
	            	 zoneZipInfo.setVersion(version);
	            	 zoneZipInfoList.add(zoneZipInfo);
	             }
	       }catch(SQLException e){
	      	 throw e;
	       }
	       
	       Logger.info("All Zone zip List :"+zoneZipInfoList.size());
		   return zoneZipInfoList; 			
	}
   
	

	private static final String ZONE_PRICING_SQL="insert into erps.pricing_zone (ID, SAP_ID, REGION_ID, SERVICE_TYPE, PARENT_ZONE_ID, DEFAULT_ZONE, VERSION, DESCRIPTION, WEB_DESCRIPTION) "+
	" values (?,?,?,?,?,?,?,?,?)";
			
	
	public static void createZoneMasterInfo(Connection con,int batchNumber,List zoneInfoList ) throws SQLException{
		
		   Connection conn = con;
			
	       try {
	    	   
	    	   for(int i=0;i<zoneInfoList.size();i++) 
	    	   {
	    		    ErpZoneMasterInfo zoneInfo=(ErpZoneMasterInfo)zoneInfoList.get(i);
	    		    ErpZoneRegionInfo region=zoneInfo.getRegion();
	    		    createZonePricingRegionInfo(conn, batchNumber,region);
	    		    List erpZoneRegionZipInfo=region.getZoneRegionZipList();
	    		    createZonePricingRegionZipInfo(conn, batchNumber, erpZoneRegionZipInfo);	    		    
		    	    String id = getNextId(conn, "ERPS");		    	    
		    	    PreparedStatement ps = conn.prepareStatement(ZONE_PRICING_SQL);
		    	    ps.setString(1, id);
		    	    ps.setString(2, zoneInfo.getSapId());
		    	    ps.setString(3, zoneInfo.getRegion().getPK().getId());
		    	    ps.setString(4, zoneInfo.getServiceType().getName());
		    	    if(zoneInfo.getParentZone()!=null)
		    	       ps.setString(4, zoneInfo.getParentZone().getPK().getId());
		    	    else
		    	       ps.setNull(4, Types.VARCHAR);
		    	    ps.setString(5, zoneInfo.isDefault()?"X":null);		    	    
				    ps.setInt(6, batchNumber);				    
				    ps.setString(7, zoneInfo.getDescription());				    
				    int rowsaffected = ps.executeUpdate();
				    if (rowsaffected != 1) {
				        throw new SQLException("Unable to create new batch.  Couldn't create zone info master table.");
				    }				    		    
			        ps.close();
	    	   }    
			    
	       }catch(SQLException e){
	      	 throw e;
	       }
		
	}
	
	  /** gets the next unique Id to use when writing a persistent object
     * to a persistent store for the first time
     * @param conn a SQLConnection to use to find an Id
     * @throws SQLException any problems while getting a unique id
     * @return an id that uniquely identifies a persistent object
     */    
    protected static String getNextId(Connection conn, String schema) throws SQLException {
		return SequenceGenerator.getNextId(conn, schema);
    }
	
	
	private static final String ZONE_PRICING_REGION_SQL="insert into erps.pricing_region (ID, SAP_ID , VERSION, DESCRIPTION) values (?,?,?,?)";

	
	public static void createZonePricingRegionInfo(Connection con,int batchNumber,ErpZoneRegionInfo region ) throws SQLException{
		
		   Connection conn = con;
	       try {
	    	    String id = getNextId(conn, "ERPS");
	    	    PrimaryKey key=new PrimaryKey(id);
	    	    region.setPK(key);
	    	    PreparedStatement ps = conn.prepareStatement(ZONE_PRICING_REGION_SQL);
	    	    ps.setString(1, id);
			    ps.setString(2, region.getSapId());
			    ps.setInt(3, batchNumber);			    
			    ps.setString(4, region.getDescription());			    
			    int rowsaffected = ps.executeUpdate();
			    if (rowsaffected != 1) {
			        throw new SQLException("Unable to begin new batch.  Couldn't update loader zone pricing Region Info .");
			    }
			    		    
			    ps.close();
			    
	       }catch(SQLException e){
	      	 throw e;
	       }
		
	}
	
	private static final String ZONE_PRICING_REGION_ZIPS_SQL="insert into erps.PRICING_REGION_ZIPS (ID, REGION_ID, VERSION, ZIP_CODE) "+
															" values (?,?,?,?)";

	
	public static void createZonePricingRegionZipInfo(Connection con,int batchNumber,List zoneInfoZipList) throws SQLException {
		
		   Connection conn = con;
	       try {
	    	   
	    	    PreparedStatement ps = conn.prepareStatement(ZONE_PRICING_REGION_ZIPS_SQL);
	    	    for(int i=0;i<zoneInfoZipList.size();i++)
	    	    {
	    	    	ErpZoneRegionZipInfo zipInfo=(ErpZoneRegionZipInfo)zoneInfoZipList.get(i);
	    	    	String id = getNextId(conn, "ERPS");
		    	    PrimaryKey key=new PrimaryKey(id);
		    	    zipInfo.setPK(key);
		    	    ps.setString(1, id);
				    ps.setString(2, zipInfo.getRegion().getPK().getId());
				    ps.setInt(3, batchNumber);
				    ps.setString(4, zipInfo.getZipCode());				    
				    ps.addBatch();				    
	    	    }    
	    	    int rowsaffected[] = ps.executeBatch();
	    	    if (rowsaffected.length != zoneInfoZipList.size()) {
			        throw new SQLException("Unable to create new batch.  Couldn't create Zone Pricing Zip information.");
			    }
			    ps.close();
			    
	       }catch(SQLException e){
	      	 throw e;
	       }		
	}

	
}
