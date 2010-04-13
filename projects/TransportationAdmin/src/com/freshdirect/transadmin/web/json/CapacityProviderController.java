package com.freshdirect.transadmin.web.json;

import java.text.ParseException;

import com.freshdirect.framework.util.MD5Hasher;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;

public class CapacityProviderController extends JsonRpcController implements
		ICapacityProvider  {

	@Override
	public IOrderModel getRoutingOrderByReservation(String reservationId) {
		// TODO Auto-generated method stub
		try {
			return new DeliveryServiceProxy().getRoutingOrderByReservation(reservationId);
		} catch (RoutingServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}	

	public int updateRoutingOrderByReservation(String reservationId, String orderSize, String serviceTime) {
		
		try {
			if(TransStringUtil.isValidDecimal(orderSize) && TransStringUtil.isValidDecimal(serviceTime)
						&& Double.parseDouble(orderSize) >= 0 && Double.parseDouble(serviceTime) >= 0) {
				return new DeliveryServiceProxy().updateRoutingOrderByReservation(reservationId,
																					Double.parseDouble(orderSize), 
																					Double.parseDouble(serviceTime));
			}
		} catch (RoutingServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int updateTimeslotForStatus(String timeslotId, boolean isClosed, String type, String baseDate, String cutOff) {
		
		try {
			return new DeliveryServiceProxy().updateTimeslotForStatus(timeslotId, isClosed, type, TransStringUtil.getDate(baseDate), cutOff);
		} catch (RoutingServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int updateTimeslotForDynamicStatus(String timeslotId, boolean isDynamic, String type, String baseDate, String cutOff, String accessCode) {
		String hashedAccessCode = MD5Hasher.hash(accessCode);
		if(hashedAccessCode != null && hashedAccessCode.equals(TransportationAdminProperties.getDynamicRoutingFeatureAccessKey())) {
			try {
				return new DeliveryServiceProxy().updateTimeslotForDynamicStatus(timeslotId, isDynamic, type, TransStringUtil.getDate(baseDate), cutOff);
			} catch (RoutingServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return 0;
	}
	
}
