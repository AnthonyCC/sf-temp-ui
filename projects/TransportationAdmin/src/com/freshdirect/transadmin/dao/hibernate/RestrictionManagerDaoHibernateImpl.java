package com.freshdirect.transadmin.dao.hibernate;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.dao.RestrictionManagerDaoI;
import com.freshdirect.transadmin.model.GeoRestriction;
import com.freshdirect.transadmin.util.TransStringUtil;

public class RestrictionManagerDaoHibernateImpl extends BaseManagerDaoHibernateImpl implements RestrictionManagerDaoI {

	public Collection getGeoRestrictions() throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from GeoRestriction");
		
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
	
	public GeoRestriction getGeoRestriction(String id) throws DataAccessException {
		return (GeoRestriction)getEntityById("GeoRestriction","restrictionId",id);
	}
			
	
	public void saveGeoRestriction(GeoRestriction geoRestriction) throws DataAccessException {
		
		if(geoRestriction.getRestrictionId()==null ||"".equals(geoRestriction.getRestrictionId())) {
		}
		else {
			saveEntity(geoRestriction);
		}
	}

	public Collection getGeoRestrictionBoundaries() throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from GeoRestrictionBoundary");
		
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
		

}
