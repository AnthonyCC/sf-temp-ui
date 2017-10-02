<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>
<%@page import="com.freshdirect.cms.application.CmsManager"%>
<%@page import="com.freshdirect.cms.fdstore.FDContentTypes"%>
<%@page import="java.util.Set"%>
<%@page import="java.lang.*"%>
<%@page import="com.freshdirect.cms.ContentKey"%>
<%@page import="com.freshdirect.fdstore.content.ContentFactory"%>
<%@page import="com.freshdirect.fdstore.content.ProductModel"%>
<%@page import="com.freshdirect.webapp.util.MediaUtils"%>

<html lang="en-US" xml:lang="en-US">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" lang="en-US">
<title>Available Multi-SKU Products</title>
</head>
<body>
<%
CharSequence keyword = "About our microwave-only, BPA-free container";
Set<ContentKey> keys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.PRODUCT);

for (ContentKey key : keys) {
	ProductModel node = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(key);
  String description = MediaUtils.renderHtmlToString(node.getProductDescription(), null);
	if (description.contains(keyword)) {
    %>
    <div> <%= node %> </div>
    <%
  }
}
%>
<div> End of the list </div>
</body>
</html>
