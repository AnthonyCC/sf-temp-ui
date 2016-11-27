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
<tmpl:insert template='/template/supervisor_resources.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Orders by Credit Card Info</tmpl:put>

<tmpl:put name='content' direct='true'>

<%@ include file="/includes/i_globalcontext.jspf" %>

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
	//	if (date1.length < 7 || date1.length > 8 ) {
		if(year==""||month==""||date==""){
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
<div class="sub_nav">
	<form name="authSearch" method="POST">
		<table width="99%" cellpadding="0" cellspacing="0" border="0" class="sub_nav_text">
			<tr>
				<td width="300" class="sub_nav_title">Orders by Credit Card Info</td>
				<td align="center">
					<table width="100%" cellpadding="0" cellspacing="0" border="0">
						<tr valign="top">
							<td width="150">CC Type:<br>
								<fd:ErrorHandler result='<%=result%>' name='cardType' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span><br></fd:ErrorHandler>
								<select name="cardType">
									<option value="">Select</option>
									<logic:iterate id="type" collection="<%=EnumCardType.getCardTypes()%>" type="com.freshdirect.common.customer.EnumCardType">
										<option value="<%=type.getFdName()%>" <%=type.equals(criteria.getCardType()) ? "selected" : "" %>><%=type.getDisplayName()%></option>
									</logic:iterate>
								</select>
							</td>
							<td width="150" style="white-space: nowrap;">Charged Amount:<br>
								<fd:ErrorHandler result='<%=result%>' name='chargedAmount' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span><br></fd:ErrorHandler>
								$&nbsp;<input name="chargedAmount" type="text" value="<%=criteria.getChargedAmount()%>" style="width:80px;">
							</td>
							<td width="150">Known Digits:<br>
								<fd:ErrorHandler result='<%=result%>' name='ccKnownNum' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span><br></fd:ErrorHandler>
								<input name="ccKnownNum" value="<%=criteria.getCCKnownNum()%>" type="text" style="width:100px;">
							</td>
							<td width="200">Transaction/Charge Date:<br>
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
					                                        <%  for (int i=2005; i<= curryear+1; i++) { %>
					                            <option value="<%= i %>" <%= (i==year)?"selected":"" %>><%= i %></option>
					                                        <%  } %>
					                        </select>
					                    </td>
					                </tr>
					            </table>
							</td>
							<td width="100" valign="middle" rowspan="2">
								<input type="submit" class="submit" onClick="javascript:checkForm(authSearch); return false;" value="GO" style="width: 50px; padding: 3px;" />
							</td>
							<td rowspan="2">&nbsp;</td>
						</tr>
						<tr>
							<td colspan="4" class="field_note"><b>All fields are required.</b> Known digits, enter * as wildcards/unknowns, e.g 1234*5678, 1234*, *5678<br><img src="/media_stat/crm/images/clear.gif" width="1" height="2"></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
</div>
<div class="list_header">
	<table width="100%" cellpadding="0" cellspacing="2" border="0" class="list_header_text">
		<tr valign="bottom">
			<td class="ordCred-spacer"></td>
			<td class="ordCred-ordId">Order #</td>
	        <td class="ordCred-type">Type</td>
	        <td class="ordCred-dlvDate">Dlv Date</td>
	        <td class="ordCred-transDate">Trans Date</td>
	        <td class="ordCred-status">Status</td>
	        <td class="ordCred-ordAmt">Amount</td>
	        <td class="ordCred-custName">Customer Name</td>
			<td class="ordCred-store">Store</td>
			<td class="ordCred-cardName">Name on Card</td>
	        <td class="ordCred-action">Action</td>
			<td class="ordCred-spacer"></td>
		</tr>
	</table>
</div>
<%if(searchResults != null && !searchResults.isEmpty()){%>
	<div class="list_content">
		<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_content_text">
			<%
				int displayedLines = 0;
			%>
			<logic:iterate id="info" collection="<%= searchResults %>" type="com.freshdirect.fdstore.customer.FDAuthInfo" indexId="counter">
			<% if ( 
					((globalContextStore).equalsIgnoreCase("All") || (globalContextStore).equals(info.geteStore())) &&
					((globalContextFacility).equalsIgnoreCase("All") || (globalContextFacility).equals(info.getFacility()))
			) { %>
				<tr valign="top" <%= counter.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %> style="cursor: pointer;" onClick="document.location='<%= response.encodeURL("/main/order_details.jsp?orderId=" + info.getSaleId()) %>'">
					<td class="ordCred-spacer"></td>
					<td class="ordCred-ordId"><a href="/main/order_details.jsp?orderId=<%=info.getSaleId()%>"><b><%=info.getSaleId()%></b></a>&nbsp;</td>
					<td class="ordCred-type"><% if(EnumSaleType.REGULAR.equals(info.getOrderType())){%>M<%}else if(EnumSaleType.SUBSCRIPTION.equals(info.getOrderType())){%>A<%}%>&nbsp;</td>
	                <td class="ordCred-dlvDate"><%=CCFormatter.formatDate(info.getDeliveryDate())%>&nbsp;</td>
					<td class="ordCred-transDate"><%=CCFormatter.formatDateTime(info.getTransactionDateTime())%>&nbsp;</td>
					<td class="ordCred-status"><%=info.getSaleStatus().getName()%>&nbsp;</td>
					<td class="ordCred-ordAmt"><%=JspMethods.formatPrice(info.getAuthAmount())%>&nbsp;</td>
					<td class="ordCred-custName"><%=info.getFirstName()%>,&nbsp;<%=info.getLastName()%></td>
					<td class="ordCred-store"><%= "TODO" %></TD>
					<td class="ordCred-facility"><%= "TODO" %></TD>
					<td class="ordCred-cardName"><%=info.getNameOnCard()%>&nbsp;</td>
					<td class="ordCred-action"><i>Auth Code:</i> <%=info.getAuthCode().trim()%>, <i>Message:</i> <%=info.getAuthDescription()%><br><i>CC type:</i> <%=info.getCardType()%>, <i>Last 4</i>: <%=info.getCCLastFourNum()%></td>
					<td class="ordCred-spacer"></td>
				</tr>
				<% displayedLines++;  %>
			<% } %>
			</logic:iterate>
		</table>
		<% if (displayedLines == 0) { %>
			<div class="content_fixed" align="center"><br><br>No matching orders found<br><br><br></div>
		<% } %>
	</div>
<%} else {%>
	<%if("POST".equalsIgnoreCase(request.getMethod()) && result.isSuccess()){%>
		<div class="content_fixed" align="center"><br><br>No matching orders found<br><br><br></div>
	<%}%>
<%}%>
</fd:CrmAuthSearchController>

</tmpl:put>

</tmpl:insert>
