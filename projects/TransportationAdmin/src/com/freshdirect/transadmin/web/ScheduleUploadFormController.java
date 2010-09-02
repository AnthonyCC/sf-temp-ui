package com.freshdirect.transadmin.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.routing.util.RoutingDateUtil;

import com.freshdirect.transadmin.datamanager.ScheduleUploadDataManager;

import com.freshdirect.transadmin.model.ScheduleEmployee;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;

import com.freshdirect.transadmin.web.model.ScheduleFileUploadCommand;

public class ScheduleUploadFormController extends BaseFormController {
	
	private LocationManagerI locationManagerService;
	private DomainManagerI domainManagerService;
		
	public LocationManagerI getLocationManagerService() {
		return locationManagerService;
	}
	
	public void setLocationManagerService(LocationManagerI locationManagerService) {
		this.locationManagerService = locationManagerService;
	}
	
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}
	
	protected Map referenceData(HttpServletRequest request)
													throws ServletException {
			Map refData = new HashMap();
			return refData;
	}

	protected Object formBackingObject(HttpServletRequest request)
		throws Exception {
		
		String processType = request.getParameter("processType");		
		ScheduleFileUploadCommand bean = new ScheduleFileUploadCommand();
		bean.setProcessType(processType);
		String currentTime = TransStringUtil.getCurrentTime();		
		return bean;
	}

	protected ModelAndView onSubmit(HttpServletRequest request,HttpServletResponse response,
		      Object command, BindException errors) throws ServletException, IOException
    {
			
		ScheduleFileUploadCommand bean = (ScheduleFileUploadCommand) command;
		byte[] bytes = bean.getFile();

		List<ScheduleEmployee> schedules = new ScheduleUploadDataManager()
										.processUploadSchedules(request, bytes,
														com.freshdirect.transadmin.security.SecurityManager.getUserName(request), domainManagerService);
		if(schedules.size() > 0){
			try{
				getDomainManagerService().saveOrUpdateEmployeeSchedule(schedules);
				saveMessage(request, getMessage("app.actionmessage.161", new Object[]{}));
			}catch(DataIntegrityViolationException ex){
				ex.printStackTrace();
				saveErrorMessage(request, getMessage("app.error.131", new Object[] {}));
			}			
		}else
			saveErrorMessage(request, getMessage("app.error.130", new Object[] {}));
		
		
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
