<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.customer.ErpAddressModel'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='com.freshdirect.common.customer.*' %>
<%@ page import='java.util.*' %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='java.text.DateFormat' %>
<%@ page import='java.text.SimpleDateFormat' %>
<%@ page import="com.freshdirect.webapp.util.MediaHelper" %>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties" %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus id="user" />

<% String siteAccessPage = request.getParameter("siteAccessPage"); 
   String jspTemplate = null;
   boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
   if(siteAccessPage!=null && siteAccessPage.equalsIgnoreCase("delivery")){
	   jspTemplate = "/site_access/site_access.jsp";
   } else {
	   if (mobWeb) {
		   jspTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
	   } else {
	   		jspTemplate = "/common/template/delivery_info_nav.jsp";
	   }
   }
   %>

<%
Map params = new HashMap();
params.put("baseUrl", "");
params.put("helper", new MediaHelper());
//--------OAS Page Variables-----------------------
request.setAttribute("sitePage", "www.freshdirect.com/help/delivery_info.jsp");
request.setAttribute("listPos", "SystemMessage,ZDeliveryRight,DeliveryFees");
%>
<tmpl:insert template='<%= jspTemplate %>'>
    <tmpl:put name='title' direct='true'>Delivery Information</tmpl:put>
    
     <tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="delivery_info_help"></fd:SEOMetaTag>
	</tmpl:put>
	
    <tmpl:put name='content' direct='true'>
    	<% if(mobWeb) { %>
		<div class="delivery_info_mobweb_nav" <%= mobWeb ? "" : "style='display: none;'" %>>
			<%@ include file="/help/delivery_info_nav.jspf" %>
		</div>
		<% } %>
		<%if(siteAccessPage==null || !siteAccessPage.equalsIgnoreCase("delivery")){%>
			<fd:IncludeMedia name="/media/editorial/site_pages/delivery_info.html" parameters="<%=params%>" withErrorReport="true"/>
		<%}else{ %>
			<fd:IncludeMedia name="/media/editorial/site_access/deliveryinfo.html" withErrorReport="true"/>
		<% } %>
    </tmpl:put>
</tmpl:insert>
