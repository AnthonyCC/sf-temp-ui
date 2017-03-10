package com.freshdirect.mobileapi.controller.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.response.AtpError;
import com.freshdirect.mobileapi.controller.data.response.AtpError.Group;
import com.freshdirect.mobileapi.model.data.Unavailability;

public class AtpErrorData implements DateFormat {
	private Date firstAvailableDate;
	private List<Group> groups = new ArrayList<Group>();
	private List<String> unreadKeys;
	private Unavailability unavailability;
	protected final SimpleDateFormat formatter = new SimpleDateFormat(STANDARDIZED_DATE_FORMAT);
	public String getFirstAvailableDate() {
		return (firstAvailableDate != null ? formatter.format(firstAvailableDate) : null);
	}

	public void setFirstAvailableDate(String firstAvailableDate)
			throws ParseException {
		this.firstAvailableDate = (firstAvailableDate != null ? formatter.parse(firstAvailableDate) : null);
	}

//	public void setFirstAvailableDate(Date firstAvailableDate) {
//		this.firstAvailableDate = firstAvailableDate;
//	}
	
	public List<Group> getGroups() {
		return groups;
	}
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	public List<String> getUnreadKeys() {
		return unreadKeys;
	}
	public void setUnreadKeys(List<String> unreadKeys) {
		this.unreadKeys = unreadKeys;
	}
	public Unavailability getUnavailability() {
		return unavailability;
	}
	public void setUnavailability(Unavailability unavailability) {
		this.unavailability = unavailability;
	}
	
	public static AtpErrorData wrap(AtpError atp) throws ParseException{
		AtpErrorData data = new AtpErrorData();
		data.setFirstAvailableDate(atp.getFirstAvailableDate());
		data.setGroups(atp.getGroups());
		data.setUnreadKeys(atp.getUnreadKeys());
		data.setUnavailability(atp.getUnavailability());
		
		return data;
	}
	
	

}
