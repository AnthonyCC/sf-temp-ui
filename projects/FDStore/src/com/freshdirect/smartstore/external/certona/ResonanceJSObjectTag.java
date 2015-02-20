package com.freshdirect.smartstore.external.certona;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;

public class ResonanceJSObjectTag extends SimpleTagSupport {
	private static final Logger LOGGER = Logger.getLogger(ResonanceJSObjectTag.class.getSimpleName());
	
	private HttpServletRequest request = null;
	private String action = "";
	private String urlOverride = "";
	
	@Override
	public void doTag() throws JspException, IOException {
		super.doTag();
		
		request = (HttpServletRequest)((PageContext) getJspContext()).getRequest();

		if ("init".equals(action)) {
			LOGGER.debug("INIT PHASE (initialize certona context)");
			assemblyUserCertonaContext(request);
		} else {
			LOGGER.debug("POST PHASE (create resonance object)");
			assemblyResonanceTag();
		}

	}
	

	/**
	 * The 'post-init' part
	 * 
	 * @throws IOException
	 * @throws JspException
	 */
	private void assemblyResonanceTag() throws IOException, JspException {

		String pageURI = request.getRequestURI();
		String certonaPageId = pageURI.substring(1, pageURI.indexOf(".")).toUpperCase();
		FDUserI user = (FDUserI)request.getSession().getAttribute("fd.user");		
		
		JspWriter out = this.getJspContext().getOut();

		JSONObject certona;
		try {
			certona = CertonaUtil.getCertonaResonanceData(request, certonaPageId, user, urlOverride, null);
		} catch (FDResourceException e) {
			throw new JspException(e);
		}

		out.println("<script>");
		out.print("var certona = JSON.parse('");
		out.print( certona.toString() );
		out.println("');");
		out.println("</script>");

		CertonaUserContextHolder.invalidateCertonaUserContext();

	}
	
	/**
	 * The 'pre-init' part
	 * 
	 * @throws IOException
	 * @throws JspException
	 */
	public static void assemblyUserCertonaContext(HttpServletRequest request) throws JspException {
		
		CertonaUserContextHolder.initCertonaContextFromCookies(request);
		String id = null;
		if (request.getParameterMap().containsKey("id")) {
			id = request.getParameter("id");
		} else if (request.getParameterMap().containsKey("catId")) {
			id = request.getParameter("catId");
		} else if (request.getParameterMap().containsKey("deptId")) {
			id = request.getParameter("deptId");
		}

		if (id != null) {
			CertonaUserContextHolder.setId(id);
		}
		
		String searchParam = request.getParameter("searchParams") == null ? "" : request.getParameter("searchParams").toString();
		CertonaUserContextHolder.setSearchParam(searchParam);
		
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUrlOverride() {
		return urlOverride;
	}

	public void setUrlOverride(String urlOverride) {
		this.urlOverride = urlOverride;
	}

}
