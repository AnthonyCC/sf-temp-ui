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
import java.rmi.RemoteException;
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
import java.util.Map;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.jms.ObjectMessage;

import org.apache.log4j.Category;

import com.freshdirect.analytics.EventType;
import com.freshdirect.analytics.RoutingModel;
import com.freshdirect.analytics.TimeslotEventDetailModel;
import com.freshdirect.analytics.TimeslotEventModel;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.MunicipalityInfo;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ejb.ErpLogActivityCommand;
import com.freshdirect.delivery.DlvAddressGeocodeResponse;
import com.freshdirect.delivery.DlvAddressVerificationResponse;
import com.freshdirect.delivery.DlvApartmentRange;
import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.DlvServiceSelectionResult;
import com.freshdirect.delivery.DlvTimeslotCapacityInfo;
import com.freshdirect.delivery.DlvZipInfoModel;
import com.freshdirect.delivery.DlvZoneCapacityInfo;
import com.freshdirect.delivery.DlvZoneCutoffInfo;
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
import com.freshdirect.delivery.ReservationUnavailableException;
import com.freshdirect.delivery.TimeslotCapacityContext;
import com.freshdirect.delivery.TimeslotCapacityWrapper;
import com.freshdirect.delivery.announcement.SiteAnnouncement;
import com.freshdirect.delivery.model.DlvRegionModel;
import com.freshdirect.delivery.model.DlvReservationModel;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.model.DlvZoneDescriptor;
import com.freshdirect.delivery.model.DlvZoneModel;
import com.freshdirect.delivery.model.EnumReservationClass;
import com.freshdirect.delivery.model.SectorVO;
import com.freshdirect.delivery.model.UnassignedDlvReservationModel;
import com.freshdirect.delivery.restriction.GeographyRestriction;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.delivery.restriction.ejb.DlvRestrictionDAO;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDDynamicTimeslotList;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDTimeslot;
import com.freshdirect.fdstore.RoutingUtil;
import com.freshdirect.fdstore.StateCounty;
import com.freshdirect.framework.core.GatewaySessionBeanSupport;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.constants.EnumOrderMetricsSource;
import com.freshdirect.routing.constants.EnumRoutingUpdateStatus;
import com.freshdirect.routing.constants.EnumWaveInstanceStatus;
import com.freshdirect.routing.constants.RoutingActivityType;
import com.freshdirect.routing.model.IDeliveryReservation;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.model.IDeliverySlotCost;
import com.freshdirect.routing.model.IDeliveryWindowMetrics;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.IRoutingNotificationModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.model.TrnFacilityType;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingEngineServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.routing.util.RoutingTimeOfDay;

public class DlvManagerSessionBean extends GatewaySessionBeanSupport {

	private static final long	serialVersionUID	= 1817746018911108166L;

	private static final Category LOGGER = LoggerFactory.getInstance(DlvManagerSessionBean.class);

	private final static ServiceLocator LOCATOR = new ServiceLocator();

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

	public List<DlvTimeslotModel> getTimeslotForDateRangeAndZone(java.util.Date begDate, java.util.Date endDate, AddressModel address)
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

	public List<DlvTimeslotModel> getAllTimeslotsForDateRange(java.util.Date startDate, java.util.Date endDate, AddressModel address)
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


