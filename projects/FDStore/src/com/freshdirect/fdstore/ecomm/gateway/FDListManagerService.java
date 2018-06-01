package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.common.context.StoreContext;
import com.freshdirect.ecomm.gateway.ExtTimeAbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.list.CopyCustomerListData;
import com.freshdirect.ecommerce.data.list.CustomerCreatedListData;
import com.freshdirect.ecommerce.data.list.CustomerListRequest;
import com.freshdirect.ecommerce.data.list.FDCustomerListData;
import com.freshdirect.ecommerce.data.list.FDCustomerListInfoData;
import com.freshdirect.ecommerce.data.list.FDCustomerListItemData;
import com.freshdirect.ecommerce.data.list.RenameCustomerListData;
import com.freshdirect.ecommerce.data.list.RenameListData;
import com.freshdirect.ecommerce.data.list.SaleStatisticsData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.ecomm.converter.ListConverter;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListExistsException;
import com.freshdirect.fdstore.lists.FDCustomerListInfo;
import com.freshdirect.fdstore.lists.FDCustomerProductList;
import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;
import com.freshdirect.fdstore.lists.FDCustomerShoppingList;
import com.freshdirect.fdstore.lists.FDQsProductListLineItem;
import com.freshdirect.fdstore.lists.FDStandingOrderList;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;

public class FDListManagerService extends ExtTimeAbstractEcommService implements FDListManagerServiceI{
	
	private final static Category LOGGER = LoggerFactory.getInstance(FDReferralManagerService.class);
	
	
	private static FDListManagerService INSTANCE;
	
	
	public static FDListManagerServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new FDListManagerService();

