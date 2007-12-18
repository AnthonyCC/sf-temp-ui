package com.freshdirect.ocf.ui.page;

import org.apache.tapestry.IDirect;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RedirectException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.link.DirectLink;

public class TapestryRedirectException extends RedirectException {

	public TapestryRedirectException(String url) {
		super(url);
	}

	public TapestryRedirectException(ILink link) {
		this(link.getURL());
	}

	/**
	 * Use this to redirect the browser to the specified page
	 */
	public TapestryRedirectException(IRequestCycle cycle, String page) {
		this(cycle.getEngine().getService(Tapestry.PAGE_SERVICE).getLink(false, cycle.getPage()));
	}

	/**
	 * Use this to redirect the browser to the specified page that implements the IExternalPage
	 * interface
	 */
	public TapestryRedirectException(IRequestCycle cycle, String page, Object parameters) {
		this(cycle.getEngine().getService(Tapestry.EXTERNAL_SERVICE).getLink(
			false,
			parameters));
	}

	/**
	 * Use this to redirect the browser to the specified listenener on the component 
	 * that implements the IDirect interface.
	 */
	public TapestryRedirectException(IRequestCycle cycle, IDirect direct, Object parameters) {
		this(cycle.getEngine().getService(Tapestry.DIRECT_SERVICE).getLink(
			false,
			direct));
	}
}