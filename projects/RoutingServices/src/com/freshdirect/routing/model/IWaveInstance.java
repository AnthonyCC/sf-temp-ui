package com.freshdirect.routing.model;

import java.util.Date;

import com.freshdirect.routing.constants.EnumWaveInstancePublishSrc;
import com.freshdirect.routing.constants.EnumWaveInstanceStatus;
import com.freshdirect.routing.util.RoutingTimeOfDay;

public interface IWaveInstance {
	
	RoutingTimeOfDay getDispatchTime();
	
	RoutingTimeOfDay getWaveStartTime();
		
	int getPreferredRunTime();
	
	int getMaxRunTime();
	
	int getNoOfResources();
	
	String getRoutingWaveInstanceId();
	
	void setDispatchTime(RoutingTimeOfDay dispatchTime);
	
	void setWaveStartTime(RoutingTimeOfDay waveStartTime);
			
	void setPreferredRunTime(int preferredRunTime);
	
	void setMaxRunTime(int maxRunTime);
	
	void setNoOfResources(int noOfResources);
	
	void setRoutingWaveInstanceId(String routingWaveInstanceId);
	
	RoutingTimeOfDay getCutOffTime();
	
	void setCutOffTime(RoutingTimeOfDay cutOffTime);
	
	int getPriority();

	void setPriority(int priority);
	
	String getWaveInstanceId();
	void setWaveInstanceId(String waveInstanceId);
	
	boolean isForce();
	void setForce(boolean force);
	
	boolean isNeedsConsolidation();
	void setNeedsConsolidation(boolean needsConsolidation);
	
	boolean matchWaveInstance(IWaveInstance other);
	void copyBaseAttributes(IWaveInstance baseInstance);
	
	EnumWaveInstanceStatus getStatus();
	void setStatus(EnumWaveInstanceStatus status);
	
	String getNotificationMessage();
	void setNotificationMessage(String notificationMessage);
	
	boolean isAdvancedRushHour();
	boolean isCapacityCheck1();
	
	boolean isCapacityCheck2();
	boolean isCapacityCheck3();
	IRoutingDepotId getDepotId();
	IRoutingEquipmentType getEquipmentType();
	int getHourlyWage();
	int getHourlyWageDuration();
	int getInboundStemTimeAdjustmentSeconds();
	int getOutboundStemTimeAdjustmentSeconds();
	int getOvertimeWage();
	String getRushHourModel();
	void setAdvancedRushHour(boolean advancedRushHour);
	void setCapacityCheck1(boolean capacityCheck1);
	void setCapacityCheck2(boolean capacityCheck2);
	void setCapacityCheck3(boolean capacityCheck3);
	void setDepotId(IRoutingDepotId depotId);
	void setEquipmentType(IRoutingEquipmentType equipmentType);
	void setHourlyWage(int hourlyWage);
	void setHourlyWageDuration(int hourlyWageDuration);
	void setInboundStemTimeAdjustmentSeconds(int inboundStemTimeAdjustmentSeconds);

	void setOutboundStemTimeAdjustmentSeconds(int outboundStemTimeAdjustmentSeconds);
	void setOvertimeWage(int overtimeWage);
	void setRushHourModel(String rushHourModel);
	
	Date getDeliveryDate();
	void setDeliveryDate(Date deliveryDate);
	
	IAreaModel getArea();
	void setArea(IAreaModel area);
	
	EnumWaveInstancePublishSrc getSource();
	void setSource(EnumWaveInstancePublishSrc source);

}
