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

import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.routing.constants.EnumArithmeticOperator;
import com.freshdirect.routing.constants.EnumGeocodeConfidenceType;
import com.freshdirect.routing.constants.EnumGeocodeQualityType;
import com.freshdirect.routing.dao.IGeographyDAO;
import com.freshdirect.routing.model.AreaModel;
import com.freshdirect.routing.model.BuildingModel;
import com.freshdirect.routing.model.GeographicLocation;
import com.freshdirect.routing.model.IAreaModel;
import com.freshdirect.routing.model.IBuildingModel;
import com.freshdirect.routing.model.IGeographicLocation;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.IServiceTimeTypeModel;
import com.freshdirect.routing.model.IZoneModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.model.ServiceTimeTypeModel;
import com.freshdirect.routing.model.ZoneModel;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.util.AddressScrubber;
import com.freshdirect.routing.util.RoutingUtil;

public class GeographyDAO extends BaseDAO implements IGeographyDAO  {

	private static final String GET_LOCATION_QRY = "select dl.ID ID, db.SCRUBBED_STREET SCRUBBED_STREET, "+
	"dl.APARTMENT APARTMENT,db.CITY CITY, db.STATE STATE, db.COUNTRY COUNTRY, db.ZIP ZIP, " +
	"db.LONGITUDE LONGITUDE, db.LATITUDE LATITUDE, " +
	"dl.SERVICETIME_TYPE LOC_SERVICETIME_TYPE, dl.SERVICETIME_OVERRIDE LOC_SERVICETIME_OVERRIDE," +
	"dl.SERVICETIME_OPERATOR LOC_SERVICETIME_OPERATOR, dl.SERVICETIME_ADJUSTMENT LOC_SERVICETIME_ADJUSTMENT," +
	"db.SERVICETIME_TYPE BLD_SERVICETIME_TYPE, db.SERVICETIME_OVERRIDE BLD_SERVICETIME_OVERRIDE," +
	"db.SERVICETIME_OPERATOR BLD_SERVICETIME_OPERATOR, db.SERVICETIME_ADJUSTMENT BLD_SERVICETIME_ADJUSTMENT," +
	"dl.BUILDINGID BUILDING_ID, db.GEO_CONFIDENCE GEO_CONFIDENCE, db.GEO_QUALITY GEO_QUALITY "+
	"from dlv.DELIVERY_LOCATION dl, dlv.DELIVERY_BUILDING db "+
	"where dl.BUILDINGID = db.ID and db.SCRUBBED_STREET = ? ";

	private static final String GET_LOCATION_BYIDS_QRY = "select dl.ID ID, db.SCRUBBED_STREET SCRUBBED_STREET, "+
	"dl.APARTMENT APARTMENT,db.CITY CITY, db.STATE STATE, db.COUNTRY COUNTRY, db.ZIP ZIP, " +
	"db.LONGITUDE LONGITUDE, db.LATITUDE LATITUDE, " +
	"dl.SERVICETIME_TYPE LOC_SERVICETIME_TYPE, dl.SERVICETIME_OVERRIDE LOC_SERVICETIME_OVERRIDE," +
	"dl.SERVICETIME_OPERATOR LOC_SERVICETIME_OPERATOR, dl.SERVICETIME_ADJUSTMENT LOC_SERVICETIME_ADJUSTMENT," +
	"db.SERVICETIME_TYPE BLD_SERVICETIME_TYPE, db.SERVICETIME_OVERRIDE BLD_SERVICETIME_OVERRIDE," +
	"db.SERVICETIME_OPERATOR BLD_SERVICETIME_OPERATOR, db.SERVICETIME_ADJUSTMENT BLD_SERVICETIME_ADJUSTMENT," +
	"dl.BUILDINGID BUILDING_ID, db.GEO_CONFIDENCE GEO_CONFIDENCE, db.GEO_QUALITY GEO_QUALITY "+
	"from dlv.DELIVERY_LOCATION dl, dlv.DELIVERY_BUILDING db "+
	"where dl.BUILDINGID = db.ID and dl.ID in (";

