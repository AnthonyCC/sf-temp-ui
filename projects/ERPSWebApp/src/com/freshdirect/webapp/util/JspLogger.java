/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.webapp.util;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.*;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class JspLogger {

	private JspLogger() {}

	public final static Category GENERIC = LoggerFactory.getInstance("com.freshdirect.FDWebSite.generic");
	public final static Category PRODUCT = LoggerFactory.getInstance("com.freshdirect.FDWebSite.product");
	public final static Category NAV = LoggerFactory.getInstance("com.freshdirect.FDWebSite.nav");
	public final static Category ACCESS = LoggerFactory.getInstance("com.freshdirect.FDWebSite.access");
	public final static Category CHECKOUT = LoggerFactory.getInstance("com.freshdirect.FDWebSite.checkout");
	public final static Category QUICKSHOP = LoggerFactory.getInstance("com.freshdirect.FDWebSite.quickshop");
	public final static Category ATP = LoggerFactory.getInstance("com.freshdirect.FDWebSite.atp");

	public final static Category CC_GENERIC = LoggerFactory.getInstance("com.freshdirect.CallCenter.generic");
	public final static Category CC_CHECKOUT = LoggerFactory.getInstance("com.freshdirect.CallCenter.checkout");
	public final static Category CC_PRODUCT = LoggerFactory.getInstance("com.freshdirect.CallCenter.product");

}