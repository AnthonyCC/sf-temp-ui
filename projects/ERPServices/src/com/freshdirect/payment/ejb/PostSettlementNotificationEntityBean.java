package com.freshdirect.payment.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumNotificationType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.erp.model.NotificationModel;
import com.freshdirect.framework.core.DataSourceLocator;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.log.LoggerFactory;

public class PostSettlementNotificationEntityBean implements EntityBean {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8292603198038001073L;
	private static Category LOGGER = LoggerFactory.getInstance(PostSettlementNotificationEntityBean.class);
	private NotificationModel model;
	private boolean modified = false;
	private PrimaryKey pk;
	private EntityContext entityCtx;
	
	public void initialize() {
		model = new NotificationModel(null, null, null,null,0.0);
	}

	public NotificationModel getModel() {
		return model.getModel();
	}
	
	
	public boolean isModified(){
		return this.modified;
	}

	/** Copy from model. */
	public void setFromModel(NotificationModel model) {
		this.model.setAmount(model.getAmount());
		this.model.setInsertDate(model.getInsertDate());
		this.model.setNotification_status(model.getNotification_status());
		this.model.setNotification_type(model.getNotification_type());
		this.model.setPk(model.getPk());
		this.model.setSale_id(model.getSale_id());
		this.model.setThird_party_name(model.getThird_party_name());
	}

	public void setPK(PrimaryKey pk) {
		this.pk=pk;
		model.setPk(pk);
	}

	public PrimaryKey getPK() {
		return this.pk;
	}
	
