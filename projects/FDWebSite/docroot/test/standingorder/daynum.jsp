<%@page import='java.util.*' %>
<%@page import="java.text.DateFormat"%>
<%@page import="com.freshdirect.framework.util.DateUtil"%>
<%@page import="java.text.SimpleDateFormat"%>


<%
String dfStr = "yyyy-MM-dd hh:mm:ss";
DateFormat df = new SimpleDateFormat(dfStr);	

String date = request.getParameter("date");
%>


<%
if (date!=null){%>
Day number from 1970-01-05 is <%=DateUtil.getDayNumFromEpochFirstMonday(df.parse(date))%>
<br><br>
<%}%>

<form action="/test/standingorder/daynum.jsp">
	Test date (<%=dfStr%>): <input name="date" value="<%=date ==null ? df.format(new Date()) :  date%>">
	<input type="submit" value="Submit">
</form>