package com.freshdirect.ecomm.gateway;

import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.erp.inventory.ErpInventoryData;
import com.freshdirect.ecommerce.data.erp.material.ErpMaterialInfoModelData;
import com.freshdirect.ecommerce.data.erp.material.OverrideSkuAttrParam;
import com.freshdirect.ecommerce.data.erp.material.SkuPrefixParam;
import com.freshdirect.ecommerce.data.erp.model.ErpProductInfoModelData;
import com.freshdirect.erp.SkuAvailabilityHistory;
import com.freshdirect.erp.model.ErpInventoryModel;
import com.freshdirect.erp.model.ErpMaterialInfoModel;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.ErpProductInfoModelConvert;
import com.freshdirect.payment.service.ModelConverter;
//import com.freshdirect.content.attributes.FlatAttributeCollection;
//import com.freshdirect.fdlogistics.exception.FDLogisticsServiceException;


public class ErpInfoService extends ExtTimeAbstractEcommService implements ErpInfoServiceI {

	private final static Category LOGGER = LoggerFactory
			.getInstance(ErpInfoService.class);
	

	private static ErpInfoService INSTANCE;
	
	private static final String ERP_MATERIALINFO_VERSION = "erpinfo/materialbybatch";
	private static final String ERP_MATERIALINFO_SAPID = "erpinfo/materialsbysapid";
	private static final String ERP_MATERIALINFO_SKUCODE = "erpinfo/materialsbyskucode";
	private static final String ERP_MATERIALINFO_DESCRIPTION = "erpinfo/materialsbydescription";
	private static final String ERP_MATERIALINFO_CHARACTERISTIC = "erpinfo/materialsbyclassandcharacteristic";
	private static final String ERP_MATERIALSINFO_CLASS= "erpinfo/materialsbyclass";
	private static final String ERP_PRODUCTINFO_SKUCODE = "erpinfo/productbysku";
	private static final String ERP_PRODUCT_SKUS = "erpinfo/productsbyskus";
	private static final String ERP_PRODUCTINFO_SAPID = "erpinfo/productbysapid";
	private static final String ERP_SKUS_SAPID = "erpinfo/skusbysapid";
	private static final String ERP_PRODUCTINFO_DESCRIPTION = "erpinfo/productsbydescription";
	private static final String ERP_PRODUCTINFO_LIKESKU = "erpinfo/productslikesku";
	private static final String ERP_PRODUCTINFO_UPC = "erpinfo/productsbyupc";
	private static final String ERP_PRODUCTINFO_CUSTOMER_UPC = "erpinfo/productsbycustomerupc";
	private static final String ERP_PRODUCTINFO_LIKEUPC = "erpinfo/productslikeupc";
	private static final String ERP_INVENTORY_INFO = "erpinfo/inventoryinfo";
	private static final String ERP_LOAD_INVENTORY_INFO = "erpinfo/loadinventory";
	private static final String ERP_LOAD_MODIFIED_SKUS = "erpinfo/loadModifiedSkus";
	private static final String ERP_NEW_SKUS_DAYS = "erpinfo/newskucodes";
	private static final String ERP_SKUS_OLDNESS = "erpinfo/skuoldness";
	private static final String ERP_REINTRODUCED_SKUCODES = "erpinfo/reintroducedskucodes";
	private static final String ERP_OUTOFSTOCK_SKUCODES = "erpinfo/outofstockskucodes";
	private static final String ERP_SKUS_BY_DEAL = "erpinfo/skusbydeal";
	private static final String ERP_NEW_SKUS = "erpinfo/newskus";
	private static final String ERP_BACK_IN_STOCK_SKUS = "erpinfo/backinstockskus";
	private static final String ERP_OVERRIDDEN_NEW_SKUS = "erpinfo/overriddennewskus";
	private static final String ERP_OVERRIDDEN_BACK_IN_STOCK_SKUS = "erpinfo/overriddenbackinstockskus";
	private static final String ERP_SKU_AVAILABILITY_HISTORY = "erpinfo/skuavailabilityhistory";
	private static final String ERP_REFRESH_NEW_AND_BACK_VIEWS = "erpinfo/refreshnewandbackviews";
	private static final String ERP_OVERRIDDEN_NEWNESS = "erpinfo/overriddennewness";
	private static final String ERP_OVERRIDDEN_BACK_IN_STOCK = "erpinfo/overriddenbackinstock";
	

	public static ErpInfoServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ErpInfoService();

