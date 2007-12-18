package com.freshdirect.dlvadmin;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import com.freshdirect.delivery.model.DlvRegionModel;
import com.freshdirect.delivery.model.DlvTimeslotModel;

public abstract class ShowCurrentTimeslots extends DlvPage {

	private final SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm aa");
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE'<br>'MMMM d, yyyy");

	/** foreach */	
	private DlvTimeslotModel currentTimeslot;


	protected void initialize() {
		//this.selectedRegion = null;
		//this.selectedZone = null;
		this.currentTimeslot = null;
	}

	public DlvTimeslotModel getCurrentTimeslot() {
		return currentTimeslot;
	}

	public void setCurrentTimeslot(DlvTimeslotModel currentTimeslot) {
		this.currentTimeslot = currentTimeslot;
	}

	public SimpleDateFormat getTimeFormatter() {
		return timeFormatter;
	}

	public SimpleDateFormat getDateFormatter() {
		return dateFormatter;
	}
	
	public NumberFormat getPercentageFormatter(){
		return NumberFormat.getPercentInstance();
	}
	
	public double getPercentage(){
		if(currentTimeslot.calculateCurrentAllocation(getCurrentDate())==0)
			return 0;
		else
			return (double)currentTimeslot.getChefsTableAllocation()/currentTimeslot.calculateCurrentAllocation(getCurrentDate());
	}
	public abstract DlvRegionModel getSelectedRegion();
	
	public abstract void setSelectedRegion(DlvRegionModel region);
	
	public abstract String getSelectedZone();
	
	public abstract void setSelectedZone(String selectedZon);
}
