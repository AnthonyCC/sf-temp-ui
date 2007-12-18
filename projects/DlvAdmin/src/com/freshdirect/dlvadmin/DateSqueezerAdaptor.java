package com.freshdirect.dlvadmin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.util.io.SqueezeAdaptor;

public class DateSqueezerAdaptor implements SqueezeAdaptor {
	
	private final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	private static final String PREFIX = "D";

	public String getPrefix() {
		return PREFIX;
	}

	public Class getDataClass() {
		return Date.class;
	}

	public String squeeze(DataSqueezer squeezer, Object o) {
		return PREFIX + this.sf.format((Date)o);
	}

	public Object unsqueeze(DataSqueezer squeezer, String str) {
		try {
			return sf.parse(str.substring(1));
		} catch (ParseException e) {
			throw new ApplicationRuntimeException(e);
		}
	}

}
