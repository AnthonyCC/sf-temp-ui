<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>


<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Reports > Cases by Queues & Subjects</tmpl:put>

<tmpl:put name='content' direct='true'>
<%! DateFormatSymbols symbols = new DateFormatSymbols();    %>
<%
    Calendar today = Calendar.getInstance();
    int currmonth = today.get(Calendar.MONTH);
    int currday  = today.get(Calendar.DATE);
    int curryear  = today.get(Calendar.YEAR);
    
    today.add(Calendar.DATE,1);

    int currmonth2 = today.get(Calendar.MONTH);
    int currday2  = today.get(Calendar.DATE);
    int curryear2  = today.get(Calendar.YEAR);
    
    
    int month = request.getParameter("month") != null ? Integer.parseInt(request.getParameter("month")) : currmonth;
    int day   = request.getParameter("day") != null ? Integer.parseInt(request.getParameter("day")) : currday;
    int year  = request.getParameter("year") != null ? Integer.parseInt(request.getParameter("year")) : curryear;
    
    int hour = request.getParameter("hour") != null ? Integer.parseInt(request.getParameter("hour")) : 7;
    int minute   = request.getParameter("minute") != null ? Integer.parseInt(request.getParameter("minute")) : 0;
    String ampm  = request.getParameter("ampm") != null ? request.getParameter("ampm") : "am";

    
    int month2 = request.getParameter("month2") != null ? Integer.parseInt(request.getParameter("month2")) : currmonth2;
    int day2   = request.getParameter("day2") != null ? Integer.parseInt(request.getParameter("day2")) : currday2;
    int year2  = request.getParameter("year2") != null ? Integer.parseInt(request.getParameter("year2")) : curryear2;

    int hour2 = request.getParameter("hour2") != null ? Integer.parseInt(request.getParameter("hour2")) : 1;
    int minute2   = request.getParameter("minute2") != null ? Integer.parseInt(request.getParameter("minute2")) : 0;
    String ampm2  = request.getParameter("ampm2") != null ? request.getParameter("ampm2") : "am";
%>

<%
List subjectLines = null;
Calendar cal = Calendar.getInstance();
Calendar cal2 = Calendar.getInstance();
Date dateParam = null;
Date dateParam2 = null;
boolean showAutoCases = true;
boolean posted = false;

