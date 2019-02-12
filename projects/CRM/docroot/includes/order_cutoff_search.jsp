<%@ page import="java.util.Calendar" %>
<%@ page import="java.text.DateFormatSymbols" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCutoffTimeInfo" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>

<%! DateFormatSymbols symbols = new DateFormatSymbols();    %>
<%
    Calendar today = Calendar.getInstance();
    int month = today.get(Calendar.MONTH);
    int date  = today.get(Calendar.DATE);
    int year  = today.get(Calendar.YEAR);
    int curryear  = today.get(Calendar.YEAR);
%>
<script>
<!--
    function changeAction(myForm, target){
        myForm.action=target;
        myForm.submit();
    }
-->
</script>
<%String actionName = ""; 
if ("post".equalsIgnoreCase(request.getMethod()) && request.getParameter("cutoffSubmit") != null) {
	actionName = "cutoffReport";
} %>
