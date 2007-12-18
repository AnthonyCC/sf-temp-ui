package com.freshdirect.dlvadmin.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidatorException;

import com.freshdirect.delivery.EnumTimeslotStatus;
import com.freshdirect.delivery.planning.DlvShiftModel;
import com.freshdirect.delivery.planning.DlvShiftTimeslotModel;
import com.freshdirect.dlvadmin.SimpleSelectionModel;

public abstract class EditTimeslots extends BaseComponent {
	
	public void commitChanges(IRequestCycle cycle) {
		if (this.validateTimeslots()) {
			this.getShift().setTimeslots(this.getTimeslots());
			getListener().actionTriggered(this, cycle);
		}
	}

	public IPropertySelectionModel getStatusPropertySelectionModel() {
		return new SimpleSelectionModel( EnumTimeslotStatus.getEnumList() ) {
			protected String getLabel(Object o) {
				if(o == null){
					return "";
				}
				return ((EnumTimeslotStatus)o).getName();
			}
		};
	}

	public void addTimeslot(IRequestCycle cycle){
		if (this.validateTimeslots()) {
			this.getShift().getTimeslots().add( new DlvShiftTimeslotModel() );			
		}
	}


	public void deleteTimeslot(IRequestCycle cycle){
		List timeslots = new ArrayList( this.getTimeslots() );
		timeslots.remove(this.getSelectedTimeslotIndex());
		this.getShift().setTimeslots(timeslots);
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

		for (Iterator i=getTimeslots().iterator(); i.hasNext(); ) {
			DlvShiftTimeslotModel timeslot = (DlvShiftTimeslotModel)i.next();
			System.out.println("validate "+timeslot);
			if (timeslot.getStartTime().after( timeslot.getEndTime() )) {
				delegate.setFormComponent(null);
				delegate.record(new ValidatorException("Start Time must be before End Time"));
			}
		}

		return !delegate.getHasErrors();	
	}

	public List getTimeslots() {
		 return getShift() == null ? null : getShift().getTimeslotsByStartDate();
	}
	
	public abstract DlvShiftModel getShift();
	
	public abstract IActionListener getListener();

	public abstract int getSelectedTimeslotIndex();

}
