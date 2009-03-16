package com.freshdirect.transadmin.web.json;

import java.text.ParseException;

import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;

public class DispatchProviderController extends JsonRpcController  implements IDispatchProvider  {
	
	private DispatchManagerI dispatchManagerService;
	
	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}

	public int updateRouteMapping(String routeDate, String cutOffId, String sessionId, boolean isDepot) {
		int result = 0;
		System.out.println("Param:"+routeDate+"->"+ cutOffId+"->"+  sessionId+"->"+  isDepot);
		try {
			result =  getDispatchManagerService().updateRouteMapping(TransStringUtil.getDate(routeDate) ,
																cutOffId, sessionId, isDepot);
		} catch (ParseException parseExp) {
			parseExp.printStackTrace();
			// Do Nothing Return 0;
		}
		return result;
	}
}
