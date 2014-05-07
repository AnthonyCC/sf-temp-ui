package com.freshdirect.transadmin.notification;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import com.freshdirect.routing.constants.EnumHandOffBatchStatus;
import com.freshdirect.routing.constants.EnumWaveInstanceStatus;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.model.WaveSyncLockActivity;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.HandOffServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
import com.freshdirect.routing.util.RoutingDateUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.HandOffCommand;

public class NotificationManager {
	
	private static NotificationManager instance = null;
	
	private NotificationManager() {
		
	}
	
	public static NotificationManager getInstance() {
	      if(instance == null) {
	         instance = new NotificationManager();
	      }
	      return instance;
	}
	
	public List<String> getNotification(ServletRequest request) {		
		
		List<String> messages = new ArrayList<String>();
		RoutingInfoServiceProxy routingProxy = new RoutingInfoServiceProxy();
		try {
			// wave(s) status notification message
			List<IWaveInstance> waveInstances = routingProxy.getWaveInstanceWithErrors();
			Map<Date, List<String>> dynamicEnabledZoneMpp = routingProxy.getDynamicEnabledZoneMapping();
			if(waveInstances != null && dynamicEnabledZoneMpp != null) {			
				int notSyncCount = 0;
				int errorCount = 0;
				for(IWaveInstance waveInstance : waveInstances) {
					if(waveInstance.getArea() != null && dynamicEnabledZoneMpp.containsKey(waveInstance.getDeliveryDate())
							&& dynamicEnabledZoneMpp.get(waveInstance.getDeliveryDate()).contains(waveInstance.getArea().getAreaCode())) {
						if(EnumWaveInstanceStatus.NOTSYNCHRONIZED.equals(waveInstance.getStatus())) {
							notSyncCount++;
						}
						if(waveInstance.getNotificationMessage() != null && waveInstance.getNotificationMessage().length() > 0) {
							errorCount++;
						}
					}
				}
				if(notSyncCount > 0 || errorCount > 0) {
					messages.add(notSyncCount+" Waves not in Sync / "+errorCount+" Wave Sync Errors");
				}
			}
			
			// handoff status notification message
			HandOffServiceProxy handoffProxy = new HandOffServiceProxy();
			
			HandOffCommand bean = new HandOffCommand();
			String currentTime = TransStringUtil.getCurrentTime();
			Date routingDate = null;
			if(currentTime != null && currentTime.endsWith("AM")) {
				routingDate = RoutingDateUtil.getCurrentDate();
			} else {
				routingDate = RoutingDateUtil.getNextDate();
			}
			bean.setDeliveryDate(routingDate);
			
			Set<IHandOffBatch> batches = handoffProxy.getHandOffBatch(bean.getDeliveryDate());			
			for(IHandOffBatch batch : batches) {
				if(batch != null && batch.getStatus().equals(EnumHandOffBatchStatus.COMPLETED)) {
					messages.add("Handoff committed - No Auto dispatch");
				}
			}
						
			// wave sync lock notification message
			WaveSyncLockActivity waveSyncLockActivity = routingProxy.isWaveSyncronizationLocked();
			if(waveSyncLockActivity != null && waveSyncLockActivity.getLockDateTime() != null) {
				try {
					messages.add("Wave Syncronization locked by User: "+waveSyncLockActivity.getInitiator()+" at "+ TransStringUtil.getDatewithTime(waveSyncLockActivity.getLockDateTime()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} catch (RoutingServiceException exp) {
			exp.printStackTrace();
		}
		return  messages;
    }
	
}
