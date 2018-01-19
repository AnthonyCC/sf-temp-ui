<!DOCTYPE html>
<%@page import="com.freshdirect.webapp.ajax.quickshop.QuickShopHelper"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties'%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ page import="com.freshdirect.webapp.soy.SoyTemplateEngine"%>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib uri="/WEB-INF/shared/tld/components.tld" prefix='comp' %>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI" %>
<%@ taglib uri="fd-features" prefix="features" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<%  request.setAttribute("sitePage", "www.freshdirect.com/quickshop/");
	request.setAttribute("listPos", "SystemMessage,QSTop");

%>
<features:isActive name="isQS20" featureName="quickshop2_0" />

<html lang="en-US" xml:lang="en-US">
	<head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge" lang="en-US"/>

	<%-- 	<%@ include file="/common/template/includes/metatags.jspf" %> --%>
		<tmpl:get name="seoMetaTag"/>
        <%@ include file="/common/template/includes/i_javascripts.jspf" %>
	  	<jwr:style src="/grid.css" media="all" />
		<jwr:style src="/global.css" media="all" />
        <jwr:style src="/oldglobal.css" media="all" />
        <jwr:style src="/quickshop.css" media="all" />
        <tmpl:get name="extraJs"/>
        <%@ include file="/shared/template/includes/i_head_end.jspf" %>

        <script type="text/javascript">
        	function showStandardAds(){
        		$jq('#QSTop').show();
        	}

        </script>

    </head>
    <body data-feature-quickshop="${isQS20 ? "2_0" : "2_2"}"  >
    <%@ include file="/shared/template/includes/i_body_start.jspf" %>
    <%@ include file="/common/template/includes/globalnav.jspf" %>


    <div id="content">
        <div id="quickshop"  class="container text10 <tmpl:get name='containerClass' />">
          <!-- content lands here -->
            <c:choose>
              <c:when test="${isQS20}">
                <div class="header">
                <h1>Reorder</h1>
                <ul>
                    <li><a href="/quickshop/qs_past_orders.jsp">Past Orders</a></li>
                    <li><a href="/quickshop/qs_shop_from_list.jsp">Your Lists</a></li>
                    <li><a href="/quickshop/qs_fd_lists.jsp">Recommended Lists</a></li>
                    <% if (user.isEligibleForStandingOrders()) { %>
                        <li><a href="/quickshop/qs_standing_orders.jsp">Recurring Orders</a></li></li>
                    <%} %>
                </ul>
                </div>
               <!-- <div  class="oas-cnt" id="oas_QSTop"><script type="text/javascript">OAS_AD('QSTop');</script></div>-->
              </c:when>
              <c:otherwise>
                <div class="header">
                  <h1 class='qs-title icon-reorder-icon-before notext'>Reorder</h1><span class="qs-subtitle"><strong>Smart shopping</strong> from <strong>past orders &amp; lists</strong></span>
                </div>
              </c:otherwise>
            </c:choose>

             <!--  Standing orders Ad - AAPDEV-4294-->

             <div class="oas-cnt" id="oas_QSTop"><script type="text/javascript">OAS_AD('QSTop');</script></div>

            <ul class="tabs qs-tabs clearfix" role="tablist">
                <c:if test="${!isQS20}">
                <li><a id="tabTopItems" role="tab" tabindex="0" aria-controls="reorder-content" href="/quickshop/qs_top_items.jsp" class="<tmpl:get name='tiSelected'/>"><strong>Your Top Items</strong> <span class="count" data-component="tabMeta" data-tabMeta="topitems"></span></a></li>
                <li><a id="tabPastOrders" role="tab" tabindex="0" aria-controls="reorder-content" href="/quickshop/qs_past_orders.jsp" class="<tmpl:get name='poSelected'/>" ><strong>Your Past Orders</strong> <span class="count" data-component="tabMeta" data-tabMeta="pastorders"></span></a></li>
                <li><a id="tabShopFromList" role="tab" tabindex="0" aria-controls="reorder-content" href="/quickshop/qs_shop_from_list.jsp" class="<tmpl:get name='listSelected'/>"><strong>Your Shopping Lists</strong> <span class="count" data-component="tabMeta" data-tabMeta="lists"></span></a></li>
                </c:if>
                <c:if test="${isQS20}">
                <li><a href="/quickshop/qs_past_orders.jsp" class="<tmpl:get name='poSelected'/>"><strong>shop from past orders</strong> <span class="count" data-component="tabMeta" data-tabMeta="pastorders"></span></a></li>
                <li><a href="/quickshop/qs_shop_from_list.jsp" class="<tmpl:get name='listSelected'/>"><strong>shop from your lists</strong> <span class="count" data-component="tabMeta" data-tabMeta="lists"></span></a></li>
                <li><a href="/quickshop/qs_fd_lists.jsp" class="<tmpl:get name='fdSelected'/>"><strong>shop<span class="fd"> FD </span>lists</strong> <span class="count" data-component="tabMeta" data-tabMeta="fd_lists"></span></a></li>
                </c:if>
                <% if (user.isEligibleForStandingOrders()) { %>
                <li><a id="tabStandingOrders" role="tab" tabindex="0" aria-controls="reorder-content" href="/quickshop/qs_standing_orders.jsp" class="<tmpl:get name='soSelected'/>" ><strong>standing orders</strong> <span class="count" data-component="tabMeta" data-tabMeta="so"></span></a></li>
                <%} %>
            </ul>
            <div class="qs-loader">
                <div class="qs-header clearfix">
                    <tmpl:get name="searchbox" />
                    <c:choose>
                      <c:when test="${isQS20}">
                        <tmpl:get name="pagination" />
                      </c:when>
                      <c:otherwise>
                        <tmpl:get name="gridlistchange" />
                      </c:otherwise>
                    </c:choose>
                </div>
                <div class="qs-container">
                    <div class="qs-menu">
                        <tmpl:get name="menu" />
                    </div>
                    <div id="reorder-content" class="qs-content" role="tabpanel" tabindex="0">
                        <tmpl:get name="ymal" />
                        <c:if test="${!isQS20}">
                          <tmpl:get name="listheader" />
                          <tmpl:get name="listactions" />
                          <tmpl:get name="pagination" />
                        </c:if>
                        <tmpl:get name="sort" />
                        <c:if test="${isQS20}">
                        <div class="clearfix">
                            <tmpl:get name="listheader" />
                            <tmpl:get name="listactions" />
                        </div>
                        </c:if>
                        <ul id="breadcrumbs"></ul>
                        <tmpl:get name="content" />
                        <tmpl:get name="listactions" />
                        <c:if test="${!isQS20}">
                          <tmpl:get name="pagination" />
                        </c:if>
                    </div>
                    <div class="qs-footer">
                    <c:if test="${isQS20}">
                      <tmpl:get name="pagination" />
                    </c:if>
                    </div>
                </div>
                <div class="qs-loading"><div class="spinner-container"></div></div>
            </div>
      <c:choose>
        <c:when test="${isQS20}">
          <%
              Map<String,Object> soyData = new HashMap<String,Object>();
              soyData.put("hasYourFavoritesRecommendation", QuickShopHelper.hasYourFavoritesRecommendation( user ));
              soyData.put("showCustRated", FDStoreProperties.isBazaarvoiceEnabled());
          %>
          <soy:render template="common.tabbedCarousel" data="<%= soyData %>" />
        </c:when>
        <c:otherwise>
        	<div id="qs-carousel">
        	</div>
          <script>
          	$jq.ajax('/carousel/carousel.jsp').then(function(page) {
          		$jq('#qs-carousel').html(page);
          		if (fd.common.tabbedRecommender) {
	          		var firstTab = $jq('#qs-carousel [data-component="tabbedRecommender"] [data-tabname]:first-child');
					fd.common.tabbedRecommender.selectTab($('#qs-carousel [data-component="tabbedRecommender"]'),firstTab.data('tabname'),firstTab);
				}
          	});
          </script>
        </c:otherwise>
      </c:choose>

          <!-- content ends above here-->
        </div>
    </div>
    <div id="ModifyBRDContainer"></div>
    <%@ include file="/common/template/includes/footer.jspf" %>
    <tmpl:get name="soytemplates" />
    <tmpl:get name="jsmodules" />
    </body>
</html>
