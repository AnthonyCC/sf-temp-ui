package com.freshdirect.webapp.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import com.freshdirect.framework.template.TemplateException;



/**
 * Helper to include additional media in template files
 * Sample usage:
 * 
 * "${helper.includeMedia("/media/editorial/site_pages/delivery_plan_table.html", parameters)}"
 * 
 * @author segabor
 * @see {@link MediaUtils}
 *
 */
public class MediaHelper {
	public String includeMedia(String name, Map parameters) throws TemplateException, IOException {
		StringWriter sout = new StringWriter();
		MediaUtils.render(name, sout, parameters, Boolean.TRUE);
		return sout.toString();
	}
}
