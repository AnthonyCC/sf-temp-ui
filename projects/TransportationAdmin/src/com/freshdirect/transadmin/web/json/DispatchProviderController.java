package com.freshdirect.transadmin.web.json;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.freshdirect.customer.ErpRouteMasterInfo;
import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.LogManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;

public class DispatchProviderController extends JsonRpcController  implements IDispatchProvider {
	
	private DispatchManagerI dispatchManagerService;
	
	private DomainManagerI domainManagerService;
	
	private LogManagerI logManager;
	
	
	public void setLogManager(LogManagerI logManager) {
		this.logManager = logManager;
	}

	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}
	
	

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
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
	
	public Collection getActiveRoute(String date,String zoneCode)
	{
		Collection results=null;		
		
		try {
			if(zoneCode==null||zoneCode.trim().length()==0)
			{
				Collection c=domainManagerService.getAdHocRoutes();
				if(c!=null)
				{			
					results=new ArrayList();
					Iterator iterator=c.iterator();
					while(iterator.hasNext())
					{
						TrnAdHocRoute info=(TrnAdHocRoute)iterator.next();					
						results.add(info.getRouteNumber());
						
					}				
				}
			}
			else
			{
				Collection c=domainManagerService.getRoutes(TransStringUtil.getServerDate(date));
				if(c!=null)
				{			
					results=new ArrayList();
					Iterator iterator=c.iterator();
					while(iterator.hasNext())
					{
						ErpRouteMasterInfo info=(ErpRouteMasterInfo)iterator.next();					
						if(zoneCode.equals(info.getZoneNumber()))
						{
							results.add(info.getRouteNumber());
						}
					}				
				}
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}

	public Collection getActivityLog(String date) 
	{
		try {
			Date fromDate=TransStringUtil.getDate(date);
			Date toDate=TransStringUtil.getDate(date);
			Calendar c=Calendar.getInstance();
			c.setTime(toDate);
			c.add(Calendar.DATE, 1);
			c.add(Calendar.SECOND, -1);
			toDate=c.getTime();
			
			fromDate=new Timestamp(fromDate.getTime());
			toDate=new Timestamp(toDate.getTime());
			Collection list= logManager.getLogs(fromDate, toDate);
			return list;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
