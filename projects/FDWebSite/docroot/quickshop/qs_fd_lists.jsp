<%@page import="com.freshdirect.webapp.ajax.quickshop.QuickShopRedirector"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<fd:CheckLoginStatus id="user" guestAllowed='false' recognizedAllowed='false' />

<%-- redirect back to old quickshop page if not allowed to see the new (partial rollout check) --%>
<fd:QuickShopRedirector user="<%=user%>" from="<%=QuickShopRedirector.FROM.NEW_FD_LISTS %>"/>

<%  //--------OAS Page Variables-----------------------
        request.setAttribute("sitePage", "www.freshdirect.com/quickshop");
        request.setAttribute("listPos", "QSBottom,SystemMessage,LittleRandy,QSTopRight");
%>
<tmpl:insert template='/quickshop/includes/qs_template.jsp'>
    <tmpl:put name="soytemplates"><soy:import packageName="quickshop"/></tmpl:put>
    <tmpl:put name="jsmodules"><%@ include file="/common/template/includes/i_jsmodules.jspf" %>
    <jwr:script src="/qsfdlists.js" useRandomParam="false" /></tmpl:put>
<%--     <tmpl:put name='title' direct='true'>FreshDirect - Reorder from FD Lists</tmpl:put> --%>
      <tmpl:put name="seoMetaTag" direct="true">
    <fd:SEOMetaTag title="FreshDirect - Reorder from FD Lists"></fd:SEOMetaTag>
       </tmpl:put>

    <tmpl:put name='fdSelected'>selected</tmpl:put>
    <tmpl:put name='containerClass'>qs-fdlists</tmpl:put>

    <tmpl:put name='pagination'>
        <div class="pagination"></div>
    </tmpl:put>

    <tmpl:put name='menu' direct='true'>
        <div id="shoppinglists" class=""></div>
        <div id="departments" class="">
			<div class="departments">
				<div class="departments-header qs-menu-header" data-listsize="">DEPARTMENTS <span class="counter"></span></div>
				<fieldset><legend class="offscreen">filter products by departments</legend><ul class="checkboxlist departments-list"></ul></fieldset>
			</div>
        </div>
        <div id="preferences" class=""></div>
    </tmpl:put>

    <tmpl:put name='ymal'>
        <div id="qs-ymal" data-eventsource="qs_ymal"></div>
    </tmpl:put>

    <tmpl:put name='listactions'>
        <div class="qs-actions">
            <button class="qs-addtolist" aria-label="add all to list" data-component="ATLButton" data-ref="#productlist">add all to list</button>
            <button class="qs-addtocart" aria-label="add all items on page" data-component="ATCButton" data-ref="#productlist">add all items on page</button>
        </div>
    </tmpl:put>

    <tmpl:put name='sort'>
        <div id="sortBar" class="qs-sortbar"></div>
    </tmpl:put>

    <tmpl:put name='listheader'>
        <div id="listheader" class="qs-list-header"></div>
    </tmpl:put>

    <tmpl:put name='content' direct='true'><div id="productlist" class="" data-eventsource="qs_fdLists"></div><div id="productlistHeader" class=""></div></tmpl:put>
</tmpl:insert>
