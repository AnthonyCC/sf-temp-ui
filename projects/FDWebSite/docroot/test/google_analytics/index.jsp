<!DOCTYPE html>
<%@ page import="com.freshdirect.dataloader.analytics.GoogleAnalyticsReportingService" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager" %>
<%@ page import="com.freshdirect.fdstore.customer.FDOrderI" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="com.freshdirect.framework.util.log.LoggerFactory" %>
<%@ page import="org.apache.http.util.EntityUtils" %>
<%@ page import="org.apache.http.HttpResponse" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>

<html lang="en-US" xml:lang="en-US">
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
	        HttpResponse httpResponse = GoogleAnalyticsReportingService.defaultService().postGAReporting(fdOrder);
	        LOGGER.debug("Order# :" + saleId + " parameters: " + EntityUtils.toString(GoogleAnalyticsReportingService.defaultService().assembleTransactionPayloadForGA(fdOrder)));
%>
	        Order data sent to GA for order#: <%=saleId%>
	        <c:if test="${not empty cookie['developer']}">
      			<pre>
  					<%= "Order# :" + saleId %>
      			</pre>
      			<pre>
  					<%= "post url: " +  GoogleAnalyticsReportingService.defaultService().GOOGLE_ANALYTICS_HOST %>
      			</pre>
      			<pre>
  					<%= "User-Agent: " +  GoogleAnalyticsReportingService.defaultService().USER_AGENT %>
      			</pre>
      			<pre>
  					<%= "parameters: " + EntityUtils.toString(GoogleAnalyticsReportingService.defaultService().assembleTransactionPayloadForGA(fdOrder)) %>
      			</pre>
      			<pre>
  					<%= "http response: " + httpResponse %>
      			</pre>
    		</c:if>
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