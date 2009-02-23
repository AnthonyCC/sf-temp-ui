package com.freshdirect.transadmin.web.model;

import java.util.Iterator;
import java.util.List;

/**
 * GeoRestrictionBoundary generated by hbm2java
 */
public class SpatialBoundary implements java.io.Serializable {

	private String code;
	private String name;
	
	private List geoloc;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List getGeoloc() {
		return geoloc;
	}

	public void setGeoloc(List geoloc) {
		this.geoloc = geoloc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public SpatialPoint getCenter() {
		double minX = Integer.MAX_VALUE;
		double maxX = Integer.MIN_VALUE;	

		double minY = Integer.MAX_VALUE;
		double maxY = Integer.MIN_VALUE;	
		
		if(geoloc != null) {
			Iterator _iterator = geoloc.iterator();
			SpatialPoint _point = null;
			while (_iterator.hasNext()) {
				_point = (SpatialPoint)_iterator.next();
				double x = _point.getX();
				minX = Math.min( minX, x );
				maxX = Math.max( maxX, x );
	
				double y = _point.getY();
				minY = Math.min( minY, y );
				maxY = Math.max( maxY, y );
			}
		}
		
		
		SpatialPoint center = new SpatialPoint();
		center.setX((minX+maxX)/2);
		center.setY((minY+maxY)/2);

		return center; 
	}
	
	
}