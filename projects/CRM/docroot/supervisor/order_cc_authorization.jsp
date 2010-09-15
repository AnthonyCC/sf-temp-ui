<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>

<%@ page import="com.freshdirect.fdstore.customer.FDAuthInfoSearchCriteria" %>
<%@ page import="com.freshdirect.common.customer.EnumCardType" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.customer.EnumSaleType"%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>

<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<%! DateFormatSymbols symbols = new DateFormatSymbols();    %>

<%
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader ("Expires", 0);
%>

<%
    Calendar today = Calendar.getInstance();
    int currmonth = today.get(Calendar.MONTH);
    int currdate  = today.get(Calendar.DATE);
    int curryear  = today.get(Calendar.YEAR);
    
    today.add(Calendar.DATE,1);
    
    int date   = request.getParameter("transDate") != null ? Integer.parseInt(request.getParameter("transDate")) : currdate;
	int month = request.getParameter("transMonth") != null ? Integer.parseInt(request.getParameter("transMonth")) : currmonth;
    int year  = request.getParameter("transYear") != null ? Integer.parseInt(request.getParameter("transYear")) : curryear;
    
%>

<%
List subjectLines = null;
Calendar cal = Calendar.getInstance();
Date dateParam = null;
boolean showAutoCases = true;
boolean posted = false;

if ("POST".equals(request.getMethod())) {
	posted = true;
        cal.set(Calendar.DAY_OF_MONTH, date);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        
        dateParam = cal.getTime();
}
%>
<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Orders by Credit Card Info</tmpl:put>

<tmpl:put name='content' direct='true'>

<jsp:include page="/includes/supervisor_nav.jsp" />
<%
FDAuthInfoSearchCriteria criteria = new FDAuthInfoSearchCriteria();
criteria.setCardType(EnumCardType.getCardType(NVL.apply(request.getParameter("cardType"), "").trim()));
String chargedAmount = NVL.apply(request.getParameter("chargedAmount"), "").trim();
criteria.setChargedAmount("".equals(chargedAmount) ? 0.0 : Double.parseDouble(chargedAmount));
criteria.setCCKnownNum(NVL.apply(request.getParameter("ccKnownNum"), ""));
criteria.setTransDate(NVL.apply(request.getParameter("transDate"), ""));
criteria.setTransMonth(NVL.apply(request.getParameter("transMonth"), ""));
criteria.setTransYear(NVL.apply(request.getParameter("transYear"), ""));
%>

<fd:CrmAuthSearchController id="searchResults" result="result" criteria="<%=criteria%>">
<div class="sub_nav">
<table width="99%" cellpadding="0" cellspacing="0" border="0" class="sub_nav_text">
	<form name="authSearch" method="POST">
	<script language="JavaScript">
    function checkForm(thisForm) {
        var date = thisForm.transDate.value;
        var month = thisForm.transMonth.value;
        var year = thisForm.transYear.value;
		var chargedAmt = thisForm.chargedAmount.value;
		var ccNum = thisForm.ccKnownNum.value;
		var date1=year+month+date;
		var validAmtChars = "0123456789.";
        var validCCChars = "0123456789*";
		var amtChar;
		var ccChar;
		var okToSubmit= true;

		if (date1.length < 7 || date1.length > 8 ) {
            alert('The date field is invalid. Please correct and try again.');
            okToSubmit = false;
        }

		if (chargedAmt.length > 0) {
			for (i = 0; i < chargedAmt.length && okToSubmit == true; i++) { 
			   amtChar = chargedAmt.charAt(i); 
			   if (validAmtChars.indexOf(amtChar) == -1) {
			      okToSubmit = false;
				  alert('Please enter a valid number for Charged Amount.');
			   }
			}
		}
	
		if (ccNum.length > 0) {
			for (k = 0; k < ccNum.length && okToSubmit == true; k++) { 
			   ccChar = ccNum.charAt(i); 
			   if (validCCChars.indexOf(ccChar) == -1) {
			      okToSubmit = false;
				  alert('Please enter a valid combination for Known Digits e.g 1234*, *5678, 1234*5678.');
			   }
			}
		}

        if (okToSubmit) {
		    thisForm.submit();
        }
    }
