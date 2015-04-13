<%@page import="com.freshdirect.webapp.ajax.quickshop.QuickShopRedirector"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-features" prefix="features" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<fd:CheckLoginStatus id="user" guestAllowed='false' recognizedAllowed='false' />

<%-- redirect back to old quickshop page if not allowed to see the new (partial rollout check) --%>
<fd:QuickShopRedirector user="<%=user%>" from="<%=QuickShopRedirector.FROM.NEW_PAST_ORDERS %>"/>
<features:isActive name="isQS20" featureName="quickshop2_0" />

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
            <c:choose>
              <c:when test="${isQS20}">
                <input type="text" placeholder="Search all orders - past year" id="searchTerm" />
                <input type="submit" value="search" />
              </c:when>
              <c:otherwise>
                <input type="text" placeholder="Search Your Past Orders" id="searchTerm" />
                <span class="reset-search"></span>
                <button class="cssbutton purple searchbutton icon-magnifying-glass-before notext">search</button>
              </c:otherwise>
            </c:choose>
        </form>
    </tmpl:put>

    <tmpl:put name="gridlistchange">
      <c:if test="${!isQS20}">
        <div class="gridlistchange">
          <button class="cssbutton purple icon-grid-view-before notext" data-component="GridListButton" data-type="grid">grid</button>
          <button class="cssbutton purple icon-list-view-before notext" data-component="GridListButton" data-type="list">list</button>
        </div>
      </c:if>
    </tmpl:put>

    <tmpl:put name='pagination'>
        <div class="pagination"></div>
    </tmpl:put>

    <tmpl:put name='menu' direct='true'>
      <c:if test="${isQS20}">
        <button id="reset">reset</button>
      </c:if>
      <div id="timeframes" class=""></div>
      <div id="orders" class="open ${isQS20 ? "" : "qs-menu-margin"}">
        <div class="orders ${isQS20 ? "" : "rounded-box"}">
          <div class="orders-header qs-menu-header" data-listsize="">${isQS20 ? "Previous Orders" : "Past Orders"} <span class="counter"></span></div>
          <ul class="checkboxlist orders-list"></ul>
          <c:if test="${isQS20}">
            <button class="qs-menu-toggle">show</button>
          </c:if>
        </div>
      </div>
      <div id="departments" class="${isQS20 ? "" : "qs-menu-margin"}">
        <div class="departments ${isQS20 ? "" : "rounded-box"}">
          <div class="departments-header qs-menu-header" data-listsize="">Departments <span class="counter"></span></div>
          <ul class="checkboxlist departments-list"></ul>
        </div>
      </div>
      <div id="preferences" class="${isQS20 ? "" : "qs-menu-margin"}"></div>
    </tmpl:put>

    <tmpl:put name='ymal'>
      <c:if test="${isQS20}">
        <div id="qs-ymal" data-cmeventsource="qs_ymal"></div>
      </c:if>
    </tmpl:put>

    <tmpl:put name='listactions'>
        <div class="qs-actions">
          <button class="qs-addtolist ${isQS20 ? "" : "cssbutton purple icon-list-black-before"}" data-component="ATLButton" data-ref="#productlist" data-alignpopup="tc-bc">add all to list</button>
          <button class="qs-addtocart ${isQS20 ? "" : "cssbutton orange icon-cart-new-after"}" ${isQS20 ? 'data-component="ATCButton" data-ref="#productlist"' : 'data-alignpopup="tr-br" data-confirm="true" data-confirm-template="common.addalltocartconfirmpopup" data-confirm-data=\'{"container": "#productlist"}\' data-confirm-header="Add all items to cart?" data-confirm-process="FreshDirect.components.AddToCart.allConfirmProcess"'}>add all items on page</button>
        </div>
    </tmpl:put>

    <tmpl:put name='sort'>
        <div id="sortBar" class="qs-sortbar"></div>
    </tmpl:put>

    <tmpl:put name='listheader'>
      <c:choose>
        <c:when test="${isQS20}">
          <div id="listheader" class="qs-list-header"></div>
        </c:when>
        <c:otherwise>
          <div id="listheader" class="qs-list-header">
            <p>Viewing all items from:</p>
            <h2>Your Past Orders</h2>
          </div>
        </c:otherwise>
      </c:choose>
    </tmpl:put>

    <tmpl:put name='content' direct='true'><div id="productlist" class="" data-cmeventsource="qs_pastOrders"></div><div id="productlistHeader" class=""></div></tmpl:put>
</tmpl:insert>
