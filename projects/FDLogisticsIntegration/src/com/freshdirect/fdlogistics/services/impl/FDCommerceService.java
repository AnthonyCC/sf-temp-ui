package com.freshdirect.fdlogistics.services.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Category;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.customer.ErpZoneMasterInfo;
import com.freshdirect.dataloader.LoaderException;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.erp.coo.CountryOfOriginData;
import com.freshdirect.ecommerce.data.erp.pricing.PricingZoneData;
import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.fdlogistics.exception.FDLogisticsServiceException;
import com.freshdirect.fdlogistics.services.ICommerceService;
import com.freshdirect.framework.util.log.LoggerFactory;


public class FDCommerceService extends AbstractLogisticsService implements ICommerceService{

	private final static Category LOGGER = LoggerFactory
			.getInstance(FDCommerceService.class);

	private static final String SAVE_PRICING_ZONES_API ="/pricing/zone";
	private static final String COO_API ="/coo";
	private static final String DB_MONITOR ="monitor/dbcheck";
	private static final String TEST_DATA_CUSTOMERID ="test/dyfeligiblecustomerid";
	private static final String TEST_DATA_PING ="test/ping";
	private static final String TEST_DATA_ERP_USERID ="test/erpidbyuserid";
	private static final String TEST_DATA_CUSID_ERPID ="test/fdcustomerid";
	private static final String TEST_DATA_SKU_CODES ="test/skucodes";
	private static final String TEST_DATA_ERP_CUSTIDS ="test/erpcustomerids";
	
	public void savePricingZoneData(List<ErpZoneMasterInfo> zoneInfoList) throws RemoteException, LoaderException{
		try {
			List<PricingZoneData> data = getOrikaMapper().mapAsList(zoneInfoList, PricingZoneData.class);
			Request<List<PricingZoneData>> request = new Request<List<PricingZoneData>>();
			request.setData(data);
			String inputJson = buildRequest(request);
			getData(inputJson, getFdCommerceEndPoint(SAVE_PRICING_ZONES_API), Response.class);
		} catch (FDLogisticsServiceException e) {
			
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage(), e);
		}
		
		
	}
	
	public void saveCountryOfOriginData(List<ErpCOOLInfo> cooList) throws RemoteException, LoaderException{
		try {
			List<CountryOfOriginData> data = getOrikaMapper().mapAsList(cooList, CountryOfOriginData.class);
			Request<List<CountryOfOriginData>> request = new Request<List<CountryOfOriginData>>();
			request.setData(data);
			String inputJson = buildRequest(request);
			getData(inputJson, getFdCommerceEndPoint(COO_API), Response.class);
		} catch (FDLogisticsServiceException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage(), e);
		}
		
		
	}
	
	public void getCountryOfOriginData() throws RemoteException, LoaderException{
		try {
			httpGetData(getFdCommerceEndPoint(COO_API), Response.class);
		} catch (FDLogisticsServiceException e) {
			
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage(), e);
		}
		
		
	}
	
	public void healthCheck() throws RemoteException{
		try {
			Response<String> response = httpGetData(getFdCommerceEndPoint(DB_MONITOR), Response.class);
			if(!response.getResponseCode().equals("OK"))
			throw new FDLogisticsServiceException(response.getMessage());
		} catch (FDLogisticsServiceException e) {
			
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage(), e);
		}
		
		
	}
	public List<Long> getDYFEligibleCustomerIDs() throws RemoteException{
		try {

			Response<List<Long>> response =  httpGetDataTypeMap(getFdCommerceEndPoint(TEST_DATA_CUSTOMERID), new TypeReference<Response<List<Long>>>() {});
			if(!response.getResponseCode().equals("OK"))
			throw new FDLogisticsServiceException(response.getMessage());
			return response.getData();
		} catch (FDLogisticsServiceException e) {
			
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage(), e);
		}

		
	}
	@Override
	public boolean ping() throws RemoteException {
		try {
			Response<Boolean> response = httpGetData(getFdCommerceEndPoint(TEST_DATA_PING), Response.class);
			if(!response.getResponseCode().equals("OK"))
			throw new FDLogisticsServiceException(response.getMessage());
			return response.getData();
		} catch (FDLogisticsServiceException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		}
	}


	@Override
	public List<Long> getErpCustomerIds() throws RemoteException {
		try {
			Response<List<Long>> response =  httpGetDataTypeMap(getFdCommerceEndPoint(TEST_DATA_ERP_CUSTIDS), new TypeReference<Response<List<Long>>>() {});
			if(!response.getResponseCode().equals("OK"))
			throw new FDLogisticsServiceException(response.getMessage());
			return response.getData();
		} catch (FDLogisticsServiceException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public String getFDCustomerIDForErpId(String erpCustomerPK)
			throws RemoteException {
		try {
			Response<String> response = httpGetData(getFdCommerceEndPoint(TEST_DATA_CUSID_ERPID+"?erpId="+erpCustomerPK), Response.class);
			if(!response.getResponseCode().equals("OK"))
			throw new FDLogisticsServiceException(response.getMessage());
			return response.getData();
		} catch (FDLogisticsServiceException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public String getErpIDForUserID(String userID) throws RemoteException {
		try {
			Response<String> response = httpGetData(getFdCommerceEndPoint(TEST_DATA_ERP_USERID+"?userid="+userID), Response.class);
			if(!response.getResponseCode().equals("OK"))
			throw new FDLogisticsServiceException(response.getMessage());
			return response.getData();
		} catch (FDLogisticsServiceException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public Collection<String> getSkuCodes() throws RemoteException {
		try {
			Response<Collection<String>> response = httpGetData(getFdCommerceEndPoint(TEST_DATA_SKU_CODES), Response.class);
			if(!response.getResponseCode().equals("OK"))
			throw new FDLogisticsServiceException(response.getMessage());
			return response.getData();
		} catch (FDLogisticsServiceException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		}
	}
	
}
