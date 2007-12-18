package com.freshdirect.dlvadmin;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.freshdirect.delivery.model.DlvRegionModel;

public abstract class HomePage extends DlvPage {
	
	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	
	public SimpleDateFormat getDateFormatter() {
		return this.dateFormatter;
	}
	
	public abstract DlvRegionModel getSelectedRegion();

	public abstract void setSelectedRegion(DlvRegionModel selectedRegion);
	
	public abstract Date getVersion();
	
	public abstract void setVersion(Date version);
}
