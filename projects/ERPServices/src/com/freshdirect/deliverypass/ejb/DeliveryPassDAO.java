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

import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.deliverypass.DeliveryPassType;
import com.freshdirect.deliverypass.EnumDlvPassStatus;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SequenceGenerator;
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
	
	private final static String CREATE_DELIVERY_PASS = "INSERT INTO CUST.DELIVERY_PASS(ID, CUSTOMER_ID, TYPE, DESCRIPTION, PURCHASE_DATE, AMOUNT, PURCHASE_ORDER_ID, TOTAL_NUM_DLVS, REM_NUM_DLVS, ORG_EXP_DATE, EXP_DATE, USAGE_CNT, STATUS) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
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
			ps.setDouble(6, model.getAmount());
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
	
	private final static String GET_DELIVERY_PASS_INFO = "SELECT ID, CUSTOMER_ID, TYPE, DESCRIPTION, PURCHASE_DATE, AMOUNT, PURCHASE_ORDER_ID, TOTAL_NUM_DLVS, REM_NUM_DLVS, ORG_EXP_DATE, EXP_DATE, USAGE_CNT, NUM_OF_CREDITS, STATUS from CUST.DELIVERY_PASS where ID = ?";
	
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
	
	private final static String GET_DELIVERY_PASSES = "SELECT ID, CUSTOMER_ID, TYPE, DESCRIPTION, PURCHASE_DATE, AMOUNT, PURCHASE_ORDER_ID, TOTAL_NUM_DLVS, REM_NUM_DLVS, ORG_EXP_DATE, EXP_DATE, USAGE_CNT, NUM_OF_CREDITS, STATUS from CUST.DELIVERY_PASS where CUSTOMER_ID = ? ORDER BY PURCHASE_DATE DESC";
	
	public static List getDeliveryPasses(Connection conn, String customerPk) throws SQLException {
		List deliveryPasses = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(GET_DELIVERY_PASSES);
			ps.setString(1, customerPk);
			rs = ps.executeQuery();
			while(rs.next()){
				if(deliveryPasses == null){
					deliveryPasses = new ArrayList();
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
	
	private final static String GET_DELIVERY_PASSES_BY_STATUS = "SELECT ID, CUSTOMER_ID, TYPE, DESCRIPTION, PURCHASE_DATE, AMOUNT, PURCHASE_ORDER_ID, TOTAL_NUM_DLVS, REM_NUM_DLVS, ORG_EXP_DATE, EXP_DATE, USAGE_CNT, NUM_OF_CREDITS, STATUS from CUST.DELIVERY_PASS where CUSTOMER_ID = ? and STATUS = ? ORDER BY PURCHASE_DATE DESC";
	
	
	
	public static List getDlvPassesByStatus(Connection conn, String customerPk, EnumDlvPassStatus status) throws SQLException {
		List deliveryPasses = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(GET_DELIVERY_PASSES_BY_STATUS);
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
	
	public static Map getAllStatusCount(Connection conn, String customerPk) throws SQLException {
		Map statusCount = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(GET_DELIVERY_PASS_STATUS_COUNT);
			ps.setString(1, customerPk);
			rs = ps.executeQuery();
			while(rs.next()){
				if(statusCount == null){
					statusCount = new HashMap();
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
	private final static String UPDATE_DLV_PASS = "UPDATE CUST.DELIVERY_PASS SET REM_NUM_DLVS = ?, EXP_DATE = ?, USAGE_CNT = ?, STATUS = ?, NUM_OF_CREDITS = ? where ID = ?";
	
	private final static String UPDATE_DLV_PASS_1 = "UPDATE CUST.DELIVERY_PASS SET REM_NUM_DLVS = ?, EXP_DATE = ?, ORG_EXP_DATE = ?, USAGE_CNT = ?, STATUS = ?, NUM_OF_CREDITS = ? where ID = ?";
	
	
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
				ps.setString(7, model.getPK().getId());
				
			}
			else {
				ps = conn.prepareStatement(UPDATE_DLV_PASS);
				//Remaining deliveries is 0 for unlimited pass.
				ps.setInt(1, model.getRemainingDlvs());
				setTimestamp(ps,2,model.getExpirationDate());
				ps.setInt(3, model.getUsageCount());
				ps.setString(4, model.getStatus().getName());
				ps.setInt(5, model.getNoOfCredits());
				ps.setString(6, model.getPK().getId());
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
	private final static String GET_DELIVERY_PASSES_BY_ORDERID = "SELECT ID, CUSTOMER_ID, TYPE, DESCRIPTION, PURCHASE_DATE, AMOUNT, PURCHASE_ORDER_ID, TOTAL_NUM_DLVS, REM_NUM_DLVS, ORG_EXP_DATE, EXP_DATE, USAGE_CNT, NUM_OF_CREDITS, STATUS from CUST.DELIVERY_PASS where PURCHASE_ORDER_ID = ?";
	/**
	 * This method returns list of deliveryPasses that was purchased during a specific order id.
	 * @param conn
	 * @param orderId
	 * @return
	 * @throws SQLException
	 */

	public static List getDeliveryPassesByOrderId(Connection conn, String orderId) throws SQLException{
		List deliveryPasses = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(GET_DELIVERY_PASSES_BY_ORDERID);
			ps.setString(1, orderId);
			rs = ps.executeQuery();
			while(rs.next()){
				if(deliveryPasses == null){
					deliveryPasses = new ArrayList();
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
			ps.setDouble(1,newPrice);
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
		
		return new DeliveryPassModel(pk, customerId, type, description, purchaseOrderId, 
				purchaseDt, amount, totalNoOfDlvs, remNoOfDlvs, orgExpDate, expDate, usageCnt, noOfCredits, status);
	}
	
	private final static String HAS_PURCHASED_PASS_QUERY = "SELECT * FROM CUST.DELIVERY_PASS WHERE CUSTOMER_ID=? AND STATUS IN ('ACT','PEN','RTU','CAN','STF')";
	public static boolean hasPurchasedPass(Connection conn, String customerPK) throws SQLException {
		
		boolean retValue = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			ps = conn.prepareStatement(HAS_PURCHASED_PASS_QUERY);
			ps.setString(1,customerPK);
			rs = ps.executeQuery();
			if (rs.next()) {
				retValue = true;
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
		return retValue;
	}
	
	private final static String GET_USABLE_AUTORENEW_PASSES_QUERY = "SELECT * FROM CUST.DELIVERY_PASS WHERE CUSTOMER_ID=? AND ( STATUS IN ('PEN','RTU')  OR (STATUS='ACT' AND TRUNC(EXP_DATE)>TRUNC(SYSDATE))) AND TYPE IN (SELECT SKU_CODE FROM CUST.DLV_PASS_TYPE WHERE IS_AUTORENEW_DP='Y')";
	public static List getUsableAutoRenewPasses(Connection conn, String customerPK) throws SQLException {
        
		List autoRenewPasses=new ArrayList(5);
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

	private final static String GET_DAYS_SINCE_DP_EXPIRED_QUERY ="select CEIL(sysdate-max(exp_date)) from cust.delivery_pass where customer_id=? and ((status ='ACT' and exp_date<sysdate) OR (status='CAN'))";
	public static int getDaysSinceDPExpiry(Connection conn, String customerID) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int days=0;
		List customer=new ArrayList(10);
		
		try{
			ps = conn.prepareStatement(GET_DAYS_SINCE_DP_EXPIRED_QUERY);
			ps.setString(1, customerID);
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
	
	
	private final static String GET_DAYS_TO_DP_EXPIRED_QUERY="select ceil((select exp_date from cust.delivery_pass where id=?) -sysdate)+"+
	                                                         " NVL((select sum(duration) from cust.delivery_pass dp, cust.dlv_pass_type dpt where customer_id=? and dp.id!=? and dp.type=dpt.SKU_CODE and dp.status IN ('PEN','RTU')),0) as days "
	                                                         +" from cust.delivery_pass where customer_id=? and rownum=1";
	
	public static int getDaysToDPExpiry(Connection conn, String customerID, String dpID) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		int days=0;
		List customer=new ArrayList(10);
		
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

}
