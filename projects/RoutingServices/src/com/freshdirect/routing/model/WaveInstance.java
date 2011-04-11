package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.routing.constants.EnumWaveInstancePublishSrc;
import com.freshdirect.routing.constants.EnumWaveInstanceStatus;
import com.freshdirect.routing.util.RoutingTimeOfDay;

public class WaveInstance  extends BaseModel implements IWaveInstance {
	
	private RoutingTimeOfDay dispatchTime;
	private RoutingTimeOfDay waveStartTime;
	private RoutingTimeOfDay cutOffTime;
	private Date deliveryDate;
	private int preferredRunTime;
	private int maxRunTime;
	private int noOfResources;
	
	private String routingWaveInstanceId;
	
	private int priority;
	
	private String waveInstanceId;
	
	private boolean force;
	private boolean needsConsolidation;
	
	private EnumWaveInstanceStatus status;
	private String notificationMessage;
	
	private boolean advancedRushHour;
	private boolean capacityCheck1;
	private boolean capacityCheck2;
	private boolean capacityCheck3;
	private IRoutingDepotId depotId;
	private IRoutingEquipmentType equipmentType;
	private int hourlyWage;
	private int hourlyWageDuration;
	private int inboundStemTimeAdjustmentSeconds;
	private int outboundStemTimeAdjustmentSeconds;
	private int overtimeWage;
	private String rushHourModel;
	
	private IAreaModel area;
	
	private EnumWaveInstancePublishSrc source;
	
	public IAreaModel getArea() {
		return area;
	}

