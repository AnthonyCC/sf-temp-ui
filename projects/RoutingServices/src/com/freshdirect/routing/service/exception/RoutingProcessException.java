package com.freshdirect.routing.service.exception;

public class RoutingProcessException extends RoutingBaseException {
	
	public RoutingProcessException(Exception e, String id) {
	      super(null, e, id);     	      
	   }
	
	public RoutingProcessException(String msg, Exception e, String id) {
	      super(msg, e, id);
	   }
	
}
