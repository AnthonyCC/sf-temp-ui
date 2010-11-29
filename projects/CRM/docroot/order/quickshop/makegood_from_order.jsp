<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.fdstore.content.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='java.text.*' %>
<%@ page import='com.freshdirect.webapp.util.RequestUtil'%>
<%@ page import='com.freshdirect.fdstore.customer.adapter.FDOrderAdapter' %>


<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>


<% request.setAttribute("needsCCL","true"); %>

<%
RequestUtil.appendToAttribute(request,"bodyOnLoad","FormChangeUtil.recordSignature('qs_cart',false)",";");
RequestUtil.appendToAttribute(request,"windowOnBeforeUnload","FormChangeUtil.warnOnSignatureChange('qs_cart')",";");
%>


<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>/ FD CRM : Quickshop > Shop from Order /</tmpl:put>
<% FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER); %>

<%!	DecimalFormat quantityFormatter = new DecimalFormat("0.##");
	SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
%>

<tmpl:put name='content' direct='true'>

<%	//
	// Get index of current search term
	//
	int searchIdx = 0;
	if (request.getParameter("searchIndex") != null) {
		searchIdx = Integer.parseInt( request.getParameter("searchIndex") );
	}
%>

<div class="order_content">
<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" ALIGN="CENTER">
	<TR>
		<TD WIDTH="60%">

<%	String orderId = request.getParameter("orderId");
	FDCartModel order = new FDCartModel();
	// FDOrderI origOrder = null;
	FDOrderI currentOrder = FDCustomerManager.getOrder(user.getIdentity(), orderId);
%>

<fd:OrderHistoryInfo id='orderInfos'>

			<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="order_detail">
				<TR bgcolor="#ffffff">
					<td colspan="2" align="right">
					<% if ( (currentOrder.getCartonContents() != null) &&  (currentOrder.getCartonContents().size() > 0) ){ %>
					<a href="javascript:pop('/main/carton_contents_view.jsp?showForm=true&orderId=<%= orderId %>&scroll=yes','600','800');">Carton Contents</a>
					<% } %>
					</td>
				</TR>
			</TABLE>
<% String actionName = request.getParameter("fdAction");
	if (actionName == null) {
		actionName = "";
	}
%>
<fd:QuickShopController id="quickCart" orderId="<%= orderId %>" action="<%= actionName %>">
<%	String qsPage = "shop_from_order.jsp"; 
	String qsDeptId = null;
	boolean hasDeptId = false;
	request.setAttribute("crm_source","true");
	final boolean isStandingOrderPage = false;
%>
<%@ include file="/shared/quickshop/includes/i_vieworder.jspf" %>
</fd:QuickShopController>

</fd:OrderHistoryInfo>
			
		</TD>
	</TR>
</TABLE>
</div>

<div class="order_list" style="width: 40%; height: 72%;">
	<%@ include file="/includes/cart_header.jspf"%>
</div>

<br clear="all">

</tmpl:put>
</tmpl:insert>
