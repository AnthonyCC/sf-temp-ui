package com.freshdirect.transadmin.dao.oracle;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import com.freshdirect.transadmin.constants.EnumAssetStatus;
import com.freshdirect.transadmin.dao.IYardManagerDAO;
import com.freshdirect.transadmin.model.ParkingLocation;
import com.freshdirect.transadmin.model.ParkingSlot;
import com.freshdirect.transadmin.model.UPSRouteInfo;

public class YardManagerDAO implements IYardManagerDAO   {
	
	private JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
	private static final String GET_PARKINGLOCATION = " SELECT * FROM TRANSP.PARKING_LOCATION ";
	
	private static final String INSERT_PARKING_LOCATION = "INSERT INTO TRANSP.PARKING_LOCATION ( LOCATION_NAME, "+
			" LOCATION_DESCRIPTION ) VALUES ( ?,? )";
	
	private static final String CLEAR_PARKINGLOCATION = "DELETE FROM TRANSP.PARKING_LOCATION WHERE LOCATION_NAME in ( ";
	
	private static final String INSERT_PARKING_SLOT = "INSERT INTO TRANSP.PARKING_LOCATIONSLOT "
		+" ( SLOT_NUMBER, SLOT_DESCRIPTION, STATUS, PARKINGLOCATION_ID ) VALUES ( ?,?,?,? ) ";
	
	private static final String GET_PARKINGLOCATION_SLOTS = "SELECT * FROM TRANSP.PARKING_LOCATION PL, TRANSP.PARKING_LOCATIONSLOT PLS "
		+ " where PL.LOCATION_NAME = PLS.PARKINGLOCATION_ID ";
	
	private static final String GET_PARKINGSLOTSBY_LOCATION = "SELECT * FROM TRANSP.PARKING_LOCATION PL, TRANSP.PARKING_LOCATIONSLOT PLS "
		+ " where PL.LOCATION_NAME = PLS.PARKINGLOCATION_ID and PL.LOCATION_NAME = ? ";
	
	private static final String GET_ASSETS = " SELECT ASSET_NO, ASSET_STATUS FROM TRANSP.ASSET WHERE ASSET_TYPE in ( ";
	
	private static final String GET_ROUTE_FIRSTSTOP_WINDOW = "SELECT R.ROUTE_NO ROUTE_NUMBER, " +
         	" (select S.WINDOW_STARTTIME from TRANSP.HANDOFF_BATCHSTOP s where S.BATCH_ID = B.BATCH_ID and S.ROUTE_NO = R.ROUTE_NO and " +
         	" S.STOP_SEQUENCE = (select min(STOP_SEQUENCE) from TRANSP.HANDOFF_BATCHSTOP sx where Sx.BATCH_ID = B.BATCH_ID and Sx.ROUTE_NO = R.ROUTE_NO) ) ROUTE_FIRSTSTOP_STARTTIME "+ 
         	" from TRANSP.HANDOFF_BATCHROUTE R, TRANSP.HANDOFF_BATCH b where B.DELIVERY_DATE = ? and B.BATCH_ID = R.BATCH_ID and B.BATCH_STATUS IN ('CPD/ADC','CPD','CPD/ADF') "+ 
         	" and R.ROUTE_NO = ? ";
	
