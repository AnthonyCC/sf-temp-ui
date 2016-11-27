package com.freshdirect.dashboard.exception;

public enum ServiceEnum {
		
	EVENT_SERVICE(1, "Event Service"), 
	ORDER_SERVICE(2, "Order Service"), 
	DISPATCHVOLUME_SERVICE(3, "Dispatch Volume Service"), 
	;
	
	private int serviceId;
	private String serviceEnumName;
	
	
	ServiceEnum(int serviceId, String serviceEnumName) {
		this.serviceId = serviceId;
		this.serviceEnumName = serviceEnumName;
	}
	
	public int getServiceId() {
		return serviceId;
	}
	
	public String getServiceEnumName(){
		return serviceEnumName;
	}
	
	static public BaseException createServiceException(RestError restError) {
		switch (restError.getServiceId()){
		case 1:
			ErrorCodeEnum errorCodeEnum = ErrorCodeEventEnum.get(restError.getErrorCode());
			return new ServiceException(errorCodeEnum, 
					restError.getDebugMessage(), restError.getMessageArgs());
		case 2:
			ErrorCodeEnum errorOrderCodeEnum = ErrorCodeOrderRateEnum.get(restError.getErrorCode());
			return new ServiceException(errorOrderCodeEnum, 
					restError.getDebugMessage(), restError.getMessageArgs());
		case 3:
			ErrorCodeEnum errorDispatchCodeEnum = ErrorCodeDispatchVolumeEnum.get(restError.getErrorCode());
			return new ServiceException(errorDispatchCodeEnum, 
					restError.getDebugMessage(), restError.getMessageArgs());

		// FIXME: add other services here

		default:
			return null;
		}
		
	}

}
