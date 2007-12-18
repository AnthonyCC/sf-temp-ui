package com.freshdirect.dlvadmin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidatorException;

import com.freshdirect.delivery.EnumTimeslotStatus;
import com.freshdirect.delivery.planning.DlvShiftTimeslotModel;

public class AddTimeslots extends DlvPage {

	private List timeslots;
	private int selectedTimeslotIndex;
	
	protected void initialize() {
		this.timeslots = new ArrayList();
		this.timeslots.add( new DlvShiftTimeslotModel() );
	}

	public void commitChanges(IRequestCycle cycle) {
		if (this.validateTimeslots()) {
			System.out.println("Commit timeslots...");
		}
	}

	public IPropertySelectionModel getStatusPropertySelectionModel() {
		return new SimpleSelectionModel( EnumTimeslotStatus.getEnumList() ) {
			protected String getLabel(Object o) {
				return ((EnumTimeslotStatus)o).getName();
			}
		};
	}

	public void addTimeslot(IRequestCycle cycle){
		if (this.validateTimeslots()) {
			this.timeslots.add( new DlvShiftTimeslotModel() );			
		}
	}

	public void setSelectedTimeslotIndex(int selectedTimeslotIndex) {
		this.selectedTimeslotIndex = selectedTimeslotIndex;
	}

	public void deleteTimeslot(IRequestCycle cycle){
		this.timeslots = new ArrayList( this.timeslots );
		this.timeslots.remove(this.selectedTimeslotIndex);
		IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
		delegate.clear();
	}

	/**
	 * @return boolean true if valid
	 */
	private boolean validateTimeslots() {
		IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
	
		if (delegate.getHasErrors()) {
			return false;
		}
	
		for (Iterator i=timeslots.iterator(); i.hasNext(); ) {
			DlvShiftTimeslotModel timeslot = (DlvShiftTimeslotModel)i.next();
			if (timeslot.getStartTime().after( timeslot.getEndTime() )) {
				delegate.setFormComponent(null);
				delegate.record(new ValidatorException("Start Time must be before End Time"));
			}
		}
	
		return !delegate.getHasErrors();	
	}


	public List getTimeslots() {
		return timeslots;
	}

	public int getTimeslotCount() {
		return this.timeslots.size();
	}

	public void setTimeslotCount(int timeslotCount) {
		this.timeslots.clear();
		for (int i=0; i<timeslotCount; i++) {
			this.timeslots.add( new DlvShiftTimeslotModel() );
		}
	}
}
