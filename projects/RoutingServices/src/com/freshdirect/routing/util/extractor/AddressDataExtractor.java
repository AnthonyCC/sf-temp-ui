package com.freshdirect.routing.util.extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.BatchSqlUpdate;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.delivery.AddressScrubber;
import com.freshdirect.delivery.InvalidAddressException;
import com.freshdirect.routing.constants.EnumGeocodeConfidenceType;
import com.freshdirect.routing.constants.EnumGeocodeQualityType;
import com.freshdirect.routing.model.BuildingModel;
import com.freshdirect.routing.model.GeographicLocation;
import com.freshdirect.routing.model.IGeographicLocation;
import com.freshdirect.routing.model.ILocationModel;
import com.freshdirect.routing.model.LocationModel;
import com.freshdirect.routing.proxy.stub.roadnet.Address;
import com.freshdirect.routing.proxy.stub.roadnet.GeocodeData;
import com.freshdirect.routing.proxy.stub.roadnet.GeocodeOptions;
import com.freshdirect.routing.proxy.stub.roadnet.MapArc;
import com.freshdirect.routing.proxy.stub.roadnet.RouteNetPortType;
import com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebService_Service;
import com.freshdirect.routing.proxy.stub.roadnet.RouteNetWebService_ServiceLocator;
import com.freshdirect.routing.service.exception.IIssue;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.RoutingUtil;
import com.freshdirect.routing.util.extractor.model.ExtractAddressModel;

public class AddressDataExtractor {

	private final static String localLookup = "t3://localhost:7001";

	private final static String standByLookup = "t3://appS1.nyc2.freshdirect.com:7001";

	private final static String ADDRESSLIST = "C:\\UPSData\\ADDRESSFULL.ser";

	private final static String ADDRESSDESTINAIONLIST = "C:\\UPSData\\unit\\";

	private final static String ADDRESSMASTERDESTINAIONLIST = "C:\\UPSData\\unitmaster\\";

	private static final String erpInputConfig = "com\\freshdirect\\routing\\util\\extractor\\sapoutboundfilemapping.xml";

	private static final String erpInputFile = "C:\\FreshDirect\\docs\\UPS\\RS_FINAL_12.txt";

	public static void main(String a[]) throws Exception {
		// loadStandByAddress(getConnection(), "C:\\UPSData\\ADDRESSFULL07222008.ser");
		//loadStandByDeliveryInfo(getConnection(), "C:\\UPSData\\ADDRESSDELIVERYINFOFULL_2006_09122008.ser");
		//loadBuildingAddress(getConnection(), "C:\\UPSData\\ADDRESSBUILDINGFULL_10132008.ser");
		//splitFile("C:\\UPSData\\ADDRESSBUILDINGFULL_10132008.ser","C:\\UPSData\\buildingunit\\",10000,"buildingaddress");
		//processBuildingForCityState("C:\\UPSData\\buildingunit\\buildingaddress13.ser", getConnection());
		simpleTest();
		//compareLowQualityBuildings(getConnection(), "C:\\UPSData\\ADDRESSDELIVERYINFOFULL_2005_09112008.ser");
		// countFile("C:\\UPSData\\ADDRESSFULL06262008.ser");
		// loadAddressFromFile();
		// insertCustomer(getDataSource(true,"fddatasource"));
		 //splitFile("C:\\UPSData\\ADDRESSFULL07222008.ser",ADDRESSDESTINAIONLIST,5000,"address");
		 //splitBuildingFile("C:\\UPSData\\ADDRESSFULL07222008.ser", ADDRESSMASTERDESTINAIONLIST,5000);
		// 9, 15 missed
		// loadStandByDeliveryAddress(getConnection(), ADDRESSLIST);
		/*for (int intCount = 1; intCount <= 1; intCount++) {
			System.out.println("START File :" + intCount + " -> "
					+ getCurrentTime());
			processAddressList(ADDRESSMASTERDESTINAIONLIST + "address"	+ intCount + ".ser", getConnection());
			processLocationList(ADDRESSDESTINAIONLIST+"address"+intCount+".ser", getConnection());
			System.out.println("End File :" + intCount + " -> "
					+ getCurrentTime());
			System.gc();
		}*/
		//processLowQualityBuildings(getConnection());
	}

	//compare local quality address with delivery info for last 3 years
	private static void compareLowQualityBuildings(Connection connection, String source) throws Exception {

		try {

			AddressModel address = null;
			ExtractAddressModel deliveryInfoAddress = null;

			List deliveryInfoAddressLst = deSerializeData(source);
			List dataList = loadLowQualityBuildings(connection);

			System.out.println("deliveryInfoAddress LIST:" + deliveryInfoAddressLst.size());
			Iterator tmpIterator = 	deliveryInfoAddressLst.iterator();
			List scrubbedList = new ArrayList();
			while(tmpIterator.hasNext()) {
				deliveryInfoAddress = (ExtractAddressModel)tmpIterator.next();
				scrubbedList.add(deliveryInfoAddress.getScrubbedAddress()+"_"+deliveryInfoAddress.getZip());
			}

			System.out.println("scrubbedList LIST:" + scrubbedList.size());
			System.out.println("ADDRESS LIST:" + dataList.size());
			tmpIterator = 	dataList.iterator();

			List hasOrderList = new ArrayList();
			List hasNoOrderList = new ArrayList();
			String srubbedAddress = null;
			while(tmpIterator.hasNext()) {
				address = (AddressModel)tmpIterator.next();
				srubbedAddress = address.getAddress1()+"_"+address.getZipCode();
				if(scrubbedList.contains(srubbedAddress)) {
					hasOrderList.add(address);
				} else {
					hasNoOrderList.add(address);
				}
			}

			System.out.println("HAS ORDER LIST:" + hasOrderList.size());
			System.out.println("HAS NO ORDER LIST:" + hasNoOrderList.size());


			printAddress(hasOrderList);

		} finally {
			connection.close();
		}
	}

