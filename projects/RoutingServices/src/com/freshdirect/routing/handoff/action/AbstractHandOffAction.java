package com.freshdirect.routing.handoff.action;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.commons.lang.StringUtils;

import com.freshdirect.routing.constants.EnumHandOffBatchStatus;
import com.freshdirect.routing.model.IHandOffBatch;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.HandOffServiceProxy;

public abstract class AbstractHandOffAction {
	
	private IHandOffBatch batch;
	
	private String userId;
	
	private NumberFormat formatter = new DecimalFormat("00");
	
	public AbstractHandOffAction(IHandOffBatch batch, String userId) {
		super();
		this.batch = batch;
		this.userId = userId;
	}
	
	public IHandOffBatch getBatch() {
		return batch;
	}

	public void setBatch(IHandOffBatch batch) {
		this.batch = batch;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public static int getRouteIndex(String search) {
		try {
			String[] dataLst = StringUtils.split(search, "-");
			if(dataLst != null && dataLst.length >1) {
				return Integer.parseInt(dataLst[1]);
			} 
		} catch(Exception e) {
			//do nothing
		}
		return 0;
	}
	
	public static String splitStringForCode(String search) {
		String[] dataLst = StringUtils.split(search, "-");
		if(dataLst != null && dataLst.length >0) {
			return dataLst[0];
		} else {
			return "000";
		}
	}
	
	public static int splitStringForValue(String search) {
		try {
			String[] dataLst = StringUtils.split(search, "-");
			if(dataLst != null && dataLst.length >1) {
				return Integer.parseInt(dataLst[1]);
			} 
		} catch(Exception e) {
			//do nothing
		}
		return 0;
	}
	
	public static String getRouteArea(String search) {
		try {
			String[] dataLst = StringUtils.split(search, "-");
			if(dataLst != null && dataLst.length >1) {
				return dataLst[0];
			} 
		} catch(Exception e) {
			//do nothing
		}
		return null;
	}
	
	public String formatRouteNumber(int input) {
		return formatter.format(input);
	}
	
	public Object execute() {
		long startTime = System.currentTimeMillis();
		try {
			return doExecute();
		} catch (Exception exp) {
			HandOffServiceProxy proxy = new HandOffServiceProxy();
			exp.printStackTrace();
			
			try {
				if(getFailureStatus() != null) {
					proxy.updateHandOffBatchStatus(this.getBatch().getBatchId(), getFailureStatus());
					proxy.updateHandOffBatchMessage(this.getBatch().getBatchId(), 
														(exp.getMessage() != null ? exp.getMessage() : exp.toString()));
				}
			} catch (RoutingServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} 
		long endTime = System.currentTimeMillis();
		System.out.println("HandOffAction "+this.getClass().getName()+" completed in"+ ((endTime - startTime)/60) +" secs");
		return null;
	}
	
	public abstract Object doExecute() throws Exception;
	
	public abstract EnumHandOffBatchStatus getFailureStatus();
	
	
}
