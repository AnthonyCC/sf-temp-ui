package com.freshdirect.erp.ejb;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.ObjectNotFoundException;
import javax.naming.NamingException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.log4j.Category;

import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.core.SessionBeanSupport;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ErpInventoryManagerSessionBean extends SessionBeanSupport {

	private static Category LOGGER = LoggerFactory.getInstance(ErpInventoryManagerSessionBean.class);

	private final static ServiceLocator LOCATOR = new ServiceLocator();

	public void updateInventories(List inventories) {
		ErpInventoryHome inventoryHome = this.getInventoryHome();

		UserTransaction utx = null;

		for (Iterator i = inventories.iterator(); i.hasNext();) {

			utx = null;
			try {
				utx = this.getSessionContext().getUserTransaction();
				utx.begin();

				ErpInventoryModel inv = (ErpInventoryModel) i.next();
				LOGGER.debug("Updating inventory " + inv);
				try {

					ErpInventoryEB eb = inventoryHome.findByPrimaryKey(new PrimaryKey(inv.getSapId()));
					// update the inventory cache in the persistent store					
					eb.setEntries(new Date(), inv.getEntries());

				} catch (ObjectNotFoundException ex) {
					// not found, create inventory
					inventoryHome.create(inv);
				}

				utx.commit();

			} catch (Exception e) {
				LOGGER.warn("Exception occured while storing inventory", e);
				if (utx != null)
					try {
						utx.rollback();
					} catch (SystemException se) {
						LOGGER.warn("Error while trying to rollback transaction", se);
					}
				// just keep going :)
			}
		}

	}

	private ErpInventoryHome getInventoryHome() {
		try {
			return (ErpInventoryHome) LOCATOR.getRemoteHome("java:comp/env/ejb/ErpInventory");
		} catch (NamingException e) {
			throw new EJBException(e);
		}
	}
}
