<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.delivery.depot.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.crm.CrmAgentRole"%>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<%
    boolean sortReversed = "true".equalsIgnoreCase(request.getParameter("reverse"));
    String search = request.getParameter("search");
    
    // get search params from request object
    FDCustomerSearchCriteria sc = new FDCustomerSearchCriteria();
	sc.setFirstName(NVL.apply(request.getParameter("firstName"), "").trim());
	sc.setLastName(NVL.apply(request.getParameter("lastName"), "").trim());
	sc.setEmail(NVL.apply(request.getParameter("email"), "").trim());
	sc.setPhone(NVL.apply(request.getParameter("phone"), "").trim());
	sc.setOrderNumber(NVL.apply(request.getParameter("orderNumber"), "").trim());
    sc.setAddress(NVL.apply(request.getParameter("address"), "").trim());
	sc.setZipCode(NVL.apply(request.getParameter("zipCode"), "").trim());
	sc.setDepotCode(NVL.apply(request.getParameter("depotCode"), "").trim());
    sc.setApartment(NVL.apply(request.getParameter("apartment"), "").trim());

	// construct the search criteria string buffer
	StringBuffer criteriaBuf = new StringBuffer();
    StringBuffer searchQuery = new StringBuffer("?search=").append(search);
	
	for(Iterator i = sc.getCriteriaMap().entrySet().iterator(); i.hasNext(); ) {
        Map.Entry e = (Map.Entry) i.next();
        criteriaBuf.append(" ").append(e.getKey()).append(": ").append("<b>").append(e.getValue()).append("</b> | ");
        searchQuery.append("&").append(StringUtils.uncapitalise(StringUtils.deleteWhitespace((String)e.getKey()))).append("=").append(e.getValue());
    }
%>

<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Customer Search Results</tmpl:put>
    
    <tmpl:put name='content' direct='true'>
<%
boolean isGuest = false; 
%>
<crm:GetCurrentAgent id="currentAgent">
	<%-- isGuest = currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.GUE_CODE)); --%> 
</crm:GetCurrentAgent>
<% String originalPage = "/main/main_index.jsp"; %>
<fd:Locator originalPage='<%= originalPage %>' results='searchResults' searchType='customer' searchCriteria='<%=sc%>'>
<%
        // if only one results returned, redirect to customer details page
		if (searchResults.size() == 1 && !isGuest) {
			FDCustomerOrderInfo loneCustomer = (FDCustomerOrderInfo) searchResults.iterator().next();
			response.sendRedirect( response.encodeRedirectURL("/main/summary.jsp?erpCustId=" + loneCustomer.getIdentity().getErpCustomerPK()));
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
            <% if (!sc.isBlank()) { %>
                <a href="<%=originalPage%><%=searchQuery.toString().trim()%>">< Refine Search</a> | 
            <% } %>
            <a href="<%=originalPage%>">New Search ></A></td>
	</tr>
	<tr>
		<td colspan="2"><img src="/media_stat/crm/images/clear.gif" width="1" height="4"><br><%= criteriaBuf.toString() %></td>
	</tr>
</table>
</div>

<div class="content" style="height: 80%;">
<div class="list_header" style="padding-top: 4px; padding-bottom: 4px;">
<table width="100%" cellpadding="0" cellspacing="2" border="0" class="list_header_text">
	<tr>
		<td width="15%"><a href="customer_search_results.jsp?compareBy=7<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Type</a></td>
		<td width="25%"><a href="/main/customer_search_results.jsp?compareBy=0<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Customer Name</a></td>
		<td width="25%"><a href="/main/customer_search_results.jsp?compareBy=2<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Email/Username</a></td>
		<td width="15%"><a href="/main/customer_search_results.jsp?compareBy=3<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Home Phone #</a></td>
		<td width="15%"><a href="/main/customer_search_results.jsp?compareBy=4<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Alt. Phone #</a></td>
		<td width="5%"></td>
	</tr>
</table>
</div>

<%	if (searchResults.size() > 0) {

		
%>
<%	Comparator comparator = null;
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
			comparator = FDCustomerOrderInfo.CustomerIdComparator;
			break;
		case 2:
			comparator = FDCustomerOrderInfo.EmailComparator;
			break;
		case 3:
			comparator = FDCustomerOrderInfo.PhoneComparator;
			break;
		case 4:
			comparator = FDCustomerOrderInfo.AltPhoneComparator;
			break;
		case 5:
			comparator = FDCustomerOrderInfo.DeliveryDateComparator;
			break;
		case 6:
			comparator = FDCustomerOrderInfo.OrderStatusComparator;
			break;
		case 7:
			comparator = FDCustomerOrderInfo.CustomerTypeComparator;
			break;
		default:
			comparator = FDCustomerOrderInfo.CustomerIdComparator;
			break;
	}

	Collections.sort(searchResults, comparator);
	if (sortReversed)
		Collections.reverse(searchResults);
%>
<div id="result" class="list_content" style="height: 95%;">
	<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_content_text">
	<logic:iterate id="info" collection="<%= searchResults %>" type="com.freshdirect.fdstore.customer.FDCustomerOrderInfo" indexId="counter">
		<tr valign="top" 
            <%= counter.intValue() % 2 == 0 ? "class='list_odd_row'" : "" %>  style="cursor: pointer;" 
            <% if (!isGuest) { %>
                onClick="document.location='<%= response.encodeURL("/main/summary.jsp?erpCustId=" + info.getIdentity().getErpCustomerPK() + "&fdCustId=" + info.getIdentity().getFDCustomerPK() ) %>'"
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

</fd:Locator>

</tmpl:put>

</tmpl:insert>