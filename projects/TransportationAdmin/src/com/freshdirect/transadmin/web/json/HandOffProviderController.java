package com.freshdirect.transadmin.web.json;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.freshdirect.routing.handoff.action.HandOffCancelAction;
import com.freshdirect.routing.handoff.action.HandOffCommitAction;
import com.freshdirect.routing.handoff.action.HandOffRoutingInAction;
import com.freshdirect.routing.handoff.action.HandOffRoutingOutAction;
import com.freshdirect.routing.handoff.action.HandOffStopAction;
import com.freshdirect.routing.model.HandOffBatchDepotSchedule;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IHandOffBatchDepotSchedule;
import com.freshdirect.routing.model.IServiceTimeScenarioModel;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.HandOffServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.HandOffBatchInfo;

public class HandOffProviderController extends BaseJsonRpcController  implements IHandOffProvider {
	
	public HandOffBatchInfo getHandOffBatchById(String batchId) {
				
		HandOffServiceProxy proxy = new HandOffServiceProxy();
		try {
			return new HandOffBatchInfo(proxy.getHandOffBatchById(batchId));
			
		} catch (RoutingServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			String deliveryDate = TransStringUtil.getDate(batch.getDeliveryDate());
			for(int intCount =0; intCount < schedule.length; intCount++) {
				depotSchInfo.add(new HandOffBatchDepotSchedule(batch.getBatchId(), schedule[intCount][0]
				                                          , TransStringUtil.getDatewithTime(deliveryDate+" "+schedule[intCount][1])
				                                          , TransStringUtil.getDatewithTime(deliveryDate+" "+schedule[intCount][2])));				
			}
			batch.setDepotSchedule(depotSchInfo);
			HandOffRoutingOutAction process = new HandOffRoutingOutAction(batch, userId);
			process.execute();
		} catch (RoutingServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	public boolean doHandOffCommit(String handOffBatchId) {
		String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());
		HandOffServiceProxy proxy = new HandOffServiceProxy();
		IHandOffBatch batch;
		try {
			batch = proxy.getHandOffBatchById(handOffBatchId);
			HandOffCommitAction process = new HandOffCommitAction(batch, userId);
			process.execute();
		} catch (RoutingServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
