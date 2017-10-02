package com.freshdirect.referral.extole;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.framework.util.DaoUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.referral.extole.model.ExtoleConversionRequest;
import com.freshdirect.referral.extole.model.ExtoleResponse;
import com.freshdirect.referral.extole.model.FDRafCreditModel;

public class FDExtoleManagerDAO implements Serializable {

	/**
	 * Extole DAO Class
	 */
	private static final long serialVersionUID = -7951921765149419771L;
	private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");
	private static final DateFormat TIME_FORMATTER = new SimpleDateFormat("MM/dd/yyyy hh:MM a");

	private static Category LOGGER = LoggerFactory
			.getInstance(FDExtoleManagerDAO.class);

	private static final String UPDATE_RAF_TRANS = "UPDATE CUST.RAF_TRANS SET TRANS_STATUS=?,ERROR_MSG=?, EXTOLE_EVENT_ID=?,TRANS_TIME=? WHERE ID=?";

	// verify the action types and query
	private static final String SELECT_EXTOLE_CREATE_CONVERSION_TRANSACTION = " SELECT RFT.ID AS TRANS_ID, RFT.TRANS_TYPE AS EVENT_TYPE ,"
			+ " SA.CUSTOMER_ID AS PARTNER_USER_ID , S.ID AS PARTNER_CONVERSION_ID , "
			+ "CIF.EMAIL AS EMAIL , CIF.FIRST_NAME AS FIRST_NAME , CIF.LAST_NAME AS LAST_NAME, "
			+ " FDC.RAF_CLICK_ID AS CLICK_ID, FDC.RAF_PROMO_CODE AS RAF_PROMO_CODE "
			+ "FROM  CUST.SALE S, CUST.SALESACTION SA, CUST.RAF_TRANS RFT, CUST.CUSTOMERINFO CIF, CUST.FDCUSTOMER FDC "
			+ " WHERE S.TYPE='REG' AND S.STATUS NOT IN ('CAN','MOC') "
			+ "AND SA.CUSTOMER_ID = S.CUSTOMER_ID AND SA.SALE_ID=S.ID "
			+ "AND SA.ID = RFT.SALESACTION_ID  AND  SA.CUSTOMER_ID=CIF.CUSTOMER_ID "
			+ "AND SA.CUSTOMER_ID=FDC.ERP_CUSTOMER_ID "
			+ "AND RFT.TRANS_STATUS IN('P','F') AND RFT.TRANS_TYPE ='purchase' "
			+ "AND SA.ACTION_TYPE = 'CRO' AND RFT.TRANS_TIME > SYSDATE-30";

	/*
	 * This method is for sending Create Conversion Request to Extole based on
	 * the model api
	 */