	private static final String GET_LOCATION_BYID_QRY = "select dl.ID ID, db.SCRUBBED_STREET SCRUBBED_STREET, "+
	"dl.APARTMENT APARTMENT,db.CITY CITY, db.STATE STATE, db.COUNTRY COUNTRY, db.ZIP ZIP, " +
	"db.LONGITUDE LONGITUDE, db.LATITUDE LATITUDE, " +
	"dl.SERVICETIME_TYPE LOC_SERVICETIME_TYPE, dl.SERVICETIME_OVERRIDE LOC_SERVICETIME_OVERRIDE," +
	"dl.SERVICETIME_OPERATOR LOC_SERVICETIME_OPERATOR, dl.SERVICETIME_ADJUSTMENT LOC_SERVICETIME_ADJUSTMENT," +
	"db.SERVICETIME_TYPE BLD_SERVICETIME_TYPE, db.SERVICETIME_OVERRIDE BLD_SERVICETIME_OVERRIDE," +
	"db.SERVICETIME_OPERATOR BLD_SERVICETIME_OPERATOR, db.SERVICETIME_ADJUSTMENT BLD_SERVICETIME_ADJUSTMENT," +
	"dl.BUILDINGID BUILDING_ID, db.GEO_CONFIDENCE GEO_CONFIDENCE, db.GEO_QUALITY GEO_QUALITY "+
	"from dlv.DELIVERY_LOCATION dl, dlv.DELIVERY_BUILDING db "+
	"where dl.BUILDINGID = db.ID and dl.ID = ?";

	private static final String GET_LOCATIONNEXTSEQ_QRY = "SELECT DLV.DELIVERY_LOCATION_SEQ.nextval FROM DUAL";

	private static final String GET_BUILDINGNEXTSEQ_QRY = "SELECT DLV.DELIVERY_BUILDING_SEQ.nextval FROM DUAL";

	private static final String GET_STATE_QRY = "SELECT DISTINCT(STATE) STATE FROM DLV.CITY_STATE";

	private static final String INSERT_LOCATION_INFORMATION = "INSERT INTO DLV.DELIVERY_LOCATION ( ID,"+
	"APARTMENT, BUILDINGID) VALUES ( "+
	"?,?,?)";

	private static final String INSERT_BUILDING_INFORMATION = "INSERT INTO DLV.DELIVERY_BUILDING ( ID,"+
	"SCRUBBED_STREET, ZIP, COUNTRY, LONGITUDE , LATITUDE , GEO_CONFIDENCE " +
	", GEO_QUALITY, CITY, STATE ) VALUES ( "+
	"?,?,?,?,?,?,?,?,?,?)";

	private static final String GET_BUILDING_QRY = "select db.ID ID, db.SCRUBBED_STREET SCRUBBED_STREET, "+
	"db.COUNTRY COUNTRY, db.ZIP ZIP, db.CITY CITY, db.STATE STATE, " +
	"db.LONGITUDE LONGITUDE, db.LATITUDE LATITUDE," +
	"db.SERVICETIME_TYPE BLD_SERVICETIME_TYPE, db.SERVICETIME_OVERRIDE BLD_SERVICETIME_OVERRIDE," +
	"db.SERVICETIME_OPERATOR BLD_SERVICETIME_OPERATOR, db.SERVICETIME_ADJUSTMENT BLD_SERVICETIME_ADJUSTMENT," +
	"db.GEO_CONFIDENCE GEO_CONFIDENCE, db.GEO_QUALITY GEO_QUALITY "+
	"from dlv.DELIVERY_BUILDING db "+
	"where db.SCRUBBED_STREET = ? ";