if ("POST".equals(request.getMethod())) {
	posted = true;
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        
        cal.set(Calendar.HOUR,hour);
        cal.set(Calendar.MINUTE,minute);
        cal.set(Calendar.AM_PM,"pm".equalsIgnoreCase(ampm)?Calendar.PM:Calendar.AM);
        
        cal2.set(Calendar.DAY_OF_MONTH, day2);
        cal2.set(Calendar.MONTH, month2);
        cal2.set(Calendar.YEAR, year2);
        
        cal2.set(Calendar.HOUR,hour2);
        cal2.set(Calendar.MINUTE,minute2);
        cal2.set(Calendar.AM_PM,"pm".equalsIgnoreCase(ampm2)?Calendar.PM:Calendar.AM);
        
        if (cal.after(cal2) ){
            cal2 = cal;
        }
        dateParam = cal.getTime();
        dateParam2 = cal2.getTime();
        showAutoCases = "true".equals(request.getParameter("showAutoCases")) ? true : false ;
        subjectLines = CallCenterServices.getSubjectReport(dateParam, dateParam2, showAutoCases);
}
%>
<jsp:include page="/includes/reports_nav.jsp" />
<div class="sub_nav">
<table width="100%" cellpadding="0" cellspacing="0" border="0" class="sub_nav_text">
<form name="subject_report" method="post" action="subject_report.jsp">
<script language="JavaScript">
    function checkForm(thisForm) {
        var okToSubmit= true;
        var day = thisForm.day.value;
        var month = thisForm.month.value;
        var year = thisForm.year.value;
        
        var hour = thisForm.hour.value;
        var minute = thisForm.minute.value;
        var ampm = thisForm.ampm.value;
        
        var hour2 = thisForm.hour2.value;
        var minute2 = thisForm.minute2.value;
        var ampm2 = thisForm.ampm2.value;
        
        
        var day2 = thisForm.day2.value;
        var month2 = thisForm.month2.value;
        var year2 = thisForm.year2.value;

        var time1;
        var time2;
        
        //make date 2 the same as date 1 if the To date is not specified
        if((day2.length+month2.length+year2.length)==0){
            day2 = day;
            month2 = month;
            year2 = year;
            thisForm.day2.value = day2;
            thisForm.month2.value = month2;
            thisForm.year2.value = year2;
        }
        
        if (day.length<2) day="0"+day;
        if (day2.length<2) day2="0"+day2;
        if (month.length<2) month="0"+month;
        if (month2.length<2) month2="0"+month2;
       
        if (ampm=='pm' && hour!='12') {
            hour=(hour*1)+12;
        } else if (hour=='12' && ampm=='am') hour='0';
        time1=(hour.length<2?"0":'')+hour+(minute.length<2?'0':'')+minute;
          
          
        if (ampm2=='pm' && hour2!='12') {
            hour2=(hour2*1)+12;
        } else if (hour2=='12' && ampm2=='am') hour2='0';
        
        time2=(hour2.length<2?"0":'')+hour2+(minute2.length<2?'0':'')+minute2;
        
        //alert(time1+" / "+time2);
        var date1=year+month+day;
        var date2=year2+month2+day2;
        
        if (date1.length!=8 ) {
            alert('The From date field is invalid. Please correct and try again.');
            okToSubmit = false;
        }
        
        if (date2.length!=8 ) {
            alert('The To date field is invalid. Please correct and try again.');
            okToSubmit = false;
        }
       
        if (okToSubmit && date2<date1) {
            alert("The To date cannot be less than the From date.  Please make the appropriate corrections.");
            okToSubmit = false;
        }
        
        
        if (okToSubmit && date2==date1 && time1>time2) {
            alert("Since the dates are the same, the Start time must be greater than the End time.");
            okToSubmit = false;
        }
        
        if (okToSubmit) {
            thisForm.submit();
        }
    }
</script>

    <tr>
        <td width="25%"><span class="sub_nav_title">Cases by Queues & Subjects <% if (posted && subjectLines.size() > 0) {%><span class="note" style="font-weight: normal;"><input type="checkbox" name="forPrint" onClick="javascript:toggleScroll('result','list_content','content_fixed');"> Print View</span><% } %></td>
        <td width="60%">
            <table align="center">
                <tr>
                    <td align="right">From:&nbsp;</td>
                    <td>
                        <select name="month" required="true" class="pulldown">
                            <option value="">Month</option>
                                        <%  for (int i=0; i<12; i++) {  %>
                                        <option value="<%= i %>" <%= (i==month)?"selected":"" %>><%= symbols.getShortMonths()[i] %></option>
                                        <%  }   %>
                        </select>
                    </td>
                    <td>
                        <select name="day" required="true" class="pulldown">
                            <option value="">Date</option>
                                        <%  for (int i=1; i<=31; i++) { %>
                            <option value="<%= i %>" <%= (i==day)?"selected":"" %>><%= i %></option>
                                        <%  } %>
                        </select>
                    </td>
                    <td>
                        <select name="year" required="true" class="pulldown">
                            <option value="">Year</option>
                                        <%  for (int i=2001; i<2011; i++) { %>
                            <option value="<%= i %>" <%= (i==year)?"selected":"" %>><%= i %></option>
                                        <%  } %>
                        </select>
                    </td>
