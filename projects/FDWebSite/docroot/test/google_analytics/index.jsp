<!DOCTYPE html>
<%@ page import="com.freshdirect.dataloader.analytics.GoogleAnalyticsReportingService" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager" %>
<%@ page import="com.freshdirect.fdstore.customer.FDOrderI" %>

<html>
	<body>
		<form method="get" action="/test/google_analytics/index.jsp">
    		Enter Sale ID: <input type="text" name="saleId" value=""/>
    		<button>Submit</button>
		</form>		

<%
	String saleId = request.getParameter("saleId");

	if (!(null == saleId)) {
	    FDOrderI fdOrder = FDCustomerManager.getOrder(saleId);
		
	    try {
	        GoogleAnalyticsReportingService.defaultService().postGAReporting(fdOrder);
%>
	        Order data sent to GA for order#: <%=saleId%> 
<%
	    } catch (Exception e) {
%>
	        Unexpected Exception in GoogleAnalyticsReportingService while reported to GA, for order#: <%=saleId%> 
<%
	    }
	   
	}
%>
	</body>
</html>