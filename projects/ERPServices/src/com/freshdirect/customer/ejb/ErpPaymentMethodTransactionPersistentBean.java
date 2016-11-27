/*
 * ErpPaymentMethodTransactionPersistentBean.java
 *
 * Created on January 9, 2002, 4:17 PM
 */

package com.freshdirect.customer.ejb;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.math.BigDecimal;
import java.sql.*;

import com.freshdirect.framework.core.*;
import com.freshdirect.customer.*;

public abstract class ErpPaymentMethodTransactionPersistentBean extends ErpTransactionPersistentBean {
	
	/** default constructor */
	public ErpPaymentMethodTransactionPersistentBean(){
		super();
	}
	
	public ErpPaymentMethodTransactionPersistentBean(PrimaryKey pk){
		this();
		this.setPK(pk);
	}
	
	/** Load constructor. */
	public ErpPaymentMethodTransactionPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}
	
	/**
	 * Copy constructor, from model.
	 * @param bean ErpPaymentMethodTransactionModel to copy from
	 */
	public ErpPaymentMethodTransactionPersistentBean(ErpPaymentMethodTransactionModel model) {
		this();
		this.setFromModel(model);
	}	
	
	/** writes a new object to the persistent store for the first time
	 * @param conn a SQLConnection to use to write this object to the
	 * persistent store
	 * @throws SQLException any problems encountered while writing this object
	 * to the persistent store
	 * @return the unique identity of this new object in the
	 * persistent store
	 */
	public PrimaryKey create(Connection conn, ErpPaymentMethodTransactionModel model) throws SQLException {
		String id = this.getNextId(conn, "CUST");
		String id1 = this.getNextId(conn, "CUST");
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.SALESACTION (ID, SALE_ID, ACTION_DATE, ACTION_TYPE, AMOUNT, SOURCE, CUSTOMER_ID) values (?,?,?,?,?,?,?)");
		PreparedStatement ps1 = conn.prepareStatement("INSERT INTO CUST.PAYMENTMETHODTRANSACTIONS (ID, SALEACTION_ID, RETURN_CODE, RETURN_MESSAGE, APPROVAL_CODE, RESPONSE_CODE, RESPONSE_MESSAGE, SEQUENCE_NUMBER, ADDRESS_MATCH, ZIP_MATCH, PROCESSOR_RESPONSE_CODE, PROCESSOR_AVS_RESULT) values (?,?,?,?,?,?,?,?,?,?,?,?)");
		
		ps.setString(1, id);
		ps.setString(2, this.getParentPK().getId());
		ps.setTimestamp(3, new java.sql.Timestamp(model.getTransactionDate().getTime()));
		ps.setString(4, this.getTransactionType());
		ps.setBigDecimal(5, new BigDecimal(String.valueOf(model.getAmount())));
		ps.setString(6, model.getTransactionSource().getCode());
		ps.setString(7, model.getCustomerId());
		
		ps1.setString(1, id1);
		ps1.setString(2, id);
		ps1.setInt(3, model.getReturnCode());
		ps1.setString(4, model.getReturnMessage());
		ps1.setString(5, model.getApprovalCode());
		ps1.setString(6, model.getResponseCode());
		ps1.setString(7, model.getResponseMessage());
		ps1.setString(8, model.getSequenceNumber());
		ps1.setString(9, model.getAddressMatch());
		ps1.setString(10, model.getZipMatch());
		ps1.setString(11, model.getProcessorResponseCode());
		ps1.setString(12, model.getProcessorAVSResult());
		
		try{
			if(ps.executeUpdate() != 1 || ps1.executeUpdate() != 1){
				throw new SQLException("new row created");
			}
			this.setPK(new PrimaryKey(id1));
		}finally{
			if(ps != null){
				ps.close();
				ps = null;
			}
			if(ps1 != null){
				ps1.close();
				ps1 = null;
			}
		}
		
		this.unsetModified();
		return this.getPK();
	}
	
	protected abstract String getTransactionType();
	
	/** 
	 * reads an object from the persistent store
	 * @param conn a SQLConnection to use when reading this object
	 * from the persistent store
	 * @throws SQLException any problems entountered while reading this object
	 * from the persistent store
	 */
	public void load(Connection conn, ErpPaymentMethodTransactionModel model) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT SALE_ID, ACTION_DATE, ACTION_TYPE, AMOUNT, SOURCE, SALEACTION_ID, RETURN_CODE, RETURN_MESSAGE, APPROVAL_CODE, RESPONSE_CODE, RESPONSE_MESSAGE, SEQUENCE_NUMBER, ADDRESS_MATCH, ZIP_MATCH, PROCESSOR_RESPONSE_CODE, PROCESSOR_AVS_RESULT FROM CUST.SALESACTION, CUST.PAYMENTMETHODTRANSACTIONS WHERE SALESACTION.ID = PAYMENTMETHODTRANSACTIONS.SALEACTION_ID AND SALESACTION.ID = ? ");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			model.setTransactionDate(rs.getTimestamp("ACTION_DATE"));
			model.setAmount(rs.getDouble("AMOUNT"));
			model.setTransactionSource(EnumTransactionSource.getTransactionSource(rs.getString("SOURCE")));
			model.setReturnCode(rs.getInt("RETURN_CODE"));
			model.setReturnMessage(rs.getString("RETURN_MESSAGE"));
			model.setApprovalCode(rs.getString("APPROVAL_CODE"));
			model.setResponseCode(rs.getString("RESPONSE_CODE"));
			model.setResponseMessage(rs.getString("RESPONSE_MESSAGE"));
			model.setSequenceNumber(rs.getString("SEQUENCE_NUMBER"));
			model.setAddressMatch(rs.getString("ADDRESS_MATCH"));
			model.setZipMatch(rs.getString("ZIP_MATCH"));
			model.setProcessorResponseCode(rs.getString("PROCESSOR_RESPONSE_CODE"));
			model.setProcessorAVSResult(rs.getString("PROCESSOR_AVS_RESULT"));
			
		} else {
			throw new SQLException("No such ErpInvoice PK: " + this.getPK());
		}
		
		rs.close();
		rs = null;
		ps.close();
		ps = null;
		
		this.unsetModified();
	}
	
}
