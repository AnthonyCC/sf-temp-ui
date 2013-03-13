package com.freshdirect.dataloader.notification.ejb;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.EJBObject;

import com.freshdirect.delivery.model.HandoffStatusNotification;

public interface HandoffNotificationSB extends EJBObject{ 

	public List<HandoffStatusNotification> getHandoffStatus() throws RemoteException;

}
