<%@ page import="java.util.*"%>
<%@ page import="com.freshdirect.fdstore.mail.*"%>
<%@ page import='com.freshdirect.storeapi.content.*'  %>
<%@ page import='com.freshdirect.fdstore.referral.FDReferralManager'  %>
<%@ page import='com.freshdirect.fdstore.customer.*'  %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.customer.*"%>
<%@ page import='com.freshdirect.fdstore.survey.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@page import="com.freshdirect.common.customer.EnumServiceType"%>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="java.net.URLEncoder" %>
<%@ page import='java.text.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato"%>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-features" prefix="features" %>

<% //expanded page dimensions
final int W_YA_CUSTOMER_PROFILE_SUMMARY_TOTAL = 970;
%>
<%
String successPage = "/your_account/credits.jsp";
String redirectPage = "/login/login.jsp?successPage=" + successPage;
request.setAttribute("sitePage", "www.freshdirect.com/your_account/customer_profile_summary.jsp");
request.setAttribute("listPos", "HPLeftTop");
%>
<fd:CheckLoginStatus id="user" guestAllowed='false' recognizedAllowed='false' redirectPage='<%=redirectPage%>'/>

<%
String template = "/common/template/no_nav.jsp";

boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
if (mobWeb) {
	template = "/common/template/mobileWeb.jsp"; //mobWeb template
	String oasSitePage = request.getAttribute("sitePage").toString();
	if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
		request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS
	}
}
%>

<tmpl:insert template='<%=template %>'>
	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="FreshDirect - Your Profile" pageId="credit"></fd:SEOMetaTag>
		<jwr:style src="/your_account.css" media="all" />
	</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<soy:import packageName="common" />
		<soy:import packageName="youraccount" />
		<features:isActive name="selfcredit" featureName="backOfficeSelfCredit" />
		<potato:storeCreditsPotato featureActive="${selfcredit}"/>
		<soy:render template="youraccount.accountCredits" data="${storeCredits}" />


		<script>
			window.FreshDirect.storeCredits = <fd:ToJSON object="${storeCredits}" noHeaders="true"/>
			console.log(window.FreshDirect.storeCredits);
		</script>
	</tmpl:put>
</tmpl:insert>
