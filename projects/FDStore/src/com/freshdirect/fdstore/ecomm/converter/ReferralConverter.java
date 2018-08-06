package com.freshdirect.fdstore.ecomm.converter;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.ecommerce.data.customer.FDUserData;
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
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.referral.EnumReferralProgramStatus;
import com.freshdirect.fdstore.referral.EnumReferralStatus;
import com.freshdirect.fdstore.referral.ManageInvitesModel;
import com.freshdirect.fdstore.referral.ReferralCampaign;
import com.freshdirect.fdstore.referral.ReferralChannel;
import com.freshdirect.fdstore.referral.ReferralHistory;
import com.freshdirect.fdstore.referral.ReferralObjective;
import com.freshdirect.fdstore.referral.ReferralPartner;
import com.freshdirect.fdstore.referral.ReferralProgram;
import com.freshdirect.fdstore.referral.ReferralProgramInvitaionModel;
import com.freshdirect.fdstore.referral.ReferralPromotionModel;
import com.freshdirect.framework.core.PrimaryKey;

public class ReferralConverter {

	public  static ReferralChannel buildReferralchannel(ReferralChannelData referralChannelData) {
		ReferralChannel channel = new ReferralChannel(new PrimaryKey(referralChannelData.getId()), referralChannelData.getName());
		channel.setType(referralChannelData.getType());
		channel.setDescription(referralChannelData.getDescription());
		return channel;
	}

	public static ReferralCampaign buildReferralCampaign(ReferralCampaignData referralCampaignData) {
		ReferralObjective objective = buildReferralObjective(referralCampaignData.getObjective());
		ReferralCampaign campaign = new ReferralCampaign(new PrimaryKey(referralCampaignData.getId()), referralCampaignData.getName(), objective);
		campaign.setDescription(referralCampaignData.getDescription());
		campaign.setUnicaRefId(referralCampaignData.getUnicaRefId());
		return campaign;
	}

	public static ReferralObjective buildReferralObjective(ReferralObjectiveData referralObjectiveData) {
		ReferralObjective objective = new  ReferralObjective();
		if(referralObjectiveData.getId() != null)
		objective.setId(referralObjectiveData.getId());
		objective.setName(referralObjectiveData.getName());
		objective.setDescription(referralObjectiveData.getDescription());
		return objective;
	}
	public static ReferralProgram buildReferralProgram(ReferralProgramData data) {
		ReferralProgram referralProgram = new ReferralProgram();
		if(data != null){
			referralProgram.setName(data.getName());
			referralProgram.setDescription(data.getDescription());
			if(data.getStartDate() != null)
			referralProgram.setStartDate(new java.sql.Date(data.getStartDate()));
			if(data.getExpDate() != null)
			referralProgram.setExpDate(new java.sql.Date(data.getExpDate()));
			referralProgram.setCreativeDesc(data.getCreativeDesc());
			ReferralCampaign campaign = buildReferralCampaign(data.getCampaign());
			referralProgram.setCampaign(campaign);
			ReferralChannel channel = buildReferralchannel(data.getChannel());
			referralProgram.setChannel(channel);
			ReferralPartner partner = buildReferralPartner(data.getPartner());
			referralProgram.setPartner(partner);
			referralProgram.setStatus(EnumReferralProgramStatus.getEnum(data.getStatus()));
			referralProgram.setPromotionCode(data.getPromotionCode());
			referralProgram.setCreativeUrl(data.getCreativeUrl());
			referralProgram.setPK(new PrimaryKey(data.getId()));
		}
		return referralProgram;
	}

	public static ReferralPartner buildReferralPartner(ReferralPartnerData referralPartnerData) {
		ReferralPartner partner = new ReferralPartner(new PrimaryKey(referralPartnerData.getId()),referralPartnerData.getName());
		partner.setDescription(referralPartnerData.getDescription());
		return partner;
	}
	
	public static ReferralHistoryData buildReferralHistoryData(ReferralHistory data) {
		ReferralHistoryData historyData = new ReferralHistoryData();
		historyData.setDateCreated(data.getDateCreated());
		historyData.setFdUserId(data.getFdUserId());
		historyData.setReferralProgramId(data.getReferralProgramId());
		historyData.setRefprgInvtId(data.getRefprgInvtId());
		historyData.setRefTrkKeyDtls(data.getRefTrkKeyDtls());
		return historyData;
	}
	
