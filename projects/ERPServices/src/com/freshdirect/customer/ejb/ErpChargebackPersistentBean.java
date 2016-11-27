/*
 * ErpChargebackPersistentBean.java
 *
 * Created on April 29, 2002, 12:00 PM
 */
package com.freshdirect.customer.ejb;

/**
 *
 * @author  knadeem
 * @version 
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpChargebackModel;
import com.freshdirect.customer.ErpPaymentModel;
import com.freshdirect.framework.core.PrimaryKey;

public class ErpChargebackPersistentBean extends ErpAbstractChargebackPersistentBean {
	
	/** 
	 * Creates new ErpChargebackPersistentBean
	 */
    public ErpChargebackPersistentBean() {
		super();
    }
    
    public ErpChargebackPersistentBean(PrimaryKey pk){
    	this();
    	this.setPK(pk);
    }
	
	/** 
	 * Load constructor. 
	 */
	public ErpChargebackPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}
	
	/**
	 * Copy constructor, from model.
	 * @param bean ErpChargebackModel to copy from
	 */
	public ErpChargebackPersistentBean(ErpChargebackModel model) {
		this();
		this.setFromModel(model);
	}
	
	/**
	 * Find ErpSettlementPersistentBean objects for a given parent.
	 * 
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 * @return a List of ErpSettlementPersistentBean objects (empty if found none).
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.SALESACTION WHERE SALE_ID = ? AND ACTION_TYPE = ? ");
		ps.setString(1, parentPK.getId());
		ps.setString(2, EnumTransactionType.CHARGEBACK.getCode());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpChargebackPersistentBean bean = new ErpChargebackPersistentBean(new PrimaryKey(rs.getString(1)), conn);
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		rs.close();
		rs = null;
		ps.close();
		ps = null;
		return lst;
	}
	
	protected String getTransactionType() {
		return EnumTransactionType.CHARGEBACK.getCode();
	}
	
	protected ErpPaymentModel createModel(){
		return new ErpChargebackModel();
	}
	
}
