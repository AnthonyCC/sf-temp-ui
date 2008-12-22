package com.freshdirect.transadmin.service;

import java.util.Collection;

public interface BaseManagerI {
	
	void saveEntity(Object entity);
	
	void removeEntity(Collection employees);
	
	void saveEntityList(Collection entity);
	
	void saveEntityEx(Object entity);
}
