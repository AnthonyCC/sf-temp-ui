<%@ page import="com.freshdirect.fdstore.customer.adapter.FDOrderAdapter"%>

<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>

<tmpl:insert template='/template/large_pop.jsp'>
	<tmpl:put name='title' direct='true'>Order Not Submitted</tmpl:put>
		<tmpl:put name='content' direct='true'>
<%	String orderId = request.getParameter("orderId"); %>
<fd:GetOrder id='order' saleId='<%= orderId %>'>
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="order">
	<tr>
		<td>
		<img src="/media_stat/images/layout/clear.gif" width="1" height="3"><br>
		<b>Order Not Submitted</b><br>
		This order was not accepted by our system. Details follow.<br><br>
		</td>
	</tr>
	<%	List messages = ((FDOrderAdapter)order).getSubmissionFailedMessage(); %>
		<logic:iterate id="message" collection="<%= messages %>" type="java.lang.String" indexId="counter"> 
	<tr valign="top" <%= counter.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %>>
	<td>
	<pre><%= message %></pre>
	</td>
	</tr>
	<tr><td class="list_separator" style="padding: 0px;"><img src="/media_stat/images/layout/clear.gif" width="1" height="1"></td>
	</tr>
	</logic:iterate>
</table>
</fd:GetOrder>
		</tmpl:put>
</tmpl:insert>





