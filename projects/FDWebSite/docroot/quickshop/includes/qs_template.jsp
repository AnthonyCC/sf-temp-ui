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
<%  request.setAttribute("sitePage", "www.freshdirect.com/quickshop/");
	request.setAttribute("listPos", "SystemMessage,QSTop");
%>
<html>
	<head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <title><tmpl:get name="title" /></title>
		<%@ include file="/common/template/includes/metatags.jspf" %>
        <%@ include file="/common/template/includes/i_javascripts.jspf" %>
	  	<jwr:style src="/grid.css"/>
		<jwr:style src="/global.css"/>
        <jwr:style src="/oldglobal.css"/>
        <jwr:style src="/quickshop.css"/>
        <tmpl:get name="extraJs"/>
        <%@ include file="/shared/template/includes/i_head_end.jspf" %>
    </head>
    <body>
    <%@ include file="/shared/template/includes/i_body_start.jspf" %>
    <%@ include file="/common/template/includes/globalnav.jspf" %>

    <div id="content">
        <div id="quickshop"  class="container text10 <tmpl:get name='containerClass' />">
          <!-- content lands here -->
            <div class="header">
                <h1>Quickshop</h1>
                <ul>
                    <li><a href="/quickshop/qs_past_orders.jsp">Past Orders</a></li>
                    <li><a href="/quickshop/qs_shop_from_list.jsp">Your Lists</a></li>
                    <li><a href="/quickshop/qs_fd_lists.jsp">Recommended Lists</a></li>
                    <% if (user.isEligibleForStandingOrders()) { %>
                        <li><a href="/quickshop/qs_standing_orders.jsp">Recurring Orders</a></li></li>
                    <%} %>
                </ul>
            </div>
            <div class="oas-cnt" id="QSTop"><script type="text/javascript">OAS_AD('QSTop');</script></div>
            <ul class="tabs qs-tabs clearfix">
                <li><a href="/quickshop/qs_past_orders.jsp" class="<tmpl:get name='poSelected'/>"><strong>shop from past orders</strong> <span class="count" data-component="tabMeta" data-tabMeta="pastorders"></span></a></li>
                <li><a href="/quickshop/qs_shop_from_list.jsp" class="<tmpl:get name='listSelected'/>"><strong>shop from your lists</strong> <span class="count" data-component="tabMeta" data-tabMeta="lists"></span></a></li>
                <li><a href="/quickshop/qs_fd_lists.jsp" class="<tmpl:get name='fdSelected'/>"><strong>shop<span class="fd"> FD </span>lists</strong> <span class="count" data-component="tabMeta" data-tabMeta="fd_lists"></span></a></li>
                <% if (user.isEligibleForStandingOrders()) { %>
                <li><a href="/quickshop/qs_standing_orders.jsp" class="<tmpl:get name='soSelected'/>"><strong>standing orders</strong> <span class="count" data-component="tabMeta" data-tabMeta="so"></span></a></li>
                <%} %>
            </ul>
            <div class="qs-loader">
                <div class="qs-header clearfix">
                    <tmpl:get name="searchbox" />
                    <tmpl:get name="pagination" />
                </div>
                <div class="qs-container">
                    <div class="qs-menu">
                        <tmpl:get name="menu" />
                    </div>
                    <div class="qs-content">
                        <tmpl:get name="ymal" />
                        <tmpl:get name="sort" />
                        <div class="clearfix">
                            <tmpl:get name="listheader" />
                            <tmpl:get name="listactions" />
                        </div>
                        <ul id="breadcrumbs"></ul>
                        <tmpl:get name="content" />
                        <tmpl:get name="listactions" />
                    </div>
                    <div class="qs-footer">
                        <tmpl:get name="pagination" />
                    </div>
                </div>
                <div class="qs-loading">loading products</div>
            </div>
            <%
                Map<String,Object> soyData = new HashMap<String,Object>();
                soyData.put("hasYourFavoritesRecommendation", QuickShopHelper.hasYourFavoritesRecommendation( user ));
            %>
            <soy:render template="common.tabbedCarousel" data="<%= soyData %>" />
          <!-- content ends above here-->
        </div>
    </div>
    <div id="ModifyBRDContainer"></div>
    <%@ include file="/common/template/includes/footer.jspf" %>
    <tmpl:get name="soytemplates" />
    <tmpl:get name="jsmodules" />
    </body>
</html>
