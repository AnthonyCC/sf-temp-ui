<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.StringWriter"%>
<%@page import="java.beans.XMLEncoder"%>

<%@page import="com.freshdirect.fdstore.*"%>
<%@page import="com.freshdirect.erp.*"%>
<%@page import="com.freshdirect.erp.model.*"%>
<%@page import="com.freshdirect.common.pricing.*"%>
<%@page import="com.freshdirect.storeapi.content.ContentFactory"%>
<%@page import="java.util.*"%>

<%
    StringBuilder message = new StringBuilder();
    FDProduct product = null;
    FDProductInfo productInfo = null;
    FDMaterial material = null;
    
    String skuCode = request.getParameter("sku");
    Integer version = request.getParameter("version") != null && request.getParameter("version").trim().length() > 0 ? Integer.parseInt(request.getParameter("version").trim()) : null;
    
    if (skuCode != null) {
        skuCode = skuCode.trim();
        try {
            productInfo = FDCachedFactory.getProductInfo(skuCode);
        } catch (FDSkuNotFoundException e) {
            message.append("<ul><li>SKU not found :<i>" + skuCode + "</i></li></ul>");
        }
    }

    if ((skuCode != null) && (version == null) && (productInfo != null)) {
        version = productInfo.getVersion();
    }
    FDSku sku = skuCode != null && version != null ? new FDSku(skuCode, version) : null;

    if (sku != null) {
         try {
             product = FDCachedFactory.getProduct(sku);
             material = product.getMaterial();
         } catch (FDSkuNotFoundException e) {
             message.append("<ul><li>SKU not found :<i>" + sku.getSkuCode() + "/" + sku.getVersion() + "</i></li></ul>");
         }
     }
