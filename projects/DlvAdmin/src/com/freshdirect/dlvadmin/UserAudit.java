/**
 * 
 */
package com.freshdirect.dlvadmin;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.valid.ValidationDelegate;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.audit.SessionAuditor;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.TimeOfDay;

/**
 * @author kocka
 * 
 */
public abstract class UserAudit extends DlvPage {

	private Format dateFormat = new SimpleDateFormat("M/d/yyyy");

	private Format timeFormat = new SimpleDateFormat("hh:mm aa");

	public Format getDateFormat() {
		return dateFormat;
	}

	public Format getTimeFormat() {
		return timeFormat;
	}

	public abstract Date getStartDate();

	public abstract Date getEndDate();

	public abstract TimeOfDay getStartTime();

	public abstract TimeOfDay getEndTime();

	public Date calculateDate(int delta) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, delta);
		return cal.getTime();
	}

	public void runReport() {
		ValidationDelegate delegate = (ValidationDelegate) getBeans().getBean(
				"delegate");
		if (!validDates()) {
			delegate.record("Start must be before end", null);
			return;
		}
	}

	private boolean validDates() {
		return getStartTime() != null && getStartDate() != null
				&& getEndTime() != null && getEndDate() != null
				&& getStartDateTime().before(getEndDateTime());
	}

	private Date getStartDateTime() {
		return getStartTime().getAsDate(getStartDate());
	}

	private Date getEndDateTime() {
		return getEndTime().getAsDate(getEndDate());
	}

	public List getReport() throws DlvResourceException {
		if (!validDates()) {
			return null;
		}
		DateRange range = new DateRange(getStartDateTime(), getEndDateTime());
		List report = SessionAuditor.getInstance().getSessionLogEntries(range);
		return report;
	}

}