	public void addParkingLocation(Set<ParkingLocation> locations) throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate batchUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), INSERT_PARKING_LOCATION);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));	
			
			batchUpdater.compile();			
			connection = this.jdbcTemplate.getDataSource().getConnection();
		
			Iterator<ParkingLocation> _slotItr = locations.iterator();				
			while(_slotItr.hasNext()) {
				ParkingLocation model = _slotItr.next();
				batchUpdater.update(new Object[]{ model.getLocationName()
														, model.getLocationDesc()
				});
			}			
			batchUpdater.flush();
		}finally{
			if(connection!=null) connection.close();
		}
	}
	
	public void clearParkingLocation(Set<String> loactionLst) throws SQLException {
		
		Connection connection = null;		
		try {
			final StringBuffer updateQ = new StringBuffer();
			updateQ.append(CLEAR_PARKINGLOCATION);
			int intCount = 0;
			for(String locName : loactionLst) {
				updateQ.append("'").append(locName).append("'");
				intCount++;
				if(intCount != loactionLst.size()) {
					updateQ.append(",");
				}
			}
			updateQ.append(")");
			this.jdbcTemplate.update(updateQ.toString(), new Object[] {});
			connection = this.jdbcTemplate.getDataSource().getConnection();	
			
		} finally {
			if(connection!=null) connection.close();
		}
	}
	
	public Map<String, ParkingLocation> getParkingLocation() throws SQLException {
		
		final Map<String, ParkingLocation> result = new HashMap<String, ParkingLocation>();
		
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
					PreparedStatement ps =
						connection.prepareStatement(GET_PARKINGLOCATION);
					return ps;
				}  
			};
			
			jdbcTemplate.query(creator, 
					new RowCallbackHandler() { 
						public void processRow(ResultSet rs) throws SQLException {				    	
							do { 
								ParkingLocation model = new ParkingLocation(rs.getString("LOCATION_NAME")
																				, rs.getString("LOCATION_DESCRIPTION"));								
								result.put(rs.getString("LOCATION_NAME"), model);
							} while(rs.next());
						}
					}
			);			
			return result;			
			
	}
	
	public void addParkingSlot(ParkingSlot slot) throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate batchUpdater = new BatchSqlUpdate(this.jdbcTemplate.getDataSource(), INSERT_PARKING_SLOT);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));	
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));	
			
			batchUpdater.compile();			
			connection = this.jdbcTemplate.getDataSource().getConnection();
			batchUpdater.update(new Object[]{ slot.getSlotNumber(), slot.getSlotDesc(), slot.getStatus()
														, slot.getLocation().getLocationName()});
			
			batchUpdater.flush();
		}finally{
			if(connection!=null) connection.close();
		}
	}
	
	public List<ParkingSlot> getParkingSlot(final String parkingLocName) throws SQLException {
		final List<ParkingSlot> result = new ArrayList<ParkingSlot>();
		final StringBuffer updateQ = new StringBuffer();
		if (parkingLocName == null || "".equalsIgnoreCase(parkingLocName)) {
			updateQ.append(GET_PARKINGLOCATION_SLOTS);
		} else {
			updateQ.append(GET_PARKINGSLOTSBY_LOCATION);
		}
		Connection connection = null;
		try {
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(
						Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(updateQ.toString());
					if(parkingLocName != null)ps.setString(1, parkingLocName);
					return ps;
				}
			};
			jdbcTemplate.query(creator, new RowCallbackHandler() {
				public void processRow(ResultSet rs) throws SQLException {
					do {
						ParkingSlot slot = new ParkingSlot();
						slot.setSlotNumber(rs.getString("SLOT_NUMBER"));
						slot.setSlotDesc(rs.getString("SLOT_DESCRIPTION"));
						slot.setStatus(rs.getString("STATUS"));
						slot.setLocation(new ParkingLocation(rs
								.getString("LOCATION_NAME"), rs
								.getString("LOCATION_DESCRIPTION")));
						result.add(slot);
					} while (rs.next());
				}
			});
			return result;
		} finally {
			if (connection != null)	connection.close();
		}	
	}
	
	public Map<String, EnumAssetStatus> getAssets(final List<String> assetTypes) throws SQLException {
		
		final Map<String, EnumAssetStatus> result = new HashMap<String, EnumAssetStatus>();
		final StringBuffer updateQ = new StringBuffer();
		if(assetTypes != null && assetTypes.size() > 0) {			
			updateQ.append(GET_ASSETS);
			int intCount = 0;
			for(String assetType : assetTypes) {
				updateQ.append("'").append(assetType).append("'");
				intCount++;
				if(intCount != assetTypes.size()) {
					updateQ.append(",");
				}
			}
			updateQ.append(")");			
		}
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
					PreparedStatement ps =
						connection.prepareStatement(updateQ.toString());
					return ps;
				}  
			};
			
			jdbcTemplate.query(creator, 
					new RowCallbackHandler() { 
						public void processRow(ResultSet rs) throws SQLException {				    	
							do { 
								if(!result.containsKey(rs.getString("ASSET_NO"))){
									result.put(rs.getString("ASSET_NO"), EnumAssetStatus.getEnum(rs.getString("ASSET_STATUS")));
								}
							} while(rs.next());
						}
					}
			);			
			return result;	
	}
	
	public UPSRouteInfo getUPSRouteInfo(final Date deliveryDate, final String routeNo) throws SQLException {
		final UPSRouteInfo _routeInfo = new UPSRouteInfo();
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

				PreparedStatement ps = connection.prepareStatement(GET_ROUTE_FIRSTSTOP_WINDOW);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				ps.setString(2, routeNo);
				return ps;
			}
		};
		
		jdbcTemplate.query(creator, new RowCallbackHandler() { 
			public void processRow(ResultSet rs) throws SQLException {				    	
				do {
					_routeInfo.setRouteNumber(rs.getString("ROUTE_NUMBER"));					
					_routeInfo.setFirstStopTime(rs.getTimestamp("ROUTE_FIRSTSTOP_STARTTIME"));
				} while(rs.next());		        		    	
			}
		}
		);
		return _routeInfo;
	}	
	
}
