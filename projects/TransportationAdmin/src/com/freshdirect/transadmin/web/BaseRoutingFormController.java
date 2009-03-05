package com.freshdirect.transadmin.web;

import java.util.Collection;
import java.util.Iterator;

import com.freshdirect.transadmin.datamanager.IServiceProvider;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.TransportationAdminProperties;

public class BaseRoutingFormController extends BaseFormController implements IServiceProvider  {
	
	private DomainManagerI domainManagerService;
	
	private DispatchManagerI dispatchManagerService;
	
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}
	
	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}
	
	protected String getOutputFilePath(String filePath) {
//		System.out.println("TransportationAdminProperties.getDownloadProviderUrl() >"+TransportationAdminProperties.getDownloadProviderUrl());
		return TransportationAdminProperties.getDownloadProviderUrl()+"?filePath="+filePath;
	}
	
	protected String getDepotZones(DomainManagerI domainManagerService) {
		
		StringBuffer strBuf = new StringBuffer();
		Collection dataList = domainManagerService.getZones();
		Zone tmpZone = null;
		TrnArea tmpArea = null;
		if(dataList != null) {
			Iterator iterator = dataList.iterator();	
			while(iterator.hasNext()) {
				tmpZone = (Zone)iterator.next();
				tmpArea = tmpZone.getArea();
				if(tmpArea != null && "X".equalsIgnoreCase(tmpArea.getIsDepot())) {
					if(strBuf.length() != 0) {
						strBuf.append(", ");
					}
					strBuf.append(tmpZone.getZoneCode());
				}
			}			
		}
		return strBuf.toString();
	}
	
	protected String getRoutingZones(DomainManagerI domainManagerService) {
		
		StringBuffer strBuf = new StringBuffer();
		Collection dataList = domainManagerService.getZones();
		Zone tmpZone = null;
		TrnArea tmpArea = null;
		if(dataList != null) {
			Iterator iterator = dataList.iterator();	
			while(iterator.hasNext()) {
				tmpZone = (Zone)iterator.next();
				tmpArea = tmpZone.getArea();
				if(tmpArea != null && "X".equalsIgnoreCase(tmpArea.getActive())) {
					if(strBuf.length() != 0) {
						strBuf.append(", ");
					}
					strBuf.append(tmpZone.getZoneCode());
				}
			}			
		}
		return strBuf.toString();
	}
}
