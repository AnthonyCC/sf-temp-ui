package com.freshdirect.transadmin.web.json;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.routing.handoff.action.HandOffAutoDispatchAction;
import com.freshdirect.routing.handoff.action.HandOffCancelAction;
import com.freshdirect.routing.handoff.action.HandOffCommitAction;
import com.freshdirect.routing.handoff.action.HandOffRoutingInAction;
import com.freshdirect.routing.handoff.action.HandOffRoutingOutAction;
import com.freshdirect.routing.handoff.action.HandOffStopAction;
import com.freshdirect.routing.model.HandOffBatchDepotSchedule;
import com.freshdirect.routing.model.HandOffBatchDepotScheduleEx;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchDepotSchedule;
import com.freshdirect.routing.model.IHandOffBatchDepotScheduleEx;
import com.freshdirect.routing.model.IHandOffBatchDispatchResource;
import com.freshdirect.routing.model.IHandOffBatchPlan;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.HandOffServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.exception.TransAdminServiceException;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.HandOffBatchInfo;

public class HandOffProviderController extends BaseJsonRpcController  implements IHandOffProvider {
	private static Logger LOGGER = LoggerFactory.getInstance(HandOffProviderController.class);
	
	private DispatchManagerI dispatchManagerService;
	
	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public void setDispatchManagerService(
			DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}
	
	public HandOffBatchInfo getHandOffBatchById(String batchId) {
				
		HandOffServiceProxy proxy = new HandOffServiceProxy();
		try {
			IHandOffBatch batch = proxy.getHandOffBatchById(batchId);
			if(batch.getDepotSchedule() == null || batch.getDepotSchedule().size() == 0) {
				String dayOfWeek = TransStringUtil.getServerDay(batch.getDeliveryDate());
				
				Set<IHandOffBatchDepotScheduleEx> tmpDepotScheduleEx = proxy.getHandOffBatchDepotSchedulesEx(dayOfWeek
																											, batch.getCutOffDateTime());
				Set<IHandOffBatchDepotSchedule> depotSchInfo = new TreeSet<IHandOffBatchDepotSchedule>();
				
				
				
				for(IHandOffBatchDepotScheduleEx tmpDptSchEx : tmpDepotScheduleEx) {
					HandOffBatchDepotSchedule tmpDptSchedule = new HandOffBatchDepotSchedule(batch.getBatchId(), tmpDptSchEx.getArea()
					           				                                          , tmpDptSchEx.getDepotArrivalTime()
					           				                                          , tmpDptSchEx.getTruckDepartureTime()
					           				                                          , tmpDptSchEx.getOriginId());
					depotSchInfo.add(tmpDptSchedule);					
				}
				batch.setDepotSchedule(depotSchInfo);
			}
			return new HandOffBatchInfo(batch);
			
		} catch (RoutingServiceException e) {
			LOGGER.error("routing service exception", e);
		} catch (ParseException e) {
			LOGGER.error("parse exception", e);
		} 
		return null;
	}

	public List<HandOffBatchInfo> getHandOffBatch(String deliveryDate) {
		
		List<HandOffBatchInfo> result = new ArrayList<HandOffBatchInfo>();
		HandOffServiceProxy proxy = new HandOffServiceProxy();
		try {
			Set<IHandOffBatch> lstBatch = null;
			if(TransStringUtil.isEmpty(deliveryDate)) {
				lstBatch = proxy.getHandOffBatch(null);
			} else {
				lstBatch = proxy.getHandOffBatch(TransStringUtil.getDate(deliveryDate));
			}			
			
			if(lstBatch != null) {
				for(IHandOffBatch batch : lstBatch) {
					
					HandOffBatchInfo _info = new HandOffBatchInfo(batch);
					result.add(_info);
					
				}
			}
		} catch (RoutingServiceException e) {
			LOGGER.error("routing service exception", e);
		} catch (ParseException e) {
			LOGGER.error("parse exception", e);
		}
		Collections.sort(result, new HandOffBatchInfoComparator());
		return result;
	}
	
