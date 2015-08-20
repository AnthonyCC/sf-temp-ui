<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/supervisor_resources.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Pending Returns</tmpl:put>

<tmpl:put name='content' direct='true'>
<%@ include file="/includes/i_globalcontext.jspf" %>

<fd:getOrdersByStatus id="orders" status="<%= EnumSaleStatus.RETURNED %>">

<div class="sub_nav">
<span class="sub_nav_title">Pending Returns ( <span class="result"><%= orders.size() %></span> )</span>
</div>

<div class="" style="height: 80%;">
	<div class="list_header" style="padding-top: 2px; padding-bottom: 2px;">
	<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" ALIGN="CENTER" class="list_header_text">
	    <tr>
			<td class="pendRet-spacer"></td>
			<td class="pendRet-ordId">Order #</td>
			<td class="pendRet-dlvDate">Delivery Date</td>
			<td class="pendRet-status">Status</td>
			<td class="pendRet-ordAmt">Amount</td>
			<td class="pendRet-custName">Customer Name</td>
			<td class="pendRet-store">Store</td>
			<td class="pendRet-facility">Facility</td>
			<td class="pendRet-email">Email/Username</td>
			<td class="pendRet-hPhone">Home Phone #</td>
			<td class="pendRet-aPhone">Alt. Phone #</td>
			<td class="pendRet-spacer"></td>
		</tr>
	</table>
	</div>
	<div class="list_content" style="height: 95%;">
<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" ALIGN="CENTER" class="">
<%	if (orders.size() > 0) { %>
	<%
		int displayedLines = 0;
	%>
	<logic:iterate id="info" collection="<%= orders %>" type="com.freshdirect.fdstore.customer.FDCustomerOrderInfo" indexId="counter">
    
		<% if ( 
					((globalContextStore).equalsIgnoreCase("All") || (globalContextStore).equals(info.geteStore())) &&
					((globalContextFacility).equalsIgnoreCase("All") || (globalContextFacility).equals(info.getFacility()))
		) { %>
			<TR VALIGN="top" <%= counter.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %> style="cursor: pointer;" onClick="document.location='<%= response.encodeURL("/main/order_details.jsp?orderId="+ info.getSaleId()) %>'">
				<td class="pendRet-spacer">&nbsp;</td>
				<TD class="pendRet-ordId"><a href="/main/order_details.jsp?orderId=<%=info.getSaleId()%>"><%=info.getSaleId()%></a></TD>
				<TD class="pendRet-dlvDate"><%= CCFormatter.formatDate(info.getDeliveryDate()) %></TD>
				<TD class="pendRet-status"><%= (info.getOrderStatus() != null) ? info.getOrderStatus().getName() : "--" %></TD>
				<TD class="pendRet-ordAmt"><%= JspMethods.formatPrice(info.getAmount()) %></TD>
				<TD class="pendRet-custName"><%= info.getLastName() %>, <%= info.getFirstName() %></TD>
				
				<td class="pendRet-store"><%= info.geteStore() %></td>
				<td class="pendRet-facility"><%= info.getFacility() %></td>
				
				<TD class="pendRet-email"><%= info.getEmail() %></TD>
				<TD class="pendRet-hPhone"><%= info.getPhone() %></TD>
				<TD class="pendRet-aPhone"><%= info.getAltPhone() %></TD>
				<td class="pendRet-spacer">&nbsp;</td>
			</TR>
			<tr><td colspan="12" class="list_separator" style="padding: 0px;"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
		<% displayedLines++;  %>
		<% } %>
	</logic:iterate>
	<% if (displayedLines == 0) { %>
		<tr><td colspan="12" align="center"><br>No pending returns need to be processed</td></tr>
	<% } %>
<% 	} else { %>
    <tr><td colspan="12" align="center"><br>No pending returns need to be processed</td></tr>
<%  }   %>
</TABLE>
	</div>
</div>
</fd:getOrdersByStatus>

</tmpl:put>

</tmpl:insert>
