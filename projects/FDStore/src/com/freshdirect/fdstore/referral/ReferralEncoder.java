package com.freshdirect.fdstore.referral;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.ecommerce.data.referral.FDReferralReportLineData;
import com.freshdirect.ecommerce.data.referral.ReferralCampaignData;
import com.freshdirect.ecommerce.data.referral.ReferralChannelData;
import com.freshdirect.ecommerce.data.referral.ReferralHistoryData;
import com.freshdirect.ecommerce.data.referral.ReferralObjectiveData;
import com.freshdirect.ecommerce.data.referral.ReferralPartnerData;
import com.freshdirect.ecommerce.data.referral.ReferralProgramData;
import com.freshdirect.ecommerce.data.referral.ReferralProgramInvitationData;
import com.freshdirect.framework.core.PrimaryKey;

public class ReferralEncoder {

	public static ReferralProgramData buildReferralProgramData(ReferralProgram refProgram) {
		ReferralProgramData referralProgramData = new ReferralProgramData();
		referralProgramData.setName(refProgram.getName());
		referralProgramData.setDescription(refProgram.getDescription());
		referralProgramData.setStartDate(refProgram.getStartDate());
		referralProgramData.setExpDate(refProgram.getExpDate());
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
		ReferralObjective objective = new  ReferralObjective(new PrimaryKey(referralObjectiveData.getId()), referralObjectiveData.getName());
		objective.setDescription(referralObjectiveData.getDescription());
		return objective;
	}
	public static ReferralProgram buildReferralProgram(ReferralProgramData data) {
		ReferralProgram referralProgram = new ReferralProgram();
		referralProgram.setName(data.getName());
		referralProgram.setDescription(data.getDescription());
		referralProgram.setStartDate(data.getStartDate());
		referralProgram.setExpDate(data.getExpDate());
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
		data.setReferralCreatedDate(referralInvitation.getReferralCreatedDate().getTime());
		data.setReferralModifiedDate(referralInvitation.getReferralModifiedDate().getTime());
		data.setReferralName(referralInvitation.getReferralName());
		data.setReferralProgramId(referralInvitation.getReferralProgramId());
		data.setReferrelEmailAddress(referralInvitation.getReferrelEmailAddress());
		data.setReferrerCustomerId(referralInvitation.getReferrerCustomerId());
		data.setReferrerFirstName(referralInvitation.getReferrerFirstName());
		data.setReferrerLastName(referralInvitation.getReferrerLastName());
		if(referralInvitation.getStatus() != null)
		data.setStatus(referralInvitation.getStatus().getName());
		return data;
	}
	
	public static ReferralProgramInvitaionModel buildReferralProgramInvitation(ReferralProgramInvitationData data) {
		ReferralProgramInvitaionModel referralProgramInvitaionModel = new ReferralProgramInvitaionModel();
		referralProgramInvitaionModel.setReferralCreatedDate(new Date(data.getReferralCreatedDate()));
		referralProgramInvitaionModel.setReferralModifiedDate(new Date(data.getReferralModifiedDate()));
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
			data.setReferralDate(new Date(fdReferralReportLineData.getReferralDate()));
			data.setReferralId(fdReferralReportLineData.getReferralId());
			data.setReferralProgramCampaignCode(fdReferralReportLineData.getReferralProgramCampaignCode());
			data.setReferralProgramDesc(fdReferralReportLineData.getReferralProgramDesc());
			data.setReferralProgramExpirationDate(new Date(fdReferralReportLineData.getReferralProgramExpirationDate()));
			data.setReferralProgramId(fdReferralReportLineData.getReferralProgramId());
			data.setReferralProgramStartDate(new Date(fdReferralReportLineData.getReferralProgramStartDate()));
			data.setReferralProgramStatus(EnumReferralProgramStatus.getEnum(fdReferralReportLineData.getReferralProgramStatus()));
			data.setReferralStatus(EnumReferralStatus.getEnum(fdReferralReportLineData.getReferralStatus()));
			data.setReferrerCustomerId(fdReferralReportLineData.getReferrerCustomerId());
			reportList.add(data);	
		}
		
		return reportList;
	}

}
