package com.freshdirect.routing.service.proxy;

import java.util.Map;

import com.freshdirect.routing.model.IDeliveryModel;
import com.freshdirect.routing.service.IDeliveryService;
import com.freshdirect.routing.service.RoutingServiceLocator;
import com.freshdirect.routing.service.exception.RoutingServiceException;

public class DeliveryServiceProxy extends BaseServiceProxy {
	
	public double getServiceTime(IDeliveryModel model, String serviceTimeFactorExpression
			, String serviceTimeExpression) throws RoutingServiceException {
	
		return getService().getServiceTime(model, serviceTimeFactorExpression, serviceTimeExpression);		
	}		
	
	public IDeliveryModel getDeliveryInfo(String saleId) throws RoutingServiceException {
		
		return getService().getDeliveryInfo(saleId);	
	}
	
	public String getDeliveryType(String zoneCode) throws RoutingServiceException {
		return getService().getDeliveryType(zoneCode);
	}
	
	public String getDeliveryZoneType(String zoneCode) throws RoutingServiceException {
		return getService().getDeliveryZoneType(zoneCode);
	}
	
	public IDeliveryService getService() {
		return RoutingServiceLocator.getInstance().getDeliveryService();
	}
	
	public double getServiceTime(IDeliveryModel model, String serviceTimeFactorExpression
			, String serviceTimeExpression, int intFixedServiceTime, int intVariableServiceTime) throws RoutingServiceException {
		return getService().getServiceTime(model,serviceTimeFactorExpression, serviceTimeExpression, intFixedServiceTime, intVariableServiceTime);	
	}
	
	public Map getDeliveryZoneDetails()  throws RoutingServiceException {
		return getService().getDeliveryZoneDetails();
	}
}
