<%@page import='com.freshdirect.smartstore.fdstore.ProductStatisticsProvider'%>
<%@page import="com.freshdirect.smartstore.fdstore.ScoreProvider"%>
<%@ page import="com.freshdirect.cms.core.domain.ContentKey" %>
<%@ page import="com.freshdirect.cms.core.domain.ContentKeyFactory" %>
<%@ page import="com.freshdirect.cms.core.domain.ContentType" %>
<%@page import='com.freshdirect.storeapi.fdstore.FDContentTypes'%>

<%
String prodId = request.getParameter("prodId");
String erpId = request.getParameter("erpId");
if (prodId == null) {
%>

<form>
ProductId: <input type="text" name="prodId"><br/>
CustomerId: <input type="text" name="erpId"></br>
<input type="submit">
</form>
<%
} else {
   
   ContentKey pkey = ContentKeyFactory.get(FDContentTypes.PRODUCT, prodId);
   Float f = (Float)ScoreProvider.getInstance().getUserProductScore(erpId,pkey);
   float cScore = f == null ? -1 : f.floatValue();
   float gScore = ProductStatisticsProvider.getInstance().getGlobalProductScore(pkey);
%>
   Customer score: <%= cScore %> (for <%=erpId%> and <%=prodId%>)<br/>
   Global score: <%= gScore %> (for <%=prodId%>)
<%
}
%>
