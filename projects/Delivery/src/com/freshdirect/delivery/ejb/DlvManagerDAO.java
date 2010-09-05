package com.freshdirect.delivery.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.ejb.FinderException;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.crm.ejb.CriteriaBuilder;
import com.freshdirect.delivery.DlvProperties;
import com.freshdirect.delivery.DlvTimeslotCapacityInfo;
import com.freshdirect.delivery.DlvZipInfoModel;
import com.freshdirect.delivery.DlvZoneCapacityInfo;
import com.freshdirect.delivery.DlvZoneCutoffInfo;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumAddressVerificationResult;
import com.freshdirect.delivery.EnumReservationStatus;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.EnumRestrictedAddressReason;
import com.freshdirect.delivery.EnumTimeslotStatus;
import com.freshdirect.delivery.EnumZipCheckResponses;
import com.freshdirect.delivery.InvalidAddressException;
import com.freshdirect.delivery.announcement.EnumPlacement;
import com.freshdirect.delivery.announcement.EnumUserDeliveryStatus;
import com.freshdirect.delivery.announcement.EnumUserLevel;
import com.freshdirect.delivery.announcement.SiteAnnouncement;
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.model.DlvZoneDescriptor;
import com.freshdirect.delivery.model.DlvZoneModel;
import com.freshdirect.delivery.routing.ejb.RoutingActivityType;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.GenericSearchCriteria;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.constants.EnumOrderMetricsSource;
import com.freshdirect.routing.constants.EnumRoutingUpdateStatus;
import com.freshdirect.routing.model.AreaModel;
import com.freshdirect.routing.model.DeliverySlot;
import com.freshdirect.routing.model.IAreaModel;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.util.RoutingUtil;

/**
 * @author knadeem
 */

public class DlvManagerDAO {
	
	private static final Category LOGGER = LoggerFactory.getInstance(DlvManagerDAO.class);
	
	public static boolean isAlcoholDeliverable(Connection conn, String scrubbedAddress, String zipcode) throws SQLException {

		PreparedStatement ps =
			conn.prepareStatement("SELECT * FROM DLV.RESTRICTED_ADDRESS WHERE SCRUBBED_ADDRESS = ? AND ZIPCODE = ? AND REASON = ?");
		ps.setString(1, scrubbedAddress);
		ps.setString(2, zipcode);
		ps.setString(3, EnumRestrictedAddressReason.ALCOHOL.getCode());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			return false;
		}

		rs.close();
		ps.close();

