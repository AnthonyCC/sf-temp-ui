package com.freshdirect.dataloader.sap.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.customer.ErpZoneRegionInfo;
import com.freshdirect.customer.ErpZoneRegionZipInfo;
import com.freshdirect.erp.EnumApprovalStatus;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.log.LoggerFactory;

public class SAPZoneInfoLoaderDAO {

    /** logger for messages
     */
    private static Category LOGGER = LoggerFactory.getInstance( SAPZoneInfoLoaderDAO.class );

	
	public static int getNextBatchNumber(Connection con) throws SQLException{
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
    	
    	    PreparedStatement ps = conn.prepareStatement("insert into erps.zone_history (version, date_created, created_by, approval_status) values (?,?,?,?)");
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
            

            PreparedStatement ps = conn.prepareStatement("update erps.zone_history set approval_status=?, description=? where version=?");
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
	
	private static final String ZONE_PRICING_SQL="insert into erps.pricing_zone (ID, SAP_ID, REGION_ID, SERVICE_TYPE, PARENT_ZONE_ID, DEFAULT_ZONE, VERSION, DESCRIPTION, WEB_DESCRIPTION) "+
												" values (?,?,?,?,?,?,?,?,?)";
	
	
	
	private static final String ZONE_PRICING_UPDATE="update erps.pricing_zone set PARENT_ZONE_ID=? where id=? ";
	
	public static void updateZoneParentInfo(Connection con,int batchNumber,List zoneInfoList) throws SQLException{

		   Connection conn = con;
			
	       try {
	    	   PreparedStatement ps = conn.prepareStatement(ZONE_PRICING_UPDATE);
	    	   for(int i=0;i<zoneInfoList.size();i++) 
	    	   {
	    		    ErpZoneMasterInfo zoneInfo=(ErpZoneMasterInfo)zoneInfoList.get(i);
	    		    if(zoneInfo.getParentZone()==null) continue;
	    		    
	    		    ps.setString(1, zoneInfo.getParentZone().getSapId());
	    		    ps.setString(2, zoneInfo.getId());
	    		    ps.addBatch();
	    	   }
	    	   int rowsaffected[] = ps.executeBatch();
			    //if (rowsaffected != 1) {
			    //    throw new SQLException("Unable to create new batch.  Couldn't create zone info master table.");
			    //}				    		    
		        ps.close();
   	       
		    
      }catch(SQLException e){
    	  e.printStackTrace();
     	 throw e;
      }
	}
	
	
	public static void createZoneMasterInfo(Connection con,int batchNumber,List<ErpZoneMasterInfo> zoneInfoList ) throws SQLException{
		
		   Connection conn = con;
			
	       try {
	    	   
	    	   for(int i=0;i<zoneInfoList.size();i++) 
	    	   {
	    		    ErpZoneMasterInfo zoneInfo=(ErpZoneMasterInfo)zoneInfoList.get(i);
	    		    ErpZoneRegionInfo region=zoneInfo.getRegion();
	    		    createZonePricingRegionInfo(conn, batchNumber,region);
	    		    System.out.println("region is created :"+region.getId());
	    		    List erpZoneRegionZipInfo=region.getZoneRegionZipList();
	    		    if(erpZoneRegionZipInfo!=null && erpZoneRegionZipInfo.size()>0)
	    		         createZonePricingRegionZipInfo(conn, batchNumber, erpZoneRegionZipInfo);
	    		    System.out.println("creating the zip ");
		    	    String id = getNextId(conn, "ERPS");	
		    	    zoneInfo.setId(id);
		    	    PreparedStatement ps = conn.prepareStatement(ZONE_PRICING_SQL);
		    	    ps.setString(1, id);
		    	    ps.setString(2, zoneInfo.getSapId());
		    	    ps.setString(3, zoneInfo.getRegion().getPK().getId());
		    	    ps.setString(4, zoneInfo.getServiceType().getName());
		    	    if(zoneInfo.getParentZone()!=null)
		    	       ps.setString(5, zoneInfo.getParentZone().getPK().getId());
		    	    else
		    	       ps.setNull(5, Types.VARCHAR);
		    	    ps.setString(6, isDefaultZoneId(zoneInfo.getSapId())?"X":null);		    	    
				    ps.setInt(7, batchNumber);				    
				    ps.setString(8, zoneInfo.getDescription());
				    if(zoneInfo.getWebDescription()!=null)
				        ps.setString(9, zoneInfo.getWebDescription());
				    else
				    	ps.setNull(9, Types.VARCHAR);
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
	
	
	private static String defaultZone[]=null;
	 
	   private static boolean isDefaultZoneId(String sapId){
		   
		   if(defaultZone==null){
			   StringTokenizer t=new StringTokenizer(ErpServicesProperties.getDefaultSAPZoneId(),",");
			   defaultZone=new String[t.countTokens()];
			   int count=0;
			   while(t.hasMoreTokens()){
				   defaultZone[count++]=t.nextToken();
			   }
		   }		   		   		   		   
		   System.out.println("defaultZone length:"+defaultZone.length);
		   for (String element : defaultZone) {
			  if(element.equalsIgnoreCase(sapId))  return true;			  		 
		   }
		   return false;
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
