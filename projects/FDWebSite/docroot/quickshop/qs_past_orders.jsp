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
    <tmpl:put name="jsmodules"><%@ include file="/common/template/includes/i_jsmodules.jspf" %><jwr:script src="/qspastorders.js" useRandomParam="false" /></tmpl:put>
    <tmpl:put name='title' direct='true'>FreshDirect - Reorder from Past Orders</tmpl:put>

    <tmpl:put name='poSelected'>selected</tmpl:put>
    <tmpl:put name='containerClass'>qs-pastorders</tmpl:put>

    <tmpl:put name='searchbox'>
        <form action="#" class="qs-search" id="qs_search">
            <input type="text" placeholder="Search all orders - past year" id="searchTerm" />
            <input type="submit" value="search" />
        </form>
    </tmpl:put>

    <tmpl:put name='pagination'>
        <div class="pagination"></div>
    </tmpl:put>

    <tmpl:put name='menu' direct='true'>
        <button id="reset">reset</button>
        <div id="timeframes" class=""></div>
        <div id="orders" class="open">
			<div class="orders">
				<div class="orders-header qs-menu-header" data-listsize="">Previous orders <span class="counter"></span></div>
				<ul class="checkboxlist orders-list"></ul>
			    <button class="qs-menu-toggle">show</button>
			</div>
        </div>
        <div id="departments" class="">
			<div class="departments">
				<div class="departments-header qs-menu-header" data-listsize="">Departments <span class="counter"></span></div>
				<ul class="checkboxlist departments-list"></ul>
			</div>
        </div>
        <div id="preferences" class=""></div>
    </tmpl:put>

    <tmpl:put name='ymal'>
        <div id="qs-ymal" data-cmeventsource="qs_ymal"></div>
    </tmpl:put>

    <tmpl:put name='listactions'>
        <div class="qs-actions">
            <button class="qs-addtolist" data-component="ATLButton" data-ref="#productlist">add all to list</button>
            <button class="qs-addtocart" data-component="ATCButton" data-ref="#productlist">add all items on page</button>
        </div>
    </tmpl:put>

    <tmpl:put name='sort'>
        <div id="sortBar" class="qs-sortbar"></div>
    </tmpl:put>

    <tmpl:put name='listheader'>
        <div id="listheader" class="qs-list-header"></div>
    </tmpl:put>

    <tmpl:put name='content' direct='true'><div id="productlist" class="" data-cmeventsource="qs_pastOrders"></div><div id="productlistHeader" class=""></div></tmpl:put>
</tmpl:insert>
