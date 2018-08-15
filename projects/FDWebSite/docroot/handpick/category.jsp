<%@ page import='com.freshdirect.fdstore.rollout.EnumRolloutFeature'%>
<%@ page import='com.freshdirect.fdstore.rollout.FeatureRolloutArbiter'%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>

<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>


<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>

<fd:CheckLoginStatus id="user"/>
<fd:BrowsePartialRolloutRedirector user="<%=user%>" id="${param.id}"/>
<c:set var="catId" value='${param.id!=null ? param.id : param.catId}'/>

<potato:browse name="browsePotato" specialLayout="true" nodeId='${catId}'/>
<c:set var="sitePage" scope="request" value="${browsePotato.descriptiveContent.oasSitePage}" />
<c:set var="listPos" scope="request" value="SystemMessage,CategoryNote,BrowseTop1,BrowseTop2,BrowseTop3,BrowseBottom1,BrowseBottom2" />
<%
String template = "/common/template/browse_noleftnav_template.jsp";
String oasSitePage = (request.getAttribute("sitePage") == null) ? "www.freshdirect.com/handpick/category.jsp" : request.getAttribute("sitePage").toString();

boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
if (mobWeb) {
	template = "/common/template/mobileWeb.jsp"; //mobWeb template
	if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
		request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS	
	}
}
%>
<tmpl:insert template='<%= template %>'>

  <tmpl:put name='soypackage' direct='true'>
    <soy:import packageName="pdp" />
    <soy:import packageName="browse" />
  </tmpl:put>

  <tmpl:put name='extraCss' direct='true'>
    <jwr:style src="/browse.css" media="all"/>
  </tmpl:put>
  
  <tmpl:put name='containerExtraClass' direct='true'>mealkit</tmpl:put>

<%--   <tmpl:put name='title'>${browsePotato.descriptiveContent.pageTitle}</tmpl:put> --%>
  
  <tmpl:put name="seoMetaTag" direct="true">
    <fd:SEOMetaTag title="${browsePotato.descriptiveContent.pageTitle}" metaDescription="${browsePotato.descriptiveContent.metaDescription}"/>
  </tmpl:put>

  <tmpl:put name='deptnav' direct='true'>
    <div class="browse-titlebar">
      <soy:render template="browse.titleBar" data="${browsePotato.descriptiveContent}" />
    </div>
  </tmpl:put>

  <tmpl:put name="extraJs">
  </tmpl:put>
  
  <tmpl:put name="content" direct='true'>
	<div class="browse-oas-top">
	    <div class="oas-cnt" id="oas_b_CategoryNote"><script type="text/javascript">OAS_AD('CategoryNote');</script></div>
	    <div class="oas-cnt" id="oas_b_BrowseTop1"><script type="text/javascript">OAS_AD('BrowseTop1');</script></div>
	    <div class="oas-cnt left" id="oas_b_BrowseTop2"><script type="text/javascript">OAS_AD('BrowseTop2');</script></div>
	    <div class="oas-cnt right" id="oas_b_BrowseTop3"><script type="text/javascript">OAS_AD('BrowseTop3');</script></div>
   	</div>
   	
   	<div class="clear">
    	<soy:render template="browse.specialMealkitLayoutMedia" data="${browsePotato}" />
    	<soy:render template="browse.specialMealkitLayoutContent" data="${browsePotato}" />
    </div>
    
    <div class="oas-cnt left" id="oas_b_BrowseBottom1"><script type="text/javascript">OAS_AD('BrowseBottom1');</script></div>
    <div class="oas-cnt right" id="oas_b_BrowseBottom2"><script type="text/javascript">OAS_AD('BrowseBottom2');</script></div>

    <script>
        window.FreshDirect = window.FreshDirect || {};
        window.FreshDirect.browse = window.FreshDirect.browse || {};
        window.FreshDirect.browse.data = <fd:ToJSON object="${browsePotato}" noHeaders="true"/>
    </script>
  </tmpl:put>
</tmpl:insert>
