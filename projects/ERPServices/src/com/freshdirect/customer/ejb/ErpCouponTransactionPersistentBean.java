package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import com.freshdirect.fdstore.ecoupon.EnumCouponTransactionStatus;
import com.freshdirect.fdstore.ecoupon.EnumCouponTransactionType;
import com.freshdirect.fdstore.ecoupon.model.ErpCouponTransactionModel;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;


public class ErpCouponTransactionPersistentBean extends ErpReadOnlyPersistentBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7840808060972124136L;
	private ErpCouponTransactionModel model;
	
	public ErpCouponTransactionPersistentBean() {
		super();
		this.model = new ErpCouponTransactionModel();
	}

	/** Load constructor. */
	public ErpCouponTransactionPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		this.load(conn);
	}

	private ErpCouponTransactionPersistentBean(PrimaryKey pk, ResultSet rs) throws SQLException {
		this();
		this.setPK(pk);
		this.loadFromResultSet(rs);
	}

	/**
	 * Copy constructor, from model.
	 * @param bean ErpCouponLineModel to copy from
	 */
	public ErpCouponTransactionPersistentBean(ErpCouponTransactionModel model) {
		this();
		this.setFromModel(model);
	}

	/**
	 * Copy into model.
	 * @return ErpCouponLineModel object.
	 */
	public ModelI getModel() {
		return this.model.deepCopy();
	}

	/** Copy from model. */
	public void setFromModel(ModelI model) {
		this.model = (ErpCouponTransactionModel)model;
	}
	
	public PrimaryKey getPK() {
		return this.model.getPK();
	}
	
	public void setPK(PrimaryKey pk) {
		this.model.setPK(pk);
	}

	/**
	 * 
	 * @param conn
	 * @param parentPK
	 * @return
	 * @throws SQLException
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		List lst = new LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID, SALESACTION_ID,TRANS_STATUS,TRANS_TYPE,ERROR_MSG, ERROR_DETAILS,CREATE_TIME,TRANS_TIME CUST.COUPON_TRANS WHERE SALESACTION_ID=? ORDER BY ID");
		ps.setString(1, parentPK.getId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpCouponTransactionPersistentBean bean = new ErpCouponTransactionPersistentBean( new PrimaryKey(rs.getString("ID")), rs );
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		rs.close();
		ps.close();
		return lst;
	}
	
	private final static String INSERT_QUERY =
		"INSERT INTO CUST.COUPON_TRANS (ID, SALESACTION_ID,TRANS_STATUS,TRANS_TYPE,ERROR_MSG, ERROR_DETAILS,CREATE_TIME,TRANS_TIME) VALUES (?,?,?,?,?,?,?,?)";

	public PrimaryKey create(Connection conn) throws SQLException {
		String id = this.getNextId(conn, "CUST");
		PreparedStatement ps = conn.prepareStatement(INSERT_QUERY);
		ps.setString(1, id);
		ps.setString(2, this.getParentPK().getId());
		ps.setString(3, this.model.getTranStatus().getName());
		ps.setString(4, this.model.getTranType().getName());
		ps.setString(5, this.model.getErrorMessage());
		ps.setString(6, this.model.getErrorDetails());
		ps.setTimestamp(7, new Timestamp(this.model.getCreateTime().getTime()));
		ps.setTimestamp(8, new Timestamp(this.model.getTranTime().getTime()));	
		try {
			if (ps.executeUpdate() != 1) {
					throw new SQLException("Row not created");
			}
			this.setPK(new PrimaryKey(id));
		} catch (SQLException sqle) {
			throw sqle;
		} finally {
			ps.close();
		}
		
		this.unsetModified();
		return this.getPK();
	}

	public void load(Connection conn) throws SQLException {
				
		PreparedStatement ps = conn.prepareStatement("SELECT ID, SALESACTION_ID,TRANS_STATUS,TRANS_TYPE,ERROR_MSG, ERROR_DETAILS,CREATE_TIME,TRANS_TIME CUST.COUPON_TRANS WHERE ID=? ");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.loadFromResultSet(rs);
		} else {
			throw new SQLException("No such ErpCouponLine PK: " + this.getPK()); 
		}
		rs.close();
		ps.close();
	}

	private void loadFromResultSet(ResultSet rs) throws SQLException {
		this.model.setId(rs.getString("ID"));
		this.model.setSaleActionId(rs.getString("SALESACTION_ID"));
		this.model.setTranStatus(EnumCouponTransactionStatus.getEnum(rs.getString("COUPON_DESC")));
		this.model.setTranType(EnumCouponTransactionType.getEnum(rs.getString("TRANS_TYPE")));
		this.model.setErrorMessage(rs.getString("ERROR_MSG"));
		this.model.setErrorDetails(rs.getString("DETAILS"));
		this.model.setCreateTime(rs.getTimestamp("CREATE_TIME"));
		this.model.setTranTime(rs.getTimestamp("TRANS_TIME"));
		
		this.unsetModified();
	}
}
