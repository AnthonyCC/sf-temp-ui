package com.freshdirect.fdstore.ecoupon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.FDCouponProperties;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.fdstore.ecoupon.model.FDCouponInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCouponUPCInfo;
import com.freshdirect.fdstore.ecoupon.model.FDCustomerCouponHistoryInfo;
import com.freshdirect.framework.util.log.LoggerFactory;


public class FDCouponManagerDAO {
	
	private static final DateFormat DATE_FORMATTER =new SimpleDateFormat("MM/dd/yyyy");
	private static final DateFormat TIME_FORMATTER =new SimpleDateFormat("MM/dd/yyyy hh:MM a");
	
	private static Category LOGGER = LoggerFactory.getInstance( FDCouponManagerDAO.class );

	public static int createHistoryData(Connection conn, Timestamp timestamp) throws SQLException	{
		   
	       try {    	
	    	    PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.FDCOUPON_HISTORY (VERSION, DATE_CREATED,STATUS) VALUES (?,?,?)");
	    	    int version = getNextId(conn);
			    ps.setInt(1, version);
			    ps.setTimestamp(2, timestamp);
			    ps.setString(3," ");
			    int rowsaffected = ps.executeUpdate();
			    if (rowsaffected != 1) {
			        throw new SQLException("Unable to create new version.  Couldn't update coupon history table.");
			    }		    		    
			    ps.close();
			    return version;
	       }catch(SQLException e){
	      	 throw e;
	       }	    
		}
	
