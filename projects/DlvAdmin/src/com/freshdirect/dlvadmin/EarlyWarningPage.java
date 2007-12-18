package com.freshdirect.dlvadmin;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.admin.DlvAdminManager;
import com.freshdirect.delivery.admin.EarlyWarningDataI;

public abstract class EarlyWarningPage extends DlvPage {
	
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE MMMM d, yyyy");

	private final static String VIEW_CT = "CT";
	private final static String VIEW_ALL = "All";

	private EarlyWarningDataI currentData;
	private List earlyWarningData;
		
	public IPropertySelectionModel getDaySelectionModel(){
		ObjectSelectionModel sm = new ObjectSelectionModel();
		Calendar startCal = Calendar.getInstance();
		startCal.set(Calendar.HOUR, 0);
		startCal.set(Calendar.MINUTE, 0);
		startCal.set(Calendar.SECOND, 0);
		startCal.set(Calendar.MILLISECOND, 0);
		Calendar endCal = (Calendar) startCal.clone();
		endCal.add(Calendar.DATE, 7);
		sm.add(null, "Select A Day");
		while(startCal.before(endCal)){
			sm.add(startCal.getTime(), dateFormatter.format(startCal.getTime()));
			startCal.add(Calendar.DATE, 1);
		}
		return sm;
	}
	
	public List getEarlyWarningData(){
		try{
			this.earlyWarningData = DlvAdminManager.getInstance().getEarlyWarningData(this.getSelectedDay());
			return this.earlyWarningData;
		}catch(DlvResourceException e){
			throw new ApplicationRuntimeException(e);
		}
	}
	
	public abstract Date getSelectedDay();
	public abstract void setSelectedDay(Date selectedDay);
	
	public EarlyWarningDataI getCurrentData() {
		return this.currentData;
	}
	
	public void setCurrentData(EarlyWarningDataI currentData) {
		this.currentData = currentData;
	}
	
	public String getDataRowClass(double percentage){
		int perc = (int)Math.round(percentage*100);
		if(perc == 0){
			return "critical";
		}
		if(perc <= 60){
			return "belowSixty";
		}
		if(perc >=61 && perc <= 75){
			return "aboveSixty";
		}
		if(perc >=75 && perc <= 85){
			return "aboveSeventyFive";
		}
		if(perc > 85){
			return "critical";
		}
		return "normal";
	}
	
	public NumberFormat getPercentageFormatter(){
		return NumberFormat.getPercentInstance();
	}
		
	public int getOrderTotal(){
		int orderTotal = 0;
		for(Iterator i = this.earlyWarningData.iterator(); i.hasNext(); ){
			EarlyWarningDataI data = (EarlyWarningDataI)i.next();
			orderTotal += data.getOrder(); 
		}
		return orderTotal;
	}
	
	public int getAllocationTotal() {
		int aTotal = 0;
		for(Iterator i = this.earlyWarningData.iterator(); i.hasNext() ;){
			EarlyWarningDataI d = (EarlyWarningDataI) i.next();
			aTotal += d.getTotalAllocation();
		}
		return aTotal;
	}
	
	public int getCapacityTotal(){
		int capacity = 0;
		for(Iterator i = this.earlyWarningData.iterator(); i.hasNext(); ){
			EarlyWarningDataI data = (EarlyWarningDataI) i.next();
			capacity += data.getCapacity();
		}
		return capacity;
	}
	
	public int getBaseAllocationTotal() {
		int aTotal = 0;
		for(Iterator i = this.earlyWarningData.iterator(); i.hasNext() ;){
			EarlyWarningDataI d = (EarlyWarningDataI) i.next();
			if(d.getCTActive())
				aTotal += d.getBaseAllocation();
		}
		return aTotal;
	}
	
	public int getBaseCapacityTotal(){
		int capacity = 0;
		for(Iterator i = this.earlyWarningData.iterator(); i.hasNext(); ){
			EarlyWarningDataI data = (EarlyWarningDataI) i.next();
			if(data.getCTActive())
				capacity += data.getBaseCapacity();
		}
		return capacity;
	}	
	
	public int getBaseOrderTotal(){
		int orderTotal = 0;
		for(Iterator i = this.earlyWarningData.iterator(); i.hasNext(); ){
			EarlyWarningDataI data = (EarlyWarningDataI)i.next();
			if(data.getCTActive())
				orderTotal += data.getBaseOrder(); 
		}
		return orderTotal;
	}
	public int getCTAllocationTotal() {
		int aTotal = 0;
		for(Iterator i = this.earlyWarningData.iterator(); i.hasNext() ;){
			EarlyWarningDataI d = (EarlyWarningDataI) i.next();
			if(d.getCTActive())
				aTotal += d.getCTAllocation();
		}
		return aTotal;
	}
	public int getCTOrderTotal(){
		int orderTotal = 0;
		for(Iterator i = this.earlyWarningData.iterator(); i.hasNext(); ){
			EarlyWarningDataI data = (EarlyWarningDataI)i.next();
			if(data.getCTActive())
				orderTotal += data.getCTOrder(); 
		}
		return orderTotal;
	}
	
	public int getCTCapacityTotal(){
		int capacity = 0;
		for(Iterator i = this.earlyWarningData.iterator(); i.hasNext(); ){
			EarlyWarningDataI data = (EarlyWarningDataI) i.next();
			if(data.getCTActive())
				capacity += data.getCTCapacity();
		}
		return capacity;
	}	

	public abstract String getSelectedView();
	public abstract void setSelectedView(String selectedView);
	public boolean isShowCT() {
		  return getSelectedView()!=null && (getSelectedView().equals(VIEW_ALL) || getSelectedView().equals(VIEW_CT)); 
	}
	public boolean isShowBase() {
		  return getSelectedView()!=null && getSelectedView().equals(VIEW_ALL); 
	}
	public boolean isShowTotal() {
		  return getSelectedView()==null || getSelectedView().equals(VIEW_ALL); 
	}

}
