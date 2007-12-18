package com.freshdirect.dlvadmin;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.EnumDayCode;
import com.freshdirect.delivery.admin.DlvAdminManager;
import com.freshdirect.delivery.planning.DlvPlanModel;
import com.freshdirect.delivery.planning.DlvShiftModel;

public abstract class EditPlan extends DlvPage {
	
	public void activateExternalPage(Object[] params, IRequestCycle cycle) {
		try {
			String planId = (String)params[0];
			this.setPlan( DlvAdminManager.getInstance().getPlan(planId) );
			this.clearEditShift();
		} catch (DlvResourceException e) {
			throw new ApplicationRuntimeException(e);
		}
	}

	public abstract DlvPlanModel getPlan();
	public abstract void setPlan(DlvPlanModel plan);

	public abstract DlvShiftModel getEditShift();
	public abstract void setEditShift(DlvShiftModel editShift);

	public abstract Integer getShiftNumber();
	public abstract void setShiftNumber(Integer shiftNumber);
	
	public void editShift(EnumDayCode day, Integer shiftNum) {
		this.setEditShift( (DlvShiftModel) this.getPlan().getShift(day, shiftNum.intValue()).deepCopy() );
		this.setShiftNumber(shiftNum);
	}
	
	public void deleteShift(EnumDayCode day, Integer shiftNum) {
		this.getPlan().deleteShift(day, shiftNum.intValue());
	}

	public void addShift(IRequestCycle cycle) {
		DlvShiftModel s = new DlvShiftModel();

		Integer i = (Integer)cycle.getListenerParameters()[0];
		s.setDayOfWeek( EnumDayCode.getEnum(i.intValue()) );

		this.setEditShift(s);
		this.setShiftNumber(null);
	}

	public void submitAddShift(IRequestCycle cycle) {
		this.getPlan().addShift(getEditShift());
		this.clearEditShift();
	}
	
	public void cancelEditShift(IRequestCycle cycle){
		this.clearEditShift();
	}

	public void submitEditShift(IRequestCycle cycle){
		this.getPlan().setShift(this.getShiftNumber().intValue(), getEditShift());
		this.clearEditShift();
	}

	public void submitEditTimeslots(IRequestCycle cycle){
		this.getPlan().setShift(this.getShiftNumber().intValue(), getEditShift());
		this.clearEditShift();
	}
	
	public void updatePlan(){
		try {
			DlvAdminManager.getInstance().updatePlan(this.getPlan());
		}catch(DlvResourceException de){
			throw new ApplicationRuntimeException(de);
		}
	}

	public abstract DlvShiftModel getCurrentShift();
	public abstract void setCurrentShift(DlvShiftModel currentShift);
	
	private void clearEditShift(){
		this.setEditShift(null);
		this.setShiftNumber(null);
	}

}
