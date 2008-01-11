package com.freshdirect.fdstore.dcpd;

import java.util.List;

/**
 * 
 * @author segabor
 *
 */
public class DCPDQueryContext {
	DCPDQuery query;
	
	List goodKeys;
	boolean skipUnavailableItems = false; // don't display unavailable SKUs
	boolean renderCSV = false; // render CSV output instead of HTML

	
	public DCPDQueryContext(DCPDQuery query) {
		this.query = query;
		if (query != null) {
			setGoodKeys(query.getContentKeys());
		}
	}

	public DCPDQuery getQuery() {
		return query;
	}


	public List getGoodKeys() {
		return goodKeys;
	}
	public void setGoodKeys(List goodKeys) {
		this.goodKeys = goodKeys;
	}

	public boolean isSkipUnavailableItems() {
		return skipUnavailableItems;
	}
	public void setSkipUnavailableItems(boolean skipUnavailableItems) {
		this.skipUnavailableItems = skipUnavailableItems;
	}

	public boolean isRenderCSV() {
		return renderCSV;
	}
	public void setRenderCSV(boolean renderCSV) {
		this.renderCSV = renderCSV;
	}
}