	public static List<ExtoleConversionRequest> getExtoleCreateConversionTransactions(
			Connection conn) throws SQLException {
		List<ExtoleConversionRequest> list = new ArrayList<ExtoleConversionRequest>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn
					.prepareStatement(SELECT_EXTOLE_CREATE_CONVERSION_TRANSACTION);
			rs = ps.executeQuery();
			while (rs.next()) {
				ExtoleConversionRequest requestModel = new ExtoleConversionRequest();
				requestModel.setRafTransId(rs.getString("TRANS_ID"));
				requestModel.setEventType(rs.getString("EVENT_TYPE"));
				requestModel.setPartnerUserId(rs.getString("PARTNER_USER_ID"));
				requestModel.setPartnerConversionId(rs.getString("PARTNER_USER_ID"));
				requestModel.setEmail(rs.getString("EMAIL"));
				requestModel.setFirstName(rs.getString("FIRST_NAME"));
				requestModel.setLastName(rs.getString("LAST_NAME"));
				requestModel.setClickId(rs.getString("CLICK_ID"));
			//	requestModel.setCouponCode("RAF_PROMO_CODE");

				list.add(requestModel);
			}

		} finally {
			DaoUtil.close(rs, ps);
		}
		return list;
	}

	/*
	 * This method is for updating Extole CreateConversionResponse,
	 * ApproveConversionResponse
	 */

	public static void updateRafExtoleTransactions(Connection conn,
			ExtoleResponse convResponse) throws SQLException {

		PreparedStatement ps = null;
		ps = conn.prepareStatement(UPDATE_RAF_TRANS);
		try {
			ps.setString(1, convResponse.getStatus());
			if (null != convResponse.getMessage()) {
				ps.setString(2, convResponse.getMessage().length() > 255? convResponse.getMessage().substring(0, 254):convResponse.getMessage());
			} else {
				ps.setNull(2, Types.VARCHAR);
			}
			if (null != convResponse.getEventId()) {
				ps.setString(3, convResponse.getEventId());
			} else {
				ps.setNull(3, Types.VARCHAR);
			}
			// sysdate for transtime
			ps.setTimestamp(4, new java.sql.Timestamp(Calendar.getInstance()
					.getTimeInMillis()));

			ps.setString(5, convResponse.getRafTransId());
			ps.executeQuery();
			ps.close();

		} catch (SQLException e) {
			LOGGER.error("Exception while updating extole response in database " , e);
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	// modified the query to include clickid as we need that to send to extole

	private static final String SELECT_EXTOLE_APPROVE_CONVERSION_TRANSACTION ="SELECT RFT.ID AS TRANS_ID , RFT.EXTOLE_EVENT_ID AS EVENT_ID , " +
"RFT.TRANS_TYPE AS EVENT_TYPE , S.CUSTOMER_ID AS PARTNER_USER_ID , FDC.RAF_CLICK_ID AS CLICK_ID " + 
"FROM CUST.SALE S, CUST.SALESACTION SA, CUST.RAF_TRANS RFT,  CUST.FDCUSTOMER FDC " + 
"WHERE  S.TYPE='REG' AND S.STATUS NOT IN ('CAN','MOC') AND   SA.CUSTOMER_ID = S.CUSTOMER_ID " +
"AND  SA.SALE_ID=S.ID AND   SA.ID = RFT.SALESACTION_ID AND S.CUSTOMER_ID=FDC.ERP_CUSTOMER_ID " +
"AND RFT.TRANS_STATUS IN('P','F') AND  RFT.TRANS_TYPE='approve' AND SA.ACTION_TYPE ='STL' " +
"AND   RFT.TRANS_TIME > SYSDATE-30";
	
	/*
	 * This method is for sending Approve Conversion Request to Extole
	 */

	public static List<ExtoleConversionRequest> getExtoleApproveConversionTransactions(
			Connection conn) throws SQLException {
		List<ExtoleConversionRequest> list = new ArrayList<ExtoleConversionRequest>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			ps = conn
					.prepareStatement(SELECT_EXTOLE_APPROVE_CONVERSION_TRANSACTION);
			rs = ps.executeQuery();
			while (rs.next()) {
				ExtoleConversionRequest requestModel = new ExtoleConversionRequest();
				requestModel.setRafTransId(rs.getString("TRANS_ID"));
				//requestModel.setEventid(rs.getString("EVENT_ID"));
				requestModel.setEventStatus(rs.getString("EVENT_TYPE"));
				requestModel.setPartnerConversionId(rs.getString("PARTNER_USER_ID"));
			//	requestModel.setClickId(rs.getString("CLICK_ID"));
			//	requestModel.setCouponCode("RAF_PROMO_CODE");

				list.add(requestModel);
			}
		} finally {
			DaoUtil.close(rs,ps);
		}
		return list;
	}

	public static void saveExtoleRewards(Connection conn,
		List<FDRafCreditModel> rewards) throws SQLException {
		PreparedStatement ps = null;

		try {
			
			ps = conn
					.prepareStatement("INSERT INTO CUST.RAF_CREDIT "
							+ "(ID,ADVOCATE_CUSTOMER_ID,STATUS,CREATION_TIME,MODIFIED_TIME,"
							+ "   ADVOCATE_FIRST_NAME,ADVOCATE_LAST_NAME,ADVOCATE_EMAIL,ADVOCATE_PARTNER_UID,"
							+ "   FRIEND_FIRST_NAME,FRIEND_LAST_NAME,FRIEND_EMAIL,FRIEND_PARTNER_UID,"
							+ "   REWARD_TYPE,REWARD_DATE,REWARD_SET_NAME,REWARD_SET_ID,REWARD_VALUE,REWARD_DETAIL) "
							+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			FDRafCreditModel earnedReward = null;
			for (Iterator<FDRafCreditModel> iterator = rewards.iterator(); iterator	.hasNext();) {
				earnedReward = (FDRafCreditModel) iterator.next();
				if(null !=earnedReward && null != earnedReward.getAdvocateEmail() && null !=earnedReward.getFriendEmail()){
					int i = 1;
					earnedReward.setId(String.valueOf(getNextId(conn)));
					ps.setString(i++, earnedReward.getId());
					ps.setString(i++, earnedReward.getAdvocateCustomerId());
			 		ps.setString(i++, earnedReward.getStatus().getValue()); 
				  	ps.setTimestamp(i++, new Timestamp(earnedReward.getCreationTime().getTime()));
				  	ps.setTimestamp(i++, new Timestamp(earnedReward.getModifiedTime().getTime()));
				 	ps.setString(i++, earnedReward.getAdvocateFirstName());
					ps.setString(i++, earnedReward.getAdvocateLastName());
					ps.setString(i++, null !=earnedReward.getAdvocateEmail()?earnedReward.getAdvocateEmail().toLowerCase():null);
					ps.setString(i++, earnedReward.getAdvocatePartnerUid());
					ps.setString(i++, earnedReward.getFriendFirstName());
					ps.setString(i++, earnedReward.getFriendLastName());
					ps.setString(i++, null !=earnedReward.getFriendEmail()?earnedReward.getFriendEmail().toLowerCase():null);
					ps.setString(i++, earnedReward.getFriendPartnerUid());
					ps.setString(i++, earnedReward.getRewardType());
					if(null != earnedReward.getRewardDate()){
					ps.setDate(i++,	new Date(earnedReward.getRewardDate().getTime()));
					} else {
					ps.setNull(i++, Types.TIMESTAMP);	
					}
					ps.setString(i++, earnedReward.getRewardSetName());
					ps.setString(i++, earnedReward.getRewardSetId());
					ps.setDouble(i++, earnedReward.getRewardValue());
					ps.setString(i++, earnedReward.getRewardDetail());
	
					try {
						int rowsaffected = ps.executeUpdate();
						if (rowsaffected != 1) {			
							LOGGER.warn(" Could not insert this record for : " + earnedReward.getAdvocateEmail() + "," + earnedReward.getFriendEmail());
						}
					} catch (SQLException e) {
						LOGGER.error(" Could not insert this record for : " + earnedReward.getAdvocateEmail() + "," + earnedReward.getFriendEmail(), e);
					}
				}
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			DaoUtil.close(ps);
		}
	}

	public static int getNextId(Connection conn) throws SQLException {
		int batchNumber = -1;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn
					.prepareStatement("SELECT CUST.RAF_SEQ.nextval FROM DUAL");
			rs = ps.executeQuery();
			if (rs.next()) {
				batchNumber = rs.getInt(1);
			} else {
				LOGGER.error("Unable to get next id from Rewards Sequence.");
				throw new SQLException(
						"Unable to get next id from RAF Credit Table.");
			}
			return batchNumber;
			// return tempSeq++;
		} catch (SQLException e) {
			throw e;
		} finally {
			DaoUtil.close(rs,ps);
		}
	}

}