<!-- start time dropdown 1 -->
					<td align="right">Time:&nbsp;</td>
                    <td> 
                        <select name="hour" required="true" class="pulldown">
                                        <%  for (int i=1; i<13; i++) {  %>
                                        <option value="<%= i %>" <%= (i==hour)?"selected":"" %>><%= i %></option>
                                        <%  }   %>
                        </select>
                    </td>
                    <td>
                        <select name="minute" required="true" class="pulldown">
                                        <%  for (int i=0; i<60; i+=10) { %>
                            <option value="<%= i %>" <%= (i==minute)?"selected":"" %>><%= i<10?"0":"" %><%=i%></option>
                                        <%  } %>
                        </select>
                    </td>
                    <td>
                        <select name="ampm" required="true" class="pulldown">
                            <option value=""> </option>
                            <option value="am" <%= ("am".equalsIgnoreCase(ampm))?"selected":"" %>>AM</option>
                            <option value="pm" <%= ("pm".equalsIgnoreCase(ampm))?"selected":"" %>>PM</option>
                        </select>
                    </td>

<!-- end time dropdown 1 -->
					<td rowspan="2">
                        <input type="checkbox" name="showAutoCases" value='true' <%=showAutoCases ? "CHECKED" : "" %>>Include System Generated Cases
                    </td>
                    <td rowspan="2">
                        <input type="submit" class="submit" onClick="javascript:checkForm(subject_report); return false;" value="GO">
                    </td>
                </tr>
                <tr>
                    <td align="right">To:&nbsp;</td>
                    <td>
                        <select name="month2" required="true" class="pulldown">
                            <option value="">Month</option>
                                        <%  for (int i=0; i<12; i++) {  %>
                                        <option value="<%= i %>" <%= (i==month2)?"selected":"" %>><%= symbols.getShortMonths()[i] %></option>
                                        <%  }   %>
                        </select>
                    </td>
                    <td>
                        <select name="day2" required="true" class="pulldown">
                            <option value="">Date</option>
                                        <%  for (int i=1; i<=31; i++) { %>
                            <option value="<%= i %>" <%= (i==day2)?"selected":"" %>><%= i %></option>
                                        <%  } %>
                        </select>
                    </td>
                    <td>
                        <select name="year2" required="true" class="pulldown">
                            <option value="">Year</option>
                                        <%  for (int i=2001; i<2011; i++) { %>
                            <option value="<%= i %>" <%= (i==year2)?"selected":"" %>><%= i %></option>
                                        <%  } %>
                        </select>
                    </td>
<!-- start time dropdown 2 -->
					<td align="right">Time:&nbsp;</td>
                    <td>
                        <select name="hour2" required="true" class="pulldown">
                                        <%  for (int i=1; i<13; i++) {  %>
                                        <option value="<%= i %>" <%= (i==hour2)?"selected":"" %>><%= i %></option>
                                        <%  }   %>
                        </select>
                    </td>
                    <td>
                        <select name="minute2" required="true" class="pulldown">
                                        <%  for (int i=0; i<60; i+=10) { %>
                            <option value="<%= i %>" <%= (i==minute2)?"selected":"" %>><%= i<10?"0":"" %><%=i%></option>
                                        <%  } %>
                        </select>
                    </td>
                    <td>
                        <select name="ampm2" required="true" class="pulldown">
                            <option value=""> </option>
                            <option value="am" <%= ("am".equalsIgnoreCase(ampm2))?"selected":"" %>>AM</option>
                            <option value="pm" <%= ("pm".equalsIgnoreCase(ampm2))?"selected":"" %>>PM</option>
                        </select>
                    