	public boolean doRoutingOut(String handOffBatchId, String[][] schedule) {
		String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());
		HandOffServiceProxy proxy = new HandOffServiceProxy();
		IHandOffBatch batch;
		try {
			
			batch = proxy.getHandOffBatchById(handOffBatchId);
			Set<IHandOffBatchDepotSchedule> depotSchInfo = new TreeSet<IHandOffBatchDepotSchedule>();
			Set<IHandOffBatchDepotScheduleEx> depotSchInfoEx = new TreeSet<IHandOffBatchDepotScheduleEx>();
			
			String deliveryDate = TransStringUtil.getDate(batch.getDeliveryDate());
			String dayOfWeek = TransStringUtil.getServerDay(batch.getDeliveryDate());
			
			for(int intCount =0; intCount < schedule.length; intCount++) {
				HandOffBatchDepotSchedule tmpDptSchedule = new HandOffBatchDepotSchedule(batch.getBatchId(), schedule[intCount][0]
				           				                                          , TransStringUtil.getDatewithTime(deliveryDate+" "+schedule[intCount][1])
				           				                                          , TransStringUtil.getDatewithTime(deliveryDate+" "+schedule[intCount][2])
				           				                                          , schedule[intCount][3]);
				depotSchInfo.add(tmpDptSchedule);	
				depotSchInfoEx.add(new HandOffBatchDepotScheduleEx(dayOfWeek
																	, batch.getCutOffDateTime()
																	, tmpDptSchedule.getArea()
																	, tmpDptSchedule.getDepotArrivalTime()
																	, tmpDptSchedule.getTruckDepartureTime()
																	, tmpDptSchedule.getOriginId()));
			}
			batch.setDepotSchedule(depotSchInfo);
			HandOffRoutingOutAction process = new HandOffRoutingOutAction(batch, userId, dayOfWeek, depotSchInfoEx);
			process.execute();
		} catch (RoutingServiceException e) {
			LOGGER.error("routing service exception", e);
		} catch (ParseException e) {
			LOGGER.error("parse exception", e);
		}
		
