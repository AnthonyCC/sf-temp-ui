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
<%
// setting up template parameters
Map params = new HashMap();
params.put("baseUrl", "");
params.put("helper", new MediaHelper());
if (user != null) {
    params.put("minCorpOrderAmount", new Integer((int)user.getMinCorpOrderAmount()) );
    params.put("corpDeliveryFee", new Double(user.getCorpDeliveryFee()) );
    params.put("corpDeliveryFeeMonday", new Double(user.getCorpDeliveryFeeMonday()) );
} else {
    params.put("minCorpOrderAmount", new Integer(0) );
    params.put("corpDeliveryFee", new Integer(0) );
    params.put("corpDeliveryFeeMonday", new Integer(0) );
}

request.setAttribute("survey_source","deliveryInfo Page");

request.setAttribute("listPos", "SystemMessage,DeliveryFees");

String template = "/common/template/delivery_info_nav.jsp";

boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
if (mobWeb) {
	template = "/common/template/mobileWeb.jsp"; //mobWeb template
}
%>
<tmpl:insert template='<%=template %>'>
	<tmpl:put name='title' direct='true'>Delivery Information</tmpl:put>
	 <tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="delivery_info_cos"></fd:SEOMetaTag>
	</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<% if(mobWeb) { %>
		<div class="delivery_info_mobweb_nav" <%= mobWeb ? "" : "style='display: none;'" %>>
			<%@ include file="/help/delivery_info_nav.jspf" %>
		</div>
		<% } %>
		<fd:IncludeMedia name="/media/editorial/site_pages/delivery_info/corp/main.ftl" parameters="<%=params%>" withErrorReport="true"/>		
		<%@ include file="/survey/includes/cos.jsp" %>
    </tmpl:put>
</tmpl:insert>