	private static void printAddress(List dataList) {

		Iterator tmpIterator = 	dataList.iterator();

		AddressModel address = null;
		int intCount=0;
		StringBuffer strBuf = new StringBuffer();
		while(tmpIterator.hasNext()) {
			address = (AddressModel)tmpIterator.next();
			strBuf.append("'"+address.getId()+"'");
			if(intCount != dataList.size()-1) {
				strBuf.append(",");
			}
		}
		System.out.println(strBuf.toString());
	}


	private static List loadLowQualityBuildings(Connection connection) throws Exception {

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
			AddressModel address = null;


			while (rs.next()) {
				address = new AddressModel();
				address.setAddress1(rs.getString("SCRUBBED_STREET"));
				address.setZipCode(rs.getString("ZIP"));
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

	private static void processLowQualityBuildings(Connection connection) throws Exception {

		try {

			AddressModel address = null;
			List dataList = loadLowQualityBuildings(connection);

			System.out.println("ADDRESS LIST:" + dataList.size());
			Iterator tmpIterator = 	dataList.iterator();

			ArrayList saveBuildingList = new ArrayList();
			ArrayList saveConfidenceQualityList = new ArrayList();
			IGeographicLocation geoLocation = null;
			BuildingModel model = null;

			while(tmpIterator.hasNext()) {
				address = (AddressModel)tmpIterator.next();
				model = new BuildingModel();
				model.setBuildingId(address.getId());
				geoLocation = new GeographicLocation();
				model.setGeographicLocation(geoLocation);

				IGeographicLocation storeFrontLocationModel = getLocalGeocode(connection, address);
				if (storeFrontLocationModel == null) {
					geoLocation.setConfidence(EnumGeocodeConfidenceType.LOW.getName());
					geoLocation.setQuality(EnumGeocodeQualityType.STOREFRONTUNSUCCESSFULGEOCODE.getName());
					saveConfidenceQualityList.add(model);
				} else {
					geoLocation.setLatitude(storeFrontLocationModel.getLatitude());
					geoLocation.setLongitude(storeFrontLocationModel.getLongitude());
					geoLocation.setConfidence(storeFrontLocationModel.getConfidence());
					geoLocation.setQuality(storeFrontLocationModel.getQuality());
					saveBuildingList.add(model);
				}
			}
			updateBuildingsConfidenceQuality(saveConfidenceQualityList,getDataSource(true,"fddatasource"));
			updateBuildings(saveBuildingList,getDataSource(true,"fddatasource"));

		} finally {
			connection.close();
		}
	}

	private static final String UPDATE_BUILDINGCONFQUAL_INFORMATION = "UPDATE DLV.DELIVERY_BUILDING SET  "
		+ "GEO_CONFIDENCE = ?, GEO_QUALITY = ? WHERE ID = ?";

	public static void updateBuildingsConfidenceQuality(List dataList, DataSource dataSource)
		throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate batchUpdater = new BatchSqlUpdate(dataSource,
					UPDATE_BUILDINGCONFQUAL_INFORMATION);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));

			batchUpdater.compile();

			Iterator iterator = dataList.iterator();
			connection = dataSource.getConnection();

			IGeographicLocation location = null;
			BuildingModel model = null;

