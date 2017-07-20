package com.freshdirect.fdstore.ecomm.converter;

import java.util.ArrayList;
import java.util.List;

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
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.referral.EnumReferralProgramStatus;
import com.freshdirect.fdstore.referral.EnumReferralStatus;
import com.freshdirect.fdstore.referral.FDReferralReportLine;
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
import com.freshdirect.framework.core.PrimaryKey;

public class ReferralConverter {

	public static ReferralProgramData buildReferralProgramData(ReferralProgram refProgram) {
		ReferralProgramData referralProgramData = new ReferralProgramData();
		referralProgramData.setName(refProgram.getName());
		referralProgramData.setDescription(refProgram.getDescription());
		if(refProgram.getStartDate() != null)
		referralProgramData.setStartDate(refProgram.getStartDate().getTime());
		if(refProgram.getExpDate()!= null)
		referralProgramData.setExpDate(refProgram.getExpDate().getTime());
		referralProgramData.setCreativeDesc(refProgram.getCreativeDesc());
		ReferralCampaignData campaign = buildReferralCampaignData(refProgram.getCampaign());
		referralProgramData.setCampaign(campaign);
		ReferralChannelData channel = buildReferralchannelData(refProgram.getChannel());
		referralProgramData.setChannel(channel);
		ReferralPartnerData partner = buildReferralPartnerData(refProgram.getPartner());
		referralProgramData.setPartner(partner);
		if(refProgram.getStatus() != null)
		referralProgramData.setStatus(refProgram.getStatus().getName());
		referralProgramData.setPromotionCode(refProgram.getPromotionCode());
		referralProgramData.setCreativeUrl(refProgram.getCreativeUrl());
		referralProgramData.setId(refProgram.getId());
		return referralProgramData;
	}

	public static ReferralCampaignData buildReferralCampaignData(ReferralCampaign campaign) {
		ReferralObjectiveData objective = buildReferralObjectiveData(campaign.getObjective());
		ReferralCampaignData campaignData = new ReferralCampaignData();
		campaignData.setId(campaign.getId());
		campaignData.setName(campaign.getName());
		campaignData.setDescription(campaign.getDescription());
		campaignData.setUnicaRefId(campaign.getUnicaRefId());
		campaignData.setObjective(objective);
		return campaignData;
	}

	public static ReferralObjectiveData buildReferralObjectiveData(ReferralObjective objective) {
		ReferralObjectiveData objectiveData = new  ReferralObjectiveData();
		objectiveData.setId(objective.getId());
		objectiveData.setDescription(objective.getDescription());
		objectiveData.setName(objective.getName());
		return objectiveData;
	}

	public static ReferralChannelData buildReferralchannelData(ReferralChannel channel) {
		ReferralChannelData channelData = new ReferralChannelData();
		channelData.setId(channel.getId());
		channelData.setName(channel.getName());
		channelData.setType(channel.getType());
		channelData.setDescription(channel.getDescription());
		return channelData;
	}

	public  static ReferralPartnerData buildReferralPartnerData(ReferralPartner partner) {
		ReferralPartnerData referralPartnerData = new ReferralPartnerData();
		referralPartnerData.setId(partner.getId());
		referralPartnerData.setName(partner.getName());
		referralPartnerData.setDescription(partner.getDescription());
		return referralPartnerData;
	}

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
	
