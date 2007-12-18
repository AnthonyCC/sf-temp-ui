/*
 * DlvManagerSessionBean.java
 *
 * Created on August 27, 2001, 6:51 PM
 */

package com.freshdirect.delivery.ejb;

/**
 * 
 * @author knadeem
 * @version
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.MunicipalityInfo;
import com.freshdirect.delivery.DlvAddressGeocodeResponse;
import com.freshdirect.delivery.DlvAddressVerificationResponse;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.DlvServiceSelectionResult;
import com.freshdirect.delivery.DlvZipInfoModel;
import com.freshdirect.delivery.DlvZoneCapacityInfo;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.delivery.EnumAddressVerificationResult;
import com.freshdirect.delivery.EnumDeliveryStatus;
import com.freshdirect.delivery.EnumReservationStatus;
import com.freshdirect.delivery.EnumReservationType;
import com.freshdirect.delivery.EnumRestrictedAddressReason;
import com.freshdirect.delivery.EnumZipCheckResponses;
import com.freshdirect.delivery.ExceptionAddress;
import com.freshdirect.delivery.InvalidAddressException;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.delivery.model.DlvRegionModel;
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.model.DlvZoneDescriptor;
import com.freshdirect.delivery.model.DlvZoneModel;
import com.freshdirect.delivery.restriction.ejb.DlvRestrictionDAO;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.StateCounty;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

public class DlvManagerSessionBean extends SessionBeanSupport {

	private static final Category LOGGER = LoggerFactory.getInstance(DlvManagerSessionBean.class);

	/** Creates new DlvManagerSessionBean */
	public DlvManagerSessionBean() {
		super();
	}

	/**
	 * Template method that returns the cache key to use for caching resources.
	 * 
	 * @return the bean's home interface name
	 */
	protected String getResourceCacheKey() {
		return "com.freshdirect.delivery.ejb.DlvManagerHome";
	}

	public List getTimeslotForDateRangeAndZone(java.util.Date begDate, java.util.Date endDate, AddressModel address)
		throws InvalidAddressException {
		Connection conn = null;
		try {
			conn = getConnection();
			return DlvManagerDAO.getTimeslotForDateRangeAndZone(conn, address, begDate, endDate);

		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}
	}

	public List getAllTimeslotsForDateRange(java.util.Date startDate, java.util.Date endDate, AddressModel address)
		throws InvalidAddressException {
		Connection conn = null;
		try {
			conn = getConnection();
			return DlvManagerDAO.getAllTimeslotsForDateRange(conn, address, startDate, endDate);

		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}
	}

	
	public List getTimeslotCapacityInfo(java.util.Date date) {
		Connection conn = null;
		try {
			conn = getConnection();
			return DlvManagerDAO.getTimeslotCapacityInfo(conn, date);

		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn in cleanup", e);
			}
		}
	}
	
	
	public List getTimeslotsForDepot(java.util.Date startDate, java.util.Date endDate, String regionId, String zoneCode)
		throws DlvResourceException {
		Connection conn = null;

		try {
			conn = getConnection();
			return DlvManagerDAO.getTimeslotsForDepot(conn, regionId, zoneCode, startDate, endDate);

		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while closing conn during cleanup", e);
			}
		}
	}

	private static String depotZoneInfoQuery = "SELECT z.ID ZONE_ID, rd.DELIVERY_CHARGES, rd.START_DATE, zd.UNATTENDED "
		+ "FROM DLV.REGION_DATA rd, DLV.ZONE z, DLV.ZONE_DESC zd WHERE z.ZONE_CODE = zd.ZONE_CODE and rd.REGION_ID = ? "
		+ "AND rd.START_DATE = (SELECT MAX(START_DATE) FROM DLV.REGION_DATA WHERE START_DATE <= ? AND REGION_ID = ?) "
		+ "AND z.REGION_DATA_ID = rd.ID and z.ZONE_CODE = ?";

	public DlvZoneInfoModel getZoneInfoForDepot(String regionId, String zoneCode, java.util.Date date) throws DlvResourceException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(depotZoneInfoQuery);
			ps.setString(1, regionId);
			ps.setDate(2, new java.sql.Date(date.getTime()));
			ps.setString(3, regionId);
			ps.setString(4, zoneCode);
			rs = ps.executeQuery();

			if (!rs.next()) {
				this.getSessionContext().setRollbackOnly();
				throw new DlvResourceException("Cannot find zone id for regionId/zoneCode");
			}
			
			DlvZoneInfoModel response = 
				new DlvZoneInfoModel(zoneCode, rs.getString("ZONE_ID"), null, EnumZipCheckResponses.DELIVER,"X".equals(rs.getString("UNATTENDED")));

			return response;

		} catch (SQLException se) {
			throw new EJBException(se.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}
	}

	public DlvReservationModel reserveTimeslot(String timeslotId,
		String customerId,
		long holdTime,
		EnumReservationType type,
		String addressId, boolean chefsTable) throws ReservationException {

		// Get the Timeslot object
		DlvTimeslotModel timeslotModel;
		try {
			timeslotModel = this.getTimeslotById(timeslotId);
		} catch (FinderException e) {
			this.getSessionContext().setRollbackOnly();
			throw new ReservationException("Cannot find timeslot " + timeslotId);
		}
		
		//
		// ensure we're not past cutoff
		//
		Date timeslotCutoff = timeslotModel.getCutoffTimeAsDate();
		Date now = new Date();
		if (timeslotCutoff.before(now)) {
			this.getSessionContext().setRollbackOnly();
			throw new ReservationException("This timeslot has expired and cannot be reserved");
		}
		
		//
		// put all reservations in base, as long as there's capacity there
		//
		if (chefsTable && timeslotModel.getBaseAvailable() > 0) {
			chefsTable = false;
		}
	
		//
		// put all reservations in base, if we're past CT auto-release
		//
		if (chefsTable && timeslotModel.isCTCapacityReleased(now)) {
			chefsTable = false;
		}

		//
		// ensure total capacity
		//
		if (!chefsTable && (timeslotModel.getCapacity()-timeslotModel.calculateCurrentAllocation(new Date())) <= 0) {
			this.getSessionContext().setRollbackOnly();
			throw new ReservationException("No more capacity available for this timeslot");
		}

		//
		// ensure chef's table capacity
		//
		if (chefsTable && timeslotModel.getChefsTableAvailable() <= 0) {
			this.getSessionContext().setRollbackOnly();
			throw new ReservationException("No more capacity available for this timeslot");
		}

		//
		// calculate expiration
		//
		Date expiration = new Date(System.currentTimeMillis() + holdTime);
		if (expiration.after(timeslotCutoff)) {
			// expiration must not be after cutoff
			expiration = timeslotCutoff;
		}

		Connection con = null;
		PreparedStatement ps = null;
		try {
			// Create a new Reservation bean and return it.
			con = getConnection();

			ps = con
				.prepareStatement("INSERT INTO dlv.reservation(ID, TIMESLOT_ID, ZONE_ID, ORDER_ID, CUSTOMER_ID, STATUS_CODE, EXPIRATION_DATETIME, TYPE, ADDRESS_ID, CHEFSTABLE) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			String newId = this.getNextId(con, "DLV");
			ps.setString(1, newId);
			ps.setString(2, timeslotId);
			ps.setString(3, timeslotModel.getZoneId());
			ps.setString(4, "x" + newId);
			ps.setString(5, customerId);
			ps.setInt(6, EnumReservationStatus.RESERVED.getCode());
			ps.setTimestamp(7, new Timestamp(expiration.getTime()));
			ps.setString(8, type.getName());
			ps.setString(9, addressId);
			ps.setString(10, chefsTable ? "X" : " ");
			
			ps.executeUpdate();
			DlvReservationModel rsv = new DlvReservationModel(
				new PrimaryKey(newId),
				"x" + newId,
				customerId,
				EnumReservationStatus.RESERVED.getCode(),
				expiration,
				timeslotId,
				timeslotModel.getZoneId(),
				type,
				addressId);

			return rsv;
		} catch (SQLException se) {
			this.getSessionContext().setRollbackOnly();
			se.printStackTrace();
			throw new ReservationException(se.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException sqle2) {
				LOGGER.warn("Exception while cleaning up: ", sqle2);
			}
		}
	}

	private DlvReservationModel getReservation(Connection con, String rsvId) throws SQLException {
		PreparedStatement ps = con
			.prepareStatement("SELECT ORDER_ID, CUSTOMER_ID, STATUS_CODE, TIMESLOT_ID, ZONE_ID, EXPIRATION_DATETIME, TYPE, ADDRESS_ID FROM DLV.RESERVATION WHERE ID = ?");
		ps.setString(1, rsvId);

		ResultSet rs = ps.executeQuery();
		if (!rs.next()) {
			rs.close();
			ps.close();
			return null;
		}
		DlvReservationModel rsv = new DlvReservationModel(new PrimaryKey(rsvId), rs.getString("ORDER_ID"), rs
			.getString("CUSTOMER_ID"), rs.getInt("STATUS_CODE"), rs.getTimestamp("EXPIRATION_DATETIME"), rs
			.getString("TIMESLOT_ID"), rs.getString("ZONE_ID"), EnumReservationType.getEnum(rs.getString("TYPE")), rs
			.getString("ADDRESS_ID"));

		rs.close();
		ps.close();

		return rsv;
	}

	public void commitReservation(String rsvId, String customerId, String orderId) throws ReservationException {
		Connection con = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;

		DlvReservationModel rsv = null;
		LOGGER.debug("Going to commit the reservation for id: " + rsvId);
		try {
			con = getConnection();
			ps1 = con
				.prepareStatement("UPDATE DLV.RESERVATION SET ORDER_ID = ?, STATUS_CODE = ? WHERE ID = ? and STATUS_CODE = ? ");

			rsv = this.getReservation(con, rsvId);

			if (rsv.getCustomerId().equals(customerId)) {
				rsv.setOrderId(orderId);
				rsv.setStatusCode(EnumReservationStatus.COMMITTED.getCode());
				ps1.setString(1, rsv.getOrderId());
				ps1.setInt(2, rsv.getStatusCode());
				ps1.setString(3, rsvId);
				ps1.setInt(4, EnumReservationStatus.RESERVED.getCode());

				int rowsUpdated = ps1.executeUpdate();
				if (rowsUpdated != 1) {
					this.getSessionContext().setRollbackOnly();
					throw new ReservationException("The reservation is already committed: " + rsvId);
				}
			} else {
				this.getSessionContext().setRollbackOnly();
				throw new ReservationException("This slot is reserved for another customer");
			}
		} catch (SQLException se) {
			LOGGER.debug(se);
			this.getSessionContext().setRollbackOnly();
			throw new ReservationException(se.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps1 != null) {
					ps1.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException sqle2) {
				LOGGER.warn("Exception while trying to cleanup", sqle2);
			}
		}
	}

	public DlvReservationModel getReservation(String reservationId) throws FinderException {
		Connection con = null;
		try {
			con = getConnection();

			DlvReservationModel rsv = this.getReservation(con, reservationId);
			if (rsv == null) {
				throw new ObjectNotFoundException("Reservation " + reservationId + " not found");
			}

			return rsv;

		} catch (SQLException se) {
			throw new FinderException(se.getMessage());

		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException sqle2) {
				// eat it
			}
		}
	}

	public List getReservationsForCustomer(String customerId) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return DlvManagerDAO.getReservationForCustomer(conn, customerId);
		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while trying to cleanup", e);
			}
		}
	}

	private List getAllReservationsByCustomerAndTimeslot(String customerId, String timeslotId) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return DlvManagerDAO.getAllReservationsByCustomerAndTimeslot(conn, customerId, timeslotId);
		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while trying to cleanup", e);
			}
		}
	}
	
	
	public void extendReservation(String rsvId, Date newExpTime) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			DlvManagerDAO.extendReservation(conn, rsvId, newExpTime);
		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while trying to cleanup", e);
			}
		}
	}

	private void restoreReservation(String rsvId) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			DlvManagerDAO.restoreReservation(conn, rsvId);
		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while trying to cleanup", e);
			}
		}
	}

	private Date getDateByNextDayOfWeek(int dayOfWeek) {
		Calendar cal = Calendar.getInstance();
		if (cal.get(Calendar.DAY_OF_WEEK) == dayOfWeek) {
			cal.add(Calendar.DATE, 1);
		}
		while (cal.get(Calendar.DAY_OF_WEEK) != dayOfWeek) {
			cal.add(Calendar.DATE, 1);
		}
		return DateUtil.truncate(cal).getTime();
	}
	
	public boolean makeRecurringReservation(String customerId, int dayOfWeek, Date startTime, Date endTime, AddressModel address) {
		try {
			Date startDate = getDateByNextDayOfWeek(dayOfWeek);
			Date endDate = DateUtil.addDays(startDate, 1);

			List lst = this.getTimeslotForDateRangeAndZone(startDate, endDate, address);
			DlvTimeslotModel foundTimeslot = null;
			for (Iterator i = lst.iterator(); i.hasNext();) {
				DlvTimeslotModel t = (DlvTimeslotModel) i.next();
				if (t.isMatching(startDate, startTime, endTime)) {
					foundTimeslot = t;
					break;
				}
			}

			if (foundTimeslot == null) {
				LOGGER.info("Failed to make recurring reservation for customer " + customerId + " - no timeslot found");
				return false;
			}
			
			List reservations = getAllReservationsByCustomerAndTimeslot(customerId, foundTimeslot.getPK().getId());
			for (Iterator i = reservations.iterator(); i.hasNext(); ) {
				DlvReservationModel rsv = (DlvReservationModel) i.next();
				if (!EnumReservationType.STANDARD_RESERVATION.equals(rsv.getReservationType())) {
					LOGGER.info("Failed to make recurring reservation for customer " + customerId + " - already reserved");
					return false;
				}
			}

			long duration = foundTimeslot.getCutoffTimeAsDate().getTime()
				- System.currentTimeMillis()
				- (FDStoreProperties.getPreReserveHours() * DateUtil.HOUR);
			if (duration < 0) {
				duration = Math.min(foundTimeslot.getCutoffTimeAsDate().getTime() - System.currentTimeMillis(), DateUtil.HOUR);
			}
			//recurring timeslot should not go to chefs table capacity
			this.reserveTimeslot(
				foundTimeslot.getPK().getId(),
				customerId,
				duration,
				EnumReservationType.RECURRING_RESERVATION,
				address.getPK().getId(), false);
			
			LOGGER.info("Made recurring reservation for " + customerId);

			return true;
			
		} catch (InvalidAddressException e) {
			LOGGER.warn("Could not Reserve a Weekly recurring timeslot for customer id: "+customerId+" because of Invalid address", e);
			return false;
		} catch (ReservationException e) {
			LOGGER.warn("Could not Reserve a Weekly recurring timeslot for customer id: "+customerId+" Reservation Exception", e);
			return false;
		}
	}

	public void removeReservation(String reservationId) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			DlvManagerDAO.removeReservation(conn, reservationId);
		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while trying to cleanup", e);
			}
		}
	}

	private final static String FIND_ZONES_BY_ZONEID_QUERY = "select id, name, region_data_id, zone_code, plan_id, CT_ACTIVE, CT_RELEASE_TIME, unattended from dlv.zone natural join dlv.zone_desc where id = ?";
	public DlvZoneModel findZoneById(String zoneId) throws FinderException {
		DlvZoneModel zoneModel = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = getConnection();
			//XXX: (template_id <-> plan_id) wtf?
			ps = con.prepareStatement(FIND_ZONES_BY_ZONEID_QUERY);
			ps.setString(1, zoneId);
			rs = ps.executeQuery();
			if (rs.next()) {
				zoneModel = new DlvZoneModel(new PrimaryKey(rs.getString("id")),
					rs.getString("name"),
					rs.getString("plan_id"),
					"X".equals(rs.getString("CT_ACTIVE")),
					rs.getInt("CT_RELEASE_TIME"),
					new DlvZoneDescriptor(rs.getString("zone_code"),"X".equals(rs.getString("unattended"))));

			} else {
				throw new FinderException("Cannot find zone for zoneId: " + zoneId);
			}

		} catch (SQLException se) {
			throw new EJBException("In findZoneById following exception occured: " + se.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while cleaning up", se);
			}
		}
		return zoneModel;

	}

	public DlvTimeslotModel getTimeslotById(String timeslotId) throws FinderException {

		Connection conn = null;
		try {
			conn = getConnection();
			return DlvManagerDAO.getTimeslotById(conn, timeslotId);
		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while trying to cleanup", e);
			}
		}

	}

	private static final String FIND_ZONES_BY_REGIONID_QUERY = "select id, name, region_data_id, zone_code, plan_id, CT_ACTIVE, CT_RELEASE_TIME, unattended from dlv.zone natural join dlv.zone_desc where region_data_id = ?";
	public List getAllZonesByRegion(String regionId) {
		ArrayList ret = new ArrayList();
		DlvZoneModel zoneModel = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = getConnection();
			//XXX: (tempalte_id <-> plan_id) wtf?
			ps = con
				.prepareStatement(FIND_ZONES_BY_ZONEID_QUERY);
			ps.setString(1, regionId);
			rs = ps.executeQuery();
			while (rs.next()) {
				zoneModel = new DlvZoneModel(new PrimaryKey(rs.getString("id")),
					rs.getString("name"),
					rs.getString("plan_id"),
					"X".equals(rs.getString("CT_ACTIVE")),
					rs.getInt("CT_RELEASE_TIME"),
					new DlvZoneDescriptor(rs.getString("zone_code"),"X".equals(rs.getString("unattended"))));
				ret.add(zoneModel);
			}
			rs.close();
			ps.close();

		} catch (SQLException se) {
			throw new EJBException("In findZoneById following exception occured: " + se.getMessage());
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while trying to cleanup", se);
			}
		}

		return ret;
	}

	public DlvAddressVerificationResponse scrubAddress(AddressModel address) {
		return this.scrubAddress(address, true);
	}

	public DlvAddressVerificationResponse scrubAddress(AddressModel address, boolean useApartment) {
		GeographyDAO dao = new GeographyDAO();
		Connection conn = null;
		try {
			conn = this.getConnection();
			EnumAddressVerificationResult result = dao.verify(address, useApartment, conn);
			LOGGER.debug("Verify Address Result: " + result.getCode());
			return new DlvAddressVerificationResponse(result, address);
		} catch (SQLException se) {
			throw new EJBException(se);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while trying to close connection", e);
			}
		}
	}

	public void addApartment(AddressModel address) throws InvalidAddressException {

		DlvAddressVerificationResponse response = scrubAddress(address, true);
		EnumAddressVerificationResult result = response.getResult();

		if (EnumAddressVerificationResult.APT_WRONG.equals(result)) {
			Connection conn = null;
			try {
				GeographyDAO dao = new GeographyDAO();
				conn = this.getConnection();
				dao.addApartment(conn, address);
			} catch (SQLException e) {
				throw new EJBException(e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					LOGGER.warn("SQLException while trying to close connection", e);
				}
			}
		}
	}

	public ArrayList findSuggestionsForAmbiguousAddress(AddressModel address) throws InvalidAddressException {
		GeographyDAO dao = new GeographyDAO();
		Connection conn = null;
		ArrayList list = new ArrayList();
		try {
			conn = this.getConnection();
			list.addAll(dao.findSuggestionsForAmbiguousAddress(address, conn));
			return list;
		} catch (SQLException se) {
			throw new EJBException(se);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("SQLException while trying to close the connection", e);
			}
		}
	}

	public DlvAddressGeocodeResponse geocodeAddress(AddressModel address) throws InvalidAddressException {
		Connection conn = null;
		String result = "";
		try {

			conn = getConnection();
			GeographyDAO dao = new GeographyDAO();
			LOGGER.debug(address+"\n--------- DLV Manage Session Bean Before geocode---------------------\n"+address);
			result = dao.geocode(address, conn);
			LOGGER.debug(address+"\n--------- DLV Manage Session Bean After geocode---------------------\n"+address);
			return new DlvAddressGeocodeResponse(address, result);

		} catch (SQLException sqle) {
			throw new EJBException("Difficulty geocoding an address : " + sqle.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqle2) {
				}
			}
		}
	}

	public DlvZoneInfoModel getZoneInfo(AddressModel address, java.util.Date date) throws InvalidAddressException {
		return this.getZoneInfo(address, date, true);
	}

	private DlvZoneInfoModel getZoneInfo(AddressModel address, java.util.Date date, boolean useApartment)
		throws InvalidAddressException {

		Connection conn = null;
		try {
			conn = getConnection();
			return DlvManagerDAO.getZoneInfo(conn, address, date, useApartment);

		} catch (SQLException sqle) {
			throw new EJBException("Difficulty locating an address within a zone : " + sqle.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("DlvManagerSB getZoneInfo: Exception while cleaning: ", se);
			}
		}
	}
	
	public List getCutoffInfo(String zoneCode, Date day) {
		Connection conn = null;
		try{
			conn = this.getConnection();
			return DlvManagerDAO.getCutoffInfo(conn, zoneCode, DateUtil.truncate(day), DateUtil.truncate(DateUtil.addDays(day, 1)));
		}catch (SQLException e) {
			throw new EJBException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB getCutoffInfo: Exception while cleaning: ", e);
			}
		}
	}
	
	public DlvZoneCapacityInfo getZoneCapacity(String zoneCode, Date day) {
		Connection conn = null;
		try{
			conn = this.getConnection();
			return DlvManagerDAO.getZoneCapacity(conn, zoneCode, DateUtil.truncate(day), DateUtil.truncate(DateUtil.addDays(day, 1)));
		}catch (SQLException e) {
			throw new EJBException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB getCutoffInfo: Exception while cleaning: ", e);
			}
		}
	}

	public List getDeliverableZipCodes(EnumServiceType serviceType) {
		Connection conn = null;
		try {
			conn = getConnection();
			return DlvManagerDAO.getDeliverableZipCodes(conn, serviceType);

		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("SQLException while trying to close the connection: ", e);
				}
			}
		}
	}

	public DlvServiceSelectionResult checkServicesForZipCode (String zipcode) {
		Connection conn = null;
		try {
			conn = getConnection();
			
			DlvServiceSelectionResult result = new DlvServiceSelectionResult();
			EnumDeliveryStatus status = isPickupAvailable(conn, zipcode);
			result.addServiceStatus(EnumServiceType.PICKUP, status);
			status = this.getServiceStatus(DlvManagerDAO.checkZipcode(conn, zipcode, EnumServiceType.HOME));
			result.addServiceStatus(EnumServiceType.HOME, status);
			status = this.getServiceStatus(DlvManagerDAO.checkZipcode(conn, zipcode, EnumServiceType.CORPORATE));
			result.addServiceStatus(EnumServiceType.CORPORATE, status);
			
			return result;
		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ignore) {
					LOGGER.warn("SQLException while closing Connection", ignore);
				}
			}
		}
	}

	public DlvServiceSelectionResult checkServicesForAddress(AddressModel address) throws InvalidAddressException {
		Connection conn = null;
		try {
			conn = getConnection();
			
			DlvServiceSelectionResult result = new DlvServiceSelectionResult();
			EnumDeliveryStatus status = isPickupAvailable(conn, address.getZipCode());
			result.addServiceStatus(EnumServiceType.PICKUP, status);
			status = this.getServiceStatus(DlvManagerDAO.checkAddress(conn, address, EnumServiceType.HOME));
			result.addServiceStatus(EnumServiceType.HOME, status);
			status = this.getServiceStatus(DlvManagerDAO.checkAddress(conn, address, EnumServiceType.CORPORATE));
			result.addServiceStatus(EnumServiceType.CORPORATE, status);
			
			result.setRestrictionReason(DlvManagerDAO.isAddressRestricted(conn, address));
			
			LOGGER.debug(result);

			return result;
		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ignore) {
					LOGGER.warn("SQLException while closing Connection", ignore);
				}
			}
		}
	}

	private final static String triStateZipCodeQuery = "select count(*) from dlv.zipplusfour where zipcode = ?";

	private EnumDeliveryStatus isPickupAvailable(Connection conn, String zipCode) throws SQLException {
		// pickup is available for any zipcode listed in the DLV.ZIPPLUSFOUR table
		PreparedStatement ps = conn.prepareStatement(triStateZipCodeQuery);
		ps.setString(1, zipCode);
		ResultSet rs = ps.executeQuery();
		
		EnumDeliveryStatus  status = null;
		if (rs.next()) {
			status = (0 < rs.getInt(1)) ? EnumDeliveryStatus.DELIVER : EnumDeliveryStatus.DONOT_DELIVER ;
		}
		rs.close();
		ps.close();
		
		return status;
	}

	public EnumDeliveryStatus getServiceStatus(List dlvInfos) {
		//
		// no geographic info for this zipcode, is pickup available?
		//
		if (dlvInfos.size() == 0) {
			return EnumDeliveryStatus.DONOT_DELIVER;
		}
		//
		// is delivery currently available?
		// find the largest coverage with the latest startDate earlier than
		// seven days from now
		//
		GregorianCalendar delWindCal = new GregorianCalendar();
		delWindCal.add(Calendar.DAY_OF_YEAR, 7);
		java.util.Date deliveryWindow = delWindCal.getTime();
		DlvZipInfoModel bestDelivery = null;
		
		for (Iterator i = dlvInfos.iterator(); i.hasNext();) {
		   DlvZipInfoModel dlvInfo = (DlvZipInfoModel) i.next();
		   if (dlvInfo.getStartDate().before(deliveryWindow)) {
			    if (bestDelivery == null) {
			     bestDelivery = dlvInfo;
			    } else {
			    	if (dlvInfo.getStartDate().after(bestDelivery.getStartDate()) && dlvInfo.getCoverage() > bestDelivery.getCoverage()) {
			    		bestDelivery = dlvInfo;
			    	} else if (DateUtil.isSameDay(bestDelivery.getStartDate(), dlvInfo.getStartDate())
			    		&& (dlvInfo.getCoverage() > bestDelivery.getCoverage())) {
			    		bestDelivery = dlvInfo;
			    	}
			    }
		   }
		}		
		
		if (bestDelivery != null) {
			if (bestDelivery.getCoverage() > 0.9) {
				return EnumDeliveryStatus.DELIVER;
			} 
			
			if (bestDelivery.getCoverage() >= 0.1) {
				return EnumDeliveryStatus.PARTIALLY_DELIVER;
			}
		}
		
		return EnumDeliveryStatus.DONOT_DELIVER;

	}

	public boolean checkForAlcoholDelivery(String scrubbedAddress, String zipcode) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return DlvManagerDAO.isAlcoholDeliverable(conn, scrubbedAddress, zipcode);
		} catch (SQLException ex) {
			throw new EJBException(ex);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException ex) {
				LOGGER.warn("Exception while trying to cleanup", ex);
			}
		}
	}

	public EnumRestrictedAddressReason checkAddressForRestrictions(AddressModel address) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return DlvManagerDAO.isAddressRestricted(conn, address);
		} catch (SQLException ex) {
			throw new EJBException(ex);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException ex) {
				LOGGER.warn("Exception while trying to cleanup", ex);
			}
		}
	}

	public List findApartmentRanges(AddressModel address) throws InvalidAddressException {
		Connection conn = null;
		List ranges = new ArrayList();
		GeographyDAO dao = new GeographyDAO();

		try {

			conn = getConnection();
			ranges = dao.findApartmentRanges(address, conn);

		} catch (SQLException se) {
			throw new EJBException(se.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ignore) {
				}
			}
		}

		return ranges;

	}

	public boolean releaseReservation(String rsvId) throws FinderException {
		DlvReservationModel rsv = getReservation(rsvId);
		boolean isRestored = false;
		if (!EnumReservationType.STANDARD_RESERVATION.equals(rsv.getReservationType())) {
			List reservations = getReservationsForCustomer(rsv.getCustomerId());
			if (reservations.isEmpty()) {
				restoreReservation(rsvId);
				isRestored = true;
			} else {
				DlvReservationModel foundRsv = null;
				for (Iterator i = reservations.iterator(); i.hasNext();) {
					DlvReservationModel r = (DlvReservationModel) i.next();
					if (!EnumReservationType.STANDARD_RESERVATION.equals(r.getReservationType())) {
						foundRsv = r;
						break;
					}
				}
				if (foundRsv != null) {
					cancelReservation(rsvId);
					isRestored = false;
				} else {
					restoreReservation(rsvId);
					isRestored = true;
				}
			}

		} else {
			cancelReservation(rsvId);
			isRestored = false;
		}
		return isRestored;
	}

	private void cancelReservation(String rsvId) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			LOGGER.debug("Giong to Release the Reservation for id: " + rsvId);
			con = getConnection();
			ps = con.prepareStatement("UPDATE DLV.RESERVATION SET STATUS_CODE = ? WHERE ID = ?");
			ps.setInt(1, EnumReservationStatus.CANCELED.getCode());
			ps.setString(2, rsvId);
			if (ps.executeUpdate() != 1) {
				throw new EJBException("Cannot release Reservation :" + rsvId);
			}
		} catch (SQLException se) {
			throw new EJBException(se.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException sqle2) {
				LOGGER.warn("Exception while trying to cleanup", sqle2);
			}
		}

	}

	public void saveFutureZoneNotification(String email, String zip) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement("INSERT INTO DLV.ZONENOTIFICATION (EMAIL, ZIPCODE, CREATE_DATE) VALUES (?, ?, ?)");
			ps.setString(1, email);
			ps.setString(2, zip);
			ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

			int rowsaffected = ps.executeUpdate();
			if (rowsaffected != 1) {
				throw new EJBException("Cannot insert record in the database");
			}
		} catch (SQLException se) {
			throw new EJBException(se.getMessage());
		} finally {
			try {
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("SQLException while trying to cleanup", se);
			}
		}
	}

	private static String zoneQuery = "select NAME, ZONE_CODE, UNATTENDED from dlv.zone natural join dlv.zone_desc where region_data_id = "
		+ "(select id from dlv.region_data where start_date = (SELECT MAX(START_DATE) "
		+ "FROM DLV.REGION_DATA WHERE START_DATE <= sysdate and region_id = ?) and region_id = ?)";

	public Collection getZonesForRegionId(String regionId) throws DlvResourceException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List zones = new ArrayList();

		try {
			conn = this.getConnection();
			ps = conn.prepareStatement(zoneQuery);
			ps.setString(1, regionId);
			ps.setString(2, regionId);

			rs = ps.executeQuery();

			while (rs.next()) {
				DlvZoneModel zone = new DlvZoneModel();
				zone.setName(rs.getString("NAME"));

				zone.setZoneDescriptor(new DlvZoneDescriptor(rs.getString("ZONE_CODE"),"X".equals(rs.getString("UNATTENDED"))));
				zones.add(zone);
			}

		} catch (SQLException se) {
			throw new EJBException(se);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("Exception while cleaning up", se);
			}
		}
		return zones;

	}

	public Collection getAllRegions() throws DlvResourceException {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List regions = new ArrayList();

		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("SELECT ID, NAME FROM DLV.REGION");

			rs = ps.executeQuery();

			while (rs.next()) {
				PrimaryKey pk = new PrimaryKey(rs.getString("ID"));
				String name = rs.getString("NAME");
				DlvRegionModel region = new DlvRegionModel(pk, name);

				regions.add(region);
			}

		} catch (SQLException se) {
			throw new EJBException(se);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (ps != null) {
					ps.close();
					ps = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException se) {
				LOGGER.warn("Exception while cleaning up", se);
			}
		}
		return regions;
	}

	public List getDlvRestrictions() throws DlvResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			return DlvRestrictionDAO.getDlvRestrictions(conn);

		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("DlvManagerSB getDlvRestriction: Exception while cleaning: " + se);
			}
		}
	}

	public List getSiteAnnouncements() throws DlvResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return DlvManagerDAO.getSiteAnnouncements(conn);
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB getSiteAnnouncements: Exception while cleaning: " + e);
			}
		}
	}
	
	public void addExceptionAddress(ExceptionAddress ex) {
		Connection conn = null;
		GeographyDAO dao = new GeographyDAO();

		try {
			conn = this.getConnection();
			dao.addExceptionAddress(conn, ex);
		} catch (SQLException e) {
			LOGGER.warn("DlvManagerSB addExceptionAddress: Exception while inserting record: " + e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB addExceptionAddress: Exception while cleaning: " + e);
			}
		}

	}
	
	public List searchGeocodeException(ExceptionAddress ex){
		Connection conn = null;
		GeographyDAO dao = new GeographyDAO();
		try{
			conn = this.getConnection();
			return dao.searchGeocodeException(conn, ex);
		} catch(SQLException e){
			throw new FDRuntimeException(e);
		} finally {
			try{
				if(conn !=null){
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB searchExceptionAddress: Exception while searching: "+e);
			}
		}
	}
	
	public void deleteGeocodeException(ExceptionAddress ex){
		Connection conn = null;
		GeographyDAO dao = new GeographyDAO();
		try{
			conn = this.getConnection();
			dao.deleteGeocodeException(conn, ex);
		} catch(SQLException e){
			throw new FDRuntimeException(e);
		} finally {
			try{
				if(conn !=null){
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB removeExceptionAddress: Exception while Removing: "+e);
			}
		}
	}
	
	public void addGeocodeException(ExceptionAddress ex, String userId){
		Connection conn = null;
		GeographyDAO dao = new GeographyDAO();
		try{
			conn = this.getConnection();
			dao.addGeocodeException(conn, ex, userId);
		} catch (SQLException e) {
			throw new FDRuntimeException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB addExceptionAddress: Exception while cleaning: " + e);
			}
		}
	}
	
	public String getCounty(String city, String state){
		Connection conn = null;
		GeographyDAO dao = new GeographyDAO();
		try{
			conn = this.getConnection();
			return dao.getCounty(conn, city, state);
		} catch (SQLException e) {
			throw new FDRuntimeException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB addExceptionAddress: Exception while cleaning: " + e);
			}
		}
	}
	
	public List getCountiesByState(String stateAbbrev){
		Connection conn = null;
		GeographyDAO dao = new GeographyDAO();
		try{
			conn = this.getConnection();
			return dao.getCountiesByState(conn, stateAbbrev);
		} catch (SQLException e) {
			throw new FDRuntimeException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB addExceptionAddress: Exception while cleaning: " + e);
			}
		}
	}
	
	
	private static String muniSql = "select * from dlv.municipality_info order by state, county desc, city desc";
	
	public List getMunicipalityInfos() {
		Connection conn = null;
		try{
			List muniList = new ArrayList();
			conn = this.getConnection();
			PreparedStatement ps = conn.prepareStatement(muniSql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String state = rs.getString("state");
				if ( state==null) continue;  
				boolean alcoholRestricted = "X".equalsIgnoreCase(rs.getString("alcohol_restricted"))? true : false;
				MunicipalityInfo mi = new MunicipalityInfo(
					state,
					rs.getString("county"),
					rs.getString("city"),
					rs.getString("gl_code"),
					rs.getDouble("tax_rate"),
					rs.getDouble("bottle_deposit"),
					alcoholRestricted );
				muniList.add(mi);
			}
			return (muniList.size()!=0 ? muniList : Collections.EMPTY_LIST);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FDRuntimeException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB addExceptionAddress: Exception while cleaning: " + e);
			}
		}
		
	}
	
	public List searchExceptionAddresses(ExceptionAddress ea){
		Connection conn = null;
		GeographyDAO dao = new GeographyDAO();
		try{
			conn = this.getConnection();
			return dao.searchExceptionAddresses(conn, ea);
		} catch (SQLException e) {
			throw new FDRuntimeException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB addExceptionAddress: Exception while cleaning: " + e);
			}
		}
	}
	
	public void deleteAddressException(String id) {
		Connection conn = null;
		GeographyDAO dao = new GeographyDAO();
		try{
			conn = this.getConnection();
			dao.deleteAddressException(conn, id);
		}catch (SQLException e) {
			throw new FDRuntimeException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB deleteAddressException: Exception while cleaning: " + e);
			}
		}
	}
	
	public StateCounty lookupStateCountyByZip(String zipcode){
		Connection conn = null;
		GeographyDAO dao = new GeographyDAO();
		try{
			conn = this.getConnection();
			return dao.lookupStateCountyByZip(conn, zipcode);
		} catch (SQLException e) {
			throw new FDRuntimeException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB addExceptionAddress: Exception while cleaning: " + e);
			}
		}
	}

	public List getCutoffTimesByDate(Date day) {
		Connection conn = null;
		try{
			conn = this.getConnection();
			return DlvManagerDAO.getCutoffTimesByDate(conn, DateUtil.truncate(day));
		}catch (SQLException e) {
			throw new EJBException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB getCutoffInfo: Exception while cleaning: ", e);
			}
		}
	}
	
	
}
