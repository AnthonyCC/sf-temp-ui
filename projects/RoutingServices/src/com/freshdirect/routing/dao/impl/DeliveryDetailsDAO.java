package com.freshdirect.routing.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.freshdirect.routing.dao.IDeliveryDetailsDAO;
import com.freshdirect.routing.model.AreaModel;
import com.freshdirect.routing.model.DeliveryModel;
import com.freshdirect.routing.model.IAreaModel;
import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IServiceTimeModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.model.ServiceTimeModel;
import com.freshdirect.routing.model.ZoneModel;

public class DeliveryDetailsDAO extends BaseDAO implements IDeliveryDetailsDAO {
		
	
	private static final String GET_DELIVERYINFO_QRY="select di.SCRUBBED_ADDRESS SCRUBBED_ADDRESS, di.DELIVERY_TYPE DELIVERY_TYPE, "+
			"di.APARTMENT APARTMENT,di.CITY CITY, di.STATE STATE, di.COUNTRY COUNTRY, " +
			"di.ZIP ZIP, di.ZONE ZONE "+			
			"from cust.sale s, cust.salesaction sa, cust.deliveryinfo di "+
			"where s.id = ? and s.id = sa.sale_id and s.customer_id = sa.customer_id and sa.id = di.salesaction_id "+ 
			"and sa.action_type in ('CRO','MOD') and sa.action_date=s.CROMOD_DATE and s.type='REG'";
	
	private static final String GET_SERVICETIME_QRY="select st.FIXED_SERVICE_TIME  FIXED_SERVICE_TIME , st.VARIABLE_SERVICE_TIME VARIABLE_SERVICE_TIME "+			
			"from dlv.SERVICETIME st "+
			"where st.SERVICETIME_TYPE = ? and st.ZONE_TYPE = ? ";
	
	private static final String GET_DELIVERYTYPE_QRY="select r.SERVICE_TYPE SERVICE_TYPE from dlv.region r, dlv.region_data rd, dlv.zone z "+
			"where rd.id = z.region_data_id and rd.region_id = r.id and z.zone_code = ? "  +
			"and rd.start_date = (select max(start_date) from dlv.region_data where start_date <= sysdate and region_id=r.id)";
	
	private static final String GET_DELIVERYZONETYPE_QRY="select z.ZONE_TYPE SERVICE_TYPE from transp.trn_zone z where z.zone_number = ? ";
	
	private static final String GET_DELIVERYZONEDETAILS_QRY="select z.ZONE_CODE ZONE_NUMBER, z.ZONE_TYPE ZONE_TYPE, a.CODE AREACODE," +
			"a.BALANCE_BY BALANCE_BY, a.LOADBALANCE_FACTOR LOADBALANCE_FACTOR, a.NEEDS_LOADBALANCE NEEDS_LOADBALANCE  from transp.zone z, transp.trn_area a  " +
			" where z.area = a.code and (z.OBSOLETE <> 'X' or z.OBSOLETE IS NULL)";
			
	
	public IDeliveryModel getDeliveryInfo(final String saleId) throws SQLException {
		 final IDeliveryModel model = new DeliveryModel();	         
         PreparedStatementCreator creator=new PreparedStatementCreator() {
             public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                 PreparedStatement ps =
                     connection.prepareStatement(GET_DELIVERYINFO_QRY);
                 ps.setString(1,saleId);
                 return ps;
             }  
         };
         jdbcTemplate.query(creator, 
        		  new RowCallbackHandler() { 
        		      public void processRow(ResultSet rs) throws SQLException {
        		    	
        		    	do { 
        		    		IZoneModel tmpModel = new ZoneModel();
				    		tmpModel.setZoneNumber(rs.getString("ZONE"));
        		    		model.setDeliveryZone(tmpModel);
        		    		//model.setDeliveryType(rs.getString("DELIVERY_TYPE"));
        		    		ILocationModel locModel = new LocationModel();
        		    		locModel.setStreetAddress1(rs.getString("SCRUBBED_ADDRESS"));
        		    		locModel.setApartmentNumber(rs.getString("APARTMENT"));
        		    		locModel.setZipCode(rs.getString("ZIP"));
        		    		locModel.setState(rs.getString("STATE"));
        		    		locModel.setCity(rs.getString("CITY"));
        		    		locModel.setCountry(rs.getString("COUNTRY"));
        		    		
        		    		model.setDeliveryLocation(locModel);        		    		
        		    	   }
        		    	   while(rs.next());		        		    	
        		      }
        		  }
        	); 
         
		return model;
	}
	
	public IServiceTimeModel getServiceTime(final String serviceTimeType, final String zoneType) throws SQLException {
		final IServiceTimeModel model = new ServiceTimeModel();  
		model.setNew(true);
		
        PreparedStatementCreator creator=new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
                PreparedStatement ps =
                    connection.prepareStatement(GET_SERVICETIME_QRY);
                ps.setString(1,serviceTimeType);
                ps.setString(2,zoneType);
                return ps;
            }  
        };
        jdbcTemplate.query(creator, 
       		  new RowCallbackHandler() { 
       		      public void processRow(ResultSet rs) throws SQLException {
       		    	
       		    	do {        		    		
       		    		model.setFixedServiceTime(rs.getDouble("FIXED_SERVICE_TIME"));
       		    		model.setVariableServiceTime(rs.getDouble("VARIABLE_SERVICE_TIME")); 
       		    		model.setNew(false);
       		    	   }
       		    	   while(rs.next());		        		    	
       		      }
       		  }
       	); 
        
		return model;
	}
	public String getDeliveryType(final String zoneCode) throws SQLException {
		
		return (String)jdbcTemplate.queryForObject(GET_DELIVERYTYPE_QRY, new Object[]{zoneCode}, String.class);	
	}	
	
	public String getDeliveryZoneType(final String zoneCode) throws SQLException {
				
		return (String)jdbcTemplate.queryForObject(GET_DELIVERYZONETYPE_QRY, new Object[]{zoneCode}, String.class);    		
	}	
	
	public Map getDeliveryZoneDetails() throws SQLException {
		final Map zoneDetailsMap = new HashMap();
				
		jdbcTemplate.query(GET_DELIVERYZONEDETAILS_QRY, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	do {        		    		
				    		String zoneCode = rs.getString("ZONE_NUMBER");
				    		IZoneModel tmpModel = new ZoneModel();
				    		tmpModel.setZoneNumber(zoneCode);
				    		
				    		
				    		tmpModel.setZoneType(rs.getString("ZONE_TYPE"));
				    		
				    		IAreaModel tmpAreaModel = new AreaModel();
				    		tmpAreaModel.setAreaCode(rs.getString("AREACODE"));
				    		tmpAreaModel.setBalanceBy(rs.getString("BALANCE_BY"));
				    		tmpAreaModel.setLoadBalanceFactor(rs.getDouble("LOADBALANCE_FACTOR"));
				    		tmpAreaModel.setNeedsLoadBalance("X".equalsIgnoreCase(rs.getString("NEEDS_LOADBALANCE")) ? true : false);
				    		
				    		tmpModel.setArea(tmpAreaModel);
				    		zoneDetailsMap.put(zoneCode, tmpModel);
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return zoneDetailsMap;
	}
	
	public List getLateDeliveryOrders(String query) throws SQLException {
		final List lateDeliveryOrders = new ArrayList();
		
		jdbcTemplate.query(query, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {				    	
				    	do {        		    		
				    		lateDeliveryOrders.add(rs.getString("ERPID"));
				    	 } while(rs.next());		        		    	
				      }
				  }
			);
		return lateDeliveryOrders;
	}
}
