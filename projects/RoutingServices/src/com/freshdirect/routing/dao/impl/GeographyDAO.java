package com.freshdirect.routing.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.delivery.InvalidAddressException;
import com.freshdirect.routing.constants.EnumGeocodeConfidenceType;
import com.freshdirect.routing.constants.EnumGeocodeQualityType;
import com.freshdirect.routing.dao.IGeographyDAO;
import com.freshdirect.routing.model.BuildingModel;
import com.freshdirect.routing.model.GeographicLocation;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.IGeographicLocation;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.util.RoutingUtil;

public class GeographyDAO extends BaseDAO implements IGeographyDAO  {
	
	private static final String GET_LOCATION_QRY = "select dl.ID ID, db.SCRUBBED_STREET SCRUBBED_STREET, "+
													"dl.APARTMENT APARTMENT,db.CITY CITY, db.STATE STATE, db.COUNTRY COUNTRY, db.ZIP ZIP, " +
													"db.LONGITUDE LONGITUDE, db.LATITUDE LATITUDE, dl.SERVICETIME_TYPE SERVICETIME_TYPE, "+
													"db.SERVICETIME_TYPE DEFAULTSERVICETIME_TYPE, dl.BUILDINGID BUILDING_ID, "+	
													"db.GEO_CONFIDENCE GEO_CONFIDENCE, db.GEO_QUALITY GEO_QUALITY "+
													"from dlv.DELIVERY_LOCATION dl, dlv.DELIVERY_BUILDING db "+
													"where dl.BUILDINGID = db.ID and db.SCRUBBED_STREET = ? ";
	
	private static final String GET_LOCATION_BYID_QRY = "select dl.ID ID, db.SCRUBBED_STREET SCRUBBED_STREET, "+
													"dl.APARTMENT APARTMENT,db.CITY CITY, db.STATE STATE, db.COUNTRY COUNTRY, db.ZIP ZIP, " +
													"db.LONGITUDE LONGITUDE, db.LATITUDE LATITUDE, dl.SERVICETIME_TYPE SERVICETIME_TYPE, "+
													"db.SERVICETIME_TYPE DEFAULTSERVICETIME_TYPE, dl.BUILDINGID BUILDING_ID, "+	
													"db.GEO_CONFIDENCE GEO_CONFIDENCE, db.GEO_QUALITY GEO_QUALITY "+
													"from dlv.DELIVERY_LOCATION dl, dlv.DELIVERY_BUILDING db "+
													"where dl.BUILDINGID = db.ID and dl.ID in (";
	
	private static final String GET_LOCATIONNEXTSEQ_QRY = "SELECT DLV.DELIVERY_LOCATION_SEQ.nextval FROM DUAL";
	
	private static final String GET_BUILDINGNEXTSEQ_QRY = "SELECT DLV.DELIVERY_BUILDING_SEQ.nextval FROM DUAL";
	
	private static final String GET_STATE_QRY = "SELECT DISTINCT(STATE) STATE FROM DLV.CITY_STATE";
	
	private static final String INSERT_LOCATION_INFORMATION = "INSERT INTO DLV.DELIVERY_LOCATION ( ID,"+
																"APARTMENT, "+ 
																" SERVICETIME_TYPE, BUILDINGID) VALUES ( "+
																 "?,?,?,?)";
	
	private static final String INSERT_BUILDING_INFORMATION = "INSERT INTO DLV.DELIVERY_BUILDING ( ID,"+
																"SCRUBBED_STREET, ZIP, COUNTRY,  "+ 
																" SERVICETIME_TYPE, LONGITUDE , LATITUDE , GEO_CONFIDENCE " +
																", GEO_QUALITY, CITY, STATE ) VALUES ( "+
																 "?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String GET_BUILDING_QRY = "select db.ID ID, db.SCRUBBED_STREET SCRUBBED_STREET, "+
													"db.COUNTRY COUNTRY, db.ZIP ZIP, " +
													"db.LONGITUDE LONGITUDE, db.LATITUDE LATITUDE, db.SERVICETIME_TYPE SERVICETIME_TYPE," +
													"db.GEO_CONFIDENCE GEO_CONFIDENCE, db.GEO_QUALITY GEO_QUALITY "+			
													"from dlv.DELIVERY_BUILDING db "+
													"where db.SCRUBBED_STREET = ? ";
	
