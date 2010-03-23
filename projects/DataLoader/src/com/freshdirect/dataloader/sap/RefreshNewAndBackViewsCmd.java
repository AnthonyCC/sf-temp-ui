package com.freshdirect.dataloader.sap;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

public class RefreshNewAndBackViewsCmd {
	private static final Logger LOGGER = LoggerFactory.getInstance(RefreshNewAndBackViewsCmd.class);

	public static void main(String[] args) {
		Context ctx = null;
		try {
			ctx = ErpServicesProperties.getInitialContext();
			SAPLoadListener.refreshNewAndBack(ctx);
		} catch (NamingException e) {
			LOGGER.warn("Error opening naming context", e);
		} finally {
			try {
				if (ctx != null)
					ctx.close();
			} catch (NamingException e) {
				LOGGER.warn("Error closing naming context", e);
			}
		}
	}
}
