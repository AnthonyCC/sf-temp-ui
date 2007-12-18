package com.freshdirect.customer.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.crm.ejb.CustomerEmailDAO;
import com.freshdirect.customer.EnumComplaintType;
import com.freshdirect.customer.EnumComplaintStatus;
import com.freshdirect.customer.EnumSendCreditEmail;
import com.freshdirect.customer.ErpComplaintLineModel;
import com.freshdirect.customer.ErpComplaintModel;
import com.freshdirect.customer.ErpCustomerEmailModel;
import com.freshdirect.framework.collection.DependentPersistentBeanList;
import com.freshdirect.framework.core.DependentPersistentBeanSupport;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;


/**
 * ErpComplaint persistent bean.
 * @version    $Revision$
 * @author     $Author$
 * @stereotype fd-persistent
 */
public class ErpComplaintPersistentBean extends DependentPersistentBeanSupport {

	private ErpComplaintModel model = new ErpComplaintModel();
	
	/** Default constructor. */
	public ErpComplaintPersistentBean() {
		super();
	}

	/** Load constructor. */
	public ErpComplaintPersistentBean(PrimaryKey pk, Connection conn) throws SQLException {
		this();
		this.setPK(pk);
		load(conn);
	}

	/**
	 * Copy constructor, from model.
	 * @param bean ErpComplaintModel to copy from
	 */
	public ErpComplaintPersistentBean(ErpComplaintModel model) {
		this();
		this.setFromModel(model);
	}
	
	public void setPK(PrimaryKey pk) {
		model.setPK(pk);
	}
	
	
	public PrimaryKey getPK() {
		return model.getPK();
	}
	
	/**
	 * Copy into model.
	 * @return ErpComplaintModel object.
	 */
	public ModelI getModel() {
		return this.model.deepCopy();
	}

	/** Copy from model. */
	public void setFromModel(ModelI model) {
		this.model = (ErpComplaintModel)model;
		this.setModified();
	}

	/**
	 * Find ErpComplaintPersistentBean objects for a given parent.
	 * @param conn the database connection to operate on
	 * @param parentPK primary key of parent
	 * @return a List of ErpComplaintPersistentBean objects (empty if found none).
	 * @throws SQLException if any problems occur talking to the database
	 */
	public static List findByParent(Connection conn, PrimaryKey parentPK) throws SQLException {
		java.util.List lst = new java.util.LinkedList();
		PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.COMPLAINT WHERE SALE_ID=?");
		ps.setString(1, parentPK.getId());
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ErpComplaintPersistentBean bean = new ErpComplaintPersistentBean(new PrimaryKey(rs.getString(1)), conn);
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
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.COMPLAINT (ID, SALE_ID, CREATE_DATE, CREATED_BY, APPROVED_DATE, APPROVED_BY, AMOUNT, NOTE, STATUS, EMAIL_ID,EMAIL_OPTION,COMPLAINT_TYPE) values (?,?,?,?,?,?,?,?,?,?,?,?)");
		ps.setString(1, id);
		ps.setString(2, this.getParentPK().getId());
		ps.setTimestamp(3, new java.sql.Timestamp( model.getCreateDate().getTime()));
		ps.setString(4, this.model.getCreatedBy());
		//approve date and aprove by is only set here because of $0 complaints otherwise they should be set to null as
		// when a complaint is created conceptualy these values should be null :(
		ps.setTimestamp(5, this.model.getApprovedDate() != null ? new java.sql.Timestamp(this.model.getApprovedDate().getTime()) : null);
		ps.setString(6, this.model.getApprovedBy());
		ps.setDouble(7, this.model.getAmount());
		ps.setString(8, this.model.getDescription());
		ps.setString(9, this.model.getStatus().getStatusCode());
		ps.setString(10,this.model.getCustomerEmail() !=null ? this.model.getCustomerEmail().getPK().getId(): "");
		ps.setString(11,this.model.getEmailOption() !=null ? this.model.getEmailOption().getName(): "");
		ps.setString(12, this.model.getType().getName());
		
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
		// create children
		ComplaintLineList clList = this.getComplaintLinePBList();
		clList.create( conn );

		this.unsetModified();
		return this.getPK();
	}


