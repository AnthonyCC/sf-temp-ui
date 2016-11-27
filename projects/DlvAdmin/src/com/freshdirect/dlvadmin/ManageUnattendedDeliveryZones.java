package com.freshdirect.dlvadmin;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.valid.IValidationDelegate;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.admin.DlvAdminManager;
import com.freshdirect.delivery.model.DlvRegionModel;
import com.freshdirect.delivery.model.DlvZoneModel;

public abstract class ManageUnattendedDeliveryZones extends DlvPage {

	
	public abstract DlvRegionModel getRegion();

	public abstract void setRegion(DlvRegionModel region);

	public abstract DlvZoneModel getZone();

	public abstract void setZone(DlvZoneModel currentZone);

	public void updateUnattendedDeliveryInZone(boolean unattended) {
		try {
			IValidationDelegate delegate = (IValidationDelegate) getBeans().getBean("delegate");
			if (!delegate.getHasErrors()) {
				DlvZoneModel z = getZone();
				
				
				z.getZoneDescriptor().setUnattended(unattended);
				System.out.println("Majd itt updateljuk a " + z + " zonat unattended = " + unattended + "-re");
				DlvAdminManager.getInstance().updateZoneUnattendedDeliveryStatus(z.getZoneCode(),unattended);
				//DlvAdminManager.getInstance().updateChefsTableZone(
						//z.getZoneCode(), ctActive, z.getCtReleaseTime());
			}
		//} catch (DlvResourceException de) {
		} catch (Exception de) {
			throw new ApplicationRuntimeException(de);
		}
	}
}
