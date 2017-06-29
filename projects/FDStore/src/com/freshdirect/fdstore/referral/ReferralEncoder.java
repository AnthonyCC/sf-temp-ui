package com.freshdirect.fdstore.referral;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.ecommerce.data.referral.FDReferralReportLineData;
import com.freshdirect.ecommerce.data.referral.ReferralCampaignData;
import com.freshdirect.ecommerce.data.referral.ReferralChannelData;
import com.freshdirect.ecommerce.data.referral.ReferralHistoryData;
import com.freshdirect.ecommerce.data.referral.ReferralObjectiveData;
import com.freshdirect.ecommerce.data.referral.ReferralPartnerData;
import com.freshdirect.ecommerce.data.referral.ReferralProgramData;
import com.freshdirect.ecommerce.data.referral.ReferralProgramInvitationData;
import com.freshdirect.ecommerce.data.referral.ReferralSearchCriteriaData;
import com.freshdirect.framework.core.PrimaryKey;

public class ReferralEncoder {

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

}
