package com.freshdirect.ecomm.gateway;

import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
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
	private static final String FDFACTORY_PRODUCTBY_SKUCODES = "productinfo/productbyskuandversion";
	private static final String FDFACTORY_PRODUCTINFO_FDSKUCODES = "productinfo/productbyFdskus";
	private static final String FDFACTORY_PRODUCTINFO_BB_SKUCODES_TEMP = "productinfo/productbyFdskusTemp";
	
	
	public static FDFactoryServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new FDFactoryService();

		return INSTANCE;
	}
	
	@Override
	public FDProductInfo getProductInfo(String sku) throws FDSkuNotFoundException, RemoteException {

		Response<ErpProductInfoModelData> response = null;
		ErpProductInfoModel model=null;
		FDProductInfo fdProductInfo=null;
		String skuCode = sku.replaceAll("[^\\x00-\\x7F]","");
		skuCode = skuCode.trim();
		
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(FDFACTORY_FDPRODUCTINFO_SKUCODE)+"/"+skuCode,  new TypeReference<Response<ErpProductInfoModelData>>(){});
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
		String sku = skuCode.replaceAll("[^\\x00-\\x7F]","");
		sku = skuCode.trim();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(FDFACTORY_FDPRODUCTINFO_SKUCODE_VERSION)+"/"+URLEncoder.encode(sku)+"/"+version,  new TypeReference<Response<ErpProductInfoModelData>>(){});
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
	
	private static List<List<String>> splitList(List<String> list, int maxListSize) {
        List<List<String>> splittedList = new ArrayList<List<String>>();
        int itemsRemaining = list.size();
        int start = 0;

        while (itemsRemaining != 0) {
        	int end = itemsRemaining >= maxListSize ? (start + maxListSize) : (start + itemsRemaining);

            splittedList.add(list.subList(start, end));

            int sizeOfFinalList = end - start;
            itemsRemaining = itemsRemaining - sizeOfFinalList;
            start = start + sizeOfFinalList;
        }

        return splittedList;
    }
	
	@Override
	public Collection getProductInfos(String[] skus) throws FDResourceException, RemoteException {
		Response<Collection<FDProductInfoData>> response = null;
		Request<String[]> request = new Request<String[]>();
		Collection<FDProductInfo> fdProductInfos = new ArrayList<FDProductInfo>();
		
		
		List<List<String>> buckets = splitList(Arrays.asList(skus), 500);
		for(List<String> bucket: buckets) {
			try {
				request.setData((String[]) bucket.toArray(new String[0]));
				String inputJson;
				inputJson = buildRequest(request);
				response = postDataTypeMap(inputJson,getFdCommerceEndPoint(FDFACTORY_FDPRODUCTINFO_SKUCODES),new TypeReference<Response<Collection<FDProductInfoData>>>() {});
				if(!response.getResponseCode().equals("OK"))
					throw new FDResourceException(response.getMessage());
				
				if(response.getData()!=null ){	
					for (FDProductInfoData fdProductInfoData : response.getData()) {
						 FDProductInfo model = ModelConverter.fdProductInfoDataToModel(fdProductInfoData);
						fdProductInfos.add(model);
					}
				}
				
			} catch (FDEcommServiceException e) {
				
				throw new RemoteException(e.getMessage());
			} 
		}
			return fdProductInfos;
	}
	
	private FDProductHelper productHelper = new FDProductHelper();
	@Override
	public FDProduct getProduct(String sku, int version) throws RemoteException,FDSkuNotFoundException {

		Response<FDProductData> response = null;
		FDProduct fdProduct;
		String skuCode = sku.replaceAll("[^\\x00-\\x7F]","");
		skuCode = sku.trim();

		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(FDFACTORY_PRODUCTBY_SKUCODES)+"/"+skuCode+"/"+version,  new TypeReference<Response<FDProductData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}else if(response.getData()==null){
				throw new FDSkuNotFoundException(response.getMessage());
			}
			
			fdProduct = ModelConverter.buildFdProduct(response.getData());
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}

		return fdProduct;
	}
	
	@Override
	public Collection<String> findSkusBySapId(String sapId) throws RemoteException, FDSkuNotFoundException {
		
		Response<Collection<String>> response = null;
		String sapIdTrmed = sapId.trim();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(ERP_SKUS_SAPID)+"/"+sapIdTrmed,  new TypeReference<Response<Collection>>(){});
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

	@Override
	public List getProduct(FDSku[] skus) throws RemoteException {
		Response<Collection<FDProductData>> response = null;
		Request<FDSkuData[]> request = new Request<FDSkuData[]>();
		List<FDProduct> fdProductList = new ArrayList<FDProduct>();
		FDSkuData[] skuData = new FDSkuData[skus.length];
		for(int i=0;i<skus.length;i++){
			FDSkuData fdSkuData =new FDSkuData();
			FDSku fdSku = skus[i];
			fdSkuData.setSkuCode(fdSku.getSkuCode());
			fdSkuData.setVersion(fdSku.getVersion());
			skuData[i]=fdSkuData;
		}
			try {
				request.setData(skuData);
				String inputJson;
				inputJson = buildRequest(request);
				response = postDataTypeMap(inputJson,getFdCommerceEndPoint(FDFACTORY_PRODUCTINFO_FDSKUCODES),new TypeReference<Response<Collection<FDProductData>>>() {});
				if(!response.getResponseCode().equals("OK"))
					throw new FDResourceException(response.getMessage());
				if(response.getData()==null)
					throw new FDResourceException(response.getMessage());
					
				for (FDProductData fdProductData : response.getData()) {
					FDProduct model = ModelConverter.buildFdProduct(fdProductData);
					fdProductList.add(model);
				}
				
			} catch (FDEcommServiceException e) {
				LOGGER.error(e.getMessage());
				throw new RemoteException(e.getMessage());
			} catch (FDResourceException e){
				LOGGER.error(e.getMessage());
				throw new RemoteException(e.getMessage());
			}
			return fdProductList;
	}

	
	
}