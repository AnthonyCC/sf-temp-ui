package com.freshdirect.transadmin.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.routing.util.IRoutingParamConstants;
import com.freshdirect.transadmin.datamanager.RouteInputDataManager;
import com.freshdirect.transadmin.datamanager.RoutingResult;
import com.freshdirect.transadmin.model.DlvServiceTimeScenario;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.web.model.FileUploadCommand;

public class RoutingInputFormController extends BaseFormController {
	
	private LocationManagerI locationManagerService;
	
	public LocationManagerI getLocationManagerService() {
		return locationManagerService;
	}
	
	private DomainManagerI domainManagerService;
		
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}

	public void setLocationManagerService(LocationManagerI locationManagerService) {
		this.locationManagerService = locationManagerService;
	}
	
	protected Map referenceData(HttpServletRequest request)
													throws ServletException {
			Map refData = new HashMap();
			refData.put("scenarios", locationManagerService.getServiceTimeScenarios());
			return refData;
	}

	protected Object formBackingObject(HttpServletRequest request)
		throws Exception {
		String processType = request.getParameter("processType");
		
		FileUploadCommand bean = new FileUploadCommand();
		bean.setProcessType(processType);
		DlvServiceTimeScenario defaultScenario = locationManagerService.getDefaultServiceTimeScenario();
		if(defaultScenario != null) {
			bean.setServiceTimeScenario(defaultScenario.getCode());
		}
		return bean;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,HttpServletResponse response,
		      Object command, BindException errors) throws ServletException, IOException
		  {
		    // cast the bean
		    logger.info("FileUploadController -- executing onSubmit!");
		    FileUploadCommand bean = (FileUploadCommand) command;
		    //let's see if there's content there
		    byte[] bytes = bean.getFile();
		   
		   	   
		    Map paramMap = new HashMap();
		    paramMap.put(IRoutingParamConstants.SERVICETIME_SCENARIO, bean.getServiceTimeScenario());
		    paramMap.put(IRoutingParamConstants.ROUTING_USER, com.freshdirect.transadmin.security.SecurityManager.getUserName(request));
		    
		    RouteInputDataManager manager = new RouteInputDataManager();
		    RoutingResult result = manager.process(bytes, null, null, com.freshdirect.transadmin.security.SecurityManager.getUserName(request)
		    											, paramMap, getDomainManagerService());
		    List errorList = result.getErrors();
		       
		    		    
		    if(errorList == null || errorList.isEmpty()) {
		    	bean.setOutputFile1(result.getOutputFile1());
			    bean.setOutputFile2(result.getOutputFile2());
			    bean.setOutputFile3(result.getOutputFile3());
		    	saveMessage(request, getMessage("app.actionmessage.114",
						new Object[] { }));
			} else {
				saveErrorMessage(request, errorList);
			}
		    
		    ModelAndView mav = new ModelAndView(getSuccessView(), errors.getModel());
			mav.getModel().put(this.getCommandName(), command);		
			mav.getModel().putAll(referenceData(request));
			
			return mav;
	}

	  protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException  {
	    // to actually be able to convert Multipart instance to byte[]
	    // we have to register a custom editor
	    logger.info("FileUploadController -- custom editor to convert Multipart instance to byte[] is registered here.");
	    binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	    // now Spring knows how to handle multipart object and convert them
	  }

}
