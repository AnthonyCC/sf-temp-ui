/*
 * 
 * ErpResubmitPaymentPersistentBean.java
 * Date: June 07/2002 11:12 PM
 */
package com.freshdirect.customer.ejb;

/**
 * 
 * @author knadeem
 * @version
 */
import java.sql.*;

import com.freshdirect.framework.core.*;
import com.freshdirect.customer.*;

public class ErpResubmitPaymentPersistentBean extends ErpInvoicePersistentBean{
	
	private ErpPaymentInfoPersistentBean paymentInfo;
	
	/**
	 * Constructor for ErpResubmitPaymentPersistentBean.
	 */
	public ErpResubmitPaymentPersistentBean() {
		super();
		this.paymentInfo = new ErpPaymentInfoPersistentBean();
	}
	
	public ErpResubmitPaymentPersistentBean(PrimaryKey pk){
		this();
		this.setPK(pk);
	}

	/**
	 * Constructor for ErpResubmitPaymentPersistentBean.
	 * @param pk
	 * @param conn
	 * @throws SQLException
	 */
	public ErpResubmitPaymentPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		super(pk, conn);
	}

	/**
	 * Constructor for ErpResubmitPaymentPersistentBean.
	 * @param pk
	 * @param conn
	 * @param rs
	 * @throws SQLException
	 */
	public ErpResubmitPaymentPersistentBean(PrimaryKey pk, Connection conn, ResultSet rs) throws SQLException {
		super(pk, conn, rs);
	}

	/**
	 * Constructor for ErpResubmitPaymentPersistentBean.
	 * @param model
	 */
	public ErpResubmitPaymentPersistentBean(ErpResubmitPaymentModel model) {
		super(model);
	}
	
	/**
	 * Copy into model.
	 * @return ErpAbstractInvoiceModel object.
	 */
	public ModelI getModel() {
		ErpResubmitPaymentModel model = (ErpResubmitPaymentModel)super.getModel();
		model.setPaymentMethod((ErpPaymentMethodI)this.paymentInfo.getModel());
		return model;
	}
	
	/** Copy from model. */
	public void setFromModel(ModelI model) {
		ErpResubmitPaymentModel m = (ErpResubmitPaymentModel)model;
		super.setFromModel(m);
		this.paymentInfo.setFromModel((ErpPaymentMethodModel)m.getPaymentMethod());
	}
	
	public PrimaryKey create(Connection conn) throws SQLException {
		PrimaryKey pk = super.create(conn);
		
		this.paymentInfo.setParentPK(pk);
		this.paymentInfo.create(conn);
		
		return pk;	
	}
	
	public void load(Connection conn) throws SQLException {
		super.load(conn);
		
		// load payment info
		this.paymentInfo.setParentPK(this.getPK());
		this.paymentInfo.load( conn );
	}
	
	/**
	 * Template method to create a concrete instance of ErpAbstractOrderModel.
	 */
	protected ErpAbstractInvoiceModel createModel(){
		return new ErpResubmitPaymentModel();
	}

	/**
	 * Template method to get the tx type.
	 */
	protected EnumTransactionType getTransactionType(){
		return EnumTransactionType.RESUBMIT_PAYMENT;
	}

}
