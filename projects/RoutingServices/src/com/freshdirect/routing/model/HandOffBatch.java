package com.freshdirect.routing.model;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.freshdirect.routing.constants.EnumHandOffBatchStatus;

public class HandOffBatch extends BaseModel implements IHandOffBatch  {
	
	private String batchId;
	private Date deliveryDate;
	private EnumHandOffBatchStatus status;
	private String systemMessage;
	
	private String serviceTimeScenario;	
	private Date cutOffDateTime;
	
	private boolean isEligibleForCommit;
	
	private Set<IHandOffBatchAction> action;
	
	private Set<IHandOffBatchSession> session;
	
	private Set<IHandOffBatchDepotSchedule> depotSchedule;
	
	private List<IHandOffBatchStop> stops; 
	
	private List<IHandOffBatchStop> routes;
	
	private int noOfOrders;
	
	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public EnumHandOffBatchStatus getStatus() {
		return status;
	}

	public void setStatus(EnumHandOffBatchStatus status) {
		this.status = status;
	}

	public String getSystemMessage() {
		return systemMessage;
	}

	public void setSystemMessage(String systemMessage) {
		this.systemMessage = systemMessage;
	}
	
	public IHandOffBatchAction getLastAction() {
		if(action != null && action instanceof TreeSet) {
			return (IHandOffBatchAction)((TreeSet)action).last();
		}
		return null;
	}
	
	public IHandOffBatchAction getFirstAction() {
		if(action != null && action instanceof TreeSet) {
			return (IHandOffBatchAction)((TreeSet)action).first();
		}
		return null;
	}

	public Set<IHandOffBatchAction> getAction() {
		return action;
	}

	public void setAction(Set<IHandOffBatchAction> action) {
		this.action = action;
	}

	public Set<IHandOffBatchSession> getSession() {
		return session;
	}

	public void setSession(Set<IHandOffBatchSession> session) {
		this.session = session;
	}

	public Set<IHandOffBatchDepotSchedule> getDepotSchedule() {
		return depotSchedule;
	}

	public void setDepotSchedule(Set<IHandOffBatchDepotSchedule> depotSchedule) {
		this.depotSchedule = depotSchedule;
	}

	public List<IHandOffBatchStop> getStops() {
		return stops;
	}

	public void setStops(List<IHandOffBatchStop> stops) {
		this.stops = stops;
	}

	public List<IHandOffBatchStop> getRoutes() {
		return routes;
	}

	public void setRoutes(List<IHandOffBatchStop> routes) {
		this.routes = routes;
	}

	public String getServiceTimeScenario() {
		return serviceTimeScenario;
	}

	public void setServiceTimeScenario(String serviceTimeScenario) {
		this.serviceTimeScenario = serviceTimeScenario;
	}

	public Date getCutOffDateTime() {
		return cutOffDateTime;
	}

	public void setCutOffDateTime(Date cutOffDateTime) {
		this.cutOffDateTime = cutOffDateTime;
	}

	public boolean isEligibleForCommit() {
		return isEligibleForCommit;
	}

	public void setEligibleForCommit(boolean isEligibleForCommit) {
		this.isEligibleForCommit = isEligibleForCommit;
	}
	
		
	public int getNoOfOrders() {
		return noOfOrders;
	}

	public void setNoOfOrders(int noOfOrders) {
		this.noOfOrders = noOfOrders;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((batchId == null) ? 0 : batchId.hashCode());
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
		HandOffBatch other = (HandOffBatch) obj;
		if (batchId == null) {
			if (other.batchId != null)
				return false;
		} else if (!batchId.equals(other.batchId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HandOffBatch [action=" + action + ", batchId=" + batchId
				+ ", deliveryDate=" + deliveryDate + ", session=" + session
				+ ", status=" + status + ", systemMessage=" + systemMessage
				+ "]";
	}
	
	
}
