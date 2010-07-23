<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.freshdirect.webapp.taglib.fdstore.BrowserInfo"%>
<%@ page import="com.freshdirect.fdstore.content.ProductModel" %>
<%@ page import="com.freshdirect.fdstore.content.ContentFactory" %>

<%@ taglib uri='freshdirect' prefix='fd'%>
<%@ taglib uri="/WEB-INF/shared/tld/fd-display.tld" prefix='display' %>

<%
BrowserInfo bi = new BrowserInfo(request);
%><html>
<head>
	<title>Transparent Box Test</title>
	<style type="text/css">
	.enabled {
		color: black;
		font-weight: bold;
	}
	.enabled2 {
		color: black;
		font-weight: bold;
		padding-right: 1em;
	}
	.disabled {
		color: gray;
	}
	.disabled2 {
		color: gray;
		text-decoration: line-through;
		padding-right: 1em;
	}
	</style>
	<script type="text/javascript" src="/assets/javascript/common_javascript.js"></script>
<%
if (request.getHeader("User-Agent").indexOf("Mac") > -1) {
%>	<link rel="stylesheet" href="/assets/css/mac_ie.css" type="text/css"><%
} else {
%>	<link rel="stylesheet" href="/assets/css/pc_ie.css" type="text/css"><%
}
%>
	<link href="/assets/css/wine.css" rel="stylesheet" type="text/css">
</head>
<body>
<%
	ProductModel prd = ContentFactory.getInstance().getProduct("apl_apl", "apl_breaburn");

	
%>

<table style="border: 1px dotted #ccc" cellpadding="3">
	<tr>
		<th>Family</th>
		<th>Browser</th>
	</tr>
	<tr>
		<td class="<%= bi.isInternetExplorer() ? "enabled" : "disabled" %>">Internet Explorer:</td>
		<% if (bi.isInternetExplorer()) { %>
		<td>
			<span class="<%= bi.isIE6() ? "enabled2" : "disabled2" %>">IE6 or older</span>
			<span class="<%= !bi.isIE6() ? "enabled2" : "disabled2" %>">Newer</span>
		</td>
		<% } else { %>
		<td>&nbsp;</td>
		<% } %>
	</tr>
	<tr>
		<td class="<%= bi.isFirefox() ? "enabled" : "disabled" %>">Mozilla Firefox:</td>
		<td><%= bi.isFirefox() ? bi.getVersion() : "&nbsp;" %></td>
	</tr>
	<tr>
		<td class="<%= bi.isWebKit() ? "enabled" : "disabled" %>">WebKit:</td>
		<td>
			<span class="<%= bi.isSafari() ? "enabled2" : "disabled2" %>">Safari</span>
			<span class="<%= bi.isChrome() ? "enabled2" : "disabled2" %>">Chrome</span>
			<span class="<%= bi.isIPhone() ? "enabled2" : "disabled2" %>">iPhone</span>
			<span class="<%= bi.isAndroid() ? "enabled2" : "disabled2" %>">Android</span>
		</td>
	</tr>
	<tr>
		<td class="<%= bi.isOpera() ? "enabled" : "disabled" %>">Opera:</td>
		<td><%= bi.isOpera() ? bi.getVersion() : "&nbsp;" %></td>
	</tr>
</table>
<br>
<br>
<br>
<table>
	<tr>
		<td>&nbsp;</td>
		<td style="font-size: 9pt; text-align: center">20%</td>
		<td style="font-size: 9pt; text-align: center">In CART</td>
	</tr>
	<tr>
		<td style="font-size: 9pt">Opacity: 1</td>
		<td><display:ProductImage product="<%= prd %>" browserInfo="<%= bi %>" savingsPercentage="<%= 0.2 %>"/></td>
		<td><display:ProductImage product="<%= prd %>" browserInfo="<%= bi %>" savingsPercentage="<%= 0.2 %>" inCart="<%= true %>"/></td>
	</tr>
	<tr>
		<td style="font-size: 9pt">Opacity: 0.5</td>
		<td><display:ProductImage product="<%= prd %>" browserInfo="<%= bi %>" savingsPercentage="<%= 0.2 %>" opacity="<%= 0.5 %>"/></td>
		<td><display:ProductImage product="<%= prd %>" browserInfo="<%= bi %>" savingsPercentage="<%= 0.2 %>" inCart="<%= true %>" opacity="<%= 0.5 %>"/></td>
	</tr>
</table>

</body>
</html>
