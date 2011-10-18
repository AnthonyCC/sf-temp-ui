package com.freshdirect.routing.model;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.freshdirect.routing.constants.EnumCrisisMngBatchStatus;

public class CrisisManagerBatch extends BaseModel implements ICrisisManagerBatch  {
	
	private String batchId;
	private Date deliveryDate;
	private Date destinationDate;
	private EnumCrisisMngBatchStatus status;
	private String systemMessage;
	private Date cutOffDateTime;
	private boolean isEligibleForCancel;
	
	private Set<ICrisisManagerBatchAction> action;
	private List<ICrisisManagerBatchOrder> order;
	private List<ICancelOrderModel> cancelOrder;
	private List<IActiveOrderModel> activeOrder;
	
	private int noOfOrders;
	private int noOfOrdersCancelled;
	private Date startTime;
	private Date endTime;
	private String[] area;
	private String[] deliveryType;
	private boolean isStandingOrderIncluded; 
	private int noOfReservations;
	private int noOfReservationsCancelled;
	
	public ICrisisManagerBatchAction getLastAction() {
		if(action != null && action instanceof TreeSet) {
			return (ICrisisManagerBatchAction)((TreeSet)action).last();
		}
		return null;
	}
	
	public ICrisisManagerBatchAction getFirstAction() {
		if(action != null && action instanceof TreeSet) {
			return (ICrisisManagerBatchAction)((TreeSet)action).first();
		}
		return null;
	}
	
	public List<ICrisisManagerBatchOrder> getOrder() {
		return order;
	}

	public void setOrder(List<ICrisisManagerBatchOrder> order) {
		this.order = order;
	}

	public Set<ICrisisManagerBatchAction> getAction() {
		return action;
	}

	public void setAction(Set<ICrisisManagerBatchAction> action) {
		this.action = action;
	}	
	
	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public EnumCrisisMngBatchStatus getStatus() {
		return status;
	}

	public void setStatus(EnumCrisisMngBatchStatus status) {
		this.status = status;
	}

	public boolean isEligibleForCancel() {
		return isEligibleForCancel;
	}

	public void setEligibleForCancel(boolean isEligibleForCancel) {
		this.isEligibleForCancel = isEligibleForCancel;
	}

	public int getNoOfOrdersCancelled() {
		return noOfOrdersCancelled;
	}

	public void setNoOfOrdersCancelled(int noOfOrdersCancelled) {
		this.noOfOrdersCancelled = noOfOrdersCancelled;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Date getDestinationDate() {
		return destinationDate;
	}

	public void setDestinationDate(Date destinationDate) {
		this.destinationDate = destinationDate;
	}

	public String getSystemMessage() {
		return systemMessage;
	}

	public void setSystemMessage(String systemMessage) {
		this.systemMessage = systemMessage;
	}
	
	public Date getCutOffDateTime() {
		return cutOffDateTime;
	}

	public void setCutOffDateTime(Date cutOffDateTime) {
		this.cutOffDateTime = cutOffDateTime;
	}

	public int getNoOfOrders() {
		return noOfOrders;
	}

	public void setNoOfOrders(int noOfOrders) {
		this.noOfOrders = noOfOrders;
	}
	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public String[] getArea() {
		return area;
	}

	public void setArea(String[] area) {
		this.area = area;
	}
	
	public String[] getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String[] deliveryType) {
		this.deliveryType = deliveryType;
	}
	
	public boolean isStandingOrderIncluded() {
		return isStandingOrderIncluded;
	}

	public void setStandingOrderIncluded(boolean isStandingOrderIncluded) {
		this.isStandingOrderIncluded = isStandingOrderIncluded;
	}

	public int getNoOfReservations() {
		return noOfReservations;
	}

	public void setNoOfReservations(int noOfReservations) {
		this.noOfReservations = noOfReservations;
	}
	

	public int getNoOfReservationsCancelled() {
		return noOfReservationsCancelled;
	}

	public void setNoOfReservationsCancelled(int noOfReservationsCancelled) {
		this.noOfReservationsCancelled = noOfReservationsCancelled;
	}

	public List<ICancelOrderModel> getCancelOrder() {
		return cancelOrder;
	}

	public void setCancelOrder(List<ICancelOrderModel> cancelOrder) {
		this.cancelOrder = cancelOrder;
	}

	public List<IActiveOrderModel> getActiveOrder() {
		return activeOrder;
	}

	public void setActiveOrder(List<IActiveOrderModel> activeOrder) {
		this.activeOrder = activeOrder;
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
		CrisisManagerBatch other = (CrisisManagerBatch) obj;
		if (batchId == null) {
			if (other.batchId != null)
				return false;
		} else if (!batchId.equals(other.batchId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrderScenarioBatch [action=" + action + ", batchId=" + batchId
				+ ", deliveryDate=" + deliveryDate 
				+ ", status=" + status + ", systemMessage=" + systemMessage
				+ "]";
	}

	
	
	
}
