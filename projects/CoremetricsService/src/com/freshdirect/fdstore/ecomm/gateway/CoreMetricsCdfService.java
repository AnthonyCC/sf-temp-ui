package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.coremetrics.CdfProcessResultData;
import com.freshdirect.ecommerce.data.coremetrics.CmContextData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.coremetrics.CmContext;
import com.freshdirect.fdstore.coremetrics.service.CdfProcessResult;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CoreMetricsCdfService extends AbstractEcommService implements CoreMetricsCdfI {

	
	private static Category LOGGER = LoggerFactory.getInstance(CoreMetricsCdfService.class);

	private static CoreMetricsCdfI INSTANCE;
	
	private static final String PROCESS_CONTEXT = "coremetrics/processcdf";
	
	
	public static CoreMetricsCdfI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CoreMetricsCdfService();

		return INSTANCE;
	}
	
	@Override
	public CdfProcessResult processCdf(CmContext ctx) throws RemoteException {
		Request<CmContextData> request = new Request<CmContextData>();
		Response<CdfProcessResultData> response = new Response<CdfProcessResultData>();
		CdfProcessResult result;
		try{
			CmContextData  data = ModelConverter.map(ctx);
			request.setData(data);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(PROCESS_CONTEXT),new TypeReference<Response<CdfProcessResultData>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new RemoteException(response.getMessage());
			}
			result = ModelConverter.map(response.getData());
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return result;
	}


	@Override
	@Deprecated
	public CdfProcessResult processCdf() throws RemoteException {
		Request<CmContextData> request = new Request<CmContextData>();
		Response<CdfProcessResultData> response = new Response<CdfProcessResultData>();
		CdfProcessResult result;
		try{
			CmContextData  data = null;
			request.setData(data);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(PROCESS_CONTEXT),new TypeReference<Response<CdfProcessResultData>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new RemoteException(response.getMessage());
			}
			result = ModelConverter.map(response.getData());
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return result;
	}
}
	