<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.BrowserInfo"%>
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
</head>
<body>
<div><span class="enabled2">Client info:</span><span id="ua_div"></span></div>
<div><span class="enabled2">BrowserInfo:</span><%= new BrowserInfo(request) %></div>

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


<table style="text-align: center;">
	<tr>
		<td>
<h2>Transparent JPG</h2>
<fd:TransparentBox disabled="false">
	<img src="/media_stat/images/thanksgiving/home_turkey.jpg">
	<div>Lorem ipsum</div>
</fd:TransparentBox>
		</td>


		<td>
<h2>Normal JPG</h2>
<fd:TransparentBox disabled="true">
	<img src="/media_stat/images/thanksgiving/home_turkey.jpg">
	<div>Lorem ipsum</div>
</fd:TransparentBox>
		</td>
	</tr>
	
	<tr>
		<td>
<h2>Transparent GIF</h2>
<fd:TransparentBox disabled="false">
	<img src="/media_stat/images/navigation/department/home/trialoffer.gif">
	<div>Lorem ipsum</div>
</fd:TransparentBox>
		</td>
		<td>
<h2>Normal GIF</h2>
<fd:TransparentBox disabled="true">
	<img src="/media_stat/images/navigation/department/home/trialoffer.gif">
	<div>Lorem ipsum</div>
</fd:TransparentBox>
		</td>
	</tr>
		
	<tr>
		<td>
<h2>Transparent JPG</h2>
<fd:TransparentBox disabled="false">
	<img src="/media/images/product/frozen/fro_mornin_harvest_01_c.jpg">
	<div>Lorem ipsum</div>
</fd:TransparentBox>
		</td>
		<td>
<h2>Normal JPG</h2>
<fd:TransparentBox disabled="true">
	<img src="/media/images/product/frozen/fro_mornin_harvest_01_c.jpg">
	<div>Lorem ipsum</div>
</fd:TransparentBox>
		</td>
	</tr>
			
</table>

<script type="text/javascript">
document.getElementById('ua_div').innerHTML = navigator.userAgent;
</script>
</body>
</html>
