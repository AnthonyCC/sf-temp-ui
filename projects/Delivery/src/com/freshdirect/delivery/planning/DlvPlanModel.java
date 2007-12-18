package com.freshdirect.delivery.planning;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.freshdirect.delivery.EnumDayCode;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.core.PrimaryKey;

public class DlvPlanModel extends ModelSupport {

    private String name;
    private String regionId;
    private List shifts = new ArrayList();
    
    public DlvPlanModel(){
        super();
    }
    
    public DlvPlanModel(PrimaryKey pk){
    	this.setPK(pk);
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }
    
    public String getRegionId(){
    	return this.regionId;
    }
    
    public void setRegionId(String regionId){
    	this.regionId = regionId;
    }
    
    public List getShifts(){
    	return this.shifts;
    }
    
	public void setShifts(List shifts) {
		this.shifts = shifts;
	}     
	
    public void addShift(DlvShiftModel shift) {
    	this.shifts.add(shift);
    }
    
    public List getShiftsForDay(EnumDayCode day){
    	List ret = new ArrayList();
		DlvShiftModel shift = null;
    	for(int i = 0, size = this.shifts.size(); i < size; i++){
    		 shift = (DlvShiftModel)this.shifts.get(i);
    		 if(day.equals(shift.getDayOfWeek())){
    		 	ret.add(shift);
    		 }
    	}
    	return ret;
    }
    
    public void deleteShift(EnumDayCode day, int shiftNum){
    	int c=0;
		for (ListIterator i=this.shifts.listIterator(); i.hasNext(); ){
			 DlvShiftModel shift = (DlvShiftModel)i.next();
			 if(day.equals(shift.getDayOfWeek())){
			 	if (shiftNum==c) {
					i.remove();
			 		return;
			 	}
				c++;
			 }
		}
    }
    


	public DlvShiftModel getShift(EnumDayCode day, int shiftNum){
		int c=0;
		DlvShiftModel foundShift = null;
		for (Iterator i=this.shifts.iterator(); i.hasNext(); ){
			 DlvShiftModel shift = (DlvShiftModel)i.next();
			 if(day.equals(shift.getDayOfWeek())){
				if (shiftNum==c) {
					foundShift = shift;
					break;
				}
				c++;
			 }
		}
		return foundShift;
	}

	public void setShift(int shiftNum, DlvShiftModel shift) {
		int c=0;
		for (ListIterator i=this.shifts.listIterator(); i.hasNext(); ){
			 DlvShiftModel currentShift = (DlvShiftModel)i.next();
			 if(currentShift.getDayOfWeek().equals(shift.getDayOfWeek())){
				if (shiftNum==c) {
					i.set(shift);
					return;
				}
				c++;
			 }
		}
	}
	
	public String toString(){
		return this.name;
	}

}
