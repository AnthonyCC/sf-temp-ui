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

import com.freshdirect.transadmin.datamanager.RouteMergeDataManager;
import com.freshdirect.transadmin.datamanager.RoutingResult;
import com.freshdirect.transadmin.web.model.RSFileMergeCommand;

public class RSFileMergeFormController extends BaseRoutingFormController {
	
		
	protected Object formBackingObject(HttpServletRequest request)
		throws Exception {
				
		RSFileMergeCommand bean = new RSFileMergeCommand();		
		
		return bean;
	}
	
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		
		Map refData = new HashMap();		
						
		return refData;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,HttpServletResponse response,
		      Object command, BindException errors) throws ServletException, IOException
		  {
		    // cast the bean
		    logger.info("Merge RS File -- executing onSubmit!");
		    RSFileMergeCommand bean = (RSFileMergeCommand) command;
    		
		    RouteMergeDataManager manager = new RouteMergeDataManager();
		    RoutingResult result = manager.process(bean, com.freshdirect.transadmin.security.SecurityManager.getUserName(request));
		    List errorList = result.getErrors();
		    
		    if(errorList == null || errorList.isEmpty()) {
			   
		    	bean.setOrderOutputFilePath(getOutputFilePath(result.getOutputFile1()));
			    bean.setTruckOutputFilePath(getOutputFilePath(result.getOutputFile2()));
			   			    		    
			    errorList = new ArrayList();
			    
								
				if(errorList == null || errorList.isEmpty()) {
					saveMessage(request, getMessage("app.actionmessage.114",
							new Object[] { }));
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
	
	
	
	

	  protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException	  {
	    // to actually be able to convert Multipart instance to byte[]
	    // we have to register a custom editor
	    logger.info("FileUploadController -- custom editor to convert Multipart instance to byte[] is registered here.");
	    binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	    // now Spring knows how to handle multipart object and convert them
	  }

}
