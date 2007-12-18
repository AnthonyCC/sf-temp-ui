package com.freshdirect.crm.command;

import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;

public abstract class CrmCommandSupport implements CrmCommandI {

	private final static Category LOGGER = LoggerFactory.getInstance(CrmCommandSupport.class);

	private transient CrmCommandContext context;

	public CrmCommandResultI execute() throws FDResourceException {
		try {
			
			return this.doExecute();

		} catch (FinderException e) {
			LOGGER.warn("FinderException occured", e);
			throw new EJBException(e);

		} catch (RemoteException e) {
			LOGGER.warn("RemoteException occured", e);
			throw new EJBException(e);

		} catch (FDResourceException e) {
			LOGGER.warn("FDResourceException occured", e);
			throw new EJBException(e);

		} catch (SQLException e) {
			LOGGER.warn("SQLException occured", e);
			throw new EJBException(e);
			
		} catch (CreateException e) {
			LOGGER.warn("CreateException occured", e);
			throw new EJBException(e);
		}
	}

	protected abstract CrmCommandResultI doExecute() throws FDResourceException, CreateException, FinderException, SQLException, RemoteException;

	protected CrmCommandContext getContext() {
		if (this.context==null) {
			throw new IllegalStateException("No context set for command. Use CrmManager.execute(cmd).");
		}
		return context;
	}

	public void setContext(CrmCommandContext context) {
		this.context = context;
	}

}
