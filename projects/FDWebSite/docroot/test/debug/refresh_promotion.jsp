<%@ page import='com.freshdirect.fdstore.promotion.PromotionFactory' %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ taglib uri='freshdirect' prefix='fd' %>




<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en-US" xml:lang="en-US" xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">

		<title>Refresh Promotion</title>
		
</head>

<body>

<%
    String promoId = request.getParameter("promoCode");
    PromotionFactory.getInstance().forceRefresh(promoId);
%>
<%= promoId %> Refreshed Successfully.

</body>
</html>