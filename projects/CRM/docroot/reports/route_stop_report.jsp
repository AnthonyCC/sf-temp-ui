<%@ taglib uri='template' prefix='tmpl' %><%@ taglib uri='logic' prefix='logic' %><%@ taglib uri='freshdirect' prefix='fd' %><%

    Calendar today = Calendar.getInstance();
    int currmonth = today.get(Calendar.MONTH);
    int currday  = today.get(Calendar.DATE);
    int curryear  = today.get(Calendar.YEAR);
    int month = request.getParameter("month") != null ? Integer.parseInt(request.getParameter("month")) : currmonth;
    int day   = request.getParameter("day") != null ? Integer.parseInt(request.getParameter("day")) : currday;
    int year  = request.getParameter("year") != null ? Integer.parseInt(request.getParameter("year")) : curryear;
    String wave = request.getParameter("wave") != null ? request.getParameter("wave") : " ";
    String route = request.getParameter("route") != null ? request.getParameter("route") : " ";
    String stop1 = request.getParameter("stop1") != null ? request.getParameter("stop1") : " ";
    String stop2= request.getParameter("stop2") != null ? request.getParameter("stop2") : " ";
    
    List routeStopLines = null;
    Calendar cal = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();
    Date dateParam = null;
    Date dateParam2 = null;
    
    if ("POST".equals(request.getMethod())) {
            cal.set(Calendar.DAY_OF_MONTH, day);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.YEAR, year);
            dateParam = cal.getTime();
         
            routeStopLines = CallCenterServices.getRouteStopReport(dateParam, wave, route, stop1, stop2);
    }
