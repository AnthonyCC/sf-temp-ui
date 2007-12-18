package com.freshdirect.dlvadmin;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.valid.IValidationDelegate;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.admin.DlvAdminManager;
import com.freshdirect.delivery.model.DlvRegionModel;
import com.freshdirect.delivery.model.DlvZoneModel;

public abstract class ManageCTZones extends DlvPage {
	
	public abstract DlvRegionModel getRegion();

	public abstract void setRegion(DlvRegionModel region);

	public abstract DlvZoneModel getZone();

	public abstract void setZone(DlvZoneModel currentZone);

	public void updateCTZone(boolean ctActive) {
		try {
			IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
			if (!delegate.getHasErrors()) {
				DlvZoneModel z = getZone();
				z.setCtActive(ctActive);
				if (!ctActive) {
					z.setCtReleaseTime(0);
				}
				DlvAdminManager.getInstance().updateChefsTableZone(
						z.getZoneCode(), ctActive, z.getCtReleaseTime());
			}
		} catch (DlvResourceException de) {
			throw new ApplicationRuntimeException(de);
		}
	}

}
