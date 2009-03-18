package com.freshdirect.routing.util.extractor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import com.freshdirect.routing.model.GeographicLocation;
import com.freshdirect.routing.model.IGeographicLocation;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.util.RoutingUtil;
import com.freshdirect.routing.util.extractor.model.ExtractAddressModel;

public class DataExtractorDAO {
	
	public static List getAddressByOrderDate(Connection connection, String date) throws Exception {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		List dataList = new ArrayList();
		try {
						
			System.out
					.println("----------------------START QUERY--------------------------");
			st = connection.prepareStatement("select * from cust.address@DBSTOSBY.NYC.FRESHDIRECT.COM a where a.CUSTOMER_ID in " +
					" (select distinct s.CUSTOMER_ID from cust.sale@DBSTOSBY.NYC.FRESHDIRECT.COM s where s.CROMOD_DATE > ?)");
			
			st.setString(1, date);
			
			rs = st.executeQuery();
			System.out
					.println("----------------------END QUERY--------------------------");
			ExtractAddressModel address = null;
			

			while (rs.next()) {
				address = new ExtractAddressModel();
				address.setId(rs.getString("ID"));
				address.setRefId(rs.getString("CUSTOMER_ID"));
				//Just for Method Ref
				address.setZone(null);
				address.setArea(null);
				address.setAddress1(rs.getString("ADDRESS1"));
				address.setAddress2(rs.getString("ADDRESS2"));
				address.setApartment(rs.getString("APARTMENT"));
				address.setCity(rs.getString("CITY"));
				address.setState(rs.getString("STATE"));
				address.setZip(rs.getString("ZIP"));
				address.setCountry(rs.getString("COUNTRY"));
				address.setScrubbedAddress(rs.getString("SCRUBBED_ADDRESS"));
				address.setRefAddressId(null);
				address.setDeliveryType(rs.getString("SERVICE_TYPE"));
				
				
				dataList.add(address);
			}
			
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
		}
		return dataList;
	}
	
	public static List getLowQualityBuildings(Connection connection) throws Exception {
		
		Statement st = null;
		ResultSet rs = null;
		List dataList = new ArrayList();
		try {
			
			st = connection.createStatement();
			System.out
					.println("----------------------START QUERY--------------------------");
			rs = st.executeQuery("SELECT * FROM DLV.DELIVERY_BUILDING  where GEO_CONFIDENCE <> 'gcHigh'");
			System.out
					.println("----------------------END QUERY--------------------------");
			ExtractAddressModel address = null;
			

			while (rs.next()) {
				address = new ExtractAddressModel();
				address.setAddress1(rs.getString("SCRUBBED_STREET"));
				address.setCity(rs.getString("CITY"));
				address.setState(rs.getString("STATE"));
				address.setZip(rs.getString("ZIP"));
				address.setCountry(rs.getString("COUNTRY"));								
				address.setId(rs.getString("ID"));
				
				dataList.add(address);
			}
			
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
		}
		return dataList;
	}
	
	private static final String GET_BUILDING_QRY = "select db.ID ID, db.SCRUBBED_STREET SCRUBBED_STREET, "
			+ "db.COUNTRY COUNTRY, db.ZIP ZIP, "
			+ "db.LONGITUDE LONGITUDE, db.LATITUDE LATITUDE, db.SERVICETIME_TYPE SERVICETIME_TYPE,"
			+ "db.GEO_CONFIDENCE GEO_CONFIDENCE, db.GEO_QUALITY GEO_QUALITY "
			+ "from dlv.STAGGING_DELIVERY_BUILDING db "
			+ "where db.SCRUBBED_STREET = ? ";
	
	public static String getBuildingId(Connection connection, String street, String zipCode) throws Exception {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String buildingId = null;
		try {
			StringBuffer buildingQ = new StringBuffer();
			buildingQ.append(GET_BUILDING_QRY);
			buildingQ.append("AND db.ZIP ").append(RoutingUtil.getQueryParam(RoutingUtil.getZipCodes(zipCode)));
	    	 
			ps = connection.prepareStatement(buildingQ.toString());
			ps.setString(1,street);
						
			rs = ps.executeQuery();
			
			while (rs.next()) {
				buildingId = rs.getString("ID");
				break;
			}
			
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
		return buildingId;
	}
	
	private static final String INSERT_LOCATION_INFORMATION = "INSERT INTO DLV.DELIVERY_LOCATION ( ID, BUILDINGID, "
		+ "APARTMENT, SERVICETIME_TYPE) VALUES ( "
		+ "DLV.DELIVERY_LOCATION_SEQ.nextval,?,?,?)";

	public static void insertLocations(List dataList, DataSource dataSource)
		throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate batchUpdater = new BatchSqlUpdate(dataSource,
					INSERT_LOCATION_INFORMATION);
			// batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
	
			batchUpdater.compile();
	
			Iterator iterator = dataList.iterator();
			connection = dataSource.getConnection();
			ExtractAddressModel address = null;
			
			while (iterator.hasNext()) {
	
				address = (ExtractAddressModel)iterator.next();
				
				batchUpdater.update(new Object[] { address.getRefAddressId(),
						(address.getApartment() != null && address.getApartment().trim().length() > 0 
												? address.getApartment() : null)
													,null });
			}
			batchUpdater.flush();
		} finally {
			if (connection != null)
				connection.close();
		}
	}
	
	private static final String GET_LOCATION_QRY = "select dl.ID ID, db.SCRUBBED_STREET SCRUBBED_STREET, "+
	"dl.APARTMENT APARTMENT,db.CITY CITY, db.STATE STATE, db.COUNTRY COUNTRY, db.ZIP ZIP, " +
	"db.LONGITUDE LONGITUDE, db.LATITUDE LATITUDE, dl.SERVICETIME_TYPE SERVICETIME_TYPE, "+
	"db.SERVICETIME_TYPE DEFAULTSERVICETIME_TYPE, dl.BUILDINGID BUILDING_ID, "+
	"db.GEO_CONFIDENCE GEO_CONFIDENCE, db.GEO_QUALITY GEO_QUALITY "+
	"from dlv.DELIVERY_LOCATION dl, dlv.DELIVERY_BUILDING db "+
	"where dl.BUILDINGID = db.ID and db.SCRUBBED_STREET = ? ";
	
	public ILocationModel hasLocation(Connection connection, final String street, final String apartment,final List zipCodes) throws SQLException {
		
		 ILocationModel model = null;
		 PreparedStatement ps = null;
		 ResultSet rs = null;
		 boolean hasApartment = apartment!=null && !"".equals(apartment.trim());
    	 StringBuffer locationQ = new StringBuffer();
    	 locationQ.append(GET_LOCATION_QRY);

    	 if (hasApartment) {
    		 locationQ.append("AND UPPER(dl.APARTMENT) = UPPER(?) ");
 		 } else {
 			locationQ.append("AND dl.APARTMENT IS NULL ");
 		 }

    	 locationQ.append("AND db.ZIP ").append(RoutingUtil.getQueryParam(zipCodes));

    	 int intCount = 1;
    	 try {
	         ps =
	             connection.prepareStatement(locationQ.toString());
	         ps.setString(intCount++,street);
	         if(hasApartment) {
	        	 ps.setString(intCount++,apartment);
	         }
	         
	         rs = ps.executeQuery();
				
				while (rs.next()) {
					model = new LocationModel();
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
				}
    	 } finally {
    		 if (rs != null)
 				rs.close();
 			if (ps != null)
 				ps.close();
    	 }

		return model;
	}
	
	
}
