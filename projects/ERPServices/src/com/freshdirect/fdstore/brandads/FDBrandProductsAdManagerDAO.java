package com.freshdirect.fdstore.brandads;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.SequenceGenerator;
//import com.freshdirect.fdstore.content.ContentFactory;
//import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.util.log.LoggerFactory;


public class FDBrandProductsAdManagerDAO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7151908615346463179L;
	/**
	 * 
	 */
	private static Category LOGGER = LoggerFactory.getInstance( FDBrandProductsAdManagerDAO.class );

	public  Map<String, List<HLOrderFeedDataModel>> getOrderProductFeedDataInfo(Connection conn, Date productsOrderFeedDate) throws SQLException, FDResourceException{
		
		Map<String, List<HLOrderFeedDataModel>> map = new LinkedHashMap<String, List<HLOrderFeedDataModel>>();
		List<HLOrderFeedDataModel> list = null;
		ResultSet rs=null;
		PreparedStatement ps = null;
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss aa");
		
		String QUERY = "select s.customer_id as cuserid,s.id as order_id, s.cromod_date, ol.sku_code as sku,ol.base_price as price ,ol.quantity as quantity,sa.amount as order_total," +
						" (select fdu.id  from cust.fduser fdu,cust.fdcustomer fdc where fdc.erp_customer_id=s.customer_id and fdu.fdcustomer_id=fdc.id) as puserid" +
						" from cust.sale s, cust.salesaction sa, cust.orderline ol where " +
						" ol.salesaction_id=sa.id and sa.sale_id=s.id and sa.action_type in ('CRO','MOD') and sa.action_date=s.cromod_date " +
						" and s.type='REG' and s.status <> 'CAN' and s.cromod_date > to_date(?, 'YYYY/MM/DD HH:MI:SS AM') " +
						"and S.E_STORE='FreshDirect' and sa.requested_date > trunc(sysdate) and S.E_STORE='FreshDirect' order by s.cromod_date, s.id";
		
	try {
				ps = conn.prepareStatement(QUERY);
				ps.setString(1, sdf.format(productsOrderFeedDate.getTime()));
//				ps.setString(2, sdf.format(new Date().getTime()));
				rs = ps.executeQuery();
				
			while (rs.next()) {
				String orderId = rs.getString("order_id");
				if(map.containsKey(orderId)){
					list = map.get(orderId);
				}else{
					list = new ArrayList<HLOrderFeedDataModel>();
					map.put(orderId, list);
				}
				HLOrderFeedDataModel hlOrderFeedDataModel =new HLOrderFeedDataModel();
			    hlOrderFeedDataModel.setClientId(ErpServicesProperties.getHLClientId());
				hlOrderFeedDataModel.setcUserId(rs.getString("cuserid"));
				hlOrderFeedDataModel.setOrderId(rs.getString("order_id"));
				hlOrderFeedDataModel.setOrderTotal(rs.getString("order_total"));
				hlOrderFeedDataModel.setPrice(rs.getString("price"));
				hlOrderFeedDataModel.setpUserId(rs.getString("puserid"));
				hlOrderFeedDataModel.setQuantity(rs.getString("quantity"));
				hlOrderFeedDataModel.setProdctSku(rs.getString("sku"));
				hlOrderFeedDataModel.setOrderCroModDate(rs.getTimestamp("cromod_date"));
				list.add(hlOrderFeedDataModel);
				map.put(orderId, list);
			}
		} 
		catch (SQLException e) {
			LOGGER.error( "fdBrandProductsManagerDAO connection failed with SQLException: ", e );
			throw new FDResourceException("fdBrandProductsManager DAO connection failed with SQLException: " + e.getMessage(), e);
		}
		 finally {
			if(null !=rs){
				rs.close();
			}
			if(null !=ps){
				ps.close();
			}
		}	
		return map;
	}