	public PrimaryKey ejbFindByPrimaryKey(PrimaryKey pk) throws ObjectNotFoundException, FinderException {
		Connection conn = null;
		try {
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.PYMT_STLMNT_NOTIFICATION where ID=?");
			ps.setString(1, pk.getId());
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				throw new ObjectNotFoundException("Unable to find ErpSale with PK " + pk);
			}

			PrimaryKey foundPk = new PrimaryKey(rs.getString(1));
			rs.close();
			ps.close();

			return foundPk;

		} catch (SQLException sqle) {
			throw new FinderException(sqle.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle2) {
				LOGGER.warn("Error closing connection", sqle2);
			}
		}		
	}
	
	public PrimaryKey ejbFindBySalesIdAndType(String saleId, EnumNotificationType type) throws ObjectNotFoundException, FinderException {
		Connection conn = null;
		try {
			initialize();
			conn = getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT ID FROM CUST.PYMT_STLMNT_NOTIFICATION where SALE_ID=? and NOTIFICATION_TYPE=?");
			ps.setString(1, saleId);
			ps.setString(2, type.getCode());
			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				throw new ObjectNotFoundException("Unable to find SettlementNotification with saleId " + saleId);
			}

			PrimaryKey foundPk = new PrimaryKey(rs.getString(1));
			setPK(foundPk);
			rs.close();
			ps.close();

			return foundPk;

		} catch (SQLException sqle) {
			throw new FinderException(sqle.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle2) {
				LOGGER.warn("Error closing connection", sqle2);
			}
		}		
	}
	
	public Collection<PrimaryKey> ejbFindMultipleByPrimaryKeys(Collection<PrimaryKey> pks) throws ObjectNotFoundException, FinderException {
		Connection conn = null;
		try {
			conn = getConnection();
			
			StringBuilder q = new StringBuilder("SELECT ID FROM CUST.PYMT_STLMNT_NOTIFICATION where ID in (");

			for(int i=0;i<pks.size();++i){
				if(i==pks.size()-1){
					q.append("?)");					
				}else{
					q.append("?,");
				}
			}
			PreparedStatement ps = conn.prepareStatement(q.toString());
			int i=1;
			for(PrimaryKey key: pks){
				ps.setString(i, key.getId());
				++i;
			}

			ResultSet rs = ps.executeQuery();
			
			List<PrimaryKey> keys = new ArrayList<PrimaryKey>();
			while(rs.next()){
				keys.add(new PrimaryKey(rs.getString(1)));
			}

			rs.close();
			ps.close();

			return keys;

		} catch (SQLException sqle) {
			throw new FinderException(sqle.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle2) {
				LOGGER.warn("Error closing connection", sqle2);
			}
		}
	}
	
	public PrimaryKey ejbFindByCriteria(String salesId, EnumSaleStatus status, EnumNotificationType type) throws FinderException{
		 List<PrimaryKey> keys = (List<PrimaryKey>) performFind("SELECT ID FROM CUST.PYMT_STLMNT_NOTIFICATION WHERE SALE_ID=? and NOTIFICATION_STATUS=? and NOTIFICATION_TYPE=?", salesId, status.getStatusCode(), type.getCode());
		 return keys.get(0);
	}
	
	public Collection<PrimaryKey> ejbFindByStatusAndType(EnumSaleStatus saleStatus, EnumNotificationType type)
			throws FinderException {
		return performFind("SELECT ID FROM CUST.PYMT_STLMNT_NOTIFICATION WHERE NOTIFICATION_STATUS=? and NOTIFICATION_TYPE=?"
				, saleStatus.getStatusCode(), type.getCode(), null);
	}
	
	public Collection<PrimaryKey> ejbFindSaleIdsByStatusAndType(EnumSaleStatus saleStatus, EnumNotificationType type)
			throws FinderException {
		return performFind("SELECT ID FROM CUST.PYMT_STLMNT_NOTIFICATION WHERE NOTIFICATION_STATUS=? and NOTIFICATION_TYPE=?"
				, saleStatus.getStatusCode(), type.getCode(), null);
	}

	private Collection<PrimaryKey> performFind(String statement, String parameter1, String parameter2, String parameter3) throws FinderException {
		Connection conn = null;
		try {
			conn = getConnection();

			PreparedStatement ps = conn.prepareStatement(statement);
			if (parameter1 != null) {
				ps.setString(1, parameter1);
			}
			if (parameter2 != null) {
				ps.setString(2, parameter2);
			}
			if (parameter3 != null) {
				ps.setString(3, parameter3);
			}
			ResultSet rs = ps.executeQuery();

			List<PrimaryKey> lst = new ArrayList<PrimaryKey>();
			while (rs.next()) {
				String id = rs.getString(1);
				lst.add(new PrimaryKey(id));
			}

			rs.close();
			ps.close();

			return lst;

		} catch (SQLException sqle) {
			throw new FinderException(sqle.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle2) {
				LOGGER.warn("Error closing connection", sqle2);
			}
		}
	}
	
	public PrimaryKey create(Connection conn) throws SQLException {
		setPK(new PrimaryKey(getNextId(conn, "CUST")));
		PreparedStatement ps = conn.prepareStatement("INSERT INTO CUST.PYMT_STLMNT_NOTIFICATION (ID,SALE_ID,AMOUNT,NOTIFICATION_TYPE,INSERT_DATE,NOTIFICATION_STATUS,THIRD_PARTY_NAME) values (?,?,?,?,?,?,?)");
		ps.setString(1, getPK().getId());
		ps.setString(2, model.getSale_id());
		ps.setDouble(3, model.getAmount());
		ps.setString(4, model.getNotification_type().getCode());
		ps.setTimestamp(5, new Timestamp(model.getInsertDate().getTime()));
		ps.setString(6, model.getNotification_status().getStatusCode());
		ps.setString(7, model.getThird_party_name());
	
		try {
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not created");
			}
		} catch (SQLException sqle) {
			setPK(null);
			throw sqle;
		} finally {
			ps.close();
		}


		return getPK();
	}
	
	
	public void load(Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("select SALE_ID,AMOUNT,NOTIFICATION_TYPE,NOTIFICATION_STATUS,THIRD_PARTY_NAME from CUST.PYMT_STLMNT_NOTIFICATION where ID=?");
		ps.setString(1, getPK().getId());
		ResultSet rs = ps.executeQuery();
		if (!rs.next()) {
			throw new SQLException("No such Notification PK: " + getPK());
		}
		String salesPk = rs.getString(1);
		Double amount = rs.getDouble(2);
		EnumNotificationType notification_type = EnumNotificationType.getNotificationType((rs.getString(3)));
		EnumSaleStatus notification_status = EnumSaleStatus.getSaleStatus(rs.getString(4));
		String thirdParty = rs.getString(5);
		this.model = new NotificationModel(salesPk, notification_type, notification_status, thirdParty, amount);
		this.model.setPk(getPK());
	}
	
   private String getNextId(Connection conn, String schema) throws SQLException {
	   return SequenceGenerator.getNextId(conn, schema);
	}

   public Connection getConnection() throws SQLException {
		   		return DataSourceLocator.getConnection(null);
	 }

     
   /**
    * Creates an entity from data provided by a model object.
    * Writes a new instance of this entity to the persistent store.
    *
    * @param model the data to use to create the new entity
    * @throws CreateException any problems during create, such as an inability to insert a row in a database table
    *
    * @return the primary key of the newly created entity
    */
   public PrimaryKey ejbCreate(NotificationModel model) throws CreateException {
       Connection conn = null;
       try {
           initialize();
           setFromModel(model);
           conn = getConnection();
           PrimaryKey pk = create(conn);
           return pk;
       } catch (SQLException sqle) {
           LOGGER.warn("Error in ejbCreate(model), setting rollbackOnly, throwing CreateException", sqle);
           throw new CreateException("SQLException in ejbCreate(ModelI) "+sqle.getMessage());
       } finally {
           if (conn != null) {
               try {
                   conn.close();
               } catch (SQLException sqle2) {
                   LOGGER.warn("Unable to close connection after ejbCreate(ModelI)");
               }
           }
       }
   }
   
   /**
    * Perform any additional tasks after a default object has been created in the persistent store.
    */
