/*
 * Created on Apr 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.freshdirect.customer.EnumTransactionType;
import com.freshdirect.customer.ErpFundsRedepositModel;
import com.freshdirect.customer.ErpPaymentModel;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * @author jng
 *
 */
public class ErpFundsRedepositPersistentBean extends ErpAbstractSettlementPersistentBean {

	public ErpFundsRedepositPersistentBean() {
		super();
		this.model = new ErpFundsRedepositModel();
	}
	
    public ErpFundsRedepositPersistentBean(PrimaryKey pk){
		this();
    	super.setPK(pk);
    }
	
	/** 
	 * Load constructor. 
	 */
	public ErpFundsRedepositPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}


	public ErpFundsRedepositPersistentBean(ErpFundsRedepositModel model) {
		this();
		this.setFromModel(model);
	}

	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.SALESACTION WHERE SALE_ID=? AND ACTION_TYPE = ?");
		ps.setString(1, parentPK.getId());
		ps.setString(2, EnumTransactionType.FUNDS_REDEPOSIT.getCode());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpFundsRedepositPersistentBean bean = new ErpFundsRedepositPersistentBean(new PrimaryKey(rs.getString(1)), conn);
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
		return EnumTransactionType.FUNDS_REDEPOSIT.getCode();
	}
	
	protected ErpPaymentModel createModel() {
		return new ErpFundsRedepositModel();
	}

}
