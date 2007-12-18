package com.freshdirect.enum;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.enum.ejb.EnumManagerHome;
import com.freshdirect.enum.ejb.EnumManagerSB;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;

public class EnumManager {

	private final static Category LOGGER = LoggerFactory.getInstance(EnumManager.class);

	private final ServiceLocator serviceLocator;
	private static EnumManager INSTANCE;

	public synchronized static EnumManager getInstance() {
		if (INSTANCE == null) {
			try {
				setInstance(new EnumManager(new ServiceLocator(ErpServicesProperties.getInitialContext())));
			} catch (NamingException e) {
				LOGGER.error(e);
				throw new EJBException(e);
			}
		}
		return INSTANCE;
	}

	public synchronized static void setInstance(EnumManager instance) {
		INSTANCE = instance;
	}

	protected EnumManager(ServiceLocator locator) {
		this.serviceLocator = locator;
	}

	public List loadEnums(Class daoClass) {
		EnumManagerSB sb = this.getEnumManagerSB();
		try {
			return sb.loadEnum(daoClass.getName());
		} catch (RemoteException e) {
			throw new EJBException(e);
		}
	}

	private EnumManagerSB getEnumManagerSB() {
		try {
			EnumManagerHome home =
				(EnumManagerHome) this.serviceLocator.getRemoteHome("freshdirect.enum.EnumManager", EnumManagerHome.class);
			return home.create();
		} catch (NamingException e) {
			LOGGER.error(e);
			throw new EJBException(e);
		} catch (CreateException e) {
			LOGGER.error(e);
			throw new EJBException(e);
		} catch (RemoteException e) {
			LOGGER.error(e);
			throw new EJBException(e);
		}
	}

}
