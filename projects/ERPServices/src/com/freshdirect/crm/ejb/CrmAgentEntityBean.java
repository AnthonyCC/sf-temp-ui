package com.freshdirect.crm.ejb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.DuplicateKeyException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Category;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.framework.core.EntityDAOI;
import com.freshdirect.framework.core.ModelI;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author knadeem
 */

public class CrmAgentEntityBean extends CrmEntityBeanSupport {

	private static final long serialVersionUID = 6967630951655333989L;
	
	private static Category LOGGER = LoggerFactory.getInstance(CrmAgentEntityBean.class);

	protected String getResourceCacheKey() {
		return "com.freshdirect.crm.ejb.CrmAgentHome";
	}

	public PrimaryKey ejbCreate(ModelI model) throws CreateException, DuplicateKeyException {
		Connection conn = null;
		try {
			conn = getConnection();
			initialize();
			setFromModel(model);
			PrimaryKey pk = create(conn);
			return pk;
		} catch (SQLException e) {
			LOGGER.warn("Error in ejbCreate(model), setting rollbackOnly, throwing CreateException", e);
			getEntityContext().setRollbackOnly();
			if (e.getMessage().toLowerCase().indexOf("unique") > -1) {
				throw new DuplicateKeyException("The user id already exists");
			}
			throw new CreateException(
				"SQLException in ejbCreate(ModelI) " + e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}
	
	public PrimaryKey ejbFindByUserIdAndPassword(String userId, String password) throws FinderException, ObjectNotFoundException {
		Connection conn = null;
		try{
			conn = this.getConnection();
			CrmAgentEntityDAO dao = (CrmAgentEntityDAO) this.getDAO();
			return dao.findUserByIdAndPassword(conn, userId, password);
		}catch(SQLException e){
			throw new FinderException(e.getMessage());
		}finally{
			try{
				if(conn != null){
					conn.close();
				}
			}catch(SQLException ignored){
			}
		}
	}
	
	public Collection<PrimaryKey> ejbFindAll() throws FinderException {
		Connection conn = null;
		try{
			conn = this.getConnection();
			CrmAgentEntityDAO dao = (CrmAgentEntityDAO)this.getDAO();
			return dao.findAll(conn);
		}catch(SQLException e){
			throw new FinderException(e.getMessage());
		}finally{
			try{
				if(conn != null){
					conn.close();
				}
			}catch(SQLException ignored){
			}
		}
	}

	protected EntityDAOI getDAO() {
		return new CrmAgentEntityDAO();
	}

	protected String getSchema() {
		return "CUST";
	}
	
	public boolean isActive() {
		return ((CrmAgentModel)this.model).isActive();
	}
	public boolean isMasqueradeAllowed() {
		return ((CrmAgentModel)this.model).isMasqueradeAllowed();
	}
	
	public PrimaryKey ejbFindAgentByLdapId(String ldapId) throws FinderException, ObjectNotFoundException {
		Connection conn = null;
		try{
			conn = this.getConnection();
			CrmAgentEntityDAO dao = (CrmAgentEntityDAO) this.getDAO();
			return dao.findAgentByLdapId(conn, ldapId);
		}catch(SQLException e){
			throw new FinderException(e.getMessage());
		}finally{
			try{
				if(conn != null){
					conn.close();
				}
			}catch(SQLException ignored){
			}
		}
	}

}
