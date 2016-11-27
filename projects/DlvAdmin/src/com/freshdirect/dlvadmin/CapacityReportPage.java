package com.freshdirect.dlvadmin;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.form.IPropertySelectionModel;

import com.freshdirect.fdstore.FDDeliveryManager;

public abstract class CapacityReportPage extends DlvPage {

	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE MMMM d, yyyy");
	
	public IPropertySelectionModel getDaySelectionModel() {
		ObjectSelectionModel sm = new ObjectSelectionModel();
		Calendar startCal = Calendar.getInstance();
		startCal.set(Calendar.HOUR, 0);
		startCal.set(Calendar.MINUTE, 0);
		startCal.set(Calendar.SECOND, 0);
		startCal.set(Calendar.MILLISECOND, 0);
		Calendar endCal = (Calendar) startCal.clone();
		endCal.add(Calendar.DATE, 7);
		sm.add(null, "Select A Day");
		while (startCal.before(endCal)) {
			sm.add(startCal.getTime(), dateFormatter.format(startCal.getTime()));
			startCal.add(Calendar.DATE, 1);
		}
		return sm;
	}

	public abstract Date getSelectedDay();

	public abstract void setSelectedDay(Date selectedDay);

	public List getCapacityInfos() {
		return FDDeliveryManager.getInstance().getTimeslotCapacityInfo(this.getSelectedDay());
	}

}
