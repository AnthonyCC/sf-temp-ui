<%@page import="com.freshdirect.framework.util.StringUtil"%>
<%@ page import='com.freshdirect.storeapi.content.*' %>
<%@ page import='com.freshdirect.storeapi.attributes.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='java.text.*' %>
<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.storeapi.ContentNodeI" %>
<%@ page import="com.freshdirect.webapp.util.FDFaqUtil" %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature" %>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter" %>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@page import="com.freshdirect.storeapi.content.ContentFactory"%>
<%@page import="com.freshdirect.webapp.helppage.data.HelpPageCategoryData"%>
<%@page import="com.freshdirect.cms.core.domain.ContentKey"%>
<%@page import="com.freshdirect.webapp.helppage.data.HelpPageContentNodeData"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="fd-features" prefix="features" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato"%>

<fd:CheckLoginStatus />
<features:isActive name="selfcredit" featureName="backOfficeSelfCredit" />
<potato:help/>
<%
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	FDIdentity identity  = user.getIdentity();
	
	boolean loyaltyHelpContact = false;
	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
	String pageTemplate = "/common/template/no_nav.jsp";
	String oasSitePage = (request.getAttribute("sitePage") == null) ? "www.freshdirect.com/help/index.jsp" : request.getAttribute("sitePage").toString();
	String csNumberMedia_i_contact_us = user.getCustomerServiceContactMediaPath();

	if (mobWeb) {
		pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
		if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
			request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS	
		}
	}
%>

<c:set var = "creditRestricted" value = "${helpPotato.customerCreditRestricted}"/>

