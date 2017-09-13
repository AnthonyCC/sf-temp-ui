package com.freshdirect.fdlogistics.dao.impl;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.freshdirect.crm.CallLogModel;
import com.freshdirect.customer.EnumDeliverySetting;
import com.freshdirect.fdlogistics.dao.IOrderDAO;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.logistics.controller.data.request.OrderSearchCriteria;
import com.freshdirect.logistics.controller.data.response.DeliveryManifest;
import com.freshdirect.logistics.controller.data.response.DiscountTimeslot;
import com.freshdirect.logistics.customer.EnumProfileList;
import com.freshdirect.logistics.customer.EnumSaleStatus;
import com.freshdirect.logistics.delivery.dto.Address;
import com.freshdirect.logistics.delivery.dto.Customer;
import com.freshdirect.logistics.delivery.dto.OrderDTO;
import com.freshdirect.logistics.delivery.dto.OrderSummaryDTO;
import com.freshdirect.logistics.delivery.dto.OrdersDTO;
import com.freshdirect.logistics.delivery.dto.OrdersSummaryDTO;
import com.freshdirect.logistics.delivery.model.CartonInfo;
import com.freshdirect.logistics.delivery.model.DeliveryException;
import com.freshdirect.logistics.delivery.model.EnumDeliveryMenuOption;
import com.freshdirect.logistics.delivery.model.RouteStop;

@Component
public class OrderDAO extends BaseDAO implements IOrderDAO {

	// START HANDOFF QUERIES
	private static String GET_ORDERSBY_DATE_CUTOFF = "SELECT /*+ USE_NL(s, sa) */ c.id customer_id, fdc.id fdc_id, ci.first_name, ci.last_name, c.user_id, ci.home_phone, ci.business_phone, "
			+ "ci.cell_phone, fde.mobile_number, s.id weborder_id, s.sap_number erporder_id, sa.requested_date, s.status, sa.amount, di.starttime, di.endtime, "
			+ "di.cutofftime, di.zone ZONE, di.address1, di.address2, di.scrubbed_address, di.apartment, di.city, di.state, di.zip, di.country, di.delivery_type, di.reservation_id, handofftime "
			+ "from cust.customer c, cust.fdcustomer fdc, cust.fdcustomer_estore fde  "
			+ ", cust.customerinfo ci "
			+ ", cust.sale s, cust.salesaction sa "
			+ ", cust.deliveryinfo di "
			+ "where c.id = ci.customer_id and c.id = fdc.erp_customer_id and c.id = s.customer_id "
			+ "and s.id = sa.sale_id  and sa.CUSTOMER_ID = s.CUSTOMER_ID  and sa.requested_date = ? and s.cromod_date=sa.action_date and sa.action_type IN ('CRO', 'MOD') "
			+ "and s.type ='REG' "
			+ "and s.status <> 'CAN' "
			+ "and sa.id = di.salesaction_id and to_char(di.cutofftime, 'HH:MI AM') = to_char(?, 'HH:MI AM') and di.delivery_type <> 'X' and FDE.E_STORE = 'FreshDirect' and FDC.ID =FDE.FDCUSTOMER_ID";

	private static String GET_ORDER_BYID = " SELECT S.ID, S.SAP_NUMBER, SA.REQUESTED_DATE, S.STATUS, SA.AMOUNT, DI.STARTTIME, DI.ENDTIME, DI.CUTOFFTIME, DI.ZONE ZONE, DI.ADDRESS1, DI.ADDRESS2, " +
			"DI.APARTMENT, DI.CITY, DI.STATE, DI.ZIP, DI.COUNTRY, DI.DELIVERY_TYPE, DI.RESERVATION_ID, S.CUSTOMER_ID CUSTOMER_ID, DI.FIRST_NAME, DI.LAST_NAME, DI.PHONE HOME_PHONE, " +
			"DI.DELIVERY_INSTRUCTIONS, SA.SOURCE,P.CARD_TYPE,P.EWALLET_ID,DI.UNATTENDED_INSTR,DI.ALT_DEST,DI.ALT_FIRST_NAME ,DI.ALT_LAST_NAME,DI.ALT_APARTMENT,DI.ALT_PHONE,  " +
			"(select  cl.amount from cust.chargeline cl where CL.SALESACTION_ID = sa.id and cl.type='TIP' ) TIP, (select count(1) from CUST.ORDERLINE ol where OL.SALESACTION_ID = sa.id and OL.ALCOHOL = 'X') ALCOHOL" +
			" FROM CUST.SALE S, CUST.SALESACTION SA" +
			" , CUST.DELIVERYINFO DI,CUST.PAYMENTINFO P WHERE S.ID = SA.SALE_ID  AND SA.CUSTOMER_ID = S.CUSTOMER_ID AND S.CROMOD_DATE=SA.ACTION_DATE AND SA.ACTION_TYPE IN ('CRO', 'MOD') "+
			"  AND S.TYPE ='REG' AND SA.ID = DI.SALESACTION_ID AND S.ID =? AND SA.ID = P.SALESACTION_ID";

	private static String GET_ORDERSBY_DATE_CUTOFFSTANDBY = "SELECT /*+ USE_NL(s, sa) */ c.id customer_id, fdc.id fdc_id, ci.first_name, ci.last_name, c.user_id, ci.home_phone, ci.business_phone, "
			+ "ci.cell_phone, fde.mobile_number, s.id weborder_id, s.sap_number erporder_id, sa.requested_date, s.status, sa.amount, di.starttime, di.endtime, "
			+ "di.cutofftime, di.zone ZONE, di.address1, di.address2, di.apartment, di.city, di.state, di.zip, di.country, di.delivery_type, di.reservation_id "
			+ " "
			+ "from cust.customer@DBSTOSBY.NYC.FRESHDIRECT.COM c, cust.fdcustomer@DBSTOSBY.NYC.FRESHDIRECT.COM fdc "
			+ ", cust.customerinfo@DBSTOSBY.NYC.FRESHDIRECT.COM ci, cust.fdcustomer_estore@DBSTOSBY.NYC.FRESHDIRECT.COM fde "
			+ ", cust.sale@DBSTOSBY.NYC.FRESHDIRECT.COM s, cust.salesaction@DBSTOSBY.NYC.FRESHDIRECT.COM sa "
			+ ", cust.deliveryinfo@DBSTOSBY.NYC.FRESHDIRECT.COM di "
			+ "where c.id = ci.customer_id and c.id = fdc.erp_customer_id and c.id = s.customer_id "
			+ "and s.id = sa.sale_id  and sa.CUSTOMER_ID = s.CUSTOMER_ID  and sa.requested_date = ? and s.cromod_date=sa.action_date and sa.action_type IN ('CRO', 'MOD') "
			+ "and s.type ='REG' "
			+ "and s.status <> 'CAN' "
			+ "and sa.id = di.salesaction_id and to_char(di.cutofftime, 'HH:MI AM') = to_char(?, 'HH:MI AM') and di.delivery_type <> 'X' and FDE.E_STORE = 'FreshDirect' and FDC.ID =FDE.FDCUSTOMER_ID";

