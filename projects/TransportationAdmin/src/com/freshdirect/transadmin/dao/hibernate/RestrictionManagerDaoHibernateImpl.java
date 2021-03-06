package com.freshdirect.transadmin.dao.hibernate;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.springframework.dao.DataAccessException;

import com.freshdirect.transadmin.dao.RestrictionManagerDaoI;
import com.freshdirect.transadmin.model.GeoRestriction;
import com.freshdirect.transadmin.model.TimeslotRestriction;
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
			saveEntityEx(geoRestriction);
		}
		else {
			saveEntity(geoRestriction);
		}
	}

	public Collection getGeoRestrictionBoundaries() throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from GeoRestrictionBoundary ORDER BY name");
		
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}
		    
	public Collection getGeoRestrictionDays(String restrictionId)  throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from GeoRestrictionDays gd");
		//strBuf.append(" where gd.restrictionId='").append(restrictionId).append("'");		
		strBuf.append(" where gd.restrictionDaysId.restrictionId='").append(restrictionId).append("'");		
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}

	@Override
	public TimeslotRestriction getTimeslotRestriction(String id)
			throws DataAccessException {
		return (TimeslotRestriction)getEntityById("TimeslotRestriction","id",id);
		}

	@Override
	public Collection getTimeslotRestrictions() throws DataAccessException {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("from TimeslotRestriction");
		
		return (Collection) getHibernateTemplate().find(strBuf.toString());
	}

	@Override
	public void saveTimeslotRestriction(TimeslotRestriction tsRestriction)
			throws DataAccessException {
		
		if(tsRestriction.getId()==null ||"".equals(tsRestriction.getId())) {
			saveEntityEx(tsRestriction);
		}
		else {
			saveEntity(tsRestriction);
		}
	}
}
