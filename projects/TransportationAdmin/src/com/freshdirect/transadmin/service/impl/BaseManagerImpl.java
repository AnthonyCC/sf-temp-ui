package com.freshdirect.transadmin.service.impl;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

import com.freshdirect.transadmin.dao.BaseManagerDaoI;
import com.freshdirect.transadmin.service.BaseManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;

public abstract class BaseManagerImpl implements BaseManagerI {
	
	protected abstract BaseManagerDaoI getBaseManageDao();
	
	public void removeEntity(Collection entities)  {
		getBaseManageDao().removeEntity(entities);		
	}
	
	public void removeEntityEx(Object entity)  {
		getBaseManageDao().removeEntityEx(entity);		
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
	
	protected String getParsedDate(Date _date) {

		try {
			return TransStringUtil.getServerDate(_date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