public  Map<String, List<HLOrderFeedDataModel>> getOrderProductFeedDataInfo(Connection conn, String order) throws SQLException, FDResourceException{
	
	Map<String, List<HLOrderFeedDataModel>> map = new HashMap<String, List<HLOrderFeedDataModel>>();
	List<HLOrderFeedDataModel> list = null;
	ResultSet rs=null;
	PreparedStatement ps = null;
	String QUERY = "select fdu.id as puserid, s.customer_id as cuserid,s.id as order_id, ol.sku_code as sku,ol.base_price as price ,ol.quantity as quantity,sa.amount as order_total" +
					" from cust.sale s, cust.salesaction sa, cust.orderline ol,cust.fduser fdu,cust.fdcustomer fdc where " +
					" ol.salesaction_id=sa.id and sa.sale_id=s.id and sa.action_type in ('CRO','MOD') and sa.action_date=s.cromod_date " +
					"and fdc.erp_customer_id=s.customer_id and fdu.fdcustomer_id=fdc.id and s.type='REG' and s.status <> 'CAN' and s.id=? ";
try { 
			ps = conn.prepareStatement(QUERY);
			ps.setString(1, order);
			rs = ps.executeQuery();
			
		while (rs.next()) {
			String orderId = rs.getString("order_id");
			if(map.containsKey(orderId)){
				list = map.get(orderId);
			}else{
				list = new ArrayList<HLOrderFeedDataModel>();
				map.put(orderId, list);
			}
			HLOrderFeedDataModel hlOrderFeedDataModel =new HLOrderFeedDataModel();
		    hlOrderFeedDataModel.setClientId(ErpServicesProperties.getHLClientId());
			hlOrderFeedDataModel.setcUserId(rs.getString("cuserid"));
			hlOrderFeedDataModel.setOrderId(rs.getString("order_id"));
			hlOrderFeedDataModel.setOrderTotal(rs.getString("order_total"));
			hlOrderFeedDataModel.setPrice(rs.getString("price"));
			hlOrderFeedDataModel.setpUserId(rs.getString("puserid"));
			hlOrderFeedDataModel.setQuantity(rs.getString("quantity"));
			hlOrderFeedDataModel.setProdctSku(rs.getString("sku"));
			list.add(hlOrderFeedDataModel);
			map.put(orderId, list);
		}
	} 
	catch (SQLException e) {
		LOGGER.error( "fdBrandProductsManagerDAO CONNECTION failed with SQLException: ", e );
		throw new FDResourceException("fdBrandProductsManager DAO CONNECTION failed with SQLException: " + e);
	}
	 finally {
		if(null !=rs){
			rs.close();
		}
		if(null !=ps){
			ps.close();
		}
	}	
	return map;
}

	private static final String GET_LAST_SENT_ORDER_TIME = "SELECT MAX(LAST_ORDER_CROMOD_TIME) FROM MIS.HL_ORDER_FEED_LOG";
	public Date getLastSentFeedOrderTime(Connection conn) throws SQLException{
		Date date = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		try{
			ps = conn.prepareStatement(GET_LAST_SENT_ORDER_TIME);
			rs = ps.executeQuery();
			if(rs.next()){
				date = rs.getTimestamp(1);
			}
		}finally {
			if(null !=rs){
				rs.close();
			}
			if(null !=ps){
				ps.close();
			}
		}	
		return date;
	}
	
	private static final String INSERT_ORDER_FEED_LOG = "INSERT INTO MIS.HL_ORDER_FEED_LOG (ID,START_TIME,END_TIME,LAST_ORDER_CROMOD_TIME,DETAILS) VALUES (?,?,?,?,?)";
	public void insertOrderFeedLog(Connection conn, HLOrderFeedLogModel orderFeedLogModel) throws SQLException{
		PreparedStatement ps = null;
		if(null !=orderFeedLogModel){
			try {
				String id = getNextId(conn);
				ps = conn.prepareStatement(INSERT_ORDER_FEED_LOG);
				ps.setString(1, id);
				ps.setTimestamp(2, new Timestamp(orderFeedLogModel.getStartTime().getTime()));
				ps.setTimestamp(3, new Timestamp(orderFeedLogModel.getEndTime().getTime()));
				ps.setTimestamp(4, new Timestamp(orderFeedLogModel.getLastSentOrderTime().getTime()));
				ps.setString(5, orderFeedLogModel.getDetails());
				
				if (ps.executeUpdate() != 1) {
					LOGGER.error( "insertOrderFeedLog() -row not created");
					throw new SQLException("insertOrderFeedLog() - row not created");
				}
			} catch (Exception e) {
				LOGGER.error( "insertOrderFeedLog() failed with Exception: ", e );
				throw new SQLException(e);
			} finally {
				if(null !=ps){
					ps.close();
				}
			}
		}
	}
	

	protected String getNextId(Connection conn) throws SQLException {
		return SequenceGenerator.getNextId(conn, "MIS");
	}
}

