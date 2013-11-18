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

import weblogic.auddi.util.Logger;

import com.freshdirect.fdstore.FDProductPromotionInfo;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ErpProductPromotionInfoDAO implements Serializable{

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
	
	private static final String FEATURED="FEATURED";
	private static final String NON_FEATURED="NON_FEATURED";
	
	
	private static final String ALL_ZONES_PRODUCTS_SQL="select * from ERPS.PRODUCT_PROMOTION_GROUP ppg where ppg.version=(select max(ppg1.version) from ERPS.PRODUCT_PROMOTION_GROUP ppg1,ERPS.PRODUCT_PROMOTION_HISTORY pph where ppg1.version=pph.version and ppg1.type=? and pph.status='S') and ppg.type=? order by ppg.zone_id,  to_number(ppg.erp_cat_position),to_number(ppg.priority),ppg.featured ";
	private static final String ALL_ZONES_REFRESH_PRODUCTS_SQL="select * from ERPS.PRODUCT_PROMOTION_GROUP ppg where ppg.version=(select max(ppg1.version) from ERPS.PRODUCT_PROMOTION_GROUP ppg1,ERPS.PRODUCT_PROMOTION_HISTORY pph where ppg1.version=pph.version and ppg1.type=? and pph.status='S' and PPH.DATE_CREATED > ? ) and ppg.type=? order by ppg.zone_id, to_number(ppg.erp_cat_position),to_number(ppg.priority),ppg.featured ";
	/**
	 * Method to get latest products for all zones, by ppType.
	 * @param conn
	 * @param ppType
	 * @return
	 */	 
	public static Map<String,List<FDProductPromotionInfo>> getAllProductsByType(Connection conn, String ppType, Date lastPublishDate) throws SQLException{
		
		   PreparedStatement ps = null;
		   ResultSet rs = null;
		   Map<String,List<FDProductPromotionInfo>> zonePromoProdMap = new HashMap<String,List<FDProductPromotionInfo>>();
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
	            	 boolean isFeatured = "X".equalsIgnoreCase(rs.getString(COLUMN_FEATURED))?true:false;
	            	 promotionProducts = zonePromoProdMap.get(zoneId);
	            	 if(null == promotionProducts){
	            		 promotionProducts = new ArrayList<FDProductPromotionInfo>();
	            		 zonePromoProdMap.put(zoneId, promotionProducts);
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
		   Map<String, Map<String,List<FDProductPromotionInfo>>> zonePromoProdMap= new HashMap<String, Map<String,List<FDProductPromotionInfo>>>();
		   Map<String,List<FDProductPromotionInfo>> promotionProductsMap = null; 
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
	            	 promotionProducts.add(promotionProduct);		            	 
	             }
	       }catch(SQLException e){
	      	 throw e;
	       }finally{
	    	   if(rs != null) rs.close();
	    	   if(ps != null) ps.close();
	       }
	       Logger.info("No.of PromotionProducts :"+promotionProducts.size());
		return promotionProducts;
	}
	
	
}