	public List<DlvTimeslotCapacityInfo> getTimeslotCapacityInfo(java.util.Date date) {
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


	public List<DlvTimeslotModel> getTimeslotsForDepot(java.util.Date startDate, java.util.Date endDate, String regionId, String zoneCode)
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

	private static String depotZoneInfoQuery = "SELECT z.ID ZONE_ID, rd.DELIVERY_CHARGES, rd.START_DATE, zd.UNATTENDED, zd.COS_ENABLED "
		+ "FROM DLV.REGION_DATA rd, DLV.ZONE z, transp.zone zd WHERE z.ZONE_CODE = zd.ZONE_CODE and rd.REGION_ID = ? "
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
				new DlvZoneInfoModel(zoneCode, rs.getString("ZONE_ID"), null, EnumZipCheckResponses.DELIVER,"X".equals(rs.getString("UNATTENDED")),"X".equals(rs.getString("COS_ENABLED")));

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

	public DlvReservationModel reserveTimeslot(DlvTimeslotModel timeslotModel,
		String customerId,
		long holdTime,
		EnumReservationType type,
		ContactAddressModel address, boolean chefsTable, String profileName, boolean isForced, TimeslotEventModel event) throws ReservationException {

		// Get the Timeslot object
		/*DlvTimeslotModel timeslotModel;
		try {
			timeslotModel = this.getTimeslotById(timeslotId);
		} catch (FinderException e) {
			this.getSessionContext().setRollbackOnly();
			throw new ReservationException("Cannot find timeslot " + timeslotId);
		}*/

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
		if (chefsTable && (timeslotModel.getBaseAvailable() > 0 || timeslotModel.getChefsTableCapacity() == 0)) {
			chefsTable = false;
		}

		//
		// put all reservations in base, if we're past CT auto-release
		//
		if (chefsTable && timeslotModel.isCTCapacityReleased(now)) {
			chefsTable = false;
		}
		
		if(timeslotModel.getRoutingSlot().isDynamicActive() && FDStoreProperties.isDynamicRoutingEnabled()) {
			
			if(!isForced) {
				List<FDTimeslot> routingTimeslots = new ArrayList<FDTimeslot>(); 
				routingTimeslots.add(new FDTimeslot(timeslotModel));
				
				event.setEventType(EventType.CHECK_TIMESLOT);
				FDDynamicTimeslotList dynamicTimeslots = this.getTimeslotForDateRangeAndZoneEx(routingTimeslots,event, address);
				
				if(routingTimeslots != null) {
					dynamicTimeslots.getTimeslots();
				}
				if(routingTimeslots != null && routingTimeslots.size() > 0) {
					
					TimeslotCapacityContext context = new TimeslotCapacityContext();					
					context.setCurrentTime(new Date());
					
					TimeslotCapacityWrapper capacityProvider = new TimeslotCapacityWrapper(routingTimeslots.get(0).getDlvTimeslot(), context);
					
					//
					// ensure total capacity
					//
					if (!chefsTable && !capacityProvider.isAvailable()) {
						this.getSessionContext().setRollbackOnly();
						throw new ReservationUnavailableException("No more capacity available for this timeslot");
					}
					context.setChefsTable(true);
					//
					// ensure chef's table capacity
					//
					if (chefsTable && !capacityProvider.isAvailable()) {
						this.getSessionContext().setRollbackOnly();
						throw new ReservationException("No more capacity available for this timeslot");
					}
				}
			}
		} else {
			//
			// ensure total capacity
			//
			if (!chefsTable && (timeslotModel.getCapacity()-timeslotModel.calculateCurrentAllocation(new Date())) <= 0) {
				this.getSessionContext().setRollbackOnly();
				throw new ReservationUnavailableException("No more capacity available for this timeslot");
			}
	
			//
			// ensure chef's table capacity
			//
			if (chefsTable && timeslotModel.getChefsTableAvailable() <= 0) {
				this.getSessionContext().setRollbackOnly();
				throw new ReservationException("No more capacity available for this timeslot");
			}
	
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
			SectorVO sectorInfo = DlvManagerDAO.getSectorInfo(con, address);
			ps = con
				.prepareStatement("INSERT INTO dlv.reservation(ID, TIMESLOT_ID, ZONE_ID, ORDER_ID, CUSTOMER_ID, STATUS_CODE" +
						", EXPIRATION_DATETIME, TYPE, ADDRESS_ID, CHEFSTABLE, MODIFIED_DTTM,CT_DELIVERY_PROFILE,IS_FORCED, SECTOR, CLASS) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?,SYSDATE,?,?,?,?)");
			String newId = this.getNextId(con, "DLV");
			ps.setString(1, newId);
			ps.setString(2, timeslotModel.getId());
			ps.setString(3, timeslotModel.getZoneId());
			ps.setString(4, "x" + newId);
			ps.setString(5, customerId);
			ps.setInt(6, EnumReservationStatus.RESERVED.getCode());
			ps.setTimestamp(7, new Timestamp(expiration.getTime()));
			ps.setString(8, type.getName());
			ps.setString(9, address.getId());
			ps.setString(10, chefsTable ? "X" : " ");
			ps.setString(11, profileName);
			ps.setString(12, isForced  ? "X" : null );
			ps.setString(13, sectorInfo != null  ? sectorInfo.getName() : null );
			ps.setString(14, (timeslotModel.isPremiumSlot() && chefsTable ? EnumReservationClass.PREMIUMCT.getName(): (timeslotModel.isPremiumSlot() && !chefsTable)?EnumReservationClass.PREMIUM.getName():""));

			ps.executeUpdate();
			EnumReservationClass rsvClass = (timeslotModel.isPremiumSlot() && chefsTable) ? EnumReservationClass.PREMIUMCT: (timeslotModel.isPremiumSlot() && !chefsTable)?EnumReservationClass.PREMIUM:null;
			
			DlvReservationModel rsv = new DlvReservationModel(
				new PrimaryKey(newId),
				"x" + newId,
				customerId,
				EnumReservationStatus.RESERVED.getCode(),
				expiration,
				timeslotModel.getId(),
				timeslotModel.getZoneId(),
				type,
				address.getId(),
				timeslotModel.getBaseDate(),
				timeslotModel.getZoneCode(),null,false,null, null, null, null, null, null, null, rsvClass, null, null);
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

	public TimeslotEventModel buildEvent(List<FDTimeslot> timeSlots, TimeslotEventModel event, DlvReservationModel reservation, IOrderModel order, ContactAddressModel address, EventType eventType, int responseTime)
	{
		Connection conn = null;
		try
		{
			conn = getConnection();
			String id = SequenceGenerator.getNextId(conn, "MIS", "TIMESLOT_LOG_SEQUENCE");
			event.setId(id);
			if(reservation!=null)
				event.setReservationId(reservation.getId());
			event.setOrderId((null==order)?RoutingUtil.getOrderNo(address):order.getOrderNumber());
			event.setCustomerId((null==reservation)?address.getCustomerId():reservation.getCustomerId());
			event.setEventType(eventType);
			event.setResponseTime(responseTime);
			event.setAddress(getAddressString(address));
			event.setEventDate(new Date());
			event.setLatitude(address.getLatitude());
			event.setLongitude(address.getLongitude());
			
	
			List<TimeslotEventDetailModel> slots = new ArrayList<TimeslotEventDetailModel>();
			
		
			if(!(eventType==EventType.GET_TIMESLOT || eventType==EventType.CHECK_TIMESLOT ) && responseTime>0)
			{
				IDeliverySlot slot =  RoutingUtil.getDeliverySlot(getTimeslotById(reservation.getTimeslotId(), reservation.isPremium()));
				TimeslotEventDetailModel eventD = new TimeslotEventDetailModel();
				List<TimeslotEventDetailModel> eventDL = new ArrayList<TimeslotEventDetailModel>();
				
				/** Get the first delivery slot as it will have only one */
	
				eventD.setStartTime(slot.getStartTime());
				eventD.setStopTime(slot.getStopTime());
				eventD.setZoneCode(slot.getZoneCode());
				eventD.setDeliveryDate(slot.getSchedulerId().getDeliveryDate());
				eventDL.add(eventD);
				event.setDetail(eventDL);
			}
				
			else
			{
				
				if(timeSlots != null) {
						
					
					for (FDTimeslot slot : timeSlots) {
						DlvTimeslotModel dlvSlot = slot.getDlvTimeslot();
						if(dlvSlot!=null)
						{
							IDeliverySlot routingSlot = dlvSlot.getRoutingSlot();
							TimeslotEventDetailModel detailModel = new TimeslotEventDetailModel();
							RoutingModel routingModel = null;
								if(routingSlot != null)
								{
									IDeliverySlotCost cost = slot.getDlvTimeslot().getRoutingSlot().getDeliveryCost();
									if(cost!=null)
									{
										routingModel = new RoutingModel(cost.getAdditionalDistance(),cost.getAdditionalRunTime(), cost.getAdditionalStopCost(),
																				/*cost.getCapacity(),*/ cost.getCostPerMile(), cost.getFixedRouteSetupCost(),
																				cost.getMaxRunTime(), cost.getOvertimeHourlyWage(), cost.getPrefRunTime(),
																				cost.getRegularHourlyWage(), cost.getRegularWageDurationSeconds(),cost.getRouteId(),
																				cost.getStopSequence(), cost.getTotalDistance(), /*cost.getTotalPUQuantity(),
																				cost.getTotalQuantity(),*/ cost.getTotalRouteCost(), cost.getTotalRunTime(), cost.getTotalServiceTime(),
																				cost.getTotalTravelTime(), cost.getTotalWaitTime(), cost.isAvailable(), cost.isFiltered(), cost.isMissedTW(),
																				cost.getWaveVehicles(), cost.getWaveVehiclesInUse(), cost.getWaveStartTime(), cost.getUnavailabilityReason(),
																				cost.getWaveOrdersTaken(), cost.getTotalQuantities(), cost.isNewRoute(), cost.getCapacities());
									
									}
									detailModel.setRoutingModel(routingModel);
									detailModel.setStartTime(routingSlot.getStartTime());
									detailModel.setStopTime(routingSlot.getStopTime());
									if(routingSlot.getSchedulerId()!=null)
										detailModel.setDeliveryDate(routingSlot.getSchedulerId().getDeliveryDate());
									detailModel.setManuallyClosed(routingSlot.isManuallyClosed());
								}
							detailModel.setWs_amount(dlvSlot.getSteeringDiscount());
							detailModel.setAlcohol_restriction(slot.isAlcoholRestricted());
							detailModel.setHoliday_restriction(slot.isHolidayRestricted());
							detailModel.setGeoRestricted(slot.isGeoRestricted());
							detailModel.setEcofriendlyslot(slot.isEcoFriendly());
							detailModel.setNeighbourhoodslot(slot.isEcoFriendly());
							detailModel.setTotalCapacity(dlvSlot.getCapacity());
							detailModel.setCtCapacity(dlvSlot.getChefsTableCapacity());
							detailModel.setStoreFrontAvailable(slot.getStoreFrontAvailable());
							detailModel.setCtAllocated(dlvSlot.getChefsTableAllocation());
							detailModel.setTotalAllocated(dlvSlot.getTotalAllocation());
							detailModel.setZoneCode(dlvSlot.getZoneCode());
							detailModel.setCutOff(dlvSlot.getCutoffTimeAsDate());
							slots.add(detailModel);	
						}
						
					}
					event.setDetail(slots);
				}
			}
		}
		catch(Exception e)
			{
				LOGGER.info("Exception while logging the timeslot: "+e.getMessage());
			}
		finally
		{
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return event;
	}
	
	private static final String RESERVATION_BY_ID="SELECT R.ID, R.ORDER_ID, R.CUSTOMER_ID, R.STATUS_CODE, R.TIMESLOT_ID, R.ZONE_ID" +
			", R.EXPIRATION_DATETIME, R.TYPE, R.ADDRESS_ID,T.BASE_DATE, Z.ZONE_CODE,R.UNASSIGNED_DATETIME, R.UNASSIGNED_ACTION" +
			", R.IN_UPS, R.ORDER_SIZE, R.SERVICE_TIME, R.RESERVED_ORDER_SIZE, R.RESERVED_SERVICE_TIME" +
			", R.UPDATE_STATUS, R.METRICS_SOURCE, R.NUM_CARTONS , R.NUM_FREEZERS , R.NUM_CASES, R.CLASS, T.IS_DYNAMIC  FROM DLV.RESERVATION R, "+
            " DLV.TIMESLOT T, DLV.ZONE Z WHERE R.TIMESLOT_ID=T.ID AND R.ZONE_ID=Z.ID AND R.ID=?";

	private DlvReservationModel getReservation(Connection con, String rsvId) throws SQLException {
		PreparedStatement ps = con
			.prepareStatement(RESERVATION_BY_ID);
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
			.getString("ADDRESS_ID"),rs.getDate("BASE_DATE"),rs.getString("ZONE_CODE"),RoutingActivityType.getEnum(rs.getString("UNASSIGNED_ACTION")),
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
		rsv.setDynamic("X".equalsIgnoreCase(rs.getString("IS_DYNAMIC"))?true:false);
		rs.close();
		ps.close();

		return rsv;
	}

	public void commitReservation(String rsvId, String customerId, String orderId,boolean pr1) throws ReservationException {
		Connection con = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;

		DlvReservationModel rsv = null;
		LOGGER.debug("Going to commit the reservation for id: " + rsvId);
		try {
			con = getConnection();
			ps1 = con
				.prepareStatement("UPDATE DLV.RESERVATION SET ORDER_ID = ?, STATUS_CODE = ?, MODIFIED_DTTM=SYSDATE ,PR1= ? WHERE ID = ? and STATUS_CODE = ? ");

			rsv = this.getReservation(con, rsvId);

			if (rsv.getCustomerId().equals(customerId)) {
				rsv.setOrderId(orderId);
				rsv.setStatusCode(EnumReservationStatus.COMMITTED.getCode());
				ps1.setString(1, rsv.getOrderId());
				ps1.setInt(2, rsv.getStatusCode());
				ps1.setString(3, pr1 ? "X" : " ");
				ps1.setString(4, rsvId);
				ps1.setInt(5, EnumReservationStatus.RESERVED.getCode());

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

	public List<DlvReservationModel> getReservationsForCustomer(String customerId) {
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

	private List<DlvReservationModel> getAllReservationsByCustomerAndTimeslot(String customerId, String timeslotId) {
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

	public boolean makeRecurringReservation(String customerId, int dayOfWeek, Date startTime, Date endTime, ContactAddressModel address, boolean chefstable, TimeslotEventModel event) {
		try {
			Date startDate = getDateByNextDayOfWeek(dayOfWeek);
			Date endDate = DateUtil.addDays(startDate, 1);

			List<DlvTimeslotModel> baseTimeslots = this.getTimeslotForDateRangeAndZone(startDate, endDate, address);
			
			List<FDTimeslot> routingTimeslots = new ArrayList<FDTimeslot>();
			
			for (DlvTimeslotModel timeslot : baseTimeslots) {
				routingTimeslots.add(new FDTimeslot(timeslot));
			}
			
			/*if(FDStoreProperties.isDynamicRoutingEnabled()) {
				routingTimeslots = this.getTimeslotForDateRangeAndZoneEx(routingTimeslots, address);
			}*/
			
			List<String> errMessages = new ArrayList<String>();

			try {
				if(routingTimeslots != null) {
					List<GeographyRestriction> geographicRestrictions = this.getGeographicDlvRestrictionsForReservation( address);

					if(geographicRestrictions != null && geographicRestrictions.size() > 0) {
						for(Iterator<FDTimeslot> i = routingTimeslots.iterator(); i.hasNext();) {
							FDTimeslot ts = (FDTimeslot)i.next();
							if (GeographyRestriction.isTimeSlotGeoRestricted(geographicRestrictions, ts, errMessages, null,null)) {
								// filter off empty timeslots (unless they must be retained)
								i.remove();
							}
						}
					}
				}
			} catch (DlvResourceException dlvResException) {
				LOGGER.info("Failed to load Geography Restriction "+errMessages);
			}

			DlvTimeslotModel foundTimeslot = null;
			for (FDTimeslot ts : routingTimeslots) {
				DlvTimeslotModel t = ts.getDlvTimeslot();
				if (t.isMatching(startDate, startTime, endTime)) {
					foundTimeslot = t;
					break;
				}
			}
			
			

			if (foundTimeslot == null) {
				logActivity(EnumTransactionSource.SYSTEM, EnumAccountActivityType.MAKE_PRE_RESERVATION,"SYSTEM", customerId,
								"Failed to make recurring reservation for customer - no timeslot found");
				LOGGER.info("Failed to make recurring reservation for customer " + customerId + " - no timeslot found");
				return false;
			}

			final List<DlvReservationModel> reservations = getAllReservationsByCustomerAndTimeslot(customerId, foundTimeslot.getPK().getId());
			for (DlvReservationModel rsv : reservations) {
				if (!EnumReservationType.STANDARD_RESERVATION.equals(rsv.getReservationType())) {
					logActivity(EnumTransactionSource.SYSTEM, EnumAccountActivityType.MAKE_PRE_RESERVATION,"SYSTEM", customerId,
									"Failed to make recurring reservation for customer - already reserved");
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
			DlvReservationModel dlvReservation=this.reserveTimeslot(
				foundTimeslot,
				customerId,
				duration,
				EnumReservationType.RECURRING_RESERVATION,
				address, chefstable,null, false, event);

			logActivity(EnumTransactionSource.SYSTEM, EnumAccountActivityType.MAKE_PRE_RESERVATION,"SYSTEM", customerId,
								"Made recurring reservation");

			LOGGER.info("Made recurring reservation for " + customerId);

			if(FDStoreProperties.isDynamicRoutingEnabled() && foundTimeslot.getRoutingSlot().isDynamicActive()) {
				this.reserveTimeslotEx(dlvReservation,address, new FDTimeslot(foundTimeslot), event);
			}
			return true;

		} catch (Exception e) {

			LOGGER.warn("Could not Reserve a Weekly recurring timeslot for customer id: "+customerId, e);
			return false;
		}
	}

	public void removeReservation(String reservationId) {
		cancelReservation(reservationId);
		/*Connection conn = null;
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
		}*/
	}

	private final static String FIND_ZONES_BY_ZONEID_QUERY =
		"select dz.id id, dz.name name, dz.region_data_id region_data_id , dz.zone_code zone_code , dz.plan_id plan_id, dz.CT_ACTIVE CT_ACTIVE, " +
		"dz.CT_RELEASE_TIME CT_RELEASE_TIME, dz.premium_ct_release_time, unattended, dz.premium_ct_active from dlv.zone dz, transp.zone tz "+
		" where dz.zone_code = tz.zone_code(+)  "+
		" and id = ? ";

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
						"X".equals(rs.getString("PREMIUM_CT_ACTIVE")), rs.getInt("CT_RELEASE_TIME"),rs.getInt("PREMIUM_CT_RELEASE_TIME"),
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

	public DlvTimeslotModel getTimeslotById(String timeslotId, boolean checkPremium) throws FinderException {

		Connection conn = null;
		try {
			conn = getConnection();
			return DlvManagerDAO.getTimeslotById(conn, timeslotId, checkPremium);
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

	private static final String FIND_ZONES_BY_REGIONID_QUERY =
	"select dz.id id, dz.name name, dz.region_data_id region_data_id, dz.zone_code zone_code, dz.plan_id plan_id, dz.CT_ACTIVE CT_ACTIVE, " +
	"dz.premium_ct_active, dz.CT_RELEASE_TIME CT_RELEASE_TIME, dz.premium_ct_release_time, tz.unattended unattended "+
	" from dlv.zone dz, transp.zone tz"+
	" where dz.ZONE_CODE=tz.ZONE_CODE(+) "+
	" and dz.region_data_id =?";


	public List<DlvZoneModel> getAllZonesByRegion(String regionId) {
		ArrayList<DlvZoneModel> ret = new ArrayList<DlvZoneModel>();
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
					"X".equals(rs.getString("PREMIUM_CT_ACTIVE")), rs.getInt("CT_RELEASE_TIME"),rs.getInt("PREMIUM_CT_RELEASE_TIME"),
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

	public List<AddressModel> findSuggestionsForAmbiguousAddress(AddressModel address) throws InvalidAddressException {
		GeographyDAO dao = new GeographyDAO();
		Connection conn = null;
		ArrayList<AddressModel> list = new ArrayList<AddressModel>();
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
			LOGGER.debug("before geocode --> "+address);
			result = dao.geocode(address, conn);
			LOGGER.debug("after geocode --> "+address);
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
			DlvZoneInfoModel zoneInfo=DlvManagerDAO.getZoneInfo(conn, address, date, useApartment);
			if(EnumZipCheckResponses.DONOT_DELIVER.equals(zoneInfo.getResponse())){
				zoneInfo=DlvManagerDAO.getZoneInfoForCosEnabled(conn, address, date, useApartment);
			}
			return zoneInfo;

		} catch (SQLException sqle) {
			LOGGER.warn("Difficulty locating an address within a zone : " + sqle.getMessage());
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

	public List<DlvZoneCutoffInfo> getCutoffInfo(String zoneCode, Date day) {
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

	public List<DlvZipInfoModel> getDeliverableZipCodes(EnumServiceType serviceType) {
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
			result.setEbtAccepted(checkEbtPaymentAccepted(conn,zipcode));
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
			
			if(EnumDeliveryStatus.DONOT_DELIVER.equals(status)){
				status= this.getServiceStatus(DlvManagerDAO.checkAddressForCosEnabled(conn, address));
				if(EnumDeliveryStatus.DELIVER.equals(status) || EnumDeliveryStatus.PARTIALLY_DELIVER.equals(status)){
					result.addServiceStatus(EnumServiceType.CORPORATE, EnumDeliveryStatus.COS_ENABLED);
				}
				
			}

			result.setRestrictionReason(DlvManagerDAO.isAddressRestricted(conn, address));
			result.setEbtAccepted(checkEbtPaymentAccepted(conn,address.getZipCode()));
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

	public EnumDeliveryStatus getServiceStatus(List<DlvZipInfoModel> dlvInfos) {
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

		for ( DlvZipInfoModel dlvInfo : dlvInfos) {
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

	public List<DlvApartmentRange> findApartmentRanges(AddressModel address) throws InvalidAddressException {
		Connection conn = null;
		List<DlvApartmentRange> ranges = new ArrayList<DlvApartmentRange>();
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
			List<DlvReservationModel> reservations = getReservationsForCustomer(rsv.getCustomerId());
			if (reservations.isEmpty()) {
				restoreReservation(rsvId);
				isRestored = true;
			} else {
				DlvReservationModel foundRsv = null;
				for ( DlvReservationModel r : reservations ) {
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
			ps = con.prepareStatement("UPDATE DLV.RESERVATION SET STATUS_CODE = ?, MODIFIED_DTTM=SYSDATE WHERE ID = ?");
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

	public void saveFutureZoneNotification(String email, String zip,String serviceType) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement("INSERT INTO DLV.ZONENOTIFICATION (EMAIL, ZIPCODE, SERVICE_TYPE, CREATE_DATE) VALUES (?, ?, ?, ?)");
			ps.setString(1, email);
			ps.setString(2, zip);
			ps.setString(3, serviceType);
			ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

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

	private static String zoneQuery =
		"select dz.NAME name, dz.ZONE_CODE ZONE_CODE, tz.UNATTENDED UNATTENDED "+
		"from dlv.zone dz, transp.zone tz "+
		"where dz.zone_code=tz.zone_code(+) "+
		" and region_data_id = "+
		"(select id from dlv.region_data where start_date = (SELECT MAX(START_DATE) "+
		" FROM DLV.REGION_DATA WHERE START_DATE <= sysdate and region_id = ?) and region_id = ?)";


	public Collection<DlvZoneModel> getZonesForRegionId(String regionId) throws DlvResourceException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DlvZoneModel> zones = new ArrayList<DlvZoneModel>();

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

	public Collection<DlvRegionModel> getAllRegions() throws DlvResourceException {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<DlvRegionModel> regions = new ArrayList<DlvRegionModel>();

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

	public List<RestrictionI> getDlvRestrictions() throws DlvResourceException {
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

	public List<GeographyRestriction> getGeographicDlvRestrictions(AddressModel address) throws DlvResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			return DlvRestrictionDAO.getGeographicDlvRestrictions(conn, address);

		} catch (SQLException e) {
			//this.getSessionContext().setRollbackOnly();
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

	public List<SiteAnnouncement> getSiteAnnouncements() throws DlvResourceException {
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

	public List<ExceptionAddress> searchGeocodeException(ExceptionAddress ex){
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

	public List<String> getCountiesByState(String stateAbbrev){
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

	public List<MunicipalityInfo> getMunicipalityInfos() {
		Connection conn = null;
		try{
			List<MunicipalityInfo> muniList = new ArrayList<MunicipalityInfo>();
			conn = this.getConnection();
			PreparedStatement ps = conn.prepareStatement(muniSql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String state = rs.getString("state");
				if ( state==null) continue;
				boolean alcoholRestricted = "X".equalsIgnoreCase(rs.getString("alcohol_restricted"))? true : false;
				MunicipalityInfo mi = new MunicipalityInfo(
					rs.getString("id"),
					state,
					rs.getString("county"),
					rs.getString("city"),
					rs.getString("gl_code"),
					rs.getDouble("tax_rate"),
					rs.getDouble("bottle_deposit"),
					alcoholRestricted );
				muniList.add(mi);
			}
			return (muniList.size() > 0 ? muniList : Collections.<MunicipalityInfo>emptyList() );
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

	public List<ExceptionAddress> searchExceptionAddresses(ExceptionAddress ea){
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

	public List<Date> getCutoffTimesByDate(Date day) {
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

	public FDDynamicTimeslotList getTimeslotForDateRangeAndZoneEx(List<FDTimeslot> _timeSlots, TimeslotEventModel event, ContactAddressModel address) {

		long startTime = System.currentTimeMillis();
		List<FDTimeslot> timeSlots = _timeSlots;
		FDDynamicTimeslotList result = new FDDynamicTimeslotList();
		if(RoutingUtil.hasAnyDynamicEnabled(_timeSlots)) {
			try {
				timeSlots = RoutingUtil.getTimeslotForDateRangeAndZone(_timeSlots, address);
				long endTime = System.currentTimeMillis();	
				result.setResponseTime((int)(endTime-startTime));
				if(event!=null && !event.isFilter())
				{
					event = buildEvent(timeSlots, event, null, null,address,event.getEventType(), (int)(endTime-startTime));
					SectorVO sectorInfo = FDDeliveryManager.getInstance().getSectorInfo(address);
					if(event!=null && event.getId()!=null && sectorInfo != null){
						event.setSector(sectorInfo.getName());
					}
					if(event!=null && event.getId()!=null)
						logTimeslots(event);
				}
				
				
			} catch (Exception e) {
					event = buildEvent(null, event, null, null,address,(event!=null)?event.getEventType():EventType.GET_TIMESLOT, 0);
					if(event!=null && event.getId()!=null)
						logTimeslots(event);
					e.printStackTrace();
					LOGGER.debug("Exception in getTimeslotForDateRangeAndZoneEx():"+e.toString());
					result.setError(e);
			}
		}
		
		result.setTimeslots(timeSlots);
		return result;
	}

	public IDeliveryReservation reserveTimeslotEx(DlvReservationModel reservation, ContactAddressModel address , FDTimeslot timeslot, TimeslotEventModel event) {

		IDeliveryReservation _reservation = null;
		
			if(reservation == null || address == null || timeslot.getDlvTimeslot().getRoutingSlot()	== null 
															|| !timeslot.getDlvTimeslot().getRoutingSlot().isDynamicActive()) {
				return null;
			}
			if (RoutingActivityType.RESERVE_TIMESLOT.equals(reservation.getUnassignedActivityType())) {
				if(reservation.getStatusCode() == 5) {
					_reservation = doReserveEx(reservation, address, timeslot, event);
				} else if(reservation.getStatusCode() == 15 || reservation.getStatusCode() == 20) {
					clearUnassignedInfo(reservation.getId());
				} else if(reservation.getStatusCode() == 10) {
					_reservation = doReserveEx(reservation, address, timeslot, event);
					if( _reservation != null && _reservation.isReserved()) {
						doConfirmEx(reservation, address, event);
					}
				}
			} else {
				_reservation = doReserveEx(reservation, address, timeslot, event);
			}
		return _reservation;
	}

	public void commitReservationEx(DlvReservationModel reservation,ContactAddressModel address, TimeslotEventModel event) throws FDResourceException{
		
		/* Refresh the reservation from the database as the flag isInUPS might have changed after the CONFIRM_TIMESLOT payload was put in the queue.
		 This code is to handle scenarios when CONFIRM_TIMESLOT payload is processed before the RESERVE_TIMESLOT */
		try {
			if(reservation!=null)
				reservation=getReservation(reservation.getId());
		} catch (FinderException e) {
			
			e.printStackTrace();
		}
		
		if(reservation==null || address==null || !reservation.isDynamic())
			return ;
		
		// Put the CONFIRM_TIMESLOT payload back in the queue to be retried after the redelivery interval configured in application server.
		if(reservation.isDynamic() && !reservation.isInUPS() && reservation.getStatusCode() == 10)
		{
			throw new RuntimeException("Reserve is not in UPS yet. putting it back in queue.");
		}

		if (RoutingActivityType.CONFIRM_TIMESLOT.equals(reservation.getUnassignedActivityType())) {
			if(reservation.getStatusCode() == 10) {
				doConfirmEx(reservation, address, event);
			} else if(reservation.getStatusCode() == 15 || reservation.getStatusCode() == 20) {
				doReleaseReservationEx(reservation, address,event);				
			} /*else if(reservation.getStatusCode() == 5) {
				updateReservationEx(reservation, address);
			}*/
		} else if (reservation.getUnassignedActivityType() == null){
			doConfirmEx(reservation, address, event);
		}	else if (RoutingActivityType.CANCEL_TIMESLOT.equals(reservation.getUnassignedActivityType())) {
			doReleaseReservationEx(reservation, address,event);
		}
	}
	
	public void updateReservationStatus(DlvReservationModel reservation, ContactAddressModel address, String erpOrderId) {

		if(reservation==null || reservation.getStatusCode() != 10 /* || !reservation.isInUPS() || reservation.getUnassignedActivityType().*/)
			return ;
		IOrderModel order = RoutingUtil.getOrderModel(reservation, address, reservation.getOrderId(), reservation.getId());
		try {
			
			order = RoutingUtil.calculateReservationSize(reservation, order, erpOrderId);
			if(order.getDeliveryInfo() != null && order.getDeliveryInfo().getPackagingDetail() != null
						&& !EnumOrderMetricsSource.DEFAULT.equals(order.getDeliveryInfo().getPackagingDetail().getSource())) {
				setReservationMetricsDetails(reservation.getId(), order.getDeliveryInfo().getPackagingDetail().getNoOfCartons()
											, order.getDeliveryInfo().getPackagingDetail().getNoOfCases()
											, order.getDeliveryInfo().getPackagingDetail().getNoOfFreezers()
											, reservation.isInUPS() ? EnumRoutingUpdateStatus.PENDING : null
											, EnumOrderMetricsSource.ACTUAL);
			}
		} catch (Exception e) {
			LOGGER.debug("Exception in updateReservationStatus():"+e.toString());
			e.printStackTrace();			
		}
	}
	
	public void updateReservationEx(DlvReservationModel reservation, ContactAddressModel address, FDTimeslot timeslot) {

		if(reservation==null || !reservation.isInUPS() || reservation.getUnassignedActivityType() != null)
			return ;
		IOrderModel order = RoutingUtil.getOrderModel(reservation, address, reservation.getOrderId(), reservation.getId());
		
		try {
			if(reservation.getStatusCode() == 15 || reservation.getStatusCode() == 20) {
				setReservationMetricsStatus(reservation.getId(), EnumRoutingUpdateStatus.SUCCESS);
			} else {
				order = RoutingUtil.calculateReservationSize(reservation, order, timeslot);
				if(((reservation.getReservedOrderSize() != null
						&& order.getDeliveryInfo().getCalculatedOrderSize() < reservation.getReservedOrderSize())
						|| (reservation.getReservedServiceTime() != null
								&& order.getDeliveryInfo().getCalculatedServiceTime() < reservation.getReservedServiceTime()))
								&& (timeslot != null && DateUtil.getDiffInMinutes(Calendar.getInstance().getTime()
														, (timeslot.getDlvTimeslot().getPremiumCutoffTime()!=null)?timeslot.getDlvTimeslot().getPremiumCutoffAsDate():timeslot.getDlvTimeslot().getCutoffTimeAsDate())
										> RoutingServicesProperties.getOMUseOriginalThreshold()) 
								&& !EnumRoutingUpdateStatus.OVERRIDDEN.equals(reservation.getUpdateStatus())) {
						return;
					
				} else {
					
					boolean isUpdated = RoutingUtil.updateReservation(reservation, order);
					
					if(isUpdated) {
						setReservationReservedMetrics(reservation.getId(), order.getDeliveryInfo().getCalculatedOrderSize()
															, order.getDeliveryInfo().getCalculatedServiceTime()
															, EnumRoutingUpdateStatus.SUCCESS);
					} else {
						if(!EnumRoutingUpdateStatus.OVERRIDDEN.equals(reservation.getUpdateStatus())) {
							this.setReservationMetricsStatus(reservation.getId(), EnumRoutingUpdateStatus.FAILED);
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.debug("Exception in updateReservationEx():"+e.toString());
			e.printStackTrace();			
		}
	}
	
	public void releaseReservationEx(DlvReservationModel reservation,ContactAddressModel address , TimeslotEventModel event) {

		/* Refresh the reservation from the database as the flag isInUPS might have changed after the CANCEL_TIMESLOT payload was put in the queue.
		 This code is to handle scenarios when CANCEL_TIMESLOT payload is processed before the RESERVE_TIMESLOT */
		try {
			if(reservation!=null)
				reservation=getReservation(reservation.getId());
		} catch (FinderException e) {
			
			e.printStackTrace();
		}
		if(reservation==null || address==null || !reservation.isDynamic())
			return ;
		
		// Put the CANCEL_TIMESLOT payload back in the queue to be retried after the redelivery interval configured in application server.
		if(reservation.isDynamic() && !reservation.isInUPS() && reservation.getStatusCode() == 15)
		{
			throw new RuntimeException("Reserve is not in UPS yet. putting it back in queue.");
		}
		if (RoutingActivityType.RESERVE_TIMESLOT.equals(reservation.getUnassignedActivityType())) {
			this.clearUnassignedInfo(reservation.getId());
		} else {
			doReleaseReservationEx(reservation, address, event);
		}
	}

	public void setUnassignedInfo(String reservationId, RoutingActivityType activity ) {
		Connection conn = null;
		try {
			conn = getConnection();
			DlvManagerDAO.setUnassignedInfo(conn, reservationId,activity.value());

		} catch (SQLException e) {
			LOGGER.warn("SQLException in DlvManagerDAO.unassignReservation() call ", e);
			e.printStackTrace();
			//throw new EJBException(e);
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
	
	public void setReservationMetricsStatus(String reservationId, EnumRoutingUpdateStatus status) {
		Connection conn = null;
		try {
			conn = getConnection();
			DlvManagerDAO.setReservationMetricsStatus(conn, reservationId, status.value());

		} catch (SQLException e) {
			LOGGER.warn("SQLException in DlvManagerDAO.setReservationMetricsStatus() call ", e);
			e.printStackTrace();
			//throw new EJBException(e);
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
	
	public void setReservationMetricsDetails(String reservationId, long noOfCartons, long noOfCases
											, long noOfFreezers, EnumRoutingUpdateStatus status) {
		Connection conn = null;
		try {
			conn = getConnection();
			DlvManagerDAO.setReservationMetricsDetails(conn, reservationId, noOfCartons, noOfCases
																, noOfFreezers, status.value());

		} catch (SQLException e) {
			LOGGER.warn("SQLException in DlvManagerDAO.setReservationMetricsDetails() call ", e);
			e.printStackTrace();
			//throw new EJBException(e);
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
	
	public void setReservationMetricsDetails(String reservationId, long noOfCartons, long noOfCases
												, long noOfFreezers, EnumRoutingUpdateStatus status
														, EnumOrderMetricsSource source) {
		Connection conn = null;
		try {
			conn = getConnection();
			DlvManagerDAO.setReservationMetricsDetails(conn, reservationId, noOfCartons, noOfCases
					, noOfFreezers, status !=null ? status.value() : null, source);

		} catch (SQLException e) {
			LOGGER.warn("SQLException in DlvManagerDAO.setReservationMetricsDetails() call ", e);
			e.printStackTrace();
			//throw new EJBException(e);
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
	
	public void setReservationMetricsDetails(String reservationId, long noOfCartons, long noOfCases
												, long noOfFreezers, EnumOrderMetricsSource source) {
		Connection conn = null;
		try {
			conn = getConnection();
			DlvManagerDAO.setReservationMetricsDetails(conn, reservationId, noOfCartons, noOfCases
														, noOfFreezers, source);

		} catch (SQLException e) {
			LOGGER.warn("SQLException in DlvManagerDAO.setReservationMetricsDetails_Source() call ", e);
			e.printStackTrace();
			//throw new EJBException(e);
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
	
	public void setReservationReservedMetrics(String reservationId, double reservedOrderSize
													, double reservedServiceTime, EnumRoutingUpdateStatus status) {
		Connection conn = null;
		try {
			conn = getConnection();
			DlvManagerDAO.setReservationReservedMetrics(conn, reservationId, reservedOrderSize, reservedServiceTime, status.value());

		} catch (SQLException e) {
			LOGGER.warn("SQLException in DlvManagerDAO.setReservationUpdateStatusInfo() call ", e);
			e.printStackTrace();
			//throw new EJBException(e);
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
		
	public void setReservationReservedMetrics(String reservationId, double reservedOrderSize, double reservedServiceTime) {
		Connection conn = null;
		try {
			conn = getConnection();
			DlvManagerDAO.setReservationReservedMetrics(conn, reservationId, reservedOrderSize, reservedServiceTime);

		} catch (SQLException e) {
			LOGGER.warn("SQLException in DlvManagerDAO.setReservationReservedMetrics() call ", e);
			e.printStackTrace();
			//throw new EJBException(e);
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
	
	public void setRoutingIndicator(String reservationId, String orderNum ) {
		Connection conn = null;
		try {
			conn = getConnection();
			DlvManagerDAO.setInUPS(conn, reservationId,orderNum );

		} catch (SQLException e) {
			LOGGER.warn("SQLException in DlvManagerDAO.setUPSIndicator() call ", e);
			e.printStackTrace();
			//throw new EJBException(e);
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
	public void clearUnassignedInfo(String reservationId ) {
		Connection conn = null;
		try {
			conn = getConnection();
			DlvManagerDAO.clearUnassignedInfo(conn, reservationId);

		} catch (SQLException e) {
			LOGGER.warn("SQLException in DlvManagerDAO.unassignReservation() call ", e);
			e.printStackTrace();
			//throw new EJBException(e);
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
	public List<UnassignedDlvReservationModel> getUnassignedReservations(Date _date,boolean includeCutoff) throws DlvResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return DlvManagerDAO.getUnassignedReservations(conn,_date,includeCutoff);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.warn("DlvManagerSB getUnassignedReservations(): " + e);
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB getUnassignedReservations(): Exception while cleaning: " + e);
			}
		}

	}
	
	public List<DlvReservationModel> getUnconfirmedReservations() throws DlvResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return DlvManagerDAO.getUnconfirmedReservations(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.warn("DlvManagerSB getUnconfirmedReservations(): " + e);
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB getUnconfirmedReservations(): Exception while cleaning: " + e);
			}
		}

	}

	public List<DlvReservationModel> getConfirmedRsvForCancelledOrders() throws DlvResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return DlvManagerDAO.getConfirmedRsvForCancelledOrders(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.warn("DlvManagerSB getConfirmedRsvForCancelledOrders(): " + e);
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB getConfirmedRsvForCancelledOrders(): Exception while cleaning: " + e);
			}
		}

	}
	public List<DlvReservationModel> getCancelledRsvInUPS() throws DlvResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return DlvManagerDAO.getCancelledRsvInUPS(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.warn("DlvManagerSB getCancelledRsvInUPS(): " + e);
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB getCancelledRsvInUPS(): Exception while cleaning: " + e);
			}
		}

	}
	
	public List<DlvReservationModel> getOrdersWithCancelledRsv() throws DlvResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return DlvManagerDAO.getOrdersWithCancelledRsv(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.warn("DlvManagerSB getOrdersWithCancelledRsv(): " + e);
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB getOrdersWithCancelledRsv(): Exception while cleaning: " + e);
			}
		}

	}
	
	public List<UnassignedDlvReservationModel> getReRouteReservations() throws DlvResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return DlvManagerDAO.getReRouteReservations(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.warn("DlvManagerSB getReRouteReservations(): " + e);
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB getReRouteReservations(): Exception while cleaning: " + e);
			}
		}

	}
	
	public void clearReRouteReservations() throws DlvResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			DlvManagerDAO.clearReRouteReservations(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.warn("DlvManagerSB clearReRouteReservations(): " + e);
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB clearReRouteReservations(): Exception while cleaning: " + e);
			}
		}

	}
	

	public List<DlvReservationModel> getExpiredReservations() throws DlvResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return DlvManagerDAO.getExpiredReservations(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.warn("DlvManagerSB getExpiredReservations(): " + e);
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB getExpiredReservations(): Exception while cleaning: " + e);
			}
		}

	}
	public void expireReservations() throws DlvResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			DlvManagerDAO.expireReservations(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.warn("DlvManagerSB expireReservations(): " + e);
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB expireReservations(): Exception while cleaning: " + e);
			}
		}

	}

	private void logActivity(EnumTransactionSource source, EnumAccountActivityType type,String initiator, String customerId, String note) {

		ErpActivityRecord rec = new ErpActivityRecord();
		rec.setActivityType(type);

		rec.setSource(source);
		rec.setInitiator(initiator);
		rec.setCustomerId(customerId);

		StringBuffer sb = new StringBuffer();
		if (note != null) {
		sb.append(note);
		}
		rec.setNote(sb.toString());
		new ErpLogActivityCommand(LOCATOR, rec).execute();
	}

	private String getAddressString(ContactAddressModel address) {
		StringBuilder resp=new StringBuilder();
		resp.append(address.getAddress1()).append(",")
			.append(address.getAddress2()!=null && !"".equals(address.getAddress2())?new StringBuilder(address.getAddress2()).append(",").toString():"")
		    .append(address.getApartment()!=null && !"".equals(address.getApartment())?new StringBuilder(address.getApartment()).append(",").toString():"")
		    .append(address.getCity()).append(",")
		    .append(address.getZipCode());

		return resp.toString();
	}
	
	public void logTimeslots(TimeslotEventModel event) {	
		
		if(FDStoreProperties.isEventLoggingEnabled())
		{
			this.enqueue(event);
		}

	}
	private void enqueue(TimeslotEventModel event) {
		try {
			ObjectMessage logMsg = this.qsession.createObjectMessage();
			logMsg.setStringProperty("MessageType","LOG_TIMESLOT" );
			logMsg.setObject(event);
			this.qsender.send(logMsg);
		} catch (Exception ex) {
			LOGGER.warn("Error enqueueing command", ex);
		}
	}
	
	private ContactAddressModel getContactAddress(AddressModel model, String customerId) {
		ContactAddressModel _cModel = new ContactAddressModel();
		_cModel.setFrom(model, "", "", customerId);
		return _cModel;
	}
	
	private IDeliveryReservation doReserveEx(DlvReservationModel reservation,ContactAddressModel address , FDTimeslot timeslot, TimeslotEventModel event) {

		long startTime=System.currentTimeMillis();
		IOrderModel order=RoutingUtil.getOrderModel(reservation, address, reservation.getOrderId(), reservation.getId());
		IDeliveryReservation _reservation=null;
		try {
			 _reservation = RoutingUtil.reserveTimeslot(reservation, order, timeslot);
			long endTime=System.currentTimeMillis();
			event = buildEvent(null, event, reservation,order,address, EventType.RESERVE_TIMESLOT, (int)(endTime-startTime));
			if(event!=null && event.getId()!=null)
				logTimeslots(event);
			if(_reservation==null || !_reservation.isReserved()) {
					setUnassignedInfo(reservation.getId(), RoutingActivityType.RESERVE_TIMESLOT);					
			} else {
				    this.setReservationReservedMetrics(reservation.getId()
															, order.getDeliveryInfo().getCalculatedOrderSize()
															, order.getDeliveryInfo().getCalculatedServiceTime()
															, EnumRoutingUpdateStatus.SUCCESS);
					clearUnassignedInfo(reservation.getId());
					if(!EnumRoutingUpdateStatus.OVERRIDDEN.equals(reservation.getUpdateStatus())
												&& order != null && order.getDeliveryInfo() != null 
																&& order.getDeliveryInfo().getPackagingDetail() != null
																	&& reservation.getMetricsSource() == null) {
						setReservationMetricsDetails(reservation.getId()
														, order.getDeliveryInfo().getPackagingDetail().getNoOfCartons()
														, order.getDeliveryInfo().getPackagingDetail().getNoOfCases()
														, order.getDeliveryInfo().getPackagingDetail().getNoOfFreezers()
														, order.getDeliveryInfo().getPackagingDetail().getSource());
					}
			}

		} catch (Exception e) {
			event = buildEvent(null, event, reservation,order,address, EventType.RESERVE_TIMESLOT, 0);
			if(event!=null && event.getId()!=null)
				logTimeslots(event);
			e.printStackTrace();
			LOGGER.debug("Exception in reserveTimeslotEx():"+e.toString());
			LOGGER.debug(reservation);
			setUnassignedInfo(reservation.getId(),RoutingActivityType.RESERVE_TIMESLOT);			
		}
		setRoutingIndicator(reservation.getId(), order.getOrderNumber());
		return _reservation;
	}

	private void doConfirmEx(DlvReservationModel reservation,ContactAddressModel address, TimeslotEventModel event) {

		long startTime=System.currentTimeMillis();
		IOrderModel order= RoutingUtil.getOrderModel(reservation, address, reservation.getOrderId(), reservation.getId());

		try {
            RoutingUtil.confirmTimeslot(reservation, order);
			long endTime=System.currentTimeMillis();
			event = buildEvent(null, event, reservation,order,address, EventType.CONFIRM_TIMESLOT,(int)(endTime-startTime));
			if(event!=null && event.getId()!=null)
				logTimeslots(event);
			if(reservation.isUnassigned()) {
				clearUnassignedInfo(reservation.getId());
			}

		} catch (Exception e) {
			event = buildEvent(null, event, reservation,order,address, EventType.CONFIRM_TIMESLOT,0);
			if(event!=null && event.getId()!=null)
				logTimeslots(event);
			e.printStackTrace();
			LOGGER.debug("Exception in commitReservationEx():"+order.getOrderNumber()+"-->"+e.toString());
			LOGGER.debug(reservation);
			setUnassignedInfo(reservation.getId(),RoutingActivityType.CONFIRM_TIMESLOT);
		}
	}



	private void doReleaseReservationEx(DlvReservationModel reservation,ContactAddressModel address, TimeslotEventModel event) {

		long startTime=System.currentTimeMillis();
		if(reservation==null || address==null ||!reservation.isInUPS())
			return ;

		IOrderModel order= RoutingUtil.getOrderModel(reservation, address, reservation.getOrderId(), reservation.getId());
		try {
            RoutingUtil.cancelTimeslot(reservation, order);
            long endTime=System.currentTimeMillis();
			event = buildEvent(null,event, reservation,order,address, EventType.CANCEL_TIMESLOT,(int)(endTime-startTime));
			if(event!=null && event.getId()!=null)
				logTimeslots(event);
			if(reservation.isUnassigned()) {
				clearUnassignedInfo(reservation.getId());
			}

		} catch (Exception e) {
			event = buildEvent(null,event, reservation,order,address, EventType.CANCEL_TIMESLOT,0);
			if(event!=null && event.getId()!=null)
				logTimeslots(event);
			e.printStackTrace();
			LOGGER.debug("Exception in releaseReservationEx():"+e.toString());
			LOGGER.debug(reservation);
			setUnassignedInfo(reservation.getId(),RoutingActivityType.CANCEL_TIMESLOT);
		}

	}

	public List getTimeslotsForDate(java.util.Date startDate) throws DlvResourceException, RemoteException {
		
		Connection conn = null;
		try {
			conn = this.getConnection();
			return DlvManagerDAO.getTimeslotsForDate(conn, startDate);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.warn("DlvManagerSB getTimeslotsForDate(): " + e);
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB getTimeslotsForDate(): Exception while cleaning: " + e);
			}
		}
	}
	
	public void purgeSchedulerByIdentity(IRoutingSchedulerIdentity schedulerId) throws DlvResourceException {
		
		RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
		try {
			proxy.purgeOrders(schedulerId, true);
			/*DeliveryServiceProxy dlvProxy = new DeliveryServiceProxy();
			List<IOrderModel> reservationForSchedulerId = dlvProxy.getRoutingOrderByDate(schedulerId.getDeliveryDate()
																							, schedulerId.getArea().getAreaCode()
																							, true);
			
			if(reservationForSchedulerId == null || reservationForSchedulerId.size() == 0) {
				LOGGER.info("DlvManagerSB purgeSchedulerByIdentity(): Purge: " + schedulerId);
				proxy.purgeOrders(schedulerId, true);
			} else {
				LOGGER.info("DlvManagerSB purgeSchedulerByIdentity(): Unable to Purge: "+schedulerId+"-->" + reservationForSchedulerId);
			}*/
		} catch (RoutingServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warn("DlvManagerSB purgeSchedulerByIdentity(): " + e);
			throw new DlvResourceException(e);
		}
	}
	
	public List<IDeliveryWindowMetrics> retrieveCapacityMetrics(IRoutingSchedulerIdentity schedulerId, List<IDeliverySlot> slots
																, boolean purge) 
																throws DlvResourceException, RemoteException {
		RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
		try {
			if(purge) {
				DeliveryServiceProxy dlvProxy = new DeliveryServiceProxy();
				List<IOrderModel> reservationForSchedulerId = dlvProxy.getRoutingOrderByDate(schedulerId.getDeliveryDate()
																								, schedulerId.getArea().getAreaCode()
																								, true);
				
				if(reservationForSchedulerId == null || reservationForSchedulerId.size() == 0) {
					LOGGER.info("DlvManagerSB retrieveCapacityMetrics(): Purge: " + schedulerId);
					proxy.purgeOrders(schedulerId, true);
				} else {
					LOGGER.info("DlvManagerSB retrieveCapacityMetrics(): Unable to Purge: "+schedulerId+"-->" + reservationForSchedulerId);
				}
			}
			return proxy.retrieveCapacityMetrics(schedulerId, slots);
		} catch (RoutingServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warn("DlvManagerSB retrieveCapacityMetrics(): " + e);
			throw new DlvResourceException(e);
		}
	}
	
	public int updateTimeslotsCapacity(List<DlvTimeslotModel> dlvTimeSlots ) throws DlvResourceException, RemoteException {
		
		Connection conn = null;
		try {
			conn = this.getConnection();
			return DlvManagerDAO.updateTimeslotsCapacity(conn, dlvTimeSlots);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.warn("DlvManagerSB updateTimeslotsCapacity(): " + e);
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB updateTimeslotsCapacity(): Exception while cleaning: " + e);
			}
		}
	}
	
	public int updateWaveInstanceStatus(List<IWaveInstance> waveInstances) throws DlvResourceException {
		
		Connection conn = null;
		try {
			conn = this.getConnection();
			return DlvManagerDAO.updateWaveInstanceStatus(conn, waveInstances);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.warn("DlvManagerSB updateWaveInstanceStatus(): " + e);
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB updateWaveInstanceStatus(): Exception while cleaning: " + e);
			}
		}
	}
	
	public void deleteZeroSyncWaveInstance(IRoutingSchedulerIdentity schedulerId) throws DlvResourceException {
		
		Connection conn = null;
		try {
			conn = this.getConnection();
			DlvManagerDAO.deleteZeroSyncWaveInstance(conn, schedulerId);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.warn("DlvManagerSB deleteZeroSyncWaveInstance(): " + e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB deleteZeroSyncWaveInstance(): Exception while cleaning: " + e);
			}
		}
	}
	
	public List<Date> getFutureTimeslotDates() throws DlvResourceException {
		
		Connection conn = null;
		try {
			conn = this.getConnection();
			return DlvManagerDAO.getFutureTimeslotDates(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			LOGGER.warn("DlvManagerSB getFutureTimeslotDates(): " + e);
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("DlvManagerSB getFutureTimeslotDates(): Exception while cleaning: " + e);
			}
		}
	}
	
	public List<IRoutingNotificationModel> retrieveNotifications() throws DlvResourceException {
		RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
		try {
			
			return proxy.retrieveNotifications();
		} catch (RoutingServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warn("DlvManagerSB retrieveNotifications(): " + e);
			throw new DlvResourceException(e);
		}
	}
	
	public Map<Date, Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>>> retrieveWaveInstanceTree(Date deliveryDate, EnumWaveInstanceStatus status) throws DlvResourceException {
		RoutingInfoServiceProxy routeInfoProxy = new RoutingInfoServiceProxy();
		try {
			
			return routeInfoProxy.getWaveInstanceTree(deliveryDate, status);
		} catch (RoutingServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warn("DlvManagerSB retrieveWaveInstanceTree(): " + e);
			throw new DlvResourceException(e);
		}
	}
	
	public Set<String> getInSyncWaveInstanceZones(Date deliveryDate) throws DlvResourceException {
		RoutingInfoServiceProxy routeInfoProxy = new RoutingInfoServiceProxy();
		try {
			
			return routeInfoProxy.getInSyncWaveInstanceZones(deliveryDate);
		} catch (RoutingServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.warn("DlvManagerSB getInSyncWaveInstanceZones(): " + e);
			throw new DlvResourceException(e);
		}
	}
	
	public void synchronizeWaveInstance(IRoutingSchedulerIdentity schedulerId
										, Map<Date, Map<String, Map<RoutingTimeOfDay, Map<RoutingTimeOfDay, List<IWaveInstance>>>>> waveInstanceTree
										, Set<String> inSyncZones, Map<String, TrnFacilityType> routingLocationMap) throws DlvResourceException {
		List<IWaveInstance> waveInstances = RoutingUtil.synchronizeWaveInstance(schedulerId, waveInstanceTree, inSyncZones, routingLocationMap);
		if(waveInstances != null && waveInstances.size() > 0) {
			updateWaveInstanceStatus(waveInstances);
			deleteZeroSyncWaveInstance(schedulerId);
		}
	}
	
	public void processCancelNotifications(List<IRoutingNotificationModel> notifications
												, List<IRoutingNotificationModel> unUsedNotifications) throws DlvResourceException {
		
		RoutingEngineServiceProxy proxy = new RoutingEngineServiceProxy();
		if(notifications != null) {
			try {
			
				for (IRoutingNotificationModel notification : notifications) {
					try {
						DlvReservationModel reservation = getReservation(notification.getOrderNumber());
						if(reservation != null) {
							if(reservation.getStatusCode() == 5 || reservation.getStatusCode() == 10) {
								setUnassignedInfo(reservation.getId(), RoutingActivityType.RESERVE_TIMESLOT);
							} else {
								this.clearUnassignedInfo(reservation.getId());
							}
						}
					} catch (ObjectNotFoundException objExp) {
						LOGGER.warn("Unable to find reservation for processing cancel notification for processCancelNotification(): " + notification.getOrderNumber());
						objExp.printStackTrace();
					}
				}
				if(notifications != null && notifications.size() > 0) {
					proxy.deleteNotifications(notifications);
				}
				
				if(unUsedNotifications != null && unUsedNotifications.size() > 0) {
					proxy.deleteNotifications(unUsedNotifications);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOGGER.warn("DlvManagerSB processCancelNotification(): " + e);
				throw new DlvResourceException(e);
			}
		}
	}
	
	public void cancelRoutingReservation(DlvReservationModel reservation, ContactAddressModel address, TimeslotEventModel event) {

		long startTime = System.currentTimeMillis();
		if(reservation == null || address == null || !reservation.isInUPS())
			return ;

		IOrderModel order= RoutingUtil.getOrderModel(reservation, address,reservation.getOrderId(),reservation.getId());
		try {
            RoutingUtil.cancelTimeslot(reservation, order);
			long endTime=System.currentTimeMillis();
			event = buildEvent(null, event, reservation,order,address, EventType.CANCEL_TIMESLOT,(int)(endTime-startTime));
			if(event!=null && event.getId()!=null)
				logTimeslots(event);
		} catch (Exception e) {
			event = buildEvent(null, event, reservation,order,address, EventType.CANCEL_TIMESLOT,0);
			if(event!=null && event.getId()!=null)
				logTimeslots(event);
			e.printStackTrace();
			LOGGER.debug("Exception in cancelRoutingReservation():"+ e.toString());
			LOGGER.debug(reservation);
		}

	}
	
	public List getActiveZoneCodes() throws RemoteException{
		Connection conn = null;
		try {
			conn = getConnection();
			return DlvManagerDAO.getActiveZoneCodes(conn);

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
	
	public List<DlvZoneModel> getActiveZones() throws RemoteException{
		Connection conn = null;
		try {
			conn = getConnection();
			return DlvManagerDAO.getActiveZones(conn);

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
	
	public List<GeographyRestriction> getGeographicDlvRestrictionsForReservation(AddressModel address) throws DlvResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			return DlvRestrictionDAO.getGeographicDlvRestrictionsForReservation(conn, address);

		} catch (SQLException e) {
			//this.getSessionContext().setRollbackOnly();
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
	
	public List<GeographyRestriction> getGeographicDlvRestrictionsForAvailable(AddressModel address)throws DlvResourceException{
		{
			Connection conn = null;
			try {
				conn = getConnection();
				return DlvRestrictionDAO.getGeographicDlvRestrictionsForAvailable(conn, address);

			} catch (SQLException e) {
				//this.getSessionContext().setRollbackOnly();
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
	}
	
	public void fixDisassociatedTimeslots()throws DlvResourceException{
	{
			Connection conn = null;
			try {
				conn = getConnection();
				DlvManagerDAO.fixDisassociatedTimeslots(conn);

			} catch (SQLException e) {
				//this.getSessionContext().setRollbackOnly();
				throw new DlvResourceException(e);
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException se) {
					LOGGER.warn("DlvManagerSB fixDisassociatedTimeslots: Exception while cleaning: " + se);
				}
			}
		}
	}
	
	public int blockTimeslotCapacity(Date sourceDate
			, Date cutoffDate, String[] area, Date startTime, Date endTime) throws DlvResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			return DlvManagerDAO.blockTimeslotCapacity(conn, sourceDate, cutoffDate, area, startTime, endTime);
		} catch (SQLException e) {			
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("DlvManagerSB fixDisassociatedTimeslots: Exception while cleaning: " + se);
			}
		}
	}
	
	public int unBlockTimeslotCapacity(Date sourceDate
			, Date cutoffDate, String[] area, Date startTime, Date endTime) throws DlvResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			return DlvManagerDAO.unBlockTimeslotCapacity(conn, sourceDate, cutoffDate, area, startTime, endTime);
		} catch (SQLException e) {			
			throw new DlvResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("DlvManagerSB fixDisassociatedTimeslots: Exception while cleaning: " + se);
			}
		}
	}

	public Map<String, TrnFacilityType> retrieveTrnFacilitys()throws RemoteException {
		Connection conn = null;
		try {
			conn = getConnection();
			return DlvManagerDAO.retrieveTrnFacilitys(conn);

		} catch (SQLException e) {
			throw new EJBException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException se) {
				LOGGER.warn("DlvManagerSB getTrnFacility: Exception while cleaning: " + se);
			}
		}
	}
	
	public SectorVO getSectorInfo(AddressModel address) {
		Connection conn = null;
		try {
			conn = getConnection();
			SectorVO neighbourhoodInfo = DlvManagerDAO.getSectorInfo(conn, address);
			
			return neighbourhoodInfo;

		} catch (SQLException sqle) {
			LOGGER.warn("Difficulty locating an address within a zone : " + sqle.getMessage());
			//throw new EJBException("Difficulty locating an address within a zone : " + sqle.getMessage());
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
		return null;
	}
	
	private final static String EBT_ZipCodeQuery = "select EBT_ACCEPTED from dlv.zipcode where zipcode = ?";
	private boolean checkEbtPaymentAccepted(Connection conn, String zipCode) throws SQLException {
		boolean isEBTAccepted = false;
		PreparedStatement ps = conn.prepareStatement(EBT_ZipCodeQuery);
		ps.setString(1, zipCode);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			isEBTAccepted = "X".equalsIgnoreCase(rs.getString("EBT_ACCEPTED"))?true:false;
		}
		rs.close();
		ps.close();

		return isEBTAccepted;
	}
}
