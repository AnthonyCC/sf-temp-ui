package com.freshdirect.dlvadmin.components;

import org.apache.tapestry.BaseComponent;

import com.freshdirect.delivery.EnumTruckType;
import com.freshdirect.delivery.model.DlvTimeslotModel;
import com.freshdirect.delivery.planning.DlvResourceModel;
import com.freshdirect.delivery.planning.DlvTruckAllocation;
import com.freshdirect.dlvadmin.DlvPage;

public abstract class EditResource extends BaseComponent {
	private String view;
	private final static String VIEW_CT = "CT";
	private final static String VIEW_ALL = "All";
	private final static String VIEW_PREMIUM = "Premium";
	private final static String VIEW_PREMIUM_CT = "Premium CT";
	
	public DlvTruckAllocation getCurrentTruckAllocation() {
		return this.getResource().getTruckAllocation(this.getTruckType());
	}

	public void setCurrentTruckAllocation(DlvTruckAllocation truck) {
		this.getResource().setTruckAllocation(truck);
	}

	public abstract EnumTruckType getTruckType();

	public abstract void setTruckType(EnumTruckType currentTruckType);

	public abstract DlvTimeslotModel getTimeslot();

	public abstract void setTimeslot(DlvTimeslotModel currentTimeslot);

	public abstract DlvResourceModel getResource();

	public abstract void setResource(DlvResourceModel resource);
	
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
	
	public boolean isShowPremiumCt() { 
		  return getView()!=null && (getView().equals(VIEW_ALL) || getView().equals(VIEW_PREMIUM_CT)); 
	}
	public boolean isShowPremium() { 
		  return getView()!=null && (getView().equals(VIEW_ALL) || getView().equals(VIEW_PREMIUM)); 
	}
	
	public boolean isTotalEditable(){
		return ((DlvPage)this.getPage()).isUserAdmin() || ((DlvPage)this.getPage()).isUserMarketing();	
	}
	public boolean isCTEditable(){
		return ((DlvPage)this.getPage()).isUserAdmin() || ((DlvPage)this.getPage()).isUserMarketing();	
	}
	public boolean isPremiumEditable(){
		return ((DlvPage)this.getPage()).isUserAdmin() || ((DlvPage)this.getPage()).isUserMarketing();	
	}
	
	public boolean isPremiumCTEditable(){
		return ((DlvPage)this.getPage()).isUserAdmin() || ((DlvPage)this.getPage()).isUserMarketing();	
	}

}