/*   public void ejbPostCreate() {
       modified = false;
   }*/
   
   /**
    * Perform any additional tasks after this object's data has been created in the persistent store.
    *
    * @param model the model that was used in the corresponding create method
    */
   public void ejbPostCreate(NotificationModel model) {
	   modified = false;
   }
 


	@Override
	public void ejbActivate() throws EJBException, RemoteException {
		modified = true;
	}

	@Override
	public void ejbLoad() throws EJBException, RemoteException {
		initialize();
        Connection conn = null;
        try {
   			this.setPK( (PrimaryKey)this.getPK() );
            conn = getConnection();
            load(conn);
            modified = false;
        } catch (SQLException sqle) {
        	LOGGER.warn("Error in ejbLoad, throwing EJBException", sqle);
            throw new EJBException(sqle);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle2) {
                    LOGGER.warn("Unable to close connection after ejbLoad()");
                }
            }
        }

        if (this.modified) {
	        LOGGER.warn("ASSERTION FAILED: ejbLoad finished for "+this+" with isModified true");
        }
	}

	@Override
	public void ejbPassivate() throws EJBException, RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void ejbRemove() throws RemoveException, EJBException,
			RemoteException {
		throw new UnsupportedOperationException("Removal of Notification is not possible");

	}

	@Override
	public void ejbStore() throws EJBException, RemoteException {
		if (!modified) {
			return;
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("ejbStore for "+this);
		}

		if (this.getEntityContext().getRollbackOnly()) {
			LOGGER.warn("ejbStore called after rollback");
			return;
		}

        Connection conn = null;
        try {
            conn = getConnection();
            update(conn, this.model);
            modified = false;
        } catch (SQLException sqle) {
        	LOGGER.warn("Error in ejbStore, setting rollbackOnly, throwing EJBException", sqle);
            throw new EJBException(sqle);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle2) {
                    LOGGER.warn("Unable to close connection after ejbStore()");
                }
            }
        }
	}
	
	public void updateNotification(NotificationModel model){
		  Connection conn = null;
	        try {
	            conn = getConnection();
	            update(conn, model);
	            modified = false;
	        } catch (SQLException sqle) {
	        	LOGGER.warn("Error in ejbStore, setting rollbackOnly, throwing EJBException", sqle);
	            throw new EJBException(sqle);
	        } finally {
	            if (conn != null) {
	                try {
	                    conn.close();
	                } catch (SQLException sqle2) {
	                    LOGGER.warn("Unable to close connection after ejbStore()");
	                }
	            }
	        }
	}

	String update = "update CUST.PYMT_STLMNT_NOTIFICATION set SALE_ID = ?, AMOUNT=?, NOTIFICATION_TYPE=?, INSERT_DATE=?, NOTIFICATION_STATUS=?,THIRD_PARTY_NAME=? where id=?";
	
	public void update(Connection conn, NotificationModel model) throws SQLException {
			PreparedStatement ps = conn.prepareStatement(update);
			int index = 1;
			ps.setString(index++, model.getSale_id()!=null?model.getSale_id():null);
			ps.setDouble(index++, model.getAmount());
			ps.setString(index++, model.getNotification_type()!=null?model.getNotification_type().getCode():null);
			ps.setTimestamp(index++, new Timestamp(model.getInsertDate().getTime()));
			ps.setString(index++, model.getNotification_status()!=null?model.getNotification_status().getStatusCode():null);
			ps.setString(index++, model.getThird_party_name()!=null?model.getThird_party_name():null);
			ps.setString(index++, getPK().getId());
			if (ps.executeUpdate() != 1) {
				throw new SQLException("Row not updated");
			}
 		ps.close();	
	}

	@Override
	public void setEntityContext(EntityContext context) throws EJBException,
			RemoteException {
        this.entityCtx = context;
        modified  =true;

	}
	
	public EntityContext getEntityContext() throws EJBException,
			RemoteException {
		return this.entityCtx;
	}

	@Override
	public void unsetEntityContext() throws EJBException, RemoteException {
		this.entityCtx = null;
	}

}
