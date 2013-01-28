package com.freshdirect.transadmin.api.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.transadmin.api.model.ListDataMessage;
import com.freshdirect.transadmin.model.PlantCapacity;
import com.freshdirect.transadmin.service.IDashboardManager;
import com.freshdirect.transadmin.util.TransStringUtil;

public class DashboardDataApiController extends BaseApiController {
	
	private static Logger LOGGER = LoggerFactory.getInstance(DashboardDataApiController.class);

	private static final String ACTION_GET_PLANTCAPACITY = "getPlantCapacity";
	
	private static final String SAVE_PLANTCAPACITY = "savePlantCapacity";
	
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
			String dayofweek;
			try {
				dayofweek = TransStringUtil.getServerDay(new Date());
				Collection capacities = getDashboardManagerService().getPlantCapacity(dayofweek);
				result.setRows(capacities);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			setResponseMessage(model, result);
			
		}
		else if(SAVE_PLANTCAPACITY.equals(action)) {

			ListDataMessage result = new ListDataMessage();
			Collection rows = result.getRows();
			String dayofweek = "";
			try {
				dayofweek = TransStringUtil.getServerDay(new Date());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<PlantCapacity> capacities = new ArrayList<PlantCapacity>();
			for(Object row: rows){
				PlantCapacity plantCapacity = new PlantCapacity();
				//plantCapacity.setDayOfWeek(();
				//plantCapacity.setDispatchTime(dispatchTime);
				//plantCapacity.setCapacity(capacity);	
				capacities.add(plantCapacity);
			}
			
		
			getDashboardManagerService().savePlantCapacity(dayofweek, capacities);
		}
		return model;
	}
	
}
