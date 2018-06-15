package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.crm.CallLogModel;
import com.freshdirect.crm.CrmVSCampaignModel;
import com.freshdirect.customer.CustomerRatingI;
import com.freshdirect.customer.EnumPaymentResponse;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.customer.ErpComplaintReason;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpRedeliveryModel;
import com.freshdirect.customer.ErpSaleNotFoundException;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.customer.VSReasonCodes;
import com.freshdirect.ecomm.converter.CustomerRatingConverter;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecomm.gateway.ErpComplaintManagerService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.customer.CrmVSCampaignData;
import com.freshdirect.ecommerce.data.customer.CustomerRatingAdapterData;
import com.freshdirect.ecommerce.data.customer.ErpRedeliveryData;
import com.freshdirect.ecommerce.data.customer.FDCustomerOrderInfoData;
import com.freshdirect.ecommerce.data.customer.FDCustomerReservationInfoData;
import com.freshdirect.ecommerce.data.customer.FDCutoffTimeInfoData;
import com.freshdirect.ecommerce.data.customer.MealData;
import com.freshdirect.ecommerce.data.customer.ResubmitPaymentData;
import com.freshdirect.ecommerce.data.delivery.AlcoholRestrictionData;
import com.freshdirect.ecommerce.data.delivery.RestrictedAddressModelData;
import com.freshdirect.ecommerce.data.delivery.RestrictionData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.content.meal.MealModel;
import com.freshdirect.fdstore.customer.CustomerCreditModel;
import com.freshdirect.fdstore.customer.FDBrokenAccountInfo;
import com.freshdirect.fdstore.customer.FDCustomerOrderInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.LateDlvReportLine;
import com.freshdirect.fdstore.ecomm.converter.CallCenterConverter;
import com.freshdirect.framework.util.EnumSearchType;
import com.freshdirect.framework.util.GenericSearchCriteria;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.SettlementBatchInfo;
import com.freshdirect.payment.service.DlvRestrictionModelConverter;

