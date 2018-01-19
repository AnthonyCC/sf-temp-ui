package com.freshdirect.erp.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpProductFamilyModel;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ErpProductFamilyDAO {
	
	/** logger for messages
     */
    private static Category LOGGER = LoggerFactory.getInstance( ErpProductFamilyDAO.class );
	
	private static final String FML_ID_SELECT_MAT_SQL="select  FAMILY_ID from ERPS.PRODUCT_FAMILY_MASTER PF where PRODUCT_ID = ? "+
													"and VERSION=(Select max(VERSION) version from ERPS.PRODUCT_FAMILY_MASTER  where FAMILY_ID=PF.FAMILY_ID) and ACTIVE <> 'D'"; 
    
	public static String getFamilyIdForMaterial(Connection conn,String matId) throws SQLException{

    	String flyId=null;
	   PreparedStatement ps = null;
	   ResultSet rs = null;
	   int resultCount = 0;	
		try {	    	   	    	   	    	   
			ps = conn.prepareStatement(FML_ID_SELECT_MAT_SQL);
			ps.setString(1,matId);
			//ps.setString(2,matId);
			rs = ps.executeQuery();	 
			while (rs.next()) {
				 flyId=rs.getString("FAMILY_ID");
				resultCount++;
			}
		}catch(SQLException e){
			throw e;
		}finally{
	    	   if(rs != null) rs.close();
	    	   if(ps != null) ps.close();
	    }
 	    if(resultCount > 1){
 	    	LOGGER.error("There are multiple active groups assciated with the same Material Number : "+matId);
 	    	//Do not associate the family info with the material/product.
 	    	return null;
 	    }
		LOGGER.debug("Family ID for loading matId:"+matId+" : and Family ID: "+flyId);
		return flyId; 			

    }
	
	
	private static final String PRODUCT_FAMILY_INFO_SELECT_SQL="select product_id from ERPS.PRODUCT_FAMILY_MASTER a where (a.active is null OR a.active <>'D') and (a.GROUP_STAT is null OR a.GROUP_STAT <>'X') and a.version= (select MAX(version) from ERPS.PRODUCT_FAMILY_MASTER where FAMILY_ID=?) and a.FAMILY_ID=?";
	
	public static ErpProductFamilyModel getFamilyInfo(Connection conn,String familyId) throws SQLException {
		ErpProductFamilyModel familyInfo = new ErpProductFamilyModel();
		 PreparedStatement ps = null;
		   ResultSet rs = null;
		   Set<String> matIds = new HashSet<String>();
			try {	    	   	
				ps = conn.prepareStatement(PRODUCT_FAMILY_INFO_SELECT_SQL);
				ps.setString(1,familyId);
				ps.setString(2,familyId);
				rs = ps.executeQuery();
				while(rs.next()) {
					matIds.add(rs.getString("PRODUCT_ID"));
				}
				familyInfo.setFamilyId(familyId);
				familyInfo.setMatList(matIds);
				familyInfo.setSkuList(getSkuCodes(conn,matIds,familyId));
				}
			catch(SQLException e){
				throw e;
			}finally{
		    	   if(rs != null) rs.close();
		    	   if(ps != null) ps.close();
		    }
			LOGGER.debug("getfamilyInfo ProductFamilyModel :"+familyInfo);
			return familyInfo; 	
			}
private static final String PRODUCT_FAMILY_INFO_WITH_MATID_SQL="select Family_id, product_id from ERPS.PRODUCT_FAMILY_MASTER a where (a.active is null OR a.active <>'D') and (a.GROUP_STAT is null OR a.GROUP_STAT <>'X') and a.version= (select MAX(version) from ERPS.PRODUCT_FAMILY_MASTER where PRODUCT_ID=?)";
	
	public static ErpProductFamilyModel getSkuFamilyInfo(Connection conn,String materialId) throws SQLException {
		ErpProductFamilyModel familyInfo = new ErpProductFamilyModel();
		 PreparedStatement ps = null;
		   ResultSet rs = null;
		   Set<String> matIds = new HashSet<String>();
		   String familyId = null;
			try {	    	   	
				ps = conn.prepareStatement(PRODUCT_FAMILY_INFO_WITH_MATID_SQL);
				ps.setString(1,materialId);
				rs = ps.executeQuery();
				while(rs.next()) {
					familyId = rs.getString("Family_id");
					matIds.add(rs.getString("product_id"));
				}
				familyInfo.setFamilyId(familyId);
				familyInfo.setMatList(matIds);
				if(familyId!=null)
				familyInfo.setSkuList(getSkuCodes(conn,matIds,familyId));
				}
			catch(SQLException e){
				throw e;
			}finally{
		    	   if(rs != null) rs.close();
		    	   if(ps != null) ps.close();
		    }
			LOGGER.debug("getSkuFamilyInfo ProductFamilyModel :"+materialId);
			return familyInfo; 	
			}
	
	
	
	public static final String PRODUCT_FAMILY_SKU_SELECT_SQL= "SELECT DISTINCT M.SKUCODE FROM  erps.material m, erps.product_family_master pm, "+
														   	  "(SELECT MAX(version) v, sap_id s FROM erps.material WHERE sap_id IN <XYZ> GROUP BY sap_id) T "+
														   	  " WHERE  m.version=T.v AND M.SAP_ID=T.s and pm.FAMILY_ID=?";
	
	
	
	public static List<String> getSkuCodes(Connection conn, Set<String> matIds,String familyId) throws SQLException{
		   List<String> skuList=new ArrayList<String>();
		   PreparedStatement ps = null;
		   ResultSet rs = null;
		   
	       try {
	    	   if(matIds == null || matIds.size() == 0) return skuList;
	    	   String query=PRODUCT_FAMILY_SKU_SELECT_SQL;
	    	   StringBuilder buf=new StringBuilder(50);
	    	   buf.append("(").append("'");
	    	   Iterator<String> it =  matIds.iterator();
	    	   while(it.hasNext()){
	    		   String matId = it.next();
	    		   buf.append(matId);
	    		   buf.append("'");
	    		   if(it.hasNext()) 
	    			   buf.append(", '");
	    		   else
	    			   buf.append(") ");
	    	   }
	    	   query=query.replaceFirst("<XYZ>", buf.toString());
	    	   ps = conn.prepareStatement(query);
	    	   ps.setString(1,familyId);
	    	   rs = ps.executeQuery();
	             while (rs.next()) {
	            	 String skuCode=rs.getString("SKUCODE");	            
	            	 skuList.add(skuCode);
	             }
	       }catch(SQLException e){
	      	 throw e;
	       }finally{
	    	   if(rs != null) rs.close();
	    	   if(ps != null) ps.close();
	       }
	       LOGGER.info("matList :"+skuList.size());
		   return skuList; 	
	}
	
	
}

	
    
															
    