	private static String GET_ORDERSTATSBY_DATE_CUTOFF = "SELECT /*+ USE_NL(s, sa) */ s.status, count(*) as order_count "
			+ "from cust.customer c, cust.fdcustomer fdc "
			+ ", cust.customerinfo ci "
			+ ", cust.sale s, cust.salesaction sa "
			+ ", cust.deliveryinfo di "
			+ "where c.id = ci.customer_id and c.id = fdc.erp_customer_id and c.id = s.customer_id "
			+ "and s.id = sa.sale_id  and sa.CUSTOMER_ID = s.CUSTOMER_ID  and sa.requested_date = ? and s.cromod_date=sa.action_date and sa.action_type IN ('CRO', 'MOD') "
			+ "and s.type ='REG' "
			+ "and s.status <> 'CAN' "
			+ "and sa.id = di.salesaction_id and to_char(di.cutofftime, 'HH:MI AM') = to_char(?, 'HH:MI AM') and di.delivery_type <> 'X' group by s.status";

	private static String GET_ORDERSTATSBY_DATE_CUTOFFSTANDBY = "SELECT /*+ USE_NL(s, sa) */ s.status, count(*) as order_count "
			+ "from cust.customer@DBSTOSBY.NYC.FRESHDIRECT.COM c, cust.fdcustomer@DBSTOSBY.NYC.FRESHDIRECT.COM fdc "
			+ ", cust.customerinfo@DBSTOSBY.NYC.FRESHDIRECT.COM ci "
			+ ", cust.sale@DBSTOSBY.NYC.FRESHDIRECT.COM s, cust.salesaction@DBSTOSBY.NYC.FRESHDIRECT.COM sa "
			+ ", cust.deliveryinfo@DBSTOSBY.NYC.FRESHDIRECT.COM di "
			+ "where c.id = ci.customer_id and c.id = fdc.erp_customer_id and c.id = s.customer_id "
			+ "and s.id = sa.sale_id  and sa.CUSTOMER_ID = s.CUSTOMER_ID  and sa.requested_date = ? and s.cromod_date=sa.action_date and sa.action_type IN ('CRO', 'MOD') "
			+ "and s.type ='REG' "
			+ "and s.status <> 'CAN' "
			+ "and sa.id = di.salesaction_id and to_char(di.cutofftime, 'HH:MI AM') = to_char(?, 'HH:MI AM') and di.delivery_type <> 'X' group by s.status";

	// END HANDOFF QUERIES

	private static String GET_ORDER_BYCRITERIA = "SELECT "
			+ " c.id CUSTOMERID, ci.first_name FIRSTNAME, ci.last_name LASTNAME, c.user_id EMAIL, ci.home_phone, ci.business_phone, ci.business_ext, "
			+ " ci.cell_phone, s.id WEBORDER_ID, s.sap_number ERPORDER_ID, sa.requested_date DELIVERY_DATE, s.status STATUS, sa.amount, di.starttime, di.endtime, "
			+ " di.cutofftime CUTOFFTIME, di.zone AREA, di.delivery_type, di.company_name, di.charity_name, s.standingorder_id, di.reservation_id "
			+ " from cust.customer c, cust.customerinfo ci, cust.sale s, cust.salesaction sa, cust.deliveryinfo di "
			+ " where c.id = ci.customer_id and c.id = s.customer_id and s.id = sa.sale_id and sa.action_type IN ('CRO', 'MOD') "
			+ " and sa.customer_id = s.customer_id and sa.action_date = s.cromod_date and sa.id = di.salesaction_id "
			+ " and s.type ='REG' and s.status in ('AUT','AVE','SUB','AUF')  "
			+ " and sa.requested_date = ? and di.delivery_type <> 'X' ";

	private static String GET_ORDERS_BYDATE = "SELECT s.status, s.id "
			+ " from cust.sale s, cust.salesaction sa, cust.deliveryinfo di "
			+ " where s.id = sa.sale_id  and sa.CUSTOMER_ID = s.CUSTOMER_ID  and sa.requested_date = ? and s.cromod_date=sa.action_date and sa.action_type IN ('CRO', 'MOD') "
			+ " and s.type ='REG' and sa.id = di.salesaction_id and di.delivery_type <> 'X'";

	private static String GET_STANDINGORDER_BYCRITERIA = "select c.id CUSTOMERID, ci.first_name FIRSTNAME, ci.last_name LASTNAME, c.user_id EMAIL, s.id WEBORDER_ID, s.sap_number ERPORDER_ID, sa.requested_date DELIVERY_DATE, so.id STANDINGORDER_ID, di.zone AREA, s.status ORDER_STATUS, "
			+ " ci.home_phone, ci.business_phone, ci.business_ext, ci.cell_phone, di.starttime, di.endtime, di.delivery_type, di.company_name, di.charity_name,  "
			+ " (select count(*) from CUST.ORDERLINE o where O.SALESACTION_ID=sa.id) SALE_LINEITEMCOUNT, "
			+ " (select count(*) from CUST.CUSTOMERLIST_DETAILS x where X.LIST_ID=CL.ID) TEMPLATE_LINEITEMCOUNT "
			+ " from cust.customer c, cust.customerinfo ci, cust.sale s, cust.salesaction sa, cust.standing_order so, cust.customerlist cl, cust.deliveryinfo di "
			+ " where c.id = ci.customer_id and c.id = s.customer_id and s.id = sa.sale_id and sa.action_type IN ('CRO', 'MOD') and s.type='REG' and s.status in ('AUT', 'AVE', 'SUB', 'AUF') "
			+ " and sa.action_date = s.cromod_date and sa.id = di.salesaction_id and s.standingorder_id = so.id and so.customerlist_id = cl.id and sa.requested_date = ?  and di.delivery_type <> 'X'";

