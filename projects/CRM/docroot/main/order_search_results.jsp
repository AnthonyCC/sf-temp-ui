<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.delivery.depot.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.crm.CrmAgentRole"%>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.DateFormat"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<%!  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); %>
<%  
    // get search params from request object
    boolean quickSearch = NVL.apply(request.getParameter("search"), "").equalsIgnoreCase("quick");
    boolean sortReversed = "true".equalsIgnoreCase(request.getParameter("reverse"));
    
    FDOrderSearchCriteria sc = new FDOrderSearchCriteria();
    sc.setFirstName(NVL.apply(request.getParameter("firstName"), "").trim());
    sc.setLastName(NVL.apply(request.getParameter("lastName"), "").trim());
    sc.setEmail(NVL.apply(request.getParameter("email"), "").trim());
    sc.setPhone(NVL.apply(request.getParameter("phone"), "").trim());
    sc.setOrderNumber(NVL.apply(request.getParameter("orderNumber"), "").trim());
    sc.setDepotLocationId(request.getParameter("depotLocationId"));
    sc.setCorporate("true".equalsIgnoreCase(request.getParameter("corporate")));
	sc.setChefsTable("true".equalsIgnoreCase(request.getParameter("chefsTable")));
   
    String deliveryDate = NVL.apply(request.getParameter("deliveryDate"),"").trim();
    if(deliveryDate !=""){
        try {
            sc.setDeliveryDate(dateFormat.parse(deliveryDate));
        } catch (Exception e) { }
    }
    
    // construct the search criteria string buffer
	StringBuffer criteriaBuf = new StringBuffer();
    StringBuffer searchQuery = new StringBuffer("?search=").append(NVL.apply(request.getParameter("search"),""));
    
    for(Iterator i = sc.getCriteriaMap().entrySet().iterator(); i.hasNext(); ) {
        Map.Entry e = (Map.Entry) i.next();
        criteriaBuf.append(" ").append(e.getKey()).append(": ").append("<b>").append(e.getValue()).append("</b> | ");
        searchQuery.append("&").append(StringUtils.uncapitalise(StringUtils.deleteWhitespace((String)e.getKey()))).append("=").append(e.getValue());
    }
    
%>

<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Order Search Results</tmpl:put>

<tmpl:put name='content' direct='true'>

<%
boolean isGuest = false;
boolean isExec = false;
String guestView = "&for=print";
%>
<crm:GetCurrentAgent id="currentAgent">
    <% isGuest = currentAgent.isGuest(); %> 
    <% isExec = currentAgent.isExec();  %> 
</crm:GetCurrentAgent>
<% String originalPage = "/main/index.jsp"; %>
<fd:Locator originalPage='<%= originalPage + searchQuery.toString().trim()%>' results='searchResults' searchType='order' searchCriteria='<%=sc%>'>


<%
    //For order cutoff search -- Remove those items which do not have same cutoff time
    
    String cutoffTime = request.getParameter("cutoffTime");
    if(cutoffTime != null && !"".equals(cutoffTime)){
        for(ListIterator li = searchResults.listIterator(); li.hasNext();){
            FDCustomerOrderInfo order = (FDCustomerOrderInfo) li.next();
            if(!order.getCutoffTime().toString().equals(cutoffTime))
                li.remove();
        }
    }
%>
<%
    int ordersOK = 0;
    int ordersCancelled = 0;
    int ordersBad = 0;
    int promoCount = 0;
    for (Iterator oIter = searchResults.iterator(); oIter.hasNext(); ) {
        FDCustomerOrderInfo oInfo = (FDCustomerOrderInfo) oIter.next();
        if (EnumSaleStatus.NOT_SUBMITTED.equals(oInfo.getOrderStatus()) || EnumSaleStatus.AUTHORIZATION_FAILED.equals(oInfo.getOrderStatus())) {
            ordersBad++;
        } else if (EnumSaleStatus.CANCELED.equals(oInfo.getOrderStatus())) {
            ordersCancelled++;
        } else {
            ordersOK++;
        }
    };

