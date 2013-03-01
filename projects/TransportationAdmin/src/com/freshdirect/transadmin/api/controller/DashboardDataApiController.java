package com.freshdirect.transadmin.api.controller;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.framework.service.ServiceException;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.transadmin.api.model.ListDataMessage;
import com.freshdirect.transadmin.api.model.Message;
import com.freshdirect.transadmin.model.PlantCapacity;
import com.freshdirect.transadmin.model.PlantDispatch;
import com.freshdirect.transadmin.service.IDashboardManager;
import com.freshdirect.transadmin.util.TransStringUtil;

public class DashboardDataApiController extends BaseApiController {
	
	private static Logger LOGGER = LoggerFactory.getInstance(DashboardDataApiController.class);

	private static final String ACTION_GET_PLANTCAPACITY = "getPlantCapacity";
	
	private static final String ACTION_SAVE_PLANTCAPACITY = "savePlantCapacity";
	
	private static final String ACTION_GET_PLANTDISPATCH = "getPlantDispatch";
	
	private static final String ACTION_SAVE_PLANTDISPATCH = "savePlantDispatch";
	
	
	private IDashboardManager dashboardManagerService;

	public IDashboardManager getDashboardManagerService() {
		return dashboardManagerService;
	}

	public void setDashboardManagerService(
			IDashboardManager dashboardManagerService) {
		this.dashboardManagerService = dashboardManagerService;
	}
	
	@Override
	protected ModelAndView processRequest(HttpServletRequest request,
			HttpServletResponse response, ModelAndView model, String action) {
		if (ACTION_GET_PLANTCAPACITY.equals(action)) {

			ListDataMessage result = new ListDataMessage();
			String dayofweek = request.getParameter("dayOfWeek");
			try{
				Collection capacities = getDashboardManagerService().getPlantCapacity(dayofweek);
				result.setRows(capacities);
			}catch (ServiceException e) {
				e.printStackTrace();
				setResponseMessage(model, Message.createFailureMessage("Get Plant Capacity failed."));
			}catch (Exception e) {
				e.printStackTrace();
				setResponseMessage(model, Message.createFailureMessage("Get Plant Capacity failed."));
			}
			setResponseMessage(model, result);
			
		}
		else if(ACTION_SAVE_PLANTCAPACITY.equals(action)) {

			try{
	
			PlantCapacity[] capacities  =  parseRequestObject(request, response, PlantCapacity[].class);
			String dayofweek = request.getParameter("dayOfWeek");
			getDashboardManagerService().savePlantCapacity(dayofweek, Arrays.asList(capacities));
			setResponseMessage(model, Message.createSuccessMessage("Save Plant Capacity successful."));
			}
			catch (ServiceException e) {
				e.printStackTrace();
				setResponseMessage(model, Message.createFailureMessage("Save Plant Capacity failed."));
			}catch (Exception e) {
				e.printStackTrace();
				setResponseMessage(model, Message.createFailureMessage("Save Plant Capacity failed."));
			}
		}else if(ACTION_GET_PLANTDISPATCH.equals(action)) {

			try{
				ListDataMessage result = new ListDataMessage();
				Collection dispatches = getDashboardManagerService().getPlantDispatch();
				result.setRows(dispatches);
				setResponseMessage(model, result);
			} 
			catch (ServiceException e) {
				e.printStackTrace();
				setResponseMessage(model, Message.createFailureMessage("Get Plant Dispatch failed."));
			}catch (Exception e) {
				e.printStackTrace();
				setResponseMessage(model, Message.createFailureMessage("Get Plant Dispatch failed."));
			}
		}else if(ACTION_SAVE_PLANTDISPATCH.equals(action)) {

			try{
				PlantDispatch[] dispatches  =  parseRequestObject(request, response, PlantDispatch[].class);
				getDashboardManagerService().savePlantDispatch(Arrays.asList(dispatches));
				setResponseMessage(model, Message.createSuccessMessage("Save Plant Dispatch successful."));
			}
			catch (ServiceException e) {
				e.printStackTrace();
				setResponseMessage(model, Message.createFailureMessage("Save Plant Dispatch failed."));
			}catch (Exception e) {
				e.printStackTrace();
				setResponseMessage(model, Message.createFailureMessage("Save Plant Dispatch failed."));
			}
		}
		return model;
	}
	
}
