/*
 * Created on Jun 30, 2005
 *
 */
package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.freshdirect.customer.ErpCustomerAlertModel;
import com.freshdirect.framework.core.DependentPersistentBeanSupport;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * @author jng
 *
 */
public class ErpCustomerAlertPersistentBean extends	DependentPersistentBeanSupport {

	/** Default constructor. */
	private ErpCustomerAlertModel model = new ErpCustomerAlertModel();
	

	public ErpCustomerAlertPersistentBean() {
		super();
	}

	/** Load constructor. */
	public ErpCustomerAlertPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		model.setPK(pk);
		load(conn);
	}

	/**
	 * Copy constructor, from model.
	 * @param bean ErpCustomerAlertModel to copy from
	 */
	public ErpCustomerAlertPersistentBean(ErpCustomerAlertModel m) {
		super(m.getPK());
		this.setFromModel(m);
	}

	/**
	 * Copy into model.
	 * @return ErpCustomerAlertModel object.
	 */
	public ModelI getModel() {
		super.decorateModel(model);
		return model;
	}

	/** Copy from model. */
	public void setFromModel(ModelI m) {
		model = (ErpCustomerAlertModel)m;
		this.setModified();
	}

	/**
	 * Find ErpCustomerAlertPersistentBean objects for a given parent.
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 * @return a List of ErpCustomerAlertPersistentBean objects (empty if found none).
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.CUSTOMERALERT WHERE CUSTOMER_ID=?");
		ps.setString(1, parentPK.getId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpCustomerAlertPersistentBean bean = new ErpCustomerAlertPersistentBean(new PrimaryKey(rs.getString(1)), conn);
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		rs.close();
		rs = null;
		ps.close();
		ps = null;
		return lst;
	}

	public PrimaryKey create(Connection conn) throws SQLException {
		String id = this.getNextId(conn, "CUST");
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.CUSTOMERALERT (ID, CUSTOMER_ID, ALERT_TYPE, CREATE_DATE, CREATE_USER_ID, NOTE) values (?,?,?,?,?,?)");
		int index = 1;
		ps.setString(index++, id);
		ps.setString(index++, this.getParentPK().getId());
		ps.setString(index++, this.model.getAlertType());
		ps.setTimestamp(index++, (this.model.getCreateDate() == null ? new java.sql.Timestamp(System.currentTimeMillis()): new Timestamp(this.model.getCreateDate().getTime())));
		ps.setString(index++, this.model.getCreateUserId());
		ps.setString(index++, this.model.getNote());
		
		try {
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not created");
			}
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

	public void load(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT CUSTOMER_ID, ALERT_TYPE, CREATE_DATE, CREATE_USER_ID, NOTE FROM CUST.CUSTOMERALERT WHERE ID=?");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.model.setCustomerId(rs.getString("CUSTOMER_ID"));
			this.model.setAlertType(rs.getString("ALERT_TYPE"));
			this.model.setCreateDate(rs.getTimestamp("CREATE_DATE"));
			this.model.setCreateUserId(rs.getString("CREATE_USER_ID"));
			this.model.setNote(rs.getString("NOTE"));
			this.model.setPK(new PrimaryKey(this.getPK().getId()));
		} else {
			throw new SQLException("No such ErpCustomerAlert PK: " + this.getPK());
		}
		rs.close();
		rs = null;
		ps.close();
		ps = null;

		this.unsetModified();
	}

	public void store(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("UPDATE CUST.CUSTOMERALERT SET CUSTOMER_ID = ?, ALERT_TYPE = ?, CREATE_DATE = ?, CREATE_USER_ID = ?, NOTE = ? WHERE ID=?");
		int index = 1;
		ps.setString(index++, this.model.getCustomerId());
		ps.setString(index++, this.model.getAlertType());
		ps.setTimestamp(index++, (this.model.getCreateDate() == null ? new java.sql.Timestamp(System.currentTimeMillis()): new Timestamp(this.model.getCreateDate().getTime())));
		ps.setString(index++, this.model.getCreateUserId());
		ps.setString(index++, this.model.getNote());
		ps.setString(index++, this.getPK().getId() );

		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not updated");
		}
		ps.close();
		ps = null;
		this.unsetModified();

	}

	public void remove(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("DELETE FROM CUST.CUSTOMERALERT WHERE ID = ?");
		ps.setString(1, this.getPK().getId());
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not deleted");
		}
		ps.close();
		ps = null;
		this.setPK(null); // make it anonymous
	}

}
