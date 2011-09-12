package com.freshdirect.dataloader.sap.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;


import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.ErpGrpPriceZoneModel;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.customer.ErpZoneRegionInfo;
import com.freshdirect.customer.ErpZoneRegionZipInfo;
import com.freshdirect.erp.EnumApprovalStatus;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.customer.ErpGrpPriceModel;

public class SAPGrpInfoLoaderDAO {

    /** logger for messages
     */
    private static Category LOGGER = LoggerFactory.getInstance( SAPGrpInfoLoaderDAO.class );

	
	public static int getNextBatchNumber(Connection con) throws SQLException{
         int batchNumber=-1;	
		 Connection conn = con;
         try {
            
             //
             // get a new batch number
             //
             PreparedStatement ps = conn.prepareStatement("select erps.grpbatch_seq.nextval from dual");
             ResultSet rs = ps.executeQuery();
             if (rs.next()) {
                 batchNumber = rs.getInt(1);
             } else {
                 LOGGER.error("Unable to begin new grp batch.  Didn't get a new grp batch number.");
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
    	
    	    PreparedStatement ps = conn.prepareStatement("insert into erps.grp_history (version, date_created, created_by, approval_status) values (?,?,?,?)");
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
            

            PreparedStatement ps = conn.prepareStatement("update erps.grp_history set approval_status=?, description=? where version=?");
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
	
	
	private static final String GRP_MASTER_INSERT=" INSERT INTO ERPS.GRP_SCALE_MASTER  ( ID, VERSION, SAP_ID , SHORT_DESC , LONG_DESC, ACTIVE ) VALUES (?,?,?,?,?,?)";
	
	public static void createGrpMasterInfo(Connection con,int batchNumber,List zoneInfoList ) throws SQLException{
		
		   Connection conn = con;
			
	       try {
	    	   
	    	   for(int i=0;i<zoneInfoList.size();i++) 
	    	   {
	    		   ErpGrpPriceModel zoneInfo=(ErpGrpPriceModel)zoneInfoList.get(i);	    		   	    		   	    		   	    	
		    	    String id = getNextId(conn, "ERPS");	
		    	    zoneInfo.setId(id);
		    	    PreparedStatement ps = conn.prepareStatement(GRP_MASTER_INSERT);
		    	    ps.setString(1, id);
		    	    ps.setInt(2, batchNumber);
		    	    ps.setString(3, zoneInfo.getGrpId());
		    	    ps.setString(4, zoneInfo.getShortDesc());
		    	    ps.setString(5, zoneInfo.getLongDesc());
		    	    if(zoneInfo.isActive())
		    	       ps.setString(6, "X");
		    	    else
		    	       ps.setNull(6, Types.VARCHAR);		    	 
				    int rowsaffected = ps.executeUpdate();
				    if (rowsaffected != 1) {
				        throw new SQLException("Unable to create new batch.  Couldn't create grp info master table.");
				    }				    		    
			        ps.close();
			        
			        createGrpPricingInfo(conn, batchNumber, id, zoneInfo.getZoneModelList());
			        createGrpMaterialInfo(conn, batchNumber, id, zoneInfo.getMatList());
			        			        		        			        
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
		   for(int i=0;i<defaultZone.length;i++){
			  if(defaultZone[i].equalsIgnoreCase(sapId))  return true;			  		 
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
	
	
	private static final String GRP_PRICING_ZONE_SQL="INSERT INTO  ERPS.GRP_PRICING ( ID, VERSION, GRP_ID, ZONE_ID, QUANTITY, PRICING_UNIT, PRICE, SCALE_UNIT) VALUES(?,?,?,?,?,?,?,?)";

	
	public static void createGrpPricingInfo(Connection con,int batchNumber,String grpId,Set grpPriceList ) throws SQLException{
		
		   Connection conn = con;
	       try {
	    	   
	    	   if(grpPriceList==null || grpPriceList.size()==0) return;
	    	   PreparedStatement ps = conn.prepareStatement(GRP_PRICING_ZONE_SQL);
	    	   Iterator iterator=grpPriceList.iterator();
	    	   
	    	   while(iterator.hasNext()){
	    	    ErpGrpPriceZoneModel zoneModel=(ErpGrpPriceZoneModel)iterator.next();
	    	    String id = getNextId(conn, "ERPS");
	    	    PrimaryKey key=new PrimaryKey(id);
	    	    //region.setPK(key);
	    	  
	    	    ps.setString(1, id);
			    ps.setInt(2, batchNumber);
			    ps.setString(3, grpId);			    
			    ps.setString(4, zoneModel.getZoneId());
			    ps.setDouble(5, zoneModel.getQty());
			    ps.setString(6, zoneModel.getUnitOfMeasure());
			    ps.setDouble(7,zoneModel.getPrice());
			    ps.setString(8,zoneModel.getScaleUnit());
			    ps.addBatch();			    			    	    	     		    			    
	    	  } 
	    	   
	    	   int rowsaffected[] = ps.executeBatch();
			    if (rowsaffected.length < 1) {
			        throw new SQLException("Unable to begin new batch.  Couldn't update loader grp zone pricing Region Info .");
			    }
	    	   ps.close();
			    
	       }catch(SQLException e){
	      	 throw e;
	       }
		
	}
	
	private static final String GRP_MAT_SQL=" INSERT INTO  ERPS.MATERIAL_GRP  ( ID, VERSION, MAT_ID, GRP_ID) VALUES (?,?,?,?)";

	
	public static void createGrpMaterialInfo(Connection con,int batchNumber,String grpId, Set matList) throws SQLException {
		
		   Connection conn = con;
	       try {
	    	   
	    	    PreparedStatement ps = conn.prepareStatement(GRP_MAT_SQL);
	    	    
	    	    Iterator iterator=matList.iterator();
		    	   
		    	while(iterator.hasNext()){
	    	   
	    	    	String mat=(String)iterator.next();
	    	    	String id = getNextId(conn, "ERPS");
		    	    PrimaryKey key=new PrimaryKey(id);		    	    
		    	    ps.setString(1, id);
				    ps.setInt(2, batchNumber);
				    ps.setString(3, mat);
				    ps.setString(4, grpId);				    
				    ps.addBatch();				    
	    	    }    
	    	    int rowsaffected[] = ps.executeBatch();
	    	    if (rowsaffected.length != matList.size()) {
			        throw new SQLException("Unable to create new batch.  Couldn't create Grp Mat Pricing information.");
			    }
			    ps.close();
			    
	       }catch(SQLException e){
	      	 throw e;
	       }			       	       
	}
    
	
}
