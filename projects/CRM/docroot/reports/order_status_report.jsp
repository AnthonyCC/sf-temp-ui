<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.common.address.EnumAddressType" %>
<%@ page import="com.freshdirect.logistics.delivery.model.EnumAddressExceptionReason" %>
<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Reports > Orders by Status</tmpl:put>

<tmpl:put name='content' direct='true'>
<%@ include file="/includes/i_globalcontext.jspf" %>
<jsp:include page="/includes/reports_nav.jsp" />
<fd:CrmOrderStatusSearch result="result" actionName="searchOrders" reportName="report">
<table width="100%" cellpadding="0" cellspacing="0" class="sub_nav"><form method='POST' name="order_status"><tr><td><span class="sub_nav_title">Orders by Status</span> Search for orders in a particular status <% if ("POST".equals(request.getMethod()) && report.size() > 0) {%><span class="note" style="font-weight: normal;"><input type="checkbox" name="forPrint" onClick="javascript:toggleScroll('result','list_content','content_fixed');"> Print View</span><% } %></td><td align="right"><a href="/reports/reports_index.jsp">All Reports >></a></td></tr></table>

<table width="100%" cellpadding="0" cellspacing="0" class="content_fixed">
        <tr>
            <td rowspan="2"><b>Select Status: </b></td>
            <td width="15%">
			<input type="checkbox" name="status" value="<%=EnumSaleStatus.NOT_SUBMITTED.getStatusCode()%>" checked><%=EnumSaleStatus.NOT_SUBMITTED.getName()%>
            </td>
			<td width="20%"><input type="checkbox" name="status" value="<%=EnumSaleStatus.INPROCESS.getStatusCode()%>"><%=EnumSaleStatus.INPROCESS.getName()%></td>
			<td width="15%"><input type="checkbox" name="status" value="<%=EnumSaleStatus.ENROUTE.getStatusCode()%>"><%=EnumSaleStatus.ENROUTE.getName()%></td>
			<td width="15%"><input type="checkbox" name="status" value="<%=EnumSaleStatus.MODIFIED.getStatusCode()%>"><%=EnumSaleStatus.MODIFIED.getName()%></td>
			<td rowspan="2"><input type="reset" value="CLEAR" class="clear"><input type="submit" value="SEARCH ORDER" class="submit"></td>
        </tr>
        <tr>
           <td><input type="checkbox" name="status" value="<%=EnumSaleStatus.NEW.getStatusCode()%>"><%=EnumSaleStatus.NEW.getName()%></td>
		   <td><input type="checkbox" name="status" value="<%=EnumSaleStatus.INPROCESS_NO_AUTHORIZATION.getStatusCode()%>"><%=EnumSaleStatus.INPROCESS_NO_AUTHORIZATION.getName()%></td>
		   <td><input type="checkbox" name="status" value="<%=EnumSaleStatus.AUTHORIZATION_FAILED.getStatusCode()%>"><%=EnumSaleStatus.AUTHORIZATION_FAILED.getName()%></td>
		   <td><input type="checkbox" name="status" value="<%=EnumSaleStatus.MODIFIED_CANCELED.getStatusCode()%>"><%=EnumSaleStatus.MODIFIED_CANCELED.getName()%></td>
        </tr>
        
    </form>
</table>
<%
String status = request.getParameter("status");
if(report != null){%>
<div class="list_header">
<table class="list_header_text" width="100%" cellpadding="0" cellspacing="0">
    <tr>
		<td style="width: 10px;">&nbsp;</td>
        <td style="width: 200px;">Order ID</td>
        <td style="width: 200px;">SAP ID</td>
        <td style="width: 200px;">Status</td>
        <td style="width: 100px;">Store</td>
        <td style="width: 100px;">Facility</td>
		<td style="width: auto;">&nbsp;</td>
    </tr>
</table>
</div>
<div id="result" class="list_content" style="height:70%;">
<table width="100%" cellpadding="0" cellspacing="0" border="0" style="empty-cells: show">
	<% if (report.size() == 0) { %>
		<tr><td align="center"><br><b>No <% if (!"".equals(status)) { %><%=EnumSaleStatus.getSaleStatus(status)%><% } %> orders found</b></td></tr>
	<% } %>
		<%
			int displayedLines = 0;
		%>	
    <logic:iterate id="order" collection="<%= report %>" type="com.freshdirect.crm.CrmOrderStatusReportLine" indexId="idx">
	    <% if ( 
				((globalContextStore).equalsIgnoreCase("All") || (globalContextStore).equals(order.geteStore())) &&
				((globalContextFacility).equalsIgnoreCase("All") || (globalContextFacility).equals(order.getFacility()))
		) { %>
			<tr>
				<td style="width: 10px;">&nbsp;</td>
		        <td style="width: 200px;" class="border_bottom"><a href="/main/order_details.jsp?orderId=<%=order.getSaleId()%>"><%=order.getSaleId()%></a>&nbsp;</td>
		        <td style="width: 200px;" class="border_bottom"><%=order.getSapNumber()%>&nbsp;</td>
		        <td style="width: 200px;" class="border_bottom"><%=order.getStatus()%>&nbsp;</td>
		        <td style="width: 100px;" class="border_bottom"><%=order.geteStore()%>&nbsp;</td>
		        <td style="width: 100px;" class="border_bottom"><%=order.getFacility()%>&nbsp;</td>
				<td style="width: auto;" class="border_bottom">&nbsp;</td>
		    </tr>
			<% displayedLines++;  %>
		<% } %>
    </logic:iterate>
    <% if (displayedLines == 0) { %>
		<tr><td align="center"><br><b>No <% if (!"".equals(status)) { %><%=EnumSaleStatus.getSaleStatus(status)%><% } %> orders found</b></td></tr>
	<% } %>
</table>
</div>
<% } %>
<script language"javascript">
	<!--
	function toggleScroll(divId,currentClass,newClass) {
	var divStyle = document.getElementById(divId);
		if (document.order_status.forPrint.checked) {
			divStyle.className = newClass;
		} else {
			divStyle.className = currentClass;
		}
	}
	//-->
	</script>

</fd:CrmOrderStatusSearch>
</tmpl:put>

</tmpl:insert>