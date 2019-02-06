<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.common.address.EnumAddressType" %>
<%@ page import="com.freshdirect.logistics.delivery.model.EnumAddressExceptionReason" %>
<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="com.freshdirect.customer.EnumTransactionType" %>
<%@ page import="com.freshdirect.customer.EnumTransactionType" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Reports > Settlement Problems</tmpl:put>

<tmpl:put name='content' direct='true'>
<% 
SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy");
DateFormatSymbols symbols = new DateFormatSymbols();

String sm = request.getParameter("failure_start_month");
String sd = request.getParameter("failure_start_day");  
String sy = request.getParameter("failure_start_year");  
String em = request.getParameter("failure_end_month");
String ed = request.getParameter("failure_end_day");  
String ey = request.getParameter("failure_end_year");  

// seet start date default to 1 week prior --> this speeds up query
Calendar cal = Calendar.getInstance();
if (sm == null || sd == null || sy == null) {	
	// set start date to 7 days prior (1 week)
	cal.setTime(new Date());
	cal.add(Calendar.DATE, -7);
	sd = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
	sm = String.valueOf(cal.get(Calendar.MONTH)+1);
	sy = String.valueOf(cal.get(Calendar.YEAR));	
}
int curryear  = cal.get(Calendar.YEAR);


%>
<jsp:include page="/includes/reports_nav.jsp" />

</tmpl:put>

</tmpl:insert>