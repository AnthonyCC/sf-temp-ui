package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ErpCustomerCreditModel;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.referral.CustomerCreditData;
import com.freshdirect.ecommerce.data.referral.FDReferralReportLineData;
import com.freshdirect.ecommerce.data.referral.FDUserData;
import com.freshdirect.ecommerce.data.referral.FNLNZipData;
import com.freshdirect.ecommerce.data.referral.FailedAttemptData;
import com.freshdirect.ecommerce.data.referral.MailData;
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
import com.freshdirect.ecommerce.data.referral.UserCreditData;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.ModelConverter;

public class FDReferralManagerService extends AbstractEcommService implements FDReferralManagerServiceI{
	
	private final static Category LOGGER = LoggerFactory.getInstance(FDReferralManagerService.class);
	private static FDReferralManagerService INSTANCE;
	
	public static FDReferralManagerServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new FDReferralManagerService();

		return INSTANCE;
	}
	private static final String GET_REFERRAL_PROMOTION_DETAILS = "referral/promotion/userId/";
	private static final String GET_REFERRAL_PROMOTION_DETAILS_BY_NAME = "referral/promotion/refName/";
	private static final String GET_MANAGE_INVITES = "referral/invites/customerId/";
	private static final String GET_USER_CREDITS = "referral/usercredits/";
	private static final String GET_MANAGE_INVITES_FOR_CRM = "referral/invites/crm/customerId/";
	private static final String GET_AVAILABLE_CREDIT = "referral/credit/available/customerId/";
	private static final String GET_REFERRAL_DISPLAY_FLAG = "referral/flag/customerId/";
	private static final String GET_SETTLED_SALES = "referral/sales";
	private static final String GET_REFERRAL_LINK = "referral/link/customerId/";
	private static final String GET_STL_SALE = "referral/stlsale/customerId/";
	private static final String SAVE_CUSTOMER_CREDIT = "referral/customercredit";
	private static final String IS_CUSTOMER_REFERRED = "referral/customerReferred/customerId/";

	private static final String UPDATE_FDUSER = "referral/fduser/update";
	private static final String UPDATE_CUSTOMER_INFO = "referral/customerInfo/update";
	private static final String UPDATE_CUSTOMER_PWD = "referral/customerpw";
	private static final String UPDATE_CUSTOMER_PWD_HINT = "referral/fdcustomer/update";
	private static final String STORE_FAILED_ATTEMPT = "referral/failAttempt";
	private static final String IS_UNIQUE_FNLNZIP = "referral/fnlnzip";
	private static final String GET_REFERRAL_NAME = "referral/name/referralId/";
	private static final String IS_REFERRAL_COMPLETE = "referral/signup/email/";
	private static final String GET_SETTLED_TRANSACTIONS = "referral/transaction";
	private static final String UPDATE_SETTLED_REWARD = "referral/settled/transaction/update";
	private static final String SEND_MAILS = "referral/mails";
	private static final String CREATE_REFERRAL_INVITES = "referral/invitee";
	
	private static final String UPDATE_REFERRAL_STATUS = "referral/status/update";
	private static final String REMOVE_REFERRAL_OBJECTIVE = "referral/objective/delete";
	private static final String REMOVE_REFERRAL_PARTNER = "referral/partner/delete";
	private static final String REMOVE_REFERRAL_CAMPAIGN = "referral/campaign/delete";
	private static final String REMOVE_REFERRAL_CHANNEL = "referral/channel/delete";
	private static final String REMOVE_REFERRAL_PROGRAM = "referral/program/delete";
	private static final String UPDATE_REFERRAL_OBJECTIVE = "referral/objective/update";
	private static final String UPDATE_REFERRAL_PARTNER = "referral/partner/update";
	private static final String UPDATE_REFERRAL_CAMPAIGN = "referral/campaign/update";
	private static final String UPDATE_REFERRAL_CHANNEL = "referral/channel/update";
	private static final String UPDATE_REFERRAL_PROGRAM = "referral/program/update";
	private static final String CREATE_REFERRAL_CHANNEL = "referral/channel/create";
	private static final String CREATE_REFERRAL_PARTNER = "referral/partner/create";
	private static final String CREATE_REFERRAL_OBJECTIVE = "referral/objective/create";
	private static final String CREATE_REFERRAL_CAMPAIGN = "referral/campaign/create";
	private static final String CREATE_REFERRAL_PROGRAM = "referral/program/create";
	private static final String CREATE_REFERRAL_HISTORY = "referral/history/create";
	private static final String STORE_REFERRAL = "referral";
	private static final String LOAD_REFERRAL_PK = "referral/id/";
	private static final String LOAD_REFERRAL_PROGRAM_ID = "referral/programid/";
	private static final String LOAD_REFERRAL_CUSTOMER_ID = "referral/customerId/";
	private static final String LOAD_REFERRAL_EMAIL = "referral/emailId";
	private static final String LOAD_REFERRAL_REPORT_CUSTOMER_ID = "referral/report/customerId/";
	private static final String LOAD_REFERRAL_REPORT_REFERRAL_CUSTOMERID = "referral/report/referal/customerId/";
	private static final String LOAD_REFERRAL_PROGRAM = "referral/program";
	private static final String LOAD_REFERRAL_CHANNEL = "referral/channel";
	private static final String LOAD_REFERRAL_PARTNER = "referral/partner";
	private static final String LOAD_REFERRAL_OBJECTIVE = "referral/objective";
	private static final String LOAD_REFERRAL_CAMPAIGN = "referral/campaign";
	private static final String LOAD_REFERRAL_NAME_BY_REFERRAL_ID = "referral/name/customerId/";
	private static final String LOAD_REFERRAL_PROGRAM_FROM_PK = "referral/programId/";
	private static final String LOAD_LATEST_ACTIVE_REFERRAL_PROGRAM = "referral/activeprogram";
	private static final String GET_REFERRAL_CHANNEL_MODEL = "referral/channel/chaId/";
	private static final String GET_REFERRAL_CAMPAIGN_MODEL = "referral/campaign/chaId/";
	private static final String GET_REFERRAL_OBJECTIVE_MODEL = "referral/objective/chaId/";
	private static final String GET_REFERRAL_PARTNER_MODEL = "referral/partner/chaId/";
	private static final String GET_REFERRAL_PROGRAM_MODEL = "referral/program/chaId/";
	private static final String GET_REF_PROGRAM_FOR_REF_CHANNELS = "referral/program/refchannel";
	private static final String GET_REFPROGRAM_FOR_REFPARTNER = "referral/program/refpartner";
	private static final String GET_REFPROGRAM_FOR_REFCAMPAIGN = "referral/program/refcampaign";
	private static final String GET_REFCAMPAIGN_FOR_REFOBJECTIVE = "referral/program/refobjective";
	private static final String IS_REFERRAL_PARTNER_EXIST = "referral/partner/name/";
	private static final String IS_REFERRAL_CAMPAIGN_EXIST = "referral/campaign/name/";
	private static final String IS_REFERRAL_OBJECTIVE_EXIST = "referral/objective/name/";
	private static final String IS_REFERRAL_CHANNEL_NAME_TYPE_EXIST = "referral/channel/name/";
	private static final String IS_REFERRAL_PROGRAM_NAME_EXIST = "referral/program/name/";
	private static final String GET_REFERRAL_PROGRAMS = "referral/program/criteria";
	private static final String GET_REFERRAL_CHANNELS = "referral/channel/criteria";
	private static final String GET_REFERRAL_CAMPAIGNS = "referral/campaign/criteria";
	private static final String GET_REFERRAL_PARTNERS = "referral/partner/criteria";
	private static final String GET_REFERRAL_OBJECTIVES = "referral/objective/criteria";

	@Override
	public void updateReferralProgram(ReferralProgramData refProgram)throws FDResourceException, RemoteException {
		Request<ReferralProgramData> request = new Request<ReferralProgramData>();
		Response<Object> response = new Response<Object>();
		try{
			request.setData(refProgram);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(UPDATE_REFERRAL_PROGRAM),new TypeReference<Response<Object>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public void updateReferralChannel(ReferralChannelData channel)throws FDResourceException, RemoteException {
		Request<ReferralChannelData> request = new Request<ReferralChannelData>();
		Response<Object> response = new Response<Object>();
		try{
			request.setData(channel);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(UPDATE_REFERRAL_CHANNEL),new TypeReference<Response<Object>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public void updateReferralCampaign(ReferralCampaignData campaign)throws FDResourceException, RemoteException {
		Request<ReferralCampaignData> request = new Request<ReferralCampaignData>();
		Response<Object> response = new Response<Object>();
		try{
			request.setData(campaign);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(UPDATE_REFERRAL_CAMPAIGN),new TypeReference<Response<Object>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public void updateReferralPartner(ReferralPartnerData partner)throws FDResourceException, RemoteException {
		Request<ReferralPartnerData> request = new Request<ReferralPartnerData>();
		Response<Object> response = new Response<Object>();
		try{
			request.setData(partner);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(UPDATE_REFERRAL_PARTNER),new TypeReference<Response<Object>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public void updateReferralObjective(ReferralObjectiveData objective)throws FDResourceException, RemoteException {
		Request<ReferralObjectiveData> request = new Request<ReferralObjectiveData>();
		Response<Object> response = new Response<Object>();
		try{
			request.setData(objective);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(UPDATE_REFERRAL_OBJECTIVE),new TypeReference<Response<Object>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	
	@Override
	public void removeReferralProgram(String[] refProgramId)throws FDResourceException, RemoteException {
		Request<String[]> request = new Request<String[]>();
		Response<Object> response = new Response<Object>();
		try{
			request.setData(refProgramId);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(REMOVE_REFERRAL_PROGRAM),new TypeReference<Response<Object>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public void removeReferralChannel(String[] channelIds)throws FDResourceException, RemoteException {
		Request<String[]> request = new Request<String[]>();
		Response<Object> response = new Response<Object>();
		try{
			request.setData(channelIds);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(REMOVE_REFERRAL_CHANNEL),new TypeReference<Response<Object>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public void removeReferralCampaign(String[] campaignIds)throws FDResourceException, RemoteException {
		Request<String[]> request = new Request<String[]>();
		Response<Object> response = new Response<Object>();
		try{
			request.setData(campaignIds);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(REMOVE_REFERRAL_CAMPAIGN),new TypeReference<Response<Object>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public void removeReferralPartner(String[] partnerIds)throws FDResourceException, RemoteException {
		Request<String[]> request = new Request<String[]>();
		Response<Object> response = new Response<Object>();
		try{
			request.setData(partnerIds);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(REMOVE_REFERRAL_PARTNER),new TypeReference<Response<Object>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public void removeReferralObjective(String[] objectiveIds)throws FDResourceException, RemoteException {
		Request<String[]> request = new Request<String[]>();
		Response<Object> response = new Response<Object>();
		try{
			request.setData(objectiveIds);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(REMOVE_REFERRAL_OBJECTIVE),new TypeReference<Response<Object>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public void updateReferralStatus(String referralId, String ststus)throws FDResourceException, RemoteException {
		Response<Object> response = new Response<Object>();
		try{
			response = postDataTypeMap(null,getFdCommerceEndPoint(UPDATE_REFERRAL_STATUS+"?referralId="+referralId+"&status="+ststus),new TypeReference<Response<Object>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
	}
	}
	@Override
	public ReferralChannelData createReferralChannel(ReferralChannelData channel)
			throws FDResourceException, RemoteException {
		Response<ReferralChannelData> response = new Response<ReferralChannelData>();
		Request<ReferralChannelData> request = new Request<ReferralChannelData>();
		try {
			request.setData(channel);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,
					getFdCommerceEndPoint(CREATE_REFERRAL_CHANNEL),
					new TypeReference<Response<ReferralChannelData>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public ReferralPartnerData createReferralPartner(ReferralPartnerData partner)throws FDResourceException, RemoteException {
		Response<ReferralPartnerData> response = new Response<ReferralPartnerData>();
		Request<ReferralPartnerData> request = new Request<ReferralPartnerData>();
		try {
			request.setData(partner);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CREATE_REFERRAL_PARTNER),
					new TypeReference<Response<ReferralPartnerData>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public ReferralObjectiveData createReferralObjective(ReferralObjectiveData objective) throws FDResourceException,
			RemoteException {
		Response<ReferralObjectiveData> response = new Response<ReferralObjectiveData>();
		Request<ReferralObjectiveData> request = new Request<ReferralObjectiveData>();
		try {
			request.setData(objective);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CREATE_REFERRAL_OBJECTIVE),
					new TypeReference<Response<ReferralObjectiveData>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public ReferralCampaignData createReferralCampaign(ReferralCampaignData campaign) throws FDResourceException,
			RemoteException {
		Response<ReferralCampaignData> response = new Response<ReferralCampaignData>();
		Request<ReferralCampaignData> request = new Request<ReferralCampaignData>();
		try {
			request.setData(campaign);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CREATE_REFERRAL_CAMPAIGN),
					new TypeReference<Response<ReferralCampaignData>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public ReferralProgramData createReferralProgram(ReferralProgramData program)throws FDResourceException, RemoteException {
		Response<ReferralProgramData> response = new Response<ReferralProgramData>();
		Request<ReferralProgramData> request = new Request<ReferralProgramData>();
		try {
			request.setData(program);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CREATE_REFERRAL_PROGRAM),
					new TypeReference<Response<ReferralProgramData>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public ReferralHistoryData createReferralHistory(ReferralHistoryData history)throws FDResourceException, RemoteException {
		Response<ReferralHistoryData> response = new Response<ReferralHistoryData>();
		Request<ReferralHistoryData> request = new Request<ReferralHistoryData>();
		try {
			request.setData(history);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CREATE_REFERRAL_HISTORY),
					new TypeReference<Response<ReferralHistoryData>>() {
					});
			if (!response.getResponseCode().equals("OK")) {
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public void storeReferral(ReferralProgramInvitationData referral,FDUserData user) throws FDResourceException, RemoteException {
		Request<ReferralProgramInvitationData> request = new Request<ReferralProgramInvitationData>();
		try {
			request.setData(referral);
			String inputJson = buildRequest(request);
			 postDataTypeMap(inputJson,getFdCommerceEndPoint(STORE_REFERRAL),
					new TypeReference<Response<ReferralProgramInvitationData>>() {
					});
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public ReferralProgramInvitationData loadReferralFromPK(String referralId)throws FDResourceException, RemoteException {
		Response<ReferralProgramInvitationData> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_REFERRAL_PK+referralId),  new TypeReference<Response<ReferralProgramInvitationData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public List<ReferralProgramInvitationData> loadReferralsFromReferralProgramId(String referralProgramId)
			throws FDResourceException, RemoteException {
		Response<List<ReferralProgramInvitationData>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_REFERRAL_PROGRAM_ID+referralProgramId),  new TypeReference<Response<List<ReferralProgramInvitationData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public List<ReferralProgramInvitationData> loadReferralsFromReferrerCustomerId(String referrerCustomerId)
			throws FDResourceException, RemoteException {
		Response<List<ReferralProgramInvitationData>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_REFERRAL_CUSTOMER_ID+referrerCustomerId),  new TypeReference<Response<List<ReferralProgramInvitationData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public List<ReferralProgramInvitationData> loadReferralsFromReferralEmailAddress(String referralEmailAddress) throws FDResourceException,
			RemoteException {
		Request<String> request = new Request<String>();
		Response<List<ReferralProgramInvitationData>> response = null;
		try {
			request.setData(referralEmailAddress);
			String inputJson = buildRequest(request);
			response =  postDataTypeMap(inputJson,getFdCommerceEndPoint(LOAD_REFERRAL_EMAIL),
					new TypeReference<Response<List<ReferralProgramInvitationData>>>() {
					});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public List<FDReferralReportLineData> loadReferralReportFromReferrerCustomerId(String referrerCustomerId) throws FDResourceException,
			RemoteException {
		Response<List<FDReferralReportLineData>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_REFERRAL_REPORT_CUSTOMER_ID+referrerCustomerId),  new TypeReference<Response<List<FDReferralReportLineData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public List<FDReferralReportLineData> loadReferralReportFromReferralCustomerId(String referralCustomerId) throws FDResourceException,
			RemoteException {
		Response<List<FDReferralReportLineData>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_REFERRAL_REPORT_REFERRAL_CUSTOMERID+referralCustomerId),  new TypeReference<Response<List<FDReferralReportLineData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public List<ReferralProgramData> loadAllReferralPrograms() throws FDResourceException, RemoteException {
		Response<List<ReferralProgramData>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_REFERRAL_PROGRAM),  new TypeReference<Response<List<ReferralProgramData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public List<ReferralChannelData> loadAllReferralChannels() throws FDResourceException, RemoteException {
		Response<List<ReferralChannelData>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_REFERRAL_CHANNEL),  new TypeReference<Response<List<ReferralChannelData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public List<ReferralPartnerData> loadAllReferralpartners()throws FDResourceException, RemoteException {
		Response<List<ReferralPartnerData>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_REFERRAL_PARTNER),  new TypeReference<Response<List<ReferralPartnerData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public List<ReferralObjectiveData> loadAllReferralObjective() throws FDResourceException, RemoteException {
		Response<List<ReferralObjectiveData>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_REFERRAL_OBJECTIVE),  new TypeReference<Response<List<ReferralObjectiveData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public List<ReferralCampaignData> loadAllReferralCampaigns()throws FDResourceException, RemoteException {
		Response<List<ReferralCampaignData>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_REFERRAL_CAMPAIGN),  new TypeReference<Response<List<ReferralCampaignData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public String loadReferrerNameFromReferralCustomerId(String referralCustomerId) throws FDResourceException,
			RemoteException {
		Response<String> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_REFERRAL_NAME_BY_REFERRAL_ID+referralCustomerId),  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public ReferralProgramData loadReferralProgramFromPK(String referralProgramId) throws FDResourceException,
			RemoteException {
		Response<ReferralProgramData> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_REFERRAL_PROGRAM_FROM_PK+referralProgramId),  new TypeReference<Response<ReferralProgramData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public ReferralProgramData loadLastestActiveReferralProgram()throws FDResourceException, RemoteException {
		Response<ReferralProgramData> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_LATEST_ACTIVE_REFERRAL_PROGRAM),  new TypeReference<Response<ReferralProgramData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public ReferralChannelData getReferralChannleModel(String refChaId)throws FDResourceException, RemoteException {
		Response<ReferralChannelData> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_REFERRAL_CHANNEL_MODEL+refChaId),  new TypeReference<Response<ReferralChannelData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public ReferralCampaignData getReferralCampaigneModel(String refChaId)throws FDResourceException, RemoteException {
		Response<ReferralCampaignData> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_REFERRAL_CAMPAIGN_MODEL+refChaId),  new TypeReference<Response<ReferralCampaignData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public ReferralObjectiveData getReferralObjectiveModel(String refChaId)throws FDResourceException, RemoteException {
		Response<ReferralObjectiveData> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_REFERRAL_OBJECTIVE_MODEL+refChaId),  new TypeReference<Response<ReferralObjectiveData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public ReferralPartnerData getReferralPartnerModel(String refChaId)throws FDResourceException, RemoteException {
		Response<ReferralPartnerData> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_REFERRAL_PARTNER_MODEL+refChaId),  new TypeReference<Response<ReferralPartnerData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public ReferralProgramData getReferralProgramModel(String refChaId)throws FDResourceException, RemoteException {
		Response<ReferralProgramData> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_REFERRAL_PROGRAM_MODEL+refChaId),  new TypeReference<Response<ReferralProgramData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	@Override
	public List<ReferralProgramData> getReferralProgarmforRefChannel(String[] refChaIds) throws FDResourceException, RemoteException {
		Request<String[]> request = new Request<String[]>();
		Response<List<ReferralProgramData>> response = new Response<List<ReferralProgramData>>();
		try{
			request.setData(refChaIds);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_REF_PROGRAM_FOR_REF_CHANNELS),new TypeReference<Response<List<ReferralProgramData>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public List<ReferralProgramData> getReferralProgarmforRefPartner(String[] refpartIds) throws FDResourceException, RemoteException {
		Request<String[]> request = new Request<String[]>();
		Response<List<ReferralProgramData>> response = new Response<List<ReferralProgramData>>();
		try{
			request.setData(refpartIds);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_REFPROGRAM_FOR_REFPARTNER),new TypeReference<Response<List<ReferralProgramData>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	@Override
	public String getLatestSTLSale(String customerId)throws FDResourceException, RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_STL_SALE+customerId),  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();

	}
	@Override
	public void saveCustomerCredit(String referral_customer_id,String customer_id, int ref_fee, String sale, String complaintId,
			String refPrgmId) throws FDResourceException, RemoteException {
		Response<Object> response = new Response<Object>();
		try {
			Request<CustomerCreditData> request = new Request<CustomerCreditData>();
			request.setData(ModelConverter.buildCustomerCreditData( referral_customer_id, customer_id,  ref_fee,  sale,  complaintId,
					 refPrgmId));
			String inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(SAVE_CUSTOMER_CREDIT), new TypeReference<Response<Object>>() {});
			if (!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}
		catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public boolean isCustomerReferred(String customerId)throws FDResourceException, RemoteException {
		Response<Boolean> response = new Response<Boolean>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(IS_CUSTOMER_REFERRED+customerId),  new TypeReference<Response<Boolean>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();

	}
	@Override
	public String updateFDUser(String customerId, String zipCode,EnumServiceType serviceType) throws FDResourceException,
			RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.postDataTypeMap(null, getFdCommerceEndPoint(UPDATE_FDUSER+"?zipCode="+zipCode+"&serviceType="+serviceType.getName()+"&customerId="+customerId), new TypeReference<Response<String>>() {});
			if (!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}
		catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public void updateCustomerInfo(String customerId, String firstName,String lastName) throws FDResourceException, RemoteException {
		Response<Object> response = new Response<Object>();
		try {
			response = this.postDataTypeMap(null, getFdCommerceEndPoint(UPDATE_CUSTOMER_INFO+"?firstName="+firstName+"&lastName="+lastName+"&customerId="+customerId), new TypeReference<Response<Object>>() {});
			if (!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}
		catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public void updateCustomerPW(String customerId, String pwdHash)throws FDResourceException, RemoteException {
		Response<Object> response = new Response<Object>();
		try {
			response = this.postDataTypeMap(null, getFdCommerceEndPoint(UPDATE_CUSTOMER_PWD+"?pwdHash="+pwdHash+"&customerId="+customerId), new TypeReference<Response<Object>>() {});
			if (!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}
		catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public void updateFdCustomer(String customerId, String pwdHint)throws FDResourceException, RemoteException {
		Response<Object> response = new Response<Object>();
		try {
			response = this.postDataTypeMap(null, getFdCommerceEndPoint(UPDATE_CUSTOMER_PWD_HINT+"?pwdHint="+pwdHint+"&customerId="+customerId), new TypeReference<Response<Object>>() {});
			if (!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}
		catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public void storeFailedAttempt(String email, String dupeCustID,String zipCode, String firstName, String lastName, String referral,
			String reason) throws FDResourceException, RemoteException {
		Response<Object> response = new Response<Object>();
		try {
			Request<FailedAttemptData> request = new Request<FailedAttemptData>();
			request.setData(ModelConverter.buildFailesAttemptData(email, dupeCustID,zipCode, firstName, lastName, referral,
					reason));
			String inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(STORE_FAILED_ATTEMPT), new TypeReference<Response<Object>>() {});
			if (!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}
		catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public boolean isUniqueFNLNZipCombo(String firstName, String lastName,String zipCode, String customerId) throws FDResourceException,
			RemoteException {
		Response<Boolean> response = new Response<Boolean>();
		try {
			Request<FNLNZipData> request = new Request<FNLNZipData>();
			request.setData(ModelConverter.buildFNLNZipData(firstName, lastName,zipCode, customerId));
			String inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(IS_UNIQUE_FNLNZIP), new TypeReference<Response<Boolean>>() {});
			if (!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}
		catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public String getReferralName(String referralId)throws FDResourceException, RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_REFERRAL_NAME +referralId),  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();

	}
	@Override
	public boolean isReferreSignUpComplete(String email, EnumEStoreId storeid)throws FDResourceException, RemoteException {
		Response<Boolean> response = new Response<Boolean>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(IS_REFERRAL_COMPLETE + email),  new TypeReference<Response<Boolean>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();

	}
	@Override
	public List<ReferralPromotionData> getSettledTransaction()throws FDResourceException, RemoteException {
		Response< List<ReferralPromotionData>> response = new Response< List<ReferralPromotionData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_SETTLED_TRANSACTIONS),  new TypeReference<Response< List<ReferralPromotionData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();

	}
	@Override
	public Map<String, String> updateSetteledRewardTransaction(List<ReferralPromotionData> models) throws FDResourceException,
			RemoteException {
		Response< Map<String, String>> response = new Response< Map<String, String>>();
		try {
			Request<List<ReferralPromotionData>> request = new Request<List<ReferralPromotionData>>();
			request.setData(models);
			String inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(UPDATE_SETTLED_REWARD), new TypeReference<Response< Map<String, String>>>() {});
			if (!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}
		catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public void sendMails(String recipient_list, String mail_message,String identity, String rpid, String serverName)
			throws FDResourceException, RemoteException {
		Response<Object> response = new Response<Object>();
		try {
			Request<MailData> request = new Request<MailData>();
			request.setData(ModelConverter.buildMailData(recipient_list, mail_message,identity, rpid, serverName));
			String inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(SEND_MAILS), new TypeReference<Response< Object>>() {});
			if (!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}
		catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public ReferralProgramInvitationData createReferralInvitee(
			ReferralIniviteData referralInviteData) throws FDResourceException,
			RemoteException {
		Response<ReferralProgramInvitationData> response = new Response<ReferralProgramInvitationData>();
		try {
			Request<ReferralIniviteData> request = new Request<ReferralIniviteData>();
			request.setData(referralInviteData);
			String inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(CREATE_REFERRAL_INVITES), new TypeReference<Response<ReferralProgramInvitationData>>() {});
			if (!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}
		catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public List<ReferralProgramData> getReferralProgarmforRefCampaign(String[] refCampIds) throws FDResourceException, RemoteException {
		Request<String[]> request = new Request<String[]>();
		Response<List<ReferralProgramData>> response = new Response<List<ReferralProgramData>>();
		try{
			request.setData(refCampIds);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_REFPROGRAM_FOR_REFCAMPAIGN),new TypeReference<Response<List<ReferralProgramData>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public List<ReferralProgramData> getReferralCampaignforRefObjective(String[] refObjIds) throws FDResourceException, RemoteException {
		Request<String[]> request = new Request<String[]>();
		Response<List<ReferralProgramData>> response = new Response<List<ReferralProgramData>>();
		try{
			request.setData(refObjIds);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_REFCAMPAIGN_FOR_REFOBJECTIVE),new TypeReference<Response<List<ReferralProgramData>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public boolean isReferralPartnerNameExist(String refPartName)throws FDResourceException, RemoteException {
		Response<Boolean> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(IS_REFERRAL_PARTNER_EXIST+refPartName),  new TypeReference<Response<Boolean>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public boolean isReferralCampaignNameExist(String refCampName)throws FDResourceException, RemoteException {
		Response<Boolean> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(IS_REFERRAL_CAMPAIGN_EXIST+refCampName),  new TypeReference<Response<Boolean>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public boolean isReferralObjectiveNameExist(String refObjName)throws FDResourceException, RemoteException {
		Response<Boolean> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(IS_REFERRAL_OBJECTIVE_EXIST +refObjName),  new TypeReference<Response<Boolean>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public boolean isReferralChannelNameAndTypeExist(String name, String type)throws FDResourceException, RemoteException {
		Response<Boolean> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(IS_REFERRAL_CHANNEL_NAME_TYPE_EXIST+name+"/type/"+type),  new TypeReference<Response<Boolean>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public boolean isReferralProgramNameExist(String refPrgName)throws FDResourceException, RemoteException {
		Response<Boolean> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(IS_REFERRAL_PROGRAM_NAME_EXIST+refPrgName),  new TypeReference<Response<Boolean>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public List<ReferralProgramData> getReferralPrograms(ReferralSearchCriteriaData criteria) throws FDResourceException,
			RemoteException {
		Request<ReferralSearchCriteriaData> request = new Request<ReferralSearchCriteriaData>();
		Response<List<ReferralProgramData>> response = new Response<List<ReferralProgramData>>();
		try{
			request.setData(criteria);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_REFERRAL_PROGRAMS),new TypeReference<Response<List<ReferralProgramData>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public List<ReferralChannelData> getReferralChannels(ReferralSearchCriteriaData criteria) throws FDResourceException,
			RemoteException {
		Request<ReferralSearchCriteriaData> request = new Request<ReferralSearchCriteriaData>();
		Response<List<ReferralChannelData>> response = new Response<List<ReferralChannelData>>();
		try{
			request.setData(criteria);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_REFERRAL_CHANNELS),new TypeReference<Response<List<ReferralChannelData>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public List<ReferralCampaignData> getReferralCampaigns(ReferralSearchCriteriaData criteria) throws FDResourceException,
			RemoteException {
		Request<ReferralSearchCriteriaData> request = new Request<ReferralSearchCriteriaData>();
		Response<List<ReferralCampaignData>> response = new Response<List<ReferralCampaignData>>();
		try{
			request.setData(criteria);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_REFERRAL_CAMPAIGNS),new TypeReference<Response<List<ReferralCampaignData>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public List<ReferralPartnerData> getReferralPartners(ReferralSearchCriteriaData criteria) throws FDResourceException,
			RemoteException {
		Request<ReferralSearchCriteriaData> request = new Request<ReferralSearchCriteriaData>();
		Response<List<ReferralPartnerData>> response = new Response<List<ReferralPartnerData>>();
		try{
			request.setData(criteria);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_REFERRAL_PARTNERS),new TypeReference<Response<List<ReferralPartnerData>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public List<ReferralObjectiveData> getReferralObjective(ReferralSearchCriteriaData criteria) throws FDResourceException,
			RemoteException {
		Request<ReferralSearchCriteriaData> request = new Request<ReferralSearchCriteriaData>();
		Response<List<ReferralObjectiveData>> response = new Response<List<ReferralObjectiveData>>();
		try{
			request.setData(criteria);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_REFERRAL_OBJECTIVES),new TypeReference<Response<List<ReferralObjectiveData>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	
	@Override
	public ReferralPromotionData getReferralPromotionDetails(String userId)throws FDResourceException, RemoteException {
		Response<ReferralPromotionData> response = new Response<ReferralPromotionData>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_REFERRAL_PROMOTION_DETAILS+userId),  new TypeReference<Response<ReferralPromotionData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();

	}
	@Override
	public ReferralPromotionData getReferralPromotionDetailsByRefName(String referral) throws FDResourceException, RemoteException {
		Response<ReferralPromotionData> response = new Response<ReferralPromotionData>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_REFERRAL_PROMOTION_DETAILS_BY_NAME+referral),  new TypeReference<Response<ReferralPromotionData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();

	}
	@Override
	public List<ManageInvitesData> getManageInvites(String customerId)throws FDResourceException, RemoteException {
		Response<List<ManageInvitesData>> response = new Response<List<ManageInvitesData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_MANAGE_INVITES+customerId),  new TypeReference<Response<List<ManageInvitesData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();

	}
	@Override
	public List<ErpCustomerCreditModel> getUserCredits(String customerId)
			throws FDResourceException, RemoteException {
		Response<List<UserCreditData>> response = new Response<List<UserCreditData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_USER_CREDITS+customerId),  new TypeReference<Response<List<UserCreditData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return ModelConverter.buildCustomerCreditModelList(response.getData());

	}
	@Override
	public List<ManageInvitesData> getManageInvitesForCRM(String customerId)throws FDResourceException, RemoteException {
		Response<List<ManageInvitesData>> response = new Response<List<ManageInvitesData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_MANAGE_INVITES_FOR_CRM+customerId),  new TypeReference<Response<List<ManageInvitesData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();

	}
	@Override
	public Double getAvailableCredit(String customerId)throws FDResourceException, RemoteException {
		Response<Double> response = new Response<Double>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_AVAILABLE_CREDIT+customerId),  new TypeReference<Response<Double>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();

	}
	@Override
	public Boolean getReferralDisplayFlag(String customerId)throws FDResourceException, RemoteException {
		Response<Boolean> response = new Response<Boolean>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_REFERRAL_DISPLAY_FLAG+customerId),  new TypeReference<Response<Boolean>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();

	}
	@Override
	public List<ReferralPromotionData> getSettledSales()throws FDResourceException, RemoteException {
		Response<List<ReferralPromotionData>> response = new Response<List<ReferralPromotionData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_SETTLED_SALES),  new TypeReference<Response<List<ReferralPromotionData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();

	}
	@Override
	public String getReferralLink(String customerId)throws FDResourceException, RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_REFERRAL_LINK+customerId),  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();

	}
	

}
