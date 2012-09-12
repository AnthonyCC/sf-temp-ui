<%@ page import='java.text.*' %>
<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@page import="com.freshdirect.fdstore.customer.CustomerCreditModel"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='crm' prefix='crm' %>

<tmpl:insert template='/template/supervisor_resources.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Auto Late Delivery Credits</tmpl:put>

<tmpl:put name='content' direct='true'>
<crm:AutoLateDlvCreditTag result="result">

<%	String id = request.getParameter("id");
	List sales = null;
	List dlvSales = null;
	if(id != null && id.length() > 0) {
		sales = CallCenterServices.getAutoLateDeliveryOrders(id); 
		dlvSales = CallCenterServices.getAutoLateDlvPassOrders(id);
	}
%>

<form method="POST" name="timePick" id="timePick">
<input type="hidden" name="action" value="submitted" />
<input type="hidden" name="autoId" value="<%=id%>"/>
<div class="sub_nav">
<span class="sub_nav_title">Auto Late Delivery Credit Lates </span> 
<input type="submit" value="Approve Selected Credits" class="submit"/>
</div>

<div class="content" style="height: 80%;">
	<div class="list_header">
<table width="100%" cellpadding="0" cellspacing="2" border="0" class="list_header_text">
	<tr>
		<td width="1%"></td>
		<td width="10%">Order#</td>
		<td width="12%">FirstName</td>
		<td width="12%">LastName</td>
		<td width="12%">Email</td>
		<td width="12%">ComplaintCode</td>
		<td width="12%">Remaining Amount</td>
		<td width="12%">RemType (�F� for Orders with cash delivery fee. �W� for Orders with waived delivery fee)</td>
		<td width="12%">Original Amount</td>
		<td width="12%">Tax Amount</td>
		<td width="12%">Tax Rate</td>
		<td><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>
	</tr>
	</table>
</div>

<div class="list_content" style="height: 95%;">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_content_text">
<%
	java.util.Iterator iter = sales.iterator();
	int count = 0;
	while(iter.hasNext()) {
		CustomerCreditModel ccm = (CustomerCreditModel) iter.next();
		count++;
	
%>
	<tr valign="top" <%= count % 2 == 0 ? "class='list_odd_row'" : "" %> style="cursor: pointer;">		
		<td width="1%"><% if(!"A".equals(ccm.getStatus())) { %><input type="checkbox" name="saleid|<%=ccm.getSaleId()%>" value="<%=ccm.getSaleId()%>"><% } else {%>&nbsp;<%}%></td>
		<td width="10%"><a class="key" href="/supervisor/auto_late_credit_orders.jsp?id=<%=ccm.getId()%>"><b><%=ccm.getSaleId()%></b></a></td>
		<td width="12%"><%=  ccm.getFirstName() %></td>
		<td width="12%"><%=ccm.getLastName()%></td>
		<td width="12%"><%=ccm.getEmail()%></td>
		<td width="12%"><%=ccm.getNewCode()%></td>
		<td width="12%"><%=ccm.getRemainingAmount()%></td>
		<td width="12%"><%=ccm.getRemType()%></td>
		<td width="12%"><%=ccm.getOriginalAmount()%></td>
		<td width="12%"><%=ccm.getTaxAmount()%></td>
		<td width="12%"><%=ccm.getTaxRate()%></td>
	</tr>
	<tr class="list_separator" style="padding: 0px;"><td colspan="12"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
<% } %>
	</table>
	</div>
</div>


<div class="sub_nav">
<span class="sub_nav_title">Auto Late Delivery Pass Lates </span> 
</div>

<div class="content" style="height: 80%;">
	<div class="list_header">
<table width="100%" cellpadding="0" cellspacing="2" border="0" class="list_header_text">
	<tr>
		<td width="1%"></td>
		<td width="10%">Order#</td>
		<td width="12%">FirstName</td>
		<td width="12%">LastName</td>
		<td width="12%">Email</td>
		<td width="12%">ComplaintCode</td>
		<td width="12%">Remaining Amount</td>
		<td width="10%">Dlv Pass ID</td>
		<td width="2%">No. Weeks</td>
		<td width="12%">Original Amount</td>
		<td width="12%">Tax Amount</td>
		<td width="12%">Tax Rate</td>
		<td><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>
	</tr>
	</table>
</div>

<div class="list_content" style="height: 95%;">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_content_text">
<%
	java.util.Iterator iter2 = dlvSales.iterator();
	count = 0;
	while(iter2.hasNext()) {
		CustomerCreditModel ccm = (CustomerCreditModel) iter2.next();
		count++;
	
%>
	<tr valign="top" <%= count % 2 == 0 ? "class='list_odd_row'" : "" %> style="cursor: pointer;">		
		<td width="1%"><input type="checkbox" name="dlvPassSaleId|<%=ccm.getSaleId()%>" value="<%=ccm.getSaleId()%>"></td>
		<td width="10%"><a class="key" href="/supervisor/auto_late_credit_orders.jsp?id=<%=ccm.getId()%>"><b><%=ccm.getSaleId()%></b></a></td>
		<td width="12%"><%=  ccm.getFirstName() %></td>
		<td width="12%"><%=ccm.getLastName()%></td>
		<td width="12%"><%=ccm.getEmail()%></td>
		<td width="12%"><%=ccm.getNewCode()%></td>
		<td width="12%"><%=ccm.getRemainingAmount()%></td>
		<td width="10%"><%=ccm.getDlvPassId()%></td>
		<td width="2%">1</td>
		<td width="12%"><%=ccm.getOriginalAmount()%></td>
		<td width="12%"><%=ccm.getTaxAmount()%></td>
		<td width="12%"><%=ccm.getTaxRate()%></td>
	</tr>
	<tr class="list_separator" style="padding: 0px;"><td colspan="12"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
<% } %>
	</table>
	</div>
</div>
</form>

<script language="JavaScript">
    function checkedAll () {
		for (var i = 0; i < document.getElementById('timePick').elements.length; i++) {
			document.getElementById('timePick').elements[i].checked = true;
		}
    }
	
	window.onload =checkedAll();
</script>	
</crm:AutoLateDlvCreditTag>
</tmpl:put>

</tmpl:insert>
