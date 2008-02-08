package com.freshdirect.transadmin.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.transadmin.model.TrnDispatchPlan;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.CopyPlanCommand;

public class CopyPlanFormController extends AbstractFormController {
			
	private DomainManagerI domainManagerService;
	
	private DispatchManagerI dispatchManagerService;
	
	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}

	protected Map referenceData(HttpServletRequest request) throws ServletException {
		Map refData = new HashMap();
		refData.put("days", domainManagerService.getDays());
		return refData;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		
		System.out.println("<<<<<<<<<<<< COPYPLAN ONSUBMIT CALLED >>>>>>>>>>>>>>>>>>>");
		//List messages = new ArrayList();
		String messageKey = "app.actionmessage.105";
		
		//copyPlan((CopyPlanCommand)command, messages);
		List messages = validateCopyPlan((CopyPlanCommand)command);
		ModelAndView mav = new ModelAndView(getSuccessView());
		mav.getModel().put(this.getCommandName(), command);
		mav.getModel().putAll(referenceData(request));
		
		if(messages.isEmpty() || "true".equalsIgnoreCase(((CopyPlanCommand)command).getIgnoreErrors())) {
			copyPlan((CopyPlanCommand)command, messages);
			((CopyPlanCommand)command).setIgnoreErrors(null);
			saveMessage(request, getMessage(messageKey,
				new Object[] { getDomainObjectName() }));
		} else {
			saveErrorMessage(request, messages);
		}
		return mav;
	}
	
	private List validateCopyPlan(CopyPlanCommand model) throws ParseException {
		
		List returnList = new ArrayList(); 
		if(model != null) {
			Date startDate = TransStringUtil.getDate(model.getSourceDate());
			Date endDate = TransStringUtil.getDate(model.getDestinationDate());
			String dayOfWeek = model.getDispatchDay();			
			
			if(TransStringUtil.isEmpty(dayOfWeek) || "null".equals(dayOfWeek)) {				
				for(int intCount = 0;intCount<7;intCount++) {
					returnList.addAll(validateCopyPlanForDate(TransStringUtil.addDays(startDate, intCount),
							TransStringUtil.addDays(endDate, intCount)));
				}
				
			} else {
				returnList.addAll(validateCopyPlanForDate(TransStringUtil.addDays(startDate, TransStringUtil.getDayinWeek(dayOfWeek)),
						TransStringUtil.addDays(endDate, TransStringUtil.getDayinWeek(dayOfWeek))));
			}
			
		}
		return returnList;
	}
	
	private List validateCopyPlanForDate(Date sourceDate, Date destinationDate) throws ParseException  {
		
		String strSourceDate = TransStringUtil.getServerDate(sourceDate);
		String strDestinationDate = TransStringUtil.getServerDate(destinationDate);
		Collection sourceData = dispatchManagerService.getPlanList(strSourceDate);
		Collection destinationData = dispatchManagerService.getPlanList(strDestinationDate);
		List messages = new ArrayList();		
		if(!destinationData.isEmpty()) {
			messages.add("Destination Plan already exists for date "+strDestinationDate);			
		} 
		
		if(sourceData.isEmpty()) {
			messages.add("Source Plan does not exists for date "+strSourceDate);			
		} 
				
		return messages;
	}
	
	private void copyPlan(CopyPlanCommand model, List messages) throws ParseException {
		
		if(model != null) {
			Date startDate = TransStringUtil.getDate(model.getSourceDate());
			Date endDate = TransStringUtil.getDate(model.getDestinationDate());
			String dayOfWeek = model.getDispatchDay();
			boolean includeEmpDetails = "true".equalsIgnoreCase(model.getIncludeEmployees());
			List deleteList = new ArrayList();
			List addList = new ArrayList();
			List retList = null;
			if(TransStringUtil.isEmpty(dayOfWeek) || "null".equals(dayOfWeek)) {				
				for(int intCount = 0;intCount<7;intCount++) {
					retList = copyPlanForDate(TransStringUtil.addDays(startDate, intCount),
							TransStringUtil.addDays(endDate, intCount), messages, includeEmpDetails);
					deleteList.addAll((List)retList.get(0));
					addList.addAll((List)retList.get(1));
				}
				
			} else {
				retList = copyPlanForDate(TransStringUtil.addDays(startDate, TransStringUtil.getDayinWeek(dayOfWeek)),
						TransStringUtil.addDays(endDate, TransStringUtil.getDayinWeek(dayOfWeek)), messages, includeEmpDetails);
				deleteList.addAll((List)retList.get(0));
				addList.addAll((List)retList.get(1));
			}			
			dispatchManagerService.copyPlan(addList, deleteList);
		}
		
	}
	
	private List copyPlanForDate(Date sourceDate, Date destinationDate, List messages,boolean includeEmpDetails) throws ParseException  {
		
		String strSourceDate = TransStringUtil.getServerDate(sourceDate);
		String strDestinationDate = TransStringUtil.getServerDate(destinationDate);
		Collection sourceData = dispatchManagerService.getPlanList(strSourceDate);
		Collection destinationData = dispatchManagerService.getPlanList(strDestinationDate);
		List returnList = new ArrayList();
		
		List deleteList = new ArrayList();
		List addList = new ArrayList();
		returnList.add(deleteList);
		returnList.add(addList);
		
		if(!destinationData.isEmpty()) {
			messages.add("Destination Plan already exists for date "+strDestinationDate);
			deleteList.addAll(destinationData);
		} 
				
		Iterator iterator = sourceData.iterator();
		TrnDispatchPlan tmpPlan = null;
		TrnDispatchPlan tmpNewPlan = null;
		while(iterator.hasNext()) {
			tmpPlan = (TrnDispatchPlan)iterator.next();
			tmpNewPlan = new TrnDispatchPlan();
			tmpNewPlan.setDispatchDay(tmpPlan.getDispatchDay());				
			tmpNewPlan.setTrnZone(tmpPlan.getTrnZone());
			tmpNewPlan.setTrnTimeslot(tmpPlan.getTrnTimeslot());
			if(includeEmpDetails) {
				tmpNewPlan.setTrnDriver(tmpPlan.getTrnDriver());
				tmpNewPlan.setTrnPrimaryHelper(tmpPlan.getTrnPrimaryHelper());
				tmpNewPlan.setTrnSecondaryHelper(tmpPlan.getTrnSecondaryHelper());
				tmpNewPlan.setTrnSupervisor(tmpPlan.getTrnSupervisor());
			}
			
			tmpNewPlan.setPlanDate(destinationDate);
			addList.add(tmpNewPlan);
		}
		return returnList;
	}
	
	public Object getBackingObject(String id) {
		return new CopyPlanCommand();
	}
	
	public Object getDefaultBackingObject() {
		return new CopyPlanCommand();
	}
	
	public boolean isNew(Object command) {
		return false;
	}
	
	public String getDomainObjectName() {
		return "Copy Plan";
	}
	
	public List saveDomainObject(Object domainObject) {	
		return null;
	}
	
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}	
	
	
	
}