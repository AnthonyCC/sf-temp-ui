package com.freshdirect.webapp.taglib.coremetrics;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;


public abstract class AbstractCmTag extends SimpleTagSupport {
	private static final Logger LOGGER = LoggerFactory.getInstance(AbstractCmTag.class);
	private static final String START_SCRIPT_TAG = "<script type=\"text/javascript\">";
	private static final String END_SCRIPT_TAG = "</script>";
	private static final String START_FUNCTION = "function() {";
	private static final String END_FUNCTION = "}";
	private static final String ATTR_DELIMITER = "-_-";
	
	private static final String AGENT_SESSION_NAME = "EDITING_AGENT";
		
	private boolean wrapIntoScriptTag;
	private boolean wrapIntoFunction;
	
	public void doTag() throws JspException, IOException {
		if (FDStoreProperties.isCoremetricsEnabled()){
			doCmTag();
		} else {
			LOGGER.debug("Coremetrics is disabled");
		}
	}
	
	public void doCmTag() throws JspException, IOException{
		try {
			checkContext();
			String tagOut = getTagJs();
			tagOut = wrapIntoFunction ? wrapIntoFunction(tagOut) : tagOut;
			tagOut = wrapIntoScriptTag ? wrapIntoScriptTag(tagOut) : tagOut;
			LOGGER.debug(tagOut);
			getJspContext().getOut().println(tagOut);

		} catch (SkipTagException e) {
			LOGGER.debug("no tag will be inserted here due to SkipTagException: "+ e.getMessage());
			if (wrapIntoFunction){
				//print empty function
				getJspContext().getOut().println(START_FUNCTION+END_FUNCTION);
			}
		} catch (CmContextException e) {
			LOGGER.debug("CmContextException: " + e.getMessage());
		}
	}
	
	protected void checkContext() throws CmContextException {
		HttpSession session = ((PageContext)getJspContext()).getSession();
		CrmAgentModel agent = (CrmAgentModel) session.getAttribute(AGENT_SESSION_NAME);
		
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		String masqueradeAgent = (user == null) ? null : user.getMasqueradeAgent();
		
		if((agent!=null || masqueradeAgent!=null) && !insertTagInCaseOfCrmContext()){
			throw new CmContextException("Context is CRM! No Coremetrics action needed.");
		}
	}
	
	protected boolean insertTagInCaseOfCrmContext(){
		return false;
	}

	protected static String wrapIntoScriptTag(String tagJs){
		return START_SCRIPT_TAG + tagJs + END_SCRIPT_TAG;
	}
	
	protected static String wrapIntoFunction(String tagJs){
		return START_FUNCTION + tagJs + END_FUNCTION;
	}
	
	protected static String toJsVar(Object var){
		if (var==null){
			return "null";
		} else {
			return "\""+StringUtil.escapeJavaScript(var.toString())+"\"";
		}
	}
	
	protected static String toJsObject(Object var){
		if (var==null){
			return "null";
		} else {
			return StringUtil.escapeJavaScript(var.toString());
		}
	}

	protected abstract String getTagJs() throws SkipTagException; 
	
	public void setWrapIntoScriptTag(boolean wrapIntoScriptTag){
		this.wrapIntoScriptTag = wrapIntoScriptTag;
	}

	public void setWrapIntoFunction(boolean wrapIntoFunction){
		this.wrapIntoFunction = wrapIntoFunction;
	}
	
	protected static String mapToAttrString(Map<Integer,String> attributesMap){
		StringBuilder attrSb = new StringBuilder();
		
		int maxKey = 0; 
		for (Integer key : attributesMap.keySet()){
			if (key > maxKey){
				maxKey = key;
			}
		}
		
		for (int i=1; i<=maxKey; i++){
			String attribute = attributesMap.get(i);
			attrSb.append(attribute==null? "" : attribute);
			attrSb.append(ATTR_DELIMITER);
		}
		
		if (maxKey>0){
			return attrSb.substring(0, attrSb.length() - ATTR_DELIMITER.length());
		} else {
			return attrSb.toString();
		}
		
	}
}