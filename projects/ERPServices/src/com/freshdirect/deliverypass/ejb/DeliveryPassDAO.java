/**
 *
 */
package com.freshdirect.deliverypass.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.crm.CrmCaseSubject;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.deliverypass.DeliveryPassType;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.DaoUtil;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author skrishnasamy
 *
 */
public class DeliveryPassDAO {
	private static final Category LOGGER = LoggerFactory.getInstance(DeliveryPassDAO.class);
	/**
	 *
	 */
	public DeliveryPassDAO() {
		super();
	}

	private final static String CREATE_DELIVERY_PASS = "INSERT INTO CUST.DELIVERY_PASS(ID, CUSTOMER_ID, TYPE, DESCRIPTION, PURCHASE_DATE, AMOUNT, PURCHASE_ORDER_ID, TOTAL_NUM_DLVS, REM_NUM_DLVS, ORG_EXP_DATE, EXP_DATE, USAGE_CNT, STATUS, ACTIVATION_DATE) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public static PrimaryKey create(Connection conn, DeliveryPassModel model) throws SQLException {
		PreparedStatement ps = null;
		String id = null;
		try{
			ps = conn.prepareStatement(CREATE_DELIVERY_PASS);
			id = SequenceGenerator.getNextId(conn, "CUST");
			ps.setString(1, id);
			ps.setString(2, model.getCustomerId());
			ps.setString(3, model.getType().getCode());
			ps.setString(4, model.getDescription());
			ps.setTimestamp(5, new Timestamp(model.getPurchaseDate().getTime()));
			//ps.setDouble(6, model.getAmount());
			ps.setBigDecimal(6, new java.math.BigDecimal(model.getAmount()));
			ps.setString(7, model.getPurchaseOrderId());
			ps.setInt(8, model.getTotalNoOfDlvs());
			ps.setInt(9, model.getRemainingDlvs());
			Date orgExpDate = model.getOrgExpirationDate();
			Date expDate = model.getExpirationDate();

			if (orgExpDate != null) {
				ps.setTimestamp(10,new Timestamp(orgExpDate.getTime()));//Original Expiration Date.
				ps.setTimestamp(11,new Timestamp(expDate.getTime()));//Expiration Date

			} else {
				ps.setNull(10, Types.TIMESTAMP);//Original Expiration Date.
				ps.setNull(11, Types.TIMESTAMP);
			}
			ps.setInt(12, model.getUsageCount());
			ps.setString(13, model.getStatus().getName());
			
			if (EnumDlvPassStatus.ACTIVE.equals(model.getStatus())) {
				setTimestamp(ps,14, model.getActivationDate());
			} else {
				setTimestamp(ps, 14, null);
			}
			
			if (ps.executeUpdate() != 1) {
				LOGGER.error("Error creating delivery pass.");
				throw new SQLException("Row not created");
			}
		}catch(SQLException sexp){
			throw sexp;
		}
		finally {
			if(ps != null)
				ps.close();
		}
		return new PrimaryKey(id);
	}

	private final static String GET_DELIVERY_PASS_INFO = "SELECT ID, CUSTOMER_ID, TYPE, DESCRIPTION, PURCHASE_DATE, AMOUNT, PURCHASE_ORDER_ID, TOTAL_NUM_DLVS, REM_NUM_DLVS, ORG_EXP_DATE, EXP_DATE, USAGE_CNT, NUM_OF_CREDITS, STATUS, ACTIVATION_DATE from CUST.DELIVERY_PASS where ID = ?";

	public static DeliveryPassModel getDeliveryPassInfo(Connection conn, PrimaryKey pk) throws SQLException {
		DeliveryPassModel deliveryPassInfo = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(GET_DELIVERY_PASS_INFO);
			ps.setString(1, pk.getId());
			rs = ps.executeQuery();
			if(rs.next()){
				deliveryPassInfo = getDeliveryPass(rs);
			}
		}catch(SQLException sexp){
			throw sexp;
		}
		finally{
			if(rs != null)
				rs.close();
			if(ps != null)
				ps.close();
		}
		return deliveryPassInfo;
	}

	private final static String GET_DELIVERY_PASSES = "SELECT ID, CUSTOMER_ID, TYPE, DESCRIPTION, PURCHASE_DATE, AMOUNT, PURCHASE_ORDER_ID, TOTAL_NUM_DLVS, REM_NUM_DLVS, ORG_EXP_DATE, EXP_DATE, USAGE_CNT, NUM_OF_CREDITS, STATUS, ACTIVATION_DATE from CUST.DELIVERY_PASS where CUSTOMER_ID = ? ORDER BY PURCHASE_DATE DESC";
	
	private final static String GET_DELIVERY_PASS_BY_CUST_AND_ESTORE=" SELECT ID, CUSTOMER_ID, TYPE, DESCRIPTION, PURCHASE_DATE, AMOUNT, PURCHASE_ORDER_ID, TOTAL_NUM_DLVS, REM_NUM_DLVS, ORG_EXP_DATE, EXP_DATE, USAGE_CNT, NUM_OF_CREDITS, STATUS, ACTIVATION_DATE from CUST.DELIVERY_PASS dp, cust.DLV_PASS_TYPE DPT "+
				"where CUSTOMER_ID =? AND DPT.SKU_CODE=DP.TYPE and (DPT.E_STORES is null or DPT.E_STORES LIKE '%?%') ORDER BY PURCHASE_DATE DESC";
	
	public static List<DeliveryPassModel> getDeliveryPasses(Connection conn, String customerPk, EnumEStoreId eStoreId) throws SQLException {
		List<DeliveryPassModel> deliveryPasses = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		try{
			
			query = GET_DELIVERY_PASS_BY_CUST_AND_ESTORE.replace("%?%", "%"+(null != eStoreId ? eStoreId.getContentId() : EnumEStoreId.FD.getContentId())+"%");

			ps = conn.prepareStatement(query);
			ps.setString(1, customerPk);
			rs = ps.executeQuery();
			while(rs.next()){
				if(deliveryPasses == null){
					deliveryPasses = new ArrayList<DeliveryPassModel>();
				}
				deliveryPasses.add(getDeliveryPass(rs));
			}
		}catch(SQLException sexp){
			throw sexp;
		}
		finally{
			if(rs != null)
				rs.close();
			if(ps != null)
				ps.close();
		}
		return deliveryPasses;
	}

	private final static String GET_DELIVERY_PASSES_BY_STATUS = "SELECT ID, CUSTOMER_ID, TYPE, DESCRIPTION, PURCHASE_DATE, AMOUNT, PURCHASE_ORDER_ID, TOTAL_NUM_DLVS, REM_NUM_DLVS, ORG_EXP_DATE, EXP_DATE, USAGE_CNT, NUM_OF_CREDITS, STATUS, ACTIVATION_DATE from CUST.DELIVERY_PASS DP,CUST.DLV_PASS_TYPE DPT where DP.CUSTOMER_ID = ? and DPT.SKU_CODE=DP.TYPE and (DPT.E_STORES is null or DPT.E_STORES LIKE '%?%')and DP.STATUS = ? ORDER BY DP.PURCHASE_DATE DESC";



	public static List<DeliveryPassModel> getDlvPassesByStatus(Connection conn, String customerPk, EnumDlvPassStatus status,EnumEStoreId eStoreId) throws SQLException {
		List<DeliveryPassModel> deliveryPasses = new ArrayList<DeliveryPassModel>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = null;
		try{
			query = GET_DELIVERY_PASSES_BY_STATUS.replace("%?%", "%"+(null != eStoreId ? eStoreId.getContentId() : EnumEStoreId.FD.getContentId())+"%");
			
			ps = conn.prepareStatement(query);
			ps.setString(1, customerPk);
			ps.setString(2, status.getName());
			rs = ps.executeQuery();
			while(rs.next()){
				deliveryPasses.add(getDeliveryPass(rs));
			}
		}catch(SQLException sexp){
			throw sexp;
		}
		finally{
			if(rs != null)
				rs.close();
			if(ps != null)
				ps.close();
		}
		return deliveryPasses;
	}

	private final static String GET_DELIVERY_PASS_STATUS_COUNT = "SELECT  STATUS, count(STATUS) from CUST.DELIVERY_PASS where CUSTOMER_ID = ? GROUP BY STATUS";

	public static Map<EnumDlvPassStatus,Integer> getAllStatusCount(Connection conn, String customerPk) throws SQLException {
		Map<EnumDlvPassStatus,Integer> statusCount = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(GET_DELIVERY_PASS_STATUS_COUNT);
			ps.setString(1, customerPk);
			rs = ps.executeQuery();
			while(rs.next()){
				if(statusCount == null){
					statusCount = new HashMap<EnumDlvPassStatus,Integer>();
				}
				statusCount.put(EnumDlvPassStatus.getEnum(rs.getString(1)), new Integer(rs.getInt(2)));
			}
		}catch(SQLException sexp){
			throw sexp;
		}
		finally{
			if(rs != null)
				rs.close();
			if(ps != null)
				ps.close();
		}
		return statusCount;
	}
	private final static String UPDATE_DLV_PASS = "UPDATE CUST.DELIVERY_PASS SET REM_NUM_DLVS = ?, EXP_DATE = ?, USAGE_CNT = ?, STATUS = ?, NUM_OF_CREDITS = ?, ACTIVATION_DATE =? where ID = ?";

	private final static String UPDATE_DLV_PASS_1 = "UPDATE CUST.DELIVERY_PASS SET REM_NUM_DLVS = ?, EXP_DATE = ?, ORG_EXP_DATE = ?, USAGE_CNT = ?, STATUS = ?, NUM_OF_CREDITS = ?, ACTIVATION_DATE =?  where ID = ?";


	public static boolean update(Connection conn, DeliveryPassModel model, boolean setOrigExpDate) throws SQLException {
		boolean retValue = true;
		PreparedStatement ps = null;
		try{
			
			if(setOrigExpDate) {
				ps = conn.prepareStatement(UPDATE_DLV_PASS_1);
				ps.setInt(1, model.getRemainingDlvs());
				setTimestamp(ps,2,model.getExpirationDate());
				setTimestamp(ps,3,model.getOrgExpirationDate());
				ps.setInt(4, model.getUsageCount());
				ps.setString(5, model.getStatus().getName());
				ps.setInt(6, model.getNoOfCredits());
				if(EnumDlvPassStatus.ACTIVE.equals(model.getStatus())) {
					setTimestamp(ps,7,model.getActivationDate());
				} else {
					setTimestamp(ps,7,null);
				}
				
				ps.setString(8, model.getPK().getId());
				

			}
			else {
				ps = conn.prepareStatement(UPDATE_DLV_PASS);
				//Remaining deliveries is 0 for unlimited pass.
				ps.setInt(1, model.getRemainingDlvs());
				setTimestamp(ps,2,model.getExpirationDate());
				ps.setInt(3, model.getUsageCount());
				ps.setString(4, model.getStatus().getName());
				ps.setInt(5, model.getNoOfCredits());
				if(EnumDlvPassStatus.ACTIVE.equals(model.getStatus()))	{
					setTimestamp(ps,6,model.getActivationDate());
				}
				 else {
						setTimestamp(ps,6,null);
					}
				ps.setString(7, model.getPK().getId());
			}
			if (ps.executeUpdate() <= 0) {
				LOGGER.error("Error updating delivery pass.");
				retValue = false;
			}
		}catch(SQLException sexp){
			throw sexp;
		}
		finally{
			if(ps != null)
				ps.close();
		}
		return retValue;
	}

	private static void setTimestamp(PreparedStatement pstmt, int index, Date date) throws SQLException {

		if(date!=null) {
			pstmt.setTimestamp(index,new Timestamp(date.getTime()));
		}
		else {
			pstmt.setNull(index, Types.TIMESTAMP);
		}
	}

	private final static String DELETE_DLV_PASS = "DELETE FROM CUST.DELIVERY_PASS where ID = ?";

	public static boolean remove(Connection conn, PrimaryKey pk) throws SQLException {
		boolean retValue = true;
		PreparedStatement ps = null;
		try{
			ps = conn.prepareStatement(DELETE_DLV_PASS);
			ps.setString(1, pk.getId());
			if (ps.executeUpdate() <= 0) {
				LOGGER.error("Error removing delivery pass.");
				retValue = false;
			}
		}catch(SQLException sexp){
			throw sexp;
		}
		finally{
			if(ps != null)
				ps.close();
		}
		return retValue;
	}
	private final static String GET_DELIVERY_PASSES_BY_ORDERID = "SELECT ID, CUSTOMER_ID, TYPE, DESCRIPTION, PURCHASE_DATE, AMOUNT, PURCHASE_ORDER_ID, TOTAL_NUM_DLVS, REM_NUM_DLVS, ORG_EXP_DATE, EXP_DATE, USAGE_CNT, NUM_OF_CREDITS, STATUS, ACTIVATION_DATE from CUST.DELIVERY_PASS where PURCHASE_ORDER_ID = ?";
	/**
	 * This method returns list of deliveryPasses that was purchased during a specific order id.
	 * @param conn
	 * @param orderId
	 * @return
	 * @throws SQLException
	 */

	public static List<DeliveryPassModel> getDeliveryPassesByOrderId(Connection conn, String orderId) throws SQLException{
		List<DeliveryPassModel> deliveryPasses = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(GET_DELIVERY_PASSES_BY_ORDERID);
			ps.setString(1, orderId);
			rs = ps.executeQuery();
			while(rs.next()){
				if(deliveryPasses == null){
					deliveryPasses = new ArrayList<DeliveryPassModel>();
				}
				deliveryPasses.add(getDeliveryPass(rs));
			}
		}catch(SQLException sexp){
			throw sexp;
		}
		finally{
			if(rs != null)
				rs.close();
			if(ps != null)
				ps.close();
		}
		return deliveryPasses;
	}

	private final static String UPDATE_PRICE = "UPDATE CUST.DELIVERY_PASS SET AMOUNT = ? where ID = ?";

	public static boolean updatePrice(Connection conn, DeliveryPassModel model, double newPrice) throws SQLException {
		boolean retValue = true;
		PreparedStatement ps = null;
		try{
			ps = conn.prepareStatement(UPDATE_PRICE);
			//ps.setDouble(1,newPrice);
			ps.setBigDecimal(1,new java.math.BigDecimal(newPrice));
			ps.setString(2,model.getPK().getId());
			if (ps.executeUpdate() <= 0) {
				LOGGER.error("Error updating price for delivery pass.");
				retValue = false;
			}
		}catch(SQLException sexp){
			throw sexp;
		}
		finally{

			if(ps != null)
				ps.close();
		}
		return retValue;
	}

	private static DeliveryPassModel getDeliveryPass(ResultSet rs) throws SQLException {
		PrimaryKey pk = new PrimaryKey(rs.getString("ID"));
		String customerId = rs.getString("CUSTOMER_ID");
		DeliveryPassType type = DeliveryPassType.getEnum(rs.getString("TYPE"));
		String description = rs.getString("DESCRIPTION");
		String purchaseOrderId = rs.getString("PURCHASE_ORDER_ID");
		Date purchaseDt = rs.getTimestamp("PURCHASE_DATE");
		double amount = rs.getDouble("AMOUNT");

		int totalNoOfDlvs = rs.getInt("TOTAL_NUM_DLVS");
		int remNoOfDlvs = rs.getInt("REM_NUM_DLVS");
		Date expDate = rs.getTimestamp("EXP_DATE");
		Date orgExpDate = rs.getTimestamp("ORG_EXP_DATE");
		int usageCnt = rs.getInt("USAGE_CNT");
		int noOfCredits = rs.getInt("NUM_OF_CREDITS");
		EnumDlvPassStatus status = EnumDlvPassStatus.getEnum(rs.getString("STATUS"));
		Date activationDate = rs.getTimestamp("ACTIVATION_DATE");

		return new DeliveryPassModel(pk, customerId, type, description, purchaseOrderId,
				purchaseDt, amount, totalNoOfDlvs, remNoOfDlvs, orgExpDate, expDate, usageCnt, noOfCredits, status, activationDate);
	}

	private final static String HAS_PURCHASED_PASS_QUERY = "SELECT * FROM CUST.DELIVERY_PASS WHERE CUSTOMER_ID=? AND STATUS IN ('ACT','PEN','RTU','CAN','STF')";
	public static boolean hasPurchasedPass(Connection conn, String customerPK) throws SQLException {

		boolean retValue = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(HAS_PURCHASED_PASS_QUERY);
			ps.setString(1, customerPK);
			rs = ps.executeQuery();
			if (rs.next()) {
				retValue = true;
			}
		} catch (SQLException sexp) {
			throw sexp;
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
		return retValue;
	}

	private final static String GET_USABLE_AUTORENEW_PASSES_QUERY = "SELECT * FROM CUST.DELIVERY_PASS WHERE CUSTOMER_ID=? AND ( STATUS IN ('PEN','RTU')  OR (STATUS='ACT' AND TRUNC(EXP_DATE)>TRUNC(SYSDATE))) AND TYPE IN (SELECT SKU_CODE FROM CUST.DLV_PASS_TYPE WHERE IS_AUTORENEW_DP='Y')";

	public static List<DeliveryPassModel> getUsableAutoRenewPasses(Connection conn, String customerPK) throws SQLException {
		List<DeliveryPassModel> autoRenewPasses=new ArrayList<DeliveryPassModel>(5);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(GET_USABLE_AUTORENEW_PASSES_QUERY);
			ps.setString(1,customerPK);
			rs = ps.executeQuery();
			while (rs.next()) {
				autoRenewPasses.add(getDeliveryPass(rs));
			}
		}catch(SQLException sexp){
			throw sexp;
		}
		finally{
			if(rs != null)
				rs.close();
			if(ps != null)
				ps.close();
		}
		return autoRenewPasses;


	}

	private final static String GET_AUTORENEWAL_INFO_QUERY = "select ci.customer_id,ci.autorenew_dp_type from cust.customerinfo ci, cust.customer c  where "+
															 "ci.customer_id=c.id and c.ACTIVE='1' and ci.HAS_AUTORENEW_DP='Y' and "+
															 " exists ( select 1 from cust.delivery_pass dp where dp.customer_id=ci.customer_id "+
															 "          and status IN ('ACT','RTU') and trunc(exp_date)<=trunc(sysdate-1) and dp.TYPE IN "+
															 "          (select sku_code from cust.dlv_pass_type where is_autorenew_dp='Y') "+
															 "        )"+
															 " and not exists ( select 1 from cust.delivery_pass dp where dp.customer_id=ci.customer_id "+
															 "                  and status IN ('ACT','RTU','PEN') and trunc(exp_date)>trunc(sysdate-1) "+
															 "                 ) "+
															 " and not exists ( select 1 from cust.case where customer_id=ci.customer_id and case_subject='DPQ-009' and case_state<>'CLSD')";
	
	
	/* DlvPass Query to fetch records based on FDCUSTOMER_ESTORE table, only if this property is True [ isDlvPassFDXEnabled() ] */
	private final static String GET_AUTORENEWAL_INFO_BY_ESTORE_QUERY = "select ci.customer_id,fdc.autorenew_dp_type from cust.customerinfo ci, cust.customer c, cust.fdcustomer fd, cust.fdcustomer_estore fdc  where "+
			 "ci.customer_id=c.id and c.ACTIVE='1' and fd.erp_customer_id=c.id and fdc.fdcustomer_id=fd.id and fdc.HAS_AUTORENEW_DP='Y' and fdc.e_store=? and "+
			 " exists ( select 1 from cust.delivery_pass dp where dp.customer_id=ci.customer_id "+
			 "          and status IN ('ACT','RTU') and trunc(exp_date)<=trunc(sysdate-1) and dp.TYPE IN "+
			 "          (select sku_code from cust.dlv_pass_type where is_autorenew_dp='Y' and e_stores =?) "+
			 "        )"+
			 " and not exists ( select 1 from cust.delivery_pass dp where dp.customer_id=ci.customer_id "+
			 "                  and status IN ('ACT','RTU','PEN') and trunc(exp_date)>trunc(sysdate-1) and dp.TYPE IN "+
			 "          (select sku_code from cust.dlv_pass_type where e_stores =?) "+
			 "                 ) "+
			 " and not exists ( select 1 from cust.case where customer_id=ci.customer_id and case_subject=? and case_state<>'CLSD')";
	
	
	public static Object[] getAutoRenewalInfo(Connection conn , EnumEStoreId eStore) throws SQLException{
		PreparedStatement ps = null;
		ResultSet rs = null;
		Object[] autoRenewalInfo=new Object[2];
		List<String> customer=new ArrayList<String>(10);
		List<String> autoRenewalSKU=new ArrayList<String>(10);
		autoRenewalInfo[0]=customer;
		autoRenewalInfo[1]=autoRenewalSKU;
		
		try{
//			if (FDStoreProperties.isDlvPassFDXEnabled()) {
				/* FOOD-373 */
				String eStoreId = eStore.getContentId().toString();
				String defaultRenewalSKU = null;
				String caseSubject = null;
				if (EnumEStoreId.FDX.getContentId().equalsIgnoreCase(eStoreId)) {
					defaultRenewalSKU = FDStoreProperties.getDefaultRenewalDPforFDX();
					caseSubject = CrmCaseSubject.CODE_AUTO_BILL_PAYMENT_MISSING_FK;
				} else {
					defaultRenewalSKU = FDStoreProperties.getDefaultRenewalDPforFD();
					caseSubject = CrmCaseSubject.CODE_AUTO_BILL_PAYMENT_MISSING;
				}
				ps = conn.prepareStatement(GET_AUTORENEWAL_INFO_BY_ESTORE_QUERY);
				ps.setString(1, eStoreId);
				ps.setString(2, eStoreId);
				ps.setString(3, eStoreId);
				ps.setString(4, caseSubject);
				rs = ps.executeQuery();

				LOGGER.info("DeliveryPass Auto renew job running, property: isDlvPassFDXEnabled() -> "+true+" ,EStore ID: "+eStore.getContentId().toString()+
						" , default renewal SKU: "+defaultRenewalSKU);
				while (rs.next()) {
					customer.add(rs.getString(1));
					if ((rs.getString(2) != null) && !("".equals(rs.getString(2)))) {
						autoRenewalSKU.add(rs.getString(2));
					} else {
						autoRenewalSKU.add(defaultRenewalSKU);
					}
				}
			/*} else {
				ps = conn.prepareStatement(GET_AUTORENEWAL_INFO_QUERY);
				rs = ps.executeQuery();

				String defaultRenewalSKU = FDStoreProperties.getDefaultRenewalDPforFD();
				LOGGER.info("DeliveryPass Auto renew job running, property: isDlvPassFDXEnabled() -> "+false+ " , renewal SKU: "+defaultRenewalSKU);
				while (rs.next()) {
					customer.add(rs.getString(1));
					if ((rs.getString(2) != null) && !("".equals(rs.getString(2)))) {
						autoRenewalSKU.add(rs.getString(2));
					} else {
						autoRenewalSKU.add(defaultRenewalSKU);
					}
				}
			}*/
			return autoRenewalInfo;
		}catch(SQLException sexp){
			throw sexp;
		}
		finally{
			DaoUtil.close(rs);
			DaoUtil.close(ps);
		}
	}


	private final static String GET_DAYS_SINCE_DP_EXPIRED_QUERY = "select CEIL(sysdate-max(exp_date)) from cust.delivery_pass dp, cust.dlv_pass_type dpt where customer_id=? and DPT.SKU_CODE=DP.TYPE and (DPT.E_STORES is null or DPT.E_STORES LIKE '%?%') and((status ='ACT' and exp_date<sysdate) OR (status='CAN'))";

	public static int getDaysSinceDPExpiry(Connection conn, String customerID, EnumEStoreId eStore)
			throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int days = 0;
		String query = null;
		try {
			query = GET_DAYS_SINCE_DP_EXPIRED_QUERY.replace("%?%",
					"%" + (null != eStore ? eStore.getContentId() : EnumEStoreId.FD.getContentId()) + "%");
			ps = conn.prepareStatement(query);
			ps.setString(1, customerID);
			rs = ps.executeQuery();

			if (rs.next()) {
				days = rs.getInt(1);
			}
			return days;
		} catch (SQLException sexp) {
			throw sexp;
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}

	}


	private final static String GET_DAYS_TO_DP_EXPIRED_QUERY="select ceil((select exp_date from cust.delivery_pass where id=?) -sysdate)+"+
	                                                         " NVL((select sum(duration) from cust.delivery_pass dp, cust.dlv_pass_type dpt where customer_id=? and dp.id!=? and dp.type=dpt.SKU_CODE and dp.status IN ('PEN','RTU')),0) as days "
	                                                         +" from cust.delivery_pass where customer_id=? and rownum=1";

	public static int getDaysToDPExpiry(Connection conn, String customerID, String dpID) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int days=0;

		try{
			ps = conn.prepareStatement(GET_DAYS_TO_DP_EXPIRED_QUERY);
			ps.setString(1, dpID);
			ps.setString(2, customerID);
			ps.setString(3, dpID);
			ps.setString(4, customerID);

			rs = ps.executeQuery();

			if(rs.next()) {
				days=rs.getInt(1);
			}
			return days;
		}catch(SQLException sexp){
			throw sexp;
		}
		finally{
			if(rs != null)
				rs.close();
			if(ps != null)
				ps.close();
		}

	}

	 private static String GET_OPEN_CASES_FOR_MISSING_PAYMENTS=
	" SELECT  INITCAP(CI.LAST_NAME) ||\',\'||INITCAP(CI.FIRST_NAME) as \"Customer Name\", "+
    " C.USER_ID AS \"User Id\", case.summary AS \"DP Renewal failure reason\" , CASE.CREATE_DATE as \"Failed On\", "+
    " FROM cust.case case, cust.customer c, cust.customerinfo ci "+
    " WHERE c.id=case.customer_id AND case_subject='DPQ-009' and  case_state<>'CLSD' "+
    " AND create_date BETWEEN TRUNC(SYSDATE-30) and SYSDATE "+
    " AND c.id=CI.CUSTOMER_ID "+
    " ORDER BY TRUNC(CASE.CREATE_DATE) DESC , INITCAP(CI.LAST_NAME) ||','||INITCAP(CI.FIRST_NAME) ASC";
	public static List<List<String>> getDPCustomersWithMissingPayments(Connection conn) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<List<String>> customers=new ArrayList<List<String>>(50);
		List<String> custInfo=new ArrayList<String>(4);
		try{
			ps = conn.prepareStatement(GET_OPEN_CASES_FOR_MISSING_PAYMENTS);
			rs = ps.executeQuery();
			while(rs.next()) {
				custInfo.add(rs.getString(1));
				custInfo.add(rs.getString(2));
				custInfo.add(rs.getString(3));
				custInfo.add(rs.getString(4));
				customers.add(custInfo);
				custInfo=new ArrayList<String>(4);
			}
			return customers;
		}catch(SQLException exp){
			throw exp;
		}
		finally{
			if(rs != null)
				rs.close();
			if(ps != null)
				ps.close();
		}
	}
	private static String GET_PENDING_DP="select INITCAP(CI.LAST_NAME) ||','||INITCAP(CI.FIRST_NAME) as \"Customer Name\", C.USER_ID AS \"User Id\",s.id as \"Order #\","+
    " case s.type when 'REG' then 'Regular' when 'SUB' then 'DP Renewal' else s.type end as \"Order Type\", "+
	" case s.status when 'ENR' then 'Enroute'  when 'STF' then 'Settlement Failed' when 'STL' then 'Settled'  "+
	" when 'AUF' then 'Auth Fail' when 'CPG' then 'Capture Pending' "+
	" else s.status end"+
	" as \"Order Status\", to_char(sa.requested_date,'MM-DD-YYYY') as \"Delivery Date\""+
	"             from cust.customer c, cust.customerinfo ci, CUST.DELIVERY_PASS dp, cust.sale s, cust.salesaction sa where DP.STATUS='PEN'"+
	" and s.id=DP.PURCHASE_ORDER_ID and s.customer_id=DP.CUSTOMER_ID and c.id=CI.CUSTOMER_ID and s.customer_id=c.id"+
	" and s.cromod_date=sa.action_date and sa.action_type in ('CRO','MOD') and s.id=sa.sale_id  and s.e_store=? "+
	" and SA.REQUESTED_DATE<trunc(sysdate) order by SA.REQUESTED_DATE, INITCAP(CI.LAST_NAME) ||','||INITCAP(CI.FIRST_NAME)";

	public static List<List<String>> getPendingPasses(Connection conn, EnumEStoreId eStore) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<List<String>> customers=new ArrayList<List<String>>(50);
		List<String> custInfo=new ArrayList<String>(4);
		try{
			ps = conn.prepareStatement(GET_PENDING_DP);
			ps.setString(1, eStore.getContentId().toString());
			rs = ps.executeQuery();
			while(rs.next()) {
				custInfo.add(rs.getString(1));
				custInfo.add(rs.getString(2));
				custInfo.add(rs.getString(3));
				custInfo.add(rs.getString(4));
				custInfo.add(rs.getString(5));
				custInfo.add(rs.getString(6));
				customers.add(custInfo);
				custInfo=new ArrayList<String>(4);
			}
			return customers;
		}catch(SQLException exp){
			throw exp;
		}
		finally{
			if(rs != null)
				rs.close();
			if(ps != null)
				ps.close();
		}
	}

	private static String FREE_TRIAL_SUBS_ORDER_CUST_SQL = "select distinct c.id from cust.fdcustomer fd,CUST.FDCUSTOMER_ESTORE fde, cust.sale s, cust.salesaction sa, cust.deliveryinfo di ,cust.customer c "+
             "where fde.FDCUSTOMER_ID =fd.id and fde.e_store='FreshDirect' and s.customer_id=fd.erp_customer_id and s.type='REG' and sa.action_date > sysdate-60 "+
             "and s.id=sa.sale_id and s.cromod_date=sa.action_date and sa.action_type in('CRO','MOD') and di.salesaction_id=sa.id and di.delivery_type='H' "+
             "and FDE.DP_FREE_TRIAL_OPTIN is not null and FDE.DP_FREE_TRIAL_OPTIN='Y' and SA.ACTION_DATE > FDE.DP_FREE_TRIAL_OPTIN_DATE and not exists (select id from cust.delivery_pass dp " +
             "where dp.customer_id=fd.erp_customer_id and dp.status not in ('CAO','CAN'))  and c.id=s.customer_id and c.id=fd.erp_customer_id and c.active='1' and exists (select 1 from cust.paymentmethod pm where pm.customer_id=c.id) ";

	public static List<String> getAllCustIdsOfFreeTrialSubsOrder(Connection conn) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> custInfo = new ArrayList<String>(10);
		try {
			ps = conn.prepareStatement(FREE_TRIAL_SUBS_ORDER_CUST_SQL);
			rs = ps.executeQuery();
			while (rs.next()) {
				custInfo.add(rs.getString(1));

			}
			return custInfo;
		} catch (SQLException exp) {
			throw exp;
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
	}
	
	private final static String UPDATE_DLV_PASS_ACTIVATION = "UPDATE cust.delivery_pass dp SET status='RTU' WHERE status IN ('PEN','STF') AND exists (select 1 from cust.sale s where s.id=DP.PURCHASE_ORDER_ID and s.status IN ( 'PPG','STP','STL'))  and purchase_order_id = ?";
	
	public static boolean updateDeliveryPassActivation(Connection conn, String saleId) throws SQLException {
		boolean retvalue=false;
		PreparedStatement ps = null;
		try{
			ps=conn.prepareStatement(UPDATE_DLV_PASS_ACTIVATION);
			ps.setString(1, saleId);
			if (ps.executeUpdate() <= 0) {
				LOGGER.error("Error updating delivery pass.");

			}
		}catch(SQLException exp){
			throw exp;
		}
		finally{
			if(ps != null)
				ps.close();
		}
		return retvalue;
	}

}
