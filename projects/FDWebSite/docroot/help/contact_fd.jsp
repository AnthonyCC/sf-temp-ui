<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='java.text.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.fdstore.rollout.EnumRolloutFeature'%>
<%@ page import='com.freshdirect.fdstore.rollout.FeatureRolloutArbiter'%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato"%>

<% //expanded page dimensions
final int W_CONTACT_FD_TOTAL = 970;
final int W_CONTACT_FD_LEFT_PAD = 300;
%>

<fd:CheckLoginStatus />
<potato:help/>

<script language='javascript'>
<!--
function limitText(textArea, length) {
    if (textArea.value.length > length) {
        textArea.value = textArea.value.substr(0,length);
    }
}
-->
</script>


<%
	
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	FDIdentity identity  = user.getIdentity();

String successPage = "/help/contact_fd_thank_you.jsp";
String overlay = "false";
if(request.getParameter("overlay")!=null){
	overlay=request.getParameter("overlay");
}

boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
String pageTemplate = "/common/template/dnav.jsp";
if (mobWeb) {
	pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
	String oasSitePage = (request.getAttribute("sitePage") != null) ? request.getAttribute("sitePage").toString() : "www.freshdirect.com/help/contact_fd.jsp";
	if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
		request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS	
	}
}

if ("true".equalsIgnoreCase(overlay)) {
	pageTemplate = "/common/template/no_nav_html5.jsp"; //instead of mobWeb template
	successPage += "?overlay=true";
}


%>

<fd:ContactFdController result="result" successPage='<%= successPage %>'>

<tmpl:insert template='<%= pageTemplate %>'>
    <tmpl:put name='extraCss' direct='true'>
    	<% if (mobWeb && "true".equalsIgnoreCase(overlay)) { %>
    		<jwr:style src="/mobileweb.css" media="all" /><%-- mobileweb should be last, for overriding --%>
    	<% } %>
   	</tmpl:put>
    <tmpl:put name="seoMetaTag" direct='true'>
      <fd:SEOMetaTag title="FreshDirect - Help - Contact Us"/>
    </tmpl:put>
<%--     <tmpl:put name='title' direct='true'>FreshDirect - Help - Contact Us</tmpl:put> --%>
    <tmpl:put name='content' direct='true'>
	    	
			<div class="contact_fd-cont"><%@ include file="/help/i_contact_us.jspf" %></div>
</tmpl:put>
</tmpl:insert>

</fd:ContactFdController>