	public static int getNextId(Connection conn) throws SQLException{
        int batchNumber=-1;	
        try {          
            PreparedStatement ps = conn.prepareStatement("SELECT CUST.COUPON_SEQ.NEXTVAL FROM DUAL");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                batchNumber = rs.getInt(1);
            } else {
                LOGGER.error("Unable to get next id from Coupon Sequence.");
                throw new SQLException("Unable to get next id from Coupon Sequence.");
            }
            rs.close();
            ps.close();
            return batchNumber;
        }catch(SQLException e){
       	 throw e;
        }  
	}
        
	public static void storeCoupons(Connection conn, List<FDCouponInfo> coupons) throws SQLException{
		PreparedStatement ps =null;
		PreparedStatement ps1 =null;
		Date currentTime =new Date();
		
		int version =createHistoryData(conn,new Timestamp(currentTime.getTime()));		
		ps =conn.prepareStatement(
					"INSERT INTO CUST.FDCOUPON "
					+ "(ID,COUPON_ID,VERSION,CATEGORY,BRAND,MANUFACTURER,VALUE,REQUIRED_QTY,EXP_DATE,SHORT_DESC,LONG_DESC,PURCHASE_REQ_DESC,PRIORITY,IS_EXPIRED,OFFER_TYPE,ENABLED) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		try {
			for (Iterator<FDCouponInfo> iterator = coupons.iterator(); iterator
					.hasNext();) {
				FDCouponInfo coupon = (FDCouponInfo) iterator.next();
				int i=1;
				coupon.setId(""+getNextId(conn));
				ps.setString(i++, coupon.getId());
				ps.setString(i++, coupon.getCouponId());
				ps.setInt(i++, version);
				ps.setString(i++, coupon.getCategory());
				ps.setString(i++, coupon.getBrandName());
				ps.setString(i++, coupon.getManufacturer());
				ps.setString(i++, coupon.getValue());
				ps.setString(i++, coupon.getRequiredQuantity());
				
				ps.setDate(i++, new java.sql.Date(null !=coupon.getExpirationDate()?coupon.getExpirationDate().getTime():null));
				
				ps.setString(i++, coupon.getShortDescription());
				ps.setString(i++, coupon.getLongDescription());
				ps.setString(i++, coupon.getRequirementDescription());
				ps.setString(i++, coupon.getOfferPriority());
//					ps.setTimestamp(i++, new java.sql.Timestamp(currentTime.getTime()));
//					ps.setTimestamp(i++, new java.sql.Timestamp(currentTime.getTime()));					
				ps.setString(i++, coupon.isExpired()?"X":"");
				if(null !=coupon.getOfferType()){
					ps.setString(i++, coupon.getOfferType().getName());
				}else{
					ps.setNull(i++, Types.VARCHAR);
				}
				ps.setString(i++, coupon.getEnabled());
				
				ps.addBatch();

			}
			ps.executeBatch();
			ps.close();
			for (Iterator<FDCouponInfo> iterator = coupons.iterator(); iterator
					.hasNext();) {
				FDCouponInfo coupon = (FDCouponInfo) iterator.next();
				insertRequiredUpcs(conn, coupon.getRequiredUpcs(), coupon.getId());
			}
			
		} finally {
			if (ps != null)
				ps.close();
		}		
	}

	

	private static void insertRequiredUpcs(Connection conn, List<FDCouponUPCInfo> fdCouponUPCInfoList,String couponId)
			throws SQLException {
		PreparedStatement ps =null;
		try {
			ps = prepareRequiredUpcs(conn, fdCouponUPCInfoList, couponId);
			ps.executeBatch();
			ps.close();
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	private static PreparedStatement prepareRequiredUpcs(Connection conn,
			List<FDCouponUPCInfo> fdCouponUPCInfoList, String couponId)
			throws SQLException {
		PreparedStatement ps;
		ps = conn.prepareStatement("INSERT INTO CUST.FDCOUPON_REQ_UPC(FDCOUPON_ID,UPC,UPC_DESC,IS_REQUIRED) VALUES(?,?,?,?)");
		
		for (Iterator iterator2 = fdCouponUPCInfoList.iterator(); iterator2.hasNext();) {
			FDCouponUPCInfo fdCouponUPCInfo = (FDCouponUPCInfo) iterator2.next();			
			int i=1;
			ps.setString(i++, couponId);
			ps.setString(i++, fdCouponUPCInfo.getUpc());
			ps.setString(i++, fdCouponUPCInfo.getUpcDescription());
			ps.setString(i++, fdCouponUPCInfo.isRequired()?"X":"");	
			ps.addBatch();
		}
		return ps;
	}
	
	public static FDCouponInfo getCouponById(Connection conn,String couponId) throws SQLException{
		FDCouponInfo couponInfo =null;
		PreparedStatement ps = null;
		ps=conn.prepareStatement("SELECT * FROM  CUST.FDCOUPON FC, CUST.FDCOUPON_REQ_UPC UPC WHERE  FC.ID=UPC.FDCOUPON_ID AND FC.COUPON_ID =?");
		ps.setString(1, couponId);				
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			if(null == couponInfo||!couponInfo.getCouponId().equals(couponId) ){
				couponInfo = loadCouponInfo(rs);
			}
			List<FDCouponUPCInfo> requiredUpcs = couponInfo.getRequiredUpcs();
			if(null==requiredUpcs){
				requiredUpcs = new ArrayList<FDCouponUPCInfo>();
				couponInfo.setRequiredUpcs(requiredUpcs);
			}
			FDCouponUPCInfo upcInfo = loadCouponUpc(rs);
			upcInfo.setFdCouponId(couponInfo.getId());
			upcInfo.setCouponId(couponId);
			requiredUpcs.add(upcInfo);
//			List<FDCouponUPCInfo> requiredUpcs =getRequiredUpcsForCoupon(couponId,couponInfo.getCouponId(),conn);
//			couponInfo.setRequiredUpcs(requiredUpcs);
//			coupons.add(couponInfo);			
		}
		rs.close();
		ps.close();
		
		return couponInfo;
	}

	private static FDCouponUPCInfo loadCouponUpc(ResultSet rs) throws SQLException {
		FDCouponUPCInfo upcInfo = new FDCouponUPCInfo();		
		upcInfo.setUpc(rs.getString("UPC"));
		upcInfo.setUpcDescription(rs.getString("UPC_DESC"));
		upcInfo.setRequired("X".equals(rs.getString("IS_REQUIRED"))?true:false);
		return upcInfo;
	}
	
	public static List<FDCouponInfo> getActiveCoupons(Connection conn,Date lastModified) throws SQLException{
		List<FDCouponInfo> coupons= new ArrayList<FDCouponInfo>();
		PreparedStatement ps = null;
		if(null ==lastModified){
			ps=conn.prepareStatement("SELECT * FROM  CUST.FDCOUPON FC, CUST.FDCOUPON_REQ_UPC UPC,CUST.FDCOUPON_HISTORY CH WHERE  FC.ID=UPC.FDCOUPON_ID AND FC.VERSION=(SELECT MAX(FC1.VERSION) " +
					" FROM CUST.FDCOUPON FC1 WHERE FC1.COUPON_ID=FC.COUPON_ID) AND CH.VERSION=(select min(fc2.version) from cust.fdcoupon fc2 where FC2.COUPON_ID=FC.COUPON_ID)"+
					" ORDER BY FC.COUPON_ID");
			if(FDCouponProperties.isCouponCacheDaysLimitEnabled()){
				int days = FDCouponProperties.getCouponCacheDaysLimit();
				ps=conn.prepareStatement("SELECT * FROM  CUST.FDCOUPON FC, CUST.FDCOUPON_REQ_UPC UPC, CUST.FDCOUPON_HISTORY CH2 WHERE  FC.ID=UPC.FDCOUPON_ID AND FC.VERSION=(SELECT MAX(FC1.VERSION) FROM CUST.FDCOUPON FC1,CUST.FDCOUPON_HISTORY CH WHERE FC1.COUPON_ID=FC.COUPON_ID"+
						" AND FC1.VERSION=CH.VERSION AND CH.DATE_CREATED >(SYSDATE-"+days+")) AND CH2.VERSION=(select min(fc2.version) from cust.fdcoupon fc2 where FC2.COUPON_ID=FC.COUPON_ID) ORDER BY FC.COUPON_ID");
			}
		}else{
			ps=conn.prepareStatement("SELECT * FROM  CUST.FDCOUPON FC, CUST.FDCOUPON_REQ_UPC UPC, CUST.FDCOUPON_HISTORY CH1 WHERE  FC.ID=UPC.FDCOUPON_ID AND FC.VERSION=(SELECT MAX(FC1.VERSION) FROM CUST.FDCOUPON FC1,CUST.FDCOUPON_HISTORY CH WHERE FC1.COUPON_ID=FC.COUPON_ID"+
					" AND FC1.VERSION=CH.VERSION AND CH.DATE_CREATED >?) AND CH1.VERSION=(select min(fc2.version) from cust.fdcoupon fc2 where FC2.COUPON_ID=FC.COUPON_ID)  ORDER BY FC.COUPON_ID");
			
//			ps=conn.prepareStatement("SELECT * FROM CUST.FDCOUPON FC WHERE VERSION=(SELECT MAX(VERSION) FROM CUST.FDCOUPON_HISTORY WHERE DATE_CREATED > ?)");
			ps.setTimestamp(1, new Timestamp(lastModified.getTime()));			
		}
		
		ResultSet rs = ps.executeQuery();
		loadCouponsFromResultSet(coupons, rs);

		rs.close();
		ps.close();
		
		return coupons;
	}

	private static void loadCouponsFromResultSet(List<FDCouponInfo> coupons,
			ResultSet rs) throws SQLException {
		FDCouponInfo couponInfo = null;
		while (rs.next()) {
			String couponId = rs.getString("COUPON_ID");
			if(null == couponInfo||!couponInfo.getCouponId().equals(couponId) ){
				couponInfo = loadCouponInfo(rs);
				coupons.add(couponInfo);
			}
			List<FDCouponUPCInfo> requiredUpcs = couponInfo.getRequiredUpcs();
			if(null==requiredUpcs){
				requiredUpcs = new ArrayList<FDCouponUPCInfo>();
				couponInfo.setRequiredUpcs(requiredUpcs);
			}
			FDCouponUPCInfo upcInfo = loadCouponUpc(rs);
			upcInfo.setFdCouponId(couponInfo.getId());
			upcInfo.setCouponId(couponInfo.getCouponId());
			requiredUpcs.add(upcInfo);		
		}
	}
	
	
	/*private static List<FDCouponUPCInfo> getRequiredUpcsForCoupon(String fdCouponId,String couponId,Connection conn) throws SQLException{
		List<FDCouponUPCInfo> requiredUpcs = new ArrayList<FDCouponUPCInfo>();
		PreparedStatement ps1 = conn
				.prepareStatement("SELECT * FROM CUST.FDCOUPON_REQ_UPC WHERE FDCOUPON_ID=?");
		ps1.setString(1, fdCouponId);
		ResultSet rs1 = ps1.executeQuery();
		
		while (rs1.next()) {
			FDCouponUPCInfo upcInfo = new FDCouponUPCInfo();
			upcInfo.setFdCouponId(fdCouponId);
			upcInfo.setCouponId(couponId);
			upcInfo.setUpc(rs1.getString("UPC"));
			upcInfo.setUpcDescription(rs1.getString("UPC_DESC"));
			upcInfo.setRequired("X".equals(rs1.getString("IS_REQUIRED"))?true:false);				
			requiredUpcs.add(upcInfo);
		}
		rs1.close();
		ps1.close();
		return requiredUpcs;
	}*/
	private static FDCouponInfo loadCouponInfo(ResultSet rs)
			throws SQLException {
		FDCouponInfo couponInfo =new FDCouponInfo();
		couponInfo.setId(rs.getString("ID"));
		couponInfo.setVersion(rs.getInt("VERSION"));
		couponInfo.setCouponId(rs.getString("COUPON_ID"));
		couponInfo.setCategory(rs.getString("CATEGORY"));
		couponInfo.setBrandName(rs.getString("BRAND"));
		couponInfo.setManufacturer(rs.getString("MANUFACTURER"));
		couponInfo.setValue(rs.getString("VALUE"));
		couponInfo.setRequiredQuantity(rs.getString("REQUIRED_QTY"));
		couponInfo.setExpirationDate(rs.getDate("EXP_DATE"));
		Date startDate = rs.getDate("DATE_CREATED");
		couponInfo.setStartDate(DATE_FORMATTER.format(startDate));
		//couponInfo.setExpirationDate(rs.getString("EXP_DATE"));
		couponInfo.setShortDescription(rs.getString("SHORT_DESC"));
		couponInfo.setLongDescription(rs.getString("LONG_DESC"));
		couponInfo.setRequirementDescription(rs.getString("PURCHASE_REQ_DESC"));
		couponInfo.setOfferPriority(rs.getString("PRIORITY"));
		couponInfo.setCreatedTime(rs.getTimestamp("CREATED_TIME"));
		couponInfo.setLastUpdatedTime(rs.getTimestamp("LAST_UPDATED_TIME"));
		couponInfo.setExpired("X".equals(rs.getString("IS_EXPIRED"))?true:false);	
		couponInfo.setOfferType(EnumCouponOfferType.getEnum(rs.getString("OFFER_TYPE")));
		couponInfo.setEnabled(rs.getString("ENABLED"));
		return couponInfo;
	}
	
	public static List<FDCouponInfo> getCouponsForCRMSearch(Connection conn, String searchTerm) throws SQLException {  	
			String QUERY = "SELECT * FROM CUST.FDCOUPON FC, CUST.FDCOUPON_REQ_UPC UPC,CUST.FDCOUPON_HISTORY CH WHERE FC.ID=UPC.FDCOUPON_ID AND FC.VERSION=(SELECT MAX(VERSION) FROM CUST.FDCOUPON_HISTORY) AND " +
					" CH.VERSION=(select min(fc1.version) from cust.fdcoupon fc1 where FC1.COUPON_ID=FC.COUPON_ID)" +
							" AND  EXISTS(SELECT 1 from CUST.FDCOUPON_REQ_UPC UPC where UPC.FDCOUPON_ID=fc.id) and (lower(short_desc) like (?) or coupon_id = ? ";		       
			List<FDCouponInfo> coupons= new ArrayList<FDCouponInfo>();
			if(FDCouponProperties.isCouponCacheDaysLimitEnabled()){
				int days = FDCouponProperties.getCouponCacheDaysLimit();
				QUERY = "SELECT * FROM CUST.FDCOUPON FC,CUST.FDCOUPON_REQ_UPC UPC,CUST.FDCOUPON_HISTORY CH WHERE FC.ID=UPC.FDCOUPON_ID AND FC.VERSION=(SELECT MAX(FC1.VERSION) FROM CUST.FDCOUPON FC1,CUST.FDCOUPON_HISTORY CH1 WHERE FC1.COUPON_ID=FC.COUPON_ID"+
						" AND FC1.VERSION=CH1.VERSION AND CH1.DATE_CREATED >(SYSDATE-"+days+")) AND  EXISTS(SELECT 1 from CUST.FDCOUPON_REQ_UPC UPC where UPC.FDCOUPON_ID=fc.id) and  CH.VERSION=(select min(fc2.version) from cust.fdcoupon fc2 where FC2.COUPON_ID=FC.COUPON_ID) and (lower(short_desc) like (?) or coupon_id = ? ";
			}
			PreparedStatement ps = null;
			boolean coupon_value = false;
			double value = 0;
			try {
				value = Double.parseDouble(searchTerm);
				QUERY = QUERY + "or value = ?)";
				coupon_value = true;
			} catch (NumberFormatException e) {
				QUERY = QUERY + ")";
			}
			
			ps = conn.prepareStatement(QUERY);			
			ps.setString(1, "%" + searchTerm.toLowerCase() + "%");
			ps.setString(2, searchTerm);
			if(coupon_value) {
				ps.setDouble(3, value);
			}
			ResultSet rs = ps.executeQuery();
			loadCouponsFromResultSet(coupons, rs);
			rs.close();
			ps.close();
			
			return coupons;
	}
	
	public static int getMaxCouponsVersion(Connection conn) throws SQLException {  	
		String QUERY = "select max(version) from CUST.FDCOUPON_HISTORY";		       
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(QUERY);			
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);			
			}
		} finally {
			rs.close();
			ps.close();
		}		
		return 0;
	}
	
	public static List<FDCustomerCouponHistoryInfo> getCustomersCouponHistoryInfo(Connection conn,String customerId) throws SQLException{
		List<FDCustomerCouponHistoryInfo> list = new ArrayList<FDCustomerCouponHistoryInfo>();
		String QUERY = "select cl.*,s.id as \"SALE_ID\",s.status as \"SALE_STATUS\",S.CROMOD_DATE as \"SALE_DATE\", sa.requested_date as \"DELIVERY_DATE\" from cust.couponline cl, cust.sale s,cust.orderline ol,cust.salesaction sa"
                        + " where s.type='REG' and CL.ORDERLINE_ID=ol.id and OL.SALESACTION_ID=sa.id and sa.sale_id=s.id and SA.ACTION_DATE=S.CROMOD_DATE and S.CUSTOMER_ID=?"
                        + " ";//and not exists(select 1 from CUST.COUPON_TRANS ct, cust.salesaction sa1 where ct.salesaction_id=sa1.id and sa1.sale_id=s.id and ct.trans_type='CANCEL_ORDER' and ct.trans_status in ('S','P'))";
		
		int days = FDCouponProperties.getCustomerCouponUsageHistoryDaysLimit();
		if(days >0){
			QUERY="select cl.*,s.id as \"SALE_ID\",s.status as \"SALE_STATUS\",S.CROMOD_DATE as \"SALE_DATE\", sa.requested_date as \"DELIVERY_DATE\" from cust.couponline cl, cust.sale s,cust.orderline ol,cust.salesaction sa"
                    + " where s.type='REG' and CL.ORDERLINE_ID=ol.id and OL.SALESACTION_ID=sa.id and sa.sale_id=s.id and SA.ACTION_DATE=S.CROMOD_DATE and S.CUSTOMER_ID=?"
                    + " and SA.REQUESTED_DATE > (SYSDATE-"+ days +")"
                    + " ";//and not exists(select 1 from CUST.COUPON_TRANS ct, cust.salesaction sa1 where ct.salesaction_id=sa1.id and sa1.sale_id=s.id and ct.trans_type='CANCEL_ORDER' and ct.trans_status in ('S','P'))";
                   
		}
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(QUERY);	
			ps.setString(1, customerId);
			rs = ps.executeQuery();
			while (rs.next()) {
				FDCustomerCouponHistoryInfo info =new FDCustomerCouponHistoryInfo();
				info.setCouponId(rs.getString("COUPON_ID"));
				info.setCouponDesc(rs.getString("COUPON_DESC"));
				info.setVersion(rs.getInt("VERSION"));
				info.setDiscountAmt(rs.getDouble("DISC_AMT"));
				info.setSaleId(rs.getString("SALE_ID"));
				info.setSaleStatus(EnumSaleStatus.getSaleStatus(rs.getString("SALE_STATUS")));
				info.setSaleDate(rs.getDate("SALE_DATE"));
				info.setDeliveryDate(rs.getDate("DELIVERY_DATE"));
				list.add(info);
			}
		} finally {
			if(null !=rs){
				rs.close();
			}
			if(null !=ps){
				ps.close();
			}
		}	
		return list;
	}
}

