<%@ page import="com.freshdirect.fdstore.FDStoreProperties" %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus id="user" guestAllowed='true' recognizedAllowed='true' />
<% 
String template = "/common/template/no_nav.jsp";

boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
if (mobWeb) {
	template = "/common/template/mobileWeb.jsp"; //mobWeb template
}
%>
<tmpl:insert template='<%=template %>'>
<tmpl:put name='title' direct='true'>FreshDirect - Alcohol Information</tmpl:put>
<tmpl:put name='content' direct='true'>
<% String successPage = request.getParameter("successPage");
    request.setAttribute("listPos", "SystemMessage,SideCartBottom");
%>
<fd:HealthWarningController successPage="<%=successPage%>" result="result">
	<fd:IncludeMedia name="/media/editorial/site_pages/health_warning_2016.html"/>
</fd:HealthWarningController>
</tmpl:put>
</tmpl:insert>