/*
 * $Workfile:CallCenterManagerSessionBean.java$
 *
 * $Date:8/27/2003 12:11:49 PM$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.customer.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.mail.MessagingException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.crm.CrmCaseOrigin;
import com.freshdirect.crm.CrmOrderStatusReportLine;
import com.freshdirect.crm.CrmSettlementProblemReportLine;
import com.freshdirect.crm.ejb.CriteriaBuilder;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.customer.EnumComplaintStatus;
import com.freshdirect.customer.EnumComplaintType;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpChargeLineModel;
import com.freshdirect.customer.ErpComplaintException;
import com.freshdirect.customer.ErpInvoiceLineI;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpRedeliveryModel;
import com.freshdirect.customer.ErpReturnLineModel;
import com.freshdirect.customer.ErpReturnOrderModel;
import com.freshdirect.customer.ErpSaleModel;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.ejb.ErpComplaintManagerHome;
import com.freshdirect.customer.ejb.ErpComplaintManagerSB;
import com.freshdirect.customer.ejb.ErpCustomerManagerHome;
import com.freshdirect.customer.ejb.ErpCustomerManagerSB;
import com.freshdirect.customer.ejb.ErpLogActivityCommand;
import com.freshdirect.delivery.ejb.DlvManagerDAO;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.deliverypass.DlvPassConstants;
import com.freshdirect.deliverypass.EnumDlvPassExtendReason;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.deliverypass.ejb.DlvPassManagerHome;
import com.freshdirect.deliverypass.ejb.DlvPassManagerSB;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.meal.MealModel;
import com.freshdirect.fdstore.content.meal.ejb.MealPersistentBean;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDAuthInfo;
import com.freshdirect.fdstore.customer.FDAuthInfoSearchCriteria;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDComplaintInfo;
import com.freshdirect.fdstore.customer.FDComplaintReportCriteria;
import com.freshdirect.fdstore.customer.FDCreditSummary;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDCustomerOrderInfo;
import com.freshdirect.fdstore.customer.FDCustomerReservationInfo;
import com.freshdirect.fdstore.customer.FDCutoffTimeInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.LateDlvReportLine;
import com.freshdirect.fdstore.customer.MakeGoodOrderInfo;
import com.freshdirect.fdstore.customer.RouteStopReportLine;
import com.freshdirect.fdstore.customer.SubjectReportLine;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.GenericSearchCriteria;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.payment.EnumBankAccountType;
import com.freshdirect.payment.EnumPaymentMethodType;

/**
 *
 *
 * @version $Revision:25$
 * @author $Author:Mike Rose$
 */
public class CallCenterManagerSessionBean extends SessionBeanSupport {

	private final static Category LOGGER = LoggerFactory.getInstance(CallCenterManagerSessionBean.class);

	private final static ServiceLocator LOCATOR = new ServiceLocator();

