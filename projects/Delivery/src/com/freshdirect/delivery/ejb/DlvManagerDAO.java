package com.freshdirect.delivery.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.ejb.FinderException;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.crm.ejb.CriteriaBuilder;
import com.freshdirect.customer.EnumDeliverySetting;
import com.freshdirect.customer.EnumUnattendedDeliveryFlag;
import com.freshdirect.customer.ErpAddressModel;
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
import com.freshdirect.delivery.model.EnumReservationClass;
import com.freshdirect.delivery.model.SectorVO;
import com.freshdirect.delivery.model.UnassignedDlvReservationModel;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.GenericSearchCriteria;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.constants.EnumOrderMetricsSource;
import com.freshdirect.routing.constants.EnumRoutingUpdateStatus;
import com.freshdirect.routing.constants.RoutingActivityType;
import com.freshdirect.routing.model.AreaModel;
import com.freshdirect.routing.model.DeliverySlot;
import com.freshdirect.routing.model.IAreaModel;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IRegionModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.model.RegionModel;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.model.TrnFacilityType;
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
		"select t.id, t.base_date, t.start_time, t.end_time, t.cutoff_time, t.status, t.zone_id, t.capacity, z.zone_code, t.ct_capacity" 
		+ ", ta.AREA AREA_CODE, ta.STEM_MAX_TIME stemmax, ta.STEM_FROM_TIME stemfrom, ta.STEM_TO_TIME stemto, ta.ZONE_ECOFRIENDLY ecoFriendly, ta.STEERING_RADIUS steeringRadius, z.NAME ZONE_NAME, " 
		+ "case when t.premium_cutoff_time is null then TO_CHAR(t.CUTOFF_TIME, 'HH_MI_PM') else TO_CHAR(t.premium_cutoff_time, 'HH_MI_PM') end WAVE_CODE, t.IS_DYNAMIC IS_DYNAMIC, t.IS_CLOSED IS_CLOSED, tr.IS_DEPOT IS_DEPOT, tr.code REGION_CODE, tr.name REGION_NAME, tr.description REGION_DESCR, a.DELIVERY_RATE AREA_DLV_RATE, " 
		+ "(select count(*) from dlv.reservation where timeslot_id=t.id and status_code <> ? and status_code <> ? and chefstable = ' ' and class is null) as base_allocation, " 
		+ "(select count(*) from dlv.reservation where timeslot_id=t.id and status_code <> ? and status_code <> ? and chefstable = 'X' and class is null) as ct_allocation, " 
		+ "(select z.ct_release_time from dlv.zone z where z.id = t.zone_id) as ct_release_time, "
		+ "(select z.ct_active from dlv.zone z where z.id = t.zone_id) as ct_active, "
		+ "t.premium_cutoff_time, t.premium_capacity, t.premium_ct_capacity, " 
	  	+ "(select count(*) from dlv.reservation where timeslot_id=t.id and status_code <> ? and status_code <> ? and class = 'P') as premium_base_allocation, " 
	  	+ "(select count(*) from dlv.reservation where timeslot_id=t.id and status_code <> ? and status_code <> ? and class = 'PC') as premium_ct_allocation, " 
	  	+ "(select z.premium_ct_release_time from dlv.zone z where z.id = t.zone_id) as premium_ct_release_time, "
		+ "(select z.premium_ct_active from dlv.zone z where z.id = t.zone_id) as premium_ct_active "
		+ "from dlv.region r, dlv.region_data rd, dlv.timeslot t, dlv.zone z, transp.zone ta, transp.trn_area a,transp.trn_region tr "
		+ "where r.service_type = ? and r.id = rd.region_id "
		+ "and rd.id = z.region_data_id "
		+ "and mdsys.sdo_relate(z.geoloc, mdsys.sdo_geometry(2001, 8265, mdsys.sdo_point_type(?, ?,NULL), NULL, NULL), 'mask=ANYINTERACT querytype=WINDOW') ='TRUE' "
		+ "and (rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= ? and region_id = r.id) "
		+ 	"or rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= ? and region_id = r.id)) "
		+ "and t.ZONE_ID = z.ID and z.ZONE_CODE = ta.ZONE_CODE and ta.AREA = a.CODE  and a.region_code = tr.code and t.base_date >= rd.start_date "
		+ "and t.base_date >= ? and t.base_date < ? "
		+ "and (( t.premium_cutoff_time is null and to_date(to_char(t.base_date-1, 'MM/DD/YY ') || to_char(t.cutoff_time, 'HH:MI:SS AM'), 'MM/DD/YY HH:MI:SS AM') > ?) or" +
		" (t.premium_cutoff_time is not null and to_date(to_char(t.base_date, 'MM/DD/YY ') || to_char(t.premium_cutoff_time, 'HH:MI:SS AM'), 'MM/DD/YY HH:MI:SS AM') > ?)) "
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
		ps.setInt(5, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(6, EnumReservationStatus.EXPIRED.getCode());
		ps.setInt(7, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(8, EnumReservationStatus.EXPIRED.getCode());
		ps.setString(9, address.getServiceType().getName());
		//ps.setDouble(6, address.getLongitude());
		ps.setBigDecimal(10, new java.math.BigDecimal(address.getLongitude()));
		//ps.setDouble(7, address.getLatitude());
		ps.setBigDecimal(11, new java.math.BigDecimal(address.getLatitude()));
		ps.setDate(12, new java.sql.Date(startDate.getTime()));
		ps.setDate(13, new java.sql.Date(endDate.getTime()));
		ps.setDate(14, new java.sql.Date(startDate.getTime()));
		ps.setDate(15, new java.sql.Date(endDate.getTime()));
		Calendar cal = Calendar.getInstance();
		ps.setTimestamp(16, new java.sql.Timestamp(cal.getTimeInMillis()));
		cal.add(Calendar.MINUTE, -1*FDStoreProperties.getSameDayMediaAfterCutoffDuration());
		ps.setTimestamp(17, new java.sql.Timestamp(cal.getTimeInMillis()));
		

		ResultSet rs = ps.executeQuery();
		List<DlvTimeslotModel> timeslots = processTimeslotResultSet(rs, true);
		rs.close();
		ps.close();
		return timeslots;
	}
	

	private static final String CF_TIMESLOTS =
		"select ts.id, ts.base_date, ts.start_time, ts.end_time, ts.cutoff_time, ts.status, ts.zone_id, ts.capacity, z.zone_code, ts.ct_capacity" 
		+" , ta.AREA AREA_CODE, ta.STEM_MAX_TIME stemmax, ta.STEM_FROM_TIME stemfrom, ta.STEM_TO_TIME stemto, ta.ZONE_ECOFRIENDLY ecoFriendly, ta.STEERING_RADIUS steeringRadius, z.NAME ZONE_NAME, " 
		+" case when ts.premium_cutoff_time is null then TO_CHAR(ts.CUTOFF_TIME, 'HH_MI_PM') else TO_CHAR(ts.premium_cutoff_time, 'HH_MI_PM') end WAVE_CODE, ts.IS_DYNAMIC IS_DYNAMIC, ts.IS_CLOSED IS_CLOSED, tr.IS_DEPOT IS_DEPOT, a.DELIVERY_RATE AREA_DLV_RATE,  "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? and chefstable = ' ' and class is null) as base_allocation, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? and chefstable = 'X' and class is null) as ct_allocation, "
			+ "(select z.ct_release_time from dlv.zone z where z.id = ts.zone_id) as ct_release_time, "
			+ "(select z.ct_active from dlv.zone z where z.id = ts.zone_id) as ct_active, "
			+ "ts.premium_cutoff_time, ts.premium_capacity, ts.premium_ct_capacity, "
		  	+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? and class = 'P') as premium_base_allocation, " 
		  	+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? and class = 'PC') as premium_ct_allocation, " 
		  	+ "(select z.premium_ct_release_time from dlv.zone z where z.id = ts.zone_id) as premium_ct_release_time, "
		  	+ "(select z.premium_ct_active from dlv.zone z where z.id = ts.zone_id) as premium_ct_active "
			+ "from dlv.region r, dlv.region_data rd, dlv.timeslot ts, dlv.zone z, transp.zone ta, transp.trn_area a,transp.trn_region tr "
			+ "where r.id=rd.region_id and rd.id=z.region_data_id and ts.ZONE_ID = z.ID and z.ZONE_CODE = ta.ZONE_CODE and ta.AREA = a.CODE and a.region_code = tr.code "
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
		ps.setInt(5, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(6, EnumReservationStatus.EXPIRED.getCode());
		ps.setInt(7, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(8, EnumReservationStatus.EXPIRED.getCode());
		
		ps.setDate(9, new java.sql.Date(startDate.getTime()));
		ps.setDate(10, new java.sql.Date(endDate.getTime()));
		ps.setString(11, address.getServiceType().getName());
		//ps.setDouble(8, address.getLongitude());
		ps.setBigDecimal(12, new java.math.BigDecimal(address.getLongitude()));
		//ps.setDouble(9, address.getLatitude());
		ps.setBigDecimal(13, new java.math.BigDecimal(address.getLatitude()));

		ResultSet rs = ps.executeQuery();
		List<DlvTimeslotModel> timeslots = processTimeslotResultSet(rs, false);
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

	private static List<DlvTimeslotModel> processTimeslotResultSet(ResultSet rs, boolean checkPremium) throws SQLException {
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
			
			lst.add(getTimeslot(rs, checkPremium));
		}
		return lst;
	}
	
	private static List<DlvTimeslotModel> processTimeslotCompositeResultSet(ResultSet rs, boolean checkPremium) throws SQLException {
		List<DlvTimeslotModel> lst = new ArrayList<DlvTimeslotModel>();
		while (rs.next()) {			
			lst.add(getTimeslot(rs, checkPremium));
		}
		return lst;
	}

	private static DlvTimeslotModel getTimeslot(ResultSet rs, boolean checkPremium) throws SQLException {

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
		

		TimeOfDay premiumCutoffTime = (rs.getTime("PREMIUM_CUTOFF_TIME")!=null)?new TimeOfDay(rs.getTime("PREMIUM_CUTOFF_TIME")):null;
		int premiumCapacity = rs.getInt("PREMIUM_CAPACITY");
		int premiumCtCapacity = rs.getInt("PREMIUM_CT_CAPACITY");
		int premiumCtReleaseTime = rs.getInt("PREMIUM_CT_RELEASE_TIME");
		boolean premiumCtActive = "X".equals(rs.getString("PREMIUM_CT_ACTIVE"));
		int premiumBaseAllocation = rs.getInt("PREMIUM_BASE_ALLOCATION");
		int premiumCtAllocation = rs.getInt("PREMIUM_CT_ALLOCATION");
		boolean premiumSlot = false;
		DlvTimeslotModel _tmpSlot =new DlvTimeslotModel(pk, zoneId, baseDate, startTime, endTime, cutoffTime, status, capacity, 
				ctCapacity, baseAllocation, ctAllocation, ctReleaseTime,ctActive,zoneCode, premiumCapacity,
				  premiumCtCapacity, premiumCutoffTime, premiumCtReleaseTime, 
				  premiumCtActive, premiumBaseAllocation,  premiumCtAllocation, premiumSlot);
		
		if(checkPremium)
		{
			premiumSlot = DateUtil.isPremiumSlot(baseDate, cutoffTime, premiumCutoffTime, FDStoreProperties.getSameDayMediaAfterCutoffDuration());
			if(premiumSlot)
			{
				_tmpSlot.setChefsTableCapacity(premiumCtCapacity);
				_tmpSlot.setCapacity(premiumCapacity);
				_tmpSlot.setBaseAllocation(premiumBaseAllocation);
				_tmpSlot.setChefsTableAllocation(premiumCtAllocation);
				_tmpSlot.setCtReleaseTime(premiumCtReleaseTime);
				_tmpSlot.setCtActive(premiumCtActive);
				_tmpSlot.setPremiumSlot(premiumSlot);
			}
		}
		
		// Prepare routing slot configuration from result set
		IDeliverySlot routingSlot = new DeliverySlot();
		routingSlot.setStartTime(rs.getTimestamp("START_TIME"));
		routingSlot.setStopTime(rs.getTimestamp("END_TIME"));
		routingSlot.setWaveCode(rs.getString("WAVE_CODE"));
		routingSlot.setDynamicActive("X".equalsIgnoreCase(rs.getString("IS_DYNAMIC")) ? true : false);
		routingSlot.setManuallyClosed("X".equalsIgnoreCase(rs.getString("IS_CLOSED")) ? true : false);
		routingSlot.setZoneCode(zoneCode);
		routingSlot.setEcoFriendly(rs.getInt("ecoFriendly"));
		routingSlot.setSteeringRadius(rs.getInt("steeringRadius"));

		IRoutingSchedulerIdentity _schId = new RoutingSchedulerIdentity();
		_schId.setDeliveryDate(baseDate);
		
		IAreaModel _aModel = new AreaModel();
		_aModel.setAreaCode(rs.getString("AREA_CODE"));
		_aModel.setDeliveryRate(rs.getDouble("AREA_DLV_RATE"));
		_aModel.setStemFromTime(rs.getInt("stemfrom"));
		_aModel.setStemToTime(rs.getInt("stemto"));
		
		IRegionModel _rModel = new RegionModel();
		_rModel.setDepot("X".equalsIgnoreCase(rs.getString("IS_DEPOT")) ? true : false);
		_rModel.setRegionCode(rs.getString("REGION_CODE"));
		_rModel.setName(rs.getString("REGION_NAME"));
		_rModel.setDescription(rs.getString("REGION_DESCR"));
		_aModel.setRegion(_rModel);
		
		_schId.setRegionId(RoutingUtil.getRegion(_aModel));
		_schId.setArea(_aModel);
		_schId.setDepot(_aModel.isDepot());
		
		routingSlot.setSchedulerId(_schId);
		_tmpSlot.setRoutingSlot(routingSlot);
		
		return _tmpSlot; 
	}
	
	private static final String DEPOT_TIMESLOTS =
		"select ts.id, ts.base_date, ts.start_time, ts.end_time, ts.cutoff_time, ts.status, ts.zone_id, ts.capacity, z.zone_code, ts.ct_capacity" +
		", ta.AREA AREA_CODE, ta.STEM_MAX_TIME stemmax, ta.STEM_FROM_TIME stemfrom, ta.STEM_TO_TIME stemto, ta.ZONE_ECOFRIENDLY ecoFriendly, ta.STEERING_RADIUS steeringRadius, z.NAME ZONE_NAME, " +
		"case when ts.premium_cutoff_time is null then TO_CHAR(ts.CUTOFF_TIME, 'HH_MI_PM') else TO_CHAR(ts.premium_cutoff_time, 'HH_MI_PM') end WAVE_CODE, ts.IS_DYNAMIC IS_DYNAMIC, ts.IS_CLOSED IS_CLOSED, tr.IS_DEPOT IS_DEPOT, tr.code REGION_CODE, tr.name REGION_NAME, tr.description REGION_DESCR, a.DELIVERY_RATE AREA_DLV_RATE,"
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? and chefstable = ' ' and class is null) as base_allocation, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? and chefstable = 'X' and class is null) as ct_allocation, "
			+ "(select z.ct_release_time from dlv.zone z where z.id = ts.zone_id) as ct_release_time, "
			+ "(select z.ct_active from dlv.zone z where z.id = ts.zone_id) as ct_active, "
			+ "ts.premium_cutoff_time, ts.premium_capacity, ts.premium_ct_capacity, "
			+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? and class = 'P') as premium_base_allocation, " 
		  	+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? and class = 'PC') as premium_ct_allocation, " 
		  	+ "(select z.premium_ct_release_time from dlv.zone z where z.id = ts.zone_id) as premium_ct_release_time, "
		  	+ "(select z.premium_ct_active from dlv.zone z where z.id = ts.zone_id) as premium_ct_active "
			+ "from dlv.region r, dlv.region_data rd, dlv.timeslot ts, dlv.zone z, transp.zone ta, transp.trn_area a,transp.trn_region tr "
			+ "where r.id=rd.region_id "
			+ "and rd.start_date=( "
			+ "select max(start_date) from dlv.region_data rd1 "
			+ "where rd1.start_date<=ts.base_date and rd1.region_id = r.id) "
			+ "and ts.base_date >= ? and ts.base_date < ? "
			+ "and r.id = ? and rd.id = z.region_data_id and z.zone_code = ? and ts.ZONE_ID = z.ID and z.ZONE_CODE = ta.ZONE_CODE and ta.AREA = a.CODE and a.region_code = tr.code  "
			+ "and (( ts.premium_cutoff_time is null and to_date(to_char(ts.base_date-1, 'MM/DD/YY ') || to_char(ts.cutoff_time, 'HH:MI:SS AM'), 'MM/DD/YY HH:MI:SS AM') > ?) or" +
			" (ts.premium_cutoff_time is not null and to_date(to_char(ts.base_date, 'MM/DD/YY ') || to_char(ts.premium_cutoff_time, 'HH:MI:SS AM'), 'MM/DD/YY HH:MI:SS AM') > ?)) "
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
		ps.setInt(5, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(6, EnumReservationStatus.EXPIRED.getCode());
		ps.setInt(7, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(8, EnumReservationStatus.EXPIRED.getCode());
		
		ps.setDate(9, new java.sql.Date(startDate.getTime()));
		ps.setDate(10, new java.sql.Date(endDate.getTime()));
		ps.setString(11, regionId);
		ps.setString(12, zoneCode);
		Calendar cal = Calendar.getInstance();
		ps.setTimestamp(13, new java.sql.Timestamp(cal.getTimeInMillis()));
		cal.add(Calendar.MINUTE, -1*FDStoreProperties.getSameDayMediaAfterCutoffDuration());
		ps.setTimestamp(14, new java.sql.Timestamp(cal.getTimeInMillis()));
		

		ResultSet rs = ps.executeQuery();
		List<DlvTimeslotModel> timeslots = processTimeslotResultSet(rs, true);
		rs.close();
		ps.close();
		return timeslots;
	}

	private static final String TIMESLOT_BY_ID =
		"select distinct ts.id, ts.base_date, ts.start_time, ts.end_time, ts.cutoff_time, ts.status, ts.zone_id, ts.capacity, ts.ct_capacity" +
		", ta.AREA AREA_CODE, ta.STEM_MAX_TIME stemmax, ta.STEM_FROM_TIME stemfrom, ta.STEM_TO_TIME stemto, ta.ZONE_ECOFRIENDLY ecoFriendly, ta.STEERING_RADIUS steeringRadius, z.NAME ZONE_NAME, " +
		"case when ts.premium_cutoff_time is null then TO_CHAR(ts.CUTOFF_TIME, 'HH_MI_PM') else TO_CHAR(ts.premium_cutoff_time, 'HH_MI_PM') end WAVE_CODE, ts.IS_DYNAMIC IS_DYNAMIC, ts.IS_CLOSED IS_CLOSED, tr.IS_DEPOT IS_DEPOT, tr.code REGION_CODE, tr.name REGION_NAME, tr.description REGION_DESCR,  a.DELIVERY_RATE AREA_DLV_RATE,"
			+ "(select count(reservation.TIMESLOT_ID) from dlv.reservation "
			+ "where zone_id = ts.zone_id AND ts.ID = reservation.TIMESLOT_ID and status_code <> ? and status_code <> ? and chefstable = ' ' and class is null) as base_allocation, "
			+ "(select count(reservation.TIMESLOT_ID) from dlv.reservation "
			+ "where zone_id = ts.zone_id AND ts.ID = reservation.TIMESLOT_ID and status_code <> ? and status_code <> ? and chefstable = 'X' and class is null) as ct_allocation, "
			+ "(select z.ct_release_time from dlv.zone z where z.id = ts.zone_id) as ct_release_time, "
			+ "(select z.ct_active from dlv.zone z where z.id = ts.zone_id) as ct_active, "
			+ "(select z.zone_code from dlv.zone z where z.id=ts.zone_id ) as zone_code, "
			+ "ts.premium_cutoff_time, ts.premium_capacity, ts.premium_ct_capacity, " 
		  	+ "(select count(reservation.TIMESLOT_ID) from dlv.reservation "
			+ "where zone_id = ts.zone_id AND ts.ID = reservation.TIMESLOT_ID and status_code <> ? and status_code <> ? and class = 'P') as premium_base_allocation, " 
			+ "(select count(reservation.TIMESLOT_ID) from dlv.reservation "
			+ "where zone_id = ts.zone_id AND ts.ID = reservation.TIMESLOT_ID and status_code <> ? and status_code <> ? and class = 'PC') as premium_ct_allocation, " 
			+ "(select z.premium_ct_release_time from dlv.zone z where z.id = ts.zone_id) as premium_ct_release_time, "
			+ "(select z.premium_ct_active from dlv.zone z where z.id = ts.zone_id) as premium_ct_active "
			+ " from dlv.timeslot ts, dlv.zone z, transp.zone ta, transp.trn_area a,transp.trn_region tr,  dlv.reservation r "
			+ "where ts.id = ? AND ts.ZONE_ID = z.ID and z.ZONE_CODE = ta.ZONE_CODE and ta.AREA = a.CODE  and a.region_code = tr.code  AND ts.id=r.TIMESLOT_ID(+)";

	public static DlvTimeslotModel getTimeslotById(Connection conn, String timeslotId, boolean checkPremium) throws SQLException, FinderException {
		PreparedStatement ps = conn.prepareStatement(TIMESLOT_BY_ID);
		ps.setInt(1, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(2, EnumReservationStatus.EXPIRED.getCode());
		ps.setInt(3, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(4, EnumReservationStatus.EXPIRED.getCode());
		ps.setInt(5, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(6, EnumReservationStatus.EXPIRED.getCode());
		ps.setInt(7, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(8, EnumReservationStatus.EXPIRED.getCode());
		
		ps.setString(9, timeslotId);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			return getTimeslot(rs, checkPremium);
		} else {
			throw new FinderException("No timeslot found for timeslotId: " + timeslotId);
		}
	}

	private static final String RESERVATION_FOR_CUSTOMER="SELECT R.ID, R.ORDER_ID, R.CUSTOMER_ID, R.STATUS_CODE, R.TIMESLOT_ID, R.ZONE_ID" +
			", R.EXPIRATION_DATETIME, R.TYPE, R.ADDRESS_ID,T.BASE_DATE, Z.ZONE_CODE,R.UNASSIGNED_DATETIME, R.UNASSIGNED_ACTION" +
			", R.IN_UPS, R.ORDER_SIZE, R.SERVICE_TIME, R.RESERVED_ORDER_SIZE, R.RESERVED_SERVICE_TIME, R.UPDATE_STATUS, R.METRICS_SOURCE " +
			", R.NUM_CARTONS , R.NUM_FREEZERS , R.NUM_CASES, R.CLASS " +
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
			", R.NUM_CARTONS , R.NUM_FREEZERS , R.NUM_CASES, R.CLASS " +
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

	private static UnassignedDlvReservationModel loadUnassignedDlvReservationFromResultSet(ResultSet rs) throws SQLException {
		return new UnassignedDlvReservationModel(
			new PrimaryKey(rs.getString("ID")),
			rs.getString("ORDER_ID"),
			rs.getString("CUSTOMER_ID"),
			rs.getInt("STATUS_CODE"),
			rs.getTimestamp("EXPIRATION_DATETIME"),
			rs.getString("TIMESLOT_ID"),
			rs.getString("ZONE_ID"),
			EnumReservationType.getEnum(rs.getString("TYPE")),
			rs.getString("ADDRESS")!=null?getAddress(rs):null,
			rs.getDate("BASE_DATE"),
			rs.getString("CUTOFF_TIME"),rs.getTimestamp("STIME"), rs.getTimestamp("ETIME"),
			rs.getString("ZONE_CODE"),
			RoutingActivityType.getEnum( rs.getString("UNASSIGNED_ACTION")) ,
			"X".equalsIgnoreCase(rs.getString("IN_UPS"))?true:false
			, rs.getBigDecimal("ORDER_SIZE") != null ? new Double(rs.getDouble("ORDER_SIZE")) : null
			, rs.getBigDecimal("SERVICE_TIME") != null ? new Double(rs.getDouble("SERVICE_TIME")) : null
			, rs.getBigDecimal("RESERVED_ORDER_SIZE") != null ? new Double(rs.getDouble("RESERVED_ORDER_SIZE")) : null
			, rs.getBigDecimal("RESERVED_SERVICE_TIME") != null ? new Double(rs.getDouble("RESERVED_SERVICE_TIME")) : null
			, rs.getBigDecimal("NUM_CARTONS") != null ? new Long(rs.getLong("NUM_CARTONS")) : null
			, rs.getBigDecimal("NUM_CASES") != null ? new Long(rs.getLong("NUM_CASES")) : null
			, rs.getBigDecimal("NUM_FREEZERS") != null ? new Long(rs.getLong("NUM_FREEZERS")) : null
			, EnumReservationClass.getEnum(rs.getString("CLASS"))
			, EnumRoutingUpdateStatus.getEnum(rs.getString("UPDATE_STATUS"))
			, EnumOrderMetricsSource.getEnum(rs.getString("METRICS_SOURCE")));
		
	}
	
	private static PhoneNumber convertPhoneNumber(String phone, String extension) {
		return "() -".equals(phone) ? null : new PhoneNumber(phone, NVL.apply(extension, ""));
	}
	private static ErpAddressModel getAddress(ResultSet rs) throws SQLException{
		
		ErpAddressModel model=new ErpAddressModel();
		model.setFirstName(rs.getString("FIRST_NAME"));
		model.setLastName(rs.getString("LAST_NAME"));
		model.setAddress1(rs.getString("ADDRESS1"));
		model.setAddress2(NVL.apply(rs.getString("ADDRESS2"), "").trim());
		model.setApartment(NVL.apply(rs.getString("APARTMENT"), "").trim());
		model.setCity(rs.getString("CITY"));
		model.setState(rs.getString("STATE"));
		model.setZipCode(rs.getString("ZIP"));
		model.setCountry(rs.getString("COUNTRY"));
		model.setPhone(convertPhoneNumber( rs.getString("PHONE"), rs.getString("PHONE_EXT") ));
		model.setAltContactPhone(convertPhoneNumber( rs.getString("ALT_PHONE"), rs.getString("ALT_PHONE_EXT") ));
		model.setInstructions(rs.getString("DELIVERY_INSTRUCTIONS"));
		model.setServiceType(EnumServiceType.getEnum(rs.getString("SERVICE_TYPE")));
		model.setCompanyName(rs.getString("COMPANY_NAME"));
		model.setAltDelivery(EnumDeliverySetting.getDeliverySetting(rs.getString("ALT_DEST")));
		model.setAltFirstName(rs.getString("ALT_FIRST_NAME"));
		model.setAltLastName(rs.getString("ALT_LAST_NAME"));
		model.setAltApartment(rs.getString("ALT_APARTMENT"));
		model.setAltPhone(convertPhoneNumber(rs.getString("ALT_CONTACT_PHONE"), rs.getString("ALT_CONTACT_EXT")));		
		model.setUnattendedDeliveryFlag(EnumUnattendedDeliveryFlag.fromSQLValue(rs.getString("UNATTENDED_FLAG")));
		model.setUnattendedDeliveryInstructions(rs.getString("UNATTENDED_INSTR"));
		model.setCustomerId(rs.getString("CUSTOMER_ID"));
		AddressInfo addressInfo = new AddressInfo();
		addressInfo.setLongitude(Double.parseDouble((rs.getBigDecimal("LONGITUDE")!=null)?rs.getBigDecimal("LONGITUDE").toString():"0"));
		addressInfo.setLatitude(Double.parseDouble((rs.getBigDecimal("LATITUDE")!=null)?rs.getBigDecimal("LATITUDE").toString():"0"));
		model.setAddressInfo(addressInfo);
		return model;
		
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
			, rs.getBigDecimal("NUM_CASES") != null ? new Long(rs.getLong("NUM_CASES")) : null
			, rs.getBigDecimal("NUM_FREEZERS") != null ? new Long(rs.getLong("NUM_FREEZERS")) : null
			, EnumReservationClass.getEnum(rs.getString("CLASS"))
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
	    PreparedStatement ps = conn.prepareStatement("UPDATE DLV.RESERVATION SET STATUS_CODE = ?,ORDER_ID='x'||ID, MODIFIED_DTTM=SYSDATE WHERE ID = ?");
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
			LOGGER.debug("getZoneInfo[geocodeAddress] :"+address);
			geocodeAddress(conn, address, useApartment);
		}

		DlvZoneInfoModel response;
		
		LOGGER.debug("getZoneInfo[QUERY] :"+address+"->"+date);
		PreparedStatement ps = conn.prepareStatement(ZONE_CODE);
		ps.setString(1, address.getServiceType().getName());
		ps.setDate(2, new java.sql.Date(date.getTime()));
		//ps.setDouble(3, address.getLongitude());
		ps.setBigDecimal(3, new java.math.BigDecimal(address.getLongitude()));
		//ps.setDouble(4, address.getLatitude());
		ps.setBigDecimal(4, new java.math.BigDecimal(address.getLatitude()));

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
			LOGGER.debug("DlvManagerDAO.getZoneInfo(DONOT_DELIVER): " + address);
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
		//ps.setDouble(2, address.getLongitude());
		ps.setBigDecimal(2, new java.math.BigDecimal(address.getLongitude()));
		//ps.setDouble(3, address.getLatitude());
		ps.setBigDecimal(3, new java.math.BigDecimal(address.getLatitude()));

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
		", ta.AREA AREA_CODE, ta.STEM_MAX_TIME stemmax, ta.STEM_FROM_TIME stemfrom, ta.STEM_TO_TIME stemto, ta.ZONE_ECOFRIENDLY ecoFriendly, ta.STEERING_RADIUS steeringRadius, z.NAME ZONE_NAME, " +
		"case when t.premium_cutoff_time is null then TO_CHAR(t.CUTOFF_TIME, 'HH_MI_PM') else TO_CHAR(t.premium_cutoff_time, 'HH_MI_PM') end WAVE_CODE, t.IS_DYNAMIC IS_DYNAMIC, t.IS_CLOSED IS_CLOSED, tr.IS_DEPOT IS_DEPOT, tr.code REGION_CODE, tr.name REGION_NAME, tr.description REGION_DESCR,  a.DELIVERY_RATE AREA_DLV_RATE," 
			+ "(select count(*) from dlv.reservation where timeslot_id=t.id and status_code <> ? and status_code <> ? and chefstable = ' ' and class is null) as base_allocation, "
			+ "(select count(*) from dlv.reservation where timeslot_id=t.id and status_code <> ? and status_code <> ? and chefstable = 'X' and class is null) as ct_allocation, "
			+ "(select z.ct_release_time from dlv.zone z where z.id = t.zone_id) as ct_release_time, "
			+ "(select z.ct_active from dlv.zone z where z.id = t.zone_id) as ct_active, p.zone_code, "
			+ "t.premium_cutoff_time, t.premium_capacity, t.premium_ct_capacity, " 
		  	+ "(select count(*) from dlv.reservation where timeslot_id=t.id and status_code <> ? and status_code <> ? and class = 'P') as premium_base_allocation, " 
		  	+ "(select count(*) from dlv.reservation where timeslot_id=t.id and status_code <> ? and status_code <> ? and class = 'PC') as premium_ct_allocation, " 
		  	+ "(select z.premium_ct_release_time from dlv.zone z where z.id = t.zone_id) as premium_ct_release_time, "
			+ "(select z.premium_ct_active from dlv.zone z where z.id = t.zone_id) as premium_ct_active "
			+ "from dlv.planning_resource p, dlv.timeslot t, dlv.zone z, transp.zone ta, transp.trn_area a,transp.trn_region tr " 
			+ "where p.id = t.resource_id and t.ZONE_ID = z.ID and z.ZONE_CODE = ta.ZONE_CODE and ta.AREA = a.CODE  and a.region_code = tr.code  and p.zone_code = ? " 
			+ "and p.day >= ? and p.day < ? "
			+ "and to_date(to_char(t.base_date-1, 'MM/DD/YY ') || to_char(t.cutoff_time, 'HH:MI:SS AM'), 'MM/DD/YY HH:MI:SS AM') > SYSDATE "
			+ "order by t.base_date, p.zone_code, t.start_time ";
	
	public static DlvZoneCapacityInfo getZoneCapacity(Connection conn, String zoneCode, Date start, Date end) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(ZONE_CAPACITY_QUERY);
		ps.setInt(1, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(2, EnumReservationStatus.EXPIRED.getCode());
		ps.setInt(3, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(4, EnumReservationStatus.EXPIRED.getCode());
		ps.setInt(5, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(6, EnumReservationStatus.EXPIRED.getCode());
		ps.setInt(7, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(8, EnumReservationStatus.EXPIRED.getCode());
		ps.setString(9, zoneCode);
		ps.setDate(10, new java.sql.Date(start.getTime()));
		ps.setDate(11, new java.sql.Date(end.getTime()));
		
		ResultSet rs = ps.executeQuery();
		List<DlvTimeslotModel> timeslots = new ArrayList<DlvTimeslotModel>();
		while(rs.next()) {
			timeslots.add(getTimeslot(rs, true));
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
		//ps.setDouble(1, address.getLongitude());
		ps.setBigDecimal(1, new java.math.BigDecimal(address.getLongitude()));
		//ps.setDouble(2, address.getLatitude());
		ps.setBigDecimal(2, new java.math.BigDecimal(address.getLatitude()));
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
		//ps.setDouble(1, address.getLongitude());
		ps.setBigDecimal(1, new java.math.BigDecimal(address.getLongitude()));
		//ps.setDouble(2, address.getLatitude());
		ps.setBigDecimal(2, new java.math.BigDecimal(address.getLatitude()));
		
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
		"select distinct case when t.premium_cutoff_time is null then t.cutoff_time else t.premium_cutoff_time end as cutofftime " +
		"from dlv.planning_resource p, dlv.timeslot t " +
		"where p.id = t.resource_id " +
		"and p.day = ? order by cutofftime asc ";
	
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
	
	private static final String UPDATE_RESERVATIONSTATUSSOURCE_QUERY = "UPDATE DLV.RESERVATION SET NUM_CARTONS = ? , NUM_FREEZERS = ? , NUM_CASES = ?" +
																			" , UPDATE_STATUS = ?, METRICS_SOURCE = ?  WHERE ID=?";
	public static void setReservationMetricsDetails(Connection conn, String reservationId
														, long noOfCartons, long noOfCases, long noOfFreezers
														, String status, EnumOrderMetricsSource source)  throws SQLException {

		PreparedStatement ps = conn.prepareStatement(UPDATE_RESERVATIONSTATUSSOURCE_QUERY);
		//ps.setDouble(1, noOfCartons);
		ps.setBigDecimal(1, new java.math.BigDecimal(noOfCartons));
		//ps.setDouble(2, noOfFreezers);
		ps.setBigDecimal(2, new java.math.BigDecimal(noOfFreezers));
		//ps.setDouble(3, noOfCases);
		ps.setBigDecimal(3, new java.math.BigDecimal(noOfCases));
		

		ps.setString(4, status);
		ps.setString(5, source.value());
		ps.setString(6, reservationId);
		if(ps.executeUpdate() != 1){
			ps.close();
			throw new SQLException("Cannot find reservation to setReservationMetricsDetails for id: " + reservationId);
		}
		ps.close();
	}

	private static final String UPDATE_RESERVATIONSTATUS_QUERY = "UPDATE DLV.RESERVATION SET NUM_CARTONS = ? , NUM_FREEZERS = ? , NUM_CASES = ?" +
			" , UPDATE_STATUS = ? WHERE ID=?";
	public static void setReservationMetricsDetails(Connection conn, String reservationId
													, long noOfCartons, long noOfCases, long noOfFreezers
													, String status)  throws SQLException {
		
		PreparedStatement ps = conn.prepareStatement(UPDATE_RESERVATIONSTATUS_QUERY);
		//ps.setDouble(1, noOfCartons);
		ps.setBigDecimal(1, new java.math.BigDecimal(noOfCartons));
		//ps.setDouble(2, noOfFreezers);
		ps.setBigDecimal(2, new java.math.BigDecimal(noOfFreezers));
		//ps.setDouble(3, noOfCases);
		ps.setBigDecimal(3, new java.math.BigDecimal(noOfCases));
		
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
		//ps.setDouble(1, noOfCartons);
		ps.setBigDecimal(1, new java.math.BigDecimal(noOfCartons));
		//ps.setDouble(2, noOfFreezers);
		ps.setBigDecimal(2, new java.math.BigDecimal(noOfFreezers));
		//ps.setDouble(3, noOfCases);
		ps.setBigDecimal(3, new java.math.BigDecimal(noOfCases));
		

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
		//ps.setDouble(1, reservedOrderSize);
		ps.setBigDecimal(1, new java.math.BigDecimal(reservedOrderSize));
		//ps.setDouble(2, reservedServiceTime);
		ps.setBigDecimal(2, new java.math.BigDecimal(reservedServiceTime));
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
		//ps.setDouble(1, reservedOrderSize);
		ps.setBigDecimal(1, new java.math.BigDecimal(reservedOrderSize));
		//ps.setDouble(2, reservedServiceTime);
		ps.setBigDecimal(2, new java.math.BigDecimal(reservedServiceTime));
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
	
	private static final String FETCH_UNASSIGNED_RESERVATIONS_QUERY="SELECT  R.ID, R.ORDER_ID, R.CUSTOMER_ID, R.STATUS_CODE, R.TIMESLOT_ID, R.ZONE_ID, R.EXPIRATION_DATETIME, R.TYPE, R.ADDRESS_ID, "+
	" T.BASE_DATE,to_char(T.CUTOFF_TIME, 'HH:MI AM') CUTOFF_TIME, T.START_TIME STIME, T.END_TIME ETIME, Z.ZONE_CODE,R.UNASSIGNED_DATETIME, R.UNASSIGNED_ACTION, R.IN_UPS, R.ORDER_SIZE, R.SERVICE_TIME, R.RESERVED_ORDER_SIZE" +
	", R.RESERVED_SERVICE_TIME, R.UPDATE_STATUS, R.METRICS_SOURCE " +
	", R.NUM_CARTONS , R.NUM_FREEZERS , R.NUM_CASES, R.CLASS, " +
	" A.ID as ADDRESS, A.FIRST_NAME,A.LAST_NAME,A.ADDRESS1,A.ADDRESS2,A.APARTMENT,A.CITY,A.STATE,A.ZIP,A.COUNTRY, "+
	" A.PHONE,A.PHONE_EXT,A.DELIVERY_INSTRUCTIONS,A.SCRUBBED_ADDRESS,A.ALT_DEST,A.ALT_FIRST_NAME, "+
	" A.ALT_LAST_NAME,A.ALT_APARTMENT,A.ALT_PHONE,A.ALT_PHONE_EXT,A.LONGITUDE,A.LATITUDE,A.SERVICE_TYPE, "+
	" A.COMPANY_NAME,A.ALT_CONTACT_PHONE,A.ALT_CONTACT_EXT,A.UNATTENDED_FLAG,A.UNATTENDED_INSTR,A.CUSTOMER_ID "+
	" FROM DLV.RESERVATION R, DLV.TIMESLOT T, DLV.ZONE Z,CUST.ADDRESS A "+
	" WHERE R.ADDRESS_ID=A.ID(+) AND R.TIMESLOT_ID=T.ID AND R.ZONE_ID=Z.ID AND t.BASE_DATE=TRUNC(?) " +
	"AND (unassigned_action IS NOT NULL OR (UPDATE_STATUS IS NOT NULL AND UPDATE_STATUS <> 'SUS'))  ";
	
	
	
	public static List<UnassignedDlvReservationModel> getUnassignedReservations(Connection conn, Date _date,boolean includeCutoff)  throws SQLException {
		
	final StringBuffer updateQ = new StringBuffer();
	updateQ.append(FETCH_UNASSIGNED_RESERVATIONS_QUERY);
	if(includeCutoff)
	{
		updateQ.append(" AND to_char(t.cutoff_time, 'HH:MI AM') <= to_char(SYSDATE, 'HH:MI AM') AND to_char(t.cutoff_time, 'HH:MI AM') >= to_char(SYSDATE-1/96, 'HH:MI AM')");
	}
	else
	{
	updateQ.append(" and (( t.premium_cutoff_time is null and to_date(to_char(t.base_date-1, 'MM/DD/YY ') || to_char(t.cutoff_time, 'HH:MI:SS AM'), 'MM/DD/YY HH:MI:SS AM') > SYSDATE+1/96 ) or" +
	" (t.premium_cutoff_time is not null and to_date(to_char(t.base_date, 'MM/DD/YY ') || to_char(t.premium_cutoff_time, 'HH:MI:SS AM'), 'MM/DD/YY HH:MI:SS AM') > SYSDATE+1/96 )) ORDER BY R.unassigned_action, R.UPDATE_STATUS NULLS LAST");
	}
	
	PreparedStatement ps =
			conn.prepareStatement(updateQ.toString());
		ps.setDate(1, new java.sql.Date(_date.getTime()));
		ResultSet rs = ps.executeQuery();
		List<UnassignedDlvReservationModel>  reservations = new ArrayList<UnassignedDlvReservationModel>();
		while (rs.next()) {
			UnassignedDlvReservationModel rsv = loadUnassignedDlvReservationFromResultSet(rs);
			reservations.add(rsv);
		}

		rs.close();
		ps.close();
       
		return reservations;
	}
	
	private static final String FETCH_REROUTE_RESERVATIONS_QUERY="SELECT R.ID, R.ORDER_ID, R.CUSTOMER_ID, R.STATUS_CODE, R.TIMESLOT_ID, R.ZONE_ID, R.EXPIRATION_DATETIME, R.TYPE, R.ADDRESS_ID, "+
	" T.BASE_DATE,to_char(T.CUTOFF_TIME, 'HH:MI AM') CUTOFF_TIME, T.START_TIME STIME, T.END_TIME ETIME, Z.ZONE_CODE,R.UNASSIGNED_DATETIME, R.UNASSIGNED_ACTION, R.IN_UPS, R.ORDER_SIZE, R.SERVICE_TIME, R.RESERVED_ORDER_SIZE" +
	", R.RESERVED_SERVICE_TIME, R.UPDATE_STATUS, R.METRICS_SOURCE " +
	", R.NUM_CARTONS , R.NUM_FREEZERS , R.NUM_CASES,R.CLASS, " +
	" A.ID as ADDRESS,A.FIRST_NAME,A.LAST_NAME,A.ADDRESS1,A.ADDRESS2,A.APARTMENT,A.CITY,A.STATE,A.ZIP,A.COUNTRY, "+
	" A.PHONE,A.PHONE_EXT,A.DELIVERY_INSTRUCTIONS,A.SCRUBBED_ADDRESS,A.ALT_DEST,A.ALT_FIRST_NAME, "+
	" A.ALT_LAST_NAME,A.ALT_APARTMENT,A.ALT_PHONE,A.ALT_PHONE_EXT,A.LONGITUDE,A.LATITUDE,A.SERVICE_TYPE, "+
	" A.COMPANY_NAME,A.ALT_CONTACT_PHONE,A.ALT_CONTACT_EXT,A.UNATTENDED_FLAG,A.UNATTENDED_INSTR,A.CUSTOMER_ID "+
	"FROM DLV.RESERVATION R, DLV.TIMESLOT T, DLV.ZONE Z, CUST.ADDRESS A "+
	" WHERE  A.ID=R.ADDRESS_ID AND R.TIMESLOT_ID=T.ID AND R.ZONE_ID=Z.ID AND t.BASE_DATE > TRUNC(SYSDATE-1) " +
	" AND  R.DO_REROUTE = 'X' ORDER BY T.BASE_DATE, Z.ZONE_CODE";
	public static List<UnassignedDlvReservationModel> getReRouteReservations(Connection conn)  throws SQLException {
		PreparedStatement ps =
			conn.prepareStatement(FETCH_REROUTE_RESERVATIONS_QUERY);
		
		ResultSet rs = ps.executeQuery();
		List<UnassignedDlvReservationModel>  reservations = new ArrayList<UnassignedDlvReservationModel>();
		while (rs.next()) {
			UnassignedDlvReservationModel rsv = loadUnassignedDlvReservationFromResultSet(rs);
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
		"select ts.id, ts.base_date, ts.start_time, ts.end_time, ts.cutoff_time, ts.premium_cutoff_time, ts.status, ts.zone_id, ts.capacity, ts.premium_capacity,  ta.STEERING_RADIUS steeringRadius," +
		" z.zone_code, ts.ct_capacity,ts.premium_ct_capacity, ta.AREA AREA_CODE, ta.STEM_MAX_TIME stemmax, ta.STEM_FROM_TIME stemfrom, ta.STEM_TO_TIME stemto, ta.ZONE_ECOFRIENDLY ecoFriendly, z.NAME ZONE_NAME, " +
		"case when ts.premium_cutoff_time is null then TO_CHAR(ts.CUTOFF_TIME, 'HH_MI_PM') else TO_CHAR(ts.premium_cutoff_time, 'HH_MI_PM') end WAVE_CODE, ts.IS_DYNAMIC IS_DYNAMIC, ts.IS_CLOSED IS_CLOSED, tr.IS_DEPOT IS_DEPOT, tr.code REGION_CODE, tr.name REGION_NAME, tr.description REGION_DESCR, a.DELIVERY_RATE AREA_DLV_RATE, "
		+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? and chefstable = ' ' and class is null) as base_allocation, "
		+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? and chefstable = 'X' and class is null) as ct_allocation, "
		+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? and class = 'P') as premium_base_allocation, "
		+ "(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code <> ? and status_code <> ? and class = 'PC') as premium_ct_allocation, "
		+ "(select z.ct_release_time from dlv.zone z where z.id = ts.zone_id) as ct_release_time, "
		+ "(select z.premium_ct_release_time from dlv.zone z where z.id = ts.zone_id) as premium_ct_release_time, "
		+ "(select z.ct_active from dlv.zone z where z.id = ts.zone_id) as ct_active, "
		+ "(select z.premium_ct_active from dlv.zone z where z.id = ts.zone_id) as premium_ct_active "
		+ "from dlv.region r, dlv.region_data rd, dlv.timeslot ts, dlv.zone z, transp.zone ta, transp.trn_area a,transp.trn_region tr "
		+ "where r.id=rd.region_id and rd.id=z.region_data_id and ts.ZONE_ID = z.ID and z.ZONE_CODE = ta.ZONE_CODE and ta.AREA = a.CODE " +
		"and (rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= ? and region_id = r.id)	" +
		"or rd.start_date >= (select max(start_date) from dlv.region_data where start_date <= ? and region_id = r.id)) " +
		"and ts.ZONE_ID = z.ID and z.ZONE_CODE = ta.ZONE_CODE and ta.AREA = a.CODE and a.region_code = tr.code and ts.base_date >= rd.start_date	and ts.base_date = ?	" +
		"and ts.is_dynamic = 'X'	" +
		"order by ts.base_date, z.zone_code, ts.start_time";

	public static List getTimeslotsForDate(Connection conn, java.util.Date startDate) throws SQLException {
				
		PreparedStatement ps = conn.prepareStatement(TIMESLOTS_BY_DATE);
		ps.setInt(1, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(2, EnumReservationStatus.EXPIRED.getCode());
		ps.setInt(3, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(4, EnumReservationStatus.EXPIRED.getCode());
		ps.setInt(5, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(6, EnumReservationStatus.EXPIRED.getCode());
		ps.setInt(7, EnumReservationStatus.CANCELED.getCode());
		ps.setInt(8, EnumReservationStatus.EXPIRED.getCode());
		
		ps.setDate(9, new java.sql.Date(startDate.getTime()));
		ps.setDate(10, new java.sql.Date(startDate.getTime()));
		ps.setDate(11, new java.sql.Date(startDate.getTime()));
		
		ResultSet rs = ps.executeQuery();
		
		List timeslots = processTimeslotCompositeResultSet(rs, false);
		rs.close();
		ps.close();
		return timeslots;
	}
	
	private static final String FUTURETIMESLOT_DATES = "select distinct  T.BASE_DATE FUTURE_DATE from dlv.timeslot t where T.BASE_DATE >= trunc(sysdate) order by BASE_DATE";
	
	public static List<Date> getFutureTimeslotDates(Connection conn) throws SQLException {
		
		PreparedStatement ps = conn.prepareStatement(FUTURETIMESLOT_DATES);
				
		ResultSet rs = ps.executeQuery();
		List<Date> result = new ArrayList<Date>();
		while (rs.next()) {			
			result.add(rs.getDate("FUTURE_DATE"));
		}
		rs.close();
		ps.close();
		return result;
	}
	
	
	private static final String UPDATE_TIMESLOTS_BY_ID = "UPDATE DLV.TIMESLOT SET CAPACITY = ? , CT_CAPACITY = ?, PREMIUM_CAPACITY=?, PREMIUM_CT_CAPACITY=? WHERE ID = ?";
	
	public static int updateTimeslotsCapacity(Connection conn, List<DlvTimeslotModel> dlvTimeSlots ) throws SQLException{
		
		PreparedStatement ps = conn.prepareStatement(UPDATE_TIMESLOTS_BY_ID);
		
	    
	    for (DlvTimeslotModel _slot : dlvTimeSlots) {
	    	if(_slot != null && _slot.getRoutingSlot() != null && 
	    			_slot.getRoutingSlot().getDeliveryMetrics() != null) {
				ps.setInt(1, _slot.getRoutingSlot().getDeliveryMetrics().getOrderCapacity());
				ps.setInt(2, _slot.getRoutingSlot().getDeliveryMetrics().getOrderCtCapacity());
				ps.setInt(3, _slot.getRoutingSlot().getDeliveryMetrics().getOrderPremiumCapacity());
				ps.setInt(4, _slot.getRoutingSlot().getDeliveryMetrics().getOrderPremiumCtCapacity());
			    ps.setString(5, _slot.getId());
			    ps.addBatch();
	    	}
		}
		int[] count = ps.executeBatch();
		ps.close();
		return count.length;
	}
	
	private static final String UPDATE_WAVEINSTANCE_BY_ID = "UPDATE TRANSP.WAVE_INSTANCE SET STATUS = ? " +
																", MODIFIED_DTTM = sysdate " +
																", REFERENCE_ID = ? " +
																", NOTIFICATION_MSG = ? " +
																", FORCE_SYNCHRONIZE = ? " +																
																" WHERE WAVEINSTANCE_ID = ?";
	
	public static int updateWaveInstanceStatus(Connection conn, List<IWaveInstance> waveInstances ) throws SQLException{
		
		PreparedStatement ps = conn.prepareStatement(UPDATE_WAVEINSTANCE_BY_ID);		
	    
	    for (IWaveInstance _instance : waveInstances) {
	    	if(_instance != null) {
	    		ps.setString(1, _instance.getStatus().getName());
	    		ps.setString(2, _instance.getRoutingWaveInstanceId());			    
	    		ps.setString(3, _instance.getNotificationMessage());
	    		ps.setString(4, _instance.isForce() ? "X" : null);
	    		ps.setString(5, _instance.getWaveInstanceId());
			    ps.addBatch();
	    	}
		}
		int[] count = ps.executeBatch();
		ps.close();
		return count.length;
	}
	
	private static final String DELETE_ZERO_RESOURCE_WAVEINSTANCES = "DELETE TRANSP.WAVE_INSTANCE WHERE STATUS = 'SYN' AND RESOURCE_COUNT=0" +
	" AND DELIVERY_DATE = ?";

	
	public static void deleteZeroSyncWaveInstance(Connection conn, IRoutingSchedulerIdentity schedulerId ) throws SQLException{
		
		PreparedStatement ps = conn.prepareStatement(DELETE_ZERO_RESOURCE_WAVEINSTANCES);	
		ps.setDate(1, new java.sql.Date(schedulerId.getDeliveryDate().getTime()));
		ps.execute();
		ps.close();
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
	
	private static final String FIX_DISASSOCIATED_TIMESLOTS =  "UPDATE dlv.timeslot ts set zone_id = ("
		+" select z.id from dlv.zone z, dlv.region_data rd where z.region_data_id=rd.id"
		+" and zone_code=(select zone_code from dlv.zone where ts.zone_id=id)"
		+" and start_date=(select max(start_date) from dlv.region_data"
		+" where region_id=rd.region_id and start_date<=ts.base_date)"
		+" ) where ts.base_date > sysdate"
		+" and ts.zone_id <> ("
		+" select z.id from dlv.zone z, dlv.region_data rd where z.region_data_id=rd.id"
		+" and zone_code=(select zone_code from dlv.zone where ts.zone_id=id)"
		+" and start_date=(select max(start_date) from dlv.region_data"
		+" where region_id=rd.region_id and start_date<=ts.base_date)"
		+" )";
	
	public static void fixDisassociatedTimeslots(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(FIX_DISASSOCIATED_TIMESLOTS);	
		ps.executeUpdate();
		ps.close();
	}

	private static final String CANCEL_RESERVATIONS_QUERY01 = "UPDATE DLV.RESERVATION SET STATUS_CODE = ?, MODIFIED_DTTM=SYSDATE WHERE ID IN (";

	public static int cancelReservations(Connection conn, Set<String> rsvIds) throws SQLException {
		StringBuffer updateQ = new StringBuffer();
		if(rsvIds != null && rsvIds.size() > 0) {			
			updateQ.append(CANCEL_RESERVATIONS_QUERY01);
			int intCount = 0;
			for(String rsvId : rsvIds) {
				updateQ.append("'").append(rsvId).append("'");
				intCount++;
				if(intCount != rsvIds.size()) {
					updateQ.append(",");
				}
			}
			updateQ.append(")");
		}
		
		PreparedStatement ps = conn.prepareStatement(updateQ.toString());
		ps.setInt(1, EnumReservationStatus.ADMINCANCELED.getCode());
		
		int updateCount = ps.executeUpdate();
		return updateCount;
	}
	
	private static final String BLOCK_TIMESLOT_CAPACITY = "UPDATE DLV.TIMESLOT ts SET ts.IS_CLOSED = 'X' WHERE ts.BASE_DATE = ? ";

	public static int blockTimeslotCapacity(Connection conn, Date sourceDate
			, Date cutoffDate, String[] area, Date startTime, Date endTime) throws SQLException {
		
		final StringBuffer updateQ = new StringBuffer();
		updateQ.append(BLOCK_TIMESLOT_CAPACITY);
		if(cutoffDate != null){
			updateQ.append(" and to_char(ts.cutoff_time, 'HH:MI AM') = to_char(?, 'HH:MI AM') ");
		}
		if(startTime != null){
			updateQ.append(" and ts.start_time >= ? ");
		}
		if(endTime != null){
			updateQ.append(" and ts.start_time < ? ");
		}
		if(area != null && area.length > 0){
			updateQ.append(" and ts.zone_id in ( select id from dlv.zone z where z.zone_code in (");
			for(int intCount = 0; intCount < area.length; intCount++ ) {
				updateQ.append("'").append(area[intCount]).append("'");				
				if(intCount < area.length-1) {
					updateQ.append(",");
				}
			}
			updateQ.append("))");
		}	
		PreparedStatement ps = conn.prepareStatement(updateQ.toString());	    
		ps.setDate(1, new java.sql.Date(sourceDate.getTime()));		
		int paramIndex = 2;
		if(cutoffDate != null){ 
			ps.setTimestamp(paramIndex++, new Timestamp(cutoffDate.getTime()));
		}
		if(startTime != null) {
			ps.setTimestamp(paramIndex++, new Timestamp(startTime.getTime()));
		}					
		if(endTime != null) { 
			ps.setTimestamp(paramIndex++, new Timestamp(endTime.getTime())); 
		}
		int updateCount = ps.executeUpdate();
	    ps.close();
		return updateCount;
	}
	
	private static final String UNBLOCK_TIMESLOT_CAPACITY = "UPDATE DLV.TIMESLOT ts SET ts.IS_CLOSED = null WHERE ts.BASE_DATE = ? ";

	public static int unBlockTimeslotCapacity(Connection conn, Date sourceDate
			, Date cutoffDate, String[] area, Date startTime, Date endTime) throws SQLException {
		final StringBuffer updateQ = new StringBuffer();
		updateQ.append(UNBLOCK_TIMESLOT_CAPACITY);
		if(cutoffDate != null){
			updateQ.append(" and to_char(ts.cutoff_time, 'HH:MI AM') = to_char(?, 'HH:MI AM') ");
		}
		if(startTime != null){
			updateQ.append(" and ts.start_time >= ? ");
		}
		if(endTime != null){
			updateQ.append(" and ts.start_time < ? ");
		}
		if(area != null && area.length > 0){
			updateQ.append(" and ts.zone_id in ( select id from dlv.zone z where z.zone_code in (");
			for(int intCount = 0; intCount < area.length; intCount++ ) {
				updateQ.append("'").append(area[intCount]).append("'");				
				if(intCount < area.length-1) {
					updateQ.append(",");
				}
			}
			updateQ.append("))");
		}
		PreparedStatement ps = conn.prepareStatement(updateQ.toString());	    
		ps.setDate(1, new java.sql.Date(sourceDate.getTime()));		
		int paramIndex = 2;
		if(cutoffDate != null){ 
			ps.setTimestamp(paramIndex++, new Timestamp(cutoffDate.getTime()));
		}
		if(startTime != null) {
			ps.setTimestamp(paramIndex++, new Timestamp(startTime.getTime()));
		}
		if(endTime != null) { 
			ps.setTimestamp(paramIndex++, new Timestamp(endTime.getTime())); 
		}
		int updateCount = ps.executeUpdate();
	    ps.close();
		return updateCount;
	}
	
	private static final String SELECT_ACTIVE_FACILITYS = "SELECT F.FACILITY_CODE CODE, FT.FACILITYTYPE_CODE, FT.DESCRIPTION" 
		+" from TRANSP.TRN_FACILITY F, TRANSP.TRN_FACILITYTYPE FT"
		+" where F.FACILITYTYPE_CODE = FT.FACILITYTYPE_CODE";

	public static Map<String, TrnFacilityType> retrieveTrnFacilitys(Connection conn)throws SQLException{

		Map<String, TrnFacilityType> result = new HashMap<String, TrnFacilityType>();
		PreparedStatement ps = conn.prepareStatement(SELECT_ACTIVE_FACILITYS);
		ResultSet rs = ps.executeQuery();

		while(rs.next()){
	  		String code = rs.getString("CODE");
			TrnFacilityType facilityType = new TrnFacilityType();
			facilityType.setName(rs.getString("FACILITYTYPE_CODE"));
			facilityType.setDescription("DESCRIPTION");			
			result.put(code, facilityType);
		}
		return result;
	}
	
	private static final String GET_SECTOR_INFO = "select name, description from transp.sector s, " +
				" transp.sector_zipcode sz where s.name = sz.sector_name and s.active = 'X' and sz.zipcode = ? ";
	
	public static SectorVO getSectorInfo(Connection conn, AddressModel address)
																throws SQLException {
		SectorVO response = null;
		
		LOGGER.debug("getSectorInfo[QUERY] :" + address );
		if(address != null){
			PreparedStatement ps = conn.prepareStatement(GET_SECTOR_INFO);
			ps.setString(1, address.getZipCode());
			
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				response = new SectorVO();
				response.setName(rs.getString("name"));
				response.setDescription(rs.getString("description"));
			} else {
				LOGGER.debug("DlvManagerDAO.getSectorInfo(NO_SECTOR): " + address.getZipCode());
			}
		
			rs.close();
			ps.close();
		}
		return response;
	}

	private static final String FETCH_UNCONFIRMED_RESERVATIONS_INUPS="SELECT R.ID, R.ORDER_ID, R.CUSTOMER_ID, R.STATUS_CODE, R.TIMESLOT_ID, R.ZONE_ID, " +
			"R.EXPIRATION_DATETIME, R.TYPE, R.ADDRESS_ID,T.BASE_DATE, Z.ZONE_CODE, R.UNASSIGNED_DATETIME, R.UNASSIGNED_ACTION, R.IN_UPS, R.ORDER_SIZE, " +
			"R.SERVICE_TIME, R.RESERVED_ORDER_SIZE, R.RESERVED_SERVICE_TIME, R.UPDATE_STATUS, R.METRICS_SOURCE, R.NUM_CARTONS, R.NUM_FREEZERS, R.NUM_CASES, R.CLASS " +
			"from dlv.reservation r, dlv.timeslot t, dlv.zone z where T.BASE_DATE > sysdate and t.zone_id = z.id and T.ID = R.TIMESLOT_ID and " +
			"R.STATUS_CODE = '10' and R.UNASSIGNED_ACTION is null and R.IN_UPS = 'X' and not exists (select 1 from MIS.TIMESLOT_EVENT_HDR h where  " +
			"H.EVENTTYPE = 'CONFIRM_TIMESLOT' and H.ORDER_ID = R.ORDER_ID and H.CUSTOMER_ID = R.CUSTOMER_ID )";
	
	private static final String FIX_UNCONFIRMED_RESERVATIONS_INUPS = "update dlv.reservation rdx set rdx.UNASSIGNED_ACTION = 'CONFIRM_TIMESLOT', " +
			"rdx.UNASSIGNED_DATETIME = sysdate where RDX.ID in (select r.ID from dlv.reservation r, dlv.timeslot t  where T.BASE_DATE > sysdate and T.ID = " +
			"R.TIMESLOT_ID  and R.STATUS_CODE = '10' and R.UNASSIGNED_ACTION is null and R.IN_UPS = 'X' and not exists (select 1 from MIS.TIMESLOT_EVENT_HDR h " +
			"where  H.EVENTTYPE = 'CONFIRM_TIMESLOT' and H.ORDER_ID = R.ORDER_ID and H.CUSTOMER_ID = R.CUSTOMER_ID ))";
	
	public static List<DlvReservationModel> getUnconfirmedReservations(Connection conn)  throws SQLException {
		PreparedStatement ps =
			conn.prepareStatement(FETCH_UNCONFIRMED_RESERVATIONS_INUPS);
		
		ResultSet rs = ps.executeQuery();
		List<DlvReservationModel>  reservations = new ArrayList<DlvReservationModel>();
		while (rs.next()) {
			DlvReservationModel rsv = loadReservationFromResultSet(rs);
			reservations.add(rsv);
		}

		if(reservations.size()>0){
			ps = conn.prepareStatement(FIX_UNCONFIRMED_RESERVATIONS_INUPS);
			ps.execute();
		}
		
		rs.close();
		ps.close();
       
		return reservations;
	}
	
	private static final String FETCH_CONFIRMED_RESERVATIONS_CANCELLED_ORDERS="select  R.ID, R.ORDER_ID, R.CUSTOMER_ID, R.STATUS_CODE, R.TIMESLOT_ID, R.ZONE_ID, " +
			"R.EXPIRATION_DATETIME, R.TYPE, R.ADDRESS_ID,T.BASE_DATE, Z.ZONE_CODE,R.UNASSIGNED_DATETIME, R.UNASSIGNED_ACTION, R.IN_UPS, R.ORDER_SIZE, R.SERVICE_TIME " +
			", R.RESERVED_ORDER_SIZE, R.RESERVED_SERVICE_TIME, R.UPDATE_STATUS, R.METRICS_SOURCE, R.NUM_CARTONS , R.NUM_FREEZERS , R.NUM_CASES, R.CLASS from cust.sale s, " +
			"cust.salesaction sa, cust.deliveryinfo di, dlv.reservation r, dlv.timeslot t, dlv.zone z where s.id=sa.sale_id and s.customer_id=sa.customer_id and " +
			"S.CROMOD_DATE=sa.action_date  and sa.action_type in ('CRO','MOD') and sa.requested_date>=trunc(sysdate) and DI.RESERVATION_ID=r.id and " +
			"sa.id=di.salesaction_id and R.ORDER_ID=s.id  and s.status='CAN' and R.IN_UPS='X' and R.STATUS_CODE='10' and T.ZONE_ID = Z.ID and " +
			"R.TIMESLOT_ID = T.ID and  R.UNASSIGNED_ACTION is null";
	
	private static final String FIX_CONFIRMED_RESERVATIONS_CANCELLED_ORDERS = "update dlv.reservation r set R.UNASSIGNED_DATETIME = sysdate , R.UNASSIGNED_ACTION " +
			"= 'CANCEL_TIMESLOT', R.STATUS_CODE = '15' where R.ID IN (select r.id   from cust.sale s, cust.salesaction sa, cust.deliveryinfo di, dlv.reservation r " +
			"where s.id=sa.sale_id and s.customer_id=sa.customer_id and S.CROMOD_DATE=sa.action_date  and sa.action_type in ('CRO','MOD') and " +
			"sa.requested_date>=trunc(sysdate) and DI.RESERVATION_ID=r.id and sa.id=di.salesaction_id and R.ORDER_ID=s.id  and s.status='CAN' " +
			"and R.IN_UPS='X' and R.STATUS_CODE='10' and  R.UNASSIGNED_ACTION is null)";
	
	
	public static List<DlvReservationModel> getConfirmedRsvForCancelledOrders(
			Connection conn) throws SQLException {
		PreparedStatement ps =
				conn.prepareStatement(FETCH_CONFIRMED_RESERVATIONS_CANCELLED_ORDERS);
			
			ResultSet rs = ps.executeQuery();
			List<DlvReservationModel>  reservations = new ArrayList<DlvReservationModel>();
			while (rs.next()) {
				DlvReservationModel rsv = loadReservationFromResultSet(rs);
				reservations.add(rsv);
			}

			if(reservations.size()>0){
				ps = conn.prepareStatement(FIX_CONFIRMED_RESERVATIONS_CANCELLED_ORDERS);
				ps.execute();
			}
			
			rs.close();
			ps.close();
	       
			return reservations;
		}
	
	private static final String FETCH_CANCELLED_RESERVATIONS_IN_UPS="select R.ID, R.ORDER_ID, R.CUSTOMER_ID, R.STATUS_CODE, R.TIMESLOT_ID, R.ZONE_ID, R.EXPIRATION_DATETIME, " +
			"R.TYPE, R.ADDRESS_ID,T.BASE_DATE, Z.ZONE_CODE ,R.UNASSIGNED_DATETIME, R.UNASSIGNED_ACTION, R.IN_UPS, R.ORDER_SIZE, R.SERVICE_TIME , R.RESERVED_ORDER_SIZE, " +
			"R.RESERVED_SERVICE_TIME, R.UPDATE_STATUS, R.METRICS_SOURCE , R.NUM_CARTONS , R.NUM_FREEZERS , R.NUM_CASES, R.CLASS from dlv.reservation r, dlv.timeslot t, dlv.zone z " +
			" where T.BASE_DATE > sysdate and T.ID = R.TIMESLOT_ID and t.zone_id = z.id and R.STATUS_CODE = '15' and R.UNASSIGNED_ACTION is null and R.IN_UPS = 'X' " +
			"and not exists (select 1 from MIS.TIMESLOT_EVENT_HDR h where H.EVENTTYPE = 'CANCEL_TIMESLOT' and H.ORDER_ID = R.ORDER_ID and H.CUSTOMER_ID = R.CUSTOMER_ID " +
			"and r.id=H.RESERVATION_ID )";
	
	private static final String FIX_CANCELLED_RESERVATIONS_IN_UPS = "update dlv.reservation r set R.UNASSIGNED_DATETIME = sysdate , R.UNASSIGNED_ACTION = " +
			"'CANCEL_TIMESLOT' where R.ID IN (select r.ID from dlv.reservation r, dlv.timeslot t  where T.BASE_DATE > sysdate and T.ID = R.TIMESLOT_ID " +
			"and R.STATUS_CODE = '15' and R.UNASSIGNED_ACTION is null and R.IN_UPS = 'X' and not exists (select 1 from MIS.TIMESLOT_EVENT_HDR h where " +
			" H.EVENTTYPE = 'CANCEL_TIMESLOT' and H.ORDER_ID = R.ORDER_ID and H.CUSTOMER_ID = R.CUSTOMER_ID and r.id=H.RESERVATION_ID ))";
	
	
	public static List<DlvReservationModel> getCancelledRsvInUPS(
			Connection conn) throws SQLException {
		PreparedStatement ps =
				conn.prepareStatement(FETCH_CANCELLED_RESERVATIONS_IN_UPS);
			
			ResultSet rs = ps.executeQuery();
			List<DlvReservationModel>  reservations = new ArrayList<DlvReservationModel>();
			while (rs.next()) {
				DlvReservationModel rsv = loadReservationFromResultSet(rs);
				reservations.add(rsv);
			}

			if(reservations.size()>0){
				ps = conn.prepareStatement(FIX_CANCELLED_RESERVATIONS_IN_UPS);
				ps.execute();
			}
			
			rs.close();
			ps.close();
	       
			return reservations;
		}

	
	private static final String FETCH_ORDERS_WITH_CANCELLED_RESERVATIONS="select R.ID, R.ORDER_ID, R.CUSTOMER_ID, R.STATUS_CODE, R.TIMESLOT_ID, R.ZONE_ID, R.EXPIRATION_DATETIME, " +
			"R.TYPE, R.ADDRESS_ID,T.BASE_DATE, Z.ZONE_CODE,R.UNASSIGNED_DATETIME, R.UNASSIGNED_ACTION, R.IN_UPS, R.ORDER_SIZE, R.SERVICE_TIME, R.RESERVED_ORDER_SIZE, " +
			"R.RESERVED_SERVICE_TIME, R.UPDATE_STATUS, R.METRICS_SOURCE, R.NUM_CARTONS , R.NUM_FREEZERS , R.NUM_CASES, R.CLASS from cust.sale s, cust.salesaction sa, " +
			"cust.deliveryinfo di, dlv.reservation r, dlv.timeslot t, dlv.zone z where s.id=sa.sale_id and s.customer_id=sa.customer_id and S.CROMOD_DATE=sa.action_date  " +
			"and sa.action_type in ('CRO','MOD') and t.zone_id = z.id and R.TIMESLOT_ID = T.ID and sa.requested_date>=trunc(sysdate) and DI.RESERVATION_ID=r.id and " +
			"sa.id=di.salesaction_id and s.status!='CAN' and R.STATUS_CODE!='10' ";
	
	private static final String FIX_ORDERS_WITH_CANCELLED_RESERVATIONS = "update dlv.reservation rdx  set rdx.UNASSIGNED_ACTION = 'RESERVE_TIMESLOT', " +
			"rdx.UNASSIGNED_DATETIME = sysdate,rdx.STATUS_CODE = '10' where rdx.id IN  (select r.id  from cust.sale s, cust.salesaction sa, " +
			"cust.deliveryinfo di, dlv.reservation r where s.id=sa.sale_id and s.customer_id=sa.customer_id and S.CROMOD_DATE=sa.action_date  " +
			"and sa.action_type in ('CRO','MOD') and sa.requested_date>=trunc(sysdate) and DI.RESERVATION_ID=r.id and sa.id=di.salesaction_id " +
			"and s.status!='CAN' and R.STATUS_CODE!='10')";
	
	
	public static List<DlvReservationModel> getOrdersWithCancelledRsv(
			Connection conn) throws SQLException {
		PreparedStatement ps =
				conn.prepareStatement(FETCH_ORDERS_WITH_CANCELLED_RESERVATIONS);
			
			ResultSet rs = ps.executeQuery();
			List<DlvReservationModel>  reservations = new ArrayList<DlvReservationModel>();
			while (rs.next()) {
				DlvReservationModel rsv = loadReservationFromResultSet(rs);
				reservations.add(rsv);
			}

			if(reservations.size()>0){
				ps = conn.prepareStatement(FIX_ORDERS_WITH_CANCELLED_RESERVATIONS);
				ps.execute();
			}
			
			rs.close();
			ps.close();
	       
			return reservations;
		}

	
	
}