	public void store(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("UPDATE CUST.COMPLAINT SET SALE_ID = ?, CREATE_DATE = ?, CREATED_BY = ?, APPROVED_DATE = ?, APPROVED_BY = ?, AMOUNT = ?, NOTE = ?, STATUS = ?, EMAIL_ID = ? , EMAIL_OPTION=? WHERE ID=?");
		
		ps.setString(1, this.getParentPK().getId());
		ps.setTimestamp(2, new java.sql.Timestamp(this.model.getCreateDate().getTime()));
		ps.setString(3, this.model.getCreatedBy());
		
		if(this.model.getApprovedDate() == null){
			ps.setNull(4, Types.TIMESTAMP);
		} else {
			ps.setTimestamp(4, new java.sql.Timestamp(this.model.getApprovedDate().getTime()));
		}
		
		ps.setString(5, this.model.getApprovedBy());
		ps.setDouble(6, this.model.getAmount());
		ps.setString(7, this.model.getDescription());
		ps.setString(8, this.model.getStatus().getStatusCode());
		ps.setString(9, this.model.getCustomerEmail() !=null ? this.model.getCustomerEmail().getPK().getId() : null);
		ps.setString(10, this.model.getEmailOption() !=null ? this.model.getEmailOption().getName() : "");
		ps.setString(11, this.model.getPK().getId());

		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not updated");
		}
		ps.close();
		ps = null;
		this.unsetModified();
	}

	public void remove(Connection conn) throws SQLException {
		
		//remove children
		ComplaintLineList clList = this.getComplaintLinePBList();
		clList.remove(conn);

		// remove self
		PreparedStatement ps = conn.prepareStatement("DELETE FROM CUST.COMPLAINT WHERE ID=?");
		ps.setString(1, this.model.getPK().getId());
		if (ps.executeUpdate() != 1) {
			throw new SQLException("Row not deleted");
		}
		ps.close();
		ps = null;

		this.setPK(null); // make it anonymous
	}


	public void load(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("SELECT CREATE_DATE, CREATED_BY, APPROVED_DATE, APPROVED_BY, AMOUNT, NOTE, STATUS,EMAIL_OPTION,EMAIL_ID, COMPLAINT_TYPE FROM CUST.COMPLAINT WHERE ID=?");
		ps.setString(1, this.getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			this.model.setCreateDate(rs.getTimestamp("CREATE_DATE"));
			this.model.setCreatedBy(rs.getString("CREATED_BY"));
			this.model.setApprovedDate(rs.getTimestamp("APPROVED_DATE"));
			this.model.setApprovedBy(rs.getString("APPROVED_BY"));
			this.model.setDescription(rs.getString("NOTE"));
			this.model.setStatus(EnumComplaintStatus.getComplaintStatus(rs.getString("STATUS")));
			this.model.setEmailOption(EnumSendCreditEmail.getEnum(rs.getString("EMAIL_OPTION")));
			this.model.setCustomerEmail(this.getCustomerEmail(conn, rs.getString("EMAIL_ID"))); 
			this.model.setType(EnumComplaintType.getEnum(rs.getString("COMPLAINT_TYPE")));
		} else {
			throw new SQLException("No such ErpComplaint PK: " + this.getPK());
		}
		rs.close();
		rs = null;
		ps.close();
		ps = null;

		// load children
		ComplaintLineList clList = new ComplaintLineList();
		clList.setParentPK(this.getPK());
		clList.load(conn);
		this.model.setComplaintLines(clList.getModelList());
		
		this.unsetModified();
	}


	protected ComplaintLineList getComplaintLinePBList(){
		ComplaintLineList clList = new ComplaintLineList();
		clList.setParentPK(this.getPK());
		for(Iterator i = this.model.getComplaintLines().iterator(); i.hasNext(); ){
			ErpComplaintLinePersistentBean pb = new ErpComplaintLinePersistentBean();
			pb.setFromModel((ErpComplaintLineModel)i.next());
			clList.add(pb);
		}
		return clList;
	}

	protected ErpCustomerEmailModel getCustomerEmail(Connection conn,String emailId)  throws SQLException {
		if (emailId==null || "".equals(emailId)) return null;
		CustomerEmailDAO ced = new CustomerEmailDAO();
		return (ErpCustomerEmailModel) ced.load(conn, new PrimaryKey(emailId));			
		
	}

	protected static class ComplaintLineList extends DependentPersistentBeanList {
	    public void load(Connection conn) throws SQLException {
			this.set(ErpComplaintLinePersistentBean.findByParent(conn, (PrimaryKey)ComplaintLineList.this.getParentPK()));
	    }
	}

}