			while (iterator.hasNext()) {
				model = (BuildingModel)iterator.next();
				location = model.getGeographicLocation();
				batchUpdater.update(new Object[] { location.getConfidence(), location.getQuality(),
						model.getBuildingId() });
			}
			batchUpdater.flush();
		} finally {
			if (connection != null)
				connection.close();
		}
	}

	private static final String UPDATE_BUILDING_INFORMATION = "UPDATE DLV.DELIVERY_BUILDING SET LATITUDE = ? , LONGITUDE = ? "
		+ " , GEO_CONFIDENCE = ?, GEO_QUALITY = ? WHERE ID = ?";

	public static void updateBuildings(List dataList, DataSource dataSource)
		throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate batchUpdater = new BatchSqlUpdate(dataSource,
					UPDATE_BUILDING_INFORMATION);
			batchUpdater.declareParameter(new SqlParameter(Types.DECIMAL));
			batchUpdater.declareParameter(new SqlParameter(Types.DECIMAL));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));

			batchUpdater.compile();

			Iterator iterator = dataList.iterator();
			connection = dataSource.getConnection();

			IGeographicLocation location = null;
			BuildingModel model = null;

			while (iterator.hasNext()) {
				model = (BuildingModel)iterator.next();
				location = model.getGeographicLocation();
				batchUpdater.update(new Object[] { location.getLatitude(),
						location.getLongitude(), location.getConfidence(), location.getQuality(),
						model.getBuildingId() });
			}
			batchUpdater.flush();
		} finally {
			if (connection != null)
				connection.close();
		}
	}

	public static IGeographicLocation getLocalGeocode(Connection connection, AddressModel model) throws SQLException  {

		com.freshdirect.delivery.ejb.GeographyDAO dao = new com.freshdirect.delivery.ejb.GeographyDAO();
		IGeographicLocation result = null;


		try {
			String geoResult = dao.geocode(model, false, connection);
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
		}
		return result;
	}

	private static void simpleTest() throws Exception {

		Address address = new Address();
		address.setLine1("225 E 63RD ST");
		address.setPostalCode("10065");
		address.setCountry("US");
		RouteNetWebService_Service service = new RouteNetWebService_ServiceLocator();
		RouteNetPortType port = service.getRouteNetWebService(new URL(
				RoutingServicesProperties.getRoadNetProviderURL()));
		GeocodeOptions options = new GeocodeOptions();
		options.setReturnCandidates(true);
		GeocodeData geographicData = port.geocodeEx(address, options);
		MapArc[] candidates = geographicData.getCandidates();
		if(candidates != null) {
			for(int intCount=0; intCount<candidates.length;intCount++) {
				System.out.println(candidates[intCount].getStreetType()+" "+candidates[intCount].getPostalCode()+" "+candidates[intCount].getRegion1());
			}
		}
		System.out.println("GEOCODE :"+geographicData.getQualityDescription()+" "+geographicData.getConfidence().getValue());
	}

	private static void loadAddressFromFile() throws Exception {

		List inputDataList = new RouteFileManager().parseRouteFile(
				erpInputConfig, new FileInputStream(erpInputFile));
		Iterator iterator = inputDataList.iterator();
		OrderInfoModel tmpInputModel = null;
		Address[] addressLst = new Address[inputDataList.size()];
		int intCount = 0;

		ArrayList tmpTrueList = new ArrayList();

		while (iterator.hasNext()) {
			tmpInputModel = (OrderInfoModel) iterator.next();
			String srubbedStreet = RoutingUtil.standardizeStreetAddress(
					tmpInputModel.getStreetAddress1().trim(), tmpInputModel
							.getStreetAddress2().trim());
			if (!hasLocation(getConnection(), srubbedStreet, tmpInputModel
					.getApartmentNumber().trim(), tmpInputModel.getZipCode()
					.trim())) {
				Address address = new Address();
				address.setLine1(RoutingUtil.standardizeStreetAddress(
						tmpInputModel.getStreetAddress1().trim(), tmpInputModel
								.getStreetAddress2().trim()));
				address.setPostalCode(tmpInputModel.getZipCode().trim());
				address.setCountry("US");
				addressLst[intCount++] = address;
				tmpTrueList.add(tmpInputModel);
			}
		}
		RouteNetWebService_Service service = new RouteNetWebService_ServiceLocator();
		RouteNetPortType port = service.getRouteNetWebService(new URL(
				RoutingServicesProperties.getRoadNetProviderURL()));
		// System.out.println("START GEOCODE :"+getCurrentTime());
		GeocodeData[] geographicData = port.batchGeocode(addressLst,
				new GeocodeOptions());
		// System.out.println("END GEOCODE :"+getCurrentTime());

		iterator = tmpTrueList.iterator();
		ILocationModel locationModel = null;
		ArrayList dataList = new ArrayList();
		intCount = 0;

		while (iterator.hasNext()) {
			tmpInputModel = (OrderInfoModel) iterator.next();
			locationModel = new LocationModel();
			dataList.add(locationModel);
			locationModel.setApartmentNumber(tmpInputModel.getApartmentNumber()
					.trim());
			locationModel.setCity(tmpInputModel.getCity().trim());
			locationModel.setState(tmpInputModel.getState().trim());
			locationModel.setStreetAddress1(RoutingUtil
					.standardizeStreetAddress(tmpInputModel.getStreetAddress1()
							.trim(), tmpInputModel.getStreetAddress2().trim()));
			// locationModel.setStreetAddress2(tmpInputModel.getStreetAddress2());
			locationModel.setZipCode(tmpInputModel.getZipCode().trim());
			locationModel.setCountry("US");

			GeocodeData tmpData = geographicData[intCount++];

			IGeographicLocation result = new GeographicLocation();
			result
					.setLatitude(""
							+ (double) (tmpData.getCoordinate().getLatitude() / 1000000.0));
			result
					.setLongitude(""
							+ (double) (tmpData.getCoordinate().getLongitude() / 1000000.0));
			result.setConfidence(tmpData.getConfidence().getValue());
			result.setQuality(tmpData.getQuality().getValue());

			locationModel.setGeographicLocation(result);
		}
		insertLocations(dataList, getDataSource(true, "fddatasource"));
	}

	private static void processAddressList(String source, Connection conn)
			throws Exception {
		try {
			processAddressList(deSerializeData(source), conn);
		} finally {
			if (conn != null)
				conn.close();
		}
	}

	private static void processBuildingForCityState(String source, Connection conn)
			throws Exception {
		try {
			List inputDataList = deSerializeData(source);
			List updateDataList = new ArrayList();
			AddressModel tmpInputModel = null;
			AddressModel tmpCityStateModel = null;
			Iterator iterator = inputDataList.iterator();
			while (iterator.hasNext()) {
				tmpInputModel = (AddressModel) iterator.next();
				tmpCityStateModel = getCityState(conn, tmpInputModel.getAddress1(), tmpInputModel.getZipCode());
				tmpInputModel.setCity(tmpCityStateModel.getCity());
				tmpInputModel.setState(tmpCityStateModel.getState());
				updateDataList.add(tmpInputModel);
			}
			updateBuildingCityState(updateDataList, getDataSource(true,"fddatasource"));
		} finally {
			if (conn != null)
				conn.close();
		}
	}

	private static final String UPDATE_BUILDINGCITYSTATE_INFORMATION = "UPDATE DLV.DELIVERY_BUILDING SET CITY = ?, STATE = ?" +
			" WHERE ID = ?";

