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
						
		String messageKey = "app.actionmessage.105";
		CopyPlanCommand tmpCommand = (CopyPlanCommand)command;
				
		if(canIgnoreError(tmpCommand)) {
			executeCopyPlan(tmpCommand);
			saveMessage(request, getMessage(messageKey,	new Object[] { getDomainObjectName() }));
		} else {
			PlanValidationResult validationResult = validateCopyPlan((CopyPlanCommand)command);
			
			if(validationResult.isHasSourceData() ) {
				if(validationResult.hasErrors()) {
					saveErrorMessage(request, validationResult);
					tmpCommand.setErrorSourceDate(tmpCommand.getSourceDate());
					tmpCommand.setErrorDestinationDate(tmpCommand.getDestinationDate());
				} else {				
					executeCopyPlan(tmpCommand);
					saveMessage(request, getMessage(messageKey,	new Object[] { getDomainObjectName() }));
				}
			} else {
				saveMessage(request, getMessage("app.actionmessage.112",	new Object[] { }));
			}			
		}
		
		ModelAndView mav = new ModelAndView(getSuccessView());
		mav.getModel().put(this.getCommandName(), command);
		mav.getModel().putAll(referenceData(request));
		return mav;
	}
	
	public void saveErrorMessage(HttpServletRequest request, PlanValidationResult validationResult) {
		List messages = validationResult.getErrorMessages();
		if (messages != null) {
			if(validationResult.getDestinationErrors().isEmpty()) {
				messages.add(getMessage("app.actionmessage.111", new Object[]{}));
			} else {
				messages.add(getMessage("app.actionmessage.110", new Object[]{}));
			}
		}
		saveErrorMessage(request, messages);
	}
	
	private boolean canIgnoreError(CopyPlanCommand tmpCommand) {
		return "true".equalsIgnoreCase(tmpCommand.getIgnoreErrors()) && tmpCommand.getSourceDate().equals(tmpCommand.getErrorSourceDate())
				&& tmpCommand.getDestinationDate().equals(tmpCommand.getErrorDestinationDate());
	}
	
	private void executeCopyPlan(CopyPlanCommand tmpCommand) throws Exception  {
		copyPlan(tmpCommand);
		tmpCommand.setIgnoreErrors(null);
		tmpCommand.setErrorSourceDate(null);
		tmpCommand.setErrorDestinationDate(null);
	}
	
	private PlanValidationResult validateCopyPlan(CopyPlanCommand model) throws ParseException {
		
		PlanValidationResult validationResult = new PlanValidationResult(); 
		List sourceList = new ArrayList();
		List destinationList = new ArrayList();
		boolean hasSourceData = false;
		if(model != null) {
			Date startDate = TransStringUtil.getDate(model.getSourceDate());
			Date endDate = TransStringUtil.getDate(model.getDestinationDate());
			String dayOfWeek = model.getDispatchDay();			
						
			List retList = null;
			List retSourceList = null;
			if(TransStringUtil.isEmpty(dayOfWeek) || "null".equals(dayOfWeek)) {				
				for(int intCount = 0;intCount<7;intCount++) {
					retList = validateCopyPlanForDate(TransStringUtil.addDays(startDate, intCount),
							TransStringUtil.addDays(endDate, intCount));
					retSourceList = (List)retList.get(0);
					sourceList.addAll(retSourceList);
					destinationList.addAll((List)retList.get(1));
					if(retSourceList.isEmpty()) {
						hasSourceData = true;
					}
				}
				
			} else {
				retList = validateCopyPlanForDate(TransStringUtil.addDays(startDate, TransStringUtil.getDayinWeek(dayOfWeek)),
						TransStringUtil.addDays(endDate, TransStringUtil.getDayinWeek(dayOfWeek)));
				retSourceList = (List)retList.get(0);
				sourceList.addAll(retSourceList);
				destinationList.addAll((List)retList.get(1));
				if(retSourceList.isEmpty()) {
					hasSourceData = true;
				}
			}
			
		}
		validationResult.setSourceErrors(sourceList);
		validationResult.setDestinationErrors(destinationList);
		validationResult.setHasSourceData(hasSourceData);
		return validationResult;
	}
	
	private List validateCopyPlanForDate(Date sourceDate, Date destinationDate) throws ParseException  {
		
		String strSourceDate = TransStringUtil.getServerDate(sourceDate);
		String strDestinationDate = TransStringUtil.getServerDate(destinationDate);
		Collection sourceData = dispatchManagerService.getPlanList(strSourceDate);
		Collection destinationData = dispatchManagerService.getPlanList(strDestinationDate);
		List messages = new ArrayList();	
		List sourceList = new ArrayList();
		List destinationList = new ArrayList();
		
		messages.add(sourceList);
		messages.add(destinationList);
		if(!destinationData.isEmpty()) {
			destinationList.add(getMessage("app.actionmessage.107", new Object[]{strDestinationDate}));						
		} 
		
		if(sourceData.isEmpty()) {
			sourceList.add(getMessage("app.actionmessage.108", new Object[]{strSourceDate}));			
		} 
				
		return messages;
	}
	
	private void copyPlan(CopyPlanCommand model) throws ParseException {
		
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
							TransStringUtil.addDays(endDate, intCount), includeEmpDetails);
					deleteList.addAll((List)retList.get(0));
					addList.addAll((List)retList.get(1));
				}
				
			} else {
				retList = copyPlanForDate(TransStringUtil.addDays(startDate, TransStringUtil.getDayinWeek(dayOfWeek)),
						TransStringUtil.addDays(endDate, TransStringUtil.getDayinWeek(dayOfWeek)), includeEmpDetails);
				deleteList.addAll((List)retList.get(0));
				addList.addAll((List)retList.get(1));
			}			
			dispatchManagerService.copyPlan(addList, deleteList);
		}
		
	}
	
	private List copyPlanForDate(Date sourceDate, Date destinationDate,boolean includeEmpDetails) throws ParseException  {
		
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
			tmpNewPlan.setTrnEndTimeslot(tmpPlan.getTrnEndTimeslot());
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
	
	class PlanValidationResult {
		
		private List sourceErrors;
		private List destinationErrors;
		private boolean hasSourceData;
		
		public List getDestinationErrors() {
			return destinationErrors;
		}
		public void setDestinationErrors(List destinationErrors) {
			this.destinationErrors = destinationErrors;
		}
		public boolean isHasSourceData() {
			return hasSourceData;
		}
		public void setHasSourceData(boolean hasSourceData) {
			this.hasSourceData = hasSourceData;
		}
		public List getSourceErrors() {
			return sourceErrors;
		}
		public void setSourceErrors(List sourceErrors) {
			this.sourceErrors = sourceErrors;
		}
		
		public boolean hasErrors() {
			return !sourceErrors.isEmpty() || !destinationErrors.isEmpty();
		}
		
		public List getErrorMessages() {
			List fullList = new ArrayList();
			fullList.addAll(getSourceErrors());
			fullList.addAll(getDestinationErrors());
			return fullList;
		}
	}
	
}