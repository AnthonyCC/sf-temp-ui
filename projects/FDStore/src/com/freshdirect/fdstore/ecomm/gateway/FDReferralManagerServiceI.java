package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.freshdirect.customer.ErpCustomerCreditModel;
import com.freshdirect.ecommerce.data.referral.ManageInvitesData;
import com.freshdirect.ecommerce.data.referral.ReferralCampaignData;
import com.freshdirect.ecommerce.data.referral.ReferralChannelData;
import com.freshdirect.ecommerce.data.referral.ReferralHistoryData;
import com.freshdirect.ecommerce.data.referral.ReferralIniviteData;
import com.freshdirect.ecommerce.data.referral.ReferralPartnerData;
import com.freshdirect.ecommerce.data.referral.ReferralProgramData;
import com.freshdirect.ecommerce.data.referral.ReferralProgramInvitationData;
import com.freshdirect.ecommerce.data.referral.ReferralPromotionData;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.referral.ReferralPromotionModel;

public interface FDReferralManagerServiceI {

	public abstract ReferralHistoryData createReferralHistory(ReferralHistoryData history)
			throws FDResourceException, RemoteException;

	public abstract ReferralProgramInvitationData createReferralInvitee(ReferralIniviteData referralInviteData)
			throws FDResourceException, RemoteException;

	public abstract List<ReferralProgramData> loadAllReferralPrograms() throws FDResourceException, RemoteException;

	public abstract ReferralProgramData loadLastestActiveReferralProgram() throws FDResourceException, RemoteException;

	public abstract ReferralChannelData getReferralChannleModel(String refChaId)
			throws FDResourceException, RemoteException;

	public abstract ReferralCampaignData getReferralCampaigneModel(String refChaId)
			throws FDResourceException, RemoteException;

	public abstract ReferralPartnerData getReferralPartnerModel(String refChaId)
			throws FDResourceException, RemoteException;

	public abstract ReferralPromotionModel getReferralPromotionDetailsByRefName(String referral)
			throws FDResourceException, RemoteException;

	public abstract List<ManageInvitesData> getManageInvites(String customerId)
			throws FDResourceException, RemoteException;

	public abstract List<ErpCustomerCreditModel> getUserCredits(String customerId)
			throws FDResourceException, RemoteException;

	public abstract Double getAvailableCredit(String customerId) throws FDResourceException, RemoteException;

	public abstract Boolean getReferralDisplayFlag(String customerId) throws FDResourceException, RemoteException;

	public abstract List<ReferralPromotionModel> getSettledSales() throws FDResourceException, RemoteException;

	public abstract String getReferralLink(String customerId) throws FDResourceException, RemoteException;

	public abstract String getLatestSTLSale(String customerId) throws FDResourceException, RemoteException;

	public abstract boolean isCustomerReferred(String customerId) throws FDResourceException, RemoteException;

	public abstract void storeFailedAttempt(String email, String dupeCustID, String zipCode, String firstName,
			String lastName, String referral, String reason) throws FDResourceException, RemoteException;

	public abstract boolean isUniqueFNLNZipCombo(String firstName, String lastName, String zipCode, String customerId)
			throws FDResourceException, RemoteException;

	public abstract String getReferralName(String referralId) throws FDResourceException, RemoteException;

	public abstract boolean isReferreSignUpComplete(String email, EnumEStoreId storeid)
			throws FDResourceException, RemoteException;

	public abstract List<ReferralPromotionModel> getSettledTransaction() throws FDResourceException, RemoteException;

	public abstract void saveCustomerCredit(String referral_customer_id, String customer_id, int ref_fee, String sale,
			String complaintId, String refPrgmId) throws FDResourceException, RemoteException;

	public abstract Map<String, String> updateSetteledRewardTransaction(List<ReferralPromotionModel> models)
			throws FDResourceException, RemoteException;

}
