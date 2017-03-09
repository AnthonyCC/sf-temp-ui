package com.freshdirect.fdlogistics.services.impl;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.log4j.Category;

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
	
	public void savePricingZoneData(List<ErpZoneMasterInfo> zoneInfoList) throws RemoteException, LoaderException{
		try {
			List<PricingZoneData> data = getOrikaMapper().mapAsList(zoneInfoList, PricingZoneData.class);
			Request<List<PricingZoneData>> request = new Request<List<PricingZoneData>>();
			request.setData(data);
			String inputJson = buildRequest(request);
			getData(inputJson, getFdCommerceEndPoint(SAVE_PRICING_ZONES_API), Response.class);
		} catch (FDLogisticsServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		}
		
		
	}
	
	public void getCountryOfOriginData() throws RemoteException, LoaderException{
		try {
			httpGetData(getFdCommerceEndPoint(COO_API), Response.class);
		} catch (FDLogisticsServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RemoteException(e.getMessage(), e);
		}
		
		
	}
	
}
