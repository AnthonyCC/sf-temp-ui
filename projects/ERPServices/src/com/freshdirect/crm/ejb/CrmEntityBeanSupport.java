package com.freshdirect.crm.ejb;

import java.sql.Connection;
import java.sql.SQLException;

import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import com.freshdirect.framework.core.EntityBeanSupport;
import com.freshdirect.framework.core.EntityDAOI;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

public abstract class CrmEntityBeanSupport extends EntityBeanSupport {

	private static final long	serialVersionUID	= 2650620991286768117L;
	
	protected ModelI model;

	public void initialize() {
		this.model = null;
	}

	public ModelI getModel() {
		return this.model.deepCopy();
	}

	public void setFromModel(ModelI model) {
		this.model = model.deepCopy();
		this.setModified();
	}

	protected abstract EntityDAOI getDAO();

	protected abstract String getSchema();

	public PrimaryKey ejbFindByPrimaryKey(PrimaryKey pk)
		throws ObjectNotFoundException, FinderException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			return this.getDAO().findByPrimaryKey(conn, pk);
		} catch (SQLException e) {
			throw new FinderException(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ignored) {
			}
		}
	}

	public PrimaryKey create(Connection conn) throws SQLException {
		PrimaryKey pk = new PrimaryKey(this.getNextId(conn, this.getSchema()));
		this.getDAO().create(conn, pk, this.model);
		this.setPK(pk);
		super.decorateModel((ModelSupport) this.model);
		return pk;
	}

	public void load(Connection conn) throws SQLException {
		this.model = this.getDAO().load(conn, this.getPK());
	}

	public void store(Connection conn) throws SQLException {
		if (super.isModified()) {
			this.getDAO().store(conn, this.model);
		}
	}

	public void remove(Connection conn) throws SQLException {
		this.getDAO().remove(conn, this.getPK());
	}

}
