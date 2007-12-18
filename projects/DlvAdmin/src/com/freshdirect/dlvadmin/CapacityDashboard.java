package com.freshdirect.dlvadmin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.log4j.Category;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.valid.FieldTracking;
import org.apache.tapestry.valid.IValidationDelegate;
import org.apache.tapestry.valid.ValidatorException;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.admin.DlvAdminManager;
import com.freshdirect.delivery.model.DlvRegionModel;
import com.freshdirect.delivery.model.DlvZoneModel;
import com.freshdirect.delivery.planning.DlvResourceModel;
import com.freshdirect.delivery.planning.Week;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;

public abstract class CapacityDashboard extends DlvPage {

	private final static Category LOGGER = LoggerFactory.getInstance(CapacityDashboard.class);

	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE'<br>'MMMM d, yyyy");
	private final SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm aa");

	public void activateExternalPage(Object[] params, IRequestCycle cycle) {
		this.setDailyResources((this.getSelectedRegion() != null ? this.loadDailyResources() : null));
	}

	public SimpleDateFormat getDateFormatter() {
		return dateFormatter;
	}

	public SimpleDateFormat getTimeFormatter() {
		return timeFormatter;
	}
	public abstract void setDailyResources(Map dailyResources);

	/**
	 * @return Map of (Date -> Map of (DlvResources -> zoneCode))
	 */
	public abstract Map getDailyResources();

	public List getZones() {
		DlvAdminEngine engine = (DlvAdminEngine) this.getEngine();
		if (this.getSelectedZone() == null) {
			return engine.getRegion(this.getSelectedRegion().getName(),
				getSelectedDay() != null ? getSelectedDay() : this.getSelectedWeek().getWeekEnd()).getZones();
		} else {
			List zones = new ArrayList();
			zones.add(engine.getRegion(this.getSelectedRegion().getName(),
				getSelectedDay() != null ? getSelectedDay() : this.getSelectedWeek().getWeekEnd()).getZone(this.getSelectedZone()));
			return zones;
		}
	}


	public IPropertySelectionModel getDaySelectionModel() {
		return new DaySelectionModel(this.getSelectedWeek().getDays());
	}

	public Date[] getDays() {
		if (this.getSelectedDay() != null) {
			return new Date[] {this.getSelectedDay()};
		}
		
		if(this.getSelectedWeek() == null){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 1);
			this.setSelectedWeek(new Week(cal.getTime()));
		}
		
