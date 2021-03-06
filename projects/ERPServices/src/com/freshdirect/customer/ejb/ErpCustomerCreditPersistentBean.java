/* Generated by Together */

package com.freshdirect.customer.ejb;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.freshdirect.affiliate.ErpAffiliate;
import com.freshdirect.customer.ErpCustomerCreditModel;
import com.freshdirect.framework.core.DependentPersistentBeanSupport;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;

/**
 * ErpCustomerCredit persistent bean.
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-persistent
 */
public class ErpCustomerCreditPersistentBean extends DependentPersistentBeanSupport {
	/** Default constructor. */
	private double remainingAmount;
	private double originalAmount;
	private String department;
	private PrimaryKey complaintPk;
	private java.util.Date createDate;
	private ErpAffiliate affiliate;


	public ErpCustomerCreditPersistentBean() {
		super();
//		this.remainingAmount = 0.0;
//		this.originalAmount = 0.0;
//		this.department = "";
//		this.complaintPk = new PrimaryKey();
//		this.createDate = new java.util.Date();
	}

	/** Load constructor. */
	public ErpCustomerCreditPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	/**
	 * Copy constructor, from model.
	 * @param bean ErpCustomerCreditModel to copy from
	 */
	public ErpCustomerCreditPersistentBean(ErpCustomerCreditModel model) {
		super(model.getPK());
		this.setFromModel(model);
	}

	/**
	 * Copy into model.
	 * @return ErpCustomerCreditModel object.
	 */
	public ModelI getModel() {
		ErpCustomerCreditModel model = new ErpCustomerCreditModel();
		super.decorateModel(model);
		model.setOriginalAmount(this.originalAmount);
		model.setRemainingAmount(this.remainingAmount);
		model.setDepartment(this.department);
		model.setComplaintPk(this.complaintPk);
		model.setCreateDate(this.createDate);
		model.setAffiliate(this.affiliate);

		return model;
	}

	/** Copy from model. */
	public void setFromModel(ModelI model) {
		ErpCustomerCreditModel m = (ErpCustomerCreditModel)model;
		this.originalAmount = m.getOriginalAmount();
		this.remainingAmount = m.getRemainingAmount();
		this.department = m.getDepartment();
		this.complaintPk = m.getComplaintPk();
		this.createDate = m.getCreateDate();
		this.affiliate = m.getAffiliate();

		this.setModified();
	}

	/**
	 * Find ErpCustomerCreditPersistentBean objects for a given parent.
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 * @return a List of ErpCustomerCreditPersistentBean objects (empty if found none).
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.CUSTOMERCREDIT WHERE CUSTOMER_ID=?");
		ps.setString(1, parentPK.getId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpCustomerCreditPersistentBean bean = new ErpCustomerCreditPersistentBean(new PrimaryKey(rs.getString(1)), conn);
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
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.CUSTOMERCREDIT (ID, CUSTOMER_ID, AMOUNT, ORIGINAL_AMOUNT, DEPARTMENT, COMPLAINT_ID, CREATE_DATE, AFFILIATE) values (?,?,?,?,?,?,?,?)");
		ps.setString(1, id);
		ps.setString(2, this.getParentPK().getId());
		ps.setBigDecimal(3, new BigDecimal(String.valueOf(this.remainingAmount)));
		ps.setBigDecimal(4, new BigDecimal(String.valueOf(this.originalAmount)));
	
		ps.setString(5, this.department);
		ps.setString(6, (this.complaintPk == null ? "" : this.complaintPk.getId()) );
		ps.setTimestamp(7, (this.createDate == null ? new java.sql.Timestamp(System.currentTimeMillis()): new Timestamp(this.createDate.getTime())));
		ps.setString(8, this.affiliate == null ? "" : this.affiliate.getCode());
		
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
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errMsg = null;
		try {
			ps = conn.prepareStatement("SELECT COMPLAINT_ID, AMOUNT, ORIGINAL_AMOUNT, DEPARTMENT, CREATE_DATE, AFFILIATE FROM CUST.CUSTOMERCREDIT WHERE ID=?");
			ps.setString(1, this.getPK().getId());
			rs = ps.executeQuery();
			if (rs.next()) {
				String pk = rs.getString("COMPLAINT_ID");
				if(!rs.wasNull()){
					this.complaintPk = new PrimaryKey(pk);
				}
				this.remainingAmount = rs.getDouble("AMOUNT");
				this.originalAmount = rs.getDouble("ORIGINAL_AMOUNT");
				this.department = rs.getString("DEPARTMENT");
				this.createDate = rs.getTimestamp("CREATE_DATE");
				ErpAffiliate a = ErpAffiliate.getEnum(rs.getString("AFFILIATE"));
				this.affiliate = a == null ? ErpAffiliate.getEnum(ErpAffiliate.CODE_FD) : a;
			} else {
				errMsg = "No such ErpCustomerCredit PK: " + this.getPK();
			}
			this.unsetModified();
		} catch (SQLException sqe) {
			throw sqe;
		} catch (Exception e) {
			throw new SQLException("Unexpected database load error : " + e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					// do nothing, safety block
				}
			}
            if (ps != null) {
                    try {
                            ps.close();
                    } catch (Exception e) {
                            // do nothing, safety block
                    }
            }
		}
		if (errMsg != null) {
			throw new SQLException(errMsg);
		}
	}

	public void store(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("UPDATE CUST.CUSTOMERCREDIT SET COMPLAINT_ID = ?, AMOUNT = ?, ORIGINAL_AMOUNT = ?, DEPARTMENT = ?, CUSTOMER_ID = ? , AFFILIATE = ? WHERE ID=?");
		ps.setString(1, this.complaintPk.getId());
		ps.setBigDecimal(2, new BigDecimal(String.valueOf(this.remainingAmount)));
		ps.setBigDecimal(3, new BigDecimal(String.valueOf(this.originalAmount)));
		ps.setString(4, this.department);
		ps.setString(5, this.getParentPK().getId());
		ps.setString(6, this.affiliate == null ? "" : this.affiliate.getCode());
		ps.setString(7, this.getPK().getId() );

		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not updated");
		}
		ps.close();
		ps = null;
		this.unsetModified();

	}

	public void remove(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("DELETE FROM CUST.CUSTOMERCREDIT WHERE ID = ?");
		ps.setString(1, this.getPK().getId());
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not deleted");
		}
		ps.close();
		ps = null;
		this.setPK(null); // make it anonymous
	}
}
