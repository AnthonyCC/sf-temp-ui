package com.freshdirect.webapp.taglib.coremetrics;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.coremetrics.CmContext;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CmInitTag extends AbstractCmTag {
	private static final Logger LOGGER = LoggerFactory.getInstance(CmInitTag.class);
	private static final String INCLUDE_CM_JS = "<script type=\"text/javascript\" src=\"//libs.coremetrics.com/eluminate.js\"></script>";

	@Override
	protected String getFunctionName() {
		return "cmSetClientID";
	}
	
	@Override
	protected void doCmTag(StringBuilder sb) throws JspException, IOException {
		if (sb==null){
			JspWriter out = getJspContext().getOut();
			out.println(INCLUDE_CM_JS);
			out.println(wrapIntoScriptTag(getSetClientIdScript()));
		} else {
			sb.append(INCLUDE_CM_JS).append("\n");
			sb.append(wrapIntoScriptTag(getSetClientIdScript())).append("\n");
		}
	}
	
	private String getSetClientIdScript(){
		final CmContext ctx = CmContext.getContext();

		String setClientIdScript = getFormattedTag( 
				toJsVar(ctx.getCompoundId()), 
				FDStoreProperties.getCoremetricsDataCollectionMethod(), //boolean value
				toJsVar(FDStoreProperties.getCoremetricsDataCollectionDomain()), 
				toJsVar(FDStoreProperties.getCoremetricsCookieDomain()) );
		
		//LOGGER.debug(setClientIdScript);
		return setClientIdScript;
	}

	/**
	 * Class overrides doCmTag so getTagJs is not used
	 */
	@Override
	public String getTagJs() {
		throw new UnsupportedOperationException();
	}
}
