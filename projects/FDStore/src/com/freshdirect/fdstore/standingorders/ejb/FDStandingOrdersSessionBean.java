package com.freshdirect.fdstore.standingorders.ejb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ejb.ErpLogActivityCommand;
import com.freshdirect.delivery.DlvZoneInfoModel;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.FDTransientCartModel;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.customer.ejb.FDSessionBeanSupport;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListItem;
import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;
import com.freshdirect.fdstore.lists.FDStandingOrderList;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
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
		Connection conn = null;
		try {
			conn=getConnection();
			
			FDStandingOrderDAO dao = new FDStandingOrderDAO();
			dao.assignToSale(conn, standingOrderPK.getId(), salePK.getId());
			
		} catch (SQLException e) {
			LOGGER.error( "SQL ERROR in assignStandingOrderToOrder() : " + e.getMessage(), e );
			e.printStackTrace();
			throw new FDResourceException(e);
		}
		finally {
			close(conn);
		}
	}

	public void logActivity(ErpActivityRecord record) {
		new ErpLogActivityCommand(LOCATOR, record).execute();
	}
	public FDUser getUser(FDIdentity identity) throws FDAuthenticationException, FDResourceException {
		return FDCustomerManager.recognize(identity);
	}
	public List<FDProductSelectionI> getValidProductSelectionsFromCCLItems(List<FDCustomerListItem> cclItems) throws FDResourceException {
		List<FDProductSelectionI> productSelections = new ArrayList<FDProductSelectionI>();
		for (Iterator<FDCustomerListItem> it = cclItems.iterator(); it.hasNext();) {
			FDCustomerProductListLineItem pl = (FDCustomerProductListLineItem)it.next(); 
			try {
				FDProductSelectionI item = pl.convertToSelection();
				productSelections.add( item );
			} catch (FDSkuNotFoundException e) {
				// Invalid selections are omitted
				continue;
			} catch (FDResourceException e) {
				throw e;
			}
		}
		return productSelections;
	}
	public  boolean isValidCustomerList(List<FDCustomerListItem> cclItems) throws FDResourceException {
		for ( FDCustomerListItem item : cclItems ) {
			if ( item instanceof FDCustomerProductListLineItem ) {
				FDCustomerProductListLineItem pl = (FDCustomerProductListLineItem)item; 
				try {
					pl.convertToSelection();
				} catch (FDSkuNotFoundException e) {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}
	public FDCartModel getCart(ErpPaymentMethodI paymentMethod,ErpAddressModel erpDeliveryAddress,FDReservation reservation,DlvZoneInfoModel zoneInfo,List<FDCustomerListItem> custListItems)throws FDResourceException {
		// build a cart
		FDCartModel cart = new FDTransientCartModel();
		// set cart parameters		
		cart.setPaymentMethod( paymentMethod );
		cart.setDeliveryAddress( erpDeliveryAddress );		
		cart.setDeliveryReservation( reservation );
        cart.setZoneInfo( zoneInfo );
        
        // fill the cart with items
		List<FDProductSelectionI> productSelectionList = OrderLineUtil.getValidProductSelectionsFromCCLItems( custListItems );
		
		try {
			for ( FDProductSelectionI ps : productSelectionList ) {
				FDCartLineI cartLine = new FDCartLineModel( ps );
				if ( !cartLine.isInvalidConfig() ) {
					cart.addOrderLine( cartLine );
				} /*else {
					hasInvalidItems = true;
				}*/
			}
			cart.refreshAll();			
		} catch ( FDInvalidConfigurationException e ) {
			LOGGER.info( "Shopping list contains some items with invalid configuration." );
		}
		return cart;
	}
}
