package com.freshdirect.dataloader.sap.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpProductFamilyModel;
import com.freshdirect.erp.EnumApprovalStatus;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.log.LoggerFactory;

public class SAPProductFamilyLoaderDAO {

	 /** logger for messages
     */
    private static Category LOGGER = LoggerFactory.getInstance( SAPProductFamilyLoaderDAO.class );
    
    public static int getNextBatchNumber(Connection con) throws SQLException{
        int batchNumber=-1;	
		 Connection conn = con;
        try {
        	//
            // get a new batch number
            //
        	  PreparedStatement ps = conn.prepareStatement("select erps.product_family_batch.nextval from dual");
              ResultSet rs = ps.executeQuery();
              if (rs.next()) {
                  batchNumber = rs.getInt(1);
              } else {
                  LOGGER.error("Unable to begin new Product Family batch.  Didn't get a new Product Family batch number.");
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
	   String id = getNextId(conn, "ERPS");
       try {

    	    PreparedStatement ps = conn.prepareStatement("insert into erps.product_family_history (ID,VERSION, DATE_CREATED,MESSAGE) values (?,?,?,?)");
		    ps.setString(1, id);
		    ps.setString(2, String.valueOf(batchNumber));
		    ps.setTimestamp(3, batchTimestamp);
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
            

            PreparedStatement ps = conn.prepareStatement("update erps.product_family_history set MESSAGE=? where version=?");
            ps.setString(1, description);
            ps.setString(2, String.valueOf(batchNumber));
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
    
    
    
    /** gets the next unique Id to use when writing a persistent object
     * to a persistent store for the first time
     * @param conn a SQLConnection to use to find an Id
     * @throws SQLException any problems while getting a unique id
     * @return an id that uniquely identifies a persistent object
     */    

    
    protected static String getNextId(Connection conn,String schema) throws SQLException {
		return SequenceGenerator.getNextId(conn,schema);
    }
    
    private static final String PRODUCT_FAMILY_MASTER_INSERT=" INSERT INTO ERPS.PRODUCT_FAMILY_MASTER  (ID, FAMILY_ID,VERSION,ACTIVE, PRODUCT_ID,GROUP_STAT) VALUES (?,?,?,?,?,?)";
    
   
    public static void createProductFamilyMasterInfo(Connection con,int batchNumber,List productFamilyList ) throws SQLException{
    Connection conn = con;
	

    Set<String> flyIds = new HashSet<String>();
    try {


    	 for(int i=0;i<productFamilyList.size();i++) 
    		
  	   {

   		 ErpProductFamilyModel erpProductFamilyModel = (ErpProductFamilyModel)productFamilyList.get(i);
   		flyIds.add(erpProductFamilyModel.getGrpId());
   		String id = getNextId(conn, "ERPS");
     	 LOGGER.debug("id value ####"+id);
     	 LOGGER.debug("inside for loop prepared statement #####");
     	 PreparedStatement ps = conn.prepareStatement(PRODUCT_FAMILY_MASTER_INSERT);
    	 ps.setString(1, id);
  	     ps.setString(2, erpProductFamilyModel.getGrpId());
    	 ps.setString(3, String.valueOf(batchNumber));
  	     ps.setString(4,erpProductFamilyModel.getAction());
  	     ps.setString(5, erpProductFamilyModel.getMaterialNumber());
  	     ps.setString(6, erpProductFamilyModel.getDeletegroup());
  	     int rowsaffected = ps.executeUpdate();
  	     if (rowsaffected != 1) {
	        throw new SQLException("Unable to create new batch.  Couldn't create grp info master table.");
	    }	
  	    		    		    
  	     ps.close();
   
  	   }
    	 updateFmlyVersion(conn,batchNumber,flyIds); 
    }
    catch(SQLException e){
     	 throw e;
      }
    }
     
    private static final String PRODUCT_FAMILY_HISTORY_INSERT=" INSERT INTO ERPS.PRODUCT_FAMILY_HISTORY  (ID,VERSION,DATE_CREATED, MESSAGE) VALUES (?,?,?,?)";
    
    private static void updateFmlyVersion(Connection conn, int version,
			Set<String> flyIds)  throws SQLException {
    	
    	String id = getNextId(conn, "ERPS");
     	
     	 LOGGER.debug("inside for updateFmlyVersion statement #####");
     	 PreparedStatement ps = conn.prepareStatement(PRODUCT_FAMILY_HISTORY_INSERT);
    	 ps.setString(1, id);
  	     ps.setInt(2, version);
    	 ps.setDate(3, new java.sql.Date(new java.util.Date().getTime()));
  	     ps.setString(4,"Success");
  	     
  	     int rowsaffected = ps.executeUpdate();
  	     if (rowsaffected != 1) {
	        throw new SQLException("Unable to create new batch.  Couldn't create product Family info master table.");
	    }	
  	    		
	}


private static final String PRODUCT_FAMLY_MAT_SQL=" INSERT INTO  ERPS.MATERIAL_PRODUCT_FAMILY  ( ID, VERSION, MAT_ID, GRP_ID) VALUES (?,?,?,?)";

	
	public static void createGrpMaterialInfo(Connection con,int batchNumber,String grpId, String materialNumber) throws SQLException {
		
		   Connection conn = con;
	       try {
	    	   
	    	    PreparedStatement ps = conn.prepareStatement(PRODUCT_FAMLY_MAT_SQL);
		    	   
	    	  
	       	 // NOT passsing ERPS to get sqence. passing system sequence for main method call testing purpose
	    	    String id = getNextId(conn, "ERPS");
	    	    	
		    	    PrimaryKey key=new PrimaryKey(id);		    	    
		    	    ps.setString(1, id);
				    ps.setInt(2, batchNumber);
				    ps.setString(3, materialNumber);
				    ps.setString(4, grpId);				    
				    int rowsaffected = ps.executeUpdate();			    
	    	      
				    if (rowsaffected != 1) {
				        throw new SQLException("Unable to create new batch.  Couldn't create MATERIAL_PRODUCT_FAMILY info table");
				    }	
			    ps.close();
			    
	       }catch(SQLException e){
	      	 throw e;
	       }			       	   
    
}

	public static final String PRODUCT_FAMILY_SELECT_SQL= "select product_id from ERPS.PRODUCT_FAMILY_MASTER a where a.active <>'D' and a.GROUP_STAT <>'X' and a.version= (select MAX(version) from ERPS.PRODUCT_FAMILY_MASTER where FAMILY_ID=?) and a.FAMILY_ID=?";

	public static List<String> fetchProductFamilyMasterInfo(Connection conn,
			String familyId) throws SQLException {

		   List<String> skuList=new ArrayList<String>();
		   PreparedStatement ps = null;
		   ResultSet rs = null;
		   Set<String> matIds = new HashSet<String>();
	       try {
	    	   String query=PRODUCT_FAMILY_SELECT_SQL;
	    	  
	    	   ps = conn.prepareStatement(query);
	    	   ps.setString(1,familyId);
	    	   ps.setString(1,familyId);
	    	   rs = ps.executeQuery();
	             while (rs.next()) {
	            	 matIds.add(rs.getString("PRODUCT_ID"));
	          	 
	             }
	             skuList= getSkuCodes(conn,matIds,familyId);
	       }catch(SQLException e){
	      	 throw e;
	       }finally{
	    	   if(rs != null) rs.close();
	    	   if(ps != null) ps.close();
	       }
	       LOGGER.info("matList :"+skuList.size());
	       
		   return skuList; 	
	
	}
	public static final String PRODUCT_FAMILY_SKU_SELECT_SQL= "SELECT DISTINCT p.sku_code FROM erps.product p, erps.materialproxy mpx, erps.material m,erps.product_family_master pm, "+
		   	  "(SELECT MAX(version) v, sap_id s FROM erps.material WHERE sap_id IN <XYZ> GROUP BY sap_id) T "+
		   	  " WHERE p.id = mpx.product_id AND  m.id= mpx.mat_id AND m.version=T.v AND M.SAP_ID=T.s and pm.FAMILY_ID=?";



	public static List<String> getSkuCodes(Connection conn, Set<String> matIds,
			String familyId) throws SQLException {
		List<String> skuList = new ArrayList<String>();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			if (matIds == null || matIds.size() == 0)
				return skuList;
			String query = PRODUCT_FAMILY_SKU_SELECT_SQL;
			StringBuilder buf = new StringBuilder(50);
			buf.append("(").append("'");
			Iterator<String> it = matIds.iterator();
			while (it.hasNext()) {
				String matId = it.next();
				buf.append(matId);
				buf.append("'");
				if (it.hasNext())
					buf.append(", '");
				else
					buf.append(") ");
			}
			query = query.replaceFirst("<XYZ>", buf.toString());
			ps = conn.prepareStatement(query);
			ps.setString(1, familyId);
			rs = ps.executeQuery();
			while (rs.next()) {
				String skuCode = rs.getString("SKU_CODE");
				skuList.add(skuCode);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
		LOGGER.info("matList :" + skuList.size());
		return skuList;
	}
	
}