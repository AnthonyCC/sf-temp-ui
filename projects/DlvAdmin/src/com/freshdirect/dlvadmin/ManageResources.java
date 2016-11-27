package com.freshdirect.dlvadmin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.EnumTruckType;
import com.freshdirect.delivery.admin.DlvAdminManager;
import com.freshdirect.delivery.model.DlvRegionModel;
import com.freshdirect.delivery.planning.DlvResourceModel;
import com.freshdirect.delivery.planning.DlvTruckAllocation;
import com.freshdirect.delivery.planning.Week;
import com.freshdirect.framework.core.PrimaryKey;

public abstract class ManageResources extends DlvPage {

	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE '('MM/dd')'");
	private final SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm");
	
	public IPropertySelectionModel getWeekSelectionModel() {
		List weeks = new ArrayList();
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 8);
		weeks.add(new Week(cal.getTime()));
		cal.add(Calendar.DATE, 7);
		weeks.add(new Week(cal.getTime()));
		cal.add(Calendar.DATE, 7);
		weeks.add(new Week(cal.getTime()));
		cal.add(Calendar.DATE, 7);
		weeks.add(new Week(cal.getTime()));
		
		return new SimpleSelectionModel( weeks );
	}
	
	public abstract DlvRegionModel getSelectedRegion();

	public abstract void setSelectedRegion(DlvRegionModel selectedRegion);

	public abstract String getSelectedZone();

	public abstract void setSelectedZone(String zone);

	public SimpleDateFormat getDateFormatter() {
		return dateFormatter;
	}
	
	public void selectWeek(IRequestCycle cycle) {
		this.setSelectedZone(null);
	}
	
	
	public abstract void setDailyResources(Map dailyResources);

	/**
	 * @return Map of Date -> List of DlvResources
	 */
	public abstract Map getDailyResources();

	public void updateCapacities(IRequestCycle cycle){		
		this.getCurrentResource().calculateCapacities();
		try{
			PrimaryKey pk = DlvAdminManager.getInstance()
					.updatePlanningResource(this.getSelectedRegion().getName(),
							this.getSelectedZone(), this.getCurrentResource());
			if (this.getCurrentResource().isAnonymous()) {
				this.getCurrentResource().setPK(pk);
			}
		} catch (DlvResourceException de) {
			throw new ApplicationRuntimeException(de);
		}
	}
	
	public void selectRegion(IRequestCycle cycle){
		this.setSelectedZone(null);
		this.setSelectedWeek(null);
	}
	
	public void selectZone(IRequestCycle cycle){
		try{
			Date[] dates = this.getDays();
			Map daysRes = new HashMap();
			for (int i=0; i<dates.length; i++) {
				daysRes.put(dates[i], DlvAdminManager.getInstance()
						.getResourcesForDate(getSelectedRegion().getName(),
								getSelectedZone(), dates[i]));
			}
			this.setDailyResources(daysRes);
		}catch(DlvResourceException de){
			throw new ApplicationRuntimeException(de);
		}
	}

	public abstract Week getSelectedWeek();

	public abstract void setSelectedWeek(Week selectedWeek);
	
	public Date[] getDays(){
		return this.getSelectedWeek().getDays();
	}
	
	public SimpleDateFormat getTimeFormatter() {
		return timeFormatter;
	}
	
	public abstract DlvResourceModel getCurrentResource();
	
	public abstract EnumTruckType getCurrentTruckType();
	
	public IPropertySelectionModel getZoneSelectionModel() {
		DlvAdminEngine eng = (DlvAdminEngine)this.getEngine();
		return eng.getZoneSelectionModel(getSelectedRegion(), this.getSelectedWeek());
	}

	public DlvTruckAllocation getCurrentTruckAllocation() {
		return this.getCurrentResource().getTruckAllocation(getCurrentTruckType());
	}

}
