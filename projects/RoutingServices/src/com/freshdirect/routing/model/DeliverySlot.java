package com.freshdirect.routing.model;

import java.text.ParseException;
import java.util.Date;

import com.freshdirect.routing.util.RoutingDateUtil;

public class DeliverySlot extends BaseModel implements IDeliverySlot  {
	
	private IDeliverySlotCost deliveryCost;
	private boolean manuallyClosed;
	private IRoutingSchedulerIdentity schedulerId;
	private Date startTime;
	private Date stopTime;
	private String waveCode;
	
	public IDeliverySlotCost getDeliveryCost() {
		return deliveryCost;
	}
	public void setDeliveryCost(IDeliverySlotCost deliveryCost) {
		this.deliveryCost = deliveryCost;
	}
	public boolean isManuallyClosed() {
		return manuallyClosed;
	}
	public void setManuallyClosed(boolean manuallyClosed) {
		this.manuallyClosed = manuallyClosed;
	}
	public IRoutingSchedulerIdentity getSchedulerId() {
		return schedulerId;
	}
	public void setSchedulerId(IRoutingSchedulerIdentity schedulerId) {
		this.schedulerId = schedulerId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getStopTime() {
		return stopTime;
	}
	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}
	
	public String formatTime(Date date) {
		try {
			return RoutingDateUtil.formatDateTime(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "ERROR";
		}
	}
	public String toString() {
		return schedulerId + "["+formatTime(startTime)+"->"+formatTime(stopTime) +"]";
	}
	public String getWaveCode() {
		return waveCode;
	}
	public void setWaveCode(String waveCode) {
		this.waveCode = waveCode;
	}
}