	public static ReferralProgramInvitationData buildReferralInvitationData(ReferralProgramInvitaionModel referralInvitation) {
		ReferralProgramInvitationData data = new  ReferralProgramInvitationData();
		if(referralInvitation.getReferralCreatedDate() != null)
		data.setReferralCreatedDate(referralInvitation.getReferralCreatedDate().getTime());
		if(referralInvitation.getReferralModifiedDate() != null)
		data.setReferralModifiedDate(referralInvitation.getReferralModifiedDate().getTime());
		data.setReferralName(referralInvitation.getReferralName());
		data.setReferralProgramId(referralInvitation.getReferralProgramId());
		data.setReferrelEmailAddress(referralInvitation.getReferrelEmailAddress());
		data.setReferrerCustomerId(referralInvitation.getReferrerCustomerId());
		data.setReferrerFirstName(referralInvitation.getReferrerFirstName());
		data.setReferrerLastName(referralInvitation.getReferrerLastName());
		data.setId(referralInvitation.getId());
		if(referralInvitation.getStatus() != null)
		data.setStatus(referralInvitation.getStatus().getName());
		return data;
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

	public static List buildReferralProgramInvitationList(List<ReferralProgramInvitationData> loadReferralsFromReferralProgramId) {
		List invitationDataList = new ArrayList();
		for (ReferralProgramInvitationData data : loadReferralsFromReferralProgramId) {
			invitationDataList.add(buildReferralProgramInvitation(data));
		}
		return invitationDataList;
	}
	public  static List<FDReferralReportLine> buildReferralReport(List<FDReferralReportLineData> fdReferralReportLine) {
		 List<FDReferralReportLine> reportList = new  ArrayList<FDReferralReportLine>();
		for (FDReferralReportLineData fdReferralReportLineData : fdReferralReportLine) {
			FDReferralReportLine data = new FDReferralReportLine();
			data.setEmailAddress(fdReferralReportLineData.getEmailAddress());
			data.setEmailAddress2(fdReferralReportLineData.getEmailAddress2());
			data.setName(fdReferralReportLineData.getName());
			data.setNumDeliveredOrders(fdReferralReportLineData.getNumDeliveredOrders());
			data.setIsReferralAccepted(fdReferralReportLineData.isReferralAccepted());
			data.setReferralDate(new java.sql.Date(fdReferralReportLineData.getReferralDate()));
			data.setReferralId(fdReferralReportLineData.getReferralId());
			data.setReferralProgramCampaignCode(fdReferralReportLineData.getReferralProgramCampaignCode());
			data.setReferralProgramDesc(fdReferralReportLineData.getReferralProgramDesc());
			data.setReferralProgramExpirationDate(new java.sql.Date(fdReferralReportLineData.getReferralProgramExpirationDate()));
			data.setReferralProgramId(fdReferralReportLineData.getReferralProgramId());
			data.setReferralProgramStartDate(new java.sql.Date(fdReferralReportLineData.getReferralProgramStartDate()));
			data.setReferralProgramStatus(EnumReferralProgramStatus.getEnum(fdReferralReportLineData.getReferralProgramStatus()));
			data.setReferralStatus(EnumReferralStatus.getEnum(fdReferralReportLineData.getReferralStatus()));
			data.setReferrerCustomerId(fdReferralReportLineData.getReferrerCustomerId());
			reportList.add(data);	
		}
		
		return reportList;
	}

	public static List<ReferralChannel> buildReferralchannelList(List<ReferralChannelData> loadAllReferralChannels) {
		List<ReferralChannel> referralChannel = new ArrayList<ReferralChannel>();
		for (ReferralChannelData referralChannelData : loadAllReferralChannels) {
			referralChannel.add(buildReferralchannel(referralChannelData));
		}
		return referralChannel;
	}

	public static List<ReferralProgram> buildReferralProgramList(List<ReferralProgramData> loadAllReferralPrograms) {
		List<ReferralProgram> referralProgram = new ArrayList<ReferralProgram>();
		for (ReferralProgramData referralProgramData : loadAllReferralPrograms) {
			referralProgram.add(buildReferralProgram(referralProgramData));
		}
		return referralProgram;
	}

	public static List<ReferralCampaign> buildReferralCampaignList(List<ReferralCampaignData> loadAllReferralCampaigns) {
		List<ReferralCampaign> referralCampaign = new ArrayList<ReferralCampaign>();
		for (ReferralCampaignData referralCampaignData : loadAllReferralCampaigns) {
			referralCampaign.add(buildReferralCampaign(referralCampaignData));
		}
		return referralCampaign;
	}

	public static List<ReferralPartner> buildReferralPartnerList(List<ReferralPartnerData> loadAllReferralpartners) {
		List<ReferralPartner> referralPartner = new ArrayList<ReferralPartner>();
		for (ReferralPartnerData referralPartnerData : loadAllReferralpartners) {
			referralPartner.add(buildReferralPartner(referralPartnerData));
		}
		return referralPartner;
		
	}

	public static List<ReferralObjective> buildReferralObjectiveList(List<ReferralObjectiveData> loadAllReferralObjective) {
		List<ReferralObjective> referralObjective = new ArrayList<ReferralObjective>();
		for (ReferralObjectiveData referralObjectiveData : loadAllReferralObjective) {
			referralObjective.add(buildReferralObjective(referralObjectiveData));
			
		}
		return referralObjective;
	}

	public static ReferralSearchCriteriaData buildSearchCriteria(ReferralSearchCriteria criteria) {
		ReferralSearchCriteriaData searchCriteria = new ReferralSearchCriteriaData();
		searchCriteria.setEndIndex(criteria.getEndIndex());
		searchCriteria.setSortByColumnName(criteria.getSortByColumnName());
		searchCriteria.setStartIndex(criteria.getStartIndex());
		searchCriteria.setTotalRcdSize(criteria.getTotalRcdSize());
		return searchCriteria;
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

	public static List<ReferralPromotionModel> buildReferralpromotionModelList(List<ReferralPromotionData> data) {
		List<ReferralPromotionModel> model = new ArrayList<ReferralPromotionModel>();
		for (ReferralPromotionData referralPromotionData : data) {
			model.add(buildReferralPromotionModel(referralPromotionData));
		}
		return model;
	}

	public static ReferralPromotionModel buildReferralPromotionModel(ReferralPromotionData referralPromotionData) {
		if(referralPromotionData != null){
			ReferralPromotionModel model = new ReferralPromotionModel();
			model.setAdvocateEmail(referralPromotionData.getAdvocateEmail());
			model.setAudience_desc(referralPromotionData.getAudience_desc());
			model.setCustomerId(referralPromotionData.getCustomerId());
			model.setDescription(referralPromotionData.getDescription());
			model.setExpiration_date(referralPromotionData.getExpiration_date());
			model.setFbFile(referralPromotionData.getFbFile());
			model.setFbHeadline(referralPromotionData.getFbHeadline());
			model.setFbText(referralPromotionData.getFbText());
			model.setFDCustomerId(referralPromotionData.getFDCustomerId());
			model.setFriendEmail(referralPromotionData.getFriendEmail());
			model.setGet_text(referralPromotionData.getGet_text());
			model.setGetHeader(referralPromotionData.getGetHeader());
			model.setGive_text(referralPromotionData.getGive_text());
			model.setGiveHeader(referralPromotionData.getGiveHeader());
			model.setId(referralPromotionData.getId());
			model.setInviteEmailLegal(referralPromotionData.getInviteEmailLegal());
			model.setInviteEmailOfferText(referralPromotionData.getInviteEmailOfferText());
			model.setInviteEmailSubject(referralPromotionData.getInviteEmailSubject());
			model.setInviteEmailText(referralPromotionData.getInviteEmailText());
			model.setPrgm_users(referralPromotionData.getPrgm_users());
			model.setPromotion_id(referralPromotionData.getPromotion_id());
			model.setRefCustomerId(referralPromotionData.getRefCustomerId());
			model.setReferral_fee(referralPromotionData.getReferral_fee());
			model.setReferral_prgm_id(referralPromotionData.getReferral_prgm_id());
			model.setReferralCreditEmailSubject(referralPromotionData.getReferralCreditEmailSubject());
			model.setReferralCreditEmailText(referralPromotionData.getReferralCreditEmailText());
			model.setReferralPageLegal(referralPromotionData.getReferralPageLegal());
			model.setReferralPageText(referralPromotionData.getReferralPageText());
			model.setSaleId(referralPromotionData.getSaleId());
			model.setShareHeader(referralPromotionData.getShareHeader());
			model.setShareText(referralPromotionData.getShareText());
			model.setSiteAccessImageFile(referralPromotionData.getSiteAccessImageFile());
			model.setTwitterText(referralPromotionData.getTwitterText());
			model.setUserListFileHolder(referralPromotionData.getUserListFileHolder());
			return model;
		}
		return null;
		
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
		data.setFdUserData(fdUserData);
		ReferralProgramInvitationData programInvitationData = new ReferralProgramInvitationData();
		if(referral.getReferralCreatedDate() != null)
		programInvitationData.setReferralCreatedDate(referral.getReferralCreatedDate().getTime());
		if(referral.getReferralModifiedDate() != null)
		programInvitationData.setReferralModifiedDate(referral.getReferralModifiedDate().getTime());
		programInvitationData.setReferralName(referral.getReferralName());
		programInvitationData.setReferralProgramId(referral.getReferralProgramId());
		programInvitationData.setReferrelEmailAddress(referral.getReferrelEmailAddress());
		programInvitationData.setReferrerCustomerId(referral.getReferrerCustomerId());
		programInvitationData.setReferrerFirstName(referral.getReferrerFirstName());
		programInvitationData.setReferrerLastName(referral.getReferrerLastName());
		if(referral.getStatus() != null)
		programInvitationData.setStatus(referral.getStatus().getName());
		data.setProgramInvitationData(programInvitationData);
		return data;
	}

}
