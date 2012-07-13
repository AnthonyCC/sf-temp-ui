package com.freshdirect.webapp.taglib.coremetrics;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CmInitTag extends AbstractCmTag {
	private static final Logger LOGGER = LoggerFactory.getInstance(CmInitTag.class);
	private static final String INCLUDE_CM_JS = "<script type=\"text/javascript\" src=\"//libs.coremetrics.com/eluminate.js\"></script>";
	private static final String SET_CLIENT_ID_FS = "cmSetClientID(%s,%s,%s,%s);";
	
	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();
		out.println(INCLUDE_CM_JS);
		out.println(wrapIntoScriptTag(getSetClientIdScript()));
	}
	
	private String getSetClientIdScript(){
		String setClientIdScript = String.format(SET_CLIENT_ID_FS, 
				toJsVar(FDStoreProperties.getCoremetricsClientId()), 
				toJsVar(FDStoreProperties.getCoremetricsDataCollectionMethod()), 
				toJsVar(FDStoreProperties.getCoremetricsDataCollectionDomain()), 
				toJsVar(FDStoreProperties.getCoremetricsCookieDomain()) );
		
		LOGGER.debug(setClientIdScript);
		return setClientIdScript;
	}

	/**
	 * Class overrides doTag so getTagJs is not used
	 */
	@Override
	protected String getTagJs() {
		throw new UnsupportedOperationException();
	}
}
