package com.freshdirect.transadmin.web.json;

import java.util.List;

import com.freshdirect.transadmin.model.DeliveryGroup;
import com.freshdirect.transadmin.model.Sector;
import com.freshdirect.transadmin.web.model.SpatialBoundary;

public interface IGeographyProvider {

	SpatialBoundary getGeoRestrictionBoundary(String code);

	SpatialBoundary getZoneBoundary(String code, String startDate);

	List getBoundaries(String code, String startDate);

	boolean doZoneExpansion(String worktable, String zone[][],
			String deliveryFee, String expansionType);

	boolean generateTimeslots(String zone[][], String worktable);

	boolean doGeoRestriction(String zone[][]);

	List<DeliveryGroup> getDeliveryGroups();
	
	boolean addToSnapshot(String servicetypes, String buildings);
		

	boolean doDeliveryGroup(String[][] zone);

	List<Sector> getSector();

	boolean addSector(String[][] _neighbourhoodData);
}
