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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.ejb.EJBException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.pricing.EnumTaxationType;
import com.freshdirect.common.pricing.MunicipalityInfo;
import com.freshdirect.delivery.ReservationException;
import com.freshdirect.delivery.ReservationUnavailableException;
import com.freshdirect.delivery.announcement.EnumPlacement;
import com.freshdirect.delivery.announcement.EnumUserDeliveryStatus;
import com.freshdirect.delivery.announcement.EnumUserLevel;
import com.freshdirect.delivery.announcement.SiteAnnouncement;
import com.freshdirect.fdlogistics.exception.FDLogisticsServiceException;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.services.IAirclicService;
import com.freshdirect.fdlogistics.services.ILogisticsService;
import com.freshdirect.fdlogistics.services.helper.LogisticsDataDecoder;
import com.freshdirect.fdlogistics.services.helper.LogisticsDataEncoder;
import com.freshdirect.fdlogistics.services.impl.LogisticsServiceLocator;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.analytics.model.LateIssueOrder;
import com.freshdirect.logistics.analytics.model.TimeslotEvent;
import com.freshdirect.logistics.controller.data.Result;
import com.freshdirect.logistics.controller.data.request.UpdateOrderSizeRequest;
import com.freshdirect.logistics.controller.data.response.DeliveryReservations;
import com.freshdirect.logistics.delivery.dto.Customer;
import com.freshdirect.logistics.delivery.model.ActualOrderSizeInfo;
import com.freshdirect.logistics.delivery.model.DeliveryException;
import com.freshdirect.logistics.delivery.model.EnumApplicationException;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.logistics.delivery.model.OrderContext;
import com.freshdirect.logistics.delivery.model.SystemMessageList;
import com.freshdirect.logistics.fdstore.StateCounty;

public class DlvManagerSessionBean extends SessionBeanSupport {
	

	private static final long	serialVersionUID	= 1817746018911108166L;

	private static final Category LOGGER = LoggerFactory.getInstance(DlvManagerSessionBean.class);

	private final static ServiceLocator LOCATOR = new ServiceLocator();
	
	private static final String ORDERSIZE_FEED_TYPE = "ORDERSIZE_FEED";
	private static final String LATEORDER_FEED_TYPE = "LATEORDER_FEED";
	

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

