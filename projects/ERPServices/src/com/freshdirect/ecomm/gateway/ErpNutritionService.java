package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.content.nutrition.ErpNutritionModel;
import com.freshdirect.content.nutrition.panel.NutritionPanel;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.nutrition.ErpNutritionModelData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.service.FDECommerceService;
import com.freshdirect.payment.service.IECommerceService;
import com.freshdirect.payment.service.ModelConverter;

public class ErpNutritionService extends AbstractEcommService implements ErpNutritionServiceI{
	
	private final static Category LOGGER = LoggerFactory
			.getInstance(FDECommerceService.class);

	private static ErpNutritionService INSTANCE;
	private static final String NUTRITION_CREATE = "nutrition/create";
	private static final String NUTRITION_UPDATE = "nutrition/update";
	private static final String NUTRITION_BY_SKU = "nutrition/skuCode";
	private static final String NUTRITION_PANEL_BY_SKU ="nutrition/panelByskuCode" ;
	private static final String NUTRITION_SKU_BY_UPC ="nutrition/skuCodeByupc" ;
	private static final String NUTRITION_BY_DATE ="nutrition/lastModified" ;
	private static final String NUTRITION_PANEL_BY_DATE ="nutrition/panelbyDate" ;
	private static final String NUTRITION_SKU_UPC_MAP ="nutrition/createUpcSkuMap" ;
	private static final String NUTRITION_PANEL_SAVE = "nutrition/savePanel";
	private static final String NUTRITION_PANEL_DELETE = "nutrition/deletePanel";
	private static final String NUTRITION_REMOVE = "nutrition/remove";
	private static final String NUTRITION_REPORT = "nutrition/nutritionreport";
	private static final String NUTRITION_CLAIM_REPORT = "nutrition/claimreport";
	// Nurtition Start 
	
	public static ErpNutritionServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ErpNutritionService();

		return INSTANCE;
	}
	@Override
	public void createNutrition(ErpNutritionModel nutritionModel)
			throws  RemoteException {
		Response response = null;
		ErpNutritionModelData data = ModelConverter.buildNutritionModelData(nutritionModel);
		Request<ErpNutritionModelData> request = new Request<ErpNutritionModelData>();
		String inputJson;
		try{
			
			request.setData(data);
			inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(NUTRITION_CREATE), Response.class);
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());	
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
		
	}
	
	@Override
	public void updateNutrition(ErpNutritionModel nutritionModel, String userId)
			throws RemoteException {
		Response response = null;
		ErpNutritionModelData data = ModelConverter.buildNutritionModelData(nutritionModel);
		Request<ErpNutritionModelData> request = new Request<ErpNutritionModelData>();
		String inputJson;
		try{
			
			request.setData(data);
			inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(NUTRITION_UPDATE)+"/user/"+userId, Response.class);
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());	
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
		
	}
	
	@Override
	public ErpNutritionModel getNutrition(String skuCode) throws RemoteException {
		Response<ErpNutritionModelData> response = null;
		
		ErpNutritionModel nutritionModel;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(NUTRITION_BY_SKU)+"/"+skuCode,  new TypeReference<Response<ErpNutritionModelData>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new RemoteException(response.getMessage());
			}
			 nutritionModel = ModelConverter.buildErpNutritionModel(response.getData());
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return nutritionModel;
	}
	@Override
	public NutritionPanel getNutritionPanel(String skuCode) throws RemoteException {
		Response<NutritionPanel> response = null;
		
		ErpNutritionModel nutritionModel;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(NUTRITION_PANEL_BY_SKU)+"/"+skuCode,  new TypeReference<Response<NutritionPanel>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new RemoteException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	@Override
	public String getSkuCodeForUpc(String upc) throws RemoteException {
		Response<String> response = null;
		
		ErpNutritionModel nutritionModel;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(NUTRITION_SKU_BY_UPC)+"/"+upc,  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new RemoteException(response.getMessage());
			}

		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	
	@Override
	public Map<String, ErpNutritionModel> loadNutrition(Date lastModified) throws RemoteException {
		Response<Map<String, ErpNutritionModelData>> response = null;
		Map<String, ErpNutritionModel> data = new HashMap();
		ErpNutritionModel nutritionModel;
		try {
			long date1=0;
			if(lastModified!=null){
			date1 = lastModified.getTime(); 
			}
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(NUTRITION_BY_DATE)+"/"+date1,  new TypeReference<Response<Map<String, ErpNutritionModelData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new RemoteException(response.getMessage());
			}
			
			for(Map.Entry<String,ErpNutritionModelData> entry : response.getData().entrySet()){
				data.put(entry.getKey(), ModelConverter.buildErpNutritionModel(entry.getValue()));
			}
			 
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return data;
	}
	@Override
	public Map<String, NutritionPanel> loadNutritionPanels(Date lastModified) throws RemoteException {
		Response<Map<String, NutritionPanel>> response = null;

		try {
			String date1=null;
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
			if(lastModified!=null){
			date1 = format1.format(lastModified); 
			}
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(NUTRITION_PANEL_BY_DATE)+"/"+date1,  new TypeReference<Response<Map<String, NutritionPanel>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new RemoteException(response.getMessage());
			}
			 
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	@Override
	public void createUpcSkuMapping(String skuCode, String upcCode ) throws RemoteException {
		Response<Map<String, NutritionPanel>> response = null;

		try {
		
			response = this.httpGetData(getFdCommerceEndPoint(NUTRITION_SKU_UPC_MAP)+"/skuCode/"+skuCode+"/upcCode/"+upcCode, Response.class);
			if(!response.getResponseCode().equals("OK")){
				throw new RemoteException(response.getMessage());
			}
			 
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	
	}
	
	@Override
	public void saveNutritionPanel(NutritionPanel nutritionModel)	throws  RemoteException {
		Response response = null;
		
		Request<NutritionPanel> request = new Request<NutritionPanel>();
		String inputJson;
		try{
			
			request.setData(nutritionModel);
			inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(NUTRITION_PANEL_SAVE), Response.class);
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());	
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	
	}
	@Override
	public void deleteNutritionPanel(String  skuCode)	throws  RemoteException {
		Response response = null;

		try{
			response = this.httpGetData(getFdCommerceEndPoint(NUTRITION_PANEL_DELETE)+"/skuCode/"+skuCode, Response.class);
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());	
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	
	}
	
	@Override
	public void removeNutrition(String  skuCode)	throws  RemoteException {
		Response response = null;

		try{
			response = this.httpGetData(getFdCommerceEndPoint(NUTRITION_REMOVE)+"/skuCode/"+skuCode, Response.class);
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());	
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	
	}
	@Override
	public List<Map<String, String>> generateNutritionReport()
			throws RemoteException {
		Response<List<Map<String, String>>> response = null;

		try{
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(NUTRITION_REPORT), new TypeReference<Response<List<Map<String, String>>>>() {
			});
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());	
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	
	}
	
	@Override
	public List<Map<String, String>> generateClaimsReport()
			throws RemoteException {
		Response<List<Map<String, String>>> response = null;

		try{
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(NUTRITION_CLAIM_REPORT), new TypeReference<Response<List<Map<String, String>>>>() {
			});
			if(!response.getResponseCode().equals("OK"))
				throw new RemoteException(response.getMessage());	
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	
	}
	/////////// Nutrition End
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