	private static final String ZONE_AREA_MAPPING =
		"select z.zone_code ZONE_CODE, a.code AREA_CODE, a.is_depot IS_DEPOT "
		+ "from dlv.region r, dlv.region_data rd, dlv.zone z, transp.zone  zd, transp.trn_area a "
		+ "where zd.zone_code = z.zone_code and rd.id = z.region_data_id and rd.region_id = r.id and zd.area = a.code "
		+ "and rd.start_date = (select max(start_date) from dlv.region_data where start_date <= sysdate and region_id=r.id) "
		+ "and mdsys.sdo_relate(z.geoloc, mdsys.sdo_geometry(2001, 8265, mdsys.sdo_point_type(?, ?,NULL), NULL, NULL), 'mask=ANYINTERACT querytype=WINDOW') ='TRUE' "
		+ "order by z.zone_code";
	
	private static final String INSERT_LOCATION_INFORMATION_NOKEY = "INSERT INTO DLV.DELIVERY_LOCATION ( ID,"+
																		"APARTMENT, BUILDINGID) VALUES ( "+
																		 "DLV.DELIVERY_LOCATION_SEQ.nextval,?,?)";

	private static final String INSERT_BUILDING_INFORMATION_NOKEY = "INSERT INTO DLV.DELIVERY_BUILDING ( ID,"+
																		"SCRUBBED_STREET, ZIP, COUNTRY, LONGITUDE , LATITUDE , GEO_CONFIDENCE " +
																		", GEO_QUALITY, CITY, STATE ) VALUES ( "+
																		 "DLV.DELIVERY_BUILDING_SEQ.nextval,?,?,?,?,?,?,?,?,?)";
	
	public void insertLocation(ILocationModel model) throws SQLException {
		
		Connection connection=null;
		try{
			this.jdbcTemplate.update(INSERT_LOCATION_INFORMATION_NOKEY, new Object[] {model.getApartmentNumber()
																						, model.getBuilding().getBuildingId()});
			
			connection=this.jdbcTemplate.getDataSource().getConnection();	
			
		}finally{
			if(connection!=null) connection.close();
		}
	}

