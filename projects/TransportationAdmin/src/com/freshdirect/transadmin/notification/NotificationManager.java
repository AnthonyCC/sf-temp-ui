package com.freshdirect.transadmin.notification;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import com.freshdirect.routing.constants.EnumHandOffBatchStatus;
import com.freshdirect.routing.constants.EnumWaveInstanceStatus;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.model.IWaveInstance;
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
	
	public String getWaveNotification(ServletRequest request) {		
		RoutingInfoServiceProxy proxy = new RoutingInfoServiceProxy();
		try {
			List<IWaveInstance> waveInstances = proxy.getWaveInstanceWithErrors();
			Map<Date, List<String>> dynamicEnabledZoneMpp = proxy.getDynamicEnabledZoneMapping();
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
					return notSyncCount+" Waves not in Sync / "+errorCount+" Wave Sync Errors";
				}
			}
		} catch (RoutingServiceException exp) {
			exp.printStackTrace();
		}
		return  null;
    }
	
	public String getHandoffNotification(ServletRequest request) {
		
		HandOffServiceProxy proxy = new HandOffServiceProxy();
		
		HandOffCommand bean = new HandOffCommand();
		String currentTime = TransStringUtil.getCurrentTime();
		Date routingDate = null;
		if(currentTime != null && currentTime.endsWith("AM")) {
			routingDate = RoutingDateUtil.getCurrentDate();
		} else {
			routingDate = RoutingDateUtil.getNextDate();
		}
		bean.setDeliveryDate(routingDate);
		try {
			Set<IHandOffBatch> batches = proxy.getHandOffBatch(bean.getDeliveryDate());			
			for(IHandOffBatch batch : batches) {
				if(batch != null && batch.getStatus().equals(EnumHandOffBatchStatus.COMPLETED)) {
					return "Handoff committed - No Auto dispatch";
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
