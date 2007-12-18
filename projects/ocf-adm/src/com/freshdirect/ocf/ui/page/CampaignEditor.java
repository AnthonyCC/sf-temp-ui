/**
 * @author ekracoff
 * Created on Jun 8, 2005*/

package com.freshdirect.ocf.ui.page;

import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ExternalServiceParameter;
import org.apache.tapestry.valid.FieldTracking;
import org.apache.tapestry.valid.IValidationDelegate;

import com.freshdirect.ocf.core.Campaign;
import com.freshdirect.ocf.core.OcfManager;
import com.freshdirect.ocf.core.OcfRecoverableException;

public abstract class CampaignEditor extends AppPage implements IExternalPage {

	public void activateExternalPage(Object[] parameters, IRequestCycle cycle) {
		if (parameters != null && parameters.length > 0) {
			setCampaign(OcfManager.getInstance().getCampaign((String) parameters[0]));
		} else {
			System.out.println("CampaignEditor.activateExternalPage()");
			setCampaign(new Campaign());
		}
	}

	public void saveOrUpdateCampaign() {
		if(getDelegate().getHasErrors()) {
			return;
		}
		OcfManager.getInstance().saveOrUpdateCampaign(getCampaign());
		FlightEditor fe = (FlightEditor) getRequestCycle().getPage("FlightEditor");
		throw new TapestryRedirectException(getRequestCycle(), "FlightEditor", new ExternalServiceParameter("FlightEditor", new Object[]{getCampaign().getId()}));
	}
	
	public void executeFlight(IRequestCycle cycle) throws OcfRecoverableException{
		OcfManager.getInstance().runFlight((String) cycle.getServiceParameters()[0]);
	}

	public abstract Campaign getCampaign();

	public abstract void setCampaign(Campaign campaign);

	public boolean getIsNew() {
		return getCampaign().getId() == null;
	}
	
	public IValidationDelegate getDelegate() {
		return (IValidationDelegate) getBeans().getBean("delegate");
	}
}