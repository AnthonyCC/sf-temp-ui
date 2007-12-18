package com.freshdirect.dlvadmin;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.admin.DlvAdminManager;
import com.freshdirect.delivery.model.DlvRegionModel;
import com.freshdirect.delivery.planning.DlvPlanModel;

public abstract class AddPlan extends DlvPage implements PageBeginRenderListener {
	
	public void pageBeginRender(PageEvent event) {
		this.setPlan(new DlvPlanModel());
		this.setRegion(null);
	}

	public void submitAddPlan(IRequestCycle cycle) {
		this.getPlan().setRegionId(getRegion().getPK().getId());
		try {
			DlvAdminManager.getInstance().addPlan(this.getPlan());
		} catch (DlvResourceException de) {
			throw new ApplicationRuntimeException(de);
		}
		//TODO to see if we can redirect to EditRegion
		cycle.activate("Home");
	}
	
	public abstract DlvRegionModel getRegion();
	
	public abstract void setRegion(DlvRegionModel region);
	
	public abstract DlvPlanModel getPlan();
	
	public abstract void setPlan(DlvPlanModel plan);	
}
