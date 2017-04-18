package com.freshdirect.referral.extole;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;
import java.text.ParseException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.payment.service.IECommerceService;
import com.freshdirect.referral.extole.model.ExtoleConversionRequest;
import com.freshdirect.referral.extole.model.ExtoleResponse;
import com.freshdirect.referral.extole.model.FDRafCreditModel;

public class FDExtoleManager {

	private static Category LOGGER = LoggerFactory
			.getInstance(FDExtoleManager.class);

	private static FDExtoleManagerHome managerHome = null;

	public static List<ExtoleConversionRequest> getExtoleCreateConversionRequest()
			throws FDResourceException {
		lookupManagerHome();

		try {
			FDExtoleManagerSB sb = managerHome.create();
			if(FDStoreProperties.isStorefront2_0Enabled()){
        		IECommerceService service = FDECommerceService.getInstance();
        		return service.getExtoleCreateConversionRequest();
			}else
				return sb.getExtoleCreateConversionRequest();
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static List<ExtoleConversionRequest> getExtoleApproveConversionRequest()
			throws FDResourceException {
		lookupManagerHome();

		try {
			FDExtoleManagerSB sb = managerHome.create();
			if(FDStoreProperties.isStorefront2_0Enabled()){
        		IECommerceService service = FDECommerceService.getInstance();
        		return service.getExtoleApproveConversionRequest();
			}else
				return sb.getExtoleApproveConversionRequest();
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static void updateConversionRequest(ExtoleResponse convResponse)
			throws FDResourceException {
		lookupManagerHome();

		try {
			FDExtoleManagerSB sb = managerHome.create();
			if(FDStoreProperties.isStorefront2_0Enabled()){
        		IECommerceService service = FDECommerceService.getInstance();
        		service.updateConversionRequest(convResponse);
			}else
				sb.updateConversionRequest(convResponse);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static void saveExtoleRewardsFile(List<FDRafCreditModel> rewards)
			throws FDResourceException {
		lookupManagerHome();

		try {
			FDExtoleManagerSB sb = managerHome.create();
			if(FDStoreProperties.isStorefront2_0Enabled()){
        		IECommerceService service = FDECommerceService.getInstance();
        		service.saveExtoleRewardsFile(rewards);
			}else
				sb.saveExtoleRewardsFile(rewards);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	private static void lookupManagerHome() throws FDResourceException {
		if (managerHome != null) {
			return;
		}
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			managerHome = (FDExtoleManagerHome) ctx.lookup(FDStoreProperties
					.getFDExtoleManagerHome());
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
		managerHome = null;
	}

	public static void createConversion() throws ExtoleServiceException,
			IOException, FDResourceException {

		lookupManagerHome();

		try {
			FDExtoleManagerSB sb = managerHome.create();
			if(FDStoreProperties.isStorefront2_0Enabled()){
        		IECommerceService service = FDECommerceService.getInstance();
        		service.createConversion();
			}else
				sb.createConversion();
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static void approveConversion() throws ExtoleServiceException,
			IOException, FDResourceException {

		lookupManagerHome();

		try {
			FDExtoleManagerSB sb = managerHome.create();
			if(FDStoreProperties.isStorefront2_0Enabled()){
        		IECommerceService service = FDECommerceService.getInstance();
        		service.approveConversion();
			}else
				sb.approveConversion();
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static void downloadAndSaveRewards(String fileName) throws ExtoleServiceException,
			IOException, FDResourceException, ParseException {

		lookupManagerHome();

		try {
			FDExtoleManagerSB sb = managerHome.create();
			if(FDStoreProperties.isStorefront2_0Enabled()){
        		IECommerceService service = FDECommerceService.getInstance();
        		service.downloadAndSaveRewards(fileName);
			}else
				sb.downloadAndSaveRewards(fileName);
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}
}
