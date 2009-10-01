/*
 * FDSurveySessionBean.java
 *
 * Created on March 11, 2002, 7:39 PM
 */

package com.freshdirect.giftcard.ejb;

/**
 *
 * @author  skrishnasamy
 * @version 
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.common.customer.EnumCardType;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpComplaintReason;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.giftcard.ErpRecipentModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.GenericSearchCriteria;
import com.freshdirect.framework.util.TextEncryptor;
import com.freshdirect.giftcard.EnumGCDeliveryMode;
import com.freshdirect.giftcard.ErpGCDlvInformationHolder;
import com.freshdirect.giftcard.ErpGiftCardI;
import com.freshdirect.giftcard.ErpGiftCardModel;
import com.freshdirect.giftcard.ErpGiftCardAuthModel;
import com.freshdirect.giftcard.ErpGiftCardUtil;
import com.freshdirect.giftcard.ErpPreAuthGiftCardModel;
import com.freshdirect.payment.EnumGiftCardTransactionType;
import com.freshdirect.payment.EnumPaymentMethodType;

public class GiftCardPersistanceDAO {

	private static String INSERT = "INSERT INTO CUST.GIFT_CARD_RECIPIENT( "
			+ "ID,CUSTOMER_ID,SENDER_NAME,SENDER_EMAIL,RECIP_NAME,RECIP_EMAIL,TEMPLATE_ID,DELIVERY_MODE,AMOUNT,PERSONAL_MSG,SALESACTION_ID,ORDERLINE_NUMBER)"
			+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";

	private static String nvl(String str) {
		return (str == null) ? "" : str;
	}

	public static void storeRecipents(Connection conn, String customerId,
			String saleId, List recipentList) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(INSERT);

		for (int i = 0; i < recipentList.size(); i++) {
			ErpRecipentModel model = (ErpRecipentModel) recipentList.get(i);

			String id = SequenceGenerator.getNextId(conn, "CUST");

			ps.setString(1, id);
			ps.setString(2, nvl(customerId));
			ps.setString(3, nvl(model.getSenderName()));
			ps.setString(4, nvl(model.getSenderEmail()));
			ps.setString(5, nvl(model.getRecipientName()));
			ps.setString(6, nvl(model.getRecipientEmail()));
			ps.setString(7, nvl(model.getTemplateId()));
			ps.setString(8, nvl(model.getDeliveryMode().getName()));
			ps.setDouble(9, model.getAmount());
			ps.setString(10, nvl(model.getPersonalMessage()));
			ps.setString(11, nvl(saleId));
			ps.setString(12, nvl(model.getOrderLineId()));
			ps.addBatch();
		}

		int num[] = ps.executeBatch();
		ps.close();
	}
	/*
	private static final String SELECT_GC_RECIPENTS_SQL = "SELECT GC.GIVEX_NUM, GCR.ID as ID, "
			+ "GCR.CUSTOMER_ID,GC.SALE_ID, GCR.SENDER_NAME,GCR.SENDER_EMAIL,GCR.RECIP_NAME,GCR.RECIP_EMAIL,GCR.TEMPLATE_ID,GCR.DELIVERY_MODE, GCR.AMOUNT GC_AMOUNT,GCR.PERSONAL_MSG,GCR.ORDERLINE_NUMBER, "
			+ "GDCC.GIFT_CARD_ID,GDCC.DELIVERY_MODE,GDCC.EMAIL_SENT, SA.ACTION_DATE "
			+ "FROM  CUST.SALESACTION SA, CUST.GIFT_CARD_RECIPIENT GCR,CUST.GIFT_CARD_DELIVERY_INFO GDCC, "
			+ "CUST.SALESACTION SA1, CUST.GIFT_CARD GC WHERE "
			+ "SA.CUSTOMER_ID = SA1.CUSTOMER_ID AND SA.SALE_ID=SA1.SALE_ID "
			+ "AND SA1.ID=GDCC.SALESACTION_ID AND GCR.ID=GDCC.RECIPIENT_ID AND GC.ID = GDCC.GIFT_CARD_ID "
			+ "AND SA.ACTION_TYPE='CRO' AND SA1.ACTION_TYPE='GCD' "
			+ "AND SA.CUSTOMER_ID = ?";
	*/
	
	private static final String SELECT_GC_RECIPENTS_SQL =  "SELECT REC_ID, DL_REC_ID, GIFT_CARD_ID, GIVEX_NUM, "+
	"CUSTOMER_ID,SALE_ID,SENDER_NAME,SENDER_EMAIL,RECIP_NAME,RECIP_EMAIL,TEMPLATE_ID, "+
	"DELIVERY_MODE, AMOUNT ,PERSONAL_MSG,ORDERLINE_NUMBER, DELIVERY_MODE,ACTION_DATE, "+
	"PURCHASE_DATE FROM ( "+
	"SELECT GCR.ID as REC_ID,  "+
	"GCR.CUSTOMER_ID,S.ID AS SALE_ID, GCR.SENDER_NAME,GCR.SENDER_EMAIL,GCR.RECIP_NAME,GCR.RECIP_EMAIL,GCR.TEMPLATE_ID, "+
	"GCR.AMOUNT ,GCR.PERSONAL_MSG,GCR.ORDERLINE_NUMBER, GCR.DELIVERY_MODE,SA.ACTION_DATE, "+
	"(SELECT ACTION_DATE FROM CUST.SALESACTION WHERE SALE_ID = S.ID AND ACTION_TYPE = 'CRO') AS PURCHASE_DATE "+
	"FROM  CUST.SALE S, CUST.SALESACTION SA, CUST.GIFT_CARD_RECIPIENT GCR "+
	"WHERE S.CUSTOMER_ID = SA.CUSTOMER_ID "+
	"and S.ID=SA.SALE_ID "+
	"and S.TYPE = 'GCD' "+
	"and SA.ID = GCR.SALESACTION_ID "+
	"and SA.ACTION_TYPE IN ('CRO','GCE') "+
	"and sa.ACTION_DATE = (select max(action_date) from cust.salesaction sa1, cust.gift_card_recipient gcr1 " +
	"where sa1.sale_id = s.id and sa1.id = gcr1.salesaction_id and gcr1.orderline_number = gcr.orderline_number and ACTION_TYPE IN ('CRO','GCE')) " + 
	"AND S.CUSTOMER_ID = ? ) CR, "+
	"(SELECT  GCD.GIFT_CARD_ID, GC.GIVEX_NUM, GCD.RECIPIENT_ID DL_REC_ID  "+
	"from CUST.SALE S, CUST.SALESACTION SA, CUST.GIFT_CARD GC, CUST.GIFT_CARD_DELIVERY_INFO GCD "+
	"WHERE S.CUSTOMER_ID = SA.CUSTOMER_ID "+
	"and S.ID=SA.SALE_ID "+
	"and S.TYPE = 'GCD' "+
	"and SA.ID = GCD.SALESACTION_ID "+
	"AND GC.ID = GCD.GIFT_CARD_ID "+
	"and SA.ACTION_TYPE IN ('GCD', 'GCE') "+
	"and sa.ACTION_DATE = (select max(action_date) from cust.salesaction sa1, CUST.GIFT_CARD_DELIVERY_INFO GCD1 "+
	"where sa1.sale_id = s.id and sa1.id = gcd1.salesaction_id and gcd1.recipient_id = gcd.recipient_id and ACTION_TYPE IN ('GCD','GCE')) "+
	"AND S.CUSTOMER_ID = ?) DL "+
	"WHERE REC_ID = DL_REC_ID(+) AND AMOUNT > 0";
	
	public static List loadGiftCardRecipents(Connection conn, String customerId)
			throws SQLException {
		List recipentList = new ArrayList();
		PreparedStatement ps = conn.prepareStatement(SELECT_GC_RECIPENTS_SQL);
		ps.setString(1, customerId);
		ps.setString(2, customerId);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpGCDlvInformationHolder holder = new ErpGCDlvInformationHolder();
			if(null != rs.getString("DL_REC_ID"))
				holder.setId(rs.getString("DL_REC_ID"));
			ErpRecipentModel recModel = new ErpRecipentModel();
			recModel.setId(rs.getString("REC_ID"));
			recModel.setCustomerId(rs.getString("CUSTOMER_ID"));
			recModel.setSenderName(rs.getString("SENDER_NAME"));
			recModel.setSenderEmail(rs.getString("SENDER_EMAIL"));
			recModel.setRecipientName(rs.getString("RECIP_NAME"));
			recModel.setRecipientEmail(rs.getString("RECIP_EMAIL"));
			recModel.setSale_id(rs.getString("SALE_ID"));
			recModel.setTemplateId(rs.getString("TEMPLATE_ID"));
			recModel.setDeliveryMode(EnumGCDeliveryMode.getEnum(rs
					.getString("DELIVERY_MODE")));
			recModel.setPersonalMessage(rs.getString("PERSONAL_MSG"));
			recModel.setAmount(rs.getDouble("AMOUNT"));
			recModel.setOrderLineId(rs.getString("ORDERLINE_NUMBER"));
			if(null != rs.getString("GIFT_CARD_ID"))
				holder.setGiftCardId(rs.getString("GIFT_CARD_ID"));
			if(null != rs.getString("GIVEX_NUM"))
				holder.setGivexNum(ErpGiftCardUtil.decryptGivexNum(rs.getString("GIVEX_NUM")));
			holder.setPurchaseDate(rs.getDate("PURCHASE_DATE"));
			holder.setRecepientModel(recModel);
			recipentList.add(holder);
		}

		rs.close();
		ps.close();
		return recipentList;
	}

	
	
	private static final String SELECT_GC_GIVEX_SQL= " SELECT GC.GIVEX_NUM, GCR.ID AS ID, "+
														 " GCR.CUSTOMER_ID,GC.SALE_ID, GCR.SENDER_NAME,GCR.SENDER_EMAIL,GCR.RECIP_NAME,GCR.RECIP_EMAIL,GCR.TEMPLATE_ID,GCR.DELIVERY_MODE, GCR.AMOUNT GC_AMOUNT,GCR.PERSONAL_MSG,GCR.ORDERLINE_NUMBER, "+  
														 " GDCC.GIFT_CARD_ID,GDCC.DELIVERY_MODE,GDCC.EMAIL_SENT, SA.ACTION_DATE "+
														 " FROM  CUST.SALESACTION SA, CUST.GIFT_CARD_RECIPIENT GCR,CUST.GIFT_CARD_DELIVERY_INFO GDCC, "+
														 " CUST.GIFT_CARD GC "+ 
														 " WHERE (GC.CERTIFICATE_NUM =? OR GC.GIVEX_NUM=? ) AND "+
														 " GC.SALE_ID=SA.SALE_ID AND "+
														 " SA.ACTION_TYPE='GCD' AND "+
														 " SA.ID=GDCC.SALESACTION_ID AND "+
														 " GCR.ID=GDCC.RECIPIENT_ID AND "+
														 " GC.ID = GDCC.GIFT_CARD_ID ";

	private static final String SELECT_GC_RECIP_SQL= "SELECT DISTINCT GC.SALE_ID, GC.GIVEX_NUM, GCR.ID AS ID, "+
													 " GCR.CUSTOMER_ID, GCR.SENDER_NAME,GCR.SENDER_EMAIL,GCR.RECIP_NAME,GCR.RECIP_EMAIL,GCR.TEMPLATE_ID,GCR.DELIVERY_MODE, GCR.AMOUNT GC_AMOUNT,GCR.PERSONAL_MSG,GCR.ORDERLINE_NUMBER, "+  
													 " GDCC.GIFT_CARD_ID,GDCC.DELIVERY_MODE,GDCC.EMAIL_SENT, SA.ACTION_DATE FROM "+  
													 " CUST.SALESACTION SA, CUST.GIFT_CARD_RECIPIENT GCR,CUST.GIFT_CARD_DELIVERY_INFO GDCC, "+
													 " CUST.GIFT_CARD GC, "+ 
													 " (SELECT DISTINCT SALE_ID FROM "+
													 " CUST.SALESACTION SA, CUST.GIFT_CARD_RECIPIENT GCR "+
													 " WHERE GCR.RECIP_EMAIL=? AND "+
													 " GCR.SALESACTION_ID=SA.ID group by SA.SALE_ID) O WHERE O.SALE_ID=GC.SALE_ID AND "+
													 " GC.SALE_ID=SA.SALE_ID AND SA.ACTION_TYPE='GCD' AND SA.ID=GDCC.SALESACTION_ID AND GCR.ID=GDCC.RECIPIENT_ID AND GC.ID = GDCC.GIFT_CARD_ID AND GCR.RECIP_EMAIL=? ";

	
	public static List loadGiftCardRecipents(Connection conn, GenericSearchCriteria resvCriteria) throws SQLException{
		 
		    List list=new ArrayList();
			String givexNum = (String) resvCriteria.getCriteriaMap().get("gcNumber");
			String certNum = (String) resvCriteria.getCriteriaMap().get("certNum");
			String recEmail = (String) resvCriteria.getCriteriaMap().get("recEmail");			
			PreparedStatement ps=null;
			if(givexNum!=null && givexNum.trim().length()>0){
			   ps = conn.prepareStatement(SELECT_GC_GIVEX_SQL);
			   ps.setString(1, "");
			   ps.setString(2, ErpGiftCardUtil.encryptGivexNum(givexNum));
			}
			else if(certNum!=null && certNum.trim().length()>0){
				ps = conn.prepareStatement(SELECT_GC_GIVEX_SQL);
				   ps.setString(1, certNum);
				   ps.setString(2, "");
			}
			else{
				ps = conn.prepareStatement(SELECT_GC_RECIP_SQL);
				ps.setString(1, recEmail);
				ps.setString(2, recEmail);
			}
			ResultSet rs = ps.executeQuery();			
			while (rs.next()) {
				ErpGCDlvInformationHolder holder=new ErpGCDlvInformationHolder();
				ErpRecipentModel recModel=new ErpRecipentModel();					
				recModel.setId(rs.getString("ID"));
				recModel.setCustomerId(rs.getString("CUSTOMER_ID"));
				recModel.setSenderName(rs.getString("SENDER_NAME"));
				recModel.setSenderEmail(rs.getString("SENDER_EMAIL"));
				recModel.setRecipientName(rs.getString("RECIP_NAME"));
				recModel.setRecipientEmail(rs.getString("RECIP_EMAIL"));
				recModel.setSale_id(rs.getString("SALE_ID"));
				recModel.setTemplateId(rs.getString("TEMPLATE_ID"));
				recModel.setDeliveryMode(EnumGCDeliveryMode.getEnum(rs.getString("DELIVERY_MODE")));
				recModel.setPersonalMessage(rs.getString("PERSONAL_MSG"));
				recModel.setAmount(rs.getDouble("GC_AMOUNT"));
				recModel.setOrderLineId(rs.getString("ORDERLINE_NUMBER"));
				holder.setGiftCardId(rs.getString("GIFT_CARD_ID"));
				System.out.println("rs.getString(GIVEX_NUM) :"+rs.getString("GIVEX_NUM"));
				holder.setGivexNum(ErpGiftCardUtil.decryptGivexNum(rs.getString("GIVEX_NUM")));
				holder.setPurchaseDate(rs.getDate("ACTION_DATE"));
				holder.setRecepientModel(recModel);
				list.add(holder);
			}	
			rs.close();
			ps.close();														
			return list;
	}

	
	
	private static final String GC_INSERT="insert into cust.gift_card(ID,GIVEX_NUM,BALANCE,ORIG_AMOUNT,CERTIFICATE_NUM,SALE_ID,CARD_TYPE,CREATE_DATE) values(?,?,?,?,?,?,?,sysdate)";  
	
	public static String storeGiftCardModel(Connection conn, ErpGiftCardModel gcModel) throws SQLException{
		
			PreparedStatement ps = conn.prepareStatement(GC_INSERT); 								
			String id=SequenceGenerator.getNextId(conn, "CUST");						
			ps.setString(1, id);
			ps.setString(2, ErpGiftCardUtil.encryptGivexNum(nvl(gcModel.getAccountNumber())));
			ps.setDouble(3, gcModel.getBalance());
			ps.setDouble(4, gcModel.getBalance());
			ps.setString(5, ErpGiftCardUtil.getCertificateNumber(gcModel.getAccountNumber()));
			ps.setString(6, nvl(gcModel.getPurchaseSaleId()));
			ps.setString(7, nvl(gcModel.getCardType().getName()));						 			
			int num=ps.executeUpdate();
			ps.close();	
			return id;
			
	}
	private static final String GC_UPDATE_BAL = "update cust.gift_card set balance = ? where givex_num = ?";

 	public static void updateBalance(Connection conn,
			ErpGiftCardModel gcModel, double newBalance) throws SQLException {

		PreparedStatement ps = conn.prepareStatement(GC_UPDATE_BAL);
		ps.setDouble(1, newBalance);
		ps.setString(2, ErpGiftCardUtil.encryptGivexNum(nvl(gcModel
				.getAccountNumber())));
		int num = ps.executeUpdate();
		ps.close();

	}

	private static final String GC_IN_USE = "select c.USER_ID from cust.PAYMENTMETHOD pm, cust.CUSTOMER c where pm.account_number = ? "
			+ "AND c.ID = pm.CUSTOMER_ID";

	public static String lookupOwner(Connection conn, String acctNum)
			throws SQLException {
		PreparedStatement ps = null;
		String userId = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(GC_IN_USE);
			ps.setString(1, acctNum);
			rs = ps.executeQuery();
			if (rs.next()) {
				userId = rs.getString("USER_ID");

			}
		} catch (SQLException se) {
			throw se;
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
		return userId;
	}

	private static final String GC_LOAD = "select ID,GIVEX_NUM,BALANCE,ORIG_AMOUNT,CERTIFICATE_NUM,SALE_ID,CARD_TYPE,CREATE_DATE from cust.gift_card where GIVEX_NUM = ?";

	public static ErpGiftCardModel loadGiftCardModel(Connection conn,
			String givexNum) throws SQLException {
		ErpGiftCardModel giftCardModel = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(GC_LOAD);
			ps.setString(1, ErpGiftCardUtil.encryptGivexNum(givexNum));
			// ps.setString(1, givexNum);
			rs = ps.executeQuery();
			if (rs.next()) {
				giftCardModel = new ErpGiftCardModel();
				PrimaryKey pk = new PrimaryKey(rs.getString("ID"));
				giftCardModel.setPK(pk);
				giftCardModel.setAccountNumber(ErpGiftCardUtil
						.decryptGivexNum(rs.getString("GIVEX_NUM")));
				// giftCardModel.setAccountNumber(rs.getString("GIVEX_NUM"));
				giftCardModel.setBalance(rs.getDouble("BALANCE"));
				giftCardModel.setOriginalAmount(rs.getDouble("ORIG_AMOUNT"));
				// giftCardModel.setCertificateNumber(rs.getString("CERTIFICATE_NUM"));
				giftCardModel
						.setCertificateNumber(ErpGiftCardUtil
								.getCertificateNumber(giftCardModel
										.getAccountNumber()));
				giftCardModel.setPurchaseSaleId(rs.getString("SALE_ID"));
				giftCardModel.setCardType(EnumCardType.getEnum(rs
						.getString("CARD_TYPE")));
				giftCardModel.setPurchaseDate(rs.getDate("CREATE_DATE"));
			}
		} catch (SQLException se) {
			throw se;
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
		return giftCardModel;
	}

	private static final String GC_LOAD_BY_SALE_ID = "select ID,GIVEX_NUM,BALANCE,ORIG_AMOUNT,CERTIFICATE_NUM,SALE_ID,CARD_TYPE from cust.gift_card where SALE_ID = ?";

	public static List loadGiftCardbySaleId(Connection conn, String saleId)
			throws SQLException {
		List gcList = new ArrayList();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(GC_LOAD_BY_SALE_ID);
			ps.setString(1, saleId);
			// ps.setString(1, givexNum);
			rs = ps.executeQuery();
			while (rs.next()) {
				ErpGiftCardModel giftCardModel = new ErpGiftCardModel();
				PrimaryKey pk = new PrimaryKey(rs.getString("ID"));
				giftCardModel.setPK(pk);
				giftCardModel.setAccountNumber(ErpGiftCardUtil
						.decryptGivexNum(rs.getString("GIVEX_NUM")));
				// giftCardModel.setAccountNumber(rs.getString("GIVEX_NUM"));
				giftCardModel.setBalance(rs.getDouble("BALANCE"));
				giftCardModel.setOriginalAmount(rs.getDouble("ORIG_AMOUNT"));
				// giftCardModel.setCertificateNumber(rs.getString("CERTIFICATE_NUM"));
				giftCardModel
						.setCertificateNumber(ErpGiftCardUtil
								.getCertificateNumber(giftCardModel
										.getAccountNumber()));
				giftCardModel.setPurchaseSaleId(rs.getString("SALE_ID"));
				giftCardModel.setCardType(EnumCardType.getEnum(rs.getString("CARD_TYPE")));
				gcList.add(giftCardModel);
			}
		} catch (SQLException se) {
			throw se;
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
		return gcList;
	}

	private static final String GC_PENDING_PRE_AUTHS = "select ID, TRAN_TYPE, TRANS_AMOUNT FROM CUST.GIFT_CARD_TRANS WHERE TRAN_TYPE IN ('PRE', 'REV-PRE') AND TRAN_STATUS = 'P' AND CERTIFICATE_NUM = ?";

	public static List loadPendingPreAuths(Connection conn, String givexNum)
			throws SQLException {
		List tranList = new ArrayList();
		// ErpGiftCardModel giftCardModel = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(GC_PENDING_PRE_AUTHS);
			//ps.setString(1, ErpGiftCardUtil.encryptGivexNum(givexNum));
			ps.setString(1, ErpGiftCardUtil.getCertificateNumber(givexNum));
			rs = ps.executeQuery();
			while (rs.next()) {
				ErpGiftCardAuthModel giftCardTranModel = new ErpPreAuthGiftCardModel();
				PrimaryKey pk = new PrimaryKey(rs.getString("ID"));
				giftCardTranModel.setPK(pk);
				giftCardTranModel
						.setGCTransactionType(EnumGiftCardTransactionType
								.getEnum((rs.getString("TRAN_TYPE"))));
				giftCardTranModel.setAmount(rs.getDouble("TRANS_AMOUNT"));
				tranList.add(giftCardTranModel);
			}
		} catch (SQLException se) {
			throw se;
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
		return tranList;
	}

	private static final String GC_VALID_PRE_AUTHS = "select ID, TRAN_TYPE, TRANS_AMOUNT FROM CUST.GIFT_CARD_TRANS WHERE TRAN_TYPE IN ('PRE') AND TRAN_STATUS IN ('P','S') AND CERTIFICATE_NUM = ? ";
	private static final String GC_VALID_REVERSE_AUTHS = "SELECT PRE_AUTH_CODE FROM CUST.GIFT_CARD_TRANS WHERE TRAN_TYPE IN ('REV-PRE', 'POST') AND CERTIFICATE_NUM = ? AND TRAN_STATUS <> 'F'";

	public static List loadValidPreAuths(Connection conn, String givexNum)
			throws SQLException {
		List preList = new ArrayList();
		List revList = new ArrayList();
		// ErpGiftCardModel giftCardModel = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(GC_VALID_PRE_AUTHS);
			//ps.setString(1, ErpGiftCardUtil.encryptGivexNum(givexNum));
			ps.setString(1, ErpGiftCardUtil.getCertificateNumber(givexNum));
			rs = ps.executeQuery();
			while (rs.next()) {
				ErpPreAuthGiftCardModel giftCardTranModel = new ErpPreAuthGiftCardModel();
				PrimaryKey pk = new PrimaryKey(rs.getString("ID"));
				giftCardTranModel.setPK(pk);
				giftCardTranModel
						.setGCTransactionType(EnumGiftCardTransactionType
								.getEnum((rs.getString("TRAN_TYPE"))));
				giftCardTranModel.setAmount(rs.getDouble("TRANS_AMOUNT"));
				preList.add(giftCardTranModel);
			}
			rs.close();
			ps.close();
			ps = conn.prepareStatement(GC_VALID_REVERSE_AUTHS);
			ps.setString(1, ErpGiftCardUtil.getCertificateNumber(givexNum));
			rs = ps.executeQuery();
			while (rs.next()) {
				String preAuthCode = rs.getString("PRE_AUTH_CODE");
				revList.add(preAuthCode);
			}
			for (Iterator it = preList.iterator(); it.hasNext();) {
				ErpPreAuthGiftCardModel giftCardTranModel = (ErpPreAuthGiftCardModel)it.next();
				if(revList.contains(giftCardTranModel.getAuthCode())) {
					//Pre-auth has been reversed. ignore it.
					it.remove();
				}
			}
			
		} catch (SQLException se) {
			throw se;
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
		return preList;
	}
	
	private static final String GC_RECIPENT_EMAIL_INSERT = "INSERT INTO CUST.GIFT_CARD_DELIVERY_INFO(ID, GIFT_CARD_ID,RECIPENT_ID,DELIVERY_MODE,EMAIIL_SENT) VALUES(?,?,?,?,?)";

	public static void storeGCRecipentsList(Connection conn, String giftCardId,
			List recipentList, String salesactionId) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(GC_RECIPENT_EMAIL_INSERT);

		for (int i = 0; i < recipentList.size(); i++) {
			ErpRecipentModel model = (ErpRecipentModel) recipentList.get(i);

			String id = SequenceGenerator.getNextId(conn, "CUST");

			ps.setString(1, id);
			ps.setString(2, nvl(giftCardId));
			ps.setString(3, nvl(model.getId()));
			ps.setString(4, nvl(model.getDeliveryMode().getName()));
			if ("E".equalsIgnoreCase(model.getDeliveryMode().getName())) {
				ps.setDate(5, new Date(System.currentTimeMillis()));
			} else {
				ps.setNull(5, Types.NULL);
			}
			ps.setString(6, nvl(salesactionId));
			ps.addBatch();
		}

		int num[] = ps.executeBatch();
		ps.close();
	}

	private static final String SELECT_ORDERS_WITH_GC="SELECT DISTINCT S.ID, S.CUSTOMER_ID, SA.AMOUNT, SA.SUB_TOTAL, SA.REQUESTED_DATE, S.STATUS ,SA.SOURCE AS CREATE_SOURCE, SA.ACTION_DATE AS CREATE_DATE, SA.INITIATOR AS CREATED_BY, S.TYPE "+  
													  " FROM CUST.SALESACTION SA,CUST.SALE S, CUST.APPLIED_GIFT_CARD AGC "+													 
													  "	WHERE S.ID=SA.SALE_ID AND "+
													  " S.CUSTOMER_ID=? AND "+
													  " S.CUSTOMER_ID=SA.CUSTOMER_ID AND"+
													  " SA.ID=AGC.SALESACTION_ID AND"+ 
													  " S.STATUS<>'CAN' AND "+
													  " SA.ACTION_TYPE IN ('CRO','MOD') AND "+
													  " SA.ACTION_DATE=S.CROMOD_DATE";
	

	public static List loadGiftCardOrders(Connection conn, String erpCustomerPK) throws SQLException {
		List saleList=new ArrayList();
		PreparedStatement ps = conn.prepareStatement(SELECT_ORDERS_WITH_GC);						
		ps.setString(1, erpCustomerPK);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {			
			
			ErpSaleInfo info= new ErpSaleInfo(
					rs.getString("ID"),
					rs.getString("CUSTOMER_ID"),
					EnumSaleStatus.getSaleStatus(rs.getString("STATUS")),
					rs.getDouble("AMOUNT"),
					rs.getDouble("SUB_TOTAL"),
					rs.getDate("REQUESTED_DATE"),
					EnumTransactionSource.getTransactionSource(rs.getString("CREATE_SOURCE")),
					rs.getTimestamp("CREATE_DATE"),
					rs.getString("CREATED_BY"),
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					0,
					0,
					null,
					null,
					null,
					EnumSaleType.getSaleType(rs.getString("TYPE")),
					null,
					null,false);			
			saleList.add(info);
		} 
		
		rs.close();
		ps.close();														
		return saleList;
	}
	
	
	private static final String SELECT_ALL_USED_GC="SELECT  DISTINCT AGC.CERTIFICATE_NUM , GC.BALANCE, GC.ID,GC.GIVEX_NUM,GC.BALANCE,GC.ORIG_AMOUNT,GC.CERTIFICATE_NUM,GC.SALE_ID,GC.CARD_TYPE,GC.CREATE_DATE "+
												"	FROM CUST.SALESACTION SA,CUST.SALE S, CUST.APPLIED_GIFT_CARD AGC ,CUST.GIFT_CARD GC "+  													 
												" WHERE S.ID=SA.SALE_ID AND "+ 
												" S.CUSTOMER_ID=? AND S.CUSTOMER_ID=SA.CUSTOMER_ID AND SA.ID=AGC.SALESACTION_ID AND "+ 
												" S.STATUS<>'CAN' AND  SA.ACTION_TYPE IN ('CRO','MOD') AND  SA.ACTION_DATE=S.CROMOD_DATE  AND GC.CERTIFICATE_NUM=AGC.CERTIFICATE_NUM";
	
	public static List loadAllGiftCardsUsed(Connection conn, String erpCustomerPK) throws SQLException {
		List saleList=new ArrayList();
		PreparedStatement ps = conn.prepareStatement(SELECT_ALL_USED_GC);						
		ps.setString(1, erpCustomerPK);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {			
						
			    ErpGiftCardModel giftCardModel = new ErpGiftCardModel();
				PrimaryKey pk = new PrimaryKey(rs.getString("ID"));
				giftCardModel.setPK(pk);
				giftCardModel.setAccountNumber(ErpGiftCardUtil.decryptGivexNum(rs.getString("GIVEX_NUM")));
				//giftCardModel.setAccountNumber(rs.getString("GIVEX_NUM"));
				giftCardModel.setBalance(rs.getDouble("BALANCE"));
				giftCardModel.setOriginalAmount(rs.getDouble("ORIG_AMOUNT"));
				//giftCardModel.setCertificateNumber(rs.getString("CERTIFICATE_NUM"));
				giftCardModel.setCertificateNumber(ErpGiftCardUtil.getCertificateNumber(giftCardModel.getAccountNumber()));
				giftCardModel.setPurchaseSaleId(rs.getString("SALE_ID"));
				giftCardModel.setCardType(EnumCardType.getEnum(rs.getString("CARD_TYPE")));
				giftCardModel.setPurchaseDate(rs.getDate("CREATE_DATE"));			
			   saleList.add(giftCardModel);
		} 
		
		rs.close();
		ps.close();														
		return saleList;
	}
	
	
	private static final String SELECT_ORDERS_WITH_GC_CERT="SELECT DISTINCT S.ID,S.CUSTOMER_ID, SA.AMOUNT, SA.SUB_TOTAL,SA.REQUESTED_DATE, S.STATUS ,SA.SOURCE AS CREATE_SOURCE, SA.ACTION_DATE AS CREATE_DATE, SA.INITIATOR AS CREATED_BY, S.TYPE "+ 
															" FROM CUST.SALESACTION SA,CUST.SALE S, CUST.APPLIED_GIFT_CARD AGC WHERE S.ID=SA.SALE_ID AND S.CUSTOMER_ID=? AND "+
															" S.CUSTOMER_ID=SA.CUSTOMER_ID AND SA.ID=AGC.SALESACTION_ID AND  S.STATUS<>'CAN' AND SA.ACTION_TYPE IN ('CRO','MOD') AND SA.ACTION_DATE=S.CROMOD_DATE AND "+
															" AGC.CERTIFICATE_NUM=?";


	public static List loadGiftCardRedemedOrders(Connection conn,
			String erpCustomerPK, String certNum) throws SQLException {
		// TODO Auto-generated method stub
		List saleList=new ArrayList();
		PreparedStatement ps = conn.prepareStatement(SELECT_ORDERS_WITH_GC_CERT);						
		ps.setString(1, erpCustomerPK);
		ps.setString(2, certNum);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {			
			
			ErpSaleInfo info= new ErpSaleInfo(
					rs.getString("ID"),
					rs.getString("CUSTOMER_ID"),
					EnumSaleStatus.getSaleStatus(rs.getString("STATUS")),
					rs.getDouble("AMOUNT"),
					rs.getDouble("SUB_TOTAL"),
					rs.getDate("REQUESTED_DATE"),
					EnumTransactionSource.getTransactionSource(rs.getString("CREATE_SOURCE")),
					rs.getTimestamp("CREATE_DATE"),
					rs.getString("CREATED_BY"),
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					0,
					0,
					null,
					null,
					null,
					EnumSaleType.getSaleType(rs.getString("TYPE")),
					null,
					null, false);			
			saleList.add(info);
		} 
		
		rs.close();
		ps.close();														
		return saleList;
	}
	
	private static final String SELECT_APPLIED_GC_ORDERS="SELECT DISTINCT S.ID,S.CUSTOMER_ID, SA.AMOUNT, SA.SUB_TOTAL,SA.REQUESTED_DATE, S.STATUS ,SA.SOURCE AS CREATE_SOURCE, SA.ACTION_DATE AS CREATE_DATE, SA.INITIATOR AS CREATED_BY, S.TYPE "+ 
	" FROM CUST.SALESACTION SA,CUST.SALE S, CUST.APPLIED_GIFT_CARD AGC WHERE S.ID=SA.SALE_ID AND "+
	" S.CUSTOMER_ID=SA.CUSTOMER_ID AND SA.ID=AGC.SALESACTION_ID AND  S.STATUS<>'CAN' AND SA.ACTION_TYPE IN ('CRO','MOD') AND SA.ACTION_DATE=S.CROMOD_DATE AND "+
	" AGC.CERTIFICATE_NUM=?";


	public static List loadGiftCardRedemedOrders(Connection conn,
			String certNum) throws SQLException {
		// TODO Auto-generated method stub
		List saleList=new ArrayList();
		PreparedStatement ps = conn.prepareStatement(SELECT_APPLIED_GC_ORDERS);						
		
		ps.setString(1, certNum);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {			

			ErpSaleInfo info= new ErpSaleInfo(
					rs.getString("ID"),
					rs.getString("CUSTOMER_ID"),
					EnumSaleStatus.getSaleStatus(rs.getString("STATUS")),
					rs.getDouble("AMOUNT"),
					rs.getDouble("SUB_TOTAL"),
					rs.getDate("REQUESTED_DATE"),
					EnumTransactionSource.getTransactionSource(rs.getString("CREATE_SOURCE")),
					rs.getTimestamp("CREATE_DATE"),
					rs.getString("CREATED_BY"),
					null,
					null,
					null,
					null,
					null,
					null,
					null,
					0,
					0,
					null,
					null,
					null,
					EnumSaleType.getSaleType(rs.getString("TYPE")),
					null,
					null, false);			
			saleList.add(info);
		} 

		rs.close();
		ps.close();		
		
		return saleList;
	}
	
	
	private static final String SELECT_GC_RECIPENTS_SALE_ID_SQL = "SELECT GC.GIVEX_NUM, GCR.ID as ID, "
		+ "GCR.CUSTOMER_ID,GC.SALE_ID, GCR.SENDER_NAME,GCR.SENDER_EMAIL,GCR.RECIP_NAME,GCR.RECIP_EMAIL,GCR.TEMPLATE_ID,GCR.DELIVERY_MODE, GCR.AMOUNT GC_AMOUNT,GCR.PERSONAL_MSG,GCR.ORDERLINE_NUMBER, "
		+ "GDCC.GIFT_CARD_ID,GDCC.DELIVERY_MODE,GDCC.EMAIL_SENT, SA.ACTION_DATE "
		+ "FROM  CUST.SALESACTION SA, CUST.GIFT_CARD_RECIPIENT GCR,CUST.GIFT_CARD_DELIVERY_INFO GDCC, "
		+ "CUST.SALESACTION SA1, CUST.GIFT_CARD GC WHERE "
		+ "SA.CUSTOMER_ID = SA1.CUSTOMER_ID AND SA.SALE_ID=SA1.SALE_ID "
		+ "AND SA1.ID=GDCC.SALESACTION_ID AND GCR.ID=GDCC.RECIPIENT_ID AND GC.ID = GDCC.GIFT_CARD_ID "
		+ "AND SA.ACTION_TYPE='INV' AND SA1.ACTION_TYPE='GCD' "
		+ "AND SA.SALE_ID = ?";
	
	
	public static List loadGiftCardRecipentsBySaleId(Connection conn, String saleId)
	throws SQLException {
		List recipentList = new ArrayList();
		PreparedStatement ps = conn.prepareStatement(SELECT_GC_RECIPENTS_SALE_ID_SQL);
		ps.setString(1, saleId);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpGCDlvInformationHolder holder = new ErpGCDlvInformationHolder();
			ErpRecipentModel recModel = new ErpRecipentModel();
			recModel.setId(rs.getString("ID"));
			recModel.setCustomerId(rs.getString("CUSTOMER_ID"));
			recModel.setSenderName(rs.getString("SENDER_NAME"));
			recModel.setSenderEmail(rs.getString("SENDER_EMAIL"));
			recModel.setRecipientName(rs.getString("RECIP_NAME"));
			recModel.setRecipientEmail(rs.getString("RECIP_EMAIL"));
			recModel.setSale_id(rs.getString("SALE_ID"));
			recModel.setTemplateId(rs.getString("TEMPLATE_ID"));
			recModel.setDeliveryMode(EnumGCDeliveryMode.getEnum(rs
					.getString("DELIVERY_MODE")));
			recModel.setPersonalMessage(rs.getString("PERSONAL_MSG"));
			recModel.setAmount(rs.getDouble("GC_AMOUNT"));
			recModel.setOrderLineId(rs.getString("ORDERLINE_NUMBER"));
			holder.setGiftCardId(rs.getString("GIFT_CARD_ID"));
			holder.setGivexNum(ErpGiftCardUtil.decryptGivexNum(rs
					.getString("GIVEX_NUM")));
			holder.setPurchaseDate(rs.getDate("ACTION_DATE"));
			holder.setRecepientModel(recModel);
			recipentList.add(holder);
		}
		
		rs.close();
		ps.close();
		return recipentList;
	}
	
	private static final String SELECT_GC_RECIPENT_GIVEX_NUM_SQL = "SELECT GCR.ID as ID, "
		+ "GCR.CUSTOMER_ID,GC.SALE_ID, GCR.SENDER_NAME,GCR.SENDER_EMAIL,GCR.RECIP_NAME,GCR.RECIP_EMAIL,GCR.TEMPLATE_ID,GCR.DELIVERY_MODE, GCR.AMOUNT GC_AMOUNT,GCR.PERSONAL_MSG,GCR.ORDERLINE_NUMBER, "
		+ "GDCC.GIFT_CARD_ID,GDCC.DELIVERY_MODE,GDCC.EMAIL_SENT, SA.ACTION_DATE "
		+ "FROM  CUST.SALESACTION SA, CUST.GIFT_CARD_RECIPIENT GCR,CUST.GIFT_CARD_DELIVERY_INFO GDCC, "
		+ "CUST.SALESACTION SA1, CUST.GIFT_CARD GC WHERE "
		+ "SA.CUSTOMER_ID = SA1.CUSTOMER_ID AND SA.SALE_ID=SA1.SALE_ID "
		+ "AND SA1.ID=GDCC.SALESACTION_ID AND GCR.ID=GDCC.RECIPIENT_ID AND GC.ID = GDCC.GIFT_CARD_ID "
		+ "AND SA.ACTION_TYPE='INV' AND SA1.ACTION_TYPE='GCD' "
		+ "AND GC.GIVEX_NUM = ?";
	
	public static ErpGCDlvInformationHolder loadGiftCardRecipentByGivexNum(Connection conn, String givexNum)
	throws SQLException {
//		List recipentList = new ArrayList();
		PreparedStatement ps = conn.prepareStatement(SELECT_GC_RECIPENT_GIVEX_NUM_SQL);
		ps.setString(1, ErpGiftCardUtil.encryptGivexNum(givexNum));
		ResultSet rs = ps.executeQuery();
		ErpGCDlvInformationHolder holder = null;
		if (rs.next()) {
			holder = new ErpGCDlvInformationHolder();
			ErpRecipentModel recModel = new ErpRecipentModel();
			recModel.setId(rs.getString("ID"));
			recModel.setCustomerId(rs.getString("CUSTOMER_ID"));
			recModel.setSenderName(rs.getString("SENDER_NAME"));
			recModel.setSenderEmail(rs.getString("SENDER_EMAIL"));
			recModel.setRecipientName(rs.getString("RECIP_NAME"));
			recModel.setRecipientEmail(rs.getString("RECIP_EMAIL"));
			recModel.setSale_id(rs.getString("SALE_ID"));
			recModel.setTemplateId(rs.getString("TEMPLATE_ID"));
			recModel.setDeliveryMode(EnumGCDeliveryMode.getEnum(rs
					.getString("DELIVERY_MODE")));
			recModel.setPersonalMessage(rs.getString("PERSONAL_MSG"));
			recModel.setAmount(rs.getDouble("GC_AMOUNT"));
			recModel.setOrderLineId(rs.getString("ORDERLINE_NUMBER"));
			holder.setGiftCardId(rs.getString("GIFT_CARD_ID"));
			holder.setGivexNum(givexNum);
			holder.setPurchaseDate(rs.getDate("ACTION_DATE"));
			holder.setRecepientModel(recModel);
//			recipentList.add(holder);//
		}
		
		rs.close();
		ps.close();
		return holder;
	}

	private static final String SELECT_GC_RECIPENT_CERTIFICATE_NUM_SQL = "SELECT GCR.ID as ID, GC.GIVEX_NUM, "
		+ "GCR.CUSTOMER_ID,GC.SALE_ID, GCR.SENDER_NAME,GCR.SENDER_EMAIL,GCR.RECIP_NAME,GCR.RECIP_EMAIL,GCR.TEMPLATE_ID,GCR.DELIVERY_MODE, GCR.AMOUNT GC_AMOUNT,GCR.PERSONAL_MSG,GCR.ORDERLINE_NUMBER, "
		+ "GDCC.GIFT_CARD_ID,GDCC.DELIVERY_MODE,GDCC.EMAIL_SENT, SA.ACTION_DATE "
		+ "FROM  CUST.SALESACTION SA, CUST.GIFT_CARD_RECIPIENT GCR,CUST.GIFT_CARD_DELIVERY_INFO GDCC, "
		+ "CUST.SALESACTION SA1, CUST.GIFT_CARD GC WHERE "
		+ "SA.CUSTOMER_ID = SA1.CUSTOMER_ID AND SA.SALE_ID=SA1.SALE_ID "
		+ "AND SA1.ID=GDCC.SALESACTION_ID AND GCR.ID=GDCC.RECIPIENT_ID AND GC.ID = GDCC.GIFT_CARD_ID "
		+ "AND SA.ACTION_TYPE='INV' AND SA1.ACTION_TYPE='GCD' "
		+ "AND GC.CERTIFICATE_NUM = ?";
	
	public static ErpGCDlvInformationHolder loadGiftCardRecipentByCertNum(Connection conn, String certNum)
	throws SQLException {
//		List recipentList = new ArrayList();
		PreparedStatement ps = conn.prepareStatement(SELECT_GC_RECIPENT_CERTIFICATE_NUM_SQL);
		ps.setString(1, certNum);//ErpGiftCardUtil.encryptGivexNum(givexNum));
		ResultSet rs = ps.executeQuery();
		ErpGCDlvInformationHolder holder = null;
		if (rs.next()) {
			holder = new ErpGCDlvInformationHolder();
			ErpRecipentModel recModel = new ErpRecipentModel();
			recModel.setId(rs.getString("ID"));
			recModel.setCustomerId(rs.getString("CUSTOMER_ID"));
			recModel.setSenderName(rs.getString("SENDER_NAME"));
			recModel.setSenderEmail(rs.getString("SENDER_EMAIL"));
			recModel.setRecipientName(rs.getString("RECIP_NAME"));
			recModel.setRecipientEmail(rs.getString("RECIP_EMAIL"));
			recModel.setSale_id(rs.getString("SALE_ID"));
			recModel.setTemplateId(rs.getString("TEMPLATE_ID"));
			recModel.setDeliveryMode(EnumGCDeliveryMode.getEnum(rs
					.getString("DELIVERY_MODE")));
			recModel.setPersonalMessage(rs.getString("PERSONAL_MSG"));
			recModel.setAmount(rs.getDouble("GC_AMOUNT"));
			recModel.setOrderLineId(rs.getString("ORDERLINE_NUMBER"));
			holder.setGiftCardId(rs.getString("GIFT_CARD_ID"));
			holder.setGivexNum("GIVEX_NUM");
			holder.setPurchaseDate(rs.getDate("ACTION_DATE"));
			holder.setRecepientModel(recModel);
//			recipentList.add(holder);//
		}
		
		rs.close();
		ps.close();
		return holder;
	}
}