public class CallCenterManagerService extends AbstractEcommService implements CallCenterManagerServiceI{
	
private final static Category LOGGER = LoggerFactory.getInstance(CallCenterManagerService.class);

private static final String RESUBMIT_PAYMENT = "callcenter/resubmitPayment/saleId/";
private static final String RESUBMIT_ORDER = "callcenter/resubmitOrder/saleId/";
private static final String RESUBMIT_CUSTOMER = "callcenter/resubmitCustomer/customerId/";
private static final String SCHEDULE_REDELIVERY = "callcenter/scheduleRedelivery/saleId/";
private static final String CHANGE_REDELIVERY_TO_RETURN = "callcenter/redelivery/return/change/saleId/";
private static final String GET_HOLIDAY_MEALS = "callcenter/holidayMeals/customerId/";


private static final String LOCATE_COMPANY_CUSTOMERS = "callcenter/companyCustomer/locate/searchType/";
private static final String ORDER_SUMMARY_SEARCH = "callcenter/orderSummary/search/searchType/";
private static final String SAVE_HOLIDAY_MEAL = "callcenter/holidayMeal/save/customerId/";
private static final String GET_CUTTOFF_TIME_FOR_DATE = "callcenter/cuttOffTime/date/";
private static final String GET_CUTTOFF_TIME_REPORT = "callcenter/cuttOffTime/report/date/";
private static final String EMAIL_CUTTOFF_TIME_REPORT = "callcenter/cuttOffTime/report/email/date/";
private static final String GET_LATE_DLV_REPORT = "callcenter/report/lateDelivery/date/";

private static final String SAVE_TOP_FAQs = "callcenter/topFaqs/save";
private static final String SAVE_VS_CAMPAIGN_INFO = "callcenter/vsCampaignList/save";
private static final String GET_VOICE_SHOT_LOG = "callcenter/voicehShotLog/date/";
private static final String GET_VOICE_SHOT_CALL_DETAILS = "callcenter/voicehShotLog/call/id/";
private static final String GET_VS_REDIAL_LIST = "callcenter/voicehShot/redialList/id/";
private static final String SAVE_VS_REDIAL_INFO = "callcenter/vsCampaign/redialInfo/save";
private static final String ADD_NEW_CAMPIAGN = "callcenter/campain/add";
private static final String GET_CAMPAIGN_DETAILS = "callcenter/campaign/details/id/";
private static final String UPDTAE_CAMPAIGN = "callcenter/campain/update";
private static final String DELETE_CAMPAIGN = "callcenter/campaign/delete/id/";
private static final String GET_VS_MSG_FOR_ORDER_PAGE = "callcenter/campaign/details/orderId/";
private static final String GET_VSR_CODES = "callcenter/voiceshot/reasonCodes";
private static final String GET_SOUND_FILE_MESSAGE = "callcenter/soundFileMessage/campaignId/";
private static final String GET_AUTO_LATE_DELIVERY_CREDITS = "callcenter/autolateDeliver/credits";
private static final String GET_AUTO_LATE_DELIVERY_ORDRS = "callcenter/deliveryOrder/autoLate/id/";
private static final String GET_AUTO_LATE_DELIVERY_PASS_ORDERS = "callcenter/deliveryPassOrder/autoLate/id/";
private static final String GET_REASON_BY_COMP_CODE = "callcenter/reason/compCode/";
private static final String ADD_NEW_IVR_CALL_LOG = "callcenter/callLog/IVR/add";
private static final String GET_REVERSE_AUTH_ORDERS = "callcenter/authOrders/reverse/date/";
private static final String GET_ORDERS_FOR_VOID_CAPTURE = "callcenter/orders/voidCapture/date/";
private static final String REVERSE_AUTH_ORDERS = "callcenter/authOrder/reverse/saleId/";
private static final String VOID_CAPTURE_ORDER = "callcenter/voidCaptureOrder/saleId/";
private static final String DO_GENERIC_SEARCH = "callcenter/genericSearch/searchType/";

	private static CallCenterManagerService INSTANCE;
	
	
	public static CallCenterManagerServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CallCenterManagerService();

