/*
 * Created on Jun 10, 2005
 *
 */
package com.freshdirect.fdstore.referral.ejb;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ErpCustomerCreditModel;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.referral.ManageInvitesModel;
import com.freshdirect.fdstore.referral.ReferralCampaign;
import com.freshdirect.fdstore.referral.ReferralChannel;
import com.freshdirect.fdstore.referral.ReferralHistory;
import com.freshdirect.fdstore.referral.ReferralObjective;
import com.freshdirect.fdstore.referral.ReferralPartner;
import com.freshdirect.fdstore.referral.ReferralProgram;
import com.freshdirect.fdstore.referral.ReferralProgramInvitaionModel;
import com.freshdirect.fdstore.referral.ReferralPromotionModel;
import com.freshdirect.fdstore.referral.ReferralSearchCriteria;

/**
 * @author jng
 * @deprecated
 */
public interface FDReferralManagerSB extends EJBObject {

	@Deprecated
	public void updateReferralStatus(String referralId, String ststus) throws FDResourceException, RemoteException;

	@Deprecated
	public void updateReferralProgram(ReferralProgram refProgram) throws FDResourceException, RemoteException;

	@Deprecated
	public void updateReferralChannel(ReferralChannel channel) throws FDResourceException, RemoteException;

	@Deprecated
	public void updateReferralCampaign(ReferralCampaign campaign) throws FDResourceException, RemoteException;

	@Deprecated
	public void updateReferralPartner(ReferralPartner partner) throws FDResourceException, RemoteException;

	@Deprecated
	public void updateReferralObjective(ReferralObjective objective) throws FDResourceException, RemoteException;

	@Deprecated
	public void removeReferralProgram(String refProgramId[]) throws FDResourceException, RemoteException;

	@Deprecated
	public void removeReferralChannel(String channelIds[]) throws FDResourceException, RemoteException;

	@Deprecated
	public void removeReferralCampaign(String campaignIds[]) throws FDResourceException, RemoteException;

	@Deprecated
	public void removeReferralPartner(String partnerIds[]) throws FDResourceException, RemoteException;

