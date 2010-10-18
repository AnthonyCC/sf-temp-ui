package com.freshdirect.routing.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.freshdirect.routing.constants.EnumHandOffBatchStatus;

public interface IHandOffBatch {
	
	String getBatchId();
	void setBatchId(String batchId);

	Date getDeliveryDate();
	void setDeliveryDate(Date deliveryDate);

	EnumHandOffBatchStatus getStatus();	
	void setStatus(EnumHandOffBatchStatus status);

	String getSystemMessage();
	void setSystemMessage(String systemMessage);

	public Set<IHandOffBatchAction> getAction();
	void setAction(Set<IHandOffBatchAction> action);
	
	Set<IHandOffBatchSession> getSession();
	void setSession(Set<IHandOffBatchSession> session);
	
	Set<IHandOffBatchDepotSchedule> getDepotSchedule();
	void setDepotSchedule(Set<IHandOffBatchDepotSchedule> depotSchedule);
	
	IHandOffBatchAction getLastAction();
	
	IHandOffBatchAction getFirstAction();
	
	List<IHandOffBatchStop> getStops();
	void setStops(List<IHandOffBatchStop> stops);
	
	List<IHandOffBatchStop> getRoutes();
	void setRoutes(List<IHandOffBatchStop> routes);
	
	String getServiceTimeScenario();
	void setServiceTimeScenario(String serviceTimeScenario);
	
	Date getCutOffDateTime();
	void setCutOffDateTime(Date cutOffDateTime);
	
	boolean isEligibleForCommit();
	void setEligibleForCommit(boolean isEligibleForCommit);
	
	int getNoOfOrders();
	void setNoOfOrders(int noOfOrders);

}