	public static ReferralHistory buildReferralHistory(ReferralHistoryData data) {
		ReferralHistory historyData = new ReferralHistory();
		historyData.setDateCreated(data.getDateCreated());
		historyData.setFdUserId(data.getFdUserId());
		historyData.setReferralProgramId(data.getReferralProgramId());
		historyData.setRefprgInvtId(data.getRefprgInvtId());
		historyData.setRefTrkKeyDtls(data.getRefTrkKeyDtls());
		return historyData;
	}
	
	public static ReferralProgramInvitaionModel buildReferralProgramInvitation(ReferralProgramInvitationData data) {
		ReferralProgramInvitaionModel referralProgramInvitaionModel = new ReferralProgramInvitaionModel();
		if(data.getReferralCreatedDate() != null)
		referralProgramInvitaionModel.setReferralCreatedDate(new java.sql.Date(data.getReferralCreatedDate()));
		if(data.getReferralModifiedDate() != null)
		referralProgramInvitaionModel.setReferralModifiedDate(new java.sql.Date(data.getReferralModifiedDate()));
		referralProgramInvitaionModel.setReferralName(data.getReferralName());
		referralProgramInvitaionModel.setReferralProgramId(data.getReferralProgramId());
		referralProgramInvitaionModel.setReferrelEmailAddress(data.getReferrelEmailAddress());
		referralProgramInvitaionModel.setReferrerCustomerId(data.getReferrerCustomerId());
		referralProgramInvitaionModel.setReferrerFirstName(data.getReferrerFirstName());
		referralProgramInvitaionModel.setReferrerLastName(data.getReferrerLastName());
		if(data.getStatus() != null)
		referralProgramInvitaionModel.setStatus(EnumReferralStatus.getEnum(data.getStatus()));
		return referralProgramInvitaionModel;
		
	}

	public static List<ReferralProgram> buildReferralProgramList(List<ReferralProgramData> loadAllReferralPrograms) {
		List<ReferralProgram> referralProgram = new ArrayList<ReferralProgram>();
		for (ReferralProgramData referralProgramData : loadAllReferralPrograms) {
			referralProgram.add(buildReferralProgram(referralProgramData));
		}
		return referralProgram;
	}

	public static List<ManageInvitesModel> buildManageInvitesModelList(List<ManageInvitesData> invites) {
		List<ManageInvitesModel> manageInvitesList = new ArrayList<ManageInvitesModel>();
		for (ManageInvitesData data : invites) {
			manageInvitesList.add(buildManageInviteModel(data));
		}
		return manageInvitesList;
	}

