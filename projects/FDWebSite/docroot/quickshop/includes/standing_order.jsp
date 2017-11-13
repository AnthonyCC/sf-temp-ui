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
        <jwr:script src="/qsstandingorder.js" />
	  	<jwr:style src="/grid.css" media="all" />
		<jwr:style src="/global.css" media="all" />
        <jwr:style src="/oldglobal.css" media="all" />
        <jwr:style src="/quickshop.css" media="all" />
        <tmpl:get name="extraCss"/>
        <tmpl:get name="extraJs"/>
        <%@ include file="/shared/template/includes/i_head_end.jspf" %>

        <script type="text/javascript">
        	function showStandardAds(){
        		$jq('#as_QSTop').show();
        	}

        </script>

    </head>
    <body data-ec-page="<tmpl:get name="ecpage" />" data-feature-quickshop="${isQS20 ? "2_0" : "2_2"}"  >
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

             <div style="display:none"  class="oas-cnt" id="oas_QSTop" ><script type="text/javascript">OAS_AD('QSTop');</script></div>

            <ul class="tabs qs-tabs clearfix">
                <c:if test="${!isQS20}">
                <li><a href="/quickshop/qs_top_items.jsp" class="<tmpl:get name='tiSelected'/>" ><strong>Your Top Items</strong> <span class="count" data-component="tabMeta" data-tabMeta="topitems"></span></a></li>
                <li><a href="/quickshop/qs_past_orders.jsp" class="<tmpl:get name='poSelected'/>" ><strong>Your Past Orders</strong> <span class="count" data-component="tabMeta" data-tabMeta="pastorders"></span></a></li>
                <li><a href="/quickshop/qs_shop_from_list.jsp" class="<tmpl:get name='listSelected'/>"><strong>Your Shopping Lists</strong> <span class="count" data-component="tabMeta" data-tabMeta="lists"></span></a></li>
                </c:if>
                <c:if test="${isQS20}">
                <li><a href="/quickshop/qs_past_orders.jsp" class="<tmpl:get name='poSelected'/>"><strong>shop from past orders</strong> <span class="count" data-component="tabMeta" data-tabMeta="pastorders"></span></a></li>
                <li><a href="/quickshop/qs_shop_from_list.jsp" class="<tmpl:get name='listSelected'/>"><strong>shop from your lists</strong> <span class="count" data-component="tabMeta" data-tabMeta="lists"></span></a></li>
                <li><a href="/quickshop/qs_fd_lists.jsp" class="<tmpl:get name='fdSelected'/>"><strong>shop<span class="fd"> FD </span>lists</strong> <span class="count" data-component="tabMeta" data-tabMeta="fd_lists"></span></a></li>
                </c:if>
                <% if (user.isEligibleForStandingOrders()) { %>
                <li><a href="/quickshop/<%=(user.isNewSO3Enabled()) ? "standing_orders.jsp" : "qs_standing_orders.jsp"%>" class="<tmpl:get name='soSelected'/>" ><strong>standing orders</strong> <span class="count" data-component="tabMeta" data-tabMeta="so"></span></a></li>
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

            </div>

          <!-- content ends above here-->
        </div>
    </div>
    <tmpl:get name="content" />
    <div id="ModifyBRDContainer"></div>
    <%@ include file="/common/template/includes/footer.jspf" %>
    <tmpl:get name="soytemplates" />
    <tmpl:get name="jsmodules" />
    </body>
</html>
