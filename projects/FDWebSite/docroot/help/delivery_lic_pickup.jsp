<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.customer.ErpAddressModel'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import='java.util.*' %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='java.text.DateFormat' %>
<%@ page import='java.text.SimpleDateFormat' %>
<%@ page import="com.freshdirect.webapp.util.MediaHelper" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_DELIVERY_LIC_PICKUP_TOTAL = 970;
final int W_DELIVERY_LIC_PICKUP_LEFT = 724;
final int W_DELIVERY_LIC_PICKUP_CENTER = 55;
final int W_DELIVERY_LIC_PICKUP_RIGHT = 191;
%>


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
%>
<tmpl:insert template='/common/template/delivery_info_nav.jsp'>
	<tmpl:put name='title' direct='true'>Delivery Information</tmpl:put>
	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="delivery_lic"></fd:SEOMetaTag>
	</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<fd:IncludeMedia name="/media/editorial/site_pages/delivery_info/pickup/main.ftl" parameters="<%=params%>" withErrorReport="true"/>
	</tmpl:put>
</tmpl:insert>
