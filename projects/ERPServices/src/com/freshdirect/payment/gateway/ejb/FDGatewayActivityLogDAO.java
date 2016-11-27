package com.freshdirect.payment.gateway.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.framework.core.SequenceGenerator;

public class FDGatewayActivityLogDAO {


public static void log(FDGatewayActivityLogModel log, Connection conn) {
	PreparedStatement ps = null;		
	try {
		ps =	conn.prepareStatement(
				"INSERT INTO MIS.GATEWAY_ACTIVITY_LOG("+
				"ID,TRANSACTION_TIME,TRANSACTION_TYPE,GATEWAY,MERCHANT,PROFILE_ID,CUSTOMER_ID,"+
				"PAYMENT_TYPE,CARD_TYPE,BANK_ACCOUNT_TYPE,ACCOUNT_NUM_LAST4,EXP_DATE,CUSTOMER_NAME,"+
				"CUSTOMER_ADDRESS1,CUSTOMER_ADDRESS2,CUSTOMER_CITY,CUSTOMER_STATE,CUSTOMER_ZIP,"+
				"CUSTOMER_COUNTRY,ORDER_ID,TX_REF_NUM,TX_REF_IDX,IS_PROCESSED,IS_APPROVED,IS_DECLINED,"+
				"IS_PROCESSING_ERROR,AUTH_CODE,IS_AVS_MATCH,IS_CVV_MATCH,AVS_RESPONSE_CODE,"+
				"CVV_RESPONSE_CODE,RESPONSE_CODE,RESPONSE_CODE_ALT,STATUS_CODE,STATUS_MSG,AMOUNT,EWALLET_ID, EWALLET_TX_ID,E_STORE,DEVICE_ID" +
				") values(?,SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		int i=1;
		ps.setInt(i++, Integer.parseInt(SequenceGenerator.getNextIdFromSequence(conn, "MIS.GATEWAY_LOG_SEQUENCE")));
		if(null !=log.getTransactionType()){
			ps.setString(i++, log.getTransactionType().name());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		
		if(null !=log.getGatewayType()){
			ps.setString(i++, log.getGatewayType().getName());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		if(null !=log.getMerchant()){
			ps.setString(i++, log.getMerchant());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		if(null !=log.getProfileId()){
			ps.setString(i++, log.getProfileId());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		if(null !=log.getCustomerId()){
			ps.setString(i++, log.getCustomerId());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
//"PAYMENT_TYPE,CARD_TYPE,BANK_ACCOUNT_TYPE,ACCOUNT_NUM_LAST4,EXP_DATE,CUSTOMER_NAME,"+		
		if(null !=log.getPaymentType()){
			ps.setString(i++, log.getPaymentType().name());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		if(null !=log.getCardType()){
			ps.setString(i++, log.getCardType().name());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		if(null !=log.getBankAccountType()){
			ps.setString(i++, log.getBankAccountType().name());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		if(null !=log.getAccountNumLast4()){
			ps.setString(i++, log.getAccountNumLast4());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		if(null !=log.getExpirationDate()){
			ps.setTimestamp(i++, new java.sql.Timestamp(log.getExpirationDate().getTime()));
		}else{
			ps.setNull(i++, Types.DATE);
		}
		 
		if(null !=log.getCustomerName()){
			ps.setString(i++, log.getCustomerName().length()>30?log.getCustomerName().substring(0,30):log.getCustomerName());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		
		//"CUSTOMER_ADDRESS1,CUSTOMER_ADDRESS2,CUSTOMER_CITY,CUSTOMER_STATE,CUSTOMER_ZIP,"+
		if(null !=log.getAddressLine1()){
			ps.setString(i++, log.getAddressLine1().length()>30?log.getAddressLine1().substring(0,30):log.getAddressLine1());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		if(null !=log.getAddressLine2()){
			ps.setString(i++, log.getAddressLine2().length()>30?log.getAddressLine2().substring(0,30):log.getAddressLine2());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		if(null !=log.getCity()){
			ps.setString(i++, log.getCity().length()>20?log.getCity().substring(0,20):log.getCity());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		if(null !=log.getState()){
			ps.setString(i++, log.getState().length()>2?log.getState().substring(0,2):log.getState());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		
		if(null !=log.getZipCode()){
			ps.setString(i++, log.getZipCode().length()>10?log.getZipCode().substring(0,10):log.getZipCode());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		if(null !=log.getCountryCode()){
			ps.setString(i++, log.getCountryCode());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		//"CUSTOMER_COUNTRY,ORDER_ID,TX_REF_NUM,TX_REF_IDX,IS_PROCESSED,IS_APPROVED,IS_DECLINED,"+
		if(null !=log.getGatewayOrderID()){
			ps.setString(i++, log.getGatewayOrderID());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		if(null !=log.getTxRefNum()){
			ps.setString(i++, log.getTxRefNum());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		if(null !=log.getTxRefIdx()){
			ps.setString(i++, log.getTxRefIdx());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		ps.setString(i++, log.isRequestProcessed()?"Y":"N");
		ps.setString(i++, log.isApproved()?"Y":"N");
		ps.setString(i++, log.isDeclined()?"Y":"N");
		//"IS_PROCESSING_ERROR,AUTH_CODE,IS_AVS_MATCH,IS_CVV_MATCH,AVS_RESPONSE_CODE,"+
		ps.setString(i++, log.isProcessingError()?"Y":"N");
		if(null !=log.getAuthCode()){
			ps.setString(i++, log.getAuthCode());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		ps.setString(i++, log.isAVSMatch()?"Y":"N");
		ps.setString(i++, log.isCVVMatch()?"Y":"N");
		if(null !=log.getAvsResponse()){
			ps.setString(i++, log.getAvsResponse());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		//"CVV_RESPONSE_CODE,RESPONSE_CODE,RESPONSE_CODE_ALT,STATUS_CODE,STATUS_MSG,AMOUNT"
		if(null !=log.getCvvResponse()){
			ps.setString(i++, log.getCvvResponse());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		if(null !=log.getResponseCode()){
			ps.setString(i++, log.getResponseCode());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		if(null !=log.getResponseCodeAlt()){
			ps.setString(i++, log.getResponseCodeAlt());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		if(null !=log.getStatusCode()){
			ps.setString(i++, log.getStatusCode());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		if(null !=log.getStatusMsg()){
			ps.setString(i++, log.getStatusMsg().length()>250?log.getStatusMsg().substring(0,250):log.getStatusMsg());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		ps.setFloat(i++, (float) log.getAmount());
		
		if(null !=log.geteWalletId()){
			ps.setString(i++, log.geteWalletId());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		
		if(null !=log.geteWalletTxId()){
			ps.setString(i++, log.geteWalletTxId());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		if(null !=log.getEStoreId()){
			ps.setString(i++, log.getEStoreId().getContentId());
		}else{
			ps.setString(i++, EnumEStoreId.FD.getContentId());
		}
		
		if(null !=log.getDeviceId()){
			ps.setString(i++, log.getDeviceId());
		}else{
			ps.setNull(i++, Types.VARCHAR);
		}
		ps.execute();
		
	} catch (SQLException e) {
		e.printStackTrace();
	}
  }
}