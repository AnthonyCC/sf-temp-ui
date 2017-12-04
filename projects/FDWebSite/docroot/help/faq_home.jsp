<%@ page import="java.util.*"%>
<%@ page import="com.freshdirect.webapp.util.MediaHelper" %>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties" %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ page import="com.freshdirect.webapp.util.FDFaqUtil" %>
<%@ page import='com.freshdirect.fdstore.rollout.EnumRolloutFeature'%>
<%@ page import='com.freshdirect.fdstore.rollout.FeatureRolloutArbiter'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ taglib uri='template' prefix='tmpl' 
%><%@ taglib uri='logic' prefix='logic'
%><%@ taglib uri='bean' prefix='bean'
%><%@ taglib uri='freshdirect' prefix='fd'
%><%@ page import='com.freshdirect.webapp.taglib.fdstore.*'
%><fd:CheckLoginStatus /><%

	//expanded page dimensions
	final int W_HELP_FAQ_HOME_TOTAL = 806;

	String faqPage = FDFaqUtil.getFaqHomeId();
	Map params = new HashMap();
	params.put("baseUrl", "");
	params.put("helper", new MediaHelper()); // include helper object. It allows to include media templates into template

	if (request.getParameter("page") != null) {
		faqPage = request.getParameter("page");
	}

	//set up template parameters
	params.put("isPage", Boolean.valueOf(
			request.getParameter("page") != null));
	params.put("isPopup", false);
	FDUserI user2 = (FDUserI) session.getAttribute(SessionName.USER);
	if (user2 != null) {
		params.put("fd_user", user2);
		params.put("minimumOrderAmount", new Integer((int) user2
				.getMinimumOrderAmount()));
		params.put("isUserCheckEligible", Boolean.valueOf(
				(user2 != null && user2.isCheckEligible())));
		params.put("customerServiceContact", user2
				.getCustomerServiceContact());
		params.put("baseDeliveryFee", new Double(user2
				.getBaseDeliveryFee()));
	} else {
		params.put("minimumOrderAmount", new Integer(0));
		params.put("isUserCheckEligible", Boolean.FALSE);
	}
	String faqSections = FDStoreProperties.getFaqSections();
	params.put("faqNodes", FDFaqUtil.getFaqsByCategory(faqPage));

	if (user2 != null ) {
		final java.text.DecimalFormat promoFormatter = new java.text.DecimalFormat(
				"$#,##0");
		params.put("eligibleForSignupPromotion", Boolean.TRUE);
		params.put("maxSignupPromotion", promoFormatter.format(user2
				.getMaxSignupPromotion()));
	} else {
		params.put("eligibleForSignupPromotion", Boolean.FALSE);
		params.put("maxSignupPromotion", "");
	}

	params.put("customerServiceContact", user2
			.getCustomerServiceContact());
	boolean flag = request.getRequestURI().toLowerCase().endsWith(
			"delivery_info_faq.jsp");
	params.put("deliveryInfoFaq", Boolean.valueOf(flag));
	params.put("careerLink", FDStoreProperties.getCareerLink());
	if (user2 != null) {
		params.put("corpDeliveryFee", new Double(user2
				.getCorpDeliveryFee()));
		params.put("corpDeliveryFeeMonday", new Double(user2
				.getCorpDeliveryFeeMonday()));
		params.put("minCorpOrderAmount", new Integer((int) user2
				.getMinCorpOrderAmount()));
	} else {
		params.put("corpDeliveryFee", new Double(0));
		params.put("corpDeliveryFeeMonday", new Double(0));
		params.put("minCorpOrderAmount", new Integer(0));
	}
	if (user2 != null) {
		params.put("isCorp", Boolean.valueOf(user2.isCorporateUser()));
		params.put("isDepotUser", Boolean.valueOf(user2.isDepotUser()));
	} else {
		params.put("isCorp", Boolean.FALSE);
		params.put("isDepotUser", Boolean.FALSE);
	}
	boolean isDefaultFtl=true;
	if (null != faqSections) {
		StringTokenizer st = new StringTokenizer(faqSections, ",");
		while (st.hasMoreTokens()) {
			String nextToken = st.nextToken().trim();
			params.put(nextToken, FDFaqUtil
					.getFaqsByCategory(nextToken));
			if(nextToken.equalsIgnoreCase(faqPage)&& isDefaultFtl){
				isDefaultFtl = false;
			}
		}
	}
	String ftl="/media/editorial/faq/"+faqPage+".ftl";
	String defaultFtl="/media/editorial/faq/intro.ftl";

	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user2) && JspMethods.isMobile(request.getHeader("User-Agent"));
	String pageTemplate = "/common/template/faq_help.jsp";
	String oasSitePage = (request.getAttribute("sitePage") == null) ? "www.freshdirect.com/help/faq_home.jsp" : request.getAttribute("sitePage").toString();
	params.put("mobWeb", mobWeb); /* put this in the params for FTL usage */

	if (mobWeb) {
		pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
		if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
			request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS	
		}
	}
%>
<tmpl:insert template='<%= pageTemplate %>'>
<%--     <tmpl:put name='title' direct='true'>FreshDirect - Help - FAQs</tmpl:put> --%>
    <tmpl:put name="seoMetaTag" direct="true">
    	<fd:SEOMetaTag title="FreshDirect - Help - FAQs" pageId="FAQHome"></fd:SEOMetaTag>
    </tmpl:put>
	<tmpl:put name='leftnav' direct='true'>
	</tmpl:put>
    <tmpl:put name='content' direct='true'>
		<table border="0" cellpadding="0" cellspacing="0" style="width: <%=((mobWeb)?"100%":W_HELP_FAQ_HOME_TOTAL+"px") %>">
		<tr>
			<td valign="top">
				<img src="/media/images/layout/clear.gif" width="10" height="1" alt="" border="0" />
			</td>
			<td>
				<% if(!isDefaultFtl){ %>
					<fd:IncludeMedia name="<%= ftl%>" parameters="<%=params%>" withErrorReport="true"/>
				<% } else { %>
					<fd:IncludeMedia name="<%= defaultFtl%>" parameters="<%=params%>" withErrorReport="true"/>
				<% } %>
			</td>
		</tr>
		</table>
	</tmpl:put>
</tmpl:insert>
