package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ErpCustomerCreditModel;
import com.freshdirect.ecommerce.data.referral.FDReferralReportLineData;
import com.freshdirect.ecommerce.data.referral.FDUserData;
import com.freshdirect.ecommerce.data.referral.ManageInvitesData;
import com.freshdirect.ecommerce.data.referral.ReferralCampaignData;
import com.freshdirect.ecommerce.data.referral.ReferralChannelData;
import com.freshdirect.ecommerce.data.referral.ReferralHistoryData;
import com.freshdirect.ecommerce.data.referral.ReferralIniviteData;
import com.freshdirect.ecommerce.data.referral.ReferralObjectiveData;
import com.freshdirect.ecommerce.data.referral.ReferralPartnerData;
import com.freshdirect.ecommerce.data.referral.ReferralProgramData;
import com.freshdirect.ecommerce.data.referral.ReferralProgramInvitationData;
import com.freshdirect.ecommerce.data.referral.ReferralPromotionData;
import com.freshdirect.ecommerce.data.referral.ReferralSearchCriteriaData;
import com.freshdirect.fdstore.FDResourceException;


public interface FDReferralManagerServiceI {

	public void updateReferralStatus(String referralId, String ststus)throws FDResourceException,  RemoteException;
	
	public void updateReferralProgram(ReferralProgramData refProgram) throws FDResourceException, RemoteException;
	
	public void updateReferralChannel(ReferralChannelData channel) throws FDResourceException, RemoteException;
	
	public void updateReferralCampaign(ReferralCampaignData campaign) throws FDResourceException, RemoteException;
	
	public void updateReferralPartner(ReferralPartnerData partner) throws FDResourceException, RemoteException;
	
	public void updateReferralObjective(ReferralObjectiveData objective) throws FDResourceException, RemoteException;
	
	public void removeReferralProgram(String refProgramId[]) throws FDResourceException, RemoteException;
	
	public void removeReferralChannel(String channelIds[]) throws FDResourceException, RemoteException;
	
	public void removeReferralCampaign(String campaignIds[]) throws FDResourceException, RemoteException;
	
	public void removeReferralPartner(String partnerIds[]) throws FDResourceException, RemoteException;
	
	public void removeReferralObjective(String objectiveIds[]) throws FDResourceException, RemoteException;
	
	public abstract ReferralChannelData createReferralChannel(ReferralChannelData channel) throws FDResourceException,  RemoteException;

	public abstract ReferralPartnerData createReferralPartner(ReferralPartnerData partner) throws FDResourceException,  RemoteException;

	public abstract ReferralObjectiveData createReferralObjective(ReferralObjectiveData objective) throws FDResourceException,  RemoteException;

	public abstract ReferralCampaignData createReferralCampaign(ReferralCampaignData campaign) throws FDResourceException, RemoteException;

	public abstract ReferralProgramData createReferralProgram(ReferralProgramData program) throws FDResourceException,  RemoteException;

	public abstract ReferralHistoryData createReferralHistory(ReferralHistoryData history) throws FDResourceException,  RemoteException;
	
	public abstract ReferralProgramInvitationData createReferralInvitee(ReferralIniviteData referralInviteData) throws FDResourceException,  RemoteException;

	public abstract void storeReferral(ReferralProgramInvitationData referral, FDUserData user) throws FDResourceException, RemoteException;

	public abstract ReferralProgramInvitationData loadReferralFromPK(String referralId) throws FDResourceException, RemoteException;
	
	public abstract List<ReferralProgramData> loadAllReferralPrograms() throws FDResourceException,	RemoteException;
	
	public abstract List<ReferralChannelData> loadAllReferralChannels() throws FDResourceException,	RemoteException;
	
	public abstract List<ReferralPartnerData> loadAllReferralpartners() throws FDResourceException,	RemoteException;
	
	public abstract List<ReferralObjectiveData> loadAllReferralObjective() throws FDResourceException,	RemoteException;
	
	public abstract List<ReferralCampaignData> loadAllReferralCampaigns() throws FDResourceException,	RemoteException;
	
	public abstract String loadReferrerNameFromReferralCustomerId( String referralCustomerId) throws FDResourceException, RemoteException;
	
	public abstract ReferralProgramData loadReferralProgramFromPK(String referralProgramId) throws FDResourceException, RemoteException;

	public abstract ReferralProgramData loadLastestActiveReferralProgram() throws FDResourceException, RemoteException;
	
	public abstract ReferralChannelData getReferralChannleModel(String refChaId) throws FDResourceException,	RemoteException;
	
	public abstract ReferralCampaignData getReferralCampaigneModel(String refChaId) throws FDResourceException,	RemoteException;

	public abstract ReferralObjectiveData getReferralObjectiveModel(String refChaId) throws FDResourceException,	RemoteException;
	
	public abstract ReferralPartnerData getReferralPartnerModel(String refChaId) throws FDResourceException,	RemoteException;
	
	public abstract ReferralProgramData getReferralProgramModel(String refChaId) throws FDResourceException,	RemoteException;
	
	public abstract List<ReferralProgramData> getReferralProgarmforRefChannel(String refChaIds[]) throws FDResourceException,	RemoteException;
	
	public abstract List<ReferralProgramData> getReferralProgarmforRefPartner(String refpartIds[]) throws FDResourceException,	RemoteException;
	
