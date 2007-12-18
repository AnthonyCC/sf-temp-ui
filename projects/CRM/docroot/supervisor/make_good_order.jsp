<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Make Good Orders</tmpl:put>

<tmpl:put name='content' direct='true'>

<%! DateFormatSymbols symbols = new DateFormatSymbols(); %>
<%
    Calendar today = Calendar.getInstance();
    int currmonth = today.get(Calendar.MONTH);
    int currday  = today.get(Calendar.DATE);
    int curryear  = today.get(Calendar.YEAR);
    int month = request.getParameter("month") != null ? Integer.parseInt(request.getParameter("month")) : currmonth;
    int day   = request.getParameter("day") != null ? Integer.parseInt(request.getParameter("day")) : currday;
    int year  = request.getParameter("year") != null ? Integer.parseInt(request.getParameter("year")) : curryear;
    
    List searchResults = null;
    Calendar cal = Calendar.getInstance();
    Date dateParam = null;
    
    if ("POST".equals(request.getMethod())) {
            cal.set(Calendar.DAY_OF_MONTH, day);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.YEAR, year);
            dateParam = cal.getTime();
         
            searchResults = CallCenterServices.getMakeGoodOrder(dateParam);
    }
%>

<jsp:include page="/includes/supervisor_nav.jsp" />
<div class="sub_nav">
<span class="sub_nav_title">
<table width="100%" border="0" cellpadding="0" cellspacing="0"><form name="makeGoodOrder" method="post">
<script language="JavaScript">
    function checkForm(thisForm) {
        var okToSubmit= true;
        var day = thisForm.day.value;
        var month = thisForm.month.value;
        var year = thisForm.year.value;
        
        if (day.length<2) day="0"+day;
        if (month.length<2) month="0"+month;
        var date1=year+month+day;
        
        if (date1.length!=8 ) {
            alert('The DATE field is invalid. Please correct and try again.');
            okToSubmit = false;
        }
        
        if (okToSubmit) {
            thisForm.submit();
        }
    }
</script>
<tr><td width="25%" class="sub_nav_title">Make Good Order<%=searchResults!=null && searchResults.size() > 1?"s":""%> ( <span class="result"><% if (searchResults!=null) { %><%=searchResults.size()%><%}else{%>0<%}%></span> )</td>
<td width="50%" align="center">Delivery date: 
<select name="month" required="true" class="pulldown">
                <option value="">Month</option>
                            <%  for (int i=0; i<12; i++) {  %>
                            <option value="<%= i %>" <%= (i==month)?"selected":"" %>><%= symbols.getShortMonths()[i] %></option>
                            <%  }   %>
            </select>
            <select name="day" required="true" class="pulldown">
                <option value="">Day</option>
                            <%  for (int i=1; i<=31; i++) { %>
                <option value="<%= i %>" <%= (i==day)?"selected":"" %>><%= i %></option>
                            <%  } %>
            </select>
            <select name="year" required="true" class="pulldown">
                <option value="">Year</option>
                            <%  for (int i=2001; i<2011; i++) { %>
                <option value="<%= i %>" <%= (i==year)?"selected":"" %>><%= i %></option>
                            <%  } %>
            </select>
			
			<input type="submit" class="submit" value="GO" onClick="javascript:checkForm(makeGoodOrder); return false;">
			</td><td class="25%">&nbsp;</td></tr></form></table></span>
</div>

<div class="content" style="height: 80%;">
	<div class="list_header" style="padding-top: 4px; padding-bottom: 4px;">
	<TABLE WIDTH="100%" CELLPADDING="2" CELLSPACING="0" BORDER="0" class="list_header_text">
	    <TR>
			<TD WIDTH="10%">Order #</TD>
			<TD WIDTH="10%">Delivery</TD>
			<TD WIDTH="10%">Placed</TD>
			<TD WIDTH="15%">Status</TD>
			<TD WIDTH="15%">Amount</TD>
			<TD WIDTH="20%">Customer Name</TD>
			<TD WIDTH="10%">Route</TD>
			<TD WIDTH="10%">Stop</TD>
		</TR>
	</table>
	</div>
	<div class="list_content" style="height: 95%;">
	<TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0" class="list_content_text">
	<%	if (searchResults!=null && searchResults.size() > 0) { %>
		<logic:iterate id="info" collection="<%= searchResults %>" type="com.freshdirect.fdstore.customer.MakeGoodOrderInfo" indexId="counter">
	    <%
	        String bgcolor = (counter.intValue() % 2 == 0) ? "#EEEEEE" : "#FFFFFF";
	    %>
		<TR VALIGN="BOTTOM" BGCOLOR="<%= bgcolor %>" style="cursor: pointer; padding: 2px;" onClick="document.location='/main/order_details.jsp?orderId=<%= info.getSaleId() %>'">
			<TD width="10%" class="border_bottom"><a href="/main/order_details.jsp?orderId=<%= info.getSaleId() %>"><%= info.getSaleId() %></a>&nbsp;</TD>
			<TD width="10%"  class="border_bottom"><%= CCFormatter.formatDate(info.getDeliveryDate()) %>&nbsp;</TD>
			<TD width="10%"  class="border_bottom"><%= CCFormatter.formatDate(info.getOrderPlacedDate()) %>&nbsp;</TD>
			<TD width="15%"  class="border_bottom"><%= (info.getSaleStatus() != null) ? info.getSaleStatus().getName() : "--" %></TD>
			<TD width="15%"  class="border_bottom"><%= CCFormatter.formatCurrency(info.getAmount()) %>&nbsp;</TD>
			<TD width="20%"  class="border_bottom"><%= info.getLastName() %>, <%= info.getFirstName() %></TD>
			<TD width="10%"  class="border_bottom"><%= info.getRoute() %>&nbsp;</TD>
			<TD width="10%"  class="border_bottom"><%= info.getStop() %>&nbsp;</TD>
			</TR>
		</logic:iterate>
	<% 	} else if ("POST".equals(request.getMethod()) && searchResults.isEmpty()) { %>
	    <tr><td colspan="6" align="center"><br><br><b>no Make Good Orders found for delivery on this day</b></td></tr>
	<% } %>
	</TABLE>
	</div>
</div>
</tmpl:put>

</tmpl:insert>
