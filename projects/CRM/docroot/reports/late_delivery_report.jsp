<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>


<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Reports > Late Delivery Orders</tmpl:put>

<tmpl:put name='content' direct='true'>
<%! DateFormatSymbols symbols = new DateFormatSymbols();    %>
<%
    Calendar today = Calendar.getInstance();
    int currmonth = today.get(Calendar.MONTH);
    int currday  = today.get(Calendar.DATE);
    int curryear  = today.get(Calendar.YEAR);
    int month = request.getParameter("month") != null ? Integer.parseInt(request.getParameter("month")) : currmonth;
    int day   = request.getParameter("day") != null ? Integer.parseInt(request.getParameter("day")) : currday;
    int year  = request.getParameter("year") != null ? Integer.parseInt(request.getParameter("year")) : curryear;
    
%>

<%
List lateDlvLines = null;
Calendar cal = Calendar.getInstance();
Calendar cal2 = Calendar.getInstance();
Date dateParam = null;
Date dateParam2 = null;

if ("POST".equals(request.getMethod())) {
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        dateParam = cal.getTime();
     
        lateDlvLines = CallCenterServices.getLateDeliveryReport(dateParam);
}
%>
<jsp:include page="/includes/reports_nav.jsp" />
<div class="sub_nav">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="sub_nav_text">
<form name="latedlv_report" method="post">
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
            alert('The date field is invalid. Please correct and try again.');
            okToSubmit = false;
        }   
        
        if (okToSubmit) {
            thisForm.submit();
        }
    }
	
</script>
<input type="hidden" 
    <tr>
        <td width="25%"><span class="sub_nav_title">Late Delivery Orders <% if (lateDlvLines!= null && lateDlvLines.size()>0) {%>( <span class="result"><%= lateDlvLines.size() %></span> ) <span class="note" style="font-weight:normal;"><input type="checkbox" name="forPrint" onClick="javascript:toggleScroll('result','list_content','content_fixed');"> Print View</span><% } %></span></td>
        <td width="50%" align="center">For: 
        <select name="month" required="true" class="pulldown">
            <option value="">Month</option>
                        <%  for (int i=0; i<12; i++) {  %>
                        <option value="<%= i %>" <%= (i==month)?"selected":"" %>><%= symbols.getShortMonths()[i] %></option>
                        <%  }   %>
        </select>
        <select name="day" required="true" class="pulldown">
            <option value="">Date</option>
                        <%  for (int i=1; i<=31; i++) { %>
            <option value="<%= i %>" <%= (i==day)?"selected":"" %>><%= i %></option>
                        <%  } %>
        </select>
        <select name="year" required="true" class="pulldown">
            <option value="">Year</option>
                        <%  for (int i=2005; i<2016; i++) { %>
            <option value="<%= i %>" <%= (i==year)?"selected":"" %>><%= i %></option>
                        <%  } %>
        </select>
        <input type="submit" class="submit" onClick="javascript:checkForm(latedlv_report); return false;" value="GO">
        </td>
        <td width="25%" align="right"><% if ("POST".equals(request.getMethod()) && lateDlvLines.size() > 0) { %><a href="/reports/late_delivery_report.xls?month=<%=month%>&day=<%=day%>&year=<%=year%>">Export to Excel format</a><%} else {%>Export to Excel format<% } %> &nbsp;|&nbsp; <a href="/reports/index.jsp">All Reports >></a></td>
    </tr>
	<script language"javascript">
	<!--
	function toggleScroll(divId,currentClass,newClass) {
	var divStyle = document.getElementById(divId);
		if (document.latedlv_report.forPrint.checked) {
			divStyle.className = newClass;
		} else {
			divStyle.className = currentClass;
		}
	}
	//-->
	</script>
    </form>
</table>
</div>
<%
  if ("POST".equals(request.getMethod())) {
    if (lateDlvLines.size() > 0) { %>
<div class="list_header">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_header_text">
<tr valign="BOTTOM" >
        <td width="12%" align="left">CASE/COMPLAINT<BR>CREATED</td>
        <td width="8%" align="left">Delivery<br>Window</td>
        <td width="6%" align="left">WAVE<br> NUMBER</td>
        <td width="6%" align="left">TRUCK<br> NUMBER</td>
        <td width="8%" align="left">STOP<br>SEQUENCE</td>
        <td width="6%" align="left">ORDER<br>NUMBER</td>
        <td width="16%" align="left">FIRST NAME</td>
        <td width="18%" align="left">LAST NAME</td>
        <td width="8%" align="left">SOURCE</td>
        <td width="6%" align="left">CHEF'S TABLE</td>
        <td width="6%" align="left">UNDECLARED</td>
    </tr>
</table>
</div>
<div class="list_content" id="result">
    <table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_content_text">
<logic:iterate id="lateDlvLine" collection="<%= lateDlvLines %>" type="com.freshdirect.fdstore.customer.LateDlvReportLine" indexId="counter">

        <tr valign="BOTTOM" >
            <td width="12%" class="border_bottom"><%=CCFormatter.formatDateTime(lateDlvLine.getTimeCaseOpened())%>&nbsp;</td>
            <td width="8%" class="border_bottom"><%=lateDlvLine.getDisplayableStartTime()+" - "+lateDlvLine.getDisplayableEndTime()%></td>
            <td width="6%" class="border_bottom"><%=lateDlvLine.getWaveNumber()!=null? lateDlvLine.getWaveNumber():"&nbsp;"%></td>
            <td width="6%" class="border_bottom"><%=lateDlvLine.getTruckNumber()!=null? lateDlvLine.getTruckNumber():"&nbsp;"%></td>
            <td width="8%" class="border_bottom"><%=lateDlvLine.getStopSequence()!=null ? lateDlvLine.getStopSequence() : "&nbsp;"%></td>
            <td width="6%" class="border_bottom"><a href="/main/order_details.jsp?orderId=<%=lateDlvLine.getOrderNumber()%>"><%=lateDlvLine.getOrderNumber()%></a>&nbsp;</td>
            <td width="16%" class="border_bottom"><%=lateDlvLine.getFirstName()%>&nbsp;</td>
            <td width="18%" class="border_bottom"><%=lateDlvLine.getLastName()%>&nbsp;</td>
            <td width="8%" class="border_bottom"><%=lateDlvLine.getSource()%>&nbsp;</td>
            <td width="6%" class="border_bottom"><%=lateDlvLine.isChefsTable() ? "X" :"&nbsp;"%></td>
            <td width="6%" class="border_bottom"><%=lateDlvLine.isUndeclared() ? "X" :"&nbsp;"%></td>
        </tr>
    </logic:iterate>
    </table>
</div>
<%      } else { %>
	<div class="content_fixed" align="center"><br><b>No Late Deliveries for <%= CCFormatter.formatDate(dateParam) %></b><br><br></div>
<%      }   %>
<%  }   %>

</tmpl:put>

</tmpl:insert>