public static void updateBuildingCityState(List dataList, DataSource dataSource)
		throws SQLException {
	Connection conn = null;
	try {
		BatchSqlUpdate batchUpdater = new BatchSqlUpdate(dataSource,
				UPDATE_BUILDINGCITYSTATE_INFORMATION);
		// batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
		batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));

		batchUpdater.compile();

		Iterator iterator = dataList.iterator();

		AddressModel model = null;
		conn = dataSource.getConnection();
		while (iterator.hasNext()) {

			model = (AddressModel) iterator.next();

			batchUpdater.update(new Object[] { model.getCity(),
					 model.getState(),
					 	model.getId() });
		}
		batchUpdater.flush();
	}finally {
		if (conn != null)
			conn.close();
	}
}

	private static AddressModel getCityState(Connection connection,
			String address, String zipCode) throws Exception {

		PreparedStatement st = null;
		ResultSet rs = null;
		String city = null;
		String state = null;
		try {

			st = connection.prepareStatement("select distinct city ct,state st from cust.address@DBSTOSBY.NYC.FRESHDIRECT.COM a where a.SCRUBBED_ADDRESS = ? and zip = ?");

			st.setString(1, address);
			st.setString(2, zipCode);
			rs = st.executeQuery();



			while (rs.next()) {
				city = rs.getString("ct");

				state = rs.getString("st");

			}



		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
		}
		if(city == null || city.trim().length() == 0) {
			city = "New York";
		}
		if(state == null || state.trim().length() == 0) {
			state = "NY";
		}

		AddressModel tmpAdd = new AddressModel();
		tmpAdd.setCity(city);
		tmpAdd.setState(state);
		return tmpAdd;
	}

	private static void processLocationList(String source, Connection conn)
			throws Exception {
		try {
			List inputDataList = deSerializeData(source);
			Iterator iterator = inputDataList.iterator();
			AddressModel tmpInputModel = null;

			while (iterator.hasNext()) {
				tmpInputModel = (AddressModel) iterator.next();
				String srubbedStreet = RoutingUtil.standardizeStreetAddress(
						trim(tmpInputModel.getAddress1()), trim(tmpInputModel
								.getAddress2()));
				String apt = tmpInputModel.getApartment() != null ? tmpInputModel
						.getApartment().trim()
						: null;
				String zipCode = tmpInputModel.getZipCode() != null ? tmpInputModel
						.getZipCode().trim()
						: "";
				// System.out.println(srubbedStreet+"$"+zipCode+"$");
				tmpInputModel.setAddress1(srubbedStreet);
				tmpInputModel.setZipCode(zipCode);
				tmpInputModel.setApartment(apt);

				String id = getBuilding(conn, srubbedStreet, zipCode);
				if (id != null) {
					tmpInputModel.setId(id);
				} else {
					throw new Exception();
				}
			}
			//insertLocations(inputDataList, getDataSource(true, "fddatasource"));
		} finally {
			if (conn != null)
				conn.close();
		}
	}

	private static Address[] getAddressArray(List inputDataList) {

		Iterator iterator = inputDataList.iterator();
		AddressModel tmpInputModel = null;
		Address[] addressLst = new Address[inputDataList.size()];
		int intCount = 0;

		while (iterator.hasNext()) {
			tmpInputModel = (AddressModel) iterator.next();

			Address address = new Address();
			address.setLine1(tmpInputModel.getAddress1());
			address.setPostalCode(tmpInputModel.getZipCode());
			address.setCountry("US");
			addressLst[intCount++] = address;
		}
		return addressLst;
	}

	private static void processAddressList(List inputDataList, Connection conn)
			throws Exception {

		Iterator iterator = inputDataList.iterator();
		AddressModel tmpInputModel = null;

		int intCount = 0;

		List tmpTrueList = new ArrayList();

		while (iterator.hasNext()) {
			tmpInputModel = (AddressModel) iterator.next();
			String srubbedStreet = RoutingUtil.standardizeStreetAddress(
					trim(tmpInputModel.getAddress1()), trim(tmpInputModel
							.getAddress2()));
			String zipCode = tmpInputModel.getZipCode() != null ? tmpInputModel
					.getZipCode().trim() : "";
			// System.out.println(srubbedStreet+"$"+zipCode+"$");
			tmpInputModel.setAddress1(srubbedStreet);
			tmpInputModel.setZipCode(zipCode);

			tmpTrueList.add(tmpInputModel);
		}

		Address[] addressLst = getAddressArray(tmpTrueList);
		RouteNetWebService_Service service = new RouteNetWebService_ServiceLocator();
		RouteNetPortType port = service.getRouteNetWebService(new URL(
				RoutingServicesProperties.getRoadNetProviderURL()));
		System.out.println("START GEOCODE :" + getCurrentTime() + " ->"
				+ RoutingServicesProperties.getRoadNetProviderURL()
				+ " ->" + tmpTrueList.size());
		GeocodeData[] geographicData = port.batchGeocode(addressLst,
				new GeocodeOptions());
		System.out.println("END GEOCODE :" + getCurrentTime());

		iterator = tmpTrueList.iterator();
		ILocationModel locationModel = null;
		ArrayList dataList = new ArrayList();
		intCount = 0;

		while (iterator.hasNext()) {
			tmpInputModel = (AddressModel) iterator.next();
			locationModel = new LocationModel();
			dataList.add(locationModel);

			String strApt = tmpInputModel.getApartment() != null ? tmpInputModel
					.getApartment().trim()
					: "";
			String zipCode = tmpInputModel.getZipCode() != null ? tmpInputModel
					.getZipCode().trim() : "";
			String city = tmpInputModel.getCity() != null ? tmpInputModel
					.getCity().trim() : "";
			String state = tmpInputModel.getState() != null ? tmpInputModel
					.getState().trim() : "";

			locationModel.setApartmentNumber(strApt);
			locationModel.setCity(city);
			locationModel.setState(state);
			locationModel.setStreetAddress1(tmpInputModel.getAddress1());
			// locationModel.setStreetAddress2(tmpInputModel.getStreetAddress2());
			locationModel.setZipCode(zipCode);
			locationModel.setCountry("US");

			GeocodeData tmpData = geographicData[intCount++];

			IGeographicLocation result = new GeographicLocation();
			result
					.setLatitude(""
							+ (double) (tmpData.getCoordinate().getLatitude() / 1000000.0));
			result
					.setLongitude(""
							+ (double) (tmpData.getCoordinate().getLongitude() / 1000000.0));
			result.setConfidence(tmpData.getConfidence().getValue());
			result.setQuality(tmpData.getQuality().getValue());

			locationModel.setGeographicLocation(result);
		}
		 //insertBuildings(dataList, getDataSource(true,"fddatasource"));
	}

	private static final String GET_LOCATION_QRY = "select dl.ID ID, dl.SCRUBBED_STREET SCRUBBED_STREET, "
			+ "dl.APARTMENT APARTMENT,dl.CITY CITY, dl.STATE STATE, dl.COUNTRY COUNTRY, dl.ZIP ZIP, "
			+ "dl.LONGITUDE LONGITUDE, dl.LATITUDE LATITUDE, dl.SERVICETIME_TYPE SERVICETIME_TYPE "
			+ "from dlv.DELIVERY_LOCATION dl "
			+ "where dl.SCRUBBED_STREET = ? ";

	public static boolean hasLocation(Connection connection, String street,
			String apartment, String zipCode) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			boolean hasApartment = apartment != null
					&& !"".equals(apartment.trim());
			StringBuffer locationQ = new StringBuffer();
			locationQ.append(GET_LOCATION_QRY);

			if (hasApartment) {
				locationQ
						.append("AND UPPER(dl.APARTMENT) = REPLACE(REPLACE(UPPER(?),'-'),' ') ");
			} else {
				locationQ.append("AND dl.APARTMENT IS NULL ");
			}

			locationQ.append("AND dl.ZIP = ? ");
			int intCount = 1;
			ps = connection.prepareStatement(locationQ.toString());
			ps.setString(intCount++, street);
			if (hasApartment) {
				ps.setString(intCount++, apartment);
			}
			ps.setString(intCount++, zipCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				return true;
			}
			return false;
		} finally {
			rs.close();
			ps.close();
			// if(connection!=null) connection.close();
		}
	}

	private static final String GET_BUILDING_QRY = "select dl.ID ID from dlv.DELIVERY_BUILDING dl "
			+ "where dl.SCRUBBED_STREET = ? ";

	public static String getBuilding(Connection connection, String street,
			String zipCode) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			StringBuffer locationQ = new StringBuffer();
			locationQ.append(GET_BUILDING_QRY);

			locationQ.append("AND dl.ZIP = ? ");
			int intCount = 1;
			ps = connection.prepareStatement(locationQ.toString());
			ps.setString(intCount++, street);

			ps.setString(intCount++, zipCode);
			rs = ps.executeQuery();

			while (rs.next()) {
				return rs.getString("ID");
			}
			return null;
		} finally {
			rs.close();
			ps.close();
			// if(connection!=null) connection.close();
		}
	}

	private static final String INSERT_BUILDING_INFORMATION = "INSERT INTO DLV.DELIVERY_BUILDING ( ID,"
			+ "SCRUBBED_STREET, ZIP, COUNTRY, LATITUDE, LONGITUDE, "
			+ " SERVICETIME_TYPE, GEO_CONFIDENCE, GEO_QUALITY ) VALUES ( "
			+ "DLV.DELIVERY_BUILDING_SEQ.nextval,?,?,?,?,?,?,?,?)";

	public static void insertBuildings(List dataList, DataSource dataSource)
			throws SQLException {
		Connection connection = null;
		try {
			BatchSqlUpdate batchUpdater = new BatchSqlUpdate(dataSource,
					INSERT_BUILDING_INFORMATION);
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.DECIMAL));
			batchUpdater.declareParameter(new SqlParameter(Types.DECIMAL));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));

			batchUpdater.compile();

			Iterator iterator = dataList.iterator();
			connection = dataSource.getConnection();
			ILocationModel model = null;
			IGeographicLocation location = null;
			String latitude = null;
			String longitude = null;
			String confidence = null;
			String quality = null;

			while (iterator.hasNext()) {

				model = (ILocationModel) iterator.next();
				location = model.getGeographicLocation();

				if (location != null) {
					latitude = location.getLatitude();
					longitude = location.getLongitude();
					confidence = location.getConfidence();
					quality = location.getQuality();
				}

				batchUpdater.update(new Object[] { model.getStreetAddress1(),
						model.getZipCode(), model.getCountry(), latitude,
						longitude, model.getServiceTimeType(), confidence,
						quality });
			}
			batchUpdater.flush();
		} finally {
			if (connection != null)
				connection.close();
		}
	}

	private static final String INSERT_LOCATION_INFORMATION = "INSERT INTO DLV.DELIVERY_LOCATION ( ID, BUILDINGID, "
			+ "SCRUBBED_STREET, APARTMENT, CITY, STATE, ZIP, COUNTRY, "
			+ " SERVICETIME_TYPE) VALUES ( "
			+ "DLV.DELIVERY_LOCATION_SEQ.nextval,?,?,?,?,?,?,?,?)";

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
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));
			batchUpdater.declareParameter(new SqlParameter(Types.VARCHAR));

			batchUpdater.compile();

			Iterator iterator = dataList.iterator();
			connection = dataSource.getConnection();
			AddressModel model = null;

			while (iterator.hasNext()) {

				model = (AddressModel) iterator.next();

				batchUpdater.update(new Object[] { model.getId(),
						model.getAddress1(), model.getApartment(),
						model.getCity(), model.getState(), model.getZipCode(),
						model.getCountry(), null });
			}
			batchUpdater.flush();
		} finally {
			if (connection != null)
				connection.close();
		}
	}

	private static void loadLocation(Connection connection,
			String destinationFile) throws Exception {

		Statement st = null;
		ResultSet rs = null;

		try {

			ArrayList addressList = new ArrayList();

			st = connection.createStatement();
			System.out
					.println("----------------------START QUERY--------------------------");
			rs = st.executeQuery("SELECT * FROM DLV.DELIVERY_BUILDING");
			System.out
					.println("----------------------END QUERY--------------------------");
			AddressModel address = null;
			List dataList = new ArrayList();
			String scrubbedStreet = null;

			while (rs.next()) {
				address = new AddressModel();
				address.setAddress1(rs.getString("ADDRESS1"));
				address.setAddress2(rs.getString("ADDRESS2"));
				address.setApartment(rs.getString("APARTMENT"));
				address.setCity(rs.getString("CITY"));
				address.setState(rs.getString("STATE"));
				address.setZipCode(rs.getString("ZIP"));
				address.setId(rs.getString("ID"));

				try {
					scrubbedStreet = standardizeStreetAddress(address.getAddress1(), address.getAddress2())
							+ address.getApartment() + address.getZipCode();
					if (!dataList.contains(scrubbedStreet)) {
						addressList.add(address);
					} else {
						dataList.add(scrubbedStreet);
					}
				} catch (RoutingServiceException ex) {
					System.out.println(address);
				}

			}
			System.out.println("ADDRESS LIST:" + addressList.size());
			System.out
					.println("----------------------START SERIALIZE--------------------------");
			serializeData(addressList, destinationFile);
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			connection.close();
		}
	}


	private static void loadStandByDeliveryInfo(Connection connection,
			String destinationFile) throws Exception {

		Statement st = null;
		ResultSet rs = null;

		try {

			ArrayList addressList = new ArrayList();

			st = connection.createStatement();
			System.out
					.println("----------------------START QUERY--------------------------");
			rs = st
					.executeQuery("select distinct d.ADDRESS1 ADDRESS1, d.ZIP ZIP from cust.deliveryinfo@DBSTOSBY.NYC.FRESHDIRECT.COM d where d.CUTOFFTIME > '01-JAN-2006'");
			System.out
					.println("----------------------END QUERY--------------------------");
			ExtractAddressModel address = null;
			List dataList = new ArrayList();
			String scrubbedStreet = null;
			String scrubbedAddress = null;

			while (rs.next()) {
				address = new ExtractAddressModel();
				address.setAddress1(rs.getString("ADDRESS1"));
				address.setZip(rs.getString("ZIP"));

				try {
					scrubbedStreet = standardizeStreetAddress(address.getAddress1(), address.getAddress1());
					address.setScrubbedAddress(scrubbedStreet);
					scrubbedAddress = scrubbedStreet + address.getZip();

					if (!dataList.contains(scrubbedAddress)) {
						addressList.add(address);
						dataList.add(scrubbedAddress);
					}
				} catch (RoutingServiceException ex) {
					System.out.println(address);
				}
			}
			System.out.println("ADDRESS LIST:" + addressList.size());
			System.out.println("----------------------START SERIALIZE--------------------------");
			serializeData(addressList, destinationFile);
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			connection.close();
		}
	}

	private static void loadBuildingAddress(Connection connection,
			String destinationFile) throws Exception {

		Statement st = null;
		ResultSet rs = null;

		try {

			ArrayList addressList = new ArrayList();

			st = connection.createStatement();
			System.out
					.println("----------------------START QUERY--------------------------");
			rs = st
					.executeQuery("SELECT * FROM DLV.DELIVERY_BUILDING");
			System.out
					.println("----------------------END QUERY--------------------------");
			AddressModel address = null;

			while (rs.next()) {
				address = new AddressModel();
				address.setAddress1(rs.getString("SCRUBBED_STREET"));
				address.setZipCode(rs.getString("ZIP"));
				address.setId(rs.getString("ID"));

				addressList.add(address);

			}
			System.out.println("ADDRESS LIST:" + addressList.size());
			System.out
					.println("----------------------START SERIALIZE--------------------------");
			serializeData(addressList, destinationFile);
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			connection.close();
		}
	}

	private static void loadStandByAddress(Connection connection,
			String destinationFile) throws Exception {

		Statement st = null;
		ResultSet rs = null;

		try {

			ArrayList addressList = new ArrayList();

			st = connection.createStatement();
			System.out
					.println("----------------------START QUERY--------------------------");
			rs = st
					.executeQuery("SELECT * FROM CUST.ADDRESS@DBSTOSBY.NYC.FRESHDIRECT.COM");
			System.out
					.println("----------------------END QUERY--------------------------");
			AddressModel address = null;
			List dataList = new ArrayList();
			String scrubbedStreet = null;

			while (rs.next()) {
				address = new AddressModel();
				address.setAddress1(rs.getString("ADDRESS1"));
				address.setAddress2(rs.getString("ADDRESS2"));
				address.setApartment(rs.getString("APARTMENT"));
				address.setCity(rs.getString("CITY"));
				address.setState(rs.getString("STATE"));
				address.setZipCode(rs.getString("ZIP"));
				address.setId(rs.getString("ID"));

				try {
					scrubbedStreet = standardizeStreetAddress(address.getAddress1(), address.getAddress2())
							+ address.getApartment() + address.getZipCode();
					if (!dataList.contains(scrubbedStreet)) {
						addressList.add(address);
					} else {
						dataList.add(scrubbedStreet);
					}
				} catch (RoutingServiceException ex) {
					System.out.println(address);
				}

			}
			System.out.println("ADDRESS LIST:" + addressList.size());
			System.out
					.println("----------------------START SERIALIZE--------------------------");
			serializeData(addressList, destinationFile);
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			connection.close();
		}
	}

	private static void loadStandByDeliveryAddress(Connection connection,
			String destinationFile) throws Exception {

		Statement st = null;
		ResultSet rs = null;

		try {

			ArrayList addressList = new ArrayList();

			st = connection.createStatement();
			System.out
					.println("----------------------START QUERY--------------------------");
			rs = st
					.executeQuery("SELECT * FROM cust.deliveryinfo@DBSTOSBY.NYC.FRESHDIRECT.COM d where d.CUTOFFTIME > '01-JAN-2006'");
			System.out
					.println("----------------------END QUERY--------------------------");
			AddressModel address = null;
			List dataList = new ArrayList();
			String scrubbedStreet = null;

			while (rs.next()) {
				address = new AddressModel();
				address.setAddress1(rs.getString("ADDRESS1"));
				address.setAddress2(rs.getString("ADDRESS2"));
				address.setApartment(rs.getString("APARTMENT"));
				address.setCity(rs.getString("CITY"));
				address.setState(rs.getString("STATE"));
				address.setZipCode(rs.getString("ZIP"));
				// address.setId(rs.getString("ID"));

				try {
					scrubbedStreet = standardizeStreetAddress(address.getAddress1(), address.getAddress2())
							+ address.getApartment() + address.getZipCode();
					if (!dataList.contains(scrubbedStreet)) {
						addressList.add(address);
					} else {
						dataList.add(scrubbedStreet);
					}
				} catch (RoutingServiceException ex) {
					System.out.println(address);
				}

			}
			System.out.println("ADDRESS LIST:" + addressList.size());
			System.out
					.println("----------------------START SERIALIZE--------------------------");
			serializeData(addressList, destinationFile);
		} finally {
			if (rs != null)
				rs.close();
			if (st != null)
				st.close();
			connection.close();
		}
	}

	private static void splitFile(String source, String destination,
			int groupSize, String prefix) {

		System.out
				.println("----------------------START splitFile--------------------------");
		ArrayList addressList = deSerializeData(source);

		ArrayList tmpList = new ArrayList();
		Iterator iterator = addressList.iterator();
		int intCount = 0;
		int intRowCount = 0;

		while (iterator.hasNext()) {

			tmpList.add(iterator.next());
			intCount++;

			if (intCount == groupSize) {
				System.out.println("COMPLETE STAGE FAILED=" + intCount);
				intRowCount++;
				serializeData(tmpList, destination + prefix + intRowCount
						+ ".ser");

				tmpList = new ArrayList();
				intCount = 0;
			}

		}
		intRowCount++;
		serializeData(tmpList, destination + prefix + intRowCount + ".ser");

		System.out
				.println("----------------------END splitFile--------------------------"
						+ addressList.size());
	}

	private static void splitBuildingFile(String source, String destination,
			int groupSize) throws Exception {

		System.out
				.println("----------------------START splitFile--------------------------");
		ArrayList addressList = deSerializeData(source);

		ArrayList tmpList = new ArrayList();
		Iterator iterator = addressList.iterator();
		int intCount = 0;
		int intRowCount = 0;
		AddressModel address = null;
		String scrubbedStreet = null;
		Map dataList = new HashMap();
		int intTotalCount = 0;
		while (iterator.hasNext()) {
			intTotalCount++;
			address = (AddressModel) iterator.next();
			scrubbedStreet = trim(RoutingUtil.standardizeStreetAddress(
					trim(address.getAddress1()), trim(address.getAddress2())))
					+ "$" + address.getZipCode().trim();
			if (!dataList.containsKey(scrubbedStreet)) {
				tmpList.add(address);
				dataList.put(scrubbedStreet, null);
				intCount++;
				/*if (scrubbedStreet.startsWith("2330")) {
					System.out.println(scrubbedStreet);
				}*/
			}

			if (intCount == groupSize) {
				System.out.println("COMPLETE STAGE =" + intRowCount + " - >"
						+ intTotalCount);
				intRowCount++;
				serializeData(tmpList, destination + "address" + intRowCount
						+ ".ser");

				tmpList = new ArrayList();
				intCount = 0;
			}

		}
		intRowCount++;
		serializeData(tmpList, destination + "address" + intRowCount + ".ser");

		System.out
				.println("----------------------END splitFile--------------------------"
						+ addressList.size());
	}

	private static String trim(String str) {

		return (str != null ? str.trim() : str);
	}

	private static ArrayList deSerializeData(String link) {
		return (ArrayList) deSerialize(link);
	}

	private static Object deSerialize(String link) {
		// System.out.println("----------------------START
		// DESRIALIZE--------------------------");
		Object button = null;
		try {
			// Deserialize from a file
			File file = new File(link);
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					file));
			button = in.readObject();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("----------------------END
		// DESRIALIZE--------------------------");
		return button;
	}

	public static String standardizeStreetAddress(String address1, String address2)
			throws RoutingServiceException {
		String streetAddressResult = null;
		// String oldStreetAddress = address.getStreetAddress1();
		try {
			streetAddressResult = AddressScrubber.standardizeForGeocode(address1);
			// streetAddress =
			// AddressScrubber.standardizeForGeocode(address.getAddress1());
		} catch (InvalidAddressException iae1) {
			try {
				streetAddressResult = AddressScrubber
						.standardizeForGeocode(address2);
			} catch (InvalidAddressException iae2) {
				throw new RoutingServiceException(iae2, IIssue.PROCESS_ADDRESSSTANDARDIZE_UNSUCCESSFUL);
			}
		}
		return streetAddressResult;
	}

	private static void serializeData(Object object, String link) {

		try {
			// Serialize to a file
			ObjectOutput out = new ObjectOutputStream(
					new FileOutputStream(link));
			out.writeObject(object);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Connection getConnection() throws SQLException {
		try {
			return getDataSource(true, "fddatasource").getConnection();
		} catch (NamingException ne) {
			ne.printStackTrace();
			throw new SQLException(
					"Unable to find DataSource to get Connection: "
							+ ne.getMessage());
		}
	}

	public static DataSource getDataSource(boolean local, String source)
			throws NamingException {
		InitialContext initCtx = null;
		try {
			Hashtable ht = new Hashtable();
			ht.put(Context.INITIAL_CONTEXT_FACTORY,
					"weblogic.jndi.WLInitialContextFactory");
			if (local) {
				ht.put(Context.PROVIDER_URL, localLookup);
			} else {
				ht.put(Context.PROVIDER_URL, standByLookup);
			}
			initCtx = new InitialContext(ht);
			return (DataSource) initCtx.lookup(source);// "java:comp/env/jdbc/dbpool"
		} finally {
			if (initCtx != null)
				try {
					initCtx.close();
				} catch (NamingException ne) {
				}
		}
	}

	private static void countFile(String source) {

		System.out
				.println("----------------------START countFile--------------------------");
		ArrayList addressList = deSerializeData(source);

		System.out
				.println("----------------------END countFile--------------------------"
						+ addressList.size());
	}

	private static String getCurrentTime() {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(cal.getTime());
	}
}