	public abstract List<ReferralProgramData> getReferralProgarmforRefCampaign(String refCampIds[]) throws FDResourceException,	RemoteException;
	
	public abstract List<ReferralProgramData> getReferralCampaignforRefObjective(String refObjIds[]) throws FDResourceException,	RemoteException;
	
	public abstract boolean isReferralPartnerNameExist(String refPartName) throws FDResourceException,	RemoteException;
	 
	public abstract boolean isReferralCampaignNameExist(String refCampName) throws FDResourceException,	RemoteException;
	
	public abstract boolean isReferralObjectiveNameExist(String refObjName) throws FDResourceException,	RemoteException;
	 
	public abstract boolean isReferralChannelNameAndTypeExist(String name,String type)  throws FDResourceException,	RemoteException;
	 
	public abstract boolean isReferralProgramNameExist(String refPrgName) throws FDResourceException, RemoteException;

	public abstract List<ReferralProgramData> getReferralPrograms(ReferralSearchCriteriaData criteria) throws FDResourceException, RemoteException;
	  
	public abstract List<ReferralChannelData> getReferralChannels(ReferralSearchCriteriaData criteria) throws FDResourceException, RemoteException;
	  
	public abstract List<ReferralCampaignData> getReferralCampaigns(ReferralSearchCriteriaData criteria)throws FDResourceException, RemoteException;
	  
	public abstract List<ReferralPartnerData> getReferralPartners(ReferralSearchCriteriaData criteria)throws FDResourceException, RemoteException;
	  
	public abstract List<ReferralObjectiveData> getReferralObjective(ReferralSearchCriteriaData criteria)throws FDResourceException, RemoteException;
	
	public abstract ReferralPromotionData getReferralPromotionDetails(String userId)throws FDResourceException, RemoteException;
	 
	public abstract ReferralPromotionData getReferralPromotionDetailsByRefName(String referral)throws FDResourceException, RemoteException;
 
	public abstract void sendMails(String recipient_list, String mail_message, String identity, String rpid, String serverName) throws FDResourceException, RemoteException;
	 
	 public abstract List<ManageInvitesData> getManageInvites(String customerId)throws FDResourceException, RemoteException;
	 
	 public abstract List<ErpCustomerCreditModel> getUserCredits(String customerId)throws FDResourceException, RemoteException;
	 
	 public abstract List<ManageInvitesData> getManageInvitesForCRM(String customerId)throws FDResourceException, RemoteException;
	 
	 public abstract Double getAvailableCredit(String customerId)throws FDResourceException, RemoteException;
	 
	 public abstract Boolean getReferralDisplayFlag(String customerId)throws FDResourceException, RemoteException;
	 
	 public abstract List<ReferralPromotionData> getSettledSales()throws FDResourceException, RemoteException;
	 
	 public abstract String getReferralLink(String customerId)throws FDResourceException, RemoteException;
	 
	 public abstract String getLatestSTLSale(String customerId)throws FDResourceException, RemoteException;
	 
	 public abstract boolean isCustomerReferred(String customerId)throws FDResourceException, RemoteException;
	 
	 public abstract String updateFDUser(String customerId, String zipCode, EnumServiceType serviceType)throws FDResourceException, RemoteException;
	 
	 public abstract void updateCustomerInfo(String customerId, String firstName, String lastName)throws FDResourceException, RemoteException;
	 
	 public abstract void updateCustomerPW(String customerId, String pwdHash)throws FDResourceException, RemoteException;
	 
	 public abstract void updateFdCustomer(String customerId, String pwdHint)throws FDResourceException, RemoteException;
	 
	 public abstract void storeFailedAttempt(String email, String dupeCustID, String zipCode, String firstName, String lastName, String referral, String reason) throws FDResourceException, RemoteException;
	 
	 public abstract boolean isUniqueFNLNZipCombo(String firstName, String lastName, String zipCode, String customerId) throws FDResourceException, RemoteException;
	 
	 public abstract String getReferralName(String referralId) throws FDResourceException, RemoteException;
	 
	 public abstract boolean isReferreSignUpComplete(String email) throws FDResourceException, RemoteException;
	 
	 public abstract List<ReferralPromotionData> getSettledTransaction() throws FDResourceException, RemoteException;
	 
	 public abstract void saveCustomerCredit(String referral_customer_id, String customer_id, int ref_fee, String sale, String complaintId, String refPrgmId) throws FDResourceException, RemoteException;
	 
	 public abstract Map<String,String> updateSetteledRewardTransaction(List<ReferralPromotionData> models) throws FDResourceException, RemoteException;
	 
	public abstract List<ReferralProgramInvitationData> loadReferralsFromReferralProgramId( String referralProgramId) throws FDResourceException, RemoteException;

	public abstract List<ReferralProgramInvitationData> loadReferralsFromReferrerCustomerId(String referrerCustomerId) throws FDResourceException, RemoteException;

	public abstract List<ReferralProgramInvitationData> loadReferralsFromReferralEmailAddress( String referralEmailAddress) throws FDResourceException, RemoteException;

	public abstract List<FDReferralReportLineData> loadReferralReportFromReferrerCustomerId(String referrerCustomerId) throws FDResourceException, RemoteException;
		
	public abstract List<FDReferralReportLineData> loadReferralReportFromReferralCustomerId( String referralCustomerId) throws FDResourceException,	RemoteException;
}