	public void saveFutureZoneNotification(String email, String zip,String serviceType) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement("INSERT INTO CUST.ZONENOTIFICATION (EMAIL, ZIPCODE, SERVICE_TYPE, CREATE_DATE) VALUES (?, ?, ?, ?)");
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
            close(ps);
            close(conn);
		}
	}

	public List<SiteAnnouncement> getSiteAnnouncements() throws FDResourceException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = this.getConnection();
			

			ps = conn
					.prepareStatement("select * from CUST.site_announcements");
			rs = ps.executeQuery();
			List<SiteAnnouncement> lst = new ArrayList<SiteAnnouncement>();
			while (rs.next()) {
				String headline = rs.getString("HEADLINE");
				String copy = rs.getString("COPY");

				Date startDate = rs.getTimestamp("START_DATE");
				Date endDate = rs.getTimestamp("END_DATE");

				Date lastOrderBefore = rs.getTimestamp("LAST_ORDER_BEFORE");

				Set<EnumPlacement> placements = getPlacements(rs
						.getString("PLACEMENT"));
				Set<EnumUserLevel> userLevels = getUserLevels(
						"X".equals(rs.getString("ANONYMOUS")),
						"X".equals(rs.getString("REGISTERED")));
				Set<EnumUserDeliveryStatus> deliveryStatuses = getUserDlvStatus(rs
						.getString("USER_DLV_STATUS"));

				SiteAnnouncement ann = new SiteAnnouncement(headline, copy,
						startDate, endDate, placements, userLevels,
						deliveryStatuses, lastOrderBefore);

				lst.add(ann);
			}
			return lst;
		
			
		} catch (SQLException e) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(e);
		} finally {
            close(rs);
            close(ps);
            close(conn);
		}
	}
	

	private static Set<EnumUserLevel> getUserLevels(boolean anonymous,
			boolean registered) {
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




	private static String muniSql = "select * from CUST.municipality_info order by state, county desc, city desc";

	public List<MunicipalityInfo> getMunicipalityInfos() {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			List<MunicipalityInfo> muniList = new ArrayList<MunicipalityInfo>();
			conn = this.getConnection();
			ps = conn.prepareStatement(muniSql);
			rs = ps.executeQuery();
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
					alcoholRestricted,
					EnumTaxationType.getEnum(rs.getString("TAXATION_TYPE")));
				muniList.add(mi);
			}
			return (muniList.size() > 0 ? muniList : Collections.<MunicipalityInfo>emptyList() );
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FDRuntimeException(e);
		}  finally {
            close(rs);
            close(ps);
            close(conn);
		}

	}
	
	/**
	 * This method checks any delivery attempted SMS are to be sent and sends
	 * them to the respective customers.
	 * @throws FDResourceException 
	 * 
	 */
	public void sendOrderSizeFeed() throws FDResourceException {
		
		List<ActualOrderSizeInfo> data = new ArrayList<ActualOrderSizeInfo>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Date toTime = new Date();
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		
		try{
			conn = this.getConnection();
			Date fromTime = getLastExport(conn, ORDERSIZE_FEED_TYPE);
			
			ps = conn.prepareStatement("SELECT S.CUSTOMER_ID , S.ID , SAP_NUMBER, NUM_REGULAR_CARTONS, NUM_FREEZER_CARTONS, NUM_ALCOHOL_CARTONS, S.CROMOD_DATE " +
					"FROM CUST.SALE S, CUST.SALESACTION SA WHERE S.ID = SA.SALE_ID AND S.CUSTOMER_ID = SA.CUSTOMER_ID AND  S.CROMOD_DATE = SA.ACTION_DATE AND SA.ACTION_TYPE IN ('CRO','MOD') " +
					"AND S.TYPE = 'REG' AND S.E_STORE = 'FreshDirect' AND S.STATUS = 'STL' AND S.ID IN ( " +
					"SELECT SALE_ID FROM CUST.SALESACTION WHERE ACTION_TYPE = 'STL'  AND ACTION_DATE >= TO_DATE(?, 'mm/dd/yyyy hh:mi:ss am') AND " +
					"ACTION_DATE < TO_DATE(?, 'mm/dd/yyyy hh:mi:ss am') )");
			
			ps.setString(1, sdf.format(fromTime));
			ps.setString(2, sdf.format(toTime));
			rs = ps.executeQuery();
			while(rs.next()){
					ActualOrderSizeInfo info = new ActualOrderSizeInfo();
					info.setCustomerId(rs.getString("CUSTOMER_ID"));
					info.setOrderId(rs.getString("ID"));
					info.setErpOrderId(rs.getString("SAP_NUMBER"));
					info.setRegularCartons(rs.getLong("NUM_REGULAR_CARTONS"));
					info.setFreezerCartons(rs.getLong("NUM_FREEZER_CARTONS"));
					info.setAlcoholCartons(rs.getLong("NUM_ALCOHOL_CARTONS"));
					info.setInsertTimestamp(new java.util.Date(rs.getTimestamp("CROMOD_DATE").getTime()));
					data.add(info);
				
			}
		}catch(SQLException e){
			throw new FDResourceException(e);
		}
		finally {
            close(rs);
            close(ps);
            close(conn);
		}
		uploadOrderSizeFeed(data);
		updateLastExport(ORDERSIZE_FEED_TYPE, toTime);
	
	}
	
	public void logFailedFdxOrder(String orderId) throws FDResourceException {
		
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("INSERT INTO CUST.LOGISTICS_FDX_ORDER(ORDER_ID,SENT_TO_LOGISTICS)"
			+ " SELECT ?,? FROM DUAL  where NOT EXISTS (select * from CUST.LOGISTICS_FDX_ORDER where order_id=? and SENT_TO_LOGISTICS IS NULL)");
			ps.setString(1, orderId);
			ps.setString(2, "");
			ps.setString(3, orderId);
			ps.execute();
		} catch(SQLException e) {
			throw new FDResourceException(e);
		} finally {
            close(ps);
            close(conn);
		}
		
	
	}
	
	
	/**
	 * This method checks any delivery attempted SMS are to be sent and sends
	 * them to the respective customers.
	 * @throws FDResourceException 
	 * 
	 */
	public void sendLateOrderFeed() throws FDResourceException {
		
		List<String> data = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Date toTime = new Date();
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		
		try{
			conn = this.getConnection();
			Date fromTime = getLastExport(conn, LATEORDER_FEED_TYPE);
			
			ps = conn.prepareStatement("SELECT LO.SALE_ID FROM CUST.LATEISSUE L, CUST.LATEISSUE_ORDERS LO WHERE L.ID = LO.LATEISSUE_ID AND L.REPORTED_AT" +
					" BETWEEN to_date(?,'MM/DD/YYYY HH:MI:SS AM') AND to_date(?,'MM/DD/YYYY HH:MI:SS AM')");
			
			ps.setString(1, sdf.format(fromTime));
			ps.setString(2, sdf.format(toTime));
			rs = ps.executeQuery();
			while(rs.next()){
				data.add(rs.getString("SALE_ID"));
			}
			
		}catch(SQLException e){
			throw new FDResourceException(e);
		}
		finally {
            close(rs);
            close(ps);
            close(conn);
		}
		
		uploadLateOrderFeed(data);
		updateLastExport(LATEORDER_FEED_TYPE, toTime);
		
	
	}
	
	
	public void uploadOrderSizeFeed(List<ActualOrderSizeInfo> data) throws FDResourceException {
		
		try{
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			UpdateOrderSizeRequest request = new UpdateOrderSizeRequest();
			request.setData(data);
			Result response = logisticsService.uploadOrderSizeFeed(request);
			LogisticsDataDecoder.decodeResult(response);	
			
		} catch (FDLogisticsServiceException ex) {
			throw new FDResourceException(ex);
		}
	}

	
	
	public void uploadLateOrderFeed(List<String> data) throws FDResourceException {
		
		
		try{
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			LateIssueOrder lateIssueOrder = new LateIssueOrder();
			lateIssueOrder.setData(data);
			Result result = logisticsService.logLateIssueOrder(lateIssueOrder);
			LogisticsDataDecoder.decodeResult(result);
		}catch(FDLogisticsServiceException e){
			throw new FDResourceException(e);
		}
	
	}
	
	/**
	 * gets the last export for the given sms alert type.
	 * @throws DlvResourceException 
	 */
	private Date getLastExport(Connection con, String alertType) throws FDResourceException {
		Date lastExport=null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			ps = con.prepareStatement("SELECT NVL(MAX(LAST_EXPORT),SYSDATE-1/24) LAST_EXPORT FROM MIS.LOGISTICS_FEED_EXPORT "
					+ "WHERE SUCCESS= 'Y' and FEED_TYPE=?");
			ps.setString(1, alertType);
			rs = ps.executeQuery();
			if (rs.next()) {
				lastExport = new java.util.Date(rs.getTimestamp("LAST_EXPORT").getTime());
			}
		} catch(SQLException e) {
			throw new FDResourceException(e);
		} finally {
            close(rs);
            close(ps);
		}
		return lastExport;
	}
	
	/**
	 * updated the SMS_transit_export table for COrresponding alert type.
	 * @throws DlvResourceException 
	 */
	public void updateLastExport(String alertType, Date toTime) throws FDResourceException {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.getConnection();
			ps = conn.prepareStatement("INSERT INTO MIS.LOGISTICS_FEED_EXPORT(LAST_EXPORT, SUCCESS, FEED_TYPE) VALUES (?,'Y',?)");
			ps.setTimestamp(1, new java.sql.Timestamp(toTime.getTime()));
			ps.setString(2, alertType);
			ps.execute();
		} catch(SQLException e) {
			throw new FDResourceException(e);
		} finally {
            close(ps);
            close(conn);
		}
	}
	
	public FDReservation reserveTimeslot(String timeslotId, String customerId,
			EnumReservationType type, Customer customer,
			boolean chefsTable, String ctDeliveryProfile, boolean isForced,
			TimeslotEvent event, boolean hasSteeringDiscount, String deliveryFeeTier) throws FDResourceException, ReservationException{
	try {

		ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
		DeliveryReservations response = logisticsService.reserveTimeslot(LogisticsDataEncoder.encodeReserveTimeslotRequest(
				 timeslotId,
				 customerId,
				 type,
				 customer,
				 chefsTable,
				 ctDeliveryProfile,
				 isForced,  event, hasSteeringDiscount, deliveryFeeTier));
		
		if(response.getErrorCode() == EnumApplicationException.ReservationUnavailableException.getValue()){
			this.getSessionContext().setRollbackOnly();
			throw new ReservationUnavailableException(SystemMessageList.DELIVERY_SLOT_RSVERROR);
		}
		else if(response.getErrorCode() == EnumApplicationException.ReservationException.getValue()){
			this.getSessionContext().setRollbackOnly();
			throw new ReservationException(SystemMessageList.DELIVERY_SLOT_RSVERROR);
		}
		List<FDReservation> reservations = LogisticsDataDecoder.decodeReservations(response);
		
		if(reservations!=null && !reservations.isEmpty())
			return reservations.get(0);
		else
			throw new ReservationException(SystemMessageList.DELIVERY_SLOT_RSVERROR);
	}catch (FDResourceException ex) {
		this.getSessionContext().setRollbackOnly();
		throw ex;
	}catch (FDLogisticsServiceException ex) {
		this.getSessionContext().setRollbackOnly();
		throw new FDResourceException(ex);
	} 
	
	}
	
	public void commitReservation(String rsvId, String customerId,
			OrderContext context, ContactAddressModel address, boolean pr1,
			TimeslotEvent event) throws ReservationException, FDResourceException{

		try {

			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			Result response = logisticsService.confirmReservation(LogisticsDataEncoder.
					encodeConfirmReservationRequest(rsvId, customerId, context, address, pr1, event));
			if(response.getErrorCode() == EnumApplicationException.ReservationUnavailableException.getValue()){
				this.getSessionContext().setRollbackOnly();
				throw new ReservationUnavailableException(SystemMessageList.DELIVERY_SLOT_RSVERROR);
			}
			else if(response.getErrorCode() == EnumApplicationException.ReservationException.getValue()){
				this.getSessionContext().setRollbackOnly();
				throw new ReservationException(SystemMessageList.DELIVERY_SLOT_RSVERROR);
			}
			LogisticsDataDecoder.decodeResult(response);
		}catch (FDResourceException ex) {
			this.getSessionContext().setRollbackOnly();
			throw ex;
		}catch (FDLogisticsServiceException ex) {
			this.getSessionContext().setRollbackOnly();
			throw new FDResourceException(ex);
		}

	}
	
	public Set<StateCounty> getCountiesByState(String state) throws FDResourceException{

		try {
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			Map<String, Set<StateCounty>> scMap = logisticsService.getCountiesByState();
			Set<StateCounty> stateCounty = scMap.get(state);
			return stateCounty;
			
		}catch (FDLogisticsServiceException ex) {
			throw new FDResourceException(ex);
		}

	}

	public StateCounty lookupStateCountyByZip(String zipcode) throws FDResourceException{

		try {
			ILogisticsService logisticsService = LogisticsServiceLocator.getInstance().getLogisticsService();
			StateCounty sc = logisticsService.lookupStateCountyByZip(zipcode);
			return sc;
		}catch (FDLogisticsServiceException ex) {
			throw new FDResourceException(ex);
		}
	}
	
	public Map<String, DeliveryException> getCartonScanInfo()
			throws FDResourceException {

		Map<String, DeliveryException> response = new HashMap<String, DeliveryException>();
		try {

			IAirclicService airclicService = LogisticsServiceLocator
					.getInstance().getAirclicService();

			if (!ErpServicesProperties.isAirclicBlackhole()) {
				response = airclicService.getCartonScanInfo();
			}
			return response;

		} catch (FDLogisticsServiceException e) {
			throw new FDResourceException(e);
		}
	}
	
}