	public Map getComplaintReasons(boolean excludeCartonReq) throws FDResourceException {
		try {
			ErpComplaintManagerSB complaintSB = this.getComplaintManagerHome().create();
			return complaintSB.getReasons(excludeCartonReq);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
	}

	public Map getComplaintCodes() throws FDResourceException {
		try {
			ErpComplaintManagerSB complaintSB = this.getComplaintManagerHome().create();
			return complaintSB.getComplaintCodes();
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
	}
	
	public void rejectMakegoodComplaint(String makegood_sale_id) throws FDResourceException {
		try {
			ErpComplaintManagerSB complaintSB = this.getComplaintManagerHome().create();
			complaintSB.rejectMakegoodComplaint(makegood_sale_id);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
	}
	private static final String PEN_COMPLAINT_QUERY_1 = "select c.sale_id, c.id as complaint_id, c.amount as complaint_amount, "
		+ "c.note as complaint_note, c.complaint_type "
		+ "from cust.complaint c "
		+ "where c.status = 'PEN' ";

	private static final String PEN_COMPLAINT_FILTER_QUERY = "and exists (select * from cust.complaintline cl where cl.complaint_id=c.id and "
		+ "complaint_dept_code_id in (select id from cust.complaint_dept_code where comp_code=?))";

	private static final String PEN_COMPLAINT_QUERY_2 = "select s.id, s.status,s.type, sa.requested_date, ci.first_name, ci.last_name, ci.email, (select max(amount) from cust.salesaction where sale_id=s.id and action_type in ('CRO','MOD','INV') "
		+ "and action_date = (select max(action_date) from cust.salesaction where action_type in ('CRO','MOD','INV') and sale_id = s.id)) as order_amount "
		+ "from cust.sale s, cust.salesaction sa, cust.customerinfo ci "
		+ "where sa.sale_id in ( ? ) and sa.action_type in ('CRO', 'MOD') "
		+ "and sa.action_date = (select max(action_date) from cust.salesaction where sale_id = sa.sale_id and action_type in ('CRO', 'MOD')) "
		+ "and s.id = sa.sale_id and s.customer_id = ci.customer_id ";

	private String substitute(String original, char marker, String replaceWith) {
		StringBuffer sb = new StringBuffer(original);
		int pos = original.indexOf(marker);
		sb.replace(pos, pos + 1, replaceWith);
		return sb.toString();
	}

	private static final Comparator COMPLAINTINFO_COMPARATOR = new Comparator() {
		public int compare(Object o1, Object o2) {
			FDComplaintInfo info1 = (FDComplaintInfo) o1;
			FDComplaintInfo info2 = (FDComplaintInfo) o2;
			return info1.getDeliveryDate().compareTo(info2.getDeliveryDate());
		}
	};

	public List getPendingComplaintOrders(String reasonCode) throws FDResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			String complaintQuery = PEN_COMPLAINT_QUERY_1;
			if (reasonCode!=null && !"".equals(reasonCode)) {
				complaintQuery += PEN_COMPLAINT_FILTER_QUERY;
			}

			PreparedStatement ps = conn.prepareStatement(complaintQuery);
			if (reasonCode!=null && !"".equals(reasonCode)) {
				ps.setString(1,reasonCode);
			}

			ResultSet rs = ps.executeQuery();
			Map infoMap = new HashMap();
			StringBuffer saleIds = new StringBuffer();

			int idCount = 0; // !!! this is just to work around the limitation of 1000 in clause in SQL
			while (rs.next() && idCount <= 900) {
				String saleId = rs.getString("SALE_ID");
				if (idCount > 0) {
					saleIds.append(",");
				}
				saleIds.append("'").append(saleId).append("'");
				FDComplaintInfo info = new FDComplaintInfo(saleId);
				info.setComplaintId(rs.getString("COMPLAINT_ID"));
				info.setComplaintAmount(rs.getDouble("COMPLAINT_AMOUNT"));
				info.setComplaintType(EnumComplaintType.getEnum(NVL.apply(rs.getString("COMPLAINT_TYPE"), "FDC")).getName());
				info.setComplaintNote(rs.getString("COMPLAINT_NOTE"));

				infoMap.put(saleId, info);
				idCount++;
			}

			rs.close();
			ps.close();
			if (infoMap.isEmpty()) {
				return Collections.EMPTY_LIST;
			}

			ps = conn.prepareStatement(this.substitute(PEN_COMPLAINT_QUERY_2, '?', saleIds.toString()));
			rs = ps.executeQuery();

			while (rs.next()) {
				String saleId = rs.getString("ID");
				FDComplaintInfo info = (FDComplaintInfo) infoMap.get(saleId);
				info.setSaleStatus(EnumSaleStatus.getSaleStatus(rs.getString("STATUS")));
				info.setOrderAmount(rs.getDouble("ORDER_AMOUNT"));
				info.setDeliveryDate(rs.getDate("REQUESTED_DATE"));
				info.setEmail(rs.getString("EMAIL"));
				info.setFirstName(rs.getString("FIRST_NAME"));
				info.setLastName(rs.getString("LAST_NAME"));
				info.setOrderType(rs.getString("TYPE"));
			}

			rs.close();
			ps.close();

			List complaintInfos = new ArrayList(infoMap.values());
			Collections.sort(complaintInfos, COMPLAINTINFO_COMPARATOR);
			return complaintInfos;

		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage());
			throw new FDResourceException(sqle);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqle) {
					LOGGER.debug("Error while cleaning:", sqle);
				}
			}
		}
	}

	public List locateCompanyCustomers(GenericSearchCriteria criteria) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			return GenericSearchDAO.genericSearch(conn, criteria);
		} catch (SQLException e) {
			throw new FDResourceException(e, "Counld not find customers matching criteria entered.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Trouble closing connection after locateCustomers", e);
				}
			}
		}
	}

	public List orderSummarySearch(GenericSearchCriteria criteria) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			return GenericSearchDAO.genericSearch(conn, criteria);
		} catch (SQLException e) {
			throw new FDResourceException(e, "Counld not find customers matching criteria entered.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Trouble closing connection after locateCustomers", e);
				}
			}
		}
	}

	private String getComplaintType (int fdcCount, int cshCount){
		if (fdcCount > 0 && cshCount > 0) {
			return "MIXED";
		} else if (cshCount > 0) {
			return "Refund";
		} else {
			return "Store Credit";
		}
	}

	private static final String COMPLAINT_REPORT = "select c.amount , c.status, c.created_by, c.approved_by, c.sale_id, ci.first_name, ci.last_name, "
		+"(select count(1) from cust.complaintline xc where xc.method ='CSH' and xc.complaint_id =c.id) COUNT_CSH, "
		+"(select count(1) from cust.complaintline yc where yc.method ='FDC' and yc.complaint_id =c.id) COUNT_FDC "
		+"from cust.complaint c, cust.sale s, cust.customerinfo ci "
		+"where c.sale_id = s.id and s.customer_id = ci.customer_id ";

	public List runComplaintReport(FDComplaintReportCriteria criteria) throws FDResourceException {
		if (criteria.isBlank()) {
			return Collections.EMPTY_LIST;
		}
		CriteriaBuilder builder = new CriteriaBuilder();

		Date d = criteria.getStartDate();
		if(d != null){
			builder.addSql("c.create_date >= ?", new Object[] { new java.sql.Date(d.getTime())});
		}
		d = criteria.getEndDate();
		if(d != null){
			builder.addSql("c.create_date < ?", new Object[] { new java.sql.Date(d.getTime())});
		}
		String value = criteria.getIssuedBy();
		if (!"".equals(value)) {
			builder.addString("c.created_by", value);
		}
		value = criteria.getApprovedBy();
		if(!"".equals(value)){
			builder.addString("c.approved_by", value);
		}

		Connection conn = null;
		try{
			conn = this.getConnection();
			PreparedStatement ps = conn.prepareStatement(COMPLAINT_REPORT + " and " + builder.getCriteria());
			Object[] par = builder.getParams();
			for (int i = 0; i < par.length; i++) {
				LOGGER.debug("Setting param[" + par[i] + "] at position[" + i + "]");
				ps.setObject(i + 1, par[i]);
			}
			ResultSet rs = ps.executeQuery();
			List l = new ArrayList();
			while(rs.next()){
				String saleId = rs.getString("SALE_ID");
				FDComplaintInfo info = new FDComplaintInfo(saleId);
				info.setComplaintAmount(rs.getDouble("AMOUNT"));
				info.setComplaintStatus(EnumComplaintStatus.getComplaintStatus(rs.getString("STATUS")));
				info.setIssuedBy(rs.getString("CREATED_BY"));
				info.setApprovedBy(rs.getString("APPROVED_BY"));
				info.setFirstName(rs.getString("FIRST_NAME"));
				info.setLastName(rs.getString("LAST_NAME"));
				info.setComplaintType(this.getComplaintType(rs.getInt("COUNT_FDC"), rs.getInt("COUNT_CSH")));

				l.add(info);
			}

			return l;

		} catch (SQLException e){
			throw new FDResourceException(e);
		} finally {
			if(conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					LOGGER.debug("SQLException while closing connection: ", e);
				}
			}
		}
	}

	public static String authInfoSearchQuery =
		"select distinct sa1.sale_id, sa1.requested_date, sa.amount, s.status, s.type, sa1.action_date, ci.first_name, ci.last_name, pi.name, p.description, p.auth_code, pi.card_type, p.ccnum_last4, pi.payment_method_type, pi.aba_route_number, pi.bank_account_type "
		+"from (select sale_id, amount from cust.salesaction where "
		+"action_date >= to_date(?) - 10 "
		+"and action_date < to_date(?) + 10 "
		+"and action_type = 'CAP' and amount = ?) sa, "
		+"cust.salesaction sa1, cust.salesaction sa2, cust.paymentinfo pi, cust.sale s, cust.customerinfo ci, cust.payment p "
		+"where sa.sale_id = sa1.sale_id and sa.sale_id = sa2.sale_id and sa.sale_id = s.id and ci.customer_id = s.customer_id and sa1.action_type in ('CRO', 'MOD') and sa2.action_type = 'AUT' and  p.salesaction_id = sa2.id "
		+"and sa1.id = pi.salesaction_id and pi.account_number like ?";

	public List runAuthInfoSearch(FDAuthInfoSearchCriteria criteria) throws FDResourceException {
		if(criteria.isBlank()){
			return Collections.EMPTY_LIST;
		}

		Connection conn = null;
		try{
			conn = this.getConnection();
			String sql = authInfoSearchQuery;
			if (criteria.getCardType() != null) {
				sql += " and pi.card_type = ?";
			}
			if (criteria.getPaymentMethodType() != null) {
				sql += " and pi.payment_method_type = ?";
			}
			if (criteria.getAbaRouteNumber() != null) {
				sql += " and pi.aba_route_number = ?";
			}
			if (criteria.getBankAccountType() != null) {
				sql += " and pi.bank_account_type = ?";
			}

			PreparedStatement ps = conn.prepareStatement(sql);
			int index = 1;
			ps.setDate(index++, new java.sql.Date(criteria.getTransactionDate().getTime()));
			ps.setDate(index++, new java.sql.Date(criteria.getTransactionDate().getTime()));
			ps.setDouble(index++, criteria.getChargedAmount());
			ps.setString(index++, criteria.getCCKnownNum().replace('*', '%'));
			if (criteria.getCardType() != null) {
				ps.setString(index++, criteria.getCardType().getFdName());
			}
			if (criteria.getPaymentMethodType() != null) {
				ps.setString(index++, criteria.getPaymentMethodType().getName());
			}
			if (criteria.getAbaRouteNumber() != null) {
				ps.setString(index++, criteria.getAbaRouteNumber());
			}
			if (criteria.getBankAccountType() != null) {
				ps.setString(index++, criteria.getBankAccountType().getName());
			}
			ResultSet rs = ps.executeQuery();
			List l = new ArrayList();
			while(rs.next()){
				String saleId = rs.getString("SALE_ID");
				FDAuthInfo info = new FDAuthInfo(saleId);
				info.setDeliveryDate(rs.getDate("REQUESTED_DATE"));
				info.setTransactionDateTime(rs.getDate("ACTION_DATE"));
				info.setSaleStatus(EnumSaleStatus.getSaleStatus(rs.getString("STATUS")));
				info.setAuthAmount(rs.getDouble("AMOUNT"));
				info.setFirstName(rs.getString("FIRST_NAME"));
				info.setLastName(rs.getString("LAST_NAME"));
				info.setNameOnCard(rs.getString("NAME"));
				info.setAuthDescription(rs.getString("DESCRIPTION"));
				info.setAuthCode(rs.getString("AUTH_CODE"));
				info.setCardType(rs.getString("CARD_TYPE"));
				info.setCCLastFourNum(rs.getString("CCNUM_LAST4"));
				info.setPaymentMethodType(EnumPaymentMethodType.getEnum(rs.getString("PAYMENT_METHOD_TYPE")));
				info.setAbaRouteNumber(rs.getString("ABA_ROUTE_NUMBER"));
				info.setBankAccountType(EnumBankAccountType.getEnum(rs.getString("BANK_ACCOUNT_TYPE")));
				info.setOrderType(rs.getString("TYPE"));
				l.add(info);
			}

			return l;

		} catch (SQLException e){
			throw new FDResourceException(e);
		} finally {
			if(conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					LOGGER.debug("SQLException while closing connection: ", e);
				}
			}
		}
	}

	public Collection getSupervisorApprovalCodes() throws FDResourceException {
		//
		// !!! reads codes from erpservices.properties MUST CHANGE EVENTUALLY
		//
		Collection codes = new ArrayList();
		String codeString = ErpServicesProperties.getCallCenterSupervisorCodes();
		StringTokenizer tokenizer = new StringTokenizer(codeString, ",");
		while (tokenizer.hasMoreTokens()) {
			codes.add(tokenizer.nextToken());
		}
		return codes;
	}

	public Collection getFailedAuthorizationSales() throws FDResourceException {
		try {
			ErpCustomerManagerSB customerManagerSB = this.getErpCustomerManagerHome().create();
			return customerManagerSB.getFailedAuthorizationSales();
		} catch (RemoteException ex) {
			throw new FDResourceException(ex);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
	}

	public void returnOrder(String saleId, ErpReturnOrderModel returnOrder) throws FDResourceException, ErpTransactionException {
		try {
			ErpCustomerManagerSB customerManagerSB = this.getErpCustomerManagerHome().create();
			customerManagerSB.processSaleReturn(saleId, returnOrder);
		} catch (RemoteException ex) {
			throw new FDResourceException(ex);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
	}

	private void returnDeliveryPass(String saleId) throws FDResourceException, CreateException, RemoteException {
		DlvPassManagerSB dlvPassManagerSB = this.getDlvPassManagerHome().create();
		//Delivery pass is returned.
		List dpasses = dlvPassManagerSB.getDlvPassesByOrderId(saleId);
		if(dpasses == null || dpasses.size() == 0){
			throw new FDResourceException("Unable to locate the delivery pass linked with this order.");
		}
		DeliveryPassModel model = (DeliveryPassModel)dpasses.get(0);
		model.setStatus(EnumDlvPassStatus.PASS_RETURNED);
		dlvPassManagerSB.cancel(model);
		//Create a activity log to track the delivery credits.
		ErpActivityRecord activityRecord = createActivity(EnumAccountActivityType.CANCEL_DLV_PASS,
															"SYSTEM",
															DlvPassConstants.CANCEL_NOTE,
															model,
															saleId,
															EnumDlvPassExtendReason.OTHER.getName());
		logActivity(activityRecord);

	}

	private void handleDeliveryPass(String saleId, ErpReturnOrderModel returnOrder) throws FDResourceException, CreateException, RemoteException {
		boolean isDlvChargeWaived = false;
		List charges = returnOrder.getCharges();
		for(Iterator i = charges.iterator(); i.hasNext(); ){
			ErpChargeLineModel charge =(ErpChargeLineModel)i.next();
			if(EnumChargeType.DELIVERY.equals(charge.getType())){
				if(charge.getDiscount() != null){
					String promoCode = charge.getDiscount().getPromotionCode();
					//Not waived due to a Delivery Pass .
					isDlvChargeWaived = !(promoCode != null && promoCode.equals(DlvPassConstants.PROMO_CODE));
					break;
				}
			}
		}
		/*
		 * Check if delivery charge has been waived by CSR due to FD's Fault or
		 * If restocking fee was not applied to the order.
		 * If either one of them is true and if delivery pass was applied then
		 * credit the delivery back to the pass.
		 */
		if(isDlvChargeWaived || !returnOrder.isRestockingApplied()){
			//Get the delivery pass id.
			String dlvPassId = returnOrder.getDeliveryPassId();
			DlvPassManagerSB dlvPassManagerSB = this.getDlvPassManagerHome().create();
			//Get the Model.
			DeliveryPassModel passModel = dlvPassManagerSB.getDeliveryPassInfo(dlvPassId);
			//Increment the pass by 1 if BSGS pass. For Unlimited it will handled on a case by case basis.
			if(!(passModel.getType().isUnlimited())){
				dlvPassManagerSB.creditDelivery(passModel, 1);
				//Create a activity log to track the delivery credits.
				ErpActivityRecord activityRecord = createActivity(EnumAccountActivityType.CREDIT_DLV_PASS,
																	"SYSTEM",
																	DlvPassConstants.RETURN_NOTE,
																	passModel,
																	saleId,
																	EnumDlvPassExtendReason.OTHER.getName());
				logActivity(activityRecord);

			}

		}
	}

	public void approveReturn(String saleId, ErpReturnOrderModel returnOrder) throws FDResourceException, ErpTransactionException {
		try {
			ErpCustomerManagerSB sb = this.getErpCustomerManagerHome().create();
			sb.approveReturn(saleId, returnOrder);
			/*
			 * Check if the return order contains a delivery pass. if yes cancel the
			 * delivery pass.
			 */
			if(returnOrder.isContainsDeliveryPass()){
				returnDeliveryPass(saleId);
			}else if(returnOrder.isDlvPassApplied()){
				//Delivery pass was applied.
				handleDeliveryPass(saleId,returnOrder);
			}


		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public void scheduleRedelivery(String saleId, ErpRedeliveryModel redeliveryModel)
		throws FDResourceException,
		ErpTransactionException {
		try {
			ErpCustomerManagerSB customerManagerSB = this.getErpCustomerManagerHome().create();
			customerManagerSB.scheduleRedelivery(saleId, redeliveryModel);
		} catch (CreateException ce) {
			throw new FDResourceException(ce, "Error Creating CustomerManagerSessionBean");
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to CustomerManagerSessionBean");
		}
	}

	public void changeRedeliveryToReturn(String saleId)
		throws FDResourceException,
		ErpTransactionException,
		ErpSaleNotFoundException {
		try {
			ErpCustomerManagerSB customerManagerSB = this.getErpCustomerManagerHome().create();
			customerManagerSB.markAsReturn(saleId, true, false);
		} catch (CreateException ce) {
			throw new FDResourceException(ce, "Error Creating CustomerManagerSessionBean");
		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to CustomerManagerSessionBean");
		}
	}

	private static final String orderByStatusQuery = "select s.id as sale_id, "
		+ "(select requested_date from cust.salesaction where sale_id=s.id and action_type in ('CRO','MOD') "
		+ "and action_date=(select max(action_date) from cust.salesaction where action_type in ('CRO','MOD') and sale_id=s.id)) as requested_date, "
		+ "s.status, "
		+ "(select amount from cust.salesaction where sale_id=s.id and action_type in ('CRO','MOD','INV') "
		+ "and action_date=(select max(action_date) from cust.salesaction where action_type in ('CRO','MOD','INV') and sale_id=s.id)) as amount, "
		+ "ci.last_name, ci.first_name, ci.customer_id as erp_id, fdc.id as fd_id, ci.email, ci.home_phone, ci.cell_phone, ci.business_phone "
		+ "from cust.sale s, cust.customerinfo ci, cust.fdcustomer fdc, cust.customer c "
		+ "where s.customer_id=c.id and c.id=ci.customer_id and c.id=fdc.erp_customer_id ";

	public List getOrdersByStatus(String[] status) throws FDResourceException {
		Connection conn = null;
		List retval = new ArrayList();
		try {
			conn = this.getConnection();
			CriteriaBuilder builder = new CriteriaBuilder();
			builder.addInString("s.status", status);
			PreparedStatement ps = conn.prepareStatement(orderByStatusQuery + " and " +builder.getCriteria());

			Object[] par = builder.getParams();
			for (int i = 0; i < par.length; i++) {
				ps.setObject(i + 1, par[i]);
			}
			ResultSet rs = ps.executeQuery();

			processOrderQueryResults(rs, retval);
			rs.close();
			ps.close();
			return retval;
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage());
			throw new FDResourceException(sqle);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqle) {
					LOGGER.debug("Error while cleaning:", sqle);
				}
			}
		}
	}
	private static final String NSM_ORDERS_QUERY =
		"select /*use_nl(s, sa)*/ s.id as sale_id, s.status, sa.requested_date, sa.amount, sa.action_date, ci.last_name, ci.first_name "
			+ "from cust.sale s, cust.sale_cro_mod_date scm, cust.salesaction sa, cust.customerinfo ci "
			+ "where sa.action_date <= (sysdate - 1/48) and s.id = scm.sale_id "
			//+ "and s.type = 'REG' "
			+ "and scm.sale_id = sa.sale_id and scm.max_date = sa.action_date "
			+ "and s.customer_id = ci.customer_id and s.status in ('NSM', 'MOD', 'MOC', 'NEW') "
			//+ "and sa.requested_date >= sysdate order by action_date ";						
			+" AND ((sa.requested_date >= SYSDATE) OR ( s.TYPE IN ('SUB','GCD','DON') AND sa.requested_date<=(SYSDATE))) ORDER BY action_date ";

	public List getNSMOrders() throws FDResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			List lst = new ArrayList();
			PreparedStatement ps = conn.prepareStatement(NSM_ORDERS_QUERY);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				FDCustomerOrderInfo info = new FDCustomerOrderInfo();
				info.setSaleId(rs.getString("SALE_ID"));
				info.setDeliveryDate(rs.getDate("REQUESTED_DATE"));
				info.setOrderStatus(EnumSaleStatus.getSaleStatus(rs.getString("STATUS")));
				info.setAmount(rs.getDouble("AMOUNT"));
				info.setFirstName(rs.getString("FIRST_NAME"));
				info.setLastName(rs.getString("LAST_NAME"));
				info.setLastCroModDate(rs.getTimestamp("ACTION_DATE"));
				lst.add(info);
			}
			rs.close();
			ps.close();

			return lst;
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage());
			throw new FDResourceException(sqle);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqle) {
					LOGGER.debug("Error while cleaning:", sqle);
				}
			}
		}
	}

	private static final String NSM_CUST_QUERY = "select c.id, c.user_id, ci.first_name, ci.last_name "
		+ "from cust.customer c, cust.customerinfo ci "
		+ "where c.sap_id is null and c.id = ci.customer_id "
		+ "order by c.id";

	public List getNSMCustomers() throws FDResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			List lst = new ArrayList();
			PreparedStatement ps = conn.prepareStatement(NSM_CUST_QUERY);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				FDCustomerOrderInfo info = new FDCustomerOrderInfo();
				info.setIdentity(new FDIdentity(rs.getString("ID")));
				info.setFirstName(rs.getString("FIRST_NAME"));
				info.setLastName(rs.getString("LAST_NAME"));
				info.setEmail(rs.getString("USER_ID"));
				lst.add(info);
			}
			rs.close();
			ps.close();

			return lst;
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage());
			throw new FDResourceException(sqle);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqle) {
					LOGGER.debug("Error while cleaning:", sqle);
				}
			}
		}
	}

	private static void processOrderQueryResults(ResultSet rs, List orders) throws SQLException {
		while (rs.next()) {
			FDCustomerOrderInfo coi = new FDCustomerOrderInfo();
			coi.setSaleId(rs.getString("SALE_ID"));
			coi.setDeliveryDate(rs.getDate("REQUESTED_DATE"));
			coi.setOrderStatus(EnumSaleStatus.getSaleStatus(rs.getString("STATUS")));
			coi.setAmount(rs.getDouble("AMOUNT"));
			coi.setFirstName(rs.getString("FIRST_NAME"));
			coi.setLastName(rs.getString("LAST_NAME"));
			coi.setIdentity(new FDIdentity(rs.getString("ERP_ID"), rs.getString("FD_ID")));
			coi.setEmail(rs.getString("EMAIL"));
			coi.setPhone(rs.getString("HOME_PHONE"));
			coi.setAltPhone(rs.getString("CELL_PHONE"));
			if (coi.getAltPhone() == null || "".equals(coi.getAltPhone()))
				coi.setAltPhone(rs.getString("BUSINESS_PHONE"));
			orders.add(coi);
		}
	}

	private final static String signupPromoAVSQuery = "select s.id as sale_id, sa.requested_date, s.status, sa.amount, ci.last_name, ci.first_name, c.id as erp_id, fdc.id as fd_id, ci.email, ci.home_phone, ci.business_phone, ci.cell_phone "
		+ "from cust.sale s, cust.salesaction sa, cust.customer c, cust.customerinfo ci, cust.fdcustomer fdc "
		+ "where s.id=sa.sale_id and s.customer_id=c.id and c.id=ci.customer_id and fdc.erp_customer_id=c.id "
		+ "and s.type = 'REG' "
		+ "and sa.requested_date > trunc(SYSDATE) and s.status='AVE' and sa.promotion_campaign='SIGNUP' and sa.action_type in ('CRO','MOD') "
		+ "and sa.action_date=(select max(action_date) from cust.salesaction where sale_id=s.id and action_type in ('CRO','MOD'))";

	public List getSignupPromoAVSExceptions() throws FDResourceException {
		Connection conn = null;
		List retval = new ArrayList();
		try {
			conn = this.getConnection();
			PreparedStatement ps = conn.prepareStatement(signupPromoAVSQuery);
			ResultSet rs = ps.executeQuery();
			processOrderQueryResults(rs, retval);
			rs.close();
			ps.close();
			return retval;
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage());
			throw new FDResourceException(sqle);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException se) {
					LOGGER.debug("Error while cleaning:", se);
				}
			}
		}
	}

	private static final String creditSummaryQuery = "select ci.first_name || ' ' || ci.last_name as customer_name, s.id as order_number, sao.requested_date as delivery, "
		+ "sai.amount as invoice, cmp.amount as pending_credit, cmp.status, cmp.note, cd.name as department, cml.line_number, "
		+ "(select description from cust.orderline where salesaction_id=sao.id and substr(orderline_number,1,4)=lpad(cml.line_number+1,4,'0')) as item_desc, "
		+ "(select configuration_desc from cust.orderline where salesaction_id=sao.id and substr(orderline_number,1,4)=lpad(cml.line_number+1,4,'0')) as item_config, "
		+ "(select sku_code from cust.orderline where salesaction_id=sao.id and substr(orderline_number,1,4)=lpad(cml.line_number+1,4,'0')) as item_sku, "
		+ "cml.quantity, cc.name as reason, (select count(*) from cust.sale where customer_id=c.id and status <> 'CAN') as number_of_orders, "
		+ "(select sum(original_amount) from cust.customercredit where customer_id=c.id) as previous_credits "
		+ "from cust.sale s, cust.salesaction sao, cust.customer c, cust.customerinfo ci, cust.salesaction sai, "
		+ "cust.complaint cmp, cust.complaintline cml, cust.complaint_dept_code cdc, cust.complaint_code cc, cust.complaint_dept cd "
		+ "where s.id=sao.sale_id and s.customer_id=c.id and c.id=ci.customer_id and s.id=cmp.sale_id and s.id=sai.sale_id(+) "
		+ "and cmp.id=cml.complaint_id and cml.complaint_dept_code_id=cdc.id and cdc.comp_code=cc.code and cdc.comp_dept=cd.code "
		+ "and sao.action_type in ('CRO','MOD') and sao.action_date=(select max(action_date) from cust.salesaction where sale_id=s.id and action_type in ('CRO','MOD')) "
		+ "and 'INV'=sai.action_type(+) and cmp.create_date>=? and cmp.create_date<? "
		+ "order by delivery, order_number, department, line_number ";

	public List getCreditSummaryForDate(java.util.Date reportDate) throws FDResourceException {
		Calendar reportDateStart = Calendar.getInstance();
		reportDateStart.setTime(reportDate);
		reportDateStart.set(Calendar.HOUR_OF_DAY, 0);
		reportDateStart.set(Calendar.MINUTE, 0);
		reportDateStart.set(Calendar.SECOND, 0);
		Calendar reportDateEnd = Calendar.getInstance();
		reportDateEnd.setTime(reportDateStart.getTime());
		reportDateEnd.add(Calendar.DATE, 1);

		Connection conn = null;
		List retval = new ArrayList();
		try {
			conn = this.getConnection();
			PreparedStatement ps = conn.prepareStatement(
				creditSummaryQuery,
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
			ps.setDate(1, new java.sql.Date(reportDateStart.getTime().getTime()));
			ps.setDate(2, new java.sql.Date(reportDateEnd.getTime().getTime()));
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				FDCreditSummary cs = new FDCreditSummary();
				String ordNum = rs.getString("ORDER_NUMBER");
				cs.setOrderNumber(ordNum);
				cs.setCustomerName(rs.getString("CUSTOMER_NAME"));
				cs.setDeliveryDate(rs.getDate("DELIVERY"));
				cs.setInvoiceAmount(rs.getDouble("INVOICE"));
				cs.setCreditAmount(rs.getDouble("PENDING_CREDIT"));
				cs.setNote(rs.getString("NOTE"));
				cs.setNumberOfOrders(rs.getInt("NUMBER_OF_ORDERS"));
				cs.setPreviousCreditAmount(rs.getDouble("PREVIOUS_CREDITS"));
				cs.setStatus(com.freshdirect.customer.EnumComplaintStatus.getComplaintStatus(rs.getString("STATUS")).getName());
				retval.add(cs);
				do {
					FDCreditSummary.Item item = new FDCreditSummary.Item();
					item.setConfiguration(rs.getString("ITEM_CONFIG"));
					item.setDescription(rs.getString("ITEM_DESC"));
					item.setQuantity(rs.getDouble("QUANTITY"));
					item.setReason(rs.getString("REASON"));
					item.setSkuCode(rs.getString("ITEM_SKU"));
					cs.addItem(item);
				} while (rs.next() && ordNum.equals(rs.getString("ORDER_NUMBER")));
				rs.previous();
			}
			rs.close();
			ps.close();
			return retval;
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage());
			throw new FDResourceException(sqle);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException se) {
					LOGGER.debug("Error while cleaning:", se);
				}
			}
		}
	}

	public EnumPaymentResponse resubmitPayment(String saleId, ErpPaymentMethodI payment, Collection charges)
		throws FDResourceException,
		ErpTransactionException {
		try {
			ErpCustomerManagerSB customerManagerSB = this.getErpCustomerManagerHome().create();
			return customerManagerSB.resubmitPayment(saleId, payment, charges);
		} catch (RemoteException ex) {
			throw new FDResourceException(ex);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		}
	}

	public void resubmitOrder(String saleId, CustomerRatingI cra,EnumSaleType saleType) throws FDResourceException, ErpTransactionException {
		try {
			ErpCustomerManagerSB customerManagerSB = (ErpCustomerManagerSB) this.getErpCustomerManagerHome().create();
			customerManagerSB.resubmitOrder(saleId, cra,saleType);

		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public void resubmitCustomer(String customerID) throws FDResourceException {
		try {
			PrimaryKey customerPk = new PrimaryKey(customerID);
			ErpCustomerManagerSB customerManagerSB = (ErpCustomerManagerSB) this.getErpCustomerManagerHome().create();
			customerManagerSB.resubmitCustomer(customerPk);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	public List getHolidayMeals(FDIdentity identity) throws FDResourceException {
		Connection conn = null;
		java.util.List lst = new java.util.LinkedList();
		try {
			conn = this.getConnection();

			PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.HOLIDAYMEAL WHERE CUSTOMER_ID=?");
			ps.setString(1, identity.getErpCustomerPK());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				MealPersistentBean bean = new MealPersistentBean(new PrimaryKey(rs.getString(1)), conn);
				bean.setParentPK(new PrimaryKey(identity.getErpCustomerPK()));
				lst.add(bean.getModel());
			}
			rs.close();
			rs = null;
			ps.close();
			ps = null;
			return lst;

		} catch (SQLException se) {
			getSessionContext().setRollbackOnly();
			throw new FDResourceException(se);
		} finally {
			if (conn != null) {
				try {
					conn.close();
					conn = null;
				} catch (SQLException se) {
					LOGGER.debug("Error while cleaning:", se);
				}
			}
		}
	}

	public MealModel saveHolidayMeal(FDIdentity identity, String agent, MealModel meal) throws FDResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();

			MealPersistentBean mpb = new MealPersistentBean(meal);
			mpb.setAgent(agent);
			mpb.setParentPK(new PrimaryKey(identity.getErpCustomerPK()));
			PrimaryKey mpk = null;
			if (meal.isAnonymous()) {
				mpk = mpb.create(conn);
			} else {
				mpk = meal.getPK();
				mpb.store(conn);
			}

			mpb = new MealPersistentBean(mpk, conn);
			return (MealModel) mpb.getModel();

		} catch (SQLException se) {
			getSessionContext().setRollbackOnly();
			throw new FDResourceException(se);
		} finally {
			if (conn != null) {
				try {
					conn.close();
					conn = null;
				} catch (SQLException se) {
					LOGGER.debug("Error while cleaning:", se);
				}
			}
		}
	}

	private static final String CUTOFFTME_QUERY = "select cutofftime from cust.deliveryInfo where starttime >= ? and endtime < ? group by cutofftime";

	public List getCutoffTimeForDate(java.util.Date date) throws FDResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			PreparedStatement ps = conn.prepareStatement(CUTOFFTME_QUERY);
			date = DateUtil.truncate(date);
			ps.setTimestamp(1, new Timestamp(date.getTime()));
			date = DateUtil.addDays(date, 1);
			ps.setTimestamp(2, new Timestamp(date.getTime()));
			ResultSet rs = ps.executeQuery();
			List ret = new ArrayList();
			while (rs.next()) {
				ret.add(rs.getTimestamp("CUTOFFTIME"));
			}
			ps.close();
			rs.close();
			return ret;
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
					conn = null;
				} catch (SQLException e) {
					LOGGER.warn("Error while cleaning:", e);
				}
			}
		}
	}

	private static final String CUTOFF_REPORT_QUERY = "select s.status, di.cutofftime, count(*) as order_count "
		+ "from cust.sale s, cust.salesaction sa, cust.deliveryinfo di "
		+ "where s.id=sa.sale_id and sa.id=di.salesaction_id and s.type<>'SUB' and sa.action_type in ('CRO','MOD') and sa.requested_date=? "
		+ "and s.type = 'REG' "
		+ "and sa.action_date=(select max(action_date) from cust.salesaction where sale_id=s.id and action_type in ('CRO','MOD')) "
		+ "and di.starttime > ? and di.starttime < ? "
		+ "group by s.status, di.cutofftime "
		+ "order by di.cutofftime, s.status ";

	public List getCutoffTimeReport(java.util.Date day) throws FDResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			PreparedStatement ps = conn.prepareStatement(CUTOFF_REPORT_QUERY);
			day = DateUtil.truncate(day);
			ps.setDate(1, new java.sql.Date(day.getTime()));
			ps.setTimestamp(2, new Timestamp(day.getTime()));
			Calendar cal = DateUtil.toCalendar(day);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			ps.setTimestamp(3, new Timestamp(cal.getTime().getTime()));
			ResultSet rs = ps.executeQuery();
			List ret = new ArrayList();

			while (rs.next()) {
				EnumSaleStatus s = EnumSaleStatus.getSaleStatus(rs.getString("STATUS"));
				ret.add(new FDCutoffTimeInfo(s, rs.getTimestamp("CUTOFFTIME"), rs.getInt("ORDER_COUNT")));
			}
			ps.close();
			rs.close();
			return ret;
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
					conn = null;
				} catch (SQLException e) {
					LOGGER.warn("Error while cleaning:", e);
				}
			}
		}
	}

	public void emailCutoffTimeReport(Date deliveryDate) throws FDResourceException{
		try {
			deliveryDate = DateUtil.truncate(deliveryDate);
			Calendar cutoffDate = DateUtil.toCalendar(deliveryDate);
			cutoffDate.add(Calendar.DATE, -1);

			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			/*
			LOGGER.debug("Getting cutoff times for " + new SimpleDateFormat().format(cutoffDate.getTime()));
			List cutoffTimes = this.getCutoffTimeForDate(cutoffDate.getTime());
			Collections.sort(cutoffTimes);
			TimeOfDay cutoff = null;

			for(Iterator cIter = cutoffTimes.iterator(); cIter.hasNext();){
				Date c = (Date)cIter.next();

				if(new TimeOfDay(c).before(new TimeOfDay(new Date()))){
					cutoff = new TimeOfDay(c);
					break;
				}
			}*/

			List cReport = getCutoffTimeReport(deliveryDate);
			StringBuffer buff = new StringBuffer();
			String br = "\n";

			buff.append("Cutoff Time Report for ").append(dateFormatter.format(deliveryDate)).append(br);
			buff.append(br);
			buff.append("Cutoff Time").append("\t\t\t").append("Order Count").append("\t\t").append("Sale Status").append(br);
			buff.append("----------------------------------------------------------------------------------").append(br);

			for(Iterator i = cReport.iterator(); i.hasNext();){
				FDCutoffTimeInfo info = (FDCutoffTimeInfo) i.next();
				//if(info.getCutoffTime().equals(deliveryDate)){
					buff.append(new SimpleDateFormat().format(info.getCutoffTime())).append("\t\t").append(info.getOrderCount()).append("\t\t\t").append(info.getStatus()).append(br);

				//}
			}

			buff.append("----------------------------------------------------------------------------------");

			ErpMailSender mailer = new ErpMailSender();
			mailer.sendMail(ErpServicesProperties.getSapMailFrom(),
							ErpServicesProperties.getSapMailTo(),
							ErpServicesProperties.getSapMailCC(),
							"Cutoff Report For " + dateFormatter.format(deliveryDate), buff.toString());

		} catch (MessagingException e) {
			LOGGER.warn("Error Sending cutoff time report: ", e);
		}
	}

	public List getSubjectReport(Date day1, Date day2, boolean showAutoCases) throws FDResourceException {
		String sql = "select cq.name as queue, cs.name as subject, count(*) as caseCount"
			+ " from cust.case c, cust.case_queue cq, cust.case_subject cs, cust.caseaction ca "
			+ " where c.case_subject=cs.code"
			+ " and cs.case_queue=cq.code"
			+ " and c.id=ca.case_id"
			+ " and ca.timestamp >= ? and ca.timestamp <=? "
			+ " and ca.timestamp=(select max(timestamp) from cust.caseaction where case_id=c.id and timestamp >= ? and timestamp <=? )"
			+ " and (cq.obsolete is null or cq.obsolete <> 'X') and (cs.obsolete is null or cs.obsolete <> 'X')"
			+ (!showAutoCases ? " AND c.CASE_ORIGIN NOT IN ('" + CrmCaseOrigin.CODE_SYS + "')" : "")
			+ " group by  cq.name, cs.name"
			+ " order by  cq.name, cs.name";

		Connection conn = null;
		try {
			conn = this.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setTimestamp(1, new Timestamp(day1.getTime()));
			ps.setTimestamp(3, new Timestamp(day1.getTime()));
			ps.setTimestamp(2, new Timestamp(day2.getTime()));
			ps.setTimestamp(4, new Timestamp(day2.getTime()));

			ResultSet rs = ps.executeQuery();
			List rpt = new ArrayList();

			while (rs.next()) {
				rpt.add(new SubjectReportLine(rs.getString("QUEUE"), rs.getString("Subject"), rs.getInt("CaseCount")));
			}
			ps.close();
			rs.close();
			return rpt;
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
					conn = null;
				} catch (SQLException e) {
					LOGGER.warn("Error while cleaning:", e);
				}
			}
		}
	}

	private static final String LATE_DLVRY_REPORT_QRY = "select * from ( "
		+ " select create_date,s.wave_number,s.truck_number, s.stop_sequence, s.id as order_number, di.first_name, di.last_name,di.starttime, di.endtime, 'case' as source,"
		+ "(select 'X' from cust.fdcustomer fdc, cust.profile p where s.customer_id=fdc.erp_customer_id and fdc.id=p.customer_id(+) and p.profile_name='ChefsTable') as chefs_table,"
		+ " (select decode(count(*),2,'X',3,'X',4,'X',NULL) from cust.sale where customer_id=s.customer_id and status<>'CAN') as undeclared"
		+ " from cust.case c, cust.sale s, cust.salesaction sa, cust.deliveryinfo di"
		+ " where c.sale_id=s.id and s.id=sa.sale_id and sa.id=di.salesaction_id"
		+ " and s.type = 'REG' "
		+ " and sa.requested_date= ? and sa.action_type in ('CRO','MOD') and s.status <> 'CAN'"
		+ " and sa.action_date=(select max(action_date) from cust.salesaction where sale_id=s.id and action_type in ('CRO','MOD'))"
		+ " and c.case_subject in ('LDQ-005','LDQ-006','LDQ-007')"
		+ " union all"
		+ " select create_date,s.wave_number,s.truck_number, s.stop_sequence, s.id as order_number, di.first_name, di.last_name,di.starttime, di.endtime,'complaint' as source,"
		+ " (select 'X' from cust.fdcustomer fdc, cust.profile p where s.customer_id=fdc.erp_customer_id and fdc.id=p.customer_id(+) and p.profile_name='ChefsTable') as chefs_table,"
		+ " (select decode(count(*),2,'X',3,'X',4,'X',NULL) from cust.sale where customer_id=s.customer_id and status<>'CAN') as undeclared"
		+ " from cust.complaint_dept_code cdc, cust.complaintline cl, cust.complaint c, cust.sale s, cust.salesaction sa, cust.deliveryinfo di"
		+ " where cdc.id=cl.complaint_dept_code_id and cl.complaint_id=c.id and c.sale_id=s.id and s.id=sa.sale_id and sa.id=di.salesaction_id"
		+ " and s.type = 'REG' "
		+ " and sa.requested_date=? and sa.action_type in ('CRO','MOD') and s.status <> 'CAN'"
		+ " and sa.action_date=(select max(action_date) from cust.salesaction where sale_id=s.id and action_type in ('CRO','MOD'))"
		+ " and cdc.comp_code='LATEDEL' and cdc.comp_dept='TRN'"
		+ ") order by wave_number, truck_number, stop_sequence";

	public List getLateDeliveryReport(Date day1) throws FDResourceException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			PreparedStatement ps = conn.prepareStatement(LATE_DLVRY_REPORT_QRY);
			Timestamp truncDate = new Timestamp(DateUtil.truncate(day1).getTime());
			ps.setTimestamp(1, truncDate);
			ps.setTimestamp(2, truncDate);
			ResultSet rs = ps.executeQuery();
			List rpt = new ArrayList();

			while (rs.next()) {
				LateDlvReportLine rl = new LateDlvReportLine();
				rs.getString("chefs_table");
				rl.setChefsTable(!rs.wasNull());
				rs.getString("undeclared");
				rl.setUndeclared(!rs.wasNull());
				rl.setFirstName(rs.getString("first_name"));
				rl.setLastName(rs.getString("last_name"));
				rl.setOrderNumber(rs.getString("order_number"));
				rl.setSource(rs.getString("source"));
				rl.setStopSequence(rs.getString("stop_Sequence"));
				rl.setTruckNumber(rs.getString("truck_number"));
				rl.setWaveNumber(rs.getString("wave_number"));
				rl.setTimeCaseOpened(rs.getTimestamp("create_date"));
				rl.setStartTime(rs.getTimestamp("starttime"));
				rl.setEndTime(rs.getTimestamp("endtime"));
				rpt.add(rl);
			}
			ps.close();
			rs.close();
			return rpt;
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
					conn = null;
				} catch (SQLException e) {
					LOGGER.warn("Error while cleaning:", e);
				}
			}
		}
	}

	private static final String ROUTE_STOP_QRY = "select * from ( "
		+ " select s.wave_number, s.truck_number, s.stop_sequence, s.id as order_number, di.first_name, di.last_name, di.phone, di.phone_ext,"
		+ " ci.email, decode(ci.email_plain_text, 'X', 'TEXT', 'HTML') as email_format_type"
		+ " from cust.sale s, cust.salesaction sa, cust.deliveryinfo di, cust.customerinfo ci"
		+ " where s.id=sa.sale_id and s.type ='REG' and sa.id=di.salesaction_id and s.customer_id=ci.customer_id and sa.requested_date=?";

	private String finalRouteStopQuery;

	private static final String ROUTE_STOP_QRY_WHERE_WAVE = " and s.wave_number=LPAD(?, 6, '0')";

	private static final String ROUTE_STOP_QRY_WHERE_ROUTE = " and s.truck_number=LPAD(?, 6, '0')";

	private static final String ROUTE_STOP_QRY_WHERE_STOP = " and (s.stop_sequence between LPAD(?, 5, '0') and LPAD(?, 5, '0'))";

	private static final String ROUTE_STOP_QRY_END = " and sa.action_type in ('CRO','MOD') and s.status <> 'CAN'"
		+ " and sa.action_date=(select max(action_date) from cust.salesaction where sale_id=s.id and action_type in ('CRO','MOD'))"
		+ ") order by wave_number, truck_number, stop_sequence";

	public List getRouteStopReport(Date date, String wave, String route, String stop1, String stop2) throws FDResourceException {

		Connection conn = null;
		try {
			conn = this.getConnection();

			finalRouteStopQuery = ROUTE_STOP_QRY;

			System.out.println("wave: " + wave +  " route: " + route + " stop: " + stop1 + " to " + stop2);

			if (wave != null && !"".equals(wave)) {
				finalRouteStopQuery += ROUTE_STOP_QRY_WHERE_WAVE;
			}

			if (route != null && !"".equals(route)) {
				finalRouteStopQuery += ROUTE_STOP_QRY_WHERE_ROUTE;
			}

			if ((stop1 != null && !"".equals(stop1)) || (stop2 != null && !"".equals(stop2))) {
				finalRouteStopQuery += ROUTE_STOP_QRY_WHERE_STOP;
			}

			finalRouteStopQuery += ROUTE_STOP_QRY_END;

			PreparedStatement ps = conn.prepareStatement(finalRouteStopQuery);
			date = DateUtil.truncate(date);
			//Timestamp truncDate = new Timestamp(DateUtil.truncate(date).getTime());
			int index = 1;
			//ps.setTimestamp(index++, truncDate);
			ps.setDate(index++, new java.sql.Date(date.getTime()));

			if (wave != null && !"".equals(wave)) {
				ps.setString(index++, wave);
			}

			if (route != null && !"".equals(route)) {
				ps.setString(index++, route);
			}

			if (stop1 != null && !"".equals(stop1)) {
				ps.setString(index++, stop1);
				if (stop2 == null || "".equals(stop2)) {
					ps.setString(index++, stop1);
				}
			}

			if (stop2 != null && !"".equals(stop2)) {
				if (stop1 == null || "".equals(stop1)) {
					ps.setString(index++, stop2);
				}
				ps.setString(index++, stop2);
			}

			ResultSet rs = ps.executeQuery();
			List rpt = new ArrayList();

			while (rs.next()) {
				RouteStopReportLine rl = new RouteStopReportLine();
				rl.setOrderNumber(rs.getString("order_number"));
				rl.setFirstName(rs.getString("first_name"));
				rl.setLastName(rs.getString("last_name"));
				rl.setDlvPhone(rs.getString("phone"));
				rl.setDlvPhoneExt(rs.getString("phone_ext"));
				rl.setPhoneNumber(rs.getString("phone"), rs.getString("phone_ext"));
				rl.setWaveNumber(rs.getString("wave_number"));
				rl.setTruckNumber(rs.getString("truck_number"));
				rl.setStopSequence(rs.getString("stop_Sequence"));
				rl.setEmail(rs.getString("email"));
				rl.setEmailFormatType(rs.getString("email_format_type"));
				rpt.add(rl);
			}
			ps.close();
			rs.close();
			return rpt;
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
					conn = null;
				} catch (SQLException e) {
					LOGGER.warn("Error while cleaning:", e);
				}
			}
		}
	}

	public List getOrderStatusReport(String[] statusCodes) throws FDResourceException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			String sql = "SELECT id, status, sap_number FROM cust.sale s WHERE ";
			CriteriaBuilder builder = new CriteriaBuilder();
			builder.addInString("s.status", statusCodes);

			conn = this.getConnection();
			ps = conn.prepareStatement(sql + builder.getCriteria());

			Object[] par = builder.getParams();
			for (int i = 0; i < par.length; i++) {
				ps.setObject(i + 1, par[i]);
			}
			rs = ps.executeQuery();

			List lst = new ArrayList();
			while (rs.next()) {
				CrmOrderStatusReportLine rl = new CrmOrderStatusReportLine(rs.getString("ID"), EnumSaleStatus.getSaleStatus(rs.getString("STATUS")), rs.getString("SAP_NUMBER"));
				lst.add(rl);
			}

			return lst;

		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("error while cleanup", e);
			}

		}

	}

	private static final String SETTLEMENT_PROBLEM_QUERY =
		"SELECT s1.id, s1.status, sa1.amount, sa1.action_type, sa1.action_date AS failure_date,"
		+ " ci.first_name || ' ' || ci.last_name AS customer_name, p.payment_method_type,"
			+ " ("
			+ "	SELECT di.starttime "
				+ " FROM cust.DELIVERYINFO di, cust.SALESACTION sa2, cust.SALE s2"
				+ " WHERE di.SALESACTION_ID = sa2.ID"
				+ " AND sa2.sale_id = s2.id"
				+ " AND s2.id = s1.id"
				+ " AND sa2.ACTION_TYPE IN ('CRO', 'MOD')"
				+ " AND sa2.action_date = ("
									+ " SELECT MAX(sa3.action_date)"
									+ " FROM cust.SALESACTION sa3"
									+ " WHERE sa3.sale_id=s1.id AND sa3.action_type IN ('CRO','MOD')"
									+ " )"
			+ " ) AS delivery_date"
		+ " FROM  CUST.SALE s1, CUST.SALESACTION sa1, cust.CUSTOMERINFO ci, cust.PAYMENT p"
		+ " WHERE sa1.sale_id=s1.id"
		+ " AND s1.customer_id = ci.customer_id"
		+ " AND sa1.id = p.salesaction_id"
		+ "	AND  sa1.action_type = ?"
		+ " AND sa1.action_date = (SELECT MAX(sa4.action_date) FROM CUST.SALESACTION sa4 WHERE sa4.sale_id=s1.id AND sa4.action_type = ?)"
		+ " AND  s1.status = ?";

	public List getSettlementProblemReport(String[] statusCodes, String[] transactionTypes, Date failureStartDate, Date failureEndDate) throws FDResourceException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < statusCodes.length && i < transactionTypes.length; i++) {
				if (i != 0) {
					sb.append(" UNION ");
				}
				sb.append(SETTLEMENT_PROBLEM_QUERY);
				if (failureStartDate != null) {
					sb.append(" AND sa1.action_date >= ?");
				}
				if (failureEndDate != null) {
					sb.append(" AND sa1.action_date <= ?");
				}
			}
			sb.append(" ORDER BY failure_date");
			System.err.println("SALE_STATUS_OR_LAST_ACTION_TYPE_QUERY sql = " + sb.toString());

			conn = this.getConnection();
			ps = conn.prepareStatement(sb.toString());

			int index = 1;
			for (int i = 0; i < statusCodes.length && i < transactionTypes.length; i++) {
				ps.setString(index++, transactionTypes[i]);
				ps.setString(index++, transactionTypes[i]);
				ps.setString(index++, statusCodes[i]);
				if (failureStartDate != null) {
					ps.setDate(index++, new java.sql.Date(DateUtil.truncate(failureStartDate).getTime()));
				}
				if (failureEndDate != null) {
					ps.setDate(index++, new java.sql.Date(DateUtil.truncate(failureEndDate).getTime()));
				}
			}

			rs = ps.executeQuery();

			List lst = new ArrayList();
			while (rs.next()) {
				CrmSettlementProblemReportLine rl = new CrmSettlementProblemReportLine(
																			rs.getString("ID"),
																			rs.getString("CUSTOMER_NAME"),
																			rs.getDouble("AMOUNT"),
																			rs.getDate("DELIVERY_DATE"),
																			rs.getDate("FAILURE_DATE"),
																			EnumSaleStatus.getSaleStatus(rs.getString("STATUS")),
																			EnumTransactionType.getTransactionType(rs.getString("ACTION_TYPE")),
																			EnumPaymentMethodType.getEnum(rs.getString("PAYMENT_METHOD_TYPE"))
																			);
				lst.add(rl);
			}

			return lst;
		} catch (SQLException e) {
			throw new FDResourceException(e);
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				LOGGER.warn("error while cleanup", e);
			}
		}
	}

	public List getMakeGoodOrder(Date date) throws FDResourceException {
		if(date == null){
			return Collections.EMPTY_LIST;
		}

		Connection conn = null;
		try{
			conn = this.getConnection();
			PreparedStatement ps = conn.prepareStatement(
				"select sa.sale_id, sa.requested_date, sa.action_date, s.status, sa.amount, ci.first_name, ci.last_name, s.truck_number as route, s.stop_sequence as stop from cust.salesaction sa, cust.sale s, cust.customerinfo ci, cust.paymentinfo pi  "
				+ "where requested_date=trunc(to_date(?)) "
				+ "and action_type in ('CRO','MOD') "
				+ "and action_date=(select max(action_date) from cust.salesaction where sale_id=sa.sale_id and action_type in ('CRO','MOD')) "
				+ "and sa.sale_id = s.id "
				+ "and s.type = 'REG' "
				+ "and s.customer_id=ci.customer_id "
				+ "and pi.salesaction_id=sa.id "
				+ "and pi.on_fd_account='M'"
			);
			ps.setDate(1, new java.sql.Date(date.getTime()));
			ResultSet rs = ps.executeQuery();
			List l = new ArrayList();
			while(rs.next()){
				String saleId = rs.getString("SALE_ID");
				MakeGoodOrderInfo info = new MakeGoodOrderInfo(saleId);
				info.setDeliveryDate(rs.getDate("REQUESTED_DATE"));
				info.setOrderPlacedDate(rs.getDate("ACTION_DATE"));
				info.setSaleStatus(EnumSaleStatus.getSaleStatus(rs.getString("STATUS")));
				info.setFirstName(rs.getString("FIRST_NAME"));
				info.setLastName(rs.getString("LAST_NAME"));
				info.setAmount(rs.getDouble("AMOUNT"));
				info.setRoute(rs.getString("ROUTE"));
				info.setStop(rs.getString("STOP"));
				l.add(info);
			}

			return l;

		} catch (SQLException e){
			throw new FDResourceException(e);
		} finally {
			if(conn != null){
				try{
					conn.close();
				}catch(SQLException e){
					LOGGER.debug("SQLException while closing connection: ", e);
				}
			}
		}
	}

	public void reverseCustomerCredit(String saleId, String complaintId)
		throws FDResourceException,
		ErpTransactionException,
		ErpComplaintException {
		try {
			ErpCustomerManagerSB sb = this.getErpCustomerManagerHome().create();
			sb.reverseCustomerCredit(saleId, complaintId);
		} catch (CreateException ce) {
			throw new FDResourceException(ce);
		} catch (RemoteException re) {
			throw new FDResourceException(re);
		}
	}

	private ErpComplaintManagerHome getComplaintManagerHome() {
		try {
			return (ErpComplaintManagerHome) LOCATOR.getRemoteHome(
				"java:comp/env/ejb/ComplaintManager",
				ErpComplaintManagerHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

	private ErpCustomerManagerHome getErpCustomerManagerHome() {
		try {
			return (ErpCustomerManagerHome) LOCATOR.getRemoteHome("freshdirect.erp.CustomerManager", ErpCustomerManagerHome.class);
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}

	/**
	 * Template method that returns the cache key to use for caching resources.
	 *
	 * @return the bean's home interface name
	 */
	protected String getResourceCacheKey() {
		return "com.freshdirect.fdstore.customer.ejb.CallCenterManagerHome";
	}

    private DlvPassManagerHome getDlvPassManagerHome() {
        try {
            return (DlvPassManagerHome) LOCATOR.getRemoteHome("java:comp/env/ejb/DlvPassManager", DlvPassManagerHome.class);
        } catch (NamingException e) {
            throw new EJBException(e);
        }
    }

	private ErpActivityRecord createActivity(EnumAccountActivityType type,
			String initiator,
			String note,
			DeliveryPassModel model,
			String saleId,
			String reasonCode) {
			ErpActivityRecord rec = new ErpActivityRecord();
			rec.setActivityType(type);

			rec.setSource(EnumTransactionSource.SYSTEM);
			rec.setInitiator(initiator);
			rec.setCustomerId(model.getCustomerId());

			StringBuffer sb = new StringBuffer();
			if (note != null) {
			sb.append(note);
			}
			rec.setNote(sb.toString());
			rec.setDeliveryPassId(model.getPK().getId());
			rec.setChangeOrderId(saleId);
			rec.setReason(reasonCode);
			return rec;
	}

	private void logActivity(ErpActivityRecord record) {
		new ErpLogActivityCommand(LOCATOR, record).execute();
	}

	public List doGenericSearch(GenericSearchCriteria criteria) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			return GenericSearchDAO.genericSearch(conn, criteria);
		} catch (SQLException e) {
			throw new FDResourceException(e, "Could not find reservations matching criteria entered.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Trouble closing connection after searchCustomerReservations", e);
				}
			}
		}
	}


	public int cancelReservations(GenericSearchCriteria resvCriteria, String initiator, String notes) throws FDResourceException{
		Connection conn = null;
		try {
			conn = getConnection();
			/*
			 * Get the reservations for the given search criteria for
			 * logging into the activity log after deleting the reservations.
			 */

			List reservations = doGenericSearch(resvCriteria);
			//cancel reservations for the given criteria.
			int updateCount = DlvManagerDAO.cancelReservations(conn, resvCriteria);
			//Create Activity Log.
			AdminToolsDAO.logCancelledReservations(conn, reservations, initiator, notes);
			
			postMassCancellation(reservations);
			
			return updateCount;
		} catch (SQLException e) {
			LOGGER.error("SQL Error occurred while creating activity logs after cancelling reservations.");
			throw new EJBException(e);
		}finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Trouble closing connection after cancelReservations", e);
				}
			}
		}
	}
	
	private void postMassCancellation(List reservations) {
		for (Iterator i = reservations.iterator(); i.hasNext();) {
			FDCustomerReservationInfo info = (FDCustomerReservationInfo)i.next();
			try {
				Collection<ErpAddressModel> addressList= FDCustomerManager.getShipToAddresses(info.getIdentity());
				for (ErpAddressModel address : addressList) {
					if(address.getId().equals(info.getAddress().getId())) {
						info.getAddress().setFrom(address, info.getFirstName(), info.getLastName(), info.getIdentity().getErpCustomerPK());
						FDDeliveryManager.getInstance().removeReservationEx(info.getId(), info.getAddress());
					}						
				}
			} catch(Exception e) {
				e.printStackTrace();
				//For Dynamic Async Phase 1 will be changed in Dynamic Routing Phase2
			}
		}
	}

	public int fixBrokenAccounts() throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			return AdminToolsDAO.fixBrokenAccounts(conn);
		} catch (SQLException e) {
			LOGGER.error("SQL Error occurred while fixing the Broken Accounts.");
			throw new FDResourceException(e, "Could not fix Broken Accounts due to SQL Error.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Trouble closing connection after fixing Broken Accounts.", e);
				}
			}
		}
	}

	public Map returnOrders(FDActionInfo info, List customerOrders) throws FDResourceException{
			List successOrders = new ArrayList();
			List failureOrders = new ArrayList();
			String saleId = null;
			try {
				ErpCustomerManagerSB sb = this.getErpCustomerManagerHome().create();
				for(Iterator iter = customerOrders.iterator();iter.hasNext();){
					FDCustomerOrderInfo orderInfo = (FDCustomerOrderInfo)iter.next();
					try{
						//Set it to actionInfo object to write to the activity log.
						saleId = orderInfo.getSaleId();
						ErpSaleModel saleModel = sb.getOrder(new PrimaryKey(saleId));
						FDOrderI order =  new FDOrderAdapter(saleModel);
						ErpReturnOrderModel returnModel = getReturnModel(order);
						//Process Full Return.
						this.returnOrder(saleId, returnModel);
						//Approve Full Return.
						this.approveReturn(saleId, returnModel);
						successOrders.add(orderInfo);
						FDIdentity identity = orderInfo.getIdentity();
						//Set it to actionInfo object to write to the activity log.
						info.setIdentity(identity);
						ErpActivityRecord rec = info.createActivity(EnumAccountActivityType.MASS_RETURN);
						this.logActivity(rec);
					}catch(FDResourceException fe){
						fe.printStackTrace();
						LOGGER.error("System Error occurred while processing Sale ID : "+saleId+"\n"+fe.getMessage());
						failureOrders.add(orderInfo);
					}catch(ErpTransactionException te){
						te.printStackTrace();
						LOGGER.error("Transaction Error occurred while processing Sale ID : "+saleId+"\n"+te.getMessage());
						failureOrders.add(orderInfo);
					}
				}
			} catch (RemoteException ex) {
				throw new FDResourceException(ex);
			} catch (CreateException ce) {
				throw new FDResourceException(ce);
			}

			Map results = new HashMap();
			results.put("SUCCESS_ORDERS", successOrders);
			results.put("FAILURE_ORDERS", failureOrders);
			return results;
	}
	private ErpReturnOrderModel getReturnModel(FDOrderI order){
		List returnLines = new ArrayList();
		List orderLines = order.getOrderLines();
		boolean containsDeliveryPass = false;
		for(Iterator iter = orderLines.iterator();iter.hasNext();){
			FDCartLineI line = (FDCartLineI) iter.next();
			ErpInvoiceLineI invoiceLine = line.getInvoiceLine();
			ErpReturnLineModel returnLine = new ErpReturnLineModel();
			returnLine.setLineNumber(invoiceLine.getOrderLineNumber());
			returnLine.setQuantity(invoiceLine.getQuantity());
			//Since it is a full return
			returnLine.setRestockingOnly(false);
			returnLines.add(returnLine);
			if(line.lookupFDProduct().isDeliveryPass()) {
				//Return order contains a delivery pass.
				containsDeliveryPass = true;
			}

		}
		List charges = new ArrayList();
		for(Iterator i = order.getCharges().iterator(); i.hasNext(); ){
			ErpChargeLineModel charge = new ErpChargeLineModel((ErpChargeLineModel)i.next());
			charges.add(charge);
			//Waive all the charges since it is full return.
			if(EnumChargeType.DELIVERY.equals(charge.getType())) {
				charge.setDiscount(new Discount("DELIVERY", EnumDiscountType.PERCENT_OFF, 1.0));
				continue;
			}
			if(EnumChargeType.PHONE.equals(charge.getType())) {
				charge.setDiscount(new Discount(null, EnumDiscountType.PERCENT_OFF, 1.0));
				continue;
			}
			if(EnumChargeType.MISCELLANEOUS.equals(charge.getType())) {
				charge.setDiscount(new Discount("DELIVERY", EnumDiscountType.PERCENT_OFF, 1.0));
				continue;
			}
		}
		ErpReturnOrderModel returnModel = new ErpReturnOrderModel();
		returnModel.setInvoiceLines(returnLines);
		returnModel.setCharges(charges);
		returnModel.setDlvPassApplied(order.isDlvPassApplied());
		returnModel.setDeliveryPassId(order.getDeliveryPassId());
		returnModel.setContainsDeliveryPass(containsDeliveryPass);
		//Since it is a full return
		returnModel.setRestockingApplied(false);
		return returnModel;
	}


	public int fixSettlemnentBatch(String batch_id) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			return AdminToolsDAO.fixSettlemnentBatch(conn, batch_id);
		} catch (SQLException e) {
			LOGGER.error("SQL Error occurred while fixing the settlement batch.");
			throw new FDResourceException(e, "Could not fix settlement batch due to SQL Error.");
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Trouble closing connection after fixing settlement batch.", e);
				}
			}
		}
	}
	
	private static final String INSERT_TOP_FAQS =
		"INSERT INTO CUST.TOP_FAQS(CMSNODE_ID, TIME_STAMP) VALUES(?,?)";
	public void saveTopFaqs(List faqIds) throws FDResourceException, RemoteException{
		Connection conn = null;
		try {
			conn = this.getConnection();
			List lst = new ArrayList();
			Date date = new Date();
			PreparedStatement ps = conn.prepareStatement(INSERT_TOP_FAQS);
			for (Iterator iterator = faqIds.iterator(); iterator.hasNext();) {
				String faqNodeId = (String) iterator.next();
				ps.setString(1, faqNodeId);
				ps.setTimestamp(2, new java.sql.Timestamp(date.getTime()));	
				ps.addBatch();
			}
			int[] result = ps.executeBatch();			
			ps.close();			
		} catch (SQLException sqle) {
			LOGGER.error(sqle.getMessage());
			throw new FDResourceException(sqle);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException sqle) {
					LOGGER.debug("Error while cleaning:", sqle);
				}
			}
		}
	}
   

}
