package com.freshdirect.dlvadmin.components;



import org.apache.tapestry.BaseComponent;

import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.planning.DlvResourceModel;

public class viewResource extends BaseComponent {
	
	private DlvResourceModel resource;
	private String view;
	private final static String VIEW_CT = "CT";
	private final static String VIEW_ALL = "All";
	private final static String VIEW_PREMIUM = "Premium";
	private final static String VIEW_PREMIUM_CT = "Premium CT";
	
	/** forEach */
	private DlvTimeslotModel currentTimeslot;

	public DlvResourceModel getResource() {
		return resource;
	}

	public void setResource(DlvResourceModel resource) {
		this.resource = resource;
	}

	public DlvTimeslotModel getCurrentTimeslot() {
		return currentTimeslot;
	}

	public void setCurrentTimeslot(DlvTimeslotModel currentTimeslot) {
		this.currentTimeslot = currentTimeslot;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}
	
	public boolean isShowCT() {
		  return getView()!=null && (getView().equals(VIEW_ALL) || getView().equals(VIEW_CT)); 
	}
	public boolean isShowBase() {
		  return getView()!=null && getView().equals(VIEW_ALL); 
	}
	public boolean isShowTotal() {
		  return getView()==null || getView().equals(VIEW_ALL); 
	}
	public boolean isShowPremium() {
		  return getView()!=null && (getView().equals(VIEW_ALL)|| getView().equals(VIEW_PREMIUM)); 
	}
	public boolean isShowPremiumCt() {
		  return getView()!=null && (getView().equals(VIEW_ALL)|| getView().equals(VIEW_PREMIUM_CT)); 
	}
}
