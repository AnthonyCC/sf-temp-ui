<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.fdstore.customer.FDCSContactHoursUtil" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCSContactHours" %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.rollout.EnumRolloutFeature'%>
<%@ page import='com.freshdirect.fdstore.rollout.FeatureRolloutArbiter'%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<fd:CheckLoginStatus />
<%
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
List<FDCSContactHours> csHours = FDCSContactHoursUtil.getFDCSHours();

String overlay = "false";
if(request.getParameter("overlay")!=null){
	overlay=request.getParameter("overlay");
}

boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
String pageTemplate = "/common/template/browse_noleftnav_template.jsp";
if (mobWeb) {
	pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
	String oasSitePage = (request.getAttribute("sitePage") != null) ? request.getAttribute("sitePage").toString() : "www.freshdirect.com/help/contact_fd_thank_you.jsp";
	if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
		request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS	
	}
}

if ("true".equalsIgnoreCase(overlay)) {
	pageTemplate = "/common/template/no_nav_html5.jsp"; //instead of mobWeb template
}

%>

<tmpl:insert template='<%= pageTemplate %>'>
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="FreshDirect - THANK YOU"/>
    </tmpl:put>
<%--     <tmpl:put name='title' direct='true'>FreshDirect - Help</tmpl:put> --%>

    <tmpl:put name='extraCss' direct='true'>
    	<% if (mobWeb) { %>
    		<jwr:style src="/mobileweb.css" media="all" /><%-- mobileweb should be last, for overriding --%>
    	<% } %>
   	</tmpl:put>
   	
    <tmpl:put name='content' direct='true'>
	<table id="contactFDThankYou" role="presentation" border="0" cellspacing="0" cellpadding="2" style="width: <%= (mobWeb) ? "100%" : "675px" %>">
	    <tr valign="TOP">
			<td style="width: <%= (mobWeb) ? "100%" : "675px" %>" align="center" class="text13">
				<img src="/media_stat/images/common/check-radio-selected.svg" width="100" height="100" alt="Thank You For Contacting Us">
				<div class="highcontrast header font24 verdana-font bold">Thank You</div>
				<div class="subheader verdana-font">
				<span>We've received your message. </span>
				<div class="help-home-hours">
				<fd:IncludeMedia name="/media/editorial/site_pages/help_home_hours.html" />
				</div>
				</div>
				<a class="cssbutton green ok-button" href="/help/index.jsp" type="button">Ok</a>    
			</td>
		</tr>
	</table>
</tmpl:put>
</tmpl:insert>
