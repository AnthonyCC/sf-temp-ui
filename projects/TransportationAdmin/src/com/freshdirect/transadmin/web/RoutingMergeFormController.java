package com.freshdirect.transadmin.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
import com.freshdirect.transadmin.datamanager.RouteMergeDataManager;
import com.freshdirect.transadmin.datamanager.RoutingResult;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnZone;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.web.model.RoutingMergeCommand;

public class RoutingMergeFormController extends BaseFormController {
	
	private DomainManagerI domainManagerService;
	
	protected Object formBackingObject(HttpServletRequest request)
		throws Exception {
				
		RoutingMergeCommand bean = new RoutingMergeCommand();		
		bean.setReferenceData(getZoneString());
		return bean;
	}
	
	protected Map referenceData(HttpServletRequest request) throws ServletException {
		
		Map refData = new HashMap();		
		refData.put("cutoffs", getDomainManagerService().getCutOffs());				
		return refData;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,HttpServletResponse response,
		      Object command, BindException errors) throws ServletException, IOException
		  {
		    // cast the bean
		    logger.info("Merge FileUploadController -- executing onSubmit!");
		    RoutingMergeCommand bean = (RoutingMergeCommand) command;
		    	   	   
		    Map paramMap = new HashMap();
		    paramMap.put(IRoutingParamConstants.ROUTING_CUTOFF, bean.getCutOff());
		    
		    RouteMergeDataManager manager = new RouteMergeDataManager();
		    RoutingResult result = manager.process(bean.getFile1(), bean.getFile2()
		    													, bean.getFile3()
		    													, com.freshdirect.transadmin.security.SecurityManager.getUserName(request)
		    													, paramMap, getDomainManagerService());
		    
		    List errorList = result.getErrors();
		    
		    if(errorList == null || errorList.isEmpty()) {
			    bean.setOutputFile1(result.getOutputFile1());
			    bean.setOutputFile2(result.getOutputFile2());
			    		    
			    errorList = new ArrayList();
			    
				
				if("X".equalsIgnoreCase(bean.getForce())) {
					try {
						this.getDomainManagerService().saveEntityList(result.getRouteNoSaveInfos());
					} catch(Exception e) {
						e.printStackTrace();
						errorList.add(this.getMessage("app.actionmessage.131", new Object[]{}));
					}
				}
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
	
	
	
	private String getZoneString() {
		
		StringBuffer strBuf = new StringBuffer();
		Collection dataList = domainManagerService.getZones();
		TrnZone tmpZone = null;
		TrnArea tmpArea = null;
		if(dataList != null) {
			Iterator iterator = dataList.iterator();	
			while(iterator.hasNext()) {
				tmpZone = (TrnZone)iterator.next();
				tmpArea = tmpZone.getTrnArea();
				if(tmpArea != null && "X".equalsIgnoreCase(tmpArea.getActive())) {
					if(strBuf.length() != 0) {
						strBuf.append(", ");
					}
					strBuf.append(tmpZone.getZoneNumber());
				}
			}			
		}
		return strBuf.toString();
	}

	  protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException	  {
	    // to actually be able to convert Multipart instance to byte[]
	    // we have to register a custom editor
	    logger.info("FileUploadController -- custom editor to convert Multipart instance to byte[] is registered here.");
	    binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	    // now Spring knows how to handle multipart object and convert them
	  }

	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}

		
}
