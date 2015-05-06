<%@page import="com.freshdirect.webapp.ajax.quickshop.QuickShopRedirector"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<fd:CheckLoginStatus id="user" guestAllowed='false' recognizedAllowed='false' />

<%-- redirect back to old quickshop page if not allowed to see the new (partial rollout check) --%>
<fd:QuickShopRedirector user="<%=user%>" from="<%=QuickShopRedirector.FROM.NEW_PAST_ORDERS %>"/>

<%  //--------OAS Page Variables-----------------------
        request.setAttribute("sitePage", "www.freshdirect.com/quickshop");
        request.setAttribute("listPos", "QSBottom,SystemMessage,LittleRandy,QSTopRight");
%>
<tmpl:insert template='/quickshop/includes/qs_template.jsp'>
    <tmpl:put name="soytemplates"><soy:import packageName="quickshop"/></tmpl:put>
    <tmpl:put name="jsmodules"><%@ include file="/common/template/includes/i_jsmodules.jspf" %><jwr:script src="/qstopitems.js" useRandomParam="false" /></tmpl:put>
    <tmpl:put name='title' direct='true'>FreshDirect - Top Items</tmpl:put>

    <tmpl:put name='tiSelected'>selected</tmpl:put>
    <tmpl:put name='containerClass'>qs-topitems</tmpl:put>

    <tmpl:put name='searchbox'>
        <form action="#" class="qs-search" id="qs_search">
            <input type="text" placeholder="Search Your Top Items" id="searchTerm" />
            <span class="reset-search"></span>
            <button class="cssbutton purple icon-magnifying-glass-before notext searchbutton">search</button>
        </form>
    </tmpl:put>

    <tmpl:put name="gridlistchange">
      <div class="gridlistchange">
        <button class="cssbutton purple icon-grid-view-before notext" data-component="GridListButton" data-type="grid">grid</button>
        <button class="cssbutton purple icon-list-view-before notext" data-component="GridListButton" data-type="list">list</button>
      </div>
    </tmpl:put>

    <tmpl:put name='pagination'>
        <div class="pagination"></div>
    </tmpl:put>

    <tmpl:put name='menu' direct='true'>
        <div id="departments" class="qs-menu-margin">
          <div class="departments rounded-box">
            <div class="departments-header qs-menu-header" data-listsize="">Departments <span class="counter"></span></div>
            <ul class="checkboxlist departments-list"></ul>
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

    <tmpl:put name='content' direct='true'><div id="productlist" class="" data-cmeventsource="qs_topItems"></div><div id="productlistHeader" class=""></div></tmpl:put>
</tmpl:insert>
