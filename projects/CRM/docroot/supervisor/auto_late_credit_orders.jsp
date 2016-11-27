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

<%
	//System.out.println("*******************"+session.getAttribute("LATE_CREDITS"));
	if("submitted".equals(request.getParameter("action")) && session.getAttribute("LATE_CREDITS") != null && "done".equals((String)session.getAttribute("LATE_CREDITS"))) {		
		session.removeAttribute("LATE_CREDITS");
%>
	<jsp:include page="auto_late_dlv_credits.jsp" />
<% } else { %>

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

<form method="POST" name="timePick" id="timePick" onsubmit="return confirm('Are you sure you want to approve credits?')">
<input type="hidden" name="action" value="submitted" />
<input type="hidden" name="autoId" value="<%=id%>" />
	
	<div class="sub_nav">
		<span class="sub_nav_title">Auto Late Delivery Credits</span> 
	
		<% if (FDStoreProperties.isAutoLateCreditButtonOn()) { %>
			<input type="submit" value="Approve Selected Credits" class="submit" style="float: right;" />
		<% } %>
		
		<span style="float: right;">(“F” for Orders with cash delivery fee. “W” for Orders with waived delivery fee)</span>
	</div>
	
	<div class="list_header">
		<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_header_text">
			<tr>
				<td class="autolateCred-cbSpacer"></td>
				<td class="autolateCred-ordId">Order #</td>
				<td class="autolateCred-fName">First Name</td>
				<td class="autolateCred-lname">Last Name</td>
				<td class="autolateCred-email">Email</td>
				<td class="autolateCred-compCode">Complaint Code</td>
				<td class="autolateCred-rmnAmt">Remaining Amt</td>
				<td class="autolateCred-remType">Rem Type</td>
				<td class="autolateCred-origAmt">Original Amt</td>
				<td class="autolateCred-taxAmt">Tax Amt</td>
				<td class="autolateCred-taxRate">Tax Rate</td>
				<td class="autolateCred-spacer"></td>			
			</tr>
		</table>
	</div>
	
	<div class="list_content" style="max-height: 37%;">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<%
			java.util.Iterator iter = sales.iterator();
			int count = 0;
			while(iter.hasNext()) {
				CustomerCreditModel ccm = (CustomerCreditModel) iter.next();
				count++;
			
		%>
			<tr valign="top" <%= count % 2 == 0 ? "class='list_odd_row'" : "" %> >		
				<td class="autolateCred-cbSpacer"><% if(!"A".equals(ccm.getStatus())) { %><input type="checkbox" name="saleid|<%=ccm.getSaleId()%>" value="<%=ccm.getSaleId()%>"><% } else {%>&nbsp;<%}%></td>
				<td class="autolateCred-ordId"><a href="/main/order_details.jsp?orderId=<%=ccm.getSaleId()%>"><%=ccm.getSaleId()%></a></td>
				<td class="autolateCred-fName"><%=  ccm.getFirstName() %></td>
				<td class="autolateCred-lname"><%=ccm.getLastName()%></td>
				<td class="autolateCred-email"><%=ccm.getEmail()%></td>
				<td class="autolateCred-compCode"><%=ccm.getNewCode()%></td>
				<td class="autolateCred-rmnAmt"><%=ccm.getRemainingAmount()%></td>
				<td class="autolateCred-remType"><%=ccm.getRemType()%></td>
				<td class="autolateCred-origAmt"><%=ccm.getOriginalAmount()%></td>
				<td class="autolateCred-taxAmt"><%=ccm.getTaxAmount()%></td>
				<td class="autolateCred-taxRate"><%=ccm.getTaxRate()%></td>
				<td class="autolateCred-spacer"></td>
			</tr>
			<tr class="list_separator" style="padding: 0px;"><td colspan="12"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
		<% } %>
		</table>
	</div>

	<div class="sub_nav">
		<span class="sub_nav_title">Auto Late Delivery Pass Lates </span> 
	</div>
	
	<div class="list_header">
		<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_header_text">
			<tr>
				
				<td class="autolateCred-cbSpacer"></td>
				<td class="autolateCred-ordId">Order&nbsp;#</td>
				<td class="autolateCred-fName">First&nbsp;Name</td>
				<td class="autolateCred-lname">Last&nbsp;Name</td>
				<td class="autolateCred-email">Email</td>
				<td class="autolateCred-compCode">Complaint&nbsp;Code</td>
				<td class="autolateCred-rmnAmt">Remaining&nbsp;Amt</td>
				<td class="autolateCred-dpId">Dlv&nbsp;Pass&nbsp;ID</td>
				<td class="autolateCred-dpWeeks">No.&nbsp;Weeks</td>
				<td class="autolateCred-origAmt">Original&nbsp;Amt</td>
				<td class="autolateCred-taxAmt">Tax&nbsp;Amt</td>
				<td class="autolateCred-taxRate">Tax&nbsp;Rate</td>
				<td class="autolateCred-spacer"></td>
			</tr>
		</table>
	</div>
	
	<div class="list_content" style="max-height: 40%;">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<%
			java.util.Iterator iter2 = dlvSales.iterator();
			count = 0;
			while(iter2.hasNext()) {
				CustomerCreditModel ccm = (CustomerCreditModel) iter2.next();
				count++;
			
		%>
			<tr valign="top" <%= count % 2 == 0 ? "class='list_odd_row'" : "" %> >		
				<td class="autolateCred-cbSpacer"><% if(!"A".equals(ccm.getStatus())) { %><input type="checkbox" name="dlvPassSaleId|<%=ccm.getSaleId()%>" value="<%=ccm.getSaleId()%>"><% } else {%>&nbsp;<%}%></td>
				<td class="autolateCred-ordId"><a href="/main/order_details.jsp?orderId=<%=ccm.getSaleId()%>"><%=ccm.getSaleId()%></a></td>
				<td class="autolateCred-fName"><%=ccm.getFirstName() %></td>
				<td class="autolateCred-lName"><%=ccm.getLastName()%></td>
				<td class="autolateCred-email"><%=ccm.getEmail()%></td>
				<td class="autolateCred-compCode"><%=ccm.getNewCode()%></td>
				<td class="autolateCred-rmnAmt"><%=ccm.getRemainingAmount()%></td>
				<td class="autolateCred-dpId"><%=ccm.getDlvPassId()%></td>
				<td class="autolateCred-dpWeeks">1</td>
				<td class="autolateCred-origAmt"><%=ccm.getOriginalAmount()%></td>
				<td class="autolateCred-taxAmt"><%=ccm.getTaxAmount()%></td>
				<td class="autolateCred-taxRate"><%=ccm.getTaxRate()%></td>
				<td class="autolateCred-spacer"></td>
			</tr>
			<tr class="list_separator" style="padding: 0px;"><td colspan="13"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
		<% } %>
		</table>
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

<% } %>
