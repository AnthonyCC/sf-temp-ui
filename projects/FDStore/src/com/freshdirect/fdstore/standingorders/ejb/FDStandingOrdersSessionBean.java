package com.freshdirect.fdstore.standingorders.ejb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ejb.ErpLogActivityCommand;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.ejb.FDSessionBeanSupport;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDStandingOrderList;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrderFilterCriteria;
import com.freshdirect.fdstore.standingorders.FDStandingOrderInfoList;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDStandingOrdersSessionBean extends FDSessionBeanSupport {
	
	private static final long serialVersionUID = 1021328260150726448L;

	private final static Category LOGGER = LoggerFactory.getInstance(FDStandingOrdersSessionBean.class);


	/**
	 * Creates a new instance based on an existing customer list
	 * 
	 * @param list
	 * @return
	 * @throws FDResourceException 
	 */
	public FDStandingOrder createStandingOrder(FDCustomerList list) throws FDResourceException {
		if (list == null) {
			return null;
		}

		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			String pk = dao.createEmptyStandingOrder(conn, list.getCustomerPk().getId(), list.getId());
			if (pk != null) {
				return new FDStandingOrder(new PrimaryKey(pk), list);
			}
			LOGGER.error("Failed to create standing order instance for customer list " + list.getId());
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in createStandingOrder() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}		
		return null;	
	}


	public Collection<FDStandingOrder> loadActiveStandingOrders() throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			Collection<FDStandingOrder> ret = dao.loadActiveStandingOrders(conn);
			
			return ret;
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in loadActiveStandingOrders() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}

	public Collection<FDStandingOrder> loadCustomerStandingOrders(FDIdentity identity) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			Collection<FDStandingOrder> ret = dao.loadCustomerStandingOrders(conn, identity);
			
			return ret;
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in loadCustomerStandingOrders() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}

	public FDStandingOrder load(PrimaryKey pk) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			FDStandingOrder ret = dao.load(conn, pk.getId());
			
			return ret;
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in load() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}

	public void delete(FDActionInfo info, FDStandingOrder so) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			dao.deleteStandingOrder(conn, so.getId(), so.getCustomerListId());
			ErpActivityRecord rec = info.createActivity(EnumAccountActivityType.STANDINGORDER_DELETED);
			rec.setStandingOrderId(so.getId());
			this.logActivity(rec);
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in delete() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}

	public String save(FDActionInfo info, FDStandingOrder so, String saleId) throws FDResourceException {
		String primaryKey = null;
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			try {
				FDStandingOrderList customerList = so.getCustomerList();
				if (customerList != null)
					customerList.setModificationDate( new Date() );
			} catch (FDResourceException e) {
				LOGGER.warn( "FDResourceException catched", e );
			}
			
			if (so.getId() == null) {
				// create object
				LOGGER.debug( "Creating new standing order." );
				primaryKey = dao.createStandingOrder(conn, so);
				ErpActivityRecord rec = info.createActivity(EnumAccountActivityType.STANDINGORDER_CREATED);
				rec.setChangeOrderId(saleId);
				rec.setStandingOrderId(primaryKey);
				this.logActivity(rec);
			} else {
				LOGGER.debug( "Updating existing standing order." );
				dao.updateStandingOrder(conn, so);
				primaryKey = so.getId();
				ErpActivityRecord rec = info.createActivity(EnumAccountActivityType.STANDINGORDER_MODIFIED);
				rec.setChangeOrderId(saleId);
				rec.setStandingOrderId(primaryKey);
				this.logActivity(rec);
			}
			
			return primaryKey;
			
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in save() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}
	
	public void assignStandingOrderToOrder(PrimaryKey salePK, PrimaryKey standingOrderPK) throws FDResourceException {		

		LOGGER.debug( "assigning SO["+standingOrderPK+"] to SALE["+salePK+"]" );
		Connection conn=null;
		try {
			 conn = getConnection();
			
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			dao.assignToSale(conn, standingOrderPK.getId(), salePK.getId());
			
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in assignStandingOrderToOrder() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		}finally {
			close(conn);
		}
	}

	public void logActivity(ErpActivityRecord record) {
		new ErpLogActivityCommand(LOCATOR, record).execute();
	}
	
	public FDStandingOrderInfoList getActiveStandingOrdersCustInfo(FDStandingOrderFilterCriteria filter) throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			FDStandingOrderInfoList ret = dao.getActiveStandingOrdersCustInfo(conn,filter);
			
			return ret;
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in loadActiveStandingOrders() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}
	
	public void clearStandingOrderErrors(String[] soIDs,String agentId)throws FDResourceException{
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			if(null !=soIDs && soIDs.length>0){
				Date date = new Date();
				String[] ids = new String[soIDs.length];
				for (int i = 0; i < soIDs.length; i++) {
					ids[i]=soIDs[i].split(",")[0];
				}
				dao.clearStandingOrderErrors(conn,ids);
				
				for (int i = 0; i < soIDs.length; i++) {
					ErpActivityRecord rec = new ErpActivityRecord();
					rec.setActivityType(EnumAccountActivityType.STANDINGORDER_ERROR_CLEARED);
					rec.setSource(EnumTransactionSource.CUSTOMER_REP);
					rec.setInitiator(agentId);
					rec.setStandingOrderId(soIDs[i].split(",")[0]);
					rec.setCustomerId(soIDs[i].split(",")[1]);
					rec.setDate(date);
					this.logActivity(rec);
				}
				
			}			
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in loadActiveStandingOrders() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}
	
	public FDStandingOrderInfoList getFailedStandingOrdersCustInfo() throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			FDStandingOrderInfoList ret = dao.getFailedStandingOrdersCustInfo(conn);
			
			return ret;
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in getFailedStandingOrdersCustInfo() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}
	
	public FDStandingOrderInfoList getMechanicalFailedStandingOrdersCustInfo() throws FDResourceException {
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			FDStandingOrderInfoList ret = dao.getMechanicalFailedStandingOrdersCustInfo(conn);
			
			return ret;
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in getMechanicalFailedStandingOrdersCustInfo() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}
	
	public Map<Date,Date> getStandingOrdersAlternateDeliveryDates() throws FDResourceException{
		Connection conn = null;
		try {
			conn = getConnection();
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			
			Map<Date,Date> map = dao.getStandingOrdersAlternateDeliveryDates(conn);
			
			return map;
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in getStandingOrdersAlternateDeliveryDates() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		} finally {
			close(conn);
		}
	}
}
