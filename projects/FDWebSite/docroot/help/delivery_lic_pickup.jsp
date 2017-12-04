<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.storeapi.content.*' %>
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
params.put("uri", request.getRequestURI());
params.put("helper", new MediaHelper());
params.put("fromZipCheck", Boolean.FALSE);
params.put("isPopup", Boolean.FALSE);
params.put("isPopupAndNotFromZipCheck", Boolean.FALSE); // = (isPopup && !fromZipCheck)
if (user != null) {
	params.put("isUserSignedIn", Boolean.valueOf(user.getLevel() >= FDUserI.RECOGNIZED) );
	params.put("minimumOrderAmount", new Double(user.getMinimumOrderAmount()) );
	params.put("customerServiceContact", user.getCustomerServiceContact() ); // String
	params.put("isUserEligibleForSignupPromotion", Boolean.valueOf(user.isEligibleForSignupPromotion()) );
} else {
    params.put("isUserSignedIn", Boolean.FALSE );
    params.put("minimumOrderAmount", new Double(0) );
    params.put("customerServiceContact", "" ); // String
    params.put("isUserEligibleForSignupPromotion", Boolean.FALSE );
}
String template = "/common/template/delivery_info_nav.jsp";

boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
if (mobWeb) {
	template = "/common/template/mobileWeb.jsp"; //mobWeb template
}
%>
<tmpl:insert template='<%=template %>'>
<%-- 	<tmpl:put name='title' direct='true'>Delivery Information</tmpl:put> --%>
	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="Delivery Information" pageId="delivery_lic"></fd:SEOMetaTag>
	</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<div class="delivery_info_mobweb_nav" <%= mobWeb ? "" : "style='display: none;'" %>>
			<%@ include file="/help/delivery_info_nav.jspf" %>
		</div>
		<fd:IncludeMedia name="/media/editorial/site_pages/delivery_info/pickup/main.ftl" parameters="<%=params%>" withErrorReport="true"/>
	</tmpl:put>
</tmpl:insert>
