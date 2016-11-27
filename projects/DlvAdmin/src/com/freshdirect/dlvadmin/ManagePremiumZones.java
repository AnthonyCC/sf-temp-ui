package com.freshdirect.dlvadmin;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.valid.IValidationDelegate;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.admin.DlvAdminManager;
import com.freshdirect.delivery.model.DlvRegionModel;
import com.freshdirect.delivery.model.DlvZoneModel;

public abstract class ManagePremiumZones extends DlvPage {
	
	public abstract DlvRegionModel getRegion();

	public abstract void setRegion(DlvRegionModel region);

	public abstract DlvZoneModel getZone();

	public abstract void setZone(DlvZoneModel currentZone);

	public void updatePremiumCTZone(boolean premiumCtActive) {
		try {
			IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
			if (!delegate.getHasErrors()) {
				DlvZoneModel z = getZone();
				z.setPremiumCtActive(premiumCtActive);
				if (!premiumCtActive) {
					z.setPremiumCtReleaseTime(0);
				}
				DlvAdminManager.getInstance().updatePremiumCtZone(
						z.getZoneCode(),  premiumCtActive, z.getPremiumCtReleaseTime());
			}
		} catch (DlvResourceException de) {
			throw new ApplicationRuntimeException(de);
		}
	}	
}