</script>

	<tr>
		<td width="20%" class="sub_nav_title">Orders by Credit Card Info</td>
		<td width="60%" align="center">
		<table width="90%" cellpadding="0" cellspacing="0" border="0">
		<tr valign="top">
			<td>CC Type:<br>
			<fd:ErrorHandler result='<%=result%>' name='cardType' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span><br></fd:ErrorHandler>
			<select name="cardType">
				<option value="">Select</option>
				<logic:iterate id="type" collection="<%=EnumCardType.getCardTypes()%>" type="com.freshdirect.common.customer.EnumCardType">
					<option value="<%=type.getFdName()%>" <%=type.equals(criteria.getCardType()) ? "selected" : "" %>><%=type.getDisplayName()%></option>
				</logic:iterate>
			</select>
			</td>
			<td>Charged Amount:<br>
			<fd:ErrorHandler result='<%=result%>' name='chargedAmount' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span><br></fd:ErrorHandler>
			$ <input name="chargedAmount" type="text" value="<%=criteria.getChargedAmount()%>" style="width:80px;">
			</td>
			<td>Known Digits:<br>
			<fd:ErrorHandler result='<%=result%>' name='ccKnownNum' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span><br></fd:ErrorHandler>
			<input name="ccKnownNum" value="<%=criteria.getCCKnownNum()%>" type="text" style="width:100px;">
			</td>
			<td>Transaction/Charge Date:<br>
			<fd:ErrorHandler result='<%=result%>' name='transMonth' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span><br></fd:ErrorHandler>
			<fd:ErrorHandler result='<%=result%>' name='transDate' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span><br></fd:ErrorHandler>
			<fd:ErrorHandler result='<%=result%>' name='transYear' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span><br></fd:ErrorHandler>
			<table cellpadding="0" cellspacing="0" border="0">
                <tr>
                    <td>
                        <select name="transMonth" required="true" class="pulldown">
                            <option value="">Month</option>
                                        <%  for (int i=0; i<12; i++) {  %>
                                        <option value="<%= i %>" <%= (i==month)?"selected":"" %>><%= symbols.getShortMonths()[i] %></option>
                                        <%  }   %>
                        </select>
                    </td>
                    <td>
                        <select name="transDate" required="true" class="pulldown">
                            <option value="">Date</option>
                                        <%  for (int i=1; i<=31; i++) { %>
                            <option value="<%= i %>" <%= (i==date)?"selected":"" %>><%= i %></option>
                                        <%  } %>
                        </select>
                    </td>
                    <td>
                        <select name="transYear" required="true" class="pulldown">
                            <option value="">Year</option>
                                        <%  for (int i=2001; i<2011; i++) { %>
                            <option value="<%= i %>" <%= (i==year)?"selected":"" %>><%= i %></option>
                                        <%  } %>
                        </select>
                    </td>
                </tr>
            </table>
			</td>
		</tr>
		<tr><td colspan="4" class="field_note"><b>All fields are required.</b> Known digits, enter * as wildcards/unknowns, e.g 1234*5678, 1234*, *5678<br><img src="/media_stat/crm/images/clear.gif" width="1" height="2"></td></tr>
		</table>
		</td>
		<td width="15%"><input type="submit" class="submit" onClick="javascript:checkForm(authSearch); return false;" value="GO"></td>
	</tr>
	</form>
</table>
</div>
<div class="list_header">
<table width="100%" cellpadding="0" cellspacing="2" border="0" class="list_header_text">
<tr valign="bottom">
		<td width="6%">Order #</td>
        <td width="2%">Type</td>
        <td width="10%">Delivery Date</td>
        <td width="12%">Transaction Date</td>
        <td width="6%">Status</td>
        <td width="9%">Amount</td>
        <td width="15%">Customer Name</td>
		<td width="15%">Name on Card</td>
        <td width="25%">Action</td>
	</tr>
</table>
</div>
<%if(searchResults != null && !searchResults.isEmpty()){%>
	<div class="list_content">
		<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_content_text">
		<logic:iterate id="info" collection="<%= searchResults %>" type="com.freshdirect.fdstore.customer.FDAuthInfo" indexId="counter">
			<tr valign="top" <%= counter.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %> style="cursor: pointer;" onClick="document.location='<%= response.encodeURL("/main/order_details.jsp?orderId=" + info.getSaleId()) %>'">
				<td width="6%" class="border_bottom"><a href="/main/order_details.jsp?orderId=<%=info.getSaleId()%>"><b><%=info.getSaleId()%></b></a>&nbsp;</td>
				<td width="2%" class="border_bottom"><% if(EnumSaleType.REGULAR.equals(info.getOrderType())){%>M<%}else if(EnumSaleType.SUBSCRIPTION.equals(info.getOrderType())){%>A<%}%>&nbsp;</td>
                <td width="10%" class="border_bottom"><%=CCFormatter.formatDate(info.getDeliveryDate())%>&nbsp;</td>
				<td width="12%" class="border_bottom"><%=CCFormatter.formatDateTime(info.getTransactionDateTime())%>&nbsp;</td>
				<td width="6%" class="border_bottom"><%=info.getSaleStatus().getName()%>&nbsp;</td>
				<td width="9%" class="border_bottom"><%=JspMethods.formatPrice(info.getAuthAmount())%>&nbsp;</td>
				<td width="15%" class="border_bottom"><%=info.getFirstName()%>,&nbsp;<%=info.getLastName()%></td>
				<td width="15%" class="border_bottom"><%=info.getNameOnCard()%>&nbsp;</td>
				<td width="25%" class="border_bottom"><i>Auth Code:</i> <%=info.getAuthCode().trim()%>, <i>Message:</i> <%=info.getAuthDescription()%><br><i>CC type:</i> <%=info.getCardType()%>, <i>Last 4</i>: <%=info.getCCLastFourNum()%></td>
			</tr>
		</logic:iterate>
		</table>
	</div>
<%} else {%>
	<%if("POST".equalsIgnoreCase(request.getMethod()) && result.isSuccess()){%>
		<div class="content_fixed" align="center"><br><br>No matching orders found<br><br><br></div>
	<%}%>
<%}%>
</fd:CrmAuthSearchController>

</tmpl:put>

</tmpl:insert>
