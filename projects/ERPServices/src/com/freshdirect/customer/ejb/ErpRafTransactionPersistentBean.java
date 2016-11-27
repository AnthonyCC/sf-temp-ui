package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;


import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.referral.extole.EnumRafTransactionStatus;
import com.freshdirect.referral.extole.EnumRafTransactionType;
import com.freshdirect.referral.extole.model.FDRafTransModel;


public class ErpRafTransactionPersistentBean extends ErpReadOnlyPersistentBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7840808060972124136L;
	private FDRafTransModel model;
	
	public ErpRafTransactionPersistentBean() {
		super();
		this.model = new FDRafTransModel();
	}

	/** Load constructor. */
	public ErpRafTransactionPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		this.load(conn);
	}

	private ErpRafTransactionPersistentBean(PrimaryKey pk, ResultSet rs) throws SQLException {
		this();
		this.setPK(pk);
		this.loadFromResultSet(rs);
	}

	/**
	 * Copy constructor, from model.
	 * @param bean ErpCouponLineModel to copy from
	 */
	public ErpRafTransactionPersistentBean(FDRafTransModel model) {
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
		this.model = (FDRafTransModel)model;
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
		//raf trans table parameters
		PreparedStatement ps = conn.prepareStatement("SELECT ID, SALESACTION_ID,TRANS_STATUS,TRANS_TYPE,ERROR_MSG, ERROR_DETAILS,EXTOLE_EVENT_ID,CREATE_TIME,TRANS_TIME FROM CUST.RAF_TRANS WHERE SALESACTION_ID=? ORDER BY ID");
		ps.setString(1, parentPK.getId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpRafTransactionPersistentBean bean = new ErpRafTransactionPersistentBean( new PrimaryKey(rs.getString("ID")), rs );
			bean.setParentPK(parentPK);
			lst.add(bean);
		}
		rs.close();
		ps.close();
		return lst;
	}
	
	private final static String INSERT_QUERY =
	"INSERT INTO CUST.RAF_TRANS (ID,SALESACTION_ID,TRANS_STATUS,TRANS_TYPE,ERROR_MSG, ERROR_DETAILS,EXTOLE_EVENT_ID, CREATE_TIME,TRANS_TIME ) VALUES(?,?,?,?,?,?,?,?,?)";		

	public PrimaryKey create(Connection conn) throws SQLException {
	
		
		String id = SequenceGenerator.getNextId(conn, "CUST", "RAF_SEQ");
		PreparedStatement ps = conn.prepareStatement(INSERT_QUERY);
		ps.setString(1, id);
		ps.setString(2, this.getParentPK().getId());
		ps.setString(3, this.model.getTransStatus().getValue());
		ps.setString(4, this.model.getTransType().toString());
		ps.setString(5, this.model.getErrorMessage());
		ps.setString(6, this.model.getErrorDetails());
		ps.setString(7, this.model.getExtoleEventId());
		ps.setTimestamp(8, new Timestamp(this.model.getCreateTime().getTime()));
		ps.setTimestamp(9, new Timestamp(this.model.getTransTime().getTime()));	
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
				
		PreparedStatement ps = conn.prepareStatement("SELECT ID, SALESACTION_ID,TRANS_STATUS,TRANS_TYPE,ERROR_MSG, ERROR_DETAILS,EXTOLE_EVENT_ID,CREATE_TIME,TRANS_TIME FROM CUST.RAF_TRANS WHERE ID=? ");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.loadFromResultSet(rs);
		} else {
			throw new SQLException("No such RafTrans PK: " + this.getPK()); 
		}
		rs.close();
		ps.close();
	}
//enum method to convert to string
	private void loadFromResultSet(ResultSet rs) throws SQLException {
		this.model.setId(rs.getString("ID"));
		this.model.setSalesActionId(rs.getString("SALESACTION_ID"));
		this.model.setTransStatus(EnumRafTransactionStatus.getEnumFromValue(rs.getString("TRANS_STATUS")));
        this.model.setTransType(EnumRafTransactionType.getEnum(rs.getString("TRANS_TYPE")));
		this.model.setErrorMessage(rs.getString("ERROR_MSG"));
		this.model.setErrorDetails(rs.getString("ERROR_DETAILS"));
		this.model.setCreateTime(rs.getTimestamp("CREATE_TIME"));
		this.model.setTransTime(rs.getTimestamp("TRANS_TIME"));
		
		this.unsetModified();
	}
}
