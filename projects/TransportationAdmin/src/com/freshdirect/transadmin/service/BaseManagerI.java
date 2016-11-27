package com.freshdirect.transadmin.service;

import java.util.Collection;

public interface BaseManagerI {
	
	void saveEntity(Object entity);
	
	void removeEntity(Collection entities);
	
	void removeEntityEx(Object entity);
	
	void saveEntityList(Collection entity);
	
	void saveEntityEx(Object entity);
}