%>
<div class="sub_nav">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="sub_nav_text">
<form name="order_search">
    <tr>
        <td width="70%"><span class="sub_nav_title">
            Search Orders: Results</span> 
            ( Total: <span class="result"><%= searchResults.size() %></span> | 
              OK: <span class="result"><%= ordersOK %></span> | 
              Failed: <span class="result"><%= ordersBad %></span> | 
              Cancelled: <span class="result"><%= ordersCancelled %></span> 
            ) 
            <span class="note" style="font-weight: normal;">
            <% if (searchResults.size() > 0) { %>
                <input type="checkbox" name="forPrint" onClick="javascript:toggleScroll('result','list_content','content_fixed');"> 
                    Print View
                </span>
            <% } %>
        </td>
        <td width="30%" align="right"><a href="<%= originalPage %><%=searchQuery.toString().trim()%>">< Refine Search</a> | <a href="<%= originalPage %>">New Search ></a></td>
    </tr>
    <tr><td colspan="2"><img src="/media_stat/crm/images/clear.gif" width="1" height="4"></td></tr>
    <tr>
        <td><%= criteriaBuf.toString() %></td>
        <td align="right" class="note">* Click on underlined column headers to sort</td>
    </tr>
</table>
</div>
<div class="content" style="height: 80%;">
<div class="list_header" style="padding-top: 4px; padding-bottom: 4px;">
<table width="100%" cellpadding="0" cellspacing="2" border="0" class="list_header_text">
    <tr>
		<%
        String link = "order_search_results.jsp?search=";
        link += (quickSearch)?"quick":"advanced";
      
        %>
        <td width="7%"><a href="<%=link%>&compareBy=0<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Order #</a></td>
        <td width="10%"><a href="<%=link%>&compareBy=1<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Delivery Date</a></td>
		<td width="10%"><a href="<%=link%>&compareBy=5<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Type</a></td>
        <td width="10%"><a href="<%=link%>&compareBy=2<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Status</a></td>
        <td width="9%"><a href="<%=link%>&compareBy=3<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Amount</a></td>
		<td><a href="<%=link%>&compareBy=12<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Type</a></td>
        <td width="10%"><a href="<%=link%>&compareBy=4<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Customer Name</a></td>
        <td width="13%"><a href="<%=link%>&compareBy=6<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Email/Username</a></td>
        <td width="10%"><a href="<%=link%>&compareBy=7<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Home Phone #</a></td>
        <td width="9%"><a href="<%=link%>&compareBy=8<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Alt. Phone #</a></td>
        <td width="6%"><a href="<%=link%>&compareBy=13<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Wave</a></td>
        <td width="6%"><a href="<%=link%>&compareBy=9<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Route</a></td>
        <td width="6%"><a href="<%=link%>&compareBy=10<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Stop#</a></td>
        <td width="10%"><a href="<%=link%>&compareBy=11<%= (!sortReversed) ? "&reverse=true" : "" %>" class="list_header_text">Payment</a></td>
        <td><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>
    </tr>