<!-- end time dropdown 2 -->
                </tr>
            </table>
                
        </td>
        <td width="15%" align="right"><a href="/reports/index.jsp">All Reports >></a></td>
    </tr>
	<script language"javascript">
	<!--
	function toggleScroll(divId,currentClass,newClass) {
	var divStyle = document.getElementById(divId);
		if (document.subject_report.forPrint.checked) {
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
     int subjSize = subjectLines.size();
%>

<%  if (subjSize > 0) { %>
<div class="list_header">
<table width="100%" cellpadding="0" cellspacing="2" border="0" class="list_header_text">
<tr>
        <td width="25%">Queue</td>
        <td width="45%">Subject</td>
        <td width="15%" align="right">Total Cases</td>
        <td width="15%" align="right">% of Total%</td>
    </tr>
</table>
</div>
<div id="result" class="list_content">
    <table width="100%" cellpadding="0" cellspacing="0" border="0" class="list_content_text">
<%
    String lastQueue = null;
    long totQueueCount=0;
    long grandTotal = 0;
    double totalCases=0;
%>
    <logic:iterate id="subjectReportLine" collection="<%= subjectLines %>" type="com.freshdirect.fdstore.customer.SubjectReportLine" indexId="counter">
      <% totalCases += subjectReportLine.getCaseCount(); %>
    </logic:iterate>
    
    <logic:iterate id="subjectReportLine" collection="<%= subjectLines %>" type="com.freshdirect.fdstore.customer.SubjectReportLine" indexId="counter">
<%
        if (lastQueue!=null && !lastQueue.equals(subjectReportLine.getQueueName())) { %>
            <tr valign="top" class="list_odd_row">
              <td width="25%" class="border_bottom">&nbsp;</td>
              <td width="45%" align="right" class="border_bottom">&nbsp;<i><%=lastQueue%> Sum</i></td>
              <td width="15%" align="right" class="border_bottom">&nbsp;<b><%=totQueueCount%></b></td>
              <td width="15%" align="right" class="border_bottom">&nbsp;<b><%=CCFormatter.formatPercentage((totQueueCount/totalCases))%></b></td>
            </tr>
<%
            totQueueCount=0;
        } 
        totQueueCount+=subjectReportLine.getCaseCount();
        grandTotal += subjectReportLine.getCaseCount();
%>
    <tr valign="top">
        <td width="25%" align="left" class="border_bottom"><%= !subjectReportLine.getQueueName().equals(lastQueue) ? "<b>"+subjectReportLine.getQueueName()+"</b>" :"&nbsp;"%></td>
        <td width="45%" align="left" class="border_bottom"><%= subjectReportLine.getSubject() %>&nbsp;</td>
        <td width="15%" align="right" class="border_bottom">&nbsp;<%= subjectReportLine.getCaseCount() %></td>
        <td width="15%" align="right" class="border_bottom">&nbsp;<%= CCFormatter.formatPercentage((subjectReportLine.getCaseCount()/totalCases))%></td>
    </tr>
<%
        lastQueue=subjectReportLine.getQueueName();            
%>
    </logic:iterate>
            <tr valign="top" class="list_odd_row">
              <td class="border_bottom">&nbsp;</td>
              <td class="border_bottom" align="right">&nbsp;<i><%=lastQueue%> Sum</i></td>
              <td class="border_bottom" align="right">&nbsp;<b><%=totQueueCount%></td>
              <td class="border_bottom" align="right">&nbsp;<b><%=CCFormatter.formatPercentage((totQueueCount/totalCases))%></td>
            </tr>
            
              <tr valign="top">
              <td class="border_bottom">&nbsp;</td>
              <td class="border_bottom" align="right">&nbsp;<b>Grand Total</td>
              <td class="border_bottom" align="right">&nbsp;<b><%=grandTotal%></td>
              <td class="border_bottom" align="right">&nbsp;<b><%=CCFormatter.formatPercentage((grandTotal/totalCases))%></td>
            </tr>
    </table>
</div>
<%      } else { %>
    <div class="content_fixed" align="center"><br><br><b>No cases created for <%= CCFormatter.formatDate(dateParam) %><%= !dateParam.equals(dateParam2) ?  " - " + CCFormatter.formatDate(dateParam2):""%></b><br><br><br></div>
<%      }   %>
<%  }   %>

</tmpl:put>

</tmpl:insert>