	private static final String GET_ACTIVEORDERS_BYAREA = "SELECT  di.zone AREA, di.starttime, di.endtime, count(di.zone) as ORDERCOUNT "
			+ " from cust.customer c, cust.fdcustomer fdc, cust.customerinfo ci, cust.sale s, cust.salesaction sa, cust.deliveryinfo di "
			+ " where c.id = ci.customer_id and c.id = fdc.erp_customer_id and c.id = s.customer_id and s.id = sa.sale_id and sa.action_type IN ('CRO', 'MOD') "
			+ " and s.type ='REG' and s.status in ('AUT', 'AVE', 'SUB', 'AUF') and sa.action_date = s.cromod_date "
			+ " and sa.id = di.salesaction_id and sa.requested_date = ? and di.delivery_type <> 'X' ";

	private static final String GET_CARTONS_BYORDER = "SELECT CARTON_NUMBER FROM CUST.CARTON_INFO WHERE SALE_ID = ?";
	
	private static final String GET_CARTONINFO_BYORDER = "SELECT SALE_ID, CARTON_NUMBER, SAP_NUMBER, CARTON_TYPE FROM CUST.CARTON_INFO WHERE SALE_ID = ?";
	
	private static final String GET_ORDERS_BY_DELIVERY_DATE = "SELECT s.id, s.sap_number, sa.requested_date, s.status, di.starttime, di.endtime,p.CARD_TYPE,p.EWALLET_ID, "
			+ "di.cutofftime, di.zone ZONE, di.address1, di.address2, di.apartment, di.city, di.state, di.zip, di.country, di.delivery_type, di.reservation_id, "
			+ "s.customer_id customer_id, di.first_name, di.last_name, di.phone home_phone, di.delivery_instructions, cl.amount "
			+ "from cust.sale s, cust.salesaction sa "
			+ ", cust.deliveryinfo di, cust.chargeline cl ,cust.PAYMENTINFO p "
			+ "where s.id = sa.sale_id  and sa.CUSTOMER_ID = s.CUSTOMER_ID and sa.id=cl.salesaction_id and s.cromod_date=sa.action_date and sa.action_type IN ('CRO', 'MOD') "
			+ "and s.type ='REG' and s.status NOT IN ('CAN') and sa.id = p.salesaction_id and cl.type='TIP' "
			+ "and sa.id = di.salesaction_id and sa.requested_date=?";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.freshdirect.fdlogistics.dao.impl.IOrderDAO#getOrderStatsByCutoff(
	 * java.util.Date, java.util.Date)
	 */
	@Override
	public Map<String, Integer> getOrderStatsByCutoff(final Date deliveryDate,
			final Date cutOff) {

		final Map<String, Integer> result = new HashMap<String, Integer>();

		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				String query = GET_ORDERSTATSBY_DATE_CUTOFF;
				/*
				 * if(RoutingServicesProperties.getRoutingCutOffStandByEnabled())
				 * { query = cutoffsbyQuery.toString(); }
				 */
				PreparedStatement ps = connection.prepareStatement(query);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				ps.setTimestamp(2, new Timestamp(cutOff.getTime()));
				return ps;
			}
		};

		jdbcTemplate.query(creator, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				do {
					result.put(rs.getString("status"), rs.getInt("order_count"));

				} while (rs.next());
			}
		});
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.freshdirect.fdlogistics.dao.impl.IOrderDAO#getOrderByCutoff(java.
	 * util.Date, java.util.Date)
	 */
	@Override
	public OrdersDTO getOrderByCutoff(final Date deliveryDate, final Date cutOff) {

		final OrdersDTO result = new OrdersDTO();

		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				String query = GET_ORDERSBY_DATE_CUTOFF;
				/*
				 * if(RoutingServicesProperties.getRoutingCutOffStandByEnabled())
				 * { // TODO is this property required? query =
				 * cutoffsbyQuery.toString(); }
				 */
				PreparedStatement ps = connection.prepareStatement(query);

				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				ps.setTimestamp(2, new Timestamp(cutOff.getTime()));
				return ps;
			}
		};

		jdbcTemplate.query(creator, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				do {

					OrderDTO infoModel = new OrderDTO();
					result.getOrders().add(infoModel);

					infoModel.setOrderNumber(rs.getString("weborder_id"));
					infoModel.setErpOrderNumber(rs.getString("erporder_id"));

					Customer customer = new Customer();
					customer.setCustomerId(rs.getString("customer_id"));
					customer.setMobilePhone(rs.getString("mobile_number"));

					infoModel.setCustomer(customer);
					infoModel.setOrderStatus(rs.getString("status"));
					infoModel.setDeliveryType(rs.getString("DELIVERY_TYPE"));

					// is_dynamic/ROUTING_START_TIME/ROUTING_END_TIME/NUM_CARTONS/NUM_CASES/NUM_FREEZERS/
					// are set in logistics.

					infoModel.setDeliveryDate(deliveryDate);
					infoModel.setStartTime(new Date(rs
							.getTimestamp("STARTTIME").getTime()));
					infoModel.setEndTime(new Date(rs.getTimestamp("ENDTIME")
							.getTime()));

					// infoModel.setDynamic("X".equalsIgnoreCase(rs.getString("is_dynamic")));

					// deliveryModel.setServiceType(rs.getString("DELIVERY_TYPE"));
					// deliveryModel.setDeliveryModel(tmpInputModel.getDeliveryModel());

					// IPackagingModel tmpPackageModel = new PackagingModel();
					// tmpPackageModel.setNoOfCartons(rs.getLong("NUM_CARTONS"));
					// tmpPackageModel.setNoOfCases(rs.getLong("NUM_CASES"));
					// tmpPackageModel.setNoOfFreezers(rs.getLong("NUM_FREEZERS"));
					// deliveryModel.setPackagingDetail(tmpPackageModel);

					// IZoneModel zoneModel = new ZoneModel();
					// zoneModel.setZoneNumber(rs.getString("ZONE"));
					// deliveryModel.setDeliveryZone(zoneModel);

					infoModel.setArea(rs.getString("ZONE"));

					// IBuildingModel bmodel = new BuildingModel();

					Address address = new Address();
					address.setAddress1(rs.getString("ADDRESS1"));
					address.setAddress2(rs.getString("ADDRESS2"));
					address.setCity(rs.getString("CITY"));
					address.setState(rs.getString("STATE"));
					address.setZipCode(rs.getString("ZIP"));
					address.setCountry(rs.getString("COUNTRY"));
					address.setApartment(rs.getString("APARTMENT"));
					address.setScrubbedStreet(rs.getString("SCRUBBED_ADDRESS"));

					// setting the service type in address
					address.setServiceType(rs.getString("DELIVERY_TYPE"));

					infoModel.setAddress(address);
					infoModel.setReservationId(rs.getString("RESERVATION_ID"));

				} while (rs.next());
			}
		});
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.freshdirect.fdlogistics.dao.impl.IOrderDAO#getActiveOrderByArea(java
	 * .util.Date, boolean)
	 */
	@Override
	public OrdersSummaryDTO getActiveOrderByArea(final Date deliveryDate, final boolean standingOrder) {
		
		final OrdersSummaryDTO result = new OrdersSummaryDTO();
		
		final StringBuffer updateQ = new StringBuffer();
		updateQ.append(GET_ACTIVEORDERS_BYAREA);
		if(standingOrder){
			updateQ.append(" and s.standingorder_id is not null ");
		} else {
			updateQ.append(" and s.standingorder_id is null ");
		}
		updateQ.append(" group by di.zone, di.starttime, di.endtime order by  di.zone, di.starttime asc ");
	
		PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {		            	 
					PreparedStatement ps =
						connection.prepareStatement(updateQ.toString());
					ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));		
					return ps;
				}  
			};
			
			jdbcTemplate.query(creator, 
					new RowCallbackHandler() { 
						public void processRow(ResultSet rs) throws SQLException {				    	
							do { 
								OrderSummaryDTO model = new OrderSummaryDTO();								
								model.setArea(rs.getString("AREA"));
								model.setStartTime(rs.getTimestamp("STARTTIME"));
								model.setEndTime(rs.getTimestamp("ENDTIME"));
								model.setOrderCount(rs.getInt("ORDERCOUNT"));
								result.getOrdersSummary().add(model);
							} while(rs.next());
						}
					}
			);
			
			return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.freshdirect.fdlogistics.dao.impl.IOrderDAO#getOrderByCriteria(java
	 * .util.Date, java.util.Date, java.lang.String[], java.lang.String,
	 * java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public OrdersDTO getOrderByCriteria(OrderSearchCriteria criteria) {
		
		final OrdersDTO result = new OrdersDTO();
		
		final StringBuffer updateQ = new StringBuffer();
		updateQ.append(GET_ORDER_BYCRITERIA);
		
		final Date deliveryDate = criteria.getDeliveryDate();
		final Date cutOffDateTime = criteria.getCutOffDateTime();
		final String[] area = criteria.getArea();
		final String startTime = criteria.getStartTime(); 
		final String endTime = criteria.getEndTime();
		final String[] deliveryType = criteria.getDeliveryType();
		final String profileName = criteria.getProfileName();
		
		
		orderSearchCriteria(cutOffDateTime, area, startTime, endTime,
				deliveryType, profileName, false, updateQ);
		
		
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {	
					PreparedStatement ps =
						connection.prepareStatement(updateQ.toString());
					ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
					int paramIndex = 2;
					if(cutOffDateTime != null)
							ps.setTimestamp(paramIndex++, new Timestamp(cutOffDateTime.getTime()));					
					if(startTime != null)				
							ps.setString(paramIndex++, startTime);						
					if(endTime != null) 
							ps.setString(paramIndex++, endTime);
						
					return ps;
				}  
			};
			
			jdbcTemplate.query(creator, 
					new RowCallbackHandler() { 
						public void processRow(ResultSet rs) throws SQLException {				    	
							do { 
								OrderDTO orderModel = new OrderDTO();	
								result.getOrders().add(orderModel);
																
								Customer custModel = new Customer();			
								orderModel.setCustomer(custModel);
								custModel.setFirstName(rs.getString("FIRSTNAME"));
								custModel.setLastName(rs.getString("LASTNAME"));
								custModel.setCustomerId(rs.getString("CUSTOMERID"));								
								custModel.setEmail(rs.getString("EMAIL"));
								custModel.setHomePhone(rs.getString("HOME_PHONE"));
								custModel.setBusinessPhone(rs.getString("BUSINESS_PHONE"));
								custModel.setBusinessExt(rs.getString("BUSINESS_EXT"));
								custModel.setCellPhone(rs.getString("CELL_PHONE"));
								custModel.setCompanyName(rs.getString("COMPANY_NAME") == null ? rs.getString("CHARITY_NAME") : rs.getString("COMPANY_NAME"));	
								
								orderModel.setOrderAmount(rs.getString("AMOUNT"));
								orderModel.setArea(rs.getString("AREA"));
								orderModel.setDeliveryDate(new java.util.Date(rs.getDate("DELIVERY_DATE").getTime()));
								orderModel.setCutOffTime(rs.getTimestamp("CUTOFFTIME"));
								orderModel.setStartTime(rs.getTimestamp("STARTTIME"));
								orderModel.setEndTime(rs.getTimestamp("ENDTIME"));
								
								orderModel.setErpOrderNumber(rs.getString("ERPORDER_ID"));
								orderModel.setOrderNumber(rs.getString("WEBORDER_ID"));
								orderModel.setOrderStatus(rs.getString("STATUS"));
								orderModel.setDeliveryType(rs.getString("DELIVERY_TYPE"));
								orderModel.setReservationId(rs.getString("reservation_id"));
								
							} while(rs.next());
						}
					}
			);
			return result;
			
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.freshdirect.fdlogistics.dao.impl.IOrderDAO#getOrderStatsByDate(java
	 * .util.Date)
	 */
	@Override
	public OrdersDTO getOrderStatsByDate(final Date deliveryDate) {

		final OrdersDTO result = new OrdersDTO();
		/*
		 * final StringBuffer updateQ = new StringBuffer();
		 * updateQ.append(GET_ORDERSTATSBY_DATE_BATCH);
		 * if(EnumCrisisMngBatchType.REGULARORDER.name().equals(batchType))
		 * updateQ.append(
		 * " and s.id in (select weborder_id from transp.CRISISMNG_BATCHORDER where batch_id = ?) group by s.status "
		 * ); else updateQ.append(
		 * " and s.id in (select weborder_id from transp.CRISISMNG_BATCHSTANDINGORDER where batch_id = ?) group by s.status "
		 * );
		 */
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection
						.prepareStatement(GET_ORDERS_BYDATE);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				// ps.setString(2, batchId);
				return ps;
			}
		};

		jdbcTemplate.query(creator, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				do {
					OrderDTO order = new OrderDTO();
					order.setOrderNumber(rs.getString("id"));
					order.setOrderStatus(rs.getString("status"));
					result.getOrders().add(order);
				} while (rs.next());
			}
		});
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.freshdirect.fdlogistics.dao.impl.IOrderDAO#getStandingOrderByCriteria
	 * (java.util.Date, java.util.Date, java.lang.String[], java.lang.String,
	 * java.lang.String, java.lang.String[], java.lang.String)
	 */
	@Override
	public OrdersDTO getStandingOrderByCriteria(
			final OrderSearchCriteria criteria) {

		final OrdersDTO result = new OrdersDTO();

		final StringBuffer updateQ = new StringBuffer();
		updateQ.append(GET_STANDINGORDER_BYCRITERIA);

		final Date deliveryDate = criteria.getDeliveryDate();
		final Date cutOffDateTime = criteria.getCutOffDateTime();
		final String[] area = criteria.getArea();
		final String startTime = criteria.getStartTime();
		final String endTime = criteria.getEndTime();
		final String[] deliveryType = criteria.getDeliveryType();
		final String profileName = criteria.getProfileName();

		orderSearchCriteria(cutOffDateTime, area, startTime, endTime,
				deliveryType, profileName, true, updateQ);

		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(updateQ
						.toString());
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				int paramIndex = 2;
				if (cutOffDateTime != null)
					ps.setTimestamp(paramIndex++,
							new Timestamp(cutOffDateTime.getTime()));
				if (startTime != null)
					ps.setString(paramIndex++, startTime);
				if (endTime != null)
					ps.setString(paramIndex++, endTime);

				return ps;
			}
		};

		jdbcTemplate.query(creator, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				do {
					OrderDTO soModel = new OrderDTO();
					result.getOrders().add(soModel);

					Customer custModel = new Customer();
					soModel.setCustomer(custModel);
					custModel.setFirstName(rs.getString("FIRSTNAME"));
					custModel.setLastName(rs.getString("LASTNAME"));
					custModel.setCustomerId(rs.getString("CUSTOMERID"));
					custModel.setEmail(rs.getString("EMAIL"));
					custModel.setHomePhone(rs.getString("HOME_PHONE"));
					custModel.setBusinessPhone(rs.getString("BUSINESS_PHONE"));
					custModel.setBusinessExt(rs.getString("BUSINESS_EXT"));
					custModel.setCellPhone(rs.getString("CELL_PHONE"));
					custModel
							.setCompanyName(rs.getString("COMPANY_NAME") == null ? rs
									.getString("CHARITY_NAME") : rs
									.getString("COMPANY_NAME"));

					soModel.setStandingOrderId(rs.getString("STANDINGORDER_ID"));
					soModel.setArea(rs.getString("AREA"));
					soModel.setOrderNumber(rs.getString("WEBORDER_ID"));
					soModel.setErpOrderNumber(rs.getString("ERPORDER_ID"));
					soModel.setStartTime(rs.getTimestamp("STARTTIME"));
					soModel.setEndTime(rs.getTimestamp("ENDTIME"));
					soModel.setOrderStatus(rs.getString("ORDER_STATUS"));
					soModel.setLineItemCount(rs.getInt("SALE_LINEITEMCOUNT"));
					soModel.setTempLineItemCount(rs
							.getInt("TEMPLATE_LINEITEMCOUNT"));

				} while (rs.next());
			}
		});
		return result;

	}

	private void orderSearchCriteria(final Date cutOffDateTime,
			final String[] area, final String startTime, final String endTime,
			final String[] deliveryType, String profileName,
			boolean standingOrder, final StringBuffer updateQ) {

		if (area != null && area.length > 0) {
			updateQ.append(" and di.zone in (");
			for (int intCount = 0; intCount < area.length; intCount++) {
				updateQ.append("'").append(area[intCount]).append("'");
				if (intCount < area.length - 1) {
					updateQ.append(",");
				}
			}
			updateQ.append(")");
		}

		if (deliveryType != null && deliveryType.length > 0) {
			updateQ.append(" and di.delivery_type in (");
			for (int intCount = 0; intCount < deliveryType.length; intCount++) {
				updateQ.append("'").append(deliveryType[intCount]).append("'");
				if (intCount < deliveryType.length - 1) {
					updateQ.append(",");
				}
			}
			updateQ.append(")");
		}

		if (cutOffDateTime != null) {
			updateQ.append(" and to_char(di.cutofftime, 'HH:MI AM') = to_char(?, 'HH:MI AM')");
		}
		if (startTime != null) {
			updateQ.append(" and to_date(to_char(di.starttime, 'HH:MI AM'), 'HH:MI AM') >= to_date(?, 'HH:MI AM')");
		}
		if (endTime != null) {
			updateQ.append(" and to_date(to_char(di.starttime, 'HH:MI AM'), 'HH:MI AM') < to_date(?, 'HH:MI AM')");
		}
		if (standingOrder) {
			updateQ.append(" and s.standingorder_id is not null ");
		} else {
			updateQ.append(" and s.standingorder_id is null ");
		}
		if (EnumProfileList.CHEFSTABLE.getName().equals(profileName)) {
			updateQ.append(" and exists (select 1 from cust.profile p, cust.fdcustomer fdc, cust.customer c "
					+ " where p.customer_id=fdc.ID and c.id=fdc.erp_customer_id "
					+ " and p.profile_name='ChefsTable' and p.profile_value='1' and c.id=ci.customer_id)");
		} else if (EnumProfileList.NONCHEFSTABLE.getName().equals(profileName)) {
			updateQ.append(" and not exists (select 1 from cust.profile p, cust.fdcustomer fdc, cust.customer c "
					+ " where p.customer_id=fdc.ID and c.id=fdc.erp_customer_id "
					+ " and p.profile_name='ChefsTable' and p.profile_value='1' and c.id=ci.customer_id)");
		}
	}

	@Override
	public OrderDTO getOrderById(final String orderId) {

		OrderDTO result = this.jdbcTemplate.queryForObject(GET_ORDER_BYID,
				new Object[] { orderId }, new RowMapper<OrderDTO>() {
					public OrderDTO mapRow(ResultSet rs, int rowNum)
							throws SQLException {

						OrderDTO result = new OrderDTO();

						Customer custModel = new Customer();
						result.setCustomer(custModel);
						custModel.setFirstName(rs.getString("FIRST_NAME"));
						custModel.setLastName(rs.getString("LAST_NAME"));
						custModel.setCustomerId(rs.getString("CUSTOMER_ID"));
						custModel.setHomePhone(rs.getString("HOME_PHONE"));

						result.setArea(rs.getString("ZONE"));
						result.setOrderNumber(rs.getString("ID"));
						result.setErpOrderNumber(rs.getString("SAP_NUMBER"));
						result.setStartTime(rs.getTimestamp("STARTTIME"));
						result.setEndTime(rs.getTimestamp("ENDTIME"));
						result.setCutOffTime(rs.getTimestamp("CUTOFFTIME"));
						result.setReservationId(rs.getString("RESERVATION_ID"));
						Address address = new Address();						
						result.setAddress(address);
						address.setAddress1(rs.getString("ADDRESS1"));
						address.setAddress2(rs.getString("ADDRESS2"));
						address.setApartment(rs.getString("APARTMENT"));
						address.setCity(rs.getString("CITY"));
						address.setState(rs.getString("STATE"));
						address.setZipCode(rs.getString("ZIP"));
						address.setDeliveryInstructions(rs.getString("DELIVERY_INSTRUCTIONS"));
						address.setUnattendedInstructions(rs.getString("UNATTENDED_INSTR"));
						result.setCardType(rs.getString("CARD_TYPE"));
						result.setEwalletID(rs.getString("EWALLET_ID"));
						result.setAlcohol(rs.getInt("ALCOHOL")>0?true:false);
						result.setETip(rs.getDouble("TIP"));
						String altDest=(rs.getString("ALT_DEST")!=null)?EnumDeliverySetting.getDeliverySetting(rs.getString("ALT_DEST")).getName():null;
						if(altDest!=null)
						{
						if(altDest.equalsIgnoreCase(EnumDeliverySetting.NEIGHBOR.getName()))
						{
						String altDeliveryInst=rs.getString("ALT_FIRST_NAME")
								+" "+rs.getString("ALT_LAST_NAME")+","+
						((rs.getString("ALT_APARTMENT")!=null)?(" "+rs.getString("ALT_APARTMENT")):"")
						+",Contact at:"+rs.getString("ALT_PHONE");
						address.setAltDeliveryInstructions(altDeliveryInst);
						}
						else
							address.setAltDeliveryInstructions(altDest);
						}
						result.setOrderStatus(EnumSaleStatus.getSaleStatus(
								rs.getString("STATUS")).getName());
						result.setSource(rs.getString("SOURCE"));
						

						return result;
					}
				}
		);
		return result;

	}

	@Override
	public List<String> getCartonList(final String orderId) {

		final List<String> cartonList = new ArrayList<String>();
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection
						.prepareStatement(GET_CARTONS_BYORDER);
				ps.setString(1, orderId);
				return ps;
			}
		};

		jdbcTemplate.query(creator, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				do {
					cartonList.add(rs.getString("CARTON_NUMBER"));
				} while (rs.next());
			}
		});
		return cartonList;

	}

	

	@Override
	public List<CartonInfo> getCartonInfo(final String orderId) {

		final List<CartonInfo> cartonList = new ArrayList<CartonInfo>();
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection
						.prepareStatement(GET_CARTONINFO_BYORDER);
				ps.setString(1, orderId);
				return ps;
			}
		};

		jdbcTemplate.query(creator, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				do {
					cartonList.add(new CartonInfo(rs.getString("SALE_ID"), rs.getString("SAP_NUMBER"), rs.getString("CARTON_NUMBER"), rs.getString("CARTON_TYPE")));
				} while (rs.next());
			}
		});
		return cartonList;

	}

	private static final String GET_ORDER_CALLLOG_QUERY = "select * from cust.ivr_calllog cl where cl.ordernumber = ? ";

	@Override
	public List<CallLogModel> getOrderCallLog(final String orderId) {

		final List<CallLogModel> calllogs = new ArrayList<CallLogModel>();

		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection
						.prepareStatement(GET_ORDER_CALLLOG_QUERY);
				ps.setString(1, orderId);
				return ps;
			}
		};

		jdbcTemplate.query(creator, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				do {
					CallLogModel model = new CallLogModel();
					model.setCallerGUIId(rs.getString("ID"));
					model.setCallerId(rs.getString("CALLERID"));
					model.setOrderNumber(rs.getString("ORDERNUMBER"));
					model.setStartTime(rs.getTimestamp("CALLTIME"));
					model.setDuration(rs.getInt("CALLDURATION"));
					model.setCallOutcome(rs.getString("CALL_OUTCOME"));
					model.setPhoneNumber(rs.getString("PHONE_NUMBER"));
					model.setMenuOption(rs.getString("MENU_OPTION") != null ? EnumDeliveryMenuOption
							.getEnum(rs.getString("MENU_OPTION")).getDesc()
							: "");
					model.setTalkTime(rs.getInt("TALKTIME"));

					calllogs.add(model);
				} while (rs.next());
			}
		});
		return calllogs;

	}

	private static final String GET_IVR_CALLLOG_QUERY = " select cl.ordernumber, cl.call_outcome from cust.ivr_calllog cl where cl.menu_option = 'EDR' "
			+ "and cl.INSERT_TIMESTAMP between to_date(?,'MM/DD/YYYY HH:MI:SS AM') and to_date(?,'MM/DD/YYYY HH:MI:SS AM')";

	@Override
	public Map<String, DeliveryException> getIVRCallLog(
			final Map<String, DeliveryException> result, final Date fromTime,
			final Date toTime) {

		final DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection
						.prepareStatement(GET_IVR_CALLLOG_QUERY);
				ps.setString(1, sdf.format(fromTime));
				ps.setString(2, sdf.format(toTime));

				return ps;
			}
		};

		jdbcTemplate.query(creator, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				do {
					String orderId = rs.getString("ORDERNUMBER");
					String callOutcome = rs.getString("CALL_OUTCOME");

					if (!StringUtils.isEmpty(orderId)) {
						if (!result.containsKey(orderId)) {
							DeliveryException model = new DeliveryException();
							model.setOrderId(orderId);
							result.put(orderId, model);
						}
						result.get(orderId).setEarlyDeliveryReq(true);
						result.get(orderId)
								.setEarlyDlvStatus(
										(null != callOutcome
												&& !"".equals(callOutcome)
												&& !"NoAnswer"
														.equals(callOutcome) && !"ReceiverRejected"
												.equals(callOutcome)) ? "Accepted"
												: "Rejected");
					}
				} while (rs.next());
			}
		});
		return result;

	}

	private static final String GET_DELIVERY_MANIFEST = "select di.last_name,di.first_name, di.delivery_instructions,di.unattended_instr, (select count(*) from cust.carton_info where sale_id=s.id) cartonCnt from "
			+ "cust.sale s, cust.salesaction sa, cust.deliveryinfo di where s.id=sa.sale_id and sa.id = di.salesaction_id and s.cromod_date=sa.action_date and sa.action_type IN ('CRO', 'MOD') and "
			+ "s.id = ?";

	@Override
	public DeliveryManifest getDeliveryManifest(final String orderId) {

		final DeliveryManifest result = new DeliveryManifest();
		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection
						.prepareStatement(GET_DELIVERY_MANIFEST);
				ps.setString(1, orderId);
				return ps;
			}
		};

		jdbcTemplate.query(creator, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				do {
					result.setFirstName(rs.getString("first_name"));
					result.setLastName(rs.getString("last_name"));
					result.setDeliveryInstructions(rs
							.getString("delivery_instructions"));
					
					result.setUattendedDeliveryInstructions(rs
							.getString("unattended_instr"));
					result.setCartonCnt(rs.getInt("cartonCnt"));
				} while (rs.next());
			}
		});
		return result;

	}

	private static final String GET_ALL_WINDOWSTEERING_DISCOUNTS = "SELECT p.id PID, "
			+ "P.MAX_AMOUNT PAMOUNT, "
			+ "P.OFFER_TYPE, "
			+ "z.DLV_ZONE ZONES, "
			+ "t.DAY_ID DAY_OF_WEEK, "
			+ "t.START_TIME STIME, "
			+ "t.END_TIME ETIME, "
			+ "t.DLV_WINDOWTYPE WINDOWTYPE, d.START_DATE "
			+ "FROM CUST.PROMOTION_NEW p, "
			+ "CUST.PROMO_DLV_ZONE_STRATEGY z, "
			+ "CUST.PROMO_DLV_TIMESLOT t, "
			+ "CUST.PROMO_DELIVERY_DATES d "
			+ "WHERE     P.ID = Z.PROMOTION_ID "
			+ "AND T.PROMO_DLV_ZONE_ID = Z.ID "
			+ "AND P.ID = D.PROMOTION_ID(+) "
			+ "AND p.campaign_code = 'HEADER' "
			+ "AND p.offer_type = 'WINDOW_STEERING' "
			+ "AND p.status = 'LIVE' "
			+ "AND P.EXPIRATION_DATE >= SYSDATE and P.EXPIRATION_DATE >= ? "
			+ "AND to_char(?,'D') = T.DAY_ID  "
			+ "AND (( d.START_DATE is NULL AND d.END_DATE is NULL) OR (d.START_DATE <= ? and d.END_DATE >= ?))";

	@Override
	public Map<String, List<DiscountTimeslot>> getWindowSteeringDiscounts(
			final Date deliveryDate) {

		final Map<String, List<DiscountTimeslot>> result = new HashMap<String, List<DiscountTimeslot>>();

		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection
						.prepareStatement(GET_ALL_WINDOWSTEERING_DISCOUNTS);
				ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));
				ps.setDate(2, new java.sql.Date(deliveryDate.getTime()));
				ps.setDate(3, new java.sql.Date(deliveryDate.getTime()));
				ps.setDate(4, new java.sql.Date(deliveryDate.getTime()));
				return ps;
			}
		};
		jdbcTemplate.query(creator, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {

				do {
					Array array = rs.getArray("ZONES");
					Array windowTypeArray = rs.getArray("WINDOWTYPE");

					DiscountTimeslot capacity = new DiscountTimeslot();
					if (rs.getString("STIME") != null)
						capacity.setStartTime(new TimeOfDay(rs
								.getString("STIME")).getNormalDate());
					if (rs.getString("ETIME") != null)
						capacity.setEndTime(new TimeOfDay(rs.getString("ETIME"))
								.getNormalDate());

					if (windowTypeArray != null) {
						String[] windowTypeDuration = (String[]) windowTypeArray
								.getArray();
						if (windowTypeDuration != null) {
							capacity.setWindowType(windowTypeDuration);
						}
					}

					if (array != null) {
						String[] zoneCodes = (String[]) array.getArray();
						if (zoneCodes != null) {
							for (String zone : zoneCodes) {
								if (!result.containsKey(zone)) {
									result.put(zone,
											new ArrayList<DiscountTimeslot>());
								}
								result.get(zone).add(capacity);
							}
						}
					}
				} while (rs.next());
			}
		});

		return result;
	}
	
	
	
	// ORDER QUERY
		private static String GET_ORDERS = "SELECT /*+ USE_NL(s, sa) */ S.ID WEBORDER_ID, S.SAP_NUMBER ERPORDER_ID, S.STATUS,S.TYPE, S.CROMOD_DATE, SA.REQUESTED_DATE, DI.DELIVERY_TYPE, " 
				+ "DI.STARTTIME, DI.ENDTIME, DI.CUTOFFTIME,  DI.HANDOFFTIME, DI.ZONE, DI.RESERVATION_ID, DI.ADDRESS1, DI.ADDRESS2, DI.APARTMENT, DI.CITY, DI.STATE, DI.ZIP, DI.COUNTRY, DI.SCRUBBED_ADDRESS FROM cust.sale s, cust.salesaction sa "
				+ ", cust.deliveryinfo di "
				+ "where s.id = sa.sale_id  and sa.CUSTOMER_ID = s.CUSTOMER_ID  and sa.requested_date >= trunc(sysdate) and s.cromod_date=sa.action_date and sa.action_type IN ('CRO', 'MOD') "
				+ "and s.type ='REG' "
				+ "and sa.id = di.salesaction_id";

		
	
	@Override
	public OrdersDTO getOrders(OrderSearchCriteria criteria) {

		final OrdersDTO result = new OrdersDTO();

		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				String query = GET_ORDERS;
				
				PreparedStatement ps = connection.prepareStatement(query);
				return ps;
			}
		};

		jdbcTemplate.query(creator, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				do {

					OrderDTO o = new OrderDTO();
					result.getOrders().add(o);

					o.setOrderNumber(rs.getString("weborder_id"));
					o.setErpOrderNumber(rs.getString("erporder_id"));
					o.setOrderStatus(rs.getString("status"));
					o.setOrderType(rs.getString("TYPE"));
					o.setCreateModifiedDate(new Date(rs.getTimestamp("CROMOD_DATE").getTime()));
					o.setDeliveryDate(new Date(rs.getTimestamp("REQUESTED_DATE").getTime()));
					o.setDeliveryType(rs.getString("DELIVERY_TYPE"));
					
					o.setStartTime(new Date(rs
							.getTimestamp("STARTTIME").getTime()));
					o.setEndTime(new Date(rs.getTimestamp("ENDTIME")
							.getTime()));
					o.setCutOffTime(new Date(rs.getTimestamp("CUTOFFTIME")
							.getTime()));
					o.setHandoffTime(new Date(rs.getTimestamp("HANDOFFTIME")
							.getTime()));
					


					o.setArea(rs.getString("ZONE"));

					Address address = new Address();
					address.setAddress1(rs.getString("ADDRESS1"));
					address.setAddress2(rs.getString("ADDRESS2"));
					address.setCity(rs.getString("CITY"));
					address.setState(rs.getString("STATE"));
					address.setZipCode(rs.getString("ZIP"));
					address.setCountry(rs.getString("COUNTRY"));
					address.setApartment(rs.getString("APARTMENT"));

					o.setAddress(address);
					o.setReservationId(rs.getString("RESERVATION_ID"));

				} while (rs.next());
			}
		});
		return result;
	}

	private static String GET_DELIVERY_REQ_QUERY = " select menu_option, call_outcome, calltime from cust.ivr_calllog where ordernumber = ? and menu_option in ('EDR','DAR')  ";

	
	public List<CallLogModel> getDeliveryReq(final String orderId){
		
		
		final List<CallLogModel> modelList = new ArrayList<CallLogModel>();

		PreparedStatementCreator creator = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				String query = GET_DELIVERY_REQ_QUERY;
				
				
				PreparedStatement ps = connection.prepareStatement(query);
				ps.setString(1, orderId);
				return ps;
			}
		};

		jdbcTemplate.query(creator, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				do {
					

					String requestType = rs.getString("MENU_OPTION");
					String callOutcome = rs.getString("CALL_OUTCOME");
					Date callTime = rs.getTimestamp("CALLTIME");
					CallLogModel model = new CallLogModel();
					model.setMenuOption(requestType);
					model.setCallOutcome(callOutcome);
					model.setStartTime(callTime);
					modelList.add(model);
				
				} while (rs.next());
			}
		});

		return modelList;
	}

	private static final String SAVE_CARTONINFO = "INSERT INTO CUST.CARTON_INFO(SALE_ID, SAP_NUMBER, CARTON_NUMBER, CARTON_TYPE) VALUES (?,?,?,?)";
	
	@Override
	public void saveCartonInfo(final List<CartonInfo> data) {
		if (data == null || data.isEmpty()) {
			return;
		}
		jdbcTemplate.batchUpdate(SAVE_CARTONINFO, new BatchPreparedStatementSetter() {
			 
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				CartonInfo cartonInfo = data.get(i);
				ps.setString(1, cartonInfo.getOrderId());
				ps.setString(2, cartonInfo.getErpOrderId());
				ps.setString(3, cartonInfo.getCartonNumber());
				ps.setString(4, cartonInfo.getCartonType());
			}
		 
			@Override
			public int getBatchSize() {
				return data.size();
			}
		});	
	}
	
	@Override	
	public OrdersDTO getOrdersByDeliveryDate(final Date deliveryDate) {
		
		
		final OrdersDTO result = new OrdersDTO();
		
		final StringBuffer query = new StringBuffer();
		query.append(GET_ORDERS_BY_DELIVERY_DATE);										
		
		
			PreparedStatementCreator creator = new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {	
					PreparedStatement ps =
						connection.prepareStatement(query.toString());
					ps.setDate(1, new java.sql.Date(deliveryDate.getTime()));					
						
					return ps;
				}  
			};
			
			jdbcTemplate.query(creator, 
					new RowCallbackHandler() { 
						public void processRow(ResultSet rs) throws SQLException {				    	
							do { 
								OrderDTO orderModel = new OrderDTO();	
								result.getOrders().add(orderModel);
																
								Customer custModel = new Customer();			
								orderModel.setCustomer(custModel);
								custModel.setFirstName(rs.getString("FIRST_NAME"));
								custModel.setLastName(rs.getString("LAST_NAME"));
								custModel.setCustomerId(rs.getString("customer_id"));								
								custModel.setHomePhone(rs.getString("HOME_PHONE"));
							  
								orderModel.setArea(rs.getString("ZONE"));
								orderModel.setDeliveryDate(new java.util.Date(rs.getDate("requested_date").getTime()));
								orderModel.setCutOffTime(rs.getTimestamp("CUTOFFTIME"));
								orderModel.setStartTime(rs.getTimestamp("STARTTIME"));
								orderModel.setEndTime(rs.getTimestamp("ENDTIME"));
								orderModel.setErpOrderNumber(rs.getString("sap_number"));
								orderModel.setOrderNumber(rs.getString("id"));
								orderModel.setOrderStatus(rs.getString("STATUS"));
								orderModel.setDeliveryType(rs.getString("DELIVERY_TYPE"));
								orderModel.setReservationId(rs.getString("reservation_id"));
								orderModel.setETip(rs.getDouble("amount"));
								orderModel.setCardType(rs.getString("CARD_TYPE"));
								orderModel.setEwalletID(rs.getString("EWALLET_ID"));
								
							} while(rs.next());
						}
					}
			);
			return result;
			
	}

	
	private static final String SAVE_ROUTESTOPINFO = "UPDATE CUST.SALE SET TRUCK_NUMBER =LPAD(?, 6, '0'), STOP_SEQUENCE = LPAD(?, 5, '0') WHERE ID = ?";
	
	@Override
	public void saveRouteStopInfo(final List<RouteStop> data) {
		if (data == null || data.isEmpty()) {
			return;
		}
		jdbcTemplate.batchUpdate(SAVE_ROUTESTOPINFO, new BatchPreparedStatementSetter() {
			 
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				RouteStop routeInfo = data.get(i);
				ps.setString(1, routeInfo.getRoute());
				ps.setString(2, routeInfo.getStop());
				ps.setString(3, routeInfo.getOrderId());
			}
		 
			@Override
			public int getBatchSize() {
				return data.size();
			}
		});	
	}

	

}
 