package com.freshdirect.webapp.taglib.test.fdstore;

import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

import com.freshdirect.framework.util.log.LoggerFactory;

import java.util.Collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import java.io.IOException;


import org.apache.log4j.Category;


public class SampleDistributionGetterTag extends AbstractGetterTag implements SessionName {
	
	private static Category LOGGER = LoggerFactory.getInstance(SampleDistributionGetterTag.class);
	
	private Set extensions;
	
	public void setExtensions(String extensions) {
		this.extensions = new HashSet(Arrays.asList(extensions.split(",")));
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7754102761798025758L;

	protected Object getResult()  {
		try {
			

			List files = null;
			try {
				files = DistributionHelper.getDistributions();
			} catch(IOException e) {
				LOGGER.warn("Could not process distro repository");
				return Collections.emptyList();
			}
			
			List result = new ArrayList();
		
			for(Iterator i = files.iterator(); i.hasNext(); ) {
				String fileName = (String)i.next();
				String extension = fileName.substring(fileName.lastIndexOf('.'));
				if (extensions.contains(extension)) result.add(fileName);
			}

			return result;
		} catch (RuntimeException e) {
			LOGGER.warn(e);
			return Collections.emptyList();
		}
	}

	public static class TagEI extends AbstractGetterTag.TagEI {
		protected String getResultType() {
			return "java.util.List";
		}
	}
}
