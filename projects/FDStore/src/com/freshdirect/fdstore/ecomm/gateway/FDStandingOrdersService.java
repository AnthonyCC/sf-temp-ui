package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.customer.ErpActivityRecord;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.customer.ErpActivityRecordData;
import com.freshdirect.ecommerce.data.list.FDCustomerListData;
import com.freshdirect.ecommerce.data.standingorders.DeleteSOData;
import com.freshdirect.ecommerce.data.standingorders.FDStandingOrderAltDeliveryDateData;
import com.freshdirect.ecommerce.data.standingorders.FDStandingOrderData;
import com.freshdirect.ecommerce.data.standingorders.FDStandingOrderInfoListData;
import com.freshdirect.ecommerce.data.standingorders.ResultData;
import com.freshdirect.ecommerce.data.standingorders.StandingOrderErrorData;
import com.freshdirect.ecommerce.data.standingorders.StandingOrderModifyData;
import com.freshdirect.ecommerce.data.survey.FDIdentityData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.ecomm.converter.StandingOrderConverter;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrderAltDeliveryDate;
import com.freshdirect.fdstore.standingorders.FDStandingOrderFilterCriteria;
import com.freshdirect.fdstore.standingorders.FDStandingOrderInfoList;
import com.freshdirect.fdstore.standingorders.FDStandingOrderSkuResultInfo;
import com.freshdirect.fdstore.standingorders.SOResult.Result;
import com.freshdirect.fdstore.standingorders.UnavDetailsReportingBean;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDStandingOrdersService extends AbstractEcommService implements FDStandingOrdersServiceI{

	private final static Category LOGGER = LoggerFactory.getInstance(FDStandingOrdersService.class);

	private static final String CREATE_STANDING_ORDER = "fdstandingOrder/create";
	private static final String LOAD_ACTIVE_SO = "fdstandingOrder/active/load/isNewSo/";
	private static final String LOAD_CUSTOMER_SO = "fdstandingOrder/load/customerId/";
	private static final String LOAD = "fdstandingOrder/load/id/";
	private static final String DELETE = "fdstandingOrder/delete";
	private static final String SAVE = "fdstandingOrder/save";
	private static final String ASSIGN_SO_TO_ORDER = "fdstandingOrder/assign/order/saleId/";
	private static final String MARK_SALE_ALT_DATE_MOVEMENT = "fdstandingOrder/altDelivery/date/saleid/";
	private static final String LOG_ACTIVITY = "fdstandingOrder/logActivity";
	private static final String GET_ACTIVE_SO_CUST_INFO = "fdstandingOrder/active/customerInfo";
	private static final String CLEAR_SO_ERRORS = "fdstandingOrder/errors/clear";
	private static final String GET_FAILED_SO_CUST_INFO = "fdstandingOrder/failed/custInfo";
	private static final String GET_MACHANICAL_SO = "fdstandingOrder/mechanicalFailed/custInfo";
	private static final String GET_SO_ALT_DELIVERY_DATES = "fdstandingOrder/alternateDelivery/dates";
	private static final String GET_SO_DATES = "fdstandingOrder/alternateDelivery";

	private static final String ADD_SO_ALT_DLV_DATE = "fdstandingOrder/altDeliveryDate/add";
	private static final String UPDATE_SO_ALT_DLV_DATE = "fdstandingOrder/altDeliveryDate/update";
	private static final String DELETE_SO_ALT_DLV_DATE = "fdstandingOrder/altDeliveryDate/delete";
	private static final String LOCK = "fdstandingOrder/lock/lockId/";
	private static final String UNLOCK = "fdstandingOrder/unlock/unlockId/";
	private static final String GET_LOCK_ID = "fdstandingOrder/lockId/";
	private static final String CHECK_FOR_DUPLICATES_SO = "fdstandingOrder/duplicate/check";
	private static final String INSERT_INTO_COREMETRICS = "fdstandingOrder/coremetricsInfo/insert/fdUserId/";
	private static final String GET_COREMETRICS_INFO = "fdstandingOrder/coremetricsUserInfo/fdUserId/";
	private static final String GET_SO_ORDERS_GLOBAL = "fdstandingOrder/altDlvDates/global";
	private static final String GET_SO_ALT_DLV_DATE_BY_ID = "fdstandingOrder/altDlvdate/id/";
	private static final String DELETE_SO_ALT_DLV_DATE_BY_ID = "fdstandingOrder/altDlvdate/id/delete";
	private static final String ADD_SO_ORDERS_ALT_DLV_DATE = "fdstandingOrder/altDlvdate/add";
	private static final String CHECK_IF_EXISTS = "fdstandingOrder/altDlvdate/checkIfExists";
	private static final String IS_VALID_SO = "fdstandingOrder/soId/";

	private static final String REPLACE_SKU_CODE = "fdstandingOrder/userId/";
	private static final String VALIDATE_SKU_CODE = "fdstandingOrder/skuValidate/existingSku/";
	private static final String PERSIST_UNAV_TO_DB = "fdstandingOrder/unavlDetails/save";
	private static final String GET_DET_FOR_REPORT_GEN = "fdstandingOrder/report/details";
	private static final String GET_SO_DETAILS = "fdstandingOrder/details";
	private static final String GET_VALID_SO_DETAILS = "fdstandingOrder/valid";
	private static final String ACTIVATE_STANDING_ORDER = "fdstandingOrder/isActive";
	private static final String CHECK_IF_CUST_HAS_SO = "fdstandingOrder/customer/check";
	private static final String UPDATE_DEFAULT_SO = "fdstandingOrder/default/update/listId/";
	private static final String LOAD_NEW_SO = "fdstandingOrder/new/load";
	private static final String LOAD_SO_CRON = "fdstandingOrder/cron/id/";
	private static final String TURN_OFF_REMINDER = "fdstandingOrder/id/";
	private static final String UPDATE_SO_CART_OVERLAY = "fdstandingOrder/cartOverlay/update/";
	private static final String UPDATE_NEW_SO_FEATURES = "fdstandingOrder/newFaetures/update/";
	private static final String UPDATE_DEACTIVATED_SO = "fdstandingOrder/deactivated/soId/";
	private static final String DELETE_ACTIVATED_SO = "fdstandingOrder/activated/delete";
	private static final String LOAD_ACTIVE_SO_FOR_WEEK = "fdstandingOrder/new/";
	
	
	
	private static FDStandingOrdersService INSTANCE;
	
	
	public static FDStandingOrdersServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new FDStandingOrdersService();

		return INSTANCE;
	}
	@Override
	public FDStandingOrder createStandingOrder(FDCustomerList list)throws FDResourceException, RemoteException {
		Request<FDCustomerListData> request = new Request<FDCustomerListData>();
		Response<FDStandingOrderData> response = new Response<FDStandingOrderData>();
		try{
			request.setData(StandingOrderConverter.buildFDCustomerListData(list));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CREATE_STANDING_ORDER),new TypeReference<Response<FDStandingOrderData>>() {});
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
		return StandingOrderConverter.buildStandingOrder(response.getData());
	}

	@Override
	public Collection<FDStandingOrder> loadActiveStandingOrders(boolean isNewSo)
			throws FDResourceException, RemoteException {
		Response<Collection<FDStandingOrderData>> response = new Response<Collection<FDStandingOrderData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_ACTIVE_SO +isNewSo),  new TypeReference<Response<Collection<FDStandingOrderData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return StandingOrderConverter.buildStandingOrderList( response.getData());
	}

	@Override
	public Collection<FDStandingOrder> loadCustomerStandingOrders(
			FDIdentity identity) throws FDResourceException,
			FDInvalidConfigurationException, RemoteException {
		Response<Collection<FDStandingOrderData>> response = new Response<Collection<FDStandingOrderData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_CUSTOMER_SO + identity.getErpCustomerPK()),  new TypeReference<Response<Collection<FDStandingOrderData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return StandingOrderConverter.buildStandingOrderList( response.getData());
	}

	@Override
	public FDStandingOrder load(PrimaryKey pk) throws FDResourceException,
			RemoteException {
		Response<FDStandingOrderData> response = new Response<FDStandingOrderData>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOAD +pk.getId()),  new TypeReference<Response<FDStandingOrderData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return StandingOrderConverter.buildStandingOrder(response.getData());
	}

	@Override
	public void delete(FDActionInfo info, FDStandingOrder so)
			throws FDResourceException, RemoteException {
		Request<StandingOrderModifyData> request = new Request<StandingOrderModifyData>();
		try{
			request.setData(StandingOrderConverter.buildStandingOrdermodifyData(info,so,null));
			String inputJson = buildRequest(request);
			postDataTypeMap(inputJson,getFdCommerceEndPoint(DELETE),new TypeReference<Response<String>>() {});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public String save(FDActionInfo info, FDStandingOrder so, String saleId)
			throws FDResourceException, RemoteException {
		Request<StandingOrderModifyData> request = new Request<StandingOrderModifyData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(StandingOrderConverter.buildStandingOrdermodifyData(info,so,saleId));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(SAVE),new TypeReference<Response<String>>() {});
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
	public void assignStandingOrderToOrder(PrimaryKey salePK,
			PrimaryKey standingOrderPK) throws FDResourceException,
			RemoteException {
		try {
			 this.httpGetDataTypeMap(getFdCommerceEndPoint(ASSIGN_SO_TO_ORDER+salePK.getId()+"/soId/"+standingOrderPK.getId()),  new TypeReference<Response<String>>(){});
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void markSaleAltDeliveryDateMovement(PrimaryKey salePK)
			throws FDResourceException, RemoteException {
		try {
			 this.httpGetDataTypeMap(getFdCommerceEndPoint(MARK_SALE_ALT_DATE_MOVEMENT +salePK.getId()),  new TypeReference<Response<String>>(){});
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void logActivity(ErpActivityRecord record)
			throws FDResourceException, RemoteException {
		Request<ErpActivityRecordData> request = new Request<ErpActivityRecordData>();
		try{
			request.setData(StandingOrderConverter.buildErpActivityRecord(record));
			String inputJson = buildRequest(request);
			postDataTypeMap(inputJson,getFdCommerceEndPoint(LOG_ACTIVITY),new TypeReference<Response<String>>() {});
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public FDStandingOrderInfoList getActiveStandingOrdersCustInfo(
			FDStandingOrderFilterCriteria filter) throws FDResourceException,
			RemoteException {
		Request<FDStandingOrderFilterCriteria> request = new Request<FDStandingOrderFilterCriteria>();
		Response<FDStandingOrderInfoListData> response = new Response<FDStandingOrderInfoListData>();
		try{
			request.setData(filter);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_ACTIVE_SO_CUST_INFO),new TypeReference<Response<FDStandingOrderInfoListData>>() {});
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
		return StandingOrderConverter.buildFdStandingOrderInfoList(response.getData());
	}

	@Override
	public void clearStandingOrderErrors(String[] soIDs, String agentId)
			throws FDResourceException, RemoteException {
		Request<StandingOrderErrorData> request = new Request<StandingOrderErrorData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(StandingOrderConverter.buildStandingOrderErrorData(soIDs,agentId));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CLEAR_SO_ERRORS),new TypeReference<Response<String>>() {});
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
	public FDStandingOrderInfoList getFailedStandingOrdersCustInfo()
			throws FDResourceException, RemoteException {
		Response<FDStandingOrderInfoListData> response = new Response<FDStandingOrderInfoListData>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_FAILED_SO_CUST_INFO),  new TypeReference<Response<FDStandingOrderInfoListData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return StandingOrderConverter.buildFdStandingOrderInfoList(response.getData());
	}

	@Override
	public FDStandingOrderInfoList getMechanicalFailedStandingOrdersCustInfo()
			throws FDResourceException, RemoteException {
		Response<FDStandingOrderInfoListData> response = new Response<FDStandingOrderInfoListData>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_MACHANICAL_SO),  new TypeReference<Response<FDStandingOrderInfoListData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return StandingOrderConverter.buildFdStandingOrderInfoList(response.getData());
	}

	@Override
	public Map<Date, Date> getStandingOrdersAlternateDeliveryDates()
			throws FDResourceException, RemoteException {
		Response<Map<Long, Long>> response = new Response<Map<Long, Long>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_SO_ALT_DELIVERY_DATES),  new TypeReference<Response<Map<Long, Long>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return StandingOrderConverter.buildMapOfDate(response.getData());
	}

	@Override
	public List<FDStandingOrderAltDeliveryDate> getStandingOrderAltDeliveryDates()
			throws FDResourceException, RemoteException {
		Response<List<FDStandingOrderAltDeliveryDateData>> response = new Response<List<FDStandingOrderAltDeliveryDateData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_SO_DATES),  new TypeReference<Response<List<FDStandingOrderAltDeliveryDateData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return StandingOrderConverter.buildfdstandinOrderAltDlvDateList(response.getData());
	}

	@Override
	public void addStandingOrderAltDeliveryDate(FDStandingOrderAltDeliveryDate altDeliveryDate)
			throws FDResourceException, RemoteException {
		Request<FDStandingOrderAltDeliveryDateData> request = new Request<FDStandingOrderAltDeliveryDateData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(StandingOrderConverter.buildStandingOrderAltDlvDateData(altDeliveryDate));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(ADD_SO_ALT_DLV_DATE),new TypeReference<Response<String>>() {});
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
	public void updateStandingOrderAltDeliveryDate(
			FDStandingOrderAltDeliveryDate altDeliveryDate)
			throws FDResourceException, RemoteException {
		Request<FDStandingOrderAltDeliveryDateData> request = new Request<FDStandingOrderAltDeliveryDateData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(StandingOrderConverter.buildStandingOrderAltDlvDateData(altDeliveryDate));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(UPDATE_SO_ALT_DLV_DATE),new TypeReference<Response<String>>() {});
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
	public void deleteStandingOrderAltDeliveryDate(
			FDStandingOrderAltDeliveryDate altDeliveryDate)
			throws FDResourceException, RemoteException {
		Request<FDStandingOrderAltDeliveryDateData> request = new Request<FDStandingOrderAltDeliveryDateData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(StandingOrderConverter.buildStandingOrderAltDlvDateData(altDeliveryDate));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(DELETE_SO_ALT_DLV_DATE),new TypeReference<Response<String>>() {});
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
	public boolean lock(FDStandingOrder so, String lockId)
			throws FDResourceException, RemoteException {
		Request<FDStandingOrderData> request = new Request<FDStandingOrderData>();
		Response<Boolean> response = new Response<Boolean>();
		try{
			request.setData(StandingOrderConverter.buildStandingOrderData(so));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(LOCK+lockId),new TypeReference<Response<Boolean>>() {});
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
	public boolean unlock(FDStandingOrder so, String lockId)throws FDResourceException, RemoteException {
		Request<FDStandingOrderData> request = new Request<FDStandingOrderData>();
		Response<Boolean> response = new Response<Boolean>();
		try{
			request.setData(StandingOrderConverter.buildStandingOrderData(so));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(UNLOCK +lockId),new TypeReference<Response<Boolean>>() {});
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
	public String getLockId(String soId) throws FDResourceException,
			RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_LOCK_ID +soId),  new TypeReference<Response<String>>(){});
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
	public void checkForDuplicateSOInstances(FDIdentity identity)
			throws FDResourceException, FDInvalidConfigurationException,
			RemoteException {
		Request<FDIdentityData> request = new Request<FDIdentityData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(StandingOrderConverter.buildFdIdentityData(identity));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CHECK_FOR_DUPLICATES_SO),new TypeReference<Response<String>>() {});
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
	public void insertIntoCoremetricsUserinfo(FDUserI fdUser, int flag)
			throws FDResourceException, RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(INSERT_INTO_COREMETRICS +fdUser.getPrimaryKey()+"/flag/"+flag),  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public boolean getCoremetricsUserinfo(FDUserI fdUser)throws FDResourceException, RemoteException {
		Response<Boolean> response = new Response<Boolean>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_COREMETRICS_INFO +fdUser.getPrimaryKey()),  new TypeReference<Response<Boolean>>(){});
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
	public Map<Date, List<FDStandingOrderAltDeliveryDate>> getStandingOrdersGlobalAlternateDeliveryDates()
			throws FDResourceException, RemoteException {
		Response<Map<Long, List<FDStandingOrderAltDeliveryDateData>>> response = new Response<Map<Long, List<FDStandingOrderAltDeliveryDateData>>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_SO_ORDERS_GLOBAL),  new TypeReference<Response<Map<Long, List<FDStandingOrderAltDeliveryDateData>>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		 Map<Date, List<FDStandingOrderAltDeliveryDate>> soResposne =  new HashMap<Date, List<FDStandingOrderAltDeliveryDate>>();
		 for (Long key : response.getData().keySet()) {
			 soResposne.put(new Date(key), StandingOrderConverter.buildfdstandinOrderAltDlvDateList(response.getData().get(key)));
			
		}
		return soResposne;
	}

	@Override
	public FDStandingOrderAltDeliveryDate getStandingOrderAltDeliveryDateById(
			String id) throws FDResourceException, RemoteException {
		Response<FDStandingOrderAltDeliveryDateData> response = new Response<FDStandingOrderAltDeliveryDateData>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_SO_ALT_DLV_DATE_BY_ID+id),  new TypeReference<Response<FDStandingOrderAltDeliveryDateData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return StandingOrderConverter.buildStandingOrderAltdeliveryDate(response.getData());
	}

	@Override
	public void deleteStandingOrderAltDeliveryDateById(String[] altIds)throws FDResourceException, RemoteException {
		Request<String[]> request = new Request<String[]>();
		Response<String> response = new Response<String>();
		try{
			request.setData(altIds);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(DELETE_SO_ALT_DLV_DATE_BY_ID),new TypeReference<Response<String>>() {});
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
	public void addStandingOrderAltDeliveryDates(List<FDStandingOrderAltDeliveryDate> altDeliveryDates)
			throws FDResourceException, RemoteException {
		Request<List<FDStandingOrderAltDeliveryDateData>> request = new Request<List<FDStandingOrderAltDeliveryDateData>>();
		Response<String> response = new Response<String>();
		try{
			request.setData(StandingOrderConverter.buildfdstandinOrderAltDlvDateDataList(altDeliveryDates));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(ADD_SO_ORDERS_ALT_DLV_DATE),new TypeReference<Response<String>>() {});
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
	public boolean checkIfAlreadyExists(FDStandingOrderAltDeliveryDate altDate)
			throws FDResourceException, RemoteException {
		Request<FDStandingOrderAltDeliveryDateData> request = new Request<FDStandingOrderAltDeliveryDateData>();
		Response<Boolean> response = new Response<Boolean>();
		try{
			request.setData(StandingOrderConverter.buildStandingOrderAltDlvDateData(altDate));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CHECK_IF_EXISTS),new TypeReference<Response<Boolean>>() {});
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
	public boolean isValidSoId(String soId) throws FDResourceException,
			RemoteException {
		Response<Boolean> response = new Response<Boolean>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(IS_VALID_SO+soId+"/isValid"),  new TypeReference<Response<Boolean>>(){});
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
	public FDStandingOrderSkuResultInfo replaceSkuCode(String existingSku,
			String replacementSku,String userId) throws FDResourceException, RemoteException {
		Response<FDStandingOrderSkuResultInfo> response = new Response<FDStandingOrderSkuResultInfo>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(REPLACE_SKU_CODE + userId +"/skuReplace/existingSku/"+existingSku+"/replacementSku/"+replacementSku),  new TypeReference<Response<FDStandingOrderSkuResultInfo>>(){});
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
	public FDStandingOrderSkuResultInfo validateSkuCode(String existingSku,
			String replacementSku) throws FDResourceException, RemoteException {
		Response<FDStandingOrderSkuResultInfo> response = new Response<FDStandingOrderSkuResultInfo>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(VALIDATE_SKU_CODE+existingSku+"/replacementSku/"+replacementSku),  new TypeReference<Response<FDStandingOrderSkuResultInfo>>(){});
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
	public void persistUnavailableDetailsToDB(List<Result> resultsList) throws FDResourceException, RemoteException {
		Request<List<ResultData>> request = new Request<List<ResultData>>();
		Response<String> response = new Response<String>();
		try{
			request.setData(StandingOrderConverter.buildResultDataList(resultsList));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(PERSIST_UNAV_TO_DB),new TypeReference<Response<String>>() {});
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
	public UnavDetailsReportingBean getDetailsForReportGeneration()
			throws FDResourceException, RemoteException {
		Response<UnavDetailsReportingBean> response = new Response<UnavDetailsReportingBean>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_DET_FOR_REPORT_GEN),  new TypeReference<Response<UnavDetailsReportingBean>>(){});
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
	public Collection<FDStandingOrder> getStandingOrderDetails(
			Collection<FDStandingOrder> fdStandingOrders)
			throws FDResourceException, FDInvalidConfigurationException,
			RemoteException {
		Request<Collection<FDStandingOrderData>> request = new Request<Collection<FDStandingOrderData>>();
		Response<Collection<FDStandingOrderData>> response = new Response<Collection<FDStandingOrderData>>();
		try{
			request.setData(StandingOrderConverter.buildStandingOrderDataList(fdStandingOrders));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_SO_DETAILS),new TypeReference<Response<Collection<FDStandingOrderData>>>() {});
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
		return StandingOrderConverter.buildStandingOrderList(response.getData());
	}

	@Override
	public Collection<FDStandingOrder> getValidStandingOrder(FDIdentity identity)
			throws FDResourceException, RemoteException,
			FDInvalidConfigurationException {
		Request<FDIdentityData> request = new Request<FDIdentityData>();
		Response<Collection<FDStandingOrderData>> response = new Response<Collection<FDStandingOrderData>>();
		try{
			request.setData(StandingOrderConverter.buildFdIdentityData(identity));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_VALID_SO_DETAILS),new TypeReference<Response<Collection<FDStandingOrderData>>>() {});
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
		return StandingOrderConverter.buildStandingOrderList(response.getData());
	}

	@Override
	public boolean activateStandingOrder(FDStandingOrder so)
			throws FDResourceException, RemoteException {
		Request<FDStandingOrderData> request = new Request<FDStandingOrderData>();
		Response<Boolean> response = new Response<Boolean>();
		try{
			request.setData(StandingOrderConverter.buildStandingOrderData(so));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(ACTIVATE_STANDING_ORDER),new TypeReference<Response<Boolean>>() {});
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
	public boolean checkIfCustomerHasStandingOrder(FDIdentity identity)
			throws FDResourceException, RemoteException {
		Request<FDIdentityData> request = new Request<FDIdentityData>();
		Response<Boolean> response = new Response<Boolean>();
		try{
			request.setData(StandingOrderConverter.buildFdIdentityData(identity));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CHECK_IF_CUST_HAS_SO),new TypeReference<Response<Boolean>>() {});
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
	public boolean updateDefaultStandingOrder(String listId,FDIdentity userIdentity) throws FDResourceException,
			RemoteException {
		Request<FDIdentityData> request = new Request<FDIdentityData>();
		Response<Boolean> response = new Response<Boolean>();
		try{
			request.setData(StandingOrderConverter.buildFdIdentityData(userIdentity));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(UPDATE_DEFAULT_SO +listId),new TypeReference<Response<Boolean>>() {});
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
	public Collection<FDStandingOrder> loadCustomerNewStandingOrders(
			FDIdentity identity) throws FDResourceException,
			FDInvalidConfigurationException, RemoteException {
		Request<FDIdentityData> request = new Request<FDIdentityData>();
		Response<Collection<FDStandingOrderData>> response = new Response<Collection<FDStandingOrderData>>();
		try{
			request.setData(StandingOrderConverter.buildFdIdentityData(identity));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(LOAD_NEW_SO),new TypeReference<Response<Collection<FDStandingOrderData>>>() {});
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
		return StandingOrderConverter.buildStandingOrderList(response.getData());
	}

	@Override
	public FDStandingOrder loadSOCron(PrimaryKey pk)
			throws FDResourceException, RemoteException {
		Response<FDStandingOrderData> response = new Response<FDStandingOrderData>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_SO_CRON +pk.getId()),  new TypeReference<Response<FDStandingOrderData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return StandingOrderConverter.buildStandingOrder(response.getData());
	}

	@Override
	public void turnOffReminderOverLayNewSo(String standingOrderId)
			throws FDResourceException, RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(TURN_OFF_REMINDER +standingOrderId+"/reminderOverlay/turnOff"),  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void updateSoCartOverlayFirstTimePreferences(String customerId)
			throws FDResourceException, RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(UPDATE_SO_CART_OVERLAY +customerId),  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void updateNewSoFeaturePreferences(String customerId)
			throws FDResourceException, RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(UPDATE_NEW_SO_FEATURES+customerId),  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void updateDeActivatedSOError(String soId)
			throws FDResourceException, RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(UPDATE_DEACTIVATED_SO+soId+"/update"),  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public void deleteActivatedSO(FDActionInfo info, FDStandingOrder so,String deleteDate) throws FDResourceException, RemoteException {
		Request<DeleteSOData> request = new Request<DeleteSOData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(StandingOrderConverter.buildDeleteSOData(info,so , deleteDate));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(DELETE_ACTIVATED_SO),new TypeReference<Response<String>>() {});
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
	public Collection<FDStandingOrder> loadActiveStandingOrdersForAWeek(boolean isNewSo) throws FDResourceException, RemoteException {
		Response<Collection<FDStandingOrderData>> response = new Response<Collection<FDStandingOrderData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(LOAD_ACTIVE_SO_FOR_WEEK +isNewSo+"/active/week"),  new TypeReference<Response<Collection<FDStandingOrderData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return StandingOrderConverter.buildStandingOrderList(response.getData());
	}

}