package com.freshdirect.enums;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.enums.ejb.EnumManagerHome;
import com.freshdirect.enums.ejb.EnumManagerSB;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.core.ServiceLocator;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;

public class EnumManager {

	private final static Category LOGGER = LoggerFactory.getInstance(EnumManager.class);

	private final ServiceLocator serviceLocator;
	private static EnumManager INSTANCE;

	public synchronized static EnumManager getInstance() {
		if (INSTANCE == null) {
			try {
				setInstance(new EnumManager(new ServiceLocator(ErpServicesProperties.getInitialContext())));
			} catch (NamingException e) {
				LOGGER.error("Error occured in EnumManager.getInstance", e);
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

		if (FDStoreProperties
				.isSF2_0_AndServiceEnabled(FDEcommProperties.EnumManagerSB)) {

			try {
				LOGGER.info("calling sf2.0 gateway ");
				return FDECommerceService.getInstance().loadEnum(
						daoClass.getSimpleName());
			} catch (Exception e) {
				throw new FDRuntimeException(e);
			}
		} else {
			try {
				EnumManagerSB sb = this.getEnumManagerSB();
				return sb.loadEnum(daoClass.getName());
			} catch (RemoteException e) {
				throw new EJBException(e);
			}
		}

	}

	private EnumManagerSB getEnumManagerSB() {
		try {
			EnumManagerHome home =
				(EnumManagerHome) this.serviceLocator.getRemoteHome("freshdirect.enums.EnumManager");
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
