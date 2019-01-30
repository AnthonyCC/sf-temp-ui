package com.freshdirect.fdstore.grp;

import java.rmi.RemoteException;
import java.util.Collection;

import org.apache.log4j.Category;

import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;

public class FDGrpInfoManager {

	private static Category LOGGER = LoggerFactory.getInstance(FDGrpInfoManager.class);

	// The SF2.0 switch will be introduced once AdServerGatewaySessionBean is moved
	// to service, B'cos of transaction isolation
	public static Collection<FDGroup> loadAllGrpInfoMaster() throws FDResourceException {

		Collection<FDGroup> zoneInfo = null;
		try {

			zoneInfo = FDECommerceService.getInstance().loadAllGrpInfoMaster();

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
		return zoneInfo;
	}

	public static int getLatestVersionNumber(String grpId) throws FDResourceException {
		try {

			return FDECommerceService.getInstance().getLatestVersionNumber(grpId);

		} catch (RemoteException re) {
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

}