	@Deprecated
	public void removeReferralObjective(String objectiveIds[]) throws FDResourceException, RemoteException;
	@Deprecated
	public abstract ReferralChannel createReferralChannel(ReferralChannel channel)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract ReferralPartner createReferralPartner(ReferralPartner partner)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract ReferralObjective createReferralObjective(ReferralObjective objective)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract ReferralCampaign createReferralCampaign(ReferralCampaign campaign)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract ReferralProgram createReferralProgram(ReferralProgram program)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract ReferralHistory createReferralHistory(ReferralHistory history)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract ReferralProgramInvitaionModel createReferralInvitee(ReferralProgramInvitaionModel referral,
			FDUserI user) throws FDResourceException, RemoteException;
	@Deprecated
	public abstract void storeReferral(ReferralProgramInvitaionModel referral, FDUser user)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract ReferralProgramInvitaionModel loadReferralFromPK(String referralId)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List loadReferralsFromReferralProgramId(String referralProgramId)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List loadReferralsFromReferrerCustomerId(String referrerCustomerId)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List loadReferralsFromReferralEmailAddress(String referralEmailAddress)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List loadReferralReportFromReferrerCustomerId(String referrerCustomerId)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List loadReferralReportFromReferralCustomerId(String referralCustomerId)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List loadAllReferralPrograms() throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List loadAllReferralChannels() throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List loadAllReferralpartners() throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List loadAllReferralObjective() throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List loadAllReferralCampaigns() throws FDResourceException, RemoteException;
	@Deprecated
	public abstract String loadReferrerNameFromReferralCustomerId(String referralCustomerId)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract ReferralProgram loadReferralProgramFromPK(String referralProgramId)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract ReferralProgram loadLastestActiveReferralProgram() throws FDResourceException, RemoteException;
	@Deprecated
	public abstract ReferralChannel getReferralChannleModel(String refChaId)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract ReferralCampaign getReferralCampaigneModel(String refChaId)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract ReferralObjective getReferralObjectiveModel(String refChaId)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract ReferralPartner getReferralPartnerModel(String refChaId)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract ReferralProgram getReferralProgramModel(String refChaId)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List getReferralProgarmforRefChannel(String refChaIds[])
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List getReferralProgarmforRefPartner(String refpartIds[])
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List getReferralProgarmforRefCampaign(String refCampIds[])
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List getReferralCampaignforRefObjective(String refObjIds[])
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract boolean isReferralPartnerNameExist(String refPartName) throws FDResourceException, RemoteException;
	@Deprecated
	public abstract boolean isReferralCampaignNameExist(String refCampName) throws FDResourceException, RemoteException;
	@Deprecated
	public abstract boolean isReferralObjectiveNameExist(String refObjName) throws FDResourceException, RemoteException;
	@Deprecated
	public abstract boolean isReferralChannelNameAndTypeExist(String name, String type)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract boolean isReferralProgramNameExist(String refPrgName) throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List getReferralPrograms(ReferralSearchCriteria criteria)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List getReferralChannels(ReferralSearchCriteria criteria)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List getReferralCampaigns(ReferralSearchCriteria criteria)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List getReferralPartners(ReferralSearchCriteria criteria)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List getReferralObjective(ReferralSearchCriteria criteria)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract ReferralPromotionModel getReferralPromotionDetails(String userId)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract ReferralPromotionModel getReferralPromotionDetailsByRefName(String referral)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract void sendMails(String recipient_list, String mail_message, FDUser identity, String rpid,
			String serverName) throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List<ManageInvitesModel> getManageInvites(String customerId)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List<ErpCustomerCreditModel> getUserCredits(String customerId)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List<ManageInvitesModel> getManageInvitesForCRM(String customerId)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract Double getAvailableCredit(String customerId) throws FDResourceException, RemoteException;
	@Deprecated
	public abstract Boolean getReferralDisplayFlag(String customerId) throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List<ReferralPromotionModel> getSettledSales() throws FDResourceException, RemoteException;
	@Deprecated
	public abstract String getReferralLink(String customerId) throws FDResourceException, RemoteException;
	@Deprecated
	public abstract String getLatestSTLSale(String customerId) throws FDResourceException, RemoteException;
	@Deprecated
	public abstract void saveCustomerCredit(String referral_customer_id, String customer_id, int ref_fee, String sale,
			String complaintId, String refPrgmId) throws FDResourceException, RemoteException;
	@Deprecated
	public abstract boolean isCustomerReferred(String customerId) throws FDResourceException, RemoteException;
	@Deprecated
	public abstract String updateFDUser(String customerId, String zipCode, EnumServiceType serviceType)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract void updateCustomerInfo(String customerId, String firstName, String lastName)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract void updateCustomerPW(String customerId, String pwdHash)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract void updateFdCustomer(String customerId, String pwdHint)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract void storeFailedAttempt(String email, String dupeCustID, String zipCode, String firstName,
			String lastName, String referral, String reason) throws FDResourceException, RemoteException;
	@Deprecated
	public abstract boolean isUniqueFNLNZipCombo(String firstName, String lastName, String zipCode, String customerId)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract String getReferralName(String referralId) throws FDResourceException, RemoteException;
	@Deprecated
	public abstract boolean isReferreSignUpComplete(String email, EnumEStoreId storeid)
			throws FDResourceException, RemoteException;
	@Deprecated
	public abstract List<ReferralPromotionModel> getSettledTransaction() throws FDResourceException, RemoteException;
	@Deprecated
	public abstract Map<String, String> updateSetteledRewardTransaction(List<ReferralPromotionModel> models)
			throws FDResourceException, RemoteException;

}
