package com.freshdirect.cms.ui.tapestry;

import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.util.io.SqueezeAdaptor;

import com.freshdirect.cms.ContentKey;

public class ContentKeySqueezeAdaptor implements SqueezeAdaptor {

	private final static String PREFIX = "k";
	
	public String squeeze(DataSqueezer squeezer, Object o) {
		ContentKey k = (ContentKey) o;
		return PREFIX + k.getEncoded();
	}

	public Object unsqueeze(DataSqueezer squeezer, String str) {
		return ContentKey.decode(str.substring(1));
	}

	public Class getDataClass() {
		return ContentKey.class;
	}

	public String getPrefix() {
		return PREFIX;
	}

}