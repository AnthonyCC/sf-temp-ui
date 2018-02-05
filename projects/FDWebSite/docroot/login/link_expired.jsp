<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed='true' recognizedAllowed='true' id='user' />
<%

	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
	String pageTemplate = "/common/template/no_site_nav.jsp"; //default
	if (mobWeb) {
		pageTemplate = "/common/template/mobileWeb.jsp"; //default
		String oasSitePage = (request.getAttribute("sitePage") != null) ? request.getAttribute("sitePage").toString() : "www.freshdirect.com"+request.getRequestURI();
		if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
			request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS
		}
	}
%>
<tmpl:insert template='<%= pageTemplate %>'>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="FreshDirect - Expired Link"/>
    </tmpl:put>
	<%--     <tmpl:put name='title' direct='true'>FreshDirect - Expired Link</tmpl:put> --%>
	<tmpl:put name='content' direct='true'>
		
		<% if (mobWeb) { %>
			<div style="padding: 20px;">Sorry, the link you are using has expired. <a href="/index.jsp">Click here</a> to browse the FreshDirect Web site.</div>
		<% } else { %>
	
			<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="500">
			        <TR VALIGN="TOP">
			            <TD WIDTH="400" COLSPAN="3">
							<!-- <img src="/media_stat/images/template/site_access/identity_confirmed.gif" width="130" height="9" alt="" border="0"><img src="/media_stat/images/layout/clear.gif" width="120" height="1" alt="" border="0"><FONT CLASS="text9">* Required Information</FONT>
		 -->					<BR>
			           		<IMG src="/media_stat/images/layout/999966.gif" ALT="" VSPACE="3" HSPACE="0" WIDTH="400" HEIGHT="1" BORDER="0"><BR>
						</TD>
					</TR>
			        <TR VALIGN="TOP">
			            <TD WIDTH="400" COLSPAN="3">
							Sorry, the link you are using has expired. <a href="/index.jsp">Click here</a> to browse the FreshDirect Web site. 
							
						</TD>
					</TR>	
		
			        <TR VALIGN="TOP">
			            <TD WIDTH="400" COLSPAN="3">
						
		
		
						</TD>
					</TR>	
			</TABLE>
		<% } %>
	</tmpl:put>
</tmpl:insert>

