package com.freshdirect.transadmin.service.impl;

import java.util.Collection;

import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.dao.ZoneManagerDaoI;
import com.freshdirect.transadmin.service.ZoneManagerI;

public class ZoneManagerImpl extends BaseManagerImpl implements ZoneManagerI {
	
	private ZoneManagerDaoI zoneManagerDAO = null;
	
	protected BaseManagerDaoI getBaseManageDao() {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection getActiveZoneCodes() {
		// TODO Auto-generated method stub
		return getZoneManagerDAO().getActiveZoneCodes();
	}

	public ZoneManagerDaoI getZoneManagerDAO() {
		return zoneManagerDAO;
	}

	public void setZoneManagerDAO(ZoneManagerDaoI zoneManagerDAO) {
		this.zoneManagerDAO = zoneManagerDAO;
	}

	public Collection getActiveZoneCodes(String date) {
		return getZoneManagerDAO().getActiveZoneCodes(date);
	}

}
