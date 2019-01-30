/*
 * Created on Jun 10, 2005
 *
 */
package com.freshdirect.fdstore.referral;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpCustomerCreditModel;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.ecomm.converter.ReferralConverter;
import com.freshdirect.fdstore.ecomm.gateway.FDReferralManagerService;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 * @author jng
 *
 */
public class FDReferralManager {
	private static Category LOGGER = LoggerFactory.getInstance(FDReferralManager.class);

	public static ReferralChannel getReferralChannleModel(String refChaId) throws FDResourceException {
		ReferralChannel channel = null;
		try {

			channel = ReferralConverter
					.buildReferralchannel(FDReferralManagerService.getInstance().getReferralChannleModel(refChaId));

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
		return channel;
	}

	public static ReferralCampaign getReferralCampaigneModel(String refChaId) throws FDResourceException {
		ReferralCampaign campaign = null;
		try {

			campaign = ReferralConverter
					.buildReferralCampaign(FDReferralManagerService.getInstance().getReferralCampaigneModel(refChaId));

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
		return campaign;
	}

	public static ReferralPartner getReferralPartnerModel(String refChaId) throws FDResourceException {
		ReferralPartner partner = null;
		try {

			partner = ReferralConverter
					.buildReferralPartner(FDReferralManagerService.getInstance().getReferralPartnerModel(refChaId));

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
		return partner;
	}

	public static List<ReferralProgram> loadAllReferralPrograms() throws FDResourceException {
		List<ReferralProgram> list = null;
		try {

			list = ReferralConverter
					.buildReferralProgramList(FDReferralManagerService.getInstance().loadAllReferralPrograms());

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
		return list;
	}

	public static ReferralHistory createReferralHistory(ReferralHistory history) throws FDResourceException {
		ReferralHistory historyNew = null;
		try {

			historyNew = ReferralConverter.buildReferralHistory(FDReferralManagerService.getInstance()
					.createReferralHistory(ReferralConverter.buildReferralHistoryData(history)));

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
		return historyNew;
	}

	public static ReferralProgramInvitaionModel createReferralInvitee(ReferralProgramInvitaionModel referral,
			FDUserI user) throws FDResourceException {

		try {
			LOGGER.debug("inside ReferralProgramInvitaionModel createReferralInvitee");

			return ReferralConverter.buildReferralProgramInvitation(FDReferralManagerService.getInstance()
					.createReferralInvitee(ReferralConverter.buildReferralInviteeData(referral, user)));

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static ReferralProgram loadLastestActiveReferralProgram() throws FDResourceException {
		try {

			return ReferralConverter
					.buildReferralProgram(FDReferralManagerService.getInstance().loadLastestActiveReferralProgram());

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static ReferralPromotionModel getReferralPromotionDetailsByRefName(String referral)
			throws FDResourceException {
		try {

			return FDReferralManagerService.getInstance().getReferralPromotionDetailsByRefName(referral);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static List<ManageInvitesModel> getManageInvites(String customerId) throws FDResourceException {
		try {

			return ReferralConverter
					.buildManageInvitesModelList(FDReferralManagerService.getInstance().getManageInvites(customerId));

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static List<ErpCustomerCreditModel> getUserCredits(String customerId) throws FDResourceException {
		try {

			return FDReferralManagerService.getInstance().getUserCredits(customerId);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}

	}

	public static Double getAvailableCredit(String customerId) throws FDResourceException {
		try {

			return FDReferralManagerService.getInstance().getAvailableCredit(customerId);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static Boolean getReferralDisplayFlag(String customerId) throws FDResourceException {
		try {

			return FDReferralManagerService.getInstance().getReferralDisplayFlag(customerId);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static String getReferralLink(String customerId) throws FDResourceException {
		try {

			return FDReferralManagerService.getInstance().getReferralLink(customerId);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static String getLatestSTLSale(String customerId) throws FDResourceException {

		try {

			return FDReferralManagerService.getInstance().getLatestSTLSale(customerId);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static boolean isCustomerReferred(String customerId) throws FDResourceException {
		try {

			return FDReferralManagerService.getInstance().isCustomerReferred(customerId);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static void storeFailedAttempt(String email, String dupeCustID, String zipCode, String firstName,
			String lastName, String referral, String reason) throws FDResourceException {
		try {

			FDReferralManagerService.getInstance().storeFailedAttempt(email, dupeCustID, zipCode, firstName, lastName,
					referral, reason);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static boolean isUniqueFNLNZipCombo(String firstName, String lastName, String zipCode, String customerId)
			throws FDResourceException {
		try {

			return FDReferralManagerService.getInstance().isUniqueFNLNZipCombo(firstName, lastName, zipCode,
					customerId);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static String getReferralName(String referralId) throws FDResourceException {
		try {

			return FDReferralManagerService.getInstance().getReferralName(referralId);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

	public static boolean isReferreSignUpComplete(String email, EnumEStoreId storeid) throws FDResourceException {
		try {

			return FDReferralManagerService.getInstance().isReferreSignUpComplete(email, storeid);

		} catch (RemoteException re) {

			throw new FDResourceException(re, "Error talking to session bean");
		}
	}

}
