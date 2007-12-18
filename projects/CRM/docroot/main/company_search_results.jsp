<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerOrderInfo"%>
<%@ page import="com.freshdirect.crm.CrmAgentRole"%>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<%
    String sortLink = "/main/company_search_results.jsp?companyName="+NVL.apply(request.getParameter("companyName"),"").trim();
    boolean sortReversed = "true".equalsIgnoreCase(request.getParameter("reverse"));
    sortLink += (!sortReversed) ? "&reverse=true" : "";
    
%>

<tmpl:insert template='/template/top_nav.jsp'>
    <tmpl:put name='title' direct='true'>Customer Search Results</tmpl:put>
    <tmpl:put name='content' direct='true'>
<%
    boolean isGuest = false; 
    String originalPage = "/main/index.jsp";
%>
<crm:GetCurrentAgent id="currentAgent">
    <%  isGuest = currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.GUE_CODE));  %> 
</crm:GetCurrentAgent>
<crm:GenericLocator id="searchResults" searchParam='COMPANY_SEARCH' result="result">
	<fd:ErrorHandler result='<%= result %>' name='inputerror' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	<fd:ErrorHandler result='<%= result %>' name='searchfailure' id='errorMsg'>
	   <%@ include file="/includes/i_error_messages.jspf" %>   
	</fd:ErrorHandler>
	
	<logic:present name ='searchResults'>
		<%
		    // if only one results returned, redirect to customer details page
		    if(searchResults != null) {
			if (searchResults.size() == 1 && !isGuest) {
			    FDCustomerOrderInfo loneCustomer = (FDCustomerOrderInfo) searchResults.iterator().next();
			    response.sendRedirect( response.encodeRedirectURL("/main/account_details.jsp?erpCustId=" + loneCustomer.getIdentity().getErpCustomerPK()));
			}

		%>
		<div class="sub_nav">
		<table width="100%" cellpadding="0" cellspacing="0" border="0" class="sub_nav_text">
		<form name="customer_search">
		    <tr>
			<td width="70%">
			    <span class="sub_nav_title">
				Search Customers: Results
			    </span> 
			    ( <span class="result"><%= searchResults.size() %></span> )
			    <% if (searchResults.size() > 0) { %>
				<input type="checkbox" name="forPrint" onClick="javascript:toggleScroll('result','list_content','content_fixed');"> Print View</span>
			    <% } %> 
			    &nbsp;<span class="note">* Click on underlined column headers to sort</span>
			</td>
			<td width="30%" align="right">
			    <a href="<%=originalPage%>">< Refine Search</a> | 
			    <a href="<%=originalPage%>">New Search ></A></td>
		    </tr>
		    <tr>
			<td colspan="2"><img src="/media_stat/crm/images/clear.gif" width="1" height="4"><br></td>
		    </tr>
		</table>
		</div>

		<div class="content" style="height: 80%;">
		<div class="list_header" style="padding-top: 4px; padding-bottom: 4px;">
		<table width="100%" cellpadding="0" cellspacing="2" border="0" class="list_header_text">
		    <tr>
			<td width="15%"><a href="<%=sortLink%>&compareBy=4" class="list_header_text">Type</a></td>
			<td width="25%"><a href="<%=sortLink%>&compareBy=0" class="list_header_text">Customer Name</a></td>
			<td width="25%"><a href="<%=sortLink%>&compareBy=1" class="list_header_text">Email/Username</a></td>
			<td width="15%"><a href="<%=sortLink%>&compareBy=2" class="list_header_text">Home Phone #</a></td>
			<td width="15%"><a href="<%=sortLink%>&compareBy=3" class="list_header_text">Alt. Phone #</a></td>
			<td width="5%"></td>
			</tr>
		</table>
		</div>

		<%
		    if (searchResults.size() > 0) {
			Comparator comparator = null;
			String compareByParam = request.getParameter("compareBy");
			int compareBy = 0;
			if (compareByParam != null) {
			    compareBy = Integer.parseInt(compareByParam);
			}
			switch (compareBy) {
			    case 0:
				comparator = FDCustomerOrderInfo.CustomerNameComparator;
				break;
			    case 1:
				comparator = FDCustomerOrderInfo.EmailComparator;
				break;
			    case 2:
				comparator = FDCustomerOrderInfo.PhoneComparator;
				break;
			    case 3:
				comparator = FDCustomerOrderInfo.AltPhoneComparator;
				break;
			    case 4:
				comparator = FDCustomerOrderInfo.CustomerTypeComparator;
				break;
			    default:
				comparator = FDCustomerOrderInfo.CustomerIdComparator;
				break;
			}

			Collections.sort(searchResults, comparator);
			if (sortReversed) {
			    Collections.reverse(searchResults);
			}
		%>
		<div id="result" class="list_content" style="height: 95%;">
		    <table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_content_text">
		    <logic:iterate id="info" collection="<%= searchResults %>" type="com.freshdirect.fdstore.customer.FDCustomerOrderInfo" indexId="counter">
			<tr valign="top" 
			    <%= counter.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %>  style="cursor: pointer;" 
			    <% if (!isGuest) { %>
				onClick="document.location='<%= response.encodeURL("/main/account_details.jsp?erpCustId=" + info.getIdentity().getErpCustomerPK() + "&fdCustId=" + info.getIdentity().getFDCustomerPK() ) %>'"
			    <% } %>>
			    <td width="15%" class="border_bottom" style="font-size: 5pt;">
				<%=info.isVIP()?"<span class=\"iVip\">V I P</span> ":""%>
				<%=info.isVIP() || info.isChefsTable() ? "" : "&nbsp;" %>
				<%=info.isChefsTable()?"<span class=\"iChef\">CHEF</span>":""%>
			    </td>
			    <td width="25%" class="border_bottom">
				<% if (!isGuest) { %>
				    <a>
				<% } %>
				<%= info.getLastName() %>, <%= info.getFirstName() %>
				<% if (!isGuest) { %>
				    </a>
				<% } %>&nbsp;

			    </td>
			    <td width="25%" class="border_bottom"><%= info.getEmail() %>&nbsp;</td>
			    <td width="15%" class="border_bottom"><%= info.getPhone() %>&nbsp;</td>
			    <td width="15%" class="border_bottom"><%= info.getAltPhone() %>&nbsp;</td>
			    <td width="5%" class="border_bottom">&nbsp;</td>
			</tr>
		    </logic:iterate>
		    </table>
		</div>
		<% 	} %>
		</form>
		</div>
		<script language"javascript">
			<!--
			function toggleScroll(divId,currentClass,newClass) {
			var divStyle = document.getElementById(divId);
				if (document.customer_search.forPrint.checked) {
					divStyle.className = newClass;
				} else {
					divStyle.className = currentClass;
				}
			}
			//-->
			</script>
		<% } %>
	</logic:present>	
</crm:GenericLocator>
</tmpl:put>
</tmpl:insert>