	public static ManageInvitesModel buildManageInviteModel(ManageInvitesData manageInvites) {
		ManageInvitesModel model = new  ManageInvitesModel();
		model.setCredit(manageInvites.getCredit());
		if(manageInvites.getId() != null)
		model.setId(manageInvites.getId());
		if(manageInvites.getCreditIssuedDate() != null)
		model.setCreditIssuedDate(new java.sql.Date(manageInvites.getCreditIssuedDate()));
		model.setRecipientCustId(manageInvites.getRecipientCustId());
		model.setRecipientEmail(manageInvites.getRecipientEmail());
		model.setSaleId(manageInvites.getSaleId());
		model.setSentDate(manageInvites.getSentDate());
		model.setStatus(manageInvites.getStatus());
		return model;
	}
	public static List<ReferralPromotionData> buildReferralPromotionDataList(List<ReferralPromotionModel> settledSales) {
		List<ReferralPromotionData> promotionData = new  ArrayList<ReferralPromotionData>();
		for (ReferralPromotionModel referralPromotionModel : settledSales) {
			promotionData.add(buildReferralPromotionData(referralPromotionModel));
		}
		return promotionData;
	}
	public static ReferralPromotionData buildReferralPromotionData(ReferralPromotionModel referralPromotionModel) {
		ReferralPromotionData promotionData = new ReferralPromotionData();
		promotionData.setAdvocateEmail(referralPromotionModel.getAdvocateEmail());
		promotionData.setAudience_desc(referralPromotionModel.getAudience_desc());
		promotionData.setCustomerId(referralPromotionModel.getCustomerId());
		promotionData.setDescription(referralPromotionModel.getDescription());
		promotionData.setExpiration_date(referralPromotionModel.getExpiration_date());
		promotionData.setFbFile(referralPromotionModel.getFbFile());
		promotionData.setFbHeadline(referralPromotionModel.getFbHeadline());
		promotionData.setFbText(referralPromotionModel.getFbText());
		promotionData.setFDCustomerId(referralPromotionModel.getFDCustomerId());
		promotionData.setFriendEmail(referralPromotionModel.getFriendEmail());
		promotionData.setGet_text(referralPromotionModel.getGet_text());
		promotionData.setGetHeader(referralPromotionModel.getGetHeader());
		promotionData.setGive_text(referralPromotionModel.getGive_text());
		promotionData.setGiveHeader(referralPromotionModel.getGiveHeader());
		promotionData.setId(referralPromotionModel.getId());
		promotionData.setInviteEmailLegal(referralPromotionModel.getInviteEmailLegal());
		promotionData.setInviteEmailOfferText(referralPromotionModel.getInviteEmailOfferText());
		promotionData.setInviteEmailSubject(referralPromotionModel.getInviteEmailSubject());
		promotionData.setInviteEmailText(referralPromotionModel.getInviteEmailText());
		promotionData.setPrgm_users(referralPromotionModel.getPrgm_users());
		promotionData.setPromotion_id(referralPromotionModel.getPromotion_id());
		promotionData.setRefCustomerId(referralPromotionModel.getRefCustomerId());
		promotionData.setReferral_fee(referralPromotionModel.getReferral_fee());
		promotionData.setReferral_prgm_id(referralPromotionModel.getReferral_prgm_id());
		promotionData.setReferralCreditEmailSubject(referralPromotionModel.getReferralCreditEmailSubject());
		promotionData.setReferralCreditEmailText(referralPromotionModel.getReferralCreditEmailText());
		promotionData.setReferralPageLegal(referralPromotionModel.getReferralPageLegal());
		promotionData.setReferralPageText(referralPromotionModel.getReferralPageText());
		promotionData.setSaleId(referralPromotionModel.getSaleId());
		promotionData.setShareHeader(referralPromotionModel.getShareHeader());
		promotionData.setShareText(referralPromotionModel.getShareText());
		promotionData.setSiteAccessImageFile(referralPromotionModel.getSiteAccessImageFile());
		promotionData.setTwitterText(referralPromotionModel.getTwitterText());
		promotionData.setUserListFileHolder(referralPromotionModel.getUserListFileHolder());
		return promotionData;
	}

	public static ReferralIniviteData buildReferralInviteeData(ReferralProgramInvitaionModel referral, FDUserI user) {
		ReferralIniviteData data = new ReferralIniviteData();
		FDUserData fdUserData = new FDUserData();
		fdUserData.setErpCustomerId(user.getIdentity().getErpCustomerPK());
		try {
			fdUserData.setReferrerEligible(user.isReferrerEligible());
		} catch(FDResourceException exception) {
			
		}
		data.setFdUserData(fdUserData);
		ReferralProgramInvitationData programInvitationData = new ReferralProgramInvitationData();
		if (referral.getReferralCreatedDate() != null)
			programInvitationData.setReferralCreatedDate(referral.getReferralCreatedDate().getTime());
		if (referral.getReferralModifiedDate() != null)
			programInvitationData.setReferralModifiedDate(referral.getReferralModifiedDate().getTime());
		programInvitationData.setReferralName(referral.getReferralName());
		programInvitationData.setReferralProgramId(referral.getReferralProgramId());
		programInvitationData.setReferrelEmailAddress(referral.getReferrelEmailAddress());
		programInvitationData.setReferrerCustomerId(referral.getReferrerCustomerId());
		programInvitationData.setReferrerFirstName(referral.getReferrerFirstName());
		programInvitationData.setReferrerLastName(referral.getReferrerLastName());
		if (referral.getStatus() != null)
			programInvitationData.setStatus(referral.getStatus().getName());
		data.setProgramInvitationData(programInvitationData);
		return data;
	}

}