		return INSTANCE;
	}


	@Override
	public Map<String, List<ErpComplaintReason>> getComplaintReasons(boolean excludeCartonReq) throws FDResourceException,
			RemoteException {
		return ErpComplaintManagerService.getInstance().getReasons(excludeCartonReq);
	}


	@Override
	public Map<String, String> getComplaintCodes() throws FDResourceException,RemoteException {
		return ErpComplaintManagerService.getInstance().getComplaintCodes();
	}


	@Override
	public void rejectMakegoodComplaint(String makegood_sale_id)throws FDResourceException, RemoteException {
		ErpComplaintManagerService.getInstance().rejectMakegoodComplaint(makegood_sale_id);
	}

	@Override
	public EnumPaymentResponse resubmitPayment(String saleId,ErpPaymentMethodI payment, Collection charges)
			throws FDResourceException, ErpTransactionException,
			RemoteException {
 		Request<ResubmitPaymentData> request = new Request<ResubmitPaymentData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(CallCenterConverter.buildResubmitPaymentData(payment,charges));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(RESUBMIT_PAYMENT +saleId),new TypeReference<Response<String>>() {});
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
		return null; // As per legacy implementation the reponse is always null in ERpCustomerManagerSB.
	}


	@Override
	public void resubmitOrder(String saleId, CustomerRatingI cra,EnumSaleType saleType) throws RemoteException, FDResourceException,
			ErpTransactionException {
		Request<CustomerRatingAdapterData> request = new Request<CustomerRatingAdapterData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(CustomerRatingConverter.buildCustomerRatingData(cra));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(RESUBMIT_ORDER+saleId+"/saleType/"+saleType.getSaleType()),new TypeReference<Response<String>>() {});
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
	public void resubmitCustomer(String customerID) throws FDResourceException,
			RemoteException {
		Response<String> response = new Response<String>();
		try{
			response = postDataTypeMap(null,getFdCommerceEndPoint(RESUBMIT_CUSTOMER+customerID),new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} 
	}

	@Override
	public void scheduleRedelivery(String saleId,ErpRedeliveryModel redeliveryModel) throws FDResourceException,
			ErpTransactionException, RemoteException {
		Request<ErpRedeliveryData> request = new Request<ErpRedeliveryData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(CallCenterConverter.buildErpRedeliveryData(redeliveryModel));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(SCHEDULE_REDELIVERY+saleId),new TypeReference<Response<String>>() {});
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
	public void changeRedeliveryToReturn(String saleId)throws FDResourceException, ErpTransactionException,
			ErpSaleNotFoundException, RemoteException {
		Response<String> response = new Response<String>();
		try{
			response = postDataTypeMap(null,getFdCommerceEndPoint(CHANGE_REDELIVERY_TO_RETURN + saleId),new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} 
	}


	@Override
	public List getHolidayMeals(FDIdentity identity)throws FDResourceException, RemoteException {
		//Generic list will create problem but this methos has dependency on Meal Persistance bean so only stub code for this mthod
		Response<List<MealData>> response = new Response<List<MealData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_HOLIDAY_MEALS +identity.getErpCustomerPK()),  new TypeReference<Response<List<MealData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CallCenterConverter.buildMealModelList(response.getData());
	}


	@Override
	public <E> List locateCompanyCustomers(GenericSearchCriteria criteria)
			throws FDResourceException, RemoteException {
		Request<Map<String,String>> request = new Request<Map<String,String>>();
		Response<List> response = null;
		try {
			request.setData(criteria.getCriteriaMap());
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(LOCATE_COMPANY_CUSTOMERS
							+ criteria.getSearchType().getName()),
							(TypeReference<E>)typeReferenceFor(criteria));
			if (!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());

		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException();
		} catch (FDEcommServiceException e) {
			throw new RemoteException();
		}

		return CallCenterConverter.buildServiceGenericModel(response.getData());}


	@Override
	public List orderSummarySearch(GenericSearchCriteria criteria)throws FDResourceException, RemoteException {
		Response<List<RestrictionData>> response = new Response<List<RestrictionData>>();
		Request<Map<String , String>> request = new Request<Map<String,String>>();
		try{
			request.setData(criteria.getCriteriaMap());
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(ORDER_SUMMARY_SEARCH+criteria.getSearchType().getName()),  new TypeReference<Response<List<RestrictionData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		}
		return DlvRestrictionModelConverter.buildDlvRestrictionListResponse(response.getData());
	}


	@Override
	public MealModel saveHolidayMeal(FDIdentity identity, String agent,MealModel meal) throws FDResourceException, RemoteException {
		Request<MealData> request = new Request<MealData>();
		Response<MealData> response = new Response<MealData>();
		try{
			request.setData(CallCenterConverter.buildMealData(meal));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(SAVE_HOLIDAY_MEAL+identity.getErpCustomerPK()+"/agent/"+agent),new TypeReference<Response<MealData>>() {});
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
		return CallCenterConverter.buildMealModel(response.getData());
	}

	@Override
	public List getCutoffTimeForDate(Date date) throws FDResourceException,RemoteException {
		Response<List<Long>> response = new Response<List<Long>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_CUTTOFF_TIME_FOR_DATE +date.getTime()),  new TypeReference<Response<List<Long>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		List<java.sql.Timestamp> cuttOffTime = new ArrayList<java.sql.Timestamp>();
		for (Long timestamp : response.getData()) {
			cuttOffTime.add(new java.sql.Timestamp(timestamp));
		}
		return cuttOffTime;
	}


	@Override
	public List getCutoffTimeReport(Date day) throws FDResourceException,RemoteException {
		Response<List<FDCutoffTimeInfoData>> response = new Response<List<FDCutoffTimeInfoData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_CUTTOFF_TIME_REPORT +day.getTime()),  new TypeReference<Response<List<FDCutoffTimeInfoData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CallCenterConverter.buildCutOffTimeInfo(response.getData());
	}


	@Override
	public void emailCutoffTimeReport(Date day) throws FDResourceException,
			RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(EMAIL_CUTTOFF_TIME_REPORT + day.getTime()),  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public List getLateDeliveryReport(Date date) throws FDResourceException,RemoteException {
		Response<List<LateDlvReportLine>> response = new Response<List<LateDlvReportLine>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_LATE_DLV_REPORT+date.getTime()),  new TypeReference<Response<List<LateDlvReportLine>>>(){});
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
	public <E> List doGenericSearch(GenericSearchCriteria criteria)throws FDResourceException, RemoteException {
		
	Response<List> response = null;
	try {
		response = httpGetDataTypeMap(getFdCommerceEndPoint(DO_GENERIC_SEARCH +criteria.getSearchType().getName()),(TypeReference<E>)typeReferenceFor(criteria));
		if (!response.getResponseCode().equals("OK"))
			throw new FDResourceException(response.getMessage());

	} catch (FDResourceException e) {
		LOGGER.error(e.getMessage());
		throw new RemoteException();
	}

	return CallCenterConverter.buildServiceGenericModel(response.getData());}


	private <E> E typeReferenceFor(GenericSearchCriteria criteria) {
		 if(EnumSearchType.COMPANY_SEARCH.equals(criteria.getSearchType())){
			 return (E) new TypeReference<Response<List<FDCustomerOrderInfoData>>>(){} ;
			} else if(EnumSearchType.EXEC_SUMMARY_SEARCH.equals(criteria.getSearchType())){
				 return (E) new TypeReference<Response<List<Map>>>(){} ;
			} else if(EnumSearchType.RESERVATION_SEARCH.equals(criteria.getSearchType())){
				return (E) new TypeReference<Response<List<FDCustomerReservationInfoData>>>(){} ;
			} else if(EnumSearchType.ORDERS_BY_RESV_SEARCH.equals(criteria.getSearchType())){			
				return (E) new TypeReference<Response<List<FDCustomerOrderInfoData>>>(){} ;
			}else if(EnumSearchType.BROKEN_ACCOUNT_SEARCH.equals(criteria.getSearchType())){
				return (E) new TypeReference<Response<List<FDBrokenAccountInfo>>>(){} ;
			}else if(EnumSearchType.CANCEL_ORDER_SEARCH.equals(criteria.getSearchType())){
				return (E) new TypeReference<Response<List<FDCustomerOrderInfoData>>>(){} ;
			}else if(EnumSearchType.RETURN_ORDER_SEARCH.equals(criteria.getSearchType())){
				return (E) new TypeReference<Response<List<FDCustomerOrderInfoData>>>(){} ;
			}else if(EnumSearchType.SETTLEMENT_BATCH_SEARCH.equals(criteria.getSearchType())){
				return (E) new TypeReference<Response<List<SettlementBatchInfo>>>(){} ;
			}
			else if(EnumSearchType.DEL_RESTRICTION_SEARCH.equals(criteria.getSearchType())||EnumSearchType.PLATTER_RESTRICTION_SEARCH.equals(criteria.getSearchType())){
				return (E) new TypeReference<Response<List<RestrictionData>>>(){} ;
			}
			else if(EnumSearchType.ALCOHOL_RESTRICTION_SEARCH.equals(criteria.getSearchType())){
				return (E) new TypeReference<Response<List<AlcoholRestrictionData>>>(){} ;
			}		
			else if(EnumSearchType.ADDR_RESTRICTION_SEARCH.equals(criteria.getSearchType())){
				return (E) new TypeReference<Response<List<RestrictedAddressModelData>>>(){} ;
			}
			else if(EnumSearchType.ORDER_SEARCH_BY_SKUS.equals(criteria.getSearchType())){
				return (E) new TypeReference<Response<List<FDCustomerOrderInfoData>>>(){} ;
			}
			else if(EnumSearchType.GET_ORDERS_TO_MODIFY.equals(criteria.getSearchType())){
				return (E) new TypeReference<Response<List<FDCustomerOrderInfoData>>>(){} ;
			}
		return null;
	}

	@Override
	public void saveTopFaqs(List faqIds) throws FDResourceException,
			RemoteException {
		Request<List<String>> request = new Request<List<String>>();
		try{
			request.setData(faqIds);
			String inputJson = buildRequest(request);
			 postDataTypeMap(inputJson,getFdCommerceEndPoint(SAVE_TOP_FAQs),new TypeReference<Response<String>>() {});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public String saveVSCampaignInfo(CrmVSCampaignModel model)
			throws FDResourceException, RemoteException {
		Request<CrmVSCampaignData> request = new Request<CrmVSCampaignData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(CallCenterConverter.buildCrmVSCampaignData(model));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(SAVE_VS_CAMPAIGN_INFO),new TypeReference<Response<String>>() {});
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
	public List<CrmVSCampaignModel> getVoiceShotLog(Date date)
			throws FDResourceException, RemoteException {
		Response<List<CrmVSCampaignData>> response = new Response<List<CrmVSCampaignData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_VOICE_SHOT_LOG+date.getTime()),  new TypeReference<Response<List<CrmVSCampaignData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CallCenterConverter.buildCrmVSCampaignModelList(response.getData());
	}


	@Override
	public List<CrmVSCampaignModel> getVoiceShotCallDetails(String id,
			String lateId) throws FDResourceException, RemoteException {
		Response<List<CrmVSCampaignData>> response = new Response<List<CrmVSCampaignData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_VOICE_SHOT_CALL_DETAILS+id+"/lateId/"+lateId),  new TypeReference<Response<List<CrmVSCampaignData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CallCenterConverter.buildCrmVSCampaignModelList(response.getData());
	}


	@Override
	public List<CrmVSCampaignModel> getVSRedialList(String id, String lateId)
			throws FDResourceException, RemoteException {
		Response<List<CrmVSCampaignData>> response = new Response<List<CrmVSCampaignData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_VS_REDIAL_LIST+id+"/lateId/"+lateId),  new TypeReference<Response<List<CrmVSCampaignData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CallCenterConverter.buildCrmVSCampaignModelList(response.getData());
	}


	@Override
	public String saveVSRedialInfo(CrmVSCampaignModel model)
			throws FDResourceException, RemoteException {
		Request<CrmVSCampaignData> request = new Request<CrmVSCampaignData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(CallCenterConverter.buildCrmVSCampaignData(model));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(SAVE_VS_REDIAL_INFO),new TypeReference<Response<String>>() {});
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
	public void addNewCampaign(CrmVSCampaignModel model)
			throws FDResourceException, RemoteException {
		Request<CrmVSCampaignData> request = new Request<CrmVSCampaignData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(CallCenterConverter.buildCrmVSCampaignData(model));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(ADD_NEW_CAMPIAGN),new TypeReference<Response<String>>() {});
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
	public CrmVSCampaignModel getCampaignDetails(String id)
			throws FDResourceException, RemoteException {
		Response<CrmVSCampaignData> response = new Response<CrmVSCampaignData>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_CAMPAIGN_DETAILS+id),  new TypeReference<Response<CrmVSCampaignData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CallCenterConverter.buildCrmVSCampaignModel(response.getData());
	}


	@Override
	public void updateCampaign(CrmVSCampaignModel model)
			throws FDResourceException, RemoteException {
		Request<CrmVSCampaignData> request = new Request<CrmVSCampaignData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(CallCenterConverter.buildCrmVSCampaignData(model));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(UPDTAE_CAMPAIGN),new TypeReference<Response<String>>() {});
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
	public void deleteCampaign(String id) throws FDResourceException,
			RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(DELETE_CAMPAIGN +id),  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}


	@Override
	public String getVSMsgForOrderPage(String orderId)
			throws FDResourceException, RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_VS_MSG_FOR_ORDER_PAGE+orderId),  new TypeReference<Response<String>>(){});
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
	public List<VSReasonCodes> getVSReasonCodes() throws FDResourceException,
			RemoteException {
		Response<List<VSReasonCodes>> response = new Response<List<VSReasonCodes>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_VSR_CODES),  new TypeReference<Response<List<VSReasonCodes>>>(){});
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
	public String getSoundFileMessage(String campaignId)
			throws FDResourceException, RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_SOUND_FILE_MESSAGE +campaignId),  new TypeReference<Response<String>>(){});
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
	public List getAutoLateDeliveryCredits() throws FDResourceException,
			RemoteException {
		Response<List<CustomerCreditModel>> response = new Response<List<CustomerCreditModel>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_AUTO_LATE_DELIVERY_CREDITS),  new TypeReference<Response<List<CustomerCreditModel>>>(){});
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
	public List getAutoLateDeliveryOrders(String id)
			throws FDResourceException, RemoteException {
		Response<List<CustomerCreditModel>> response = new Response<List<CustomerCreditModel>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_AUTO_LATE_DELIVERY_ORDRS+id),  new TypeReference<Response<List<CustomerCreditModel>>>(){});
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
	public List getAutoLateDlvPassOrders(String id) throws FDResourceException,
			RemoteException {
		Response<List<CustomerCreditModel>> response = new Response<List<CustomerCreditModel>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_AUTO_LATE_DELIVERY_PASS_ORDERS +id),  new TypeReference<Response<List<CustomerCreditModel>>>(){});
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
	public ErpComplaintReason getReasonByCompCode(String cCode)
			throws FDResourceException, RemoteException {
		return ErpComplaintManagerService.getInstance().getReasonByCompCode(cCode);
	}


	@Override
	public void addNewIVRCallLog(CallLogModel callLogModel)
			throws FDResourceException, RemoteException {
		Request<CallLogModel> request = new Request<CallLogModel>();
		try{
			request.setData(callLogModel);
			String inputJson = buildRequest(request);
			postDataTypeMap(inputJson,getFdCommerceEndPoint(ADD_NEW_IVR_CALL_LOG),new TypeReference<Response<String>>() {});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}


	@Override
	public List<FDCustomerOrderInfo> getReverseAuthOrders(String date)
			throws FDResourceException, RemoteException {
		Response<List<FDCustomerOrderInfoData>> response = new Response<List<FDCustomerOrderInfoData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_REVERSE_AUTH_ORDERS+date),  new TypeReference<Response<List<FDCustomerOrderInfoData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CallCenterConverter.buildCustomerOrderInfoList(response.getData());
	}


	@Override
	public List<FDCustomerOrderInfo> getOrdersForVoidCapture(String date)
			throws FDResourceException, RemoteException {
		Response<List<FDCustomerOrderInfoData>> response = new Response<List<FDCustomerOrderInfoData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_ORDERS_FOR_VOID_CAPTURE+date),  new TypeReference<Response<List<FDCustomerOrderInfoData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CallCenterConverter.buildCustomerOrderInfoList(response.getData());
	}


	@Override
	public void reverseAuthOrder(String saleId) throws RemoteException,
			FDResourceException, ErpTransactionException {
		try {
			 this.httpGetDataTypeMap(getFdCommerceEndPoint(REVERSE_AUTH_ORDERS+saleId),  new TypeReference<Response<String>>(){});
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}


	@Override
	public void voidCaptureOrder(String saleId) throws RemoteException,
			FDResourceException, ErpTransactionException {
		try {
			 this.httpGetDataTypeMap(getFdCommerceEndPoint(VOID_CAPTURE_ORDER+saleId),  new TypeReference<Response<String>>(){});
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	

}
