package com.freshdirect.dlvadmin;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.DlvTemplateManager;
import com.freshdirect.delivery.admin.DlvAdminManager;
import com.freshdirect.delivery.model.DlvRegionModel;
import com.freshdirect.delivery.model.DlvZoneModel;
import com.freshdirect.delivery.planning.DlvPlanModel;
import com.freshdirect.delivery.planning.DlvPlan;

public abstract class EditRegion extends DlvPage {

	public void activateExternalPage(Object[] params, IRequestCycle cycle) {
		try {
			String regionName = (String) params[0];
			this.setStartDate((Date) params[1]);
			this.setRegion(DlvTemplateManager.getInstance().getRegion(regionName, this.getStartDate()));
		} catch (DlvResourceException e) {
			throw new ApplicationRuntimeException(e);
		}
	}

	public List getPlans() {
		try {
			return DlvAdminManager.getInstance().getPlansForRegion(this.getRegion().getPK().getId());
		} catch (DlvResourceException e) {
			throw new ApplicationRuntimeException(e);
		}
	}

	public IPropertySelectionModel getPlanSelectionModel() {
		ObjectSelectionModel sm = new ObjectSelectionModel();
		sm.add("", "None");
		for (Iterator i = this.getPlans().iterator(); i.hasNext();) {
			DlvPlan plan = (DlvPlan) i.next();
			sm.add(plan.getPK().getId(), plan.getName());
		}
		return sm;
	}

	public void updateZonePlan(IRequestCycle cycle) {
		try {
			DlvAdminManager.getInstance().updateZones(this.getRegion().getName(), this.getRegion().getZones(), this.getStartDate());
		} catch (DlvResourceException de) {
			throw new ApplicationRuntimeException(de);
		}
	}

	public void deletePlan(IRequestCycle cycle) {
		try {
			String pk = (String) cycle.getListenerParameters()[0];
			DlvAdminManager.getInstance().deletePlan(pk);
		} catch (DlvResourceException de) {
			throw new ApplicationRuntimeException(de);
		}

	}

	public void updateRegionData(IRequestCycle cycle) {

		try {
			DlvAdminManager.getInstance().updateRegionData(
				this.getRegion().getRegionDataPk(),
				this.getStartDate(),
				this.getRegion().getDeliveryCharges());
		} catch (DlvResourceException de) {
			throw new ApplicationRuntimeException(de);
		}

	}
	
	public abstract DlvRegionModel getRegion();
	
	public abstract void setRegion(DlvRegionModel region);
	
	public abstract Date getStartDate();

	public abstract void setStartDate(Date startDate);

	public abstract DlvZoneModel getZone();

	public abstract void setZone(DlvZoneModel currentZone);
	
	public abstract DlvPlan getPlan();

	public abstract void setPlan(DlvPlan currentPlan);

}
