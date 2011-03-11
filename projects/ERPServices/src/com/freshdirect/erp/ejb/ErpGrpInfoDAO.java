package com.freshdirect.erp.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Category;
import org.jaxen.function.StringFunction;

import weblogic.auddi.util.Logger;

import com.freshdirect.customer.EnumZoneServiceType;
import com.freshdirect.customer.ErpGrpPriceModel;
import com.freshdirect.customer.ErpGrpPriceZoneModel;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.customer.ErpZoneRegionInfo;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ErpGrpInfoDAO {


    /** logger for messages
     */
    private static Category LOGGER = LoggerFactory.getInstance( ErpGrpInfoDAO.class );
	
    
    private static final String ALL_GRP_PRICING_SELECT_SQL="SELECT MAX(VERSION) VERSION,SAP_ID  FROM ERPS.GRP_SCALE_MASTER GROUP BY (SAP_ID) ";
   	
	public static Collection<FDGroup> getAllGroupIds(Connection con) throws SQLException{
		
		   Connection conn = con;
		   PreparedStatement ps = null;
		   ResultSet rs = null;
		   List<FDGroup> groups =new ArrayList<FDGroup>();	
	       try {
	    	   ps = conn.prepareStatement(ALL_GRP_PRICING_SELECT_SQL);
	    	   rs = ps.executeQuery();
	             while (rs.next()) {
	            	 groups.add(new FDGroup(rs.getString("SAP_ID"), rs.getInt("VERSION")));	            	 
	             }
	       }catch(SQLException e){
	      	 throw e;
	       }finally{
	    	   if(rs != null) rs.close();
	    	   if(ps != null) ps.close();
	       }
 	       Logger.info("All Grp Id List :"+groups.size());
		   return groups; 
	}
    
	
	private static final String GRP_PRICING_SELECT_MAT_SQL="select  SAP_ID , VERSION from ERPS.GRP_SCALE_MASTER G "+
															"where ID IN "+ 
															"(select distinct GRP_ID from ERPS.MATERIAL_GRP where mat_id=?) "+
															"and version=(select max(version) version from ERPS.GRP_SCALE_MASTER where sap_id=G.SAP_ID) "+ 
															"and ACTIVE='X'"; 


	public static FDGroup getGroupIdentityForMaterial(Connection conn,String matId) throws SQLException{
		FDGroup group=null;
	   PreparedStatement ps = null;
	   ResultSet rs = null;
		
		try {	    	   	    	   	    	   
			ps = conn.prepareStatement(GRP_PRICING_SELECT_MAT_SQL);
			ps.setString(1,matId);
			//ps.setString(2,matId);
			rs = ps.executeQuery();	    	   
			if(rs.next()) {
				String sapId=rs.getString("SAP_ID");
				int version=rs.getInt("VERSION");
				group = new FDGroup(sapId, version);
			}
		}catch(SQLException e){
			throw e;
		}finally{
	    	   if(rs != null) rs.close();
	    	   if(ps != null) ps.close();
	    }
 	    
		Logger.debug("Group ID for loading matId:"+matId+" : and FDGroup: "+group);
		return group; 			
}
	
	private static final String GRP_PRICING_SELECT_SQL="select  ID ,SAP_ID   ,SHORT_DESC  ,LONG_DESC  ,ACTIVE "+   
    													" from  ERPS.GRP_SCALE_MASTER where sap_id=? and version=?"; 
	


	public static ErpGrpPriceModel getGrpInfo(Connection conn,String grpId, int version) throws SQLException{
				ErpGrpPriceModel groupInfo=null;
			   PreparedStatement ps = null;
			   ResultSet rs = null;
				try {	    	   	    	   	    	   
					ps = conn.prepareStatement(GRP_PRICING_SELECT_SQL);
					ps.setString(1,grpId);
					ps.setInt(2,version);
					rs = ps.executeQuery();	    	   
					if(rs.next()) {
						String sapId=rs.getString("SAP_ID");
						String shortDesc=rs.getString("SHORT_DESC");
						String longDesc=rs.getString("LONG_DESC");
						boolean isActive="X".equalsIgnoreCase(rs.getString("ACTIVE"));
						groupInfo=new ErpGrpPriceModel(sapId,longDesc,shortDesc,isActive);
						groupInfo.setId(rs.getString("ID"));					
						groupInfo.setVersion(version);
						groupInfo.setZoneModelList(getGrpPricingInfo(conn, groupInfo.getId(), version));  
						Set<String> matIds = getGrpMatInfoDetails(conn, groupInfo.getId(), version);
						groupInfo.setMatList(matIds);
						//Populate skus assoaciated with Mat List.
						groupInfo.setSkuList(getSkuCodes(conn, matIds));
					
					}
				}catch(SQLException e){
					throw e;
				}finally{
			    	   if(rs != null) rs.close();
			    	   if(ps != null) ps.close();
			    }
				Logger.debug("getGrpInfo ErpGrpPriceModel :"+groupInfo);
				return groupInfo; 			
		}
	
	
	
	public static final String GRP_PRICING_ZONE_SELECT_SQL=" select ID  ,GRP_ID ,ZONE_ID ,QUANTITY ,PRICING_UNIT ,SCALE_UNIT,PRICE ,VERSION FROM ERPS.GRP_PRICING "+
															" WHERE GRP_ID=? AND VERSION= ?";
	
	public static Set<ErpGrpPriceZoneModel> getGrpPricingInfo(Connection con,String grpId,int version) throws SQLException{
		   Connection conn = con;
		   PreparedStatement ps = null;
		   ResultSet rs = null;
		   Set<ErpGrpPriceZoneModel> zoneList=new HashSet<ErpGrpPriceZoneModel>();
	       try {
	    	   ps = conn.prepareStatement(GRP_PRICING_ZONE_SELECT_SQL);
	    	   ps.setString(1, grpId);	    	   
	    	   ps.setInt(2,version);
	    	   rs = ps.executeQuery();
	             while (rs.next()) {
	            	 
	             
	            	 String id=rs.getString("ID");
	            	 //String grpId=rs.getString("GRP_ID");
	            	 String zoneId=rs.getString("ZONE_ID");
	            	 double quantity=rs.getDouble("QUANTITY");
	            	 String unitOfMeasure=rs.getString("PRICING_UNIT");
	            	 String scaleUnit=rs.getString("SCALE_UNIT");	
	            	 double price=rs.getDouble("PRICE");
	            	 ErpGrpPriceZoneModel  grpPriceZoneInfo=new ErpGrpPriceZoneModel(zoneId,quantity,unitOfMeasure,price, scaleUnit);	            	 
	            	 grpPriceZoneInfo.setId(id);
	            	 zoneList.add(grpPriceZoneInfo);
	             }
	       }catch(SQLException e){
	      	 throw e;
	       }finally{
	    	   if(rs != null) rs.close();
	    	   if(ps != null) ps.close();
	       }
	       Logger.info("zoneList :"+zoneList.size());
		   return zoneList; 	
	}
	
	
	
	private static final String GRP_PRICING_SKU_EXISTS="select  mpx.mat_id, mpx.version version,ptmp.sku_code sku_code from ( "+
														" select max(to_number(id)) id, max(version) version, max(sku_code) sku_code from erps.product p "+  
														" where p.sku_code in ('{0}')  group by p.sku_code "+  
														" ) ptmp, erps.materialproxy mpx "+
														" where ptmp.id = mpx.product_id and ptmp.version=mpx.version and mpx.mat_id not in "+
														" ( select max(to_number(m.id)) id from erps.material m, erps.material_grp mg,"+
														" ( select max(version) version, max(to_number(id)) id, max(sap_id) sap_id  from erps.grp_scale_master  where active ='X' group by sap_id "+
														")  tmp  where tmp.id=mg.grp_id and mg.mat_id=m.sap_id  group by m.sap_id )";
	
	

	
   public static Collection getFilteredSkus(Connection con,List skus) throws SQLException{
	   
	   Connection conn = con;
	   PreparedStatement ps = null;
	   ResultSet rs = null;
	   Set zoneList=new HashSet();
       try {
    	   
    	   StringBuilder builder = new StringBuilder();
    	   int i=0;
    	   String skuCode="";
    	   Iterator iterator=skus.iterator();
           while(iterator.hasNext()){
           	FDSku model=(FDSku)iterator.next();        	
           	i=i+1;
           	if(i==1){
           		skuCode=model.getSkuCode();
           		builder.append(skuCode);
   				if(i!=skus.size()) builder.append("',"); 
   			}
   			else{
   				if(i==skus.size())	
   				{
   					builder.append("'").append(model.getSkuCode());					
   				}
   				else{
   					builder.append("'").append(model.getSkuCode()).append("',");					
   				}				
   			}        	        	           	
           }
    	   
           System.out.println("builder :"+builder.toString());
           
           //String sql=GRP_PRICING_SKU_EXISTS.format("$$", builder.toString());
    	   
           MessageFormat form = new MessageFormat(GRP_PRICING_SKU_EXISTS);
           String sql=form.format(new String[]{builder.toString()});                                            
           if(sql.indexOf("{0}")!=-1)
           {           	
           	MessageFormat form1 = new MessageFormat(sql);
           	sql=form1.format(new String[]{builder.toString()});        	
           }	
           System.out.println("final sql :"+sql);
                   
           
    	   ps = conn.prepareStatement(sql);
    	   Set<String> removedSkus=new HashSet();
    	   rs = ps.executeQuery();
             while (rs.next()) {
            	              
            	 String skuCde=rs.getString("sku_code");
            	 //String grpId=rs.getString("GRP_ID");
            	 int version=rs.getInt("version");
            	 //FDSku sku=new FDSku(skuCde,version);
            	 removedSkus.add(skuCode);
             }
             
             if(removedSkus.size()>0)
             {
            	 Iterator iteratorNew=skus.iterator();
                 while(iteratorNew.hasNext()){
                 	FDSku model=(FDSku)iterator.next();
                 	if(!removedSkus.contains(model.getSkuCode())){
                 		System.out.println("removing sku codes :"+model.getSkuCode());
                 		iterator.remove();                 		
                 	}
                 }	
             }
       }catch(SQLException e){
      	 throw e;
       }finally{
    	   if(rs != null) rs.close();
    	   if(ps != null) ps.close();
       }
       Logger.info("sku size :"+skus.size());
	   return skus;
	   
   }
	
	
	
	public static final String GRP_PRICING_MAT_SELECT_SQL=" SELECT ID ,  MAT_ID ,  GRP_ID , VERSION FROM ERPS.MATERIAL_GRP WHERE GRP_ID=? and VERSION=? ";
															
	
	public static Set getGrpMatInfoDetails(Connection con,String grpId,int version) throws SQLException{
		   Connection conn = con;
		   PreparedStatement ps = null;
		   ResultSet rs = null;
		   Set matList=new HashSet();
	       try {
	    	   ps = conn.prepareStatement(GRP_PRICING_MAT_SELECT_SQL);
	    	   ps.setString(1, grpId);	    	   
	    	   ps.setInt(2,version);
	    	   rs = ps.executeQuery();
	             while (rs.next()) {
	            	 
	             
	            	 String matId=rs.getString("MAT_ID");	            
	            	 matList.add(matId);
	             }
	       }catch(SQLException e){
	      	 throw e;
	       }finally{
	    	   if(rs != null) rs.close();
	    	   if(ps != null) ps.close();
	       }
	       Logger.info("matList :"+matList.size());
		   return matList; 	
	}
	
	public static final String GRP_PRICING_SKU_SELECT_SQL=
	 "select  p.sku_code "+
	 "from erps.product p, erps.materialproxy mpx, erps.material m "+
	 "where p.id = mpx.product_id and mpx.mat_id = m.id "+
	 "and p.version = (select max(version) from erps.product p2 where p2.sku_code = p.sku_code) ";
	 //"and m.sap_id in ('000000000200200366', '000000000200200367', '000000000200200373') "+  
	 //"order by M.SAP_ID,P.ID";
															
	
	public static List<String> getSkuCodes(Connection conn, Set<String> matIds) throws SQLException{
		   List<String> skuList=new ArrayList<String>();
		   PreparedStatement ps = null;
		   ResultSet rs = null;
		   
	       try {
	    	   if(matIds == null || matIds.size() == 0) return skuList;
	    	   StringBuffer buf = new StringBuffer(GRP_PRICING_SKU_SELECT_SQL);
	    	   buf.append("and m.sap_id in (").append("'");
	    	   Iterator<String> it =  matIds.iterator();
	    	   while(it.hasNext()){
	    		   String matId = it.next();
	    		   buf.append(matId);
	    		   buf.append("'");
	    		   if(it.hasNext()) 
	    			   buf.append(", '");
	    		   else
	    			   buf.append(") order by m.sap_id,p.id");
	    	   }
	    	   ps = conn.prepareStatement(buf.toString());
	    	   rs = ps.executeQuery();
	             while (rs.next()) {
	            	 String skuCode=rs.getString("SKU_CODE");	            
	            	 skuList.add(skuCode);
	             }
	       }catch(SQLException e){
	      	 throw e;
	       }finally{
	    	   if(rs != null) rs.close();
	    	   if(ps != null) ps.close();
	       }
	       Logger.info("matList :"+skuList.size());
		   return skuList; 	
	}
	
	public static final String GET_LATEST_VERSION_NUM =" SELECT max(version)latest from erps.GRP_SCALE_MASTER where sap_id = ?";

	 public static int getLatestVersionNumber(Connection con, String grpId) throws SQLException {
		   Connection conn = con;
		   PreparedStatement ps = null;
		   ResultSet rs = null;
		   
		   int version = -1;
	       try {
	    	   ps = conn.prepareStatement(GET_LATEST_VERSION_NUM);
	    	   ps.setString(1, grpId);	    	   
	    	   rs = ps.executeQuery();
	             while (rs.next()) {
	            	 version = rs.getInt("LATEST");	            
	             }
	       }catch(SQLException e){
	      	 throw e;
	       }finally{
	    	   if(rs != null) rs.close();
	    	   if(ps != null) ps.close();
	       }
	       Logger.info("Latest Version of Group ID : "+grpId);
		   return version; 		 
	 }
	 
	private static final String ALL_GRPS_FOR_MAT_ID_SQL=
		"SELECT distinct GSM.SAP_ID, max(GSM.VERSION) VERSION " + 
			 "FROM  ERPS.MATERIAL_GRP mg, ERPS.GRP_SCALE_MASTER gsm " +
			"WHERE  MG.MAT_ID = ? " +
			   "AND MG.GRP_ID = GSM.ID " +
			   "AND GSM.ACTIVE = 'X' " +
			   "group by sap_id";
	   	
	public static Collection<FDGroup> getAllGroupsForMaterial(Connection con, String matId) throws SQLException {
		Connection conn = con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<FDGroup> groups = new ArrayList<FDGroup>();
		try {
			ps = conn.prepareStatement(ALL_GRPS_FOR_MAT_ID_SQL);
			ps.setString(1, matId);
			rs = ps.executeQuery();
			while (rs.next()) {
				groups.add(new FDGroup(rs.getString("SAP_ID"), rs
						.getInt("VERSION")));
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
		return groups;
	}
}
