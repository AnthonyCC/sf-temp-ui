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

<fd:getOrdersByStatus id="orders" status="<%= EnumSaleStatus.RETURNED %>">

<div class="sub_nav">
<span class="sub_nav_title">Pending Returns ( <span class="result"><%= orders.size() %></span> )</span>
</div>

<div class="content" style="height: 80%;">
	<div class="list_header" style="padding-top: 2px; padding-bottom: 2px;">
	<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="2" BORDER="0" ALIGN="CENTER" class="list_header_text">
	    <tr>
			<td width="1%"></td>
			<td width="10%">Order #</td>
			<td width="10%">Delivery Date</td>
			<td width="11%">Status</td>
			<td width="10%">Amount</td>
			<td width="18%">Customer Name</td>
			<td width="18%">Email/Username</td>
			<td width="11%">Home Phone #</td>
			<td width="11%">Alt. Phone #</td>
			<td><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>
		</tr>
	</table>
	</div>
	<div class="list_content" style="height: 95%;">
<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" ALIGN="CENTER" class="list_content_text">
<%	if (orders.size() > 0) { %>
	<logic:iterate id="info" collection="<%= orders %>" type="com.freshdirect.fdstore.customer.FDCustomerOrderInfo" indexId="counter">
    <%
        //String bgcolor = (counter.intValue() % 2 == 0) ? "#EEEEEE" : "#FFFFFF";
    %>
	<TR VALIGN="top" <%= counter.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %> style="cursor: pointer;" onClick="document.location='<%= response.encodeURL("/main/order_details.jsp?orderId="+ info.getSaleId()) %>'">
		<td width="1%"></td>
		<TD width="10%"><a href="/main/order_details.jsp?orderId=<%=info.getSaleId()%>"><%=info.getSaleId()%></a></TD>
		<TD width="10%"><%= CCFormatter.formatDate(info.getDeliveryDate()) %></TD>
		<TD width="11%"><%= (info.getOrderStatus() != null) ? info.getOrderStatus().getName() : "--" %></TD>
		<TD width="10%"><%= JspMethods.formatPrice(info.getAmount()) %></TD>
		<TD width="18%"><%= info.getLastName() %>, <%= info.getFirstName() %></TD>
		<TD width="18%"><%= info.getEmail() %></TD>
		<TD width="11%"><%= info.getPhone() %></TD>
		<TD width="11%"><%= info.getAltPhone() %></TD>
	</TR>
	<tr><td colspan="11" class="list_separator" style="padding: 0px;"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
	</logic:iterate>
<% 	} else { %>
    <tr><td colspan="11" align="center"><br>No pending returns need to be processed</td></tr>
<%  }   %>
</TABLE>
	</div>
</div>
</fd:getOrdersByStatus>

</tmpl:put>

</tmpl:insert>
