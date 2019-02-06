<%@ page import="java.util.*" %>
<%@ page import='java.text.*' %>

<%@ page import="com.freshdirect.fdstore.customer.FDAuthInfoSearchCriteria" %>
<%@ page import="com.freshdirect.common.customer.EnumCardType" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.customer.EnumSaleType"%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>

<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<%! DateFormatSymbols symbols = new DateFormatSymbols();    %>

<%
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader ("Expires", 0);
%>

<%
    Calendar today = Calendar.getInstance();
    int currmonth = today.get(Calendar.MONTH);
    int currdate  = today.get(Calendar.DATE);
    int curryear  = today.get(Calendar.YEAR);
    
    today.add(Calendar.DATE,1);
    
    int date   = request.getParameter("transDate") != null ? Integer.parseInt(request.getParameter("transDate")) : currdate;
	int month = request.getParameter("transMonth") != null ? Integer.parseInt(request.getParameter("transMonth")) : currmonth;
    int year  = request.getParameter("transYear") != null ? Integer.parseInt(request.getParameter("transYear")) : curryear;
    
%>

<%
List subjectLines = null;
Calendar cal = Calendar.getInstance();
Date dateParam = null;
boolean showAutoCases = true;
boolean posted = false;

if ("POST".equals(request.getMethod())) {
	posted = true;
        cal.set(Calendar.DAY_OF_MONTH, date);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        
        dateParam = cal.getTime();
}
%>
<tmpl:insert template='/template/supervisor_resources.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Orders by Credit Card Info</tmpl:put>

<tmpl:put name='content' direct='true'>

<%@ include file="/includes/i_globalcontext.jspf" %>

<%
FDAuthInfoSearchCriteria criteria = new FDAuthInfoSearchCriteria();
criteria.setCardType(EnumCardType.getCardType(NVL.apply(request.getParameter("cardType"), "").trim()));
String chargedAmount = NVL.apply(request.getParameter("chargedAmount"), "").trim();
criteria.setChargedAmount("".equals(chargedAmount) ? 0.0 : Double.parseDouble(chargedAmount));
criteria.setCCKnownNum(NVL.apply(request.getParameter("ccKnownNum"), ""));
criteria.setTransDate(NVL.apply(request.getParameter("transDate"), ""));
criteria.setTransMonth(NVL.apply(request.getParameter("transMonth"), ""));
criteria.setTransYear(NVL.apply(request.getParameter("transYear"), ""));
%>



</tmpl:put>

</tmpl:insert>
