<%@ page import='com.freshdirect.framework.util.NVL'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='java.text.*' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus id="user" guestAllowed="false" recognizedAllowed="true" />
<%
	String setShowOverlayTo = NVL.apply(request.getParameter("setShowOverlayTo"), "");
	FDSessionUser sessionUser = (FDSessionUser)session.getAttribute(SessionName.USER);
	String userId = NVL.apply(sessionUser.getUser().getUserId(), "<i>anonymous</i>");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title> Pending Order Merge Project Test Page </title>
		<style>
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
Pending Order Count: <%= pendingOrderCount %><br />

	<% if (sessionUser != null) { %>
		Current Session UserId: <%= userId %><br />
		<%
			if (!"".equals(setShowOverlayTo) && ("true".equalsIgnoreCase(setShowOverlayTo) || "false".equalsIgnoreCase(setShowOverlayTo))) {
				sessionUser.setShowPendingOrderOverlay(Boolean.parseBoolean(setShowOverlayTo));
				%>Show Pending Order Overlay <b>Set As</b>: <%=setShowOverlayTo%><br /><%
			}else{
				%>Pending Order Overlay: <%=sessionUser.isShowPendingOrderOverlay()%><br /><%
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
</body>
</html>