		return this.getSelectedWeek().getDays();
	}

	private Map loadDailyResources() {
		try {
			LOGGER.debug("Start Loading Daily resources");

			Map resources = DlvAdminManager.getInstance().getResourcesForRegionAndDateRange(getSelectedRegion().getPK().getId(),
				this.getDays());
			LOGGER.debug("End Loading Daily resources");
			return Collections.unmodifiableMap(resources);

		} catch (DlvResourceException de) {
			throw new ApplicationRuntimeException(de);
		}
	}

	public void selectRegion(IRequestCycle cycle) {
		this.setSelectedZone(null);
		this.setDailyResources((this.loadDailyResources()));
	}

	public List getResources() {
		HashMap map = (HashMap) this.getDailyResources().get(this.getZone().getZoneCode());
		if (map == null) {
			return Collections.EMPTY_LIST;
		}
		return (List) map.get(this.getDay());
	}

	public List getResourcesForDay() {
		HashMap map = (HashMap) this.getDailyResources().get(this.getZone().getZoneCode());
		if (map == null) {
			return Collections.EMPTY_LIST;
		}
		return (List) map.get(this.getSelectedDay());
	}

	private boolean updateResource(DlvResourceModel resource, String zoneCode, IRequestCycle requestCycle) {
		this.setRecentResourceId(resource.getPK().getId());

		if (resource.isOvercommited()) {
			IValidationDelegate delegate = (IValidationDelegate) this.getBeans().getBean("delegate");
			delegate.setFormComponent(null);
			delegate.record(new ValidatorException("Unable to update - resources are overcommited."));
			getEditableResources().add(resource.getPK().getId());
			setRecentResourceId(resource.getPK().getId());
			return false;
		}
		IValidationDelegate delegate = (IValidationDelegate) this.getBeans().getBean("delegate");
		if(delegate.getHasErrors()){
			LOGGER.info("Form has errors.");
			for(Iterator i = delegate.getFieldTracking().iterator(); i.hasNext();){
				FieldTracking tracking = (FieldTracking) i.next();
				LOGGER.info(tracking.getFieldName() + ": "+tracking.getConstraint());
			}
			getEditableResources().add(resource.getPK().getId());
			setRecentResourceId(resource.getPK().getId());
			return false;
		}
		try {
			DlvAdminManager.getInstance().updatePlanningResource(getSelectedRegion().getName(), zoneCode, resource);

			this.refreshResource(resource.getPK());

		} catch (DlvResourceException de) {
			throw new ApplicationRuntimeException(de);
		}
		return true;
	}

	private void refreshResource(PrimaryKey pk) {
		DlvResourceModel res;
		try {
			res = DlvAdminManager.getInstance().getResourceById(pk.getId());
		} catch (DlvResourceException e) {
			throw new ApplicationRuntimeException(e);
		}
		Map zoneResources = (Map) this.getDailyResources().get(res.getZoneCode());
		List dayResources = (List) zoneResources.get(res.getDay());
		for (ListIterator i = dayResources.listIterator(); i.hasNext();) {
			DlvResourceModel currRes = (DlvResourceModel) i.next();
			if (pk.equals(currRes.getPK())) {
				i.set(res);
			}
		}
	}

	public void updateAssets(IRequestCycle cycle) {
		DlvResourceModel r = this.getResource();
		r.calculateCapacities();
		this.updateResource(r, getZone().getZoneCode(), cycle);
	}

	public void overrideCapacity(IRequestCycle cycle) {
		this.updateResource(this.getResource(), getZone().getZoneCode(), cycle);
	}

	public String getZoneName() {
		return getZone().getZoneCode() + " - " + getZone().getName();
	}

	public void editAllResourcesForZone(IRequestCycle cycle) {
		String zoneCode = (String) cycle.getListenerParameters()[0];
		Map dailyResources = (Map) this.getDailyResources().get(zoneCode);
		boolean recent = false;
		if(dailyResources != null){
			for (Iterator i = dailyResources.values().iterator(); i.hasNext();) {
				List resources = (List) i.next();
				for (Iterator j = resources.iterator(); j.hasNext();) {
					String resId = ((DlvResourceModel) j.next()).getPK().getId();
					if (!recent) {
						this.setRecentResourceId(resId);
						recent = true;
					}
					this.getEditableResources().add(resId);
				}
			}
		}
	}

	public void shutdownZone(IRequestCycle cycle) {
		String zoneCode = (String) cycle.getListenerParameters()[0];
		Map zoneResources = (Map) this.getDailyResources().get(zoneCode);
		if (zoneResources == null) {
			throw new ApplicationRuntimeException("NO Zone found for ZONE_CODE: " + zoneCode);
		}
		List resources = (List) zoneResources.get(this.getSelectedDay());
		if (resources.isEmpty()) {
			throw new ApplicationRuntimeException("NO Resource for ZONE_CODE: " + zoneCode + " and DAY: " + this.getSelectedDay());
		}
		for (Iterator i = resources.iterator(); i.hasNext();) {
			DlvResourceModel r = (DlvResourceModel) i.next();
			r.shutdown();
			this.updateResource(r, zoneCode, cycle);
		}
	}

	public void closeAllResourcesForZone(IRequestCycle cycle) {
		String zoneCode = (String) cycle.getListenerParameters()[0];
		Map zoneResources = (Map) this.getDailyResources().get(zoneCode);
		if(zoneResources != null){
			for (Iterator i = zoneResources.values().iterator(); i.hasNext();) {
				List resources = (List) i.next();
				for (Iterator j = resources.iterator(); j.hasNext();) {
					PrimaryKey pk = ((DlvResourceModel) j.next()).getPK();
					this.getEditableResources().remove(pk.getId());
					this.refreshResource(pk);
				}
			}
		}
	}

	public void editResource(IRequestCycle cycle) {
		String id = (String) cycle.getListenerParameters()[0];
		this.getEditableResources().add(id);
		this.setRecentResourceId(id);
	}

	public void submitCancel(IRequestCycle cycle) {
		this.getEditableResources().remove(this.getResource().getPK().getId());
		this.refreshResource(this.getResource().getPK());
	}
	
	abstract public DlvRegionModel getSelectedRegion();
	abstract public void setSelectedRegion(DlvRegionModel selectedRegion);

	abstract public String getSelectedZone();
	abstract public void setSelectedZone(String selectedZone);

	public abstract Date getSelectedDay();
	public abstract void setSelectedDay(Date selectedDay);

	public abstract String getSelectedView();
	public abstract void setSelectedView(String selectedView);

	public abstract Set getEditableResources();
	public abstract void setEditableResources(Set editableResources);

	abstract public String getRecentResourceId();
	abstract public void setRecentResourceId(String recentResourceId);
	
	public abstract Week getSelectedWeek();
	public abstract void setSelectedWeek(Week selectedWeek);
	
	public abstract Date getDay();
	public abstract void setDay(Date day);
	
	public abstract DlvResourceModel getResource();
	public abstract void setResource(DlvResourceModel resource);
	
	public abstract DlvZoneModel getZone();
	public abstract void setZone(DlvZoneModel zone);

	public String getOnClickScript(String displayName) {
		return "return confirm('SHUTDOWN  " + displayName + " ?');";
	}

}