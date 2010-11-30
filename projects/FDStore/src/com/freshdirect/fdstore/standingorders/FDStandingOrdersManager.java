package com.freshdirect.fdstore.standingorders;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.customer.ErpSaleInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderHistory;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.adapter.FDOrderInfoAdapter;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.lists.FDStandingOrderList;
import com.freshdirect.fdstore.standingorders.ejb.FDStandingOrdersHome;
import com.freshdirect.fdstore.standingorders.ejb.FDStandingOrdersSB;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDStandingOrdersManager {
	
	private final static Category LOGGER = LoggerFactory.getInstance(FDStandingOrdersManager.class);

	private static FDStandingOrdersHome soHome = null;

	private static void lookupManagerHome() throws FDResourceException {
		if (soHome != null) {
			return;
		}
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			soHome = (FDStandingOrdersHome) ctx.lookup(FDStandingOrdersHome.JNDI_HOME);
		} catch (NamingException ne) {
			throw new FDResourceException(ne);
		} finally {
			try {
				if (ctx != null) {
					ctx.close();
				}
			} catch (NamingException ne) {
				LOGGER.warn("Cannot close Context while trying to cleanup", ne);
			}
		}
	}

	private static void invalidateManagerHome() {
		soHome = null;
	}


	private static FDStandingOrdersManager sharedInstance;

	protected FDStandingOrdersManager() {}
	
	public static synchronized FDStandingOrdersManager getInstance() {
		if (sharedInstance == null) {
			sharedInstance = new FDStandingOrdersManager();
		}
		return sharedInstance;
	}
	

	
	public FDStandingOrder createStandingOrder(FDCustomerList list) throws FDResourceException {
		lookupManagerHome();
		try {
			FDStandingOrdersSB sb = soHome.create();
			
			return sb.createStandingOrder(list);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public Collection<FDStandingOrder> loadActiveStandingOrders() throws FDResourceException {
		lookupManagerHome();
		try {
			FDStandingOrdersSB sb = soHome.create();
			
			return sb.loadActiveStandingOrders();
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public Collection<FDStandingOrder> loadCustomerStandingOrders(FDIdentity identity) throws FDResourceException {
		lookupManagerHome();
		try {
			FDStandingOrdersSB sb = soHome.create();
			
			return sb.loadCustomerStandingOrders(identity);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public FDStandingOrder load(PrimaryKey pk) throws FDResourceException {
		lookupManagerHome();
		try {
			FDStandingOrdersSB sb = soHome.create();
			
			return sb.load(pk);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public void delete(FDActionInfo info, FDStandingOrder so) throws FDResourceException {
		lookupManagerHome();
		try {
			FDStandingOrdersSB sb = soHome.create();
			
			sb.delete(info, so);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
	
	public String save(FDActionInfo info, FDStandingOrder so) throws FDResourceException {
		return save(info, so, null);
	}	

	public String save(FDActionInfo info, FDStandingOrder so, String saleId) throws FDResourceException {
		lookupManagerHome();
		try {
			FDStandingOrdersSB sb = soHome.create();
			
			return sb.save(info, so, saleId);
			
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	};

	
	public FDOrderInfoI getLastOrder(FDUserI user, FDStandingOrder so) throws FDResourceException {
		FDOrderHistory h = (FDOrderHistory) user.getOrderHistory();

		FDOrderInfoI esi = null;
		for (ErpSaleInfo i : h.getErpSaleInfos()) {
			if (so.getId().equalsIgnoreCase(i.getStandingOrderId()) /* && !i.getStatus().isPending() */) {
				FDOrderInfoAdapter x = new FDOrderInfoAdapter(i);
				if (/* x.isModifiable() && */ ( esi == null || esi.getCreateDate().before(x.getCreateDate()) ) ) {
					esi = x;
				}
			}
		}
		
		return esi;
	}
	
	public List<FDOrderInfoI> getAllOrders(FDUserI user, FDStandingOrder so) throws FDResourceException {
		
		FDOrderHistory h = (FDOrderHistory)user.getOrderHistory();
		List<FDOrderInfoI> result = new ArrayList<FDOrderInfoI>();

		for ( ErpSaleInfo i : h.getErpSaleInfos() ) {
			if ( so.getId().equalsIgnoreCase( i.getStandingOrderId() ) ) {
				FDOrderInfoAdapter x = new FDOrderInfoAdapter( i );
				result.add( x );
			}
		}

		return result;
	}
	
	/**
	 * Create new standing order right after the corresponding order placed
	 * @param orderId
	 * @param cart
	 * @param standingOrder
	 * @return
	 * @throws FDResourceException 
	 */
	public boolean assignStandingOrderToSale(String orderId, FDStandingOrder standingOrder) throws FDResourceException {
		
		if ( orderId == null || orderId.trim().equals( "" ) ) {
			LOGGER.error( "assignStandingOrderToSale: orderId is null" );
			return false;
		}
		if ( standingOrder == null ) {
			LOGGER.error( "assignStandingOrderToSale: standingOrder is null" );
			return false;
		}
		if ( standingOrder.getPK() == null ) {
			LOGGER.error( "assignStandingOrderToSale: standingOrder has no PK" );			
			return false;
		}
		
		lookupManagerHome();
		try {
			FDStandingOrdersSB sb = soHome.create();
			
			sb.assignStandingOrderToOrder(new PrimaryKey(orderId), standingOrder.getPK());
			
		} catch (RemoteException e) {
			invalidateManagerHome();
			throw new FDResourceException(e);
		} catch (CreateException e) {
			invalidateManagerHome();
			throw new FDResourceException(e);
		}
		
		return true;
	}


	/**
	 * Creates a new customer list for the specified standing order
	 * with the content of the current shopping cart
	 * or updates if already exists
	 * 
	 * It also saves / updates standing order itself
	 * 
	 * @param ident
	 * @param cart
	 * @param standingOrder
	 * @return primary key of the standing order created
	 * @throws FDResourceException
	 */
	public String manageStandingOrder(FDActionInfo info, FDCartModel cart, FDStandingOrder standingOrder, String saleId) throws FDResourceException {
		
		LOGGER.debug( "manageStandingOrder() starting." );
		
		try {
			FDIdentity ident = standingOrder.getCustomerIdentity(); 
				
			LOGGER.debug( "identity =" + ident );
			
			// #1 - Create customer list (if it has not been)
			if (standingOrder.getCustomerListId() == null) {
				/**  Create shopping list (if not created yet) */
				
				LOGGER.debug( "Creating new shopping list." );
				FDStandingOrderList l = new FDStandingOrderList();
				
				PrimaryKey custPk = new PrimaryKey(ident.getErpCustomerPK());
				LOGGER.debug( "setting customer pk :" + custPk );
				l.setCustomerPk( custPk );
				l.setName(standingOrder.getCustomerListName());
				
				LOGGER.debug( "customer list created :" + l );
				
				// populate list
				Collection<FDCartLineI> cl = cart.getOrderLines();
				LOGGER.debug( "populating customer list : " + cl.size() + "items => "+ cl );
				for (FDCartLineI s : cl) {
					LOGGER.debug( "merging cartline : " + s );
					l.mergeSelection(s, false);
				}

				LOGGER.debug( "storing customer list :" + l );
				String listId = FDListManager.storeCustomerList(l);
				LOGGER.debug( "FDListManager.storeCustomerList() returned:" + listId );
				
				// Standing Order - INIT PHASE #2
				LOGGER.debug( "original l.getId() = " + l.getId() );
				l.setId( listId );
				LOGGER.debug( "new l.getId() = " + l.getId() );
				
				LOGGER.debug( "setting customer list id : listId = " + listId );
				standingOrder.setCustomerListId( listId );
				
			} else {
				
				/** Just update the content */
				LOGGER.debug( "Updating content." );
				
				FDStandingOrderList l = (FDStandingOrderList) FDListManager.getCustomerList(ident, EnumCustomerListType.SO, standingOrder.getCustomerListName());

				// clean list
				l.removeAllLineItems();
				
				// copy items from cart to list
				Collection<FDCartLineI> cl = cart.getOrderLines();
				for (FDCartLineI s : cl) {
					l.mergeSelection(s, false);
				}

				FDListManager.storeCustomerList(l);

				standingOrder.clearLastError();
			}
			
			// #2 Update standing order
			return save(info, standingOrder, saleId);
		} catch (FDResourceException e) {
			ErpActivityRecord rec = info.createActivity(EnumAccountActivityType.STANDINGORDER_SAVE_FAILED);
			if (standingOrder == null)
				rec.setNote("Standing Order Create Failed");
			else
				rec.setNote("Standing Order Modify Failed");				
			rec.setChangeOrderId(saleId);
			rec.setStandingOrderId(standingOrder.getId());
			this.logActivity(rec);
			throw e;
		}
	}

	private void logActivity(ErpActivityRecord record) throws FDResourceException {
		lookupManagerHome();
		try {
			FDStandingOrdersSB sb = soHome.create();
			
			sb.logActivity(record);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}
}
