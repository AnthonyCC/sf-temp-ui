package com.freshdirect.ecomm.gateway;

import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.erp.model.ErpProductInfoModelData;
import com.freshdirect.ecommerce.data.fdstore.FDProductData;
import com.freshdirect.ecommerce.data.fdstore.FDProductInfoData;
import com.freshdirect.ecommerce.data.fdstore.FDSkuData;
import com.freshdirect.erp.model.ErpProductInfoModel;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.ejb.FDProductHelper;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.ModelConverter;


public class FDFactoryService extends ExtTimeAbstractEcommService implements FDFactoryServiceI {

	private final static Category LOGGER = LoggerFactory
			.getInstance(FDFactoryService.class);
	

	private static FDFactoryService INSTANCE;
	
	private static final String ERP_SKUS_SAPID = "erpinfo/skusbysapid";
	private static final String ERP_LOAD_MODIFIED_SKUS = "erpinfo/loadModifiedSkus";
	private static final String FDFACTORY_FDPRODUCTINFO_SKUCODE = "productinfo/productinfobysku";
	private static final String FDFACTORY_FDPRODUCTINFO_SKUCODE_VERSION = "productinfo/productInfobyskuandversion";
	private static final String FDFACTORY_FDPRODUCTINFO_SKUCODES = "productinfo/productsinfobyskus";
	private static final String FDFACTORY_PRODUCTINFO_SKUCODES = "productinfo/productbyskuandversion";
	
	public static FDFactoryServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new FDFactoryService();

		return INSTANCE;
	}
	
	@Override
	public FDProductInfo getProductInfo(String skuCode) throws FDSkuNotFoundException, RemoteException {

		Response<ErpProductInfoModelData> response = null;
		ErpProductInfoModel model=null;
		FDProductInfo fdProductInfo=null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(FDFACTORY_FDPRODUCTINFO_SKUCODE)+"/"+skuCode.trim(),  new TypeReference<Response<ErpProductInfoModelData>>(){});
			if(response.getData() == null){
				throw new FDSkuNotFoundException(response.getMessage());
			}
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			model = ModelConverter.buildProdInfoMod(response.getData());
			
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		try {
			fdProductInfo= productHelper.getFDProductInfoNew(model);
		} catch (FDResourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw new FDResourceException(e);
		}//::FDX::
		return fdProductInfo;
	
	}
	
	
	
	@Override
	public FDProductInfo getProductInfo(String skuCode, int version) throws RemoteException,FDSkuNotFoundException {
		Response<ErpProductInfoModelData> response = null;
		FDProductInfo fdProductInfo;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(FDFACTORY_FDPRODUCTINFO_SKUCODE_VERSION)+"/"+(skuCode.trim())+"/"+version,  new TypeReference<Response<ErpProductInfoModelData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			if(response.getData()==null){
				throw new FDSkuNotFoundException(response.getMessage());
			}
			ErpProductInfoModel model = ModelConverter.buildProdInfoMod(response.getData());
			fdProductInfo = productHelper.getFDProductInfoNew(model);
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return fdProductInfo;
	}
	
	@Override
	public Collection getProductInfos(String[] skus) throws FDResourceException, RemoteException {
		Response<Collection<ErpProductInfoModelData>> response = null;
		Request<String[]> request = new Request<String[]>();
		Collection<FDProductInfo> fdProductInfos = new ArrayList<FDProductInfo>();
			try {
				request.setData(skus);
				String inputJson;
				inputJson = buildRequest(request);
				response = postDataTypeMap(inputJson,getFdCommerceEndPoint(FDFACTORY_FDPRODUCTINFO_SKUCODES),new TypeReference<Response<Collection<ErpProductInfoModelData>>>() {});
				if(!response.getResponseCode().equals("OK"))
					throw new FDResourceException(response.getMessage());
					
				for (ErpProductInfoModelData fdProductInfoData : response.getData()) {
					ErpProductInfoModel model = ModelConverter.buildProdInfoMod(fdProductInfoData);
					fdProductInfos.add(productHelper.getFDProductInfoNew(model));
				}
				
			} catch (FDEcommServiceException e) {
				
				throw new RemoteException(e.getMessage());
			} 
			return fdProductInfos;
	}
	
	private FDProductHelper productHelper = new FDProductHelper();
	@Override
	public FDProduct getProduct(String sku, int version) throws RemoteException,FDSkuNotFoundException {
		Response<ErpMaterialData> response = null;
		FDProduct fdProduct;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(FDFACTORY_PRODUCTINFO_SKUCODES)+"/"+sku.trim()+"/"+version,  new TypeReference<Response<ErpMaterialData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}else if(response.getData()==null){
				throw new FDSkuNotFoundException(response.getMessage());
			}
			
			ErpMaterialModel model = ModelConverter.convertErpMaterialDataToModel(response.getData());
			fdProduct = productHelper.getFDProduct(model);
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return fdProduct;
	}
	
	@Override
	public Collection<String> findSkusBySapId(String sapId) throws RemoteException, FDSkuNotFoundException {
		
		Response<Collection<String>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_SKUS_SAPID)+"/"+sapId,  new TypeReference<Response<Collection>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
			if(response.getData()==null){
				throw new FDSkuNotFoundException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	@Override
	public Set<String> getModifiedSkus(long lastModified) throws RemoteException {
		Response<Set<String> > response = new Response<Set<String> >(); 
		try {
			response = httpGetData(getFdCommerceEndPoint(ERP_LOAD_MODIFIED_SKUS)+"/"+lastModified, Response.class);
		
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());
		} catch (FDResourceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		
		}
		return new HashSet<String>(response.getData());
	}

}