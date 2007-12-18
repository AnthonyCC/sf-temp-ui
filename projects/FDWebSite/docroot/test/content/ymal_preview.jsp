<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import="com.freshdirect.common.pricing.*" %>
<%@ page import='java.util.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%!
    java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
%>

<fd:CheckLoginStatus />

<%
/*
 *  A page used for previewing ymals for specific products.
 *  This page acts the same is if the product has been added to the shopping
 *  cart, and the relevant YMALs would be placed on the bottom of that page.
 *
 *  HTTP request parameters:
 *
 *  catId     - the category id of the product.
 *  productId - the id of the product
 */

    String         categoryId  = request.getParameter("catId");
    String         productId   = request.getParameter("productId");
    ContentFactory cf          = ContentFactory.getInstance();
    ProductModel   productNode = cf.getProductByName(categoryId, productId);
    Recipe         recipe      = null;
    FDUserI        user        = (FDUserI) session.getAttribute(SessionName.USER);
	String         spacer      = "/media_stat/images/layout/clear.gif";

    // TODO: check if the product is a recipe, and then get the recipe object.

%>
<tmpl:insert template='/common/template/blank.jsp'>
    <tmpl:put name='content' direct='true'>
        <%@ include file="/includes/i_ymal.jspf"%>
    </tmpl:put>
    <tmpl:put name='title' direct='true'>YMAL preview for <%= productNode.getFullName() %></tmpl:put>
</tmpl:insert>
