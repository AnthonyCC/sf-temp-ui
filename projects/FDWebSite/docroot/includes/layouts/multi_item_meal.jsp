<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import='java.util.*'  %>
<%@ page import='java.net.URLEncoder'%>
<%@ page import='com.freshdirect.fdstore.content.*,com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.content.attributes.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import="com.freshdirect.framework.webapp.*"%>
<%@ page import='com.freshdirect.framework.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='oscache' prefix='oscache' %>
<fd:CheckLoginStatus />
<%!
	java.text.NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance(Locale.US);
	java.text.DecimalFormat quantityFormatter = new java.text.DecimalFormat("0.##");
%>
<%
FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
List brands=null;
String catId = request.getParameter("catId");
String productId = request.getParameter("productId");
// get the product that mathes the productId that was passed into the page
ProductModel productNode=(ProductModel)ContentFactory.getInstance().getProductByName(catId,productId);

String cartMode = CartName.ADD_TO_CART;
FDCartLineI templateLine = null ;
ActionResult result = (ActionResult)request.getAttribute("actionResult");
if ("POST".equals(request.getMethod()) && result == null) {
   throw new JspException("AHHHH!!!!!!  NO RESULT OBJECT!!!!!");
}
%>
<tmpl:insert template='/common/template/both_dnav.jsp'>
    <tmpl:put name='title' direct='true'>FreshDirect - <%= productNode.getFullName() %></tmpl:put>
    <tmpl:put name='leftnav' direct='true'>
    </tmpl:put>
<tmpl:put name='content' direct='true'>
<%@ include file="/shared/includes/product/i_product_quality_note.jspf" %>
<%@ include file="/includes/product/cutoff_notice.jspf" %>
<%@ include file="/includes/product/i_dayofweek_notice.jspf" %>
<%@ include file="/includes/product/multi_item_meal.jspf" %>
</tmpl:put>
</tmpl:insert>