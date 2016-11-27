package com.freshdirect.delivery.helper;

import com.freshdirect.logistics.controller.data.request.RequestContext;

public class RequestContextUtil {

	
	public static RequestContext getRequestContext(String initiator, String applicationId){
		RequestContext context = new RequestContext();
		context.setInitiator(initiator);
		context.setApplicationId(applicationId);
		return context;
	}
}