		return INSTANCE;
	}
	

	private static final String REMOVE_CUSTOMER_LIST_ITEM = "list/customerlist/remove/listId/";
	private static final String CREATE_CUSTOMER_CREATED_LIST = "list/customercreatedlist/create";
	private static final String DELETE_CUSTOMER_CREATED_LIST = "list/customercreatedlist/delete";
	private static final String DELETE_SHOPPING_LIST = "list/shoppinglist/delete/listId/";
	private static final String IS_CUSTOMER_LIST = "list/customer";
	private static final String GET_LIST_NAME = "list/name/erpCustomerId/";
	private static final String RENAME_CUSTOMER_CREATED_LIST = "list/customercreated/rename";
	private static final String RENAME_CUSTOMER_LIST = "list/customer/rename";
	private static final String RENAME_SHOPPING_LIST = "list/shopping/rename";
	private static final String GET_CUSTOMER_LIST = "list/customerlist";
	private static final String GET_CUSTOMER_LIST_BY_ID = "list/customerlistById";
	private static final String GET_CUSTOMER_CREATED_LISTS = "list/customercreatedlist";
	private static final String GET_CUSTOMER_CREATED_LIST_INFO = "list/customercreatedlistinfo";
	private static final String GET_STANDING_ORDER_LIST_INFO = "list/standingorderlist/erpcustomerId/";
	private static final String MODIFY_CUSTOMER_CREATED_LIST = "list/customercreatedlist/modify";
	private static final String COPY_CUSTOMER_CREATED_LIST = "list/customercreatedlist/copy";
	private static final String GET_ORDER_DETAILS = "list/orderdetails/erpCustomerId/";
	private static final String GET_STANDING_ORDER_LIST = "list/standingorder/erpCustomerId/";
	private static final String GET_CUSTOMER_CREATED_LIST = "list/customercreated/erpCustomerId/";
	private static final String STORE_CUSTOMER_LIST = "list/customerlist/store";
	private static final String GENEARATE_ITEM_EVER_ORDERED = "list/customerlist/erpCustomerId/";
	private static final String GET_QS_SPECIFIC_ITEM = "list/itemeverordered/erpCustomerId/";
	private static final String GET_EVERY_ITEM_EVER_ORDERED = "list/itemEverOrdered/erpCustomerId/";
	private static final String GET_QS_SPECIFIC_TOP_ITEM = "list/itemeverordered/topItems/erpCustomerId/";


	@Override
	public boolean removeCustomerListItem(FDIdentity identity, PrimaryKey id)throws FDResourceException, RemoteException {
		Response<Boolean> response = new Response<Boolean>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(REMOVE_CUSTOMER_LIST_ITEM+id.getId()+"/erpCustomerId/"+identity.getErpCustomerPK()),  new TypeReference<Response<Boolean>>(){});
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
	public String createCustomerCreatedList(FDIdentity identity,StoreContext storeContext, String listName)
			throws FDResourceException, RemoteException,FDCustomerListExistsException {
		Request<CustomerCreatedListData> request = new Request<CustomerCreatedListData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(ListConverter.buildCustomerCreatedData(identity,storeContext,listName));
			String inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(CREATE_CUSTOMER_CREATED_LIST),Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new FDCustomerListExistsException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public void deleteCustomerCreatedList(FDIdentity identity, String listName,StoreContext storeContext) throws FDResourceException,
			RemoteException {
		Request<CustomerCreatedListData> request = new Request<CustomerCreatedListData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(ListConverter.buildCustomerCreatedData(identity,storeContext,listName));
			String inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(DELETE_CUSTOMER_CREATED_LIST),Response.class);
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
	public void deleteShoppingList(String listId) throws FDResourceException,
			RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(DELETE_SHOPPING_LIST+listId),  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public boolean isCustomerList(FDIdentity identity,EnumCustomerListType type, String listName)throws FDResourceException, RemoteException {
		Request<CustomerListRequest> request = new Request<CustomerListRequest>();
		Response<Boolean> response = new Response<Boolean>();
		try{
			request.setData(ListConverter.buildCustomerListRequest(identity,type,listName,null));
			String inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(IS_CUSTOMER_LIST),Response.class);
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
	public String getListName(FDIdentity identity, String ccListId)throws FDResourceException, RemoteException {
		Response<String> response = new Response<String>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_LIST_NAME+identity.getErpCustomerPK()+"/ccListId/"+ccListId),  new TypeReference<Response<String>>(){});
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
	public void renameCustomerCreatedList(FDIdentity identity, String oldName,String newName) throws FDCustomerListExistsException,
			FDResourceException, RemoteException {
		Request<RenameListData> request = new Request<RenameListData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(ListConverter.buildRenameCustomerCreatedListData(identity,oldName,newName));
			String inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(RENAME_CUSTOMER_CREATED_LIST),Response.class);
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
	public void renameCustomerList(FDActionInfo info,EnumCustomerListType type, String oldName, String newName)
			throws FDCustomerListExistsException, FDResourceException,
			RemoteException {
		Request<RenameCustomerListData> request = new Request<RenameCustomerListData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(ListConverter.buildRenameCustomerListData(info,type,oldName,newName));
			String inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(RENAME_CUSTOMER_LIST),Response.class);
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
	public void renameShoppingList(String listId, String newName)throws FDResourceException, RemoteException {
		Request<RenameListData> request = new Request<RenameListData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(ListConverter.buildRenameShoppingListData(listId,newName));
			String inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(RENAME_SHOPPING_LIST),Response.class);
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
	public FDCustomerList getCustomerList(FDIdentity identity,EnumCustomerListType type, String listName)
			throws FDResourceException, RemoteException {
		Request<CustomerListRequest> request = new Request<CustomerListRequest>();
		Response<FDCustomerListData> response = new Response<FDCustomerListData>();
		FDCustomerList list = null;
		try{
			request.setData(ListConverter.buildCustomerListRequest( identity, type,  listName, null));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_CUSTOMER_LIST),new TypeReference<Response<FDCustomerListData>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			list = ListConverter.buildFDCustomerList(response.getData());
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return list;
	}

	@Override
	public FDCustomerList getCustomerListById(FDIdentity identity,EnumCustomerListType type, String listId)
			throws FDResourceException, RemoteException {
		Request<CustomerListRequest> request = new Request<CustomerListRequest>();
		Response<FDCustomerListData> response = new Response<FDCustomerListData>();
		try{
			request.setData(ListConverter.buildCustomerListRequest( identity, type,  null, listId));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_CUSTOMER_LIST_BY_ID),new TypeReference<Response<FDCustomerListData>>() {});
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
		return ListConverter.buildFDCustomerList(response.getData());
	}

	@Override
	public List<FDCustomerCreatedList> getCustomerCreatedLists(FDIdentity identity, StoreContext storeContext)
			throws FDResourceException, RemoteException {
		Request<CustomerCreatedListData> request = new Request<CustomerCreatedListData>();
		Response<List<FDCustomerListData>> response = new Response<List<FDCustomerListData>>();
		try{

			request.setData(ListConverter.buildCustomerCreatedData(identity,storeContext,null));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_CUSTOMER_CREATED_LISTS),new TypeReference<Response<List<FDCustomerListData>>>() {});
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
		
		List<FDCustomerCreatedList>  custList = ListConverter.buildFDCustomerCreatedList(response.getData());
		OrderLineUtil.cleanProductLists(custList);
		return custList;
	}

	@Override
	public List<FDCustomerListInfo> getCustomerCreatedListInfos(FDIdentity identity, StoreContext storeContext)
			throws FDResourceException, RemoteException {
		Request<CustomerCreatedListData> request = new Request<CustomerCreatedListData>();
		Response<List<FDCustomerListInfoData>> response = new Response<List<FDCustomerListInfoData>>();
		try{
			request.setData(ListConverter.buildCustomerCreatedData(identity,storeContext,null));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_CUSTOMER_CREATED_LIST_INFO),new TypeReference<Response<List<FDCustomerListInfoData>>>() {});
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
		return ListConverter.buildFDCustomerListInfo(response.getData());
	}

	@Override
	public List<FDCustomerListInfo> getStandingOrderListInfos(FDIdentity identity) throws FDResourceException, RemoteException {
		Response<List<FDCustomerListInfoData>> response = new Response<List<FDCustomerListInfoData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_STANDING_ORDER_LIST_INFO+identity.getErpCustomerPK()),  new TypeReference<Response<List<FDCustomerListInfoData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return ListConverter.buildFDCustomerListInfo(response.getData());
	}

	@Override
	public void modifyCustomerCreatedList(FDCustomerList list)throws FDResourceException, RemoteException {
		Request<FDCustomerListData> request = new Request<FDCustomerListData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(ListConverter.buildCustomerListData(list));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(MODIFY_CUSTOMER_CREATED_LIST),new TypeReference<Response<String>>() {});
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
	public void copyCustomerCreatedList(FDCustomerList oldList,FDCustomerList newList) throws FDResourceException,
			RemoteException, FDCustomerListExistsException {
		Request<CopyCustomerListData> request = new Request<CopyCustomerListData>();
		Response<String> response = new Response<String>();
		try{
			CopyCustomerListData customerList = new CopyCustomerListData();
			customerList.setNewList(ListConverter.buildCustomerListData(newList));
			customerList.setOldList(ListConverter.buildCustomerListData(oldList));
			request.setData(customerList);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(COPY_CUSTOMER_CREATED_LIST),new TypeReference<Response<String>>() {});
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
	public FDCustomerProductList getOrderDetails(String erpCustomerId,List<String> skus) throws FDResourceException, RemoteException {
		Request<List<String>> request = new Request<List<String>>();
		Response<FDCustomerListData> response = new Response<FDCustomerListData>();
		try{
			request.setData(skus);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(GET_ORDER_DETAILS+erpCustomerId),new TypeReference<Response<FDCustomerListData>>() {});
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
		return (FDCustomerProductList) ListConverter.buildFDCustomerList(response.getData());
	}

	@Override
	public FDStandingOrderList getStandingOrderList(FDIdentity identity,String soListId) throws FDResourceException, RemoteException {
		Response<FDCustomerListData> response = new Response<FDCustomerListData>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_STANDING_ORDER_LIST+identity.getErpCustomerPK()+"/soListId/"+soListId),  new TypeReference<Response<FDCustomerListData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return (FDStandingOrderList) ListConverter.buildFDCustomerList(response.getData());
	}

	@Override
	public FDCustomerCreatedList getCustomerCreatedList(FDIdentity identity,String ccListId) throws FDResourceException, RemoteException {
		Response<FDCustomerListData> response = new Response<FDCustomerListData>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_CUSTOMER_CREATED_LIST+identity.getErpCustomerPK()+"/ccListId/"+ccListId),  new TypeReference<Response<FDCustomerListData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		FDCustomerCreatedList finalList = (FDCustomerCreatedList) ListConverter.buildFDCustomerList(response.getData());
		if(finalList!=null){
			finalList.cleanList();
		}
		return  finalList;
	}

	@Override
	public FDCustomerList storeCustomerList(FDCustomerList list)throws FDResourceException, RemoteException {
		Response<FDCustomerListData> response = new Response<FDCustomerListData>();
		Request<FDCustomerListData> request = new Request<FDCustomerListData>();
		try{

			request.setData(ListConverter.buildCustomerListData(list));
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(STORE_CUSTOMER_LIST),new TypeReference<Response<FDCustomerListData>>() {});
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
		return  ListConverter.buildFDCustomerList(response.getData());
	}

	@Override
	public FDCustomerShoppingList generateEveryItemEverOrderedList(FDIdentity identity) throws FDResourceException, RemoteException {
		Response<FDCustomerListData> response = new Response<FDCustomerListData>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GENEARATE_ITEM_EVER_ORDERED+identity.getErpCustomerPK()),  new TypeReference<Response<FDCustomerListData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return  (FDCustomerShoppingList) ListConverter.buildFDCustomerList(response.getData());
	}

	@Override
	public List<FDProductSelectionI> getQsSpecificEveryItemEverOrderedList(
			FDIdentity identity, StoreContext storeContext)
			throws FDResourceException, RemoteException {
		Response<List<SaleStatisticsData>> response = new Response<List<SaleStatisticsData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_QS_SPECIFIC_ITEM+identity.getErpCustomerPK()+"/estoreId/"+storeContext.getEStoreId().getContentId()),  new TypeReference<Response<List<SaleStatisticsData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		List<FDProductSelectionI> listResults = new ArrayList();
		for(SaleStatisticsData item:response.getData()){
			try {
				listResults.add(((FDQsProductListLineItem)ListConverter.buildSaleStatisticsI(item)).convertToSelection());
			} catch (FDSkuNotFoundException e) {
				LOGGER.warn("Loaded an invalid sku - skipping", e);
				e.printStackTrace();
			}
		}
		
		return   listResults;
	}

	@Override
	public List<FDProductSelectionI> getQsSpecificEveryItemEverOrderedListTopItems(FDIdentity identity, StoreContext storeContext)
			throws FDResourceException, RemoteException {
		Response<List<SaleStatisticsData>> response = new Response<List<SaleStatisticsData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_QS_SPECIFIC_TOP_ITEM+identity.getErpCustomerPK()+"/estoreId/"+storeContext.getEStoreId().getContentId()),  new TypeReference<Response<List<SaleStatisticsData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		List<FDProductSelectionI> listResults = new ArrayList();
		for(SaleStatisticsData item:response.getData()){
			try {
				listResults.add(((FDQsProductListLineItem)ListConverter.buildSaleStatisticsI(item)).convertToSelection());
			} catch (FDSkuNotFoundException e) {
				LOGGER.warn("Loaded an invalid sku - skipping", e);
				e.printStackTrace();
			}
		}
		
		return   listResults;
	}

	@Override
	public List<FDProductSelectionI> getEveryItemEverOrdered(FDIdentity identity)throws FDResourceException, RemoteException {
		Response<List<FDCustomerListItemData>> response = new Response<List<FDCustomerListItemData>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_EVERY_ITEM_EVER_ORDERED+identity.getErpCustomerPK()),  new TypeReference<Response<List<FDCustomerListItemData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		List<FDProductSelectionI> listResults = new ArrayList();
		for(FDCustomerListItemData dataItem: response.getData()){
			FDCustomerProductListLineItem com = (FDCustomerProductListLineItem)ListConverter.buildFDCustomerListItem(dataItem);
			try {
				listResults.add(com.convertToSelection());
			} catch (FDSkuNotFoundException e) {
				LOGGER.warn("Loaded an invalid sku - skipping", e);
				e.printStackTrace();
			} 
		}
		
		return   listResults;
	}

}
