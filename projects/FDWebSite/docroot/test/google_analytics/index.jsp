<!DOCTYPE html>
<%@ page import="com.freshdirect.dataloader.analytics.GoogleAnalyticsReportingService" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager" %>
<%@ page import="com.freshdirect.fdstore.customer.FDOrderI" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.freshdirect.framework.util.log.LoggerFactory" %>
<%@ page import="org.apache.http.util.EntityUtils" %>

<html>
	<body>
		<form method="get" action="/test/google_analytics/index.jsp">
    		Enter Sale ID: <input type="text" name="saleId" value=""/>
    		<button>Submit</button>
		</form>		

<%
	final Logger LOGGER = LoggerFactory.getInstance("index.jsp");
	String saleId = request.getParameter("saleId");

	if (null != saleId) {
		
	    try {
		    FDOrderI fdOrder = FDCustomerManager.getOrder(saleId);
	        GoogleAnalyticsReportingService.defaultService().postGAReporting(fdOrder);
	        LOGGER.debug("Order# :" + saleId + " parameters: " + EntityUtils.toString(GoogleAnalyticsReportingService.defaultService().assembleTransactionPayloadForGA(fdOrder)));
%>
	        Order data sent to GA for order#: <%=saleId%> 
<%
	    } catch (Exception e) {
	        LOGGER.error("Unexpected Exception in GoogleAnalyticsReportingService while reported to GA, for order#: "+ saleId, e);
%>
	        Unexpected Exception in GoogleAnalyticsReportingService while reported to GA, for order#: <%=saleId%> 
<%
	    }
	   
	}
%>
	</body>
</html>