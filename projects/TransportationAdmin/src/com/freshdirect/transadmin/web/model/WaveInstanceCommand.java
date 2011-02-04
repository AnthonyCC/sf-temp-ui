package com.freshdirect.transadmin.web.model;

import com.freshdirect.routing.constants.EnumWaveInstanceStatus;
import com.freshdirect.routing.model.IWaveInstance;
import com.freshdirect.routing.model.WaveInstance;
import com.freshdirect.routing.util.RoutingStringUtil;

public class WaveInstanceCommand extends WaveInstance {
	
	private Boolean isInSync;
	
	public WaveInstanceCommand() {
		super();
	}
	
	public WaveInstanceCommand(IWaveInstance instance) {
		super();
		this.setWaveInstanceId(instance.getWaveInstanceId());
		this.setRoutingWaveInstanceId(instance.getRoutingWaveInstanceId());
		this.setDeliveryDate(instance.getDeliveryDate());
		this.setArea(instance.getArea());
		this.setDispatchTime(instance.getDispatchTime());
		this.setWaveStartTime(instance.getWaveStartTime());
		this.setPreferredRunTime(instance.getPreferredRunTime());
		this.setMaxRunTime(instance.getMaxRunTime());
		this.setNoOfResources(instance.getNoOfResources());
		this.setCutOffTime(instance.getCutOffTime());
		this.setForce(instance.isForce());
		this.setNotificationMessage(instance.getNotificationMessage());		
		this.setIsInSync(EnumWaveInstanceStatus.SYNCHRONIZED.equals(instance.getStatus()));
		this.setSource(instance.getSource());
	}

	public Boolean getIsInSync() {
		return isInSync;
	}

	public void setIsInSync(Boolean isInSync) {
		this.isInSync = isInSync;
	}
	
	public String getPreferredRunTimeInfo() {
		return RoutingStringUtil.calcHM(this.getPreferredRunTime());
	}

	public String getMaxRunTimeInfo() {
		return RoutingStringUtil.calcHM(this.getMaxRunTime());
	} 
}
