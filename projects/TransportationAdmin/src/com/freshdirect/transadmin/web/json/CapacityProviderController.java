package com.freshdirect.transadmin.web.json;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.freshdirect.framework.util.MD5Hasher;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.model.WaveSyncLockActivity;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingInfoServiceProxy;
import com.freshdirect.transadmin.model.WaveInstance;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;

public class CapacityProviderController extends JsonRpcController implements
		ICapacityProvider  {

	private static Logger LOGGER = LoggerFactory.getInstance(CapacityProviderController.class);
	
	private DispatchManagerI dispatchManagerService;
	
	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}

	@Override
	public IOrderModel getRoutingOrderByReservation(String reservationId) {
		// TODO Auto-generated method stub
		try {
			return new DeliveryServiceProxy().getRoutingOrderByReservation(reservationId);
		} catch (RoutingServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}	

	public int updateRoutingOrderByReservation(String reservationId, String orderSize, String serviceTime) {
		
		try {
			if(TransStringUtil.isValidDecimal(orderSize) && TransStringUtil.isValidDecimal(serviceTime)
						&& Double.parseDouble(orderSize) >= 0 && Double.parseDouble(serviceTime) >= 0) {
				return new DeliveryServiceProxy().updateRoutingOrderByReservation(reservationId,
																					Double.parseDouble(orderSize), 
																					Double.parseDouble(serviceTime));
			}
		} catch (RoutingServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int updateTimeslotForStatus(String timeslotId, boolean isClosed, String type, String baseDate, String cutOff) {
		
		try {
			return new DeliveryServiceProxy().updateTimeslotForStatus(timeslotId, isClosed, type, TransStringUtil.getDate(baseDate), cutOff);
		} catch (RoutingServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int forceWaveInstance(String waveInstanceId) {
		
		try {
			WaveInstance _instance = this.getDispatchManagerService().getWaveInstance(waveInstanceId);
			if(_instance != null) {
				if(_instance.getNotificationMessage() != null 
						&& _instance.getNotificationMessage().indexOf("orders will be unassigned") > 0) {
					_instance.setForceSynchronize(true);
					this.getDispatchManagerService().saveEntity(_instance);
					return 1;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public int updateTimeslotForDynamicStatus(String timeslotId, boolean isDynamic, String type, String baseDate, String cutOff, String accessCode) {
		String hashedAccessCode = MD5Hasher.hash(accessCode);
		if(hashedAccessCode != null && hashedAccessCode.equals(TransportationAdminProperties.getDynamicRoutingFeatureAccessKey())) {
			try {
				return new DeliveryServiceProxy().updateTimeslotForDynamicStatus(timeslotId, isDynamic, type, TransStringUtil.getDate(baseDate), cutOff);
			} catch (RoutingServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return 0;
	}
	
	public int flagReRouteReservation(String deliveryDate, String zone) {
		
		try {
			List<String> zones = new ArrayList<String>();
			if(zone != null && !TransStringUtil.isEmpty(zone)) {
				zones.add(zone);
			}
			return new RoutingInfoServiceProxy().flagReRouteReservation(TransStringUtil.getDate(deliveryDate)
																		, zones);
		} catch (RoutingServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int flagReservationStatus(String deliveryDate, String cutOff, String windowStartTime, String windowEndTime, String zone) {
		Date cutoff = null;
		String[] zoneArray = null;
		try {
			if(cutOff != null && !TransStringUtil.isEmpty(cutOff)) {				
				cutoff = TransStringUtil.getServerTime(cutOff);
			}			
			if(zone != null && !TransStringUtil.isEmpty(zone)) {
				zoneArray = StringUtil.decodeStrings(zone);
			}
			if(windowStartTime == null || TransStringUtil.isEmpty(windowStartTime)) {
				windowStartTime = null;
			}
			if(windowEndTime == null || TransStringUtil.isEmpty(windowEndTime)) {
				windowEndTime = null;
			}
			return new RoutingInfoServiceProxy().flagReservationStatus(TransStringUtil.getDate(deliveryDate)
																			, cutoff, windowStartTime, windowEndTime, zoneArray);
		} catch (RoutingServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public String doLockWaveSyncActivity() {
		String userId = com.freshdirect.transadmin.security.SecurityManager.getUserName(getHttpServletRequest());
		RoutingInfoServiceProxy proxy = new RoutingInfoServiceProxy();
		WaveSyncLockActivity waveSyncLockActivity = null;
		try {
			waveSyncLockActivity = proxy.isWaveSyncronizationLocked();
			
			if(waveSyncLockActivity != null && waveSyncLockActivity.getInitiator() != null) {
				LOGGER.debug("WaveSync lock released by: "+ userId );
				proxy.releaseWaveSyncLock(userId); // release lock for wave sync to routing system
				return "LOCK_RELEASED";
			} else {
				proxy.addWaveSyncLockActivity(userId); // lock wave sync to routing system 
				return "WAVESYNC_LOCKED";
			}			
		} catch (RoutingServiceException e) {
			LOGGER.error("routing service exception", e);
		}
		return null;
	}
	
	public boolean isWaveSyncronizationLocked() {
		
		RoutingInfoServiceProxy proxy = new RoutingInfoServiceProxy();
		WaveSyncLockActivity waveSyncLockActivity = null;
		try {
			waveSyncLockActivity = proxy.isWaveSyncronizationLocked();
			return (waveSyncLockActivity != null && waveSyncLockActivity.getInitiator() != null) ? true : false;
		} catch (RoutingServiceException e) {
			LOGGER.error("routing service exception", e);
		}
		return false;
	}
	
}
