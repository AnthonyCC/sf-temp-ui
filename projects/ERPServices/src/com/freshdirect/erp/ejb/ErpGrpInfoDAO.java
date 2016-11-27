package com.freshdirect.erp.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Category;

import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.customer.ErpGrpPriceModel;
import com.freshdirect.customer.ErpGrpPriceZoneModel;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.SalesAreaInfo;
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
 	       LOGGER.info("All Grp Id List :"+groups.size());
		   return groups; 
	}
    
	
	private static final String GRP_PRICING_SELECT_MAT_SQL="SELECT  DISTINCT SAP_ID , g.VERSION,NVL(GP.SALES_ORG,'0001') SALES_ORG, NVL(GP.DISTRIBUTION_CHANNEL,'01') DISTRIBUTION_CHANNEL FROM ERPS.GRP_SCALE_MASTER G, erps.grp_pricing gp "+ 
            " WHERE g.ID IN "+
            " (select distinct GRP_ID from ERPS.MATERIAL_GRP where mat_id=?) "+
            " AND g.version=(SELECT MAX(version) version FROM ERPS.GRP_SCALE_MASTER WHERE sap_id=G.SAP_ID) "+
            " AND ACTIVE='X'      and gp.grp_id=g.id ";

	private static final String GRP_PRICING_SELECT_MAT_SQL_NEW="SELECT DISTINCT SAP_ID , g.VERSION,NVL(GP.SALES_ORG,'0001') SALES_ORG, NVL(GP.DISTRIBUTION_CHANNEL,'01') DISTRIBUTION_CHANNEL FROM "+
	       " (select grp_id,version from erps.material_grp mg where mat_id=?) A, ERPS.GRP_SCALE_MASTER G, erps.grp_pricing gp "+
	       " WHERE A.grp_id=g.id and a.version=g.version and g.id=GP.GRP_ID and g.version=(select max(version) from ERPS.GRP_SCALE_MASTER WHERE sap_id=g.sap_id) and g.active='X' and g.version=gp.version ";

	public static Map<String,FDGroup> getGroupIdentityForMaterial(Connection conn,String matId) throws SQLException{
		FDGroup group=null;
	   PreparedStatement ps = null;
	   ResultSet rs = null;
	   int resultCount = 0;	
	   Map<String,FDGroup> groups=new HashMap<String,FDGroup>();
	   String key="";
		try {	    	   	    	   	    	   
			ps = conn.prepareStatement(GRP_PRICING_SELECT_MAT_SQL_NEW);
			ps.setString(1,matId);
			//ps.setString(2,matId);
			rs = ps.executeQuery();	 
			while (rs.next()) {
				String sapId=rs.getString("SAP_ID");
				int version=rs.getInt("VERSION");
				key=rs.getString("SALES_ORG")+"-"+rs.getString("DISTRIBUTION_CHANNEL");
				group = new FDGroup(sapId, version);
				if(groups.containsKey(key))
					resultCount++;
				else 
					groups.put(key, group);
			}
		}catch(SQLException e){
			throw e;
		}finally{
	    	   if(rs != null) rs.close();
	    	   if(ps != null) ps.close();
	    }
 	    if(resultCount > 1){
 	    	LOGGER.error("There are multiple active groups assciated with the same Material Number : "+matId);
 	    	//Do not associate the group info with the material/product.
 	    	return null;
 	    }
		LOGGER.debug("Group ID for loading matId:"+matId+" : and FDGroups: "+groups);
		return groups; 			
	}
	
	private static final String GRP_PRICING_SELECT_MAT_SALES_AREA_SQL="select  SAP_ID , g.VERSION,gp.sales_org,GP.DISTRIBUTION_CHANNEL from ERPS.GRP_SCALE_MASTER g, ERPS.GRP_PRICING gp" 
                                                           +" where g.id=gp.grp_id and g.ID IN"  
                                                           +" (select distinct GRP_ID from ERPS.MATERIAL_GRP where mat_id=?)" 
                                                           +" and g.version=(select max(version) version from ERPS.GRP_SCALE_MASTER where sap_id=G.SAP_ID)"  
                                                           +" and ACTIVE='X'"; 
	public static Map<SalesAreaInfo, FDGroup> getGroupIdentitiesForMaterial(Connection conn,String matId) throws SQLException{
		Map<SalesAreaInfo, FDGroup> salesAreaGroups = new HashMap<SalesAreaInfo, FDGroup>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {	    	   	    	   	    	   
			ps = conn.prepareStatement(GRP_PRICING_SELECT_MAT_SALES_AREA_SQL);
			ps.setString(1,matId);
			//ps.setString(2,matId);
			rs = ps.executeQuery();	 
			while (rs.next()) {				
				String sapId=rs.getString("SAP_ID");
				int version=rs.getInt("VERSION");
				FDGroup group = new FDGroup(sapId, version);
				String salesOrg = rs.getString("SALES_ORG");
				String distChannel = rs.getString("DISTRIBUTION_CHANNEL");
				SalesAreaInfo salesArea = new SalesAreaInfo(salesOrg, distChannel);
				if(!salesAreaGroups.containsKey(salesArea)){
					salesAreaGroups.put(salesArea, group);
				}
			}
		}catch(SQLException e){
			throw e;
		}finally{
	    	   if(rs != null) rs.close();
	    	   if(ps != null) ps.close();
	    }
		LOGGER.debug("Group ID for loading matId:"+matId+" : and FDGroup: "+salesAreaGroups);
		return salesAreaGroups; 			
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
				LOGGER.debug("getGrpInfo ErpGrpPriceModel :"+groupInfo);
				return groupInfo; 			
		}
	
	
	
	public static final String GRP_PRICING_ZONE_SELECT_SQL=" select ID  ,GRP_ID ,ZONE_ID ,QUANTITY ,PRICING_UNIT ,SCALE_UNIT,PRICE ,VERSION, NVL(SALES_ORG,'0001') as SALES_ORG,NVL(DISTRIBUTION_CHANNEL,'01') as DISTRIBUTION_CHANNEL FROM ERPS.GRP_PRICING "+
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
	            	 String salesOrg = rs.getString("SALES_ORG");
	            	 String distChannel = rs.getString("DISTRIBUTION_CHANNEL");
	            	 if(salesOrg==null)salesOrg="0001";
	            	 if(distChannel==null)distChannel="01";
	            	 ErpGrpPriceZoneModel  grpPriceZoneInfo=new ErpGrpPriceZoneModel(new ZoneInfo(zoneId,salesOrg, distChannel),quantity,unitOfMeasure,price, scaleUnit);	            	 
	            	 grpPriceZoneInfo.setId(id);
	            	 zoneList.add(grpPriceZoneInfo);
	             }
	       }catch(SQLException e){
	      	 throw e;
	       }finally{
	    	   if(rs != null) rs.close();
	    	   if(ps != null) ps.close();
	       }
	       LOGGER.info("zoneList :"+zoneList.size());
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
       LOGGER.info("sku size :"+skus.size());
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
	       LOGGER.info("matList :"+matList.size());
		   return matList; 	
	}
	
	
	public static final String GRP_PRICING_SKU_SELECT_SQL="SELECT  SKUCODE from erps.material m, "+
    " (SELECT MAX(version) v,  sap_id FROM erps.material WHERE sap_id IN <XYZ> GROUP BY sap_id) T "+ 
   " WHERE T.v=m.version and T.sap_id=M.SAP_ID ";
	
	public static List<String> getSkuCodes(Connection conn, Set<String> matIds) throws SQLException{
		   List<String> skuList=new ArrayList<String>();
		   PreparedStatement ps = null;
		   ResultSet rs = null;
		   
	       try {
	    	   if(matIds == null || matIds.size() == 0) return skuList;
	    	   String query=GRP_PRICING_SKU_SELECT_SQL;
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
	       LOGGER.info("Latest Version of Group ID : "+grpId);
		   return version; 		 
	 }
	 
	private static final String ALL_GRPS_FOR_MAT_ID_SQL=
		"SELECT DISTINCT GSM.SAP_ID, MAX (GSM.VERSION) VERSION " +
		"FROM ERPS.MATERIAL_GRP mg, ERPS.GRP_SCALE_MASTER gsm " +
		"WHERE     MG.MAT_ID in (select sap_id from erps.material where ID in ( " +
                                    "SELECT mat_id FROM erps.materialproxy mp WHERE product_id in " + 
                                      "(select id from (select * from erps.product where sku_code = ? order by version desc) where rownum =1) " + 
                                          "and mp.version in " +
                                             "(select version from (select * from erps.product where sku_code = ? order by version desc) where rownum =1) " +
                                      ")       ) " +
         "AND MG.GRP_ID = GSM.ID " +
         "AND GSM.ACTIVE = 'X' " +
         "GROUP BY sap_id";
	   	
	public static Collection<FDGroup> getAllGroupsForMaterial(Connection con, String matId) throws SQLException {
		Connection conn = con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<FDGroup> groups = new ArrayList<FDGroup>();
		try {
			ps = conn.prepareStatement(ALL_GRPS_FOR_MAT_ID_SQL);
			ps.setString(1, matId);
			ps.setString(2, matId);
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
	public static final String GET_LATEST_ACTIVE_GROUP="select SAP_ID,VERSION from erps.grp_scale_master gcm where GCM.SAP_ID=? and "+ 
	" version=(select max(version) from  erps.grp_scale_master gcm where GCM.SAP_ID=?) and gcm.active='X' ";
	public static FDGroup getLatestActiveGroup(Connection con,String group) throws SQLException{
		Connection conn = con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		FDGroup _group=null;
		try {
			ps = conn.prepareStatement(GET_LATEST_ACTIVE_GROUP);
			ps.setString(1, group);
			ps.setString(2, group);
			
			rs = ps.executeQuery();
			if (rs.next()) {
				 _group=new FDGroup( rs.getString("SAP_ID"),rs.getInt("VERSION"));
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
		return _group;
	}
	
	
	public static final String GET_LATEST_GROUPS_MATERIALS_MODIFIED =" select gsm.sap_id,gsm.version,GH.DATE_CREATED,mg.mat_id from ERPS.GRP_SCALE_MASTER gsm,ERPS.MATERIAL_GRP mg, (select gh.version,gh.date_created from ERPS.GRP_HISTORY gh where GH.DATE_CREATED > ?)gh"
              +" where gsm.version=(select max(gsm1.version) from ERPS.GRP_SCALE_MASTER gsm1 where GSM1.SAP_ID=GSM.SAP_ID and gsm1.version=gh.version)"
              +" and gsm.version=gh.version and MG.GRP_ID=gsm.id order by gsm.sap_id,mg.mat_id ";
	
	public static final String GET_ALL_GROUPS_MATERIALS =" select gsm.sap_id,gsm.version,GH.DATE_CREATED,mg.mat_id from ERPS.GRP_SCALE_MASTER gsm,ERPS.MATERIAL_GRP mg, ERPS.GRP_HISTORY gh"
            +" where gsm.version=(select max(gsm1.version) from ERPS.GRP_SCALE_MASTER gsm1 where GSM1.SAP_ID=GSM.SAP_ID) and gsm.active='X' "
            +" and gsm.version=gh.version and MG.GRP_ID=gsm.id order by gsm.sap_id,mg.mat_id ";
	
	public static Map<String,List<String>> getModifiedOnlyGroups(Connection con,Date lastModified) throws SQLException{
		Connection conn = con;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String,List<String>> map = new HashMap<String,List<String>>();
		
		try {
			if(null != lastModified){
				ps = conn.prepareStatement(GET_LATEST_GROUPS_MATERIALS_MODIFIED);
				ps.setTimestamp(1, new Timestamp(lastModified.getTime()));
			}else{
				ps = conn.prepareStatement(GET_ALL_GROUPS_MATERIALS);
			}
			
			rs = ps.executeQuery();
			while (rs.next()) {
//					FDGroup group=new FDGroup( rs.getString("SAP_ID"),rs.getInt("VERSION"),rs.getTime("DATE_CREATED"));
				String groupId = rs.getString("SAP_ID");
				String material = rs.getString("MAT_ID");
				List<String> grpMaterials = map.get(groupId);
				if(null == grpMaterials){
					grpMaterials = new ArrayList<String>();
					map.put(groupId, grpMaterials);
				}
				if(!grpMaterials.contains(material)){
					grpMaterials.add(material);
				}
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
		return map;
	}
}
