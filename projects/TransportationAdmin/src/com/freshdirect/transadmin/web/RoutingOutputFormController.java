package com.freshdirect.transadmin.web;

import java.io.IOException;
import java.util.ArrayList;
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

import com.freshdirect.transadmin.datamanager.RouteOutputDataManager;
import com.freshdirect.transadmin.datamanager.RoutingResult;
import com.freshdirect.transadmin.web.model.RoutingOutCommand;

public class RoutingOutputFormController extends BaseRoutingFormController {
	
		
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();		
		refData.put("cutoffs", getDomainManagerService().getCutOffs());
				
		return refData;
	}

	protected Object formBackingObject(HttpServletRequest request)
		throws Exception {		
		RoutingOutCommand bean = new RoutingOutCommand();
		bean.setRoutingDepotZones(getDepotZones(this.getDomainManagerService()));
		return bean;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,HttpServletResponse response,
		      Object command, BindException errors) throws ServletException, IOException
		  {
		    // cast the bean
		    logger.info("FileUploadController -- executing onSubmit!");
		    RoutingOutCommand bean = (RoutingOutCommand) command;
		      	   
		   
		    RouteOutputDataManager manager = new RouteOutputDataManager();
		    RoutingResult result = manager.process(bean, com.freshdirect.transadmin.security.SecurityManager.getUserName(request)
		    											, this);
		    
		    List errorList = result.getErrors();
		    
		    if(errorList == null || errorList.isEmpty()) {
			    bean.setOrderOutputFilePath(getOutputFilePath(result.getOutputFile1()));
			    bean.setTruckOutputFilePath(getOutputFilePath(result.getOutputFile2()));
			    bean.setCutoffReportFilePath(getOutputFilePath(result.getOutputFile3()));
			    		    
			    errorList = new ArrayList();
			    
				
				if("X".equalsIgnoreCase(bean.getForce())) {
					try {
						this.getDomainManagerService().saveRouteNumberGroup(result.getRouteNoSaveInfos());
					} catch(Exception e) {
						e.printStackTrace();
						errorList.add(this.getMessage("app.actionmessage.131", new Object[]{}));
					}
				}
				
				if(errorList == null || errorList.isEmpty()) {
					saveMessage(request, getMessage("app.actionmessage.114", new Object[] { }));
				} else {
					saveErrorMessage(request, errorList);
				}
		    }  else {
				saveErrorMessage(request, errorList);
			}
			
			ModelAndView mav = new ModelAndView(getSuccessView(), errors.getModel());
			mav.getModel().put(this.getCommandName(), command);	
			mav.getModel().putAll(referenceData(request));
			
			return mav;
		  }

		  protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException
		  {
		    // to actually be able to convert Multipart instance to byte[]
		    // we have to register a custom editor
		    logger.info("FileUploadController -- custom editor to convert Multipart instance to byte[] is registered here.");
		    binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		    // now Spring knows how to handle multipart object and convert them
		  }
		  
		  
}
