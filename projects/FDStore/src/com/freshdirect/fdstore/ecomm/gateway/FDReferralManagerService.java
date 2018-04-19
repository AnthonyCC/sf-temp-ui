package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.customer.ErpCustomerCreditModel;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.referral.CustomerCreditData;
import com.freshdirect.ecommerce.data.referral.FNLNZipData;
import com.freshdirect.ecommerce.data.referral.FailedAttemptData;
import com.freshdirect.ecommerce.data.referral.ManageInvitesData;
import com.freshdirect.ecommerce.data.referral.ReferralCampaignData;
import com.freshdirect.ecommerce.data.referral.ReferralChannelData;
import com.freshdirect.ecommerce.data.referral.ReferralHistoryData;
import com.freshdirect.ecommerce.data.referral.ReferralIniviteData;
import com.freshdirect.ecommerce.data.referral.ReferralPartnerData;
import com.freshdirect.ecommerce.data.referral.ReferralProgramData;
import com.freshdirect.ecommerce.data.referral.ReferralProgramInvitationData;
import com.freshdirect.ecommerce.data.referral.ReferralPromotionData;
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
	private static final String GET_REFERRAL_PROMOTION_DETAILS_BY_NAME = "referral/promotion/refName/";
	private static final String GET_MANAGE_INVITES = "referral/invites/customerId/";
	private static final String GET_USER_CREDITS = "referral/usercredits/";
	private static final String GET_AVAILABLE_CREDIT = "referral/credit/available/customerId/";
	private static final String GET_REFERRAL_DISPLAY_FLAG = "referral/flag/customerId/";
	private static final String GET_SETTLED_SALES = "referral/sales";
	private static final String GET_REFERRAL_LINK = "referral/link/customerId/";
	private static final String GET_STL_SALE = "referral/stlsale/customerId/";
	private static final String SAVE_CUSTOMER_CREDIT = "referral/customercredit";
	private static final String IS_CUSTOMER_REFERRED = "referral/customerReferred/customerId/";

	private static final String STORE_FAILED_ATTEMPT = "referral/failAttempt";
	private static final String IS_UNIQUE_FNLNZIP = "referral/fnlnzip";
	private static final String GET_REFERRAL_NAME = "referral/name/referralId/";
	private static final String IS_REFERRAL_COMPLETE = "referral/signup/email/";
	private static final String GET_SETTLED_TRANSACTIONS = "referral/transaction";
	private static final String UPDATE_SETTLED_REWARD = "referral/settled/transaction/update";
	private static final String CREATE_REFERRAL_INVITES = "referral/invitee";
	
	private static final String CREATE_REFERRAL_HISTORY = "referral/history/create";
	private static final String LOAD_REFERRAL_PROGRAM = "referral/program";
	private static final String LOAD_LATEST_ACTIVE_REFERRAL_PROGRAM = "referral/activeprogram";
	private static final String GET_REFERRAL_CHANNEL_MODEL = "referral/channel/chaId/";
	private static final String GET_REFERRAL_CAMPAIGN_MODEL = "referral/campaign/chaId/";
	private static final String GET_REFERRAL_PARTNER_MODEL = "referral/partner/chaId/";
	
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
