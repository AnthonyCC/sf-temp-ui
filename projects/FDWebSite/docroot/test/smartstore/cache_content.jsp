<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.StringWriter"%>
<%@page import="java.beans.XMLEncoder"%>
<%@page import="com.freshdirect.fdstore.FDProductInfoCache"%>
<%@page import="com.freshdirect.fdstore.FDProductInfo"%>
<%@page import="com.freshdirect.fdstore.FDProduct"%>
<%@page import="com.freshdirect.fdstore.FDSku"%>
<%@page import="com.freshdirect.fdstore.FDProductCache"%>
<%!
	String format(Object x) {
    	if (x == null) {
    	    return "<b>Not found!</b>";
    	}
        return "<code>" + x.toString().replaceAll("\n", "<br/>") + "</code>";
    }%>
<%
    String skuCode = request.getParameter("sku");
    Integer version = request.getParameter("version") != null && request.getParameter("version").trim().length() > 0 ? Integer.parseInt(request
            .getParameter("version").trim()) : null;

    if ((skuCode != null) && (version == null)) {
        version = FDProductCache.getInstance().getLastVersion(skuCode);
    }
    FDSku sku = skuCode != null && version != null ? new FDSku(skuCode, version) : null;

    FDProduct extProduct = null;
    FDProduct inProduct = null;
    FDProductInfo extProductInfo = null;
    FDProductInfo inProductInfo = null;
    if (sku != null) {
        extProduct = FDProductCache.getInstance().getFromExternalCache(sku);
        inProduct = FDProductCache.getInstance().getFDProduct(sku);
    }
    if (skuCode != null) {
        extProductInfo = ((FDProductInfoCache) FDProductInfoCache.getInstance()).getFromExternalCache(skuCode);
        inProductInfo = FDProductInfoCache.getInstance().get(skuCode);
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
			<td class="odd"><%=format(inProduct)%></td>
		</tr>
		<tr>
			<td class="even">FDProduct in Memcache</td>
			<td class="even"><%=format(extProduct)%></td>
		</tr>
		<tr>
			<td class="odd">FDProductInfo in VM</td>
			<td class="odd"><%=format(inProductInfo)%></td>
		</tr>
		<tr>
			<td class="even">FDProductInfo in Memcache</td>
			<td class="even"><%=format(extProductInfo)%></td>
		</tr>
		
	</table>
</body>    