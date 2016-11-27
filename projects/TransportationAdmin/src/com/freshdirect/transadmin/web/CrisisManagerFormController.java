package com.freshdirect.transadmin.web;

import java.io.IOException;
import java.text.ParseException;
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

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchStatus;
import com.freshdirect.transadmin.constants.EnumCrisisMngBatchType;
import com.freshdirect.routing.constants.EnumDeliveryType;
import com.freshdirect.routing.constants.EnumProfileList;
import com.freshdirect.transadmin.model.ICrisisManagerBatch;
import com.freshdirect.transadmin.web.model.TriggerCrisisManagerResult;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.transadmin.crisis.manager.action.CrisisManagerOrderInAction;
import com.freshdirect.transadmin.model.ICrisisManagerBatchDeliverySlot;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.ICrisisManagerService;
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.service.exception.TransAdminServiceException;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.CrisisManagerCommand;

public class CrisisManagerFormController extends BaseFormController {

	private DomainManagerI domainManagerService;

	private ZoneManagerI zoneManagerService;
	
	private ICrisisManagerService  crisisManagerService;
	
	public ICrisisManagerService getCrisisManagerService() {
		return crisisManagerService;
	}

	public void setCrisisManagerService(ICrisisManagerService crisisManagerService) {
		this.crisisManagerService = crisisManagerService;
	}

	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}
	
	public ZoneManagerI getZoneManagerService() {
		return zoneManagerService;
	}

	public void setZoneManagerService(ZoneManagerI zoneManagerService) {
		this.zoneManagerService = zoneManagerService;
	}

	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request)
			throws ServletException {
		Map refData = new HashMap();
			    
        Collection activeZones = domainManagerService.getZones();
        Collection activeZoneCodes = zoneManagerService.getActiveZoneCodes();
        if(activeZones != null && activeZoneCodes != null) {
        		Iterator _iterator = activeZones.iterator();
        		Zone _tmpZone = null;
        		while(_iterator.hasNext()) {
        			_tmpZone = (Zone)_iterator.next();        			
        			if(!activeZoneCodes.contains(_tmpZone.getZoneCode())) {
        				_iterator.remove();
        			}
        		}
        }
        
        refData.put("deliveryTypes", EnumDeliveryType.getEnumList());
		refData.put("customerTypes", EnumProfileList.getEnumList());
		refData.put("cutoffs", getDomainManagerService().getCutOffs());
		refData.put("zones", activeZones);
		refData.put("regions", getDomainManagerService().getRegions());	
		return refData;
	}

	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		CrisisManagerCommand bean = new CrisisManagerCommand();
		String currentTime = TransStringUtil.getCurrentTime();
		Date selectedDate = null;
		if(currentTime != null && currentTime.endsWith("AM")) {
			selectedDate = RoutingDateUtil.getCurrentDate();
		} else {
			selectedDate = RoutingDateUtil.getNextDate();
		}
		bean.setSelectedDate(selectedDate);				
				
		return bean;
	}

	@SuppressWarnings("unchecked")
	protected ModelAndView onSubmit(HttpServletRequest request
										, HttpServletResponse response, Object command
										, BindException errors)
											throws ServletException, IOException {

		CrisisManagerCommand bean = (CrisisManagerCommand) command;
				
		try {
			String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(request);
			TrnCutOff cutOff = getDomainManagerService().getCutOff(bean.getCutOff());
			Date cutoff = null;
			if(cutOff != null){				
				cutoff = RoutingDateUtil.getNormalDate(RoutingDateUtil.getPreviousDate(bean.getSelectedDate()), cutOff.getCutOffTime().getAsDate());
			}
			String[] deliveryType = StringUtil.decodeStrings(bean.getDeliveryType());
			
			String profileName = null;
			if(!"All".equalsIgnoreCase(bean.getCustomerType())){
				profileName = bean.getCustomerType();
			}
			Date startTime = null;
			Date endTime = null;
			if(bean.getSelectedDate() != null
					&& !TransStringUtil.isEmpty(bean.getStartTime()) && !TransStringUtil.isEmpty(bean.getEndTime())) {
		
				startTime = TransStringUtil.getDatewithTime(TransStringUtil.getDate(bean.getSelectedDate())+" "+bean.getStartTime());
				endTime = TransStringUtil.getDatewithTime(TransStringUtil.getDate(bean.getSelectedDate())+" "+bean.getEndTime());
			}
			
			Set<ICrisisManagerBatch> batches = this.crisisManagerService.getCrisisMngBatch(bean.getSelectedDate());
			boolean hasRunningbatch = false;
			
			for(ICrisisManagerBatch batch : batches) {
				if(batch != null && !(batch.getStatus().equals(EnumCrisisMngBatchStatus.CANCELLED)
										|| batch.getStatus().equals(EnumCrisisMngBatchStatus.COMPLETED))) {
					hasRunningbatch = true;
					saveErrorMessage(request, "There is already a batch running, please complete or cancel the existing batch\n");
					break;
				}
			}
			if(!hasRunningbatch) {
				TriggerCrisisManagerResult triggerResult = this.crisisManagerService.createNewCrisisMngBatch( bean.getSelectedDate()
					, bean.getDestinationDate(), userId, getZoneArray(request), cutoff, startTime, endTime
					, deliveryType,((deliveryType == null) ? EnumCrisisMngBatchType.STANDINGORDER : EnumCrisisMngBatchType.REGULARORDER)
					, profileName, hasRunningbatch);
			
				if(triggerResult.getCrisisMngBatchId() != null) {
					ICrisisManagerBatch batch = this.crisisManagerService.getCrisisMngBatchById(triggerResult.getCrisisMngBatchId());
					CrisisManagerOrderInAction process = new CrisisManagerOrderInAction(batch, userId, this.crisisManagerService);
					process.execute();
					Map<String, List<ICrisisManagerBatchDeliverySlot>> exceptionSlots = 
						this.crisisManagerService.getCrisisMngBatchTimeslot(triggerResult.getCrisisMngBatchId(), false);
					if(exceptionSlots != null && exceptionSlots.size() > 0){
						saveErrorMessage(request, "There are timeslot exceptions for the batch created.");
					}
				}
				saveMessage(request, formatMessages(triggerResult.getMessages()));
			}
			
		} catch (TransAdminServiceException e) {			
			e.printStackTrace();
			saveErrorMessage(request, e.getMessage());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveErrorMessage(request, e.getMessage());
		}
		
		ModelAndView mav = new ModelAndView(getSuccessView(), errors.getModel());
		//mav.getModel().put(this.getCommandName(), command);
		mav.getModel().putAll(referenceData(request));

		return mav;
	}
	
	@SuppressWarnings("unchecked")
	private String[] getZoneArray(HttpServletRequest request){
		
		String[] zoneArray = new String[100];
		String[] regions = StringUtil.decodeStrings(request.getParameter("region"));
		List<String> zoneCodes = new ArrayList<String>();
		Collection activeZoneCodes = zoneManagerService.getActiveZoneCodes();
		if (regions != null && regions.length > 0) {
			Region region = null;
			Set zones = null;
			for (String _regionCode : regions) {
				 region = getDomainManagerService().getRegion(_regionCode);
				 if(region != null){
					 zones = region.getZones();	
					 if(zones != null){
						 Iterator<Zone> itr = zones.iterator();
						 while(itr.hasNext()){
							 Zone _zone = itr.next();
							 if(!activeZoneCodes.contains(_zone.getZoneCode())){
								 itr.remove();
							 }else{
								 zoneCodes.add(_zone.getZoneCode());
							 }
						 }
					 }
				 }
			}
			zoneArray = (String[]) zoneCodes.toArray(new String[0]);
		} else {
			zoneArray = StringUtil.decodeStrings(request.getParameter("zone"));
		}	
		
		return zoneArray;
	}
	
	private String formatMessages(List<String> messages) {
		
		StringBuffer strBuf = new StringBuffer();
		if(messages != null) {
			for(String message : messages) {
				strBuf.append(message).append("<br/> ");
			}
		}
		return strBuf.toString();
	}

}