		return true;
	}
	
	public boolean doRoutingIn(String handOffBatchId) {
		String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());
		HandOffServiceProxy proxy = new HandOffServiceProxy();
		IHandOffBatch batch;
		try {
			batch = proxy.getHandOffBatchById(handOffBatchId);
			HandOffRoutingInAction process = new HandOffRoutingInAction(batch, userId);
			process.execute();
		} catch (RoutingServiceException e) {
			LOGGER.error("routing service exception", e);
		}
		
		return true;
	}
	
	public boolean doHandOffCancel(String handOffBatchId) {
		String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());
		HandOffServiceProxy proxy = new HandOffServiceProxy();
		IHandOffBatch batch;
		try {
			batch = proxy.getHandOffBatchById(handOffBatchId);
			HandOffCancelAction process = new HandOffCancelAction(batch, userId);
			process.execute();
		} catch (RoutingServiceException e) {
			LOGGER.error("routing service exception", e);
		}
		
		return true;
	}
	
	public boolean doHandOffStop(String handOffBatchId) {
		String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());
		HandOffServiceProxy proxy = new HandOffServiceProxy();
		IHandOffBatch batch;
		try {
			batch = proxy.getHandOffBatchById(handOffBatchId);
			HandOffStopAction process = new HandOffStopAction(batch, userId);
			process.execute();
		} catch (RoutingServiceException e) {
			LOGGER.error("routing service exception", e);
		}
		
		return true;
	}
	
	public String doHandOffCommit(String handOffBatchId, boolean force, boolean isCommitCheck) {
		String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());
		HandOffServiceProxy proxy = new HandOffServiceProxy();
		IHandOffBatch batch;
		
		try {
			batch = proxy.getHandOffBatchById(handOffBatchId);
			HandOffCommitAction process = new HandOffCommitAction(batch, userId, force, isCommitCheck);
			@SuppressWarnings("unchecked")
			Map<String, EnumSaleStatus> exceptions = (Map<String, EnumSaleStatus>) process.execute();
			
			boolean hasError = (exceptions != null && exceptions.keySet().size() > 0);
			if(hasError) {
				StringBuffer exceptionMessage = new StringBuffer();
				exceptionMessage.append("Below are the list of order exceptions");
				for(Map.Entry<String, EnumSaleStatus> exp : exceptions.entrySet()) {
					
					exceptionMessage.append("\n").append(exp.getKey()+"="+exp.getValue().getName());
				}
				if(isCommitCheck) {
					exceptionMessage.append("\n\n"+"Report will not contain these orders. Do you want to continue?");
				} else {
					exceptionMessage.append("\n\n"+"Do you want to force the commit?");
				}
				return exceptionMessage.toString();
			}
			
			if(!isCommitCheck) {
				ErpMailSender emailer = new ErpMailSender();
				emailer.sendMail(RoutingServicesProperties.getHandOffMailFrom(), RoutingServicesProperties.getHandOffMailTo()
											, RoutingServicesProperties.getHandOffMailCC(), RoutingServicesProperties.getHandOffMailSubject()
											, process.getProcessResponse());
			}
			
		} catch (RoutingServiceException e) {
			LOGGER.error("routing service exception", e);
		}  catch (MessagingException e) {
			LOGGER.error("messaging exception", e);
		}
		
		return null;
	}
	
	public boolean doHandOffAutoDispatch(String handOffBatchId, boolean isBullpen) {
		LOGGER.info("doHandOffAutoDispatch were called, id: " + handOffBatchId);
		String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());
		HandOffServiceProxy proxy = new HandOffServiceProxy();
		IHandOffBatch batch;
		
		try {
			
			batch = proxy.getHandOffBatchById(handOffBatchId);			
				
			List<IHandOffBatchPlan> batchPlans = new ArrayList<IHandOffBatchPlan>();			
			List<IHandOffBatchDispatchResource> batchPlanResources = new ArrayList<IHandOffBatchDispatchResource>();			

			batchPlans = proxy.getHandOffBatchPlansByDispatchStatus(batch.getBatchId(), batch.getDeliveryDate());
			batchPlans.addAll(proxy.getHandOffBatchTrailerPlans(batch.getDeliveryDate(), batch.getCutOffDateTime()));
			batchPlanResources =  proxy.getHandOffBatchPlanResourcesByDispatchStatus(batch.getBatchId(), batch.getDeliveryDate());
			batchPlanResources.addAll(proxy.getHandOffBatchTrailerPlanResource(batch.getDeliveryDate(), batch.getCutOffDateTime()));

			Map<String, Set<IHandOffBatchDispatchResource>> resourceMapping = new HashMap<String, Set<IHandOffBatchDispatchResource>>();
			Iterator<IHandOffBatchDispatchResource> itr = batchPlanResources.iterator();
			while(itr.hasNext()){
				IHandOffBatchDispatchResource batchPlanResource = itr.next();
				if(!resourceMapping.containsKey(batchPlanResource.getPlanId())){
					resourceMapping.put(batchPlanResource.getPlanId(), new HashSet<IHandOffBatchDispatchResource>());
				}
				resourceMapping.get(batchPlanResource.getPlanId()).add(batchPlanResource);				
			}
			
			Iterator<IHandOffBatchPlan> planItr = batchPlans.iterator();
			while(planItr.hasNext()){
				IHandOffBatchPlan batchPlan = planItr.next();
				batchPlan.setBatchPlanResources(resourceMapping.get(batchPlan.getPlanId()));
			}			
			
			HandOffAutoDispatchAction process = new HandOffAutoDispatchAction(batch, userId, batchPlans);
			LOGGER.info("dispatch action process created and being executed: " + process.getBatch().getBatchId());
			process.execute();
		} catch (RoutingServiceException e) {			
			LOGGER.warn("routing service exception", e);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warn("generic exception", e);
		}
		
		return true;
	}
	
	public String getServiceTimeScenario(String deliveryDate) {
		RoutingInfoServiceProxy proxy = new RoutingInfoServiceProxy();
		IServiceTimeScenarioModel scenario;
		try {
			scenario = proxy.getRoutingScenarioByDate(TransStringUtil.getDate(deliveryDate));
			if(scenario != null) {
				return scenario.getCode();
			}
		} catch (RoutingServiceException e) {
			LOGGER.error("routing service exception", e);
		} catch (ParseException e) {
			LOGGER.error("parse exception", e);
		}		
		return null;
	}
	
	private class HandOffBatchInfoComparator implements Comparator<HandOffBatchInfo> {

		public int compare(HandOffBatchInfo batch1, HandOffBatchInfo batch2) {
			if(batch1.getLastActionDateTime() != null &&  batch2.getLastActionDateTime() != null) {
				return -(batch1.getLastActionDateTime().compareTo(batch2.getLastActionDateTime()));
			}
			return 0;
		}

	}
	
}
