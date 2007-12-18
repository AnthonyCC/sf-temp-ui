<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.fdstore.lists.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='java.text.*' %>
<%@ page import='com.freshdirect.webapp.util.RequestUtil'%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<script language="javascript" src="/assets/javascript/grocery_product.js"></script>
<script language="javascript">

function getWinYOffset(){
	if (window.scrollY) return window.scrollY; // Mozilla
 	if (window.pageYOffset) return window.pageYOffset; // Opera, NN4
 	if (document.documentElement && document.documentElement.scrollTop) return document.documentElement.scrollTop; // IE
 	if (document.body && document.body.scrollTop) return document.body.scrollTop;
	return 0;
}

function scrollPosition() {
	var hash = window.location.hash;
	var pos = hash.substring(5, hash.length);
	if (pos != "") {
		scrollTo(0, pos);
	}
}

window.onload = function() {
	scrollPosition();
}

</script>

<% request.setAttribute("needsCCL","true"); %>

<%
RequestUtil.appendToAttribute(request,"bodyOnLoad","FormChangeUtil.recordSignature('qs_cart',false)",";");
RequestUtil.appendToAttribute(request,"windowOnBeforeUnload","FormChangeUtil.warnOnSignatureChange('qs_cart')",";");
%>


<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>/ FD CRM : Quickshop > Every Item Ordered /</tmpl:put>

<% FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER); %>

<%!	DecimalFormat quantityFormatter = new DecimalFormat("0.##");
	SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
	NumberFormat currencyFormatter = java.text.NumberFormat.getCurrencyInstance( Locale.US );
%>

<tmpl:put name='content' direct='true'>
<jsp:include page='/includes/order_header.jsp'/>

<%	//
	// Get index of current search term
	//
	int searchIdx = 0;
	if (request.getParameter("searchIndex") != null) {
		searchIdx = Integer.parseInt( request.getParameter("searchIndex") );
	}
%>

<div class="content_scroll" style="float: left; width: 60%; height: 72%;">
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER" class="order">
	<TR VALIGN="TOP">
		<TD WIDTH="2%">&nbsp;</TD>
		<TD WIDTH="58%">
			<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="order">
				<TR>
				<TD WIDTH="70%">
					<a href="/order/quickshop/index.jsp">Shop From Previous Orders</a>
					&nbsp;&nbsp;|&nbsp;&nbsp;
					Shop from Every Item Ordered
					<fd:GetCustomerRecipeList id="recipes">
						&nbsp;&nbsp;|&nbsp;&nbsp;
						<a href="/order/quickshop/recipes.jsp">Recipes</a>
					</fd:GetCustomerRecipeList>
				</TD>
				<TD WIDTH="30%" ALIGN="RIGHT"> [ <A HREF="/order/place_order_batch_search_results.jsp?searchIndex=<%= searchIdx %>">back to list in progress</A> ]</TD>
				</TR>
			</TABLE>
			<hr class="gray1px">
			<div class="order_detail">We've remembered every item, just the way you like it. Use your previous purchases to re-order with a couple clicks. Just update the quantities (or delete items) and then add them to your cart.<br></div>
			<hr class="gray1px">

<% String actionName = request.getParameter("fdAction"); 
	if (actionName == null) {
		actionName = "";
	}
	String orderId = null; 
	String qsDeptId = null;
	boolean hasDeptId = false;
%>
	<fd:QuickShopController id="quickCart" orderId="every" action="<%= actionName %>">
	
	<%  String qsPage = "every_item.jsp"; %>
	<%@ include file="/shared/quickshop/includes/i_vieworder.jspf"%>
	</fd:QuickShopController>

		</TD>
	</TR>
</TABLE>
</div>

<div class="order_list" style="width: 40%; height: 72%;">
	<%@ include file="/includes/cart_header.jspf"%>
</div>

<br clear="all">

</tmpl:put>

<tmpl:put name='slot4' direct='true'>
</tmpl:put>
</tmpl:insert>
