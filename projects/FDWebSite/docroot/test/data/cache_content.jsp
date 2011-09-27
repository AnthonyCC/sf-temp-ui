<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.StringWriter"%>
<%@page import="java.beans.XMLEncoder"%>

<%@page import="com.freshdirect.fdstore.FDProductInfo"%>
<%@page import="com.freshdirect.fdstore.FDProduct"%>
<%@page import="com.freshdirect.fdstore.FDSku"%>
<%@page import="com.freshdirect.fdstore.FDCachedFactory"%>
<%!
	String format(Object x) {
    	if (x == null) {
    	    return "<b>Not found!</b>";
    	}
        return "<code>" + x.toString().replaceAll("\n", "<br/>") + "</code>";
    }%>
<%
	FDProduct product = null;
	FDProductInfo productInfo = null;

	String skuCode = request.getParameter("sku");
    Integer version = request.getParameter("version") != null && request.getParameter("version").trim().length() > 0 ? Integer.parseInt(request
            .getParameter("version").trim()) : null;
	
    if (skuCode != null) {        
        productInfo = FDCachedFactory.getProductInfo(skuCode);
    }
    
    if ((skuCode != null) && (version == null) && (productInfo != null)) {
        version = productInfo.getVersion();
    }
    FDSku sku = skuCode != null && version != null ? new FDSku(skuCode, version) : null;
            
    if (sku != null) {
        product = FDCachedFactory.getProduct(sku);
    }
    
%>    
<html>
<head>
	<link rel="stylesheet" type="text/css" href="/test/search/config.css" />
	<title>Cache Content</title>
</head>
<body>
 <form method="get" >
 	<label for="sku">SKU code:</label>
 	<input id="sku" type="text" name="sku" maxlength="50" value="<%=skuCode != null ? skuCode : ""%>"/>
 	<br/>
 	<label for="version">Version:</label>
 	<input id="version" type="text" name="version" maxlength="10" value="<%=version != null ? version.toString() : ""%>"/>
 	<br/>
 	<input type="submit" value="Check"/>
 </form>
	<table>
		<tr>
			<td class="even">Sku</td>
			<td class="even"><%=sku%></td>
		</tr>
		<tr>
			<td class="odd">FDProduct in VM</td>
			<td class="odd"><%=format(product)%></td>
		</tr>
		
		<tr>
			<td class="even">FDProductInfo in VM</td>
			<td class="even"><%=format(productInfo)%></td>
		</tr>
		
	</table>
</body>    