	public void insertBuilding(IBuildingModel model) throws SQLException {
		Connection connection=null;
		try{
						
			IGeographicLocation location = null;

			String latitude = null;
			String longitude = null;
			String confidence = null;
			String quality = null;

			location = model.getGeographicLocation();

			if(location != null) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				confidence = location.getConfidence();
				quality = location.getQuality();
			}
			
			this.jdbcTemplate.update(INSERT_BUILDING_INFORMATION_NOKEY
						, new Object[] {model.getSrubbedStreet(),model.getZipCode()
											, model.getCountry()
											, longitude, latitude, confidence, quality
											, model.getCity(), model.getState()});
			connection=this.jdbcTemplate.getDataSource().getConnection();
			
		}finally{
			if(connection!=null) connection.close();
		}
	}
	
	public void insertLocations(List dataList) throws SQLException {
		Connection connection=null;
		try{
			BatchSqlUpdate batchUpdater=new BatchSqlUpdate(this.jdbcTemplate.getDataSource(),INSERT_LOCATION_INFORMATION);
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
								model.getApartmentNumber(), model.getBuilding().getBuildingId()}
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
								model.getCountry(), longitude, latitude, confidence, quality
								, model.getCity(), model.getState()}
				);
			}
			batchUpdater.flush();
		}finally{
			if(connection!=null) connection.close();
		}
	}

	public ILocationModel getLocation(final String street, final String apartment,final List zipCodes) throws SQLException {
		
		final List<ILocationModel> results = new ArrayList<ILocationModel>();
		PreparedStatementCreator creator=new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

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
					
					results.add(getLocationFromResultSet(rs));
					break;
				} while(rs.next());
			}
		}
		);

		return results.size() > 0 ? results.get(0) : null;
	}

	public List getLocationByIds(final List locIds) throws SQLException {
		
		final List<ILocationModel> results = new ArrayList<ILocationModel>();
		PreparedStatementCreator creator=new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

				StringBuffer locationQ = new StringBuffer();
				locationQ.append(GET_LOCATION_BYIDS_QRY);

				Iterator tmpIterator = locIds.iterator();
				int intCount = 0;
				while(tmpIterator.hasNext()) {
					locationQ.append("'").append(tmpIterator.next()).append("'");
					intCount++;
					if(intCount != locIds.size()) {
						locationQ.append(",");
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
					results.add(getLocationFromResultSet(rs));
				} while(rs.next());
			}
		}
		);

		return results;
	}
	
	public ILocationModel getLocationById(final String locId) throws SQLException {
		final List<ILocationModel> results = new ArrayList<ILocationModel>();
		PreparedStatementCreator creator=new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {


				PreparedStatement ps =
					connection.prepareStatement(GET_LOCATION_BYID_QRY);

				ps.setString(1, locId);

				return ps;
			}
		};

		jdbcTemplate.query(creator,
				new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {

				do {

					results.add(getLocationFromResultSet(rs));
					break;
				} while(rs.next());
			}
		}
		);

		return results.size() > 0 ? results.get(0) : null;
	}
	private ILocationModel getLocationFromResultSet(ResultSet rs) throws SQLException {
		
		IBuildingModel bmodel = new BuildingModel();		
		
		ILocationModel model = new LocationModel(bmodel);
		model.setLocationId(rs.getString("ID"));
		model.setApartmentNumber(rs.getString("APARTMENT"));
		
		IGeographicLocation geoLoc = new GeographicLocation();
		geoLoc.setLatitude(rs.getString("LATITUDE"));
		geoLoc.setLongitude(rs.getString("LONGITUDE"));
		geoLoc.setConfidence(rs.getString("GEO_CONFIDENCE"));
		geoLoc.setQuality(rs.getString("GEO_QUALITY"));
				
		bmodel.setGeographicLocation(geoLoc);
		
		bmodel.setSrubbedStreet(rs.getString("SCRUBBED_STREET"));
		bmodel.setStreetAddress1(rs.getString("SCRUBBED_STREET"));
		bmodel.setState(rs.getString("STATE"));
		bmodel.setCity(rs.getString("CITY"));
		bmodel.setZipCode(rs.getString("ZIP"));
		bmodel.setCountry(rs.getString("COUNTRY"));
		bmodel.setBuildingId(rs.getString("BUILDING_ID"));

		IServiceTimeTypeModel serviceTimeType = new ServiceTimeTypeModel();
		serviceTimeType.setCode( rs.getString("BLD_SERVICETIME_TYPE"));
		bmodel.setServiceTimeType(serviceTimeType);
		
		bmodel.setAdjustmentOperator(EnumArithmeticOperator.getEnum(rs.getString("BLD_SERVICETIME_OPERATOR")));
		bmodel.setServiceTimeAdjustment(rs.getDouble("BLD_SERVICETIME_ADJUSTMENT"));
		bmodel.setServiceTimeOverride(rs.getDouble("BLD_SERVICETIME_OVERRIDE"));
		
		IServiceTimeTypeModel locServiceTimeType = new ServiceTimeTypeModel();
		locServiceTimeType.setCode( rs.getString("LOC_SERVICETIME_TYPE"));
		model.setServiceTimeType(locServiceTimeType);
		
		model.setAdjustmentOperator(EnumArithmeticOperator.getEnum(rs.getString("LOC_SERVICETIME_OPERATOR")));
		model.setServiceTimeAdjustment(rs.getDouble("LOC_SERVICETIME_ADJUSTMENT"));
		model.setServiceTimeOverride(rs.getDouble("LOC_SERVICETIME_OVERRIDE"));
		
		return model;
	}


	public IBuildingModel getBuildingLocation(final String street, final List zipCode) throws SQLException {
		final IBuildingModel bmodel = new BuildingModel();

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
					IGeographicLocation geoLoc = new GeographicLocation();
					geoLoc.setLatitude(rs.getString("LATITUDE"));
					geoLoc.setLongitude(rs.getString("LONGITUDE"));
					geoLoc.setConfidence(rs.getString("GEO_CONFIDENCE"));
					geoLoc.setQuality(rs.getString("GEO_QUALITY"));
										
					bmodel.setGeographicLocation(geoLoc);
					
					bmodel.setSrubbedStreet(rs.getString("SCRUBBED_STREET"));
					bmodel.setZipCode(rs.getString("ZIP"));
					bmodel.setCountry(rs.getString("COUNTRY"));
					bmodel.setCity(rs.getString("CITY"));
					bmodel.setState(rs.getString("STATE"));
					bmodel.setBuildingId(rs.getString("ID"));

					IServiceTimeTypeModel serviceTimeType = new ServiceTimeTypeModel();
					serviceTimeType.setCode( rs.getString("BLD_SERVICETIME_TYPE"));
					bmodel.setServiceTimeType(serviceTimeType);
					
					bmodel.setAdjustmentOperator(EnumArithmeticOperator.getEnum(rs.getString("BLD_SERVICETIME_OPERATOR")));
					bmodel.setServiceTimeAdjustment(rs.getDouble("BLD_SERVICETIME_ADJUSTMENT"));
					bmodel.setServiceTimeOverride(rs.getDouble("BLD_SERVICETIME_OVERRIDE"));

				} while(rs.next());
			}
		}
		);

		return bmodel;
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
		
		IGeographicLocation result = null;
		AddressModel model = new AddressModel();
		model.setAddress1(srubbedStreet);
		model.setZipCode(zipCode);
		//model.setAddress1(srubbedStreet);
		Connection conn = null;
		try {
			conn = this.jdbcTemplate.getDataSource().getConnection();
			String geoResult = geocodeAddress(model, false, false, conn);
			if (GEOCODE_OK.equals(geoResult)) {
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
		} catch (RoutingServiceException exp) {
			exp.printStackTrace();
		} finally {
			if(conn != null) {
				conn.close();
			}
		}
		return result;
	}

	public String geocodeAddress(AddressModel address, boolean munge, boolean useLocationDB, Connection conn) throws SQLException, RoutingServiceException  {

		String result = null;
		
		result = checkGeocodeExceptions(address, conn);
		if (GEOCODE_OK.equals(result)) {			
			return result;
		}

		result = reallyGeocode(address, conn, false);
		if (GEOCODE_OK.equals(result)) {			
			return result;
		}

		result = reallyGeocode(address, conn, true);
		if (GEOCODE_OK.equals(result)) {			
			return result;
		}



		if (munge) {
			//
			// failed, guess where the '-' is
			//
			String streetAddress = address.getAddress1();
			int numBreak = streetAddress.indexOf(' ');
			String bldgNum = streetAddress.substring(0, numBreak);
			//
			// if the bldgNum is too short, or already contained a '-' and the first attempt failed anyway, this won't help at all
			//
			if (bldgNum.length() < 3 || bldgNum.indexOf('-') > -1)
				return GEOCODE_FAILED;
			//
			// OK, now inset the '-' and try again
			//
			String theRest = streetAddress.substring(numBreak + 1);
			bldgNum = bldgNum.substring(0, bldgNum.length() - 2) + "-" + bldgNum.substring(bldgNum.length() - 2, bldgNum.length());
			address.setAddress1(bldgNum + " " + theRest);
			String resultTmp = reallyGeocode(address, conn, false);

			//LOGGER.debug(address+"\n--------- END GEOCODE BASE3---------------------\n");
			return resultTmp;
		}
		//LOGGER.debug(address+"\n--------- END GEOCODE BASE4---------------------\n");
		return result;
	}

	private String reallyGeocode(AddressModel address, Connection conn
			, boolean reverseDirectionSubstition) throws SQLException, RoutingServiceException {
		String streetAddress = null;
		String oldStreetAddress = address.getAddress1();
		try {
			streetAddress = (reverseDirectionSubstition ? AddressScrubber.standardizeForGeocodeNoSubstitution(address.getAddress1())
					: AddressScrubber.standardizeForGeocode(address.getAddress1()));
//			streetAddress = AddressScrubber.standardizeForGeocode(address.getAddress1());
		} catch (RoutingServiceException iae1) {
			try {
				streetAddress = (reverseDirectionSubstition ? AddressScrubber.standardizeForGeocodeNoSubstitution(address.getAddress2())
						: AddressScrubber.standardizeForGeocode(address.getAddress2()));
//				streetAddress = AddressScrubber.standardizeForGeocode(address.getAddress2());
			} catch (RoutingServiceException iae2) {
				throw iae2;
			}
		}


//		separate street number and street name

		int numBreak = streetAddress.indexOf(' ');
		String streetName = streetAddress.substring(numBreak + 1, streetAddress.length());
		int bldgNumber = Integer.valueOf(streetAddress.substring(0, numBreak)).intValue();

		numBreak = oldStreetAddress.indexOf(' ');
		String bldgString = oldStreetAddress.substring(0, numBreak);

		int newBldgNumber = bldgNumber;
		if (!bldgString.equals("" + bldgNumber)) {
			String clean = AddressScrubber.cleanBldgNum(bldgString);
			try {
				newBldgNumber = Integer.parseInt(clean);
			} catch (NumberFormatException e) {
//				i tried, i dont know what else i can do here, just set it back
				newBldgNumber = bldgNumber;
			}
		}

		String streetNormal = AddressScrubber.compress(streetName);
		StreetSegment segment = null;

		String leftAddressScheme = "";
		String rightAddressScheme = "";
		String[] zipCode = AddressScrubber.getZipCodeFromAddress(address);

		if(zipCode != null) {
			int intLength = zipCode.length;
			for(int intCount=0; intCount<intLength; intCount++) {

//				find row in db for street segment with a zip code

				PreparedStatement ps = null;
				ps = conn.prepareStatement(streetSegQuery);
				ps.setString(1, streetNormal);
				ps.setString(2, zipCode[intCount]);
				ps.setString(3, zipCode[intCount]);
				ps.setInt(4, bldgNumber);
				ps.setInt(5, bldgNumber);
				ps.setInt(6, newBldgNumber);
				ps.setInt(7, newBldgNumber);
				ps.setInt(8, bldgNumber);
				ps.setInt(9, bldgNumber);
				ps.setInt(10, newBldgNumber);
				ps.setInt(11, newBldgNumber);
				ResultSet rs = ps.executeQuery();

//				LOGGER.debug("--------- START REALLY GEOCODE ---------------------\n"+address);
				if (rs.next()) {
//					LOGGER.debug("--------- HAS GEOCODE SEGMENT---------------------");
					segment = new StreetSegment();
					leftAddressScheme = rs.getString("L_ADDRSCH");
					rightAddressScheme = rs.getString("R_ADDRSCH");
					segment.leftNearNum = rs.getInt("L_REFADDR");
					segment.leftFarNum = rs.getInt("L_NREFADDR");

					segment.rightNearNum = rs.getInt("R_REFADDR");
					segment.rightFarNum = rs.getInt("R_NREFADDR");

					segment.leftZipcode = rs.getString("L_POSTCODE");
					segment.rightZipcode = rs.getString("R_POSTCODE");

					segment.nearX = rs.getDouble("COORD");

					if (rs.next()) {
						segment.nearY = rs.getDouble("COORD");
					}

					if (rs.next()) {
						segment.farX = rs.getDouble("COORD");
					}

					if (rs.next()) {
						segment.farY = rs.getDouble("COORD");
					}
				}
				rs.close();
				ps.close();
				if (segment == null) {
//					LOGGER.debug("--------- GEOCODE FAILED ---------------------");
					if(intCount==intLength-1) {
						return GEOCODE_FAILED;
					} else {
						continue;
					}
				}

//				which of the street segments returned and which side of the the street is the correct one?

				if (leftAddressScheme == null || leftAddressScheme.equals("")) {
					leftAddressScheme = "";
					if (rightAddressScheme != null) {
						if (rightAddressScheme.equals("E")) {
							leftAddressScheme = "O";
						}
						if (rightAddressScheme.equals("O")) {
							leftAddressScheme = "E";
						}
					}
				}

				boolean onLeft = false;
				boolean doOffset = true;
				if (leftAddressScheme.equals("E") || leftAddressScheme.equals("O")) {

					if (leftAddressScheme.equals("E") && (newBldgNumber % 2 == 0)) {
						onLeft = true;
					}
					if (leftAddressScheme.equals("O") && (newBldgNumber % 2 == 1)) {
						onLeft = true;
					}
				} else { // Don't do offset.  If can't figure out; always default to right side of street
					doOffset = false;
				}

				double dX = segment.farX - segment.nearX;
				double dY = segment.farY - segment.nearY;
				double distance = Math.sqrt(dX * dX + dY * dY);

				double slope = dY / dX;


//				how far along the segment is the street address?

				double perc = 0.0;
				if (onLeft) {
					perc = ((double) (bldgNumber - segment.leftNearNum)) / ((double) (segment.leftFarNum - segment.leftNearNum));
				} else {
					perc = ((double) (bldgNumber - segment.rightNearNum)) / ((double) (segment.rightFarNum - segment.rightNearNum));
				}

//				Fix boundary conditions
				if (new Double(perc).isNaN() || (perc == Double.NEGATIVE_INFINITY) || (perc == Double.POSITIVE_INFINITY)) {
					perc = 0.5;
				}

//				This is one one corner, offset it by a wee bit.
				if (perc == 0)
					perc = 0.05;

//				On another corner
				if (perc == 1.0)
					perc = 0.95;


//				find the X,Y point on the street

				double pX, pY;
				double newDistance = distance * perc;

				if (new Double(slope).isNaN() || (slope == Double.NEGATIVE_INFINITY) || (slope == Double.POSITIVE_INFINITY)) {
					pX = segment.nearX;
					pY = newDistance + segment.nearY;
				} else if (slope == 0) {
					pX = newDistance + segment.nearX;
					pY = segment.nearY;
				} else {
					if (segment.nearX > segment.farX) {
						pX = -newDistance / Math.sqrt(slope * slope + 1) + segment.nearX;
					} else {
						pX = newDistance / Math.sqrt(slope * slope + 1) + segment.nearX;
					}
					pY = slope * (pX - segment.nearX) + segment.nearY;
				}

//				Now offset this 20 meters
				double newSlope = -1 / slope;

//				Approximate actual offset
				double newX, newY;
				if (doOffset) {
					double offsetDistance;

					if (pX >= segment.farX) {
						offsetDistance = offsetFromStreet * XmetersToDeg;
					} else {
						offsetDistance = -offsetFromStreet * XmetersToDeg;
					}

//					Assign Left is negative
					if (onLeft)
						offsetDistance *= -1;

					if (newSlope < 0)
						offsetDistance *= -1;

					newX = offsetDistance / Math.sqrt(newSlope * newSlope + 1) + pX;

					if (slope == 0.0)
						newY = pY;
					else {
						newY = newSlope * (newX - pX) + pY;
					}

				} else {
					newX = pX;
					newY = pY;
				}

				AddressInfo info = address.getAddressInfo();


				if (info == null) {
					info = new AddressInfo();
				}

				info.setLongitude(newX);
				info.setLatitude(newY);
				
				address.setAddressInfo(info);

				return GEOCODE_OK;
			}
		}
		return GEOCODE_FAILED;
	}
	
	private final static String GEOCODE_EXCEPTIONS = "select * from dlv.geocode_exceptions where scrubbed_address = ? and zipcode = ?";

	private String checkGeocodeExceptions(AddressModel address, Connection conn) throws SQLException, RoutingServiceException {
		String streetAddress = AddressScrubber.standardizeForGeocode(address.getAddress1());

		String result = GEOCODE_FAILED;
		PreparedStatement ps = conn.prepareStatement(GEOCODE_EXCEPTIONS);

		ps.setString(1, streetAddress);
		ps.setString(2, address.getZipCode());

		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			AddressInfo info = address.getAddressInfo() == null ? new AddressInfo() : address.getAddressInfo();
			info.setLatitude(rs.getDouble("LATITUDE"));
			info.setLongitude(rs.getDouble("LONGITUDE"));
			info.setGeocodeException(true);
			address.setAddressInfo(info);
			result = GEOCODE_OK;

		}
		rs.close();
		ps.close();

		return result;
	}

	public List getZoneMapping(final double latitude, final double longitude) throws SQLException {
		final List result = new ArrayList();

		PreparedStatementCreator creator=new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

				PreparedStatement ps =
					connection.prepareStatement(ZONE_AREA_MAPPING);
				ps.setDouble(1, longitude);
				ps.setDouble(2, latitude);
				return ps;
			}
		};

		jdbcTemplate.query(creator,
				new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {

				do {
					IZoneModel zoneModel = new ZoneModel();
					zoneModel.setZoneNumber(rs.getString("ZONE_CODE"));
					IAreaModel areaModel = new AreaModel();
					areaModel.setAreaCode(rs.getString("AREA_CODE"));
					areaModel.setDepot("X".equalsIgnoreCase(rs.getString("IS_DEPOT")));
					zoneModel.setArea(areaModel);

					result.add(zoneModel);				    		

				} while(rs.next());
			}
		}
		);

		return result;
	}

	
	
	public static final String GEOCODE_OK = "GEOCODE_OK";	
	public final static String GEOCODE_FAILED = "GEOCODE_FAILED";
	
	private final static double offsetFromStreet = 10; // meters
	private final static double XmetersToDeg = 1.0 / 84515.0; // meters per degree longitude approx at 40.7 deg latitude
	private final static double YmetersToDeg = 1.0 / 111048.0; // meters per degree latitude approx at 40.7 deg latitude
	
	private final static double slopeAdjustment = YmetersToDeg / XmetersToDeg;
	private final static String streetSegQuery = "SELECT ng.link_id, ng.l_addrsch, ng.r_addrsch, ng.l_nrefaddr, ng.l_refaddr, "
		+ "ng.r_nrefaddr, ng.r_refaddr, ng.l_postcode, ng.r_postcode, "
		+ "gg.column_value AS coord "
		+ "FROM dlv.navtech_geocode ng, TABLE (ng.geoloc.sdo_ordinates) gg "
		+ "WHERE ng.st_normal = ? "
		+ "AND (ng.l_postcode = ? OR ng.r_postcode = ?) "
		+ "AND ((((? BETWEEN ng.l_nrefaddr AND ng.l_refaddr) OR (? BETWEEN ng.l_refaddr AND ng.l_nrefaddr)) "
		+ "AND DECODE (l_addrsch,'M', 1,'E', decode(mod(?,2), 0, 1, 0), 'O', decode(mod(?,2), 1, 1, 0),0) = 1) "
		+ "OR   (((? BETWEEN ng.r_nrefaddr AND ng.r_refaddr) OR (? BETWEEN ng.r_refaddr AND ng.r_nrefaddr)) "
		+ "AND DECODE (r_addrsch,'M', 1,'E', decode(mod(?,2), 0, 1, 0), 'O', decode(mod(?,2), 1, 1, 0),0) = 1)) ";
	
	class StreetSegment {
        
        public long linkId = -1;
        public String street;
        public String leftZipcode;
        public String rightZipcode;
        public int leftNearNum;
        public int leftFarNum;
        public int rightNearNum;
        public int rightFarNum;
        public double nearX;
        public double nearY;
        public double farX;
        public double farY;
        
    }
}
