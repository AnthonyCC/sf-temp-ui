package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.List;
import org.apache.log4j.Category;
import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.crm.CrmCaseOperation;
import com.freshdirect.crm.CrmSystemCaseInfo;
import com.freshdirect.ecomm.converter.CrmManagerConverter;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.crm.CrmCaseOperationData;
import com.freshdirect.ecommerce.data.crm.CrmSystemCaseInfoData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CrmManagerService extends AbstractEcommService implements CrmManagerServiceI {
	
	private static CrmManagerService INSTANCE;
	
	private final static Category LOGGER = LoggerFactory.getInstance(CrmManagerService.class);

	private static final String GET_OPERATIONS = "crm/operations";
	private static final String CREATE_SYSTEM_CASE = "crm/systemCase/create";
	private static final String CREATE_SYSTEM_CASE_IN_SINGLE_TXN = "crm/systemCase/create/singleTxn";
	
	public static CrmManagerServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CrmManagerService();

		return INSTANCE;
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

}
