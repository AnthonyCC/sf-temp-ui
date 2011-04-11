package com.freshdirect.erp.ejb;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.freshdirect.erp.model.ATPFailureInfo;
import com.freshdirect.erp.model.ActivityLog;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.core.SequenceGenerator;

public class ErpActivityLogDAO {
	
	private final static Logger LOGGER = LoggerFactory.getInstance(ErpActivityLogDAO.class);
	
	public static void logActivity(Connection conn, List<ActivityLog> activityList) {
		String mat_id = "";
		if(activityList.get(0) != null)
			mat_id = activityList.get(0).getMatId();
		String sku_code = getSkuCode(conn, mat_id);		
		PreparedStatement pstmt = null;
		
		try {
			pstmt = 		
				conn.prepareStatement(
					"INSERT INTO ERPS.ACTIVITY_LOG(LOG_ID, USER_ID, LOG_DATE, SKU_CODE, SAP_ID, COLUMN_NAME, OLD_VALUE, NEW_VALUE, " +
									              "ROOT_ID, CHILD1_ID, CHILD2_ID, ATR_TYPE, ATR_NAME, NUTRITION_TYPE, " +
									              "NUTRITIONINFO_TYPE, NUTRITION_PRIORITY) " +
					"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			for (Iterator i = activityList.iterator(); i.hasNext();) {
				ActivityLog info = (ActivityLog) i.next();
				String sku = null;
				if(info.getSkuCode() == null) {
					if(sku_code == null) {
						//If it is a characteristic description that is being updated, sku_code will be stored as an attribute
						String root_id = info.getRootId();
						String child1_id = info.getChild1Id();
						String child2_id = info.getChild2Id();
						if(child1_id != null && child2_id != null)
							sku = getSkuFromAttributes(conn, root_id, child1_id, child2_id);
					} else {
						sku = sku_code;
					}
				} else {
					sku = info.getSkuCode();
				}
	
				pstmt.setString(1, SequenceGenerator.getNextIdFromSequence(conn, "ERPS.ACTIVITYLOG_SEQ"));
				pstmt.setString(2, info.getUser());
				pstmt.setTimestamp(3, info.getRowAddedDttm());
				pstmt.setString(4, sku);
				pstmt.setString(5, mat_id);
				pstmt.setString(6, info.getFieldName());
				pstmt.setString(7, info.getOldValue());
				pstmt.setString(8, info.getNewValue());	
				pstmt.setString(9, info.getRootId());
				pstmt.setString(10, info.getChild1Id());
				pstmt.setString(11, info.getChild2Id());
				pstmt.setString(12, info.getAtrType());
				pstmt.setString(13, info.getAtrName());
				pstmt.setString(14, info.getNutritionType());
				pstmt.setString(15, info.getNutInfoType());
				pstmt.setInt(16, info.getNutritionPriority());
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		}catch (Exception e) {
			LOGGER.error("Exception while trying to log the activity" , e);
		} finally {
			try {
				if(pstmt != null)
					pstmt.close();
			} catch (Exception e1) {
        		//Can be Ignored.
        	}
		}
		
	}
	
	public static String getSkuCode(Connection conn, String mat_id) {
		ResultSet rset = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(
					"select SKU_CODE from erps.product where id IN ( " +
	   				   "select product_id from erps.materialproxy where mat_id= ( " + 
						 "select id from erps.material where sap_id=? AND version=( " +
						   "select max(version) from erps.material where sap_id=?)))" );		
			pstmt.setString(1, mat_id);
			pstmt.setString(2, mat_id);
			rset = pstmt.executeQuery();
			if(rset.next())
				return rset.getString(1);
		} catch(SQLException e){
        	LOGGER.error("Exception while trying to get SKUCODE for material: " + mat_id, e);
        } finally {
			try {
	        	if(rset != null)
	        		rset.close();
	        	if(pstmt != null)
	        		pstmt.close();
        	} catch (Exception e1) {
        		//Can be Ignored.
        	}
		}
		return null;
	}
	
	public static String getSkuFromAttributes(Connection conn, String root_id, String child1_id, String child2_id) {
		ResultSet rset = null;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(
					"select atr_value from erps.attributes where root_id = ? and child1_id = ? and child2_id = ? and atr_name = 'skucode'");		
			pstmt.setString(1, root_id);
			pstmt.setString(2, child1_id);
			pstmt.setString(3, child2_id);
			rset = pstmt.executeQuery();
			if(rset.next())
				return rset.getString(1);
		} catch(SQLException e){
        	LOGGER.error("Exception while trying to get SKUCODE for attributes: " + root_id + "|child1_id:" + child1_id + "|child2_id:" + child2_id , e);
        } finally {
			try {
	        	if(rset != null)
	        		rset.close();
	        	if(pstmt != null)
	        		pstmt.close();
        	} catch (Exception e1) {
        		//Can be Ignored.
        	}
		}
		return null;
	}
	
}
