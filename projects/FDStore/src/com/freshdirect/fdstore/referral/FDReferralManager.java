/*
 * Created on Jun 10, 2005
 *
 */
package com.freshdirect.fdstore.referral;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpCustomerCreditModel;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDEcommProperties;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.ecomm.converter.ReferralConverter;
import com.freshdirect.fdstore.ecomm.gateway.FDReferralManagerService;
import com.freshdirect.fdstore.referral.ejb.FDReferralManagerHome;
import com.freshdirect.fdstore.referral.ejb.FDReferralManagerSB;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author jng
 *
 */
public class FDReferralManager {
	private static Category LOGGER = LoggerFactory.getInstance(FDReferralManager.class);

	private static FDReferralManagerHome managerHome = null;

	public static ReferralChannel getReferralChannleModel(String refChaId) throws FDResourceException {
		ReferralChannel channel = null;
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)) {
				channel = ReferralConverter
						.buildReferralchannel(FDReferralManagerService.getInstance().getReferralChannleModel(refChaId));
			} else {
				lookupManagerHome();
				FDReferralManagerSB sb = managerHome.create();
				channel = sb.getReferralChannleModel(refChaId);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
		return channel;
	}

	public static ReferralCampaign getReferralCampaigneModel(String refChaId) throws FDResourceException {
		ReferralCampaign campaign = null;
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)) {
				campaign = ReferralConverter.buildReferralCampaign(
						FDReferralManagerService.getInstance().getReferralCampaigneModel(refChaId));
			} else {
				lookupManagerHome();
				FDReferralManagerSB sb = managerHome.create();
				campaign = sb.getReferralCampaigneModel(refChaId);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
		return campaign;
	}
	
	public static ReferralPartner getReferralPartnerModel(String refChaId) throws FDResourceException {
		ReferralPartner partner = null;
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)) {
				partner = ReferralConverter
						.buildReferralPartner(FDReferralManagerService.getInstance().getReferralPartnerModel(refChaId));
			} else {
				lookupManagerHome();
				FDReferralManagerSB sb = managerHome.create();
				partner = sb.getReferralPartnerModel(refChaId);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
		return partner;
	}
	
	public static List loadAllReferralPrograms() throws FDResourceException {
		List list = null;
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)) {
				list = ReferralConverter
						.buildReferralProgramList(FDReferralManagerService.getInstance().loadAllReferralPrograms());
			} else {
				lookupManagerHome();
				FDReferralManagerSB sb = managerHome.create();
				list = sb.loadAllReferralPrograms();
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
		return list;
	}

	public static ReferralHistory createReferralHistory(ReferralHistory history) throws FDResourceException {
		ReferralHistory historyNew = null;
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)) {
				historyNew = ReferralConverter.buildReferralHistory(FDReferralManagerService.getInstance()
						.createReferralHistory(ReferralConverter.buildReferralHistoryData(history)));
			} else {
				lookupManagerHome();
				FDReferralManagerSB sb = managerHome.create();
				historyNew = sb.createReferralHistory(history);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
		return historyNew;
	}

	public static ReferralProgramInvitaionModel createReferralInvitee(ReferralProgramInvitaionModel referral,
			FDUserI user) throws FDResourceException {
		
		ReferralProgramInvitaionModel referralNew = null;
		try {
			LOGGER.debug("inside ReferralProgramInvitaionModel createReferralInvitee");
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)) {
				return ReferralConverter.buildReferralProgramInvitation(FDReferralManagerService.getInstance()
						.createReferralInvitee(ReferralConverter.buildReferralInviteeData(referral, user)));
			} else {
				lookupManagerHome();
				FDReferralManagerSB sb = managerHome.create();
				referralNew = sb.createReferralInvitee(referral, user);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
		return referralNew;
	}
	
	public static ReferralProgram loadLastestActiveReferralProgram() throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)) {
				return ReferralConverter.buildReferralProgram(
						FDReferralManagerService.getInstance().loadLastestActiveReferralProgram());
			} else {
				lookupManagerHome();
				FDReferralManagerSB sb = managerHome.create();
				return sb.loadLastestActiveReferralProgram();
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	private static void invalidateManagerHome() {
		managerHome = null;
	}

	private static void lookupManagerHome() throws FDResourceException {
		if (managerHome != null) {
			return;
		}
		Context ctx = null;
		try {
			ctx = FDStoreProperties.getInitialContext();
			managerHome = (FDReferralManagerHome) ctx.lookup(FDStoreProperties.getFDReferralManagerHome());
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

	public static ReferralPromotionModel getReferralPromotionDetailsByRefName(String referral)
			throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)) {
				return FDReferralManagerService.getInstance().getReferralPromotionDetailsByRefName(referral);
			} else {
				lookupManagerHome();
				FDReferralManagerSB sb = managerHome.create();
				return sb.getReferralPromotionDetailsByRefName(referral);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static List<ManageInvitesModel> getManageInvites(String customerId) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)) {
				return ReferralConverter.buildManageInvitesModelList(
						FDReferralManagerService.getInstance().getManageInvites(customerId));
			} else {
				lookupManagerHome();
				FDReferralManagerSB sb = managerHome.create();
				return sb.getManageInvites(customerId);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static List<ErpCustomerCreditModel> getUserCredits(String customerId) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)) {
				return FDReferralManagerService.getInstance().getUserCredits(customerId);
			} else {
				lookupManagerHome();
				FDReferralManagerSB sb = managerHome.create();
				return sb.getUserCredits(customerId);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static Double getAvailableCredit(String customerId) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)) {
				return FDReferralManagerService.getInstance().getAvailableCredit(customerId);
			} else {
				lookupManagerHome();
				FDReferralManagerSB sb = managerHome.create();
				return sb.getAvailableCredit(customerId);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Boolean getReferralDisplayFlag(String customerId) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)) {
				return FDReferralManagerService.getInstance().getReferralDisplayFlag(customerId);
			} else {
				lookupManagerHome();
				FDReferralManagerSB sb = managerHome.create();
				return sb.getReferralDisplayFlag(customerId);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static String getReferralLink(String customerId) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)) {
				return FDReferralManagerService.getInstance().getReferralLink(customerId);
			} else {
				lookupManagerHome();
				FDReferralManagerSB sb = managerHome.create();
				return sb.getReferralLink(customerId);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static String getLatestSTLSale(String customerId) throws FDResourceException {
		lookupManagerHome();

		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)) {
				return FDReferralManagerService.getInstance().getLatestSTLSale(customerId);
			} else {
				FDReferralManagerSB sb = managerHome.create();
				return sb.getLatestSTLSale(customerId);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static boolean isCustomerReferred(String customerId) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)) {
				return FDReferralManagerService.getInstance().isCustomerReferred(customerId);
			} else {
				lookupManagerHome();
				FDReferralManagerSB sb = managerHome.create();
				return sb.isCustomerReferred(customerId);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	
	public static void storeFailedAttempt(String email, String dupeCustID, String zipCode, String firstName,
			String lastName, String referral, String reason) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)) {
				FDReferralManagerService.getInstance().storeFailedAttempt(email, dupeCustID, zipCode, firstName,
						lastName, referral, reason);
			} else {
				lookupManagerHome();
				FDReferralManagerSB sb = managerHome.create();
				sb.storeFailedAttempt(email, dupeCustID, zipCode, firstName, lastName, referral, reason);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static boolean isUniqueFNLNZipCombo(String firstName, String lastName, String zipCode, String customerId)
			throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)) {
				return FDReferralManagerService.getInstance().isUniqueFNLNZipCombo(firstName, lastName, zipCode,
						customerId);
			} else {
				lookupManagerHome();
				FDReferralManagerSB sb = managerHome.create();
				return sb.isUniqueFNLNZipCombo(firstName, lastName, zipCode, customerId);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static String getReferralName(String referralId) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)) {
				return FDReferralManagerService.getInstance().getReferralName(referralId);
			} else {
				lookupManagerHome();
				FDReferralManagerSB sb = managerHome.create();
				return sb.getReferralName(referralId);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static boolean isReferreSignUpComplete(String email, EnumEStoreId storeid) throws FDResourceException {
		try {
			if (FDStoreProperties.isSF2_0_AndServiceEnabled(FDEcommProperties.FDReferralManagerSB)) {
				return FDReferralManagerService.getInstance().isReferreSignUpComplete(email, storeid);
			} else {
				lookupManagerHome();
				FDReferralManagerSB sb = managerHome.create();
				return sb.isReferreSignUpComplete(email, storeid);
			}
		} catch (CreateException ce) {
			invalidateManagerHome();
			throw new FDResourceException(ce, "Error creating session bean");
		} catch (RemoteException re) {
			invalidateManagerHome();
			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

}
