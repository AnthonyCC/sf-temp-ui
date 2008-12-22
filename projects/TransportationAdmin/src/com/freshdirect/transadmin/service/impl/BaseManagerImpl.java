package com.freshdirect.transadmin.service.impl;

import java.util.Collection;

import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.service.BaseManagerI;

public abstract class BaseManagerImpl implements BaseManagerI {
	
	protected abstract BaseManagerDaoI getBaseManageDao();
	
	public void removeEntity(Collection employees)  {
		getBaseManageDao().removeEntity(employees);		
	}
	
	public void saveEntity(Object entity) {
		getBaseManageDao().saveEntity(entity);
	}
	
	public void saveEntityList(Collection entity) {
		getBaseManageDao().saveEntityList(entity);
	}
	
	public void saveEntityEx(Object entity) {
		getBaseManageDao().saveEntityEx(entity);
		
	}
	
}
