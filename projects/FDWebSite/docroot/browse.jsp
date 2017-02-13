<%@ page import="com.freshdirect.fdstore.FDStoreProperties" %>
<%@ page import="com.freshdirect.fdstore.rollout.EnumRolloutFeature"%>
<%@ page import="com.freshdirect.fdstore.rollout.FeatureRolloutArbiter"%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import='com.freshdirect.webapp.ajax.browse.FilteringFlowType' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>

<%@ taglib uri="fd-data-potatoes" prefix="potato" %>
<%@ taglib uri="unbxd" prefix='unbxd' %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>

<fd:CheckLoginStatus id="user" guestAllowed='true' recognizedAllowed='true' />
<fd:CheckDraftContextTag/>
<fd:SearchRedesignRedirector user="<%=user%>" pageType="<%=FilteringFlowType.PRES_PICKS%>"/>
<fd:BrowsePartialRolloutRedirector user="<%=user%>" id="${param.id}"/>

<%--Might be useless
 <potato:globalnav/> --%> 
<potato:browse/>

<%-- OAS variables --%>
<c:set var="sitePage" scope="request" value="${browsePotato.descriptiveContent.oasSitePage}" />
<c:set var="listPos" scope="request" value="SystemMessage,CategoryNote,BrowseTop1,BrowseTop2,BrowseTop3,BrowseBottom1,BrowseBottom2" />
<%
String template = "/common/template/browse_template.jsp";

boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
if (mobWeb) {
	template = "/common/template/mobileWeb.jsp"; //mobWeb template
	String oasSitePage = request.getAttribute("sitePage").toString();
	if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
		request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS	
	}
}
%>
<unbxd:browseEvent/>


<tmpl:insert template='<%=template %>'>
  <tmpl:put name='cmeventsource' direct='true'>BROWSE</tmpl:put>

  <tmpl:put name='soypackage' direct='true'>
    <soy:import packageName="browse" />
  </tmpl:put>

  <tmpl:put name='extraCss' direct='true'>
    <jwr:style src="/quickshop.css" media="all"/>
    <jwr:style src="/browse.css" media="all"/>
  </tmpl:put>

  <tmpl:put name='containerExtraClass' direct='true'>browse</tmpl:put>

  <tmpl:put name='title'>${browsePotato.descriptiveContent.pageTitle}</tmpl:put>
  
  <tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="${browsePotato.descriptiveContent.pageTitle}" metaDescription="${browsePotato.descriptiveContent.metaDescription}"></fd:SEOMetaTag>
	</tmpl:put>

  <c:if test="${browsePotato.descriptiveContent.wineDepartment == true}">
    <tmpl:put name='department'>WINE</tmpl:put>
  </c:if>

  <tmpl:put name='deptnav' direct='true'>
    <div class="browse-titlebar">
      <soy:render template="browse.titleBar" data="${browsePotato.descriptiveContent}" />
    </div>
  </tmpl:put>

  <tmpl:put name='leftnav' direct='true'>
    <div id="leftnav">
      <soy:render template="browse.menu" data="${browsePotato.menuBoxes}" />
    </div>
  </tmpl:put>

  <tmpl:put name='content' direct='true'>
    <div class="browse-breadcrumbs">
      <soy:render template="browse.breadCrumb" data="${browsePotato.breadCrumbs}" />
    </div>

	<div class="browse-oas-top">
	    <div class="oas-cnt" id="oas_b_CategoryNote"><script type="text/javascript">OAS_AD('CategoryNote');</script></div>
	    <div class="oas-cnt" id="oas_b_BrowseTop1"><script type="text/javascript">OAS_AD('BrowseTop1');</script></div>
	    <div class="oas-cnt left" id="oas_b_BrowseTop2"><script type="text/javascript">OAS_AD('BrowseTop2');</script></div>
	    <div class="oas-cnt right" id="oas_b_BrowseTop3"><script type="text/javascript">OAS_AD('BrowseTop3');</script></div>
   	</div>

    <div class="browse-media-top">
      <soy:render template="browse.topMedia" data="${browsePotato.descriptiveContent}" />
    </div>

    <div class="browse-carousels-top">
      <soy:render template="browse.topCarousels" data="${browsePotato.carousels}" />
    </div>

    <div class="pager-holder top">
      <soy:render template="browse.pager" data="${browsePotato.pager}" />
    </div>

    <div id="sorter">
      <soy:render template="browse.sortBar" data="${browsePotato.sortOptions}" />
    </div>

    <div class="browse-media-middle">
      <soy:render template="browse.middleMedia" data="${browsePotato.descriptiveContent}" />
    </div>
    
    <div class="pagetype-header">
      <soy:render template="srch.header" data="${browsePotato.searchParams}" />
    </div>
    
    <div class="browse-superdepartment">
      <soy:render template="browse.superDepartment" data="${browsePotato.sections}" />
    </div>

    <div class="browse-sections transactional">
      <soy:render template="browse.content" data="${browsePotato.sections}" />
    </div>
    
    <div class="oas-cnt left" id="oas_b_BrowseBottom1"><script type="text/javascript">OAS_AD('BrowseBottom1');</script></div>
    <div class="oas-cnt right" id="oas_b_BrowseBottom2"><script type="text/javascript">OAS_AD('BrowseBottom2');</script></div>

    <div class="browse-media-bottom">
      <soy:render template="browse.bottomMedia" data="${browsePotato.descriptiveContent}" />
    </div>

    <div class="browse-carousels-bottom">
      <soy:render template="browse.bottomCarousels" data="${browsePotato.carousels}" />
    </div>

    <div class="pager-holder bottom">
      <soy:render template="browse.pager" data="${browsePotato.pager}" />
    </div>
    
    <script>
      window.FreshDirect = window.FreshDirect || {};
      window.FreshDirect.browse = window.FreshDirect.browse || {};
      window.FreshDirect.globalnav = window.FreshDirect.globalnav || {};
      window.FreshDirect.activeDraft = window.FreshDirect.activeDraft || {};

      window.FreshDirect.browse.data = <fd:ToJSON object="${browsePotato}" noHeaders="true"/>
      window.FreshDirect.globalnav.data = <fd:ToJSON object="${globalnav}" noHeaders="true"/>
      window.FreshDirect.coremetricsData = window.FreshDirect.browse.data.coremetrics;
      window.FreshDirect.activeDraft = "${activeDraft}"
      window.FreshDirect.activeDraftDirectLink = "${activeDraftDirectLink}"
    </script>
    <script type="text/javascript">
	    $jq(document).ready(function() {
	    	if($jq("#oas_b_BrowseTop1").height()>0 || $jq("#oas_b_BrowseTop2").height()>0 || $jq("#oas_b_BrowseTop3").height()>0){
	    		if ($jq(".browse .browse-oas-top").css("margin-bottom")!= "14px"){
	    			$jq(".browse .browse-oas-top").css("display", "inline-block");
	    			$jq(".browse .browse-oas-top").css("width", "100%");
	    			$jq(".browse .browse-oas-top").css("margin-bottom", "14px");
	    		}
	    	}
	    });
    </script>
    
  </tmpl:put>

<% if (mobWeb) { %>
  <tmpl:put name="jsmodules">
    <%@ include file="/common/template/includes/i_jsmodules.jspf" %>
  </tmpl:put>
<% } %>

  <tmpl:put name='extraJsModules'>
    <jwr:script src="/browse.js"  useRandomParam="false" />
  </tmpl:put>
</tmpl:insert>