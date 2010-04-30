package com.freshdirect.transadmin.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.freshdirect.transadmin.datamanager.model.WorkTableModel;
import com.freshdirect.transadmin.datamanager.model.ZoneWorktableModel;
import com.freshdirect.transadmin.model.ZoneWorkTableForm;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.web.util.ZoneWorkTableUtil;

public class ZoneExpansionFormController extends BaseFormController {
	
	private DomainManagerI domainManagerService;
		
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}
	
	protected Map referenceData(HttpServletRequest request)
											throws ServletException {
		Map refData = new HashMap();
		Map worktableData=ZoneWorkTableUtil.getActiveWorkTableAndRegionIds();
		refData.put("zoneWorkTables", worktableData.keySet());
		return refData;
		
	}
	
	protected Object formBackingObject(HttpServletRequest request)
															throws Exception {
		return super.formBackingObject(request);
	}
	
   	protected ModelAndView onSubmit(HttpServletRequest request,HttpServletResponse response,
		      Object command, BindException errors) throws ServletException, IOException  {
		 
		ZoneWorkTableForm commandObj=(ZoneWorkTableForm)command;
   		String workTable= commandObj.getZoneWorkTable(); 
   		String expansionType= commandObj.getType();
		
		String regionId = ZoneWorkTableUtil.getRegionId(workTable);
		
		request.setAttribute("zoneWorkTable", workTable);
		request.setAttribute("type", expansionType);
		
		request.setAttribute("secondView", "true");
		//Environment
		String url=request.getRequestURL().toString();
	    if(url.indexOf("dev")>-1 || url.indexOf("stg")>-1 || url.indexOf("trn")>-1){
	    	request.setAttribute("rightEnvironment", true);
	    	String envName="";
	    	if(url.indexOf("dev")>-1){
	    		envName = "DEV";
	    	}else if(url.indexOf("stg")>-1){
	    		envName = "STAGE";
	    	}else if(url.indexOf("trn")>-1){
	    		envName = "PROD";
	    	}
	    	request.setAttribute("environment", envName);
	    }
		
		ModelAndView mav = new ModelAndView(getSuccessView(),errors.getModel());
   		
		Collection zoneList= new ArrayList();
		if("rollout".equalsIgnoreCase(expansionType)){
	   		if(request.getAttribute("environment")!=null && ("DEV".equals((String)request.getAttribute("environment")))){
				domainManagerService.refreshDev(workTable);
			}
	   		zoneList =domainManagerService.getZoneWorkTableInfo(workTable, regionId);
	   	}else if("zExpansion".equalsIgnoreCase(expansionType)){
	   		zoneList =domainManagerService.getCommonList(workTable, regionId);
	   	}
   		
		String deliveryFee=domainManagerService.getDeliveryCharge(regionId);  			
	   	commandObj.setDeliveryFee(deliveryFee);
   			
   		mav.getModel().put(this.getCommandName(), command);
   		mav.getModel().put("zones", zoneList);
   		mav.getModel().putAll(referenceData(request));
   	
   		if(!zoneList.isEmpty()){
   			saveMessage(request, getMessage("app.actionmessage.153", new Object[]{}));
   		}else{
   			saveErrorMessage(request, getMessage("app.error.128", new Object[]{}));
   		}
   		return mav;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// validate for polygons.
		//if errors create a view and return
		Collection dataList=new ArrayList();
		if("GET".equalsIgnoreCase(request.getMethod())){
			dataList=domainManagerService.checkPolygons();
			if(dataList==null || dataList.isEmpty()){
				saveMessage(request, getMessage("app.actionmessage.152",new Object[] { }));
			}
		}
		if(!dataList.isEmpty()){
			saveErrorMessage(request, "Drawn Polygons as some errors. Please check it before proceeding.");
		}
		
		return super.handleRequestInternal(request, response);
	}	
   	
}
