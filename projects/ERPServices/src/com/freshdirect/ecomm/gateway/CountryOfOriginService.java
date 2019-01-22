package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.erp.ErpCOOLInfo;
import com.freshdirect.erp.ErpCOOLKey;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CountryOfOriginService extends AbstractEcommService implements CountryOfOriginServiceI {

	private static final String COO_API = "coo";
	private static final Category LOGGER = LoggerFactory.getInstance(CountryOfOriginService.class);

	private static CountryOfOriginServiceI INSTANCE;

	public static CountryOfOriginServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CountryOfOriginService();

		return INSTANCE;
	}

	@Override
	public Map<ErpCOOLKey, ErpCOOLInfo> getCountryOfOriginData(Date since) throws RemoteException {
		Response<List<ErpCOOLInfo>> response = new Response<List<ErpCOOLInfo>>();

		try {
			long date1 = 0;

			if (since != null) {
				date1 = since.getTime();
			}
			response = httpGetDataTypeMap(getFdCommerceEndPoint(COO_API + "/" + date1),
					new TypeReference<Response<List<ErpCOOLInfo>>>() {
					});

			List<ErpCOOLInfo> resultList = response.getData();
			Map<ErpCOOLKey, ErpCOOLInfo> map = new HashMap<ErpCOOLKey, ErpCOOLInfo>();
			for (ErpCOOLInfo result : resultList) {

				ErpCOOLKey key = new ErpCOOLKey(result.getSapID(), result.getPlantId());
				map.put(key, result);
			}
			return map;

		} catch (FDResourceException e) {
			LOGGER.error("Error in saveCountryOfOriginData, date=" + since, e);
			throw new RemoteException(e.getMessage(), e);
		}

	}

	public void saveCountryOfOriginData(List<ErpCOOLInfo> cooList) throws FDResourceException, RemoteException {
		String inputJson = null;
		try {
			Request<List<ErpCOOLInfo>> request = new Request<List<ErpCOOLInfo>>();
			request.setData(cooList);
			inputJson = buildRequest(request);
			Response<Void> response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(COO_API), new TypeReference<Response<Void>>() {
			});
			if (!response.getResponseCode().equals("OK")) {
				LOGGER.error("Error in saveCountryOfOriginData: inputJson=" + inputJson
						+ ", response=" + response);
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDEcommServiceException e) {
			LOGGER.error("Error in saveCountryOfOriginData, data=" + inputJson, e);
			throw new RemoteException(e.getMessage(), e);
		} 

	}
}
