/*
 * Created on Apr 14, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpChargebackReversalModel;
import com.freshdirect.customer.ErpPaymentModel;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * @author knadeem
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ErpChargebackReversalPersistentBean extends ErpAbstractChargebackPersistentBean {

	public ErpChargebackReversalPersistentBean() {
		super();
	}
	
	public ErpChargebackReversalPersistentBean(PrimaryKey pk){
		this();
		this.setPK(pk);
	}
	
	/** 
	 * Load constructor. 
	 */
	public ErpChargebackReversalPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}
	
	/**
	 * Copy constructor, from model.
	 * @param bean ErpChargebackReversalModel to copy from
	 */
	public ErpChargebackReversalPersistentBean(ErpChargebackReversalModel model) {
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
		ps.setString(2, EnumTransactionType.CHARGEBACK_REVERSAL.getCode());
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

	protected ErpPaymentModel createModel() {
		return new ErpChargebackReversalModel();
	}

	protected String getTransactionType() {
		return EnumTransactionType.CHARGEBACK_REVERSAL.getCode();
	}

}