	public void insertLocations(List dataList) throws SQLException {
		Connection connection=null;
		try{				
			BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),INSERT_LOCATION_INFORMATION);		
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));	
			
			batchUpdater.compile(); 
			
			Iterator iterator=dataList.iterator();
			connection=this.jdbcTemplate.getDataSource().getConnection();
			ILocationModel model = null;
									
			while(iterator.hasNext()){
				
				model = (ILocationModel)iterator.next();
										
				
				batchUpdater.update(
				new Object[]{ model.getLocationId(), 
						model.getApartmentNumber(),model.getServiceTimeType(), model.getBuildingId()}
				);			
			}
			batchUpdater.flush();
		}finally{
			if(connection!=null) connection.close();
		}		
	}
	
	public void insertBuildings(List dataList) throws SQLException {
		Connection connection=null;
		try{				
			BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),INSERT_BUILDING_INFORMATION);		
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.DECIMAL));
			batchUpdater.declareParameter(new SqlParameter(Types.DECIMAL));						
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			
			
			batchUpdater.compile(); 
			
			Iterator iterator=dataList.iterator();
			connection=this.jdbcTemplate.getDataSource().getConnection();
			IBuildingModel model = null;
			IGeographicLocation location = null;
			
			String latitude = null;
			String longitude = null;
			String confidence = null;
			String quality = null;
			
			while(iterator.hasNext()){
				
				model = (IBuildingModel)iterator.next();
				location = model.getGeographicLocation();
				
				if(location != null) {
					latitude = location.getLatitude();
					longitude = location.getLongitude();
					confidence = location.getConfidence();
					quality = location.getQuality();
				}
				
				batchUpdater.update(
				new Object[]{ model.getBuildingId(), 
						model.getSrubbedStreet(),model.getZipCode(),
						model.getCountry(), model.getServiceTimeType(), longitude, latitude, confidence, quality
						, model.getCity(), model.getState()}
				);			
			}
			batchUpdater.flush();
		}finally{
			if(connection!=null) connection.close();
		}		
	}
	
	public ILocationModel getLocation(final String street, final String apartment,final List zipCodes) throws SQLException {
		 final ILocationModel model = new LocationModel();	         
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
		     public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		    	 
		    	 boolean hasApartment = apartment!=null && !"".equals(apartment.trim());
		    	 StringBuffer locationQ = new StringBuffer();
		    	 locationQ.append(GET_LOCATION_QRY);
		    	 
		    	 if (hasApartment) {
		    		 locationQ.append("AND UPPER(dl.APARTMENT) = REPLACE(REPLACE(UPPER(?),'-'),' ') ");
		 		 } else {
		 			locationQ.append("AND dl.APARTMENT IS NULL ");
		 		 }
		    	 
		    	 locationQ.append("AND db.ZIP ").append(RoutingUtil.getQueryParam(zipCodes));	
		    	 //System.out.println("locationQ >"+locationQ+" "+zipCodes);
		    	 int intCount = 1;
		         PreparedStatement ps =
		             connection.prepareStatement(locationQ.toString());
		         ps.setString(intCount++,street);
		         if(hasApartment) {
		        	 ps.setString(intCount++,apartment);
		         }
		         //ps.setString(intCount++,zipCode);
		         return ps;
		     }  
		 };
		 
		 jdbcTemplate.query(creator, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {
				    	
				    	do {        		    		
				    		model.setStreetAddress1(rs.getString("SCRUBBED_STREET"));
				    		model.setApartmentNumber(rs.getString("APARTMENT"));
				    		model.setZipCode(rs.getString("ZIP"));
				    		model.setState(rs.getString("STATE"));
				    		model.setCity(rs.getString("CITY"));
				    		model.setCountry(rs.getString("COUNTRY"));
				    		model.setLocationId(rs.getString("ID"));
				    		String serviceTimeType = rs.getString("SERVICETIME_TYPE");
				    		if(serviceTimeType == null || serviceTimeType.trim().length() ==0) {
				    			serviceTimeType = rs.getString("DEFAULTSERVICETIME_TYPE");
				    		}
				    		model.setServiceTimeType(serviceTimeType);
				    		model.setBuildingId(rs.getString("BUILDING_ID"));
				    		
				    		IGeographicLocation geoLoc = new GeographicLocation();
				    		geoLoc.setLatitude(rs.getString("LATITUDE"));
				    		geoLoc.setLongitude(rs.getString("LONGITUDE"));
				    		geoLoc.setConfidence(rs.getString("GEO_CONFIDENCE"));
				    		geoLoc.setQuality(rs.getString("GEO_QUALITY"));
				    		model.setGeographicLocation(geoLoc);
				    	 } while(rs.next());		        		    	
				      }
				  }
			); 
		 
		return model;
	}
	
	public List getLocationByIds(final List locIds) throws SQLException {
		 final List modelList = new ArrayList();	         
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
		     public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		    	 
		    	 StringBuffer locationQ = new StringBuffer();
		    	 locationQ.append(GET_LOCATION_BYID_QRY);
		    	 
		    	 Iterator tmpIterator = locIds.iterator();
		    	 int intCount = 0;
		    	 while(tmpIterator.hasNext()) {
		    		 locationQ.append("'").append(tmpIterator.next()).append("'");
		    		 intCount++;
		    		 if(intCount != locIds.size()) {
		    			 locationQ.append("'").append(tmpIterator.next()).append(","); 
		    		 }
		    	 }
		    	 locationQ.append(")");
		         PreparedStatement ps =
		             connection.prepareStatement(locationQ.toString());
		         
		         return ps;
		     }  
		 };
		 
		 jdbcTemplate.query(creator, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {
				    	
				    	do { 
				    		ILocationModel model = new LocationModel();
				    		model.setStreetAddress1(rs.getString("SCRUBBED_STREET"));
				    		model.setApartmentNumber(rs.getString("APARTMENT"));
				    		model.setZipCode(rs.getString("ZIP"));
				    		model.setState(rs.getString("STATE"));
				    		model.setCity(rs.getString("CITY"));
				    		model.setCountry(rs.getString("COUNTRY"));
				    		model.setLocationId(rs.getString("ID"));
				    		String serviceTimeType = rs.getString("SERVICETIME_TYPE");
				    		if(serviceTimeType == null || serviceTimeType.trim().length() ==0) {
				    			serviceTimeType = rs.getString("DEFAULTSERVICETIME_TYPE");
				    		}
				    		model.setServiceTimeType(serviceTimeType);
				    		model.setBuildingId(rs.getString("BUILDING_ID"));
				    		
				    		IGeographicLocation geoLoc = new GeographicLocation();
				    		geoLoc.setLatitude(rs.getString("LATITUDE"));
				    		geoLoc.setLongitude(rs.getString("LONGITUDE"));
				    		geoLoc.setConfidence(rs.getString("GEO_CONFIDENCE"));
				    		geoLoc.setQuality(rs.getString("GEO_QUALITY"));
				    		model.setGeographicLocation(geoLoc);
				    		modelList.add(model);
				    	 } while(rs.next());		        		    	
				      }
				  }
			); 
		 
		return modelList;
	}
	
	public IBuildingModel getBuildingLocation(final String street, final List zipCode) throws SQLException {
		 final IBuildingModel model = new BuildingModel();	
		 
		 PreparedStatementCreator creator=new PreparedStatementCreator() {
		     public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		    	 		    	 
		    	 StringBuffer locationQ = new StringBuffer();
		    	 locationQ.append(GET_BUILDING_QRY);
		    	 
		    	 		    	 
		    	 locationQ.append("AND db.ZIP ").append(RoutingUtil.getQueryParam(zipCode));		    	 
		    	 int intCount = 1;
		         PreparedStatement ps =
		             connection.prepareStatement(locationQ.toString());
		         ps.setString(intCount++,street);
		         
		         //ps.setString(intCount++,zipCode);
		         return ps;
		     }  
		 };
		 
		 jdbcTemplate.query(creator, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {
				    	
				    	do {        		    		
				    		model.setSrubbedStreet(rs.getString("SCRUBBED_STREET"));  
				    		model.setZipCode(rs.getString("ZIP"));
				    		model.setCountry(rs.getString("COUNTRY"));
				    		model.setBuildingId(rs.getString("ID"));
				    		model.setServiceTimeType( rs.getString("SERVICETIME_TYPE"));				    		 						    		
				    		IGeographicLocation geoLoc = new GeographicLocation();
				    		geoLoc.setLatitude(rs.getString("LATITUDE"));
				    		geoLoc.setLongitude(rs.getString("LONGITUDE"));
				    		geoLoc.setConfidence(rs.getString("GEO_CONFIDENCE"));
				    		geoLoc.setQuality(rs.getString("GEO_QUALITY"));
				    		model.setGeographicLocation(geoLoc);
				    				    		
				    	 } while(rs.next());		        		    	
				      }
				  }
			); 
		 
		return model;
	}
	
	public String getLocationId() throws SQLException {
		return ""+jdbcTemplate.queryForLong(GET_LOCATIONNEXTSEQ_QRY);//SequenceGenerator.getNextId(jdbcTemplate.getDataSource().getConnection(), "dlv", "DELIVERY_LOCATION_SEQ");
	}
	
	public String getBuildingId() throws SQLException {
		return ""+jdbcTemplate.queryForLong(GET_BUILDINGNEXTSEQ_QRY);//SequenceGenerator.getNextId(jdbcTemplate.getDataSource().getConnection(), "dlv", "DELIVERY_LOCATION_SEQ");
	}
	
	public List getStateList() throws SQLException {
		final List dataList = new ArrayList();	         
		 		 
		 jdbcTemplate.query(GET_STATE_QRY, 
				  new RowCallbackHandler() { 
				      public void processRow(ResultSet rs) throws SQLException {
				    	
				    	do {        		    		
				    		dataList.add(rs.getString("STATE"));
				    	 } while(rs.next());		        		    	
				      }
				  }
			); 
		 
		return dataList;
	}
	
	public IGeographicLocation getLocalGeocode(String srubbedStreet, String apartment, String zipCode) throws SQLException  {
		
		com.freshdirect.delivery.ejb.GeographyDAO dao = new com.freshdirect.delivery.ejb.GeographyDAO();
		IGeographicLocation result = null;
		AddressModel model = new AddressModel();
		model.setAddress1(srubbedStreet);
		//model.setAddress1(srubbedStreet);
		Connection conn = null;
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			String geoResult = dao.geocodeAddress(model, false, false, conn);
			if (com.freshdirect.delivery.ejb.GeographyDAO.GEOCODE_OK.equals(geoResult)) {
				result = new GeographicLocation();
				result.setLatitude(""+model.getAddressInfo().getLatitude());
				result.setLongitude(""+model.getAddressInfo().getLongitude());
				result.setConfidence(EnumGeocodeConfidenceType.LOW.getName());
				if(model.getAddressInfo().isGeocodeException()) {					
					result.setQuality(EnumGeocodeQualityType.STOREFRONTEXCEPTION.getName());					
				} else {
					result.setQuality(EnumGeocodeQualityType.STOREFRONTGEOCODE.getName());
				}
			}
		} catch (InvalidAddressException exp) {
			exp.printStackTrace();
		} finally {
			if(conn != null) {
				conn.close();
			}
		}
		return result;
	}
}
