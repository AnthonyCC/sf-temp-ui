package com.freshdirect.dlvadmin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DaySelectionModel extends SimpleSelectionModel {

	private static final SimpleDateFormat DAY_FORMATTER = new SimpleDateFormat("EEEE, MMM dd");  
	
	public DaySelectionModel(List values) {
		super(values);
	}

	public DaySelectionModel(Object[] values) {
		super(values);
	}
	
	protected String getLabel(Object o) {
		return o==null ? "Whole Week" : DAY_FORMATTER.format((Date)o);
	}

}
