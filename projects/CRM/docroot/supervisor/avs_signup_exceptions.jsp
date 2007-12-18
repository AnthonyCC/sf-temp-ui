<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > AVS Signup Promo Exceptions</tmpl:put>

<tmpl:put name='content' direct='true'>

<% List searchResults = CallCenterServices.getSignupPromoAVSExceptions(); %>

<jsp:include page="/includes/supervisor_nav.jsp" />
<div class="sub_nav">
<span class="sub_nav_title">AVS Exceptions on Signup Promotion ( <span class="result"><%= searchResults.size() %></span> )</span>
</div>

<div class="content" style="height: 80%;">
	<div class="list_header" style="padding-top: 4px; padding-bottom: 4px;">
	<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="list_header_text">
	    <TR>
			<TD WIDTH="2%"></TD>
			<TD WIDTH="8%">Order #</TD>
			<TD WIDTH="10%">Delivery</TD>
			<TD WIDTH="7%">Status</TD>
			<TD WIDTH="7%">Amount</TD>
			<TD WIDTH="13%">Customer Name</TD>
			<TD WIDTH="7%">ID</TD>
			<TD WIDTH="20%">Email</TD>
			<TD WIDTH="10%">Home Phone #</TD>
			<TD WIDTH="10%">Alt. Phone #</TD>
			<TD WIDTH="4%"></TD>
		</TR>
	</table>
	</div>
	<div class="list_content" style="height: 95%;">
	<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="list_content_text">
	<%	if (searchResults.size() > 0) { %>
		<logic:iterate id="info" collection="<%= searchResults %>" type="com.freshdirect.fdstore.customer.FDCustomerOrderInfo" indexId="counter">
	    <%
	        String bgcolor = (counter.intValue() % 2 == 0) ? "#EEEEEE" : "#FFFFFF";
	    %>
		<TR VALIGN="BOTTOM" BGCOLOR="<%= bgcolor %>">
			<TD>&nbsp;</TD>
			<TD><a href="/main/order_details.jsp?orderId=<%= info.getSaleId() %>"><%= info.getSaleId() %></a></TD>
			<TD><%= CCFormatter.formatDate(info.getDeliveryDate()) %></TD>
			<TD><%= (info.getOrderStatus() != null) ? info.getOrderStatus().getName() : "--" %></TD>
			<TD><%= CCFormatter.formatCurrency(info.getAmount()) %></TD>
			<TD><%= info.getLastName() %>, <%= info.getFirstName() %></TD>
			<TD><%= info.getIdentity().getFDCustomerPK() %></TD>
			<TD><%= info.getEmail() %></TD>
			<TD><%= info.getPhone() %></TD>
			<TD><%= info.getAltPhone() %></TD>
			<TD ALIGN="CENTER"><a href="/main/order_details.jsp?orderId=<%= info.getSaleId() %>"><img src="/media_stat/images/buttons/eyeglass.gif" width="14" height="13" border="0" alt="Is this the order you want?"></a></TD>
		</TR>
		<tr><td colspan="11" class="separator" style="padding: 0px;"><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td></tr>
		</logic:iterate>
	<% 	} else { %>
	    <tr><td colspan="11" align="center"><br><br><b>no AVS Exceptions found on Signup promo orders</b></td></tr>
	<%  }   %>
	</TABLE>
	</div>
</div>
</tmpl:put>

</tmpl:insert>
