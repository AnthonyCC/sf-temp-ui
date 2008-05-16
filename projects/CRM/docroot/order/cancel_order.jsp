<%@ page import='java.text.*, java.util.*' %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/top_nav.jsp'>
<%
    // Get the OrderModel using the orderId from the request
    //
    String orderId = (String) session.getAttribute(SessionName.RECENT_ORDER_NUMBER);
    FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
%>
<fd:GetOrder id='order' saleId='<%= orderId %>'>
<fd:ModifyOrderController action="cancel" orderId="<%= orderId %>" result="result" successPage='<%= "/main/order_details.jsp?orderId=" + orderId %>'>

<tmpl:put name='title' direct='true'>Order <%= orderId%> Cancel</tmpl:put>

<tmpl:put name='content' direct='true'>
<%@ include file="/includes/order_nav.jspf"%>
<%@ include file="/includes/order_summary.jspf"%>

<div class="sub_nav">
<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center">
<form name="cancel_order" method="POST">
<script language="javascript">
	function confirmCancelOrder(thisForm) {
		var doCancel = confirm ("Are you sure you want to cancel this order?");
		if (doCancel == true) {
			thisForm.submit();
		}
	}
</script>
	<tr>
		<td class="sub_nav_title" width="33%">Cancel Order, Reason & Notes</td>
		<td align="right"><input type="submit" class="submit" onClick="javascript:confirmCancelOrder(cancel_order);return false;" value="CANCEL THIS ORDER"></td>
		<td><a href="/main/order_details.jsp?orderId=<%= orderId %>" class="cancel">FORGET IT</a></td>
		<td width="33%"></td>
	</tr>
</table>
</div>
<div class="content_fixed">
<table width="100%" cellpadding="2" cellspacing="0" border="0" align="center">
	<tr valign="top">
		<td width="2%">&nbsp;</td>
		<td width="98%">
		<fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'><span class="error"><%=errorMsg%></span><br></fd:ErrorHandler>
		<fd:ErrorHandler result='<%=result%>' name='order_status' id='errorMsg'><span class="error"><%=errorMsg%></span><br></fd:ErrorHandler>
		<fd:ErrorHandler result='<%=result%>' name='no_customer' id='errorMsg'><span class="error"><%=errorMsg%></span><br></fd:ErrorHandler>
		
		<input type="checkbox" name="silent_mode"> Do not send email confirmation
		<br>
       
		<br>
<%	String [] reasons = {"Product quality not acceptable",
						 "Poor service experience",
						 "Delivery window missed",
						 "Delivery date/time not convenient",
						 "Customer will not be home/not able to select alt. recipient",
						 "Order placed in error",
						 "Duplicate order",
						 "Wants to take advantage of web offer ($50 or $25)",
						 "No reason cited/given"
						 };
%>
		<SELECT name="cancel_reason">
			<OPTION VALUE="">Select reason</OPTION>
            <%	for (int i = 0; i < reasons.length; i++) { %>
                <OPTION <%= reasons[i].equals( request.getParameter("cancel_reason") ) ? "SELECTED" : "" %>><%= reasons[i] %></OPTION>
            <%	} %>
		</SELECT>
		<br>
		<br>
		Notes:<br>
		<TEXTAREA NAME="cancel_notes" WRAP="virtual" COLS="50" ROWS="10"><%= request.getParameter("cancel_notes") %></TEXTAREA>
		<br><br>

		</td>
	</tr>
	</form>
</table>
</div>
<%CrmSession.invalidateCachedOrder(session);%>
</tmpl:put>
</fd:ModifyOrderController>
</fd:GetOrder>
</tmpl:insert>
