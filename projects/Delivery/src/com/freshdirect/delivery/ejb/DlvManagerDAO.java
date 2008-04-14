package com.freshdirect.delivery.ejb;

/*
 * Created on Apr 23, 2003
 */
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
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.GenericSearchCriteria;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;

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
		"select t.id, t.base_date, t.start_time, t.end_time, t.cutoff_time, t.status, t.zone_id, t.capacity, z.zone_code, t.ct_capacity, " 
		+ "(select count(*) from dlv.reservation where timeslot_id=t.id and status_code <> ? and status_code <> ? and chefstable = ' ') as base_allocation, " 
		+ "(select count(*) from dlv.reservation where timeslot_id=t.id and status_code <> ? and status_code <> ? and chefstable = 'X') as ct_allocation, " 
		+ "(select z.ct_release_time from dlv.zone z where z.id = t.zone_id) as ct_release_time, "
		+ "(select z.ct_active from dlv.zone z where z.id = t.zone_id) as ct_active "
		+ "from dlv.region r, dlv.region_data rd, dlv.zone z, dlv.timeslot t "
		+ "where r.service_type = ? and r.id = rd.region_id "
		+ "and rd.id = z.region_data_id "
		+ "and mdsys.sdo_relate(z.geoloc, mdsys.sdo_geometry(2001, 8265, mdsys.sdo_point_type(?, ?,NULL), NULL, NULL), 'mask=ANYINTERACT querytype=WINDOW') ='TRUE' "
		+ "and (rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= ? and region_id = r.id) "
		+ 	"or rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= ? and region_id = r.id)) "
		+ "and z.id = t.zone_id and t.base_date >= rd.start_date "
		+ "and t.base_date >= ? and t.base_date < ? "
		+ "and to_date(to_char(t.base_date-1, 'MM/DD/YY ') || to_char(t.cutoff_time, 'HH:MI:SS AM'), 'MM/DD/YY HH:MI:SS AM') > SYSDATE "
		+ "order by t.base_date, z.zone_code, t.start_time";

	public static List getTimeslotForDateRangeAndZone(
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
		List timeslots = processTimeslotResultSet(rs);
		rs.close();
		ps.close();
		return timeslots;
	}
	

	private static final String CF_TIMESLOTS =
		"select ts.id, ts.base_date, ts.start_time, ts.end_time, ts.cutoff_time, ts.status, ts.zone_id, ts.capacity, z.zone_code, ts.ct_capacity, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? and chefstable = ' ') as base_allocation, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? and chefstable = 'X') as ct_allocation, "
			+ "(select z.ct_release_time from dlv.zone z where z.id = ts.zone_id) as ct_release_time, "
			+ "(select z.ct_active from dlv.zone z where z.id = ts.zone_id) as ct_active "
			+ "from dlv.region r, dlv.region_data rd, dlv.zone z, dlv.timeslot ts "
			+ "where r.id=rd.region_id and rd.id=z.region_data_id and z.id=ts.zone_id "
			+ "and rd.start_date=( "
			+ "select max(start_date) from dlv.region_data rd1 "
			+ "where rd1.start_date<=ts.base_date and rd1.region_id = r.id) "
			+ "and ts.base_date >= ? "
			+ "and ts.base_date < ? "
			+ "and r.service_type = ? "
			+ "and mdsys.sdo_relate(z.geoloc, mdsys.sdo_geometry(2001, 8265, mdsys.sdo_point_type(?,?,NULL), NULL, NULL), 'mask=ANYINTERACT querytype=WINDOW') ='TRUE' "
			+ "order by ts.base_date, z.zone_code, ts.start_time ";

	public static List getAllTimeslotsForDateRange(
		Connection conn,
		AddressModel address,
		java.util.Date startDate,
		java.util.Date endDate)
		throws SQLException, InvalidAddressException {
		if ((address.getLatitude() == 0.0) || (address.getLongitude() == 0.0)) {
			geocodeAddress(conn, address, false);
		}
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
		List timeslots = processTimeslotResultSet(rs);
		rs.close();
		ps.close();
		return timeslots;
	}

	private static void geocodeAddress(Connection conn, AddressModel address, boolean useApartment)
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

	private static List processTimeslotResultSet(ResultSet rs) throws SQLException {
		List lst = new ArrayList();
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
		return new DlvTimeslotModel(pk, zoneId, baseDate, startTime, endTime, cutoffTime, status, capacity, ctCapacity, baseAllocation, ctAllocation, ctReleaseTime, ctActive);
	}

	private static final String DEPOT_TIMESLOTS =
		"select ts.id, ts.base_date, ts.start_time, ts.end_time, ts.cutoff_time, ts.status, ts.zone_id, ts.capacity, z.zone_code, ts.ct_capacity, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? and chefstable = ' ') as base_allocation, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? and chefstable = 'X') as ct_allocation, "
			+ "(select z.ct_release_time from dlv.zone z where z.id = ts.zone_id) as ct_release_time, "
			+ "(select z.ct_active from dlv.zone z where z.id = ts.zone_id) as ct_active "
			+ "from dlv.region r, dlv.region_data rd, dlv.timeslot ts, dlv.zone z "
			+ "where r.id=rd.region_id "
			+ "and rd.start_date=( "
			+ "select max(start_date) from dlv.region_data rd1 "
			+ "where rd1.start_date<=ts.base_date and rd1.region_id = r.id) "
			+ "and ts.base_date >= ? and ts.base_date < ? "
			+ "and r.id = ? and rd.id = z.region_data_id and z.zone_code = ? and ts.zone_id = z.id "
			+ "and to_date(to_char(ts.base_date-1, 'MM/DD/YY ') || to_char(ts.cutoff_time, 'HH:MI:SS AM'), 'MM/DD/YY HH:MI:SS AM') > SYSDATE "
			+ "order by ts.base_date, z.zone_code, ts.start_time ";

	public static List getTimeslotsForDepot(
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
		List timeslots = processTimeslotResultSet(rs);
		rs.close();
		ps.close();
		return timeslots;
	}

	private static final String TIMESLOT_BY_ID =
		"select distinct timeslot.id, base_date, start_time, end_time, cutoff_time, timeslot.status, timeslot.zone_id, capacity, ct_capacity, "
			+ "(select count(reservation.TIMESLOT_ID) from dlv.reservation "
			+ "where zone_id = timeslot.zone_id AND timeslot.ID = reservation.TIMESLOT_ID and status_code <> ? and status_code <> ? and chefstable = ' ') as base_allocation, "
			+ "(select count(reservation.TIMESLOT_ID) from dlv.reservation "
			+ "where zone_id = timeslot.zone_id AND timeslot.ID = reservation.TIMESLOT_ID and status_code <> ? and status_code <> ? and chefstable = 'X') as ct_allocation, "
			+ "(select z.ct_release_time from dlv.zone z where z.id = timeslot.zone_id) as ct_release_time, "
			+ "(select z.ct_active from dlv.zone z where z.id = timeslot.zone_id) as ct_active "
			+ "from dlv.timeslot, dlv.reservation "
			+ "where timeslot.id = ? AND timeslot.id=reservation.TIMESLOT_ID(+)";

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

	public static List getReservationForCustomer(Connection conn, String customerId) throws SQLException {
		PreparedStatement ps =
			conn.prepareStatement(
				"SELECT ID, ORDER_ID, CUSTOMER_ID, STATUS_CODE, TIMESLOT_ID, ZONE_ID, EXPIRATION_DATETIME, TYPE, ADDRESS_ID FROM DLV.RESERVATION WHERE CUSTOMER_ID = ? AND STATUS_CODE = ?");
		ps.setString(1, customerId);
		ps.setInt(2, EnumReservationStatus.RESERVED.getCode());
		ResultSet rs = ps.executeQuery();
		List reservations = new ArrayList();
		while (rs.next()) {
			DlvReservationModel rsv = loadReservationFromResultSet(rs);
			reservations.add(rsv);
		}

		rs.close();
		ps.close();

		return reservations;
	}

	public static List getAllReservationsByCustomerAndTimeslot(Connection conn, String customerId, String timeslotId) throws SQLException {
		PreparedStatement ps =
			conn.prepareStatement(
				"SELECT ID, ORDER_ID, CUSTOMER_ID, STATUS_CODE, TIMESLOT_ID, ZONE_ID, EXPIRATION_DATETIME, TYPE, ADDRESS_ID FROM DLV.RESERVATION WHERE CUSTOMER_ID = ? AND TIMESLOT_ID=?");
		ps.setString(1, customerId);
		ps.setString(2, timeslotId);
		ResultSet rs = ps.executeQuery();
		List reservations = new ArrayList();
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
			rs.getString("ADDRESS_ID"));
	}

	
	public static void extendReservation(Connection conn, String rsvId, Date newExpTime) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("UPDATE DLV.RESERVATION SET EXPIRATION_DATETIME = ? WHERE ID = ?");
		ps.setTimestamp(1, new Timestamp(newExpTime.getTime()));
		ps.setString(2, rsvId);
		if (ps.executeUpdate() != 1) {
		    ps.close();
			throw new SQLException("Cannot find reservation to extend for id: " + rsvId);
		}
		ps.close();
	}
	
	public static void restoreReservation(Connection conn, String rsvId) throws SQLException {
	    PreparedStatement ps = conn.prepareStatement("UPDATE DLV.RESERVATION SET STATUS_CODE = ? WHERE ID = ? ");
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
		"select rd.id, rd.region_id, rd.start_date, rd.delivery_charges, z.id zone_id, z.zone_code, zd.unattended "
			+ "from dlv.region r, dlv.region_data rd, dlv.zone z, dlv.zone_desc zd "
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
				"X".equals(rs.getString("unattended"))
			);
		} else {
			response = new DlvZoneInfoModel(null, null, null, EnumZipCheckResponses.DONOT_DELIVER,false);
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

	
	public static List getCutoffInfo(Connection conn, String zoneCode, Date start, Date end) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(CUTOFF_INFO_QUERY);
		ps.setString(1, zoneCode);
		ps.setDate(2, new java.sql.Date(start.getTime()));
		ps.setDate(3, new java.sql.Date(end.getTime()));
		
		ResultSet rs = ps.executeQuery();
		List infos = new ArrayList();
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
		"select t.id, t.base_date, t.start_time, t.end_time, t.cutoff_time, t.status, t.zone_id, t.capacity, t.ct_capacity, " 
			+ "(select count(*) from dlv.reservation where timeslot_id=t.id and status_code <> ? and status_code <> ? and chefstable = ' ') as base_allocation, "
			+ "(select count(*) from dlv.reservation where timeslot_id=t.id and status_code <> ? and status_code <> ? and chefstable = 'X') as ct_allocation, "
			+ "(select z.ct_release_time from dlv.zone z where z.id = t.zone_id) as ct_release_time, "
			+ "(select z.ct_active from dlv.zone z where z.id = t.zone_id) as ct_active "
			+ "from dlv.planning_resource p, dlv.timeslot t " 
			+ "where p.id = t.resource_id and p.zone_code = ? " 
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
		List timeslots = new ArrayList();
		while(rs.next()) {
			timeslots.add(getTimeslot(rs));
		}
		
		rs.close();
		ps.close();
		
		int totalCapacity = 0;
		int totalReservations = 0;
		
		for(Iterator i = timeslots.iterator(); i.hasNext(); ) {
			DlvTimeslotModel t = (DlvTimeslotModel) i.next();
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

	public static List checkZipcode(Connection conn, String zipcode, EnumServiceType serviceType) throws SQLException {
		
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
		List lst = new ArrayList();
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
	public static List checkAddress(Connection conn, AddressModel address, EnumServiceType serviceType) throws SQLException, InvalidAddressException {
		if ((address.getLatitude() == 0.0) || (address.getLongitude() == 0.0)) {
			geocodeAddress(conn, address, false);
		}

		PreparedStatement ps = conn.prepareStatement(ADDRESS_CHECK);
		ps.setDouble(1, address.getLongitude());
		ps.setDouble(2, address.getLatitude());
		ps.setString(3, serviceType.getName());

		ResultSet rs = ps.executeQuery();
		/* List of DlvZipInfoModel */
		List infoList = new ArrayList();
		while (rs.next()) {
			DlvZipInfoModel info =
				new DlvZipInfoModel(address.getZipCode(), rs.getTimestamp("START_DATE"), rs.getDouble("COVERAGE"));
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
		

	public static List getDeliverableZipCodes(Connection conn, EnumServiceType serviceType) throws SQLException {
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
		List lst = new ArrayList();
		while (rs.next()) {
			lst.add(new DlvZipInfoModel(rs.getString(1), new java.util.Date(), rs.getDouble(2)));
		}
		rs.close();
		ps.close();
		return lst;
	}

	public static List getSiteAnnouncements(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("select * from dlv.site_announcements");
		ResultSet rs = ps.executeQuery();
		List lst = new ArrayList();
		while (rs.next()) {
			String headline = rs.getString("HEADLINE");
			String copy = rs.getString("COPY");

			Date startDate = rs.getTimestamp("START_DATE");
			Date endDate = rs.getTimestamp("END_DATE");

			Date lastOrderBefore = rs.getTimestamp("LAST_ORDER_BEFORE");

			Set placements = getPlacements(rs.getString("PLACEMENT"));
			Set userLevels = getUserLevels("X".equals(rs.getString("ANONYMOUS")), "X".equals(rs.getString("REGISTERED")));
			Set deliveryStatuses = getUserDlvStatus(rs.getString("USER_DLV_STATUS"));

			SiteAnnouncement ann =
				new SiteAnnouncement(headline, copy, startDate, endDate, placements, userLevels, deliveryStatuses, lastOrderBefore);

			lst.add(ann);
		}
		return lst;
	}

	private static Set getUserLevels(boolean anonymous, boolean registered) {
		Set s = new HashSet();
		if (anonymous) {
			s.add(EnumUserLevel.GUEST);
		}
		if (registered) {
			s.add(EnumUserLevel.RECOGNIZED);
		}
		return s;
	}

	private static Set getPlacements(String placements) {
		Set plc = new HashSet();
		if (placements != null) {
			StringTokenizer st = new StringTokenizer(placements, ",");
			while (st.hasMoreTokens()) {
				String token = st.nextToken().trim();
				plc.add(EnumPlacement.getEnum(token));
			}
		}
		return plc;
	}

	private static Set getUserDlvStatus(String dlvStatus) {
		Set dlv = new HashSet();
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
	public static List getTimeslotCapacityInfo(Connection conn, Date date) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(QUERY_TIMESLOT_CAPACITY_INFO);
		ps.setDate(1, new java.sql.Date(date.getTime()));
		ResultSet rs = ps.executeQuery();
		List lst = new ArrayList();
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

	private static final String CANCEL_RESERVATIONS_QUERY = "UPDATE DLV.RESERVATION SET STATUS_CODE = ? WHERE ID IN ("
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
	
	public static List getCutoffTimesByDate(Connection conn, Date start) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(CUTOFF_BY_DATE_QUERY);
		ps.setDate(1, new java.sql.Date(start.getTime()));
	

		ResultSet rs = ps.executeQuery();
		List cutOffTimes = new ArrayList();
		while(rs.next()) {
			Date cutOffTime = rs.getTimestamp("CUTOFFTIME");
			cutOffTimes.add(cutOffTime);
		}
		
		rs.close();
		ps.close();
		
		return cutOffTimes;
	}
	
}