	public void setArea(IAreaModel area) {
		this.area = area;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public boolean isAdvancedRushHour() {
		return advancedRushHour;
	}

	public boolean isCapacityCheck1() {
		return capacityCheck1;
	}

	public boolean isCapacityCheck2() {
		return capacityCheck2;
	}

	public boolean isCapacityCheck3() {
		return capacityCheck3;
	}

	public IRoutingDepotId getDepotId() {
		return depotId;
	}

	public IRoutingEquipmentType getEquipmentType() {
		return equipmentType;
	}

	public int getHourlyWage() {
		return hourlyWage;
	}

	public int getHourlyWageDuration() {
		return hourlyWageDuration;
	}

	public int getInboundStemTimeAdjustmentSeconds() {
		return inboundStemTimeAdjustmentSeconds;
	}

	public int getOutboundStemTimeAdjustmentSeconds() {
		return outboundStemTimeAdjustmentSeconds;
	}

	public int getOvertimeWage() {
		return overtimeWage;
	}

	public String getRushHourModel() {
		return rushHourModel;
	}

	public void setAdvancedRushHour(boolean advancedRushHour) {
		this.advancedRushHour = advancedRushHour;
	}

	public void setCapacityCheck1(boolean capacityCheck1) {
		this.capacityCheck1 = capacityCheck1;
	}

	public void setCapacityCheck2(boolean capacityCheck2) {
		this.capacityCheck2 = capacityCheck2;
	}

	public void setCapacityCheck3(boolean capacityCheck3) {
		this.capacityCheck3 = capacityCheck3;
	}

	public void setDepotId(IRoutingDepotId depotId) {
		this.depotId = depotId;
	}

	public void setEquipmentType(IRoutingEquipmentType equipmentType) {
		this.equipmentType = equipmentType;
	}

	public void setHourlyWage(int hourlyWage) {
		this.hourlyWage = hourlyWage;
	}

	public void setHourlyWageDuration(int hourlyWageDuration) {
		this.hourlyWageDuration = hourlyWageDuration;
	}

	public void setInboundStemTimeAdjustmentSeconds(
			int inboundStemTimeAdjustmentSeconds) {
		this.inboundStemTimeAdjustmentSeconds = inboundStemTimeAdjustmentSeconds;
	}

	public void setOutboundStemTimeAdjustmentSeconds(
			int outboundStemTimeAdjustmentSeconds) {
		this.outboundStemTimeAdjustmentSeconds = outboundStemTimeAdjustmentSeconds;
	}

	public void setOvertimeWage(int overtimeWage) {
		this.overtimeWage = overtimeWage;
	}

	public void setRushHourModel(String rushHourModel) {
		this.rushHourModel = rushHourModel;
	}

	public int getPreferredRunTime() {
		return preferredRunTime;
	}

	public int getMaxRunTime() {
		return maxRunTime;
	}

	public int getNoOfResources() {
		return noOfResources;
	}

	public String getRoutingWaveInstanceId() {
		return routingWaveInstanceId;
	}
	
	public void setPreferredRunTime(int preferredRunTime) {
		this.preferredRunTime = preferredRunTime;
	}

	public void setMaxRunTime(int maxRunTime) {
		this.maxRunTime = maxRunTime;
	}

	public void setNoOfResources(int noOfResources) {
		this.noOfResources = noOfResources;
	}

	public void setRoutingWaveInstanceId(String routingWaveInstanceId) {
		this.routingWaveInstanceId = routingWaveInstanceId;
	}

	public RoutingTimeOfDay getCutOffTime() {
		return cutOffTime;
	}

	public void setCutOffTime(RoutingTimeOfDay cutOffTime) {
		this.cutOffTime = cutOffTime;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	

	public RoutingTimeOfDay getDispatchTime() {
		return dispatchTime;
	}

	public RoutingTimeOfDay getWaveStartTime() {
		return waveStartTime;
	}

	public void setDispatchTime(RoutingTimeOfDay dispatchTime) {
		this.dispatchTime = dispatchTime;
	}

	public void setWaveStartTime(RoutingTimeOfDay waveStartTime) {
		this.waveStartTime = waveStartTime;
	}
	
	public String getWaveInstanceId() {
		return waveInstanceId;
	}

	public void setWaveInstanceId(String waveInstanceId) {
		this.waveInstanceId = waveInstanceId;
	}

	public boolean isForce() {
		return force;
	}

	public void setForce(boolean force) {
		this.force = force;
	}

	public boolean isNeedsConsolidation() {
		return needsConsolidation;
	}

	public void setNeedsConsolidation(boolean needsConsolidation) {
		this.needsConsolidation = needsConsolidation;
	}

	public EnumWaveInstanceStatus getStatus() {
		return status;
	}

	public void setStatus(EnumWaveInstanceStatus status) {
		this.status = status;
	}

	public String getNotificationMessage() {
		return notificationMessage;
	}

	public void setNotificationMessage(String notificationMessage) {
		this.notificationMessage = notificationMessage;
	}
	
	public EnumWaveInstancePublishSrc getSource() {
		return source;
	}

	public void setSource(EnumWaveInstancePublishSrc source) {
		this.source = source;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((cutOffTime == null) ? 0 : cutOffTime.hashCode());
		result = prime * result
				+ ((dispatchTime == null) ? 0 : dispatchTime.hashCode());
		result = prime * result + maxRunTime;
		result = prime * result + preferredRunTime;
		result = prime * result
				+ ((waveStartTime == null) ? 0 : waveStartTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		WaveInstance other = (WaveInstance) obj;
		if (cutOffTime == null) {
			if (other.cutOffTime != null)
				return false;
		} else if (!cutOffTime.equals(other.cutOffTime))
			return false;
		if (dispatchTime == null) {
			if (other.dispatchTime != null)
				return false;
		} else if (!dispatchTime.equals(other.dispatchTime))
			return false;
		if (maxRunTime != other.maxRunTime)
			return false;
		if (preferredRunTime != other.preferredRunTime)
			return false;
		if (waveStartTime == null) {
			if (other.waveStartTime != null)
				return false;
		} else if (!waveStartTime.equals(other.waveStartTime))
			return false;
		return true;
	}
	
	public boolean matchWaveInstance(IWaveInstance other) {
		if (this == other)
			return true;				
		if (cutOffTime == null) {
			if (other.getCutOffTime() != null)
				return false;
		} else if (!cutOffTime.equals(other.getCutOffTime()))
			return false;		
		if (maxRunTime != other.getMaxRunTime())
			return false;
		if (preferredRunTime != other.getPreferredRunTime())
			return false;
		if (waveStartTime == null) {
			if (other.getWaveStartTime() != null)
				return false;
		} else if (!waveStartTime.equals(other.getWaveStartTime()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WaveInstance [area="+(area != null ? area.getAreaCode() : null)+", dispatchTime=" + dispatchTime
				+ ", waveStartTime=" + waveStartTime + ", cutOffTime="
				+ cutOffTime + ", preferredRunTime=" + preferredRunTime
				+ ", maxRunTime=" + maxRunTime + ", noOfResources="
				+ noOfResources + ", routingWaveInstanceId="
				+ routingWaveInstanceId + ", priority=" + priority
				+ ", waveInstanceId=" + waveInstanceId + ", force=" + force
				+ ", needsConsolidation=" + needsConsolidation + "]";
	}

	@Override
	public void copyBaseAttributes(IWaveInstance baseInstance) {
		// TODO Auto-generated method stub
		this.setAdvancedRushHour(baseInstance.isAdvancedRushHour());
		this.setCapacityCheck1(baseInstance.isCapacityCheck1());
		this.setCapacityCheck2(baseInstance.isCapacityCheck2());
		this.setCapacityCheck3(baseInstance.isCapacityCheck3());
		this.setDepotId(baseInstance.getDepotId());
		this.setEquipmentType(baseInstance.getEquipmentType());
		this.setHourlyWage(baseInstance.getHourlyWage());
		this.setHourlyWageDuration(baseInstance.getHourlyWageDuration());
		this.setInboundStemTimeAdjustmentSeconds(baseInstance.getInboundStemTimeAdjustmentSeconds());
		this.setOutboundStemTimeAdjustmentSeconds(baseInstance.getOutboundStemTimeAdjustmentSeconds());
		this.setOvertimeWage(baseInstance.getOvertimeWage());
		this.setRushHourModel(baseInstance.getRushHourModel());
	}
	
	public static void consolidateWaveInstance(IWaveInstance _rootWaveInstance, IWaveInstance _srcWaveInst) {
		if(_srcWaveInst.getWaveStartTime().before(_rootWaveInstance.getWaveStartTime())) {
			_rootWaveInstance.setWaveStartTime(_srcWaveInst.getWaveStartTime());
		}
		if(_srcWaveInst.getPreferredRunTime() > _rootWaveInstance.getPreferredRunTime()) {
			_rootWaveInstance.setPreferredRunTime(_srcWaveInst.getPreferredRunTime());
		}
		if(_srcWaveInst.getMaxRunTime()> _rootWaveInstance.getMaxRunTime()) {
			_rootWaveInstance.setMaxRunTime(_srcWaveInst.getMaxRunTime());
		}
		if(_srcWaveInst.getNoOfResources() > _rootWaveInstance.getNoOfResources()) {
			_rootWaveInstance.setNoOfResources(_srcWaveInst.getNoOfResources());
		}
		if(_srcWaveInst.getRoutingWaveInstanceId() != null) {
			_rootWaveInstance.setRoutingWaveInstanceId(_srcWaveInst.getRoutingWaveInstanceId());
		}
	}
}
