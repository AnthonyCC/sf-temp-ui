<%@ page import="java.util.*" %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.storeapi.content.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.crm.*" %>
<%@ page import="com.freshdirect.crm.CrmAuthSearchCriteria" %>
<%@ page import="com.freshdirect.crm.CrmAuthInfo" %>
<%@ page import="com.freshdirect.framework.util.DateUtil" %>
<%@ page import='com.freshdirect.framework.util.*'%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<% boolean isGuest = false;
%>
    
<tmpl:insert template='/template/supervisor_resources.jsp'>

	<tmpl:put name='title' direct='true'>Supervisor Resources > Authorizations view</tmpl:put>
	<tmpl:put name='content' direct='true'>
<style type="text/css">
.case_content_red_field {
color: #CC0000;
font-weight: bold;
font-size: 10pt;
}
</style>
<script type="text/javascript">
function validateForm()
{   
       if(!isNumber(document.frmAuthFilter.amount.value) && document.frmAuthFilter.amount.value!="" ) {
		alert(" Please enter a valid amount ");
		return false;
       }
	if(document.frmAuthFilter.authFromDate.value=="" ||document.frmAuthFilter.authToDate.value=="") {
		alert(" Please select From and To dates to search for authorizations");
		return false;
	} 
}
function isNumber(n) {
  return !isNaN(parseFloat(n)) && isFinite(n);
}
</script>
	<crm:GetCurrentAgent id="currentAgent">
	<%
		CrmAuthSearchCriteria  authFilter =  (CrmAuthSearchCriteria)request.getSession().getAttribute("authSearchCriteria"); 
	
		Date fromDate=null;
		String fromDateStr=null;
		Date toDate=null;
		String toDateStr=null;
		String customerName=null;
		String amount=null;
	
		if(null == authFilter || authFilter.isEmpty()){
			authFilter = new CrmAuthSearchCriteria();
			
		}
		if(null !=request.getParameter("auth_search_submit")){
					
			fromDateStr = request.getParameter("authFromDate");
			toDateStr = request.getParameter("authToDate");
			customerName=request.getParameter("customerName");
			amount=request.getParameter("amount");
			authFilter = new CrmAuthSearchCriteria();
			authFilter.setFromDateStr(fromDateStr);
			authFilter.setToDateStr(toDateStr);
			authFilter.setCustomerName(customerName);
			authFilter.setAmount(amount);
		}else if(null != authFilter && !authFilter.isEmpty()){
			
			fromDateStr = authFilter.getFromDateStr();
			toDateStr = authFilter.getToDateStr();
			customerName=authFilter.getCustomerName();
			amount=authFilter.getAmount();
		}
	%>
	<crm:CrmAuthSearch id="authList" filter="<%= authFilter %>" result="result">
	<%!
		private final static Map<String,Comparator<CrmAuthInfo>> AUTH_COMPARATORS = new HashMap<String,Comparator<CrmAuthInfo>>();
		static {
			AUTH_COMPARATORS.put("amount", CrmAuthInfo.COMP_AMOUNT);
			AUTH_COMPARATORS.put("customer", CrmAuthInfo.COMP_CUSTOMER_NAME);
			AUTH_COMPARATORS.put("transactionTime", CrmAuthInfo.COMP_TRANSACTION_TIME);
			
		}
	%>
	<%
		JspTableSorter sort = new JspTableSorter(request);
		Comparator<CrmAuthInfo> comp = AUTH_COMPARATORS.get(sort.getSortBy());
		if(null != authList){
			if (comp == null) {
				Collections.sort(authList, new ReverseComparator<CrmAuthInfo>(CrmAuthInfo.COMP_TRANSACTION_TIME));
			} else {
				Collections.sort(authList, sort.isAscending() ? comp : new ReverseComparator<CrmAuthInfo>(comp));					
			}
		}
	%>		
	<form method='POST' name="frmAuthFilter" id="frmAuthFilter"  onsubmit="return validateForm();" action="/supervisor/crm_auths_view.jsp">
		<div class="BG_live">
			<table class="BG_live" width="100%" style="border-bottom:2px solid #000000;border-top:1px solid #000000;">
				<tr >
					<td class="promo_page_header_text" colspan='10'>View Authorizations</td>
				</tr>
				<tr>
					<td >&nbsp; </td>					
					<td >&nbsp; </td>
					<td>Card holder Name</td>					
					<td><input type="text" id="customerName" value="<%= customerName %>" name="customerName" class="w300px" />&nbsp;</td>
					<td>Amount</td>
					<td><input type="text" id="amount" value="<%= amount %>" name="amount" class="w100px" />&nbsp;</td>
					<td> From</td><td><input type="text" id="authFromDate" value="<%= fromDateStr %>" name="authFromDate" class="w100px" /> <a href="#" onclick="return false;" class="promo_ico_cont" id="authFromDate_trigger" name="authFromDate_trigger"><img src="/media_stat/crm/images/calendar.gif" width="16" height="16" alt="" /></a></td>
					<td>To</td><td><input type="text" id="authToDate" value="<%= toDateStr %>" name="authToDate" class="w100px" /> <a href="#" onclick="return false;" class="promo_ico_cont" id="authToDate_trigger" name="authToDate_trigger"><img src="/media_stat/crm/images/calendar.gif" width="16" height="16" alt="" /></a></td>
					<td>&nbsp;</td>
					<td><input type="submit" value="Search"  id="auth_search_submit" name="auth_search_submit" class="promo_btn_grn" /></td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>			
				</tr>	
				
				<% if (null !=pageContext.getAttribute("authSearchErr")) { %>
					<tr>
						<td colspan='1'>&nbsp;</td>
						<td colspan='9' class="case_content_red_field"><%= pageContext.getAttribute("authSearchErr") %></td>
					</tr>
				<%} else { %>
					<tr >
						<td colspan='10'>&nbsp;</td>
					</tr>
				<% } %>
			</table>
		</div>
		<fd:ErrorHandler result="<%=result%>" name="so_error" id="errorMsg">
			<div class="errContainer">
				<%@ include file="/includes/i_error_messages.jspf" %>   
			</div>
		</fd:ErrorHandler>
		<% if (null !=pageContext.getAttribute("successMsg") && null!=authList && !authList.isEmpty()) { %>
			<table width="100%" cellspacing="0" border="0">
				<% if (null !=pageContext.getAttribute("successMsg")) {%>
					<tr >
						<td colspan="10" width="100%" class="case_content_field"><%= pageContext.getAttribute("successMsg") %></td>
					</tr>
					<%} else { %>
					<% } %>
				<% if (null!=authList && !authList.isEmpty()){ %>
					<tr>
						<td colspan="6"><b><%= authList.size()%> record(s) found.</b>&nbsp;&nbsp;</td>				
					</tr>
				<% } else { %>
				<% } %>
			</table>
		<% } %>	
		<div class="list_header">
			<table>
				<tr bgcolor="#333366" class="list_header_text">
					<td class="resubcust-spacer"  >&nbsp;</td>
					<td class="authView-Timestamp" >Timestamp</td>					
					<td class="authView-Order">Order</a></td>						
					<td class="authView-CardHoldName">Card holder Name</td>
					<td class="authView-Card">Card </td>
					<td class="authView-Amount">Amount</td>					
					<td class="authView-Merchant">Merchant</td>	
					<td class="authView-Approval">Approval Code</td>
					<td class="authView-Auth">Auth Response</td>
					<td class="authView-Zip">Zip Match</td>
					<td class="authView-CVV">CVV Match</td>
					<td class="authView-Address">Address</td>					
					<td class="resubcust-spacer-auto" >&nbsp;</td>
				</tr>				
			</table>
		</div>
		<div id="result" class="list_content" style="height:76%;">
			<table width="100%" cellspacing="0" border="0" style="empty-cells: show" id="resubmit_orders">
			<% if(authList!=null && authList.isEmpty()){ %>
				<tr valign="top" style="color:#CC0000; font-weight: bold;"><td colspan="11" align="center">No matching standing orders found.</td></tr>
			
			
			<% } else if(authList!=null) {%>
			<logic:iterate id="authInfo" collection="<%= authList %>" type="com.freshdirect.crm.CrmAuthInfo" indexId="counter">
			        <% boolean isValidOrder=authInfo.isValidOrder();
				if(isValidOrder){%>
				<tr  <%= counter.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %> style="cursor: pointer; padding: 2px;" onClick="document.location='/main/order_details.jsp?orderId=<%=authInfo.getWebOrder()%>'">
				<%} else if(!"".equals(authInfo.getCustomerId())) {%>
				<tr <%= counter.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %>style="cursor: pointer; padding: 2px;" onClick="document.location='/main/account_details.jsp?erpCustId=<%=authInfo.getCustomerId()%>'">
				<%} else {%>
				<tr <%= counter.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %>>
				<%}%>
					<td class="resubcust-spacer border_bottom"  >&nbsp;</td>
					<td class="authView-Timestamp border_bottom" ><%=authInfo.getTransactionTime()%></td>						
					<td class="authView-Order border_bottom" ><% if(isValidOrder) {%><%=authInfo.getOrder()%> <%} else {%><%=authInfo.getOrder()%><%}%></td>
					<td class="authView-CardHoldName border_bottom" ><%=authInfo.getCustomerName()%></td>
					<td class="authView-Card border_bottom" ><%=authInfo.getCardType()%></td>
					<td class="authView-Amount border_bottom" ><%=authInfo.getAmount()%></td>
					<td class="authView-Merchant border_bottom" ><%=authInfo.getMerchantId()%></td>
					<td class="authView-Approval border_bottom" ><%=authInfo.getApprovalCode()%></td>
					<td class="authView-Auth border_bottom" ><%=authInfo.getAuthResponse()%></td>
					<td class="authView-Zip border_bottom" ><%=authInfo.getZipCheckReponse()%></td>
					<td class="authView-CVV border_bottom" ><%=authInfo.getCvvResponseCode()%></td>
					<td class="authView-Address border_bottom" ><%=authInfo.getAddress()%></td>
					<td class="resubcust-spacer-auto border_bottom" >&nbsp;</td>
										
				</tr>
			</logic:iterate>
			<%}%>
			</table>
		</div>
	</form>	
	</crm:CrmAuthSearch>	
	</crm:GetCurrentAgent>	
	</tmpl:put>
</tmpl:insert>


<script language="javascript">
Calendar.setup(
		{
			showsTime : false,
			electric : false,
			inputField : "authFromDate",
			ifFormat : "%m/%d/%Y",
			singleClick: true,
			button : "authFromDate_trigger"
		}
	);
Calendar.setup(
		{
			showsTime : false,
			electric : false,
			inputField : "authToDate",
			ifFormat : "%m/%d/%Y",
			singleClick: true,
			button : "authToDate_trigger"
		}
	);
</script>