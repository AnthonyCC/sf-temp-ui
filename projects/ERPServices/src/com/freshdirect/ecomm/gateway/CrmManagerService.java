package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.FinderException;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.crm.CrmAgentInfo;
import com.freshdirect.crm.CrmAgentList;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.crm.CrmAuthInfo;
import com.freshdirect.crm.CrmAuthSearchCriteria;
import com.freshdirect.crm.CrmAuthenticationException;
import com.freshdirect.crm.CrmAuthorizationException;
import com.freshdirect.crm.CrmCaseAction;
import com.freshdirect.crm.CrmCaseInfo;
import com.freshdirect.crm.CrmCaseModel;
import com.freshdirect.crm.CrmCaseOperation;
import com.freshdirect.crm.CrmCaseTemplate;
import com.freshdirect.crm.CrmCustomerHeaderInfo;
import com.freshdirect.crm.CrmLateIssueModel;
import com.freshdirect.crm.CrmQueueInfo;
import com.freshdirect.crm.CrmStatus;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.customer.CustomerCreditModel;
import com.freshdirect.customer.EnumAccountActivityType;
import com.freshdirect.customer.EnumCannedTextCategory;
import com.freshdirect.customer.ErpCannedText;
import com.freshdirect.customer.ErpDuplicateUserIdException;
import com.freshdirect.customer.ErpTruckInfo;
import com.freshdirect.deliverypass.DeliveryPassModel;
import com.freshdirect.ecomm.converter.CrmManagerConverter;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.crm.AuthorisationData;
import com.freshdirect.ecommerce.data.crm.CannedTextData;
import com.freshdirect.ecommerce.data.crm.CrmAgentInfoData;
import com.freshdirect.ecommerce.data.crm.CrmAgentListData;
import com.freshdirect.ecommerce.data.crm.CrmAuthInfoData;
import com.freshdirect.ecommerce.data.crm.CrmCaseData;
import com.freshdirect.ecommerce.data.crm.CrmCaseOperationData;
import com.freshdirect.ecommerce.data.crm.CrmCaseTemplateData;
import com.freshdirect.ecommerce.data.crm.CrmCustomerHeaderInfoData;
import com.freshdirect.ecommerce.data.crm.CrmDeliveryPassData;
import com.freshdirect.ecommerce.data.crm.CrmLateIssueData;
import com.freshdirect.ecommerce.data.crm.CrmQueueInfoData;
import com.freshdirect.ecommerce.data.crm.CrmStatusData;
import com.freshdirect.ecommerce.data.crm.CrmSystemCaseInfoData;
import com.freshdirect.ecommerce.data.crm.DownloadCaseData;
import com.freshdirect.ecommerce.data.crm.ErpTruckInfoData;
import com.freshdirect.ecommerce.data.crm.IncrCountData;
import com.freshdirect.ecommerce.data.crm.LoginAgentData;
import com.freshdirect.ecommerce.data.crm.UpdateCaseData;
import com.freshdirect.ecommerce.data.crm.ViewAccountData;
import com.freshdirect.ecommerce.data.dlvpass.DeliveryPassData;
import com.freshdirect.ecommerce.data.ecoupon.CrmAgentModelData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CrmManagerService extends AbstractEcommService implements CrmManagerServiceI {
	
	private static CrmManagerService INSTANCE;
	
	private final static Category LOGGER = LoggerFactory.getInstance(CrmManagerService.class);

	private static final String FIND_CASES = "crm/cases/find";
	private static final String LOCK_CASE = "crm/cases/lock/agentPK/";
	private static final String UNLOCK_CASE = "crm/cases/unlock/casePk/";
	private static final String CLOSE_AUTO_CASE = "crm/cases/close/casePk/";
	private static final String GET_QUEUE_OVERVIEW = "crm/queue/overview";
	private static final String GET_CSR_OVERVIEW = "crm/csr/overview";
	private static final String GET_OPERATIONS = "crm/operations";
	private static final String DOWNLOAD_CASES = "crm/cases/download";
	private static final String GET_SESSION_STATUS = "crm/session/status/agentPK/";
	private static final String SAVE_SESSION_STATUS = "crm/session/save";
	private static final String CREATE_LATE_ISSUE = "crm/lateIssue/create";
	private static final String UPDATE_LATE_ISSUE = "crm/lateIssue/update";
	private static final String GET_LATE_ISSUE_BY_ID = "crm/lateIssue/id/";
	private static final String GET_LATE_ISSUE_BY_ROUTE_AND_DATE = "crm/lateIssue/route/";
	private static final String GET_LATE_ISSUE_BY_DATE = "crm/lateIssue/date/";
	private static final String GET_TRUCK_NUMBER_FOR_DATE = "crm/trucknumber/date/";
	private static final String GET_RECENT_LATE_ISSUE_FOR_ORDER = "crm/lateIssue/orderId/";
	private static final String GET_CUSTOMER_HEADER_INFO = "crm/customer/header/customerId/";
	private static final String CANCEL_DELIVERY_PASS = "crm/deliveryPass/cancel";
	private static final String REACTIVATE_DELIVERY_PASS = "crm/deliveryPass/reactivate";
	private static final String LOOKUP_ACCOUNT = "crm/account/accountNum/";
	private static final String LOOKUP_ORDERS = "crm/orders/accountNum/";
	private static final String LOG_VIEW_ACCOUNT = "crm/viewAccount/customerId/";
	private static final String VIEW_ACCOUNT = "crm/viewAccount";
	private static final String CREATE_CANNED_TEXT = "crm/cannedText/create";
	private static final String UPDATE_CANNED_TEXT = "crm/cannedText/update/id/";
	private static final String DELETE_CANNED_TEXT = "crm/cannedText/delete/id/";
	private static final String GET_CANNED_TEXT_BY_ID = "crm/cannedText/id/";
	private static final String GET_ALL_CANNEDTEXT_IN_CATEGORY = "crm/cannedText/category/";
	private static final String GET_ALL_CANNEDTEXT = "crm/cannedText";
	private static final String GET_COMPLAINT_DLVISSUE_TYPES = "crm/complaint/deliveryIssues/erpCustomerId/";
	private static final String GET_LAST_DELIVERED_ORDER = "crm/order/lastDelivered/erpCustomerId/";
	private static final String GET_AUTHORIZATIONS = "crm/authorizations";
	private static final String GET_ORDER_FOR_LATE_CREDIT = "crm/orders/lateCredit/saleId/";
	private static final String CASE_CREATED_ORDER_LATE_DELIVERY = "crm/cases/order/latedelivery/saleId/";
	private static final String ORDER_CREDITED_FOR_ORDER_LATE_DELIVERY = "crm/order/latedelivery/saleId/";
	private static final String GET_DELIVERY_PASS_INFO_BY_ID = "crm/dlvpass/dlvPassId/";
	private static final String UPDATE_AUTO_LATE_CREDIT = "crm/lateCredit/orderId/";
	private static final String GET_ACTIVE_DP = "crm/dlvpass/active/customerId/";
	private static final String UPDATE_LATE_CREDITS_REJECTED = "crm/lateCredit/rejected/agent/";
	private static final String IS_DELIVERY_PASS_EXTENDED = "crm/dlvPass/extended/orderId/";
	private static final String CRM_RESTRICTION_ENABLED = "crm/restriction/enabled";
	private static final String GET_ALLOWED_USERS = "crm/allowedUsers";

	private static final String CREATE_AGENT = "crm/agent/create/agentId/";
	private static final String UPDATE_AGENT = "crm/agent/update/agentId/";
	private static final String GET_AGENT_BY_PK = "crm/agent/agentId/";
	private static final String GET_ALL_AGENTS = "crm/agent";
	private static final String GET_CASE_BY_PK = "crm/case/id/";
	private static final String LOGIN_AGENT = "crm/loginAgent";
	private static final String CREATE_CASE = "crm/case/create";
	private static final String CREATE_SYSTEM_CASE = "crm/systemCase/create";
	private static final String CREATE_SYSTEM_CASE_IN_SINGLE_TXN = "crm/systemCase/create/singleTxn";
	private static final String UPDATE_CASE = "crm/case/update";
	private static final String INCR_DELIVERY_COUNT = "crm/deliveryCount/incr";
	private static final String INCR_EXPIRATION_PERIOD = "crm/expirationPeriod/incr";
	private static final String GET_AGENT_BY_LDAP_ID = "crm/agent/ldapId/";
	
	public static CrmManagerServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CrmManagerService();

		return INSTANCE;
	}

	@Override
	public PrimaryKey createAgent(CrmAgentModel agent, PrimaryKey userPk)throws FDResourceException, CrmAuthorizationException,
			ErpDuplicateUserIdException, RemoteException {
		Request<CrmAgentModelData> request = new Request<CrmAgentModelData>();
		String inputJson;
		Response<String> response = null;
		try{
			request.setData(CrmManagerConverter.buildCrmAgentModelData(agent));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CREATE_AGENT+userPk.getId()), new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			return new PrimaryKey(response.getData());
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public void updateAgent(CrmAgentModel agent, PrimaryKey userPk)throws FDResourceException, CrmAuthorizationException,
			RemoteException {
		Request<CrmAgentModelData> request = new Request<CrmAgentModelData>();
		String inputJson;
		Response<String> response = null;
		try{
			request.setData(CrmManagerConverter.buildCrmAgentModelData(agent));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(UPDATE_AGENT+userPk.getId()), new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public CrmAgentModel getAgentByPk(String agentPk)throws FDResourceException, FinderException, RemoteException {
		Response<CrmAgentModelData> response = new Response<CrmAgentModelData>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_AGENT_BY_PK+agentPk),  new TypeReference<Response<CrmAgentModelData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CrmManagerConverter.buildcrmAgentModel(response.getData());
	}

	@Override
	public CrmAgentList getAllAgents() throws FDResourceException,RemoteException {
		Response<CrmAgentListData> response = new Response<CrmAgentListData>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_ALL_AGENTS),  new TypeReference<Response<CrmAgentListData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CrmManagerConverter.buildcrmAgentList(response.getData());
	}

	@Override
	public List<CrmCaseModel> findCases(CrmCaseTemplate template)throws FDResourceException, RemoteException {
		Request<CrmCaseTemplateData> request = new Request<CrmCaseTemplateData>();
		String inputJson;
		Response<List<CrmCaseData>> response = null;
		try{
			request.setData(CrmManagerConverter.buildCrmCaseTemplateData(template));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(FIND_CASES), new TypeReference<Response<List<CrmCaseData>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			return CrmManagerConverter.buildCrmCaseModelList(response.getData());
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public CrmCaseModel getCaseByPk(String casePk) throws FDResourceException,RemoteException {
		Response<CrmCaseData> response = new Response<CrmCaseData>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_CASE_BY_PK+casePk),  new TypeReference<Response<CrmCaseData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CrmManagerConverter.buildCrmCaseModel(response.getData());
	}

	@Override
	public CrmAgentModel loginAgent(String username, String password)throws FDResourceException, CrmAuthenticationException,
			RemoteException {
		Request<LoginAgentData> request = new Request<LoginAgentData>();
		String inputJson;
		Response<CrmAgentModelData> response = null;
		try{
			request.setData(CrmManagerConverter.buildLoginAgentData(username,password));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(LOGIN_AGENT), new TypeReference<Response<CrmAgentModelData>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			return CrmManagerConverter.buildcrmAgentModel(response.getData());
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public boolean lockCase(PrimaryKey agentPK, PrimaryKey casePK)throws FDResourceException, RemoteException {
		Response<Boolean> response = new Response<Boolean>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOCK_CASE+agentPK.getId()+"/casePK/"+casePK.getId()),  new TypeReference<Response<Boolean>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public void unlockCase(PrimaryKey casePK) throws FDResourceException,RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(UNLOCK_CASE+casePK.getId()),  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public boolean closeAutoCase(PrimaryKey casePK) throws FDResourceException,RemoteException {
		Response<Boolean> response = new Response<Boolean>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(CLOSE_AUTO_CASE+casePK.getId()),  new TypeReference<Response<Boolean>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public PrimaryKey createCase(CrmCaseModel caseModel)throws FDResourceException, RemoteException,
			CrmAuthorizationException {
		Request<CrmCaseData> request = new Request<CrmCaseData>();
		String inputJson;
		Response<String> response = null;
		try{
			request.setData(CrmManagerConverter.buildCrmCaseData(caseModel));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CREATE_CASE), new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			return new PrimaryKey(response.getData());
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public PrimaryKey createSystemCase(CrmSystemCaseInfo caseInfo)throws FDResourceException, RemoteException {
		Request<CrmSystemCaseInfoData> request = new Request<CrmSystemCaseInfoData>();
		String inputJson;
		Response<String> response = null;
		try{
			request.setData(CrmManagerConverter.buildCrmSystemCaseInfoData(caseInfo));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CREATE_SYSTEM_CASE), new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			return new PrimaryKey(response.getData());
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public PrimaryKey createSystemCaseInSingleTx(CrmSystemCaseInfo caseInfo)throws FDResourceException, RemoteException {
		Request<CrmSystemCaseInfoData> request = new Request<CrmSystemCaseInfoData>();
		String inputJson;
		Response<String> response = null;
		try{
			request.setData(CrmManagerConverter.buildCrmSystemCaseInfoData(caseInfo));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CREATE_SYSTEM_CASE_IN_SINGLE_TXN), new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			return new PrimaryKey(response.getData());
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public void updateCase(CrmCaseInfo caseInfo, CrmCaseAction action,PrimaryKey agentPk) throws FDResourceException,
			CrmAuthorizationException, RemoteException {
		Request<UpdateCaseData> request = new Request<UpdateCaseData>();
		String inputJson;
		Response<String> response = null;
		try{
			request.setData(CrmManagerConverter.buildUpdateCaseData(caseInfo,action,agentPk));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(UPDATE_CASE), new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public List<CrmQueueInfo> getQueueOverview() throws FDResourceException,RemoteException {
		Response<List<CrmQueueInfoData>> response = new Response<List<CrmQueueInfoData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_QUEUE_OVERVIEW),  new TypeReference<Response<List<CrmQueueInfoData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CrmManagerConverter.buildCrmQueueInfoList(response.getData());
	}

	@Override
	public List<CrmAgentInfo> getCSROverview() throws FDResourceException,RemoteException {
		Response<List<CrmAgentInfoData>> response = new Response<List<CrmAgentInfoData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_CSR_OVERVIEW),  new TypeReference<Response<List<CrmAgentInfoData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CrmManagerConverter.buildCrmAgentInfoList(response.getData());
	}

	@Override
	public List<CrmCaseOperation> getOperations() throws FDResourceException,RemoteException {
		Response<List<CrmCaseOperationData>> response = new Response<List<CrmCaseOperationData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_OPERATIONS),  new TypeReference<Response<List<CrmCaseOperationData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CrmManagerConverter.buildCrmCaseOperationList(response.getData());
	}

	@Override
	public void downloadCases(PrimaryKey agentPK, String queue, String subject,
			int numberToDownload) throws FDResourceException, RemoteException {
		Request<DownloadCaseData> request = new Request<DownloadCaseData>();
		String inputJson;
		Response<String> response = null;
		try{
			request.setData(CrmManagerConverter.buildCrmDownloadCaseData(agentPK,queue,subject,numberToDownload));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(DOWNLOAD_CASES), new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public CrmStatus getSessionStatus(PrimaryKey agentPK)throws FDResourceException, RemoteException {
		Response<CrmStatusData> response = new Response<CrmStatusData>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_SESSION_STATUS+agentPK.getId()),  new TypeReference<Response<CrmStatusData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CrmManagerConverter.buildCrmStatus(response.getData());
	}

	@Override
	public void saveSessionStatus(CrmStatus status) throws FDResourceException,
			RemoteException {
		Request<CrmStatusData> request = new Request<CrmStatusData>();
		String inputJson;
		Response<String> response = null;
		try{
			request.setData(CrmManagerConverter.buildCrmStatusData(status));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(SAVE_SESSION_STATUS), new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	@Override
	public PrimaryKey createLateIssue(CrmLateIssueModel lateIssue)throws FDResourceException, RemoteException {
		Request<CrmLateIssueData> request = new Request<CrmLateIssueData>();
		String inputJson;
		Response<String> response = null;
		try{
			request.setData(CrmManagerConverter.buildCrmLateIssueData(lateIssue));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CREATE_LATE_ISSUE), new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return new PrimaryKey(response.getData());
	}

	@Override
	public void updateLateIssue(CrmLateIssueModel lateIssue)throws FinderException, FDResourceException, RemoteException {
		Request<CrmLateIssueData> request = new Request<CrmLateIssueData>();
		String inputJson;
		Response<String> response = null;
		try{
			request.setData(CrmManagerConverter.buildCrmLateIssueData(lateIssue));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(UPDATE_LATE_ISSUE), new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public CrmLateIssueModel getLateIssueById(String id)throws FinderException, FDResourceException, RemoteException {
		Response<CrmLateIssueData> response = new Response<CrmLateIssueData>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_LATE_ISSUE_BY_ID+id),  new TypeReference<Response<CrmLateIssueData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CrmManagerConverter.buildCrmLateIssueModel(response.getData());
	}

	@Override
	public Collection<CrmLateIssueModel> getLateIssuesByRouteAndDate(String route, Date date) throws FDResourceException,
			RemoteException {
		Response<Collection<CrmLateIssueData>> response = new Response<Collection<CrmLateIssueData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_LATE_ISSUE_BY_ROUTE_AND_DATE+route+"/date/"+date.getTime()),  new TypeReference<Response<Collection<CrmLateIssueData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CrmManagerConverter.buildCrmLateIssueModelCollection(response.getData());
	}

	@Override
	public Collection<CrmLateIssueModel> getLateIssuesByDate(Date date)throws FDResourceException, RemoteException {
		Response<Collection<CrmLateIssueData>> response = new Response<Collection<CrmLateIssueData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_LATE_ISSUE_BY_DATE+date.getTime()),  new TypeReference<Response<Collection<CrmLateIssueData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CrmManagerConverter.buildCrmLateIssueModelCollection(response.getData());
	}

	@Override
	public List<ErpTruckInfo> getTruckNumbersForDate(Date date)throws FDResourceException, RemoteException {
		Response<List<ErpTruckInfoData>> response = new Response<List<ErpTruckInfoData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_TRUCK_NUMBER_FOR_DATE+date.getTime()),  new TypeReference<Response<List<ErpTruckInfoData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CrmManagerConverter.buildErpTruckInfoList(response.getData());
	}

	@Override
	public CrmLateIssueModel getRecentLateIssueForOrder(String orderId)throws FDResourceException, RemoteException {
		Response<CrmLateIssueData> response = new Response<CrmLateIssueData>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_RECENT_LATE_ISSUE_FOR_ORDER+orderId),  new TypeReference<Response<CrmLateIssueData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CrmManagerConverter.buildCrmLateIssueModel(response.getData());
	}

	@Override
	public CrmCustomerHeaderInfo getCustomerHeaderInfo(String customerId)throws FDResourceException, RemoteException {
		Response<CrmCustomerHeaderInfoData> response = new Response<CrmCustomerHeaderInfoData>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_CUSTOMER_HEADER_INFO+customerId),  new TypeReference<Response<CrmCustomerHeaderInfoData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CrmManagerConverter.buildCrmCustomerHeaderInfo(response.getData());
	}

	@Override
	public void incrDeliveryCount(DeliveryPassModel model,CrmAgentModel agentmodel, int delta, String note,
			String reasonCode, String saleId) throws FDResourceException,
			CrmAuthorizationException, RemoteException {
		Request<IncrCountData> request = new Request<IncrCountData>();
		String inputJson;
		Response<String> response = null;
		try{
			request.setData(CrmManagerConverter.buildIncrCountData(model , agentmodel,delta,note,reasonCode,saleId,0));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(INCR_DELIVERY_COUNT), new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void incrExpirationPeriod(DeliveryPassModel model,CrmAgentModel agentmodel, int noOfDays, String note,
			String reasonCode, String saleId) throws FDResourceException,
			CrmAuthorizationException, RemoteException {
		Request<IncrCountData> request = new Request<IncrCountData>();
		String inputJson;
		Response<String> response = null;
		try{
			request.setData(CrmManagerConverter.buildIncrCountData(model , agentmodel,0,note,reasonCode,saleId , noOfDays));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(INCR_EXPIRATION_PERIOD), new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void cancelDeliveryPass(DeliveryPassModel model,CrmAgentModel agentmodel, String note, String reasonCode,
			String saleId) throws FDResourceException, RemoteException {
		Request<CrmDeliveryPassData> request = new Request<CrmDeliveryPassData>();
		String inputJson;
		Response<String> response = null;
		try{
			request.setData(CrmManagerConverter.buildCrmDlvPassData(model,agentmodel,note,reasonCode,saleId));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CANCEL_DELIVERY_PASS), new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void reactivateDeliveryPass(DeliveryPassModel model)throws FDResourceException, RemoteException {
		Request<DeliveryPassData> request = new Request<DeliveryPassData>();
		String inputJson;
		Response<String> response = null;
		try{
			request.setData(CrmManagerConverter.buildDlvPassData(model));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(REACTIVATE_DELIVERY_PASS), new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public String lookupAccount(String accountNum) throws FDResourceException,RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOOKUP_ACCOUNT+accountNum),  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public List<String> lookupOrders(String accountNum)throws FDResourceException, RemoteException {
		Response<List<String>> response = new Response<List<String>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOOKUP_ORDERS +accountNum),  new TypeReference<Response<List<String>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public void logViewAccount(CrmAgentModel agent, String customerID) throws FDResourceException, RemoteException {
		Request<CrmAgentModelData> request = new Request<CrmAgentModelData>();
		String inputJson;
		Response<String> response = null;
		try{
			request.setData(CrmManagerConverter.buildCrmAgentModelData(agent));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(LOG_VIEW_ACCOUNT+customerID), new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void logViewAccount(CrmAgentModel agent, String customerID,EnumAccountActivityType activityType, String maskedAcctNumber)
			throws FDResourceException, RemoteException {
		Request<ViewAccountData> request = new Request<ViewAccountData>();
		String inputJson;
		Response<String> response = null;
		try{
			request.setData(CrmManagerConverter.buildViewAccountData(agent,customerID,activityType,maskedAcctNumber));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(VIEW_ACCOUNT), new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public ErpCannedText createCannedText(ErpCannedText cannedText)throws FDResourceException, RemoteException {
		Request<CannedTextData> request = new Request<CannedTextData>();
		String inputJson;
		Response<CannedTextData> response = null;
		try{
			request.setData(CrmManagerConverter.buildCannedTextData(cannedText));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CREATE_CANNED_TEXT), new TypeReference<Response<CannedTextData>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return CrmManagerConverter.builderpCannedText(response.getData());
	}

	@Override
	public void updateCannedText(ErpCannedText cannedText, String id)
			throws FDResourceException, RemoteException {
		Request<CannedTextData> request = new Request<CannedTextData>();
		String inputJson;
		Response<String> response = null;
		try{
			request.setData(CrmManagerConverter.buildCannedTextData(cannedText));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(UPDATE_CANNED_TEXT +id), new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void deleteCannedText(String id) throws FDResourceException,RemoteException {
		Response<String> response = null;
		try{
			response = postDataTypeMap(null,getFdCommerceEndPoint(DELETE_CANNED_TEXT+id), new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}  catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public ErpCannedText getCannedTextById(String id)throws FDResourceException, RemoteException {
		Response<CannedTextData> response = new Response<CannedTextData>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_CANNED_TEXT_BY_ID+id),  new TypeReference<Response<CannedTextData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CrmManagerConverter.builderpCannedText(response.getData());
	}

	@Override
	public Collection<ErpCannedText> getAllCannedTextInCategory(EnumCannedTextCategory category) throws FDResourceException,
			RemoteException {
		Response<Collection<CannedTextData>> response = new Response<Collection<CannedTextData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_ALL_CANNEDTEXT_IN_CATEGORY+category.getName()),  new TypeReference<Response<Collection<CannedTextData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CrmManagerConverter.builderpCannedTextCollection(response.getData());
	}

	@Override
	public Collection<ErpCannedText> getAllCannedText()throws FDResourceException, RemoteException {
		Response<Collection<CannedTextData>> response = new Response<Collection<CannedTextData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_ALL_CANNEDTEXT),  new TypeReference<Response<Collection<CannedTextData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CrmManagerConverter.builderpCannedTextCollection(response.getData());
	}

	@Override
	public Map<String, Set<String>> getComplaintDeliveryIssueTypes(String erpCustomerId) throws FDResourceException, RemoteException {
		Response<Map<String, Set<String>>> response = new Response<Map<String, Set<String>>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_COMPLAINT_DLVISSUE_TYPES+erpCustomerId),  new TypeReference<Response<Map<String, Set<String>>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public String getLastDeliveredOrder(String erpCustomerId)throws FDResourceException, RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_LAST_DELIVERED_ORDER+erpCustomerId),  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public CrmAgentModel getAgentByLdapId(String agentLdapId)throws FDResourceException, CrmAuthenticationException,
			RemoteException {
		Response<CrmAgentModelData> response = new Response<CrmAgentModelData>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_AGENT_BY_LDAP_ID+agentLdapId),  new TypeReference<Response<CrmAgentModelData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CrmManagerConverter.buildcrmAgentModel(response.getData());
	}

	@Override
	public List<CrmAuthInfo> getAuthorizations(CrmAgentRole role,CrmAuthSearchCriteria filter) throws FDResourceException,
			RemoteException {
		Request<AuthorisationData> request = new Request<AuthorisationData>();
		String inputJson;
		Response<List<CrmAuthInfoData>> response = null;
		try{
			request.setData(CrmManagerConverter.buildAuthorizationData(role,filter));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_AUTHORIZATIONS), new TypeReference<Response<List<CrmAuthInfoData>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return CrmManagerConverter.buildCrmAuthInfoList(response.getData());
	}

	@Override
	public CustomerCreditModel getOrderForLateCredit(String saleId,String autoId) throws FDResourceException, RemoteException {
		Response<CustomerCreditModel> response = new Response<CustomerCreditModel>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_ORDER_FOR_LATE_CREDIT+saleId+"/autoId/"+autoId),  new TypeReference<Response<CustomerCreditModel>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public boolean isCaseCreatedForOrderLateDelivery(String saleId)throws FDResourceException, RemoteException {
		Response<Boolean> response = new Response<Boolean>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(CASE_CREATED_ORDER_LATE_DELIVERY+saleId),  new TypeReference<Response<Boolean>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public boolean isOrderCreditedForLateDelivery(String saleId)throws FDResourceException, RemoteException {
		Response<Boolean> response = new Response<Boolean>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ORDER_CREDITED_FOR_ORDER_LATE_DELIVERY+saleId),  new TypeReference<Response<Boolean>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public DeliveryPassModel getDeliveryPassInfoById(String dlvPassId)throws FDResourceException, RemoteException {
		Response<DeliveryPassData> response = new Response<DeliveryPassData>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_DELIVERY_PASS_INFO_BY_ID+dlvPassId),  new TypeReference<Response<DeliveryPassData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CrmManagerConverter.buildDlvPassModel(response.getData());
	}

	@Override
	public void updateAutoLateCredit(String autoId, String orderId)throws FDResourceException, RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(UPDATE_AUTO_LATE_CREDIT+orderId+"/autoId/"+autoId),  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public DeliveryPassModel getActiveDP(String custId)throws FDResourceException, RemoteException {
		Response<DeliveryPassData> response = new Response<DeliveryPassData>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_ACTIVE_DP+custId),  new TypeReference<Response<DeliveryPassData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return CrmManagerConverter.buildDlvPassModel(response.getData());
	}

	@Override
	public void updateLateCreditsRejected(String autoId, String agent)throws FDResourceException, RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(UPDATE_LATE_CREDITS_REJECTED+agent+"/autoId/"+agent),  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public boolean isDlvPassAlreadyExtended(String orderId, String customerId)
			throws FDResourceException, RemoteException {
		Response<Boolean> response = new Response<Boolean>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(IS_DELIVERY_PASS_EXTENDED+orderId+"/customerId/"+customerId),  new TypeReference<Response<Boolean>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public String getAllowedUsers() throws FDResourceException, RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_ALLOWED_USERS),  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public boolean isCRMRestrictionEnabled() throws FDResourceException,
			RemoteException {
		Response<Boolean> response = new Response<Boolean>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(CRM_RESTRICTION_ENABLED),  new TypeReference<Response<Boolean>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

}