if ("POST".equals(request.getMethod()) && "yes".equalsIgnoreCase(request.getParameter("xPortVS")) && routeStopLines.size() > 0) { 
            response.addHeader ("Content-Disposition","attachment;filename=route_stop.txt");
            response.setContentType("application/Text"); 
            PrintWriter pw = response.getWriter();
            pw.write("Name,Number\r\n");
            for(Iterator itr = routeStopLines.iterator();itr.hasNext();) {
                    RouteStopReportLine routeStopLine  = (RouteStopReportLine)itr.next();
                    String unFmtPhone = JspMethods.removeChars(routeStopLine.getPhoneNumber(),"() -");
                    if (unFmtPhone==null || unFmtPhone.length() <10) unFmtPhone="";
                    pw.write(routeStopLine.getFirstName()+" "+routeStopLine.getLastName()+",");
                    pw.write(unFmtPhone+"\r\n");
            }

} else if ("POST".equals(request.getMethod()) && "yes".equalsIgnoreCase(request.getParameter("xPortSP")) && routeStopLines.size() > 0) { 
            response.addHeader ("Content-Disposition","attachment;filename=route_stop.txt");
            response.setContentType("application/Text"); 
            PrintWriter pw = response.getWriter();
            pw.write("Email,EmailType,FirstName\r\n");
            for(Iterator itr = routeStopLines.iterator();itr.hasNext();) {
                    RouteStopReportLine routeStopLine  = (RouteStopReportLine)itr.next();
                   	if (routeStopLine.getEmail() == null || "".equals(routeStopLine.getEmail())) continue;
                    String firstName = routeStopLine.getFirstName();
                    if (firstName == null || "".equals(firstName)) {
                    	firstName = "FreshDirect Customer";
                    }
                    pw.write(routeStopLine.getEmail()+"\t"+routeStopLine.getEmailFormatType()+"\t"+firstName+"\r\n");
            }

} else {
%>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Reports > Orders by Route & Stop</tmpl:put>

<tmpl:put name='content' direct='true'>
<jsp:include page="/includes/reports_nav.jsp" />
<div class="sub_nav">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="sub_nav_text">
<form name="routestop_report" method="post">
 <input type="hidden" name="xPortVS" value='no'>
 <input type="hidden" name="xPortSP" value='no'>
<script language="JavaScript">
    function checkForm(thisForm) {
        var okToSubmit= true;
        var day = thisForm.day.value;
        var month = thisForm.month.value;
        var year = thisForm.year.value;
        var wave = thisForm.wave.value;
        var route = thisForm.route.value;
        var stop1 = thisForm.stop1.value;
        var stop2 = thisForm.stop2.value;
        document.routestop_report.xPortVS.value='no';
        
        
        if (day.length<2) day="0"+day;
        if (month.length<2) month="0"+month;
        var date1=year+month+day;
        
        if (date1.length!=8 ) {
            alert('The DATE field is invalid. Please correct and try again.');
            okToSubmit = false;
        }
        
        if(isNaN(wave)) {
            alert('The WAVE field is invalid. Please correct and try again.');
            okToSubmit = false;
        }

        if(isNaN(route)) {
            alert('The ROUTE field is invalid. Please correct and try again.');
            okToSubmit = false;
        }
        
        if(isNaN(stop1) || isNaN(stop2)) {
            alert('The STOP field is invalid. Please correct and try again.');
            okToSubmit = false;
        }
        
        if ((route.length==0 || route==null) && ((stop1.length>0 && stop1!=null) || (stop2.length>0 && stop2!=null))) {
            alert('ROUTE field is required with STOP. Please correct and try again.');
            okToSubmit = false;
        }
        
        if (!isNaN(stop1) && !isNaN(stop2)) {
            if (parseFloat(stop1) > parseFloat(stop2)) {
                alert('Minimum range of STOP field cannot be larger than maximum range. Please correct and try again.');
                okToSubmit = false;
            }
        }
        
        if (okToSubmit) {
            thisForm.submit();
        }
    }
    
    function setXportSPOn() {
        document.routestop_report.xPortSP.value='yes';
        document.routestop_report.submit();
    }

    function setXportVSOn() {
        document.routestop_report.xPortVS.value='yes';
        document.routestop_report.submit();
    }
</script>
    <tr>
        <td width="15%"><span class="sub_nav_title">Orders by Route & Stop <% if (routeStopLines!= null && routeStopLines.size()>0) {%>( <span class="result"><%= routeStopLines.size() %></span> ) <span class="note" style="font-weight:normal;"><input type="checkbox" name="forPrint" onClick="javascript:toggleScroll('result','list_content','content_fixed');"> Print View</span><% } %></span></td>
        <td width="55%" align="center">
            Date* 
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
            &nbsp;
            Wave <input type="text" name="wave" size="6" maxlength="6" class="text" value="<%=request.getParameter("wave")%>">
            &nbsp;
            Route <input type="text" name="route" size="6" maxlength="6" class="text" value="<%=request.getParameter("route")%>">
            &nbsp;
            Stop <input type="text" name="stop1" size="5" maxlength="5" class="text" value="<%=request.getParameter("stop1")%>"> to <input type="text" name="stop2" size="5" maxlength="5" class="text" value="<%=request.getParameter("stop2")%>">

            <input type="submit" class="submit" onClick="javascript:checkForm(routestop_report); return false;" value="GO"> 
        </td>
        <td width="30%" align="left"><a href="javascript:setXportSPOn();">Exp. to SilverPop fmt</a> | <a href="javascript:setXportVSOn();">Exp. to VoiceShot fmt</a> | <a href="/reports/index.jsp">All Reports >></a></td>
    </tr>
	<script language"javascript">
	<!--
	function toggleScroll(divId,currentClass,newClass) {
	var divStyle = document.getElementById(divId);
		if (document.routestop_report.forPrint.checked) {
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
    if (routeStopLines.size() > 0) {    %>
<div class="list_header">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_header_text">
    <tr>
        <td width="10%">&nbsp;Order #</td>
        <td width="25%">Customer Name</td>
        <td width="10%">Dlv Phone #</td>
        <td width="25%">Email</td>
        <td width="10%">Wave</td>
        <td width="10%">Route</td>
        <td width="10%">Stop</td>
        <td><img src="/media_stat/crm/images/clear.gif" width="12" height="1"></td>
    </tr>
</table>
</div>
<div class="list_content" id="result">
    <table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_content_text">
<logic:iterate id="routeStopLine" collection="<%= routeStopLines %>" type="com.freshdirect.fdstore.customer.RouteStopReportLine" indexId="counter">
        <tr>
            <td width="10%" class="border_bottom"><a href="/main/order_details.jsp?orderId=<%=routeStopLine.getOrderNumber()%>"><%=routeStopLine.getOrderNumber()%></a>&nbsp;</td>
            <td width="25%" class="border_bottom"><%=routeStopLine.getFirstName()%> <%=routeStopLine.getLastName()%>&nbsp;</td>
            <td width="10%" class="border_bottom"><%=routeStopLine.getPhoneNumber()%>&nbsp;</td>
            <td width="25%" class="border_bottom"><%=routeStopLine.getEmail()%>&nbsp;</td>
            <td width="10%" class="border_bottom"><%=routeStopLine.getWaveNumber()!=null? routeStopLine.getWaveNumber():"&nbsp;"%></td>
            <td width="10%" class="border_bottom"><%=routeStopLine.getTruckNumber()!=null? routeStopLine.getTruckNumber():"&nbsp;"%></td>
            <td width="10%" class="border_bottom"><%=routeStopLine.getStopSequence()!=null ? routeStopLine.getStopSequence() : "&nbsp;"%></td>
        </tr>
    </logic:iterate>
    </table>
</div>
<%      } else { %>
    <div class="content_fixed" align="center"><br><b>No Deliveries for <%= CCFormatter.formatDate(dateParam) %></b><br><br></div>
<%      }   %>
<%  }   %>

</tmpl:put>

</tmpl:insert>

<% }
//*** IN order to supress the extra blank lines in the export, we had to move these page import down, which is OK as far as he JSP compiler is concerned
%><%@ page import="java.util.*,java.text.*,java.io.*" %><%@ page import="com.freshdirect.fdstore.customer.*,com.freshdirect.fdstore.*,com.freshdirect.webapp.util.*" %>
<%! DateFormatSymbols symbols = new DateFormatSymbols(); %>