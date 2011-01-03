<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>


<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Reports > Credit Summary Report</tmpl:put>

<tmpl:put name='content' direct='true'>
<%! DateFormatSymbols symbols = new DateFormatSymbols();    %>
    <%
        Calendar today = Calendar.getInstance();
        int currmonth = today.get(Calendar.MONTH);
        int currday  = today.get(Calendar.DATE);
        int curryear  = today.get(Calendar.YEAR);
    %>
<%
    int month = request.getParameter("month") != null ? Integer.parseInt(request.getParameter("month")) : currmonth;
    int day   = request.getParameter("day") != null ? Integer.parseInt(request.getParameter("day")) : currday;
    int year  = request.getParameter("year") != null ? Integer.parseInt(request.getParameter("year")) : curryear;
%>
<jsp:include page="/includes/reports_nav.jsp" />

<%
List credits = null;
Calendar cal = Calendar.getInstance();

if ("POST".equals(request.getMethod())) {
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);

        credits = CallCenterServices.getCreditSummaryForDate(cal.getTime());
}
%>

<div class="sub_nav">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="sub_nav_text">
<form name="credit_report" method="post" action="credit_summary_report.jsp">
<script language="JavaScript">
	function checkForm(thisForm) {
		var day = thisForm.day.value;
		var month = thisForm.month.value;
		var year = thisForm.year.value;
		if (day!='' && month!='' && year!='' ) {
			thisForm.submit();
		} else {
			alert('Required fields are missing. Please complete selections and try again.');
		}
	}
</script>
	<tr>
		<td width="30%"><span class="sub_nav_title">Credit Summary Report <% if (credits!= null && credits.size()>0) {%>( <span class="result"><%= credits.size() %></span> ) <span class="note" style="font-weight:normal;"><input type="checkbox" name="forPrint" onClick="javascript:toggleScroll('result','list_content','content_fixed');"> Print View</span><% } %></span></td>
		<td width="70%">Find credits issued on: 
		<select name="month" required="true" class="pulldown">
			<option value="">Month</option>
                        <%  for (int i=0; i<12; i++) {  %>
                        <option value="<%= i %>" <%= (i==month)?"selected":"" %>><%= symbols.getShortMonths()[i] %></option>
                        <%  }   %>
		</select>
		<select name="day" required="true" class="pulldown">
			<option value="">Date</option>
                        <% 	for (int i=1; i<=31; i++) { %>
			<option value="<%= i %>" <%= (i==day)?"selected":"" %>><%= i %></option>
                        <%	} %>
		</select>
		<select name="year" required="true" class="pulldown">
			<option value="">Year</option>
                        <% 	for (int i=2005; i<2016; i++) { %>
			<option value="<%= i %>" <%= (i==year)?"selected":"" %>><%= i %></option>
                        <%	} %>
		</select>
       	<input type="submit" class="submit" onClick="javascript:checkForm(credit_report); return false;" value="GO">
		</td>
	</tr>
	</form>
	<script language"javascript">
	<!--
	function toggleScroll(divId,currentClass,newClass) {
	var divStyle = document.getElementById(divId);
		if (document.credit_report.forPrint.checked) {
			divStyle.className = newClass;
		} else {
			divStyle.className = currentClass;
		}
	}
	//-->
	</script>
</table>
</div>

<%
    if ("POST".equals(request.getMethod())) {
        /*Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);

        List credits = CallCenterServices.getCreditSummaryForDate(cal.getTime());*/
%>

<%  if (credits.size() > 0) { %>
<div class="list_header">
<table width="100%" cellpadding="0" cellspacing="2" border="0" class="list_header_text">
<tr valign="BOTTOM" >
		<td width="10%">Customer</td>
        <td width="6%">Order</td>
        <td width="9%">Status</td>
        <td width="9%">Delivery</td>
        <td width="15%">Note</td>
        <td width="7%" align="right">Invoice</td>
        <td width="7%" align="right">Credit</td>
        <td width="8%" align="center">Total Orders</td>
        <td width="10%">Previous Credits</td>
        <td width="21%">Credit Items</td>
		<td><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>
	</tr>
</table>
</div>
<div class="list_content" id="result">
	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_content_text">
	<logic:iterate id="credit" collection="<%= credits %>" type="com.freshdirect.fdstore.customer.FDCreditSummary" indexId="counter">
	<tr valign="top" <%= counter.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %>>
		<td width="10%" class="border_bottom"><%= credit.getCustomerName() %>&nbsp;</td>
        <td width="6%" class="border_bottom"><%= credit.getOrderNumber() %>&nbsp;</td>
        <td width="9%" class="border_bottom"><%= credit.getStatus() %>&nbsp;</td>
        <td width="9%" class="border_bottom"><%= CCFormatter.formatDate(credit.getDeliveryDate()) %>&nbsp;</td>
        <td width="15%" class="border_bottom"><%= credit.getNote() %>&nbsp;</td>
        <td width="7%" align="right" class="border_bottom">&nbsp;<%= JspMethods.formatPrice(credit.getInvoiceAmount()) %></td>
        <td width="7%" align="right" class="border_bottom">&nbsp;<%= JspMethods.formatPrice(credit.getCreditAmount()) %></td>
        <td width="8%" align="center" class="border_bottom"><%= credit.getNumberOfOrders() %>&nbsp;</td>
        <td width="10%" class="border_bottom"><%= JspMethods.formatPrice(credit.getPreviousCreditAmount()) %>&nbsp;</td>
        <td width="21%" class="border_bottom">
            <table cellpadding="0" cellspacing="0" border="0" width="100%" class="list_content_text">
                <logic:iterate id="item" collection="<%= credit.getItems() %>" type="com.freshdirect.fdstore.customer.FDCreditSummary.Item" indexId="counter">
                <% if (counter.intValue() > 0) { %>
					<tr class="list_separator" style="padding: 0px;"><td colspan="3"><img src="/media_stat/crm/images/clear.gif" width="1" height="1"></td></tr>
				<% } %>
				<tr valign="top">
                    <td>
                        <%= item.getDescription() %>
                        <% if (!"".equals(item.getConfiguration()) && (null != item.getConfiguration())) { %>(<%= item.getConfiguration() %>)<% } %>
                        <% if (!"".equals(item.getSkuCode())) { %><%= item.getSkuCode() %><% } %>
                    </td>
                    <td><%= item.getReason() %></td>
                    <td align="right"><% if (item.getQuantity() > 0.0) { %><%= CCFormatter.formatQuantity(item.getQuantity()) %><% } %></td>
                </tr>
                </logic:iterate>
            </table>
        </td>
	</tr>
	</logic:iterate>
	</table>
</div>
<%      } else { %>
    <div class="content_fixed" align="center"><br><b>No credits issued on <%= CCFormatter.formatDate(cal.getTime()) %></b><br><br></div>
<%      }   %>
<%  }   %>

</tmpl:put>

</tmpl:insert>
