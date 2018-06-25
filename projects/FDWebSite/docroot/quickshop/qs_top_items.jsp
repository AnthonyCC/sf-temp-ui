<%@page import="com.freshdirect.webapp.ajax.quickshop.QuickShopRedirector"%>
<%@ page import='com.freshdirect.fdstore.rollout.EnumRolloutFeature'%>
<%@ page import='com.freshdirect.fdstore.rollout.FeatureRolloutArbiter'%>
<%@ page import='com.freshdirect.webapp.util.JspMethods' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<fd:CheckLoginStatus id="user" guestAllowed='false' recognizedAllowed='false' />

<%  //--------OAS Page Variables-----------------------
        //request.setAttribute("sitePage", "www.freshdirect.com/quickshop");
        //request.setAttribute("listPos", "QSBottom,SystemMessage,LittleRandy,QSTopRight");
%>
<%
	/* the page should set this, not the template (qs_template.jsp overrides these) */
	request.setAttribute("sitePage", "www.freshdirect.com/quickshop");
	request.setAttribute("listPos", "SystemMessage,QSTop");   
%>
<%
	boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
	String pageTemplate = "/quickshop/includes/qs_template.jsp";
	if (mobWeb) {
		pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
		String oasSitePage = request.getAttribute("sitePage").toString();
		if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
			request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS	
		}
	}
%>
<tmpl:insert template="<%= pageTemplate %>">
    <tmpl:put name="soytemplates"><soy:import packageName="quickshop"/></tmpl:put>
    <tmpl:put name="jsmodules"><%@ include file="/common/template/includes/i_jsmodules.jspf" %><jwr:script src="/qstopitems.js" useRandomParam="false" /></tmpl:put>
<%--     <tmpl:put name='title' direct='true'>FreshDirect - Top Items</tmpl:put> --%>
    <tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag title="FreshDirect - Top Items" pageId="qs_top_items"></fd:SEOMetaTag>
	</tmpl:put>

    <tmpl:put name='tiSelected'>selected</tmpl:put>
    <tmpl:put name='containerClass'>qs-topitems</tmpl:put>

    <tmpl:put name='searchbox'>
        <form action="#" class="qs-search" id="qs_search">
            <input type="text" aria-label="Search Your Top Items" placeholder="Search Your Top Items" id="searchTerm" />
            <span class="reset-search"></span>
            <button class="cssbutton purple icon-magnifying-glass-before notext searchbutton">search</button>
        </form>
    </tmpl:put>

    <tmpl:put name="gridlistchange">
      <div class="gridlistchange">
        <button class="cssbutton purple icon-grid-view-before notext" data-component="GridListButton" data-type="grid">grid<span class="offscreen">view of products</span></button>
        <button class="cssbutton purple icon-list-view-before notext" data-component="GridListButton" data-type="list">list<span class="offscreen">view of products</span></button>
      </div>
    </tmpl:put>

    <tmpl:put name='pagination'>
        <div class="pagination"></div>
    </tmpl:put>

    <tmpl:put name='menu' direct='true'>
        <div id="departments" class="qs-menu-margin">
          <div class="departments rounded-box">
            <div class="departments-header qs-menu-header" data-listsize="">Departments <span class="counter"></span></div>
            <fieldset><legend class="offscreen">filter products by departments</legend><ul class="checkboxlist departments-list"></ul></fieldset>
          </div>
        </div>
        <div id="preferences" class="qs-menu-margin"></div>
    </tmpl:put>

    <tmpl:put name='sort'>
        <div id="sortBar" class="qs-sortbar"></div>
    </tmpl:put>

    <tmpl:put name='listheader'>
        <div id="listheader" class="qs-list-header">
		<p>Viewing:</p>
		<h2>Your Top Items</h2>
	</div>
    </tmpl:put>

    <tmpl:put name='content' direct='true'>
    	<% if (mobWeb) { /* edit the cookie, which resets the view, there's prob a better way */ %>
    		<script>
    			FreshDirect.modules.common.utils.createCookie('viewtype', 'grid');
    		</script>
    	<% } %>
    	<div id="productlist" class="" data-cmeventsource="qs_topItems"></div>
    	<div id="productlistHeader" class=""></div>
    </tmpl:put>
</tmpl:insert>