		return INSTANCE;
	}
	

	@Override
	public Collection findMaterialsByBatch(int batchNum) throws RemoteException {
		Response<Collection<ErpMaterialInfoModelData>> response = null;
		Collection<ErpMaterialInfoModel> erpModelList = new ArrayList();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_MATERIALINFO_VERSION)+"/"+batchNum, new TypeReference<Response<Collection<ErpMaterialInfoModelData>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			for(ErpMaterialInfoModelData data: response.getData()){
				ErpMaterialInfoModel model = ModelConverter.convertErpMaterialInfoDataToModel(data);
				erpModelList.add(model);
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpModelList;
	}
	@Override
	public Collection findMaterialsBySapId(String sapId) throws RemoteException {
		Response<Collection<ErpMaterialInfoModelData>> response = null;
		Collection<ErpMaterialInfoModel> erpModelList = new ArrayList();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_MATERIALINFO_SAPID)+"/"+sapId, new TypeReference<Response<Collection<ErpMaterialInfoModelData>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			for(ErpMaterialInfoModelData data: response.getData()){
				ErpMaterialInfoModel model = ModelConverter.convertErpMaterialInfoDataToModel(data);
				erpModelList.add(model);
			}
			
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpModelList;
	}
	@Override
	public Collection findMaterialsBySku(String skuCode) throws RemoteException {
		Response<Collection<ErpMaterialInfoModelData>> response = null;
		Collection<ErpMaterialInfoModel> erpModelList = new ArrayList();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_MATERIALINFO_SKUCODE)+"/"+skuCode, new TypeReference<Response<Collection<ErpMaterialInfoModelData>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			for(ErpMaterialInfoModelData data: response.getData()){
				ErpMaterialInfoModel model = ModelConverter.convertErpMaterialInfoDataToModel(data);
				erpModelList.add(model);
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpModelList;
	}
	@Override
	public Collection findMaterialsByDescription(String description) throws RemoteException {
		Response<Collection<ErpMaterialInfoModelData>> response = null;
		Collection<ErpMaterialInfoModel> erpModelList = new ArrayList();
		try {
			
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_MATERIALINFO_DESCRIPTION)+"/"+URLEncoder.encode(description), new TypeReference<Response<Collection<ErpMaterialInfoModelData>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			for(ErpMaterialInfoModelData data: response.getData()){
				ErpMaterialInfoModel model = ModelConverter.convertErpMaterialInfoDataToModel(data);
				erpModelList.add(model);
			}
			
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpModelList;
	}
	@Override
	public Collection findMaterialsByCharacteristic(String characteristic) throws RemoteException {
		Response<Collection> response = null;
		try {
			characteristic = characteristic.replace('_', '-');
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_MATERIALINFO_CHARACTERISTIC)+"/"+URLEncoder.encode(characteristic), new TypeReference<Response<Collection>>() {});
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
	public Collection findMaterialsByClass(String className) throws RemoteException {
		Response<Collection<ErpMaterialInfoModelData>> response = null;
		Collection<ErpMaterialInfoModel> erpModelList = new ArrayList();
		try {
			className = className.replace('_', '-');
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_MATERIALSINFO_CLASS)+"/"+className,  new TypeReference<Response<Collection<ErpMaterialInfoModelData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			for(ErpMaterialInfoModelData data: response.getData()){
				ErpMaterialInfoModel model = ModelConverter.convertErpMaterialInfoDataToModel(data);
				erpModelList.add(model);
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpModelList;
	}
	
	@Override
	public ErpProductInfoModel findProductBySku(String skuCode) throws RemoteException, ObjectNotFoundException {
		Response<ErpProductInfoModelData> response = null;
		ErpProductInfoModel erpProductInfoModel;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_PRODUCTINFO_SKUCODE)+"/"+skuCode,  new TypeReference<Response<ErpProductInfoModelData>>(){});
			if(response.getData() == null){
				throw new ObjectNotFoundException(response.getMessage());
			}
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			erpProductInfoModel = ErpProductInfoModelConvert.convertModelToData(response.getData());
			
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpProductInfoModel;
	}
	
	@Override
	public Collection<ErpProductInfoModel> findProductsBySapId(String sapId) throws RemoteException {
		Response<Collection<ErpProductInfoModelData>> response = null;
		Collection<ErpProductInfoModel> erpProductInfoModels;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_PRODUCTINFO_SAPID)+"/"+sapId,  new TypeReference<Response<Collection<ErpProductInfoModelData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			erpProductInfoModels = ErpProductInfoModelConvert.convertListDataToListModel(response.getData());
			
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpProductInfoModels;
	}
	@Override
	public Collection findProductsByDescription(String description) throws RemoteException {
		
		Response<Collection<ErpProductInfoModelData>> response = null;
		Collection<ErpProductInfoModel> erpProductInfoModels;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_PRODUCTINFO_DESCRIPTION)+"/"+URLEncoder.encode(description),  new TypeReference<Response<Collection<ErpProductInfoModelData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			erpProductInfoModels = ErpProductInfoModelConvert.convertListDataToListModel(response.getData());
			
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpProductInfoModels;
	}
	
	@Override
	public Collection findProductsLikeSku(String sku) throws RemoteException {
		
		Response<Collection<ErpProductInfoModelData>> response = null;
		Collection<ErpProductInfoModel> erpProductInfoModels;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_PRODUCTINFO_LIKESKU)+"/"+sku,  new TypeReference<Response<Collection<ErpProductInfoModelData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			erpProductInfoModels = ErpProductInfoModelConvert.convertListDataToListModel(response.getData());
			
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpProductInfoModels;
	}
	
	@Override
	public Collection<ErpProductInfoModel> findProductsByUPC(String upc) throws RemoteException {
		
		Response<Collection<ErpProductInfoModelData>> response = null;
		Collection<ErpProductInfoModel> erpProductInfoModels;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_PRODUCTINFO_UPC)+"/"+upc,  new TypeReference<Response<Collection<ErpProductInfoModelData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			erpProductInfoModels = ErpProductInfoModelConvert.convertListDataToListModel(response.getData());
			
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpProductInfoModels;
	}
	
	
	@Override
	public Collection<String> findProductsByCustomerUPC(String erpCustomerPK, String upc) throws RemoteException {
		
		Response<Collection<String>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_PRODUCTINFO_CUSTOMER_UPC)+"/"+erpCustomerPK+"/"+upc,  new TypeReference<Response<Collection>>(){});
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
	public Collection<ErpProductInfoModel> findProductsLikeUPC(String upc) throws RemoteException {
		
		Response<Collection<ErpProductInfoModelData>> response = null;
		Collection<ErpProductInfoModel> erpProductInfoModels;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_PRODUCTINFO_LIKEUPC)+"/"+upc,  new TypeReference<Response<Collection<ErpProductInfoModelData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			erpProductInfoModels = ErpProductInfoModelConvert.convertListDataToListModel(response.getData());
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpProductInfoModels;
	}
	//FdFactory
	@Override
	public ErpInventoryModel getInventoryInfo(String materialNo) throws RemoteException {
		
		Response<ErpInventoryData> response = null;
		ErpInventoryModel erpInventoryModel = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_INVENTORY_INFO)+"/"+materialNo,  new TypeReference<Response<ErpInventoryData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			erpInventoryModel = ModelConverter.convertErpInventoryDataToModel(response.getData());
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpInventoryModel;
	}
	//FdFactory
	@Override
	public Map<String,ErpInventoryModel> loadInventoryInfo(Date date) throws RemoteException {
		
		Response<Map<String,ErpInventoryData>> response = null;
		Map<String,ErpInventoryModel> erpInventoryModelMap = null;
		try {
			long date1 = 0;
			if(date!=null){
			date1 = date.getTime(); 
			}
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_LOAD_INVENTORY_INFO)+"/"+date1,  new TypeReference<Response<Map<String,ErpInventoryData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			erpInventoryModelMap = ModelConverter.convertErpInventoryDataMapToModelMap(response.getData());
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpInventoryModelMap;
	}
	//FdFactory

	// FdFactory
	@Override
	public Collection<String> findNewSkuCodes(int days) throws RemoteException {
		
		Response<Collection<String>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_NEW_SKUS_DAYS)+"/"+days,  new TypeReference<Response<Collection>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	// FdFactory
	@Override
	public Map<String, Integer> getSkusOldness() throws RemoteException {
		
		Response<Map<String, Integer>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_SKUS_OLDNESS),  new TypeReference<Response<Map<String, Integer>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	
	// FDFactory
	@Override
	public Collection<String> findReintroducedSkuCodes(int days) throws RemoteException {
		Response<Collection<String>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_REINTRODUCED_SKUCODES)+"/"+days,  new TypeReference<Response<Collection>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	// FDFactory
	@Override
	public Collection<String> findOutOfStockSkuCodes() throws RemoteException {
		Response<Collection<String>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_OUTOFSTOCK_SKUCODES),  new TypeReference<Response<Collection>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	// FDFactory
	@Override
	public Collection<ErpProductInfoModel> findProductsBySku(String[] skuCodes) throws RemoteException {
		Response<Collection<ErpProductInfoModelData>> response = null;
		Collection<ErpProductInfoModel> erpProductInfoModels;
		Request<String[]> request = new Request<String[]>();
		try {
			request.setData(skuCodes);
			String inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ERP_PRODUCT_SKUS),  new TypeReference<Response<Collection<ErpProductInfoModelData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			erpProductInfoModels = ErpProductInfoModelConvert.convertListDataToListModel(response.getData());
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return erpProductInfoModels;
	}
	
	@Override
	public void setOverriddenNewness(String sku,
			Map<String, String> salesAreaOverrides) throws RemoteException {
		Response<Void> response = null;
		Request<OverrideSkuAttrParam> request = new Request<OverrideSkuAttrParam>();
		try {
			OverrideSkuAttrParam overrideskuattrparam = new OverrideSkuAttrParam();
			overrideskuattrparam.setSalesAreaOverrides(salesAreaOverrides);
			overrideskuattrparam.setSku(sku);
			request.setData(overrideskuattrparam);
			String inputJson = buildRequest(request);
			response = this.postData(inputJson, getFdCommerceEndPoint(ERP_OVERRIDDEN_NEWNESS),  Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	
	
	@Override
	public void setOverriddenBackInStock(String sku,
			Map<String, String> salesAreaOverrides) throws RemoteException {
		Response<Void> response = null;
		Request<OverrideSkuAttrParam> request = new Request<OverrideSkuAttrParam>();
		try {
			OverrideSkuAttrParam overrideskuattrparam = new OverrideSkuAttrParam();
			overrideskuattrparam.setSku(sku);
			overrideskuattrparam.setSalesAreaOverrides(salesAreaOverrides);
			request.setData(overrideskuattrparam);
			String inputJson = buildRequest(request);
			response = this.postData(inputJson, getFdCommerceEndPoint(ERP_OVERRIDDEN_BACK_IN_STOCK),  Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	
	@Override
	public Map<String, String> getOverriddenNewness(String sku) throws RemoteException {
		Response<Map<String, String>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_OVERRIDDEN_NEWNESS)+"/"+sku,  new TypeReference<Response<Map<String, String>>>(){});
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
	public Map<String, String> getOverriddenBackInStock(String sku) throws RemoteException {
		Response<Map<String, String>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_OVERRIDDEN_BACK_IN_STOCK)+"/"+sku,  new TypeReference<Response<Map<String, String>>>(){});
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
	public Map<String, Map<String,Date>> getNewSkus() throws RemoteException {
		Response<Map<String, Map<String,Date>>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_NEW_SKUS),  new TypeReference<Response<Map<String, Map<String,Date>>>>(){});
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
	public Map<String, Map<String,Date>> getBackInStockSkus() throws RemoteException {
		Response<Map<String, Map<String,Date>>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_BACK_IN_STOCK_SKUS),  new TypeReference<Response<Map<String, Map<String,Date>>>>(){});
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
	public Map<String, Map<String,Date>> getOverriddenNewSkus() throws RemoteException {
		Response<Map<String, Map<String,Date>>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_OVERRIDDEN_NEW_SKUS),  new TypeReference<Response<Map<String, Map<String,Date>>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	// FdFactory
	@Override
	public Map<String, Map<String,Date>> getOverriddenBackInStockSkus() throws RemoteException {
		Response<Map<String, Map<String,Date>>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_OVERRIDDEN_BACK_IN_STOCK_SKUS),  new TypeReference<Response<Map<String, Map<String,Date>>>>(){});
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
	public List<SkuAvailabilityHistory> getSkuAvailabilityHistory(String skuCode) throws RemoteException {
		Response<List<SkuAvailabilityHistory>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_SKU_AVAILABILITY_HISTORY)+"/"+skuCode,  new TypeReference<Response<List<SkuAvailabilityHistory>>>(){});
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
	public void refreshNewAndBackViews() throws RemoteException {
		Response<Void> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_REFRESH_NEW_AND_BACK_VIEWS),  new TypeReference<Response<Void>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	@Override
	public Collection<String> findSKUsByDeal(double lowerLimit, double upperLimit,List skuPrefixes) throws RemoteException {
		Response<Collection> response = null;
		Request<SkuPrefixParam> request = new Request<SkuPrefixParam>();
		try {
			SkuPrefixParam overrideskuattrparam = new SkuPrefixParam();
			overrideskuattrparam.setLowerLimit(lowerLimit);
			overrideskuattrparam.setUpperLimit(upperLimit);
			overrideskuattrparam.setSkuPrefixes(skuPrefixes);
			request.setData(overrideskuattrparam);
			String inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(ERP_SKUS_BY_DEAL),  new TypeReference<Response<Collection<String>>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new FDRuntimeException(e, "Unable to process the request.");
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

}