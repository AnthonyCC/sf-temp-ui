package com.freshdirect.fdstore.ecomm.gateway;

import com.freshdirect.ecommerce.data.coremetrics.CdfProcessResultData;
import com.freshdirect.ecommerce.data.coremetrics.CmContextData;
import com.freshdirect.fdstore.coremetrics.CmContext;
import com.freshdirect.fdstore.coremetrics.service.CdfProcessResult;

public class ModelConverter {

	public static CmContextData map(CmContext ctx) {
		CmContextData data = new CmContextData();
		data.setClientId(ctx.getClientId());
		data.setCompoundId(ctx.getCompoundId());
		data.setSiteId(ctx.getSiteId());
		data.setStoreId(ctx.getInstance().getEStoreId().getContentId());
		data.setFaceId(ctx.getInstance().getFacade().name());
		data.setTestAccount(ctx.isTestAccount());
		return data;
	}

	public static CdfProcessResult map(CdfProcessResultData data) {
		CdfProcessResult result = new CdfProcessResult(data.isSuccess(),data.getError());
		return result;
	}
}
