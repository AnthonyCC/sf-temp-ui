package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.smartstore.DynamicSiteFeatureData;
import com.freshdirect.ecommerce.data.smartstore.VariantData;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.ejb.DynamicSiteFeature;
import com.freshdirect.storeapi.application.CmsManager;

public class SmartStoreConfigurationService extends AbstractEcommService implements
		SmartStoreConfigurationServiceI {

	private static SmartStoreConfigurationService INSTANCE;
	
	private static final String GET_VARIANT_BY_SITEFEATURE_ESTOREID = "smartstore/variants/";
	private static final String GET_DYNAMIC_SITEFEATURE_BY_ESTOREID = "smartstore/sitefeature/";
	
	public static SmartStoreConfigurationServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new SmartStoreConfigurationService();

		return INSTANCE;
	}
	
	@Override
	public Collection<Variant> getVariants(EnumSiteFeature feature)
			throws RemoteException, SQLException {
		Response<Collection<VariantData>> response = new Response<Collection<VariantData>>();
		String eStoreId = CmsManager.getInstance().getEStoreId();
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(GET_VARIANT_BY_SITEFEATURE_ESTOREID+feature.getName()+"/"+eStoreId),new TypeReference<Response<Collection<VariantData>>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDRuntimeException(response.getMessage());

		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return ModelConverter.buildVariantList(response.getData());
	}

	@Override
	public Collection<DynamicSiteFeature> getSiteFeatures(String eStoreId)
			throws RemoteException, SQLException {
		Response<Collection<DynamicSiteFeatureData>> response = new Response<Collection<DynamicSiteFeatureData>>();
		try {
			response = httpGetDataTypeMap(
					getFdCommerceEndPoint(GET_DYNAMIC_SITEFEATURE_BY_ESTOREID+eStoreId),new TypeReference<Response<Collection<DynamicSiteFeatureData>>>() {
					});
			if (!response.getResponseCode().equals("OK"))
				throw new FDRuntimeException(response.getMessage());

		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return ModelConverter.buildDynamicSiteFeatureList(response.getData());
	}

	
}
