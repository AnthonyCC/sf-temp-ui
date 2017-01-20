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
<%@ page import="com.freshdirect.fdstore.FDStoreProperties" %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus id="user" guestAllowed='true' recognizedAllowed='true' />

<%
String template = "/common/template/delivery_info_nav.jsp";

boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
if (mobWeb) {
	template = "/common/template/mobileWeb.jsp"; //mobWeb template
}
%>

<tmpl:insert template='<%=template %>'>
	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="delivery_jersey_shore"></fd:SEOMetaTag>
	</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<div class="delivery_info_mobweb_nav" <%= mobWeb ? "" : "style='display: none;'" %>>
			<%@ include file="/help/delivery_info_nav.jspf" %>
		</div>
		<fd:IncludeMedia name="/media/editorial/summer_services/jersey_shore/jersey_shore_service.html" />
	</tmpl:put>
</tmpl:insert>