<%@ page import='com.freshdirect.framework.util.NVL'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.content.*' %>
<%@ page import='java.util.*' %>
<%@ page import='java.text.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic'%>
<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<fd:CheckLoginStatus id="user" guestAllowed="true" recognizedAllowed="true" />
<%
	String setShowOverlayTo = NVL.apply(request.getParameter("setShowOverlayTo"), "");
	String clearUserCart = NVL.apply(request.getParameter("clearUserCart"), "false");
	
	FDSessionUser sessionUser = (FDSessionUser)session.getAttribute(SessionName.USER);
	String userId = NVL.apply(sessionUser.getUser().getUserId(), "<i>anonymous</i>");
	
	if ("true".equalsIgnoreCase(clearUserCart)) {
		user.getShoppingCart().clearOrderLines();
		user.saveCart();
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US" xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title> Pending Order Merge Project Test Page </title>
		<style>
			body {
				font-family: Verdana;
				font-size: 13px;
			}
			.pageTitle {
				font-weight: bold;
				font-size: 14px;
			}
			.title {
				font-weight: bold;
			}
			.empty {
				font-weight: bold;
				font-style: italic;
				color: #ccc;
			}
			.smallText {
				font-size: 11px;
			}
			hr {
				border: 1px solid #ccc;
			}
			.pageTitleHR {
				border: 1px solid #333;
			}
		</style>
	</head>
<body>
	<% 
		int pendingOrderCount = 0;
		List validPendingOrders = new ArrayList();

		if (user.getLevel() >= FDUserI.RECOGNIZED) { 
		validPendingOrders.addAll(user.getPendingOrders());
		//set count (in case this variable is needed elsewhere (and we'll just use it now as well)
		pendingOrderCount = validPendingOrders.size();
	} %>
	
<div style="float: right; font-size: 22px;"><a href="./mergePendOrder.jsp">RELOAD PAGE</a></div>	
Pending Order Count: <%= pendingOrderCount %><br />

	<% if (sessionUser != null) { %>
		Current Session UserId: <%= userId %><br />
		<%
			if (!"".equals(setShowOverlayTo) && ("true".equalsIgnoreCase(setShowOverlayTo) || "false".equalsIgnoreCase(setShowOverlayTo))) {
				sessionUser.setShowPendingOrderOverlay(Boolean.parseBoolean(setShowOverlayTo));
				%>Show Pending Order Overlay <b>Set As</b>: <%=setShowOverlayTo%><br /><%
			}else{
				%>Pending Order Overlay: <%=sessionUser.getShowPendingOrderOverlay()%><br /><%
			}
		%>
	<% } %>
	Reset To: <a href="./mergePendOrder.jsp?setShowOverlayTo=true">true</a> <a href="./mergePendOrder.jsp?setShowOverlayTo=false">false</a><br />
	<hr />
	Pending Orders:<br />
	<%
		//check for any valid orders
		if (pendingOrderCount > 0) {
			
			//multiple orders, dropdown
			for (Iterator hIter = validPendingOrders.iterator(); hIter.hasNext(); ) {
				FDOrderInfoI orderInfo = (FDOrderInfoI) hIter.next();
				%><%= orderInfo.getErpSalesId() %> - <%= new SimpleDateFormat("EEEE, MM/dd/yyyy").format(orderInfo.getRequestedDate()) %><br /><%
			} %>
		<%
		} else {
			//we have no pending orders at all
			%>
			No pending orders!
			<%
		}
	%>
	<hr />
	<hr />
	<style type="text/css">
		th { background-color: #ccc; }
	</style>
	<% FDCartModel tempCart = new FDCartModel(); %>
	Merge Pending Cart:<br />
	<% tempCart = user.getMergePendCart(); %>
	<div style="overflow: hidden; width: 650px;">
		<table style="width: 600px; margin-left: 20px; font-size: 12px; font-family: Verdana; border-bottom: none;" border="1" cellpadding="0" cellspacing="0">
			<tr>
				<th width="150" style="border-bottom: none;">[desc_]cartLineId</th>
				<th width="50" style="border-bottom: none;">img</th>
				<th style="border-bottom: none;">prodDescrip</th>
			</tr>
		</table>
		<div style="<%= (tempCart.getOrderLines().size() > 5) ? "height: 260px; overflow-y: scroll;" : "" %>">
			<table style="width: 600px; margin-left: 20px; font-size: 12px; font-family: Verdana;" border="1" cellpadding="0" cellspacing="0">
				
				<logic:iterate id="cartLine" collection="<%= tempCart.getOrderLines() %>" type="com.freshdirect.fdstore.customer.FDCartLineI" indexId="idx">
					<tr>
						<td width="150">addCLID_<%= cartLine.getCartlineId() %></td>
						<%
							ProductModel pm = cartLine.lookupProduct();
						
							if (pm != null && pm.getCategoryImage() != null) {
								Image catImage = pm.getCategoryImage();
								%><td style="width: 50px; height: 50px; vertical-align: middle;"><img border="0" src="<%= catImage.getPath() %>" width="<%= catImage.getFittedWidth(45, 45) %>" height="<%= catImage.getFittedHeight(45, 45) %>" alt="" style="margin-top: 5px;" /></td><%
							}else{
								%><td style="width: 50px; height: 50px; vertical-align: middle;">[NOIMG]</td><%
							}
						%>
						<td style="width: 400px; vertical-align: middle;"><div style="margin-left: 8px; text-indent: -8px; font-weight: bold;"><display:ProductName product="<%= pm %>" disabled="true" /></div></td>
					</tr>
				</logic:iterate>
				<% if (tempCart.getOrderLines().size() == 0) { %>
					<tr><td colspan="3" align="center">Cart is empty.</td></tr>
				<% } %>
			</table>
		</div>
	</div>
	<hr />
	<% tempCart = user.getShoppingCart(); %>
	User Cart<%= (tempCart instanceof FDModifyCartModel) ? " (Modify)" : "" %>: (<a href="./mergePendOrder.jsp?clearUserCart=true">Clear Cart</a>)<br />
	<div style="overflow: hidden; width: 650px;">
		<table style="width: 600px; margin-left: 20px; font-size: 12px; font-family: Verdana; border-bottom: none;" border="1" cellpadding="0" cellspacing="0">
			<tr>
				<th width="150" style="border-bottom: none;">[desc_]cartLineId</th>
				<th width="50" style="border-bottom: none;">img</th>
				<th style="border-bottom: none;">prodDescrip</th>
			</tr>
		</table>
		<div style="<%= (tempCart.getOrderLines().size() > 5) ? "height: 260px; overflow-y: scroll;" : "" %>">
			<table style="width: 600px; margin-left: 20px; font-size: 12px; font-family: Verdana;" border="1" cellpadding="0" cellspacing="0">
				<logic:iterate id="cartLine" collection="<%= tempCart.getOrderLines() %>" type="com.freshdirect.fdstore.customer.FDCartLineI" indexId="idx">
					<tr>
						<td width="150">addCLID_<%= cartLine.getCartlineId() %></td>
						<%
							ProductModel pm = cartLine.lookupProduct();
						
							if (pm != null && pm.getCategoryImage() != null) {
								Image catImage = pm.getCategoryImage();
								%><td style="width: 50px; height: 50px; vertical-align: middle;"><img border="0" src="<%= catImage.getPath() %>" width="<%= catImage.getFittedWidth(45, 45) %>" height="<%= catImage.getFittedHeight(45, 45) %>" alt="" style="margin-top: 5px;" /></td><%
							}else{
								%><td style="width: 50px; height: 50px; vertical-align: middle;">[NOIMG]</td><%
							}
						%>
						<td style="width: 400px; vertical-align: middle;"><div style="margin-left: 8px; text-indent: -8px; font-weight: bold;"><display:ProductName product="<%= pm %>" disabled="true" /></div></td>
					</tr>
				</logic:iterate>
				<% if (tempCart.getOrderLines().size() == 0) { %>
					<tr><td colspan="3" align="center">Cart is empty.</td></tr>
				<% } %>
			</table>
		</div>
	</div>
</body>
</html>