		return true;
	}

	public static EnumRestrictedAddressReason isAddressRestricted(Connection conn, AddressModel address) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(
					"SELECT APARTMENT, REASON FROM DLV.RESTRICTED_ADDRESS WHERE SCRUBBED_ADDRESS = ? AND ZIPCODE = ? and REASON <> ?");
			ps.setString(1, address.getScrubbedStreet());
			ps.setString(2, address.getZipCode());
			ps.setString(3, EnumRestrictedAddressReason.ALCOHOL.getCode());
			rs = ps.executeQuery();
			while (rs.next()) {
				String apt = rs.getString("APARTMENT");
				if (rs.wasNull() || "".equals(apt)) {
					String reason = rs.getString("REASON");
					return EnumRestrictedAddressReason.getRestrictionReason(reason);
				}
	
				if (apt.equalsIgnoreCase(address.getApartment())) {
					String reason = rs.getString("REASON");
					return EnumRestrictedAddressReason.getRestrictionReason(reason);
				}
			}
			return EnumRestrictedAddressReason.NONE;
		} finally{
			if(rs != null) rs.close();
			if(ps != null) ps.close();
		}
	}

	private static final String TIMESLOTS =
		"select t.id, t.base_date, t.start_time, t.end_time, t.cutoff_time, t.status, t.zone_id, t.capacity, z.zone_code, t.ct_capacity" +
		", ta.AREA AREA_CODE, ta.STEM_MAX_TIME stemmax, ta.STEM_FROM_TIME stemfrom, ta.STEM_TO_TIME stemto, z.NAME ZONE_NAME, TO_CHAR(t.CUTOFF_TIME, 'HH_MI_PM') WAVE_CODE, t.IS_DYNAMIC IS_DYNAMIC, t.IS_CLOSED IS_CLOSED, a.IS_DEPOT IS_DEPOT, a.DELIVERY_RATE AREA_DLV_RATE,  " 
		+ "(select count(*) from dlv.reservation where timeslot_id=t.id and status_code <> ? and status_code <> ? and chefstable = ' ') as base_allocation, " 
		+ "(select count(*) from dlv.reservation where timeslot_id=t.id and status_code <> ? and status_code <> ? and chefstable = 'X') as ct_allocation, " 
		+ "(select z.ct_release_time from dlv.zone z where z.id = t.zone_id) as ct_release_time, "
		+ "(select z.ct_active from dlv.zone z where z.id = t.zone_id) as ct_active "
		+ "from dlv.region r, dlv.region_data rd, dlv.timeslot t, dlv.zone z, transp.zone ta, transp.trn_area a "
		+ "where r.service_type = ? and r.id = rd.region_id "
		+ "and rd.id = z.region_data_id "
		+ "and mdsys.sdo_relate(z.geoloc, mdsys.sdo_geometry(2001, 8265, mdsys.sdo_point_type(?, ?,NULL), NULL, NULL), 'mask=ANYINTERACT querytype=WINDOW') ='TRUE' "
		+ "and (rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= ? and region_id = r.id) "
		+ 	"or rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= ? and region_id = r.id)) "
		+ "and t.ZONE_ID = z.ID and z.ZONE_CODE = ta.ZONE_CODE and ta.AREA = a.CODE and t.base_date >= rd.start_date "
		+ "and t.base_date >= ? and t.base_date < ? "
		+ "and to_date(to_char(t.base_date-1, 'MM/DD/YY ') || to_char(t.cutoff_time, 'HH:MI:SS AM'), 'MM/DD/YY HH:MI:SS AM') > SYSDATE "
		+ "order by t.base_date, z.zone_code, t.start_time";

	public static List<DlvTimeslotModel> getTimeslotForDateRangeAndZone(
		Connection conn,
		AddressModel address,
		java.util.Date startDate,
		java.util.Date endDate)
		throws SQLException, InvalidAddressException {

		//if ((address.getLatitude() == 0.0) || (address.getLongitude() == 0.0)) {
			geocodeAddress(conn, address, false);
		//}
		
		PreparedStatement ps = conn.prepareStatement(TIMESLOTS);
		
		ps.setInt(1, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(2, EnumReservationStatus.EXPIRED.getCode());
		ps.setInt(3, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(4, EnumReservationStatus.EXPIRED.getCode());
		ps.setString(5, address.getServiceType().getName());
		ps.setDouble(6, address.getLongitude());
		ps.setDouble(7, address.getLatitude());
		ps.setDate(8, new java.sql.Date(startDate.getTime()));
		ps.setDate(9, new java.sql.Date(endDate.getTime()));
		ps.setDate(10, new java.sql.Date(startDate.getTime()));
		ps.setDate(11, new java.sql.Date(endDate.getTime()));
		

		ResultSet rs = ps.executeQuery();
		List<DlvTimeslotModel> timeslots = processTimeslotResultSet(rs);
		rs.close();
		ps.close();
		return timeslots;
	}
	

	private static final String CF_TIMESLOTS =
		"select ts.id, ts.base_date, ts.start_time, ts.end_time, ts.cutoff_time, ts.status, ts.zone_id, ts.capacity, z.zone_code, ts.ct_capacity" +
		", ta.AREA AREA_CODE, ta.STEM_MAX_TIME stemmax, ta.STEM_FROM_TIME stemfrom, ta.STEM_TO_TIME stemto, z.NAME ZONE_NAME, TO_CHAR(ts.CUTOFF_TIME, 'HH_MI_PM') WAVE_CODE, ts.IS_DYNAMIC IS_DYNAMIC, ts.IS_CLOSED IS_CLOSED, a.IS_DEPOT IS_DEPOT, a.DELIVERY_RATE AREA_DLV_RATE,  "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? and chefstable = ' ') as base_allocation, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? and chefstable = 'X') as ct_allocation, "
			+ "(select z.ct_release_time from dlv.zone z where z.id = ts.zone_id) as ct_release_time, "
			+ "(select z.ct_active from dlv.zone z where z.id = ts.zone_id) as ct_active "
			+ "from dlv.region r, dlv.region_data rd, dlv.timeslot ts, dlv.zone z, transp.zone ta, transp.trn_area a "
			+ "where r.id=rd.region_id and rd.id=z.region_data_id and ts.ZONE_ID = z.ID and z.ZONE_CODE = ta.ZONE_CODE and ta.AREA = a.CODE "
			+ "and rd.start_date=( "
			+ "select max(start_date) from dlv.region_data rd1 "
			+ "where rd1.start_date<=ts.base_date and rd1.region_id = r.id) "
			+ "and ts.base_date >= ? "
			+ "and ts.base_date < ? "
			+ "and r.service_type = ? "
			+ "and mdsys.sdo_relate(z.geoloc, mdsys.sdo_geometry(2001, 8265, mdsys.sdo_point_type(?,?,NULL), NULL, NULL), 'mask=ANYINTERACT querytype=WINDOW') ='TRUE' "
			+ "order by ts.base_date, z.zone_code, ts.start_time ";

	public static List<DlvTimeslotModel> getAllTimeslotsForDateRange(
		Connection conn,
		AddressModel address,
		java.util.Date startDate,
		java.util.Date endDate)
		throws SQLException, InvalidAddressException {
		//if ((address.getLatitude() == 0.0) || (address.getLongitude() == 0.0)) {
			geocodeAddress(conn, address, false);
		//}
		PreparedStatement ps = conn.prepareStatement(CF_TIMESLOTS);
		ps.setInt(1, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(2, EnumReservationStatus.EXPIRED.getCode());
		ps.setInt(3, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(4, EnumReservationStatus.EXPIRED.getCode());
		ps.setDate(5, new java.sql.Date(startDate.getTime()));
		ps.setDate(6, new java.sql.Date(endDate.getTime()));
		ps.setString(7, address.getServiceType().getName());
		ps.setDouble(8, address.getLongitude());
		ps.setDouble(9, address.getLatitude());

		ResultSet rs = ps.executeQuery();
		List<DlvTimeslotModel> timeslots = processTimeslotResultSet(rs);
		rs.close();
		ps.close();
		return timeslots;
	}

	public static void geocodeAddress(Connection conn, AddressModel address, boolean useApartment)
		throws SQLException, InvalidAddressException {
		GeographyDAO dao = new GeographyDAO();

		EnumAddressVerificationResult verifyResult = dao.verify(address, useApartment, conn);

		String result = "";
		if (EnumAddressVerificationResult.ADDRESS_OK.equals(verifyResult)) {
			result = dao.geocode(address, conn);
		} else {
			LOGGER.debug("Unable to geocode this address: " + address);
			throw new InvalidAddressException.GeocodingException("Unable to geocode this address");
		}
		if (GeographyDAO.GEOCODE_FAILED.equalsIgnoreCase(result)) {
			throw new InvalidAddressException.GeocodingException("Provided address is invalid");
		}
	}

	private static List<DlvTimeslotModel> processTimeslotResultSet(ResultSet rs) throws SQLException {
		List<DlvTimeslotModel> lst = new ArrayList<DlvTimeslotModel>();
		java.util.Date baseDate = null;
		String zoneCode = null;
		while (rs.next()) {
			Date tmpDate = rs.getDate("BASE_DATE");
			String tmpZoneCode = rs.getString("ZONE_CODE");
			
			if (baseDate == null || !tmpDate.equals(baseDate)) {
				baseDate = tmpDate;
				zoneCode = tmpZoneCode;
			}
			if (!tmpZoneCode.equals(zoneCode)) {
				continue;
			}
			
			lst.add(getTimeslot(rs));
		}
		return lst;
	}
	
	private static List<DlvTimeslotModel> processTimeslotCompositeResultSet(ResultSet rs) throws SQLException {
		List<DlvTimeslotModel> lst = new ArrayList<DlvTimeslotModel>();
		while (rs.next()) {			
			lst.add(getTimeslot(rs));
		}
		return lst;
	}

	private static DlvTimeslotModel getTimeslot(ResultSet rs) throws SQLException {

		PrimaryKey pk = new PrimaryKey(rs.getString("ID"));
		java.util.Date baseDate = rs.getDate("BASE_DATE");
		TimeOfDay startTime = new TimeOfDay(rs.getTime("START_TIME"));
		TimeOfDay endTime = new TimeOfDay(rs.getTime("END_TIME"));
		TimeOfDay cutoffTime = new TimeOfDay(rs.getTime("CUTOFF_TIME"));
		EnumTimeslotStatus status = EnumTimeslotStatus.getEnum(rs.getInt("STATUS"));
		int capacity = rs.getInt("CAPACITY");
		int baseAllocation = rs.getInt("BASE_ALLOCATION");
		int ctAllocation = rs.getInt("CT_ALLOCATION");
		String zoneId = rs.getString("ZONE_ID");
		int ctCapacity = rs.getInt("CT_CAPACITY");
		int ctReleaseTime = rs.getInt("CT_RELEASE_TIME");
		boolean ctActive = "X".equals(rs.getString("CT_ACTIVE"));
		String zoneCode = rs.getString("ZONE_CODE");
		DlvTimeslotModel _tmpSlot = new DlvTimeslotModel(pk, zoneId, baseDate, startTime, endTime, cutoffTime, status, capacity, ctCapacity, baseAllocation
									, ctAllocation, ctReleaseTime, ctActive,zoneCode);
		
		// Prepare routing slot configuration from result set
		IDeliverySlot routingSlot = new DeliverySlot();
		routingSlot.setStartTime(rs.getTimestamp("START_TIME"));
		routingSlot.setStopTime(rs.getTimestamp("END_TIME"));
		routingSlot.setWaveCode(rs.getString("WAVE_CODE"));
		routingSlot.setDynamicActive("X".equalsIgnoreCase(rs.getString("IS_DYNAMIC")) ? true : false);
		routingSlot.setManuallyClosed("X".equalsIgnoreCase(rs.getString("IS_CLOSED")) ? true : false);
		routingSlot.setZoneCode(zoneCode);
				
		IRoutingSchedulerIdentity _schId = new RoutingSchedulerIdentity();
		_schId.setDeliveryDate(baseDate);
		
		IAreaModel _aModel = new AreaModel();
		_aModel.setAreaCode(rs.getString("AREA_CODE"));
		_aModel.setDepot("X".equalsIgnoreCase(rs.getString("IS_DEPOT")) ? true : false);
		_aModel.setDeliveryRate(rs.getDouble("AREA_DLV_RATE"));
		_aModel.setStemFromTime(rs.getInt("stemfrom"));
		_aModel.setStemToTime(rs.getInt("stemto"));
		_aModel.setMaxStemTime(rs.getInt("stemmax"));
		
		_schId.setRegionId(RoutingUtil.getRegion(_aModel));
		_schId.setArea(_aModel);
		_schId.setDepot(_aModel.isDepot());
		
		routingSlot.setSchedulerId(_schId);
		_tmpSlot.setRoutingSlot(routingSlot);
		
		return _tmpSlot; 
	}

	private static final String DEPOT_TIMESLOTS =
		"select ts.id, ts.base_date, ts.start_time, ts.end_time, ts.cutoff_time, ts.status, ts.zone_id, ts.capacity, z.zone_code, ts.ct_capacity" +
		", ta.AREA AREA_CODE, ta.STEM_MAX_TIME stemmax, ta.STEM_FROM_TIME stemfrom, ta.STEM_TO_TIME stemto, z.NAME ZONE_NAME, TO_CHAR(ts.CUTOFF_TIME, 'HH_MI_PM') WAVE_CODE, ts.IS_DYNAMIC IS_DYNAMIC, ts.IS_CLOSED IS_CLOSED, a.IS_DEPOT IS_DEPOT, a.DELIVERY_RATE AREA_DLV_RATE,"
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? and chefstable = ' ') as base_allocation, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? and chefstable = 'X') as ct_allocation, "
			+ "(select z.ct_release_time from dlv.zone z where z.id = ts.zone_id) as ct_release_time, "
			+ "(select z.ct_active from dlv.zone z where z.id = ts.zone_id) as ct_active "
			+ "from dlv.region r, dlv.region_data rd, dlv.timeslot ts, dlv.zone z, transp.zone ta, transp.trn_area a "
			+ "where r.id=rd.region_id "
			+ "and rd.start_date=( "
			+ "select max(start_date) from dlv.region_data rd1 "
			+ "where rd1.start_date<=ts.base_date and rd1.region_id = r.id) "
			+ "and ts.base_date >= ? and ts.base_date < ? "
			+ "and r.id = ? and rd.id = z.region_data_id and z.zone_code = ? and ts.ZONE_ID = z.ID and z.ZONE_CODE = ta.ZONE_CODE and ta.AREA = a.CODE "
			+ "and to_date(to_char(ts.base_date-1, 'MM/DD/YY ') || to_char(ts.cutoff_time, 'HH:MI:SS AM'), 'MM/DD/YY HH:MI:SS AM') > SYSDATE "
			+ "order by ts.base_date, z.zone_code, ts.start_time ";

	public static List<DlvTimeslotModel> getTimeslotsForDepot(
		Connection conn,
		String regionId,
		String zoneCode,
		java.util.Date startDate,
		java.util.Date endDate)
		throws SQLException {
		
		
		PreparedStatement ps = conn.prepareStatement(DEPOT_TIMESLOTS);
		ps.setInt(1, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(2, EnumReservationStatus.EXPIRED.getCode());
		ps.setInt(3, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(4, EnumReservationStatus.EXPIRED.getCode());
		ps.setDate(5, new java.sql.Date(startDate.getTime()));
		ps.setDate(6, new java.sql.Date(endDate.getTime()));
		ps.setString(7, regionId);
		ps.setString(8, zoneCode);

		ResultSet rs = ps.executeQuery();
		List<DlvTimeslotModel> timeslots = processTimeslotResultSet(rs);
		rs.close();
		ps.close();
		return timeslots;
	}

	private static final String TIMESLOT_BY_ID =
		"select distinct ts.id, ts.base_date, ts.start_time, ts.end_time, ts.cutoff_time, ts.status, ts.zone_id, ts.capacity, ts.ct_capacity" +
		", ta.AREA AREA_CODE, ta.STEM_MAX_TIME stemmax, ta.STEM_FROM_TIME stemfrom, ta.STEM_TO_TIME stemto, z.NAME ZONE_NAME, TO_CHAR(ts.CUTOFF_TIME, 'HH_MI_PM') WAVE_CODE, ts.IS_DYNAMIC IS_DYNAMIC, ts.IS_CLOSED IS_CLOSED, a.IS_DEPOT IS_DEPOT, a.DELIVERY_RATE AREA_DLV_RATE,"
			+ "(select count(reservation.TIMESLOT_ID) from dlv.reservation "
			+ "where zone_id = ts.zone_id AND ts.ID = reservation.TIMESLOT_ID and status_code <> ? and status_code <> ? and chefstable = ' ') as base_allocation, "
			+ "(select count(reservation.TIMESLOT_ID) from dlv.reservation "
			+ "where zone_id = ts.zone_id AND ts.ID = reservation.TIMESLOT_ID and status_code <> ? and status_code <> ? and chefstable = 'X') as ct_allocation, "
			+ "(select z.ct_release_time from dlv.zone z where z.id = ts.zone_id) as ct_release_time, "
			+ "(select z.ct_active from dlv.zone z where z.id = ts.zone_id) as ct_active, "
			+ "(select z.zone_code from dlv.zone z where z.id=ts.zone_id ) as zone_code "
			+ " from dlv.timeslot ts, dlv.zone z, transp.zone ta, transp.trn_area a, dlv.reservation r "
			+ "where ts.id = ? AND ts.ZONE_ID = z.ID and z.ZONE_CODE = ta.ZONE_CODE and ta.AREA = a.CODE AND ts.id=r.TIMESLOT_ID(+)";

	public static DlvTimeslotModel getTimeslotById(Connection conn, String timeslotId) throws SQLException, FinderException {
		PreparedStatement ps = conn.prepareStatement(TIMESLOT_BY_ID);
		ps.setInt(1, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(2, EnumReservationStatus.EXPIRED.getCode());
		ps.setInt(3, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(4, EnumReservationStatus.EXPIRED.getCode());				
		
		ps.setString(5, timeslotId);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			return getTimeslot(rs);
		} else {
			throw new FinderException("No timeslot found for timeslotId: " + timeslotId);
		}
	}

	private static final String RESERVATION_FOR_CUSTOMER="SELECT R.ID, R.ORDER_ID, R.CUSTOMER_ID, R.STATUS_CODE, R.TIMESLOT_ID, R.ZONE_ID" +
			", R.EXPIRATION_DATETIME, R.TYPE, R.ADDRESS_ID,T.BASE_DATE, Z.ZONE_CODE,R.UNASSIGNED_DATETIME, R.UNASSIGNED_ACTION" +
			", R.IN_UPS, R.ORDER_SIZE, R.SERVICE_TIME, R.RESERVED_ORDER_SIZE, R.RESERVED_SERVICE_TIME, R.UPDATE_STATUS, R.METRICS_SOURCE " +
			", R.NUM_CARTONS , R.NUM_FREEZERS , R.NUM_CASES " +
			"FROM DLV.RESERVATION R, DLV.TIMESLOT T, DLV.ZONE Z WHERE  R.CUSTOMER_ID = ? AND R.STATUS_CODE = ? " +
			"AND R.TIMESLOT_ID=T.ID AND R.ZONE_ID=Z.ID";
	public static List<DlvReservationModel> getReservationForCustomer(Connection conn, String customerId) throws SQLException {
		PreparedStatement ps =
			conn.prepareStatement(RESERVATION_FOR_CUSTOMER);
		ps.setString(1, customerId);
		ps.setInt(2, EnumReservationStatus.RESERVED.getCode());
		ResultSet rs = ps.executeQuery();
		List<DlvReservationModel> reservations = new ArrayList<DlvReservationModel>();
		while (rs.next()) {
			DlvReservationModel rsv = loadReservationFromResultSet(rs);
			reservations.add(rsv);
		}

		rs.close();
		ps.close();

		return reservations;
	}

	private static final String RESERVATION_BY_CUSTOMER_AND_TIMESLOT="SELECT R.ID, R.ORDER_ID, R.CUSTOMER_ID, R.STATUS_CODE, R.TIMESLOT_ID" +
			", R.ZONE_ID, R.EXPIRATION_DATETIME, R.TYPE, R.ADDRESS_ID,T.BASE_DATE, Z.ZONE_CODE" +
			",R.UNASSIGNED_DATETIME,R.UNASSIGNED_ACTION,R.IN_UPS, R.ORDER_SIZE, R.SERVICE_TIME" +
			", R.RESERVED_ORDER_SIZE, R.RESERVED_SERVICE_TIME, R.UPDATE_STATUS, R.METRICS_SOURCE " +
			", R.NUM_CARTONS , R.NUM_FREEZERS , R.NUM_CASES " +
			"FROM DLV.RESERVATION R, DLV.TIMESLOT T, DLV.ZONE Z WHERE R.CUSTOMER_ID = ? AND R.TIMESLOT_ID=? " +
			"AND R.TIMESLOT_ID=T.ID AND R.ZONE_ID=Z.ID";
	public static List<DlvReservationModel> getAllReservationsByCustomerAndTimeslot(Connection conn, String customerId, String timeslotId) throws SQLException {
		PreparedStatement ps =
			conn.prepareStatement(RESERVATION_BY_CUSTOMER_AND_TIMESLOT);
				//"SELECT ID, ORDER_ID, CUSTOMER_ID, STATUS_CODE, TIMESLOT_ID, ZONE_ID, EXPIRATION_DATETIME, TYPE, ADDRESS_ID FROM DLV.RESERVATION WHERE CUSTOMER_ID = ? AND TIMESLOT_ID=?";
					/*" SELECT R.ID, R.ORDER_ID, R.CUSTOMER_ID, R.STATUS_CODE, R.TIMESLOT_ID, R.ZONE_ID, R.EXPIRATION_DATETIME, R.TYPE, R.ADDRESS_ID, Z.ZONE_CODE FROM DLV.RESERVATION R, "+
					" DLV.TIMESLOT T, DLV.ZONE Z WHERE R.TIMESLOT_ID=T.ID AND R.ZONE_ID=Z.ID "*/
		ps.setString(1, customerId);
		ps.setString(2, timeslotId);
		ResultSet rs = ps.executeQuery();
		List<DlvReservationModel> reservations = new ArrayList<DlvReservationModel>();
		while (rs.next()) {
			DlvReservationModel rsv = loadReservationFromResultSet(rs);
			reservations.add(rsv);
		}

		rs.close();
		ps.close();

		return reservations;
	}

	private static DlvReservationModel loadReservationFromResultSet(ResultSet rs) throws SQLException {
		return new DlvReservationModel(
			new PrimaryKey(rs.getString("ID")),
			rs.getString("ORDER_ID"),
			rs.getString("CUSTOMER_ID"),
			rs.getInt("STATUS_CODE"),
			rs.getTimestamp("EXPIRATION_DATETIME"),
			rs.getString("TIMESLOT_ID"),
			rs.getString("ZONE_ID"),
			EnumReservationType.getEnum(rs.getString("TYPE")),
			rs.getString("ADDRESS_ID"),
			rs.getDate("BASE_DATE"),
			rs.getString("ZONE_CODE"),
			RoutingActivityType.getEnum( rs.getString("UNASSIGNED_ACTION")) ,
			"X".equalsIgnoreCase(rs.getString("IN_UPS"))?true:false
			, rs.getBigDecimal("ORDER_SIZE") != null ? new Double(rs.getDouble("ORDER_SIZE")) : null
			, rs.getBigDecimal("SERVICE_TIME") != null ? new Double(rs.getDouble("SERVICE_TIME")) : null
			, rs.getBigDecimal("RESERVED_ORDER_SIZE") != null ? new Double(rs.getDouble("RESERVED_ORDER_SIZE")) : null
			, rs.getBigDecimal("RESERVED_SERVICE_TIME") != null ? new Double(rs.getDouble("RESERVED_SERVICE_TIME")) : null
			, rs.getBigDecimal("NUM_CARTONS") != null ? new Long(rs.getLong("NUM_CARTONS")) : null
			, rs.getBigDecimal("NUM_FREEZERS") != null ? new Long(rs.getLong("NUM_FREEZERS")) : null
			, rs.getBigDecimal("NUM_CASES") != null ? new Long(rs.getLong("NUM_CASES")) : null
			, EnumRoutingUpdateStatus.getEnum(rs.getString("UPDATE_STATUS"))
			, EnumOrderMetricsSource.getEnum(rs.getString("METRICS_SOURCE")));
		
	}

	
	public static void extendReservation(Connection conn, String rsvId, Date newExpTime) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("UPDATE DLV.RESERVATION SET EXPIRATION_DATETIME = ?, MODIFIED_DTTM=SYSDATE WHERE ID = ?");
		ps.setTimestamp(1, new Timestamp(newExpTime.getTime()));
		ps.setString(2, rsvId);
		if (ps.executeUpdate() != 1) {
		    ps.close();
			throw new SQLException("Cannot find reservation to extend for id: " + rsvId);
		}
		ps.close();
	}
	
	public static void restoreReservation(Connection conn, String rsvId) throws SQLException {
	    PreparedStatement ps = conn.prepareStatement("UPDATE DLV.RESERVATION SET STATUS_CODE = ?,ORDER_ID='x'||ID, MODIFIED_DTTM=SYSDATE WHERE ID = ? ");
	    ps.setInt(1, EnumReservationStatus.RESERVED.getCode());
	    ps.setString(2, rsvId);
	    if(ps.executeUpdate() != 1){
	        ps.close();
	        throw new SQLException("Cannot find reservation to RESTORE for id: " + rsvId);
	    }
	    ps.close();
	}

	public static void removeReservation(Connection conn, String reservationId) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("DELETE FROM DLV.RESERVATION WHERE ID = ?");
		ps.setString(1, reservationId);
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Cannot find reservation to extend for id: " + reservationId);
		}
		ps.close();
	}

	private static final String ZONE_CODE =
		"select rd.id, rd.region_id, rd.start_date, rd.delivery_charges, z.id zone_id, z.zone_code, zd.unattended, zd.cos_enabled "
			+ "from dlv.region r, dlv.region_data rd, dlv.zone z, transp.zone  zd "
			+ "where zd.zone_code = z.zone_code and rd.id = z.region_data_id and rd.region_id = r.id and r.service_type = ? "
			+ "and rd.start_date = (select max(start_date) from dlv.region_data where start_date <= ? and region_id=r.id) "
			+ "and mdsys.sdo_relate(z.geoloc, mdsys.sdo_geometry(2001, 8265, mdsys.sdo_point_type(?, ?,NULL), NULL, NULL), 'mask=ANYINTERACT querytype=WINDOW') ='TRUE' "
			+ "order by z.zone_code";

	public static DlvZoneInfoModel getZoneInfo(Connection conn, AddressModel address, java.util.Date date, boolean useApartment)
		throws SQLException, InvalidAddressException {

		if ((address.getLatitude() == 0.0) || (address.getLongitude() == 0.0)) {
			geocodeAddress(conn, address, useApartment);
		}

		DlvZoneInfoModel response;
		
		
		PreparedStatement ps = conn.prepareStatement(ZONE_CODE);
		ps.setString(1, address.getServiceType().getName());
		ps.setDate(2, new java.sql.Date(date.getTime()));
		ps.setDouble(3, address.getLongitude());
		ps.setDouble(4, address.getLatitude());

		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			response = new DlvZoneInfoModel(
				rs.getString("zone_code"), 
				rs.getString("zone_id"), 
				rs.getString("region_id"), 
				EnumZipCheckResponses.DELIVER,
				"X".equals(rs.getString("unattended")),
				"X".equals(rs.getString("cos_enabled"))
			);
		} else {
			response = new DlvZoneInfoModel(null, null, null, EnumZipCheckResponses.DONOT_DELIVER,false,false);
		}

		rs.close();
		ps.close();

		return response;
	}
	
	private static final String ZONE_CODE_FOR_COS_ENABLED =
		"select rd.id, rd.region_id, rd.start_date, rd.delivery_charges, z.id zone_id, z.zone_code, zd.unattended, zd.cos_enabled "
			+ "from dlv.region r, dlv.region_data rd, dlv.zone z, transp.zone zd "
			+ "where zd.zone_code = z.zone_code and rd.id = z.region_data_id and rd.region_id = r.id and r.service_type = 'HOME' and zd.cos_enabled='X'"
			+ "and rd.start_date = (select max(start_date) from dlv.region_data where start_date <= ? and region_id=r.id) "
			+ "and mdsys.sdo_relate(z.geoloc, mdsys.sdo_geometry(2001, 8265, mdsys.sdo_point_type(?, ?,NULL), NULL, NULL), 'mask=ANYINTERACT querytype=WINDOW') ='TRUE' "
			+ "order by z.zone_code";

	public static DlvZoneInfoModel getZoneInfoForCosEnabled(Connection conn, AddressModel address, java.util.Date date, boolean useApartment)
		throws SQLException, InvalidAddressException {

		if ((address.getLatitude() == 0.0) || (address.getLongitude() == 0.0)) {
			geocodeAddress(conn, address, useApartment);
		}

		DlvZoneInfoModel response;
						
		PreparedStatement ps = conn.prepareStatement(ZONE_CODE_FOR_COS_ENABLED);
		ps.setDate(1, new java.sql.Date(date.getTime()));
		ps.setDouble(2, address.getLongitude());
		ps.setDouble(3, address.getLatitude());

		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			response = new DlvZoneInfoModel(
				rs.getString("zone_code"), 
				rs.getString("zone_id"), 
				rs.getString("region_id"), 
				EnumZipCheckResponses.DELIVER,
				"X".equals(rs.getString("unattended")),
				"X".equals(rs.getString("cos_enabled"))
			);
		} else {
			response = new DlvZoneInfoModel(null, null, null, EnumZipCheckResponses.DONOT_DELIVER,false,false);
		}

		rs.close();
		ps.close();

		return response;
	}
	
	private static final String CUTOFF_INFO_QUERY = 
		"select t.base_date, min(t.start_time) as start_time, max(t.end_time) as end_time, t.cutoff_time "   
			+ "from dlv.planning_resource p, dlv.timeslot t " 
			+ "where p.id = t.resource_id and p.zone_code = ? " 
			+ "and p.day >= ? and p.day < ? "
			+ "group by t.base_date, t.cutoff_time "
			+ "order by t.base_date, t.cutoff_time ";

	
	public static List<DlvZoneCutoffInfo> getCutoffInfo(Connection conn, String zoneCode, Date start, Date end) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(CUTOFF_INFO_QUERY);
		ps.setString(1, zoneCode);
		ps.setDate(2, new java.sql.Date(start.getTime()));
		ps.setDate(3, new java.sql.Date(end.getTime()));
		
		ResultSet rs = ps.executeQuery();
		List<DlvZoneCutoffInfo> infos = new ArrayList<DlvZoneCutoffInfo>();
		while(rs.next()) {
			DlvZoneCutoffInfo info = new DlvZoneCutoffInfo(zoneCode,
										rs.getDate("BASE_DATE"), 
										new TimeOfDay(rs.getTime("START_TIME")), 
										new TimeOfDay(rs.getTime("END_TIME")), 
										new TimeOfDay(rs.getTime("CUTOFF_TIME"))
								 );
			infos.add(info);
			
		}
		
		rs.close();
		ps.close();
		
		return infos;
	}
	
	private static final String ZONE_CAPACITY_QUERY = 
		"select t.id, t.base_date, t.start_time, t.end_time, t.cutoff_time, t.status, t.zone_id, t.capacity, t.ct_capacity" +
		", ta.AREA AREA_CODE, ta.STEM_MAX_TIME stemmax, ta.STEM_FROM_TIME stemfrom, ta.STEM_TO_TIME stemto, z.NAME ZONE_NAME, TO_CHAR(t.CUTOFF_TIME, 'HH_MI_PM') WAVE_CODE, t.IS_DYNAMIC IS_DYNAMIC, t.IS_CLOSED IS_CLOSED, a.IS_DEPOT IS_DEPOT, a.DELIVERY_RATE AREA_DLV_RATE," 
			+ "(select count(*) from dlv.reservation where timeslot_id=t.id and status_code <> ? and status_code <> ? and chefstable = ' ') as base_allocation, "
			+ "(select count(*) from dlv.reservation where timeslot_id=t.id and status_code <> ? and status_code <> ? and chefstable = 'X') as ct_allocation, "
			+ "(select z.ct_release_time from dlv.zone z where z.id = t.zone_id) as ct_release_time, "
			+ "(select z.ct_active from dlv.zone z where z.id = t.zone_id) as ct_active, p.zone_code "
			+ "from dlv.planning_resource p, dlv.timeslot t, dlv.zone z, transp.zone ta, transp.trn_area a " 
			+ "where p.id = t.resource_id and t.ZONE_ID = z.ID and z.ZONE_CODE = ta.ZONE_CODE and ta.AREA = a.CODE and p.zone_code = ? " 
			+ "and p.day >= ? and p.day < ? "
			+ "and to_date(to_char(t.base_date-1, 'MM/DD/YY ') || to_char(t.cutoff_time, 'HH:MI:SS AM'), 'MM/DD/YY HH:MI:SS AM') > SYSDATE "
			+ "order by t.base_date, p.zone_code, t.start_time ";
	
	public static DlvZoneCapacityInfo getZoneCapacity(Connection conn, String zoneCode, Date start, Date end) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(ZONE_CAPACITY_QUERY);
		ps.setInt(1, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(2, EnumReservationStatus.EXPIRED.getCode());
		ps.setInt(3, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(4, EnumReservationStatus.EXPIRED.getCode());
		ps.setString(5, zoneCode);
		ps.setDate(6, new java.sql.Date(start.getTime()));
		ps.setDate(7, new java.sql.Date(end.getTime()));
		
		ResultSet rs = ps.executeQuery();
		List<DlvTimeslotModel> timeslots = new ArrayList<DlvTimeslotModel>();
		while(rs.next()) {
			timeslots.add(getTimeslot(rs));
		}
		
		rs.close();
		ps.close();
		
		int totalCapacity = 0;
		int totalReservations = 0;
		
		for(Iterator<DlvTimeslotModel> i = timeslots.iterator(); i.hasNext(); ) {
			DlvTimeslotModel t = i.next();
			totalCapacity += t.getCapacity();
			totalReservations += t.getTotalAllocation(); 
		}
		
		DlvZoneCapacityInfo info = new DlvZoneCapacityInfo(zoneCode, totalCapacity, totalCapacity - totalReservations);
		
		return info;
	}

	private static final String ZIP_CHECK =
		"select zip.zipcode, rd.start_date, "
			+ "sum((sdo_geom.sdo_area(sdo_geom.sdo_intersection(zn.geoloc, zip.geoloc, .5), .5)/sdo_geom.sdo_area(zip.geoloc, .5))) as coverage "
			+ "from dlv.zipcode zip, dlv.region r, dlv.region_data rd, dlv.zone zn "
			+ "where zip.zipcode = ? "
			+ "and (rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= sysdate and region_id = r.id) "
			+ 	"or rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= sysdate + 8 and region_id = r.id)) "
			+ "and rd.region_id = r.id and r.service_type = ? and rd.id = zn.region_data_id "
			+ "group by zipcode, start_date ";
	
	private static final String NO_SPATIAL_HOME_ZIPCHECK = 
		"SELECT zipcode, trunc(sysdate) as start_date, home_coverage as coverage "
			+ "from dlv.zipcode where zipcode = ?";
	
	private static final String NO_SPATIAL_COS_ZIPCHECK = 
		"SELECT zipcode, trunc(sysdate) as start_date, cos_coverage as coverage "
			+ "from dlv.zipcode where zipcode = ?";

	public static List<DlvZipInfoModel> checkZipcode(Connection conn, String zipcode, EnumServiceType serviceType) throws SQLException {
		
		String query = null;
		if(DlvProperties.useFreeSpatialOnly()) {
			if(EnumServiceType.CORPORATE.equals(serviceType)) {
				query = NO_SPATIAL_COS_ZIPCHECK;
			} else {
				query = NO_SPATIAL_HOME_ZIPCHECK;
			}
		} else {
			query = ZIP_CHECK;
		}
		
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setString(1, zipcode);
		
		if(!DlvProperties.useFreeSpatialOnly()){
			ps.setString(2, serviceType.getName());
		}

		ResultSet rs = ps.executeQuery();
		List<DlvZipInfoModel> lst = new ArrayList<DlvZipInfoModel>();
		while (rs.next()) {
			DlvZipInfoModel info =
				new DlvZipInfoModel(rs.getString("ZIPCODE"), rs.getTimestamp("START_DATE"), rs.getDouble("COVERAGE"));
			lst.add(info);
		}
		rs.close();
		ps.close();
		return lst;
	}

	private static final String ADDRESS_CHECK =
		"select z.zone_code, r.id, rd.start_date, 1.0 as coverage "
			+ "from dlv.zone z, dlv.region_data rd, dlv.region r "
			+ "where mdsys.sdo_relate(z.geoloc, mdsys.sdo_geometry(2001, 8265, mdsys.sdo_point_type(?, ?, NULL), NULL, NULL), 'mask=ANYINTERACT querytype=WINDOW') ='TRUE' "
			+ "and z.region_data_id = rd.id and rd.region_id = r.id and r.service_type = ? "
			+ "and (rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= sysdate and region_id = r.id) "
			+ 	"or rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= sysdate + 8 and region_id = r.id)) "
			+ "order by start_date ";
	

	/**
	 * @return List of DlvZipInfoModel
	 */
	public static List<DlvZipInfoModel> checkAddress(Connection conn, AddressModel address, EnumServiceType serviceType) throws SQLException, InvalidAddressException {
		if ((address.getLatitude() == 0.0) || (address.getLongitude() == 0.0)) {
			geocodeAddress(conn, address, false);
		}

		PreparedStatement ps = conn.prepareStatement(ADDRESS_CHECK);
		ps.setDouble(1, address.getLongitude());
		ps.setDouble(2, address.getLatitude());
		ps.setString(3, serviceType.getName());

		ResultSet rs = ps.executeQuery();
		/* List of DlvZipInfoModel */
		List<DlvZipInfoModel> infoList = new ArrayList<DlvZipInfoModel>();
		while (rs.next()) {
			DlvZipInfoModel info =
				new DlvZipInfoModel(address.getZipCode(), rs.getTimestamp("START_DATE"), rs.getDouble("COVERAGE"));
			infoList.add(info);
		}
		rs.close();
		ps.close();

		return infoList;
	}
	
	private static final String ADDRESS_CHECK_FOR_COS_ENABLED =
		"select z.zone_code, r.id, rd.start_date, 1.0 as coverage, tz.cos_enabled "
			+ "from dlv.zone z, dlv.region_data rd, dlv.region r, transp.zone tz "
			+ "where mdsys.sdo_relate(z.geoloc, mdsys.sdo_geometry(2001, 8265, mdsys.sdo_point_type(?, ?, NULL), NULL, NULL), 'mask=ANYINTERACT querytype=WINDOW') ='TRUE' "
			+ "and z.region_data_id = rd.id and rd.region_id = r.id and z.zone_code = tz.zone_code and r.service_type='HOME' and tz.cos_enabled='X' "
			+ "and (rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= sysdate and region_id = r.id) "
			+ 	"or rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= sysdate + 8 and region_id = r.id)) "
			+ "order by start_date ";
	

	/**
	 * @return List of DlvZipInfoModel
	 */
	public static List checkAddressForCosEnabled(Connection conn, AddressModel address) throws SQLException, InvalidAddressException {
		if ((address.getLatitude() == 0.0) || (address.getLongitude() == 0.0)) {
			geocodeAddress(conn, address, false);
		}

		PreparedStatement ps = conn.prepareStatement(ADDRESS_CHECK_FOR_COS_ENABLED);
		ps.setDouble(1, address.getLongitude());
		ps.setDouble(2, address.getLatitude());
		
		ResultSet rs = ps.executeQuery();
		/* List of DlvZipInfoModel */
		List infoList = new ArrayList();
		while (rs.next()) {
			DlvZipInfoModel info =
				new DlvZipInfoModel(address.getZipCode(), rs.getTimestamp("START_DATE"), rs.getDouble("COVERAGE"), "X".equals(rs.getString("COS_ENABLED")));
			infoList.add(info);
		}
		rs.close();
		ps.close();

		return infoList;
	}
	
	private static final String DELIVERABLE_ZIPS = 
		"select * from "
		+ "(SELECT zipcode, sum(sdo_geom.sdo_area(sdo_geom.sdo_intersection(zones.geoloc, zip.geoloc, .5), .5)/sdo_geom.sdo_area(zip.geoloc, .5)) as coverage " 
		+ "FROM dlv.zipcode zip, " 
		+ "(select zn.zone_code, zn.geoloc, rd.start_date " 
		+ "from dlv.region r, "
		+ "dlv.region_data rd, dlv.zone zn "
		+ "where r.id = rd.region_id and zn.region_data_id = rd.id and r.service_type = ? "
		+ "and rd.start_date = (select max(start_date) from dlv.region_data rd1 where rd1.region_id = r.id and rd1.start_date <= sysdate + 8)) zones "
		+ "group by zipcode order by zipcode) where coverage > .1 ";
	
	private static final String DELIVERABLE_ZIPS_NO_SPATIAL_HOME = 
		"select zipcode, home_coverage from dlv.zipcode where home_coverage > .1";
	
	private static final String DELIVERABLE_ZIPS_NO_SPATIAL_COS =
		"select zipcode, cos_coverage from dlv.zipcode where cos_coverage > .1";
		

	public static List<DlvZipInfoModel> getDeliverableZipCodes(Connection conn, EnumServiceType serviceType) throws SQLException {
		String query = null;
		if(DlvProperties.useFreeSpatialOnly()) {
			if(EnumServiceType.CORPORATE.equals(serviceType)) {
				query = DELIVERABLE_ZIPS_NO_SPATIAL_COS;
			} else {
				query = DELIVERABLE_ZIPS_NO_SPATIAL_HOME;
			}
		} else {
			query = DELIVERABLE_ZIPS;
		}
				
		PreparedStatement ps = conn.prepareStatement(query);
		if(!DlvProperties.useFreeSpatialOnly()) {
			ps.setString(1, serviceType.getName());
		}
		ResultSet rs = ps.executeQuery(); 
		List<DlvZipInfoModel> lst = new ArrayList<DlvZipInfoModel>();
		while (rs.next()) {
			lst.add(new DlvZipInfoModel(rs.getString(1), new java.util.Date(), rs.getDouble(2)));
		}
		rs.close();
		ps.close();
		return lst;
	}

	public static List<SiteAnnouncement> getSiteAnnouncements(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("select * from dlv.site_announcements");
		ResultSet rs = ps.executeQuery();
		List<SiteAnnouncement> lst = new ArrayList<SiteAnnouncement>();
		while (rs.next()) {
			String headline = rs.getString("HEADLINE");
			String copy = rs.getString("COPY");

			Date startDate = rs.getTimestamp("START_DATE");
			Date endDate = rs.getTimestamp("END_DATE");

			Date lastOrderBefore = rs.getTimestamp("LAST_ORDER_BEFORE");

			Set<EnumPlacement> placements = getPlacements(rs.getString("PLACEMENT"));
			Set<EnumUserLevel> userLevels = getUserLevels("X".equals(rs.getString("ANONYMOUS")), "X".equals(rs.getString("REGISTERED")));
			Set<EnumUserDeliveryStatus> deliveryStatuses = getUserDlvStatus(rs.getString("USER_DLV_STATUS"));

			SiteAnnouncement ann =
				new SiteAnnouncement(headline, copy, startDate, endDate, placements, userLevels, deliveryStatuses, lastOrderBefore);

			lst.add(ann);
		}
		return lst;
	}

	private static Set<EnumUserLevel> getUserLevels(boolean anonymous, boolean registered) {
		Set<EnumUserLevel> s = new HashSet<EnumUserLevel>();
		if (anonymous) {
			s.add(EnumUserLevel.GUEST);
		}
		if (registered) {
			s.add(EnumUserLevel.RECOGNIZED);
		}
		return s;
	}

	private static Set<EnumPlacement> getPlacements(String placements) {
		Set<EnumPlacement> plc = new HashSet<EnumPlacement>();
		if (placements != null) {
			StringTokenizer st = new StringTokenizer(placements, ",");
			while (st.hasMoreTokens()) {
				String token = st.nextToken().trim();
				plc.add(EnumPlacement.getEnum(token));
			}
		}
		return plc;
	}

	private static Set<EnumUserDeliveryStatus> getUserDlvStatus(String dlvStatus) {
		Set<EnumUserDeliveryStatus> dlv = new HashSet<EnumUserDeliveryStatus>();
		if (dlvStatus != null) {
			StringTokenizer st = new StringTokenizer(dlvStatus, ",");
			while (st.hasMoreTokens()) {
				String token = st.nextToken().trim();
				dlv.add(EnumUserDeliveryStatus.getEnum(token));
			}
		}
		return dlv;
	}

	
	
	private static String QUERY_TIMESLOT_CAPACITY_INFO = "select t.cutoff_time, z.zone_code, z.name, t.start_time, t.end_time, t.capacity, t.ct_capacity"
		+ " from dlv.timeslot t, dlv.zone z"
		+ " where t.base_date=trunc(?)"
		+ " and t.zone_id=z.id"
		+ " order by cutoff_time, zone_code, start_time";

	/** @return List of DlvTimeslotCapacityInfo */
	public static List<DlvTimeslotCapacityInfo> getTimeslotCapacityInfo(Connection conn, Date date) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(QUERY_TIMESLOT_CAPACITY_INFO);
		ps.setDate(1, new java.sql.Date(date.getTime()));
		ResultSet rs = ps.executeQuery();
		List<DlvTimeslotCapacityInfo> lst = new ArrayList<DlvTimeslotCapacityInfo>();
		while (rs.next()) {
			TimeOfDay cutoff = new TimeOfDay(rs.getTimestamp("CUTOFF_TIME"));
			String zone = rs.getString("ZONE_CODE");
			String zoneName = rs.getString("NAME");
			TimeOfDay start = new TimeOfDay(rs.getTimestamp("START_TIME"));
			TimeOfDay end = new TimeOfDay(rs.getTimestamp("END_TIME"));
			int capacity = rs.getInt("CAPACITY");

			DlvTimeslotCapacityInfo ci = new DlvTimeslotCapacityInfo(cutoff, zone, zoneName, start, end, capacity);

			lst.add(ci);
		}
		return lst;
	}

	private static final String CANCEL_RESERVATIONS_QUERY = "UPDATE DLV.RESERVATION SET STATUS_CODE = ?, MODIFIED_DTTM=SYSDATE WHERE ID IN ("
															+ "SELECT "
															+ "rs.id  "
															+ "from dlv.reservation rs, dlv.timeslot ts, dlv.zone ze, cust.customerinfo ci, cust.customer c "
															+ "where ts.id = rs.timeslot_id and ze.id = ts.zone_id and rs.customer_id = c.id and ci.customer_id = c.id and rs.status_code = 5 "
															+ "and rs.type in ('WRR','OTR')";


	public static int cancelReservations(Connection conn, GenericSearchCriteria resvCriteria) throws SQLException {
		CriteriaBuilder builder = new CriteriaBuilder();
		buildReservationSearch(resvCriteria, builder);
		String query = CANCEL_RESERVATIONS_QUERY + " and " + builder.getCriteria() + ")";
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setInt(1, EnumReservationStatus.CANCELED.getCode());
		Object[] obj = builder.getParams();
		for(int i = 0; i < obj.length; i++) {
			ps.setObject(i+2, obj[i]);
		}
		int updateCount = ps.executeUpdate();
		return updateCount;
	}
	
	private static void buildReservationSearch(GenericSearchCriteria criteria, CriteriaBuilder builder) {
		java.util.Date baseDate = (java.util.Date) criteria.getCriteriaMap().get("baseDate");
		builder.addSql("ts.base_date = ?", 
					new Object[] {new java.sql.Date(baseDate.getTime())});
		Object cutoffTime = criteria.getCriteriaMap().get("cutoffTime");
		if(cutoffTime != null){
			builder.addSql("to_date(to_char(ts.cutoff_time, 'HH:MI AM'),'HH:MI AM') = to_date(?,'HH:MI AM')", 
					new Object[] { 
					cutoffTime.toString() });
		}
		Object zoneArray = criteria.getCriteriaMap().get("zoneArray");
		if(zoneArray != null){
			builder.addInString("ze.zone_code", (String[])zoneArray);	
		}
		Object startTime = criteria.getCriteriaMap().get("startTime");
		if(startTime != null){
			builder.addSql("to_date(to_char(ts.start_time, 'HH:MI AM'),'HH:MI AM') >= to_date(?,'HH:MI AM')", 
					new Object[] {startTime.toString() });
		}
		Object endTime = criteria.getCriteriaMap().get("endTime");
		if(endTime != null){
			builder.addSql("to_date(to_char(ts.end_time, 'HH:MI AM'),'HH:MI AM') <= to_date(?,'HH:MI AM')", 
					new Object[] {endTime.toString() });
		}
	}
	
	private static final String CUTOFF_BY_DATE_QUERY = 
		"select t.cutoff_time as cutofftime "   
			+ "from dlv.planning_resource p, dlv.timeslot t " 
			+ "where p.id = t.resource_id "
			+ "and p.day = ? "
			+ "group by t.cutoff_time "
			+ "order by t.cutoff_time ";
	
	public static List<Date> getCutoffTimesByDate(Connection conn, Date start) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(CUTOFF_BY_DATE_QUERY);
		ps.setDate(1, new java.sql.Date(start.getTime()));
	

		ResultSet rs = ps.executeQuery();
		List<Date> cutOffTimes = new ArrayList<Date>();
		while(rs.next()) {
			Date cutOffTime = rs.getTimestamp("CUTOFFTIME");
			cutOffTimes.add(cutOffTime);
		}
		
		rs.close();
		ps.close();
		
		return cutOffTimes;
	}
	
	private static final String UNASSIGN_RESERVATION_QUERY="UPDATE DLV.RESERVATION SET UNASSIGNED_DATETIME=SYSDATE, MODIFIED_DTTM=SYSDATE, UNASSIGNED_ACTION=? WHERE ID=?";
	public static void setUnassignedInfo(Connection conn,String reservationId, String action)  throws SQLException {
		
		PreparedStatement ps = conn.prepareStatement(UNASSIGN_RESERVATION_QUERY);
		ps.setString(1, action);
	    ps.setString(2, reservationId);
	    if(ps.executeUpdate() != 1){
	        ps.close();
	        throw new SQLException("Cannot find reservation to UNASSIGN for id: " + reservationId);
	    }
	    ps.close();
	}
	
	private static final String UPDATE_RESERVATIONSTATUSONLY_QUERY = "UPDATE DLV.RESERVATION SET UPDATE_STATUS = ? WHERE ID=?";
	public static void setReservationMetricsStatus(Connection conn, String reservationId, String status)  throws SQLException {

		PreparedStatement ps = conn.prepareStatement(UPDATE_RESERVATIONSTATUSONLY_QUERY);
		ps.setString(1, status);
		ps.setString(2, reservationId);
		
		if(ps.executeUpdate() != 1){
			ps.close();
			throw new SQLException("Cannot find reservation to METRICS UPDATE STATUS for id: " + reservationId);
		}
		ps.close();
	}

	private static final String UPDATE_RESERVATIONSTATUS_QUERY = "UPDATE DLV.RESERVATION SET NUM_CARTONS = ? , NUM_FREEZERS = ? , NUM_CASES = ?" +
			" , UPDATE_STATUS = ? WHERE ID=?";
	public static void setReservationMetricsDetails(Connection conn, String reservationId
													, long noOfCartons, long noOfCases, long noOfFreezers
													, String status)  throws SQLException {
		
		PreparedStatement ps = conn.prepareStatement(UPDATE_RESERVATIONSTATUS_QUERY);
		ps.setDouble(1, noOfCartons);
		ps.setDouble(2, noOfCases);
		ps.setDouble(3, noOfFreezers);
		
	    ps.setString(4, status);
	    ps.setString(5, reservationId);
	    if(ps.executeUpdate() != 1){
	        ps.close();
	        throw new SQLException("Cannot find reservation to METRICS UPDATE STATUS for id: " + reservationId);
	    }
	    ps.close();
	}
	
	private static final String UPDATE_RESERVATIONSOURCE_QUERY = "UPDATE DLV.RESERVATION SET NUM_CARTONS = ? " +
																	", NUM_FREEZERS = ? , NUM_CASES = ?" +
																		" , METRICS_SOURCE = ? WHERE ID=?";
	public static void setReservationMetricsDetails(Connection conn, String reservationId
													, long noOfCartons, long noOfCases, long noOfFreezers
													, EnumOrderMetricsSource source)  throws SQLException {

		PreparedStatement ps = conn.prepareStatement(UPDATE_RESERVATIONSOURCE_QUERY);
		ps.setDouble(1, noOfCartons);
		ps.setDouble(2, noOfCases);
		ps.setDouble(3, noOfFreezers);

		ps.setString(4, (source != null ? source.value() : null));
		ps.setString(5, reservationId);
		if(ps.executeUpdate() != 1){
			ps.close();
			throw new SQLException("Cannot find reservation to METRICS UPDATE STATUS for id: " + reservationId);
		}
		ps.close();
	}
	
	private static final String UPDATE_RESERVATIONMETRICSSTATUS_QUERY = "UPDATE DLV.RESERVATION SET RESERVED_ORDER_SIZE = ?" +
			"																, RESERVED_SERVICE_TIME = ?, UPDATE_STATUS = ? WHERE ID=?";
	public static void setReservationReservedMetrics(Connection conn, String reservationId, double reservedOrderSize
															, double reservedServiceTime, String status)  throws SQLException {
		
		PreparedStatement ps = conn.prepareStatement(UPDATE_RESERVATIONMETRICSSTATUS_QUERY);
		ps.setDouble(1, reservedOrderSize);
		ps.setDouble(2, reservedServiceTime);
	    ps.setString(3, status);
	    ps.setString(4, reservationId);
	    if(ps.executeUpdate() != 1){
	        ps.close();
	        throw new SQLException("Cannot find reservation to METRICS UPDATE STATUS for id: " + reservationId);
	    }
	    ps.close();
	}
	
	private static final String UPDATE_RESERVATIONMETRICS_QUERY = "UPDATE DLV.RESERVATION SET RESERVED_ORDER_SIZE = ?" +
			"																, RESERVED_SERVICE_TIME = ? WHERE ID=?";
	public static void setReservationReservedMetrics(Connection conn, String reservationId
														, double reservedOrderSize, double reservedServiceTime)  throws SQLException {
		
		PreparedStatement ps = conn.prepareStatement(UPDATE_RESERVATIONMETRICS_QUERY);
		ps.setDouble(1, reservedOrderSize);
		ps.setDouble(2, reservedServiceTime);	    
	    ps.setString(3, reservationId);
	    if(ps.executeUpdate() != 1){
	        ps.close();
	        throw new SQLException("Cannot find reservation to RESERVED METRICS STATUS for id: " + reservationId);
	    }
	    ps.close();
	}
	
	private static final String SET_RESERVATION_SET_TO_UPS_FLAG_QUERY = "UPDATE DLV.RESERVATION SET IN_UPS='X', ROUTING_ORDER_ID=? WHERE ID=?";
	public static void setInUPS(Connection conn,String reservationId, String orderNum)  throws SQLException {
		
		PreparedStatement ps = conn.prepareStatement(SET_RESERVATION_SET_TO_UPS_FLAG_QUERY);
		ps.setString(1, orderNum);
	    ps.setString(2, reservationId);
	    if(ps.executeUpdate() != 1){
	        ps.close();
	        throw new SQLException("Cannot find reservation to set IN_UPS for id: " + reservationId);
	    }
	    ps.close();
	}
	private static final String MARK_RESERVATION_ASSIGNED_QUERY="UPDATE DLV.RESERVATION SET UNASSIGNED_DATETIME=null, UNASSIGNED_ACTION=null, MODIFIED_DTTM=SYSDATE WHERE ID=?";
	public static void clearUnassignedInfo(Connection conn,String reservationId)  throws SQLException {
		
		PreparedStatement ps = conn.prepareStatement(MARK_RESERVATION_ASSIGNED_QUERY);
		
	    ps.setString(1, reservationId);
	    if(ps.executeUpdate() != 1){
	        ps.close();
	        throw new SQLException("Cannot find reservation to clear unasssignedInfo for id: " + reservationId);
	    }
	    ps.close();
	}
	//List<DlvReservationModel> getUnassignedReservations()
	
	private static final String FETCH_UNASSIGNED_RESERVATIONS_QUERY="SELECT R.ID, R.ORDER_ID, R.CUSTOMER_ID, R.STATUS_CODE, R.TIMESLOT_ID, R.ZONE_ID, R.EXPIRATION_DATETIME, R.TYPE, R.ADDRESS_ID, "+
	" T.BASE_DATE, Z.ZONE_CODE,R.UNASSIGNED_DATETIME, R.UNASSIGNED_ACTION, R.IN_UPS, R.ORDER_SIZE, R.SERVICE_TIME, R.RESERVED_ORDER_SIZE" +
	", R.RESERVED_SERVICE_TIME, R.UPDATE_STATUS, R.METRICS_SOURCE " +
	", R.NUM_CARTONS , R.NUM_FREEZERS , R.NUM_CASES " +
	"FROM DLV.RESERVATION R, DLV.TIMESLOT T, DLV.ZONE Z "+
	" WHERE R.TIMESLOT_ID=T.ID AND R.ZONE_ID=Z.ID AND t.BASE_DATE=TRUNC(?) " +
	"AND (unassigned_action IS NOT NULL OR (UPDATE_STATUS IS NOT NULL AND UPDATE_STATUS <> 'SUS'))  " +
	"ORDER BY R.unassigned_action, R.UPDATE_STATUS NULLS LAST ";
	
	public static List<DlvReservationModel> getUnassignedReservations(Connection conn, Date _date)  throws SQLException {
		PreparedStatement ps =
			conn.prepareStatement(FETCH_UNASSIGNED_RESERVATIONS_QUERY);
		
		ps.setDate(1, new java.sql.Date(_date.getTime()));
		ResultSet rs = ps.executeQuery();
		List<DlvReservationModel>  reservations = new ArrayList<DlvReservationModel>();
		while (rs.next()) {
			DlvReservationModel rsv = loadReservationFromResultSet(rs);
			reservations.add(rsv);
		}

		rs.close();
		ps.close();
       
		return reservations;
	}
	
	private static final String FETCH_REROUTE_RESERVATIONS_QUERY="SELECT R.ID, R.ORDER_ID, R.CUSTOMER_ID, R.STATUS_CODE, R.TIMESLOT_ID, R.ZONE_ID, R.EXPIRATION_DATETIME, R.TYPE, R.ADDRESS_ID, "+
	" T.BASE_DATE, Z.ZONE_CODE,R.UNASSIGNED_DATETIME, R.UNASSIGNED_ACTION, R.IN_UPS, R.ORDER_SIZE, R.SERVICE_TIME, R.RESERVED_ORDER_SIZE" +
	", R.RESERVED_SERVICE_TIME, R.UPDATE_STATUS, R.METRICS_SOURCE " +
	", R.NUM_CARTONS , R.NUM_FREEZERS , R.NUM_CASES " +
	"FROM DLV.RESERVATION R, DLV.TIMESLOT T, DLV.ZONE Z "+
	" WHERE R.TIMESLOT_ID=T.ID AND R.ZONE_ID=Z.ID AND t.BASE_DATE > TRUNC(SYSDATE-1) " +
	" AND  R.DO_REROUTE = 'X' ORDER BY T.BASE_DATE, Z.ZONE_CODE";
	public static List<DlvReservationModel> getReRouteReservations(Connection conn)  throws SQLException {
		PreparedStatement ps =
			conn.prepareStatement(FETCH_REROUTE_RESERVATIONS_QUERY);
		
		ResultSet rs = ps.executeQuery();
		List<DlvReservationModel>  reservations = new ArrayList<DlvReservationModel>();
		while (rs.next()) {
			DlvReservationModel rsv = loadReservationFromResultSet(rs);
			reservations.add(rsv);
		}

		rs.close();
		ps.close();
       
		return reservations;
	}
	
	public static void clearReRouteReservations(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("UPDATE DLV.RESERVATION r SET r.DO_REROUTE =null WHERE r.ID in (SELECT R.ID FROM  DLV.RESERVATION R, DLV.TIMESLOT T, DLV.ZONE Z "+
					" WHERE R.TIMESLOT_ID=T.ID AND R.ZONE_ID=Z.ID AND t.BASE_DATE > TRUNC(SYSDATE-1) " +
						" AND R.DO_REROUTE = 'X')");	
		int result = ps.executeUpdate();
		if (result <= 0) {
		    ps.close();
			throw new SQLException("Cannot find reroute reservation to clear: ");
		}
		ps.close();
	} 
	
	private static final String LOCK_EXPIRED_RESERVATIONS="UPDATE DLV.RESERVATION SET STATUS_CODE=25 WHERE EXPIRATION_DATETIME<= SYSDATE AND STATUS_CODE=5";
	private static final String GET_LOCKED_RESERVATIONS_QUERY="SELECT R.ID, R.ORDER_ID, R.CUSTOMER_ID, R.STATUS_CODE, R.TIMESLOT_ID, R.ZONE_ID, R.EXPIRATION_DATETIME, R.TYPE, R.ADDRESS_ID,T.BASE_DATE, Z.ZONE_CODE" +
			",R.UNASSIGNED_DATETIME, R.UNASSIGNED_ACTION, R.IN_UPS, R.ORDER_SIZE, R.SERVICE_TIME" +
			", R.RESERVED_ORDER_SIZE, R.RESERVED_SERVICE_TIME, R.UPDATE_STATUS, R.METRICS_SOURCE " +
			", R.NUM_CARTONS , R.NUM_FREEZERS , R.NUM_CASES " +
			"FROM DLV.RESERVATION R, "+
            " DLV.TIMESLOT T, DLV.ZONE Z  where  r.expiration_datetime <= sysdate and r.status_code = 25 " +
            "AND R.TIMESLOT_ID=T.ID AND R.ZONE_ID=Z.ID";
	public static List<DlvReservationModel> getExpiredReservations(Connection conn)  throws SQLException {
		
		PreparedStatement ps=conn.prepareStatement(LOCK_EXPIRED_RESERVATIONS);
		ps.execute();
		ps.close();
		
		ps =conn.prepareStatement(GET_LOCKED_RESERVATIONS_QUERY);
		ResultSet rs = ps.executeQuery();
		List<DlvReservationModel>  reservations = new ArrayList<DlvReservationModel>();
		while (rs.next()) {
			DlvReservationModel rsv = loadReservationFromResultSet(rs);
			reservations.add(rsv);
		}

		rs.close();
		ps.close();
       
		return reservations;
	}	
	
	private static final String MARK_LOCKED_RESERVATION_AS_EXPIRED_QUERY="update dlv.reservation set status_code = 20 where expiration_datetime <= sysdate and status_code = 25";
	public static void expireReservations(Connection conn)  throws SQLException {
		
		PreparedStatement ps = conn.prepareStatement(MARK_LOCKED_RESERVATION_AS_EXPIRED_QUERY);
	    ps.executeUpdate();
	    ps.close();
	}
	
	private static final String TIMESLOTS_BY_DATE =
		"select ts.id, ts.base_date, ts.start_time, ts.end_time, ts.cutoff_time, ts.status, ts.zone_id, ts.capacity" +
		", z.zone_code, ts.ct_capacity, ta.AREA AREA_CODE, ta.STEM_MAX_TIME stemmax, ta.STEM_FROM_TIME stemfrom, ta.STEM_TO_TIME stemto, z.NAME ZONE_NAME, TO_CHAR(ts.CUTOFF_TIME, 'HH_MI_PM') WAVE_CODE, ts.IS_DYNAMIC IS_DYNAMIC, ts.IS_CLOSED IS_CLOSED, a.IS_DEPOT IS_DEPOT, a.DELIVERY_RATE AREA_DLV_RATE, "
		+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? and chefstable = ' ') as base_allocation, "
		+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? and chefstable = 'X') as ct_allocation, "
		+ "(select z.ct_release_time from dlv.zone z where z.id = ts.zone_id) as ct_release_time, "
		+ "(select z.ct_active from dlv.zone z where z.id = ts.zone_id) as ct_active "
		+ "from dlv.region r, dlv.region_data rd, dlv.timeslot ts, dlv.zone z, transp.zone ta, transp.trn_area a "
		+ "where r.id=rd.region_id and rd.id=z.region_data_id and ts.ZONE_ID = z.ID and z.ZONE_CODE = ta.ZONE_CODE and ta.AREA = a.CODE " +
		"and (rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= ? and region_id = r.id)	" +
		"or rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= ? and region_id = r.id)) " +
		"and ts.ZONE_ID = z.ID and z.ZONE_CODE = ta.ZONE_CODE and ta.AREA = a.CODE and ts.base_date >= rd.start_date	and ts.base_date = ?	" +
		"and ts.is_dynamic = 'X'	" +
		"order by ts.base_date, z.zone_code, ts.start_time";

	public static List getTimeslotsForDate(Connection conn, java.util.Date startDate) throws SQLException {
				
		PreparedStatement ps = conn.prepareStatement(TIMESLOTS_BY_DATE);
		ps.setInt(1, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(2, EnumReservationStatus.EXPIRED.getCode());
		ps.setInt(3, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(4, EnumReservationStatus.EXPIRED.getCode());
		
		ps.setDate(5, new java.sql.Date(startDate.getTime()));
		ps.setDate(6, new java.sql.Date(startDate.getTime()));
		ps.setDate(7, new java.sql.Date(startDate.getTime()));
		
		ResultSet rs = ps.executeQuery();
		
		List timeslots = processTimeslotCompositeResultSet(rs);
		rs.close();
		ps.close();
		return timeslots;
	}
	
	private static final String UPDATE_TIMESLOTS_BY_ID = "UPDATE DLV.TIMESLOT SET CAPACITY = ? , CT_CAPACITY = ? WHERE ID = ?";
	
	public static int updateTimeslotsCapacity(Connection conn, List<DlvTimeslotModel> dlvTimeSlots ) throws SQLException{
		
		PreparedStatement ps = conn.prepareStatement(UPDATE_TIMESLOTS_BY_ID);
		
	    
	    for (DlvTimeslotModel _slot : dlvTimeSlots) {
	    	if(_slot != null && _slot.getRoutingSlot() != null && 
	    			_slot.getRoutingSlot().getDeliveryMetrics() != null) {
				ps.setInt(1, _slot.getRoutingSlot().getDeliveryMetrics().getOrderCapacity());
				ps.setInt(2, _slot.getRoutingSlot().getDeliveryMetrics().getOrderCtCapacity());
			    ps.setString(3, _slot.getId());
			    ps.addBatch();
	    	}
		}
		int[] count = ps.executeBatch();
		ps.close();
		return count.length;
	}
	
	private static final String SELECT_ACTIVE_ZONES = "select ZONE_CODE from TRANSP.Zone where OBSOLETE IS NULL Order By ZONE_CODE";
	
	public static List getActiveZoneCodes(Connection conn)throws SQLException{
		PreparedStatement ps = conn.prepareStatement(SELECT_ACTIVE_ZONES);
		ResultSet rs =ps.executeQuery();
		List zoneCodes = new ArrayList();
		while(rs.next()){
			zoneCodes.add(rs.getString(1));
		}
		return zoneCodes;
	}
	
	private static final String SELECT_ACTIVE_ZONES_NAMES = "select * from TRANSP.Zone where OBSOLETE IS NULL Order By ZONE_CODE";
	public static List<DlvZoneModel> getActiveZones(Connection conn)throws SQLException{
		PreparedStatement ps = conn.prepareStatement(SELECT_ACTIVE_ZONES_NAMES);
		ResultSet rs =ps.executeQuery();
		List<DlvZoneModel> zoneCodes = new ArrayList<DlvZoneModel>();
		while(rs.next()){
			DlvZoneModel model = new DlvZoneModel();
			DlvZoneDescriptor descriptor = new DlvZoneDescriptor();
			descriptor.setZoneCode(rs.getString("ZONE_CODE"));
			model.setZoneDescriptor(descriptor);
			model.setName(rs.getString("NAME"));
			zoneCodes.add(model);
		}
		return zoneCodes;
	}
}
