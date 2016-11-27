<%@page import="com.freshdirect.webapp.ajax.quickshop.QuickShopRedirector"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="fd-features" prefix="features" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<fd:CheckLoginStatus id="user" guestAllowed='false' recognizedAllowed='false' />
<features:isActive name="isQS20" featureName="quickshop2_0" />

<%-- redirect back to old quickshop page if not allowed to see the new (partial rollout check) --%>
<fd:QuickShopRedirector user="<%=user%>" from="<%=QuickShopRedirector.FROM.NEW_LISTS %>"/>

<%  //--------OAS Page Variables-----------------------
        request.setAttribute("sitePage", "www.freshdirect.com/quickshop");
        request.setAttribute("listPos", "QSBottom,SystemMessage,LittleRandy,QSTopRight");
%>
<tmpl:insert template='/quickshop/includes/qs_template.jsp'>
    <tmpl:put name="soytemplates"><soy:import packageName="quickshop"/></tmpl:put>
    <tmpl:put name="jsmodules"><%@ include file="/common/template/includes/i_jsmodules.jspf" %><jwr:script src="/qsshopfromlist.js" useRandomParam="false" /></tmpl:put>
    <tmpl:put name='title' direct='true'>FreshDirect - Reorder from Your Lists</tmpl:put>
    <tmpl:put name="seoMetaTag" direct="true">
		<fd:SEOMetaTag pageId="shop_list"></fd:SEOMetaTag>
	</tmpl:put>

    <tmpl:put name='listSelected'>selected</tmpl:put>
    <tmpl:put name='containerClass'>qs-lists</tmpl:put>

    <tmpl:put name='searchbox'>
      <c:if test="${!isQS20}">
      <%-- HIDDEN temporary - APPDEV-4051 Should be reenabled during the implementation of APPDEV-4048 --%>
      <form style="display: none;" action="#" class="qs-search" id="qs_search">
        <input type="text" placeholder="Search Your Shopping Lists" id="searchTerm" />
        <span class="reset-search"></span>
        <button class="cssbutton purple searchbutton icon-magnifying-glass-before notext">search</button>
      </form>
      </c:if>
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
      <div id="shoppinglists" class=""></div>
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
          <c:if test="${!isQS20}">
          <button class="qs-deletelist cssbutton transparent white icon-trash-new-before" data-alignpopup="tc-bc" data-confirm="true" data-confirm-header="Would you also like to delete this list?" 
            data-confirm-data="{&quot;buttons&quot;: [{&quot;id&quot;: &quot;delete&quot;, &quot;name&quot;: &quot;Delete List&quot;, &quot;class&quot;: &quot;cssbutton red&quot;},{&quot;id&quot;: &quot;keep&quot;, &quot;name&quot;: &quot;Keep List&quot;, &quot;class&quot;: &quot;cssbutton green&quot;}]}" data-confirm-button-delete="FreshDirect.quickshop.shopFromList.manageshoppinglists.deleteCurrentList" data-confirm-button-keep="FreshDirect.quickshop.shopFromList.manageshoppinglists.emptyCurrentList">Remove All Items</button>
          </c:if>
          <c:if test="${isQS20}"><button class="qs-addtolist" data-component="ATLButton" data-ref="#productlist">add all to list</button></c:if>
          <button class="qs-addtocart ${isQS20 ? "" : "cssbutton orange icon-cart-new-after"}" ${isQS20 ? 'data-component="ATCButton" data-ref="#productlist"' : 'data-alignpopup="tr-br" data-confirm="true" data-confirm-template="common.addalltocartconfirmpopup" data-confirm-data=\'{"container": "#productlist"}\' data-confirm-header="Add all items to cart?" data-confirm-process="FreshDirect.components.AddToCart.allConfirmProcess"'}>add all items on page</button>
        </div>
    </tmpl:put>

    <tmpl:put name='sort'>
        <div id="sortBar" class="qs-sortbar"></div>
    </tmpl:put>

    <tmpl:put name='listheader'>
        <div class="qs-list-header">
            <div id="listheader"></div>
            <div id="recipeheader"></div>
        </div>
    </tmpl:put>

    <tmpl:put name='content' direct='true'><div id="productlist" class="" data-cmeventsource="qs_customerLists"></div><div id="productlistHeader" class=""></div></tmpl:put>
</tmpl:insert>
