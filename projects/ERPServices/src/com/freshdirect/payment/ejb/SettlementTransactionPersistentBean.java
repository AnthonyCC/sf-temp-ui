package com.freshdirect.payment.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.freshdirect.framework.core.DependentPersistentBeanSupport;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.payment.model.ErpSettlementInvoiceModel;
import com.freshdirect.payment.model.ErpSettlementTransactionModel;

public class SettlementTransactionPersistentBean extends DependentPersistentBeanSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3736115320293581426L;
	private String transactionId = null;
	private String gatewayOrderId = null;
	private String paypalReferenceId = null;
	private String paypalReferenceIdType = null;
	private String transactionEventCode = null;
	private Date transactionInitiationDate = null;
	private Date transactionCompletionDate = null;
	private String transactionDebitOrCredit = null;
	private long grossTransactionAmount = 0;
	private String grossTransactionCurrency = null;
	private String feeDebitOrCredit = null;
	private long feeAmount = 0;
	private String feeCurrency = null;
	private String consumerId = null;
	private String paymentTrackingId = null;
	private String customField = null;
	private String storeId = null;
	private long creditTransactionalFee = 0;
	private long creditPromotionalFee = 0;
	private String creditTerm = null;
	private String status = null;
	
	
	/** 
	 * Creates new SettlementInvoicePersistentBean
	 */
    public SettlementTransactionPersistentBean() {
        super();
    }
    
    /** 
     * Creates new SettlementInvoicePersistentBean
     */
    public SettlementTransactionPersistentBean(PrimaryKey pk, String transactionId,
    						String invoiceId, String paypalReferenceId,	String paypalReferenceIdType,
    						String transactionEventCode, Date transactionInitiationDate, Date transactionCompletionDate,
    						String transactionDebitOrCredit, long grossTransactionAmount,
    						String grossTransactionCurrency, String feeDebitOrCredit,
    						long feeAmount, String feeCurrency, String customField,
    						String consumerId, String paymentTrackingId, String storeId,
    						String bankReferenceId, //Not Applicable
    						long creditTransactionalFee, // Not Applicable
    						long creditPromotionalFee, // Not Applicable
    						String creditTerm, String status) {
        super(pk);
    	this.transactionId = transactionId;
    	this.gatewayOrderId = invoiceId;
    	this.paypalReferenceId = paypalReferenceId;
    	this.paypalReferenceIdType = paypalReferenceIdType;
    	this.transactionEventCode = transactionEventCode;
    	this.transactionInitiationDate = transactionInitiationDate;
    	this.transactionCompletionDate = transactionCompletionDate;
    	this.transactionDebitOrCredit = transactionDebitOrCredit;
    	this.grossTransactionAmount = grossTransactionAmount;
    	this.grossTransactionCurrency = grossTransactionCurrency;
    	this.feeDebitOrCredit = feeDebitOrCredit;
    	this.feeAmount = feeAmount;
    	this.feeCurrency = feeCurrency;
    	this.consumerId = consumerId;
    	this.paymentTrackingId = paymentTrackingId;
    	this.customField = customField;
    	this.storeId = storeId;
    	this.creditTransactionalFee = creditTransactionalFee;
    	this.creditPromotionalFee = this.creditPromotionalFee;
    	this.creditTerm = creditTerm;
    	this.status = status;
    }
    
    /**
     * Load constructor.
     */
    public SettlementTransactionPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
        this();
        this.setPK(pk);
        load(conn);
    }
    
    /**
     * Copy constructor, from model.
     *
     * @param ErpSummaryDetailModel to copy from
     */
    public SettlementTransactionPersistentBean(ErpSettlementTransactionModel model) {
        super();
        this.setFromModel(model);
    }
    
    /** 
     * Get model representing the state of this object
     * 
     * @throws RemoteException in case there is any problem
     * @return Model representing state of this object
     */
    public ModelI getModel(){
    	ErpSettlementTransactionModel model = new ErpSettlementTransactionModel(transactionId,
				gatewayOrderId, paypalReferenceId,	paypalReferenceIdType,
				transactionEventCode, transactionInitiationDate, transactionCompletionDate,
				transactionDebitOrCredit, grossTransactionAmount,
				grossTransactionCurrency, feeDebitOrCredit,
				feeAmount, feeCurrency,
				consumerId, paymentTrackingId, customField, storeId, creditTransactionalFee,
				creditPromotionalFee, creditTerm, status);
        super.decorateModel(model);
        return model;
    }
    
    /** 
     * set properties of this object from this model
     * 
     * @param model the model to copy
     * @throws RemoteException in case any problem in accessing remote object
     */
    public void setFromModel(ModelI model){
        ErpSettlementTransactionModel m = (ErpSettlementTransactionModel)model;
    	this.transactionId = m.getTransactionId();
    	this.gatewayOrderId = m.getGatewayOrderId();
    	this.paypalReferenceId = m.getPaypalReferenceId();
    	this.paypalReferenceIdType = m.getPaypalReferenceIdType();
    	this.transactionEventCode = m.getTransactionEventCode();
    	this.transactionInitiationDate = m.getTransactionInitiationDate();
    	this.transactionCompletionDate = m.getTransactionCompletionDate();
    	this.transactionDebitOrCredit = m.getTransactionDebitOrCredit();
    	this.grossTransactionAmount = m.getGrossTransactionAmount();
    	this.grossTransactionCurrency = m.getGrossTransactionCurrency();
    	this.feeDebitOrCredit = m.getFeeDebitOrCredit();
    	this.feeAmount = m.getFeeAmount();
    	this.feeCurrency = m.getFeeCurrency();
    	this.consumerId = m.getConsumerId();
    	this.paymentTrackingId = m.getPaymentTrackingId();
    	this.customField = m.getCustomField();
    	this.storeId = m.getStoreId();
    	this.creditTransactionalFee = m.getCreditTransactionalFee();
    	this.creditPromotionalFee = m.getCreditPromotionalFee();
    	this.creditTerm = m.getCreditTerm();
    	this.status = m.getStatus();
    }
    
    /**
     * Find SettlementInvoicePersistentBean objects for a given parent.
     *
     * @param SQLConnection connection
     * @param parentPK primary key of parent
     * @return a List of SettlementInvoicePersistentBean objects (empty if found none).
     * @throws SQLException if any problems occur talking to the database
     */
    public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
        java.util.List beans = new java.util.LinkedList();
        PreparedStatement ps = conn.prepareStatement("SELECT ID, TRXN_ID, ORDER_ID, PP_REF_ID, PP_REF_TYPE, TX_EVENT_CODE, TX_INITIATION_DATE, " +
        		"TX_COMPLETION_DATE, TX_DEBIT_CREDIT, TX_GROSS_AMOUNT, TX_GROSS_CURRENCY, FEE_DEBIT_CREDIT, " +
        		"FEE_AMOUNT, FEE_CURRENCY, CONSUMER_ID, PAYMENT_TRACKING_ID, BANK_REF_ID, STATUS " +
        		"FROM CUST.SETTLEMENT_TRANSACTION WHERE SETTLEMENT_ID = ?");
        ps.setString(1, parentPK.getId());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String id = rs.getString("ID");
        	String transactionId = null;
        	String invoiceId = null;
        	String paypalReferenceId = null;
        	String paypalReferenceIdType = null;
        	String transactionEventCode = null;
        	Date transactionInitiationDate = null;
        	Date transactionCompletionDate = null;
        	String transactionDebitOrCredit = null;
        	long grossTransactionAmount = 0;
        	String grossTransactionCurrency = null;
        	String feeDebitOrCredit = null;
        	long feeAmount = 0;
        	String feeCurrency = null;
        	String customField = null;
        	String consumerId = null;
        	String paymentTrackingId = null;
        	String status = null;
        	String storeId = null;
        	String bankReferenceId = null; //Not Applicable
        	long creditTransactionalFee = 0; // Not Applicable
        	long creditPromotionalFee = 0; // Not Applicable
        	String creditTerm = null;
            SettlementTransactionPersistentBean bean =	new SettlementTransactionPersistentBean(new PrimaryKey(id), transactionId,
    				invoiceId, paypalReferenceId,	paypalReferenceIdType,
    				transactionEventCode, transactionInitiationDate, transactionCompletionDate,
    				transactionDebitOrCredit, grossTransactionAmount,
    				grossTransactionCurrency, feeDebitOrCredit,
    				feeAmount, feeCurrency, customField,
    				consumerId, paymentTrackingId, storeId,
    				bankReferenceId, //Not Applicable
    				creditTransactionalFee, // Not Applicable
    				creditPromotionalFee, // Not Applicable
    				creditTerm,status);
            bean.setParentPK(parentPK);
            beans.add(bean);
        }
        rs.close();
        rs = null;
        ps.close();
        ps = null;
        
        return beans;
    }
    
    /** 
     * Writes a new object to the persistent store for the first time
     * 
     * @param conn a SQLConnection used to write this object
     * @throws SQLException if any problems encountered while writing this object
     * @return the unique identity of this new object
     */
    public PrimaryKey create(Connection conn) throws SQLException {
    	String id = this.getNextId(conn, "CUST");
        PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.SETTLEMENT_TRANSACTION (ID, SETTLEMENT_ID, TRXN_ID," + //3
        		" ORDER_ID, GATEWAY_ORDER_ID, PP_REF_ID, PP_REF_TYPE, TX_EVENT_CODE, TX_INITIATION_DATE, " + //6
        		"TX_COMPLETION_DATE, TX_DEBIT_CREDIT, TX_GROSS_AMOUNT, TX_GROSS_CURRENCY, FEE_DEBIT_CREDIT, " + //5
        		"FEE_AMOUNT, FEE_CURRENCY, CONSUMER_ID, PAYMENT_TRACKING_ID, CREATED_TIME_DATE, CUSTOM_FIELD, STORE_ID, " + //7
        		"CREDIT_TX_FEE, CREDIT_PROMOTIONAL_FEE, CREDIT_TERM, STATUS " + //4
        		") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, 'P')" );
        int i = 1;
        String orderId = this.gatewayOrderId != null ? this.gatewayOrderId.substring(0, this.gatewayOrderId.indexOf("X")) : null;
        ps.setString(i++, id);
        ps.setString(i++, this.getParentPK().getId());
    	ps.setString(i++, this.transactionId);
    	ps.setString(i++, orderId);
    	ps.setString(i++, this.gatewayOrderId);
    	ps.setString(i++, this.paypalReferenceId);
    	ps.setString(i++, this.paypalReferenceIdType);
    	ps.setString(i++, this.transactionEventCode);
    	ps.setDate(i++, new java.sql.Date(this.transactionInitiationDate.getTime()));
    	ps.setDate(i++, new java.sql.Date(this.transactionCompletionDate.getTime()));
    	ps.setString(i++, this.transactionDebitOrCredit);
    	ps.setLong(i++, this.grossTransactionAmount);
    	ps.setString(i++, this.grossTransactionCurrency);
    	ps.setString(i++, this.feeDebitOrCredit);
    	ps.setLong(i++, this.feeAmount);
    	ps.setString(i++, this.feeCurrency);
    	ps.setString(i++, this.consumerId);
    	ps.setString(i++, this.paymentTrackingId);
    	ps.setDate(i++, new java.sql.Date((new Date()).getTime()));
    	ps.setString(i++, this.customField);
    	ps.setString(i++, this.storeId);
    	ps.setLong(i++, this.creditTransactionalFee);
    	ps.setLong(i++, this.creditPromotionalFee);
    	ps.setString(i++, this.creditTerm);
        
	    try {
            ps.executeUpdate();
            this.setPK(new PrimaryKey(id));
        } catch (SQLException sqle) {
            throw sqle;
        } finally {
            ps.close();
            ps = null;
        }
        this.unsetModified();
        return this.getPK();
    }
    
    /** 
     * Load this object from persistent store
     * 
     * @param conn a SQLConnection to used to load this object
     * @throws SQLException any problems entountered while loading this object
     */
    
    public void load(Connection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT ID, TRXN_ID, GATEWAY_ORDER_ID, PP_REF_ID, PP_REF_TYPE, TX_EVENT_CODE, TX_INITIATION_DATE, " +
        		"TX_COMPLETION_DATE, TX_DEBIT_CREDIT, TX_GROSS_AMOUNT, TX_GROSS_CURRENCY, FEE_DEBIT_CREDIT, " +
        		"FEE_AMOUNT, FEE_CURRENCY, CONSUMER_ID, PAYMENT_TRACKING_ID " +
        		"FROM CUST.SETTLEMENT_TRANSACTION WHERE ID = ?");
        
        ps.setString(1, this.getPK().getId());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
        	this.transactionId = rs.getString("TRXN_ID");
        	this.gatewayOrderId = rs.getString("GATEWAY_ORDER_ID");
        	this.paypalReferenceId = rs.getString("PP_REF_ID");
        	this.paypalReferenceIdType = rs.getString("PP_REF_TYPE");
        	this.transactionEventCode = rs.getString("TX_EVENT_CODE");
        	this.transactionInitiationDate = rs.getDate("TX_INITIATION_DATE");
        	this.transactionCompletionDate = rs.getDate("TX_COMPLETION_DATE");
        	this.transactionDebitOrCredit = rs.getString("TX_DEBIT_CREDIT");
        	this.grossTransactionAmount = rs.getLong("TX_GROSS_AMOUNT");
        	this.grossTransactionCurrency = rs.getString("TX_GROSS_CURRENCY");
        	this.feeDebitOrCredit = rs.getString("FEE_DEBIT_CREDIT");
        	this.feeAmount = rs.getLong("FEE_AMOUNT");
        	this.feeCurrency = rs.getString("FEE_CURRENCY");
        	this.consumerId = rs.getString("PAYMENT_TRACKING_ID");
        	this.paymentTrackingId = rs.getString("BANK_REF_ID");
        } else {
            throw new SQLException("No such SettlementSummaryBean PK: " + this.getPK() );
        }
        rs.close();
        rs = null;
        ps.close();
        ps = null;
        
        this.unsetModified();
    }
    
    public final void store(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("Object cannot be modified");
    }

	public final void remove(Connection conn) throws SQLException {
        throw new UnsupportedOperationException("Object cannot be modified");
	}
	
	/**
     * Overriden isModified, always false, as this is a read-only object.
     */
	public final boolean isModified() {
		return false;
	}

}
