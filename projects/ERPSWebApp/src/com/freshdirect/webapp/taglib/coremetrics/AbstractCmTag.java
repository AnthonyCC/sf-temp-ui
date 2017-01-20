package com.freshdirect.webapp.taglib.coremetrics;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.common.context.MasqueradeContext;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.coremetrics.CmContext;
import com.freshdirect.fdstore.coremetrics.CmContextUtility;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.coremetrics.tagmodel.AbstractTagModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.crm.CrmSession;
import com.freshdirect.webapp.taglib.fdstore.SessionName;


public abstract class AbstractCmTag extends SimpleTagSupport {
	private static final Logger LOGGER = LoggerFactory.getInstance(AbstractCmTag.class);
	private static final String START_SCRIPT_TAG = "<script type=\"text/javascript\">";
	private static final String END_SCRIPT_TAG = "</script>";
	private static final String START_FUNCTION = "function() {";
	private static final String END_FUNCTION = "}";
	private static final String PARAM_DELIMITER = ",";
	private static final String START_JSON_ARRAY = "[";
	private static final String END_JSON_ARRAY = "]";
	private static final String AGENT_SESSION_NAME = "EDITING_AGENT";
		
	private boolean wrapIntoScriptTag;
	private boolean wrapIntoFunction;
	private String outStringVar = null;
	protected boolean returnAsJson;
	private HttpSession sessionFromSetter;
	private HttpServletRequest requestFromSetter;
	
	/** see /test/coremetrics/jsontest.jsp for examples */
	public static String wrapTagsIntoJson(List<String> tags){
		StringBuilder sb = new StringBuilder();
		sb.append(START_JSON_ARRAY);

		if (FDStoreProperties.isCoremetricsEnabled()){
			for (int i=0; i<tags.size(); i++){
				String tag = tags.get(i);
				if (tag.trim().length()>0){
					if(i>0){
						sb.append(PARAM_DELIMITER);
					}
					sb.append(tag);
				}
			}
		}
		
		sb.append(END_JSON_ARRAY);
		return sb.toString();
	}
	
	public void doTag() throws JspException, IOException {
		doCmTag(null);
	}
	
	public String getTagOutput() {
		StringBuilder sb = new StringBuilder();
		try {
			doCmTag(sb);
		} catch (JspException e) {
			LOGGER.error(e);
		} catch (IOException e) {
			LOGGER.error(e);
		}
		return sb.toString();
	}
	
	protected void doCmTag(StringBuilder sb) throws JspException, IOException{
		if (true){
			try {
				checkContext();
				String tagOut = getTagJs();
				if (!returnAsJson) {
					tagOut = wrapIntoFunction ? wrapIntoFunction(tagOut) : tagOut;
					tagOut = wrapIntoScriptTag ? wrapIntoScriptTag(tagOut) : tagOut;
				}
				//LOGGER.debug(tagOut);
				if ( outStringVar != null ) {
					getJspContext().setAttribute( outStringVar, tagOut );
				} else {
					if (sb==null){
						getJspContext().getOut().println(tagOut);
					} else {
						sb.append(tagOut);
					}
				}
	
			} catch (SkipTagException e) {
				//LOGGER.debug("no tag will be inserted here due to SkipTagException: "+ e.getMessage());
				handleException(sb);
			} catch (CmContextException e) {
				LOGGER.debug("CmContextException: " + e.getMessage());
				handleException(sb);
			} catch (NullPointerException e){
				LOGGER.debug("NullPointerException, check session, request, jspContext", e);
			}
		} else {
			LOGGER.debug("Coremetrics is disabled");
		}

	}
	
	private void handleException(StringBuilder sb) throws IOException{
		if (wrapIntoFunction && !returnAsJson){
			String out = START_FUNCTION+END_FUNCTION;
			if (sb==null){
				getJspContext().getOut().println(out);
			} else {
				sb.append(out);
			}
		}
	}
	
	public void checkContext() throws CmContextException {
		final HttpSession session = getSession();
		final FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		final boolean allowOverride = insertTagInCaseOfCrmContext();
		if ( !CmContextUtility.isCoremetricsAvailable(user, allowOverride) ) {
			throw new CmContextException("CM events are disabled");
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

	public abstract String getTagJs() throws SkipTagException; 
	
	protected abstract String getFunctionName();

	protected String getFormattedTag(String... params){
		return getFormattedTag(getFunctionName(), params);
	}

	protected String getFormattedTag(String functionName, String[] params){
		StringBuilder sb = new StringBuilder();

		if (returnAsJson){
			sb.append(START_JSON_ARRAY).append(toJsVar(functionName));
		} else {
			sb.append(functionName).append("(");
		}
		
		for (int i=0; i<params.length; i++){
			if(returnAsJson || i>0){
				sb.append(PARAM_DELIMITER);
			}
			sb.append(params[i]);
		}

		if (returnAsJson){
			sb.append(END_JSON_ARRAY);
		} else {
			sb.append(");");
		}

		return sb.toString();
	}
	
	protected String getTagDelimiter(){
		return returnAsJson ? PARAM_DELIMITER : "\n";
	}
	
	public void setWrapIntoScriptTag(boolean wrapIntoScriptTag){
		this.wrapIntoScriptTag = wrapIntoScriptTag;
	}

	public void setWrapIntoFunction(boolean wrapIntoFunction){
		this.wrapIntoFunction = wrapIntoFunction;
	}

	public void setOutStringVar(String outStringVar){
		this.outStringVar = outStringVar;
	}
	
	/** see /test/coremetrics/jsontest.jsp for examples */
	public void setReturnAsJson(boolean returnAsJson) {
		this.returnAsJson = returnAsJson;
	}

	protected HttpSession getSession(){
		if (sessionFromSetter == null){
			PageContext ctx = (PageContext)getJspContext(); 
			if (ctx!=null){
				return ctx.getSession();
			}
		} 
		return sessionFromSetter;
	}
	
	public void setSession(HttpSession session){
		sessionFromSetter = session;
	}

	protected HttpServletRequest getRequest(){
		if (requestFromSetter == null){
			PageContext ctx = (PageContext)getJspContext(); 
			if (ctx!=null){
				return (HttpServletRequest) ctx.getRequest();
			}
		} 
		return requestFromSetter;
	}
	
	public void setRequest(HttpServletRequest request) {
		requestFromSetter = request;
	}
	
	public static String mapToAttrString(Map<Integer,String> attributesMap) {
		return AbstractTagModel.mapToAttrString( attributesMap );
	}
}