package com.freshdirect.transadmin.web;

import java.text.ParseException;
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
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.transadmin.model.TrnDispatch;
import com.freshdirect.transadmin.model.TrnDispatchId;
import com.freshdirect.transadmin.model.TrnDispatchPlan;
import com.freshdirect.transadmin.model.TrnEmployee;
import com.freshdirect.transadmin.model.TrnRoute;
import com.freshdirect.transadmin.model.TrnTimeslot;
import com.freshdirect.transadmin.model.TrnTruck;
import com.freshdirect.transadmin.model.TrnZone;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.AssignmentCommand;
import com.freshdirect.transadmin.web.model.DispatchSheetCommand;

public class AssignmentFormController extends BaseFormController {

	private DomainManagerI domainManagerService;

	private DispatchManagerI dispatchManagerService;
	
	private String zoneIdForRoute = null;

	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
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
		refData.put("zones", domainManagerService.getZones());
		refData.put("drivers", dispatchManagerService.getDrivers());
		refData.put("helpers", dispatchManagerService.getHelpers());
		refData.put("timeslots", domainManagerService.getTimeSlots());
		refData.put("supervisors", getDomainManagerService().getSupervisors());
		refData.put("trucks", getDomainManagerService().getTrucks());
		if(zoneIdForRoute == null || zoneIdForRoute.trim().length() == 0) {
			refData.put("routes", getDomainManagerService().getRoutes());
		} else {
			refData.put("routes", getDomainManagerService().getRouteForZone(zoneIdForRoute));
		}
		zoneIdForRoute = null;
		return refData;
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		AssignmentCommand command = new AssignmentCommand();
		command.setPlanDate(TransStringUtil.getCurrentDate());
		return command;
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		AssignmentCommand model = (AssignmentCommand)command;
		if(model.getSubmitAssignment() != null &&  model.getSubmitAssignment().equalsIgnoreCase("1")) {
			dispatchManagerService.saveEntityList(getDispatchWrapper(model.getPlanDataList(),model.getPlanDate()));
			saveMessage(request, getMessage("app.actionmessage.101",
					new Object[] { "Assignment" }));
		}
		Collection planDataList = dispatchManagerService.getPlan
									(TransStringUtil.getDayofWeek(model.getPlanDate()), model.getZoneId()
											, TransStringUtil.getServerDate(model.getPlanDate()));
				
		model.setPlanDataList(getAssignmentWrapper(planDataList));
		
		zoneIdForRoute = model.getZoneId();
		ModelAndView mav = new ModelAndView(getSuccessView());
		mav.getModel().put("assignmentForm", model);
		mav.getModel().putAll(referenceData(request));		
		return mav;
	}
	
	private List getAssignmentWrapper(Collection dataList) {
		
		List wrapperList = new ArrayList();		
		if(dataList != null) {
			Iterator iterator = dataList.iterator();
			while(iterator.hasNext()) {
				wrapperList.add(new DispatchSheetCommand((TrnDispatchPlan)iterator.next()));
			}
		}
		return wrapperList;
		
	}
	
	private List getDispatchWrapper(Collection dataList, String dispatchDate) throws ParseException {
		
		List wrapperList = new ArrayList();		
		if(dataList != null) {
			Iterator iterator = dataList.iterator();
			DispatchSheetCommand tmpCommand = null;
			TrnDispatch tmpDispatch = null;
			TrnDispatchId tmpId = null;
			while(iterator.hasNext()) {
				tmpCommand = (DispatchSheetCommand)iterator.next();	
				if(tmpCommand.getSelected() != null && tmpCommand.getSelected().equalsIgnoreCase("on")) {
					tmpId = new TrnDispatchId(TransStringUtil.getDate(dispatchDate), tmpCommand.getPlanId());
					tmpDispatch = new TrnDispatch(tmpId);
					tmpDispatch.setTrnSupervisor(new TrnEmployee(tmpCommand.getSupervisorId()));
					tmpDispatch.setTrnZone(new TrnZone(tmpCommand.getZoneId()));
					tmpDispatch.setTrnTimeslot(new TrnTimeslot(tmpCommand.getSlotId()));
					tmpDispatch.setTrnDriver(new TrnEmployee(tmpCommand.getDriverId()));
					tmpDispatch.setTrnPrimaryHelper(new TrnEmployee(tmpCommand.getPrimaryHelperId()));
					tmpDispatch.setTrnSecondaryHelper(new TrnEmployee(tmpCommand.getSecondaryHelperId()));	
					tmpDispatch.setTrnTruck(new TrnTruck(tmpCommand.getTruckId()));
					tmpDispatch.setTrnRoute(new TrnRoute(tmpCommand.getRouteId()));
					tmpDispatch.setNextelId(tmpCommand.getNextelId());
					wrapperList.add(tmpDispatch);
				}
			}
		}
		return wrapperList;
		
	}
}
