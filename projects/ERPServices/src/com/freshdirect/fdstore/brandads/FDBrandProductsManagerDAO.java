package com.freshdirect.fdstore.brandads;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.fdstore.FDResourceException;
//import com.freshdirect.fdstore.content.ContentFactory;
//import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.util.log.LoggerFactory;


public class FDBrandProductsManagerDAO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7151908615346463179L;
	/**
	 * 
	 */
	private static Category LOGGER = LoggerFactory.getInstance( FDBrandProductsManagerDAO.class );

	public  Map<String, List<HLOrderFeedDataModel>> getOrderProductFeedDataInfo(Connection conn, Date productsOrderFeedDate) throws SQLException, FDResourceException{
		
		Map<String, List<HLOrderFeedDataModel>> map = new HashMap<String, List<HLOrderFeedDataModel>>();
		List<HLOrderFeedDataModel> list = null;
		ResultSet rs=null;
		PreparedStatement ps = null;
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss aa");
		
		String QUERY = "select fdu.id as puserid, s.customer_id as cuserid,s.id as order_id, ol.sku_code as sku,ol.base_price as price ,ol.quantity as quantity,sa.amount as order_total," +
						" to_char(sa.action_date, 'YYYY/MM/DD HH:MI:SS') as order_date from cust.sale s, cust.salesaction sa, cust.orderline ol,cust.fduser fdu,cust.fdcustomer fdc where " +
						" ol.salesaction_id=sa.id and sa.sale_id=s.id and sa.action_type in ('CRO','MOD') and sa.action_date=s.cromod_date " +
						"and fdc.erp_customer_id=s.customer_id and fdu.fdcustomer_id=fdc.id and s.type='REG' and s.status <> 'CAN' and sa.action_date  BETWEEN  to_date(?, 'YYYY/MM/DD HH:MI:SS AM') " +
						"and  to_date(?, 'YYYY/MM/DD HH:MI:SS AM') order by s.id";
		
	try {
				ps = conn.prepareStatement(QUERY);
				ps.setString(1, sdf.format(productsOrderFeedDate.getTime()));
				ps.setString(2, sdf.format(new Date().getTime()));
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
}

