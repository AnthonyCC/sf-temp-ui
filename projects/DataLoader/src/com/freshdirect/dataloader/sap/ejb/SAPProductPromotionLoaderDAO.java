package com.freshdirect.dataloader.sap.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.erp.EnumApprovalStatus;
import com.freshdirect.erp.ErpProductPromotion;
import com.freshdirect.erp.ErpProductPromotionInfo;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * 
 * @author ksriram
 *
 */
public class SAPProductPromotionLoaderDAO {


	private static Category LOGGER = LoggerFactory.getInstance( SAPProductPromotionLoaderDAO.class );
	
	private static final String INSERT_INTO_ERPS_PRODUCT_PROMOTION_HISTORY = "INSERT INTO ERPS.PRODUCT_PROMOTION_HISTORY (VERSION, DATE_CREATED,STATUS) VALUES (?,?,?)";
	private static final String UPDATE_ERPS_PRODUCT_PROMOTION_HISTORY = "UPDATE ERPS.PRODUCT_PROMOTION_HISTORY SET STATUS=? WHERE VERSION =?";
	private static final String INSERT_INTO_ERPS_PRODUCT_PROMOTION_GROUP = "INSERT INTO ERPS.PRODUCT_PROMOTION_GROUP(ID,ZONE_ID,VERSION,TYPE,MAT_NUM,SKU_CODE,PRIORITY,ERP_DEPT,FEATURED,FEATURED_HEADER,ERP_CATEGORY,ERP_CAT_POSITION,ERP_PP_ID,SALES_ORG,DISTRIBUTION_CHANNEL) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String INSERT_INTO_ERPS_PRODUCT_PROMOTION = "INSERT INTO ERPS.PRODUCT_PROMOTION(ID,VERSION,ERP_PP_ID,START_DATE,END_DATE,TYPE) VALUES(?,?,?,?,?,?)";

	public static void loadProductPromotions(Connection conn,List<ErpProductPromotion> ppList,List<ErpProductPromotionInfo> ppInfoList,int batchVersion) throws SQLException{
		
		loadProductPromotion(conn, ppList,batchVersion);		
		loadProductPromotionInfo(conn, ppInfoList,batchVersion);
		
	}

	private static void loadProductPromotion(Connection conn,
			List<ErpProductPromotion> ppList,int batchVersion) throws SQLException {
		if(null != ppList && !ppList.isEmpty()){
			LOGGER.info("No.of Product Promotions exported:"+ppList.size());
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement(INSERT_INTO_ERPS_PRODUCT_PROMOTION);
				for (Iterator<ErpProductPromotion> iterator = ppList.iterator(); iterator.hasNext();) {
					ErpProductPromotion erpProductPromotion = iterator.next();
					String id = SequenceGenerator.getNextId(conn, "ERPS");
					ps.setString(1,id);
					ps.setInt(2, batchVersion);
					ps.setString(3,erpProductPromotion.getErpPromtoionId());
					ps.setTimestamp(4,new java.sql.Timestamp(erpProductPromotion.getStartDate().getTime()));
					ps.setTimestamp(5,new java.sql.Timestamp(erpProductPromotion.getEndDate().getTime()));
					ps.setString(6,erpProductPromotion.getPpType());
					ps.addBatch();				
				}
				ps.executeBatch();
			} catch(SQLException e){
		      	 throw e;
		    } finally{
				if(null != ps && !ps.isClosed()){
					ps.close();
				}
			}
		}
	}

	private static void loadProductPromotionInfo(Connection conn,
			List<ErpProductPromotionInfo> ppInfoList,int batchVersion) throws SQLException {
		if(null != ppInfoList && !ppInfoList.isEmpty()){
			LOGGER.info("No.of Product Promotion info exported:"+ppInfoList.size());
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement(INSERT_INTO_ERPS_PRODUCT_PROMOTION_GROUP);
				for (Iterator<ErpProductPromotionInfo> iterator = ppInfoList.iterator(); iterator.hasNext();) {
					ErpProductPromotionInfo erpProductPromotionInfo = iterator.next();
					String id = SequenceGenerator.getNextId(conn, "ERPS");
					ps.setString(1, id);
					ps.setString(2, erpProductPromotionInfo.getZoneId());
					ps.setInt(3, batchVersion);
					ps.setString(4,erpProductPromotionInfo.getType());
					ps.setString(5, erpProductPromotionInfo.getMatNumber());
					ps.setString(6, erpProductPromotionInfo.getSkuCode());
					ps.setString(7,""+erpProductPromotionInfo.getPriority());
					ps.setString(8,erpProductPromotionInfo.getErpDeptId());
					ps.setString(9,erpProductPromotionInfo.getFeatured());
					ps.setString(10,erpProductPromotionInfo.getFeaturedHeader());
					ps.setString(11,erpProductPromotionInfo.getErpCategory());
					ps.setString(12,""+erpProductPromotionInfo.getErpCatPosition());
					ps.setString(13,erpProductPromotionInfo.getErpPromtoionId());
					ps.setString(14,erpProductPromotionInfo.getSalesOrg());
					ps.setString(15,erpProductPromotionInfo.getDistChannel());
					ps.addBatch();				
				}
				ps.executeBatch();
			} catch(SQLException e){
		      	 throw e;
		    } finally{
				if(null != ps && !ps.isClosed()){
					ps.close();
				}
			}
		}
	}	

	public static void createHistoryData(Connection conn, Timestamp batchTimestamp,int batchNumber) throws SQLException	{
	   
       try {    	
    	    PreparedStatement ps = conn.prepareStatement(INSERT_INTO_ERPS_PRODUCT_PROMOTION_HISTORY);
		    ps.setInt(1, batchNumber);
		    ps.setTimestamp(2, batchTimestamp);
		    ps.setString(3,"L");
		    int rowsaffected = ps.executeUpdate();
		    if (rowsaffected != 1) {
		        throw new SQLException("Unable to begin new batch.  Couldn't update loader history table.");
		    }		    		    
		    ps.close();		    
       }catch(SQLException e){
      	 throw e;
       }	    
	}
	
	public static void updateHistoryData(Connection conn, int batchNumber,String status) throws SQLException	{		   
	       try {    	
	    	    PreparedStatement ps = conn.prepareStatement(UPDATE_ERPS_PRODUCT_PROMOTION_HISTORY);			    
//			    ps.setTimestamp(1, batchTimestamp);
			    ps.setString(1,status);
			    ps.setInt(2, batchNumber);
			    int rowsaffected = ps.executeUpdate();
			    if (rowsaffected != 1) {
			        throw new SQLException("Unable to begin new batch.  Couldn't update loader history table.");
			    }		    		    
			    ps.close();		    
	       }catch(SQLException e){
	      	 throw e;
	       }	    
		}
	
	public static int getNextBatchNumber(Connection conn) throws SQLException{
        int batchNumber=-1;	
        try {          
            PreparedStatement ps = conn.prepareStatement("SELECT ERPS.PRODUCTPROMO_BATCH_SEQ.NEXTVAL FROM DUAL");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                batchNumber = rs.getInt(1);
            } else {
                LOGGER.error("Unable to begin new product promotion batch.  Didn't get a new batch number.");
                throw new SQLException("Unable to begin new batch.  Didn't get a new batch number.");
            }
            rs.close();
            ps.close();
            return batchNumber;
        }catch(SQLException e){
       	 throw e;
        }         
	}
}
