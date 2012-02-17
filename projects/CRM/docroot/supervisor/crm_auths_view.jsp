<%@ page import="java.util.*" %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.content.*" %>
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
    
<tmpl:insert template='/template/top_nav.jsp'>
	<tmpl:put name='title' direct='true'>Authorizations view</tmpl:put>
	<tmpl:put name='content' direct='true'>
	<crm:GetCurrentAgent id="currentAgent">
	<%	CrmAuthSearchCriteria  authFilter =  (CrmAuthSearchCriteria)request.getSession().getAttribute("authSearchCriteria"); 
	
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
		<form method='POST' name="frmAuthFilter" id="frmAuthFilter"  onsubmit="return validateForm();" action="/supervisor/crm_auths_view.jsp">
		
			<crm:CrmAuthSearch id="authList" filter="<%= authFilter %>" result="result">
			
			<div class="BG_live">
		
			<table class="BG_live" width="100%" style="border-bottom:2px solid #000000;border-top:1px solid #000000;">
				
				<tr >
				<td class="promo_page_header_text" colspan='10'>View Authorizations</td>
				
				</tr>
				
				<tr>
					<td >&nbsp; </td>					
					<td >&nbsp; </td>
					<td>Customer Name</td>					
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
				
				<% if(null !=pageContext.getAttribute("authSearchErr")) {%>
				
				<tr >
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
			<div class="errContainer">
					<fd:ErrorHandler result="<%=result%>" name="so_error" id="errorMsg">
						<%@ include file="/includes/i_error_messages.jspf" %>   
					</fd:ErrorHandler>					
			</div>
			<table width="100%" cellspacing="0" border="0" style="empty-cells: show">
			
			<% if(null !=pageContext.getAttribute("successMsg")) {%>
				<tr >
				
				<td colspan='10' width="100%" class="case_content_field"><%= pageContext.getAttribute("successMsg") %></td>
				
				</tr>
				<%} else { %>
				<tr >
				<td colspan='9'>&nbsp;</td>
				</tr>
			<% } %>
			<% if(null!=authList && !authList.isEmpty()){ %>
				<tr> <td colspan="6"><b><%= authList.size()%> record(s) found.</b>&nbsp;&nbsp;</td>				
				</tr>
			<% } else { %>
				<tr >
				<td colspan='9'>&nbsp;</td>
				</tr>
			<% } %>
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
				</table><div class="list_header">
				<table>
				<tr bgcolor="#333366" class="list_header_text">
					
					<td align="left" width="10%">Timestamp</td>					
					<td align="left" width="10%">Order</a></td>						
					<td align="left" width="15%">Customer</td>
					<td align="left" width="5%">Card </td>
					<td align="right" width="5%">Amount</td>					
					<td align="left" width="10%">Merchant</td>	
					<td align="left" width="5%">Approval Code</td>
					<td align="left" width="10%">Auth Response</td>
					<td align="left" width="5%">Zip Match</td>
					<td align="left" width="5%">CVV Match</td>
					<td align="left" width="20%">Address</td>					
					
				</tr>				
			</table>
			</div>
			<div id="result" class="list_content" style="height:76%;">
				<table width="100%" cellspacing="0" border="0" style="empty-cells: show" id="resubmit_orders">
				<% if(authList!=null && authList.isEmpty()){ %>
					<tr valign="top" style="color:#CC0000; font-weight: bold;"><td colspan="11" align="center">No matching standing orders found.</td></tr>
				
				
				<% } else if(authList!=null) {%>
				<logic:iterate id="authInfo" collection="<%= authList %>" type="com.freshdirect.crm.CrmAuthInfo">
					<tr>
						<td class="border_bottom" width="10%" align="left"><%=authInfo.getTransactionTime()%></td>						
						<td class="border_bottom" width="10%" align="left"><%=authInfo.getOrder()%></td>
						<td class="border_bottom" width="15%"><%=authInfo.getCustomerName()%></td>
						<td class="border_bottom" width="5%"><%=authInfo.getCardType()%></td>
						<td class="border_bottom" width="5%" align="right"><%=authInfo.getAmount()%></td>
						<td class="border_bottom" width="10%"><%=authInfo.getMerchantId()%></td>
						<td class="border_bottom" width="5%"><%=authInfo.getApprovalCode()%></td>
						<td class="border_bottom" width="10%"><%=authInfo.getAuthResponse()%></td>
						<td class="border_bottom" width="5%"><%=authInfo.getZipCheckReponse()%></td>
						<td class="border_bottom" width="5%"><%=authInfo.getCvvResponseCode()%></td>
						<td class="border_bottom" width="20%"><%=authInfo.getAddress()%></td>
					</tr>
				</logic:iterate>
				<%}%>
				</table>
			</div>
	</crm:CrmAuthSearch>
	</form>		
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