</table>
</div>
<%  if (searchResults.size() > 0) { 
        // if only one results returned, redirect to order details page
        if (searchResults.size() == 1) {
            FDCustomerOrderInfo info = (FDCustomerOrderInfo) searchResults.iterator().next();
            response.sendRedirect( response.encodeRedirectURL("/main/summary.jsp?orderId="+ info.getSaleId() + (isGuest ? guestView : "")) );
        }
%>      
<%  Comparator comparator = null;
    String compareByParam = request.getParameter("compareBy");
    int compareBy = 0;
    if (compareByParam != null) {
        compareBy = Integer.parseInt(compareByParam);
    }
    switch (compareBy) {
        case 0:
            comparator = FDCustomerOrderInfo.SaleIdComparator;
            break;
        case 1:
            comparator = FDCustomerOrderInfo.DeliveryDateComparator;
            break;
        case 2:
            comparator = FDCustomerOrderInfo.OrderStatusComparator;
            break;
        case 3:
            comparator = FDCustomerOrderInfo.AmountComparator;
            break;
        case 4:
            comparator = FDCustomerOrderInfo.CustomerNameComparator;
            break;
        case 5:
            comparator = FDCustomerOrderInfo.DeliveryTypeComparator;
            break;
        case 6:
            comparator = FDCustomerOrderInfo.EmailComparator;
            break;
        case 7:
            comparator = FDCustomerOrderInfo.PhoneComparator;
            break;
        case 8:
            comparator = FDCustomerOrderInfo.AltPhoneComparator;
            break;
        case 9:
            comparator = FDCustomerOrderInfo.RouteNumComparator;
            break;
        case 10:
            comparator = FDCustomerOrderInfo.StopSequenceComparator;
            break;                           
        case 11:
            comparator = FDCustomerOrderInfo.PaymentTypeComparator;
            break;  
		case 12:
            comparator = FDCustomerOrderInfo.CustomerTypeComparator;
            break;  
        case 13:
            comparator = FDCustomerOrderInfo.WaveNumComparator;
            break;
        default:
            comparator = FDCustomerOrderInfo.SaleIdComparator;
            break;
    }
    
    Collections.sort(searchResults, comparator);
    if (sortReversed) Collections.reverse(searchResults);

    FDCustomerOrderInfo nextInfo = null;
    FDCustomerOrderInfo lastInfo = null;
%>

<div id="result" class="list_content" style="height: 95%;">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_content_text">
    <logic:iterate id="info" collection="<%= searchResults %>" type="com.freshdirect.fdstore.customer.FDCustomerOrderInfo" indexId="counter">
    <%
        String rowClass = counter.intValue() % 2 == 0 ? "class='list_odd_row'" : "";
        if (comparator.equals(FDCustomerOrderInfo.EmailComparator)) {
            if (counter.intValue() > 0) lastInfo = (FDCustomerOrderInfo) searchResults.get(counter.intValue()-1); else lastInfo = null;
            if (counter.intValue() < (searchResults.size()-1)) nextInfo = (FDCustomerOrderInfo) searchResults.get(counter.intValue()+1); else nextInfo = null;
            if (  (lastInfo != null) && info.getIdentity().getErpCustomerPK().equals(lastInfo.getIdentity().getErpCustomerPK()) )
                rowClass = "class='list_duplicate_row'";
            else if ((nextInfo != null) && info.getIdentity().getErpCustomerPK().equals(nextInfo.getIdentity().getErpCustomerPK()))
                rowClass = "class='list_duplicate_row'";
        }
    %>
        <tr valign="top" <%=rowClass%> style="cursor: pointer;" onClick="document.location='<%= response.encodeURL("/main/order_details.jsp?orderId=" + info.getSaleId() + (isGuest ? guestView : "") ) %>'">
			<td width="7%" class="border_bottom"><a><b><%= info.getSaleId() %></b></a>&nbsp;</td>
            <td width="10%" class="border_bottom"><%= info.getDeliveryDate() %>&nbsp;</td>
			<td width="10%" class="border_bottom"><%= EnumDeliveryType.getDeliveryType(info.getDeliveryType()).getName() %>&nbsp;</td>
            <td width="10%" class="border_bottom"><%= (info.getOrderStatus() != null) ? info.getOrderStatus().getName() : "--" %></td>
            <td width="9%" class="border_bottom"><%= CCFormatter.formatCurrency(info.getAmount()) %>&nbsp;</td>
            <td width="1%" class="border_bottom" style="font-size: 5pt;">
                <%=info.isVIP()?"<div class=\"iVip\">V I P</div> ":""%>
                <%=info.isVIP() || info.isChefsTable() ? "" : "&nbsp;" %>
                <%=info.isChefsTable()?"<div class=\"iChef\">CHEF</div>":""%>
            </td>
			<td width="10%" class="border_bottom"><%= info.getLastName() %>,&nbsp;<%= info.getFirstName() %></td>
            <td width="13%" class="border_bottom"><%= info.getEmail() %>&nbsp;</td>
            <td width="10%" class="border_bottom"><%= info.getPhone() %>&nbsp;</td>
            <td width="9%" class="border_bottom"><%= info.getAltPhone() %>&nbsp;</td>
            <td width="6%" class="border_bottom"><%= info.getWaveNum() %>&nbsp;</td>
            <td width="6%" class="border_bottom"><%= info.getRouteNum() %>&nbsp;</td>
            <td width="6%" class="border_bottom"><%= info.getStopSequence() %>&nbsp;</td>
            <td width="10%" class="border_bottom" style="color: red;">
                <%= (info.getPaymentType()==null || EnumPaymentType.REGULAR.equals(info.getPaymentType())) ?  "" : info.getPaymentType().getDescription() %>&nbsp;
            </td>
        </tr>
    </logic:iterate>
<%  } %>
</form>
</TABLE>
</div>
</div>
<script language"javascript">
	<!--
	function toggleScroll(divId,currentClass,newClass) {
	var divStyle = document.getElementById(divId);
		if (document.order_search.forPrint.checked) {
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
