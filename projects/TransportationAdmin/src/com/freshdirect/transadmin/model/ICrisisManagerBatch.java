package com.freshdirect.transadmin.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.freshdirect.transadmin.constants.EnumCrisisMngBatchStatus;

public interface ICrisisManagerBatch {
	
	String getBatchId();
	void setBatchId(String batchId);

	Date getDeliveryDate();
	void setDeliveryDate(Date deliveryDate);
	
	Date getDestinationDate();
	void setDestinationDate(Date destinationDate);

	EnumCrisisMngBatchStatus getStatus();	
	void setStatus(EnumCrisisMngBatchStatus status);

	String getSystemMessage();
	void setSystemMessage(String systemMessage);

	public Set<ICrisisManagerBatchAction> getAction();
	void setAction(Set<ICrisisManagerBatchAction> action);
	
	List<ICrisisManagerBatchOrder> getOrder();
	void setOrder(List<ICrisisManagerBatchOrder> order);
	
	ICrisisManagerBatchAction getLastAction();
	
	ICrisisManagerBatchAction getFirstAction();
	
	Date getCutOffDateTime();
	void setCutOffDateTime(Date cutOffDateTime);
	
	boolean isEligibleForCancel();
	void setEligibleForCancel(boolean isEligibleForCancel);
	
	int getNoOfOrders();
	void setNoOfOrders(int noOfOrders);
	
	int getNoOfOrdersCancelled();
	void setNoOfOrdersCancelled(int noOfOrdersCancelled);

	Date getStartTime(); 
	void setStartTime(Date startTime);

	Date getEndTime();
    void setEndTime(Date endTime);
	
	String[] getArea();
    void setArea(String[] area);
    
    String[] getDeliveryType();
    void setDeliveryType(String[] deliveryType);
    
    boolean isStandingOrderIncluded();
	void setStandingOrderIncluded(boolean isStandingOrderIncluded);
	
	int getNoOfReservations();
	void setNoOfReservations(int noOfReservations);
	
	int getNoOfReservationsCancelled();
	void setNoOfReservationsCancelled(int noOfReservationsCancelled);
	
	List<ICancelOrderModel> getCancelOrder();
	void setCancelOrder(List<ICancelOrderModel> cancelOrder);

	List<IActiveOrderModel> getActiveOrder();
    void setActiveOrder(List<IActiveOrderModel> activeOrder);
    
    String getProfile();
	void setProfile(String profile);
 
}