<tmpl:insert template='<%= pageTemplate %>'>
<%-- 	<tmpl:put name='title' direct='true'>FreshDirect - Help</tmpl:put> --%>
	<tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="FreshDirect - Help" pageId="index_help" includeSiteSearchLink="true"/>
	</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<% if (mobWeb) { %>

			<div id="help-page">
				<div id="helpcenter">
				    <div class="help help--top-bar flex">
						<div class="help help-center--container">
							<h1 class="help-center--header">Help Center</h1>
							<% if(user.isChefsTable()) {%>
								<p class="help-center--subheader font16">Dedicated Chef's Table specialists are standing by to help.</p>
							<%} else {%>
								<p class="help-center--subheader font16">Dedicated specialists are standing by to help.</p>
							<%} %>
							<div class="search-cont">
								<fd:ContactFdController result="result" successPage=''>
									<form method="post" name="contact_fd" id="contact_fd_faq" class="flex">
										<label for="search-faq" class="offscreen">search</label>
										<input type="text" id="search-faq" class="search-input font16" value="" maxlength="100" name="searchFAQ" placeholder="Search Help Topics" autocomplete="off" />
										<button class="cssbutton green small search-button" type="submit" name="searchFAQButton cssbutton fdxgreen cssbutton-flat"></button>
									</form>
								</fd:ContactFdController>
							</div>
						</div>

						<c:if test="${selfcredit && !creditRestricted}">
							<soy:render template="common.helpSelfCredit" />
						</c:if>
					</div>
				</div>

			</div>
			<div>
				<ul class="mm-listview cleanli">
					<li><a href="/help/contact_fd.jsp">Contact Us</a></li>
					<li>
						<a href="#" class="gen-accord-toggler gen-accord-toggler-gray noClickThrough">FAQ</a>
						<ul class="mm-listview cleanli gen-accord-content" style="display: none;">
							<c:forEach items="${helpPotato.faqSections}" var="contentNode">
								<li class="">
									<a class="" href="/help/faq_home.jsp?page=${contentNode.id}">${contentNode.name}</a>
								</li>
							</c:forEach>
						</ul>
					</li>
					<li><a href="/help/freshness.jsp">Freshness Guarantee</a></li>
					<li>
						<a href="#" class="gen-accord-toggler gen-accord-toggler-gray noClickThrough">Food Safety</a>
						<ul class="mm-listview cleanli gen-accord-content" style="display: none;">
							<c:forEach items="${helpPotato.foodSafetySubCategories}" var="subCategory">
									<li><a href="/browse.jsp?id=${subCategory.contentName}">${subCategory.fullName}</a></li>
							</c:forEach>
						</ul>
					</li>
					<%-- NO TARGET, comment out for now <li><a href="#">Legal</a></li> --%>
				</ul>
			</div>
			
		<% } else { %>

			<div id="help-page">
				<div id="helpcenter">
				<div class="help help--top-bar flex">
					<div class="help help-center--container">
						<h1 class="help-center--header bold">Help Center</h1>
						<% if(user.isChefsTable()) {%>
							<p class="help-center--subheader font16">Dedicated Chef's Table specialists are standing by to help.</p>
						<%} else {%>
							<p class="help-center--subheader font16">Dedicated specialists are standing by to help.</p>
						<%} %>
						<div class="search-cont">
							<form method="post" name="contact_fd" id="contact_fd_faq" class="flex">
								<label for="search-faq" class="offscreen">search</label><input type="text" id="search-faq" class="search-input font16" value="" maxlength="100" name="searchFAQ" placeholder="Search Help Topics" />
								<button class="cssbutton green small search-button fdxgreen cssbutton-flat" type="submit" name="searchFAQButton"></button>
							</form>
						</div>
					</div>
					<c:if test="${selfcredit && !creditRestricted}">
						<soy:render template="common.helpSelfCredit" />
					</c:if>
				</div>
				<div class="help--main-content flex">
						<div id="helpsidecontent">
							<soy:render template="common.helpSideContent"/> 
							<ul class="mm-listview cleanli gen-accord-content your-account link-list-container verdana-font font16">
								<c:forEach items="${helpPotato.faqSections}" var="contentNode">
									<li class="">
										<a class="" href="/help/faq_home.jsp?page=${contentNode.id}">${contentNode.name}</a>
									</li>
								</c:forEach>
							</ul>
						</div>
					<%-- CONTACT FORM --%>
					<div class="main-content--right">
						<div class="help-contact-form">
						<%@ include file="/help/i_contact_us.jspf" %>
						</div>
					</div>
				</div>

				<%-- FOOTER --%>
				<div class="help footer-container">
					<div id="helpfooter" class="flex">
						<div class="help-aside font16 verdana-font flex">
							<div>Email us:<a href="mailto:${helpPotato.customerServiceEmail }" class="green">${helpPotato.customerServiceEmail }</a></div>
						</div>
						<div class="helpline-details font16 verdana-font flex">
							<div><span>Call us: </span><fd:IncludeMedia name="<%= csNumberMedia_i_contact_us %>" /></div>
						</div>
					</div>
				</div>
			<div>
		<% } %>

    <script type="application/ld+json">
    { 
      "@context" : "http://schema.org",
      "@type" : "Organization",
      "url" : "https://www.freshdirect.com/",
      "contactPoint" : [
        { "@type" : "ContactPoint",
          "telephone" : <%=StringUtil.quote(user.getCustomerServiceContact())%>,
          "contactType" : "customer service"
        }
      ]
    }

    </script>
	<script>
		window.FreshDirect = window.FreshDirect || {};
		var fd = window.FreshDirect;
		var dataLayer = window.dataLayer || [];
		
		fd.gtm = fd.gtm || {};
		fd.gtm.key = '<%= FDStoreProperties.getGoogleTagManagerKey() %>';
		fd.gtm.auth = '<%= FDStoreProperties.getGoogleTagManagerAuthToken() %>';
		fd.gtm.preview = '<%= FDStoreProperties.getGoogleTagManagerPreviewId() %>';
		fd.gtm.gakey = '<%= FDStoreProperties.getGoogleAnalyticsKey() %>';
		fd.gtm.gadomain = '<%= FDStoreProperties.getGoogleAnlayticsDomain() %>';
	</script>
	<jwr:script src="/fdgtm.js" useRandomParam="false" />
	</tmpl:put>
</tmpl:insert>
