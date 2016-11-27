package com.freshdirect.transadmin.datamanager.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteNoGenerationModel {
	
	private int currentSequenceNo;
	
	private int previousSequenceNo;
	
	private String areaPrefix;
	
	private List orders;
	
	private List successors;
	
	private List predecessors;
	
	private Map routeNos;

	public String getAreaPrefix() {
		return areaPrefix;
	}

	public void setAreaPrefix(String areaPrefix) {
		this.areaPrefix = areaPrefix;
	}

		
	public int getCurrentSequenceNo() {
		return currentSequenceNo;
	}

	public void setCurrentSequenceNo(int currentSequenceNo) {
		this.currentSequenceNo = currentSequenceNo;
	}

	public int getPreviousSequenceNo() {
		return previousSequenceNo;
	}

	public void setPreviousSequenceNo(int previousSequenceNo) {
		this.previousSequenceNo = previousSequenceNo;
	}

	public int incrementCurrentSequenceNo() {
		this.currentSequenceNo++;
		return  getCurrentSequenceNo(); 
	}

	public List getOrders() {
		return orders;
	}

	public void setOrders(List orders) {
		this.orders = orders;
	}
	
	public void addOrder(Object order) {
		if(orders == null) {
			orders = new ArrayList();
		}
		orders.add(order);
	}
	
	public void addSuccessor(Object objTmp) {
		if(successors == null) {
			successors = new ArrayList();
		}
		successors.add(objTmp);
	}
	
	public void addSuccessors(List objTmp) {
		if(successors == null) {
			successors = new ArrayList();
		}
		successors.addAll(objTmp);
	}
	
	public void addPredecessor(Object objTmp) {
		if(predecessors == null) {
			predecessors = new ArrayList();
		}
		predecessors.add(objTmp);
	}
	
	public void addPredecessors(List objTmp) {
		if(predecessors == null) {
			predecessors = new ArrayList();
		}
		predecessors.addAll(objTmp);
	}

	public List getPredecessors() {
		return predecessors;
	}

	public void setPredecessors(List predecessors) {
		this.predecessors = predecessors;
	}

	public List getSuccessors() {
		return successors;
	}

	public void setSuccessors(List successors) {
		this.successors = successors;
	}

	public Map getRouteNos() {
		return routeNos;
	}

	public void setRouteNos(Map routeNos) {
		this.routeNos = routeNos;
	}
	
	public void putRoute(Object objKey, Object value) {
		if(routeNos == null) {
			routeNos = new HashMap();
		}
		routeNos.put(objKey, value);
	}
	
	public Object getRoute(Object objKey) {
		if(routeNos != null) {
			return routeNos.get(objKey);
		}
		return null;
	}

}