%>
<html lang="en-US" xml:lang="en-US">
<head>
	<style>
		<style type="text/css">
		body, html  { height: 100%; }
		html, body, div, span, applet, object, iframe,
		/*h1, h2, h3, h4, h5, h6,*/ p, blockquote, pre,
		a, abbr, acronym, address, big, cite, code,
		del, dfn, em, font, img, ins, kbd, q, s, samp,
		small, strike, strong, sub, sup, tt, var,
		b, u, i, center,
		dl, dt, dd, ol, ul, li,
		fieldset, form, label, legend,
		table, caption, tbody, tfoot, thead, tr, th, td {
			margin: 0;
			padding: 0;
			border: 0;
			outline: 0;
			font-size: 100%;
			vertical-align: baseline;
			background: transparent;
		}
		body { line-height: 1; }
		ol, ul { list-style: none; }
		blockquote, q { quotes: none; }
		blockquote:before, blockquote:after, q:before, q:after { content: ''; content: none; }
		:focus { outline: 0; }
		del { text-decoration: line-through; }
		table {border-spacing: 0; } /* IMPORTANT, I REMOVED border-collapse: collapse; FROM THIS LINE IN ORDER TO MAKE THE OUTER BORDER RADIUS WORK */
		
		/*------------------------------------------------------------------ */
		
		/*This is not important*/
		body{
			font-family:Arial, Helvetica, sans-serif;
			margin:0 auto;
			width:100%;
		}
		a:link {
			color: #666;
			font-weight: bold;
			text-decoration:none;
		}
		a:visited {
			color: #666;
			font-weight:bold;
			text-decoration:none;
		}
		a:active,
		a:hover {
			color: #bd5a35;
			text-decoration:underline;
		}
		
		
		/*
		Table Style - This is what you want
		------------------------------------------------------------------ */
		table a:link {
			color: #666;
			font-weight: bold;
			text-decoration:none;
		}
		table a:visited {
			color: #999999;
			font-weight:bold;
			text-decoration:none;
		}
		table a:active,
		table a:hover {
			color: #bd5a35;
			text-decoration:underline;
		}
		table {
			font-family:Arial, Helvetica, sans-serif;
			color:#666;
			font-size:12px;
			text-shadow: 1px 1px 0px #fff;
			background:#eaebec;
			margin:20px;
			border:#ccc 1px solid;
		
			-moz-border-radius:3px;
			-webkit-border-radius:3px;
			border-radius:3px;
		
			-moz-box-shadow: 0 1px 2px #d1d1d1;
			-webkit-box-shadow: 0 1px 2px #d1d1d1;
			box-shadow: 0 1px 2px #d1d1d1;
		}
		table caption {
			font-size:16px;
			text-shadow: 1px 1px 0px #fff;
			font-weight: bold;
			padding:5px;
			color: #333;
		}
		table th {
			padding:11px 15px 12px 15px;
			border-top:1px solid #fafafa;
			border-bottom:1px solid #e0e0e0;
		
			background: #ededed;
			background: -webkit-gradient(linear, left top, left bottom, from(#ededed), to(#ebebeb));
			background: -moz-linear-gradient(top,  #ededed,  #ebebeb);
		}
		table th:first-child{
			text-align: left;
			padding-left:20px;
		}
		table tr:first-child th:first-child{
			-moz-border-radius-topleft:3px;
			-webkit-border-top-left-radius:3px;
			border-top-left-radius:3px;
		}
		table tr:first-child th:last-child{
			-moz-border-radius-topright:3px;
			-webkit-border-top-right-radius:3px;
			border-top-right-radius:3px;
		}
		table tr{
			text-align: center;
			padding-left:20px;
		}
		table tr td:first-child{
			text-align: left;
			padding-left:10px;
			border-left: 0;
		}
		table tr td {
			padding:8px;
			border-top: 1px solid #ffffff;
			border-bottom:1px solid #e0e0e0;
			border-left: 1px solid #e0e0e0;
			
			background: #fafafa;
			background: -webkit-gradient(linear, left top, left bottom, from(#fbfbfb), to(#fafafa));
			background: -moz-linear-gradient(top,  #fbfbfb,  #fafafa);
		}
		table tr.even td{
			background: #f6f6f6;
			background: -webkit-gradient(linear, left top, left bottom, from(#f8f8f8), to(#f6f6f6));
			background: -moz-linear-gradient(top,  #f8f8f8,  #f6f6f6);
		}
		table tr:last-child td{
			border-bottom:0;
		}
		table tr:last-child td:first-child{
			-moz-border-radius-bottomleft:3px;
			-webkit-border-bottom-left-radius:3px;
			border-bottom-left-radius:3px;
		}
		table tr:last-child td:last-child{
			-moz-border-radius-bottomright:3px;
			-webkit-border-bottom-right-radius:3px;
			border-bottom-right-radius:3px;
		}
		table tr:hover td{
			background: #f2f2f2;
			background: -webkit-gradient(linear, left top, left bottom, from(#f2f2f2), to(#f0f0f0));
			background: -moz-linear-gradient(top,  #f2f2f2,  #f0f0f0);	
		}
		
		form label {
			font-size:14px;
			font-weight: bold;
			font-family:Arial, Helvetica, sans-serif;
		}

</style>

<title>Product Information</title>
</head>
<body>	

<h2 align="center">Product Information</h2>
	<form method="get">
		<div align="center">
			<label for="sku">SKU Code:</label> <input id="sku" type="text" name="sku" maxlength="50" value="<%=skuCode != null ? skuCode : ""%>" />&nbsp;&nbsp;&nbsp;
			<label for="version">Version:</label> <input id="version" type="text" name="version" maxlength="10" value="<%=version != null ? version.toString() : ""%>" />&nbsp;&nbsp;&nbsp; 
			<input type="submit" value="Check" />
		</div>	
		
	</form>
	</br>
	<% if(productInfo != null) { %>
		<% out.println("<div style=\"display: inline-flex;\">"); %>	
			<%@include file="i_materialcore.jspf" %>
			<%@include file="i_materialplant.jspf" %>	
		<% out.println("</div>"); %>
		
		<% out.println("<div style=\"display: inline-flex;\">"); %>	
			<%@include file="i_materialsalesarea.jspf" %>
			<%@include file="i_inventory.jspf" %>	
		<% out.println("</div>"); %>
		
		<% out.println("<div style=\"display: inline-flex;\">"); %>
			<%@include file="i_materialsalesunit.jspf" %>
			<%@include file="i_variation.jspf" %>						
		<% out.println("</div>"); %>
					
		<%@include file="i_materialprice.jspf" %>
		
	<% } else { %>
		<div class="error"><%= message.length() > 0 ? message : "" %></div>
	<% } %>	   
</body>
</html>