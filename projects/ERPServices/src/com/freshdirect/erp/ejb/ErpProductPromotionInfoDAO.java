package com.freshdirect.erp.ejb;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import com.freshdirect.common.pricing.ZoneInfo;
import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.fdstore.SalesAreaInfo;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ErpProductPromotionInfoDAO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1850046746832219296L;

	private static Category LOGGER = LoggerFactory.getInstance( ErpProductPromotionInfoDAO.class );
	
	private static final String COLUMN_ID="ID";
	private static final String COLUMN_ZONE_ID="ZONE_ID";
	private static final String COLUMN_VERSION="VERSION";
	private static final String COLUMN_MAT_NUM="MAT_NUM";
	private static final String COLUMN_SKU_CODE="SKU_CODE";
	private static final String COLUMN_PRIORITY="PRIORITY";
	private static final String COLUMN_FEATURED="FEATURED";
	private static final String COLUMN_FEATURED_HEADER="FEATURED_HEADER";
	private static final String COLUMN_TYPE="TYPE";
	private static final String COLUMN_ERP_CATEGORY="ERP_CATEGORY";
	private static final String COLUMN_ERP_CAT_POSITION="ERP_CAT_POSITION";
	private static final String COLUMN_ERP_PP_ID="ERP_PP_ID";
	private static final String COLUMN_SALES_ORG="SALES_ORG";
	private static final String COLUMN_DISTRIBUTION_CHANNEL="DISTRIBUTION_CHANNEL";
	
	private static final String ALL_ZONES_PRODUCTS_SQL="select * from ERPS.PRODUCT_PROMOTION_GROUP ppg where ppg.version=(select max(ppg1.version) from ERPS.PRODUCT_PROMOTION_GROUP ppg1,ERPS.PRODUCT_PROMOTION_HISTORY pph where ppg1.version=pph.version and ppg1.type=? and pph.status='S') and ppg.type=? order by ppg.zone_id,  to_number(ppg.erp_cat_position),to_number(ppg.priority),ppg.featured ";
	private static final String ALL_ZONES_REFRESH_PRODUCTS_SQL="select * from ERPS.PRODUCT_PROMOTION_GROUP ppg where ppg.version=(select max(ppg1.version) from ERPS.PRODUCT_PROMOTION_GROUP ppg1,ERPS.PRODUCT_PROMOTION_HISTORY pph where ppg1.version=pph.version and ppg1.type=? and pph.status='S' and PPH.DATE_CREATED > ? ) and ppg.type=? order by ppg.zone_id, to_number(ppg.erp_cat_position),to_number(ppg.priority),ppg.featured ";
	/**
	 * Method to get latest products for all zones, by ppType.
	 * @param conn
	 * @param ppType
	 * @return
	 */	 
	public static Map<ZoneInfo,List<FDProductPromotionInfo>> getAllProductsByType(Connection conn, String ppType, Date lastPublishDate) throws SQLException{
		
		   PreparedStatement ps = null;
		   ResultSet rs = null;
		   Map<ZoneInfo,List<FDProductPromotionInfo>> zonePromoProdMap = new HashMap<ZoneInfo,List<FDProductPromotionInfo>>();
		   List<FDProductPromotionInfo> promotionProducts =null;;	
	       try {
	    	   if(lastPublishDate !=null){
	    		   ps = conn.prepareStatement(ALL_ZONES_REFRESH_PRODUCTS_SQL);
	    		   ps.setString(1,ppType);
	    		   ps.setTimestamp(2,new java.sql.Timestamp(lastPublishDate.getTime()));
		    	   ps.setString(3,ppType);
	    	   }else{
	    		   ps = conn.prepareStatement(ALL_ZONES_PRODUCTS_SQL);
	    		   ps.setString(1,ppType);
		    	   ps.setString(2,ppType);
	    	   }	    	   
	    	   rs = ps.executeQuery();
	           while (rs.next()) {
	            	 String zoneId = rs.getString(COLUMN_ZONE_ID);
	            	 SalesAreaInfo saleAreaInfo =new SalesAreaInfo(rs.getString(COLUMN_SALES_ORG), rs.getString(COLUMN_DISTRIBUTION_CHANNEL));
	            	 ZoneInfo zoneInfo =new ZoneInfo(zoneId,saleAreaInfo.getSalesOrg(),saleAreaInfo.getDistChannel());
	            	 boolean isFeatured = "X".equalsIgnoreCase(rs.getString(COLUMN_FEATURED))?true:false;
	            	 promotionProducts = zonePromoProdMap.get(zoneInfo);
	            	 if(null == promotionProducts){
	            		 promotionProducts = new ArrayList<FDProductPromotionInfo>();
	            		 zonePromoProdMap.put(zoneInfo, promotionProducts);
	            	 }
	            	 FDProductPromotionInfo promotionProduct = new FDProductPromotionInfo();
	            	 promotionProduct.setId(rs.getString(COLUMN_ID));
	            	 promotionProduct.setVersion(rs.getInt(COLUMN_VERSION));
	            	 promotionProduct.setZoneId(zoneId);
	            	 promotionProduct.setSkuCode(rs.getString(COLUMN_SKU_CODE));
	            	 promotionProduct.setMatNumber(rs.getString(COLUMN_MAT_NUM));
	            	 promotionProduct.setPriority(Integer.parseInt(rs.getString(COLUMN_PRIORITY)));
	            	 promotionProduct.setFeatured(isFeatured);
	            	 promotionProduct.setFeaturedHeader(rs.getString(COLUMN_FEATURED_HEADER));
	            	 promotionProduct.setType(rs.getString(COLUMN_TYPE));
	            	 promotionProduct.setErpCategory(rs.getString(COLUMN_ERP_CATEGORY));
	            	 promotionProduct.setErpCatPosition(null !=rs.getString(COLUMN_ERP_CAT_POSITION)?Integer.parseInt(rs.getString(COLUMN_ERP_CAT_POSITION)):0);
	            	 promotionProduct.setErpPromtoionId(rs.getString(COLUMN_ERP_PP_ID));
	            	 promotionProduct.setSalesArea(saleAreaInfo);
	            	 promotionProducts.add(promotionProduct);	            	 
	           }
	       }catch(SQLException e){
	      	 throw e;
	       }finally{
	    	   if(rs != null) rs.close();
	    	   if(ps != null) ps.close();
	       }
//	       Logger.info("No.of PromotionProducts :"+promotionProducts.size());
		return zonePromoProdMap;
	}
	
	private static final String ZONE_PRODUCTS_SQL="select * from ERPS.PRODUCT_PROMOTION_GROUP ppg where ppg.zone_id=? and ppg.version=(select max(ppg1.version) from ERPS.PRODUCT_PROMOTION_GROUP ppg1 where ppg1.type=?) and ppg.type=? order byto_number(ppg.priority) ";
	
	/**
	 * Method to get latest products by zoneId and by ppType.
	 * @param conn
	 * @param ppType
	 * @param zoneId
	 * @return
	 */
	public static List<FDProductPromotionInfo> getProductsByZoneAndType(Connection conn,String ppType, String zoneId) throws SQLException{
		PreparedStatement ps = null;
		   ResultSet rs = null;
		   List<FDProductPromotionInfo> promotionProducts =new ArrayList<FDProductPromotionInfo>();	
	       try {
	    	   ps = conn.prepareStatement(ZONE_PRODUCTS_SQL);
	    	   ps.setString(1,zoneId);
	    	   ps.setString(2,ppType);
	    	   ps.setString(3,ppType);
	    	   rs = ps.executeQuery();
	             while (rs.next()) {
	            	 boolean isFeatured = "X".equalsIgnoreCase(rs.getString(COLUMN_FEATURED))?true:false;	            	 
	            	 FDProductPromotionInfo promotionProduct = new FDProductPromotionInfo();
	            	 promotionProduct.setId(rs.getString(COLUMN_ID));
	            	 promotionProduct.setVersion(rs.getInt(COLUMN_VERSION));
	            	 promotionProduct.setZoneId(zoneId);
	            	 promotionProduct.setSkuCode(rs.getString(COLUMN_SKU_CODE));
	            	 promotionProduct.setMatNumber(rs.getString(COLUMN_MAT_NUM));
	            	 promotionProduct.setPriority(Integer.parseInt(rs.getString(COLUMN_PRIORITY)));
	            	 promotionProduct.setFeatured(isFeatured);
	            	 promotionProduct.setFeaturedHeader(rs.getString(COLUMN_FEATURED_HEADER));
	            	 promotionProduct.setType(rs.getString(COLUMN_TYPE));
	            	 promotionProduct.setErpCategory(rs.getString(COLUMN_ERP_CATEGORY));
	            	 promotionProduct.setErpCatPosition(Integer.parseInt(rs.getString(COLUMN_ERP_CAT_POSITION)));
	            	 promotionProduct.setSalesArea(new SalesAreaInfo(rs.getString(COLUMN_SALES_ORG), rs.getString(COLUMN_DISTRIBUTION_CHANNEL)));
	            	 promotionProducts.add(promotionProduct);		            	 
	             }
	       }catch(SQLException e){
	      	 throw e;
	       }finally{
	    	   if(rs != null) rs.close();
	    	   if(ps != null) ps.close();
	       }
	       LOGGER.info("No.of PromotionProducts :"+promotionProducts.size());
		return promotionProducts;
	}
	
	private static final String TYPE_ID_PRODUCTS_SQL="select ppg.* from ERPS.PRODUCT_PROMOTION_GROUP ppg, ERPS.PRODUCT_PROMOTION pp where pp.ERP_PP_ID=ppg.ERP_PP_ID and ppg.ERP_PP_ID=? and ppg.version=(select max(ppg1.version) from ERPS.PRODUCT_PROMOTION_GROUP ppg1 where ppg1.type=? and ppg1.ERP_PP_ID=pp.ERP_PP_ID) and ppg.type=? order byto_number(ppg.priority) ";
	
	
	private static final String ALL_PROMOTION_PRODUCTS_SQL="select * from ERPS.PRODUCT_PROMOTION_GROUP ppg"+
			" where ppg.version=(select max(pp.version) from ERPS.PRODUCT_PROMOTION pp, ERPS.PRODUCT_PROMOTION_HISTORY pph where PPh.VERSION=pp.version and pph.status='S' and pp.type=? and PP.ERP_PP_ID=ppg.erp_pp_id and PP.END_DATE >= sysdate)"+ 
			" and ppg.type=? order by ppg.zone_id,  to_number(ppg.erp_cat_position),to_number(ppg.priority),ppg.featured ";
	
	private static final String ALL_PROMOTION_REFRESH_PRODUCTS_SQL="select * from ERPS.PRODUCT_PROMOTION_GROUP ppg"+
			" where ppg.version=(select max(pp.version) from ERPS.PRODUCT_PROMOTION pp, ERPS.PRODUCT_PROMOTION_HISTORY pph where PPh.VERSION=pp.version and pph.status='S' and pp.type=? and PP.ERP_PP_ID=ppg.erp_pp_id and PPH.DATE_CREATED > ? and PP.END_DATE >= sysdate)"+ 
			" and ppg.type=? order by ppg.zone_id,  to_number(ppg.erp_cat_position),to_number(ppg.priority),ppg.featured ";
	
	/**
	 * Method to get latest products for all zones, by ppType.
	 * @param conn
	 * @param ppType
	 * @return
	 */	 
	public static Map<String,Map<ZoneInfo,List<FDProductPromotionInfo>>> getAllPromotionsByType(Connection conn, String ppType, Date lastPublishDate) throws SQLException{
		
		   PreparedStatement ps = null;
		   ResultSet rs = null;
		   Map<String,Map<ZoneInfo,List<FDProductPromotionInfo>>> promoProdMap = new HashMap<String,Map<ZoneInfo,List<FDProductPromotionInfo>>>();
		   List<FDProductPromotionInfo> promotionProducts =null;
		   Map<ZoneInfo,List<FDProductPromotionInfo>> promotionZoneProducts = null;
	       try {
	    	   if(lastPublishDate !=null){
	    		   ps = conn.prepareStatement(ALL_PROMOTION_REFRESH_PRODUCTS_SQL);
	    		   ps.setString(1,ppType);
	    		   ps.setTimestamp(2,new java.sql.Timestamp(lastPublishDate.getTime()));
		    	   ps.setString(3,ppType);
	    	   }else{
	    		   ps = conn.prepareStatement(ALL_PROMOTION_PRODUCTS_SQL);
	    		   ps.setString(1,ppType);
		    	   ps.setString(2,ppType);
	    	   }	    	   
	    	   rs = ps.executeQuery();
	           while (rs.next()) {
	            	 String ppId = rs.getString(COLUMN_ERP_PP_ID);
	            	 String zoneId = rs.getString(COLUMN_ZONE_ID);
	            	 SalesAreaInfo saleAreaInfo =new SalesAreaInfo(rs.getString(COLUMN_SALES_ORG), rs.getString(COLUMN_DISTRIBUTION_CHANNEL));
	            	 ZoneInfo zoneInfo =new ZoneInfo(zoneId,saleAreaInfo.getSalesOrg(),saleAreaInfo.getDistChannel());
	            	 boolean isFeatured = "X".equalsIgnoreCase(rs.getString(COLUMN_FEATURED))?true:false;
	            	 promotionZoneProducts = promoProdMap.get(ppId);
	            	 if(null ==promotionZoneProducts){
	            		 promotionZoneProducts = new HashMap<ZoneInfo,List<FDProductPromotionInfo>>();
	            		 promoProdMap.put(ppId, promotionZoneProducts);
	            	 }
	            	 
	            	 promotionProducts = promotionZoneProducts.get(zoneInfo);
	            	 if(null == promotionProducts){
	            		 promotionProducts = new ArrayList<FDProductPromotionInfo>();
	            		 promotionZoneProducts.put(zoneInfo, promotionProducts);
	            	 }
	            	 FDProductPromotionInfo promotionProduct = new FDProductPromotionInfo();
	            	 promotionProduct.setId(rs.getString(COLUMN_ID));
	            	 promotionProduct.setVersion(rs.getInt(COLUMN_VERSION));
	            	 promotionProduct.setZoneId(rs.getString(COLUMN_ZONE_ID));
	            	 promotionProduct.setSkuCode(rs.getString(COLUMN_SKU_CODE));
	            	 promotionProduct.setMatNumber(rs.getString(COLUMN_MAT_NUM));
	            	 promotionProduct.setPriority(Integer.parseInt(rs.getString(COLUMN_PRIORITY)));
	            	 promotionProduct.setFeatured(isFeatured);
	            	 promotionProduct.setFeaturedHeader(rs.getString(COLUMN_FEATURED_HEADER));
	            	 promotionProduct.setType(rs.getString(COLUMN_TYPE));
	            	 promotionProduct.setErpCategory(rs.getString(COLUMN_ERP_CATEGORY));
	            	 promotionProduct.setErpCatPosition(null !=rs.getString(COLUMN_ERP_CAT_POSITION)?Integer.parseInt(rs.getString(COLUMN_ERP_CAT_POSITION)):0);
	            	 promotionProduct.setErpPromtoionId(rs.getString(COLUMN_ERP_PP_ID));
	            	 promotionProduct.setSalesArea(saleAreaInfo);
	            	 promotionProducts.add(promotionProduct);	            	 
	           }
	       }catch(SQLException e){
	      	 throw e;
	       }finally{
	    	   if(rs != null) rs.close();
	    	   if(ps != null) ps.close();
	       }
//	       Logger.info("No.of PromotionProducts :"+promotionProducts.size());
		return promoProdMap;
	}
}
