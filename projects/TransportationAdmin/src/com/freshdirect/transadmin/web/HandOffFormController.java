package com.freshdirect.transadmin.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.routing.constants.EnumHandOffBatchStatus;
import com.freshdirect.routing.handoff.action.HandOffRoutingInAction;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.model.TriggerHandOffResult;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.HandOffServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.HandOffCommand;

public class HandOffFormController extends BaseFormController {

	private LocationManagerI locationManagerService;

	private DomainManagerI domainManagerService;

	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}

	public LocationManagerI getLocationManagerService() {
		return locationManagerService;
	}

	public void setLocationManagerService(
			LocationManagerI locationManagerService) {
		this.locationManagerService = locationManagerService;
	}

	protected Map referenceData(HttpServletRequest request)
			throws ServletException {
		Map refData = new HashMap();
		refData.put("scenarios", locationManagerService.getServiceTimeScenarios());
		refData.put("cutoffs", getDomainManagerService().getCutOffs());
		
		Collection areas = getDomainManagerService().getAreas();
		List<TrnArea> areaLookup = new ArrayList<TrnArea>();
		TrnArea tmpArea = null;
		if(areas != null) {
			Iterator iterator = areas.iterator();
			while(iterator.hasNext()) {
				tmpArea = (TrnArea)iterator.next();				
				if(tmpArea != null && "X".equalsIgnoreCase(tmpArea.getIsDepot())) {
					areaLookup.add(tmpArea);
				}
			}
		}
		refData.put("zones", areaLookup);
		return refData;
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		HandOffCommand bean = new HandOffCommand();
		String currentTime = TransStringUtil.getCurrentTime();
		Date routingDate = null;
		if(currentTime != null && currentTime.endsWith("AM")) {
			routingDate = RoutingDateUtil.getCurrentDate();
		} else {
			routingDate = RoutingDateUtil.getNextDate();
		}
		bean.setDeliveryDate(routingDate);
		RoutingInfoServiceProxy proxy = new RoutingInfoServiceProxy();
		IServiceTimeScenarioModel scenario = proxy.getRoutingScenarioByDate(routingDate);
		
		if(scenario != null) {
			bean.setServiceTimeScenario(scenario.getCode());
		}
		return bean;
	}

	protected ModelAndView onSubmit(HttpServletRequest request
										, HttpServletResponse response, Object command
										, BindException errors)
											throws ServletException, IOException {

		HandOffCommand bean = (HandOffCommand) command;
		
		HandOffServiceProxy proxy = new HandOffServiceProxy();
		try {
			String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(request);
			TrnCutOff cutOff = getDomainManagerService().getCutOff(bean.getCutOff());
			Set<IHandOffBatch> batches = proxy.getHandOffBatch(bean.getDeliveryDate());
			boolean hasRunningbatch = false;
			for(IHandOffBatch batch : batches) {
				if(batch != null && !(batch.getStatus().equals(EnumHandOffBatchStatus.CANCELLED)
										|| batch.getStatus().equals(EnumHandOffBatchStatus.COMPLETED))) {
					hasRunningbatch = true;
					saveErrorMessage(request, "There is already a batch running please complete or cancel the existing batch");
					break;
				}
			}
			if(!hasRunningbatch) {
				TriggerHandOffResult triggerResult = proxy.createNewHandOffBatch(bean.getDeliveryDate()
																		, userId, bean.getServiceTimeScenario()
																		, cutOff.getCutOffTime().getAsDate());
				
				if(triggerResult.getHandOffBatchId() != null) {
					IHandOffBatch batch = proxy.getHandOffBatchById(triggerResult.getHandOffBatchId());
					HandOffRoutingInAction routeInManager = new HandOffRoutingInAction(batch, userId);
					
					routeInManager.execute();
				}
				saveErrorMessage(request, triggerResult.getMessage());
			}
		} catch (RoutingServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveErrorMessage(request, e.getMessage());
		}
		
		ModelAndView mav = new ModelAndView(getSuccessView(), errors.getModel());
		mav.getModel().put(this.getCommandName(), command);
		mav.getModel().putAll(referenceData(request));

		return mav;
	}

}
