/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.fdstore.depot;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.fdstore.FDDepotManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class GetDepotsTag extends AbstractGetterTag {
	
	boolean includeCorpDepots = false;
	
	public void setIncludeCorpDepots(boolean includeCorpDepots){
		this.includeCorpDepots = includeCorpDepots;
	}

	protected Object getResult() throws FDResourceException {
		List allDepots = new ArrayList(FDDepotManager.getInstance().getDepots());
		if(includeCorpDepots){
			allDepots.addAll(FDDepotManager.getInstance().getCorporateDepots());
		}
		return allDepots;
	}

	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return "java.util.Collection";
		}

	}

}
